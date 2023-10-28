package app.dpapp.IncludingSFC;

/**
 * Created by S7187445 on 2017/6/14.
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

public class FOLInOutCheckGAOven  extends ActivityInteractive{
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
    private TextView opnametxt;
    private TextView dieqtytxt;
    private EditText scantb;
    private LinearLayout scangv;


    String item = "0";
    String lotno = "";
    String opno = "";
    String opname = "";
    String dieqty = "";
    String sendstatus="";

    private String ipstatic;
    private String HDSerialNo;

    private String status = "";//1.进站扫描，2.出站扫描
    private String sqlinsertupdatesql = "begin ";
    String[] parmerlist;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_carrierscan);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            lotnotxt = (TextView) findViewById(R.id.isfc_folinoutcheckgaoven_lotnotbv1);
            opnotxt = (TextView) findViewById(R.id.isfc_folinoutcheckgaoven_opnotbv1);
            opnametxt = (TextView) findViewById(R.id.isfc_folinoutcheckgaoven_opnametv1);
            dieqtytxt=(TextView)findViewById(R.id.isfc_folinoutcheckgaoven_dieqtytbv1);

            scantb=(EditText)findViewById(R.id.isfc_folinoutcheckgaoven_scanet1);
            scangv = (LinearLayout) findViewById(R.id.isfc_carrierscan_stationviewll1);
            scangv.removeAllViews();

//            submit = (Button) findViewById(R.id.isfc_defectinput_submitbt);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];

            //lotno,string opno,string qty,string sendstatus
            lotno =  ss[3];
            opno =  ss[4];
            dieqty =  ss[5];
            sendstatus =  ss[6];

            //scantb.setFocusable(true);

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    FOLInOutCheckGAOven_Load();
                    _loadingstatus = true;
                }
            }, "FOLInOutCheckGAOven Loading Task");


            scantb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    try {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    textBox_gano_KeyUp(13);
                                }
                            }, "FOLInOutCheckGAOven  Scan Task");
                        }
                    }
                    catch(Exception ex)
                    {
                        BaseFuncation.showmessage(FOLInOutCheckGAOven.this,"FOLInOutCheckGAOven  Scan Task Error:"+ex.getMessage());
                    }
                    return true;
                }
            });

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    private void FOLInOutCheckGAOven_Load() throws Exception {

        setText(lotnotxt, lotno);
        setText(opnotxt,opno);
        setText(dieqtytxt,dieqty);

        opname =cwa.CallRS("getopname", opno);
        setText(opnametxt,opname);

        SFCStaticdata.staticmember.FolGaOvenStationNoInOutTimeIsOK = false;
        if(status=="1")//进站扫描，清除当前站位(只有一个站位)已有的记录，按批号
        {
            Boolean bl=delete(lotno);
            if(bl==false)
            {
                ShowMessage("清除GAOven站位已有的扫描信息时发生异常，请联系MIS");
                setEnabled(scantb,false);
            }
        }
    }

    public Boolean isexists(String gano)  throws Exception
    {
        int rowcount = getChildCount(scangv);
        if (rowcount >= 1)
        {
            for (int i = 0; i < rowcount; i++)
            {
                //  if(dataGridView_GAOvenno.Rows[i].Cells[2].Value.ToString()==gano)
                String a = getText((TextView) scangv.getChildAt(i).findViewById(R.id.lisrviewexpendsourceMname1)).toString().trim();

                if (a.equals(gano))
                {
                    return false;
                }
            }
        }
        else
        {
            return true;
        }

        return true;
    }
    public Boolean isOk(DataTable dt) throws Exception
    {
        int rowcount = getChildCount(scangv);
        if(rowcount!=dt.Rowscount())
        {
            return false;
        }
        for (int j = 0; j < dt.Rowscount(); j++)
        {
            for (int i = 0; i < rowcount; i++)
            {
                String a = getText((TextView) scangv.getChildAt(i).findViewById(R.id.lisrviewexpendsourceMname1)).toString().trim();
                if (i < rowcount - 1)
                {//dataGridView_GAOvenno.Rows[i].Cells[2].Value.ToString()
                    if (a== dt.Rows(j).get_CellValue(0))
                    {
                        break;
                    }
                }
                else if (i ==rowcount- 1)
                {
                    if (a == dt.Rows(j).get_CellValue(0))
                    {
                        break;
                    }
                    else
                    {
                        return false;//DT值里面某一个在DataGridView里面没有，即没有正常扫描，故返回false
                    }
                }
            }
        }
        return true;
    }
    public Boolean delete(String lotno) throws Exception
    {
       Boolean bl=cwa.CallRB("FOLInOutCheckGAOven_delete", lotno);
        return bl;
    }

    public Boolean isgano(String ganonow) throws Exception
    {
        String[] arr = ganonow.split("-");
        if((ganonow.length()!=7)||(arr.length!=2))
        {
           ShowMessage("请输入7位正确的弹匣号('M'+'-'+五位数字)");
            return false;
        }
        if(arr[0]!="M")
        {
            ShowMessage("弹匣编号错误(首字母为M)");
            return false;
        }
        if(arr[1].length()!=5)
        {
            ShowMessage("弹匣编号错误('M'+'-'+五位数字)");
            return false;
        }
        try
        {
            int aint = Integer.parseInt(arr[1]);
        }
        catch (Exception el)
        {
            ShowMessage("弹匣编号后五位为数字，请重新输入");
            return false;
        }
        return true;


    }
    private void textBox_gano_KeyUp(int keycahr) throws Exception
    {
        if(keycahr == 13)
        {
            String gano =getText(scantb).toString().trim();
            if(!isgano(gano))
            {
               setText(scantb,"");
                return;
            }
            if(!isexists(gano))
            {
                ShowMessage("重复扫描");
                setText(scantb, "");
                return;

            }

            if(status.equals("1"))
            {
                View cv = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_folinoutcheckgaoven, null);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname1)).setText(lotno);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname2)).setText(opno);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname3)).setText(gano);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname4)).setText(SFCStaticdata.staticmember.userid);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname5)).setText(SFCStaticdata.staticmember.HDSerialNo);
                addView(scangv, cv);

            }
            else if(status.equals("2"))
            {
                View cv = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_folinoutcheckgaoven, null);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname1)).setText(lotno);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname2)).setText(opno);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname3)).setText(gano);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname4)).setText(SFCStaticdata.staticmember.userid);
                ((TextView) cv.findViewById(R.id.listviewfolinoutcheckgaovenname5)).setText(SFCStaticdata.staticmember.HDSerialNo);
                addView(scangv, cv);
            }
            setText(scantb,"");
        }
    }
    public void csubmitclick(View v) {
        String taskname = "FOLInOutCheckGAOven onclick Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_commit_Click();
                }
            }, taskname);
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }
    private void button_commit_Click() throws Exception
    {
        if (getChildCount(scangv) < 1)
        {
            ShowMessage("請輸入基板信息後再進行過站");
            return;
        }

        String lotnogv= getText((TextView) scangv.getChildAt(0).findViewById(R.id.lisrviewexpendsourceMname1)).toString().trim();
        String userid= getText((TextView) scangv.getChildAt(0).findViewById(R.id.lisrviewexpendsourceMname4)).toString().trim();
        String ip= getText((TextView) scangv.getChildAt(0).findViewById(R.id.lisrviewexpendsourceMname5)).toString().trim();
        String opno =  getText((TextView) scangv.getChildAt(0).findViewById(R.id.lisrviewexpendsourceMname2)).toString().trim();
        String gano = getText((TextView) scangv.getChildAt(0).findViewById(R.id.lisrviewexpendsourceMname3)).toString().trim();

        Boolean blexec =false;
        if (status == "2")//以批号为主
        {

            DataTable dt =cwa.CallRDT("FOLInOutCheckGAOven_submit_1", lotnogv);
            Boolean bl = isOk(dt);
            if (bl == false)
            {
                ShowMessage("当前扫描的弹匣号与该批号进站时扫描的弹匣号不一致，请重新作业"); return;
            }

            String timePoint=cwa.CallRS("FOLInOutCheckGAOven_submit_2",lotnogv);
            if (Double.parseDouble(timePoint) < 55)
            {
                Boolean updatebool = cwa.CallRB("FOLInOutCheckGAOven_submit_3",userid,ip, lotnogv);
                if (updatebool == false)
                {
                    ShowMessage("彈匣號進出站時間<55min，更新狀態時發生異常，請聯繫MIS");
                }
                ShowMessage("該彈匣編號進出站時間小於55分鐘，系統已重新計時，請按流程作業。");
                this.Close();
                return;
            }

            sqlinsertupdatesql = sqlinsertupdatesql + "update tblfolgaovenchecknodata set outdate=sysdate,outop=:op,checkstatus='2',outip=:ip where  lotno=:lot ;";
            sqlinsertupdatesql = sqlinsertupdatesql + "end;";
            String[] parmerlist={userid,ip,lotnogv};
            blexec =  cwa.CallRB("checkout_submit_90", sqlinsertupdatesql, BaseFuncation.SerializeObjectArrayString(parmerlist));
        }
        else
        {
            for (int i = 0; i < getChildCount(scangv); i++)
            {
                if (status == "1")
                {
                    sqlinsertupdatesql = sqlinsertupdatesql + "insert into tblfolgaovenchecknodata values(:lotno" + i + ",:opno" + i + ",:gano" + i+ ",:checkstatus" + i + ",sysdate,null,:inop" + i + ",null,:ip" + i+ ",null,null,null,null,null,null);";
                    sqlinsertupdatesql = sqlinsertupdatesql + "end;";
                    String[] parmerlist={lotnogv,opno,gano,"1",userid,ip};
                    blexec =  cwa.CallRB("checkout_submit_90", sqlinsertupdatesql, BaseFuncation.SerializeObjectArrayString(parmerlist));
                }
            }
        }

        if (blexec == true)
        {
            SFCStaticdata.staticmember.FolGaOvenStationNoInOutTimeIsOK = true;
            if (status == "1")
            { ShowMessage("存儲進站彈匣號信息成功！"); }
            else if (status == "2")
            { ShowMessage("更新出站彈匣號信息成功！"); }
            this.Close();
        }
        else
        {
            SFCStaticdata.staticmember.FolGaOvenStationNoInOutTimeIsOK = false;
            if (status == "1")
            { ShowMessage("存儲進站彈匣號信息失敗，請聯繫MIS！"); }
            else if (status == "2")
            { ShowMessage("更新出站彈匣號信息失敗，請聯繫MIS！"); }
        }

    }

    public void finish() {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                }
            }, "FOLInOutCheckGAOven Closeing Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "FOLInOutCheckGAOven Closeing Task Error:" + ex.getMessage());
        }
        super.finish();
    }

    public void scantbclick(View v) {

        Intent openCameraIntent = new Intent(FOLInOutCheckGAOven.this, CaptureActivity.class);
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

            String taskname = "FOLInOutCheckGAOven scantbclick Task";
            try {
                ExecTask(new SFCTaskVoidInterface() {
                    @Override
                    public void taskvoid(Object valueo) throws Exception {
                        textBox_gano_KeyUp(13);
                    }
                }, taskname);
            } catch (Exception ex) {
                BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
            }
        }
    }
}
