package app.dpapp.IncludingSFC;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Hashtable;

import app.dpapp.DataTable.DataTable;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.FinalStaticCloass;
import app.dpapp.appcdl.NetUtils;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.appcdl.SFCBLLPack.expendable_input;
import app.dpapp.zxing.activity.CaptureActivity;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class expendable extends ActivityInteractive {

    private Boolean _loadingstatus=false;
    private static String shortmapid;

    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotserialpage;
    private String _opfalg;
    private String _checktype;

    private TextView opnotb;
    private TextView opnonametb;
    private TextView keypartnametb;
    private TextView textBox_maname;
    private CheckBox checkBoxrepro;
    private TextView textBox_replacematerial;
    private TextView factorylab;
    private TextView label6;

    private Spinner keypartnocb;
    private Spinner comboBoxreproduct;
    private Spinner comboBox_afmachineno;
    private EditText Editafmachineet;

    private EditText inputtb;
    private EditText Serialtxt;
    private EditText QCIDtb;
    private TextView Seriallab;

    private LinearLayout Keygw;

    private Button submitbt;

    private String now_lotno;
    private String now_opno;
    private String now_productno;
    private String scanstate;
    private String Quserid;
    private String keyparttype;
    private String limittime;
    private String lottypemode;
    private String checklotuse;
    private String tablename;
    private String locationname;
    private String linename;
    private String execsql;
    private String tempexecsql;
    private String lotserialpage;
    private String opatt2str = "";
    private Boolean checklotnobymachine = new Boolean(false);
    private Boolean isFolSubstrateMaterialForSMTLotBool = false;
    private Boolean isFolSubstrateMateiralFormSMTLotDeviceBool = false;
    DataTable gluedata = new DataTable();

    private String ipstatic;
    private String HDSerialNo;

    private AppCompatActivity _c;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            _c=this;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_expendable);

            SFCStaticdata.staticmember.lotfolbingpicheck=false;
            SFCStaticdata.staticmember.englotcheckflag=true;
            SFCStaticdata.staticmember.engRilotcheckflag=false;
            SFCStaticdata.staticmember.lotcheckeolflag=false;
            SFCStaticdata.staticmember.devicetype="BC-L";
          

            ipstatic = NetUtils.getLocalIPAddress(this);
            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            factorylab = (TextView) findViewById(R.id.isfc_expendable_factorylabel1);
            opnotb = (TextView) findViewById(R.id.isfc_expendable_opnovalue1);
            opnonametb = (TextView) findViewById(R.id.isfc_expendable_opnamevalue1);
            keypartnocb = (Spinner) findViewById(R.id.isfc_expendable_materialvalue1);
            keypartnametb = (TextView) findViewById(R.id.isfc_expendable_materialnamevalue1);
            textBox_maname = (TextView) findViewById(R.id.isfc_expendable_materialshortvalue1);
            checkBoxrepro = (CheckBox) findViewById(R.id.isfc_expendable_replacematerialcb1);
            comboBoxreproduct = (Spinner) findViewById(R.id.isfc_expendable_replacematerialvalue1);
            textBox_replacematerial = (TextView) findViewById(R.id.isfc_expendable_replacematerialname1);
            inputtb = (EditText) findViewById(R.id.isfc_expendable_scanvalue1);
            comboBox_afmachineno = (Spinner) findViewById(R.id.isfc_expendable_scanafmachinenosp1);
            Editafmachineet=(EditText) findViewById(R.id.isfc_expendable_Serialltvl1);
            Serialtxt = (EditText) findViewById(R.id.isfc_expendable_Serialvalue1);
            Seriallab = (TextView) findViewById(R.id.isfc_expendable_Seriallabel1);
            QCIDtb = (EditText) findViewById(R.id.isfc_expendable_qcconfirmvalue1);
            label6=(TextView)findViewById(R.id.isfc_expendable_scanname1);

            submitbt = (Button) findViewById(R.id.isfc_expendable_submitbt1);
            Keygw = (LinearLayout) findViewById(R.id.isfc_expendable_materialviewll1);
            factorylab.setText("");

            Bundle b = this.getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno =  ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            this.now_opno = ss[3];
            this.now_lotno = ss[4];
            this.now_productno = ss[5];
            _lotserialpage = ss[6];
            _opfalg = ss[7];
            _checktype = ss[8];

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);


            SFCStaticdata.staticmember.str_opno=this.now_opno;
            SFCStaticdata.staticmember.str_lotno=this.now_lotno;

            opnotb.setText(this.now_opno);

            this.ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    DataTable dtexpend = new DataTable();
                    if (_opfalg.equals("") || _opfalg == null) {
                        dtexpend = cwa.CallRDT("getexpendablelist", now_opno, now_productno);
                    } else {
                        dtexpend = cwa.CallRDT("getexpendablelistinout", now_opno, now_productno, _checktype);
                    }
                    setAdapter(keypartnocb, BaseFuncation.setvalue(dtexpend, null, "expendableproductno", _c));
                    setText(opnonametb, cwa.CallRS("getopname", now_opno));
                    setSelection(keypartnocb, 0);
                    scanstate = "KeyPart";
                    DataTable locdt = new DataTable();
                    locdt = cwa.CallRDT("getlocationlinebyip", HDSerialNo);
                    if (locdt.Rowscount() == 0) {
                        throw new Exception("無法獲取位置信息");
                    }
                    locationname = locdt.Rows(0).get_CellValue("location").trim();
                    linename = locdt.Rows(0).get_CellValue("linename").trim();
                    execsql = "begin ";
                    setText(inputtb, "");
                    setFocusable(inputtb, true);
                    expendable_Load();
//                    keypartnocb_SelectedIndexChanged();
                    _loadingstatus = true;
                }
            }, "LoadTest");

            inputtb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        shortmapid="scanhc";
                        Intent openCameraIntent = new Intent(expendable.this, CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);
                    }
                    return false;
                }
            });


            keypartnocb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (_loadingstatus) {
                    try {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    keypartnocb_SelectedIndexChanged();
                                }
                            }, "KeyPartChanged Task");

                    } catch (Exception ex) {
                        BaseFuncation.showmessage(expendable.this, "Load KeyPartChanged Error:" + ex.getMessage());
                    }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            checkBoxrepro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBoxrepro_CheckedChanged();
                                }
                            }, "checkBoxreproChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(expendable.this, "Load checkBoxreproChanged Error:" + ex.getMessage());
                    }
                }
            });

            comboBox_afmachineno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    comboBox_afmachineno_TextChanged();
                                }
                            }, "comboBox_afmachinenoChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(expendable.this, "Load comboBox_afmachinenoChanged Error:" + ex.getMessage());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Editafmachineet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        if (_loadingstatus&&!hasFocus) {

                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    comboBox_afmachineno_Leave();
                                }
                            }, "FC IDChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(expendable.this, "Load FC IDChanged Task Error:" + ex.getMessage());
                    }
                }
            });

            /*comboBoxreproduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    comboBoxreproduct_SelectedIndexChanged();//comboBox_afmachineno_TextChanged();
                                }
                            }, "comboBoxreproductChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(expendable.this, "Load comboBoxreproductChanged Error:" + ex.getMessage());
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }

        catch(Exception ex)
        {
            BaseFuncation.showmessage(this,ex.getMessage());
            this.finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            String checkintenttemp1 = null;
            if (scanResult == null) {
                //保持为当前页面
                checkintenttemp1 = "Current";
            } else {
                checkintenttemp1 = scanResult.substring(0, 1);
            }

            /*Intent intent = new Intent(this, expendable.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("Eqid", scanResult);
            bundle1.putString("checktype", "0");
            intent.putExtras(bundle1);
            startActivity(intent);*/
            if (shortmapid=="scanhc") {
                inputtb.setText(scanResult);
                try {
                    ExecTask(new SFCTaskVoidInterface() {
                        @Override
                        public void taskvoid(Object valueo) throws Exception {
                            inputtb_KeyUp();
                        }
                    }, "SCAN Task");
                } catch (Exception ex) {
                    BaseFuncation.showmessage(this, "SCAN Task Error:" + ex.getMessage());
                }
            }
        }
    }

    public void eqrepairenginputsubmit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    execsubmit();
                }
            }, "Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Submit Error:" + ex.getMessage());
        }
    }

    private void execsubmit() throws Exception
    {
        if (getChildCount(Keygw) < 1)
        {
            ShowMessage("請輸入耗材信息後再進行過站");
            return;
        }

        String losematerial = "";
        Boolean checkallscan = true;

        FinalStaticCloass.SpinnerData drv=null;
        DataTable dtexpend = new DataTable();
        if (_opfalg.equals("") || _opfalg == null) {
            dtexpend = cwa.CallRDT("getexpendablelist", now_opno, now_productno);
        } else {
            dtexpend = cwa.CallRDT("getexpendablelistinout", now_opno, now_productno, _checktype);
        }
        for (int i = 0; i < dtexpend.Rowscount(); i++)
        {
            Boolean checktemp = false;
            drv = (FinalStaticCloass.SpinnerData)getItemAtPosition(keypartnocb,i);
            String mcb = dtexpend.Rows(i).get_CellValue("expendableproductno"); //drv.getText().trim();
            for (int j = 0; j < getChildCount(Keygw) ; j++)
            {
                //String mgv = Keygw.Rows[j].Cells["materialcol"].Value.toString().Trim();
                String mgv=getText((TextView) Keygw.getChildAt(j).findViewById(R.id.lisrviewexpendsourceMname1)).toString().trim();
                if (mcb.equals(mgv))
                {
                    checktemp = true;
                    break;
                }
            }
            if (!checktemp)
            {
                losematerial = losematerial + "," + mcb;
                checkallscan = false;
                break;
            }
        }

        if (!checkallscan)
        {
            ShowMessage("耗材信息不完整（"+losematerial+"），请點擊耗材下拉框選取并输入完整信息");
            setFocusable(keypartnocb,true);
            return;
        }

        //对需要特殊检测的料号进行管控
        DataTable materiallotdt = new DataTable();
        materiallotdt.AddColumn("materiallotdc");
        for (int p = 0; p <getChildCount(Keygw); p++)
        {
            String mgv =getText((TextView) Keygw.getChildAt(p).findViewById(R.id.lisrviewexpendsourceMname1)).toString();
            DataTable dtqtycontrol = cwa.CallRDT("expendable_submit_1", mgv);
            if (dtqtycontrol == null)
            {
                ShowMessage("檢測料號是否要管控批號個數異常，請聯繫MIS");
                return;
            }
            else if (dtqtycontrol.Rowscount() > 0)
            {
                String qty = dtqtycontrol.Rows(0).get_CellValue("qty").toString();
                int lotcount = 0;
                for (int t = 0; t < getChildCount(Keygw) - 1; t++)
                {
                    if (getText((TextView) Keygw.getChildAt(p).findViewById(R.id.lisrviewexpendsourceMname1)).toString() == mgv)
                    {
                        lotcount++;
                    }
                }
                if (lotcount != Integer.parseInt(qty))
                {
                    if (isNotRepeat(materiallotdt, mgv))
                    {
                        if(MessageBox("系統提示",mgv + "掃描的耗材批號數量不對應,是否需要强行提交該耗材信息？(" + String.valueOf(lotcount) + "!=" + qty + ")")
                                == BaseFuncation.DialogResult.OK) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class,
                                    _deviceno,
                                    _newdeviceno,
                                    _odbname,
                                    mgv + "掃描的耗材批號個數不等於設定數量<" + String.valueOf(lotcount) + "!=" + qty + ">",
                                    now_lotno,
                                    now_opno,
                                    SFCStaticdata.staticmember.odbname,
                                    "5",
                                    "9",
                                    "41",
                                    "0"
                                    );
                            if (!SFCStaticdata.staticmember.podtestsumcheck)
                            {
                                ShowMessage("验证强行出站失败！");
                                return;
                            }
                        }
                        else
                        {
                            return;
                        }
                    }
                }
            }
            if (isNotRepeat(materiallotdt, mgv))
            {
                materiallotdt.AddRow(mgv);
            }
        }

        execsql = execsql + "end;";
        execsql = execsql.replace("QCidtb", QCIDtb.getText().toString().trim());
        String[] parmers = { "a" };

        Boolean returnflag = cwa.CallRB("checkout_submit_90", execsql, BaseFuncation.SerializeObjectArrayString("a"));
        if (returnflag)
        {
            SFCStaticdata.expendm.expendpageflag = true;
            this.finish();
        }
        else
        {
            SFCStaticdata.expendm.expendpageflag = false;
            ShowMessage("存儲tblwipexpendablelog失敗");
            setText(inputtb,"");
            setFocusable(inputtb,true);
            return;
        }
    }

    private Boolean isNotRepeat(DataTable dt, String no) throws Exception {
        Boolean bl = true;
        if (dt.Rowscount() > 0) {
            for (int i = 0; i < dt.Rowscount(); i++) {
                if (dt.Rows(i).get_CellValue(0) == no) {
                    bl = false;
                    break;
                }
            }
        }
        return bl;
    }

    private void panelreplacematerial(Boolean b) throws Exception
    {
        setEnabled(checkBoxrepro, b);
        setEnabled(comboBoxreproduct, b);
        setEnabled(textBox_replacematerial, b);
    }

    private void checkNoMachineMaterial(String pro, String dev) throws Exception {

        DataTable dtspe = cwa.CallRDT("expendabel_checkNoMachineMaterial", ipstatic);
        if (dtspe.Rowscount() > 0) //特殊IP直接输入批号即可
        {
            setEnabled(inputtb, true);
            checklotnobymachine = false;
        } else {
            DataTable dt = cwa.CallRDT("expendabel_checkNoMachineMaterial_2", pro, dev);
            if (dt == null)//查詢異常
            {
                ShowMessage("查詢該料號是否為需要選擇機台號的料號是發生異常，請聯繫MIS");
                setEnabled(inputtb, false);
                checklotnobymachine = new Boolean(false);
            } else if (dt.Rowscount() > 0)//不需要選機台號的料號
            {
                setEnabled(inputtb, true);
                checklotnobymachine = false;
            } else if (dt.Rowscount() <= 0)//需要選機台號的料號
            {
                setEnabled(inputtb, true);
                checklotnobymachine = true;
            }
        }
    }

    private void getexitdata(String keyno) throws Exception
    {
        //DataTable hdt = new BLL.basedata().getexitmaterialdata(now_lotno, keyno);
        DataTable hdt = cwa.CallRDT("getexitmaterialdata", now_lotno, keyno);
        if (hdt != null)
        {
            if (hdt.Rowscount() > 0)
            {
                for (int i = 0; i < hdt.Rowscount(); i++)
                {
                    View cv= LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_expendsource, null);
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname1)).setText(hdt.Rows(i).get_CellValue(0).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname2)).setText(hdt.Rows(i).get_CellValue(1).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname3)).setText(hdt.Rows(i).get_CellValue(2).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname4)).setText(hdt.Rows(i).get_CellValue(3).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname5)).setText(hdt.Rows(i).get_CellValue(4).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname6)).setText(hdt.Rows(i).get_CellValue(5).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname7)).setText(hdt.Rows(i).get_CellValue(6).trim());
                    ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname8)).setText(hdt.Rows(i).get_CellValue(7).trim());
                    addView(Keygw,cv);
//                    Keygwaddview(hdt.Rows[i][0].toString().Trim(), hdt.Rows[i][1].toString().Trim(), hdt.Rows[i][2].toString().Trim(), hdt.Rows[i][3].toString().Trim(), hdt.Rows[i][4].toString().Trim(), hdt.Rows[i][5].toString().Trim(), hdt.Rows[i][6].toString().Trim(), hdt.Rows[i][7].toString().Trim());
                }
            }
        }
    }

    private void checkserialdata() throws Exception {
        String materialno = ((FinalStaticCloass.SpinnerData) getSelectedItem(keypartnocb)).getText().trim();

        String exceitdata = cwa.CallRS("checkserialsetdata", SFCStaticdata.staticmember.devicetype, materialno);
        if (exceitdata != null && !exceitdata.equals("")) {
            setEnabled(Seriallab, true);
            setEnabled(Serialtxt, true);
        } else {
            setEnabled(Seriallab, false);
            setEnabled(Serialtxt, false);
        }
    }

    private void keypartnocb_SelectedIndexChanged() throws Exception {

        removeAllViewsInLayout(comboBoxreproduct);

        String keyno = ((FinalStaticCloass.SpinnerData) getSelectedItem(keypartnocb)).getText().toString();

        setText(keypartnametb, cwa.CallRS("getkeypartname", keyno));

        DataTable dtexpend = new DataTable();
        dtexpend = cwa.CallRDT("getexpendablelistspec", now_opno, now_productno, keyno);

        if (dtexpend == null || dtexpend.Rowscount() == 0) {
            return;
        }

        keyparttype = dtexpend.Rows(0).get_CellValue("specvalue").trim();
        tablename = dtexpend.Rows(0).get_CellValue("tablename").trim();
        limittime = dtexpend.Rows(0).get_CellValue("checktime").trim();
        lottypemode = dtexpend.Rows(0).get_CellValue("lotmode").trim();
        checklotuse = dtexpend.Rows(0).get_CellValue("checktime1").trim();
        String materialnotype = dtexpend.Rows(0).get_CellValue("att8").trim();
        if (SFCStaticdata.staticmember.lotfolbingpicheck == true && (materialnotype.equals("0") && isFolSubstrateMateiralFormSMTLotDeviceBool == true))//需要管控的機種+FOL+基板
        {
            isFolSubstrateMaterialForSMTLotBool = true;
        } else {
            isFolSubstrateMaterialForSMTLotBool = false;
        }
        setText(textBox_maname, dtexpend.Rows(0).get_CellValue("expendableproductnoname").trim());

        String replacematerial1 = dtexpend.Rows(0).get_CellValue("att2").trim();
        String replacematerial2 = dtexpend.Rows(0).get_CellValue("att3").trim();
        String replacematerial3 = dtexpend.Rows(0).get_CellValue("att7").trim();

        if (replacematerial1.equals("") && replacematerial2.equals("") && replacematerial3.equals("")) {
            panelreplacematerial(false);
        } else {
            panelreplacematerial(true);

            setText(textBox_replacematerial, " * * " + replacematerial1 + " * * " + replacematerial2 + " * * " + replacematerial3);
            BaseFuncation.createspdata bc = new BaseFuncation().new createspdata();
            if (!replacematerial1.equals("")) {
                bc.setvalue("", replacematerial1);
            }
            if (!replacematerial2.equals("")) {
                bc.setvalue("", replacematerial2);
            }
            if (!replacematerial3.equals("")) {
                bc.setvalue("", replacematerial3);
            }
            setAdapter(comboBoxreproduct, bc.getspdata(expendable.this));
            setSelection(comboBoxreproduct, 0);
        }
        if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotcheckeolflag)//RI+ and RI F6 EOL/F1 EOL
        {
            checkNoMachineMaterial(((FinalStaticCloass.SpinnerData) getSelectedItem(keypartnocb)).getText().trim(), SFCStaticdata.staticmember.deviceno);
            if (checklotnobymachine == true)// 需要選擇機台號
            {
                setText(label6, "請輸入機台號:");
                // checkBoxrepro.Enabled = false;
            } else {
                setText(label6, "請掃描耗材批號:");
                // checkBoxrepro.Enabled = true;
            }
        } else {
            if (SFCStaticdata.staticmember.lotcheckeolflag) {
                DataTable dtbfmcsfc = cwa.CallRDT("expendable_submit_2", SFCStaticdata.staticmember.deviceno);
                if (dtbfmcsfc == null) {
                    ShowMessage("檢測該機種是否需要進行BFMC/SFC對接異常");
                    return;
                } else if (dtbfmcsfc.Rowscount() > 0)//需要對接
                {
                    checkNoMachineMaterial(((FinalStaticCloass.SpinnerData) getSelectedItem(keypartnocb)).getText().trim(), SFCStaticdata.staticmember.deviceno);
                    if (checklotnobymachine == true)// 需要選擇機台號
                    {
                        setText(label6, "請輸入機台號:");
                        // checkBoxrepro.Enabled = false;
                    } else {
                        setText(label6, "請掃描耗材批號:");
                        // checkBoxrepro.Enabled = true;
                    }
                }
            }
        }
        if (getChildCount(Keygw) == 1) {
            getexitdata(keyno);
        } else {
            for (int p = 0; p < getChildCount(Keygw) - 1; p++) {
                String tempkeyno = getText((TextView) Keygw.getChildAt(p).findViewById(R.id.lisrviewexpendsourceMname1)).toString();
//                    String tempkeyno = Keygw.Rows[p].Cells[0].Value.toString().Trim();
                if (tempkeyno == keyno) {
                    break;
                } else {
                    if (p == getChildCount(Keygw) - 2) {
                        getexitdata(keyno);
                    }
                }
            }
        }

        setEnabled(checkBoxrepro, true);

        setEnabled(comboBoxreproduct, true);
//            setText(inputtb,"");
        setText(inputtb, "");
        setFocusable(inputtb, true);
//            setFocusable(inputtb,true);
        checkserialdata();
    }


    public String CheckLHAGAiswrong(String exproductno, String materlotno) throws Exception
    {
        DataTable dt_newtable = cwa.CallRDT("expendable_CheckLHAGAiswrong", materlotno);
        if (dt_newtable.Rowscount() >= 1)
        {
            if (!exproductno.equals(dt_newtable.Rows(0).get_CellValue("productno").trim()))
            {
                return "1";
            }
        }
        return "0";
    }


    public DataTable isMultiMachineSelectStation(String opnoselect) throws Exception
    {
        DataTable dt = cwa.CallRDT("expendable_isMultiMachineSelectStation", opnoselect);
        return dt;
    }

    private Boolean checkkeypart(String keypartno, String keypartlotno) throws Exception
    {
        Boolean flag = true;
        int rowcount = getChildCount(Keygw);
        if (rowcount >= 1)
        {
            for (int i = 0; i < rowcount; i++)
            {
                String a = getText((TextView) Keygw.getChildAt(i).findViewById(R.id.lisrviewexpendsourceMname1)).toString().trim();
                String b = getText((TextView) Keygw.getChildAt(i).findViewById(R.id.lisrviewexpendsourceMname2)).toString().trim();
                //if (a == keypartno)// && b == keypartlotno
                if (a.equals(keypartno) && b.equals(keypartlotno))// && b == keypartlotno
                {
                    flag = false;
                    break;
                }

            }
        }
        else
        {
            flag = true;
        }
        return flag;
    }
    private Boolean checkkeypartbak(String keypartno, String keypartlotno) throws Exception
    {
        Boolean flag = false;
        int rowcount = getChildCount(Keygw);
        if (rowcount > 1)
        {
            for (int i = 0; i < rowcount - 1; i++)
            {
                String a = getText((TextView) Keygw.getChildAt(i).findViewById(R.id.lisrviewexpendsourceMname1)).toString().trim();
                String b = getText((TextView) Keygw.getChildAt(i).findViewById(R.id.lisrviewexpendsourceMname2)).toString().trim();
                //if (a == keypartno)// && b == keypartlotno
                if (a == keypartno && b == keypartlotno)// && b == keypartlotno
                {
                    flag = false;
                    break;
                }


                if (i == rowcount - 2)
                {
                    flag = true;
                }
            }
        }
        else
        {
            flag = true;
        }
        return flag;
    }
    public Boolean CheckSubstrateLotSMTBool(String SMTLot) throws Exception
    {
        Boolean bl = false;

        try
        {
            //string sql = "select lotno from t_lotstatus where lotno like :lotnonosub";
            //string[] parmer = { SMTLot + "%" };
            //DataTable dt = new DAL.Ora_dal().OracleExecSqlparmer(sql, parmer, parmer.length(), SFCStaticdata.staticmember.odbname);
            DataTable dt = cwa.CallRDT("expendable_CheckSubstrateLotSMTBool_1", SMTLot);
            if (dt.Rowscount() > 0)
            {
                if (SMTLot.startsWith("S"))
                {
                    bl = true;
                }
                else
                {
                    bl = false;
                }
            }
            else
            {
                bl = false;
            }

        }
        catch(Exception ex)
        {
            ShowMessage("查詢SMT木批號對應的SMT過站信息異常");
            bl = false;
        }
        return bl;
    }
    public Boolean CheckSubstrateLotSMTSUM(String SMTLot, String FOLlot, String opno) throws Exception
    {
        Boolean bl = false;

        String lot_s=cwa.CallRS("expendable_CheckSubstrateLotSMTSUM_1",FOLlot);
        if (lot_s!=null && !lot_s.equals(""))
        {
            bl = true;
            return bl;
        }

        String smtqty = "0";
        String folqty = "0";
        String lotqty = "0";

        String sql = " select opno from t_processdata a,( select processno,to_number(opnoflowid)-1 as opnoflowid from t_processdata where opno=:opno ) b where a.processno=b.processno and a.opnoflowid=b.opnoflowid  ";
        String[] parmer = { opno };
        try
        {
            DataTable dt_3 = cwa.CallRDT("expendable_CheckSubstrateLotSMTSUM_2",opno);
            if (dt_3.Rowscount() > 0)
            {
                opno = dt_3.Rows(0).get_CellValue(0);
            }
            else
            {
                bl = false;
            }

        }
        catch(Exception e1)
        {
            ShowMessage("查詢基板對應的FOL批號數量異常");
            bl = false;
        }


        try
        {
            DataTable dt = cwa.CallRDT("expendable_CheckSubstrateLotSMTSUM_3", opno, SMTLot);
            if (dt.Rowscount() > 0)
            {
                folqty = dt.Rows(0).get_CellValue(0);
                if (folqty.equals(""))
                {
                    folqty = "0";
                }

            }
            else
            {
                bl = false;
            }

        }
        catch(Exception e1)
        {
            ShowMessage("查詢基板對應的FOL批號數量異常");
            bl = false;
        }


        try
        {
            DataTable dt_1 = cwa.CallRDT("expendable_CheckSubstrateLotSMTSUM_4",SMTLot);
            if (dt_1.Rowscount() > 0)
            {
                smtqty = dt_1.Rows(0).get_CellValue(0);
                if (smtqty.equals(""))
                {
                    smtqty = "0";
                }
            }
            else
            {
                bl = false;
            }

        }
        catch(Exception e1)
        {
            ShowMessage("查詢基板對應的SMT批號數量異常");
            bl = false;
        }


        try
        {
            DataTable dt_2 =  cwa.CallRDT("expendable_CheckSubstrateLotSMTSUM_4", FOLlot);
            if (dt_2.Rowscount() > 0)
            {
                lotqty = dt_2.Rows(0).get_CellValue(0);
                if (lotqty.equals(""))
                {
                    lotqty = "0";
                }
            }
            else
            {
                bl = false;
            }

        }
        catch(Exception e1)
        {
            ShowMessage("查詢當前批號數量異常");
            bl = false;

        }

        if (Integer.parseInt(smtqty) + 1000 <Integer.parseInt(folqty) +Integer.parseInt(lotqty))
        {
            ShowMessage("FOL已掃批號數量(" + (Integer.parseInt(folqty)) + "+" + lotqty + ")大於SMT批號的數量(" + smtqty + "+1000)，不可掃描");
            bl = false;
        }
        else
        {
            bl = true;
        }

        return bl;
    }
    private void Keygwaddview(String... ps) throws  Exception{

        if(ps.length!=8)
            throw new Exception("GridView Columns length()()=8,Send Parameters length()()="+String.valueOf(ps.length));
        View cv = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_expendsource, null);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname1)).setText(ps[0]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname2)).setText(ps[1]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname3)).setText(ps[2]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname4)).setText(ps[3]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname5)).setText(ps[4]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname6)).setText(ps[5]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname7)).setText(ps[6]);
        ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname8)).setText(ps[7]);
        addView(Keygw, cv);
    }

    private void inputtb_KeyUp() throws Exception {

        String keypartnocbtext = ((FinalStaticCloass.SpinnerData) getSelectedItem(keypartnocb)).getText().trim();
        String inputtbtext = getText(inputtb).toString().trim();
        String opnotbtext = getText(opnotb).toString().trim();
        Boolean serialtxtisenable = getEnabled(Serialtxt);
        Boolean comboBox_afmachinenoenable = getEnabled(comboBox_afmachineno);
        Boolean EditafmachineetEnabled=getEnabled(Editafmachineet);
        String EditafmachineetText=getText(Editafmachineet).toString().trim();
        //String comboBox_afmachinenotext = ((FinalStaticCloass.SpinnerData) getSelectedItem(comboBox_afmachineno)).getText().trim();
        FinalStaticCloass.SpinnerData ssafmachineno=(FinalStaticCloass.SpinnerData)getSelectedItem(comboBox_afmachineno);
        String comboBox_afmachinenotext =ssafmachineno!=null?ssafmachineno.getText():"";
        Boolean checkBoxreprochecked = isChecked(checkBoxrepro);
        //String comboBoxreproducttext = ((FinalStaticCloass.SpinnerData) getSelectedItem(comboBoxreproduct)).getText().trim();
        FinalStaticCloass.SpinnerData ssreproduct=(FinalStaticCloass.SpinnerData)getSelectedItem(comboBoxreproduct);
        String comboBoxreproducttext =ssreproduct!=null?ssreproduct.getText():"";

        setText(factorylab, cwa.CallRS("expendable_inputtb_keyup_12", keypartnocbtext));
        String factorylabtext = getText(factorylab).toString().trim();


        //核對LHA和GA膠是否相互錄入錯誤
        tempexecsql = "begin ";
        DataTable dt_select = cwa.CallRDT("expendable_inputtb_keyup_1", keypartnocbtext);
        if (dt_select.Rowscount() > 0) {
            String flag = CheckLHAGAiswrong(keypartnocbtext, inputtbtext);
            switch (flag) {
                case "1":
                    ShowMessage("請確認當前批號對應的料號是否正確");
                    return;
                default:
                    break;
            }
        }
        //檢測耗材批號格式是否正確
        Boolean flag1=expendable_input.LENScheckProductnoAndLotno(cwa, keypartnocbtext, inputtbtext);
        if(!flag1)
        {
            ShowMessage("耗材料號對應的批號格式設置錯誤,tblsfcallsetting 8 102");
            return;
        }
        //檢測LENS批號是否有在物料管控系統中過站 8 76
        expendable_input.checkLENScheckinflag(this,cwa,_newdeviceno,now_opno,inputtbtext,keypartnocbtext);


        //check 物料NHA類似機種物料LCM100機台 站位到SFConline 0351_1站位不能超越一個小時
        DataTable dt_checkoverWafer2 = cwa.CallRDT("expendable_inputtb_keyup_2",  opnotbtext);
        if (dt_checkoverWafer2.Rowscount() > 0) {

            if (dt_checkoverWafer2.Rows(0).get_CellValue("att4").equals("1")) {

                DataTable sqllaserfirsttime = cwa.CallRDT("expendable_inputtb_keyup_3", inputtbtext);
                DataTable dt_productno = cwa.CallRDT("expendable_inputtb_keyup_4", keypartnocbtext);

                if (dt_productno.Rowscount() > 0) {

                    if (sqllaserfirsttime.Rowscount() > 0) {
                        String settime=dt_checkoverWafer2.Rows(0).get_CellValue("att2");
                        if (settime == null || settime.equals(""))
                        {
                            settime = "2";
                        }
                        //if (Long.parseLong(sqllaserfirsttime.Rows(0).get_CellValue("Hours").trim()) > 1.1) {
                        if (Double.parseDouble(sqllaserfirsttime.Rows(0).get_CellValue("Hours").trim()) >Double.parseDouble(settime)) {
                            //todo: TaskProcesscontrol.breaklist Need check function
                            DataTable tbreaklistdt = cwa.CallRDT("getbackfronttasklist", sqllaserfirsttime.Rows(0).get_CellValue("classpid").trim(), "BFMC");
                            String[] breaklistss = new String[tbreaklistdt.Rowscount()];

                            for (int i = 0; i < tbreaklistdt.Rowscount(); i++) {
                                breaklistss[i] = tbreaklistdt.Rows(i).get_CellValue(1);
                            }
                            //Object o = MessageBox("BFMC", "P201610002", breaklistss);
                            Object o = MessageBox("Reject List", "驳回节点", breaklistss);
                            Object[] os = (Object[]) o;
                            if (BaseFuncation.DialogResult.Cancel == (BaseFuncation.DialogResult) os[0]) {
                                ShowMessage("請點擊SubMit按鈕，駁回到相應節點，從新過Lens流程！~");
                                return;
                            } else {
                                Hashtable hii = new Hashtable();
                                hii.put("directpar", "backfrontstatus");
                                hii.put("processid", "P201610002");
                                hii.put("dbname", "BFMC");
                                hii.put("userid", sqllaserfirsttime.Rows(0).get_CellValue("createid").trim());
                                hii.put("classpid", sqllaserfirsttime.Rows(0).get_CellValue("classpid").trim());
                                hii.put("flowid", tbreaklistdt.Rows(Integer.parseInt(os[1].toString())).get_CellValue(0).trim());
                                boolean bcw = cwa.CallRB("backfrontstatus", "P201610002", sqllaserfirsttime.Rows(0).get_CellValue("classpid").trim(), tbreaklistdt.Rows(Integer.parseInt(os[1].toString())).get_CellValue(0).trim()
                                        , sqllaserfirsttime.Rows(0).get_CellValue("createid").trim(), "BFMC");

                                if (!bcw) {
                                    ShowMessage("執行失敗，lens節點駁回失敗");
                                    return;
                                }
                                Boolean update_flag = cwa.CallRB("expendable_inputtb_keyup_5", sqllaserfirsttime.Rows(0).get_CellValue("classpid"), inputtbtext);
                                if (update_flag) {
                                    ShowMessage("執行成功，請去lens過站流程裏面過站");
                                    return;
                                }
                            }

                        } else {
                        }
                    } else {
                        ShowMessage("請先過鏡頭過站流程至INV！~");
                        return;
                    }
                }
            }
            else
            {
                DataTable sqllaserfirsttime =cwa.CallRDT("expendable_inputtb_keyup_30", inputtbtext);

                DataTable dt_productno = cwa.CallRDT("expendable_inputtb_keyup_31",keypartnocbtext);

                if (dt_productno.Rowscount() > 0)
                {
                    if (sqllaserfirsttime.Rowscount() > 0)
                    {
                        String checkoverWafer2_att2=dt_checkoverWafer2.Rows(0).get_CellValue("att2");
                        if (Double.parseDouble(sqllaserfirsttime.Rows(0).get_CellValue("Hours")) > Double.parseDouble(checkoverWafer2_att2))
                        {
                            ShowMessage("LCM100 超時" +checkoverWafer2_att2 + "H");
                            return;
                        }
                        else
                        {
                        }
                    }
                    else
                    {
                        ShowMessage("鏡頭未過站至LCM100！~");
                        return;
                    }
                }
            }
        }

        //check 物料NHALENS機種物料plassma 站位到SFConline AA站位不能超越六個小時
        expendable_input.checklensplassmatime(this, cwa, _newdeviceno, now_opno, inputtbtext,keypartnocbtext);

        //check 物料VTQ類似機種物料wafer UV/Clean 站位到SFConline0725站位不能超越一個小時
        DataTable dt_checkoverWafer = cwa.CallRDT("expendable_inputtb_keyup_6", SFCStaticdata.staticmember.str_opno);
        if (dt_checkoverWafer.Rowscount() > 0) {

            //判斷是否在錄入的是wafer的料號
           DataTable dt_waferselect = cwa.CallRDT("expendable_inputtb_keyup_7", keypartnocbtext);
            if (dt_waferselect.Rowscount() > 0) {
                String templotno = "";
                String Ptemplotno = "";
                if (SFCStaticdata.staticmember.str_lotno.startsWith("PK")) {
                    templotno = SFCStaticdata.staticmember.str_lotno.substring(1, 10);
                    Ptemplotno = SFCStaticdata.staticmember.str_lotno.substring(0, 9);
                } else {
                    templotno = SFCStaticdata.staticmember.str_lotno.substring(0,9);
                    Ptemplotno = SFCStaticdata.staticmember.str_lotno.substring(0,9);
                }

                //加入判斷Wafer清洗的次數是否超過三次，如果一片wafer在wafer過站流程中有過三次以上的記錄的時候不能過站，屬於直接報廢品 by Alan
                DataTable dt_cleantimes = cwa.CallRDT("expendable_inputtb_keyup_8", inputtbtext, templotno);

                if (dt_cleantimes.Rowscount() > 3) {
                    ShowMessage("該片Wafer清洗次數已經超過三次，需要報廢，請聯繫相關人員");
                    return;
                }

                //-----------------------------------------------------------------------------------------------------------------------------------
                DataTable sqllaserfirsttime = cwa.CallRDT("expendable_inputtb_keyup_9", templotno, inputtbtext);
                if (sqllaserfirsttime.Rowscount() > 0) {
                    if (sqllaserfirsttime.Rows(0).get_CellValue("opno").trim().equals("INV") || sqllaserfirsttime.Rows(0).get_CellValue("opno").trim().equals("UV/clean")) {

                        DataTable dt_new = cwa.CallRDT("expendable_inputtb_keyup_10", inputtbtext, Ptemplotno);
                        if (dt_new.Rowscount() > 0) {

                        } else {
                            if (Double.parseDouble(sqllaserfirsttime.Rows(0).get_CellValue("Hours").trim()) >=Double.parseDouble(dt_checkoverWafer.Rows(0).get_CellValue("att2").trim())) {
                                ShowMessage("從UVClean站到現在已超" + dt_checkoverWafer.Rows(0).get_CellValue("att2").trim() + "小時(" + sqllaserfirsttime.Rows(0).get_CellValue("Hours").trim() + ")，請重新過物料UVClean站");

                                Boolean flags = cwa.CallRB("expendable_inputtb_keyup_11", inputtbtext, templotno);
                                if (!flags) {
                                    ShowMessage("更新物料系統中的wafer狀態失敗，請聯繫MIS");
                                }
                                return;
                            }
                        }
                    } else {
                        ShowMessage("此Wafer還未過完物料" + sqllaserfirsttime.Rows(0).get_CellValue("opno") + "站位，請先過站");//"此Wafer與綁定工單不一致，請確認Wafer是否用錯");//此Wafer尚未在物料管控系统中过站
                        return;
                    }
                } else {
                    ShowMessage("此Wafer與綁定工單不一致，請確認Wafer是否用錯 ");
                    return;
                }
            }
        }

        //HP 17-02-17  VT-Q 的電容3必須輸入流水嗎
        String serialno = getText(Serialtxt).toString().trim().toUpperCase();
        if (serialtxtisenable == true) {
            if (serialno == null || serialno.equals("")) {
                ShowMessage("該物料" + keypartnocbtext + "必須輸入流水號，請輸入流水號再掃描物料批號");
                setText(inputtb, "");
                setFocusable(Serialtxt, true);
                return;
            } else {
                String serialdeviceno = serialno.substring(serialno.length() - 1,serialno.length());
                if (!serialdeviceno.equals("V")) {
                    ShowMessage("流水碼與所選機種不符，不能掃描");
                    //setText(inputtb, "");
                    //setFocusable(Serialtxt, true);
                    return;
                }
            }
        }

        String factorylabvalue = cwa.CallRS("expendable_inputtb_keyup_32",keypartnocbtext);
        setText(factorylab,factorylabvalue);

        String machinenonow = "";
        String fcno = "";
        String jfbh = "";

        if (comboBox_afmachinenoenable == true && opatt2str.equals("12")) {
            machinenonow = comboBox_afmachinenotext.trim();
            if (machinenonow == null || machinenonow.length() <= 0) {
                ShowMessage("AF機台號不可為空");
                setText(inputtb, "");
                return;
            }
        } else if (EditafmachineetEnabled == true && opatt2str.equals("11")) {
            fcno = EditafmachineetText.trim().toUpperCase();
            if (fcno == null || fcno == "") {
                ShowMessage("請掃入FC冶具名稱，以便統計冶具卡700K數量重新作業");
                return;
            }
        }

        //check smt material 是否需要緩存
        String tempstring = inputtbtext;
        String smtstore = cwa.CallRS("getsmtstorematerial", tempstring);

        switch (tempstring) {
            case "Qid":
                scanstate = "Qid";
                setText(label6, "請掃描Q人員工號﹕");
                setText(inputtb, "");
                setFocusable(inputtb, true);
                break;
            case "submit":
                eqrepairenginputsubmit(null);
                break;
            case "KeyPart":
                scanstate = "KeyPart";
                setText(label6, "請掃描耗材批號");
                setText(inputtb, "");
                setFocusable(inputtb, true);
                break;
            default:
                if (tempstring == null || tempstring.equals("")) {
                    ShowMessage("非法指令");
                    setText(inputtb, "");
                    setFocusable(inputtb, true);
                    return;
                }
                if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") || SFCStaticdata.staticmember.deviceno.equals("NH-A")) {

                    DataTable sudt = cwa.CallRDT("getcustomerdata", tempstring, keypartnocbtext.trim());
                    if (sudt.Rowscount() > 0) {
                        setText(factorylab, sudt.Rows(0).get_CellValue("att1").trim());
                    }

                    setText(factorylab, cwa.CallRS("expendable_inputtb_keyup_13", keypartnocbtext.trim()));
                }

                if (scanstate.equals("Qid")) {

                } else if (scanstate.equals("KeyPart")) {
                    String materialno = keypartnocbtext;
                    //EOL卡膠是否超過開風期  BY HP 16-01-06
                    DataTable jiaodt = cwa.CallRDT("getcheckjiaodata", materialno);
                    if (jiaodt != null && jiaodt.Rowscount() > 0) {
                        String righttime = jiaodt.Rows(0).get_CellValue("att1").trim();
                        if (righttime == null || righttime.equals("")) {
                            ShowMessage("該類型的膠" + materialno + "的開封時間尚未設定，請聯繫MIS部門");
                            return;
                        }

                        DataTable modt = cwa.CallRDT("checkmaterialovertime", materialno, tempstring);
                        if (modt != null && modt.Rowscount() > 0) {
                            String movertime = modt.Rows(0).get_CellValue("overtime").trim();
                            if (Long.parseLong(movertime) > Long.parseLong(righttime)) {
                                ShowMessage("該膠水" + tempstring + "開封時間" + movertime + "已經超過規定的開封時間" + righttime + "小時，不能掃描");
                                return;
                            }
                        } else {
                            ShowMessage("該膠水" + tempstring + "在物料管控系统中没有记录，不能掃描");
                            return;
                        }
                    }


                    if (checklotnobymachine == true && SFCStaticdata.staticmember.lotcheckeolflag)//所有機種的EOL 並且是需要選擇機台號的料號
                    {
                        String tempdeviceno = "";
                        switch (SFCStaticdata.staticmember.deviceno) {
                            case "NH-A":
                                tempdeviceno = "ATF001";
                                break;
                            default:
                                tempdeviceno = SFCStaticdata.staticmember.deviceno;
                                break;
                        }


                        int int_count = 0;
                        if (inputtbtext.contains("DSPR")) {
                            int_count = 2;
                        } else {
                            int_count = 1;
                        }

                        String tempchoosematerial = null;
                        if (checkBoxreprochecked)//替代料以替代料號為主查詢--與輸入批號卡批號格式不同--顧本華
                        {
                            tempchoosematerial = comboBoxreproducttext;
                        } else {

                            tempchoosematerial = keypartnocbtext;
                        }

                        DataTable bfmcDt = cwa.CallRDT("expendable_inputtb_keyup_14", tempchoosematerial, tempstring, tempdeviceno);

                        if (bfmcDt == null) {
                            ShowMessage("查詢該料號對應輸入的機台號上的膠信息時發生異常，請聯繫MIS");
                            return;
                        } else if (bfmcDt.Rowscount() <= 0) {
                            ShowMessage("該料號對應的機台上線上已進料的膠的記錄不存在，請確認");
                            return;
                        } else if (bfmcDt.Rowscount() > int_count) {
                            ShowMessage("該料號對應的機台上線上已進料的膠的數量大於1，請聯繫MIS");
                            return;
                        }
                        DataTable gluedata = isMultiMachineSelectStation(now_opno);
                        if (gluedata.Rowscount() > 0) {
                           DataTable dtmachine = cwa.CallRDT("expendable_inputtb_keyup_15", tempstring);
                            if (SFCStaticdata.staticmember.deviceno.equals("RI-A")) {
                                if (dtmachine == null) {
                                    ShowMessage("查詢該機台號对应胶阀時發生異常，請聯繫MIS");
                                    return;
                                } else if (dtmachine.Rowscount() <= 0) {
                                    ShowMessage("該該機台號对应胶阀不存在，請確認");
                                    return;
                                } else if (dtmachine.Rowscount() > 1) {
                                    ShowMessage("該機台號对应胶阀大於1，請聯繫MIS");
                                    return;
                                }
                                jfbh = dtmachine.Rows(0).get_CellValue("gluename").trim();
                                SFCStaticdata.staticmember.CheckInMultiMachineList.add(dtmachine.Rows(0).get_CellValue("gluename").trim());
                            }
                        }
                        //添加卡控使用次數
                        //String materialno = keypartnocb.Text.toString().Trim();
                        if (checklotuse == null || checklotuse.equals("")) {
                            ShowMessage("耗材對應批號最大使用次數尚未設定，請聯繫MIS");
                            setText(inputtb, "");
                            setFocusable(inputtb, true);
                            return;
                        } else {
                           String lotusernowtime = cwa.CallRS("expendable_inputtb_keyup_16", tempstring, materialno, now_opno);
                            if (lotusernowtime == null || lotusernowtime.equals("")) {
                                ShowMessage("查詢耗材批號已經掃描次數時發生錯誤");
                                setText(inputtb, "");
                                setFocusable(inputtb, true);
                                return;
                            } else {
                                if (Integer.parseInt(lotusernowtime) + 1 > Integer.parseInt(checklotuse)) {
                                    ShowMessage("該批號已經使用超過限定次數，請重新輸入");
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    return;
                                }
                            }
                        }

                        String tempproductno = bfmcDt.Rows(0).get_CellValue("productno").trim();
                        String tempmateriallotno = ""; //增加對N罐膠的管控！~
                        for (int i = 0; i < bfmcDt.Rowscount(); i++) {
                            if ((i + 1) == bfmcDt.Rowscount()) {
                                tempmateriallotno += bfmcDt.Rows(i).get_CellValue("lotno").trim();

                            } else {
                                tempmateriallotno += bfmcDt.Rows(i).get_CellValue("lotno").trim() + ",";

                            }
                        }

                        if (checkkeypart(tempproductno, tempmateriallotno)) {
                            execsql = execsql + " delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + tempproductno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempmateriallotno + "';"
                                    + "insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5) values('" + now_lotno + "','" + tempproductno + "','" + tempmateriallotno + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" +
                                    SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','"
                                    + factorylabtext.trim() + "');";
                            View cv = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_expendsource, null);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname1)).setText(keypartnocbtext);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname2)).setText(tempmateriallotno);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname3)).setText(BaseFuncation.getnowdatetime());
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname4)).setText(locationname);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname5)).setText(linename);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname6)).setText(jfbh);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname7)).setText(factorylabtext);
                            ((TextView) cv.findViewById(R.id.lisrviewexpendsourceMname8)).setText("");
                            addView(Keygw, cv);
                            setText(inputtb, "");
                            setFocusable(inputtb, true);
                            return;
                        } else {
                            ShowMessage("重複掃入");
                            return;
                        }
                    } else {

                        if (checkkeypart(materialno, tempstring)) {
                            //keypartnocb_SelectedIndexChanged(null,null);
                            if (checkBoxreprochecked) {
                                String reproductno = comboBoxreproducttext.trim();
                                DataTable dtrepro = cwa.CallRDT("getreplaceexpendablelistspec", reproductno);
                                if (dtrepro.Rowscount() == 0) {
                                    ShowMessage("替代料尚未設定相關信息，請聯繫MIS");
                                    return;
                                } else {
                                    lottypemode = dtrepro.Rows(0).get_CellValue("lotmode").trim();
                                    checklotuse = dtrepro.Rows(0).get_CellValue("checktime1").trim();
                                }
                            }
                            //keyparttype="4";
                            //只要是基板，不管管控任何類型，首先要看是不是正確的SMT批號
                            if (isFolSubstrateMaterialForSMTLotBool == true) {
                                if (!CheckSubstrateLotSMTBool(tempstring)) {
                                    ShowMessage("該基板批號(SMT母批號)在系統中查詢不到對應的SMT過站信息，請確認批號是否正確");
                                    //setText(inputtb, "");
                                   // setFocusable(inputtb, true);
                                    //return;
                                }
                                //數量檢測 < SMT+1000
                                if (!CheckSubstrateLotSMTSUM(tempstring, now_lotno, now_opno))
                                {
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    return;
                                }

                                expendable_input.checksmtandfoljiban(this,cwa,inputtbtext,keypartnocbtext);
                            }

                            //FOL 卡wafer必須在物料中過站完畢
                            DataTable wadt = cwa.CallRDT("getwafersetmaterialdata", materialno);
                            if (wadt != null) {
                                if (wadt.Rowscount() > 0) {
                                    String templotno = SFCStaticdata.staticmember.str_lotno.split("-")[0];
                                    if (templotno.startsWith("PK") || templotno.startsWith("AK"))
                                    {
                                        templotno = templotno.substring(1, templotno.length());
                                    }
                                    DataTable dkzdt = cwa.CallRDT("getwaferstatedata", tempstring,templotno);
                                    if (dkzdt != null) {
                                        if (dkzdt.Rowscount() > 0) {
                                            String waferopnostr = dkzdt.Rows(0).get_CellValue("opno").trim();
                                            if (waferopnostr.equals("INV") || waferopnostr.equals("END")) {

                                            } else {
                                                ShowMessage("該片wafer尚未在物料管控系統中過站完畢，目前在" + waferopnostr + "站位，不能在此掃描");
                                                return;
                                            }
                                        } else {
                                            ShowMessage("該片wafer尚未在物料管控系統中過站");
                                            return;
                                        }
                                    }
                                }
                            }
                            switch (keyparttype) {

                                case "0"://管控數量
                                    Boolean key0 = cwa.CallRB("keypartavailable", materialno, tempstring, tablename);
                                    if (key0) {
                                        execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';"
                                                + "insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                                + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','"
                                                + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','"
                                                + factorylabtext.trim() + "','" + serialno + "');";

                                        tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                                + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','"
                                                + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','"
                                                + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                        Keygwaddview(
                                                materialno,
                                                tempstring,
                                                BaseFuncation.getnowdatetime(),
                                                locationname,
                                                linename,
                                                "",
                                                "",
                                                serialno
                                        );
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        setText(Serialtxt, "");
                                        return;
                                    } else {
                                        ShowMessage("此耗材不可用，請確認后重新掃描！！！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    }

                                case "1"://管控實效性
                                    //bool key1 = new BLL.basedata().keypartavailable(materialno, tempstring, tablename);
                                    Boolean key1 = cwa.CallRB("keypartavailable", materialno, tempstring, tablename);
                                    if (key1) {
                                       String now_expect = cwa.CallRS("KeyActualeffect", materialno, tempstring, tablename);
                                        if (Long.parseLong(now_expect) <= Long.parseLong(limittime)) {
                                            execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                            tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                                    + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                            Keygwaddview(
                                                    materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "",
                                                    factorylabtext.trim(), serialno);
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            setText(Serialtxt, "");
                                            return;
                                        } else {
                                            ShowMessage("此耗材應該在【" + limittime + "】小時內使用，現已超時【" + now_expect + "】小時，請確認后重新掃描！！！");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }
                                    } else {
                                        ShowMessage("無此耗材批號，請確認后重新掃描！！！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    }

                                case "2"://普通管控
                                    Boolean key2 = cwa.CallRB("keypartavailable", materialno, tempstring, tablename);
                                    if (key2) {
                                        execsql = execsql + " delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "'; insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                        tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                                + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno
                                                + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                        Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);

                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        setText(Serialtxt, "");
                                        return;

                                    } else {
                                        ShowMessage("此耗材不可用，請確認后重新掃描！！！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    }
                                case "3"://只需記錄下
                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';"
                                            + "insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) "
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;

                                case "4":   //比對格式
                                    //lottypemode = "9G@";
                                    if (lottypemode == null || lottypemode.equals("")) {
                                        ShowMessage("料號設定異常，請聯繫MIS重新設定！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {

                                        DataTable EXPP = cwa.CallRDT("expendable_inputtb_keyup_20", keypartnocbtext);//expendable_inputtb_keyup_17
                                        if (EXPP.Rowscount() > 0) {
                                            String hclotno = "";
                                            String hclotnotext = inputtbtext.trim().toUpperCase();
                                            String att4 = EXPP.Rows(0).get_CellValue("att4");
                                            String att5 = EXPP.Rows(0).get_CellValue("att5");
                                            if (att4.equals("1")) {
                                                hclotno = hclotnotext;
                                            }
                                            if (att4.equals("2")) {
                                                hclotno = hclotnotext.substring(4, 11); //hclotno = hclotnotext.substring(4, 7);
                                            }
                                            if (att4.equals("3")) {
                                                hclotno = hclotnotext.substring(0, 5); //hclotno = hclotnotext.substring(0, 5);
                                            }
                                            if (att4.equals("4"))
                                            {
                                                hclotno = hclotnotext.substring(0, 6).toString();
                                            }
                                            if (att4.equals("5"))
                                            {
                                                hclotno = hclotnotext.substring(0, 8).toString();
                                            }
                                            if (att4.equals("6"))
                                            {
                                                hclotno = hclotnotext.split("-")[0].toString();
                                            }
                                            String lotnonew = now_lotno.substring(1, 13);

                                            DataTable ckjltable = new DataTable();
                                            String ckjl = "";
                                            String gdfl = "";
                                            ckjltable = cwa.CallRDT("expendable_inputtb_keyup_26", hclotno, "", "1");

                                            if (ckjltable == null)
                                            {
                                                ShowMessage("連接倉庫收料Webservers失敗，請聯繫MIS部門！");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            }
                                            else
                                            {
                                                Boolean userfalg = false;
                                                String erpmaterialstr = "";
                                                String templserpproductno="";
                                                if (ckjltable.Rowscount() > 0)
                                                {
                                                    templserpproductno= ckjltable.Rows(0).get_CellValue(0);
                                                    for (int r = 0; r < ckjltable.Rowscount(); r++)
                                                    {
                                                        String erpproductno = ckjltable.Rows(r).get_CellValue(0);
                                                        erpmaterialstr = erpmaterialstr + erpproductno + ",";
                                                        if (erpproductno == materialno)
                                                        {
                                                            userfalg = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    ShowMessage("該批號" + hclotno + "尚未在收料系統中掃描，不能使用！");
                                                    setText(inputtb, "");
                                                    setFocusable(inputtb, true);
                                                    return;
                                                }

                                                if (!userfalg)
                                                {
                                                    // 檢測原物料是否可以替代
                                                    Boolean flag=expendable_input.getreplacedta(this,cwa,_newdeviceno,keypartnocbtext,templserpproductno);
                                                    if (!flag)
                                                    {
                                                       ShowMessage("該批號" + hclotno + "在收料系統中對應的料號是" + erpmaterialstr + ",與SFC所選料號不符");
                                                        setText(inputtb, "");
                                                        setFocusable(inputtb, true);
                                                        return;
                                                    }
                                                }
                                            }
                                        } else {
                                            int lotnoleng = lottypemode.length();
                                            int tempstringleng = tempstring.length();
                                            if (lotnoleng != tempstringleng) {
                                                ShowMessage("料號長度異常，請重新掃面");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            }

                                            if (!cwa.CallRB("checkchartypenew", lottypemode, tempstring)) {
                                                ShowMessage("批號格式與提供樣板不同，請重新輸入！" + lottypemode);
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            }
                                        }
                                    }
                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");

                                    break;
                                case "5":   //使用次數 < 一個耗材最多能用於幾個子批號 >
                                    //checklotuse = "1";
                                    if (checklotuse == null || checklotuse.equals("")) {
                                        ShowMessage("耗材對應批號最大使用次數尚未設定，請聯繫MIS");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {

                                        String lotusernowtime = cwa.CallRS("expendable_inputtb_keyup_19", tempstring, materialno, now_opno);
                                        if (lotusernowtime == null || lotusernowtime.equals("")) {
                                            ShowMessage("查詢耗材批號已經掃描次數時發生錯誤");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        } else {
                                            if (Integer.parseInt(lotusernowtime) + 1 > Integer.parseInt(checklotuse)) {
                                                ShowMessage("該批號已經使用超過限定次數，請重新輸入");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            }
                                        }
                                    }

                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;
                                case "6":   //   批號格式    +++    使用次數 < 一個耗材最多能用於幾個子批號 >

                                    if (lottypemode == null || lottypemode.equals("")) {
                                        ShowMessage("料號設定異常，請聯繫MIS重新設定！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {
                                        String hclh = keypartnocbtext;
                                       DataTable EXPP = cwa.CallRDT("expendable_inputtb_keyup_20", hclh);
                                        if (EXPP.Rowscount() > 0) {
                                            String hclotno = "";
                                            String hclotnotext = inputtbtext.trim().toUpperCase();
                                            String att4 = EXPP.Rows(0).get_CellValue("att4");
                                            String att5 = EXPP.Rows(0).get_CellValue("att5");
                                            if (att4.equals("1")) {
                                                hclotno = hclotnotext;
                                            }
                                            if (att4.equals("2")) {
                                                hclotno = hclotnotext.substring(4, 11).toString();//hclotnotext.substring(4, 7).toString();
                                            }
                                            if (att4.equals("3")) {
                                                hclotno = hclotnotext.substring(0, 5).toString();//hclotnotext.substring(0, 5).toString();
                                            }
                                            if (att4.equals("4"))
                                            {
                                                hclotno = hclotnotext.substring(0, 6).toString();
                                            }
                                            if (att4.equals("5"))
                                            {
                                                hclotno = hclotnotext.substring(0, 8).toString();
                                            }
                                            if (att4.equals("6"))
                                            {
                                                hclotno = hclotnotext.split("-")[0].toString();
                                            }
                                            String lotnonew = now_lotno.substring(1, 13);
//
                                            DataTable ckjltable = new DataTable();
                                            DataTable gdfltable = new DataTable();
                                            String ckjl = "";
                                            String gdfl = "";
                                            //String blwip = cwa.CallRS("expendable_inputtb_keyup_18", lotnonew);
                                            ckjltable = cwa.CallRDT("expendable_inputtb_keyup_26", hclotno, "", "1");
                                            //gdfltable = cwa.CallRDT("expendable_inputtb_keyup_26", hclotno, blwip, "2");
                                            if (ckjltable == null) {
                                                ShowMessage("連接倉庫收料Webservers失敗，請聯繫MIS部門！");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;

                                            } else {
                                                Boolean userfalg = false;
                                                String erpmaterialstr = "";
                                                String templserpproductno="";
                                                if (ckjltable.Rowscount() > 0) {
                                                    templserpproductno = ckjltable.Rows(0).get_CellValue(0);
                                                    for (int r = 0; r < ckjltable.Rowscount(); r++) {
                                                        String erpproductno = ckjltable.Rows(r).get_CellValue(0);
                                                        erpmaterialstr = erpmaterialstr + erpproductno + ",";
                                                        if (erpproductno.equals(materialno)) {
                                                            userfalg = true;
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    ShowMessage("該批號" + hclotno + "尚未在收料系統中掃描，不能使用！");
                                                    setText(inputtb, "");
                                                    setFocusable(inputtb, true);
                                                    return;
                                                }
                                                if (!userfalg) {
                                                    // 檢測原物料是否可以替代
                                                    Boolean flag=expendable_input.getreplacedta(this,cwa,_newdeviceno,keypartnocbtext,templserpproductno);
                                                    if (!flag)
                                                    {
                                                        ShowMessage("該批號" + hclotno + "在收料系統中對應的料號是" + erpmaterialstr + ",與SFC所選料號不符");
                                                        setText(inputtb, "");
                                                        setFocusable(inputtb, true);
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            int lotnoleng = lottypemode.length();
                                            int tempstringleng = tempstring.length();
                                            if (lotnoleng != tempstringleng)
                                            {
                                                ShowMessage("料號長度異常，請確認使用物料是否錯誤");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            }

                                            if (checklotuse == null || checklotuse.equals("")) {
                                                ShowMessage("耗材對應批號最大使用次數尚未設定，請聯繫MIS");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            } else {
                                                String lotusernowtime = cwa.CallRS("expendable_inputtb_keyup_19", tempstring, materialno, now_opno);
                                                if (lotusernowtime == null || lotusernowtime.equals("")) {
                                                    ShowMessage("查詢耗材批號已經掃描次數時發生錯誤");
                                                    setText(inputtb, "");
                                                    setFocusable(inputtb, true);
                                                    return;
                                                } else {
                                                    if (Integer.parseInt(lotusernowtime) + 1 > Integer.parseInt(checklotuse)) {
                                                        ShowMessage("該批號已經使用超過限定次數，請重新輸入");
                                                        setText(inputtb, "");
                                                        setFocusable(inputtb, true);
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;
                                case "66":   //   批號格式    +++    使用次數 < 一個耗材最多能用於幾個子批號 >
                                    String tempuserqty = "";
                                    if (lottypemode == null || lottypemode.equals("")) {
                                        ShowMessage("料號設定異常，請聯繫MIS重新設定！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {
                                        int lotnoleng = lottypemode.length();
                                        int tempstringleng = tempstring.length();
                                        if (lotnoleng != tempstringleng) {
                                            ShowMessage("料號長度異常，請重新掃面");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }

                                        //if (!new BLL.basedata().checkchartypenew(lottypemode, tempstring))
                                        if (!cwa.CallRB("checkchartypenew", lottypemode, tempstring)) {
                                            ShowMessage("批號格式與提供樣板不同，請重新輸入！" + lottypemode);
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }
                                    }

                                    String sumqty = cwa.CallRS("getmaterialsumqtydata", keypartnocbtext);
                                    if (sumqty == null || sumqty.equals("") || sumqty.equals("0")) {
                                        ShowMessage("耗材對應批號最大使用次數尚未設定，請聯繫MIS");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {

                                        String lotusernowtime = cwa.CallRS("expendable_inputtb_keyup_21", tempstring, materialno, now_opno);
                                        if (lotusernowtime == null || lotusernowtime.equals("")) {
                                            ShowMessage("查詢耗材批號已經掃描次數時發生錯誤");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        } else {
                                            if (Integer.parseInt(lotusernowtime) > Integer.parseInt(checklotuse)) {
                                                ShowMessage("該批號已經使用超過限定数量，請重新輸入");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            } else {
                                                if (Integer.parseInt(checklotuse) - Integer.parseInt(lotusernowtime) >= Integer.parseInt(SFCStaticdata.staticmember.checkinqty)) {
                                                    tempuserqty = SFCStaticdata.staticmember.checkinqty;
                                                } else {
                                                    tempuserqty = String.valueOf(Integer.parseInt(checklotuse) - Integer.parseInt(lotusernowtime)).trim();
                                                }
                                            }
                                        }
                                    }

                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att6,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + tempuserqty + "','" + serialno + "');";


                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;
                                case "7":   //比對格式  --  只限長度
                                    //lottypemode = "9G@";
                                    if (lottypemode == null || lottypemode.equals("")) {
                                        ShowMessage("料號設定異常，請聯繫MIS重新設定！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {
                                        int lotnoleng = lottypemode.length();
                                        int tempstringleng = tempstring.length();
                                        if (lotnoleng != tempstringleng) {
                                            ShowMessage("料號長度異常，請重新掃面");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }
                                    }
                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, "", factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;

                                case "8":
                                   DataTable dtwafer = cwa.CallRDT("expendable_inputtb_keyup_22", tempstring, materialno);
                                    if (dtwafer.Rowscount() > 0) {
                                        String waferstatus = dtwafer.Rows(0).get_CellValue("status");
                                        String wafermaterialno = dtwafer.Rows(0).get_CellValue("lotno");  // 耗材批號
                                        String waferupdate = dtwafer.Rows(0).get_CellValue("updatetime");  // 最夠更改時間
                                        if (!waferstatus.equals("99")) {
                                            ShowMessage("Wafer批號尚未執行其對應流程過站，請先掃描Wafer過站！" + dtwafer.Rows(0).get_CellValue("opno").trim());
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }
                                         String dtwaferstr = cwa.CallRS("expendable_inputtb_keyup_23", tempstring, materialno);
                                        if (dtwaferstr == null || dtwaferstr.equals("")) {
                                            ShowMessage("查詢Wafer批號對應清洗時間為空，請聯繫MIS進行確認！" + dtwafer.Rows(0).get_CellValue("opno").trim());
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        } else {
                                           String timedbnow = cwa.CallRS("getdbsystime");
                                            if (BaseFuncation.getnowdatetime(dtwaferstr, "yyyy/MM/dd HH:mm:ss").getTime() + 60 * 60 * 1000
                                                    < BaseFuncation.getnowdatetime(timedbnow, "yyyy/MM/dd HH:mm:ss").getTime()) {
                                                ShowMessage("Wafer清洗已經超過 1 小時，請重新進行清洗動作！"
                                                        + BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(dtwaferstr, "yyyy/MM/dd HH:mm:ss").getTime()
                                                        , "yyyy/MM/dd HH:mm:ss"));
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                return;
                                            }
                                        }
                                    } else {
                                        ShowMessage("Wafer料號與現需料號不符！" + dtwafer.Rows(0).get_CellValue("materialno").trim());
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    }

                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "';"
                                            + "insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";


                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";
                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, jfbh, factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;
                                case "9"://6標誌+連接物料管控系統
                                    //當站位卡批號是否有過物料管控系統過站和扣帳記錄，並且卡安全開封期 20140922-add gu
                                    if (lottypemode == null || lottypemode.equals("")) {
                                        ShowMessage("料號設定異常，請聯繫MIS重新設定！");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {
                                        int lotnoleng = lottypemode.length();
                                        int tempstringleng = tempstring.length();
                                        if (lotnoleng != tempstringleng) {
                                            ShowMessage("料號長度異常，請重新掃面");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }

                                        if (!cwa.CallRB("checkchartypenew", lottypemode, tempstring)) {
                                            ShowMessage("批號格式與提供樣板不同，請重新輸入！" + lottypemode);
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            return;
                                        }
                                    }

                                    if (checklotuse == null || checklotuse.equals("")) {
                                        ShowMessage("耗材對應批號最大使用次數尚未設定，請聯繫MIS");
                                        setText(inputtb, "");
                                        setFocusable(inputtb, true);
                                        return;
                                    } else {
                                        String lotusernowtime = cwa.CallRS("expendable_inputtb_keyup_16", tempstring, materialno, now_opno);
                                        if (lotusernowtime == null || lotusernowtime.equals("")) {
                                            ShowMessage("查詢耗材批號已經掃描次數時發生錯誤");
                                            setText(inputtb, "");
                                            setFocusable(inputtb, true);
                                            setText(Serialtxt, "");
                                            return;
                                        } else {
                                            if (Integer.parseInt(lotusernowtime) + 1 > Integer.parseInt(checklotuse)) {
                                                ShowMessage("該批號已經使用超過限定次數，請重新輸入");
                                                setText(inputtb, "");
                                                setFocusable(inputtb, true);
                                                setText(Serialtxt, "");
                                                return;
                                            }
                                        }
                                    }

                                    DataTable dtbfmc = cwa.CallRDT("expendable_inputtb_keyup_24", materialno, tempstring);
                                    if (dtbfmc == null) {
                                        ShowMessage("查詢物料管控系統該批號的記錄失敗，請聯繫MIS");
                                        return;
                                    } else if (dtbfmc.Rowscount() <= 0) {
                                        if (checkBoxreprochecked)//如果有選擇替代料，而且該批號沒有主料信息，那麼就再查一次替代料是否有批號過站的信息-BFMC
                                        {

                                            dtbfmc = cwa.CallRDT("expendable_inputtb_keyup_24", comboBoxreproducttext, tempstring);
                                            if (dtbfmc == null) {
                                                ShowMessage("查詢物料管控系統該批號的記錄失敗，請聯繫MIS");
                                                return;
                                            } else if (dtbfmc.Rowscount() <= 0) {
                                                ShowMessage("該批號在物料管控系統中沒有相應的記錄，請聯繫MIS");
                                                return;
                                            } else if (dtbfmc.Rowscount() > 0) {
                                                if (dtbfmc.Rows(0).get_CellValue("workidstatus").trim().equals("4"))//已經END
                                                {
                                                    ShowMessage("該批號已經END，不可進行作業");
                                                    return;
                                                } else {
                                                    if (dtbfmc.Rows(0).get_CellValue("lotstatus").trim().equals("8")) {
                                                        ShowMessage("該批號已經扣帳，不可進行作業");
                                                        return;
                                                    } else {
                                                       String timepart = cwa.CallRS("expendable_inputtb_keyup_25", dtbfmc.Rows(0).get_CellValue("att4").trim());
                                                        if (timepart == null) {
                                                            ShowMessage("獲取當前批號是否已過安全開封器信息失敗，請聯繫MIS");
                                                            return;
                                                        }
                                                        long timed = Long.parseLong(timepart);
                                                        if (timed >= 0) {
                                                            ShowMessage("該批號的安全開封到期時間為" + dtbfmc.Rows(0).get_CellValue("att4").trim() + "，已經過期，不可作業");
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            ShowMessage("該批號在物料管控系統中沒有相應的記錄，請聯繫MIS");
                                            return;
                                        }

                                    } else if (dtbfmc.Rowscount() > 0) {
                                        if (dtbfmc.Rows(0).get_CellValue("workidstatus").trim().equals("4"))//已經END
                                        {
                                            ShowMessage("該批號已經END，不可進行作業");
                                            return;
                                        } else {
                                            if (dtbfmc.Rows(0).get_CellValue("lotstatus").trim().equals("8")) {
                                                ShowMessage("該批號已經扣帳，不可進行作業");
                                                return;
                                            } else {
                                               String timepart = cwa.CallRS("expendable_inputtb_keyup_25", dtbfmc.Rows(0).get_CellValue("att4").trim());
                                                if (timepart == null) {
                                                    ShowMessage("獲取當前批號是否已過安全開封器信息失敗，請聯繫MIS");
                                                    return;
                                                }
                                                Long timed = Long.parseLong(timepart);
                                                if (timed >= 0) {
                                                    ShowMessage("該批號的安全開封到期時間為" + dtbfmc.Rows(0).get_CellValue("att4").trim() + "，已經過期，不可作業");
                                                    return;
                                                }
                                            }
                                        }
                                    }

                                    execsql = execsql + "delete from tblwipexpendablelotlog where lotno ='" + now_lotno + "' and materialproductno ='" + materialno + "' and opno ='" + now_opno + "' and materiallotno ='" + tempstring + "' ;"
                                            + "insert into tblwipexpendablelotlog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7) "
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";


                                    tempexecsql = tempexecsql + "insert into tblwipexpendablelottemplog (lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5,att7)"
                                            + "values('" + now_lotno + "','" + materialno + "','" + tempstring + "','" + now_opno + "',sysdate,'" + locationname + "','" + linename + "','" + SFCStaticdata.staticmember.userid + "','QCidtb','" + lotserialpage + "','" + SFCStaticdata.staticmember.ip + "','" + machinenonow + "','" + fcno + "','" + factorylabtext.trim() + "','" + serialno + "');";

                                    Keygwaddview(materialno, tempstring, BaseFuncation.getnowdatetime(), locationname, linename, jfbh, factorylabtext.trim(), serialno);
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;


                                default:
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    setText(Serialtxt, "");
                                    break;
                            }

                            tempexecsql = tempexecsql + "end;";
                            String[] parmers = {};
                            //Boolean returnflag = cwa.CallRB("execinfo", tempexecsql, BaseFuncation.SerializeObjectArrayString(parmers));
                             //插入緩存信息
                            Boolean returnflag = false;
                            if (smtstore != null && !smtstore.equals(""))
                            {
                                returnflag = cwa.CallRB("execinfo", tempexecsql);

                                if (!returnflag)
                                {
                                    SFCStaticdata.expendm.expendpageflag = false;
                                    ShowMessage("存儲tblwipexpendablelottemplog失敗");
                                    setText(inputtb, "");
                                    setFocusable(inputtb, true);
                                    return;
                                }
                            }


                        } else {
                            ShowMessage("重複掃描！！！");
                            setText(inputtb, "");
                            setFocusable(inputtb, true);
                            setText(Serialtxt, "");
                            return;
                        }
                    }

                }
                break;
        }
    }
    private void checkBoxrepro_CheckedChanged() throws Exception {
        Boolean checkBoxreproChecked = isChecked(checkBoxrepro);
        String comboBoxreproducttext = ((FinalStaticCloass.SpinnerData) getSelectedItem(comboBoxreproduct)).getText().trim();
        String keypartnocbtext = ((FinalStaticCloass.SpinnerData) getSelectedItem(keypartnocb)).getText().trim();
        if (checkBoxreproChecked)//選中了則用替代料好判斷
        {
            ShowMessage("耗材料號已確定為替代料");
            setEnabled(comboBoxreproduct, true);
            if (checkBoxreproChecked) {
                if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotcheckeolflag)//RI+ and RI F6 EOL/F1 EOL
                {
                    checkNoMachineMaterial(comboBoxreproducttext, SFCStaticdata.staticmember.deviceno);
                    if (checklotnobymachine == true)// 需要選擇機台號
                    {
                        setText(label6, "請輸入機台號");
                    } else {
                        setText(label6, "請掃描耗材批號");
                    }
                } else {
                    if (SFCStaticdata.staticmember.lotcheckeolflag) {

                        DataTable dtbfmcsfc = cwa.CallRDT("expendable_checkBoxrepro_CheckedChanged");
                        if (dtbfmcsfc == null) {
                            ShowMessage("檢測該機種是否需要進行BFMC/SFC對接異常");
                            return;
                        } else if (dtbfmcsfc.Rowscount() > 0)//需要對接
                        {
                            checkNoMachineMaterial(keypartnocbtext.trim(), SFCStaticdata.staticmember.deviceno);
                            if (checklotnobymachine == true)// 需要選擇機台號
                            {
                                setText(label6, "請輸入機台號");
                            } else {
                                setText(label6, "請掃描耗材批號:");
                            }
                        }
                    }
                }
            }
            return;
        } else//沒選中就是用主料判斷
        {
            if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotcheckeolflag)//RI+ and RI F6 EOL/F1 EOL
            {
                checkNoMachineMaterial(keypartnocbtext.trim(), SFCStaticdata.staticmember.deviceno);
                if (checklotnobymachine == true)// 需要選擇機台號
                {
                    setText(label6, "請輸入機台號");
                } else {
                    setText(label6, "請掃描耗材批號:");
                }
            } else {
                if (SFCStaticdata.staticmember.lotcheckeolflag) {
                    DataTable dtbfmcsfc = cwa.CallRDT("expendable_checkBoxrepro_CheckedChanged");
                    if (dtbfmcsfc == null) {
                        ShowMessage("檢測該機種是否需要進行BFMC/SFC對接異常");
                        return;
                    } else if (dtbfmcsfc.Rowscount() > 0)//需要對接
                    {
                        checkNoMachineMaterial(keypartnocbtext.trim(), SFCStaticdata.staticmember.deviceno);
                        if (checklotnobymachine == true)// 需要選擇機台號
                        {
                            setText(label6, "請輸入機台號");
                        } else {
                            setText(label6, "請掃描耗材批號");
                        }
                    }
                }
            }
            setEnabled(comboBoxreproduct, false);
        }
    }

    public String lineGet(String lotno) throws Exception
    {
        String lineno = "";
        //String sql = "select charvalue from T_LOTEQUPLINELOG where lotno=:lot and CHARACTERID='C0005'";
        //String[] parmer = { lotno };
        //lineno = new DAL.Ora_dal().OracleExecstringparmer(sql, parmer, parmer.Length, BLL.staticmember.odbname);
        lineno = cwa.CallRS("expendable_lineGet", lotno);
        return lineno;
    }
    private void expendable_Load() throws Exception
    {
        DataTable gluedata = isMultiMachineSelectStation(now_opno);
        if (SFCStaticdata.staticmember.deviceno.equals("RI-A") && now_opno.equals("0610") && gluedata.Rowscount() > 0)
        {
            SFCStaticdata.staticmember.CheckInMultiMachineList = new ArrayList<String>();
        }
        //opatt2str = new BLL.basedata().getopatt2(now_opno);
        opatt2str = cwa.CallRS("getopatt2", now_opno);
        if (opatt2str.equals("12"))
        {
            setEnabled(comboBox_afmachineno,true);
            String lineno = lineGet(now_lotno);
            if (lineno.length() == 9)
            {
                lineno =lineno.substring(0, 8) + "0" + lineno.substring(8, 9); //lineno.substring(0, 8) + "0" + lineno.substring(8, 1);
            }
            //String sql = "select att1 from tblsfcallsetting where type='0' and status='2' and att2=:lines order by att1";
            //String[] parmer = { lineno };
            //DataTable dtmachineno = new DAL.Ora_dal().OracleExecSqlparmer(sql, parmer, parmer.Length, "N41CONN");
            DataTable dtmachineno = cwa.CallRDT("expendable_load_1", lineno);
            if (dtmachineno == null)
            {
                ShowMessage("查詢該批號線別設定的AF機台號出錯，請聯繫MIS"); return;
            }
            else if (dtmachineno.Rowscount() <= 0)
            {
                ShowMessage(now_lotno + "對應的線別" + lineno + "尚未設定對應AF機台號，請聯繫MIS"); return;
            }
            else if (dtmachineno.Rowscount() > 0)
            {
                setAdapter(comboBox_afmachineno,BaseFuncation.setvalue(dtmachineno,"","att1",expendable.this));
                setSelection(comboBox_afmachineno,0);
            }
        }
        else if (opatt2str.equals("11") && (SFCStaticdata.staticmember.englotcheckflag == true || SFCStaticdata.staticmember.engRilotcheckflag == true))//FC,UV,GA
        {
            //String sqlfc = "select item,att1 from tblsfcallsetting where type='0' and status='11' and  item=:opno";
            //String[] parmersfc = { now_opno };
            //DataTable dtfc = new DAL.Ora_dal().OracleExecSqlparmer(sqlfc, parmersfc, parmersfc.Length, BLL.staticmember.odbname);
            DataTable dtfc = cwa.CallRDT("expendable_load_2", now_opno);
            if (dtfc == null)
            {
                ShowMessage("查詢該站位是否為FC站位卡冶具700K數量是發生異常(tblsfcallsetting/status='11')，請聯繫MIS");
                return;
            }
            else if (dtfc.Rowscount() > 0)
            {
                setEnabled(Editafmachineet,true);
//                comboBox_afmachineno.DropDownStyle = ComboBoxStyle.DropDown;
            }
        }
        else
        {
            setEnabled(comboBox_afmachineno,false);
            setEnabled(Editafmachineet,false);
        }

        if (SFCStaticdata.staticmember.lotfolbingpicheck == true)
        {
            //string substratesql = "select item from tblsfcallsetting where type='0' and status='55' and item=:itemnow";
            //string[] substrateparmer = { BLL.staticmember.deviceno };
            //DataTable substratedt = new DAL.Ora_dal().OracleExecSqlparmer(substratesql, substrateparmer, substrateparmer.Length, BLL.staticmember.odbname);
            DataTable substratedt = cwa.CallRDT("expendable_load_3");
            if (substratedt == null)
            {
                ShowMessage("查詢該機種是否要檢驗基板批號的正確性時發生異常，請聯繫MIS");
                isFolSubstrateMateiralFormSMTLotDeviceBool = false;
                return;
            }
            else if (substratedt.Rowscount() > 0)
            {
                isFolSubstrateMateiralFormSMTLotDeviceBool = true;
            }
            else
            {
                isFolSubstrateMateiralFormSMTLotDeviceBool = false;
            }
        }
        keypartnocb_SelectedIndexChanged();
    }

    private void comboBox_afmachineno_TextChanged() throws Exception
    {
        Boolean comboBox_afmachinenoEnable=getEnabled(Editafmachineet);
       if (opatt2str == "11" && comboBox_afmachinenoEnable)
        {
            execsql = "begin ";
            removeAllViewsInLayout(Keygw);
       }
    }

    public String getqty(String opno, String fcno) throws Exception
    {
        DataTable dbnamedt = cwa.CallRDT("expendable_getqty_1");
        if (dbnamedt == null)
        {
            ShowMessage("查詢RI/RI+對應的所有機種信息時發生異常，請聯繫MIS"); return null;
        }
        else if (dbnamedt.Rowscount() <= 0)
        {
            ShowMessage("RI/RI+對應的機種關係尚未設定，請聯繫MIS"); return null;
        }
        int qty = 0;
        String sql = "select nvl(sum(inputqty),0) LOTQTY from t_lotlog where lotno in(select distinct lotno from tblwipexpendablelotlog where att4=:att4flag) and opno=:opnoflag union  select nvl(sum(DIEQTY),0) LOTQTY from t_lotSTATUS where lotno in(select distinct lotno from tblwipexpendablelotlog where att4=:att4flag1) and opno=:opnoflag1";//進站數量
        String[] parmer = { fcno, opno, fcno, opno };
        for (int j = 0; j < dbnamedt.Rowscount(); j++)//逐個查詢個機種里已經綁定的數量
        {
            //DataTable dt = new DAL.Ora_dal().OracleExecSqlparmer(sq, parmer, parmer.Length, dbnamedt.Rows[j]["dbname"].ToString().Trim());
            DataTable dt = cwa.CallRDT("expendable_getqty_2", fcno, opno, dbnamedt.Rows(j).get_CellValue("dbname").trim());
            if (dt == null)
            {
                return null;
            }
            else
            {
                for (int i = 0; i < dt.Rowscount(); i++)
                {
                    qty = qty + Integer.parseInt(dt.Rows(i).get_CellValue(0).trim());
                }
            }
        }

        return String.valueOf(qty);
    }

    public Boolean isRealFcNozzleNo(String fcno)
    {
        String fctno = fcno;
        if (fctno.length() != 6)
        {
            return false;
        }
        if (!fcno.substring(0, 2).equals("FC"))
        {
            return false;
        }
        //if (!fcno.substring(2, 1).equals("T") && !fcno.substring(2, 1).equals("X") && !fcno.substring(2, 1).equals("W"))
        if (!fcno.substring(2, 3).equals("T") && !fcno.substring(2, 3).equals("X") && !fcno.substring(2, 3).equals("W"))
        {
            return false;
        }
        return true;
    }
    private void comboBox_afmachineno_Leave() throws Exception
    {
        Boolean comboBox_afmachinenoEnable=getEnabled(Editafmachineet);
        String comboBox_afmachinenoText= getText(Editafmachineet).toString();
        if (opatt2str.equals("11") && comboBox_afmachinenoEnable)
        {
            execsql = "begin ";
            removeAllViewsInLayout(Keygw);

            String opno = now_opno;
            String fcno = comboBox_afmachinenoText.toUpperCase();
            Boolean bl = isRealFcNozzleNo(fcno);
            if (bl == false)
            {
                ShowMessage("請輸入正確的Nozzle號");
                setText(Editafmachineet,"");
                return;
            }
            String qty = getqty(opno, fcno);
            if (qty == null)
            {
                setText(Editafmachineet,"");
                ShowMessage("查詢" + fcno + "在FC站位已掃入批號對應的數量失敗，請聯繫MIS");
                return;
            }
            else
            {

                if (Integer.parseInt(qty) >= 700000)
                {
                    setText(Editafmachineet,"");
                    ShowMessage(fcno + "在FC站位已掃入批號對應的數量為" + qty + ",已等於/超過700K的數量，該冶具不可再作業，知悉！"); return;
                }


                //string sql_mail = "select * from tblsfcallsetting where type='9' and status='30' and item='" + fcno + "' ";
                //DataTable dt_mail = new DAL.Ora_dal().OracleExecSqlparmer(sql_mail, null, 0, "N41CONN");
                DataTable dt_mail = cwa.CallRDT("expendable_getqty_3", fcno);
                if (Integer.parseInt(qty) >= 500000 && dt_mail.Rowscount() == 0)
                {
                    DataTable maildata = new DataTable();
                    maildata.AddColumn("機種");
                    maildata.AddColumn("nozzleno");
                    maildata.AddColumn("已掃數量");
                    maildata.AddColumn("線別");
                    maildata.AddColumn("時間");
                    DataTable dt_line = cwa.CallRDT("expendable_getqty_4", fcno);

                    maildata.AddRow(SFCStaticdata.staticmember.deviceno,
                            fcno,
                            qty,
                            dt_line.Rows(0).get_CellValue("linename").trim(), BaseFuncation.getnowdatetime());
                    //new BLL.basedata().execSendMailAll(maildata, "Nozzle500k", "【預警郵件】Nozzle 已掃數量 500K預警 ", "1");
                    cwa.CallRB("execSendMailAll",BaseFuncation.SerializeObjectDataTable(maildata), "Nozzle500k", "【預警郵件】Nozzle 已掃數量 500K預警 ", "1");
                    String in_mail = "insert into tblsfcallsetting values('" + fcno + "','9','30','" + SFCStaticdata.staticmember.ip + "','" + SFCStaticdata.staticmember.userid + "',sysdate,'','','','','已發送預警郵件的Nozzle') ";
                    Boolean in_flag = cwa.CallRB("expendable_getqty_5", fcno, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.userid);
                }
                setFocusable(inputtb,true);
            }
        }
    }

    private void comboBoxreproduct_SelectedIndexChanged() throws  Exception
    {
        Boolean checkBoxreproChecked=isChecked(checkBoxrepro);
        String comboBoxreproductText=((FinalStaticCloass.SpinnerData)getSelectedItem(comboBoxreproduct)).getText().trim();
        String keypartnocbText=((FinalStaticCloass.SpinnerData)getSelectedItem(keypartnocb)).getText().trim();

        if (checkBoxreproChecked)//替代料好變動，則用替代料好判斷
        {
            if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotcheckeolflag)//RI+ and RI F6 EOL/F1 EOL
            {
                checkNoMachineMaterial(comboBoxreproductText, SFCStaticdata.staticmember.deviceno);
                if (checklotnobymachine == true)// 需要選擇機台號
                {
                    setText(label6,"請輸入機台號:");
                }
                else
                {
                    setText(label6,"請掃描耗材批號:");
                }
            }
            else
            {
                if (SFCStaticdata.staticmember.lotcheckeolflag)
                {
                    DataTable dtbfmcsfc = cwa.CallRDT("expendable_comboBoxreproduct_SelectedIndexChanged");
                    if (dtbfmcsfc == null)
                    {
                        ShowMessage("檢測該機種是否需要進行BFMC/SFC對接異常");
                        return;
                    }
                    else if (dtbfmcsfc.Rowscount() > 0)//需要對接
                    {
                        checkNoMachineMaterial(keypartnocbText, SFCStaticdata.staticmember.deviceno);
                        if (checklotnobymachine == true)// 需要選擇機台號
                        {
                            setText(label6,"請輸入機台號:");
                        }
                        else
                        {
                            setText(label6,"請掃描耗材批號:");
                            // checkBoxrepro.Enabled = true;
                        }
                    }
                }
            }
        }
    }

    public void expendsourceonclik(final View view) {
        final String materialno = ((TextView) view.findViewById(R.id.lisrviewexpendsourceMname1)).getText().toString();
        final String mlotno = ((TextView) view.findViewById(R.id.lisrviewexpendsourceMname2)).getText().toString();
        try {
            this.ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Boolean falg = cwa.CallRB("deleteexitmaterialdata", now_lotno, materialno, mlotno);
                    if (!falg) {
                        ShowMessage("刪除數據失敗，請聯繫MIS部門");
                        return;
                    } else {
                        removeView(Keygw, view);
//                        Keygw.removeView(view);
                    }
                }
            }, "Keygw Delete Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Keygw Delete Task Error:" + ex.getMessage());
        }
    }

}
