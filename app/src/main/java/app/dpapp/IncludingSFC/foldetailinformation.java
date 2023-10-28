package app.dpapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class foldetailinformation extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;

    private EditText Op;
    private EditText Information;

    String F_lotno = "";
    String F_opno = "";
    String F_opnoer = "";
    String F_qty = "";
    String sql_count = "";
    String infocount = "";
    String sql_insert = "";
    String informationstr = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_foldetailinformation);

            Op = (EditText) findViewById(R.id.isfc_foldetailinformation_opet1);
            Information = (EditText) findViewById(R.id.isfc_foldetailinformation_ineformationet1);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            F_lotno = ss[3];
            F_opno = ss[4];
            F_opnoer = ss[5];
            F_qty =ss[6];

            Op.setText(SFCStaticdata.staticmember.userid);

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    public void confirmsubmit(View v)
    {
        try
        {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button1_Click();
                }
            },"foldetailinformation submit task");
        }
        catch(Exception ex)
        {
            BaseFuncation.showmessage(this,"foldetailinformation submit task Error:"+ex.getMessage());
        }
    }

    private void button1_Click() throws Exception
    {
        sql_count = " select nvl(max(infocount),0) as NUM from t_lotdetailinformation ";
        String[] sql_counttemp = {};
        infocount =cwa.CallRS("basefunction_execsql_ora_s", sql_count, _odbname);

        if (!infocount.equals("")||infocount != null )
        {
            infocount = String.valueOf(Integer.parseInt(infocount) + 1);
        }
        informationstr = getText(Information).toString().trim();
        sql_insert = "insert into t_lotdetailinformation(lotno,opno,qty,createtime,information,infocount,att1) values (:lotno,:opno,:qty,sysdate,:information,:infocount,:att1)";
        String[] sql_inserttemp = {F_lotno,F_opno,F_qty,informationstr,infocount, F_opnoer};

//        BLL.staticmember.Folinformationflag = new DAL.Ora_dal().OracleExecVoidSqlparmer(sql_insert, sql_inserttemp, sql_inserttemp.Length, "VTQCONN");
        SFCStaticdata.staticmember.Folinformationflag=cwa.CallRB("basefunction_execsql_ora_b",sql_insert,BaseFuncation.SerializeObjectArrayString(sql_inserttemp),_odbname);
        this.Close();
    }
}

