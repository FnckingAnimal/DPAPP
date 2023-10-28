package app.dpapp.IncludingSFC;/**
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

import app.dpapp.DataTable.DataTable;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.NetUtils;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class folcheckpage extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotno;
    private String _qty;
    private String _opno;

    private TextView textBox_lotno;
    private TextView textBox_opno;
    private TextView textBox_opname;
    private TextView textBox_qty;
    private TextView textBox_checkngqty;
    private TextView textBox_checkqty;
    private LinearLayout defectvaluegv;
    private Button button_folcheckng;

    private String ipstatic;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_folcheckpage);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            textBox_lotno = (TextView) findViewById(R.id.isfc_folcheckpage_lotnotbv1);
            textBox_opno = (TextView) findViewById(R.id.isfc_folcheckpage_opnotbv1);
            textBox_opname = (TextView) findViewById(R.id.isfc_folcheckpage_opnametbv1);
            textBox_qty = (TextView) findViewById(R.id.isfc_folcheckpage_qtytv1);
            textBox_checkngqty = (TextView) findViewById(R.id.isfc_folcheckpage_checkngqtytbv1);
            textBox_checkqty = (TextView) findViewById(R.id.isfc_folcheckpage_checkqtytbv1);
            defectvaluegv = (LinearLayout) findViewById(R.id.isfc_folcheckpage_stationviewll1);
            button_folcheckng= (Button) findViewById(R.id.isfc_folcheckpage_folcheckngbt);
//            submit = (Button) findViewById(R.id.isfc_folcheckpage_stationviewll1);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            _lotno = ss[3];
            _opno = ss[4];
            _qty = ss[5];


            textBox_lotno.setText(_lotno);
            textBox_opno.setText(_opno);
            textBox_qty.setText(_qty);
            textBox_checkngqty.setText("0");
            textBox_lotno.setEnabled(false);
            textBox_opname.setEnabled(false);
            textBox_opno.setEnabled(false);
            textBox_qty.setEnabled(false);
            
            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    FOLCheckPage();
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
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
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

    public void FOLCheckPage() throws Exception {
        setText(textBox_opname, cwa.CallRS("getopname", _opno));
        SFCStaticdata.staticmember.FolCheckStationInsertSql = "begin  ";
        Boolean clearBoolean = execclearData(_lotno, _opno);
        if (!clearBoolean) {
            ShowMessage("執行清除當站位抽檢數據異常，請聯繫MIS");
            return;
        }
    }
    List<String> parmerArray = new ArrayList<String>();

    public void folcheckpagesubmitclick(View v)
    {
        String taskname="folcheckpagesubmitclick Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_commit_Click();
                }
            },taskname);
        }
        catch (Exception ex)
        {
            BaseFuncation.showmessage(this,taskname+" Error:"+ex.getMessage());
        }
    }

    public void folcheckpagefolcheckngclick(View v) {
        String taskname = "folcheckpagefolcheckngclick Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_folcheckng_Click();
                }
            }, taskname);
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }

    private void button_folcheckng_Click() throws Exception {
        String checkqty = getText(textBox_checkqty).toString().trim();
        String ngcheckqty = getText(textBox_checkngqty).toString().trim();
        String textBox_opnoText = getText(textBox_opno).toString().trim();
        try {
            if (Integer.parseInt(ngcheckqty) > Integer.parseInt(checkqty)) {
                ShowMessage("NG數量不可超過抽檢數量");
                return;
            }
        } catch (Exception ex) {
            ShowMessage("抽檢數量與不良數量不為正確的整型數據，請重新輸入");
            return;
        }
        DataTable errdt = new DataTable();
        errdt =
                cwa.CallRDT("geterrorcode", textBox_opnoText);

        for (int i = 0; i < errdt.Rowscount(); i++) {
            View view = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.listview_defectinput, null);
            setText((TextView) view.findViewById(R.id.listviewdefectinputmname1), errdt.Rows(0).get_CellValue("errorno"));
            setText((TextView) view.findViewById(R.id.listviewdefectinputmname2), errdt.Rows(0).get_CellValue("errorname"));
            addView(defectvaluegv, view);
        }
    }

    private void textBox_checkngqty_TextChanged() throws Exception {
        String ngqty = getText(textBox_checkngqty).toString().trim();
        if (ngqty.isEmpty() || ngqty.equals("0")) {
            setEnabled(button_folcheckng, false);
        } else {
            setEnabled(button_folcheckng, true);
        }
        SFCStaticdata.staticmember.FolCheckStationInsertSql = "begin ";
        parmerArray = new ArrayList<String>();
    }

    private void button_commit_Click() throws Exception {
        SFCStaticdata.staticmember.FolCheckStationInsertSql = "begin ";
        parmerArray = new ArrayList<String>();
        String lotno = getText(textBox_lotno).toString().trim();
        String opno = getText(textBox_opno).toString().trim();
        Integer lotqty = Integer.parseInt(getText(textBox_qty).toString().trim());
        int defectvaluegvRowsCount = getChildCount(defectvaluegv);
        int checkqty = 0;
        int ngcheckqty = 0;
        try {
            checkqty = Integer.parseInt(getText(textBox_checkqty).toString().trim());
            ngcheckqty = Integer.parseInt(getText(textBox_checkngqty).toString().trim());
        } catch (Exception ex) {
            ShowMessage("抽檢數和不良數都必須為整型字符");
            return;
        }
        if (checkqty > lotqty) {
            ShowMessage("抽檢數量不可超過批號當站數量");
            return;
        }
        if (ngcheckqty > checkqty) {
            ShowMessage("不良數量不可超過抽檢數量");
            return;
        }
        if (ngcheckqty > 0) {
            if (defectvaluegvRowsCount > 0) {
                int factngqty = 0;
                for (int i = 0; i < defectvaluegvRowsCount; i++) {
                    String tempname = getText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1)).toString().trim();
                    String tempvalue = getText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname3)).toString().trim();
                    if (i < defectvaluegvRowsCount - 1) {
                        SFCStaticdata.staticmember.FolCheckStationInsertSql = SFCStaticdata.staticmember.FolCheckStationInsertSql + "insert into TBLFOLCHECKDEFECTDATA values(:lot" + i + ",:opno" + i + ",:errorno" + i + ",:errorqty" + i + ",:checkop" + i + ",sysdate,:ip" + i + ",null,null,null,null,null);";
                        parmerArray.add(lotno);
                        parmerArray.add(opno);
                        parmerArray.add(tempname);
                        parmerArray.add(tempvalue);
                        parmerArray.add(SFCStaticdata.staticmember.userid);
                        parmerArray.add(ipstatic);
                        factngqty = factngqty + Integer.parseInt(tempvalue);
                    } else if (i == defectvaluegvRowsCount - 1) {
                        SFCStaticdata.staticmember.FolCheckStationInsertSql = SFCStaticdata.staticmember.FolCheckStationInsertSql + "insert into TBLFOLCHECKDEFECTDATA values(:lot" + i + ",:opno" + i + ",:errorno" + i + ",:errorqty" + i + ",:checkop" + i + ",sysdate,:ip" + i + ",null,null,null,null,null);";
                        parmerArray.add(lotno);
                        parmerArray.add(opno);
                        parmerArray.add(tempname);
                        parmerArray.add(tempvalue);
                        parmerArray.add(SFCStaticdata.staticmember.userid);
                        parmerArray.add(ipstatic);
                        factngqty = factngqty + Integer.parseInt(tempvalue);
                        if (factngqty != ngcheckqty) {
                            ShowMessage("輸入的抽檢不良數量與實際打的不良數量不一致，請重新確認之後再提交");
                            SFCStaticdata.staticmember.FolCheckStationInsertSql = "begin ";
                            parmerArray = new ArrayList<String>();
                            return;
                        }
                    }
                }
            } else {
                ShowMessage("請先輸入不良項目");
                return;
            }
        }
        SFCStaticdata.staticmember.FolCheckStationInsertSql = SFCStaticdata.staticmember.FolCheckStationInsertSql + "insert into TBLFOLCHECKDATA values(:lotno00,:opno00,:lotqty00,:checkqty00,:ngcheckqty00,:checker00,sysdate,:checkip00,null,null,null,null,null);end;";
        parmerArray.add(lotno);
        parmerArray.add(opno);
        parmerArray.add(String.valueOf(lotqty));
        parmerArray.add(String.valueOf(checkqty));
        parmerArray.add(String.valueOf(ngcheckqty));
        parmerArray.add(SFCStaticdata.staticmember.userid);
        parmerArray.add(ipstatic);
        String[] parmerinsert = new String[parmerArray.size()];
        parmerArray.toArray(parmerinsert);
//        Boolean blinsert = new DAL.Ora_dal().OracleExecVoidSqlparmer(SFCStaticdata.staticmember.FolCheckStationInsertSql,parmerinsert,parmerinsert.Length,SFCStaticdata.staticmember.odbname);
        Boolean blinsert = cwa.CallRB("basefunction_execsql_ora_b", SFCStaticdata.staticmember.FolCheckStationInsertSql, BaseFuncation.SerializeObjectArrayString(parmerinsert), SFCStaticdata.staticmember.odbname);
        if (blinsert) {
            ShowMessage("存儲抽檢數據成功");
            SFCStaticdata.staticmember.FolCheckStationInsertSql = "begin ";
            parmerArray = new ArrayList<String>();
            this.Close();
        } else {
            ShowMessage("存儲抽檢數據異常，請聯系MIS");
            SFCStaticdata.staticmember.FolCheckStationInsertSql = "begin ";
            parmerArray = new ArrayList<String>();
        }
    }
    public Boolean execclearData(String lotno,String opno) throws Exception {
        String delsql = "begin delete from TBLFOLCHECKDATA where lotno=:lot and opno=:opnonow;delete from TBLFOLCHECKDEFECTDATA where lotno=:lot1 and opno=:opnonow1;end;";
        String[] parmerdel = {lotno, opno, lotno, opno};
//        Boolean bl = new DAL.Ora_dal().OracleExecVoidSqlparmer(delsql,parmerdel,parmerdel.Length,SFCStaticdata.staticmember.odbname);
        return cwa.CallRB("basefunction_execsql_ora_b", delsql, BaseFuncation.SerializeObjectArrayString(parmerdel), SFCStaticdata.staticmember.odbname);
    }
}

