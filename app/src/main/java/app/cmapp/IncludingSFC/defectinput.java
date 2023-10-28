package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.cmapp.DataTable.DataTable;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.NetUtils;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.R;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class defectinput extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotno;
    private String _staticdefectqty;
    private String _opno;

    private TextView textBox_loton;
    private TextView textBox_opno;
    private TextView textBox_defectqty;
    private Button buttonclear;
    private LinearLayout defectvaluegv;
    private Button submit;

    private String ipstatic;
    private String HDSerialNo;

    Boolean RIandRIADDEOLQC = false;
//    int staticopnoflowid;
//    String staticproductno;
    DataTable multiMachinenodt = new DataTable();
    String guigestr = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_defectinput);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            textBox_loton = (TextView) findViewById(R.id.isfc_defectinput_lotnotbv1);
            textBox_opno = (TextView) findViewById(R.id.isfc_defectinput_opnotbv1);
            textBox_defectqty = (TextView) findViewById(R.id.isfc_defectinput_defectqtytv1);
            buttonclear = (Button) findViewById(R.id.isfc_defectinput_clearbt);
            defectvaluegv = (LinearLayout) findViewById(R.id.isfc_defectinput_stationviewll1);
            submit = (Button) findViewById(R.id.isfc_defectinput_submitbt);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            _lotno = ss[3];
            _opno= ss[4];
            _staticdefectqty  = ss[5];

//            _deviceno = b.getString("deviceno");
//            _newdeviceno = b.getString("newdeviceno");
//            _odbname = b.getString("odbname");
//            _lotno = b.getString("lotno");
//            _staticdefectqty = b.getString("senddefectqty");
//            _opno = b.getString("nowopno");
            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    loaddata();
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");

            defectvaluegv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus)
                    {
                        if(_loadingstatus)
                        {
                            vieworder();
                        }
                    }
                }
            });

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Check In Loading Task Error:"+ ex.getMessage());
            this.finish();
        }
    }

    public void finish() {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                }
            }, "Defect Input Closeing Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Defect Input Closeing Task Error:" + ex.getMessage());
        }
        super.finish();
    }

    private void vieworder()
    {
        try {
            List<SFCStaticdata.defectdata> ld = new ArrayList<SFCStaticdata.defectdata>();
            SFCStaticdata.defectdata tld;
            for (int i = 0; i < defectvaluegv.getChildCount(); i++) {
                tld = new SFCStaticdata().new defectdata();
                tld.errorqty = Integer.parseInt(((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname3)).getText().toString().trim());
                tld.errorname = ((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname2)).getText().toString().trim();
                tld.errorname = ((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1)).getText().toString().trim();
                ld.add(tld);
            }
            Collections.sort(ld, new ComparatorValues());

            defectvaluegv.removeAllViewsInLayout();
            View gv;
            for (SFCStaticdata.defectdata d : ld
                    ) {
                gv = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.listview_defectinput, null);
                ((TextView) gv.findViewById(R.id.listviewdefectinputmname1)).setText(d.errorcode);
                ((TextView) gv.findViewById(R.id.listviewdefectinputmname1)).setText(d.errorname);
                ((TextView) gv.findViewById(R.id.listviewdefectinputmname1)).setText(d.errorqty);
                defectvaluegv.addView(gv);
            }
        }
        catch(Exception ex)
        {
            BaseFuncation.showmessage(this,"Defect View order error");
        }
    }

    public static final class ComparatorValues implements Comparator<SFCStaticdata.defectdata> {
        @Override
        public int compare(SFCStaticdata.defectdata object1, SFCStaticdata.defectdata object2) {
            int m1 = object1.errorqty;
            int m2 = object2.errorqty;
            int result = 0;
            if (m1 > m2) {
                result = 1;
            }
            if (m1 < m2) {
                result = -1;
            }
            return result;
        }
    }


    private void loaddata() throws  Exception
    {
        setText(textBox_loton,_lotno);
        setText(textBox_opno,_opno);
        setText(textBox_defectqty, _staticdefectqty);

        DataTable errdt = new DataTable();
        errdt=cwa.CallRDT("geterrorcode",_opno);
        for(int i=0;i<errdt.Rowscount();i++) {
            View view = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.listview_defectinput, null);
            setText((TextView) view.findViewById(R.id.listviewdefectinputmname1), errdt.Rows(i).get_CellValue("errorno"));
            setText((TextView) view.findViewById(R.id.listviewdefectinputmname2), errdt.Rows(i).get_CellValue("errorname"));
            addView(defectvaluegv, view);
        }
    }

    public void submitbt(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    submit_Click();
                }
            }, "defectinput Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "defectinput Submit Task Error:" + ex.getMessage());
        }
    }

    private void submit_Click() throws Exception {
        int defectvaluegvrowscount = getChildCount(defectvaluegv);
        String textBox_opnoText = getText(textBox_opno).toString().trim();
        String textBox_lotonText = getText(textBox_loton).toString().trim();

        if (defectvaluegvrowscount < 1) {
            ShowMessage("請輸入不良原因");
            return;
        }

        int defnum = 0;

        //   特殊不良代碼發出預警郵件
        String sql_dev = "select att5 from tblsfcallsetting where type= '7' and status = '13' and item = :item and att5 = :att5";
        DataTable dt_defect = new DataTable();
        DataTable dt_newtable = new DataTable();
        dt_newtable.AddColumn("機種");
        dt_newtable.AddColumn("線體");
        dt_newtable.AddColumn("批號");
        dt_newtable.AddColumn("站位");
        dt_newtable.AddColumn("不良代碼");
        dt_newtable.AddColumn("不良名稱");
        dt_newtable.AddColumn("不良數量");

        String sql_xianti = "select charvalue from t_lotequplinelog where lotno = :lotno and characterid = 'C0005'";
        String[] sql_xianti_array = {textBox_lotonText};
        DataTable dt_xianti = cwa.CallRDT("basefunction_execsql_ora_d", sql_xianti, BaseFuncation.SerializeObjectArrayString(sql_xianti_array), SFCStaticdata.staticmember.odbname);

        String opname = "select opname from t_opdata a  where a.opno = :opno";
        String[] array_opname = {textBox_opnoText};
        DataTable dt_opname = cwa.CallRDT("basefunction_execsql_ora_d", opname, BaseFuncation.SerializeObjectArrayString(array_opname),  "N41CONN");

        SFCStaticdata.staticmember.errorqtydata=new ArrayList<SFCStaticdata.defectdata>();
        int int_total = 0;
        for (int i = 0; i < defectvaluegvrowscount; i++) {
            String tdefectno = getText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1)).toString().trim();
            String tdefectna = getText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname2)).toString().trim();
            String tdefectv = getText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname3)).toString().trim();
            String[] temp_array = {SFCStaticdata.staticmember.newdeviceno, tdefectno};
            dt_defect = cwa.CallRDT("basefunction_execsql_ora_d", sql_dev, BaseFuncation.SerializeObjectArrayString(temp_array), "N41CONN");


            if(!tdefectv.equals("")) {

                if (dt_defect.Rowscount() > 0) {
                    dt_newtable.AddRow(SFCStaticdata.staticmember.newdeviceno,
                            dt_xianti.Rows(0).get_CellValue("charvalue").trim(), textBox_lotonText,
                            dt_opname.Rows(0).get_CellValue("opname").trim(),
                            tdefectno,
                            tdefectna,
                            tdefectv);
                }

                try {
                    Integer.parseInt(tdefectv);
                } catch (Exception ex) {
                    ShowMessage("輸入數量不為數字請重新輸入");
                    setText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1), "0");
                    SFCStaticdata.staticmember.errorqtydata.clear();
                    SFCStaticdata.staticmember.errorqtydata = null;
                    return;
                }

                if (Integer.parseInt(tdefectv) <= 0) {
                    ShowMessage("輸入不良數量不能為小於或等於0的數");
                    setText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1), "0");
                    SFCStaticdata.staticmember.errorqtydata.clear();
                    SFCStaticdata.staticmember.errorqtydata = null;
                    return;
                }

                if (!(tdefectno.contains("1036-02"))) {
                    int_total += Integer.parseInt(tdefectv);
                }


                String defectcharacter = cwa.CallRS("geterrorcharacter", tdefectno, textBox_opnoText);
                String defectcharacteratt6 = cwa.CallRS("geterrorcharacteratt6", tdefectno, textBox_opnoText);

                if (defectcharacter == null || defectcharacter.equals("")) {
                    ShowMessage("不良代碼特性尚未設定（att3），请联系MIS！");
                    return;
                }

                if (Integer.parseInt(defectcharacter) == 2)     //   OTHER
                {
                    SFCStaticdata.staticmember.defngflag = false;
                    //String opnonowaa = opno_tb.Text.ToString().Trim();   // "defectinput"
                    SFCStaticdata.staticmember.engconfiginfo = false;
//                    ENGConfirm engcf = new ENGConfirm(lotno, errqty, errorno, textBox_opno.Text.ToString().Trim());
//                    engcf.ShowDialog();
                    CreatNewActivity(ENGConfirm.class, _deviceno, _newdeviceno, _odbname,
                            textBox_lotonText, tdefectv, tdefectno, textBox_opnoText);
                    if (SFCStaticdata.staticmember.defngflag) {
                        //this.Close();
                    } else {
                        ShowMessage("請輸入Other項不良原因");
                        return;
                    }
                }


                if (defectcharacteratt6.equals("") || defectcharacteratt6 == null) {
                    //沒有設定att6值則不做考慮
                } else {
                    if (Integer.parseInt(defectcharacteratt6) == 1) {
                        SFCStaticdata.staticmember.podtestsumcheck = false;
//                        errorinfoshow errorshow = new errorinfoshow("当前站位不良特殊代碼需要工程確認，是否需要強行過站 ?<" + errorno + ">", lotno, textBox_opno.Text.ToString().Trim(), SFCStaticdata.staticmember.odbname, "9", "2", "37", 1);
//                        errorshow.ShowDialog();
                        CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                "当前站位不良特殊代碼需要工程確認，是否需要強行過站 ?<" + tdefectno + ">", textBox_lotonText, textBox_opnoText, SFCStaticdata.staticmember.odbname, "9", "2", "37", "1");
                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                            ShowMessage("验证强行出站失败！<當前站位特殊不良驗證失敗>");
                            return;
                        }
                    }
                }


                defnum = defnum + Integer.parseInt(tdefectv);


                SFCStaticdata.defectdata sd = new SFCStaticdata().new defectdata();
                sd.errorcode = tdefectno;
                sd.errorname = tdefectna;
                sd.errorqty = Integer.parseInt(tdefectv);

                SFCStaticdata.staticmember.errorqtydata.add(sd);



                //--------------------------------------聶前良，VTQ打other不良檢測數量-------------------------------------------
                DataTable dt_temptable = new DataTable();
                dt_temptable =cwa.CallRDT("GetCehcktimes", SFCStaticdata.staticmember.deviceno, tdefectno);

                if (dt_temptable.Rowscount() > 0) {
                    if (Integer.parseInt(tdefectv) >= Integer.parseInt(dt_temptable.Rows(0).get_CellValue("item").trim())) {
                        ShowMessage("打Other超過設定數量" + dt_temptable.Rows(0).get_CellValue("item") + "顆，請確認！~");
//                        ENGConfirm gs = new ENGConfirm(lotno, errqty, errorno, textBox_opno.Text.ToString().Trim());
//                        gs.ShowDialog();
                        CreatNewActivity(ENGConfirm.class, _deviceno, _newdeviceno, _odbname,
                                tdefectv, tdefectno, textBox_opnoText);
                    }
                }
                //===============================================================================================================
            }
        }

        if (dt_newtable.Rowscount() > 0) {
            if (BaseFuncation.DialogResult.OK == MessageBox("存在特殊不良代碼，需要發送預警郵件，請確認！~", "提示")) {
//                        new BLL.basedata().execSendMailAll(dt_newtable, "test", "SFCsystem預警郵件 MBO/PBO/FAI不良預警", "1");
                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(dt_newtable), "test", "SFCsystem預警郵件 MBO/PBO/FAI不良預警", "1", ipstatic);
            } else {
                ShowMessage("請選擇其他不良項目");
                return;
            }
        }


        //增加一個前段打不良的數量和1036測試站位的不良數量一樣的功能！~ Alan
        String sql_selec_funcation = "select item from tblsfcallsetting where type = '7' and status = '17' and item=:item and att4 = :att4";
        String[] temp_array1 = {SFCStaticdata.staticmember.newdeviceno, textBox_opnoText};
        DataTable dt_newtable1 =cwa.CallRDT("basefunction_execsql_ora_d", sql_selec_funcation, BaseFuncation.SerializeObjectArrayString(temp_array1), "N41CONN");

        if (dt_newtable1.Rowscount() > 0) {
            String sql_num = "select nvl(count(sn),0) as totalnum from t_lotpodtestup2bak where testopid = :lotno and passflag = '0'";
            String[] totalnum = {textBox_lotonText.trim().substring(1)};
            DataTable dt_totalnum = cwa.CallRDT("basefunction_execsql_ora_d", sql_num, BaseFuncation.SerializeObjectArrayString(totalnum), SFCStaticdata.staticmember.odbname);


            if (!dt_totalnum.Rows(0).get_CellValue("totalnum").trim().equals(String.valueOf(int_total).trim())) {
                ShowMessage("1036測試不良為" + dt_totalnum.Rows(0).get_CellValue("totalnum") + "與當前1036測試的不良" + String.valueOf(int_total).trim() + "");
                return;
            }
        }

        //---這裡是此功能結束

        if (defnum != Integer.parseInt(_staticdefectqty)) {
            ShowMessage("輸入不良數量不符合");
            SFCStaticdata.staticmember.errorqtydata.clear();
            SFCStaticdata.staticmember.errorqtydata = null;
            return;
        }

        //RIandRIADDEOLQC = new BLL.basedata().RIandRIAddEOLQCStation(nowopno);
        if (SFCStaticdata.staticmember.fqcflag) {
            //todo create new class open
//                QCPage.QCBackInputPage qb = new SFConline.QCPage.QCBackInputPage("FOL");
//                qb.ShowDialog();
            if (!existsQCBarcode("FOL")) {
                ShowMessage("Q退相關責任人員不可為空，請重新輸入");
                return;
            }
        }

        this.Close();
    }


    public void buttonclear_Click(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    buttonclear_Click();
                }
            }, "defectinput clear Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "defectinput clear Task Error:" + ex.getMessage());
        }
    }

    private void buttonclear_Click() throws Exception
    {
        String opnoatt2 =cwa.CallRS("getopatt2", _opno);
        if (Integer.parseInt(opnoatt2) != 2)
        {
            Boolean delopnoflag =cwa.CallRB("deldefectsn", _lotno, _opno, "当站清空按钮defectinput");
            //Boolean checkopnoflag = deldefectsn(lotno, nowopno,"当站清空按钮");
            if (!delopnoflag)
            {
                ShowMessage("清除该批号当前出站站位不良信息时发生错误！");
                return;
            }
        }
    }

    private Boolean existsQCBarcode(String sn)
    {
        if (SFCStaticdata.staticmember.qcstaticListString != null)
        {
            for (int i = 0; i < SFCStaticdata.staticmember.qcstaticListString.size(); i++)
            {
                if (sn.equals(SFCStaticdata.staticmember.qcstaticListString.get(i)[6]))
                {
                    return true;
                }
            }
        }
        return false;
    }
}

