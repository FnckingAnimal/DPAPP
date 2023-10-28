package app.dpapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.dpapp.DataTable.DataTable;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.NetUtils;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.zxing.activity.CaptureActivity;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class Carrierscan extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotno;
    private String _staticdefectqty;
    private String _opno;

    private TextView lotnotxt;
    private TextView opnotxt;
    private TextView scandeftb;
    private EditText scantb;
    private TextView defqtytb;
    private LinearLayout scangv;


    String item = "0";
    String lotno = "";
    String opno = "";
    
    private String ipstatic;
    private String HDSerialNo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_carrierscan);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            lotnotxt = (TextView) findViewById(R.id.isfc_carrierscan_lotnotbv1);
            opnotxt = (TextView) findViewById(R.id.isfc_carrierscan_opnotbv1);
            scandeftb = (TextView) findViewById(R.id.isfc_carrierscan_scantv1);
            scantb=(EditText)findViewById(R.id.isfc_carrierscan_scanet1);
            defqtytb=(TextView)findViewById(R.id.isfc_carrierscan_inputtbv1);
            scangv = (LinearLayout) findViewById(R.id.isfc_carrierscan_stationviewll1);
            scangv.removeAllViews();

//            submit = (Button) findViewById(R.id.isfc_defectinput_submitbt);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            

            lotno =  ss[3];
            opno =  ss[4];
            lotnotxt.setText(lotno);
            opnotxt.setText(opno);
            scandeftb.setText(item);
            //scantb.setFocusable(true);

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Carrierscan_Load();
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");


            scantb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    try {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    scantb_KeyPress(13);
                                }
                            }, "Carrierscan  Scan Task");
                        }
                    }
                    catch(Exception ex)
                    {
                        BaseFuncation.showmessage(Carrierscan.this,"Carrierscan  Scan Task Error:"+ex.getMessage());
                    }
                    return true;
                }
            });

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    private void Carrierscan_Load() throws Exception {
        DataTable dt =
                cwa.CallRDT("getcarrierdata", lotno);
        if (dt != null) {
            if (dt.Rowscount() > 0) {
                setText(defqtytb, String.valueOf(dt.Rowscount()));
            } else {
                setText(defqtytb,"");
                SFCStaticdata.staticmember.carrierflag = "1";
            }
        } else {
            ShowMessage("獲取綁定載板的數量時失敗，請聯繫MIS部門");
            //return;
        }
    }

    public void csubmitclick(View v) {
        String taskname = "Carrierscan onclick Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    submit_Click();
                }
            }, taskname);
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }

    private void submit_Click() throws Exception
    {
        String defqtytbText=getText(defqtytb).toString().trim();
        String scandeftbText=getText(scandeftb).toString().trim();
        if (defqtytbText.equals(scandeftbText))
        {
            Boolean flag =cwa.CallRB("upcarrierstatedata", lotno);
            if (!flag)
            {
                ShowMessage("解綁載板失敗，請聯繫MIS部門");
                SFCStaticdata.staticmember.carrierflag = "";
                //return;
            }
            else
            {
                ShowMessage("掃描解綁載板成功");
                SFCStaticdata.staticmember.carrierflag = "1";
                this.Close();
            }
        }
        else
        {
            ShowMessage("綁定的載板數量尚未掃描完畢，不能解綁");
            SFCStaticdata.staticmember.carrierflag = "";
            //return;
        }
        //removeAllViewsInLayout(scangv);

    }
    public void finish() {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                }
            }, "Check In Closeing Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Check In Closeing Task Error:" + ex.getMessage());
        }
        super.finish();
    }

    private void scantb_KeyPress(int keycahr) throws Exception {
        if (keycahr == 13) {
            String defqtytbText = getText(defqtytb).toString().trim();
            String scantbText = getText(scantb).toString().trim();
            if (item.equals(defqtytbText)) {
                ShowMessage("該批號綁定的載板已經掃描完畢，無需再掃");
                return;
            }
            String carrierno = scantbText;
            if (carrierno.isEmpty()) {
                ShowMessage("載板編號不能為空");
                return;
            }
            DataTable dt = cwa.CallRDT("getbangcarrierdata", carrierno, lotno);
            if (dt != null) {
                if (dt.Rowscount() > 0) {

                } else {
                    ShowMessage("沒有該彈夾的信息或是該彈夾沒有綁定該批號，請確認是否輸入錯誤");
                    return;
                }
            } else {
                ShowMessage("獲取該載板綁定的信息時發生錯誤，請聯繫MIS部門");
                return;
            }

//            if (checksn(carrierno))
//            {
            item = String.valueOf(Integer.parseInt(item) + 1);
            View v = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.listview_carrierscan, null);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname1), item);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname2), lotno);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname3), carrierno);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname4), SFCStaticdata.staticmember.userid);

            addView(scangv, v);
            setText(scandeftb, item);
            setText(scantb, "");
//            }
//            else
//            {
//                ShowMessage("該顆載板已經存在，不能重複掃描");
//                return;
//            }
        }
    }

    public void scantbclick(View v) {

        Intent openCameraIntent = new Intent(Carrierscan.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            String checkintenttemp1 = null;
            if (scanResult == null) {
                //保持为当前页面
                checkintenttemp1 = "Current";
            } else {
                checkintenttemp1 = scanResult.substring(0, 1);
            }

            //setText(scandeftb,scanResult);
            scantb.setText(scanResult);

            String taskname = "Carrierscan scantbclick Task";
            try {
                ExecTask(new SFCTaskVoidInterface() {
                    @Override
                    public void taskvoid(Object valueo) throws Exception {
                        scantb_KeyPress(13);
                    }
                }, taskname);
            } catch (Exception ex) {
                BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
            }
        }
    }
}

