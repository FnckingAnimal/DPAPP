package app.dpapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/5.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.dpapp.DataTable.DataTable;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.NetUtils;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.zxing.activity.CaptureActivity;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class errorinfoshow extends ActivityInteractive {

    private Object _locko=new Object();

    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    private EditText label_error;
    private EditText richTextBox_keyin;
    private TextView label1;
    private TextView label_error1;
    private TextView label_error2;
    private EditText textBox_userid;
    private EditText textBox_pw;
    private EditText machinetxt;
    private TextView label_pw;

    private String _error1;
    private String _error2;
    private String _error3;
    private String _opkeynew;
    private String _checktypenew;
    private String _checkatt;
    private String _errtypenew;
    private String _checkuseridnew;
    private String _dbname;
    private String ipstatic;
    private String HDSerialNo;

    private AppCompatActivity _c;
    private Button mButton1,mButton2;
    private String shortmapid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            _c=this;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_errorinfoshow);

            Bundle b = this.getIntent().getExtras();
            String[] ssa=b.getStringArray("sendvalue");
            _deviceno = ssa[0];//b.getString("deviceno");
            _newdeviceno = ssa[1];//b.getString("newdeviceno");
            _odbname = ssa[2];//b.getString("odbname");
            _error1 = ssa[3];//b.getString("error1");
            _error2 = ssa[4];//b.getString("error2");
            _error3 = ssa[5];//b.getString("error3");
            _dbname = ssa[6];//b.getString("dbname");

            _checktypenew = ssa[7];//b.getString("checktype");
            _checkatt = ssa[8];//b.getString("att");
            _errtypenew = ssa[9];//b.getString("errtype");
            _opkeynew = ssa[10];//b.getString("opkey");
            _checkuseridnew=ssa[11];//b.getString("checkuserid");


            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ipstatic = NetUtils.getLocalIPAddress(this);
            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            label_error=(EditText)findViewById(R.id.isfc_errorinfoshow_labelerroret1);
            richTextBox_keyin=(EditText)findViewById(R.id.isfc_errorinfoshow_reasonet1);
            label1=(TextView)findViewById(R.id.isfc_errorinfoshow_label1l1);
            label_error1=(TextView)findViewById(R.id.isfc_errorinfoshow_labelerror1l1);
            label_error2=(TextView)findViewById(R.id.isfc_errorinfoshow_labelerror2l1);
            textBox_userid=(EditText)findViewById(R.id.isfc_errorinfoshow_useridet1);
            textBox_pw=(EditText)findViewById(R.id.isfc_errorinfoshow_pwdet1);
            machinetxt=(EditText)findViewById(R.id.isfc_errorinfoshow_eqidet1);
            label_pw=(TextView)findViewById(R.id.isfc_errorinfoshow_pwdl1);
            mButton1 = (Button) findViewById(R.id.isfc_saomiao_password);
            mButton2 = (Button) findViewById(R.id.isfc_saomiao_zhanghao);

            label_error.setText(_error1);
            label_error1.setText(_error2);
            label_error2.setText(_error3);

            if (_checkuseridnew.equals("1"))
            {
//                label3.Visible = false;
                label_pw.setVisibility(View.VISIBLE);
                textBox_pw.setVisibility(View.VISIBLE);
                textBox_userid.setText(SFCStaticdata.staticmember.userid);
            }

            if (_opkeynew.equals("1"))//&&_opkeynew.isEmpty()
            {
//                labelkeyin.Visible = true;

                richTextBox_keyin.setVisibility(View.VISIBLE);
            }
            else
            {
//                labelkeyin.Visible = false;
                richTextBox_keyin.setVisibility(View.INVISIBLE);
            }
            if (_checktypenew.equals("5") && _checkatt.equals("4") && _errtypenew.equals( "23") && _opkeynew.equals("1"))
            {
//                machinelab.Visible = true;
                machinetxt.setEnabled(true);
            }
            else
            {
//                machinelab.Visible = false;
                machinetxt.setEnabled(false);
            }

            this.ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {

                }
            },"LoadTest");

        }
        catch(Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
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
            }, "errorinfoshow Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "errorinfoshow Submit Task Error:" + ex.getMessage());
        }
    }

    public void button_saomiao_password(View v){
        shortmapid = "saomiao_password";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent,0);
    }
    public void button_saomiao_zhanghao(View v){
        shortmapid = "saomiao_zhanghao";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent,0);
    }

    private void button_submit_Click() throws  Exception
    {
        String userid = getText(textBox_userid).toString().trim();
        String pw = getText(textBox_pw).toString().trim();
        String machineno = getText(machinetxt).toString().trim();
        Boolean machinetxtEnable=getEnabled(machinetxt);
        String errorkeyinop = getText(richTextBox_keyin).toString().trim();
        String errornewinfo = getText(label_error).toString().trim();
        String label_error1Text=getText(label_error1).toString().trim();
        String label_error2Text=getText(label_error2).toString().trim();

        if (machinetxtEnable)
        {
            if (machineno.equals("") || machineno == null)
            {
                ShowMessage("請輸入機台號");
                return;
            }

        }
      if (userid.equals(null) || userid.equals("")){
            ShowMessage("請輸入帳號");
            return;
        }
        if (pw.equals(null) || pw.equals("")) {
            ShowMessage("請輸入密碼");
            return;
        }
        if (_opkeynew.equals("1"))
        {
            if (errorkeyinop.equals("") || errorkeyinop == null)
            {
                ShowMessage("請手動Key入不良原因！");
                setFocusable(richTextBox_keyin,true);
                return;
            }
            else
            {
                errornewinfo = errorkeyinop;
            }
        }

        if (!_checkuseridnew.equals("1")) {
            String tempname = "";
            String sql_temp = "";
            String[] parmers = {};
            if (_checktypenew.equals("5") && _checkatt.equals("55"))  //中心值不良的權限
            {
                sql_temp = " select item password,att2 att1  from tblsfcallsetting where type ='5' and status ='13' and att1 =:att1 ";
                tempname = SFCStaticdata.staticmember.odbname;
                String[] parmers2 = {userid};
                parmers = parmers2;
            } else {
                sql_temp = " select userid,password,checktype,att1,att2 from t_usercurence where userid=:userid and checktype=:checktype and att1=:checkatt ";
                tempname = "N41CONN";
                String[] parmers1 = {userid, _checktypenew, _checkatt};
                parmers = parmers1;
            }


            //DataTable dt = oraconn.OracleExecSqlparmer(sql_temp, parmers, parmers.Length, tempname);

            DataTable dt = cwa.CallRDT("basefunction_execsql_ora_d",sql_temp, BaseFuncation.SerializeObjectArrayString(parmers), tempname);
            if (dt.Rowscount() > 0) {
                String pwdb = dt.Rows(0).get_CellValue("password").trim();
                String checkdb = dt.Rows(0).get_CellValue("att1").trim();
                if (!pw.equals(pwdb)) {
                    ShowMessage("密码错误，请重新输入");
                    setText(textBox_pw, "");
                    return;
                }
                if (!_checkatt.equals(checkdb)) {
                    ShowMessage("该账户没有对应权限，请重新输入");
                    setText(textBox_userid, "");
                    setText(textBox_pw, "");
                    return;
                }
//                Boolean insdbflag = new BLL.basedata().errorinfobackdb(label_error1.Text.ToString().Trim(), label_error2.Text.ToString().Trim(), errornewinfo, userid, SFCStaticdata.staticmember.ip, dbname, errtypenew, machineno);
                Boolean insdbflag = cwa.CallRB("errorinfobackdb", label_error1Text, label_error2Text, errornewinfo, userid, SFCStaticdata.staticmember.ip, _odbname, _errtypenew, machineno);
                if (!insdbflag) {
                    ShowMessage("备份失败！");
                    SFCStaticdata.staticmember.podtestsumcheck = false;
                    SFCStaticdata.staticmember.staticYieldLimitedOpno = false;
                    SFCStaticdata.staticmember.qcchangefalg = false;
                    this.Close();
                    return;
                } else {
                    SFCStaticdata.staticmember.podtestsumcheck = true;
                    SFCStaticdata.staticmember.staticYieldLimitedOpno = true;
                    SFCStaticdata.staticmember.qcchangefalg = true;
                    this.Close();
                }

            } else {

                ShowMessage("没有该账号信息");
                setText(textBox_userid, "");
                setText(textBox_pw, "");
                return;
            }
        }
        else {
            Boolean insdbflag = cwa.CallRB("errorinfobackdb", label_error1Text, label_error2Text, errornewinfo, userid, SFCStaticdata.staticmember.ip, _odbname, _errtypenew, "");
            if (!insdbflag) {
                ShowMessage("备份失败！");
                SFCStaticdata.staticmember.podtestsumcheck = false;
                this.Close();
                return;
            } else {
                SFCStaticdata.staticmember.podtestsumcheck = true;
                this.Close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if(scanResult == null || "".equals(scanResult)) {
               Toast.makeText(this,"掃描為空",Toast.LENGTH_SHORT).show();
            } else {
                switch (shortmapid) {
                    case "saomiao_zhanghao":
                        textBox_userid.setText(scanResult);
                        break;
                        case "saomiao_password":
                            textBox_pw.setText(scanResult);
                            break;
                }
            }
        }
    }
}
