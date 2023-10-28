package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/19.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class dbdefectinput extends ActivityInteractive {

    private Boolean _loadingstatus = false;
    private CallWebapi cwa;
    private String _deviceno;
    private String _newdeviceno;
    private String _odbname;
    private String _lotno;
    private String _staticdefectqty;
    private String _opno;

    private TextView textBox_loton;
    private TextView textBox_opno;
    private EditText textBox_defectqty;

    private EditText textBox_input;
    private Button buttonclear;
    private LinearLayout defectvaluegv;
    private Button submit;

    private String ipstatic;
    private String HDSerialNo;
    private int opnoflowid;

    private String lotno;
    private String nowopno;
    private String dieqtyoutnew;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_dbdefectinput);
            ipstatic = NetUtils.getLocalIPAddress(this);
//            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");

            textBox_loton = (TextView) findViewById(R.id.isfc_dbdefectinput_lotnotbv1);
            textBox_opno = (TextView) findViewById(R.id.isfc_dbdefectinput_opnotbv1);
            textBox_defectqty = (EditText) findViewById(R.id.isfc_dbdefectinput_defectqtytv1);
            textBox_input=(EditText)findViewById(R.id.isfc_dbdefectinput_inputtbv1);
            defectvaluegv = (LinearLayout) findViewById(R.id.isfc_dbdefectinput_stationviewll1);
            submit = (Button) findViewById(R.id.isfc_defectinput_submitbt);

            Bundle b = getIntent().getExtras();
            String[] ss=b.getStringArray("sendvalue");
            _deviceno = ss[0];
            _newdeviceno = ss[1];
            _odbname = ss[2];
            lotno = ss[3];
            nowopno = ss[4];
            dieqtyoutnew = ss[5];
            opnoflowid = Integer.parseInt(ss[6]);


            cwa = new CallWebapi(_deviceno, _newdeviceno, _odbname, this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    loaddata();
                    _loadingstatus = true;
                }
            }, "Check In Loading Task");

           /* defectvaluegv.setOnFocusChangeListener(new View.OnFocusChange
           Listener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus)
                    {
                        if(_loadingstatus)
                        {
                            vieworder();
                        }
                    }
                }
            });*/

        } catch (Exception ex) {
            BaseFuncation.showmessage(this, ex.getMessage());
            finish();
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

    private void vieworder()
    {
        try {
            List<SFCStaticdata.defectdata> ld = new ArrayList<SFCStaticdata.defectdata>();
            SFCStaticdata.defectdata tld;
            for (int i = 0; i < defectvaluegv.getChildCount(); i++) {
                tld = new SFCStaticdata().new defectdata();
                tld.errorqty = Integer.parseInt(((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname3)).getText().toString().trim());
                tld.errorname = ((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname2)).getText().toString().trim();
                tld.errorcode = ((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1)).getText().toString().trim();
                ld.add(tld);
            }
            Collections.sort(ld, new ComparatorValues());

            defectvaluegv.removeAllViewsInLayout();
            View gv;
            for (SFCStaticdata.defectdata d : ld
                    ) {
                gv = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_defectinput, null);
                ((TextView) gv.findViewById(R.id.listviewdefectinputmname1)).setText(d.errorcode);
                ((TextView) gv.findViewById(R.id.listviewdefectinputmname2)).setText(d.errorname);
                ((TextView) gv.findViewById(R.id.listviewdefectinputmname3)).setText(d.errorqty);
                defectvaluegv.addView(gv);
            }
        }
        catch(Exception ex)
        {
            BaseFuncation.showmessage(this,"Defect View order error");
        }
    }

    public static final class ComparatorValues implements Comparator<SFCStaticdata.defectdata> {
        @Override
        public int compare(SFCStaticdata.defectdata object1, SFCStaticdata.defectdata object2) {
            int m1 = object1.errorqty;
            int m2 = object2.errorqty;
            int result = 0;
            if (m1 > m2) {
                result = 1;

            }
            if (m1 < m2) {
                result = -1;
            }
            return result;
        }
    }


    private void loaddata() throws  Exception
    {
        setText(textBox_loton,lotno);
        setText(textBox_opno,nowopno);
        setText(textBox_input,dieqtyoutnew);

        DataTable errdt = new DataTable();
        errdt=cwa.CallRDT("getdberrorcode",nowopno);
        for(int i=0;i<errdt.Rowscount();i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_defectinput, null);
            setText((TextView) view.findViewById(R.id.listviewdefectinputmname1), errdt.Rows(i).get_CellValue("errorno"));
            setText((TextView) view.findViewById(R.id.listviewdefectinputmname2), errdt.Rows(i).get_CellValue("errorname"));
            addView(defectvaluegv, view);
        }
    }

    public void dbdefesubtmi(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    submitclick();
                }
            }, "dbdefectinput submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, " Error:" + ex.getMessage());
        }
    }

    private void submitclick() throws Exception {
        String datenew = cwa.CallRS("getdbsystime");
        String[] bb = datenew.split(":|-| ");
        String date = bb[3] + bb[4] + bb[5];

        String inputqtydb = getText(textBox_input).toString().trim();
        String opno = getText(textBox_opno).toString().trim();
        String lotno = getText(textBox_loton).toString().trim();
        String ngqty = getText(textBox_defectqty).toString().trim();
        int defectvaluegv1RowsCount = getChildCount(defectvaluegv);

        String lotnosub = date + lotno;
        String lotserial = lotno + "-" + BaseFuncation.padLeft(String.valueOf(opnoflowid), 3, '0');
        if (inputqtydb.isEmpty()) {
            ShowMessage("请输入DB数量");
            setFocusable(textBox_input, true);
            return;
        }

        if (ngqty.isEmpty()) {
            ShowMessage("请输入不良数量");
            setFocusable(textBox_input, true);
            return;
        }
        else
        {
            try {
                Integer.parseInt(ngqty);
            } catch (Exception ex) {
                ShowMessage("不良数量输入异常");
                setFocusable(textBox_defectqty, true);
                return;
            }
        }

        if (defectvaluegv1RowsCount > 0) {
            int defnum = 0;
            String sql = "begin update LotnoSpecialRecords set lotno='" + lotnosub + "' where lotno='" + lotno + "' and opno='" + opno + "' and recordtype='1';update t_lotdbdefectdata set lotno='" + lotnosub + "' where lotno='" + lotno + "' and opno='" + opno + "'; insert into LotnoSpecialRecords values('" + lotno + "','1','" + ipstatic + "','" + opno + "',sysdate,'" + ngqty + "','" + inputqtydb + "','','','','','');";
            for (int i = 0; i < defectvaluegv1RowsCount; i++) {

                String errorno = getText((TextView) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname1)).toString().trim();
                String errqty = getText((EditText) defectvaluegv.getChildAt(i).findViewById(R.id.listviewdefectinputmname3)).toString().trim();
                if(errqty!=null && !"".equals(errqty)) {
                    defnum = defnum + Integer.parseInt(errqty);
                    sql += "insert into t_lotdbdefectdata values('" + ngqty + "','" + lotno + "','" + opno + "','" + errorno + "','" + errqty + "',sysdate,'1','" + lotserial + "','');";
                }
            }

            sql += "end;";

            if (defnum != Integer.parseInt(ngqty)) {
                ShowMessage("輸入不良數量不符合");
                return;
            } else {
                try {
                    String[] parmersss = {};
                    Boolean inserlotdblog = cwa.CallRB("basefunction_execsql_ora_b", sql, _odbname);
                    if (!inserlotdblog) {
                        SFCStaticdata.staticmember.lotyicinginfo = false;
                        ShowMessage("批号一次不良记录保存失败，请联系MIS！");
                    } else {
                        SFCStaticdata.staticmember.lotyicinginfo = true;
                    }
                } catch (Exception ex) {
                    SFCStaticdata.staticmember.lotyicinginfo = false;
                    ShowMessage("记录批号DB记录时发生错误");
                }
            }
        } else {
            ShowMessage("請輸入不良原因");
            return;
        }
           Close();
    }
}

