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
public class Podtestpage extends ActivityInteractive {

    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    String lotno_now = "";
    String opno_now = "";
    String qty_now = "";
    Boolean testallflagnow=false;    // 是否為  百測

    private TextView text_lot;
    private TextView text_opno;
    private EditText text_testqty;
//    private Button submit;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_podtestpage);

            text_lot = (TextView) findViewById(R.id.isfc_podtestpage_lotnotbv1);
            text_opno = (TextView) findViewById(R.id.isfc_podtestpage_opnotbv1);
            text_testqty = (EditText) findViewById(R.id.isfc_podtestpage_defectqtytv1);
//            submit = (Button) findViewById(R.id.isfc_podtestpage_mainlabelheader);

            Bundle b = getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotno_now = ss[3];
            opno_now = ss[4];
            qty_now = ss[5];
            String t=ss[6];
            switch (t) {
                case "1":
                    testallflagnow = true;
                    break;
                default:
                    testallflagnow = false;
                    break;
            }

            text_lot.setText(lotno_now);
            text_opno.setText( opno_now);

            if (testallflagnow)
            {
                text_testqty.setText(qty_now);
                text_testqty.setEnabled(false);

            }
            else
            {
                text_testqty.setText("160");
                text_testqty.setEnabled(true);
            }
            SFCStaticdata.staticmember.Podtestflag = false;
            text_testqty.setFocusable(true);
            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            finish();
        }
    }

    public void setsubmit_click(View v)
    {
        try
        {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    submit_Click();
                }
            },"Podtestpage submit Task");
        }
        catch(Exception ex)
        {
            BaseFuncation.showmessage(this,"Podtestpage submit Task Error:"+ex.getMessage());
        }
    }

    private void submit_Click() throws Exception
    {
        String inputqtydb = getText(text_testqty).toString().trim();
        Boolean text_testqtyEnable=getEnabled(text_testqty);
       
        if (!testallflagnow)
        {
            if (inputqtydb.equals("") || inputqtydb == null)
            {
                ShowMessage("請輸入抽測的POD不良數目");
            }
        }

        String podtestqtyflag = "0";   // 0 默認百測      1 默認抽測
        if (text_testqtyEnable)
        {
            podtestqtyflag = "0";
        }
        else
        {
            podtestqtyflag = "1";
        }
        
        String sql = "begin delete from LOTNOSPECIALRECORDS where lotno=:lotno_now and recordtype='2';insert into LotnoSpecialRecords values(:lotno_now1,'2',:ip,:opno_now,sysdate,'nongqty',:inputqtydb,:podtestqtyflag,'','','','');";
        sql += "end;";
        
        try
        {
            String[] parmersss = { lotno_now, lotno_now, SFCStaticdata.staticmember.ip, opno_now, inputqtydb, podtestqtyflag };
            Boolean inserlotdblog =
            cwa.CallRB("basefunction_execsql_ora_b", sql, BaseFuncation.SerializeObjectArrayString(parmersss), SFCStaticdata.staticmember.odbname);
            if (!inserlotdblog)
            {
                ShowMessage("批号DB记录保存失败，请联系MIS！");
                 SFCStaticdata.staticmember.Podtestflag = false;
            } else
            {
                 SFCStaticdata.staticmember.Podtestflag = true;
            }
        } catch(Exception ex)
          {


        }

        Close();
    }
}

