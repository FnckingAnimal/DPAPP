package app.dpapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.DataTable.DataTable;
import app.dpapp.R;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.FinalStaticCloass;
import app.dpapp.appcdl.NetUtils;
import app.dpapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.dpapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.appcdl.SFCBLLPack.checkoutsubmit;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class checkin extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotno;
    private String _dieqty;
    private String _opno;

    private TextView lotnotextBox;
    private TextView dietextBox;
    private TextView newdevicenotextBox;
    private TextView devicenotextBox;
    private TextView opnotextBox;
    private TextView opnametextBox;

    private LinearLayout bindingdataGridView;
    private EditText textBoxqcreturn;
    private TextView text_qcreturn;
    private CheckBox checkBoxline;
    private Spinner comboBoxline;
    private CheckBox checkBox_dblotno;
    private EditText textBox_dbqty;
    private EditText textBox_follot;
    private TextView text_follot;
    private TextView txt_rule;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private EditText txt_moxuehao;
    private TextView tv_moxuehao;
    private Spinner comboBoxbuild;
    private TextView textbuild;
    private Spinner ComboBox_waferlotno;
    private TextView text_waferlotno;
    private EditText textBox_fc;
    private CheckBox recordflag;

    private String ipstatic;
    private String HDSerialNo;

    private  View v;
    int staticopnoflowid;
    String staticproductno;
    DataTable multiMachinenodt = new DataTable();
    String guigestr = "";
    Boolean muxuehao = false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_checkin);

            ipstatic = NetUtils.getLocalIPAddress(this);
            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            lotnotextBox = (TextView) findViewById(R.id.isfc_checnkin_lotnotbv1);
            dietextBox = (TextView) findViewById(R.id.isfc_checnkin_dieqtyv1);
            newdevicenotextBox = (TextView) findViewById(R.id.isfc_checnkin_newdevicev1);
            devicenotextBox = (TextView) findViewById(R.id.isfc_checnkin_devicev1);

            opnotextBox = (TextView) findViewById(R.id.isfc_checnkin_opnotbv1);
            opnametextBox = (TextView) findViewById(R.id.isfc_checnkin_opnamev1);

            bindingdataGridView = (LinearLayout) findViewById(R.id.isfc_checnkin_stationviewll1);
            textBoxqcreturn = (EditText) findViewById(R.id.isfc_checnkin_qcreturnet1);
            text_qcreturn = (TextView) findViewById(R.id.isfc_checnkin_qcreturntl1);
            checkBoxline = (CheckBox) findViewById(R.id.isfc_checnkin_linecb1);
            comboBoxline = (Spinner) findViewById(R.id.isfc_checnkin_linesp1);
            checkBox_dblotno = (CheckBox) findViewById(R.id.isfc_checnkin_dblotnocb1);
            textBox_dbqty = (EditText) findViewById(R.id.isfc_checnkin_dbqtyet1);
            textBox_follot = (EditText) findViewById(R.id.isfc_checnkin_follotnoet1);
            text_follot = (TextView) findViewById(R.id.isfc_checnkin_follotnotl1);
            checkBox1 = (CheckBox) findViewById(R.id.isfc_checnkin_rulecb1);
            checkBox2 = (CheckBox) findViewById(R.id.isfc_checnkin_rulecb2);
            txt_rule = (TextView) findViewById(R.id.isfc_checnkin_ruletl1);

            tv_moxuehao = (TextView) findViewById(R.id.isfc_checnkin_moxuetl1);
            txt_moxuehao = (EditText) findViewById(R.id.isfc_checnkin_moxueet1);
            comboBoxbuild = (Spinner) findViewById(R.id.isfc_checnkin_buildsp1);
            textbuild = (TextView) findViewById(R.id.isfc_checnkin_buildtl1);
            ComboBox_waferlotno = (Spinner) findViewById(R.id.isfc_checnkin_waferlonosp1);
            text_waferlotno = (TextView) findViewById(R.id.isfc_checnkin_waferlonotl1);
            textBox_fc = (EditText) findViewById(R.id.isfc_checnkin_fcet1);
            recordflag = (CheckBox) findViewById(R.id.isfc_checnkin_fccb1);

            Bundle b = this.getIntent().getExtras();
            _deviceno = SFCStaticdata.staticmember.deviceno;//b.getString("deviceno");
            _newdeviceno =SFCStaticdata.staticmember.newdeviceno;// b.getString("newdeviceno");
            _odbname =SFCStaticdata.staticmember.odbname;// b.getString("odbname");
            _lotno = b.getString("lotno");
            _dieqty = b.getString("dieqty");
            _opno = b.getString("opno");
            staticproductno =  b.getString("productno");
            staticopnoflowid=Integer.parseInt(b.getString("sendopflowid"));

            v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_checkin, null);

            SFCStaticdata.staticmember.checkinqty = _dieqty;

            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Checkin();
                    Checkin_Load();
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");

            checkBox_dblotno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        ExecTask(new SFCTaskVoidInterface() {
                            @Override
                            public void taskvoid(Object valueo) throws Exception {
                                checkBox_dblotno_CheckedChanged();
                            }
                        }, "Check DB Task");
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkin.this, "Check DB Task Error:" + ex.getMessage());
                    }
                }
            });

            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        ExecTask(new SFCTaskVoidInterface() {
                            @Override
                            public void taskvoid(Object valueo) throws Exception {
                                checkBox1_CheckedChanged();
                            }
                        }, "Rule A Task");
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkin.this, "Rule A Task Error:" + ex.getMessage());
                    }
                }
            });

            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        ExecTask(new SFCTaskVoidInterface() {
                            @Override
                            public void taskvoid(Object valueo) throws Exception {
                                checkBox2_CheckedChanged();
                            }
                        }, "Rule B Task");
                    } catch (Exception ex) {
                        BaseFuncation.showmessage(checkin.this, "Rule B Task Error:" + ex.getMessage());
                    }
                }
            });

            textBox_follot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (_loadingstatus && !hasFocus) {
                        try {
                            ExecTask(new SFCTaskVoidInterface() {
                                @Override
                                public void taskvoid(Object valueo) throws Exception {
                                    textBox_follot_Leave();
                                }
                            }, "FOL Lot Input Task");
                        } catch (Exception ex) {
                            BaseFuncation.showmessage(checkin.this, "FOL Lot Input Task Error:" + ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            this.finish();
        }
    }

    @Override
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

    public void finish() {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    Checkin_FormClosing();
                }
            }, "Check In Closeing Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "Check In Closeing Task Error:" + ex.getMessage());
        }
        super.finish();
    }

    public DataTable isMultiMachineSelectStation(String opnoselect) throws Exception {
        return cwa.CallRDT("checkin_isMultiMachineSelectStation", opnoselect);
    }

    private Boolean tempisexist(List<String> str, String searchstr) {
        String[] arry = new String[str.size()];
        str.toArray(arry);
        for (String ta : arry) {
            if (ta.equals(searchstr))
                return true;
        }
        return false;
    }

    public void Checkin() throws Exception {
        setText(devicenotextBox, _deviceno);
        setText(newdevicenotextBox, _newdeviceno);
        setText(lotnotextBox, _lotno);
        setText(dietextBox, _dieqty);
        setText(opnotextBox, _opno);
        setText(opnametextBox, cwa.CallRS("getopname", _opno));
        selectopno(_opno, _lotno);
        setEnabled(checkBox_dblotno, false);
        //設定FC站位標注框顯示
        DataTable dt_fc = cwa.CallRDT("checkin_main_1", _opno);
        if (dt_fc.Rowscount() > 0) {
            setEnabled(textBox_fc, true);
            setVisibility(textBox_fc, View.VISIBLE);
            setEnabled(recordflag, true);
            setVisibility(recordflag, View.VISIBLE);

        }
        // 設定 DB 選項框
        String OQCflag = cwa.CallRS("getopatt2", _opno);
        String dbflag = cwa.CallRS("getopatt7", _opno);
        DataTable dtguige = cwa.CallRDT("checkin_main_2", _opno);
        if (dtguige != null) {
            if (dtguige.Rowscount() > 0) {
                setEnabled(checkBox1, true);
                setEnabled(checkBox2, true);

                setVisibility(checkBox1, View.VISIBLE);
                setVisibility(checkBox2, View.VISIBLE);
            }
        }

        DataTable build = cwa.CallRDT("checkin_main_3", _opno);
        if (build != null) {
            if (build.Rowscount() > 0) {
                setclickable(comboBoxbuild, true);
                setVisibility(comboBoxbuild,View.GONE);
                setVisibility(textbuild, View.GONE);
                setAdapter(comboBoxbuild,
                        BaseFuncation.setvalue(
                                build,
                                null,
                                "att1",
                                checkin.this
                        )
                );
                setSelection(comboBoxbuild, 0);
            }
        }

        if (dbflag.equals("4")) {
            setEnabled(checkBox_dblotno, true);
        }
        if (OQCflag == null || OQCflag.equals("")) {
            ShowMessage("站位特性尚未设定（att2）");
            return;
        }
        switch (OQCflag) {
            case "2":
                String qcsum = getlotqclog(_lotno, _opno);   // lotcheckqty  抽测数目
                qcsum = String.valueOf(Integer.parseInt(qcsum) + 1);
                setText(textBoxqcreturn, qcsum);
                setVisibility(textBoxqcreturn, View.VISIBLE);
                setVisibility(text_qcreturn, View.VISIBLE);
                break;
            case "13":
                SFCStaticdata.staticmember.lotoutchecktimeflag = true;
                break;
            case "14":
                SFCStaticdata.staticmember.lotacfchecklasertime = true;
                break;
            default:
                break;
        }

        //J系列一條龍
        Boolean specj = false;
        DataTable dtjj = cwa.CallRDT("checkin_main_4", SFCStaticdata.staticmember.deviceno);
        if (dtjj.Rowscount() > 0) {
            specj = true;
        } else if (_lotno.startsWith("AK")) {
            specj = true;
        }

        if ((SFCStaticdata.staticmember.devicenomacinfo && SFCStaticdata.staticmember.deviceno.startsWith("J")) && SFCStaticdata.staticmember.lotcheckeolflag && Integer.parseInt(getprocessflowid(_opno)) == 1)//mac eol firstopno
        {
            setEnabled(textBox_follot, true);
            setVisibility(textBox_follot, View.VISIBLE);
            setVisibility(text_follot, View.VISIBLE);
            if (specj)
                setEnabled(textBox_follot, false);
            setVisibility(textBox_follot, View.INVISIBLE);
            setVisibility(text_follot, View.INVISIBLE);
        } else {
            setEnabled(textBox_follot, false);
            setVisibility(textBox_follot, View.INVISIBLE);
            setVisibility(text_follot, View.INVISIBLE);
        }

        // 需要輸入模穴號的站位  WZH 各機種 8 37
        muxuehao = cwa.CallRB("MOXUE_OPNO",_opno);
        if (muxuehao)
        {   //groupBox6.Visible = true;
            setVisibility(tv_moxuehao, View.VISIBLE);
            setVisibility(txt_moxuehao, View.VISIBLE);
            setEnabled(tv_moxuehao, true);
            setEnabled(txt_moxuehao, true);
        }

    }

    private String getlotqclog(String lotno, String opno) throws Exception {
        String qcsum = "0";
        try {
            qcsum = cwa.CallRS("checkin_getlotqclog", lotno, opno);
        } catch (Exception ex) {
            ShowMessage("查询批号Q退次数时发生错误");
        }
        return qcsum;
    }

    //  ( String lotno, String db, String opstr)     getopatt2( String opno)
    private void lotnobind(String lotno, String db, String op, String qty, String opno) throws Exception {
        String dtlot = cwa.CallRS("getopatt2", opno);
        if (dtlot == null || dtlot.equals("")) {
            ShowMessage("站位特性尚未设定（att2）");
            return;
        }
        if (Integer.parseInt(dtlot) == 5)      //    查詢  att2 ==5  綁定批號站位
        {
            //todo: Creatnewclass,no fol page return
                /*LotNewOldBind lotbind = new LotNewOldBind(lotno, db, op, qty);
                lotbind.ShowDialog();*/
            ShowMessage("MIS no develop");
            return;
        }
    }

    public void submitbt(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    submitbutton_Click();
                }
            }, "CheckIn Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "CheckIn Submit Task Error:" + ex.getMessage());
        }
    }

    private void submitbutton_Click() throws Exception {
        String tempsql = null;
        Boolean checksqlstatus = false;
        String lotno = getText(lotnotextBox).toString().trim().toUpperCase();

        String productno = staticproductno;
        String opno = getText(opnotextBox).toString().trim().toUpperCase();
        String opnoflowid = String.valueOf(staticopnoflowid);
        String dieqty = getText(dietextBox).toString().trim().toUpperCase();
        String lotstate = "2";
        String userid = SFCStaticdata.staticmember.userid;
        String att1 = null;
        String att2 = null;
        String att3 = null;
        String ip = SFCStaticdata.staticmember.HDSerialNo; //SFCStaticdata.staticmember.ip;
        String textBox_fcText = getText(textBox_fc).toString().trim().toUpperCase();

        Boolean groupBox4Visible = getEnabled(checkBox1);
        Boolean groupBox5Visible =getclickable(comboBoxbuild); //getEnabled(comboBoxbuild);
        FinalStaticCloass.SpinnerData ssbuild=(FinalStaticCloass.SpinnerData)getSelectedItem(comboBoxbuild);
        //ss!=null?ss.getText():"";
        String comboBoxbuildText =ssbuild!=null?ssbuild.getText():"";
        Boolean recordflagChecked = isChecked(recordflag);

        Boolean textBox_follotEnabled = getEnabled(textBox_follot);
        String textBox_follotText = getText(textBox_follot).toString().trim().toUpperCase();
        Boolean checkBox_dblotnoChecked = isChecked(checkBox_dblotno);
        String textBox_dbqtyText = getText(textBox_dbqty).toString().trim().toUpperCase();
        Boolean checkBoxlineChecked = isChecked(checkBoxline);
        String opnametextBoxText = getText(opnametextBox).toString().trim().toUpperCase();

        String moxuehao=getText(txt_moxuehao).toString().trim();
        //todo paramers setting
        int bindingdataGridViewRowscount = getChildCount(bindingdataGridView);
        FinalStaticCloass.SpinnerData sswaferlotno=(FinalStaticCloass.SpinnerData) getSelectedItem(ComboBox_waferlotno);
        String comboBox_waferlotniText =sswaferlotno!=null?sswaferlotno.getText().trim().toUpperCase():"";
        String dietextBoxText = getText(dietextBox).toString().trim().toUpperCase();

        //進站彈框
        DataTable at = cwa.CallRDT("getmessagedata", opno,"1");
        if(at!= null && !"".equals(at)) {
            if(at.Rowscount() > 0) {
                String strmesage=at.Rows(0).get_CellValue(0).toString();
                if (BaseFuncation.DialogResult.OK == MessageBox(strmesage, "單擊繼續")) {

                } else {
                    return;
                }

            }
        }

        //首站保存CONFIG信息
        checkoutsubmit.saveconfigdata(cwa,lotno,opno,opnoflowid,SFCStaticdata.staticmember.lotfoleolcheckflag);

        //stagingtime checkin to checkout
        //checkoutsubmit.checkintonotintime(this, cwa, _deviceno, _newdeviceno, _odbname, _lotno, opno, opnametextBoxText, ip);

        //模穴號
        if (muxuehao)
        {
            if (moxuehao.equals(""))
            {
                ShowMessage("模穴號不可為空");
                return;
            }
            Boolean savemoxue =cwa.CallRB("inputmoxuehao",lotno, opno,moxuehao,userid);
            if (!savemoxue)
            {
                ShowMessage("保存模穴號失敗");
                return;
            }
        }

        if (groupBox4Visible && guigestr.equals("")) {
            ShowMessage("請選擇規格信息");
            return;
        } else if (groupBox4Visible && !guigestr.equals("")) {
            if (cwa.CallRB("checkin_submit_1", lotno, guigestr, opno, userid)) {
            } else {
                DataTable dt = cwa.CallRDT("checkin_submit_2", lotno);
                if (dt.Rowscount() == 0) {
                    ShowMessage("記錄規格信息失敗");
                    return;
                }
            }
        }

        //新機種卡是否解析數據 重工不需要解析數據
        checkoutsubmit.checkin_submit2(this, cwa, _newdeviceno, lotno, opno);

        //前段李翩翩要求加入FC站位加入綁定纖體功能
        DataTable dt_opuser1 = cwa.CallRDT("checkin_submit_3", opno, SFCStaticdata.staticmember.deviceno);
        if (dt_opuser1.Rowscount() > 0) {

            CreatNewActivity(FolBangdingline.class,_deviceno,_newdeviceno,_odbname,"C0003",opno);
        }

        //前段李翩翩要求加入記錄GSB直球記錄
        DataTable dt_opuser = cwa.CallRDT("checkin_submit_4", opno, SFCStaticdata.staticmember.deviceno);
        if (dt_opuser.Rowscount() > 0) {
            CreatNewActivity(GSBOPUSERRecord.class,_deviceno,_newdeviceno,_odbname,lotno,opno,"1");
        }

        //存儲FC站位過站站位的標注
        DataTable dt_fc = cwa.CallRDT("checkin_submit_5", opno, SFCStaticdata.staticmember.deviceno);
        if (dt_fc.Rowscount() > 0) {
            if (recordflagChecked) {
                Boolean flag = cwa.CallRB("checkin_submit_6", lotno, opno, textBox_fcText, userid);
                if (!flag) {
                    ShowMessage("記錄失敗，請重新輸入");
                    return;
                }
            }
        }

        //李春奎要求卡VTQ6037Hold功能，临时解除只需要修改t_qcholdlotno表styleflag = '10'即可
        Boolean vtq6037 = checkVtq6037holdBoolean(lotno, opno);//false;//
        if (vtq6037) {
            ShowMessage("因VTQ6037站位解析超規扣留，請聯繫QC解鎖！~");
            return;
        }

        //VTQFOL 0725 卡定wip   夏琴 20160416
        if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") && SFCStaticdata.staticmember.lotfoleolcheckflag) {
            DataTable dtconqty = cwa.CallRDT("checkin_submit_7", opno);
            if (dtconqty.Rowscount() > 0) {
                Double fconqty = Double.parseDouble(dtconqty.Rows(0).get_CellValue("att1").trim());
                DataTable dtCunWip = cwa.CallRDT("checkin_submit_8", "0725", "1036", "1", "2");
                if (dtCunWip.Rowscount() > 0) {
                    Double nowwip = Double.parseDouble(dtCunWip.Rows(0).get_CellValue("lotwip"));
                    if (nowwip > fconqty) {
                        ShowMessage("Image test 和 Cubic Assembly 的現在的wip為" + nowwip.toString() + " > " + fconqty + ",FOL 主管規定不能進站");
                        return;
                    }
                }
            }
        }

        //记录VTQ BUILD（VT-Q生產各階段名稱)  BY CYF
        if (groupBox5Visible &&  "".equals(comboBoxbuildText)) {
            ShowMessage("請選擇BUILD信息");
            return;
        } else if (groupBox5Visible && !"".equals(comboBoxbuildText)) {
            DataTable builddt = cwa.CallRDT("checkin_submit_9", lotno);
            if (builddt.Rowscount() > 0) {
                cwa.CallRB("checkin_submit_10", lotno);
            } else {
                if (cwa.CallRB("checkin_submit_11", lotno, comboBoxbuildText, opno, userid)) {
                } else {
                    String sql = "select *  from t_Builddata where lotno=:lotno";
                }
            }
        }
        //check振动站位有无振动记录 BY cyf 2015.11.9
        if (cwa.CallRDT("getvdevicedata", SFCStaticdata.staticmember.deviceno, opno).Rowscount() > 0) {
            String lotnoVi = "";
            if (lotno.substring(0, 2).equals("AK")) {
                lotnoVi = lotno.substring(1, 12);
            } else {
                lotnoVi = lotno;
            }

            DataTable dtVi = cwa.CallRDT("checkin_submit_12", lotnoVi, opno);
            if (dtVi.Rowscount() > 0) {
                DataTable dtViQC = cwa.CallRDT("checkin_submit_13", lotnoVi, opno);
                if (dtViQC.Rowscount() > 0) {

                } else {
                    ShowMessage("該批號在該站未經QC振動確認，不能進站");
                    return;
                }
            } else {
                ShowMessage("該批號尚未在該站振動確認，不能進站");
                return;
            }
        }

        // String att2lot = new BLL.basedata().getopatt2(opno);
        String att2lot = cwa.CallRS("getopatt2", opno);
        if (att2lot == null || att2lot.equals("")) {
            ShowMessage("站位特性尚未设定（att2）");
            return;
        }
        if (Integer.parseInt(att2lot) == 5)      //    查詢  att2 == 5  綁定批號站位
        {
            //todo new class open no fol page return
               /* LotNewOldBind lotbind = new LotNewOldBind(lotno, SFCStaticdata.staticmember.odbname, SFCStaticdata.staticmember.userid, dieqty.toString());
                lotbind.ShowDialog();*/

            if (!SFCStaticdata.staticmember.feflag) {
                ShowMessage("請重新輸入FOL批號進行綁定！");
                //rebacklotbind(lotno, SFCStaticdata.staticmember.odbname);
                return;
            } else {
                return;
            }
        }
        //region HC0806/HC0902/HC0304导入GA进站管控YF清洗四小时内  -顧20150516
        //step1 匹配站位特性
        if (SFCStaticdata.staticmember.lotfolbingpicheck && cwa.CallRB("gaOpno", opno))//GA站位
        {
            //step2 匹配機種情況
            DataTable dtopnofoldev = cwa.CallRDT("checkin_submit_14", SFCStaticdata.staticmember.deviceno);
            if (dtopnofoldev == null) {
                ShowMessage("檢測該機種是否要檢測Bracket YF清洗超4H時發生異常，請聯繫MIS");
                return;
            } else if (dtopnofoldev.Rowscount() > 0) {
                DataTable dtfolbracket = cwa.CallRDT("checkin_submit_15", lotno);
                if (dtfolbracket == null) {
                    ShowMessage("查詢該批號的Bracket過站狀態記錄時發生異常，請聯繫MIS");
                    return;
                } else if (dtfolbracket.Rowscount() <= 0) {
                    ShowMessage("系統中沒有該批號YF清洗的記錄，請確認");
                    return;
                } else {
                    Double checktime = Double.parseDouble(dtfolbracket.Rows(0).get_CellValue("checktime").trim());
                    if (checktime > 4) {
                        ShowMessage("該批號Bracket管控系統YF清洗完至GA進站已超4H(" + checktime + "H)；系統將自動把該批號的Bracket管控系統的狀態還原至YF清洗站，請重新YF清洗之後再進行SFC GA站位過站作業。");
                        String backstationcount = "";
                        //獲取已退站數量

                        String follotcountqty = cwa.CallRS("checkin_submit_16", lotno);
                        if (follotcountqty.equals("0")) {
                            backstationcount = "C001";
                        } else {
                            backstationcount = "C" + BaseFuncation.padLeft(
                                    String.valueOf(Integer.parseInt(follotcountqty) + 1),
                                    3,
                                    '0'
                            );
                        }

                        Boolean bl = cwa.CallRB("checkin_submit_17", SFCStaticdata.staticmember.userid, backstationcount, ip, lotno);
                        if (bl) {
                            ShowMessage("還原Bracket數據OK");
                            return;
                        } else {
                            ShowMessage("還原Bracket數據異常，請聯繫MIS");
                            return;
                        }
                    }
                }
            }
        }
        //J系列FOL 母批與EOL子批 綁定
        if (textBox_follotEnabled) {
            String lotnofol = textBox_follotText;
            if (lotnofol.equals("") || lotnofol == null) {
                ShowMessage("請輸入FOL母批號");
                return;
            }
            if (lotnofol.startsWith("K")) {
                lotnofol = "P" + lotnofol;
            }
            String lottemp = "";
            if (lotno.startsWith("K")) {
                lottemp = "A" + lotno;
            } else {
                lottemp = lotno;
            }
            DataTable dteolfol = cwa.CallRDT("FOLEOLLotData", lottemp);
            if (dteolfol.Rowscount() > 0)//已經有數據
            {
                Boolean bl = cwa.CallRB("checkin_submit_18", lottemp);
                if (!bl) {
                    ShowMessage("清除" + SFCStaticdata.staticmember.deviceno + "機種EOL-FOL批號綁定信息異常，請聯繫MIS");
                    return;
                }
            }
            if (!cwa.CallRB("checkin_submit_19", lotnofol, lottemp, ip, SFCStaticdata.staticmember.userid)) {
                ShowMessage("綁定EOL子批號和FOL母批號異常，請聯繫MIS");
                return;
            }
        }

        // FOL FC站位(0004 站)卡彈夾是否已結批
        if (SFCStaticdata.staticmember.lotfolbingpicheck && (SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag)) {
            DataTable dtfcstation = cwa.CallRDT("checkin_submit_20", opno);
            if (dtfcstation == null) {
                ShowMessage("检测当站位是否需要检测弹匣信息异常，请联系MIS");
                return;
            } else if (dtfcstation.Rowscount() > 0) {

                String substratelot = "";
                if (lotno.length() % 2 != 0) {
                    substratelot = lotno.substring(1, lotno.length() - 1);
                } else {
                    substratelot = lotno;
                }
                DataTable dt_test = cwa.CallRDT("checkin_submit_21", substratelot);

                if (dt_test == null) {
                    ShowMessage("查詢批號綁定的彈夾過站信息失敗，請聯繫MIS");
                    return;
                }
                if (dt_test.Rowscount() == 0) {
                    ShowMessage("查詢批號綁定的彈夾尚未結批");
                    return;
                }
            }
        }

        //GaOven卡彈匣編號進出站55MIS
        if ((SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag) && SFCStaticdata.staticmember.lotfolbingpicheck && cwa.CallRB("gaOvenOpno", opno)) {
            //todo new class create
//                FOLFunctionAddPage.FOLInOutCheckGAOven ficg = new SFConline.FOLFunctionAddPage.FOLInOutCheckGAOven(lotnotextBox.Text.toString().trim(), opnotextBox.Text.toString().trim(), dietextBox.Text.toString().trim(), "1");
//                ficg.ShowDialog();
            if (!SFCStaticdata.staticmember.FolGaOvenStationNoInOutTimeIsOK) {
                ShowMessage("FOL GaOven站位彈匣號進站信息不完整");
                return;
            }
        }
        //针对FOL批号进行检测是否为需要扫描弹匣的站位
        if (SFCStaticdata.staticmember.lotfolbingpicheck && (SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag)) {

            DataTable dtbox = cwa.CallRDT("checkin_submit_22", opno);
            if (dtbox == null) {
                ShowMessage("检测当前站位是否需要扫描弹匣信息异常，请联系MIS");
                return;
            } else if (dtbox.Rowscount() > 0) {
                //todo new class create
//                    FOLNoPaper.FOLCheckCodePage fccp = new SFConline.FOLNoPaper.FOLCheckCodePage(lotno, dietextBox.Text.toString().trim(), opno, SFCStaticdata.staticmember.deviceno, "弹匣编号");
//                    fccp.ShowDialog();
                //检测是否有扫描OK
                DataTable dtboxcheck = cwa.CallRDT("checkin_submit_23", lotno, opno);
                if (dtboxcheck == null) {
                    ShowMessage("查询该批号在当前站位对应的弹匣信息异常，请联系MIS");
                    return;
                } else if (dtboxcheck.Rowscount() <= 0) {
                    ShowMessage("当前站位尚未扫描弹匣信息，不可进行进站操作");
                    return;
                }
            }
        }

        if (checkBox_dblotnoChecked) {
            String dbqtynew = textBox_dbqtyText;
            if (dbqtynew.equals("") || dbqtynew == null) {
                ShowMessage("请输入正确的DB数量！");
                setFocusable(textBox_dbqty, true);
                return;
            }

            //String[] parmerdblotno = { lotno, lotno, ip, opno, dbqtynew };
            try {
                String datenow = BaseFuncation.getnowdatetime();
//                     String datenow = DateTime.Now.ToString("yyyy/MM/dd/HH:mm:ss");      //大寫24h
                Boolean inserdblot = cwa.CallRB("checkin_submit_24", lotno, opno, ip, datenow, dbqtynew);
                if (!inserdblot) {
                    ShowMessage("DB批號標注失敗，請聯繫MIS！");
                    return;
                }
            } catch (Exception ex) {
                ShowMessage("DB批號標注時發生錯誤，請聯繫MIS！");
                return;
            }
        }

        if (checkBoxlineChecked) {
            Boolean insdbflag = cwa.CallRB("errorinfobackdb", lotno, opno, "實際作業腺體與所選不符", SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.odbname, "36", "");
            if (!insdbflag) {
                ShowMessage("實際線別存儲失敗，請重新過站！");
                return;
            }
        }

        if (SFCStaticdata.staticmember.lotoutchecktimeflag) {
            String sysdatenow = cwa.CallRS("getlotoutintime", lotno);
            if (Double.parseDouble(sysdatenow) > 2.5) {
                if (BaseFuncation.DialogResult.OK ==
                        MessageBox("GA入烤箱时间超过 2.5 Hour，是否需要強行過站 ?<" + sysdatenow + ">", "單擊繼續")) {
                    String testitemerror = "GA入烤箱时间超过 2.5 Hour，是否需要強行過站 ?<" + sysdatenow + ">";
                    String testonocart = "FOL GA";
                    SFCStaticdata.staticmember.testsumcheckflag = false;

                    CreatNewActivity(testopnosnsumcheck.class,_deviceno,_newdeviceno,_odbname,lotno, opno, testitemerror, testonocart);

                    if (!SFCStaticdata.staticmember.testsumcheckflag) {
                        ShowMessage("记录不良信息失败");
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        DataTable bitsever = cwa.CallRDT("checkin_submit_25", SFCStaticdata.staticmember.deviceno);

        String bitopno = "", bitmailsever = "";

        if (bitsever.Rowscount() == 1) {
            bitopno = bitsever.Rows(0).get_CellValue("ITEM").trim();
            bitmailsever = bitsever.Rows(0).get_CellValue("ATT2").trim();
            if (bitsever.Rowscount() > 0 && opno.equals(bitopno)) {
                DataTable dt100 = cwa.CallRDT("checkin_submit_26", lotno);
                if (dt100.Rowscount() > 0) {
                    Boolean bl100bit = false;
                } else {
                    List<String[]> list100bit = new ArrayList<String[]>();
                    Boolean bl100bitok = true;
                    StringBuilder ErrorMessageStr = new StringBuilder();
                    DataTable dt100bit = cwa.CallRDT("checkin_submit_27");
                    if (dt100bit == null) {
                        ShowMessage("查詢該機種是否需要檢測100Bit燒錄信息異常，請聯繫MIS");
                        return;
                    } else if (dt100bit.Rowscount() > 0) {
                        List<String> tempfind = new ArrayList<String>();
                        int bborn = 0;
                        for (int i = 0; i < dt100bit.Rowscount(); i++) {
                            String name100bit = dt100bit.Rows(i).get_CellValue("content").trim();
                            String[] arrayspilt100bit = dt100bit.Rows(i).get_CellValue("type").split(";");
                            String type100bit = arrayspilt100bit[0];
                            String location100bit = arrayspilt100bit[1];
                            String specvalue100bit = dt100bit.Rows(i).get_CellValue("specvalue").trim();
                            String itemsend100bit = dt100bit.Rows(i).get_CellValue("itemsend");
                            String lotnotemp = "";
                            String itemnum = cwa.CallRS("checkin_submit_28", itemsend100bit);
                            int tempint = -1;
                            int intnum = Integer.parseInt(itemnum) - 1;
                            if (location100bit.equals("FOL")) {
                                if (!lotno.startsWith("AK")) {
                                    lotnotemp = cwa.CallRS("checkin_submit_29", lotno);
                                    if (lotnotemp == null) {
                                        ShowMessage("查詢" + lotno + "對應的前段批號異常，請聯繫MIS");
                                        return;
                                    } else if (lotnotemp.equals("")) {
                                        ShowMessage("未查詢到" + lotno + "對應的前段批號");
                                        return;
                                    }
                                } else if (lotno.startsWith("AK")) {
                                    lotnotemp = lotno.replace("AK", "PK");
                                }
                            } else {
                                lotnotemp = lotno;
                            }
                            if (type100bit.equals("1"))//機台號 t_lotequplinelog
                            {
                                DataTable dtmachine100 = cwa.CallRDT("checkin_submit_30", lotnotemp, specvalue100bit);
                                if (dtmachine100 == null) {
                                    ShowMessage("查詢" + lotnotemp + "對應的" + name100bit + "信息異常，請聯繫MIS");
                                    return;
                                } else if (dtmachine100.Rowscount() <= 0) {
                                    if (bl100bitok) {
                                        bl100bitok = false;
                                    }
                                    ErrorMessageStr.append(lotnotemp + "沒有對應的" + name100bit + "的信息;\r\n\r\n");
                                } else if (dtmachine100.Rowscount() > 0) {
                                    for (int j = 0; j < dtmachine100.Rowscount(); j++) {
                                        String[] temparr100bit = {lotnotemp, dtmachine100.Rows(j).get_CellValue("charvalue"), itemsend100bit};
                                        list100bit.add(temparr100bit);
                                        bborn++;
                                        tempint++;
                                        if (tempint == intnum) {
                                            break;
                                        }
                                    }
                                }
                            } else if (type100bit.equals("2"))//耗材批號 tblwipexpendablelotlog
                            {
                                if (itemsend100bit.equals("7"))  //基板
                                {
                                    DataTable dtmaterial = cwa.CallRDT("checkin_submit_31", lotnotemp);

                                    if (dtmaterial.Rowscount() == 0) {
                                        dtmaterial = cwa.CallRDT("checkin_submit_32", lotnotemp);
                                    }
                                    if (dtmaterial == null) {
                                        ShowMessage("查詢" + lotnotemp + "對應的基板信息異常，請聯繫MIS");
                                        return;
                                    } else if (dtmaterial.Rowscount() > 0) {
                                        for (int k = 0; k < dtmaterial.Rowscount(); k++) {
                                            String currentstr = dtmaterial.Rows(k).get_CellValue("materiallotno");   // 这里加入防呆
                                            if (currentstr.contains("-")) {
//                                                char[] cc = { '-' };
                                                String[] ss = currentstr.split("-");
                                                currentstr = ss[0];
                                            }

                                            if (tempisexist(tempfind, currentstr)) {
                                                continue;
                                            }
                                            tempfind.add(currentstr);
                                            String[] temparr100bit = {lotnotemp, currentstr, itemsend100bit};//只取一個基板
                                            list100bit.add(temparr100bit);
                                            bborn++;
                                            tempint++;
                                            if (tempint == intnum) {
                                                tempfind.clear();
                                                break;
                                            }

                                        }
                                    } else {
                                        if (bl100bitok) {
                                            bl100bitok = false;
                                        }
                                        ErrorMessageStr.append(lotnotemp + "沒有對應的基板信息異常的信息;\r\n\r\n");

                                    }


                                } else {
                                    DataTable dtmaterial100 = cwa.CallRDT("checkin_submit_33", lotnotemp, specvalue100bit);
                                    if (dtmaterial100 == null) {
                                        ShowMessage("查詢" + lotnotemp + "對應的" + name100bit + "信息異常，請聯繫MIS");
                                        return;
                                    } else if (dtmaterial100.Rowscount() <= 0) {
                                        dtmaterial100 = cwa.CallRDT("checkin_submit_34", lotnotemp, specvalue100bit);
                                        if (dtmaterial100 == null) {
                                            ShowMessage("查詢" + lotnotemp + "對應的" + name100bit + "信息異常，請聯繫MIS");
                                            return;
                                        } else if (dtmaterial100.Rowscount() <= 0) {
                                            dtmaterial100 = cwa.CallRDT("checkin_submit_35", lotnotemp, specvalue100bit);
                                            if (dtmaterial100 == null) {
                                                ShowMessage("查詢" + lotnotemp + "對應的" + name100bit + "信息異常，請聯繫MIS");
                                                return;
                                            } else if (dtmaterial100.Rowscount() <= 0) {
                                                dtmaterial100 = cwa.CallRDT("checkin_submit_36", lotnotemp, specvalue100bit);
                                                if (dtmaterial100 == null) {
                                                    ShowMessage("查詢" + lotnotemp + "對應的" + name100bit + "信息異常，請聯繫MIS");
                                                    return;
                                                } else if (dtmaterial100.Rowscount() <= 0) {
                                                    if (bl100bitok) {
                                                        bl100bitok = false;
                                                    }
                                                    ErrorMessageStr.append(lotnotemp + "沒有對應的" + name100bit + "的信息;\r\n\r\n");
                                                } else if (dtmaterial100.Rowscount() > 0) {
                                                    for (int j = 0; j < dtmaterial100.Rowscount(); j++) {
                                                        String currentstr = dtmaterial100.Rows(j).get_CellValue("materiallotno");   // 这里加入防呆
                                                        if (currentstr.contains("-")) {
//                                                            char[] cc = { '-' };
                                                            String[] ss = currentstr.split("-");
                                                            currentstr = ss[0];
                                                        }

                                                        if (tempisexist(tempfind, currentstr)) {
                                                            continue;
                                                        }
                                                        tempfind.add(currentstr);
                                                        String[] temparr100bit = {lotnotemp, currentstr, itemsend100bit};
                                                        list100bit.add(temparr100bit);
                                                        bborn++;
                                                        tempint++;
                                                        if (tempint == intnum) {
                                                            tempfind.clear();
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else if (dtmaterial100.Rowscount() > 0) {
                                                for (int j = 0; j < dtmaterial100.Rowscount(); j++) {
                                                    String currentstr = dtmaterial100.Rows(j).get_CellValue("materiallotno");   // 这里加入防呆
                                                    if (currentstr.contains("-")) {
//                                                        char[] cc = { '-' };
                                                        String[] ss = currentstr.split("-");
                                                        currentstr = ss[0];
                                                    }

                                                    if (tempisexist(tempfind, currentstr)) {
                                                        continue;
                                                    }
                                                    tempfind.add(currentstr);
                                                    String[] temparr100bit = {lotnotemp, currentstr, itemsend100bit};
                                                    list100bit.add(temparr100bit);
                                                    bborn++;
                                                    tempint++;
                                                    if (tempint == intnum) {
                                                        tempfind.clear();
                                                        break;
                                                    }
                                                }
                                            }
                                        } else if (dtmaterial100.Rowscount() > 0) {
                                            for (int j = 0; j < dtmaterial100.Rowscount(); j++) {
                                                String currentstr = dtmaterial100.Rows(j).get_CellValue("materiallotno");   // 这里加入防呆
                                                if (currentstr.contains("-")) {
//                                                    char[] cc = { '-' };
                                                    String[] ss = currentstr.split("-");
                                                    currentstr = ss[0];
                                                }

                                                if (tempisexist(tempfind, currentstr)) {
                                                    continue;
                                                }
                                                tempfind.add(currentstr);
                                                String[] temparr100bit = {lotnotemp, currentstr, itemsend100bit};
                                                list100bit.add(temparr100bit);
                                                bborn++;
                                                tempint++;
                                                if (tempint == intnum) {
                                                    tempfind.clear();
                                                    break;
                                                }
                                            }
                                        }
                                    } else if (dtmaterial100.Rowscount() > 0) {
                                        for (int j = 0; j < dtmaterial100.Rowscount(); j++) {
                                            String currentstr = dtmaterial100.Rows(j).get_CellValue("materiallotno");   // 这里加入防呆
                                            if (currentstr.contains("-")) {
//                                                char[] cc = { '-' };
                                                String[] ss = currentstr.split("-");
                                                currentstr = ss[0];
                                            }

                                            if (tempisexist(tempfind, currentstr)) {
                                                continue;
                                            }
                                            tempfind.add(currentstr);
                                            String[] temparr100bit = {lotnotemp, currentstr, itemsend100bit};
                                            list100bit.add(temparr100bit);
                                            bborn++;
                                            tempint++;
                                            if (tempint == intnum) {
                                                tempfind.clear();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (bl100bitok) {
                        String insert100bitsql = "begin  ";
                        List<String> parmer100bitlist = new ArrayList<String>();
                        for (int s = 0; s < list100bit.size(); s++) {
                            String lotnoinsert = list100bit.get(s)[0];
                            String valueinsert = list100bit.get(s)[1];
                            String iteminsert = list100bit.get(s)[2];
                            if (valueinsert.contains("-")) {
//                                char[] cc = { '-' };
                                String[] ss = valueinsert.split("-");
                                valueinsert = ss[0];
                            }
                            DataTable dt_choseitem = cwa.CallRDT("checkin_submit_37", lotnoinsert, iteminsert, valueinsert);
                            if (dt_choseitem.Rowscount() > 0) {

                            } else {
                                insert100bitsql = insert100bitsql + "insert into t_lotnomesinfo (lotno,item,borninginfo,creatdate,ip,att1,att2,att3,att4,att5) values(:lotnonow" + s + ",:itemnow" + s + ",:burninginfonow" + s + ",sysdate,:ip" + s + ",null,null,null,null,null);";

                                parmer100bitlist.add(lotnoinsert);
                                parmer100bitlist.add(iteminsert);
                                parmer100bitlist.add(valueinsert);
                                parmer100bitlist.add(ip);//SFCStaticdata.staticmember.ip
                            }
                        }
                        String[] tempstr = new String[parmer100bitlist.size()];
                        parmer100bitlist.toArray(tempstr);
                        Boolean blinsert100bit = cwa.CallRB("checkout_submit_90", insert100bitsql, BaseFuncation.SerializeObjectArrayString(tempstr));
                        parmer100bitlist.toArray(tempstr);

                        if (!blinsert100bit) {
                            ShowMessage("存儲燒錄信息異常請聯繫MIS");
                            return;
                        }
                        //发送烧录预警邮件   add by ljj 20151227
                        DataTable dtborn = new DataTable();
                        dtborn.AddColumn("Item");
                        dtborn.AddColumn("TypeInfo");
                        dtborn.AddColumn("Info");
                        String eollotno = lotno;
                        if (lotno.startsWith("AK")) {
                            eollotno = lotno.substring(1, lotno.length() - 1);
                        } else {
                            eollotno = lotno;
                        }
                        ;
                        String follotno = "";
                        if (!eollotno.substring(0, 1).equals("K")) {
                            String follotnotemp = cwa.CallRS("checkin_submit_38", eollotno);
                            if (!follotnotemp.equals(""))
                                follotno = follotnotemp;
                            else {

                                dtborn.AddRow("error", "error", "error");
                                dtborn.AddRow();
                            }

                        } else {
                            follotno = "P" + eollotno;
                            eollotno = "A" + eollotno;
                        }
                        ;

                        String querylotno = "";
                        DataTable dtitem = cwa.CallRDT("checkin_submit_39");
                        for (int i = 0; i < dtitem.Rowscount(); i++) {
                            String item = dtitem.Rows(i).get_CellValue("item");
                            String itemsfc = dtitem.Rows(i).get_CellValue("att2");
                            String current = dtitem.Rows(i).get_CellValue("att3");
                            String isline = dtitem.Rows(i).get_CellValue("line");
                            String typeinfo = dtitem.Rows(i).get_CellValue("typeinfo");
                            if (current.equals("")) {
                                dtborn.AddRow("error", typeinfo, "error");
                            }
                            int intcurrent = Integer.parseInt(current);
                            String location = dtitem.Rows(i).get_CellValue("att1");
                            if (location.contains("EOL")) {
                                querylotno = eollotno;
                            } else {
                                querylotno = follotno;
                            }
                            if (dtitem.Rows(i).get_CellValue("enable").equals("1")) {
                                DataTable dtgetsfc = cwa.CallRDT("checkin_submit_40", querylotno, itemsfc);
                                if (dtgetsfc.Rowscount() > 0) {
                                    if (dtgetsfc.Rowscount() <= intcurrent) {
                                        dtborn.AddRow(String.valueOf(i + 1), "");
                                    } else {
                                        dtborn.AddRow(String.valueOf(i + 1), typeinfo, dtgetsfc.Rows(intcurrent).get_CellValue("borninginfo").trim());
                                    }
                                } else {
                                    dtborn.AddRow("error", typeinfo, "error");
                                }
                            }
                        }
                        try {
                            cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(dtborn), bitmailsever, SFCStaticdata.staticmember.deviceno + " " + lotno + "耗材烧录信息預警郵件,请相关部门检查", "0");
                        } catch (Exception ex) {
                            ShowMessage("发送烧录预警邮件失败");
                            return;
                        }
                    } else {
                        ShowMessage(ErrorMessageStr + "請重新選機台號和掃描耗材批號之後再作業");
                        return;
                    }
                }
            }
        }
        //ACF進站卡全掃
        if (att2lot.equals("17") && false) {
            String lotnowono = "";
            if (lotno.length() % 2 == 1) {
                lotnowono = lotno.substring(1, lotno.length() - 1);
            }
            String acfallscan = cwa.CallRS("checkin_submit_41", lotno);
            if (acfallscan.equals("") || acfallscan == null || acfallscan.equals("0")) {
                ShowMessage("請確認ACF是否完成全掃動作！");
                return;
            } else {
                if (Integer.parseInt(acfallscan) != Integer.parseInt(dieqty)) {
                    if (BaseFuncation.DialogResult.OK == MessageBox("ACF帶進站全掃與進站數據不符,是否需要强行过站？(" + acfallscan + "!=" + dieqty + ")", "系統提示")) {
                        SFCStaticdata.staticmember.podtestsumcheck = false;

                        CreatNewActivity(errorinfoshow.class,_deviceno,_newdeviceno,_odbname,
                                "ACF帶進站全掃與進站數據不符,是否需要强行过站？(" + acfallscan + "!=" + dieqty + ")", lotnowono, opno, SFCStaticdata.staticmember.odbname, "5", "1", "32");
                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                            ShowMessage("验证强行出站失败！");
                            return;
                        }
                        Boolean blPro = cwa.CallRB("checkin_submit_42", lotno);
                        if (blPro) {
                            ShowMessage("更改狀態失敗！");
                        }
                    } else {
                        return;
                    }
                }
            }
        }
        // 判斷是否有進行大賣場掃描
        String lotno_m = "";
        if (lotno.length() % 2 == 1)//13位批號???????開出11為批號的話？。。。。。。。如何處理？
        {
            lotno_m = lotno.substring(1, 13);
        }
        String[] charlotno = lotno_m.split("-");
        String lotnolike = charlotno[0] + "%";
        if (att2lot.equals("2") && opno.equals("0810") && ((ip.equals("10.143.41.204")) || (ip.equals("10.134.34.35")))) //?????????????IP還在使用麼？
        {
            String lotchecksnqtydb = cwa.CallRS("checkin_submit_43", lotnolike, lotno_m);
            if (lotchecksnqtydb.equals("") || lotchecksnqtydb == null || lotchecksnqtydb.equals("0")) {
                ShowMessage("此批號尚未完成進行Image-Test2(30cm)站位大賣場掃描,請先進行大賣場掃描再過站,知悉~");
                return;
            } else {
                String checksninfoqty = cwa.CallRS("checkin_submit_44", lotno);
                if (Integer.parseInt(checksninfoqty) > Integer.parseInt(lotchecksnqtydb))     //宸叉巸SN涓嶈兘灏忎簬鎵硅櫉鍏х附鏁搁噺
                {
                    if (BaseFuncation.DialogResult.OK == MessageBox("此批號尚未完成Image-Test2(30cm)站位大賣場掃描記錄,是否需要繼續過站(已掃SN:" + lotchecksnqtydb + "共有SN:" + checksninfoqty + ")", "系統提示")) {
                    } else {
                        return;
                    }
                } else {
                }
            }
        }
        //check 物料plasma 站位到設定站位是否超過6個小時   HP12-31  +++++++++++++++++++++++++++++++++++++++++++++++++++++++
        String passflag = "N";
        //DataTable redt = new BLL.basedata().getreturndata(lotno);   //退站批號不卡
        DataTable redt = cwa.CallRDT("getreturndata", lotno);   //退站批號不卡
        if (redt != null && redt.Rowscount() > 0) {
            String startopno = redt.Rows(0).get_CellValue("opno_now").trim();
            String backopno = redt.Rows(0).get_CellValue("opno_back").trim();
            // String startopnoflowid = new BLL.basedata().getopnoflowiddata(startopno);
            String startopnoflowid = cwa.CallRS("getopnoflowiddata", startopno);
            String backopnoflowid = cwa.CallRS("getopnoflowiddata", backopno);
            try {
                if (Integer.parseInt(startopnoflowid) >= Integer.parseInt(opnoflowid) && Integer.parseInt(backopnoflowid) <= Integer.parseInt(opnoflowid)) {
                    passflag = "Y";
                }
            } catch (Exception ex) {
                passflag = "Y";
            }
        }
        if (passflag.equals("N")) {
            String checkoveropno = cwa.CallRS("checkoveropnodata", opno);
            if (!checkoveropno.equals("") && checkoveropno != null) {
                DataTable ovdt = cwa.CallRDT("getovertimedata", lotno);
                if (ovdt == null) {
                    ShowMessage("獲取超時信息時發生錯誤");
                    return;
                } else {
                    if (ovdt.Rowscount() > 0) {
                        try {

                            String tempmaopno = ovdt.Rows(0).get_CellValue("opno").trim();
                            if (!tempmaopno.equals("INV") && !tempmaopno.equals("END")) {
                                ShowMessage("該批次" + lotno + "在物料管控系統中綁定的" + ovdt.Rows(0).get_CellValue("magazineno") + "尚未過站完畢，不能在此過站");
                                return;
                            }
                            Double overstr = Double.parseDouble(ovdt.Rows(0).get_CellValue("overtime").trim());
                            double double_overtime = 0;
                            DataTable dt_overtime =cwa.CallRDT("checkin_submit_80",opno);
                            if (dt_overtime.Rowscount() > 0)
                            {
                                try
                                {
                                    double_overtime =Double.parseDouble(dt_overtime.Rows(0).get_CellValue("att1"));
                                }
                                catch(Exception e1)
                                {
                                    double_overtime = 6;
                                }
                            }
                            //這個功能到這裡結束
                            if (overstr > double_overtime)
                            {
                                ShowMessage("該批號" + lotno + "在物料管控系統中綁定的彈夾" + ovdt.Rows(0).get_CellValue("magazineno") + "Plasma站位到現在已經超時" + double_overtime + "個小時，請在物料管控系統中重新清洗  ~   溫馨提示");
                                return;
                            }

                        } catch (Exception ex) {
                            ShowMessage("轉換超時時間時發生錯誤，請聯繫MIS部門或者請檢查物料管控系統中是否有過物料信息！~");
                            return;
                        }
                    }
                    else
                    {
                        ShowMessage("該批號" + lotno + "在物料管控系統中沒有過站，請現在物料管控系統中過站再在SFC系統中過站");
                        return;
                    }
                }
            }
        }

        //RI系列机种卡Plasma站位到指定站位不可以超??定的??  by Alan 20170214
        DataTable dt_plasma = cwa.CallRDT("checkin_submit_78",_newdeviceno,opno);

        DataTable dtRebacklotno =  cwa.CallRDT("getreturndata", lotno);   //退站批號不卡

        if (dt_plasma.Rowscount() > 0 && (dtRebacklotno.Rowscount() <= 0 || dtRebacklotno == null))
        {
            String temptimelotno = "";
            if (SFCStaticdata.staticmember.str_lotno.startsWith("PK"))
            {
                temptimelotno = SFCStaticdata.staticmember.str_lotno.substring(1, 13);
            }
            else
            {
                temptimelotno = SFCStaticdata.staticmember.str_lotno;
            }

            DataTable dt_outtime = cwa.CallRDT("checkin_submit_78",temptimelotno);

            double double_time = 0;
            try
            {
                double_time = Double.parseDouble(dt_outtime.Rows(0).get_CellValue("hours"));
            }
            catch (Exception e1)
            {
                ShowMessage("該批號綁定的Plasma站位還未出站，請先出Plasma站位。");
                return;
            }

            if (double_time >= Double.parseDouble(dt_plasma.Rows(0).get_CellValue("att3")))
            {
                ShowMessage("從物料Plasma站位到當前站位已經超過" + dt_plasma.Rows(0).get_CellValue("att3") + "小時，請重新過Plasma站位");
                return;
            }

        }

        //check 物料NHA類似機種物料LCM100機台 站位到SFConline 0351_1站位不能超越一個小時
        //VT-Q FC站位解綁Cover
        // String carrieropno = new BLL.basedata().getopencarrieropno(opno);
        String carrieropno = cwa.CallRS("getopencarrieropno", opno);
        if (!carrieropno.equals("") && carrieropno != null) {
            //Boolean jiefalg = new BLL.basedata().firedcoverdata(lotno);
            Boolean jiefalg = cwa.CallRB("firedcoverdata", lotno);
            if (!jiefalg) {
                ShowMessage("解綁Cover信息失敗，請聯繫MIS部門");
                return;
            }
        }

        String devicenoflagccc = SFCStaticdata.staticmember.odbname;
        if (devicenoflagccc.equals("RIGCONN"))  // 只针对 RIG 产品卡时间。
        {
            if (SFCStaticdata.staticmember.lotacfchecklasertime)  // ACF check add 0814 liang
            {
                String sqllaserfirsttime = cwa.CallRS("checkin_submit_49", lotno);
                if (sqllaserfirsttime.equals("") || sqllaserfirsttime == null) {
                    ShowMessage("查询批号至ACF已用时间时发生错误");
                    return;
                } else {
                    if (Double.parseDouble(sqllaserfirsttime) > 40) {
                        if (BaseFuncation.DialogResult.OK == MessageBox("Cube Incoming至目前为止已超过 40 Hours，是否需要強行過站 ?<" + sqllaserfirsttime + ">", "單擊繼續")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            CreatNewActivity(errorinfoshow.class,_deviceno,_newdeviceno,_odbname,
                                    "Cube Incoming至目前为止已超过 40 Hours，是否需要強行過站 ?<" + sqllaserfirsttime + ">", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "100", "22");
                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                ShowMessage("验证强行出站失败！<ACF 40Hours>");
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }


        //增加從一個站位到下面任意一個站位卡控時間，如果超時會退站
        DataTable dt_checkovertime = cwa.CallRDT("checkin_submit_50", opno, SFCStaticdata.staticmember.newdeviceno);

        if (dt_checkovertime.Rowscount() > 0) {
            // String sqllaserfirst = "select trunc((sysdate-(select checkoutdate  from t_lotlog where lotno= :lotno1 and opno = :opno1))*24,2) as hours from dual";
            //String[] sqllaserfirstpar = { lotno, dt_checkovertime.Rows(0).get_CellValue("att1").toString().trim() };
            // String sqllaserfirsttime = new DAL.Ora_dal().OracleExecstringparmer(sqllaserfirst, sqllaserfirstpar, sqllaserfirstpar.length(), SFCStaticdata.staticmember.odbname);
            String sqllaserfirsttime = cwa.CallRS("checkin_submit_51", lotno, dt_checkovertime.Rows(0).get_CellValue("att1").toString().trim());
            if (sqllaserfirsttime == null || "".equals(sqllaserfirsttime)) {
                ShowMessage("查询批号至ACF已用时间时发生错误");
                return;
            } else {
                if (Double.parseDouble(sqllaserfirsttime) > Double.parseDouble(dt_checkovertime.Rows(0).get_CellValue("att3").trim())) {
                    //檢測是否有退站，有退站就放過
                    DataTable dt_backopno = cwa.CallRDT("checkin_submit_52", lotno);
                    if (dt_backopno.Rowscount() > 0) {
                    } else {
                        ShowMessage("從" + dt_checkovertime.Rows(0).get_CellValue("att1").trim() + "到" + dt_checkovertime.Rows(0).get_CellValue("att2").trim() + "超時" + dt_checkovertime.Rows(0).get_CellValue("att3").trim() + "小時，請退站至" + dt_checkovertime.Rows(0).get_CellValue("att1").trim() + "站位重新過站");
                        return;
                    }
                }
            }
        }
        //卡任意設定站位超過設定時間不允許進站
        // 判定是否有设定料号对应耗材，必须设定  20151114  BY cyf
        DataTable dtexpenddevice = cwa.CallRDT("getexpendsettingdata", SFCStaticdata.staticmember.deviceno);
        if (dtexpenddevice != null) {
            if (dtexpenddevice.Rowscount() > 0) {

            } else {
                DataTable dtexpendlist = new DataTable();
                //dtexpendlist = new BLL.basedata().getexpendable(productno);
                dtexpendlist = cwa.CallRDT("getexpendable", productno);
                if (SFCStaticdata.staticmember.devicenomacinfo && lotno.substring(0, 1).equals("P"))     //J系列不卡
                {
                } else {
                    if (dtexpendlist.Rowscount() == 0) {
                        ShowMessage("该料号尚未设定对应耗材资料，请联系MIS设定");
                        return;
                    }
                }
            }

        } else {
            ShowMessage("獲取不卡掃耗材的機種信息時發生錯誤，請聯繫MIS部門");
            return;
        }
        //卡物料管控系統載版是否上線
        Boolean magazineflag = false;
        Boolean magazineflag_1 = false;
        magazineflag_1 = getmagazinelotflag(lotno);
        magazineflag = getmagazineflag(opno);


        if (magazineflag && !magazineflag_1) {
            int qty = Integer.parseInt(dieqty) / 400;
            if (Integer.parseInt(dieqty) % 400 != 0) {
                qty++;
            }
            String mzqty = cwa.CallRS("checkin_submit_53", lotno, opno);
            try {
                if (qty == Integer.parseInt(mzqty)) // 已有綁定數據
                {
                } else {
                    CreatNewActivity(magazineform.class,_deviceno,_newdeviceno,_odbname,
                            lotno, opno, String.valueOf(qty), "1");
                    Boolean mz = false;
                    if (!mz) {
                        ShowMessage("進站失敗");
                        return;
                    }
                }
            } catch (Exception ex) {
            }

        }

        String outinopno = cwa.CallRS("checkoutinsertmaterialopno", opno);   //VT-Q 如果有維護站位，就放到出站頁面掃描   wu.hp 12-26

        SFCStaticdata.expendm.expendpageflag = false;
        DataTable dtexpend = new DataTable();
        dtexpend = cwa.CallRDT("getexpendablelist", opno, productno);
        String opnoflowidnewin = lotno + "-" + BaseFuncation.padLeft(opnoflowid.trim(), 3, '0');
        if (dtexpend.Rowscount() > 0) {
            String in_expendable =dtexpend.Rows(0).get_CellValue("att5").toString(); //cwa.CallRS("checkin_submit_54", productno, opno);
            if (in_expendable.equals("OUT")) {

            } else {
                //SFCStaticdata.expendm.expendpageflag = false;
                CreatNewActivity(expendable.class,_deviceno,_newdeviceno,_odbname,
                        opno, lotno, productno, opnoflowidnewin, outinopno, "IN");
                //if (!b.getBoolean("expendpageflag"))
                  if (!SFCStaticdata.expendm.expendpageflag)
                {
                    ShowMessage("未輸入耗材資料﹐請重新輸入");
                    //rebacklotbind(lotno, SFCStaticdata.staticmember.odbname);
                    return;
                }
            }
        }

        String execsql = "begin ";
        String charno, charname, charvalue = "";
        String linesametype = "";

        for (int i = 0; i < bindingdataGridViewRowscount; i++) {
            charno = getText((TextView) bindingdataGridView.getChildAt(i).findViewById(R.id.listviewcheckinmname1)).toString().trim();
            charname = getText((TextView) bindingdataGridView.getChildAt(i).findViewById(R.id.listviewcheckinmname2)).toString().trim();

            try {
                charvalue =((FinalStaticCloass.SpinnerData) getSelectedItem(
                                (Spinner) bindingdataGridView.getChildAt(i).findViewById(R.id.listviewcheckinmname3)
                        )).getText();
            } catch (Exception ex) {
                charvalue = null;
            }

            //SMT，FOL檢查線體是否一致
            //DataTable firdt = new BLL.basedata().getfollineopnodata(opno);
            DataTable firdt = cwa.CallRDT("getfollineopnodata", opno);
            if (firdt != null && firdt.Rowscount() > 0) {
                String tempareastr = "";
                if (lotno.substring(0, 1).trim().equals("S")) {
                    tempareastr = "SMT Lines";
                } else if (lotno.substring(0, 1).trim().equals("P")) {
                    tempareastr = "FOL Lines";
                } else {
                    tempareastr = "EOL Lines";
                }
                //DataTable linedt = new BLL.basedata().getsmtlinedata(lotno, tempareastr);
                DataTable linedt = cwa.CallRDT("getsmtlinedata", lotno, tempareastr);
                if (linedt != null && linedt.Rowscount() > 0) {
                    String smtlinestr = linedt.Rows(0).get_CellValue("charvalue").trim();
                    String opnamestr = linedt.Rows(0).get_CellValue("opname").trim();
                    if (!smtlinestr.equals(charvalue) && charname.equals(tempareastr)) {
                        ShowMessage("該批次現在選擇的線體和在 " + opnamestr + " 站位的線體 " + smtlinestr + " 不一致，不能過站");
                        return;
                    } else {
                        linesametype = "1";
                    }
                }
            }

            if (charno == null || charno.equals("")) {
                ShowMessage("特性資料不完整");
                return;
            }
            if (charname == null || charname.equals("")) {
                ShowMessage("特性資料不完整");
                return;
            }
            if (charvalue == null || charvalue.equals("")) {
                ShowMessage("特性資料不完整");
                return;
            }

            if (SFCStaticdata.staticmember.useflag)    //  only for line check
            {
                String dt_char = "";
                try {
                    dt_char = cwa.CallRS("checkin_submit_55", charno, charvalue);
                    if (dt_char.equals("") || dt_char == null) {
                        ShowMessage("該檢測tblcharactervalue是否維護(att1)！");
                        return;
                    } else {
                        int charflag;
                        if ((SFCStaticdata.staticmember.odbname).equals("N41ACONN")) {
                            charflag = Integer.parseInt(dt_char.substring(0, 1).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("N41BCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(1, 2).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("N41CCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(2,3).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("N41CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(3, 4).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("RIGCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(4, 5).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("RILCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(5, 6).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("RIMCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(6, 7).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("J1617CONN"))   //   && SFCStaticdata.staticmember.odbname != "J17CONN"
                        {
                            charflag = Integer.parseInt(dt_char.substring(7,8).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("J17CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(8, 9).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("J70CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(9, 10).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("J78CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(10, 11).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("N94CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(11, 12).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("TC1103BCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(12, 13).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("SC1005CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(13, 14).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("SC1002CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(14, 15).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("C149BCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(15, 16).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("J94CONN")) {
                            charflag = Integer.parseInt(dt_char.substring(10, 11).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if (SFCStaticdata.staticmember.odbname.equals("THREEDCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(0, 1).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        } else if ((SFCStaticdata.staticmember.odbname).equals("VTQCONN")) {
                            charflag = Integer.parseInt(dt_char.substring(0, 1).trim());
                            if (charflag != 1) {
                                ShowMessage("該幾種與所選線別不符，請重新選擇");
                                return;
                            }
                        }

                        try {
                            DataTable dt = cwa.CallRDT("checkin_submit_56", ip, charvalue);
                            if (dt.Rowscount() <= 0 && SFCStaticdata.staticmember.lotcheckeolflag)//如果是(FOL||EOL)且没有IP权限(SMT不管空)20150127
                            {
                                ShowMessage("該IP無過該線別的權限！");
                                return;
                            } else {
                                if (false) {
                                    String aa = "";
                                    if (lotno.length() == 12 || lotno.length() == 15) {
                                        aa = lotno;
                                    } else if (lotno.length() == 13 || lotno.length() == 16) {
                                        aa = "P" + lotno.substring(1, 12);
                                    }

                                    try {
                                        DataTable checkeolfol = cwa.CallRDT("checkin_submit_57", aa, charvalue, opno);
                                        if (checkeolfol.Rowscount() <= 0) {
                                            try {
                                                //sql_checklineef = "select eolline,eolopno,folline,folopno from t_linefoleolsetbak where eolline=:charvalue and eolopno=:opno";
                                                //String[] eolitem = { charvalue, opno };
                                                //checkeolfol = oraconn.OracleExecSqlparmer(sql_checklineef, eolitem, 2, SFCStaticdata.staticmember.odbname);
                                                checkeolfol = cwa.CallRDT("checkin_submit_58", charvalue, opno);
                                                if (checkeolfol.Rowscount() > 0) {
                                                    String Folline = "";
                                                    for (int m = 0; m < checkeolfol.Rowscount(); m++) {
                                                        if (m == checkeolfol.Rowscount() - 1) {
                                                            Folline += checkeolfol.Rows(m).get_CellValue("folline");
                                                            continue;
                                                        }
                                                        Folline += checkeolfol.Rows(m).get_CellValue("folline") + " / ";
                                                    }
                                                    ShowMessage("EOL線別" + charvalue + "只能過FOL線別：" + Folline + "請重新選擇");
                                                    return;
                                                } else {
                                                    ShowMessage("EOL線別" + charvalue + "尚未設定FOL對應線別，請聯繫MIS");
                                                    return;
                                                }
                                            } catch (Exception ex) {
                                                ShowMessage("查詢EOL對應FOL線別時發生錯誤！");
                                                return;
                                            }
                                        }
                                    } catch (Exception ex) {
                                        ShowMessage("驗證EOL對應FOL線別時發生錯誤！");
                                        return;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ShowMessage("驗證IP信息時發生錯誤");
                            return;
                        }
                    }
                } catch (Exception ex) {
                    ShowMessage("查詢特性資料時發生錯誤!");
                    return;
                }
            }

            DataTable uplf = cwa.CallRDT("getuplinedata");
            String uplineflag = "";
            if (uplf != null && uplf.Rowscount() > 0) {
                String upitem = uplf.Rows(0).get_CellValue("item").trim();
                String uplatt1 = uplf.Rows(0).get_CellValue("att1").trim();
                if (uplatt1.equals(SFCStaticdata.staticmember.deviceno) && upitem.equals(opno)) {
                    //DataTable exdt = new BLL.basedata().getexcielinedata(lotno, opno);
                    DataTable exdt = cwa.CallRDT("getexcielinedata", lotno, opno);
                    if (exdt != null && exdt.Rowscount() > 0) {
                        uplineflag = "1";
                    }
                }
            }
            if (uplineflag.equals("1")) {
                execsql = execsql + "insert into t_lotequplinelog_log values"
                        + "('" + lotno + "','" + opno + "','" + charno + "','" + charname + "','" + charvalue + "','" + userid + "',sysdate,null,null,null);"
                        + " update t_lotequplinelog set charvalue ='" + charvalue + "' where lotno ='" + lotno + "' and opno ='" + opno + "' ;";
            } else {
                if (linesametype.equals("1")) //FOL  SMT  如果有選擇纖體就放過
                {

                } else {
                    execsql = execsql + "insert into t_lotequplinelog values('" + lotno + "','" + opno + "','" + charno + "','" + charname + "','" + charvalue + "','" + userid + "',sysdate,null,null,null);";
                }
            }
        }

        execsql = execsql + "end;";
        if (bindingdataGridViewRowscount > 0) {
            //卡FC纖體是否和選擇的一致
            DataTable dt_opuser2 = cwa.CallRDT("checkin_submit_59", opno, SFCStaticdata.staticmember.deviceno);
            List<String> liststr = new ArrayList<String>();

            if (dt_opuser2.Rowscount() > 0) {
                // String sql_select_line = "select lotno,linename from t_oplinedata where lotno = :lotno";
                //String[] array_selectline = { lotnotextBox.Text.toString().trim() };
                //DataTable dt_new = new DataTable();
                //dt_new = new DAL.Ora_dal().OracleExecSqlparmer(sql_select_line, array_selectline, array_selectline.length(), SFCStaticdata.staticmember.odbname.toString());
                DataTable dt_new = cwa.CallRDT("checkin_submit_60", lotno);

                if (dt_new.Rowscount() > 0) {
                    for (int i = 0; i < dt_new.Rowscount(); i++) {
                        liststr.add(dt_new.Rows(i).get_CellValue("linename").trim());
                    }
                }

                if (liststr.contains(charvalue)) {
                } else {
                    ShowMessage("請確認選擇纖體是否正確");
                    return;
                }
            }
            if (linesametype.equals("1")) {

            } else {
                Boolean charflag = false;
                try {
                    // String delloteqlog = "begin delete from t_lotequplinelog where lotno=:lotno and opno=:opno;end;";
                    //String[] delloteqlogparmers = { lotno, opno };
                    //Boolean deleqlogc5 = new DAL.Ora_dal().OracleExecVoidSqlparmer(delloteqlog, delloteqlogparmers, 2, SFCStaticdata.staticmember.odbname.toString());
                    Boolean deleqlogc5 = cwa.CallRB("checkin_submit_61", lotno, opno);
                    if (deleqlogc5) {
                        //charflag = new BLL.basedata().execinfo(execsql, null, 0);
                        charflag = cwa.CallRB("execinfo", execsql);

                        if (!charflag) {
                            ShowMessage("存儲特性資料表t_lotequplinelog失敗，請聯繫MIS部門處理,Error:E0010");
                            //rebacklotbind(lotno, SFCStaticdata.staticmember.odbname);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    ShowMessage("執行過程中發生錯誤，請聯繫MIS進行退站處理");
                    return;
                }
            }
        }
        //// 耗材切換廠商 提示
        if (SFCStaticdata.staticmember.deviceno.equals("J94") || SFCStaticdata.staticmember.deviceno.equals("J95")) {
            String setting = cwa.CallRS("checkin_submit_62", opno);
            if (setting.equals("") && lotno.substring(lotno.length() - 2,lotno.length()).equals("01")) {
                String line_1 = cwa.CallRS("checkin_submit_63", lotno, opno);
                DataTable loglotdt = cwa.CallRDT("checkin_submit_64", opno, line_1);
                if (loglotdt.Rowscount() > 0) {
                    String lastlens = "";
                    String nowlens = "";
                    String lotlog = loglotdt.Rows(0).get_CellValue("lotno").trim();
                    if (setting.contains("1")) //// LENS
                    {
                        lastlens = cwa.CallRS("checkin_submit_64", lotlog);
                        nowlens = cwa.CallRS("checkin_submit_65", lotno);
                        if (lastlens == null || lastlens.equals("")) {
                            ShowMessage("未查询到之前批次所使用的镜头类型");
                        } else {
                            if (lastlens.equals(nowlens)) {
                                ShowMessage("鏡頭切換廠商，請確認是否有送首件");
                            }
                        }
                    }
                    if (setting.contains("2"))  //// 基板
                    {
                        lastlens = cwa.CallRS("checkin_submit_66", lotno);
                        nowlens = cwa.CallRS("checkin_submit_67", lotno);
                        if (lastlens == null || lastlens.equals("")) {
                            ShowMessage("未查询到之前批次所使用的基板类型");
                        } else {
                            if (lastlens.equals(nowlens)) {
                                ShowMessage("基板切換廠商，請確認是否有送首件");
                            }
                        }
                    }
                }
            }
        }

        String multisql = "begin  ";
        if (SFCStaticdata.staticmember.CheckInMultiMachinenoSelectBoolean && SFCStaticdata.staticmember.CheckInMultiMachineList.size() <= 0) {
            ShowMessage("尚未選擇多機台號信息");
            return;
        } else if (SFCStaticdata.staticmember.CheckInMultiMachinenoSelectBoolean && SFCStaticdata.staticmember.CheckInMultiMachineList.size() > 0) {
            String[] multidelparmer = {lotno, opno, multiMachinenodt.Rows(0).get_CellValue(1)};
            String multidelsql = "begin delete from t_lotequplinelog where lotno=:lotno and opno=:opno and characterid=:characteridnow;end;";
            for (int j = 0; j < SFCStaticdata.staticmember.CheckInMultiMachineList.size(); j++) {
                multisql = multisql + "insert into t_lotequplinelog values('" + lotno + "','" + opno + "','" + multiMachinenodt.Rows(0).get_CellValue(1) + "','" + multiMachinenodt.Rows(0).get_CellValue(0) + "','" +
                        SFCStaticdata.staticmember.CheckInMultiMachineList.get(j) + "','" + userid + "',sysdate,null,null,null);";
            }
            multisql = multisql + "end;";
            String[] multiparmer = {};
            //String[] multiparmerexec = { };
            try {
                Boolean delflagmulti = cwa.CallRB("checkout_submit_90", multidelsql, BaseFuncation.SerializeObjectArrayString(multidelparmer));
                if (delflagmulti) {
                    if (!cwa.CallRB("checkout_submit_90", multisql, BaseFuncation.SerializeObjectArrayString(multidelparmer))) {
                        ShowMessage("存儲多機台號信息異常，請聯繫MIS");
                        return;
                    }
                }
            } catch (Exception ex) {
                ShowMessage("執行過程中發生錯誤，請聯繫MIS進行退站處理");
                return;
            }
        }

        //20150722 加入Q退批號進站提醒
        DataTable riqrebackalerdt = new DataTable();
        Boolean riqccheckin = false;
        riqrebackalerdt.AddColumn("Deviceno");
        riqrebackalerdt.AddColumn("Lotno");
        riqrebackalerdt.AddColumn("站位");
        riqrebackalerdt.AddColumn("進站時間");
        if (SFCStaticdata.staticmember.engRilotcheckflag || SFCStaticdata.staticmember.englotcheckflag) {
            DataTable dt = new DataTable();
            String lotno1 = lotno;
            dt = cwa.CallRDT("checkin_submit_68", lotno);
            if (dt != null) {
                if (dt.Rowscount() > 0)
                    riqccheckin = true;
            }
        }

        if (staticopnoflowid == 1) {
            DataTable dtlot = cwa.CallRDT("checkin_submit_69", lotno);
            if (dtlot.Rowscount() == 0) {
                checksqlstatus = false;
                checksqlstatus = cwa.CallRB("checkin_submit_70", lotno, productno, opno, opnoflowid, dieqty, lotstate, userid, att1, att2, att3);
            } else {
                checksqlstatus = false;
                checksqlstatus = cwa.CallRB("checkin_submit_71", lotstate, userid, lotno);
            }
            if (!checksqlstatus) {
                ShowMessage("進站失敗");
                Close();
            } else {
                // 可能是Q  20150722
                if ((SFCStaticdata.staticmember.engRilotcheckflag || SFCStaticdata.staticmember.englotcheckflag) && riqccheckin) {
                    DataTable dt = cwa.CallRDT("checkin_submit_72", lotno);
                    if (dt.Rowscount() > 0) {
                        riqrebackalerdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, dt.Rows(0).get_CellValue("opname"), dt.Rows(0).get_CellValue("intime"));
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(riqrebackalerdt), "EM038", "[預警郵件]LH RI q退批號再次進站提醒" + SFCStaticdata.staticmember.deviceno, "0");
                    }
                }

                ShowMessage("進站成功");

                //首站更新TBLWIPRIVersion表，管控FIFO
                if (!SFCStaticdata.staticmember.fifoflag.equals("") && SFCStaticdata.staticmember.fifoflag != null) {
                    Boolean upwipflag = cwa.CallRB("upwipversiondata", lotno);
                    if (!upwipflag) {
                        ShowMessage("更新TBLWIPRIVersion表數據失敗，請聯繫MIS部門");
                    }
                }
                this.Close();
            }
        } else {
            checksqlstatus = cwa.CallRB("checkin_submit_71", lotstate, userid, lotno);
            if (!checksqlstatus) {
                ShowMessage("進站失敗");
                this.Close();
            } else {
                // 可能是Q  20150722
                if ((SFCStaticdata.staticmember.engRilotcheckflag || SFCStaticdata.staticmember.englotcheckflag) && riqccheckin) {
                    DataTable dt = cwa.CallRDT("checkin_submit_72", lotno);
                    if (dt.Rowscount() > 0) {
                        riqrebackalerdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, dt.Rows(0).get_CellValue("opname"), dt.Rows(0).get_CellValue("intime"));
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(riqrebackalerdt), "EM038", "[預警郵件]LH RI q退批號再次進站提醒" + SFCStaticdata.staticmember.deviceno, "0");
                    }
                }
                ShowMessage("進站成功");

                //VTQ 整體不良預警改在進站  HP 1-29
                Boolean vtqallholdfalg = false;
                // String aholdopno = new BLL.basedata().getallholdopnodata1(opno);  //在FOL的FQC，EOL的Pack站位才Hold
                String aholdopno = cwa.CallRS("getallholdopnodata1", opno);  //在FOL的FQC，EOL的Pack站位才Hold
                if (!aholdopno.equals("") && aholdopno.equals("")) {
                    String holdfalg = "";
                    //DataTable qchdt = new BLL.basedata().getqcholddata(lotno, opno);  //如果在此之前有被Hold,就放過
                    DataTable qchdt = cwa.CallRDT("getqcholddata", lotno, opno);  //如果在此之前有被Hold,就放過
                    if (qchdt != null && qchdt.Rowscount() > 0) {
                        String stylefalg = qchdt.Rows(0).get_CellValue("STYLEFLAG").trim();
                        String qchatt1 = qchdt.Rows(0).get_CellValue("att1").trim();

                        if (qchatt1.equals("VTQ整體")) {
                            holdfalg = "1";
                        }
                    }
                    //整体停線預警
                    if (SFCStaticdata.staticmember.qcholddefalg.equals("2") &&  SFCStaticdata.staticmember.deviceno.equals("VT-Q") && holdfalg.equals("")) {
                        DataTable dtRIstopdt = new DataTable();
                        String messagestring1 = "整體不良率超標情況:\r\n";

                        DataTable VTQalerdt = new DataTable();
                        DataTable VTQstopdt = new DataTable();

                        VTQalerdt.AddColumn("Deviceno");
                        VTQalerdt.AddColumn("Lotno");
                        VTQalerdt.AddColumn("Station Name");
                        VTQalerdt.AddColumn("Qty In");
                        VTQalerdt.AddColumn("ErrorNoName");
                        VTQalerdt.AddColumn("NGQty");
                        VTQalerdt.AddColumn("NGYield");
                        VTQalerdt.AddColumn("Alert Line NGYield");
                        VTQalerdt.AddColumn("Line");
                        VTQalerdt.AddColumn("ReMark");

                        VTQstopdt.AddColumn("Deviceno");
                        VTQstopdt.AddColumn("Lotno");
                        VTQstopdt.AddColumn("Station Name");
                        VTQstopdt.AddColumn("Qty In");
                        VTQstopdt.AddColumn("ErrorNoName");
                        VTQstopdt.AddColumn("NGQty");
                        VTQstopdt.AddColumn("NGYield");
                        VTQstopdt.AddColumn("Stop Line NGYield");
                        VTQstopdt.AddColumn("Line");
                        VTQstopdt.AddColumn("ReMark");
                        VTQstopdt.AddColumn("Code");

                        String dtRIqty = dietextBoxText;

                        String num = cwa.CallRS("checkin_submit_73");
                        num=BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num,"yyyyMMdd").getTime(),"yyyyMMdd");

//                            num = Convert.ToDateTime(num).ToString("yyyyMMdd");
                        //numsql = "select substr(att4,7,12) numbe from t_qcholdlotno where att4 like '%ATE001%' order by att4 desc";
                        //String[] numberparmer = { };
                        // String numbe = new DAL.Ora_dal().OracleExecstringparmer(numsql, numberparmer, 0, "VTQCONN");
                        String numbe = cwa.CallRS("checkin_submit_74");
                        if (numbe.equals("") || numbe == null) {
                            numbe = "0001";
                        } else {
                            if (!num.equals(numbe.substring(0, 8))) {
                                numbe = "0001";
                            } else {
                                int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, numbe.length())) + 1;
                                numbe = ("0000" + String.valueOf(number1));
                                numbe = numbe.substring(numbe.length() - 4,numbe.length());
                            }
                        }
                        String vtqinqtyzt = "";
                        String VTQLOTNOZ = lotno;
                        String VTQLOTZ = VTQLOTNOZ.substring(1, 12);
                        // String vtqinqtysqlz = "select INPUTQTY from T_LOTLOG WHERE opnoflowid = '1' AND LOTNO =:lotno ";
                        //String[] vtqinptyzparmer = { "P" + VTQLOTZ };
                        //DataTable vtqinqtysqlzt = new DAL.Ora_dal().OracleExecSqlparmer(vtqinqtysqlz, vtqinptyzparmer, vtqinptyzparmer.length(), SFCStaticdata.staticmember.odbname);
                        DataTable vtqinqtysqlzt = cwa.CallRDT("checkin_submit_75", VTQLOTZ);
                        if (vtqinqtysqlzt != null) {
                            if (vtqinqtysqlzt.Rowscount() > 0) {
                                vtqinqtyzt = vtqinqtysqlzt.Rows(0).get_CellValue("INPUTQTY");
                            } else {
                                vtqinqtyzt = "0";
                            }
                        } else {
                            vtqinqtyzt = "0";
                        }


                        //獲取截止目前的Setup總數
                        // String setupsum = new BLL.basedata().getsetupsumdata(lotno);
                        String setupsum = cwa.CallRS("getsetupsumdata", lotno);
                        if (setupsum.equals("")) {
                            setupsum = "0";
                        }

                        String numberdt = "ATE001" + num + numbe;
                        String dtRIlot = lotno;
                        String dtRIopno = cwa.CallRS("getatt18data", opno);
                        if (dtRIopno.equals("") || dtRIopno == null) {
                            dtRIopno = opno;
                        }
                        String dtRIlotno = lotno;


                        DataTable dtRIdefqty = cwa.CallRDT("checkin_submit_76", VTQLOTZ);
                        int vtqstrz = 0;
                        if (dtRIdefqty != null) {
                            if (dtRIdefqty.Rowscount() > 0) {
                                try {
                                    vtqstrz = Integer.parseInt(dtRIdefqty.Rows(0).get_CellValue("ngqty").trim());
                                } catch (Exception ex) {
                                    vtqstrz = 0;
                                }
                            }
                        }

                        String line = cwa.CallRS("checkin_submit_77", dtRIlotno);
                        int ngsinglesql1 = 12;
                        List<String> VTQparmer = new ArrayList<String>();

                        DecimalFormat df = new DecimalFormat("#.##");
                        String ngyield1 = df.format((((vtqstrz) / (Double.parseDouble(vtqinqtyzt) - Double.parseDouble(setupsum))) * 100));

                        String VTQholdsql = "begin ";


                        if ((Double.parseDouble(ngyield1) > (ngsinglesql1))) {
                            VTQstopdt.AddRow(SFCStaticdata.staticmember.deviceno,
                                    dtRIlot,
                                    opnametextBoxText,
                                    vtqinqtyzt,
                                    "整體不良率",
                                    String.valueOf(vtqstrz),
                                    ngyield1 + "%",
                                    String.valueOf(ngsinglesql1) + "%",
                                    line,
                                    "請立即停機改善",
                                    numberdt);
                            //messagestring1 = messagestring1 + new BLL.basedata().getopname(dtRIopno) + "站位投入為" + dietextBox.Text.toString().trim() + "," + "" + "的不良數量為" + dtRIdefqty + ",不良率為" + ngyield1 + ",超過停線不良率(Stop line:" + ngsinglesql1 + ")\r\n";
                            messagestring1 = messagestring1 + cwa.CallRS("getopname", dtRIopno) + "站位投入為" + dietextBoxText + "," + "" + "的不良數量為" + dtRIdefqty + ",不良率為" + ngyield1 + ",超過停線不良率(Stop line:" + ngsinglesql1 + ")\r\n";

                            VTQholdsql = VTQholdsql + "insert into t_qcholdlotno "
                                    + " (lotno,opno,yield,inuptqty,dbqty,createdate,styleflag,valuesflag,errorno,checkuser,checkip,unholddate,unholduser,unholdip,att1,att2,att3,att4) "
                                    + " values (:lotno,:opno,:VTQNG,:vtqinqty,:b,sysdate,'0','1',:errorno,:userid,:ip,null,null,null,'VTQ整體','Open',:odbname,:numberdt);";
                            VTQparmer.add(dtRIlot);
                            VTQparmer.add(dtRIopno);
                            VTQparmer.add(ngyield1 + "%");
                            VTQparmer.add(vtqinqtyzt);
                            VTQparmer.add(String.valueOf(vtqstrz)
                            );
                            VTQparmer.add("");
                            VTQparmer.add(SFCStaticdata.staticmember.userid);
                            VTQparmer.add(ip);
                            VTQparmer.add(SFCStaticdata.staticmember.deviceno);
                            VTQparmer.add(numberdt);
                            vtqallholdfalg = true;
                        }

                        if (vtqallholdfalg) {//执行预警邮件发送
                            if (SFCStaticdata.staticmember.lotcheckeolflag)//EOL
                            {
                                if (VTQstopdt.Rowscount() > 0) {
                                    //new BLL.basedata().execSendMailAll(VTQstopdt, "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBox.Text.toString().trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                                    cwa.CallRB("execSendMailAll",
//                                                JsonConvert.SerializeObject(VTQstopdt),
                                            BaseFuncation.SerializeObjectDataTable(VTQstopdt),
                                            "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBoxText.trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                                }
                            } else if (SFCStaticdata.staticmember.lotfolbingpicheck)//FOL
                            {

                                if (VTQstopdt.Rowscount() > 0) {
                                    //new BLL.basedata().execSendMailAll(VTQstopdt, "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBox.Text.toString().trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                                    cwa.CallRB("execSendMailAll",
//                                                JsonConvert.SerializeObject(VTQstopdt),
                                            BaseFuncation.SerializeObjectDataTable(VTQstopdt),
                                            "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBoxText.trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                                }
                            } else//smt
                            {

                                if (VTQstopdt.Rowscount() > 0) {
                                    //new BLL.basedata().execSendMailAll(VTQstopdt, "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBox.Text.toString().trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                                    cwa.CallRB("execSendMailAll",
                                            BaseFuncation.SerializeObjectDataTable(VTQstopdt),
                                            "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBoxText.trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                                }
                            }

                            if (VTQstopdt.Rowscount() > 0) {
                                ShowMessage(messagestring1);
                                VTQholdsql = VTQholdsql + "end;";
                                Boolean holdBoolean = cwa.CallRB("checkout_submit_90", VTQholdsql,
                                        BaseFuncation.SerializeObjectDataTable(VTQstopdt)
                                );
                                if (!holdBoolean) {
                                    ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                    if (!cwa.CallRB("deldefectwipsn", lotno, opno, "批號Hold失敗(VTQ)")) {
                                        ShowMessage("回溯不良資料異常，請聯繫MIS");
                                    }
                                    return;
                                }

                                Boolean upqhfalg = cwa.CallRB("upqcholdstatelotnodata", lotno);
                                if (!upqhfalg) {
                                    ShowMessage("更改批號Hold狀態時發生錯誤，請聯繫MIS部門");
                                    return;
                                }
                            }
                        }
                    }
                }
                this.Close();
            }
        }
    }

    private void selectopno(String opnono, String lotno) throws Exception {
        SFCStaticdata.staticmember.useflag = false;
        SFCStaticdata.staticmember.EolcheckFolLine = false;
        DataTable opdt = new DataTable();
        String opnostr = opnono;
        try {
            opdt = cwa.CallRDT("checkin_selectopno", opnostr);
        } catch (Exception ex) {
            ShowMessage("獲取特性資料失敗!Error:E0013");
            return;
        }
        removeAllViewsInLayout(bindingdataGridView);
        if (opdt.Rowscount() == 0) {
            return;
        }

        DataTable dtsqldb = cwa.CallRDT("checkin_selectopno_3");
        if (dtsqldb.Rowscount() > 0) {
            setclickable(comboBoxline,true); //setEnabled(comboBoxline,true);
            setAdapter(comboBoxline,
                    BaseFuncation.setvalue(dtsqldb, null, "charactervalue", checkin.this)
            );
            setSelection(comboBoxline, 0);
        }

        DataTable chardt;
        setText((TextView) v.findViewById(R.id.listviewcheckinmname1), "");
        for (int i = 0; i < opdt.Rowscount(); i++) {
            if (Integer.parseInt(opdt.Rows(i).get_CellValue("att").trim()) == 1)   //  需要檢測是否有權限的 選擇 線別
            {
                SFCStaticdata.staticmember.useflag = true;
            }
            if (!opdt.Rows(i).get_CellValue("location").trim().equals("") && opdt.Rows(i).get_CellValue("location").trim() != null) {
                if (Integer.parseInt(opdt.Rows(i).get_CellValue("location").trim()) == 2)     // EOL 要確定FOL線別是否可以同時過
                {
                    if (lotno.substring(0, 2).trim().toUpperCase().equals("AK")) {
                        SFCStaticdata.staticmember.EolcheckFolLine = true;
                    }
                }
            }

            String specatt2 = opdt.Rows(i).get_CellValue("att2").trim();
            String machinetype = opdt.Rows(i).get_CellValue("charactername").trim();
            String characteridstr = opdt.Rows(i).get_CellValue("characterid").trim();
            String dbnamenow = "";



            setText((TextView) v.findViewById(R.id.listviewcheckinmname1), opdt.Rows(i).get_CellValue("characterid").trim());
            setText((TextView) v.findViewById(R.id.listviewcheckinmname2), opdt.Rows(i).get_CellValue("charactername").trim());
            //setText((TextView) v.findViewById(R.id.listviewcheckinmname1), "");

            chardt = new DataTable();
            try {
                //chardt = cwa.CallRDT("checkin_selectopno_2", specatt2, SFCStaticdata.staticmember.ip, machinetype, characteridstr);
                chardt = cwa.CallRDT("checkin_selectopno_2", specatt2, SFCStaticdata.staticmember.HDSerialNo, machinetype, characteridstr);
            } catch (Exception ex) {
                ShowMessage("獲取站位特性值失敗!Error:E0014");
            }
            //List<String> spinnerList=FixDataProvider.GetTenNumberList();
            //ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,(Spinner) v.findViewById(R.id.listviewcheckinmname3),spinnerList);
            setAdapter(
                    (Spinner) v.findViewById(R.id.listviewcheckinmname3),
                    BaseFuncation.setvalue(chardt, null, "charactervalue", checkin.this)
            );

            addView(bindingdataGridView, v);
        }
    }

    private void Checkin_Load() throws Exception {
        String opno = getText(opnotextBox).toString().trim().toUpperCase();
        String lotno = getText(lotnotextBox).toString().trim().toUpperCase();
        String deviceno = getText(devicenotextBox).toString().trim().toUpperCase();
        String ip=SFCStaticdata.staticmember.HDSerialNo;//SFCStaticdata.staticmember.ip;
        DataTable dt_checkoverWafer = cwa.CallRDT("checkin_load_1", opno);
        if (dt_checkoverWafer.Rowscount() > 0) {
            setclickable(ComboBox_waferlotno, true);//setEnabled(ComboBox_waferlotno, true);
            setVisibility(ComboBox_waferlotno, View.VISIBLE);

            setVisibility(text_waferlotno, View.VISIBLE);


            String MoterLotno = lotno;
            if (MoterLotno.startsWith("PK")) {
                MoterLotno = MoterLotno.substring(1, 9).trim();
            } else {
                MoterLotno = MoterLotno.substring(0, 9).trim();
            }
            DataTable dt_wafer = cwa.CallRDT("checkin_load_2", MoterLotno);

            if (dt_wafer.Rowscount() > 0) {

                setAdapter(ComboBox_waferlotno,
                        BaseFuncation.setvalue(dt_wafer, null, "waferlotno", checkin.this)
                );
                setSelection(ComboBox_waferlotno, -1);
            }
        }

        SFCStaticdata.staticmember.CheckInMultiMachinenoSelectBoolean = false;
        multiMachinenodt = isMultiMachineSelectStation(opno);
        if (multiMachinenodt.Rowscount() > 0 && (SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.engRilotcheckflag)) {
            SFCStaticdata.staticmember.CheckInMultiMachinenoSelectBoolean = true;
            String tempopno = opno;
            String tempdev = deviceno;
            if (tempopno.equals("0610") && tempdev.equals("RI-A")) {
            } else {


                SFCStaticdata.staticmember.CheckInMultiMachinenoSelectBoolean = true;
                //// TODO: 2017/4/19  new class open no fol page return
//                    SMT.StationMultMachinenoSelect st = new SFConline.SMT.StationMultMachinenoSelect(multiMachinenodt.Rows(0).get_CellValue(0).toString(), multiMachinenodt.Rows(0).get_CellValue(2).toString(), multiMachinenodt.Rows(0).get_CellValue(3).toString());
//                    st.ShowDialog();
                if (SFCStaticdata.staticmember.CheckInMultiMachineList.size() <= 0) {
                    ShowMessage("尚未選擇機台號");
                    this.Close();
                }
            }
        }
        setEnabled(textBox_dbqty, false);
        //DataTable dt = new BLL.basedata().getuseridinfo(lotno);
        DataTable dt = cwa.CallRDT("getuseridinfo", lotno);
        SFCStaticdata.staticmember.updatelotuserflag = false;
        if (dt != null) //
        {
            if (dt.Rowscount() > 0) {
                String lotstate = dt.Rows(0).get_CellValue("userstate").trim();
                if (lotstate.equals("1")) {
                    if (dt.Rows(0).get_CellValue("ipaddress").equals(ip)) {
                        SFCStaticdata.staticmember.updatelotuserflag = true;
                        return;
                    }
                } else if (lotstate.equals("0")) {
                    //Boolean uplotlogon = new BLL.basedata().updateuserlogoinfo(lotno);
                    Boolean uplotlogon = cwa.CallRB("updateuserlogoinfo", lotno,ip);
                    if (uplotlogon) {
                        SFCStaticdata.staticmember.updatelotuserflag = true;
                        return;
                    } else {
                        SFCStaticdata.staticmember.updatelotuserflag = false;
                        return;
                    }
                }
            } else {
                //Boolean inslotstate = new BLL.basedata().checkuseridinfo(lotno);
                Boolean inslotstate = cwa.CallRB("checkuseridinfo", lotno, ip);
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

    private void checkBox_dblotno_CheckedChanged() throws Exception {
        if (isChecked(checkBox_dblotno)) {
            setEnabled(textBox_dbqty, true);
            setFocusable(textBox_dbqty, true);
            setText(textBox_dbqty, "0");
            return;
        } else {
            setEnabled(textBox_dbqty, false);
            return;
        }
    }

    private void Checkin_FormClosing() throws Exception {
        String lotno = getText(lotnotextBox).toString().trim().toUpperCase();
        if (SFCStaticdata.staticmember.updatelotuserflag) {
            //Boolean inslotstate = new BLL.basedata().updateuserid(lotno);
            Boolean inslotstate = cwa.CallRB("updateuserid", lotno);
            if (!inslotstate) {
                ShowMessage("批號佔用狀態失敗，請聯繫MIS!");
                this.Close();
                return;
            }
        }
    }

    private void textBox_follot_Leave() throws Exception {
        String lotnosfc = getText(textBox_follot).toString().trim().toUpperCase();
        if (lotnosfc == null || lotnosfc.equals("")) {
            ShowMessage("請輸入FOL母批號");
            return;
        }
        DataTable dtwip = cwa.CallRDT("checkin_follot_Leave_1", lotnosfc);
        if (dtwip.Rowscount() <= 0) {
            ShowMessage("請輸入9位的母批號或該批號WIP尚未分批作業");
            setText(textBox_follot, "");
            return;
        } else {
            if (lotnosfc.startsWith("K")) {
                lotnosfc = "P" + lotnosfc;
            }
            //J17前段公用j16  ljj20150307  repair
            String tempdbname = SFCStaticdata.staticmember.odbname;
            if (SFCStaticdata.staticmember.odbname.equals("J17CONN")) {
                tempdbname = SFCStaticdata.staticmember.odbname;
                SFCStaticdata.staticmember.odbname = "J1617CONN";
            }
            DataTable dtsfc = cwa.CallRDT("checkin_follot_Leave_2", lotnosfc, SFCStaticdata.staticmember.odbname);
            SFCStaticdata.staticmember.odbname = tempdbname;
            Boolean bl = true;
            if (dtsfc.Rowscount() != dtwip.Rowscount()) {
                ShowMessage("該母批WIP子批號數量為" + String.valueOf(dtwip.Rowscount()) + ",SFC過站系統中存在過站數據的批號的數量為" + String.valueOf(dtsfc.Rowscount()) + "，數量不符");
                setText(textBox_follot, "");
                return;
            } else {
                for (int i = 0; i < dtsfc.Rowscount(); i++) {
                    if (dtsfc.Rows(i).get_CellValue("opno").equals("INV")) {
                        bl = false;
                        break;
                    }
                }
            }
            if (!bl) {
                ShowMessage("該母批中仍有部分子批尚未INV，不可以綁定後段子批號");
                setText(textBox_follot, "");
                return;
            }
        }
    }

    public String getprocessflowid(String opno) throws Exception {
        return cwa.CallRS("checkin_getprocessflowid", opno);
    }

    private void checkBox1_CheckedChanged() throws Exception {
        if (isChecked(checkBox1)) {
            guigestr = "B";
            checkBox2.setChecked(false);
        }
    }

    private void checkBox2_CheckedChanged() throws Exception {
        if (isChecked(checkBox2)) {
            guigestr = "B";
            checkBox1.setChecked(false);
        }
    }

    private Boolean checkVtq6037holdBoolean(String lotno, String opno) throws Exception {
        DataTable dt_hold = cwa.CallRDT("check_checkVtq6037holdbool", opno, lotno);
        Boolean update_flag = false;
        if (dt_hold.Rowscount() > 0) {
            update_flag = cwa.CallRB("check_checkVtq6037holdbool_2", lotno);
        }

        return update_flag;
    }

    private Boolean getmagazineflag(String opno) throws Exception {
        Boolean flag = false;
        String att2 = cwa.CallRS("checkin_getmagazineflag", opno);

        if (att2.equals("1")) {
            flag = true;
        }
        return flag;
    }

    private Boolean getmagazinelotflag(String lotno) throws Exception {
        Boolean flag = false;
        String item = cwa.CallRS("checkin_getmagazinelotflag", lotno, getText(opnotextBox).toString().trim());
        if (item.equals(lotno)) {
            flag = true;
        }
        return flag;
    }
}