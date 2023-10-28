package app.cmapp.IncludingSFC;
/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.DataTable.DataTable;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.NetUtils;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.cmapp.appcdl.SFCBLLPack.checkoutsubmit;
import app.dpapp.R;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class checkout extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _sendproductno, _processno, _sendopnoflowid;
    private String _lotno;
    private String _dieqty;
    private String _opno;
    private TextView lotnotextBox;
    private TextView dietextBox;
    private TextView newdevicenotextBox;
    private TextView devicenotextBox;
    private TextView opnotextBox;
    private TextView opnametextBox;
    private Spinner comboBoxgsbno;
    private TextView nextopnotextBox;
    private TextView nextopnametextBox;
    private Spinner comboBoxline;
    private CheckBox DetailInfor;
    private EditText defectqtytextBox;
    private LinearLayout errordataGridView;

    private RadioButton raditButton80;
    private RadioButton raditButton315;
    private RadioButton raditButton500;
    private CheckBox checkBox_dblotno;
    private EditText textBox_dbqty;
    private Button button2;
    private CheckBox checkBoxdb;
    private Button dbbutton;

    private CheckBox checkBox_yieldcor;
    private CheckBox checkBox1;
    private CheckBox checkBox_folcheck;

    private CheckBox checkBox_pod;
    private RadioButton radiobutton_all;
    private RadioButton radiobutton_part;

    private CheckBox checkBox_paoliao;
    private EditText textBox_paoliao;
    private Button button_addpaoliao;

    private EditText Lhatxt;
    private EditText textBoxfqcop;
    private CheckBox OQCbackchcb;
    private Spinner OQCundoOPNOcb;
    private EditText textBox_textqty;
    private EditText textBox_qreturnsum;
    private Button submitbutton;

    private Button endlotbt;
    private TextView label6;
    private RadioGroup panel_podset;
    private RadioGroup panelsmtqc;

    private String ipstatic;
    private String HDSerialNo;
    int staticopnoflowid;
    String staticproductno;
    Boolean gsbnoBoolean=false;
    Boolean reworknocheck = false;
    String incomingsql = "";
    String[] incomingp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_checkout);
            ipstatic = NetUtils.getLocalIPAddress(this);
            HDSerialNo=SFCStaticdata.staticmember.HDSerialNo;
            lotnotextBox = (TextView) findViewById(R.id.isfc_checnkout_lotnotbv1);
            dietextBox = (TextView) findViewById(R.id.isfc_checnkout_dieqtyv1);
            newdevicenotextBox = (TextView) findViewById(R.id.isfc_checnkout_newdevicev1);
            devicenotextBox = (TextView) findViewById(R.id.isfc_checnkout_devicev1);
            opnotextBox = (TextView) findViewById(R.id.isfc_checnkout_opnotbv1);
            opnametextBox = (TextView) findViewById(R.id.isfc_checnkout_opnamev1);
            comboBoxgsbno = (Spinner) findViewById(R.id.isfc_checnkout_gsbsp1);
            nextopnotextBox = (TextView) findViewById(R.id.isfc_checnkout_nextopnotbv1);
            nextopnametextBox = (TextView) findViewById(R.id.isfc_checnkout_nextopnamev1);
            comboBoxline = (Spinner) findViewById(R.id.isfc_checnkout_comboBoxlinesp1);
            DetailInfor = (CheckBox) findViewById(R.id.isfc_checnkout_detailinforcb1);
            defectqtytextBox = (EditText) findViewById(R.id.isfc_checnkout_defectqtyet1);
            errordataGridView = (LinearLayout) findViewById(R.id.isfc_checnkout_stationviewll1);
            raditButton80 = (RadioButton) findViewById(R.id.isfc_checnkout_otherdatarsqreturnrb1);
            raditButton315 = (RadioButton) findViewById(R.id.isfc_checnkout_otherdatarsqreturnrb2);
            raditButton500 = (RadioButton) findViewById(R.id.isfc_checnkout_otherdatarsqreturnrb3);
            checkBox_dblotno = (CheckBox) findViewById(R.id.isfc_checnkout_dbreturncb1);
            textBox_dbqty = (EditText) findViewById(R.id.isfc_checnkout_dbqtyet1);
            button2 = (Button) findViewById(R.id.isfc_checnkout_magazinebt);
            checkBoxdb = (CheckBox) findViewById(R.id.isfc_checnkout_firstyieldcb1);
            dbbutton = (Button) findViewById(R.id.isfc_checnkout_firstbt1);
            checkBox_yieldcor = (CheckBox) findViewById(R.id.isfc_checnkout_yieldcontrollotcb1);
            checkBox1 = (CheckBox) findViewById(R.id.isfc_checnkout_qcspecnoticecb1);
            checkBox_folcheck = (CheckBox) findViewById(R.id.isfc_checnkout_folrollcheckcb1);
            checkBox_pod = (CheckBox) findViewById(R.id.isfc_checnkout_podtestcb1);
            radiobutton_all = (RadioButton) findViewById(R.id.isfc_checnkout_podtest100rb1);
            radiobutton_part = (RadioButton) findViewById(R.id.isfc_checnkout_podtestrollrb1);
            checkBox_paoliao = (CheckBox) findViewById(R.id.isfc_checnkout_losepcb1);
            textBox_paoliao = (EditText) findViewById(R.id.isfc_checnkout_losepet1);
            button_addpaoliao = (Button) findViewById(R.id.isfc_checnkout_loseuaddbt1);
            Lhatxt = (EditText) findViewById(R.id.isfc_checnkout_lhaeqet1);
            textBoxfqcop = (EditText) findViewById(R.id.isfc_checnkout_lhauseret1);
            OQCbackchcb = (CheckBox) findViewById(R.id.isfc_checnkout_qcbackcb1);
            OQCundoOPNOcb = (Spinner) findViewById(R.id.isfc_checnkout_qcbackopnosp1);
            textBox_textqty = (EditText) findViewById(R.id.isfc_checnkout_rollqtyet1);
            textBox_qreturnsum = (EditText) findViewById(R.id.isfc_checnkout_putqcountet1);
            endlotbt = (Button) findViewById(R.id.isfc_checnkout_endlotinputbt1);
            label6 = (TextView) findViewById(R.id.isfc_checnkout_nextopnotbl1);
            panel_podset = (RadioGroup) findViewById(R.id.isfc_checnkout_podtestrg1);
            panelsmtqc = (RadioGroup) findViewById(R.id.isfc_checnkout_qcreturntrg1);
            submitbutton = (Button) findViewById(R.id.isfc_checnkout_submitbt);
            Bundle b = this.getIntent().getExtras();
            String[] ss = b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            _lotno = ss[3];
            _dieqty = ss[4];
            SFCStaticdata.staticmember.checkinqty = _dieqty;
            _opno = ss[5];
            _sendproductno = ss[6];
            _processno = ss[7];
            _sendopnoflowid = ss[8];
            SFCStaticdata.staticmember.checkinqty = _dieqty;
            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Checkout(_lotno, _dieqty, _opno, _sendproductno, _processno, _sendopnoflowid);
                    Checkout_Load();
                    _loadingstatus = true;
                }
            }, "LoadTask");
            defectqtytextBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        if (!hasFocus) {
                            if (_loadingstatus)
                                ExecTask(new SFCTaskVoidInterface() {
                                    @Override
                                    public void taskvoid(Object valueo) throws Exception {
                                        defectqtytextBox_Leave();
                                    }
                                }, "defectqtytextBox_Leave Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "defectqtytextBox_Leave Task Error:" + ex.getMessage());
                    }
                }
            });
            OQCbackchcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    OQCbackchcb_CheckedChanged();
                                }
                            }, "OQCbackchcb_CheckedChanged");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "OQCbackchcb_CheckedChanged Task Error:" + ex.getMessage());
                    }
                }
            });
            OQCundoOPNOcb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    OQCundoOPNOcb_SelectedIndexChanged();
                                }
                            }, "OQCundoOPNOcb_SelectedIndexChanged");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "OQCundoOPNOcb_SelectedIndexChanged Task Error:" + ex.getMessage());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            checkBoxdb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox_db_CheckedChanged();
                                }
                            }, "checkBox_db_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox_db_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
            checkBox_yieldcor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox_yieldcor_CheckedChanged();
                                }
                            }, "checkBox_yieldcor_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox_yieldcor_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
            checkBox_pod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox_pod_CheckedChanged();
                                }
                            }, "checkBox_pod_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox_pod_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
            checkBox_paoliao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox_paoliao_CheckedChanged();
                                }
                            }, "checkBox_paoliao_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox_paoliao_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox1_CheckedChanged();
                                }
                            }, "checkBox1_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox1_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
            checkBox_folcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox_folcheck_CheckedChanged();
                                }
                            }, "checkBox_folcheck_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox_folcheck_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
            checkBox_dblotno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (_loadingstatus) {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    checkBox_dblotno_CheckedChanged();
                                }
                            }, "checkBox_dblotno_CheckedChanged Task");
                        }
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkout.this, "checkBox_dblotno_CheckedChanged Task Error" + ex.getMessage());
                    }
                }
            });
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    String nowprocessno;
    String refflag = "";
    String undoopno;
    Boolean OQCflagBoolean;
    Boolean podtestcheck = false;
    Boolean eoloqcflag = false;
    Boolean checkopnoBoolean = false;
    Boolean magazineflag = false;
    Boolean magazinelot = false;
    Boolean magazineflag_1 = false;
    DataTable wsdt = new DataTable();
    String oqcngfviflag = "";

    public void Checkout(String lotno, String dieqty, String opno, String sendproductno, String processno, String sendopnoflowid) throws Exception {
        Boolean DetailInforEnabled = isChecked(DetailInfor);
        DetailInforEnabled = SFCStaticdata.staticmember.deviceno.equals("VTQCONN") && opno.equals("0725");

        if ( SFCStaticdata.staticmember.deviceno.equals("VTQCONN") && opno.equals("0725"))
        {
           setEnabled(DetailInfor,true);
        }
        else
        {
            setEnabled(DetailInfor,false);
        }
        setText(devicenotextBox, SFCStaticdata.staticmember.deviceno);
        setText(newdevicenotextBox, SFCStaticdata.staticmember.newdeviceno);
        SFCStaticdata.staticmember.laserchecksnitem = false;
        SFCStaticdata.staticmember.IsQCBackStationSFConlineFlag = false;
        setText(lotnotextBox, lotno);
        setText(dietextBox, dieqty);
        setText(opnotextBox, opno);
        setText(opnametextBox, cwa.CallRS("getopname", opno));
        staticopnoflowid = Integer.parseInt(sendopnoflowid);
        staticproductno = sendproductno;

        String nextopnotextBoxText = cwa.CallRS("getnextopno", processno,Integer.toString(staticopnoflowid + 1));

        nowprocessno = processno;
        if (nextopnotextBoxText.trim().equals("INV")) {
            setText(nextopnotextBox, "INV");
            setText(nextopnametextBox, "INV");
        } else {
            setText(nextopnotextBox,nextopnotextBoxText);
            setText(nextopnametextBox, cwa.CallRS("getopname", nextopnotextBoxText));
        }

        setText(defectqtytextBox, "0");
        undoopno = null;
        String OQCflag = cwa.CallRS("getopatt2", opno);
        if (OQCflag == null || OQCflag.equals("")) {
            ShowMessage("站位特性尚未设定（att2）");
            return;
        }
        String att14 = cwa.CallRS("getopatt14", opno);
        if (att14.equals("1")) {
            setEnabled(Lhatxt, true);
        } else if (att14.equals("2")) {
            setEnabled(Lhatxt, true);
            setText(label6, "GA機台:");
        }
        magazineflag = getmagazineflag(opno);
        magazineflag_1 = getmagazinelotflag(lotno, opno);
        if (magazineflag && !magazineflag_1) {
            setEnabled(button2, true);
        }
        DataTable fqcdata = cwa.CallRDT("getFQCflag", opno);
        if (fqcdata == null) {
            ShowMessage("站位特性失敗");
            return;
        } else if (fqcdata.Rowscount() > 0) {
            SFCStaticdata.staticmember.fqcflag = true;
            setEnabled(textBoxfqcop, true);
        } else {
            SFCStaticdata.staticmember.fqcflag = false;
            setEnabled(textBoxfqcop, false);
        }
        setEnabled(panel_podset, false);
        DataTable undoopnodt = new DataTable();
        switch (OQCflag) {
            case "2":   //EOL   QC
                eoloqcflag = true;
                setEnabled(textBox_textqty, true);
                OQCflagBoolean = true;
                setEnabled(OQCbackchcb, true);
                setChecked(OQCbackchcb, false);
                setEnabled(textBox_textqty, true);
                undoopnodt = cwa.CallRDT("getOQCundoopno", opno, sendproductno);
                setAdapter(OQCundoOPNOcb,
                        BaseFuncation.setvalue(undoopnodt, "opno", "opname", checkout.this)
                );
                setEnabled(OQCundoOPNOcb, false);
                setEnabled(checkBoxdb, false);
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                String qcsum = getlotqclog(lotno, opno);   // lotcheckqty  抽测数目
                qcsum = String.valueOf(Integer.parseInt(qcsum) + 1);
                SFCStaticdata.staticmember.fvicheckbag = false;
                setText(textBox_qreturnsum, qcsum);
                break;
            case "6":    // FOL QC   +   SMT  QC 
                OQCflagBoolean = true;    //  eoloqcflag   OQCflagBoolean
                setEnabled(textBox_textqty, true);
                SFCStaticdata.staticmember.checksmtqcflag = checksmtqc(lotno);
                if (SFCStaticdata.staticmember.checksmtqcflag) {
                    setEnabled(panelsmtqc, true);
                } else {
                    setEnabled(panelsmtqc, false);
                }
                setEnabled(OQCbackchcb, true);
                setChecked(OQCbackchcb, false);
                setEnabled(textBox_textqty, true);
                undoopnodt = cwa.CallRDT("getOQCundoopno", opno, sendproductno);
                setAdapter(OQCundoOPNOcb,
                        BaseFuncation.setvalue(undoopnodt, "opno", "opname", checkout.this)
                );
                setEnabled(OQCundoOPNOcb, false);
                setChecked(checkBoxdb, false);
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                String qcsum1 = getlotqclog(lotno, opno);   // lotcheckqty  抽测数目
                qcsum = String.valueOf(Integer.parseInt(qcsum1) + 1);
                setText(textBox_qreturnsum, qcsum1);
                SFCStaticdata.staticmember.fvicheckbag = false;
                break;
            case "8":    // FOL POD
                podtestcheck = true;
                OQCflagBoolean = false;
                setEnabled(OQCbackchcb, false);
                setChecked(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                setEnabled(checkBoxdb, true);
                setChecked(checkBoxdb, true);
                setEnabled(dbbutton, true);
                setEnabled(checkBox_yieldcor, false);
                SFCStaticdata.staticmember.fvicheckbag = false;
                setEnabled(checkBox_pod, true);
                setEnabled(panel_podset, true);
                break;
            case "4":    //   FOL FVI    db 站位记录  /  良率管控
                String loclotno = cwa.CallRS("getprocessatt2", processno);
                if (loclotno.isEmpty()) {
                    ShowMessage("流程特性位置尚未设定-t_processdata");
                    return;
                }
                setEnabled(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                OQCflagBoolean = false;
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                SFCStaticdata.staticmember.fvicheckbag = false;
                if (Integer.parseInt(loclotno) == 3) {
                    setEnabled(checkBoxdb, true);
                    setChecked(checkBoxdb, true);
                    setEnabled(dbbutton, true);
                    setEnabled(checkBox_yieldcor, true);
                } else {
                    setChecked(checkBoxdb, false);
                    setEnabled(checkBox_yieldcor, false);
                }
                break;
            case "9":    // pack  站位出站 扫描 bagid
                setEnabled(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                OQCflagBoolean = false;
                setEnabled(checkBoxdb, true);
                setChecked(checkBoxdb, true);
                setEnabled(dbbutton, true);
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                SFCStaticdata.staticmember.fvicheckbag = true;
                break;
            case "10":    // laser  站位 卡 扫描 SN 颗数   +  添加< laser站位检测 首站时间 8小时 >
                setEnabled(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                OQCflagBoolean = false;
                DataTable dt_deviceno = cwa.CallRDT("checkout_main_1", SFCStaticdata.staticmember.newdeviceno);
                if (dt_deviceno.Rowscount() > 0) {
                    setEnabled(checkBoxdb, true);
                    setChecked(checkBoxdb, true);
                    setEnabled(dbbutton, true);
                } else {
                    setChecked(checkBoxdb, false);
                }
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                SFCStaticdata.staticmember.laserchecksnitem = true;
                break;
            case "12":    // AutoFocus 添加 拋料動作。
                setEnabled(checkBox_paoliao, true);
                setEnabled(textBox_paoliao, false);
                setEnabled(textBox_paoliao, false);
                setEnabled(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                OQCflagBoolean = false;
                setEnabled(checkBoxdb, true);
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                SFCStaticdata.staticmember.fvicheckbag = false;
                break;
            case "14":  // 增加ACF检测从laser出站时间40个小时。
                setEnabled(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                OQCflagBoolean = false;
                setEnabled(checkBoxdb, true);
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                SFCStaticdata.staticmember.lotacfchecklasertime = true;
                break;
            default:
                setEnabled(OQCbackchcb, false);
                setEnabled(OQCundoOPNOcb, false);
                OQCflagBoolean = false;
                setEnabled(checkBoxdb, true);
                setEnabled(checkBox_yieldcor, false);
                setEnabled(checkBox_pod, false);
                setEnabled(panel_podset, false);
                SFCStaticdata.staticmember.fvicheckbag = false;
                break;
        }
        String reworkfviflag = cwa.CallRS("getreworfvikdata", lotno);
        if (reworkfviflag.equals("929")) {
            refflag = "1";
            setEnabled(textBox_textqty, false);
        }
        if (SFCStaticdata.staticmember.lotfolbingpicheck) {
            if (SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) {
                checkopnoBoolean = returnFolCheckOpno(opno);
                if (checkopnoBoolean) {
                    setEnabled(checkBox_folcheck, true);
                }
            } else {

                DataTable dtfolcheckDouble = cwa.CallRDT("checkout_main_2", SFCStaticdata.staticmember.deviceno);
                if (dtfolcheckDouble == null) {
                    ShowMessage("检测该机种是否需要进行FOL抽检时发生异常，请联系MIS");
                    return;
                } else if (dtfolcheckDouble.Rowscount() > 0) {
                    checkopnoBoolean = returnFolCheckOpno(opno);
                    if (checkopnoBoolean) {
                        setEnabled(checkBox_folcheck, true);
                    }
                }
            }
        }
        if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotfolbingpicheck) {
            DataTable gsbnodt = cwa.CallRDT("checkout_main_3", opno);
            if (gsbnodt.Rowscount() > 0) {
                setEnabled(comboBoxgsbno, true);
                gsbnoBoolean = true;
                DataTable machinedt = cwa.CallRDT("checkout_main_4", "GSB%", "F");
                setAdapter(comboBoxgsbno,
                        BaseFuncation.setvalue(machinedt, null, "machineno", checkout.this)
                );
            } else {
                setEnabled(comboBoxgsbno, false);
                gsbnoBoolean = false;
            }
        }
        Boolean linechange = cwa.CallRB("getlinechangeopno", opno);
        if (linechange) {
            setclickable(comboBoxline, true);
        }
        SFCStaticdata.staticmember.OQCNGCheckinFVI = cwa.CallRB("qcfvicheckdefect", opno); // QC不良是否在外观再打掉
        SFCStaticdata.staticmember.FVICHECKOQCNG = cwa.CallRB("FVIcheckqcng", opno); // FVI打掉QC不良;
        if (SFCStaticdata.staticmember.FVICHECKOQCNG) {
            DataTable qcngdata = cwa.CallRDT("getqcngqty", lotno, opno);
            if (qcngdata == null) {
                ShowMessage("查询QC不良信息时发生错误");
                return;
            }
            if (qcngdata.Rowscount() > 0) {
                ShowMessage("QC有 " + String.valueOf(qcngdata.Rowscount()) + " 不良需在該站位打掉");
                setText(defectqtytextBox, String.valueOf(qcngdata.Rowscount()));
            }
        }
    }

    private Boolean checksmtqc(String lotno) throws Exception {
        String lotnofirst = lotno.substring(0, 1).trim().toUpperCase();
        return lotnofirst.equals("S");
    }

    private String getlotqclog(String lotno, String opno) throws Exception {
        String qcsum = "0";
        try {
            qcsum = cwa.CallRS("checkout_getlotqclog", lotno, opno);
        } catch (Exception ex) {
            ShowMessage("查询批号Q退次数时发生错误");
        }
        return qcsum;
    }

    public DataTable getwiplotnosum(String lotno) throws Exception {
        if (lotno.startsWith("AK") || lotno.startsWith("PK")) {
            lotno = lotno.substring(1, lotno.indexOf("-")).trim() + "%";
        } else {
            lotno = lotno.substring(0, lotno.indexOf("-")).trim() + "%";
        }
        return cwa.CallRDT("checkout_getwiplotnosum", lotno);
    }

    public DataTable getwiplotnosum1(String lotno) throws Exception {
        if (lotno.startsWith("AK") || lotno.startsWith("PK")) {
            lotno = lotno.substring(1, lotno.length() - 1).trim();
        } else {
            lotno = lotno.substring(0, lotno.length()).trim();
        }
        return cwa.CallRDT("checkout_getwiplotnosum1", lotno);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                ExecTask(new SFCTaskVoidInterface() {
                    @Override
                    public void taskvoid(Object valueo) throws Exception {
                        Close();
                    }
                }, "Check In Closeing Task");
            } catch (Exception ex) {
                BaseFuncation.showmessage(this, "Check In Closeing Task Error:" + ex.getMessage());
            }
        }
        return false;
    }
    
    public void submitbt(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    submitbutton_Click();
                }
            }, "CheckOut Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "CheckOut Submit Task Error:" + ex.getMessage());
        }
    }

    private void submitbutton_Click() throws Exception {
        Boolean groupBox6Enabled = getclickable(comboBoxline);
        String lotnotextBoxText = getText(lotnotextBox).toString().trim();
        String opnotextBoxText = getText(opnotextBox).toString().trim();
        String opnametextBoxText = getText(opnametextBox).toString().trim();
        FinalStaticCloass.SpinnerData sline=(FinalStaticCloass.SpinnerData)getSelectedItem(comboBoxline);
        String comboBoxlineText =sline!=null?sline.getText():"";
       // String comboBoxlineText = ((FinalStaticCloass.SpinnerData) getSelectedItem(comboBoxline)).getText().trim();
        Boolean DetailInforEnabled = getEnabled(DetailInfor);
        String devicenotextBoxText = getText(devicenotextBox).toString().trim();
        String dietextBoxText = getText(dietextBox).toString().trim();
        Boolean checkBox1Checked = isChecked(checkBox1);
        Boolean OQCbackchcbChecked = isChecked(OQCbackchcb);
        String defectqtytextBoxText = getText(defectqtytextBox).toString().trim();
        Boolean radioButton80Checked = isChecked(raditButton80);
        Boolean radioButton315Checked = isChecked(raditButton315);
        Boolean radioButton500Checked = isChecked(raditButton500);
        String textBox_textqtyText = getText(textBox_textqty).toString().trim();
        String textBoxfqcopText = getText(textBoxfqcop).toString().trim();
        String newdevicenotextBoxText = getText(newdevicenotextBox).toString().trim();
        String LhatxtText = getText(Lhatxt).toString().trim();
        Boolean checkBox_dbChecked = isChecked(checkBoxdb);
        FinalStaticCloass.SpinnerData sGSB=(FinalStaticCloass.SpinnerData)getSelectedItem(comboBoxgsbno);
        String comboBox_gsbnoText =sGSB!=null?sGSB.getText():"";
        //String comboBox_gsbnoText = ((FinalStaticCloass.SpinnerData) getSelectedItem(comboBoxgsbno)).getText().trim();
        String nextopnametextBoxText = getText(nextopnametextBox).toString().trim();
        Boolean checkBox_yieldcorChecked = isChecked(checkBox_yieldcor);
        Boolean checkBox_podChecked = isChecked(checkBox_pod);
        Boolean checkBox_dblotnoChecked = isChecked(checkBox_dblotno);
        String textBox_dbqtyText = getText(textBox_dbqty).toString().trim();
        Boolean radioButton_partChecked = isChecked(radiobutton_part);
        Boolean radioButton_allChecked = isChecked(radiobutton_all);
        String textBox_paoliaoText = getText(textBox_paoliao).toString().trim();
        Boolean checkBox_paoliaoChecked = isChecked(checkBox_paoliao);
        Boolean checkBox_dbEnabled = isChecked(checkBoxdb);
        String nextopnotextBoxText = getText(nextopnotextBox).toString().trim();

        //特殊機種重工不管控
        Boolean Rstatus= checkoutsubmit.reworknocheckdev(cwa, _newdeviceno);
        if (Rstatus && SFCStaticdata.staticmember.AllReworkFlag)
        {
            reworknocheck = true;
        }

        //出站彈框
        DataTable at = cwa.CallRDT("getmessagedata", opnotextBoxText,"2");
        if(at!= null && !"".equals(at)) {
            if(at.Rowscount() > 0) {
                String strmesage=at.Rows(0).get_CellValue(0).toString();
                if (BaseFuncation.DialogResult.OK == MessageBox(strmesage, "單擊繼續")) {

                } else {
                    return;
                }
            }
        }
        //出站檢測LENS所對應的TRAY是否超時，超時HOLD并發送郵件 7 77
        checkoutsubmit.checkLENSholdflag(this, cwa, _newdeviceno, _opno, _lotno, HDSerialNo);

        //檢測LENS與批號的數量是否對應 8 78
        checkoutsubmit.checklensandlotqty(this, cwa, _newdeviceno, _opno, _lotno);

        //stagingtime checkin to checkout
        //checkoutsubmit.checkintonotouttime(this,cwa,_deviceno,_newdeviceno,_odbname,_lotno,opnotextBoxText,opnametextBoxText,HDSerialNo);

        //出站时选AB线别
        if (groupBox6Enabled) {
            String linelot = cwa.CallRS("getlinelot", lotnotextBoxText);
            if (linelot.equals(lotnotextBoxText)) {
                SFCStaticdata.staticmember.lineflag = false;
                //todo new class open no  fol return
              /*  unholdlinelot un = new unholdlinelot(lotnotextBoxText);
                un.ShowDialog();*/
                return;
            } else {
                String linevalue = cwa.CallRS("getlinevalue", opnotextBoxText, lotnotextBoxText);
                String linevaluenow = comboBoxlineText;
                if (!linevaluenow.equals("A") && !linevaluenow.equals("B")) {
                    ShowMessage("请选择线别");
                    return;
                }
                if (linevalue.endsWith(linevaluenow)) {
                } else {
                    ShowMessage("进站时所选线别为 " + linevalue + ",出站时所选线别为 " + linevaluenow + ",已被HOLD,请找有权限的人员解锁");
                    Boolean insertlot = cwa.CallRB("insertunlinelot", lotnotextBoxText, SFCStaticdata.staticmember.userid);
                    if (!insertlot) {
                        ShowMessage("插入信息时失败");
                        return;
                    }
                    SFCStaticdata.staticmember.lineflag = false;
                    //todo new class open no fol return
                   /* unholdlinelot un = new unholdlinelot(lotnotextBoxText);
                    un.ShowDialog();*/
                    return;
                }
            }
        }

        // 前段李翩翩要求加入記錄GSB直球記錄
        DataTable dt_opuser = cwa.CallRDT("checkout_submit_1", opnotextBoxText, SFCStaticdata.staticmember.deviceno);
        if (dt_opuser.Rowscount() > 0) {
            CreatNewActivity(GSBOPUSERRecord.class, _deviceno, _newdeviceno, _odbname, lotnotextBoxText, opnotextBoxText, "2");
        }

        // 解綁彈夾
        if (magazineflag && !magazineflag_1) {
            magazinelot = cwa.CallRB("removebindingformagazine_1",lotnotextBoxText,opnotextBoxText);//checkoutsubmit.removebindingformagazine(this, cwa, opnotextBoxText,lotnotextBoxText);

            if (!magazinelot) {
                ShowMessage("彈夾尚未解綁");
                return;
            }
        }
        //進站到出站，超時發送郵件，不卡控過站
        checkoutsubmit.checkintocheckouttimemail(this,cwa,_newdeviceno,lotnotextBoxText,opnotextBoxText,opnametextBoxText);

//        setEnabled(submitbutton, false);

        //添加VTQ0725站位輸入詳細信息框提示頁面
        if (DetailInforEnabled) {
            SFCStaticdata.staticmember.Folinformationflag = false;
            CreatNewActivity(foldetailinformation.class, _deviceno, _newdeviceno, _odbname, lotnotextBoxText, SFCStaticdata.staticmember.userid, opnotextBoxText, dietextBoxText);
            if (!SFCStaticdata.staticmember.Folinformationflag) {
                ShowMessage("錄入信息失敗，請重新錄入即可");
                return;
            }
        }

        //VTQImage Test出站的前提條件是：測試數量在進站數量的99%~105%之間
        if (devicenotextBoxText.equals("VT-Q") && opnotextBoxText.equals("1036")) {
            String lotnotest = lotnotextBoxText;
            String LOTNO = lotnotest.substring(1, 13);
            String sq = cwa.CallRS("checkout_submit_2", lotnotest, "VTQCONN");
            String imagetestdata = sq;
            String vtqkotnocount = dietextBoxText;
            Double imagetestdatas = Double.parseDouble(imagetestdata);
            Double vtqkotnocounts = Double.parseDouble(vtqkotnocount);
            if (vtqkotnocounts * 0.95 > imagetestdatas || imagetestdatas > vtqkotnocounts * 1.05) {
                if (BaseFuncation.DialogResult.OK == MessageBox("該批號在ImageTest站位測試數量不在進站數量的95%~105%之間需重新測試，是否強行出站？", "單擊繼續")) {
                    SFCStaticdata.staticmember.podtestsumcheck = false;
                    CreatNewActivity(VtqImageTestCheckout.class, _deviceno, _newdeviceno, _odbname);
                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                        ShowMessage("验证强行出站失败！");
                        return;
                    }
                } else {
                    return;
                }
            }
            //FOL卡複測的比例
            DataTable podredt = cwa.CallRDT("getpodngdata", LOTNO);
            if (podredt != null && podredt.Rowscount() > 0) {
                String firstqty = podredt.Rows(0).get_CellValue("qty");
                DataTable podrengdt = cwa.CallRDT("getpodsecondngdata", LOTNO);
                if (podrengdt != null && podrengdt.Rowscount() > 0) {
                    String secondqty = podrengdt.Rows(0).get_CellValue("qty").trim();
                    try {
                        if (Double.parseDouble(secondqty) / Double.parseDouble(firstqty) <= 0.9) {
                            //20160409  這個複測要切換 ljj
                            podrengdt = cwa.CallRDT("getpodsecondngdata2", LOTNO);
                            secondqty = podrengdt.Rows(0).get_CellValue("qty").trim();
                            if (secondqty.equals("")) secondqty = "0";
                            if (Double.parseDouble(secondqty) / Double.parseDouble(firstqty) <= 0.9) {
                                if (BaseFuncation.DialogResult.OK == MessageBox("該批號在ImageTest站位複測試比率小於90%，是否強行出站？", "單擊繼續")) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(VtqImageTestCheckout.class, _deviceno, _newdeviceno, _odbname);
                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        ShowMessage("验证强行出站失败！");
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ShowMessage("裝換POD複測數量時發生錯誤，請聯繫MIS部門");
                        return;
                    }
                }
            }
        }


        //HC0806label收集不够，不让出站
        if (devicenotextBoxText.equals("HC0806") && opnotextBoxText.equals("0380")) {
            if (SFCStaticdata.staticmember.AllReworkFlag) {
                if (checkLabelReworkQty(lotnotextBoxText)) {
                } else {
                    ShowMessage("请先进行重工label收集");
                    return;
                }
            } else {
                if (checkLabelQty(lotnotextBoxText)) {
                } else {
                    ShowMessage("请先进行label确认");
                    return;
                }
            }
        }

        //王丽增加输入作业员批号20160531
        if (SFCStaticdata.staticmember.lotsmtcheckflag) {
            String opnum = "0";
            DataTable dtcount = cwa.CallRDT("checkout_submit_3", opnotextBoxText, SFCStaticdata.staticmember.deviceno);
            if (dtcount.Rowscount() == 1) {
                opnum = dtcount.Rows(0).get_CellValue("att2");
            }
            if (!opnum.equals("0") && !opnum.equals("")) {
                //todo creea new calss open no flol return
//                frm_spec ss = new frm_spec(opnotextBoxText, SFCStaticdata.staticmember.deviceno, lotnotextBoxText, opnum);
//                ss.ShowDialog();
            }
            if (!SFCStaticdata.staticmember.useridsave) {
                this.Close();
            }
        }

        //檢測FOL測試數量 wzh
        Boolean foltestqtyflag=checkoutsubmit.checkfoltestnum(this, cwa, _deviceno,_newdeviceno,_odbname, lotnotextBoxText, opnotextBoxText, dietextBoxText);
        if (!foltestqtyflag)
        {
            return;
        }


        if (checkBox1Checked && !SFCStaticdata.staticmember.lotqcspecialinfo) {
            ShowMessage("QC特殊记录失败，请重新勾选“QC特殊标注”记录");
            return;
        }
        Boolean checksmtqc80flag = false;
        if ((OQCflagBoolean && OQCbackchcbChecked)) {
            if (defectqtytextBoxText.equals("") || defectqtytextBoxText.equals("0")) {
                ShowMessage("OQC判退必須掃描不良產品，否則無法判退！！！");
                setFocusable(defectqtytextBox, true);
                return;
            }
        } else {
            if (SFCStaticdata.staticmember.checksmtqcflag) {
                if (SFCStaticdata.staticmember.checksmtqcflag && radioButton315Checked && (Integer.parseInt(defectqtytextBoxText.trim()) >= 2)) {
                    ShowMessage("SMT QC 315/1 大于2颗必须Q退 ! ! !");
                    return;
                }
                if (SFCStaticdata.staticmember.checksmtqcflag && radioButton80Checked && (Integer.parseInt(defectqtytextBoxText.trim()) >= 1)) {
                    ShowMessage("SMT QC 80/0 大于1颗必须Q退 ! ! !");
                    return;
                }
                if (SFCStaticdata.staticmember.checksmtqcflag && radioButton500Checked && (Integer.parseInt(defectqtytextBoxText.trim()) >= 3)) {
                    ShowMessage("SMT QC 500/3 大于3颗必须Q退 ! ! !");
                    return;
                }
            }
        }
        String lotdefeqtynow = defectqtytextBoxText;
        if (lotdefeqtynow == null || lotdefeqtynow.equals("")) {
        } else {
            int defectlotint = 0;
            try {
                defectlotint = Integer.parseInt(lotdefeqtynow);
            } catch (Exception ex) {
                ShowMessage("不良數目輸入異常，請重新輸入！");
                return;
            }
            if (!OQCbackchcbChecked) {
                if (OQCflagBoolean && defectlotint > 0) {
                    if (!SFCStaticdata.staticmember.checksmtqcflag) {
                        if (!(SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) && !(SFCStaticdata.staticmember.OQCNGCheckinFVI && SFCStaticdata.staticmember.lotcheckeolflag)) {
                            ShowMessage("OQC 站位有不良輸入必須退站");
                            return;
                        } else {
                            if (BaseFuncation.DialogResult.OK == MessageBox("是否正常打不良，不Q退？", "")) {
                                if (SFCStaticdata.staticmember.OQCNGCheckinFVI) {
                                    oqcngfviflag = "FVICHECK";
                                }
                            } else {
                                ShowMessage("OQC 站位有不良輸入必須退站");
                                return;
                            }
                        }
                    }
                }
            }
        }
        String lotcheckqty = textBox_textqtyText;  //  抽测数目 textBox_textqty   // defectqtytextBox
        int aabbqty = 0;
        try {
            aabbqty = Integer.parseInt(lotcheckqty);
        } catch (Exception ex) {
            setText(textBox_textqty, "0");
            return;
        }
        if (OQCflagBoolean && (aabbqty <= 0)) {
            String newqcflag = cwa.CallRS("checkout_submit_4", opnotextBoxText);
            if (!refflag.equals("1")) {
            } else if (Integer.parseInt(dietextBoxText) == 0) {
            } else if (newqcflag.equals("3")) {
            } else {
                ShowMessage("QC站位抽測不可為0！");
                return;
            }
        }
        String tempsql = null;
        Boolean checksqlstatus = false;
        String lotno = lotnotextBoxText.toUpperCase();
        String lotnooldnndd = lotno;
        String productno = staticproductno;
        String opno = opnotextBoxText;
        String opnoflowid = String.valueOf(staticopnoflowid);
        String nextopno = getText(nextopnotextBox).toString().trim();
        String nextopnoflowid = String.valueOf((staticopnoflowid + 1));
        String dieqty = dietextBoxText;
        String lotstate = "1";
        String userid = SFCStaticdata.staticmember.userid;
        String att1 = null;
        String att2 = null;
        String att3 = null;
        String lotserial = lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
        String checkoutip = SFCStaticdata.staticmember.ip;
        String checkoutpcname = SFCStaticdata.staticmember.HDSerialNo;
        String checkindate, checkinuser;
        String wono = "";
        String ip =SFCStaticdata.staticmember.HDSerialNo; //SFCStaticdata.staticmember.ip;
        String transfererp = cwa.CallRS("getopatt2", opno);

        //记录前段检验人员名字   20150106 by CYF
        if (SFCStaticdata.staticmember.fqcflag && !opnametextBoxText.equals("Wafer IQC")) {
            if (OQCbackchcbChecked) {
                String fqcname = textBoxfqcopText;
                if (fqcname.equals("") || fqcname == null) {
                    ShowMessage("本站位为前段QC站位需要填写检验员");
                    setFocusable(textBoxfqcop, true);
                    return;
                }
            } else {
                if (SFCStaticdata.staticmember.fqcflag) {
                    String fqcname = textBoxfqcopText;
                    if (fqcname.equals("") || fqcname == null) {
                        ShowMessage("本站位为前段QC站位需要填写检验员");
                        setFocusable(textBoxfqcop, true);
                        return;
                    }
                    Boolean insertflag = cwa.CallRB("checkout_submit_5", lotno, opno, fqcname, userid, ip);
                }
            }
        }

        //VTQ 卡控Tray盤在當站位是否結批  CYF
        if (SFCStaticdata.staticmember.deviceno.equals("VT-Q")) {
            DataTable dttrayno = cwa.CallRDT("checkout_submit_6", opno);
            if (dttrayno == null) {
                ShowMessage("獲取站位信息失敗");
                return;
            } else if (dttrayno.Rowscount() > 0) {
                DataTable dttraynodata = cwa.CallRDT("checkout_submit_7", lotno.substring(1, 12), opno);
                if (dttraynodata == null) {
                    ShowMessage("獲取Tray盤結批數據失敗");
                    return;
                } else if (dttraynodata.Rowscount() <= 0) {
                    ShowMessage("該批號在" + opno + "站位TRAYNO尚未結批，不可出站");
                    return;
                }
            }
        }
        if(!SFCStaticdata.staticmember.AllReworkFlag)// (!(lotnotextBoxText.contains("C0"))) {
        {   //新機種卡是否解析數據 重工不需要解析數據
            if (SFCStaticdata.staticmember.ip.equals("10.155.23.18")) {
            } else {
                if (true) {
                    DataTable dtanalyzeopno = cwa.CallRDT("checkout_submit_8", newdevicenotextBoxText, opnotextBoxText);
                    if (dtanalyzeopno == null) {
                        ShowMessage("獲取解析站位異常");
                        return;
                    } else if (dtanalyzeopno.Rowscount() > 0) {
                        String lotnoA = "";
                        if (lotno.startsWith("AK")) {
                            lotnoA = lotno.substring(1, 13);
                        } else {
                            lotnoA = lotno;
                        }
                        DataTable dt_anazylerecord = cwa.CallRDT("checkout_submit_9", lotnoA);
                        DataTable dt_analyzeitem = cwa.CallRDT("checkout_submit_10");
                        List<String> list_anazylerecord = new ArrayList<String>();
                        List<String> list_analyzeitem = new ArrayList<String>();
                        if (dt_anazylerecord.Rowscount() > 0) {
                            for (int i = 0; i < dt_anazylerecord.Rowscount(); i++) {
                                list_anazylerecord.add(dt_anazylerecord.Rows(i).get_CellValue("avex").trim());
                            }
                        }
                        if (dt_analyzeitem.Rowscount() > 0) {
                            for (int i = 0; i < dt_analyzeitem.Rowscount(); i++) {
                                list_analyzeitem.add(dt_analyzeitem.Rows(i).get_CellValue("att5").trim());
                            }
                        }
                        String futre_item = "";//未解析的項目
                        for (int a = 0; a < list_analyzeitem.size(); a++) {
                            if (list_anazylerecord.contains(list_analyzeitem.get(a).trim())) {
                            } else {
                                futre_item += list_analyzeitem.get(a).trim() + ";";
                            }
                        }
                        if (futre_item.equals("") || futre_item == null) {
                        } else {
                            ShowMessage("請解析未解析的項目" + futre_item);
                            return;
                        }
                    }
                }
            }
        }

        //EOL J94,J95過站綁定LHA機台號
        String att14 = cwa.CallRS("getopatt14", opno);
        if (att14.equals("1")) {
            String lhanostr = LhatxtText;
            if (lhanostr == null || lhanostr.equals("")) {
                ShowMessage("該站位請輸入LHA機台號後再進行過站");
                return;
            }
        } else if (att14.equals("2")) {
            String lhanostr = LhatxtText;
            if (lhanostr == null || lhanostr.equals("")) {
                ShowMessage("該站位請輸入GA機台號後再進行過站");
                return;
            }
        }

        //Pack出站检测扫入包号的总数量与批号良品数量是否相等 --RI/RI+
        if (transfererp.equals("9") && (SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag)) {
            DataTable dt_canclecheck = cwa.CallRDT("checkout_submit_11", lotno);
            if (dt_canclecheck.Rowscount() < 1) {
                Boolean bldel = cwa.CallRB("checkout_submit_12", lotno);
                if (!bldel) {
                    ShowMessage("请出该批号Pack站位出站扫描包号数据异常，请联系MIS");
                    return;
                }
                String dieqty_now = String.valueOf(
                        Integer.parseInt(dieqty) -
                                Integer.parseInt(defectqtytextBoxText.trim()));
                //todo create new class open no fol return
//                PackCheckOutBagidScan pc = new PackCheckOutBagidScan(lotno, dieqty_now, opno);
//                pc.ShowDialog();
                if (SFCStaticdata.staticmember.packcheckoutbagidscanqty != Integer.parseInt(dieqty_now)) {
                    ShowMessage(lotno + "在Pack站位出站数量为" + dieqty_now + ",而Pack包号出站扫描包号确认的数量为" + String.valueOf(SFCStaticdata.staticmember.packcheckoutbagidscanqty) + ",两者不相符，请重新进行作业。");
                    return;
                }
            }
        }

        //新增對FOL抽檢站位抽檢數據檢測
        if (checkopnoBoolean) {
            DataTable dtfolcheck = cwa.CallRDT("checkout_submit_13", lotno, opno);
            if (dtfolcheck == null) {
                ShowMessage("獲取批號FOL當站位抽檢數據異常");
                return;
            } else if (dtfolcheck.Rowscount() <= 0) {
                ShowMessage(lotno + "在" + opno + "(" + cwa.CallRS("getopname", opno) + ")站位尚未完成抽檢，請抽檢之後再出站");
                return;
            } else {
                Boolean folupdateBoolean = cwa.CallRB("checkout_submit_14", lotno, opno);
                if (!folupdateBoolean) {
                    ShowMessage("执行FOL抽检数据状态更新异常，请联系MIS");
                    return;
                }
            }
        }

        //FOL GaOven站位卡彈匣號進出站55分鐘內
        if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotfolbingpicheck &&
                cwa.CallRB("gaOvenOpno", opnotextBoxText)) {
            //todo create new class open no fol return
//            FOLFunctionAddPage.FOLInOutCheckGAOven focg = new SFConline.FOLFunctionAddPage.FOLInOutCheckGAOven(lotnotextBoxText, opnametextBoxText.trim(), dietextBoxText.trim(), "2");
//            focg.ShowDialog();
            CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname, lotnooldnndd, opno,dieqty,"2");
            if (!SFCStaticdata.staticmember.FolGaOvenStationNoInOutTimeIsOK) {
                ShowMessage("FOL GaOven站位彈匣號出站信息不完整");
                return;
            }
        }

        //新增对一次不良进行确认
        if (transfererp.equals("4") || transfererp.equals("8") || transfererp.equals("16")) {
            if (!checkBox_dbChecked) {
                ShowMessage("SFC LHA OVEN/POD Test/FVI必須輸入<一次良率>數據");
                return;
            } else {
                if (!SFCStaticdata.staticmember.lotyicinginfo) {
                    ShowMessage("SFC LHA OVEN/POD Test/FVI必須輸入<一次良率>數據失敗！");
                    return;
                }
            }
        }

        //GSB記錄機台號
        if (gsbnoBoolean) {
            String gsbno = comboBox_gsbnoText;
            if (gsbno.equals("") || gsbno == null) {
                ShowMessage("請選擇GSB機台號");
                return;
            }
            String gsblotno = lotno;
            if (gsblotno.startsWith("K")) {
                gsblotno = "P" + gsblotno;
            }
            DataTable gsbselectdt = cwa.CallRDT("checkout_submit_15", gsblotno);
            if (gsbselectdt.Rowscount() > 0) {
                Boolean gsbdelBoolean = cwa.CallRB("checkout_submit_16", gsblotno);
                if (!gsbdelBoolean) {
                    ShowMessage("刪除GSB機台號已有記錄是發生異常，請聯繫MIS");
                    return;
                }
            }
            Boolean insertgsbBoolean = cwa.CallRB("checkout_submit_17", gsblotno, opno, gsbno, SFCStaticdata.staticmember.userid);
            if (!insertgsbBoolean) {
                ShowMessage("記錄GSB機台號異常，請聯繫MIS");
                return;
            }
        }

        //POD测试资料检测记录检测 暂时屏蔽
        if (SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) {
            if (transfererp.equals("8")) {
                String podchecklot = lotno;
                if (podchecklot.startsWith("K")) {
                    podchecklot = "P" + podchecklot;
                }
                DataTable podcheckdt = cwa.CallRDT("checkout_submit_18", podchecklot);
                if (podcheckdt.Rowscount() <= 0) {
                    ShowMessage("该批号尚未在POD测试站位进行测试资料检测，请重新检测之后之后再过站(tbllotpodtestdatacheckdata)");
                    return;
                }
            }
        }
        //FOL lot + 上一站位为分并批站位  // FOL 分批并批檢測
        checkoutsubmit.checkout_submit11(this,cwa,transfererp,SFCStaticdata.staticmember.lotfolbingpicheck,lotno,devicenotextBoxText,lotnooldnndd);

        if (nextopnametextBoxText.equals("INV")) {
            lotstate = "4";
        }

        //OQC 抽测时数量记录
        if (OQCflagBoolean && !reworknocheck) {
            if (!refflag.equals("1")) {
                if (lotcheckqty.equals("") || lotcheckqty == null) {
                    if (dietextBoxText.equals("0"))  // 当站进入数量
                    {
                        SFCStaticdata.staticmember.errorqtydata = null;
                    } else {
                        ShowMessage("抽测数目不可为空");
                        setFocusable(textBox_textqty, true);
                        return;
                    }
                } else {
                    if (dietextBoxText.equals("0")) {
                        if (!(textBox_textqtyText.equals("0"))) {
                            ShowMessage("当前站位数目已经为0，不可抽测！");
                            setText(textBox_textqty, "");
                            setFocusable(textBox_textqty, true);
                            return;
                        }
                    } else {
                        Boolean insertlotflag = cwa.CallRB("checkout_submit_22", lotnooldnndd, lotserial, opno, SFCStaticdata.staticmember.userid, ip, lotcheckqty);
                        if (!insertlotflag) {
                            ShowMessage("记录QC抽测数量时发生错误！");
                            return;
                        }
                    }
                }
            }
        }

        //RI與RI+各機種抽檢檢測  顧本華 2014/08/29
        String nocheckchoustr = cwa.CallRS("getnochoujiandata", lotno);
        if (SFCStaticdata.staticmember.ip.equals("10.156.126.84") || (nocheckchoustr != null && !nocheckchoustr.equals("")) || SFCStaticdata.staticmember.ip.equals("10.156.126.75") || SFCStaticdata.staticmember.ip.equals("10.155.23.184")) {
            SFCStaticdata.staticmember.lotnouncheckBoolean = true;
        }

        if ((SFCStaticdata.staticmember.engRilotcheckflag || SFCStaticdata.staticmember.englotcheckflag) && !SFCStaticdata.staticmember.lotnouncheckBoolean)
        {
            DataTable dtdevicenostation = returnCheckOpnoData(opnotextBoxText);
            if (dtdevicenostation == null) {
                ShowMessage("查詢當前站位是否屬於需要執行檢測抽檢數據動作的站位時發生異常，請聯繫MIS");
            } else if (dtdevicenostation.Rowscount() > 0) {
                String tablenamenow = dtdevicenostation.Rows(0).get_CellValue("tablename").trim();
                String opnamenow = dtdevicenostation.Rows(0).get_CellValue("opname").trim();
                String checkqtynow = dtdevicenostation.Rows(0).get_CellValue("checkqty").trim();
                String lotnowono = lotnooldnndd;
                if (lotnooldnndd.length() % 2 == 1) {
                    lotnowono = lotnooldnndd.substring(1, lotnooldnndd.length() - 1);
                }
                String lotacfqtydb = cwa.CallRS("checkout_submit_23", tablenamenow, lotnowono);
                if (lotacfqtydb.equals("") || lotacfqtydb == null || lotacfqtydb.equals("0")) {
                    ShowMessage("此批號尚未完成" + opnamenow + "站位抽檢記錄");
                    return;
                }
                String lotnoqty = cwa.CallRS("checkout_submit_24", lotno);
                if (Integer.parseInt(lotnoqty) >= Integer.parseInt(checkqtynow)) {
                    if (Integer.parseInt(checkqtynow) != Integer.parseInt(lotacfqtydb)) {
                        if (BaseFuncation.DialogResult.OK == MessageBox("此批號尚未完成" + opnamenow + "站位主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", "系統提示")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                    "此批號尚未完成主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "1", "31","","1");
                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                ShowMessage("验证强行出站失败！");
                                return;
                            }
                            String lotnoPro = lotno;
                            if (lotnoPro.startsWith("K")) {
                                lotnoPro = "A" + lotnoPro;
                            }
                            Boolean blPro = cwa.CallRB("checkout_submit_25", lotnoPro);
                            if (blPro) {
                                ShowMessage("更改狀態失敗！");
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    if (Integer.parseInt(lotnoqty) != Integer.parseInt(lotacfqtydb)) {
                        if (BaseFuncation.DialogResult.OK == MessageBox("此批號尚未完成" + opnamenow + "站位主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", "系統提示")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                    "此批號尚未完成主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "1", "31","","1");

                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                ShowMessage("验证强行出站失败！");
                                return;
                            }
                            String lotnoPro = lotno;
                            if (lotnoPro.startsWith("K")) {
                                lotnoPro = "A" + lotnoPro;
                            }
                            Boolean blPro = cwa.CallRB("checkout_submit_25", lotnoPro);
                            if (blPro) {
                                ShowMessage("更改狀態失敗！");
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        } else {
            if (!SFCStaticdata.staticmember.lotnouncheckBoolean) {
                DataTable dtchinamarket = cwa.CallRDT("checkout_submit_26", SFCStaticdata.staticmember.deviceno);
                if (dtchinamarket == null) {
                    ShowMessage("查询当前机种" + SFCStaticdata.staticmember.deviceno + "是否需要检测抽检数据时异常，请联系MIS");
                    return;
                } else if (dtchinamarket.Rowscount() > 0) {
                    DataTable dtdevicenostation = returnCheckOpnoData(opnotextBoxText);
                    if (dtdevicenostation == null) {
                        ShowMessage("查詢當前站位是否屬於需要執行檢測抽檢數據動作的站位時發生異常，請聯繫MIS");
                    } else if (dtdevicenostation.Rowscount() > 0) {
                        String tablenamenow = dtdevicenostation.Rows(0).get_CellValue("tablename").trim();
                        String opnamenow = dtdevicenostation.Rows(0).get_CellValue("opname").trim();
                        String checkqtynow = dtdevicenostation.Rows(0).get_CellValue("checkqty").trim();
                        String lotnowono = lotnooldnndd;
                        if (lotnooldnndd.length() % 2 == 1) {
                            lotnowono = lotnooldnndd.substring(1, lotnooldnndd.length() - 1);
                        }
                        String lotacfqtydb = cwa.CallRS("checkout_submit_23", tablenamenow, lotnowono);
                        if (lotacfqtydb.equals("") || lotacfqtydb == null || lotacfqtydb.equals("0")) {
                            ShowMessage("此批號尚未完成" + opnamenow + "站位抽檢記錄");
                            return;
                        }
                        String lotnoqty = cwa.CallRS("checkout_submit_24", lotno);
                        if (Integer.parseInt(lotnoqty) >= Integer.parseInt(checkqtynow)) {
                            if (Integer.parseInt(checkqtynow) != Integer.parseInt(lotacfqtydb)) {
                                if (BaseFuncation.DialogResult.OK == MessageBox("此批號尚未完成" + opnamenow + "站位主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", "系統提示")) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            "此批號尚未完成主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "1", "31","","1");

                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        ShowMessage("验证强行出站失败！");
                                        return;
                                    }
                                    String lotnoPro = lotno;
                                    if (lotnoPro.startsWith("K")) {
                                        lotnoPro = "A" + lotnoPro;
                                    }
                                    Boolean blPro = cwa.CallRB("checkout_submit_25", lotnoPro);
                                    if (blPro) {
                                        ShowMessage("更改狀態失敗！");
                                    }
                                } else {
                                    return;
                                }
                            }
                        } else {
                            if (Integer.parseInt(lotnoqty) != Integer.parseInt(lotacfqtydb)) {
                                if (BaseFuncation.DialogResult.OK == MessageBox("此批號尚未完成" + opnamenow + "站位主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", "系統提示")) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            "此批號尚未完成主壓抽檢記錄,是否需要强行过站？(" + checkqtynow + "!=" + lotacfqtydb + ")", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "1", "31","","1");
                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        ShowMessage("验证强行出站失败！");
                                        return;
                                    }
                                    String lotnoPro = lotno;
                                    if (lotnoPro.startsWith("K")) {
                                        lotnoPro = "A" + lotnoPro;
                                    }
                                    Boolean blPro = cwa.CallRB("checkout_submit_25", lotnoPro);
                                    if (blPro) {
                                        ShowMessage("更改狀態失敗！");
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        //fvicheckbag  qc check sn must be in bagid lotno
        if (SFCStaticdata.staticmember.fvicheckbag && !SFCStaticdata.staticmember.lotnouncheckBoolean)
        {
            checkoutsubmit.checkout_submit12(this,cwa,lotno, _deviceno,_newdeviceno,_odbname,lotnooldnndd,opno);
        }

        // 对于db批号可以选择性的标注// 对于db批号可以选择性的标注
        if (checkBox_dbChecked) {
            try {
                Boolean inslotlog = cwa.CallRB("checkout_submit_32", lotno + "bak", lotno, ip, opno);
                if (!inslotlog) {
                    try {
                        inslotlog = cwa.CallRB("checkout_submit_33", lotno);
                        if (inslotlog) {
                            ShowMessage("插入一次不良時發生錯誤，请重新过站");
                            return;
                        } else {
                            ShowMessage("插入一次不良時發生錯誤，请联系MIS-LotnoSpecialRecords");
                            return;
                        }
                    } catch (Exception ex) {
                        ShowMessage("插入一次不良時發生錯誤-LotnoSpecialRecords");
                        return;
                    }
                }
            } catch (Exception ex) {
                ShowMessage("插入一次不良時發生錯誤-LotnoSpecialRecords");
                return;
            }
        }

        //laserchecksnitem
        String lotlaserssddafasdf = "";
        if (SFCStaticdata.staticmember.laserchecksnitem) {
            if (!SFCStaticdata.staticmember.AllReworkFlag) {
                DataTable dtlotspe3 = cwa.CallRDT("getlotspecialtype3", lotno);   // laser t_special type='3' 免检。
                if (lotno.length() == 12 || lotno.length() == 15) {
                    lotlaserssddafasdf = lotno;
                } else if (lotno.length() == 13 || lotno.length() == 16) {
                    lotlaserssddafasdf = lotno.substring(1, 13);
                } else {
                    ShowMessage("批号位数不符逻辑");
                    return;
                }
                if (dtlotspe3.Rowscount() <= 0) {
                    String lotdieqty = dietextBoxText.trim();
                    String lotlasersum = cwa.CallRS("checkout_submit_34", lotlaserssddafasdf);
                    String lotlasersumqty = lotlasersum;
                    DataTable dt_newtable = cwa.CallRDT("checkout_submit_35", SFCStaticdata.staticmember.newdeviceno);
                    if (dt_newtable.Rowscount() > 0) {
                        lotlasersum = cwa.CallRS("checkout_submit_36", lotlaserssddafasdf);
                        lotdieqty = String.valueOf(Integer.parseInt(lotdieqty) - Integer.parseInt(defectqtytextBoxText));
                    }
                    if (lotdieqty.equals("") || lotdieqty == null || lotlasersum.equals("") || lotlasersum == null) {
                        ShowMessage("查询批号WIP数量或者laser确认数量时发生错误");
                        return;
                    } else {
                        String ribdieqty = "";
                        if (!(Integer.parseInt(lotlasersum) >= Integer.parseInt(lotdieqty))) {
                            ShowMessage("批号laser确认数目尚未达到WIP数量，不可出站 < 需要碼：" + lotdieqty + " 共要码：" + lotlasersumqty + ";已确认：" + lotlasersum + " > ！");
                            return;
                        }
                    }
                }
            } else {
                DataTable dtlotspe3 = cwa.CallRDT("getlotspecialtype3", lotno);
                if (lotno.length() == 12 || lotno.length() == 15) {
                    lotlaserssddafasdf = lotno;
                } else if (lotno.length() == 13 || lotno.length() == 16) {
                    lotlaserssddafasdf = lotno.substring(1, 13);
                } else {
                    ShowMessage("批号位数不符逻辑");
                    return;
                }
                if (dtlotspe3.Rowscount() <= 0) {
                    String lotdieqty = dietextBoxText.trim();
                    String lotlasersum = cwa.CallRS("checkout_submit_37", lotlaserssddafasdf);
                    String lotlasersumqty = cwa.CallRS("checkout_submit_37", lotlaserssddafasdf);
                    if (lotdieqty.equals("") || lotdieqty == null || lotlasersum.equals("") || lotlasersum == null) {
                        ShowMessage("查询批号WIP数量或者laser确认数量时发生错误");
                        return;
                    } else {
                        String ribdieqty = "";
                        if (Integer.parseInt(lotdieqty) != Integer.parseInt(lotlasersum)) {
                            ShowMessage("重工收集尚未达到WIP数量，不可出站 < 需要碼：" + lotdieqty + " 共要码：" + lotlasersumqty + ";已收集：" + lotlasersum + " > ！");
                            return;
                        }
                    }
                }
            }
            String devicenoflagbbb = SFCStaticdata.staticmember.odbname;
            if (devicenoflagbbb.equals("RIGCONN")) {
                String sqllaserfirsttime = cwa.CallRS("sqllaserfirsttime", lotno);
                if (sqllaserfirsttime.equals("") || sqllaserfirsttime == null) {
                    ShowMessage("查询批号至Laser已用时间时发生错误");
                    return;
                } else {
                    if (Double.parseDouble(sqllaserfirsttime) > 8) {
                        if (BaseFuncation.DialogResult.OK == MessageBox("至目前为止已超过 8 Hours，是否需要強行過站 ?<" + sqllaserfirsttime + ">", "單擊繼續")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                    "至目前为止已超过 8 Hours，是否需要強行過站 ?<" + sqllaserfirsttime + ">", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "100", "21","","1");

                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                ShowMessage("验证强行出站失败！<Laser 8Hours>");
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }

        //// ACF check add 0814 liang
        if (SFCStaticdata.staticmember.lotacfchecklasertime) {
            String sqllaserfirsttime = cwa.CallRS("checkout_submit_39", lotno);
            if (sqllaserfirsttime.equals("") || sqllaserfirsttime == null) {
                ShowMessage("查询批号至ACF已用时间时发生错误");
                return;
            } else {
                if (Double.parseDouble(sqllaserfirsttime) > 40) {
                    if (BaseFuncation.DialogResult.OK == MessageBox("Cube Incoming至目前为止已超过 40 Hours，是否需要強行過站 ?<" + sqllaserfirsttime + ">", "單擊繼續")) {
                        String testitemerror = "Cube Incoming至目前为止已超过 40 Hours，是否需要強行過站 ?<" + sqllaserfirsttime + ">";
                        String testonocart = "EOL ACF";
                        SFCStaticdata.staticmember.podtestsumcheck = false;
                        CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                testitemerror + "(" + sqllaserfirsttime + ")", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "1", "31","","1");
                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                            ShowMessage("验证强行出站失败！<Lser 40Hours>");
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        //特殊權限管控
        if (checkBox_yieldcorChecked) {
            if (!SFCStaticdata.staticmember.controlconfigflag) {
                ShowMessage("請輸入正確良率管控項目！");
                return;
            }
        }
        if (checkBox_podChecked) {
            if (!SFCStaticdata.staticmember.Podtestflag) {
                ShowMessage("請輸入正確的POD測試資料");
                return;
            }
        } else {
            if (SFCStaticdata.staticmember.Podtestflag) {
                Boolean sql_rebackflag = cwa.CallRB("checkout_submit_40", lotno);
                try {
                    if (!sql_rebackflag) {
                        ShowMessage("批號有冗餘記錄，請聯繫MIS-LOTNOSPECIALRECORDS");
                    }
                } catch (Exception ex) {
                    ShowMessage("批號有冗餘記錄，請聯繫MIS-LOTNOSPECIALRECORDS");
                }
            }
        }


        //POD 测试资料检查确认测试资料是否足够
        Boolean testpodcheckfalg = false;
        String devicenoflagaaa = SFCStaticdata.staticmember.odbname.substring(0, 4);
        if (podtestcheck && !SFCStaticdata.staticmember.deviceno.equals("VT-Q")) {
            if (lotno.length() == 13) {
                lotlaserssddafasdf = lotno.substring(1, 13);//LQ添加,一条龙
            } else if (lotno.length() == 12) {
                lotlaserssddafasdf = lotno;  //三段式
            }
            try {
                String qtypod1 = "";
                String qtypod2 = "";
                qtypod1 = cwa.CallRS("checkout_submit_41", lotlaserssddafasdf);
                qtypod2 = cwa.CallRS("checkout_submit_42", lotlaserssddafasdf);

                int aaa = Integer.parseInt(qtypod1);
                int bbb = Integer.parseInt(qtypod2);
                double testproportion =Double.parseDouble(cwa.CallRS("gettestproportion", SFCStaticdata.staticmember.newdeviceno));
                if (bbb == 0) {
                    ShowMessage("資料庫中還沒有該批號的不良品复測記錄");
                    if (aaa == 0) {
                        ShowMessage("该批号尚未进行POD复测");
                    }
                } else {
                    if (aaa == 0) {
                        if (BaseFuncation.DialogResult.OK == MessageBox("该批号尚未进行POD复测,是否需要强行过站？(" + aaa + "<" + bbb + ")", "系統提示")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                    "复測數目不到測試不良品總數的70%<" + aaa + "-" + bbb + "-0.7>", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "5", "20","","1");
                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                ShowMessage("验证强行出站失败！");
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
                if (aaa < bbb * testproportion/100){ //if (aaa < bbb * 0.7)
                    if (BaseFuncation.DialogResult.OK == MessageBox("复測數目不到測試不良品總數的70%,是否需要强行过站？(" + aaa + "<" + bbb * 0.7 + ")", "系統提示")) {
                        SFCStaticdata.staticmember.podtestsumcheck = false;
                        CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                "复測數目不到測試不良品總數的70%<" + aaa + "-" + bbb + "-0.7>", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "5", "20","","1");

                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                            ShowMessage("验证强行出站失败！");
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    //Boolean insdbflag = cwa.CallRB("errorinfobackdb", lotnooldnndd, opno, "POD正常測試<" + aaa + "-" + bbb + "-0.7>", userid, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.odbname, "30", "");
                    Boolean insdbflag = cwa.CallRB("errorinfobackdb",lotnooldnndd, opno, "POD正常測試<" + aaa + "-" + bbb + "-" + testproportion + ">", userid, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.odbname, "30", "");
                    if (!insdbflag) {
                        ShowMessage("記錄POD測試比例時發生錯誤！");
                        return;
                    }
                }
            } catch (Exception ex) {
                ShowMessage("查詢批號POD複測資料時發生錯誤");
                return;
            }
        }

        //DB標注
            if (checkBox_dblotnoChecked) {
                String dbqtynew = textBox_dbqtyText;
                if (dbqtynew.equals("") || dbqtynew == null) {
                    ShowMessage("请输入正确的DB数量！");
                    setFocusable(textBox_dbqty, true);
                    return;
                }
                try {
                    Boolean inserdblot = cwa.CallRB("checkout_submit_43", lotno, opno, ip, dbqtynew);
                    if (!inserdblot) {
                        ShowMessage("DB批號標注失敗，請聯繫MIS！");
                        return;
                    }
                } catch (Exception ex) {
                    ShowMessage("DB批號標注時發生錯誤，請聯繫MIS！");
                    return;
                }
            }
            // Cube incoming 站出站的時候更改 收料--認為已經發料
            if (SFCStaticdata.staticmember.updatelotcartflag) {
                if (lotnooldnndd.length() == 13) {
                    lotnooldnndd = lotnooldnndd.substring(1, 12).trim();
                }
                Boolean uplotstate = cwa.CallRB("updatelotkitinfo", lotnooldnndd);
                if (!uplotstate) {
                    ShowMessage("确认收料信息失败");
                    return;
                }
            }

            //加入卡進出站時間 1118 liang
            String attitem9 = cwa.CallRS("getopatt8", opno);
            if (!attitem9.equals("") && attitem9 != null && !SFCStaticdata.staticmember.lotnouncheckBoolean) {
                String lotdatecheck = cwa.CallRS("getchecknowdata", lotno);
                if (lotdatecheck.equals("") || lotdatecheck == null) {
                    ShowMessage("獲取批號進站與當前時間差失敗！");
                    return;
                } else {
                    if (Double.parseDouble(lotdatecheck) > Double.parseDouble(attitem9)) {
                        if (BaseFuncation.DialogResult.OK == MessageBox("出站時間已經超出當前最大設定時間,是否需要强行过站？(" + lotdatecheck + "<" + attitem9 + ")", "系統提示")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                    "出站時間已經超出當前最大設定時間<" + lotdatecheck + "<" + attitem9 + ">", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "5", "32","","1");
                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                ShowMessage("验证强行出站失败！");
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }

            //通过lotstate获取进站时间
            DataTable tempcheckindt = new DataTable();
            tempcheckindt = cwa.CallRDT("getcheckindata", lotno);
            if (tempcheckindt != null) {
                if (tempcheckindt.Rowscount() == 1) {
                    checkindate = tempcheckindt.Rows(0).get_CellValue("updatedate");
                    checkinuser = tempcheckindt.Rows(0).get_CellValue("userid");
                } else {
                    ShowMessage("無法獲取checkin資料﹐請聯系MIS部門");
                    return;
                }
            } else {
                ShowMessage("無法獲取checkin資料﹐請聯系MIS部門");
                return;
            }
            if (OQCbackchcbChecked) {
                if (undoopno == null || undoopno.equals("")) {
                    ShowMessage("請選擇需要判退的站位！！！");
                    return;
                }
            }

            //测试资料数量查询
            DataTable dttestcheck = new DataTable();
            dttestcheck = cwa.CallRDT("getopatttestcheck", opno);
            checkoutsubmit.checkout_submit1(
                    this, cwa, dttestcheck, lotnotextBoxText, dieqty, opnametextBoxText, _deviceno, _newdeviceno, _odbname, lotnooldnndd, opno, checkBox_podChecked, radioButton_partChecked
            );

            //check in out time check  加入卡進出站最大時間
            String opnoatt6 = cwa.CallRS("getopnoatt6", opno);
            checkoutsubmit.checkout_submit2(this, cwa, opnoatt6, lotnooldnndd, _deviceno, _newdeviceno, _odbname, opno, "5", "6", "24");

            //check in out time check  加入卡  進出站 最小時間
            String opnoatt9 = cwa.CallRS("getopatt9", opno);
            checkoutsubmit.checkout_submit2_min(this, cwa, opnoatt9, lotnooldnndd, _deviceno, _newdeviceno, _odbname, opno,"5","1","34");

            if (transfererp != null) {
                //                #region erp checkin
                if (transfererp.equals("5")) {
                    //erp checkin
                    String lotno_new = "";
                    if (lotno.length() == 13 || lotno.length() == 16) {
                        lotno_new = lotno.substring(1, lotno.length());
                    } else if (lotno.length() == 12 || lotno.length() == 15) {
                        lotno_new = lotno;
                    }
                    wono = cwa.CallRS("checkout_submit_46", lotno_new);
                    if (wono.equals("") || wono == null) {
                        ShowMessage("请检查批号是否为WIP批号");
                        return;
                    }
                    if (!cwa.CallRB("checkout_submit_47", lotno, wono, opno, productno, dieqty)) {
                        ShowMessage("拋轉ERP備份資料失敗﹐請聯系MIS部門");
                        return;
                    }
                    //todo：ERP资料操作 2017/03/22 留下观察
//                String deletesql = "delete from swv_file where swv03='" + wono + "' and swv06='" + lotno + "';";
//                String insertsql = "insert into swv_file values('','',today,'" + wono + "','" + productno + "'," + dieqty + ",'" + lotno + "','','','',today,'" + wono + "','','N');";
//                Infx_CDAL.Infx_CDAL infx = new Infx_CDAL.Infx_CDAL();
//                if (!infx.Execinsert("InfxConnection", deletesql))
//                {
//                    ShowMessage("清除ERP失敗﹐請聯系MIS部門");
//                    return;
//                }
//
//                if (!infx.Execinsert("InfxConnection", insertsql))
//                {
//                    ShowMessage("拋轉ERP失敗﹐請聯系MIS部門");
//                    return;
//                }
                }
                if (transfererp.equals("11")) {//针对FOL特殊站位分别进行 数量统计 25K
                    String sqlcheckdayout = "";
                    String systimeall = cwa.CallRS("getdbsystime");
                    String daynow = systimeall.substring(11, 2);
                    String systime = "";
                    if (Integer.parseInt(daynow) >= 8 && Integer.parseInt(daynow) <= 23) {
                        systime = cwa.CallRS("getdbsystime").substring(0, 10) + " 08:00:00";
                    } else {
                        systime = cwa.CallRS("getdbsystimezuotian").substring(0, 10) + " 08:00:00";
                    }
                    try {
                        String checkoutqty = cwa.CallRS("checkout_submit_48", opnoflowid, opno, systime, lotnooldnndd);
                        if (checkoutqty.equals("") || checkoutqty == null) {
                            ShowMessage("查询当前站位当日产出异常");
                            return;
                        } else {
                            if (Integer.parseInt(checkoutqty) + Integer.parseInt(dieqty) > 45000) {
                                ShowMessage("当前站位最大出站数量" + String.valueOf((45000 - Integer.parseInt(checkoutqty))) + " 45K");
                                return;
                            }
                        }
                    } catch (Exception ex) {
                        ShowMessage("查询该站位当日内产出数量时发生错误");
                        return;
                    }
                }
                if (transfererp.equals("16")) {
                    String devicenoflagBBB = SFCStaticdata.staticmember.odbname;
                    if (devicenoflagBBB.equals("N41ACONN") || devicenoflagBBB.equals("N41BCONN") ||
                            devicenoflagBBB.equals("N41CCONN") || devicenoflagBBB.equals("N41CONN")) {
                        String sqlcheckdayout = "";
                        String systimeall = cwa.CallRS("getdbsystime");
                        String daynow = systimeall.substring(11, 2);
                        String systime = "";
                        if (Integer.parseInt(daynow) >= 8 && Integer.parseInt(daynow) <= 23) {
                            systime = cwa.CallRS("getdbsystime").substring(0, 10) + " 08:00:00";
                        } else {
                            systime = cwa.CallRS("getdbsystimezuotian").substring(0, 10) + " 08:00:00";
                        }
                        try {
                            //String checkoutqty = new DAL.Ora_dal().OracleExecStringparmer(sqlcheckdayout, checkdayoutparmer, 3, SFCStaticdata.staticmember.odbname);
                            String checkoutqty = cwa.CallRS(opnoflowid, opno, systime, lotnooldnndd);
                            if (checkoutqty.equals("") || checkoutqty == null) {
                                ShowMessage("查询当前站位当日产出异常");
                                return;
                            } else {
                                if (Integer.parseInt(checkoutqty) + Integer.parseInt(dieqty) > 45000) {
                                    ShowMessage("当前站位最大出站数量" + String.valueOf((35000 - Integer.parseInt(checkoutqty))) + "45K");
                                    return;
                                }
                            }
                        } catch (Exception ex) {
                            ShowMessage("查询该站位当日内产出数量时发生错误");
                            return;
                        }
                    }
                }
            } else {
                if (transfererp == null || transfererp.equals("")) {
                    ShowMessage("站位特性尚未设定（att2）");
                    return;
                }
            }

            //defect insert into db check
            String[] parmerdeletedefect = {lotno, opno};
            String opnodefectno = null;
            String ngqty = null;
            String setupqty = "0";
            if (!OQCflagBoolean) {
                String paoliaoqty = textBox_paoliaoText;
                if (!defectqtytextBoxText.equals("0")) {
                    if (SFCStaticdata.staticmember.errorqtydata == null) {
                        if (textBox_paoliaoText.equals("0") || textBox_paoliaoText == null || textBox_paoliaoText.equals("")) {
                            ShowMessage("尚未輸入不良品資料");
                            return;
                        } else {
                            if (!checkBox_paoliaoChecked) {
                                ShowMessage("輸入正確的拋料數目請點擊“Add”確認");
                                return;
                            } else {
                                if (Integer.parseInt(textBox_paoliaoText) != Integer.parseInt(defectqtytextBoxText)) {
                                    ShowMessage("請輸入正確的拋料數目");
                                    return;
                                }
                            }
                        }
                    } else {
                        if (SFCStaticdata.staticmember.errorqtydata.size() == 0) {
                            if (textBox_paoliaoText.equals("0") || textBox_paoliaoText == null || textBox_paoliaoText.equals("")) {
                                removeAllViewsInLayout(errordataGridView);
                                ShowMessage("尚未輸入不良品資料,請重新輸入！");
                                return;
                            }
                        } else {
                            String opnodefectnoqty = "0";
                            for (int i = 0; i < SFCStaticdata.staticmember.errorqtydata.size(); i++) {
                                opnodefectnoqty = String.valueOf(SFCStaticdata.staticmember.errorqtydata.get(i).errorqty + Integer.parseInt(opnodefectnoqty));
                            }
                            if (checkBox_paoliaoChecked) {
                                if (Integer.parseInt(textBox_paoliaoText) + Integer.parseInt(opnodefectnoqty) != Integer.parseInt(defectqtytextBoxText)) {
                                    ShowMessage("請輸入正確的拋料數目");
                                    return;
                                }
                            } else {
                                if (Integer.parseInt(opnodefectnoqty) != Integer.parseInt(defectqtytextBoxText)) {
                                    ShowMessage("請輸入正確的拋料數目");
                                    return;
                                }
                            }
                        }
                        if (Integer.parseInt(transfererp) != 2) {
                            Boolean deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout插入ng前清空1823");
                            if (deldblot) {
                            } else {
                                ShowMessage("删除当前站位不良失败，请联系MIS");
                                return;
                            }
                        }
                        tempsql = "begin ";
                        for (int i = 0; i < SFCStaticdata.staticmember.errorqtydata.size(); i++) {
                            opnodefectno = SFCStaticdata.staticmember.errorqtydata.get(i).errorcode;
                            String defectcharacter = cwa.CallRS("geterrorcharacter", opnodefectno, opno);
                            if (Integer.parseInt(defectcharacter) == 1) {
                                setupqty = String.valueOf(Integer.parseInt(setupqty) + SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                            }
                            ngqty = String.valueOf(SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                            String lenstype = cwa.CallRS("checkout_submit_49", lotno);
                            if (lenstype != null && !lenstype.equals("")) {
                                String limits = cwa.CallRS("checkout_submit_50", lenstype, opno, SFCStaticdata.staticmember.errorqtydata.get(i).errorcode.trim());
                                if (limits != null && !limits.equals("")) {
                                    Double ngrate = SFCStaticdata.staticmember.errorqtydata.get(i).errorqty / Double.parseDouble(dieqty) * 100;
                                    if (ngrate > Double.parseDouble(limits)) {
                                        SFCStaticdata.staticmember.pename = null;
                                        CreatNewActivity(ENGConfirm.class, _deviceno, _newdeviceno, _odbname,
                                                SFCStaticdata.staticmember.errorqtydata.get(i).errorname, limits, ngrate.toString());
                                    }
                                }
                            }
                            String[] tempparmer = {};
                            if (i == SFCStaticdata.staticmember.errorqtydata.size() - 1) {
                                tempsql = tempsql + " insert into T_lotdefectdata values('" + lotserial + "','" + lotno + "','" + opno + "','" + opnodefectno + "','" + ngqty + "',sysdate,'" + SFCStaticdata.staticmember.pename + "','" + att2 + "','" + att3 + "');";
                                checksqlstatus = false;
                                try {
                                    String tempNgsnSql = tempsql;
                                    if (SFCStaticdata.staticmember.insertNgSnLotSql == null)//對於不需要掃描單顆的不良直接往下走
                                    {
                                    } else if (SFCStaticdata.staticmember.insertNgSnLotSql.trim() != null && SFCStaticdata.staticmember.insertNgSnLotSql.trim() != "" && SFCStaticdata.staticmember.insertNgSnLotSql.toString().trim().toUpperCase().contains("INSERT")) {
                                        tempNgsnSql = tempNgsnSql + SFCStaticdata.staticmember.insertNgSnLotSql;
                                    }
                                    tempNgsnSql = tempNgsnSql + "end;";
                                    checksqlstatus = cwa.CallRB("checkout_submit_51", tempNgsnSql);
                                    SFCStaticdata.staticmember.insertNgSnLotSql = "";
                                } catch (Exception ex) {
                                    ShowMessage(ex.toString());
                                }
                            } else {
                                tempsql = tempsql + " insert into T_lotdefectdata values('" + lotserial + "','" + lotno + "','" + opno + "','" + opnodefectno + "','" + ngqty + "',sysdate,'" + SFCStaticdata.staticmember.pename + "','" + att2 + "','" + att3 + "');";
                            }
                        }
                        if (!checksqlstatus) {
                            Boolean deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout儲存不良失敗");
                            if (deldblot) {
                                ShowMessage("儲存不良失敗");
                                return;
                            } else {
                                ShowMessage("checkout儲存不良失敗，恢复资料失败");
                                return;
                            }
                        }
                        DataTable defcqty = cwa.CallRDT("checkout_submit52", lotno);
                        if (defcqty.Rowscount() >= 3) {
                            if (BaseFuncation.DialogResult.OK == MessageBox("該批號在當站位打夠三顆holder missing不良，需驗證出站？", "單擊繼續")) {
                                SFCStaticdata.staticmember.podtestsumcheck = false;
                                CreatNewActivity(VtqImageTestCheckout.class, _deviceno, _newdeviceno, _odbname);
                                if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                    ShowMessage("验证强行出站失败！");
                                    return;
                                }
                            }
                        }
                    }
                    if (checkBox_paoliaoChecked) {
                        String opnopaoliao = opno + "-000";
                        SFCStaticdata.staticmember.lotpaoliaocheckeolflag = false;
                        CreatNewActivity(ENGCheckInfo.class, _deviceno, _newdeviceno, _odbname,
                                lotnooldnndd, textBox_paoliaoText);
                        if (!SFCStaticdata.staticmember.lotpaoliaocheckeolflag) {
                            Boolean deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout抛料信息确认失败");
                            if (deldblot) {
                                ShowMessage("抛料信息确认不完整，请重新确认");
                                return;
                            } else {
                                ShowMessage("checkout抛料信息确认失败，恢复资料失败");
                                return;
                            }
                        }
                        checksqlstatus = false;
                        try {
                            checksqlstatus = cwa.CallRB("checkout_submit53", lotserial, lotno, opno, opnopaoliao, paoliaoqty, SFCStaticdata.staticmember.pename, att2, att3);
                        } catch (Exception ex) {
                            ShowMessage(ex.toString());
                        }
                        if (!checksqlstatus) {

                            Boolean deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout抛料信息确认失败");
                            if (deldblot) {
                                ShowMessage("抛料信息儲存不良失敗");
                                return;
                            } else {
                                ShowMessage("checkout抛料信息儲存不良失敗，恢复资料失败");
                                return;
                            }
                        }
                    }
                    if (SFCStaticdata.staticmember.odbname.equals("J1617APDEV") || SFCStaticdata.staticmember.odbname.equals("J17APDEV") ||
                            SFCStaticdata.staticmember.odbname.equals("J70APDEV") || SFCStaticdata.staticmember.odbname.equals("J78APDEV")) {
                    } else {
                        String checkitemcount = cwa.CallRS("checkout_submit54", lotno, opno);
                        if (checkitemcount.equals("") || checkitemcount == null) {
                            ShowMessage("查询SetUP类型不良数目时发生错误，请联系MIS");
                            return;
                        } else {
                            String setqtycheck = cwa.CallRS("getopatt10", opno);
                            if (!setqtycheck.equals("") && setqtycheck != null) {
                                if (Integer.parseInt(checkitemcount) > Integer.parseInt(setqtycheck)) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            "当前站位Setup不良数量超过设定数量（" + checkitemcount + "顆），是否需要強行過站 ?<" + checkitemcount + ">", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "4", "23", "1","1");
                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        ShowMessage("验证强行出站失败！<当前站位Setup不良数量超过设定数量（" + checkitemcount + "顆）>");
                                        return;
                                    }
                                }
                            } else {
                                ShowMessage("當前站位SetUp數量管控尚未設定，請聯繫MIS設定");
                                return;
                            }
                        }
                    }
                    if (SFCStaticdata.staticmember.odbname.equals("J1617APDEV") || SFCStaticdata.staticmember.odbname.equals("J17APDEV") ||
                            SFCStaticdata.staticmember.odbname.equals("J70APDEV") || SFCStaticdata.staticmember.odbname.equals("J78APDEV") ||
                            SFCStaticdata.staticmember.deviceno.equals("3D")) {

                    } else {
                        String checkitemcount = cwa.CallRS("checkout_submit_55", lotno, opno);
                        if (checkitemcount.equals("") || checkitemcount == null) {
                            ShowMessage("查询SetUP类型不良数目时发生错误，请联系MIS");
                            return;
                        } else {
                            String setqtycheck = cwa.CallRS("getopatt10", opno);
                            if (!setqtycheck.equals("") && setqtycheck != null) {
                                if (Integer.parseInt(checkitemcount) > Integer.parseInt(setqtycheck)) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            "当前站位設定的不良代碼不良数量超过设定数量（" + String.valueOf(checkitemcount) + "顆），是否需要強行過站 ?<" + checkitemcount + ">", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "4", "23", "1","1");
                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        ShowMessage("验证强行出站失败！<当前站位設定的不良代碼不良数量超过设定数量（" + checkitemcount + "顆）>");
                                        return;
                                    }
                                }
                            } else {
                                ShowMessage("當前站位不良代碼數量管控尚未設定，請聯繫MIS設定");
                                return;
                            }
                        }
                    }
                }
            } else    // OQC站位
            {
                String qcsum = getlotqclog(lotno, opno);     // lotcheckqty  抽测次數
                qcsum = String.valueOf(Integer.parseInt(qcsum) + 1);   // update t_wiplotsnlog set att2=:qcsum2 where lotno=:lotno2 and opno=:opno2;end;
                setText(textBox_qreturnsum, String.valueOf(qcsum));
                String fqcqty = defectqtytextBoxText;
                if (OQCbackchcbChecked)  // Q退
                {
                    if (fqcqty.equals("") || fqcqty == null) {
                        ShowMessage("不良数据不可为空");
                        setFocusable(defectqtytextBox, true);
                        return;
                    } else {
                        if (Integer.parseInt(fqcqty) != 0) {
                            //Sql 執行
                            if ((SFCStaticdata.staticmember.checksmtqcflag && radioButton315Checked && (Integer.parseInt(defectqtytextBoxText.trim()) == 1 && (!OQCbackchcbChecked))) || ((SFCStaticdata.staticmember.checksmtqcflag && radioButton500Checked && ((Integer.parseInt(defectqtytextBoxText.toString().trim()) == 1 || Integer.parseInt(defectqtytextBoxText.toString().trim()) == 2) && (!OQCbackchcbChecked))))) {
                                ShowMessage("對應原則無需Q退站！");
                                return;
                            } else {
                                //QC 退站原因輸入
                                SFCStaticdata.staticmember.podtestsumcheck = false;
                                CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                        "請輸入QC退站原因", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "4", "34", "1", "1");  // 不檢測工號密碼。
                                if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                    ShowMessage("QC退站原因記錄失敗,不良信息已經還原！");
                                    return;
                                }
                                String OQCsql = "";
                                String[] OQCparmers = null;
                                if (cwa.CallRB("RIandRIAddEOLQCStation", opnotextBoxText))//RI與RI+ EOL QC站位  FOL也加入 20151210 BY CYF
                                {
                                    List<String> Stringparmer = new ArrayList<String>();
                                    OQCsql = "begin ";
                                    for (int i = 0; i < SFCStaticdata.staticmember.qcstaticListString.size(); i++) {
                                        OQCsql = OQCsql + "insert into tblFQCbackstationbak (lotno,fromopno,toopno,backtime,backip,creator,qcworkdaydn,mfgop,mfgcheck,engcheck,qccheck,att1,att2,att3,att4,att5) values(:lotno" + String.valueOf(i) + ",:OQCOPNO" + String.valueOf(i) + ",:undoopno" + String.valueOf(i) + ",sysdate,:backip" + String.valueOf(i) + ",:creator" + String.valueOf(i) + ",:qcworkdaydn" + String.valueOf(i) + ",:mfgop" + String.valueOf(i) + ",:mfgcheck" + String.valueOf(i) + ",:engcheck" + String.valueOf(i) + ",:qccheck" + String.valueOf(i) + ",:lotcheckqty" + String.valueOf(i) + ",:lotcheckqtysum" + String.valueOf(i) + ",:qcsum" + String.valueOf(i) + ",:defectgroup" + String.valueOf(i) + ",:snnow" + String.valueOf(i) + ");";
                                        Stringparmer.add(lotno);
                                        Stringparmer.add(opno);
                                        Stringparmer.add(undoopno);
                                        Stringparmer.add(checkoutip);
                                        Stringparmer.add(userid);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[0]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[1]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[2]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[3]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[4]);
                                        Stringparmer.add(lotcheckqty);
                                        Stringparmer.add(defectqtytextBoxText);
                                        Stringparmer.add(qcsum);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[5]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[6]);
                                    }
                                    OQCsql = OQCsql + "insert into tblFQCbackstationlog (lotno,fromopno,toopno,backtime,backip,creator,att1,att2,att3) values(:lotno,:OQCOPNO,:undoopno,sysdate,:backip,:creator,:lotcheckqty,:lotcheckqtysum,:qcsum);end;";
                                    Stringparmer.add(lotno);
                                    Stringparmer.add(opno);
                                    Stringparmer.add(undoopno);
                                    Stringparmer.add(checkoutip);
                                    Stringparmer.add(userid);
                                    Stringparmer.add(lotcheckqty);
                                    Stringparmer.add(defectqtytextBoxText);
                                    Stringparmer.add(qcsum);
                                    String[] parmerstemp = new String[Stringparmer.size()];
                                    Stringparmer.toArray(parmerstemp);
                                    OQCparmers = parmerstemp;
                                } else if (SFCStaticdata.staticmember.fqcflag) {
                                    List<String> Stringparmer = new ArrayList<String>();
                                    OQCsql = "begin ";
                                    for (int i = 0; i < SFCStaticdata.staticmember.qcstaticListString.size(); i++) {
                                        OQCsql = OQCsql + "insert into tblFQCbackstationbak (lotno,fromopno,toopno,backtime,backip,creator,qcworkdaydn,mfgop,mfgcheck,engcheck,qccheck,att1,att2,att3,att4,att5) values(:lotno" + String.valueOf(i) + ",:OQCOPNO" + String.valueOf(i) + ",:undoopno" + String.valueOf(i) + ",sysdate,:backip" + String.valueOf(i) + ",:creator" + String.valueOf(i) + ",:qcworkdaydn" + String.valueOf(i) + ",:mfgop" + String.valueOf(i) + ",:mfgcheck" + String.valueOf(i) + ",:engcheck" + String.valueOf(i) + ",:qccheck" + String.valueOf(i) + ",:lotcheckqty" + String.valueOf(i) + ",:lotcheckqtysum" + String.valueOf(i) + ",:qcsum" + String.valueOf(i) + ",:defectgroup" + String.valueOf(i) + ",:snnow" + String.valueOf(i) + ");";
                                        Stringparmer.add(lotno);
                                        Stringparmer.add(opno);
                                        Stringparmer.add(undoopno);
                                        Stringparmer.add(checkoutip);
                                        Stringparmer.add(userid);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[0]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[1]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[2]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[3]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[4]);
                                        Stringparmer.add(lotcheckqty);
                                        Stringparmer.add(defectqtytextBoxText);
                                        Stringparmer.add(qcsum);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[5]);
                                        Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[6]);
                                    }
                                    OQCsql = OQCsql + "insert into tblFQCbackstationlog (lotno,fromopno,toopno,backtime,backip,creator,att1,att2,att3) values(:lotno,:OQCOPNO,:undoopno,sysdate,:backip,:creator,:lotcheckqty,:lotcheckqtysum,:qcsum);";
                                    Stringparmer.add(lotno);
                                    Stringparmer.add(opno);
                                    Stringparmer.add(undoopno);
                                    Stringparmer.add(checkoutip);
                                    Stringparmer.add(userid);
                                    Stringparmer.add(lotcheckqty);
                                    Stringparmer.add(defectqtytextBoxText);
                                    Stringparmer.add(qcsum);
                                    String fqcname = textBoxfqcopText;
                                    OQCsql = OQCsql + "insert into tblfaccheckopdata values(:lotno00,:fqcname,:createid,:createip,sysdate,null,:att2opno,null,null,null);end;";
                                    Stringparmer.add(lotno);
                                    Stringparmer.add(fqcname);
                                    Stringparmer.add(userid);
                                    Stringparmer.add(ip);
                                    Stringparmer.add(opno);
                                    String[] parmerstemp = new String[Stringparmer.size()];
                                    Stringparmer.toArray(parmerstemp);
                                    OQCparmers = parmerstemp;
                                } else {
                                    OQCsql = "begin insert into tblFQCbackstationlog (lotno,fromopno,toopno,backtime,backip,creator,att1,att2,att3) values(:lotno,:OQCOPNO,:undoopno,sysdate,:backip,:creator,:lotcheckqty,:lotcheckqtysum,:qcsum);end;";
                                    String[] parmerstemp = {lotno, opno, undoopno, checkoutip, userid, lotcheckqty, defectqtytextBoxText, qcsum};
                                    OQCparmers = parmerstemp;
                                }
                                //Todo:测试时需注意，此处将String[]转Json传入，需测试传入后执行的正确性 Tod 2017/03/22
                                Boolean OQCexec = cwa.CallRB("execinfo", OQCsql, BaseFuncation.SerializeObjectArrayString(OQCparmers));
                                if (!OQCexec) {
                                    ShowMessage("存儲tblFQCbackstationlog失敗﹐請聯系MIS部門");
                                    return;
                                }
                                if (cwa.CallRB("RIandRIAddEOLQCStation", opnotextBoxText)) {
                                    SFCStaticdata.staticmember.qcstaticListString.clear();
                                }
                                if (SFCStaticdata.staticmember.errorqtydata == null) {
                                    ShowMessage("尚未輸入不良品資料");
                                    return;
                                }
                                if (SFCStaticdata.staticmember.errorqtydata.size() == 0) {
                                    removeAllViewsInLayout(errordataGridView);
                                    ShowMessage("尚未輸入不良品資料,請重新輸入！");
                                    return;
                                }
                                String tempsqlnew = "begin ";
                                if (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) {
                                    att3 = "0";//代表QC退記錄t_lotdefectdata
                                }
                                for (int i = 0; i < SFCStaticdata.staticmember.errorqtydata.size(); i++) {
                                    opnodefectno = SFCStaticdata.staticmember.errorqtydata.get(i).errorcode;
                                    String defectcharacter = cwa.CallRS("geterrorcharacter", opnodefectno, opno);
                                    if (Integer.parseInt(defectcharacter) == 1) {
                                        setupqty = String.valueOf(Integer.parseInt(setupqty) + SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                                    }
                                    ngqty = String.valueOf(SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                                    String lenstype = cwa.CallRS("checkout_submit_49", lotno);
                                    if (lenstype != null && !lenstype.equals("")) {
                                        String limits = cwa.CallRS("checkout_submit_50", opno, SFCStaticdata.staticmember.errorqtydata.get(i).errorcode.trim());
                                        if (limits != null && !limits.equals("")) {
                                            Double ngrate = SFCStaticdata.staticmember.errorqtydata.get(i).errorqty / Double.parseDouble(dieqty) * 100;
                                            if (ngrate > Double.parseDouble(limits)) {
                                                SFCStaticdata.staticmember.pename = null;
                                                CreatNewActivity(ENGConfirm.class, _deviceno, _newdeviceno, _odbname,
                                                        SFCStaticdata.staticmember.errorqtydata.get(i).errorname, limits, ngrate.toString());
                                            }
                                        }
                                    }
                                    String[] tempparmer = {};
                                    if (i == SFCStaticdata.staticmember.errorqtydata.size() - 1) {
                                        tempsqlnew = tempsqlnew + " insert into T_lotdefectdata values('" + lotserial + "','" + lotno + "','" + opno + "','" + opnodefectno + "','" + ngqty + "',sysdate,'" + SFCStaticdata.staticmember.pename + "','" + att2 + "','" + att3 + "');";
                                        checksqlstatus = false;
                                        try {
                                            String tempNgsnSql = tempsqlnew;
                                            if (SFCStaticdata.staticmember.insertNgSnLotSql == null)//對於不需要掃描單顆的不良直接往下走
                                            {
                                            } else if (SFCStaticdata.staticmember.insertNgSnLotSql.trim() != null &&
                                                    !SFCStaticdata.staticmember.insertNgSnLotSql.trim().equals("") &&
                                                    SFCStaticdata.staticmember.insertNgSnLotSql.trim().toUpperCase().contains("INSERT")) {
                                                tempNgsnSql = tempNgsnSql + SFCStaticdata.staticmember.insertNgSnLotSql;
                                            }
                                            tempNgsnSql = tempNgsnSql + "end;";
                                            checksqlstatus = cwa.CallRB("checkout_submit_51", tempNgsnSql);
                                            SFCStaticdata.staticmember.insertNgSnLotSql = "";
                                        } catch (Exception ex) {
                                            ShowMessage("QC执行插入不良动作时发生错误！" + ex.toString());

                                        }
                                    } else {
                                        tempsqlnew = tempsqlnew + " insert into T_lotdefectdata values('" + lotserial + "','" + lotno + "','" + opno + "','" + opnodefectno + "','" + ngqty + "',sysdate,'" + SFCStaticdata.staticmember.pename + "','" + att2 + "','" + att3 + "');";
                                    }
                                }
                                if (!checksqlstatus) {
                                    Boolean deldblot = deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout儲存不良失敗QC");
                                    if (deldblot) {
                                        ShowMessage("儲存不良失敗QC");
                                        return;
                                    } else {
                                        ShowMessage("checkout儲存不良失敗QC，恢复资料失败");
                                        return;
                                    }
                                }
                            }
                        } else {
                            ShowMessage("Q退不良数据不可为0");
                            setFocusable(defectqtytextBox, true);
                            return;
                        }
                    }
                } else {
                    if (Integer.parseInt(fqcqty) != 0) {
                        if ((SFCStaticdata.staticmember.checksmtqcflag && radioButton315Checked && (Integer.parseInt(defectqtytextBoxText.trim()) == 1 && (!OQCbackchcbChecked))) || ((SFCStaticdata.staticmember.checksmtqcflag && radioButton500Checked && ((Integer.parseInt(defectqtytextBoxText.toString().trim()) == 1 || Integer.parseInt(defectqtytextBoxText.toString().trim()) == 2) && (!OQCbackchcbChecked)))) || (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean == true && SFCStaticdata.staticmember.lotcheckeolflag == true)) {
                            if (SFCStaticdata.staticmember.errorqtydata == null) {
                                ShowMessage("尚未輸入不良品資料");
                                return;
                            }
                            if (SFCStaticdata.staticmember.errorqtydata.size() == 0) {
                                removeAllViewsInLayout(errordataGridView);
                                ShowMessage("尚未輸入不良品資料,請重新輸入！");
                                return;
                            }
                            String tempsqlqc = "begin ";
                            for (int i = 0; i < SFCStaticdata.staticmember.errorqtydata.size(); i++) {
                                opnodefectno = SFCStaticdata.staticmember.errorqtydata.get(i).errorcode;
                                String defectcharacter = cwa.CallRS("geterrorcharacter", opnodefectno, opno);
                                if (Integer.parseInt(defectcharacter) == 1) {
                                    setupqty = String.valueOf(Integer.parseInt(setupqty) + SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                                }
                                ngqty = String.valueOf(SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                                String lenstype = cwa.CallRS("checkout_submit_49", lotno);
                                if (lenstype != null && !lenstype.equals("")) {
                                    String limits = cwa.CallRS("checkout_submit_50", lenstype, opno, SFCStaticdata.staticmember.errorqtydata.get(i).errorcode.trim());
                                    if (limits != null && !limits.equals("")) {
                                        Double ngrate = SFCStaticdata.staticmember.errorqtydata.get(i).errorqty / Double.parseDouble(dieqty) * 100;
                                        if (ngrate > Double.parseDouble(limits)) {
                                            SFCStaticdata.staticmember.pename = null;
                                            CreatNewActivity(ENGConfirm.class, _deviceno, _newdeviceno, _odbname,
                                                    SFCStaticdata.staticmember.errorqtydata.get(i).errorname, limits, ngrate.toString());
                                        }
                                    }
                                }
                                String[] tempparmer = {};
                                if (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) {
                                    att3 = "1";
                                }
                                if (i == SFCStaticdata.staticmember.errorqtydata.size() - 1) {
                                    tempsqlqc = tempsqlqc + " insert into T_lotdefectdata values('" + lotserial + "','" + lotno + "','" + opno + "','" + opnodefectno + "','" + ngqty + "',sysdate,'" + SFCStaticdata.staticmember.pename + "','" + att2 + "','" + att3 + "');";
                                    checksqlstatus = false;
                                    try {
                                        String tempNgsnSql = tempsqlqc;
                                        if (SFCStaticdata.staticmember.insertNgSnLotSql == null) {
                                        } else if (SFCStaticdata.staticmember.insertNgSnLotSql.trim() != null && !SFCStaticdata.staticmember.insertNgSnLotSql.trim().equals("") && SFCStaticdata.staticmember.insertNgSnLotSql.toString().trim().toUpperCase().contains("INSERT")) {
                                            tempNgsnSql = tempNgsnSql + SFCStaticdata.staticmember.insertNgSnLotSql;
                                        }
                                        tempNgsnSql = tempNgsnSql + "end;";
                                        checksqlstatus = cwa.CallRB("checkout_submit_51", tempNgsnSql);
                                        SFCStaticdata.staticmember.insertNgSnLotSql = "";
                                    } catch (Exception ex) {
                                        ShowMessage("QC执行插入不良动作时发生错误！" + ex.toString());
                                    }
                                } else {
                                    tempsqlqc = tempsqlqc + " insert into T_lotdefectdata values('" + lotserial + "','" + lotno + "','" + opno + "','" + opnodefectno + "','" + ngqty + "',sysdate,'" + SFCStaticdata.staticmember.pename + "','" + att2 + "','" + att3 + "');";
                                }
                            }
                            if (!checksqlstatus) {
                                Boolean deldblot = false;
                                if (Integer.parseInt(transfererp) != 2) {
                                    deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout儲存不良失敗QC");
                                } else {
                                    if (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) {
                                        deldblot = cwa.CallRB("deldefectsnForQC", lotno, opno, "checkout儲存不良失敗QC");
                                    }
                                }
                                if (deldblot) {
                                    ShowMessage("儲存不良失敗QC");
                                    return;
                                } else {
                                    ShowMessage("checkout儲存不良失敗QC，恢复资料失败");
                                    return;
                                }
                            }
                        } else if (SFCStaticdata.staticmember.OQCNGCheckinFVI) {
                            String qcngcheckopno = cwa.CallRS("getqcngcheckopno", opnotextBoxText);
                            if (qcngcheckopno.equals("")) {
                                qcngcheckopno = opnotextBoxText;
                            }
                            String OQCsql = "";
                            String[] OQCparmers = null;
                            if (cwa.CallRB("RIandRIAddEOLQCStation", opnotextBoxText))//RI與RI+ EOL QC站位  FOL也加入 
                            {
                                List<String> Stringparmer = new ArrayList<String>();
                                OQCsql = "begin ";
                                for (int i = 0; i < SFCStaticdata.staticmember.qcstaticListString.size(); i++) {
                                    OQCsql = OQCsql + "insert into tblFQCbackstationbak (lotno,fromopno,toopno,backtime,backip,creator,qcworkdaydn,mfgop,mfgcheck,engcheck,qccheck,att1,att2,att3,att4,att5) values(:lotno" + String.valueOf(i) + ",:OQCOPNO" + String.valueOf(i) + ",:undoopno" + String.valueOf(i) + ",sysdate,:backip" + String.valueOf(i) + ",:creator" + String.valueOf(i) + ",:qcworkdaydn" + String.valueOf(i) + ",:mfgop" + String.valueOf(i) + ",:mfgcheck" + String.valueOf(i) + ",:engcheck" + String.valueOf(i) + ",:qccheck" + String.valueOf(i) + ",:lotcheckqty" + String.valueOf(i) + ",:lotcheckqtysum" + String.valueOf(i) + ",:qcsum" + String.valueOf(i) + ",:defectgroup" + String.valueOf(i) + ",:snnow" + String.valueOf(i) + ");";
                                    Stringparmer.add(lotno);
                                    Stringparmer.add(opno);
                                    Stringparmer.add(qcngcheckopno);
                                    Stringparmer.add(checkoutip);
                                    Stringparmer.add(userid);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[0]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[1]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[2]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[3]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[4]);
                                    Stringparmer.add(lotcheckqty);
                                    Stringparmer.add(defectqtytextBoxText);
                                    Stringparmer.add(qcsum);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[5]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[6]);
                                }
                                OQCsql = OQCsql + "insert into tblFQCbackstationlog (lotno,fromopno,toopno,backtime,backip,creator,att1,att2,att3) values(:lotno,:OQCOPNO,:undoopno,sysdate,:backip,:creator,:lotcheckqty,:lotcheckqtysum,:qcsum);end;";
                                Stringparmer.add(lotno);
                                Stringparmer.add(opno);
                                Stringparmer.add(qcngcheckopno);
                                Stringparmer.add(checkoutip);
                                Stringparmer.add(userid);
                                Stringparmer.add(lotcheckqty);
                                Stringparmer.add(defectqtytextBoxText);
                                Stringparmer.add(qcsum);
                                String[] parmerstemp = new String[Stringparmer.size()];
                                Stringparmer.toArray(parmerstemp);
                                OQCparmers = parmerstemp;
                            } else if (SFCStaticdata.staticmember.fqcflag) {
                                List<String> Stringparmer = new ArrayList<String>();
                                OQCsql = "begin ";
                                for (int i = 0; i < SFCStaticdata.staticmember.qcstaticListString.size(); i++) {
                                    OQCsql = OQCsql + "insert into tblFQCbackstationbak (lotno,fromopno,toopno,backtime,backip,creator,qcworkdaydn,mfgop,mfgcheck,engcheck,qccheck,att1,att2,att3,att4,att5) values(:lotno" + String.valueOf(i) + ",:OQCOPNO" + String.valueOf(i) + ",:undoopno" + String.valueOf(i) + ",sysdate,:backip" + String.valueOf(i) + ",:creator" + String.valueOf(i) + ",:qcworkdaydn" + String.valueOf(i) + ",:mfgop" + String.valueOf(i) + ",:mfgcheck" + String.valueOf(i) + ",:engcheck" + String.valueOf(i) + ",:qccheck" + String.valueOf(i) + ",:lotcheckqty" + String.valueOf(i) + ",:lotcheckqtysum" + String.valueOf(i) + ",:qcsum" + String.valueOf(i) + ",:defectgroup" + String.valueOf(i) + ",:snnow" + String.valueOf(i) + ");";
                                    Stringparmer.add(lotno);
                                    Stringparmer.add(opno);
                                    Stringparmer.add(qcngcheckopno);
                                    Stringparmer.add(checkoutip);
                                    Stringparmer.add(userid);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[0]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[1]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[2]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[3]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[4]);
                                    Stringparmer.add(lotcheckqty);
                                    Stringparmer.add(defectqtytextBoxText);
                                    Stringparmer.add(qcsum);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[5]);
                                    Stringparmer.add(SFCStaticdata.staticmember.qcstaticListString.get(i)[6]);
                                }
                                OQCsql = OQCsql + "insert into tblFQCbackstationlog (lotno,fromopno,toopno,backtime,backip,creator,att1,att2,att3) values(:lotno,:OQCOPNO,:undoopno,sysdate,:backip,:creator,:lotcheckqty,:lotcheckqtysum,:qcsum);";
                                Stringparmer.add(lotno);
                                Stringparmer.add(opno);
                                Stringparmer.add(qcngcheckopno);
                                Stringparmer.add(checkoutip);
                                Stringparmer.add(userid);
                                Stringparmer.add(lotcheckqty);
                                Stringparmer.add(defectqtytextBoxText);
                                Stringparmer.add(qcsum);
                                String fqcname = textBoxfqcopText;
                                OQCsql = OQCsql + "insert into tblfaccheckopdata values(:lotno00,:fqcname,:createid,:createip,sysdate,null,:att2opno,null,null,null);end;";
                                Stringparmer.add(lotno);
                                Stringparmer.add(fqcname);
                                Stringparmer.add(userid);
                                Stringparmer.add(ip);
                                Stringparmer.add(opno);
                                String[] parmerstemp = new String[Stringparmer.size()];
                                Stringparmer.toArray(parmerstemp);
                                OQCparmers = parmerstemp;
                            } else {
                                OQCsql = "begin insert into tblFQCbackstationlog (lotno,fromopno,toopno,backtime,backip,creator,att1,att2,att3) values(:lotno,:OQCOPNO,:undoopno,sysdate,:backip,:creator,:lotcheckqty,:lotcheckqtysum,:qcsum);end;";
                                String[] parmerstemp = {lotno, opno, undoopno, checkoutip, userid, lotcheckqty, defectqtytextBoxText, qcsum};
                                OQCparmers = parmerstemp;
                            }
                            Boolean OQCexec = cwa.CallRB("execinfo", OQCsql, BaseFuncation.SerializeObjectArrayString(OQCparmers));
                            if (!OQCexec) {
                                ShowMessage("存儲tblFQCbackstationlog失敗﹐請聯系MIS部門");
                                return;
                            }
                            if (cwa.CallRB("RIandRIAddEOLQCStation", opnotextBoxText)) {
                                SFCStaticdata.staticmember.qcstaticListString.clear();
                            }
                            if (SFCStaticdata.staticmember.errorqtydata == null) {
                                ShowMessage("尚未輸入不良品資料");
                                return;
                            }
                            if (SFCStaticdata.staticmember.errorqtydata.size() == 0) {
                                removeAllViewsInLayout(errordataGridView);
                                ShowMessage("尚未輸入不良品資料,請重新輸入！");
                                return;
                            }
                            String tempsqlqc = "begin ";
                            for (int i = 0; i < SFCStaticdata.staticmember.errorqtydata.size(); i++) {
                                opnodefectno = SFCStaticdata.staticmember.errorqtydata.get(i).errorcode;
                                String defectcharacter = cwa.CallRS("geterrorcharacter", opnodefectno, opno);
                                if (Integer.parseInt(defectcharacter) == 1) {
                                    setupqty = String.valueOf(Integer.parseInt(setupqty) + SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                                }
                                ngqty = String.valueOf(SFCStaticdata.staticmember.errorqtydata.get(i).errorqty);
                                String lenstype = cwa.CallRS("checkout_submit_49", lotno);
                                if (lenstype != null && !lenstype.equals("")) {
                                    String limits = cwa.CallRS("checkout_submit_50", lenstype, opno, SFCStaticdata.staticmember.errorqtydata.get(i).errorcode.trim());
                                    if (limits != null && !limits.equals("")) {
                                        Double ngrate = SFCStaticdata.staticmember.errorqtydata.get(i).errorqty / Double.parseDouble(dieqty) * 100;
                                        if (ngrate > Double.parseDouble(limits)) {
                                            SFCStaticdata.staticmember.pename = null;
                                            CreatNewActivity(ENGConfirm.class, _deviceno, _newdeviceno, _odbname,
                                                    SFCStaticdata.staticmember.errorqtydata.get(i).errorname, limits, ngrate.toString());
                                        }
                                    }
                                }
                                String[] tempparmer = {};
                                checksqlstatus = false;
                                try {
                                    String tempNgsnSql = tempsqlqc;
                                    if (SFCStaticdata.staticmember.insertNgSnLotSql == null) {
                                    } else if (SFCStaticdata.staticmember.insertNgSnLotSql.trim() != null && !SFCStaticdata.staticmember.insertNgSnLotSql.trim().equals("") && SFCStaticdata.staticmember.insertNgSnLotSql.trim().toUpperCase().contains("INSERT")) {
                                        tempNgsnSql = tempNgsnSql + SFCStaticdata.staticmember.insertNgSnLotSql;
                                    }
                                    tempNgsnSql = tempNgsnSql + "end;";
                                    tempNgsnSql = tempNgsnSql.replace("OQCFVICHECKNG", oqcngfviflag);
                                    checksqlstatus = cwa.CallRB("checkout_submit_51", tempNgsnSql);
                                    SFCStaticdata.staticmember.insertNgSnLotSql = "";
                                } catch (Exception ex) {
                                    ShowMessage("QC执行插入不良动作时发生错误！" + ex.toString());
                                }
                            }
                            if (!checksqlstatus) {
                                Boolean deldblot = false;
                                if (Integer.parseInt(transfererp) != 2) {
                                    deldblot = cwa.CallRB("deldefectsn", lotno, opno, "checkout儲存不良失敗QC");
                                } else {
                                    if (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) {
                                        deldblot = cwa.CallRB("deldefectsnForQC", lotno, opno, "checkout儲存不良失敗QC");
                                    }
                                }
                                if (deldblot) {
                                    ShowMessage("儲存不良失敗QC");
                                    return;
                                } else {
                                    ShowMessage("checkout儲存不良失敗QC，恢复资料失败");
                                    return;
                                }
                            }
                        }
                    }
                }
            }


            if (checksqlstatus) {
                if (SFCStaticdata.staticmember.deviceno.equals("RI-L PRQ") ||
                        SFCStaticdata.staticmember.deviceno.equals("RI-L") || SFCStaticdata.staticmember.deviceno.equals("RI-G")) {
                    String opnonow = opnotextBoxText;
                    String num_in = dietextBoxText.trim();
                    String num_ng = defectqtytextBoxText.trim();
                    if (num_ng.startsWith("0")) {
                        num_ng = num_ng.substring(1, num_ng.length() - 1);
                    }
                    String yield = "";
                    DataTable yieldset = cwa.CallRDT("getyielddata", opnonow);
                    if (yieldset.Rowscount() > 0) {
                        if (yieldset.Rows(0).get_CellValue("att1").trim().equals("ALL")) {
                        } else {
                            num_ng = cwa.CallRS("checkout_submit_56", opnonow, yieldset.Rows(0).get_CellValue("att1").trim(), lotnotextBoxText);
                            if (num_ng.equals("")) {
                                num_ng = "0";
                            }
                            if (num_ng == null) {
                                ShowMessage("");
                                return;
                            }
                        }
                        yield = String.valueOf((Double.parseDouble(num_ng) / Double.parseDouble(num_in)) * 100);
                        if ((Double.parseDouble(yield) > Double.parseDouble(yieldset.Rows(0).get_CellValue("att2").trim()))) {
                            ShowMessage(yieldset.Rows(0).get_CellValue("att4") + "不良率" + yield + "% 超过规定值" + yieldset.Rows(0).get_CellValue("att2").trim() + "%，需PE解锁才能过站");
//todo create new class open no folo return
//                        unholdlinelot un = new unholdlinelot("peunhold");
//                        un.ShowDialog();
                            if (SFCStaticdata.staticmember.yieldflag) {
                            } else {
                                ShowMessage("解锁失败");
                                setEnabled(submitbutton, true);
                                return;
                            }
                        } else if ((Integer.parseInt(num_ng) >= Integer.parseInt(yieldset.Rows(0).get_CellValue("att3").trim()))) {
                            ShowMessage(yieldset.Rows(0).get_CellValue("att4") + "不良数" + num_ng + " 超过规定单批不良数" + yieldset.Rows(0).get_CellValue("att3").toString().trim() + "，需PE解锁才能过站");
                            //todo create new class open no fol return
//                        unholdlinelot un = new unholdlinelot("peunhold");
//                        un.ShowDialog();
                            if (SFCStaticdata.staticmember.yieldflag) {
                            } else {
                                ShowMessage("解锁失败");
                                setEnabled(submitbutton, true);
                                return;
                            }
                        }
                    }
                }
            }
            //在此處插入LHA數據
            checkoutsubmit.checkout_submit3(this,cwa,att14,LhatxtText,lotno,opno,"EOL插入LHA機;號失敗，請聯繫MIS部門","checkout_submit_57");
            //在此處插入GA數據
            checkoutsubmit.checkout_submit3(this,cwa,att14,LhatxtText,lotno,opno,"FOL插入GA機台號失敗，請聯繫MIS部門","checkout_submit_58");
            String outinopno = cwa.CallRS("checkoutinsertmaterialopno", opno);   //VT-Q 出站掃入耗材 wu.hp 12-26
            checkoutsubmit.checkout_submit4(this, cwa, outinopno, opno, productno, lotno, opnoflowid, _deviceno, _newdeviceno, _odbname);

        // VT-Q FC 站位出站解綁載板
            //String carrieropno = cwa.CallRS("getopencarrieropno", opno);
            //checkoutsubmit.checkout_submit5(this, carrieropno,_deviceno,  _newdeviceno,  _odbname,lotno, opno);
             checkoutsubmit.removecarrieropno(this, cwa, opnotextBoxText, lotnotextBoxText);

        //WIP分批
             checkoutsubmit.checkout_submit6(this, cwa,
                    lotno, opno, dieqty, defectqtytextBoxText, opnoflowid, lotnotextBoxText, ip,  getwiplotnosum(lotno),
                    getwiplotnosum1(lotno)
            );

        //RI and RI+ EOL QC   在插入單顆SN記錄之後再作業  写的有病 Q退两个站位以后呢，程序崩溃
            checkoutsubmit.checkout_submit7(this,cwa, staticproductno, opno, lotnotextBoxText, lotno);

            String defectqty = "";
            String defsetupqty = "";
            String curqty = "";
            String loginputqty = "";
        //不良数目变更  SMT
            if (SFCStaticdata.staticmember.checksmtqcflag) {
                if ((SFCStaticdata.staticmember.checksmtqcflag && radioButton315Checked && (Integer.parseInt(defectqtytextBoxText.trim()) == 1 && (!OQCbackchcbChecked))) || ((SFCStaticdata.staticmember.checksmtqcflag && radioButton500Checked && ((Integer.parseInt(defectqtytextBoxText.toString().trim()) == 1 || Integer.parseInt(defectqtytextBoxText.toString().trim()) == 2) && (!OQCbackchcbChecked))))) {
                    defectqty = String.valueOf(Integer.parseInt(defectqtytextBoxText) - Integer.parseInt(setupqty)).trim();
                    defsetupqty = defectqtytextBoxText;
                    curqty = String.valueOf(Integer.parseInt(dieqty));
                    loginputqty = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(setupqty)).trim();
                } else {
                    defectqty = String.valueOf(Integer.parseInt(defectqtytextBoxText) - Integer.parseInt(setupqty)).trim();
                    defsetupqty = defectqtytextBoxText;
                    curqty = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(defsetupqty));
                    loginputqty = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(setupqty)).trim();
                }
            } else {
                if (SFCStaticdata.staticmember.OQCNGCheckinFVI && !OQCbackchcbChecked) {
                    defectqty = "0";
                    defsetupqty = "0";
                    curqty = dieqty;
                    loginputqty = dieqty;
                } else {
                    defectqty = String.valueOf(Integer.parseInt(defectqtytextBoxText) - Integer.parseInt(setupqty)).trim();
                    defsetupqty = defectqtytextBoxText;
                    curqty = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(defsetupqty));
                    loginputqty = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(setupqty)).trim();
                }
            }

        //中国市场要卡一次不良20150401  opdata  att2='8'
            checkoutsubmit.checkout_submit8( this,cwa, checkBox_dbEnabled, lotno, opno, defectqty, lotnotextBoxText);

             //VT-Q機種卡test37站位重工是否有測試數據VTQ
            checkoutsubmit.checkout_submit9(this,cwa,SFCStaticdata.staticmember.newdeviceno,lotno, opno);

            //HC0806  預警郵件發送 顧-20150420
            checkoutsubmit.checkout_submit10(this, cwa, lotno, opno, loginputqty, ip);

            //VTQFOL 1036站不良預警  ***DT
            checkoutsubmit.checkout_submit17(this, cwa, _deviceno,opnotextBoxText, lotno,SFCStaticdata.staticmember.AllReworkFlag, dietextBoxText,opnametextBoxText);

            //VTQsetup 不良预警  ***DT
            checkoutsubmit.checkout_submit13(this, cwa, lotno, opno, opnametextBoxText);

            //POD 特殊不良預警郵件      2015.08.05  原
            checkoutsubmit.checkout_submit14(this, cwa, lotno, opno, loginputqty, ngqty);

            //// 只有  后段 QC 才会有单颗扫描 80 颗产品数据。
            String dtqconono = "";
            if (eoloqcflag && !reworknocheck) {
                DataTable dtqcno = cwa.CallRDT("getopnoforqc");
                if (SFCStaticdata.staticmember.devicenomacinfo) {
                    dtqcno = cwa.CallRDT("getopnoforqc", opno);
                }
                if (dtqcno.Rowscount() > 0) {
                    dtqconono = cwa.CallRS("oqcfvicheckopnoset",SFCStaticdata.staticmember.newdeviceno);
                    if (dtqconono == "" || dtqconono == null)
                    {
                        dtqconono = dtqcno.Rows(0).get_CellValue("opno");
                    }
                    if (dtqconono.equals(opno)) {
                        SFCStaticdata.staticmember.qcsntestsumcheck = false;
                        String lotnoaabb = "";
                        if (lotno.length() == 12 || lotno.length() == 15) {
                            lotnoaabb = lotno;
                        } else if (lotno.length() == 13 || lotno.length() == 16) {
                            lotnoaabb = lotno.substring(1, 13);
                        } else {
                            ShowMessage("批号信息位数异常");
                            return;
                        }
                        if ((!OQCbackchcbChecked) && (!SFCStaticdata.staticmember.devicenomacinfo)) {
                            if (SFCStaticdata.staticmember.deviceno.equals("VT-Q")) {
                                SFCStaticdata.staticmember.qcsntestsumcheck = false;
                                //todo create new class open no fol check
//                            VTQQCchecksnscan qccheck = new VTQQCchecksnscan(lotnoaabb, opno, lotcheckqty);
//                            qccheck.ShowDialog();
                            } else if (SFCStaticdata.staticmember.deviceno.equals("NH-A") || SFCStaticdata.staticmember.deviceno.equals("MAINE") ||
                                    SFCStaticdata.staticmember.newdeviceno.equals("APG002") ||
                                    SFCStaticdata.staticmember.newdeviceno.equals("A3G001")) {
                                SFCStaticdata.staticmember.qcsntestsumcheck = false;
                                String newqcflag = cwa.CallRS("checkout_submit_80", opno);
                                if (newqcflag.equals("3")) {
                                    try {
                                        if (Integer.parseInt(textBox_textqtyText) == 0) {
                                            if (BaseFuncation.DialogResult.OK == MessageBox("該批次是否不抽檢？", "")) {
                                                SFCStaticdata.staticmember.qcsntestsumcheck = true;
                                            } else {
                                                //todo new create class open no fol return
//                                            QCSCANOL qccheck = new QCSCANOL(lotnoaabb, opno, textBox_textqtyText);
//                                            qccheck.ShowDialog();
                                            }
                                        } else {
                                            //todo new create class open no fol qc check
//                                        QCSCANOL qccheck = new QCSCANOL(lotnoaabb, opno, textBox_textqtyText);
//                                        qccheck.ShowDialog();
                                        }
                                    } catch (Exception ex) {
                                        ShowMessage("抽檢數量需輸入數字");
                                        return;
                                    }
                                } else {
                                    //todo new create class open no fol return
//                                NEWQCchecksnscan qccheck = new NEWQCchecksnscan(lotnoaabb, opno, lotcheckqty);
//                                qccheck.ShowDialog();
                                }
                            } else {
                                if (!refflag.equals("1")) {
                                    SFCStaticdata.staticmember.qcsntestsumcheck = false;
                                    //todo create new class open no fol qc check
//                                QCchecksnscan qccheck = new QCchecksnscan(lotnoaabb, opno, lotcheckqty);
//                                qccheck.ShowDialog();
                                }
                            }
                            if (!SFCStaticdata.staticmember.qcsntestsumcheck) {
                                if (!refflag.equals("1")) {
                                    ShowMessage("QC抽检记录失败，请重新扫描");
                                    return;
                                }
                            }
                        } else if ((!OQCbackchcbChecked) && SFCStaticdata.staticmember.devicenomacinfo) {
                            //20150812
                            SFCStaticdata.staticmember.qcsntestsumcheck = false;
                            //todo create new class open no fol return
//                        JQCchecksnscan qccheck = new JQCchecksnscan(lotnoaabb, opno, lotcheckqty);
//                        qccheck.ShowDialog();
                            if (!SFCStaticdata.staticmember.qcsntestsumcheck) {
                                ShowMessage("QC抽检记录失败，请重新扫描");
                                return;
                            }
                        }
                    }
                } else {
                    ShowMessage("该机种下没有对应的ＱＣ站位信息");
                    return;
                }

                if (SFCStaticdata.staticmember.newdeviceno.equals("ANG001") && !dtqconono.equals(opno)) {
                    SFCStaticdata.staticmember.qcsntestsumcheck = false;
                    String lotnoaabb = "";
                    if (lotno.length() == 12 || lotno.length() == 15) {
                        lotnoaabb = lotno;
                    } else if (lotno.length() == 13 || lotno.length() == 16) {
                        lotnoaabb = lotno.substring(1, 12);
                    } else {
                        ShowMessage("批号信息位数异常");
                        return;
                    }
                    if ((!OQCbackchcbChecked) && (!SFCStaticdata.staticmember.devicenomacinfo)) {
                        if (!refflag.equals("1")) {
                            SFCStaticdata.staticmember.qcsntestsumcheck = false;
                            //todo create new class open no fol return
//                        QCchecksnscan qccheck = new QCchecksnscan(lotnoaabb, opno, lotcheckqty);
//                        qccheck.ShowDialog();
                        }
                        if (!SFCStaticdata.staticmember.qcsntestsumcheck) {
                            if (!refflag.equals("1")) {
                                ShowMessage("QC抽检记录失败，请重新扫描");
                                return;
                            }
                        }
                    } else if ((!OQCbackchcbChecked) && SFCStaticdata.staticmember.devicenomacinfo) {
                        //20150812
                        SFCStaticdata.staticmember.qcsntestsumcheck = false;
                        //todo create new class  open no fol return
//                    JQCchecksnscan qccheck = new JQCchecksnscan(lotnoaabb, opno, lotcheckqty);
//                    qccheck.ShowDialog();
                        if (!SFCStaticdata.staticmember.qcsntestsumcheck) {
                            ShowMessage("QC抽检记录失败，请重新扫描");
                            return;
                        }
                    }
                }
            }

            //加入mes信息20150704
            List<String> mesparlist = new ArrayList<String>();
            String mesinertsql = "begin ";
            if (SFCStaticdata.staticmember.IsMesDevice) {
                DataTable usermesdt = new DataTable();
                usermesdt = cwa.CallRDT("checkout_submit_81", opno, SFCStaticdata.staticmember.deviceno);
                if (usermesdt == null || usermesdt.Rowscount() == 0) {
                    SFCStaticdata.staticmember.IsMesDevice = false;
                } else if (usermesdt.Rowscount() > 1) {}

                    String meslotno = lotno;
                    String mesuserid = userid;
                    String mesoutput = curqty;
                    String mesinputqty = loginputqty;
                    String mesdefectqty = defectqty;
                    String mesdeviceno = SFCStaticdata.staticmember.deviceno;
                    String mesgroupid = usermesdt.Rows(0).get_CellValue("groupid").trim();
                    String mesip = checkoutip;
                    String mesline = usermesdt.Rows(0).get_CellValue("linename").trim();
                    DataTable dtalluser = cwa.CallRDT("checkout_submit_82", opno);
                    if (judgemes(lotno, opno, SFCStaticdata.staticmember.odbname)) {
                        for (int mesi = 0; mesi < dtalluser.Rowscount(); mesi++) {
                            mesgroupid = dtalluser.Rows(mesi).get_CellValue("groupid").trim();
                            mesline = dtalluser.Rows(mesi).get_CellValue("linename").trim();
                            mesinertsql += " insert into t_mesoutputdatabak(userid, opno, outputqty, inputqty, defectqty, deviceno, groupid, createdate, line, createip, lotno,att1) values (:userid" + mesi + ",:opno" + mesi + ", :outputqty" + mesi + ", :inputqty" + mesi + ", :defectqty" + mesi + ", :deviceno" + mesi + ", :groupid" + mesi + ", sysdate, :line" + mesi + ", :createip" + mesi + ", :lotno" + mesi + ", :att1" + mesi + ");";
                            mesparlist.add(dtalluser.Rows(mesi).get_CellValue("userid").trim());
                            mesparlist.add(opno);
                            mesparlist.add(BaseFuncation.doubletostring(Double.parseDouble(mesoutput) / Double.parseDouble(dtalluser.Rows(mesi).get_CellValue("userqty")), "0.000"));
                            mesparlist.add(BaseFuncation.doubletostring(Double.parseDouble(mesinputqty) / Double.parseDouble(dtalluser.Rows(mesi).get_CellValue("userqty")), "0.000"));
                            mesparlist.add(BaseFuncation.doubletostring(Double.parseDouble(mesdefectqty) / Double.parseDouble(dtalluser.Rows(mesi).get_CellValue("userqty")), "0.000"));
                            mesparlist.add(mesdeviceno);
                            mesparlist.add(mesgroupid);
                            mesparlist.add(mesline);
                            mesparlist.add(mesip);
                            mesparlist.add(meslotno);
                            mesparlist.add(checkindate);
                        }
                        mesinertsql += "end;";
                    }

            }
            String[] parmer1 = {nextopno, nextopnoflowid, curqty, lotstate, userid, lotno, lotserial, lotno, opno, opnoflowid, loginputqty, curqty, defectqty,
                    checkindate, checkinuser, userid, checkoutpcname, checkoutip, att1, att2, att3, setupqty};
            Boolean checkOQC = false;
            String sql_backupqc = "";
            String[] parmer1OQCback = {};
            //非OQC站位或者OQC站位無需判退 sql
            if (!OQCflagBoolean || (OQCflagBoolean && !OQCbackchcbChecked)) {
                checkOQC = false;
                tempsql = "begin update T_lotstatus set opno=:opno,opnoflowid=:opnoflowid,dieqty=:dieqty,lotstate=:lotstate,userid=:userid,updatedate=sysdate where lotno=:lotno;";
                tempsql += " insert into T_lotlog values(:lotserial,:lotno1,:opno1,:opnoflowid1,:inputqty,:goodqty,:ngqty,to_date(:checkindate,'YYYY-MM-DD HH24:MI:SS'),:checkinuserid,sysdate"
                        + ",:checkoutuserid,:checkoutcompname,:checkoutIP,:att1,:att2,:att3,:setupqty);";
                tempsql += "end;";

                if (SFCStaticdata.staticmember.updatelotcartflag)
                {
                    incomingp = parmer1;
                    incomingsql = tempsql;
                }

                //add check qty lot 与不良细项新增 具体不良与log不良的卡批动作
                if (Integer.parseInt(transfererp) != 2) {
                    int ngsetqty = (Integer.parseInt(defectqty) + Integer.parseInt(setupqty));
                    String sqlchecklotdb = "select nvl(sum(ngqty),0) ngqty from t_lotdefectdata where lotno=:lotno and opno=:opno ";
                    String[] checklotdbparmers = {lotno, opno};
                    int ngsetqtyint = Integer.parseInt(cwa.CallRS("chceckout_submit_83", lotno, opno));
                    if (ngsetqty != ngsetqtyint) {
                        Boolean deldblot = cwa.CallRB("deldefectwipsn", lotno, opno, "插入ng前后不符2273");    //   这个应该需要改一下
                        if (deldblot) {
                            Boolean lotupflag = cwa.CallRB("checkout_submit_84", lotno);
                            cwa.CallRB("sendmail", "wwjc-mis-wip@mail.foxconn.com,wwlh-mis-liu.jj@mail.foxconn.com", cwa.CallRS("getdbsystime") + "批號過站異常DB中不良數目與目前數目不相同！站位：" + opno + "**page-qty" + ngsetqty + "**DB-qty" + ngsetqtyint + "**帳戶：" + lotnooldnndd + SFCStaticdata.staticmember.userid + "**資料庫：" + SFCStaticdata.staticmember.odbname, "");
                            Boolean insdbflag = cwa.CallRB("errorinfobackdb", lotno, cwa.CallRS("getdbsystime") + "不良插入异常！" + opno + "**" + ngsetqty + "**" + ngsetqtyint + "**" + lotnooldnndd + SFCStaticdata.staticmember.userid + "||" + SFCStaticdata.staticmember.odbname, "应该：" + ngsetqty + "**实际：" + ngsetqtyint, SFCStaticdata.staticmember.userid, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.odbname, "33", "");
                            ShowMessage("该批号Log不良与单颗实际不良不符，已恢复数据，请重新过站");
                            this.Close();
                            return;
                        } else {
                            ShowMessage("该批号Log不良与单颗实际不良不符，恢复数据失败，请联系MIS");
                            return;
                        }
                    } else {
                        String att8new = cwa.CallRS("getopatt8", opno);
                        if (att8new.equals("") || att8new == null) {
                        } else {
                            if (ngsetqtyint > 150) {
                                if (BaseFuncation.DialogResult.OK == MessageBox("當前站位打不良數超過最大設定值！,是否需要强行过站？(150)('" + ngsetqtyint + "')", "系統提示")) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            "當前站位打不良數超過最大設定值！（200->150）", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "3", "27","","1");
                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        Boolean dellotdefect = cwa.CallRB("deldefectwipsn", lotno, opno, "單站位強行過站取消強行過站失敗");
                                        if (dellotdefect) {
                                            return;
                                        } else {
                                            ShowMessage("批號強行過站取消過站時隱藏不良失敗，請知會MIS!");
                                            return;
                                        }
                                    }
                                } else {
                                    Boolean dellotdefect = cwa.CallRB("deldefectwipsn", lotno, opno, "單站位強行過站取消強行過站");
                                    if (dellotdefect) {
                                        return;
                                    } else {
                                        ShowMessage("批號強行過站取消過站時隱藏不良失敗，請知會MIS!");
                                        return;
                                    }
                                }
                            }
                            if (ngsetqtyint > Integer.parseInt(att8new)) {
                                if (BaseFuncation.DialogResult.OK == MessageBox("當前站位打不良數超過最大設定值！,是否需要强行过站？(100)('" + ngsetqtyint + "')", "系統提示")) {
                                    SFCStaticdata.staticmember.podtestsumcheck = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            "當前站位打不良數超過最大設定值！", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "2", "26","","1");
                                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                        Boolean dellotdefect = cwa.CallRB("deldefectwipsn", lotno, opno, "验证强行出站失败");
                                        if (dellotdefect) {
                                            return;
                                        } else {
                                            ShowMessage("批號強行過站失敗過站時隱藏不良失敗，請知會MIS!");
                                            return;
                                        }
                                    }
                                } else {
                                    Boolean dellotdefect = cwa.CallRB("deldefectwipsn", lotno, opno, "單站位強行過站取消強行過站");
                                    if (dellotdefect) {
                                        ShowMessage("單站位強行過站取消強行過站");
                                        return;
                                    } else {
                                        ShowMessage("批號強行過站取消過站時隱藏不良失敗，請知會MIS!");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) {
                        int ngsetqty = (Integer.parseInt(defectqty) + Integer.parseInt(setupqty));
                        int ngsetqtyint = Integer.parseInt(cwa.CallRS("checkout_submit_85", lotno, opno));
                        if (ngsetqty != ngsetqtyint) {
                            Boolean deldblot = cwa.CallRB("deldefectwipsnForQC", lotno, opno, "插入ng前后不符2273");
                            if (deldblot) {
                                Boolean lotupflag = cwa.CallRB("checkout_submit_86", lotno);
                                cwa.CallRB("sendmail", "wwjc-mis-wip@mail.foxconn.com,wwlh-mis-liu.jj@mail.foxconn.com", cwa.CallRS("getdbsystime") + "批號過站異常DB中不良數目與目前數目不相同！站位：" + opno + "**page-qty" + ngsetqty + "**DB-qty" + ngsetqtyint + "**帳戶：" + lotnooldnndd + SFCStaticdata.staticmember.userid + "**資料庫：" + SFCStaticdata.staticmember.odbname, "");
                                Boolean insdbflag = cwa.CallRB("errorinfobackdb", lotno, cwa.CallRS("getdbsystime") + "不良插入异常！" + opno + "**" + ngsetqty + "**" + ngsetqtyint + "**" + lotnooldnndd + SFCStaticdata.staticmember.userid + "||" + SFCStaticdata.staticmember.odbname, "应该：" + ngsetqty + "**实际：" + ngsetqtyint, SFCStaticdata.staticmember.userid, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.odbname, "33", "");
                                ShowMessage("该批号Log不良与单颗实际不良不符，已恢复数据，请重新过站");
                                this.Close();
                                return;
                            } else {
                                ShowMessage("该批号Log不良与单颗实际不良不符，恢复数据失败，请联系MIS");
                                return;
                            }
                        } else {
                            String att8new = cwa.CallRS("getopatt8", opno);
                            if (att8new.equals("") || att8new == null) {
                            } else {
                                if (ngsetqtyint > 150) {
                                    if (BaseFuncation.DialogResult.OK == MessageBox("當前站位打不良數超過最大設定值！,是否需要强行过站？(150)('" + ngsetqtyint + "')", "系統提示")) {
                                        SFCStaticdata.staticmember.podtestsumcheck = false;
                                        CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                                "當前站位打不良數超過最大設定值！（200->150）", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "3", "27","","1");

                                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                            Boolean dellotdefect = cwa.CallRB("deldefectwipsnForQC", lotno, opno, "單站位強行過站取消強行過站失敗");
                                            if (dellotdefect) {
                                                return;
                                            } else {
                                                ShowMessage("批號強行過站取消過站時隱藏不良失敗，請知會MIS!");
                                                return;
                                            }
                                        }
                                    } else {
                                        Boolean dellotdefect = cwa.CallRB("deldefectwipsnForQC", lotno, opno, "單站位強行過站取消強行過站");
                                        if (dellotdefect) {
                                            return;
                                        } else {
                                            ShowMessage("批號強行過站取消過站時隱藏不良失敗，請知會MIS!");
                                            return;
                                        }
                                    }
                                }
                                if (ngsetqtyint > Integer.parseInt(att8new)) {
                                    if (BaseFuncation.DialogResult.OK == MessageBox("當前站位打不良數超過最大設定值！,是否需要强行过站？(100)('" + ngsetqtyint + "')", "系統提示")) {
                                        SFCStaticdata.staticmember.podtestsumcheck = false;
                                        CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                                "當前站位打不良數超過最大設定值！", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "2", "26","","1");
                                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                            Boolean dellotdefect = cwa.CallRB("deldefectwipsnForQC", lotno, opno, "验证强行出站失败");
                                            if (dellotdefect) {
                                                return;
                                            } else {
                                                ShowMessage("批號強行過站失敗過站時隱藏不良失敗，請知會MIS!");
                                                return;
                                            }
                                        }
                                    } else {
                                        Boolean dellotdefect = cwa.CallRB("deldefectwipsnForQC", lotno, opno, "單站位強行過站取消強行過站");
                                        if (dellotdefect) {
                                            ShowMessage("單站位強行過站取消強行過站");
                                            return;
                                        } else {
                                            ShowMessage("批號強行過站取消過站時隱藏不良失敗，請知會MIS!");
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {//OQC判退
                checkOQC = true;
                DataTable OQCdt = cwa.CallRDT("checkout_submit_87", lotno, undoopno);
                String oqcundodie = OQCdt.Rows(0).get_CellValue("inputqty").trim();
                String oqcundosetqty = OQCdt.Rows(0).get_CellValue("setupqty").trim();
                if (oqcundosetqty.equals("") || oqcundosetqty == null) {
                } else {
                    oqcundodie = String.valueOf(Integer.parseInt(oqcundodie) + Integer.parseInt(oqcundosetqty));
                }
                String oqcundoopnoflowid = OQCdt.Rows(0).get_CellValue("opnoflowid");
                String oqcundolotserial = OQCdt.Rows(0).get_CellValue("lotserial");
                //是否要刪除額外抽檢SN
                DataTable dtoqcFviCheck = cwa.CallRDT("getopnoforqc");
                if (dtoqcFviCheck.Rowscount() > 0) {
                    if (dtoqcFviCheck.Rows(0).get_CellValue("opno").equals(opno)) {
                        String qcFvichecklotno = lotno;
                        if (qcFvichecklotno.length() == 13) {
                            qcFvichecklotno = qcFvichecklotno.substring(1, 12);
                        }
                        Boolean bl = cwa.CallRB("checkout_submit_88", qcFvichecklotno);
                        if (!bl) {
                            ShowMessage("清除最後一個Q站位額外掃描SN信息失敗，請聯繫MIS");
                            return;
                        }
                    }
                }
                String[] parmer1OQC = {undoopno, oqcundoopnoflowid, oqcundodie, userid, lotno, lotno, oqcundolotserial, lotno, oqcundolotserial, lotno, oqcundolotserial, lotno, oqcundolotserial};
                parmer1 = parmer1OQC;
                tempsql = "begin update T_lotstatus set opno=:opno,opnoflowid=:opnoflowid,dieqty=:dieqty,lotstate='1',userid=:userid,updatedate=sysdate where lotno=:lotno;";
                tempsql += "delete from t_lotlog where lotno=:lotno0 and lotserial>=:oqclotserial;delete from t_lotdefectdata where lotno=:lotno1 and lotserial>=:oqclotserial1;delete from t_wiplotsnlog where lotno=:lotno2 and lotserial>=:oqclotserial2 and ((opno not in(select opno from t_opdata where att2='2')) or (opno in(select opno from t_opdata where att2='2') and qcstate is null));delete from tblwipexpendablelotlog where lotno=:lotno3 and att1>=:oqclotserial3;";
                tempsql += "end;";
                String[] parmer1OQCbacknow = {lotno, oqcundolotserial, lotno, oqcundolotserial, lotno, oqcundolotserial, lotno, oqcundolotserial};
                parmer1OQCback = parmer1OQCbacknow;
                sql_backupqc = "begin insert into t_lotlog_bak select lotserial,lotno,opno,opnoflowid,inputqty,goodqty,setupqty,ngqty,checkindate,checkinuserid,checkoutdate,checkoutuserid,checkoutcompname,checkoutip,'QCR',att2,att3 from t_lotlog where lotno=:lotno0 and lotserial>=:oqclotserial;insert into  t_lotdefectdata_bak select lotserial,lotno,opno,opnodefectno,ngqty,createdate,'QCR',att2,att3 from t_lotdefectdata where lotno=:lotno1 and lotserial>=:oqclotserial1;insert into t_wiplotsnlog_bak select * from t_wiplotsnlog where lotno=:lotno2 and lotserial>=:oqclotserial2 and ((opno not in(select opno from t_opdata where att2='2')) or (opno in(select opno from t_opdata where att2='2') and qcstate is null));insert into tblwipexpendablelotlog_bak select lotno,materialproductno,materiallotno,opno,createtime,locationname,linename,creator,qccheck,att1,att2,att3,att4,att5 from tblwipexpendablelotlog where lotno=:lotno3 and att1>=:oqclotserial3;end;";
            }
            checksqlstatus = false;
            //sql exec 1026 liang
            try {
                if (!checkOQC) {//在更新過站信息之前对良率是否符合要求进行判断
                    if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotcheckeolflag && IsProcessYieltLimitedOpno(opno)) {
                        String lensnow = cwa.CallRS("lensTypeByLot", lotno);
                        String deviceno = cwa.CallRS("getdevicenoBydevicetype", SFCStaticdata.staticmember.deviceno);
                        DataTable dtlimit = cwa.CallRDT("checkout_submit_89", deviceno, opno, lensnow);
                        if (dtlimit.Rowscount() > 0) {
                            Double yieldset = Double.parseDouble(dtlimit.Rows(0).get_CellValue(0));
                            Double yieldcurrent =
                                    Double.parseDouble(String.valueOf(Double.parseDouble(curqty) / Double.parseDouble(loginputqty) * 100));
                            if (yieldcurrent < yieldset) {
                                String showString = "此批號当站位良率过低,是否需要强行过站?(" + yieldcurrent.toString() + "<" + yieldset + ")";
                                if (BaseFuncation.DialogResult.OK == MessageBox(showString, "")) {
                                    SFCStaticdata.staticmember.staticYieldLimitedOpno = false;
                                    CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                            showString, lotno, opno, SFCStaticdata.staticmember.odbname, "5", "8", "39","","1");
                                    if (!SFCStaticdata.staticmember.staticYieldLimitedOpno) {
                                        ShowMessage("良率异常确认信息未正常记录，请确认正確記錄之后再进行出站作业");
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                        } else {
                            ShowMessage(deviceno + "在站位" + opno + "(" + cwa.CallRS("getopname", opno) + ")对应的" + lensnow + "良率下限尚未设定，请联系MIS");
                            return;
                        }
                    } else {
                        if (SFCStaticdata.staticmember.lotcheckeolflag) {
                            //todo miss update webapi ,need update
                            DataTable dtotherdevicelimitcheck = cwa.CallRDT("checkout_submit_172");
                            if (dtotherdevicelimitcheck.Rowscount() > 0) {
                                String lensnow = cwa.CallRS("lensTypeByLot", lotno);
                                String deviceno = cwa.CallRS("getdevicenoBydevicetype", SFCStaticdata.staticmember.deviceno);
                                DataTable dtlimit = cwa.CallRDT("checkout_submit_89", deviceno, opno, lensnow);
                                if (dtlimit.Rowscount() > 0) {
                                    Double yieldset = Double.parseDouble(dtlimit.Rows(0).get_CellValue(0));
                                    Double yieldcurrent = Double.parseDouble(String.valueOf(Double.parseDouble(curqty) / Double.parseDouble(loginputqty) * 100));
                                    if (yieldcurrent < yieldset) {
                                        String showString = "此批號当站位良率过低,請確認是否繼續進行過站作業(" + yieldcurrent.toString() + "<" + yieldset + ")";
                                        if (BaseFuncation.DialogResult.Cancel == MessageBox(showString, "")) {
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (SFCStaticdata.staticmember.IsMesDevice) {
                        String[] mesparlistarray = new String[mesparlist.size()];
                        if (cwa.CallRB("checkout_submit_90", mesinertsql, BaseFuncation.SerializeObjectArrayString(mesparlist.toArray(mesparlistarray)))) {
                        }
                    }
                    checksqlstatus = cwa.CallRB("checkout_submit_90", tempsql, BaseFuncation.SerializeObjectArrayString(parmer1));
                } else {
                    Boolean OQCBACKflag = cwa.CallRB("checkout_submit_90", sql_backupqc, BaseFuncation.SerializeObjectArrayString(parmer1OQCback));
                    if (OQCBACKflag) {
                        checksqlstatus = cwa.CallRB("checkout_submit_90", tempsql, BaseFuncation.SerializeObjectArrayString(parmer1));
                        if (cwa.CallRB("RIandRIAddEOLQCStation", opnotextBoxText)) {
                            DataTable riqrebackalerdt = new DataTable();
                            riqrebackalerdt.AddColumn("Deviceno");
                            riqrebackalerdt.AddColumn("Lotno");
                            riqrebackalerdt.AddColumn("Q退時間");
                            riqrebackalerdt.AddColumn("Q退站位");
                            riqrebackalerdt.AddColumn("Q退次數");
                            riqrebackalerdt.AddColumn("不良sn");
                            riqrebackalerdt.AddColumn("不良項目");
                            riqrebackalerdt.AddColumn("線別");
                            DataTable dt = new DataTable();
                            String lotno1 = lotnotextBoxText.toUpperCase().trim();
                            dt = cwa.CallRDT("checkout_submit_91", lotno1);
                            if (dt.Rowscount() > 0) {
                                for (int jj = 0; jj < dt.Rowscount(); jj++) {
                                    riqrebackalerdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno1, dt.Rows(jj).get_CellValue("btime"), dt.Rows(jj).get_CellValue("opname"), dt.Rows(jj).get_CellValue("qnum"), dt.Rows(jj).get_CellValue("att5"), dt.Rows(jj).get_CellValue("opdefectnamezh"), dt.Rows(jj).get_CellValue("charvalue"));
                                }
                            } else {
                                ShowMessage("查詢Q退信息失敗！");
                                return;
                            }
                            cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(riqrebackalerdt), "EM038", "[預警郵件]LH RI Q退 郵件提醒" + SFCStaticdata.staticmember.deviceno, "0");
                        }
                    } else {
                        ShowMessage("OQC退站失敗！請重新輸入");
                        return;
                    }
                }
            } catch (Exception ex) {
                ShowMessage(ex.getMessage());
                return;
            }
            if (!checksqlstatus) {
                Boolean dellotdefect = false;
                if (Integer.parseInt(transfererp) != 2) {
                    dellotdefect = cwa.CallRB("deldefectwipsn", lotno, opno, "出站失敗，清除資料");
                } else {
                    if (SFCStaticdata.staticmember.IsOQCStationNeedCheckNGDeviceBoolean && SFCStaticdata.staticmember.lotcheckeolflag) {
                        dellotdefect = cwa.CallRB("deldefectwipsnForQC", lotno, opno, "出站失敗，清除資料");
                    }
                }
                if (dellotdefect) {
                    ShowMessage("出站失败，清除資料成功﹐請聯系MIS部門");
                    return;
                } else {
                    ShowMessage("出站失败，清除資料失敗﹐請聯系MIS部門");
                    return;
                }
            } else {
                try {
                    //todo FA webservice ,need new function
//                WebservicesforFA.Service webforFA = new SFConline.WebservicesforFA.Service();
//                webforFA.getandinsertfalotdatafromsfcbylotno(SFCStaticdata.staticmember.odbname, lotnotextBoxText, newdevicenotextBoxText.toString().trim(), opnotextBoxText);
                } catch (Exception ex) {
                }
                //異步解析數據和拋轉FA失效分析系統 成功則成功，不成功則通過另外的方法補解析一下或從新拋轉一下數據
                try {
                    //todo fa class ,need new function
//                CommonAnalyze.CommonanalyzeClass jiexiclass = new SFConline.CommonAnalyze.CommonanalyzeClass();
                    String str_lotno = "";
                    String str_qcflag = "0";
                    if (lotno.startsWith("AK")) {
                        str_lotno = lotno.substring(1, 13);
                    } else {
                        str_lotno = lotno;
                    }
                    DataTable dt_newtable = cwa.CallRDT("checkout_submit_92", opnotextBoxText);
                    if (dt_newtable.Rows(0).get_CellValue("att2").contains("OQC")) {
                        str_qcflag = "1";
                    }
                    if (dt_newtable.Rowscount() > 0) {
                        for (int i = 0; i < dt_newtable.Rowscount(); i++) {
                            //todo:unknow work fa thread
//                        Thread anazyle = new Thread(() => jiexiclass.analyze(str_lotno, SFCStaticdata.staticmember.odbname, dt_newtable.Rows(i).get_CellValue("att2"), SFCStaticdata.staticmember.userid));
//                        anazyle.Start();
                        }
                    }
                } catch (Exception ex) {
                }


               // 線邊倉出站 WZH   N41 8 39
                if (SFCStaticdata.staticmember.updatelotcartflag && SFCStaticdata.staticmember.xbccheckoutdev==true)
                {
                    incomingp = parmer1;
                    incomingsql = tempsql;

                    Boolean xbccheckout = cwa.CallRB("xbccheckout", incomingsql, lotno,incomingp[0],incomingp[1],incomingp[2],incomingp[3],
                            incomingp[4],incomingp[5],incomingp[6],incomingp[7],incomingp[8],incomingp[9],incomingp[13],incomingp[14],
                            incomingp[15],incomingp[16],incomingp[17],incomingp[18],incomingp[19],incomingp[20]);
                    if (!xbccheckout)
                    {
                        ShowMessage("分出的子批過站失敗，請手動過站");
                    }
                }


                ShowMessage("出站成功");
                //加入VTQ往ERS拋去工單  20160312 by lijingjing
                if (devicenotextBoxText.equals("VT-Q") && SFCStaticdata.staticmember.lotfoleolcheckflag && nextopnotextBoxText.equals("INV")) {
                    try {
                        String templotno = lotno;
                        if (templotno.startsWith("PK")) {
                            templotno = templotno.substring(1, templotno.length() - 1);
                        }
                        String[] getlotString = templotno.split("-");
                        templotno = getlotString[0];
                        DataTable lotnobasedatadt = cwa.CallRDT("checkout_submit_93", templotno);
                        String getwono = lotnobasedatadt.Rows(0).get_CellValue(0).trim();
                        //todo send erp data to serve
                        //WebReferenceThrowErs.Service ersdal = new SFConline.WebReferenceThrowErs.Service();
                    //ersdal.getinsertMis(getwono, SFCStaticdata.staticmember.deviceno);
                    } catch (Exception ex) {
                    }
                }

                checkoutsubmit.checkout_submit16(this,cwa,lotno,opno,devicenotextBoxText,opnotextBoxText,dietextBoxText,lotnotextBoxText,setupqty,
                        opnametextBoxText,loginputqty,lotstate,nextopnoflowid,nowprocessno,nextopno,curqty,userid,productno,checkoutpcname,nextopnametextBoxText,
                defectqtytextBoxText,OQCbackchcbChecked,nextopnotextBoxText,wono,reworknocheck);

                setEnabled(submitbutton, true);
                this.Close();

        }
    }

    private void defectqtytextBox_Leave() throws Exception {
        String now_opno = getText(opnotextBox).toString().trim();
        String now_lotno = getText(lotnotextBox).toString().trim();
        String dietextBoxText = getText(dietextBox).toString().trim();
        Boolean checkBox_paoliaoChecked = isChecked(checkBox_paoliao);
        String defectqtytextBoxText = getText(defectqtytextBox).toString().trim();
        String textBox_paoliaoText = getText(textBox_paoliao).toString().trim();
        String tablename = cwa.CallRS("getopatt1", now_opno);
        int testdefectqty = 0;
        Boolean testflag = false;
        if (tablename != null && !tablename.equals("")) {
            testflag = true;
            DataTable testdt = new DataTable();
            testdt = cwa.CallRDT("gettestdatacount", tablename, now_lotno);
            String testin = testdt.Rows(0).get_CellValue("inputqty").trim();
            String now_inputqty = dietextBoxText.trim();
            try {
                int now_qty = Integer.parseInt(now_inputqty);
                int testqty = Integer.parseInt(testin);
                if (testqty >= now_qty) {
                    testdefectqty = Integer.parseInt(testdt.Rows(0).get_CellValue("ngqty").trim());
                } else {
                    ShowMessage("批號：【" + now_lotno + "】只測試了" + testqty + "pcs，請測試完成再進行過站");
                    return;
                }
            } catch (Exception ex) {
                ShowMessage(ex.getMessage());
                return;
            }
        }
        int defectqty = 0;
        try {
            if (checkBox_paoliaoChecked) {
                defectqty = Integer.parseInt(defectqtytextBoxText) - Integer.parseInt(textBox_paoliaoText);
            } else {
                defectqty = Integer.parseInt(defectqtytextBoxText);
            }
        } catch (Exception ex) {
            ShowMessage("請在不良項目中正確輸入數字");
            setText(defectqtytextBox, "0");
            setFocusable(defectqtytextBox, true);
            return;
        }
        if (defectqty > 0) {
            SFCStaticdata.staticmember.errorqtydata = null;
            String Scanflag = cwa.CallRS("getopatt2", now_opno);
            if (Scanflag == null || Scanflag.equals("")) {
                ShowMessage("站位特性尚未设定（att2）");
                return;
            }
            if ((SFCStaticdata.staticmember.englotcheckflag) || (SFCStaticdata.staticmember.engRilotcheckflag)) {
                if (SFCStaticdata.staticmember.engmfglotcheckflag) {
                    if (SFCStaticdata.staticmember.lotcheckeolflag) {
                        DataTable dt_selectdeviceno = cwa.CallRDT("checkout_dbl_1", SFCStaticdata.staticmember.deviceno.trim());
                        if (dt_selectdeviceno.Rowscount() > 0) {
                            DataTable checkopnodt = cwa.CallRDT("checkout_dbl_2", now_opno);
                            if (checkopnodt == null) {
                                ShowMessage("查詢當前RI-B站位是否在Laser站位之後出錯，請聯繫MIS");
                                return;
                            } else if (checkopnodt.Rowscount() > 0)// Laser站位之後的站位
                            {
                                String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                                //todo create new class open no fol return
//                                ScanForm sf = new ScanForm(now_lotno, now_opno, String.valueOf(defectqty), lotserial, staticproductno, Scanflag);
//                                sf.ShowDialog();
                            } else if (checkopnodt.Rowscount() <= 0) {
                                CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                                        now_lotno, now_opno, String.valueOf(defectqty));
                            }
                        } else {
                            String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                            //todo create new class open no fol return
//                            ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                            sf.ShowDialog();
                        }
                    } else {
//                        defectinput di = new defectinput(lotnotextBox.Text.trim(), now_opno, defectqty);
//                        di.ShowDialog();
                        CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                                now_lotno, now_opno, String.valueOf(defectqty));
                    }
                } else {
                    if (SFCStaticdata.staticmember.lotcheckeolflag) {
                        DataTable dt_selectdeviceno = cwa.CallRDT("checkout_dbl_3", SFCStaticdata.staticmember.deviceno.trim());

                        if (dt_selectdeviceno.Rowscount() > 0) {
                            DataTable checkopnodt = cwa.CallRDT("checkout_dbl_2", now_opno);
                            if (checkopnodt == null) {
                                ShowMessage("查詢當前RI-B站位是否在Laser站位之後出錯，請聯繫MIS");
                                return;
                            } else if (checkopnodt.Rowscount() > 0) {
                                String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                                //todo create new class open no fol return
//                                ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                                sf.ShowDialog();
                            } else if (checkopnodt.Rowscount() <= 0) {

//                                defectinput di = new defectinput(lotnotextBox.Text.trim(), now_opno, defectqty);
//                                di.ShowDialog();
                                CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                                        now_lotno, now_opno, String.valueOf(defectqty));
                            }
                        } else {
                            String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                            //todo create new class open no fol return
//                            ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                            sf.ShowDialog();
                        }
                    } else {
                        CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                                now_lotno, now_opno, String.valueOf(defectqty));
                    }
                }
            } else if ((!SFCStaticdata.staticmember.englotcheckflag) && (!SFCStaticdata.staticmember.engRilotcheckflag)) {
                DataTable dtdeviceno = getScanNGBarcodeDevice(SFCStaticdata.staticmember.deviceno);
                if (dtdeviceno == null) {
                    ShowMessage("檢測該機種打不良是否需要進行單顆掃描動作異常，請聯繫MIS");
                    return;
                } else if (dtdeviceno.Rowscount() > 0) {
                    if (SFCStaticdata.staticmember.lotcheckeolflag) {
                        String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                        // todo create new class open function no fol return
//                        ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                        sf.ShowDialog();
                    } else {
                        CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                                now_lotno, now_opno, String.valueOf(defectqty));
                    }
                } else if (SFCStaticdata.staticmember.fqcflag) {
//                    defectinput di = new defectinput(lotnotextBox.Text.trim(), now_opno, defectqty);
//                    di.ShowDialog();
                    CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                            now_lotno, now_opno, String.valueOf(defectqty));
                } else {
                    if ((Scanflag.equals("105") || Scanflag.equals("107") || Scanflag.equals("108") && SFCStaticdata.staticmember.devicenomacinfo)) {
                        String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                        //todo create new class open no fol return
//                        ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                        sf.ShowDialog();
                    } else if (Scanflag.equals("1")) {
                        String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                        //todo create new class open function no fol return
//                    ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                    sf.ShowDialog();
                    } else if (Scanflag.equals("2")) {
                        String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                        //todo create new class open function no fol return
//                    ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                    sf.ShowDialog();
                    } else if (Scanflag.equals("6")) {
                        String lotserial = now_lotno + "-" + BaseFuncation.padLeft(String.valueOf(staticopnoflowid), 3, '0');
                        //todo create new class open function no fol return
//                    ScanForm sf = new ScanForm(lotnotextBoxText, now_opno, defectqty.toString(), lotserial, staticproductno, Scanflag);
//                    sf.ShowDialog();
                    } else {
                        CreatNewActivity(defectinput.class, _deviceno, _newdeviceno, _odbname,
                                now_lotno, now_opno, String.valueOf(defectqty));
                    }
                }
            }
            loaderrordatagvdata(SFCStaticdata.staticmember.errorqtydata);
        } else {
            SFCStaticdata.staticmember.errorqtydata = null;
        }

    }

    private void loaderrordatagvdata(List<SFCStaticdata.defectdata> dt) throws Exception {
        View v;
        if(dt==null || dt.isEmpty())
        {
            return;
        }

        removeAllViewsInLayout(errordataGridView);
            for (SFCStaticdata.defectdata d : dt
                    ) {
                v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_checkout, null);
                setText((TextView) v.findViewById(R.id.listviewcheckoutmname1), d.errorcode);
                setText((TextView) v.findViewById(R.id.listviewcheckoutmname2), d.errorname);
                setText((TextView) v.findViewById(R.id.listviewcheckoutmname3), String.valueOf(d.errorqty));
                addView(errordataGridView, v);
            }

    }

    public DataTable getScanNGBarcodeDevice(String deviceno) throws Exception {
        return cwa.CallRDT("check_getScanNGBarcodeDevice_1", deviceno);
    }

    private void OQCbackchcb_CheckedChanged() throws Exception {
        Boolean OQCbackchcbChecked = isChecked(OQCbackchcb);
        if (OQCbackchcbChecked) {
            setEnabled(OQCundoOPNOcb, true);
            SFCStaticdata.staticmember.IsQCBackStationSFConlineFlag = true;
            SFCStaticdata.staticmember.OQCNGCheckinFVI = false;
        } else {
            setEnabled(OQCundoOPNOcb, false);
            SFCStaticdata.staticmember.IsQCBackStationSFConlineFlag = false;
            SFCStaticdata.staticmember.OQCNGCheckinFVI = true;
        }
    }

    private void OQCundoOPNOcb_SelectedIndexChanged() throws Exception {
        FinalStaticCloass.SpinnerData soqcline=(FinalStaticCloass.SpinnerData)getSelectedItem(OQCundoOPNOcb);
        undoopno =soqcline!=null?soqcline.getValue():"";
        //undoopno = ((FinalStaticCloass.SpinnerData) getSelectedItem(OQCundoOPNOcb)).getValue();
    }

    public void firstdbinputsubmit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    label10_Click();
                }
            }, "firstdbinputsubmit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "firstdbinputsubmit Task  Error:" + ex.getMessage());
        }
    }

    private void label10_Click() throws Exception {
        CreatNewActivity(dbdefectinput.class, _deviceno, _newdeviceno, _odbname,
                _lotno, _opno, _dieqty, String.valueOf(staticopnoflowid));
    }

    private void checkBox_db_CheckedChanged() throws Exception {
        Boolean checkBox_dbChecked = isChecked(checkBoxdb);
        if (checkBox_dbChecked) {
            setEnabled(dbbutton, true);
        } else {
            setEnabled(dbbutton, false);
        }
    }

    private void checkBox_yieldcor_CheckedChanged() throws Exception {
        Boolean checkBox_yieldcorChecked = isChecked(checkBox_yieldcor);
        if (checkBox_yieldcorChecked) {
            String lotno = getText(lotnotextBox).toString().trim();
            String opno = getText(opnotextBox).toString().trim();
            CreatNewActivity(ControlLotnoPage.class, _deviceno, _newdeviceno, _odbname, lotno, opno);
        }
    }

    private void checkBox_pod_CheckedChanged() throws Exception {
        Boolean checkBox_podChecked = isChecked(checkBox_pod);
        Boolean radioButton_allChecked = isChecked(radiobutton_all);
        Boolean radioButton_partChecked = isChecked(radiobutton_part);
        String dieqty = getText(dietextBox).toString().trim();
        if (checkBox_podChecked) {
            //setEnabled(radiobutton_all,true);
            //setEnabled(radiobutton_part,true);
            if (radioButton_allChecked) {
                CreatNewActivity(Podtestpage.class, _deviceno, _newdeviceno, _odbname,_lotno, _opno, dieqty, "1");
            } else if (radioButton_partChecked) {
                CreatNewActivity(Podtestpage.class, _deviceno, _newdeviceno, _odbname, _lotno, _opno, dieqty, "0");
            }
        } else {
            ShowMessage("如若要填寫POD信息，請勾選'POD測試'");
            return;
        }
    }

    public void button_defectinfosubmit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button_defectinfo_Click();
                }
            }, "button_defectinfosubmit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "button_defectinfosubmit Task Error:" + ex.getMessage());
        }
    }
    private void button_defectinfo_Click() throws Exception {
        defectqtytextBox_Leave();
    }

    private void checkBox_paoliao_CheckedChanged() throws Exception {
        Boolean checkBox_paoliaoChecked = isChecked(checkBox_paoliao);
        if (checkBox_paoliaoChecked) {
            setEnabled(textBox_paoliao, true);
            setEnabled(button_addpaoliao, true);
        } else {
            setEnabled(textBox_paoliao, false);
            setEnabled(button_addpaoliao, true);
        }
    }

    private void checkBox1_CheckedChanged() throws Exception {
        Boolean checkBox1Checked = isChecked(checkBox1);
        String lotnotextBoxText = getText(lotnotextBox).toString().trim();
        String opnotextBoxText = getText(opnotextBox).toString().trim();
        if (checkBox1Checked) {
            String eolfolflag = "2";
            if (SFCStaticdata.staticmember.lotcheckeolflag) {
                eolfolflag = "1";
            }
            if (SFCStaticdata.staticmember.lotfolbingpicheck) {
                eolfolflag = "0";
            }
            if (eolfolflag.equals("2")) {
                setChecked(checkBox1, false);
                checkBox1Checked = false;
                ShowMessage("前后段标志位出现异常，请直接联系MIS");
                return;
            }
            SFCStaticdata.staticmember.lotqcspecialinfo = false;
            //todo create new class open no fol return
//            qcspecialinfo qcspecialpage = new qcspecialinfo(lotnotextBoxText, opnotextBoxText, dietextBoxText.trim(), SFCStaticdata.staticmember.userid, opnotextBoxText, eolfolflag);
//            qcspecialpage.ShowDialog();
            //检测对错放在出站中
            if (!SFCStaticdata.staticmember.lotqcspecialinfo) {
                ShowMessage("收集QC特殊標記失敗！");
                checkBox1Checked = false;
                this.Close();
                return;
            }
        } else {
            Boolean flagdel = false;
            try {
                flagdel = cwa.CallRB("check_checkbox1checked", lotnotextBoxText, opnotextBoxText);
            } catch (Exception ex) {
                flagdel = false;
            }
            if (!flagdel) {
                ShowMessage("删除QC特殊记录失败！请联系MIS！");
                return;
            }
        }
    }

    private void Checkout_FormClosing() throws Exception {
        String lotno = getText(lotnotextBox).toString().trim();
        if (SFCStaticdata.staticmember.updatelotuserflag) {
            Boolean inslotstate = cwa.CallRB("updateuserid", lotno);
            if (!inslotstate) {
                ShowMessage("批號佔用狀態失敗，請聯繫MIS!");
                this.Close();
                return;
            }
        }
    }

    private void Checkout_Load() throws Exception {
        setEnabled(submitbutton, true);
        String lotno = getText(lotnotextBox).toString().trim();
        DataTable dt = cwa.CallRDT("getuseridinfo", lotno);
        SFCStaticdata.staticmember.updatelotuserflag = false;
        String HDSerialNo=SFCStaticdata.staticmember.HDSerialNo;
        if (dt != null) {
            if (dt.Rowscount() > 0) {
                String lotstate = dt.Rows(0).get_CellValue("userstate").trim();
                if (lotstate.equals("1")) {
                    if (dt.Rows(0).get_CellValue("ipaddress").equals(HDSerialNo)) {
                        SFCStaticdata.staticmember.updatelotuserflag = true;
                        return;
                    } else {
                        ShowMessage("批號已經在占用操作中，請查詢目標機台！" + dt.Rows(0).get_CellValue("ipaddress"));
                        Boolean insdbflag = cwa.CallRB("errorinfobackdb", lotno, "批號已經在占用操作中", dt.Rows(0).get_CellValue("ipaddress"), SFCStaticdata.staticmember.userid, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.odbname, "32", "");
                        SFCStaticdata.staticmember.updatelotuserflag = false;
                        this.Close();
                        return;
                    }
                } else if (lotstate.equals("0")) {
                    Boolean uplotlogon = cwa.CallRB("updateuserlogoinfo", lotno,HDSerialNo);
                    if (uplotlogon) {
                        SFCStaticdata.staticmember.updatelotuserflag = true;
                        return;
                    } else {
                        SFCStaticdata.staticmember.updatelotuserflag = false;
                        return;
                    }
                }
            } else {
                Boolean inslotstate = cwa.CallRB("checkuseridinfo", lotno,HDSerialNo);
                if (!inslotstate) {
                    ShowMessage("批號佔用狀態更改失敗，請聯繫MIS!");
                    SFCStaticdata.staticmember.updatelotuserflag = false;
                    this.Close();
                    return;
                } else {
                    SFCStaticdata.staticmember.updatelotuserflag = true;
                    return;
                }
            }
        }
    }

    public void finish() {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Checkout_FormClosing();
                }
            }, "Check Out Closeing Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Check Out Closeing Task Error:" + ex.getMessage());
        }
        super.finish();
    }

    public DataTable returnCheckOpnoData(String opnonow) throws Exception {
        return cwa.CallRDT("check_returnCheckOpnoData", opnonow);
    }

    public Boolean IsProcessYieltLimitedOpno(String opno) throws Exception {
        Boolean bl = false;
        DataTable dtopno = cwa.CallRDT("check_IsProcessYieltLimitedOpno", opno);
        if (dtopno.Rowscount() > 0) {
            bl = true;
        }
        return bl;
    }

    private void checkBox_folcheck_CheckedChanged() throws Exception {
        Boolean checkBox_folcheckChecked = isChecked(checkBox_folcheck);
        if (checkBox_folcheckChecked) {
            //todo create new class open function
//            FOLNoPaper.FOLCheckPage fcp = new SFConline.FOLNoPaper.FOLCheckPage(lotnotextBoxText, opnotextBoxText, dietextBoxText.trim());
//            fcp.ShowDialog();
        }
    }

    public Boolean returnFolCheckOpno(String opno) throws Exception {
        Boolean returnBoolean = false;
        DataTable dt = cwa.CallRDT("check_returnFolCheckOpno", opno);
        if (dt == null) {
            ShowMessage("查詢當前站位是否需要進行抽檢異常，請聯繫MIS");
            returnBoolean = false;
        } else if (dt.Rowscount() > 0) {
            returnBoolean = true;
        } else {
            returnBoolean = false;
        }
        return returnBoolean;
    }

    public Boolean checkLabelQty(String lotno) throws Exception {
        Boolean returnBoolean = false;
        String qty = cwa.CallRS("check_checkLabelQty_2", lotno);
        if (lotno.substring(0, 2).equals("AK"))
            lotno = lotno.substring(1, (lotno.length() - 1));
        String[] par1 = {lotno};
        String cqty = cwa.CallRS("check_checkLabelQty_1", lotno);

        if (qty.equals("") || qty == null) {
            return false;
        }
        if (cqty.equals(qty)) {
            return true;
        } else
            return returnBoolean;
    }

    public Boolean checkLabelReworkQty(String lotno) throws Exception {
        Boolean returnBoolean = false;
        if (lotno.substring(0, 2).equals("AK"))
            lotno = lotno.substring(1, (lotno.length() - 1));
        String[] par = {lotno};
        String[] par1 = null;
        if (lotno.startsWith("K")) {
            String[] parmertemp = {"A" + lotno};
            par1 = parmertemp;
        } else {
            String[] parmertemp = {lotno};
            par1 = parmertemp;
        }
        String cqty = cwa.CallRS("check_checkLabelQty_3", lotno);
        String qty = cwa.CallRS("check_checkLabelQty_4", lotno);
        String ngqty = cwa.CallRS("check_checkLabelQty_5", lotno);
        if (qty.equals("") || qty == null) {
            returnBoolean = false;
        }
        if (cqty.equals("") || cqty == null) {
            returnBoolean = false;
        }
        if (Integer.parseInt(cqty) - Integer.parseInt(ngqty) == Integer.parseInt(qty)) {
            returnBoolean = true;
        } else {
            returnBoolean = false;
        }
        return returnBoolean;
    }

    private Boolean judgemes(String lotno, String opno, String db) throws Exception {
        Boolean yesinert = false;
        DataTable dt = cwa.CallRDT("check_judgemes_1", lotno, opno);
        if (dt.Rowscount() == 0) {
            yesinert = true;
            return yesinert;
        } else if (dt.Rowscount() == 2) {
            yesinert = false;
            return yesinert;
        }
        DataTable dtqc = cwa.CallRDT("check_judgemes_2", lotno, opno);
        if (dtqc.Rowscount() > 0) {
            yesinert = true;
            return yesinert;
        }
        return yesinert;
    }

    private Boolean getmagazineflag(String opno) throws Exception {
        Boolean flag = false;
        String att2 = cwa.CallRS("check_getmagazineflag", opno);
        if (att2.equals("1")) {
            flag = true;
        }
        return flag;
    }

    private Boolean getmagazinelotflag(String lotno, String opno) throws Exception {
        Boolean flag = false;
        String item = cwa.CallRS("check_getmagazinelotflag", lotno, opno);
        if (item.equals(lotno)) {
            flag = true;
        }
        return flag;
    }

    private void opnametextBox_TextChanged() {
    }

    public void magazinesubmit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    button2_Click();
                }
            }, "magazinesubmit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "magazinesubmit Task Error:" + ex.getMessage());
        }
    }

    private void button2_Click() throws Exception {
        String dietextBoxText = getText(dietextBox).toString().trim();
        int qty = Integer.parseInt(dietextBoxText) / 400;
        if (Integer.parseInt(dietextBoxText) % 400 != 0) {
            qty++;
        }
        String mzqtyok = cwa.CallRS("check_button2_Click_1");

        String mzqty = cwa.CallRS("check_button2_click_2");
        if (Integer.parseInt(mzqty) < 1) {
            if (Integer.parseInt(mzqtyok) < 1) {
                ShowMessage("該批號進站時未綁定彈夾");
                return;
            } else if (Integer.parseInt(mzqtyok) == qty) {
                ShowMessage("該批號彈夾已解绑");
                magazinelot = true;
                return;
            }
        }
        CreatNewActivity(magazineform.class, _deviceno, _newdeviceno, _odbname,
                _lotno, _opno, String.valueOf(qty), "2");
        if (SFCStaticdata.staticmember.magazineformflag) {
            magazinelot = true;
        }
    }

    public void endlotsubmit(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    label19_Click();
                }
            }, "endlotsubmit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "endlotsubmit Task Error:" + ex.getMessage());
        }
    }

    private void label19_Click() throws Exception {
        //todo create new class open function no fol return
//        weishulotno ws = new weishulotno(lotnotextBoxText, dietextBoxText.trim(), opnotextBoxText, wsdt);
//        ws.ShowDialog();
        Boolean weishulotupdateflag = false;
        if (weishulotupdateflag) {
            setEnabled(OQCundoOPNOcb, false);
            setText(endlotbt, "記錄ok");
        }
    }

    private void checkBox_dblotno_CheckedChanged() throws Exception {
        Boolean checkBox_dblotnoChecked = isChecked(checkBox_dblotno);
        String opnotextBoxText = getText(opnotextBox).toString().trim();
        if (checkBox_dblotnoChecked) {
            try {
                wsdt = cwa.CallRDT("check_checkBox_dblotno_CheckedChanged", opnotextBoxText);
            } catch (Exception ex) {
                ShowMessage("查詢尾數批記錄項目時發生錯誤");
            }
            if (wsdt.Rowscount() > 0) {
                label19_Click();
            }
        }
    }
    private String getqcholdopno(String type, String nextopno, String nowopno) throws Exception {
        String opno = "";
        DataTable dt = new DataTable();
        dt = cwa.CallRDT("check_getqcholdopno", type, nowopno);
        if (dt == null) {
            opno = "";
            return opno;
        }
        if (dt.Rowscount() > 0) {
            opno = dt.Rows(0).get_CellValue("att1").trim();
        } else {
            opno = "";
        }
        return opno;
    }
}


