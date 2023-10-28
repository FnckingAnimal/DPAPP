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
import app.dpapp.appcdl.NetUtils;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class lotsubopnoinput extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    
    private EditText textBox_user;
    private EditText textBox_pw;

    String lotnonow = "";
    String opnonow = "";
    String opnoflowidnow = "";
    String lotstatenow = "";
    String userid = "";
    String pw = "";
    String ipstatic;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_testopnosnsumcheck);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");
            textBox_user = (EditText) findViewById(R.id.isfc_lotsubopnoinput_useret1);
            textBox_pw=(EditText)findViewById(R.id.isfc_lotsubopnoinput_pwdet1);


            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno =  SFCStaticdata.staticmember.deviceno;//ss[0];
            _newdeviceno = SFCStaticdata.staticmember.newdeviceno;// ss[1];
            _odbname = SFCStaticdata.staticmember.odbname;// ss[2];
            lotnonow = ss[0];
            opnonow =ss[1];
            opnoflowidnow=ss[2];
            lotstatenow=ss[3];
            
            textBox_user.setFocusable(true);


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

    public void lotsubopnoinputsubmit(View v)
    {
        String taskname="lotsubopnoinputsubmit Task";
        try
        {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_submit_Click();
                }
            },taskname);
        }
        catch (Exception ex)
        {
            BaseFuncation.showmessage(this,taskname+" Error:"+ex.getMessage());
        }
    }

    private void button_submit_Click() throws Exception
    {
        userid = getText(textBox_user).toString().trim();
        pw = getText(textBox_pw).toString().trim();
        DataTable dt = new DataTable();
        
        String temp = "select userid,password,checktype,att1 from t_usercurence where userid=:userid and checktype='5' ";
        if (SFCStaticdata.staticmember.lotcheckeolflag)//EOL 卡att1='7'
        {
            temp = temp + "  and att1='7'";
        }
        String[] parmers = { userid };
        try
        {
            dt =
            cwa.CallRDT("basefunction_execsql_ora_d",temp,BaseFuncation.SerializeObjectArrayString(parmers),"N41CONN");
            if (dt.Rowscount() >= 1)
            {
                String userpw = dt.Rows(0).get_CellValue("password").trim();
                if (!userpw.equals(pw)) {
                    ShowMessage("帳戶密碼不正確，請重新輸入");
                    setText(textBox_pw, "");
                    setFocusable(textBox_pw, true);
                    return;
                }
                else
                {
                    temp = "begin insert into T_lotopnolog values(:lotnonow,:opnonow,:opnoflowidnow,:lotstatenow,:userid,sysdate,:ip,'0',null,null,null);end;";
                    String[] parmersins = { lotnonow, opnonow, opnoflowidnow, lotstatenow, userid, ipstatic };
                    try
                    {
                        Boolean insflag =
                        cwa.CallRB("basefunction_execsql_ora_b", temp, BaseFuncation.SerializeObjectArrayString(parmersins), SFCStaticdata.staticmember.odbname);
                        if (insflag)
                        {
                            SFCStaticdata.staticmember.checklotsub = true;
                            this.Close();
                            return;
                        }
                        else
                        {
                            SFCStaticdata.staticmember.checklotsub = false;
                            this.Close();
                            return;
                        }
                    }
                    catch(Exception ex)
                    {
                        ShowMessage("批號T_lotopnolog插入記錄失敗");
                        setText(textBox_user, "");
                        setText(textBox_pw, "");
                        setFocusable(textBox_pw, true);
                        return;
                    }
                }
            }
            else
            {
                ShowMessage("沒有該帳戶的相關信息，請聯繫MIS");
                setText(textBox_user, "");
                setText(textBox_pw, "");
                setFocusable(textBox_pw, true);
                return;
            }
        }
        catch(Exception ex)
        {
            ShowMessage("驗證帳戶權限時發生錯誤");
            setText(textBox_user, "");
            setText(textBox_pw, "");
            setFocusable(textBox_pw, true);
            return;
        }
    }

}

