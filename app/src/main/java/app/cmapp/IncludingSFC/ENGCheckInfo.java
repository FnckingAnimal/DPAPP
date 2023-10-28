package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
public class ENGCheckInfo extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String lotno;
    private String ngqty;

    private EditText richTextBox1;
    private EditText petb;

    String errornonow = "";
    String lotnonow = "";
    String errqty = "";
    Boolean engconfirmflag = false;
    String ipstatic;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_engcheckinfo);
            ipstatic = NetUtils.getLocalIPAddress(this);
            richTextBox1 = (EditText) findViewById(R.id.isfc_engcheckinfo_richet1);
            petb = (EditText) findViewById(R.id.isfc_engcheckinfo_peet1);

            Bundle b = this.getIntent().getExtras();
            String[] ss = b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotno=ss[3];
            ngqty=ss[4];

            richTextBox1.setText( "批号" +lotno + "共抛料SN颗数：" + ngqty);
            errqty = ngqty;
            errornonow = "批号" + lotno + "共抛料SN颗数：" + ngqty;
            lotnonow = lotno;
            SFCStaticdata.staticmember.lotpaoliaocheckeolflag = true;
            errqty = ngqty;
            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    public void confirmsubmit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_submit_Click();
                }
            }, "ENGCheckInfo submit task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "v submit task Error:" + ex.getMessage());
        }
    }

    private void button_submit_Click() throws Exception {
        String richTextBox2Text=getText(richTextBox1).toString().trim();
        if (richTextBox2Text.isEmpty())
        {
           ShowMessage("備註信息不可為空！");
            return;
        }
        String pestr = getText(petb).toString().trim();
        String sql = "select userid from t_usercurence where userid=:userid and checktype='13'";
        String[] parmers = { pestr };
        DataTable dt = new DataTable();
        dt =
        cwa.CallRDT("basefunction_execsql_ora_d",sql,BaseFuncation.SerializeObjectArrayString(parmers), "N41CONN");
        if (dt.Rowscount() == 0)
        {
            ShowMessage("此工號沒有權限確認不良！！！");
            SFCStaticdata.staticmember.lotpaoliaocheckeolflag = false;
            setText(petb,"");
            setFocusable(petb,true);
            return;
        }
        else
        {
            Boolean insflag =
            cwa.CallRB("inserterrorinfo", lotnonow, "paoliao", errornonow, pestr, "", ipstatic, "13", errqty, null);
            if (insflag)
            {
               ShowMessage("成功存儲確認不良信息< CheckInfo >");
                SFCStaticdata.staticmember.lotpaoliaocheckeolflag = true;
                this.Close();
            }
            else
            {
               ShowMessage("存儲確認信息失敗< CheckInfo >！");
                SFCStaticdata.staticmember.lotpaoliaocheckeolflag = false;
                this.Close();
            }
        }
    }
}

