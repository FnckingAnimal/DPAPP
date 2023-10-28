package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.cmapp.DataTable.DataTable;
import app.dpapp.R;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.NetUtils;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class magazineform extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    private TextView txt_lotno;
    private TextView txt_opno;
    private TextView txt_qty;
    private EditText txt_magazineno;
    private TextView txt_okqty;
    private LinearLayout dataGridView1;

    String insertsql = "begin ";
    String deletesql = "begin ";
    String updatesql = "begin ";
    int qty_2 = 0;
    String status = "";

    
    String lotno;
    String opno;
    String type;
    String mqty;
    
    private String ipstatic;
    private String HDSerialNo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_magazineform);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            txt_lotno = (TextView) findViewById(R.id.isfc_magazineform_lotnotbv1);
            txt_opno = (TextView) findViewById(R.id.isfc_magazineform_opnotbv1);
            txt_okqty = (TextView) findViewById(R.id.isfc_magazineform_scantv1);
            txt_magazineno=(EditText)findViewById(R.id.isfc_magazineform_scanet1);
            txt_qty=(TextView)findViewById(R.id.isfc_magazineform_inputtbv1);
            dataGridView1 = (LinearLayout) findViewById(R.id.isfc_carrierscan_stationviewll1);
//            submit = (Button) findViewById(R.id.isfc_defectinput_submitbt);

            SFCStaticdata.staticmember.magazineformflag=false;
            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotno=ss[3];
            opno=ss[4];
            mqty=ss[5];
            type=ss[6];
            
            txt_lotno.setText(lotno);
            txt_opno.setText(opno);
            txt_qty.setText(mqty);
            status = type;
           

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");

            txt_magazineno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    try {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    txt_magazineno_KeyDown(13);
                                }
                            }, "Carrierscan  Scan Task");
                        }
                    }
                    catch(Exception ex)
                    {
                        BaseFuncation.showmessage(magazineform.this,"Carrierscan  Scan Task Error:"+ex.getMessage());
                    }
                    return true;
                }
            });

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }



    private void txt_magazineno_KeyDown(int keycahr) throws Exception
    {
        if (keycahr == 13)
        {
            String magazineno = getText(txt_magazineno).toString().trim();
            String lotno = getText(txt_lotno).toString().trim();
            String opno = getText(txt_opno).toString().trim();
            String txt_qtyText=getText(txt_qty).toString().trim();
            String txt_okqtyText = getText(qty_2).toString().trim();

            if (!isboxno(magazineno))
            {
                setText(txt_magazineno,"");
                setFocusable(txt_magazineno,true);
                return;
            }

            try
            {
                DataTable dt = new DataTable();
                dt = magazinestate(magazineno,opno);
                if (dt.Rowscount() > 0)
                {
                    if (!dt.Rows(0).get_CellValue("lotno").trim().equals(lotno))
                    {
                        ShowMessage("該彈夾已綁定批號 " + dt.Rows(0).get_CellValue("lotno").trim() + " ，尚未解綁");
                        setText(txt_magazineno, "");
                        setFocusable(txt_magazineno, true);
                        return;
                    }
                }
                else
                {
                    if(status.equals("2"))
                    {
                        dt=null;
                        dt = magazinestateok(magazineno, opno, lotno);
                        if (dt.Rowscount() > 0)
                        { }
                        else
                        {
                            ShowMessage("該彈夾尚未綁定批號，請確認是否拿錯 ");
                            setText(txt_magazineno, "");
                            setFocusable(txt_magazineno, true);
                            return;
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                ShowMessage("查詢该弹夹是否已綁定時發生錯誤");
                return;
            }
            if (checksn(magazineno))
            {
                ShowMessage("該彈夾重複掃描");
                setText(txt_magazineno, "");
                setFocusable(txt_magazineno, true);
                return;
            }

            qty_2++;

            View v = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.listview_carrierscan, null);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname1), String.valueOf(qty_2));
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname2), lotno);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname3), magazineno);
            setText((TextView) v.findViewById(R.id.listviewcarrierscanmname4), SFCStaticdata.staticmember.userid);
            addView(dataGridView1, v);

            if (status.equals("1")) // 進站
            {
                insertsql = insertsql + " delete from t_magazinedata where lotno='" + lotno + "' and magazineno='" + magazineno + "' and mtype='1' and opno='" + opno + "' ;";
                insertsql = insertsql + " INSERT INTO T_MAGAZINEDATA VALUES('" + lotno + "','" + magazineno + "','" + status + "','" + opno + "',SYSDATE,'" + SFCStaticdata.staticmember.userid + "','','','','','','');  ";
                setText(txt_okqty, String.valueOf(qty_2));
                setText(txt_magazineno, "");
                setFocusable(txt_magazineno, true);

                if (qty_2 == Integer.parseInt(txt_qtyText))
                {
                    if (insertsql.contains(" END ; "))
                    {
                    }
                    else
                    {
                        insertsql = insertsql + " END ; ";
                    }
                    if (deletesql.contains(" END ; "))
                    {
                    }
                    else
                    {
                        deletesql = deletesql + " END ; ";
                    }

                    SFCStaticdata.staticmember.magazineformflag =
                    cwa.CallRB("basefunction_execsql_ora_b",insertsql,"BFMC");
                    if (SFCStaticdata.staticmember.magazineformflag)
                    {
                        ShowMessage("彈夾與批號綁定成功");
                        this.Close();
                    }
                    else
                    {
                        ShowMessage("彈夾與批號綁定失敗");
                    }
                }
            }
            else if(status.equals("2")) // 出站
            {
                updatesql = updatesql + " update T_MAGAZINEDATA set mtype='2'  where  lotno='" + lotno + "'  and  opno='" + opno + "' and mtype='1' AND MAGAZINENO='" + magazineno + "' ; ";
                setText(txt_okqty, String.valueOf(qty_2));
                setText(txt_magazineno, "");
                setFocusable(txt_magazineno, true);

                if (qty_2 == Integer.parseInt(txt_qtyText))
                {
                    if (updatesql.contains(" END ; "))
                    {
                    }
                    else
                    {
                        updatesql = updatesql + " END ; ";
                    }
                    SFCStaticdata.staticmember.magazineformflag =
                    cwa.CallRB("basefunction_execsql_ora_b",updatesql,"BFMC");
                    if (SFCStaticdata.staticmember.magazineformflag)
                    {
                        ShowMessage("彈夾與批號解綁成功");
                        this.Close();
                    }
                    else
                    {
                        ShowMessage("彈夾與批號解綁失敗");
                    }
                }
            }
        }
    }
    public DataTable magazinestate(String sn,String opno) throws Exception
    {

        String sql = "select lotno from  T_MAGAZINEDATA  where magazineno=:sn and mtype='1' and opno=:opno ";//  1 已綁定
        String[] parmer = { sn ,opno};
        DataTable data = new DataTable();

            data =
            cwa.CallRDT("basefunction_execsql_ora_d",sql,BaseFuncation.SerializeObjectArrayString(parmer),"BFMC");

        return data;
    }
    public DataTable magazinestateok(String sn,String opno,String lotno) throws Exception
    {

        String sql = "select lotno from  T_MAGAZINEDATA  where magazineno=:sn and mtype='2' and opno=:opno and lotno=:lotno";//  1 已綁定
        String[] parmer = { sn, opno, lotno };
        DataTable data = new DataTable();

            data =
            cwa.CallRDT("basefunction_execsql_ora_d",sql,BaseFuncation.SerializeObjectArrayString(parmer),"BFMC");

        return data;
    }
    private Boolean checksn(String sn) throws Exception {
        Boolean checksnflag = false;

        int rowcount = getChildCount(dataGridView1);
        if (rowcount > 1) {
            for (int i = 0; i < rowcount - 1; i++) {
                String sninner = getText((TextView) dataGridView1.getChildAt(i).findViewById(R.id.listviewcarrierscanmname3)).toString().trim();
                if (sn.equals(sninner)) {
                    checksnflag = true;
                    break;
                }
            }
        } else {
            checksnflag = false;
        }
        return checksnflag;
    }

    public Boolean isboxno(String ganonow) throws Exception {
        String[] arr = ganonow.split("-");
        if ((ganonow.length() != 7) || (arr.length != 2)) {
            ShowMessage("请输入7位正确的弹匣号('M'+'-'+五位数字)");
            return false;
        }
        if (arr[1].length() != 5) {
            ShowMessage("弹匣编号错误('M'+'-'+五位数字)");
            return false;
        }
        try {
            int aint = Integer.parseInt(arr[1]);
        } catch (Exception ex) {
            ShowMessage("弹匣编号后五位为数字，请重新输入");
            return false;
        }
        return true;
    }
}

