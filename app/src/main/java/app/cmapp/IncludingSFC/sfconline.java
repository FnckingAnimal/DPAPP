package app.cmapp.IncludingSFC;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.dpapp.R;
import app.cmapp.Staticdata;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.DataTable.DataTable;
import app.cmapp.zxing.activity.CaptureActivity;

/**
 * Created by S7187445 on 2017/4/13.
 */
public class sfconline extends ActivityInteractive {

    private Object _locko=new Object();
    private CallWebapi cwa;

    private static String Sessionuser;
    private static String shortmapid;

    EditText etlotno;
    Button submitbt;
    TextView spidevice;
    TextView spinewdeviceno;
    TextView labopno;
    Spinner ddlopno;
    EditText etip;
    LinearLayout lly1;
    View convertView;

    private String now_lotno;
    private String Site;
    //private String sysErrorcode; // 1:获取资料异常
    private String strfifoflag; //
    private String strdeviceno;
    private String strnewdeviceno;
    //private String opnoloadfalg;
    private DataTable lotshowdata;
    private String ip;
    private GoogleApiClient client;

    private AppCompatActivity _c;
    private String str;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            _c=this;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sfconline);

            strfifoflag=SFCStaticdata.staticmember.fifoflag;
            strdeviceno=SFCStaticdata.staticmember.deviceno;
            strnewdeviceno=SFCStaticdata.staticmember.newdeviceno;
            ip=SFCStaticdata.staticmember.HDSerialNo;//SFCStaticdata.staticmember.ip;

            cwa = new CallWebapi(strdeviceno, strnewdeviceno, null, this);

            //HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            final Staticdata sc = (Staticdata) getApplication();
            Sessionuser =sc.getLoginUserID();
            Site = sc.usersite;

            etlotno = (EditText) findViewById(R.id.et_lotno);
            spinewdeviceno= (TextView) findViewById(R.id. tv_newdeviceno);
            spidevice = (TextView) findViewById(R.id.tv_deviceno);
            labopno = (TextView) findViewById(R.id.tv_opnoshow);
            ddlopno = (Spinner) findViewById(R.id.spi_opno);
            etip=(EditText) findViewById(R.id.et_ip);
            submitbt = (Button) findViewById(R.id.btn_submit);
            lly1 = (LinearLayout) findViewById(R.id.LinearLayout_lotlist);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    setText(spinewdeviceno, strnewdeviceno);
                    setText(spidevice, strdeviceno);
                    setText(etip, SFCStaticdata.staticmember.ip);

                    showdata();
                }
            }, "SFC Online Loading Task");

        }
         catch(Exception ex)
        {
            BaseFuncation.showmessage(this, ex.getMessage());
            finish();
        }
    }

    private void showdata()
    {
        try {
            if (strfifoflag.equals("1")) {
                //labopno.setVisibility(View.VISIBLE);
                //ddlopno.setVisibility(View.VISIBLE);
                setVisibility(labopno,View.VISIBLE);//setEnabled(labopno,true);
                setVisibility(ddlopno,View.VISIBLE);//setEnabled(ddlopno,true);
                String areastr = cwa.CallRS("getipareadata");
                DataTable vtqdt = cwa.CallRDT("getvtqopname", areastr);

                setAdapter(ddlopno,
                        BaseFuncation.setvalue(vtqdt, null, "waferlotno", sfconline.this)
                );
            }
            else
            {
                //labopno.setVisibility(View.GONE);
                //ddlopno.setVisibility(View.GONE);
                //setVisibility(labopno, View.GONE);//setEnabled(labopno,true);
                setText(labopno,"");
                setVisibility(ddlopno, View.GONE);//setEnabled(ddlopno,true);
            }


            DataTable qcnowdt = cwa.CallRDT("IsQCNGCheckDeviceDt", strdeviceno);
            if (qcnowdt == null)
            {
                SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean = false;
            }
            else if (qcnowdt.Rowscount() <= 0)
            {
                SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean = false;
            }
            else
            {
                SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean = true;
            }

            /*View convertView;

            lotshowdata=cwa.CallRDT("onlinemap_showdata_1");
            removeAllViewsInLayout(lly1);
            for (int i = 0; i <lotshowdata.Rowscount(); i++) {

                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source16, null);
                //((TextView) convertView.findViewById(R.id.listviewsource12Mname0)).setText(Integer.toString(i + 1));
                //((TextView) convertView.findViewById(R.id.listviewsource12Mname1)).setText(lotshowdata.Rows(i).get_CellValue("LOTNO"));
                //((TextView) convertView.findViewById(R.id.listviewsource12Mname2)).setText(lotshowdata.Rows(i).get_CellValue("OPNO"));
                //((TextView) convertView.findViewById(R.id.listviewsource12Mname3)).setText(lotshowdata.Rows(i).get_CellValue("DIEQTY"));
                //((TextView) convertView.findViewById(R.id.listviewsource12Mname4)).setText(lotshowdata.Rows(i).get_CellValue("UPDATEDATE"));
                //((EditText) convertView.findViewById(R.id.listviewsource12Mname5)).setText(lotshowdata.Rows(i).get_CellValue("LOTSTATE"));
               // ((EditText) convertView.findViewById(R.id.listviewsource12Mname5)).setEnabled(false);
                //((EditText) convertView.findViewById(R.id.listviewsource12Mname5)).setTextColor(Color.RED);
                // lly1.addView(convertView);

                setText((TextView) convertView.findViewById(R.id.listviewsource12Mname0),Integer.toString(i + 1));
                setText((TextView) convertView.findViewById(R.id.listviewsource12Mname1),lotshowdata.Rows(i).get_CellValue("LOTNO"));
                setText((TextView) convertView.findViewById(R.id.listviewsource12Mname2),lotshowdata.Rows(i).get_CellValue("OPNO"));
                setText((TextView) convertView.findViewById(R.id.listviewsource12Mname3),lotshowdata.Rows(i).get_CellValue("DIEQTY"));
                setText((TextView) convertView.findViewById(R.id.listviewsource12Mname4),lotshowdata.Rows(i).get_CellValue("UPDATEDATE"));
                setText((TextView) convertView.findViewById(R.id.listviewsource12Mname5), lotshowdata.Rows(i).get_CellValue("LOTSTATE"));

                addView(lly1,convertView);
            }*/
        }
        catch (Exception e1)
        {
            Toast.makeText(this, e1.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void nfconclick(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception{
                    submitbutton_Click();
                }
            }, "SFCOnline Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "SFCOnline Submit Task Error:" + ex.getMessage());
        }
    }

    public void submitbutton_Click() {
        SFCStaticdata.staticmember.engmfglotcheckflag = false; // 确认批号是否为工程批
        SFCStaticdata.staticmember.lotsmtcheckflag = false;  //默认不是smt
        SFCStaticdata.staticmember.errorqtydata = null;
        SFCStaticdata.staticmember.lotfolbingpicheck = false;

        String lotno = etlotno.getText().toString().toUpperCase();
        String userid = Sessionuser;
        String usergroup = "";

        String lotnooldnndd = lotno;
        String lotnoeolfolflag = lotno;
        String lotnosub = "";

        lotno = fullToHalf(lotno); //全角转换为半角

        if (lotno == null || lotno.equals(""))
        {
            BaseFuncation.showmessage(this,"lotno 内容不能为空");
            finish();
        }
        else   // sublot are allow 0723
        {
            try {
                //cwa = new CallWebapi(strdeviceno,strnewdeviceno, null, this);
                lotnosub = lotno.substring(0, 9).trim();//(0,9)

                if (lotno.startsWith("S")) {
                    SFCStaticdata.staticmember.lotsmtcheckflag = true;
                }

                // 全部以子批作業！！！
                if(!lotno.contains("-"))//if (lotno.indexOf("-") == -1)
                {
                    ShowMessage("请以子批作业");
                    return;
                }
                //产品是否为重工品
                if (lotno.substring(5,6).toUpperCase().equals("C"))   // 20150424 ljj  repair  拿掉帶D的工單，吳浩甫12-24  (5,1)
                {
                    SFCStaticdata.staticmember.AllReworkFlag = true;
                } else
                {
                    SFCStaticdata.staticmember.AllReworkFlag = false;
                }


                //String[] strlotno = lotno.split("-");
                String lotnoengmfgflag = lotno.substring(5,6).toUpperCase(); //(5,1)
                String staticinputlotno = lotno;

                //判斷批號是否不需要卡進出站時間以及抽檢數據等
                DataTable dtifcheck = cwa.CallRDT("onlinemap_submit_1", lotno);
                if (dtifcheck.Rowscount() > 0)
                {
                    SFCStaticdata.staticmember.lotnouncheckBoolean = true;  //不用卡進出站時間以及抽檢數據等--按母批（保證系統中沒有子批的相同權限）
                }
                else
                {
                    SFCStaticdata.staticmember.lotnouncheckBoolean = false;
                }

                //特殊工單不卡母批
                DataTable  dtnomupi = cwa.CallRDT("onlinemap_submit_2",lotnosub);
                if (dtnomupi == null)//表不存在或者可能執行異常
                {
                    SFCStaticdata.staticmember.nocheckmupicheckBoolean = false;
                }
                else if (dtnomupi.Rowscount() <= 0)//沒有導入數據
                {
                    SFCStaticdata.staticmember.nocheckmupicheckBoolean = false;
                }
                else if (dtnomupi.Rowscount() > 0)//存在數據
                {
                    SFCStaticdata.staticmember.nocheckmupicheckBoolean = true;
                }

                //檢測批號狀態
                DataTable lotnobasedatadt = cwa.CallRDT("onlinemap_submit_3", lotno);
                if (lotnobasedatadt != null)
                {
                    if (lotnobasedatadt == null || lotnobasedatadt.Rowscount() != 1)
                    {
                        DataTable lotnostate = cwa.CallRDT("onlinemap_submit_4", lotno.substring(1, 10));  //(0, 9)
                        if (lotnostate == null  || lotnostate.Rowscount() <= 0)
                        {
                            ShowMessage("請聯繫生管確認該工單是否有開立母批");
                            regdata();
                            return;
                        }
                        else
                        {
                            if (lotnostate.Rows(0).get_CellValue("lotstate").trim().equals("1")) //lotstate
                            {
                                ShowMessage("批號尚未分批，點擊確定聯繫相關人員進行分批！");
                                SFCStaticdata.staticmember.wipopflag = false;
                                //Creat New Activity
                                //wiplotoperate wiplotno = new wiplotoperate(lotno.Substring(0, 9), lotnostate.Rows[0]["wono"].ToString().Trim(), lotnostate.Rows[0]["dieqty"].ToString().Trim(), lotnostate.Rows[0]["productno"].ToString().Trim());
                                //wiplotno.ShowDialog();.Rows(0)
                                if (SFCStaticdata.staticmember.wipopflag)
                                {
                                    ShowMessage("批號已成功分批，請重新輸入進站！");
                                    return;
                                }
                                else
                                {
                                    ShowMessage("分批失敗，請聯繫MIS！");
                                    return;
                                }
                            }
                            else
                            {
                                ShowMessage("請聯繫生管確認批號是否正常！批號狀態：" + lotnostate);
                                regdata();
                                return;
                            }
                        }
                    }
                }
                else
                {
                    ShowMessage("檢測WIP資料異常！！！");
                    regdata();
                    return;
                }

                if (!lotno.substring(0, 1).equals("S")) //lotno.Substring(0, 1) != "S"
                {
                    if (lotnoengmfgflag.equals("E"))
                    {
                        SFCStaticdata.staticmember.engmfglotcheckflag = true;
                    }
                }

                String lotstate = cwa.CallRS("onlinemap_submit_5", lotno);
                if (lotstate == null || lotstate.equals(""))
                {
                    ShowMessage("批號WIP狀態數據異常，請聯繫MIS！！！");
                    return;
                }
                if (lotstate.trim().equals("6"))
                {
                    ShowMessage("已分批，請已子批過站");
                    regdata();
                    return;
                }

                String productno = lotnobasedatadt.Rows(0).get_CellValue("PRODUCTNO").trim(); //productno
                SFCStaticdata.staticmember.hpproductno = productno;
                String dieqty = lotnobasedatadt.Rows(0).get_CellValue("dieqty").trim(); //dieqty
                String wono = lotnobasedatadt.Rows(0).get_CellValue("wono").trim();   //wono

                // 批號轉換   **********************************************************************************************************
                if (lotno.substring(0, 1).equals("K"))       //針對現從 批號 else 情況  substring(0, 1)
                {
                    String prolotno = lotno;     //  獲取一個初始的  Lotno

                    DataTable dt_proget;

                    DataTable lotallstate = cwa.CallRDT("onlinemap_submit_6", lotno);
                    if (lotallstate.Rowscount() == 1)
                    {
                        lotno = lotallstate.Rows(0).get_CellValue("lotstate").trim(); //lotstate

                        String flag = lotallstate.Rows(0).get_CellValue("locationstate").trim(); //locationstate

                        if (flag.equals("1"))    //  FOL    ----  select productno,productnoeol,productnofol from t_productfdata;
                        {
                            SFCStaticdata.staticmember.lotfoleolcheckflag = true;
                        }
                        else if (flag.equals("2"))
                        {
                            SFCStaticdata.staticmember.lotfoleolcheckflag = false;
                        }
                        else if (flag.equals("4"))
                        {
                            ShowMessage("该批号已经过站完毕");//MessageBox.Show("该批号已经过站完毕！");
                            regdata();
                            return;
                        }

                        try
                        {
                            dt_proget = cwa.CallRDT("onlinemap_submit_7", productno);
                            if (dt_proget.Rowscount()> 0)
                            {
                                productno = dt_proget.Rows(0).get_CellValue(0).trim(); //"productnofol"
                            }
                            else
                            {
                                ShowMessage("產品成品料號尚未設定對應關係");//MessageBox.Show("產品成品料號尚未設定對應關係");
                                return;
                            }
                        }
                        catch(Exception e1)
                        {
                            ShowMessage("獲取料號AEF關係時發生錯誤！"); //MessageBox.Show("獲取料號AEF關係時發生錯誤！");
                            return;
                        }
                    }
                    else if (lotallstate.Rowscount()== 0)
                    {
                        SFCStaticdata.staticmember.lotfoleolcheckflag = true;  // lot 处于 FOL
                        lotno = "P" + lotno;

                        try
                        {
                            dt_proget = cwa.CallRDT("onlinemap_submit_7", productno);
                            if (dt_proget.Rowscount() > 0)
                            {
                                productno = dt_proget.Rows(0).get_CellValue(0).trim(); //"productnofol"

                                String op =SFCStaticdata.staticmember.HDSerialNo;//SFCStaticdata.staticmember.ip;// BLL.staticmember.ip.ToString();
                                boolean insertlot = cwa.CallRB("onlinemap_submit_8", prolotno, lotno, op, wono);
                                if (!insertlot)
                                {
                                    ShowMessage("首次更新成品批號狀態時失敗！"); // MessageBox.Show("首次更新成品批號狀態時失敗");
                                    return;
                                }
                            }
                            else
                            {
                                ShowMessage("產品成品料號尚未設定對應關係！"); //MessageBox.Show("產品成品料號尚未設定對應關係");
                                return;
                            }
                        }
                        catch(Exception ex1)
                        {
                            ShowMessage("獲取料號AEF關係時發生錯誤！");  // MessageBox.Show("獲取料號AEF關係時發生錯誤！");
                            return;
                        }
                    }
                    else
                    {
                        ShowMessage("Lot前後段對應錯誤！");
                        return;
                    }
                }


                DataTable producrdatadt = cwa.CallRDT("onlinemap_submit_9", productno);
                String devicename = null, processno = null, locationaa = null;
                if (producrdatadt == null)
                {
                    ShowMessage("獲取process工程資料異常！");  //MessageBox.Show("獲取process工程資料異常");
                    regdata();
                    return;
                }

                if (producrdatadt.Rowscount() == 1)
                {
                    processno = producrdatadt.Rows(0).get_CellValue("processno");
                    devicename = producrdatadt.Rows(0).get_CellValue("devicename");
                    locationaa = producrdatadt.Rows(0).get_CellValue("att1");
                    String productnovis = producrdatadt.Rows(0).get_CellValue("att4");
                    if (!productnovis.equals("1"))
                    {
                        ShowMessage("該料號不可用，請聯繫MIS！");
                        regdata();
                        return;
                    }
                    if (!devicename.equals(spidevice.getText()))//devicenotextBox.Text.Trim()
                    {
                        ShowMessage("機種名稱異常！");
                        regdata();
                        return;
                    }
                    if (locationaa.startsWith("SMT"))
                    {
                        SFCStaticdata.staticmember.lotsmtcheckflag = true;
                    }

                    if (locationaa.equals("EOL"))
                    {
                        SFCStaticdata.staticmember.lotcheckeolflag = true;
                    }
                    else
                    {
                        SFCStaticdata.staticmember.lotcheckeolflag = false;  // FOL + SMT
                        //String aa=lotno.Substring(0, 1) ;
                        if  (!lotno.substring(0, 1).equals("S"))//(lotno.Substring(0, 1) != "S")
                        {
                            SFCStaticdata.staticmember.lotfolbingpicheck = true; // FOL
                        }
                    }
                }
                else
                {
                    ShowMessage("獲取process工程資料異常！");
                    regdata();
                    return;
                }

                DataTable dt = cwa.CallRDT("onlinemap_submit_16", devicename);
                if (dt != null)
                {
                    if (dt.Rowscount() > 0)
                    {
                        SFCStaticdata.staticmember.IsMesDevice = true;
                    }
                    else
                    {
                        SFCStaticdata.staticmember.IsMesDevice = false;
                    }
                }
                else
                {
                    ShowMessage("獲取MES機種設定信息時發生錯誤！");
                    return;
                }


                DataTable lotstatusdt = cwa.CallRDT("onlinemap_submit_10", lotno);
                //VTQ-特殊批次不卡控  by hp 12-25
                String noholdlotno = cwa.CallRS("getspecialnoholddata", lotno);  //1表示不再進行Hold 空表示要Hold  不設定批號的情況下重工品不用Hold
                if (noholdlotno == null || noholdlotno.equals(""))
                {
                    if (lotno.contains("C"))
                    {
                        SFCStaticdata.staticmember.vtqnoholdfalg = "1";
                    }
                    else
                    {
                        SFCStaticdata.staticmember.vtqnoholdfalg = "";
                    }
                }
                else
                {
                    SFCStaticdata.staticmember.vtqnoholdfalg = "1";
                }

                //HC0806 良率超标卡批号
                if (strnewdeviceno.equals("HC0806"))
                {
                    DataTable dtqclotno = cwa.CallRDT("onlinemap_submit_11", lotno);
                    if (dtqclotno.Rowscount() > 0)
                    {
                        ShowMessage("当前批号在" + dtqclotno.Rows(0).get_CellValue("opno") + "因良率超标被锁,良率" + dtqclotno.Rows(0).get_CellValue("yield") + "，请联系qc解锁，并提交改善报告！");
                        return;
                    }
                }

                // HC0806单测podpog
                if (strnewdeviceno.equals("HC0806"))//(devicenotextBox.Text.Trim() .equals("HC0806")
                {
                    if (lotstatusdt.Rowscount() > 0)
                    {
                        if (lotstatusdt.Rows(0).get_CellValue("lotstate").equals("14"))
                        {
                            String lotnosum = cwa.CallRS("onlinemap_submit_14", lotno);//总数
                            String failqty = cwa.CallRS("onlinemap_submit_13", lotno);//单测数
                            if (failqty.equals("")) failqty = "0";

                            String checksimple = cwa.CallRS("onlinemap_submit_12");
                            if (checksimple.equals("")) checksimple = "0";

                            if (lotnosum.equals(""))
                            {
                                ShowMessage( "请去单测podpog！");
                                return;
                            }
                            else
                            {

                                if ((Float.parseFloat(failqty) / Float.parseFloat(lotnosum)) > Float.parseFloat(checksimple))
                                {
                                    boolean updatestate = cwa.CallRB("onlinemap_submit_15", lotno);
                                    if (updatestate)
                                    {
                                        ShowMessage("单测不良率超标！");
                                    }
                                    else
                                    {
                                        ShowMessage("更新单测数据失败,请联系咨询simplepodbak！");
                                        return;

                                    }
                                    return;
                                }
                                else
                                {
                                    boolean updatestate = cwa.CallRB("onlinemap_submit_15", lotno);
                                    if (updatestate)
                                    {
                                        updatestate = cwa.CallRB("onlinemap_submit_30", lotno);
                                        if (!updatestate)
                                        {
                                            ShowMessage("请让资讯将lotstate改为1！");
                                            return;
                                        }

                                        lotstatusdt = cwa.CallRDT("onlinemap_submit_10", lotno);
                                    }
                                    else
                                    {
                                        ShowMessage("更新单测数据失败，请联系咨询,请让资讯将lotstate改为1,simplepodbak-qty改为1");
                                    }
                                }
                            }
                        }
                    }
                }


                String lotnoKK = "";
                if (lotstatusdt.Rowscount() == 0)
                {
                    //新增首站檢測幾種  ABCF
                    //string lotnoKK = "";
                    if (lotno.length() == 12 || lotno.length() == 15)
                    {
                        lotnoKK = lotno;
                    }
                    else if (lotno.length() == 13 || lotno.length() == 16)
                    {
                        lotnoKK = lotno.substring(1, lotno.length()); //(1, lotno.length() - 1);
                    }

                    if (!SFCStaticdata.staticmember.devicenomacinfo)
                    {
                        if (!checklottype(lotnoKK))
                        {
                            return;
                        }
                    }
                }
                else
                {
                    String qcholdcheck = lotstatusdt.Rows(0).get_CellValue("lotstate");
                    if (qcholdcheck.equals("12"))
                    {
                        if (SFCStaticdata.staticmember.vtqnoholdfalg.equals("1"))  //VTQ 特殊批號放過
                        {

                        }
                        else
                        {
                            ShowMessage("批號為QC-Hold住產品，不可過站<EOL>");
                            regdata();
                            return;
                        }
                    }

                    DataTable dtlotholdqc = cwa.CallRDT("onlinemap_submit_17", lotno, lotstatusdt.Rows(0).get_CellValue("opno"));
                    if (dtlotholdqc.Rowscount() > 0)
                    {
                        boolean updatelotholdflag = cwa.CallRB("onlinemap_submit_18", lotno, lotstatusdt.Rows(0).get_CellValue("opno"));
                        ShowMessage("批號為QC-Hold住產品，不可過站<EOL>");
                    }
                }

                //檢測是否一條龍EOL first station
                if (lotstatusdt.Rowscount() == 0)
                {
                    DataTable tempcheckalotqtydt = cwa.CallRDT("onlinemap_submit_19", staticinputlotno);
                    if (tempcheckalotqtydt.Rowscount() == 1)
                    {
                        if (tempcheckalotqtydt.Rows(0).get_CellValue("lotstate").trim().substring(0, 1).equals("A")) //(tempcheckalotqtydt.Rows[0]["lotstate"].ToString().Trim().Substring(0, 1) .equals("A")
                        {
                            dieqty = cwa.CallRS("onlinemap_submit_20", "P" + staticinputlotno);
                            try
                            {
                                Integer.parseInt(dieqty);
                            }
                            catch(Exception e1)
                            {
                                ShowMessage("Get dieqty error");  // MessageBox.Show("Get dieqty error");
                                regdata();
                                return;
                            }

                        }
                    }
                }

                String lotstatus = null;
                int sendopflowid = 0;
                String nowopno = null;
                String checkindate = null, checkinuser = null;
                if (lotstatusdt == null)
                {
                    ShowMessage("獲取批號當前信息失敗");
                    regdata();
                    return;
                }


                if (lotstatusdt.Rowscount() == 1)
                {
                    lotstatus = lotstatusdt.Rows(0).get_CellValue("lotstate");
                    sendopflowid = Integer.parseInt(lotstatusdt.Rows(0).get_CellValue("opnoflowid"));
                    nowopno = lotstatusdt.Rows(0).get_CellValue("opno");
                    dieqty = lotstatusdt.Rows(0).get_CellValue("dieqty");
                    checkindate = lotstatusdt.Rows(0).get_CellValue("updatedate");//getstrDate(lotstatusdt.Rows(0).get_CellValue("updatedate").toString());
                    checkinuser = lotstatusdt.Rows(0).get_CellValue("userid");

                    if (locationaa.toUpperCase().equals("EOL"))
                    {
                        String nextopno = cwa.CallRS("getnextopno", processno,Integer.toString(sendopflowid+1));   // 下一站

                        String opatt2 = cwa.CallRS("getopatt2", nextopno);
                        if (!nextopno.equals("INV"))   // 最後 INV站位也無需
                        {
                            if (Integer.parseInt(opatt2) != 4)
                            {
                                if (Integer.parseInt(lotstatus) == 2)  // 只在出站的時候驗證是否可以  過站
                                {
                                    String opnoline = cwa.CallRS("getlineopflowid", processno);  // 獲取 掃線別的 站位

                                    if (opnoline == null  || opnoline.equals(""))
                                    {
                                        ShowMessage("掃描幾台線別站位尚未設定，無需驗證，請聯繫MIS！");
                                    }
                                    else
                                    {
                                        if (Integer.parseInt(opnoline) <= sendopflowid)  // 從選擇線別的站位開始檢測
                                        {
                                            DataTable dtlotlineinfo =cwa.CallRDT("getlotnolinelog", lotno);

                                            if (dtlotlineinfo.Rowscount() <= 0)
                                            {
                                                ShowMessage("批號線別信息丟失，請退站重新過站");
                                                regdata();
                                                return;
                                            }

                                            String lotline = dtlotlineinfo.Rows(0).get_CellValue("charvalue").trim();

                                            DataTable dt_opnolotstate  = cwa.CallRDT("getopnolot", nextopno, lotline);
                                            // 查看當前待出站批號 所在線體 所在站位的下一站有無批號 WIP

                                            if (dt_opnolotstate.Rowscount() > 0)  //K30430009-16
                                            {
                                                boolean lotbtween = false;

                                                String lotnostringaa = "";
                                                String[] lotnoitems = { };
                                                DataTable dtlotdis = new DataTable();
                                                dtlotdis.AddColumn("dislotno");

                                                for (int a = 0; a < dt_opnolotstate.Rowscount(); a++)
                                                {
                                                    String opnolotnow = dt_opnolotstate.Rows(a).get_CellValue("lotno").trim();
                                                    //String opnoshowlot = opnolotnow;

                                                    String[] lotnosplit = opnolotnow.split("-");
                                                    opnolotnow = lotnosplit[0];
                                                    if (opnolotnow.length() == 10)
                                                    {
                                                        opnolotnow =opnolotnow.substring(1,10); //opnolotnow.Substring(1, 9);
                                                    }
                                                    else if (opnolotnow.length() == 9)
                                                    {
                                                    }
                                                    else
                                                    {
                                                        ShowMessage("批號不符邏輯，知悉");
                                                        regdata();
                                                        return;
                                                    }

                                                    if (!opnolotnow.equals(lotnosub))
                                                    {
                                                        lotbtween = true;
                                                        lotnostringaa = lotnostringaa + "<>" + opnolotnow;
                                                        if (dtlotdis.Rowscount() == 0)
                                                        {
                                                            //dtlotdis.Rows.Add(opnolotnow);
                                                            dtlotdis.AddRow(opnolotnow);
                                                        }
                                                        else
                                                        {
                                                            boolean aa = true;
                                                            for (int j = 0; j < dtlotdis.Rowscount(); j++)
                                                            {
                                                                String lothasin = dtlotdis.Rows(j).get_CellValue(0).trim();
                                                                if (lothasin.equals(opnolotnow))
                                                                {
                                                                    dtlotdis.AddRow(opnolotnow);
                                                                    aa = false;
                                                                    //continue;
                                                                }

                                                            }
                                                            if (aa)
                                                            {
                                                                dtlotdis.AddRow(opnolotnow); //dtlotdis.Rows.Add(opnolotnow);
                                                            }
                                                        }
                                                    }
                                                }
                                                if (lotbtween)
                                                {
                                                    if (dtlotdis.Rowscount() >= 2)
                                                    {
                                                        if (MessageBox("过站提醒", "當前站位已被母批" + lotnostringaa + "占用，是否需要強行過站 ?") == BaseFuncation.DialogResult.OK) {
                                                            SFCStaticdata.staticmember.checklotsub = false;
                                                            //Creat New Activity
                                                            CreatNewActivity(lotsubopnoinput.class,lotno,nowopno,Integer.toString(sendopflowid),lotstatus);
                                                            //  //lotsubopnoinput lotsubin = new lotsubopnoinput(lotno, nowopno, sendopflowid.ToString(), lotstatus);
                                                            // //lotsubin.ShowDialog();
                                                            if (!SFCStaticdata.staticmember.checklotsub)  // 如果 意外關閉，則會檢測是否正確驗證帳戶。
                                                            {
                                                                //MessageBox.Show("帳戶認證失敗，請重新進站");
                                                                regdata();
                                                                return;
                                                            }
                                                            else {
                                                                regdata();
                                                                return;
                                                            }

                                                        }
                                                        /*//if (DialogResult.Yes == MessageBox.Show("當前站位已被母批" + lotnostringaa + "占用，是否需要強行過站 ?", "單擊繼續", MessageBoxButtons.YesNo, MessageBoxIcon.Question))
                                                        //{
                                                        //   app.checklotsub = false;
                                                        //  //lotsubopnoinput lotsubin = new lotsubopnoinput(lotno, nowopno, sendopflowid.ToString(), lotstatus);
                                                        // //lotsubin.ShowDialog();

                                                        //if (!app.checklotsub)  // 如果 意外關閉，則會檢測是否正確驗證帳戶。
                                                        //{
                                                        //    //MessageBox.Show("帳戶認證失敗，請重新進站");
                                                        //    regdata();
                                                        //    return;
                                                        //}
                                                        //}
                                                        //else
                                                        //{
                                                        //   regdata();
                                                        //   return;
                                                        //}*/
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (locationaa.equals("FOL"))
                    {
                        String opatt2 = cwa.CallRS("getopatt2", nowopno);

                        String lhcheckatt6 = cwa.CallRS("getlhchecksnatt6");

                        if (lhcheckatt6 == null || lhcheckatt6.equals(""))
                        {
                            ShowMessage("分并批站位尚未設定，請聯繫MIS！t_lhchecksnitemsetting");
                            regdata();
                            return;
                        }

                        char[] bbb = { '-' };
                        char[] ccc = { ';' };
                        String[] spilitopno = lhcheckatt6.split(";");
                        String[] lotnostrnew = staticinputlotno.split("-");
                        if (lotnostrnew.length >= 3)
                        {
                            if (Integer.parseInt(opatt2) == Integer.parseInt(spilitopno[1]))
                            {
                                ShowMessage("該批號已經到達并批站位，不可過站，請及時并批！" + spilitopno[1]);  //MessageBox.Show("該批號已經到達并批站位，不可過站，請及時并批！" + spilitopno[1]);
                                regdata();
                                return;
                            }
                        }
                    }
                }
                else
                {
                    lotstatus = "1";
                    sendopflowid = 1;

                    nowopno = cwa.CallRS("onlinemap_submit_21", processno);
                    if (nowopno.equals("") ||  nowopno == null)
                    {
                        ShowMessage("獲取流程信息失敗或流程设置有");  //MessageBox.Show("");
                        regdata();
                        return;
                    }
                }


                if (lotstatus.equals("4"))
                {
                    ShowMessage("批號狀態為已入庫");  //MessageBox.Show("批號狀態為已入庫");
                    regdata();
                    return;
                }
                if (lotstatus.equals("13"))
                {
                    ShowMessage("不良數目不符 LOTSTATUS = 13，請聯繫MIS");  //MessageBox.Show("不良數目不符 LOTSTATUS = 13，請聯繫MIS");
                    regdata();
                    return;
                }

                // 三段式工單檢測EOL對應的FOL批號
                if (SFCStaticdata.staticmember.deviceno.equals("MAINE"))
                {
                    DataTable bitsever = cwa.CallRDT("onlinemap_submit_22", SFCStaticdata.staticmember.deviceno);
                    String nowflowid = cwa.CallRS("onlinemap_submit_23", nowopno);

                    if (bitsever.Rowscount() > 0 && nowflowid.equals("1"))
                    {
                        if (!lotno.startsWith("AK"))
                        {
                            String lotnotemp = cwa.CallRS("onlinemap_submit_24", lotno);

                            if (lotnotemp == null)
                            {
                                ShowMessage("查詢" + lotno + "對應的前段批號異常，請聯繫MIS");  //MessageBox.Show("查詢" + lotno + "對應的前段批號異常，請聯繫MIS", "提示");
                                return;
                            }
                            else if (lotnotemp.equals(""))
                            {
                                ShowMessage("未查詢到" + lotno + "對應的前段批號");  // MessageBox.Show("未查詢到" + lotno + "對應的前段批號", "提示");
                                return;
                            }
                        }
                    }
                }

                //檢測用戶名    liang / 0808
                String OQCflag = cwa.CallRS("getopatt2", nowopno);
                if (OQCflag == null || OQCflag.equals(""))
                {
                    ShowMessage("站位特性尚未设定（att2）");  //MessageBox.Show("站位特性尚未设定（att2）");
                    return;
                }

                DataTable dt_checkemployeeno = cwa.CallRDT("onlinemap_submit_25", userid);

                if (dt_checkemployeeno != null)
                {
                    if (dt_checkemployeeno.Rowscount() == 0)
                    {
                        ShowMessage("無此用戶名﹐請聯系MIS部門新增");
                        regdata();
                        return;
                    }

                    if (!dt_checkemployeeno.Rows(0).get_CellValue("employeeno").trim().equals(userid))
                    {
                        ShowMessage("用戶名檢測無法通過");
                        regdata();
                        return;
                    }
                    else
                    {
                        if (!dt_checkemployeeno.Rows(0).get_CellValue("groupid").trim().equals("G001"))
                        {
                            String tgroupatt1 = dt_checkemployeeno.Rows(0).get_CellValue("att1").trim();  //    0 无需验证的群组 / 1 需要验证的群组
                            usergroup = dt_checkemployeeno.Rows(0).get_CellValue("groupid").trim();

                            try
                            {

                                dt_checkemployeeno = cwa.CallRDT("onlinemap_submit_26", usergroup, nowopno);
                                if (dt_checkemployeeno.Rowscount() <= 0)
                                {
                                    String opname = cwa.CallRS("getopname", nowopno);
                                    ShowMessage("此用戶沒有當前站位-->  " + opname + "  <-- 过站權限<  " + nowopno + "  >，無法過此站位！！！");  //MessageBox.Show("此用戶沒有當前站位-->  " + opname + "  <-- 过站權限<  " + nowopno + "  >，無法過此站位！！！");
                                    regdata();
                                    return;
                                }
                                else  // add opno check  0519
                                {
                                    if (tgroupatt1 == null  || tgroupatt1.equals(""))
                                    {
                                    }
                                    else
                                    {
                                        if (tgroupatt1.equals("1"))
                                        {
                                            String dtcehckempatt3 = dt_checkemployeeno.Rows(0).get_CellValue("att3").trim();
                                            if (dtcehckempatt3 == null  || dtcehckempatt3.equals(""))
                                            {
                                            }
                                            else
                                            {
                                                if (dtcehckempatt3.equals("1"))   // in
                                                {
                                                    if (lotstatus.equals("2"))
                                                    {
                                                        String opname = cwa.CallRS("getopname", nowopno);
                                                        ShowMessage("此用戶沒有當前站位--> " + opname + "  <-- 进站權限<  " + nowopno + "  >，無法进站！！！");  // MessageBox.Show("此用戶沒有當前站位-->  " + opname + "  <-- 出站權限<  " + nowopno + "  >，無法出站！！！");
                                                        regdata();
                                                        return;
                                                    }
                                                }
                                                else if (dtcehckempatt3.equals("2"))    // out
                                                {
                                                    if (lotstatus.equals("1"))
                                                    {
                                                        String opname = cwa.CallRS("getopname", nowopno);
                                                        ShowMessage("此用戶沒有當前站位--> " + opname + "  <-- 进站權限<  " + nowopno + "  >，無法进站！！！");  // MessageBox.Show("此用戶沒有當前站位-->  " + opname + "  <-- 进站權限<  " + nowopno + "  >，無法进站！！！");
                                                        regdata();
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            catch(Exception e1)
                            {
                                ShowMessage("查詢群組權限時發生錯誤");  //MessageBox.Show("查詢群組權限時發生錯誤"); regdata();
                                return;
                            }
                        }
                    }
                }
                else
                {
                    ShowMessage("用戶名檢測無法通過");  //MessageBox.Show("用戶名檢測無法通過");
                    regdata();
                    return;
                }


                SFCStaticdata.staticmember.userid = userid;
                // VTQ  FIFO  wuhp  15-11-28LIAO
                String getlinesql = "";
                String getline = "";
                String areastr = "";
                if (lotno.substring(0, 1).trim().equals("S"))
                {
                    areastr = "S";
                    getline = cwa.CallRS("onlinemap_submit_27", lotno, "C0004");
                }
                else if (lotno.substring(0,1).trim().equals("P"))
                {
                    areastr = "P";
                    getline = cwa.CallRS("onlinemap_submit_27", lotno, "C0003");
                }
                else
                {
                    areastr = "A";
                    getline = cwa.CallRS("onlinemap_submit_27", lotno, "C0005");
                }

                if ((SFCStaticdata.staticmember.fifoflag.contains("A") && lotno.contains("A")) || (SFCStaticdata.staticmember.fifoflag.contains("S") && lotno.contains("S")) || (SFCStaticdata.staticmember.fifoflag.contains("P") && lotno.contains("P")))
                {
                    if (lotno.contains("E") || lotno.contains("D") || lotno.contains("C") || lotno.contains("987"))  //特殊批次放過
                    {
                    }
                    else
                    {
                        DataTable fidt = cwa.CallRDT("getgofifodata", lotno);  //特殊批號放過
                        if (fidt != null)
                        {
                            if (fidt.Rowscount() > 0)
                            {
                            }
                            else
                            {
                                DataTable redt = cwa.CallRDT("getreturndata", lotno);   //退站批號不卡
                                if (redt != null)
                                {
                                    if (redt.Rowscount() > 0)
                                    {
                                    }
                                    else
                                    {
                                        DataTable qredt = cwa.CallRDT("getqcreturndata", lotno);     //Q退的批號不卡
                                        if (qredt != null)
                                        {
                                            if (qredt.Rowscount() > 0)
                                            {
                                            }
                                            else
                                            {
                                                if (sendopflowid == 1)    //首站卡控WIP開單數量
                                                {
                                                    if (lotstatus.equals("1"))    //卡首站進站時按批號的先後循序進站
                                                    {
                                                        if (areastr.equals("P") || areastr.equals("S"))
                                                        {
                                                            int nowno = Integer.parseInt(lotno.substring((lotno.indexOf("-") + 1), lotno.indexOf("-")+3));//int.Parse(lotno.Substring((lotno.IndexOf("-") + 1), 2).toString().trim());
                                                            String molotno =lotno.substring(0, (lotno.indexOf("-") + 1)).trim(); //lotno.Substring(0, lotno.IndexOf("-")).toString().trim();

                                                            String temparea = "";
                                                            if (areastr.equals("P"))
                                                            {
                                                                temparea = "K";
                                                            }
                                                            else
                                                            {
                                                                temparea = "S";
                                                            }

                                                            DataTable molotsdt = cwa.CallRDT("getmolotnostatedata", molotno);
                                                            if (molotsdt != null && molotsdt.Rowscount() > 0)
                                                            {
                                                                //檢測子批的狀態表，如果有，就按FIFO管控
                                                                String lastlotno = molotsdt.Rows(0).get_CellValue("lotno").trim();

                                                                int dbnowno =  Integer.parseInt(lastlotno.substring((lastlotno.indexOf("-") + 1), lotno.indexOf("-") + 3));//int.Parse(lastlotno.Substring((lastlotno.IndexOf("-") + 1), 2).toString().trim());
                                                                int nextno = Integer.parseInt(lastlotno.substring((lastlotno.indexOf("-") + 1), lotno.indexOf("-")+3))+1;// int.Parse(lastlotno.Substring((lastlotno.IndexOf("-") + 1), 2).toString().trim()) + 1;

                                                                if (nextno != nowno)
                                                                {
                                                                    if (nowno <= nextno)
                                                                    {
                                                                    }
                                                                    else
                                                                    {
                                                                        String temsplitstr = "00" +Integer.toString(nextno); // "00" + nextno.toString().trim();
                                                                        temsplitstr =temsplitstr.substring(temsplitstr.length() - 2, temsplitstr.length()); //temsplitstr.Substring(temsplitstr.length() - 2, 2);
                                                                        //String splitlotno = molotno + "-" + temsplitstr;

                                                                        ShowMessage("根據先進先出的原則，請先過" + temsplitstr + "子批！");  // MessageBox.Show("根據先進先出的原則，請先過" + temsplitstr + "子批", "溫馨提示");
                                                                        return;
                                                                    }
                                                                }

                                                            }
                                                            else
                                                            {
                                                                // 如果state表和log表都為空，就檢查母批
                                                                DataTable firstdt = cwa.CallRDT("getwiprevisiondatas", temparea); //不管控重工批次和工程批
                                                                if (firstdt != null)
                                                                {
                                                                    if (firstdt.Rowscount() > 0)
                                                                    {
                                                                        String fifolotno = firstdt.Rows(0).get_CellValue("lotno").trim();
                                                                        String nowmolotno = lotno.substring(1, (lotno.indexOf("-"))).trim();//lotno.substring(1, (lotno.IndexOf("-") - 1)).toString().trim();
                                                                        String snowmolotno = lotno.substring(0, lotno.indexOf("-")).trim();
                                                                        if (areastr.equals("S"))
                                                                        {
                                                                            if (!fifolotno.equals(snowmolotno))
                                                                            {
                                                                                String exlotno = cwa.CallRS("checkwipdatadata", fifolotno);  //如果WIP中有刪除，就放過
                                                                                if (exlotno==null || exlotno.equals("") || exlotno.equals("0"))
                                                                                {
                                                                                    boolean upmflag = cwa.CallRB("upwipversiondata1", fifolotno);
                                                                                }
                                                                                else
                                                                                {
                                                                                    ShowMessage("根據先進先出的原則，應該先過" + fifolotno + "母批號！");  //MessageBox.Show("根據先進先出的原則，應該先過" + fifolotno + "母批號", "溫馨提示");
                                                                                    return;
                                                                                }
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            if (!nowmolotno.equals(fifolotno))
                                                                            {
                                                                                String exlotno = cwa.CallRS("checkwipdatadata", fifolotno);  //如果WIP中有刪除，就放過
                                                                                if (exlotno == null || exlotno.equals("") || exlotno.equals("0"))
                                                                                {
                                                                                    boolean upmflag = cwa.CallRB("upwipversiondata1", fifolotno);
                                                                                }
                                                                                else
                                                                                {
                                                                                    ShowMessage("根據先進先出的原則，應該先過" + fifolotno + "母批號！");  //MessageBox.Show("根據先進先出的原則，應該先過" + fifolotno + "母批號", "溫馨提示");
                                                                                    return;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        if (nowno != 1)
                                                                        {
                                                                            ShowMessage("根據先進先出的原則，請先過該母批" + molotno + "的 01 子批！");  //MessageBox.Show("根據先進先出的原則，請先過該母批" + molotno + "的 01 子批", "溫馨提示");
                                                                            return;
                                                                        }
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    ShowMessage("獲取TBLWIPRIVersion表先進先出數據時發生錯誤，請聯繫MIS部門！");  //MessageBox.Show("獲取TBLWIPRIVersion表先進先出數據時發生錯誤，請聯繫MIS部門", "溫馨提示");
                                                                    return;
                                                                }

                                                            }

                                                        }
                                                        else
                                                        {
                                                            //if (lotno.Substring(6, 1).ToString().Trim() .equals("C" || lotno.Substring(6, 1).ToString().Trim() .equals("E" || lotno.Substring(6, 1).ToString().Trim() .equals("D")   //重工批不卡
                                                            if (lotno.substring(6, 7).trim().equals("C") ||lotno.substring(6,7).trim().equals("E") ||lotno.substring(6, 7).trim().equals("D"))   //重工批不卡
                                                            {
                                                            }
                                                            else
                                                            {
                                                                DataTable modt = new DataTable();
                                                                if (strnewdeviceno.equals("VT-Q"))//((devicenotextBox.Text.Trim() .equals("VT-Q"))
                                                                {
                                                                    modt = cwa.CallRDT("getdistinctmolotnodata1byline", getline);
                                                                }
                                                                else
                                                                {
                                                                    modt = cwa.CallRDT("getdistinctmolotnodata1");  //查尋在當前站位的母批號
                                                                }
                                                                if (modt != null)
                                                                {
                                                                    if (modt.Rowscount() > 0)
                                                                    {
                                                                        String fristlotno = modt.Rows(0).get_CellValue("templotno").trim();
                                                                        if (!fristlotno.equals(lotno))
                                                                        {
                                                                            ShowMessage("根據先進先出的原則，請先過這個批號" + fristlotno + "的！");  //MessageBox.Show("根據先進先出的原則，請先過這個批號" + fristlotno + "的", "溫馨提示");
                                                                            return;
                                                                        }

                                                                    }

                                                                }

                                                            }
                                                        }

                                                    }
                                                    else
                                                    {
                                                        if (areastr.equals("P") || areastr.equals("S"))
                                                        {
                                                            //  卡控FIFO
                                                            DataTable modt = new DataTable();
                                                            if (strnewdeviceno.equals("VT-Q"))//((devicenotextBox.Text.Trim() .equals("VT-Q"))
                                                            {
                                                                modt = cwa.CallRDT("getdistinctmolotnodatabyline", nowopno, lotstatus, getline);
                                                            }
                                                            else
                                                            {
                                                                modt = cwa.CallRDT("getdistinctmolotnodata", nowopno, lotstatus);
                                                            }
                                                            if (modt != null)
                                                            {
                                                                if (modt.Rowscount() > 0)
                                                                {
                                                                    String firstlotno = modt.Rows(0).get_CellValue("lotno").trim();
                                                                    if (!firstlotno.equals(lotno))
                                                                    {
                                                                        ShowMessage("根據先進先出的原則，請先過這個 " + firstlotno + " 子批！");  //MessageBox.Show("根據先進先出的原則，請先過這個 " + firstlotno + " 子批", "溫馨提示");
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        else   //EOL不卡
                                                        {
                                                        }
                                                    }
                                                }
                                                else    //非首站
                                                {
                                                    //  卡控FIFO
                                                    if (SFCStaticdata.staticmember.lotcheckeolflag)  //EOL，SMT卡控原則
                                                    {
                                                        // 出站不卡
                                                        //if (lotstatus .equals("2" || lotno.Substring(6, 1).ToString().Trim() .equals("E" || lotno.Substring(6, 1).ToString().Trim() .equals("D" || lotno.Substring(6, 1).ToString().Trim() .equals("C")   //重工批不卡
                                                        if (lotstatus.equals("2") || lotno.substring(6, 7).trim().equals("E") || lotno.substring(6, 7).trim().equals("D") || lotno.substring(6, 7).trim().equals("C"))   //重工批不卡
                                                        {
                                                        }
                                                        else
                                                        {
                                                            DataTable modt = new DataTable();
                                                            if(strnewdeviceno.equals("VT-Q")) //((devicenotextBox.Text.Trim() .equals("VT-Q"))
                                                            {
                                                                //modt = new BLL.basedata().getdistinctmolotnodatabyline(nowopno, lotstatus, getline, "");
                                                                modt=cwa.CallRDT("getdistinctmolotnodatabyline",nowopno, lotstatus, getline, "");
                                                            }
                                                            else
                                                            {
                                                                //modt = new BLL.basedata().getdistinctmolotnodata(nowopno, lotstatus, "");
                                                                modt=cwa.CallRDT("getdistinctmolotnodata",nowopno, lotstatus, "");
                                                            }

                                                            if (modt != null)
                                                            {
                                                                if (modt.Rowscount() > 0)
                                                                {
                                                                    String firstlotno = modt.Rows(0).get_CellValue("lotno").trim();
                                                                    if (!firstlotno.equals(lotno))
                                                                    {
                                                                        ShowMessage("根據先進先出的原則，請先過這個 " + firstlotno + " 子批！");  //MessageBox.Show("根據先進先出的原則，請先過這個 " + firstlotno + " 子批", "溫馨提示");
                                                                        return;
                                                                    }
                                                                }

                                                            }

                                                        }

                                                    }
                                                    else
                                                    {
                                                        //HP 17-02-21  FOL 特殊站位卡控母批
                                                        String mocontralfalg =cwa.CallRS("getmocontralfalgsetdata",nowopno); //new BLL.basedata().getmocontralfalgsetdata(nowopno);

                                                        DataTable modt = new DataTable();
                                                        if(strnewdeviceno.equals("VT-Q")) // ((devicenotextBox.Text.Trim() .equals("VT-Q"))
                                                        {
                                                            //modt = new BLL.basedata().getdistinctmolotnodatabyline(nowopno, lotstatus, getline, mocontralfalg);
                                                            modt =cwa.CallRDT("getdistinctmolotnodatabyline",nowopno, lotstatus, getline, mocontralfalg);
                                                        }
                                                        else
                                                        {
                                                            //modt = new BLL.basedata().getdistinctmolotnodata(nowopno, lotstatus, mocontralfalg);
                                                            modt =cwa.CallRDT("getdistinctmolotnodata",nowopno, lotstatus, mocontralfalg);
                                                        }

                                                        if (modt != null)
                                                        {
                                                            if (modt.Rowscount() > 0)
                                                            {
                                                                if (mocontralfalg == null  || mocontralfalg.equals(""))
                                                                {
                                                                    String firstlotno = modt.Rows(0).get_CellValue("lotno").trim();
                                                                    if (!firstlotno.equals(lotno))
                                                                    {
                                                                        ShowMessage("根據先進先出的原則，請先過這個" + firstlotno + " 子批！");  // MessageBox.Show("根據先進先出的原則，請先過這個 " + firstlotno + " 子批", "溫馨提示");
                                                                        return;
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    String firstmolotno = modt.Rows(0).get_CellValue("lotno").trim();

                                                                    if (!firstmolotno.equals(lotno.substring(1, 10).trim())) //(firstmolotno != lotno.Substring(1, 9).ToString().Trim())
                                                                    {
                                                                        ShowMessage("根據先進先出的原則，請先過這個 " + firstmolotno + " 母批！");  //MessageBox.Show("根據先進先出的原則，請先過這個 " + firstmolotno + " 母批", "溫馨提示");
                                                                        return;
                                                                    }
                                                                }
                                                            }

                                                        }

                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            ShowMessage("獲取Q退的批號時發生錯誤，請聯繫MIS部門！");  //MessageBox.Show("獲取Q退的批號時發生錯誤，請聯繫MIS部門", "溫馨提示");
                                            return;
                                        }
                                    }
                                }
                                else
                                {
                                    ShowMessage("獲取該批號是否有退站時發生錯誤，請聯繫MIS部門！");  //MessageBox.Show("獲取該批號是否有退站時發生錯誤，請聯繫MIS部門", "溫馨提示");
                                    return;
                                }

                            }
                        }
                        else
                        {
                            ShowMessage("獲取特殊放過FIFO的批號時發生錯誤，請聯繫MIS部門！");  //MessageBox.Show("獲取特殊放過FIFO的批號時發生錯誤，請聯繫MIS部門", "溫馨提示");
                            return;
                        }

                    }
                }

                //VT-Q機種卡test37站位重工是否要退站
                if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") || SFCStaticdata.staticmember.newdeviceno.equals("ATF001"))
                {
                    DataTable re37dt = cwa.CallRDT("getretestholddata", lotno);
                    if (re37dt != null)
                    {
                        if (re37dt.Rowscount() > 0)
                        {
                            String retimes = re37dt.Rows(0).get_CellValue("times").trim();
                            String reatt1 = re37dt.Rows(0).get_CellValue("att1").trim();
                            if (!retimes.equals("1") && !reatt1.equals("1"))
                            {
                                String reopno = re37dt.Rows(0).get_CellValue("opno").trim();
                                if (!reopno.equals(nowopno))
                                {
                                    ShowMessage("該批號因為不良超標已經被HOLD住，請退站重測37站再進行過站！");  //MessageBox.Show("該批號因為不良超標已經被HOLD住，請退站重測37站再進行過站", "溫馨提示");
                                    return;
                                }
                            }

                        }
                    }
                    else
                    {
                        ShowMessage("獲取VT-Q QC重測數據時失敗！");  //MessageBox.Show("獲取VT-Q QC重測數據時失敗", "溫馨提示");
                        return;
                    }
                }

                //VTQ良率超标卡批号
                DataTable dt_q=cwa.CallRDT("onlinemap_submit_31",SFCStaticdata.staticmember.newdeviceno );
                if (dt_q == null)
                {
                    ShowMessage("查询该机种良率超标是否HOLD失敗！");  //MessageBox.Show("查询该机种良率超标是否HOLD失敗");
                    return;
                }
                String setopno = "";
                if (dt_q.Rowscount() > 0)
                {
                    setopno = dt_q.Rows(0).get_CellValue("ITEM");
                }
                if (setopno.equals(SFCStaticdata.staticmember.newdeviceno))
                {
                    if (SFCStaticdata.staticmember.vtqnoholdfalg.equals("1"))  //VTQ 特殊批號放過
                    {
                    }
                    else
                    {
                        DataTable vtqholalot = cwa.CallRDT("onlinemap_submit_28", lotno);
                        if (vtqholalot.Rowscount() > 0)
                        {
                            String holdtype = vtqholalot.Rows(0).get_CellValue("att1").trim();
                            String holdopno = vtqholalot.Rows(0).get_CellValue("opno").trim();

                            if (nowopno.equals(holdopno))
                            {
                                ShowMessage("当前批号在" + vtqholalot.Rows(0).get_CellValue("opno") + "因"+holdtype+"被锁,请联系qc解锁，并提交改善报告！");  //MessageBox.Show("当前批号在" + vtqholalot.Rows[0]["opno"].ToString() + "因良率超标被锁,请联系qc解锁，并提交改善报告");
                                return;
                            }
                        }
                    }
                }


                //卡控FOL出站到EOL ACF站位必須大於6個小時
                DataTable overdt = cwa.CallRDT("getcheckovertimeopnodata", nowopno);
                if (overdt != null)
                {
                    DataTable overlotno = cwa.CallRDT("getcheckovertimelotnodata", lotno);
                    if (overlotno.Rowscount() > 0)
                    {
                    }
                    else if (lotno.contains("C"))  //特殊批次放過
                    {
                    }
                    else
                    {
                        if (overdt.Rowscount() > 0)
                        {
                            String oatt2 = overdt.Rows(0).get_CellValue("att2").trim();
                            String checkopno = overdt.Rows(0).get_CellValue("att1").trim();
                            String overdtime = cwa.CallRS("getovertimedata1", lotno, checkopno);
                            if (Double.parseDouble(oatt2)>Double.parseDouble(overdtime))
                            {
                                ShowMessage("該批次從FOL Cube Assembly出站时间至EOL ACF 站位进站时间间隔為" + overdtime + "小時，小於" + oatt2 + "小時，不能過站");  // MessageBox.Show("該批次從FOL Cube Assembly出站时间至EOL ACF 站位进站时间间隔為" + overdtime + "小時，小於" + oatt2 + "小時，不能過站", "溫馨提示");
                                return;
                            }
                        }
                    }
                }


                //QC外觀站位進站增加提示功能 BY 戈霄
                if (lotstatusdt.Rowscount() > 0)
                {
                    lotstatus = lotstatusdt.Rows(0).get_CellValue("lotstate");
                }
                //这样下去迟早会被你们玩死，能不能不要写这么完美的代码！~
                if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") && lotstatus.equals("1") && nowopno.equals("0002_4") && lotno.contains("A"))
                {
                    SFCStaticdata.staticmember.IsQccheck = true;
                    //QCcheck qc = new QCcheck(lotno);
                    //qc.ShowDialog();
                    ShowMessage(" 跳转到页面 QCcheck ");

                    if (!SFCStaticdata.staticmember.IsQccheck) {
                        regdata();
                        return;
                    }
                }
                String nowopno_1 = nowopno;
                boolean checkinoutflag = false;
                boolean flag3 = false;//是否執行了case 3:
                while (!checkinoutflag)
                {
                    String att11 = "";
                    String opnotype = cwa.CallRS("getopatt2",nowopno);
                    if (opnotype == null || opnotype.equals(""))
                    {
                        ShowMessage("站位特性尚未设定（att2）");  //MessageBox.Show("站位特性尚未设定（att2）");
                        return;
                    }
                    switch (opnotype)
                    {
                        //no check in/out
                        case "3"://no check in/out
                            checkinoutflag = false;
                            String nextopno = cwa.CallRS("getnextopno", processno,Integer.toString(sendopflowid + 1));   // 下一站
                            flag3 = true;
                            String lotstate333 = "1";
                            String lotserial =lotno + "-" +String.format("%03", sendopflowid); //lotno + "-" + (sendopflowid).toString("000");
                            String checkoutip = SFCStaticdata.staticmember.HDSerialNo;//SFCStaticdata.staticmember.ip;
                            String checkoutpcname ="";// System.Net.Dns.GetHostName().toString().trim();
                            if (nextopno.equals("INV"))
                            {
                                lotstate333 = "4";
                                checkinoutflag = true;
                            }
                            //String[] parmers333 = { nextopno,Integer.toString(sendopflowid + 1), dieqty, lotstate333, SFCStaticdata.staticmember.userid, lotno, lotserial, lotno, nowopno, Integer.toString(sendopflowid), dieqty, dieqty, checkindate, checkinuser, SFCStaticdata.staticmember.userid, checkoutpcname, checkoutip };

                            boolean execflag = cwa.CallRB("onlinemap_submit_29", nextopno, Integer.toString(sendopflowid + 1), dieqty,
                                    lotstate333, SFCStaticdata.staticmember.userid, lotno, lotserial, nowopno, Integer.toString(sendopflowid),
                                    checkindate, checkinuser, checkoutpcname, checkoutip
                            );

                            if (execflag)
                            {
                                if (checkinoutflag)
                                {
                                    ShowMessage(nowopno_1 + "無需Check In/Out，已移至INV");  //MessageBox.Show(nowopno_1 + "無需Check In/Out，已移至INV");
                                    regdata();
                                    //showdata();
                                    return;
                                }

                                nowopno = nextopno;
                                sendopflowid++;
                                DataTable tempcheckindt = new DataTable();
                                tempcheckindt = cwa.CallRDT("getcheckindata", lotno);
                                if (tempcheckindt != null)
                                {
                                    if (tempcheckindt.Rowscount() == 1)
                                    {
                                        checkindate = getstrDate(tempcheckindt.Rows(0).get_CellValue("updatedate"));
                                        checkinuser = tempcheckindt.Rows(0).get_CellValue("userid");
                                    }
                                    else
                                    {
                                        ShowMessage("無法獲取checkin資料﹐請聯系MIS部門！");  // MessageBox.Show("無法獲取checkin資料﹐請聯系MIS部門");
                                        return;
                                    }
                                }
                                else
                                {
                                    ShowMessage("無法獲取checkin資料﹐請聯系MIS部門！");  //MessageBox.Show("無法獲取checkin資料﹐請聯系MIS部門");
                                    return;
                                }
                            }
                            else
                            {
                                ShowMessage("批號：" + lotno + "在站位：" + nowopno + "過站失敗,請聯系MIS部門");  //MessageBox.Show("批號：" + lotno + "在站位：" + nowopno + "過站失敗,請聯系MIS部門");
                                return;
                            }
                            break;
                        case "7":
                            checkinoutflag = true;
                            // 一条龙 、、、三段式  都去检测 是否有收料过程
                            DataTable dt_lotkit = cwa.CallRDT("getlotkitinfo", lotnooldnndd);   //lotnooldnndd
                            if (SFCStaticdata.staticmember.AllReworkFlag)
                            {
                                if(Integer.parseInt(lotstatus)==1) //(int.Parse(lotstatus) == 1)
                                {
                                    ShowMessage(" 跳转到页面 Checkin ");
                                    regdata();
                                    //showdata();

                                    //Creat New Activity
                                    Intent PublicLoading = new Intent(sfconline.this, app.cmapp.IncludingSFC.checkin.class);
                                    Bundle bundler = new Bundle();
                                    bundler.putString("lotno",lotno);
                                    bundler.putString("dieqty", dieqty);
                                    bundler.putString("opno",nowopno);
                                    bundler.putString("productno", productno);
                                    bundler.putString("processno",processno);
                                    bundler.putString("sendopflowid",Integer.toString(sendopflowid));
                                    PublicLoading.putExtras(bundler);
                                    startActivity(PublicLoading);
                                    //Checkin ci = new Checkin(lotno,Integer.parseInt(dieqty), nowopno, productno, processno, sendopflowid);
                                    //ci.ShowDialog();

                                }
                                else
                                {
                                    if (lotstatus.equals("2"))
                                    {
                                        SFCStaticdata.staticmember.updatelotcartflag = true;      // 有收料過程標誌位改變

                                        //出站时选AB线别
                                        boolean linechange = cwa.CallRB("getlinechangeopno", nowopno);

                                        if (linechange)
                                        {
                                            ShowMessage("因出站时选择线别错误被锁，使用有权限的工号解锁！");  //MessageBox.Show("因出站时选择线别错误被锁，使用有权限的工号解锁");
                                            String linelot = cwa.CallRS("getlinelot", lotno);
                                            if (linelot.equals(lotno))
                                            {
                                                SFCStaticdata.staticmember.lineflag = false;
                                                //Creat New Activity
                                                //unholdlinelot un = new unholdlinelot(lotno);
                                                //un.ShowDialog();
                                                ShowMessage(" 跳转到页面 unholdlinelot ");
                                                return;
                                            }
                                        }
                                        ShowMessage(" 跳转到页面 Checkout ");
                                        regdata();
                                        //showdata();

                                        //Creat New Activity
                                        CreatNewActivity(checkout.class, strdeviceno,strnewdeviceno,SFCStaticdata.staticmember.odbname, lotno, dieqty, nowopno, productno, processno, Integer.toString(sendopflowid));
                                    }
                                }
                            }
                            else if (dt_lotkit.Rowscount() > 0)
                            {
                                //补充WIP分批后SFC里的资料
                                att11 = cwa.CallRS("getatt11data", nowopno);
                                if (att11.equals("2"))  //2代表补充SFC里分批后的数据
                                {
                                    if (SFCStaticdata.staticmember.eolspitflag.equals("1"))   //分小子批标记位
                                    {
                                        try
                                        {
                                            //int tempdtqty = 0;
                                            //String returnstr = "";
                                            String str = getspitdata(etlotno.getText().toString().trim(), SFCStaticdata.staticmember.odbname);
                                            if (str == null || str.equals(""))
                                            {
                                            }
                                            else
                                            {
                                                if (str.equals("Y"))
                                                {
                                                }
                                                else
                                                {
                                                    ShowMessage(str);  //MessageBox.Show("" + str + "", "温馨提示");
                                                    return;
                                                }
                                            }
                                        }
                                        catch (Exception ex)
                                        {
                                            ShowMessage(ex + " 连接WEB-SERVER失败，请联系MIS部门！");  // MessageBox.Show("" + ex + " 连接WEB-SERVER失败，请联系MIS部门", "温馨提示");
                                            return;
                                        }
                                    }
                                }

                                dt_lotkit = cwa.CallRDT("getlotkitinfo", lotnooldnndd);   //lotnooldnndd

                                if (Integer.parseInt(dt_lotkit.Rows(0).get_CellValue("lotqty").trim()) != Integer.parseInt(dieqty))
                                {
                                    ShowMessage("發料與批號實際數據不符，請查看！");  //MessageBox.Show("發料與批號實際數據不符，請查看");
                                    regdata();
                                    return;
                                }
                                else
                                {
                                    if (Integer.parseInt(lotstatus) == 1)
                                    {
                                        ShowMessage(" 跳转到页面 Checkin ");
                                        regdata();
                                        //showdata();

                                        //Creat New Activity
                                        Intent PublicLoading = new Intent(sfconline.this, app.cmapp.IncludingSFC.checkin.class);
                                        Bundle bundler = new Bundle();
                                        bundler.putString("lotno",lotno);
                                        bundler.putString("dieqty", dieqty);
                                        bundler.putString("opno",nowopno);
                                        bundler.putString("productno", productno);
                                        bundler.putString("processno",processno);
                                        bundler.putString("sendopflowid",Integer.toString(sendopflowid));
                                        PublicLoading.putExtras(bundler);
                                        startActivity(PublicLoading);
                                        //Checkin ci = new Checkin(lotno, Integer.parseInt(dieqty), nowopno, productno, processno, sendopflowid);
                                        //ci.ShowDialog();
                                    }
                                    else
                                    {
                                        if (lotstatus.equals("2"))
                                        {
                                            SFCStaticdata.staticmember.updatelotcartflag = true;      // 有收料過程標誌位改變
                                            //出站时选AB线别
                                            boolean linechange = cwa.CallRB("getlinechangeopno", nowopno);

                                            if (linechange)
                                            {
                                                ShowMessage("因出站时选择线别错误被锁，使用有权限的工号解锁！");  //MessageBox.Show("因出站时选择线别错误被锁，使用有权限的工号解锁");
                                                //String linelot = new BLL.basedata().getlinelot(lotno);
                                                String linelot =cwa.CallRS("getlinelot",lotno);
                                                if (linelot.equals(lotno))
                                                {
                                                    SFCStaticdata.staticmember.lineflag = false;
                                                    //Creat New Activity
                                                    //unholdlinelot un = new unholdlinelot(lotno);
                                                    //un.ShowDialog();
                                                    ShowMessage(" 跳转到页面 unholdlinelot ");
                                                    return;
                                                }
                                            }
                                            ShowMessage(" 跳转到页面 Checkout ");
                                            regdata();
                                            //showdata();

                                            //Creat New Activity
                                            CreatNewActivity(checkout.class,strdeviceno,strnewdeviceno,SFCStaticdata.staticmember.odbname, lotno, dieqty, nowopno, productno, processno, Integer.toString(sendopflowid));

                                        }
                                    }
                                }
                            }
                            else
                            {
                                ShowMessage("線邊倉尚未執行收料動作！");  //MessageBox.Show("線邊倉尚未執行收料動作");
                                regdata();
                                return;
                            }
                            break;
                        default:
                            checkinoutflag = true;
                            if (flag3)
                            {
                                ShowMessage(nowopno_1 + "無需Check In/Out，已移至下一站" + nowopno);  //MessageBox.Show(nowopno_1 + "無需Check In/Out，已移至下一站" + nowopno);
                                regdata();
                                //showdata();
                            }
                            else
                            {
                                //补充WIP分批后SFC里的资料
                                att11 = cwa.CallRS("getatt11data", nowopno);
                                if (att11.equals("2"))  //2代表补充SFC里分批后的数据
                                {
                                    if (SFCStaticdata.staticmember.eolspitflag.equals("1"))   //分小子批标记位
                                    {
                                        try
                                        {
                                            int tempdtqty = 0;
                                            String returnstr = "";
                                            String str = getspitdata(etlotno.getText().toString().trim(), SFCStaticdata.staticmember.odbname);
                                            if (str == null || str.equals(""))
                                            {

                                            }
                                            else
                                            {
                                                if (str.equals("Y"))
                                                {

                                                }
                                                else
                                                {
                                                    ShowMessage(str);  //MessageBox.Show("" + str + "", "温馨提示");
                                                    return;
                                                }
                                            }
                                        }
                                        catch (Exception ex)
                                        {
                                            ShowMessage(ex + " 连接WEB-SERVER失败，请联系MIS部门！");  // MessageBox.Show("" + ex + " 连接WEB-SERVER失败，请联系MIS部门", "温馨提示");
                                            return;
                                        }
                                    }
                                }

                                if (lotstatus.equals("1"))
                                {
                                    ShowMessage("跳转到页面 Checkin");  // MessageBox.Show("" + ex + " 连接WEB-SERVER失败，请联系MIS部门", "温馨提示");
                                    regdata();
                                    //showdata();

                                    //Creat New Activity
                                    Intent PublicLoading = new Intent(sfconline.this, app.cmapp.IncludingSFC.checkin.class);
                                    Bundle bundler = new Bundle();
                                    bundler.putString("lotno",lotno);
                                    bundler.putString("dieqty", dieqty);
                                    bundler.putString("opno",nowopno);
                                    bundler.putString("productno", productno);


                                    bundler.putString("processno",processno);
                                    bundler.putString("sendopflowid",Integer.toString(sendopflowid));
                                    PublicLoading.putExtras(bundler);
                                    startActivity(PublicLoading);
                                    //Checkin ci = new Checkin(lotno, Integer.parseInt(dieqty), nowopno, productno, processno, sendopflowid);
                                    //ci.ShowDialog();

                                }
                                else
                                {
                                    if (lotstatus.equals("2"))
                                    {
                                        //出站时选AB线别
                                        boolean linechange = cwa.CallRB("getlinechangeopno", nowopno);
                                        if (linechange)
                                        {
                                            //String linelot = new BLL.basedata().getlinelot(lotno);
                                            String linelot = cwa.CallRS("getlinelot", lotno);
                                            if (linelot == lotno)
                                            {
                                                ShowMessage("因出站时选择线别错误被锁，使用有权限的工号解锁！");  // MessageBox.Show("因出站时选择线别错误被锁，使用有权限的工号解锁");
                                                SFCStaticdata.staticmember.lineflag = false;
                                                //Creat New Activity
                                                //unholdlinelot un = new unholdlinelot(lotno);
                                                //un.ShowDialog();
                                                ShowMessage("跳转到页面 unholdlinelot");  // MessageBox.Show("" + ex + " 连接WEB-SERVER失败，请联系MIS部门", "温馨提示");
                                                return;
                                            }
                                        }

                                        ShowMessage("跳转到页面 Checkout");  // MessageBox.Show("" + ex + " 连接WEB-SERVER失败，请联系MIS部门", "温馨提示");
                                        regdata();
                                        //showdata();

                                        //Creat New Activity
                                        CreatNewActivity(checkout.class,strdeviceno,strnewdeviceno,SFCStaticdata.staticmember.odbname, lotno, dieqty, nowopno, productno, processno, Integer.toString(sendopflowid));

                                    }
                                }
                            }
                            break;
                    }
                }

            }
            catch (Exception ex)
            {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    //protected void onResume(){
    //    super.onResume();
   //      showdatanew();
    // }

     //功能：字符串半角转换为全角
     //说明：全角空格为12288，半角空格为32       其他字符全角(65281-65374)与半角(33-126)的对应关系是：均相差65248
     //输入参数：input --需要转换的字符串      输出参数：无
     //返回值: 转换后的字符串
     public static String HalfTofull(String input) {
         char[] c = input.toCharArray();
         for (int i = 0; i < c.length; i++) {
             if (c[i] == 32) // 全角空格
             {
                 c[i] = (char) 12288;
                 continue;
             }
             if (c[i] > 32 && c[i] < 127) {
                 c[i] = (char) (c[i] + 65248);
             }
         }
         return new String(c);
     }

     //功能：字符串全角转换为半角
     //说明：全角空格为12288，半角空格为32       其他字符全角(65281-65374)与半角(33-126)的对应关系是：均相差65248
     //输入参数：input --需要转换的字符串      输出参数：无
     //返回值: 转换后的字符串
     public static String fullToHalf(String input) {
         char[] c = input.toCharArray();
         for (int i = 0; i < c.length; i++) {
             if (c[i] == 12288) // 全角空格
             {
                 c[i] = (char) 32;
                 continue;
             }
             if (c[i] > 65280 && c[i] < 65375) {
                 c[i] = (char) (c[i] - 65248);
             }
         }
         return new String(c);
     }

     public boolean checklottype(String lotno) throws Exception {
         SFCStaticdata.staticmember.updatelotcartflag = false;

         String lotno_now = lotno.substring(0, 9); //lotno.Substring(0, 9);


         CallWebapi cwa = new CallWebapi(strdeviceno, strnewdeviceno, null, this);
         DataTable dt_lot = cwa.CallRDT("onlinemap_checklottype_1", lotno_now);
         if (dt_lot.Rowscount() >= 1) {
             String devnow = dt_lot.Rows(0).get_CellValue("optvalue").trim();
             String typevalue = cwa.CallRS("onlinemap_checklottype_2");
             if (typevalue.equals("0") || typevalue.equals("1") || (typevalue.equals("3") && SFCStaticdata.staticmember.deviceno.equals("N94")) || typevalue.equals("2"))// RI與RI+,n94,J系列
             {
                 String devno;

                 DataTable dt_sql_select = cwa.CallRDT("onlinemap_checklottype_3");
                 if (dt_sql_select.Rowscount() > 0) {
                     devno = dt_sql_select.Rows(0).get_CellValue("att1").trim();
                 } else {
                     try {
                         devno = SFCStaticdata.staticmember.deviceno.split("-")[1];
                     } catch (Exception e1) {
                         devno = SFCStaticdata.staticmember.newdeviceno;
                     }
                 }

                 if (!devnow.equals(devno)) {
                     ShowMessage("該批號與已選擇的机种不對應");
                     regdata();
                     return false;
                 } else {
                     return true;
                 }
             }
             if ((typevalue.equals("3") && !SFCStaticdata.staticmember.deviceno.equals("N94")) || typevalue.equals("4"))//中國市場中不與N94一樣的機種
             {
                 if (devnow != SFCStaticdata.staticmember.deviceno) {
                     ShowMessage("該批號與已選擇的幾種不對應");
                     regdata();
                     return false;
                 } else {
                     return true;
                 }
             } else {
                 if (SFCStaticdata.staticmember.ip.equals("10.156.126.98")) {
                     return true;
                 } else {
                     ShowMessage("機種信息在N41裡面尚未設置特性，請聯繫MIS");
                     return false;
                 }

             }
         } else {
             ShowMessage("請檢測該批號是否在tbloptlist-tblwipriversion中綁定");
             regdata();
             return false;
         }

     }


     private void regdata() throws Exception {
         setFocusable(etlotno, true);
         setText(etlotno, "");
     }


    public void scantbclick(View v) {
        shortmapid="lotscan";
        Intent openCameraIntent = new Intent(sfconline.this, CaptureActivity.class);
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

            if (shortmapid=="lotscan") {
                etlotno.setText(scanResult);
                //setText(scandeftb,scanResult);
                    shortmapid = "";
                    try {
                        ExecTask(new SFCTaskVoidInterface() {
                            @Override
                            public void taskvoid(Object valueo) throws Exception {
                                submitbutton_Click();
                            }
                        }, "SFCOnline Submit Task");
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(this, "SFCOnline Submit Task Error:" + ex.getMessage());
                    }
            }

        }
    }
     public String getspitdata(String lotno, String dbname) {
         try {
             CallWebapi cwa = new CallWebapi(strdeviceno, strnewdeviceno, null, this);
             return cwa.CallRS("onlinemap_getspitdata", lotno, dbname);
         } catch (Exception ex) {
             BaseFuncation.showmessage(this, ex.getMessage());
             finish();
             return "";
         }
     }


     public static Date getDate() {
         Date currentTime = new Date();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String dateString = formatter.format(currentTime);
         ParsePosition pos = new ParsePosition(0);
         Date currentTime2 = formatter.parse(dateString, pos);
         return currentTime2;
     }

     public static Date getDate(String strDate) {
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         ParsePosition pos = new ParsePosition(0);
         Date currentTime_2 = formatter.parse(strDate, pos);
         return currentTime_2;
     }

     public static String getstrDate(String strDate) {
         try {
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             ParsePosition pos = new ParsePosition(0);
             Date currentTime_2 = formatter.parse(strDate, pos);
             String strDate2 = formatter.format(currentTime_2);
             return strDate2;
         } catch (Exception ex) {
             return strDate;
         }
     }

     public static String getstrDate(String strDate, String sformat) {
         SimpleDateFormat formatter = new SimpleDateFormat(sformat);
         String currentTime_2 = formatter.format(strDate);
         return currentTime_2;
     }
 }
