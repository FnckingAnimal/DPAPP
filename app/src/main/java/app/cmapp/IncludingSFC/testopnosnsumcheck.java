package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class testopnosnsumcheck extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;


    private TextView label_errorinfo;
    private TextView label_testcart;
    private EditText textBox_user;
    private EditText textBox_pw;
    private CheckBox checkBox_line1;
    private CheckBox checkBox_line2;

    String lotnonow = "";
    String opnonow = "";
    String errorinfonow = "";

    String testcartnow = "";
    String checktypenew = "";
    String attnew = "";
    String ipstatic;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_testopnosnsumcheck);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            label_errorinfo = (TextView) findViewById(R.id.isfc_testopnosnsumcheck_errorinfotv1);
            label_testcart = (TextView) findViewById(R.id.isfc_testopnosnsumcheck_testcarttv1);
            textBox_user = (EditText) findViewById(R.id.isfc_testopnosnsumcheck_useret1);
            textBox_pw=(EditText)findViewById(R.id.isfc_testopnosnsumcheck_pwdet1);


            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotnonow = ss[3];
            opnonow =ss[4];
            errorinfonow = ss[5];
            testcartnow = ss[6];

            textBox_user.setFocusable(true);
            
            label_errorinfo.setText(errorinfonow);
            label_testcart.setText(testcartnow);

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");

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

    public void testopnosnsumchecksubmit(View v) {
        String taskname = "testopnosnsumchecksubmit Task";
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_submit_Click();
                }
            }, taskname);
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }

    private void button_submit_Click() throws Exception
    {
        String userid = getText(textBox_user).toString().trim();
        String pw = getText(textBox_pw).toString().trim();
        DataTable dt = new DataTable();

        String temp = "select userid,password,checktype,att1 from t_usercurence where userid=:userid and checktype='11'";
        String[] parmers = { userid };
        try
        {
//            dt = oraconn.OracleExecSqlparmer(temp, parmers, 1, "N41CONN");
            dt=cwa.CallRDT("",temp,BaseFuncation.SerializeObjectArrayString(parmers),"N41CONN");
            if (dt.Rowscount() == 1)
            {
                String userpw = dt.Rows(0).get_CellValue("password");
                if (!userpw.equals(pw))
                {
                    ShowMessage("帳戶密碼不正確，請重新輸入");
                    setText(textBox_pw, "");
                    setFocusable(textBox_pw, true);
                    return;
                }
                else
                {
                    //:lotno_now,:sn_now,:unusualinfo,:userid,sysdate,:ip,:opno,null,:errqty,:att
//                    Boolean insflag = new BLL.basedata().inserterrorinfo(lotnonow, "testsninfoerr", errorinfonow + testcartnow, textBox_user.Text.ToString(), opnonow, ip, "11", "",null);
                    Boolean insflag = cwa.CallRB("inserterrorinfo", lotnonow, "testsninfoerr", errorinfonow + testcartnow, userid, opnonow, ipstatic, "11", "", null);
                    if (insflag)
                    {
                        //ShowMessage("成功存儲確認不良信息< Other >");
                        SFCStaticdata.staticmember.testsumcheckflag = true;
                        this.Close();
                    }
                    else
                    {
                        //ShowMessage("存儲確認信息失敗< Other >！");
                        SFCStaticdata.staticmember.testsumcheckflag = false;
                        //petb.Text = "";
                        //richTextBox1.Text = "";
                    }
                }
            }
            else
            {
                ShowMessage("沒有該帳戶的相關信息，請聯繫MIS");
                setText(textBox_pw, "");
                setText(textBox_user, "");
                setFocusable(textBox_pw, true);
                return;
            }
        }
        catch(Exception ex)
        {
            ShowMessage("驗證帳戶權限時發生錯誤");
            setText(textBox_pw, "");
            setText(textBox_user, "");
            setFocusable(textBox_user, true);
            return;
        }
    }

}

