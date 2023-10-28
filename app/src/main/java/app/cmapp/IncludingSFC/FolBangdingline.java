package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import app.cmapp.DataTable.DataTable;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.R;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class FolBangdingline extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;


    private TextView label_lotno;
    private TextView label_opno;
    private Spinner comboBox_line1;
    private Spinner comboBox_line2;
    private CheckBox checkBox_line1;
    private CheckBox checkBox_line2;


    String sql_select_line = "";
    String sql_insert1 = "";
    String sql_insert2 = "";
    Boolean flag1 = false, flag2 = false;
    String lotno_insert = "";
    String opno_insert = "";
    private String lotno;
    private String opno;
    private String characterid;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_folbangdingline);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            label_lotno = (TextView) findViewById(R.id.isfc_folbangdingline_lotnotv1);
            label_opno = (TextView) findViewById(R.id.isfc_folbangdingline_opnotv1);
            comboBox_line1 = (Spinner) findViewById(R.id.isfc_folbangdingline_line1sp1);
            comboBox_line2=(Spinner)findViewById(R.id.isfc_folbangdingline_line2sp1);
            checkBox_line1 = (CheckBox) findViewById(R.id.isfc_folbangdingline_line1cb1);
            checkBox_line2 = (CheckBox) findViewById(R.id.isfc_folbangdingline_line2cb1);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotno = ss[3];
            characterid=ss[4];
            opno = ss[5];
            
            label_lotno.setText(lotno);
            label_opno.setText(opno);

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    FolBangdingline();
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");

            comboBox_line1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    checkBox_line1.isChecked();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

           comboBox_line2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   checkBox_line2.isChecked();
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

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



    public void FolBangdingline() throws Exception {
        sql_select_line = " select characterid,charactervalue from tblcharactervalue a where characterid= :characterid ";
        String[] temp_array = {characterid};
        DataTable dt_line1 = cwa.CallRDT("basefunction_execsql_ora_d", sql_select_line, BaseFuncation.SerializeObjectArrayString(temp_array), SFCStaticdata.staticmember.odbname);
        DataTable dt_line2 = dt_line1;
//        cwa.CallRDT("basefunction_execsql_ora_d",sql_select_line,BaseFuncation.SerializeObjectArrayString(temp_array),SFCStaticdata.staticmember.odbname);
        lotno_insert = lotno;
        opno_insert = opno;

        if (dt_line1.Rowscount() > 0) {
            setAdapter(comboBox_line1, BaseFuncation.setvalue(
                    dt_line1, null, "charactervalue", FolBangdingline.this
            ));
            setAdapter(comboBox_line2, BaseFuncation.setvalue(
                    dt_line2, null, "charactervalue", FolBangdingline.this
            ));
//            comboBox_line1.DataSource = dt_line1;
//            comboBox_line1.SelectedIndex = -1;
//            comboBox_line1.DisplayMember = "charactervalue";

//            comboBox_line2.DataSource = dt_line2;
//            comboBox_line2.SelectedIndex = -1;
//            comboBox_line2.DisplayMember = "charactervalue";
        }
    }

    public void folbangdinglinesubmit(View v) {
        String taskname = "folbangdinglinesubmit Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    confirm_Click();
                }
            }, taskname);
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }

    private void confirm_Click() throws Exception
    {

        Boolean checkBox_line1Checked=isChecked(checkBox_line1);
        Boolean checkBox_line2Checked=isChecked(checkBox_line2);
        //第一種，還有過任何一個母批，第二種，已經有過一個母批中的子批的情形
        List<String> ls = new ArrayList<String>();
        String line1 =  ((FinalStaticCloass.SpinnerData)getSelectedItem(comboBox_line1)).getText();
        String line2 = ((FinalStaticCloass.SpinnerData)getSelectedItem(comboBox_line2)).getText();
        String sql_select_sl = "select lotno,linename from t_oplinedata where lotno like :lotno";
        String[] temparray_sl = new String[2];
        if (lotno_insert.startsWith("P"))
        {
            temparray_sl = lotno_insert.split("-");
        }
        else
        {
            temparray_sl = lotno_insert.split("-");
        }
        String [] temparray = {temparray_sl[0].trim()+"%"};
        DataTable dt_sl = 
cwa.CallRDT("basefunction_execsql_ora_d", sql_select_sl, BaseFuncation.SerializeObjectArrayString(temparray), SFCStaticdata.staticmember.odbname);
        if (dt_sl.Rowscount() > 0) {
            for (int i = 0; i < dt_sl.Rowscount(); i++) {
                ls.add(dt_sl.Rows(i).get_CellValue("linename"));
            }

            if (checkBox_line1Checked) {
                if (!ls.contains(line1)) {
                    if (ls.size() == 1) {
                        ShowMessage("請選擇與之前相同纖體" + ls.get(0));
                        return;
                    } else {
                        ShowMessage("請選擇與之前相同纖體" + ls.get(0) + ";" + ls.get(1));
                        return;
                    }
                }
            }
            if (checkBox_line2Checked) {
                if (!ls.contains(line2)) {
                    if (ls.size() == 1) {
                        ShowMessage("請選擇與之前相同纖體" + ls.get(0));
                        return;
                    } else {
                        ShowMessage("請選擇與之前相同纖體" + ls.get(0) + ";" + ls.get(1));
                        return;
                    }
                }
            }
        }




        if (checkBox_line1Checked)
        {
            sql_insert1 = "insert into t_oplinedata (lotno,opno,linename,checkid,checktime) values (:lotno,:opno,:linename,:checkid,sysdate)";
            String[] sql_insert1_array = {lotno_insert,opno_insert,line1,SFCStaticdata.staticmember.userid };
//            flag1 = new DAL.Ora_dal().OracleExecVoidSqlparmer(sql_insert1, sql_insert1_array, sql_insert1_array.Length, SFCStaticdata.staticmember.odbname);
            flag2=cwa.CallRB("basefunction_execsql_ora_b", sql_insert1, BaseFuncation.SerializeObjectArrayString(sql_insert1_array), SFCStaticdata.staticmember.odbname);
        }

        if (checkBox_line2Checked)
        {
            sql_insert2 = "insert into t_oplinedata (lotno,opno,linename,checkid,checktime) values (:lotno,:opno,:linename,:checkid,sysdate)";
            String[] sql_insert2_array = { lotno_insert, opno_insert, line2, SFCStaticdata.staticmember.userid };
//            flag2 = new DAL.Ora_dal().OracleExecVoidSqlparmer(sql_insert2, sql_insert2_array, sql_insert2_array.Length, SFCStaticdata.staticmember.odbname);
            flag2=cwa.CallRB("basefunction_execsql_ora_b",sql_insert2,BaseFuncation.SerializeObjectArrayString(sql_insert2_array), SFCStaticdata.staticmember.odbname);
        }

        this.Close();
    }


}

