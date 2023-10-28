package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.DataTable.DataTable;
import app.dpapp.R;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class QCBackInputPage extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    
    private Spinner comboBox_defectgroup;
    private Spinner comboBox_DN;
    private EditText textBox_mfgop;
    private EditText textBox_mfgcheck;
    private EditText textBox_engcheck;
    private EditText textBox_qccheck;
    String snnow = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_qcbackinputpage);
//            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");
            comboBox_defectgroup = (Spinner) findViewById(R.id.isfc_qcbackinputpage_dgsp1);
            comboBox_DN=(Spinner)findViewById(R.id.isfc_qcbackinputpage_missp1);
            textBox_mfgop=(EditText)findViewById(R.id.isfc_qcbackinputpage_moet1);
            textBox_mfgcheck=(EditText)findViewById(R.id.isfc_qcbackinputpage_mc1et1);
            textBox_engcheck=(EditText)findViewById(R.id.isfc_qcbackinputpage_eg1et1);
            textBox_qccheck=(EditText)findViewById(R.id.isfc_qcbackinputpage_qc1et1);

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            snnow = ss[3];


            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    QCBackInputPage_Load();
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

    public void qcbackinputpagesubmit(View v)
    {
        String taskname="qcbackinputpagesubmit Task";
        try
        {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_confirm_Click();
                }
            },taskname);
        }
        catch (Exception ex)
        {
            BaseFuncation.showmessage(this, taskname + " Error:" + ex.getMessage());
        }
    }



    private void QCBackInputPage_Load( ) throws Exception
    {
        DataTable dta=new DataTable();
        dta.AddColumn("header");
        dta.AddRow("D");
        dta.AddRow("N");
        
        setAdapter(comboBox_DN,BaseFuncation.setvalue(dta,null,"header",this));

        String sql = "select item from TBLSFCALLSETTING where type='0' and status='13'";
        String[] parmer = { };
        DataTable dt = 
        cwa.CallRDT("basefunction_execsql_ora_d", sql, BaseFuncation.SerializeObjectArrayString(parmer), "N41CONN");
        setAdapter(comboBox_defectgroup,BaseFuncation.setvalue(dt,null,"item",this));
    }

    private void button_confirm_Click() throws Exception
    {
        String dnpart = ((FinalStaticCloass.SpinnerData)getSelectedItem(comboBox_DN)).getText().trim();
        String mfgop=getText(textBox_mfgop).toString().trim();
        String mfgcheck=getText(textBox_mfgcheck).toString().trim();
        String engcheck=getText(textBox_engcheck).toString().trim();
        String qccheck=getText(textBox_qccheck).toString().trim();
        String qcdefectcode =  ((FinalStaticCloass.SpinnerData)getSelectedItem(comboBox_defectgroup)).getText().trim();

        if (dnpart.isEmpty() || mfgop.isEmpty() || mfgcheck.isEmpty() || engcheck.isEmpty() || qccheck .isEmpty())
        {
            ShowMessage("所有相關人員信息都不能為空"); return;
        }


        List<String> tempstr=new ArrayList<String>();
        if (snnow.contains("/"))
        {
            String[] snnow_1 = snnow.split("//");
            for (int i = 0; i < snnow_1.length - 1; i++)
            {
                tempstr=new ArrayList<String>();
                tempstr.add(dnpart);
                tempstr.add(mfgop);
                tempstr.add(mfgcheck);
                tempstr.add(engcheck);
                tempstr.add(qccheck);
                tempstr.add(qcdefectcode);
                tempstr.add(snnow_1[i]);
                String[] tta=new String[tempstr.size()];
                tempstr.toArray(tta);
                SFCStaticdata.staticmember.qcstaticListString.add(tta);
            }
        }
        else
        {
            tempstr.add(dnpart);
            tempstr.add(mfgop);
            tempstr.add(mfgcheck);
            tempstr.add(engcheck);
            tempstr.add(qccheck);
            tempstr.add(qcdefectcode);
            tempstr.add(snnow);
            String[] tta=new String[tempstr.size()];
            tempstr.toArray(tta);
            SFCStaticdata.staticmember.qcstaticListString.add(tta);
        }
        //SFCStaticdata.staticmember.qcstaticListString.a

        //SFCStaticdata.staticmember.qcstaticListString.Add(tempstr.ToArray());

        //if (SFCStaticdata.staticmember.qcbackstaticworkdayDN != null && SFCStaticdata.staticmember.qcbackstaticworkdayDN != "" && SFCStaticdata.staticmember.qcbackstaticmfgop != null && SFCStaticdata.staticmember.qcbackstaticmfgop != "" && SFCStaticdata.staticmember.qcbackstaticmfgcheck != null && SFCStaticdata.staticmember.qcbackstaticmfgcheck!=""&&SFCStaticdata.staticmember.qcbackstaticengcheck!=null&&SFCStaticdata.staticmember.qcbackstaticengcheck!=""&&SFCStaticdata.staticmember.qcbackstaticqccheck!=null&&SFCStaticdata.staticmember.qcbackstaticqccheck!=""&&SFCStaticdata.staticmember.qcbackstaticdefectgroup!=""&&SFCStaticdata.staticmember.qcbackstaticdefectgroup!=null)
        if(SFCStaticdata.staticmember.qcstaticListString.size()>0)
        {
            this.Close();
        }
    }

}

