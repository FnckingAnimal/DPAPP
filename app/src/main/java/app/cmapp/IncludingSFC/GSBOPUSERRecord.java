package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import app.dpapp.R;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class GSBOPUSERRecord extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    private TextView label_lotno;
    private TextView label_opno;
    private EditText textBox_dev;
    private EditText textBox_opnouser;

    String sql_insert = "";
    String sql_delete = "";
    String sql_update = "";
    String lotno = "";
    String opno1 = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_gsbopuserrecord);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            label_lotno = (TextView) findViewById(R.id.isfc_gsbopuserrecord_lotnotv1);
            label_opno = (TextView) findViewById(R.id.isfc_gsbopuserrecord_opnotv1);
            textBox_dev = (EditText) findViewById(R.id.isfc_gsbopuserrecord_devet1);
            textBox_opnouser = (EditText) findViewById(R.id.isfc_gsbopuserrecord_useret1);


            Bundle b = this.getIntent().getExtras();
            String[] ss = b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotno = ss[3];
            opno1 = ss[4];
            label_lotno.setText(lotno);

            switch (ss[5]) {
                case "1":
                    label_opno.setText("進站");
                    break;
                case "2":
                    label_opno.setText("出站");
                    break;
            }

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);


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

    public void gsbopuserrecordsubmit(View v) throws Exception {
        String taskname = "GSBOPUSERecord Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Confirm_Click();
                }
            }, taskname);
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }

    private void Confirm_Click() throws Exception {
        String devicenotext = getText(textBox_dev).toString().trim();
        String opnouserrecord = getText(textBox_opnouser).toString().trim();
        String label_opnoText = getText(label_opno).toString().trim();
        String textBox_devText = getText(textBox_dev).toString().trim();
        String textBox_opnouserText = getText(textBox_opnouser).toString().trim();
        String label_lotnoText = getText(label_lotno).toString().trim();

        if (devicenotext.isEmpty()) {
            ShowMessage("請輸入機台號！~");
            return;
        }

        if (opnouserrecord.isEmpty()) {
            ShowMessage("請輸入過站人員！~");
            return;
        }

        sql_delete = "update t_opuserdata set lotno = 'bak'||lotno where lotno = :lotno";
        String[] temp_array_delete = {lotno};
        Boolean flag_delete = cwa.CallRB("basefunction_execsql_ora_b", sql_delete, BaseFuncation.SerializeObjectArrayString(temp_array_delete), SFCStaticdata.staticmember.odbname);

        if (!flag_delete) {
            ShowMessage("刪除數據失敗，請聯繫MIS手動刪除！~");
            return;
        }

        if (label_opnoText.equals("進站")) {
            sql_insert = "insert into t_opuserdata(lotno,opno,deviceno,checkinid,checkintime) values (:lotno,:opno,:deviceno,:checkid,sysdate)";
            String[] temp_array_insert = {label_opnoText, opno1, textBox_devText, textBox_opnouserText};
//            Boolean falg_insert = new DAL.Ora_dal().OracleExecVoidSqlparmer(sql_insert, temp_array_insert, temp_array_insert.Length, SFCStaticdata.staticmember.odbname);
            Boolean falg_insert = cwa.CallRB("basefunction_execsql_ora_b", sql_insert, BaseFuncation.SerializeObjectArrayString(temp_array_insert), SFCStaticdata.staticmember.odbname);

            ShowMessage("插入成功！~");
            this.Close();

        }

        if (label_opnoText.equals("出站")) {
            sql_update = " update t_opuserdata set checkoutid = :checkoutid,checkouttime = sysdate where lotno = :lotno ";
            String[] temp_array_update = {textBox_opnouserText, label_lotnoText};
            Boolean flag_update =
                    cwa.CallRB("basefunction_execsql_ora_b", sql_update, BaseFuncation.SerializeObjectArrayString(temp_array_update), SFCStaticdata.staticmember.odbname);
            ShowMessage("成功");
            this.Close();
        }
    }
}

