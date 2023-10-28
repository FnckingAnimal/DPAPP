package app.dpapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class ControlLotnoPage extends ActivityInteractive {

    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotno;
    private String _opno;

    private TextView textBox_lotno;
    private TextView textBox_opno;
    private EditText richTextBox_info;
//    private Button button_submit;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_controllotnopage);

            textBox_lotno = (TextView) findViewById(R.id.isfc_controllotnopage_lotnotbv1);
            textBox_opno = (TextView) findViewById(R.id.isfc_controllotnopage_opnotbv1);
            richTextBox_info = (EditText) findViewById(R.id.isfc_controllotnopage_richtextBoxinfoet1);
//            button_submit = (Button) findViewById(R.id.isfc_defectinput_submitbt);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = b.getString("deviceno");
            _newdeviceno = b.getString("newdeviceno");
            _odbname = b.getString("odbname");
            _lotno = b.getString("lotno");
            _opno = b.getString("nowopno");



            textBox_lotno.setText(_lotno);
            textBox_opno.setText(_opno);
            richTextBox_info.setFocusable(true);
            SFCStaticdata.staticmember.controlconfigflag = false;

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    public void setButton_submit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_submit_Click();
                }
            }, "ControlLotnoPage submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "ControlLotnoPage submit Task Error:" + ex.getMessage());
        }
    }

    private void button_submit_Click() throws Exception
    {
        String textinfo =getText(richTextBox_info).toString().trim().replace(" ","");
        if (textinfo == null||textinfo.equals(""))
        {
            ShowMessage("請輸入批號管控的具體項目");
            setFocusable(richTextBox_info,true);
            return;
        }

        String lotno_now = _lotno;
        String opno_now =_opno;
        String op_now = SFCStaticdata.staticmember.userid;
        String ip_now = SFCStaticdata.staticmember.ip;

        Boolean insflag =
        cwa.CallRB("inserterrorinfo", "ControlLot", textinfo, op_now, opno_now, ip_now, "2", "", null);
        if (!insflag)
        {
            ShowMessage( "批號管控項目記錄失敗！");
            SFCStaticdata.staticmember.controlconfigflag = false;
            this.Close();
        }
        else
        {
            SFCStaticdata.staticmember.controlconfigflag = true;
            this.Close();
        }
    }
}

