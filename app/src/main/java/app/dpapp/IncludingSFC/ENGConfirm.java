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
public class ENGConfirm extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    private EditText richTextBox1;
    private EditText petb;

    String opno = "";
    String errornonow = "";
    String lotnonow = "";
    String snnow = "";
    String errqty = "";
    Boolean engconfirmflag = false;
    String ipstatic;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_engconfirm);
            ipstatic = NetUtils.getLocalIPAddress(this);
            richTextBox1 = (EditText) findViewById(R.id.isfc_engconfirm_richet1);
            petb = (EditText) findViewById(R.id.isfc_engconfirm_peet1);

            Bundle b = this.getIntent().getExtras();
            String[] ss = b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];

            switch (ss[3].trim())
            {
                case "3":
                    richTextBox1.setText( "不良項目為:" + ss[4] + "的不良比率上限是:" + ss[5] + "%,"
                            + "實際不良比率為:" + ss[6] + "%,已超標！！！");
                    petb.setText("");
                    petb.setFocusable(true);
                    engconfirmflag = true;
                    break;
                case "4":
                    lotnonow =ss[4];
                    errqty =ss[5];
                    errornonow =ss[6];
                    opno = ss[7];  //站位
                    engconfirmflag = false;
                    petb.setText("");
                    richTextBox1.setText(errornonow);
                    richTextBox1.setFocusable(true);
                    break;
            }
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
            }, "ENGConfirm submit task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "ENGConfirm submit task Error:" + ex.getMessage());
        }
    }

    private void button_submit_Click() throws Exception {

        String richTextBox1Text = getText(richTextBox1).toString().trim();
        String petbText = getText(petb).toString().trim();
        if (richTextBox1Text.trim().isEmpty()) {
            ShowMessage("備註信息不可為空！");
            return;
        }

        String pestr = petbText;
        String sql = "select userid from t_usercurence where userid=:userid and checktype='4'";
        String[] parmers = {pestr};
        DataTable dt = new DataTable();
        dt = cwa.CallRDT("basefunction_execsql_ora_d",sql, BaseFuncation.SerializeObjectArrayString(parmers), "N41CONN");
        if (dt.Rowscount() == 0) {
            ShowMessage("此工號沒有權限確認不良！！！");
            SFCStaticdata.staticmember.defngflag = false;
            setText(petb, "");
            setFocusable(petb, true);
            return;
        } else {
            if (engconfirmflag)     // 如果是工程确认下良率超限的话，则无需记录信息，只需确认下即可。
            {
                //return;
                this.Close();
                return;
            }
            SFCStaticdata.staticmember.pename = pestr;

            //inserterrorinfo(String lotno_now, String sn_now, String unusualinfo, String userid, String deviceno, String ip)
            Boolean insflag = cwa.CallRB("inserterrorinfo", lotnonow, errornonow, richTextBox1Text, pestr, opno, ipstatic, "1", errqty, null);
            if (insflag) {
                ShowMessage("成功存儲確認不良信息< Other >");
                SFCStaticdata.staticmember.defngflag = true;
                this.Close();
            } else {
                ShowMessage("存儲確認信息失敗< Other >！");
                SFCStaticdata.staticmember.defngflag = false;

            }
        }
    }
}

