package app.dpapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import app.dpapp.DataTable.DataTable;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class VtqImageTestCheckout extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    private EditText textBox_user;
    private EditText textBox_pw;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_vtqimagetestcheckout);

            textBox_user = (EditText) findViewById(R.id.isfc_vtqimagetestcheckout_useret1);
            textBox_pw = (EditText) findViewById(R.id.isfc_vtqimagetestcheckout_pwdet);

            Bundle b = this.getIntent().getExtras();
            String[] ss = b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];


            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    public void submitbt(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_submit_Click();
                }
            }, "VtqImageTestCheckout submit task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "VtqImageTestCheckout submit task Error:" + ex.getMessage());
        }
    }

    private void button_submit_Click() throws Exception {
        String userid = getText(textBox_user).toString().trim();
        String pw = getText(textBox_pw).toString().trim();

        if(userid == null || "".equals(userid))
        {
            ShowMessage("账号不能为空~");
            setFocusable(textBox_user, true);
            return;
        }

        if(pw == null || "".equals(pw) )
        {
            ShowMessage("密码不能为空~");
            setFocusable(textBox_pw, true);
            return;
        }

        String temp = "select password from t_usercurence where userid=:userid and att1='101'";
        String[] parmers = {userid};
        DataTable dt = cwa.CallRDT("basefunction_execsql_ora_d",temp, BaseFuncation.SerializeObjectArrayString(parmers), "N41CONN");

        if (dt.Rowscount() >= 1) {
            String userpw = dt.Rows(0).get_CellValue("password").trim();
            if (!userpw.equals(pw)) {
                ShowMessage("帳戶密碼不正確，請重新輸入");
                SFCStaticdata.staticmember.podtestsumcheck = false;
              //  setText(textBox_pw, "");
                setFocusable(textBox_pw, true);
                return;
            } else {
                SFCStaticdata.staticmember.podtestsumcheck = true;
                this.Close();
            }
        } else {
            ShowMessage("沒有該帳戶的相關信息，請聯繫MIS");
            setText(textBox_user, "");
            setText(textBox_pw, "");
            setFocusable(textBox_pw, true);
            return;
        }
    }
}

