package app.dpapp;

import static android.widget.Toast.makeText;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.exechttprequest;
import app.cmapp.parameterclass.httprequestinputdata;
import app.dpapp.Interface.Iobjectrhandler;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.Exectempfile;
import app.dpapp.appcdl.FreedomSOAPCallBack;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.appcdl.uploadimage;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.utils.ImageUtils;

/**
 * Created by Tod on 2016/4/15.
 */
public class eqchecklist extends AppCompatActivity implements Iobjectrhandler, FreedomSOAPCallBack {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String Site;
    protected String Eqid;
    protected String Eqname;
    protected String Eqopname;
    protected String Eqlinename;
    protected String Deviceno;
    private String flag;
    protected String Checkdataid;
    protected String SOAPSENDSTR;
    protected String SOAPRETURNSTR;
    protected String Filerevsion;

    protected TextView eqopnametv1;
    protected TextView eqlinanemtv1;
    protected TextView eqfileversiontv1;
    protected TextView eqcheckdheadertv1, mTv_deviceno;
    private String sysErrorcode; // 1:获取资料异常
    protected int clicktimes;  //  提交次数  提交失败递增，提交成功就初始化为0

    private View scontextview;
    private String mFilePath = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
    public static final String PATHNEW = "/MyImage/";
    //定义数据源
    //protected List<Slist> Sourcelist;
    private ListView mListView;
    private final int FROM_ALBUM = 1;//表示从相册获取照片
    private final int FROM_CAMERA = 2;//表示从相机获取照片
    private String mItem, mDefectName = "4.系統異常";
    private String returnstr;
    private String path = Environment.getExternalStorageDirectory() + PATHNEW;
    private String camera_photo_name;// 保存的名称

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    class formdata {
        private String header;
        private List<Slist> detaillist;

        public String getHeader() {
            return header;
        }

        public void setHeader(String v) {
            header = v;
        }

        public List<Slist> getDetaillist() {
            return detaillist;
        }

        public void setDetaillist(List<Slist> v) {
            detaillist = v;
        }
    }

    class Slist {
        private String Sessionuser;
        private String Eqid;
        private String Eqname;
        private String Sysdocumentid;
        private String Idept;
        private String Itemid;
        private String Itemname;
        private String Value;
        private String Valuespec;
        private String Valueshowpoint;
        private String Valueunit;
        private String TABLETYPE;
        private String att3;

        public String getAtt3() {
            return att3;
        }

        public void setAtt3(String att3) {
            this.att3 = att3;
        }

        public String getTABLETYPE() {
            return TABLETYPE;
        }

        public void setTABLETYPE(String TABLETYPE) {
            this.TABLETYPE = TABLETYPE;
        }

        public String getValueshowpoint() {
            return Valueshowpoint;
        }

        public void setValueshowpoint(String str1) {
            this.Valueshowpoint = str1;
        }

        public String getValuespec() {
            return Valuespec;
        }

        public void setValuespec(String str1) {
            this.Valuespec = str1;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String str1) {
            this.Value = str1;
        }

        public String getItemname() {
            return Itemname;
        }

        public void setItemname(String str1) {
            this.Itemname = str1;
        }

        public String getItemid() {
            return Itemid;
        }

        public void setItemid(String str1) {
            this.Itemid = str1;
        }

        public String getIdept() {
            return Idept;
        }

        public void setIdept(String str1) {
            this.Idept = str1;
        }

        public String getSysdocumentide() {
            return Sysdocumentid;
        }

        public void setSysdocumentid(String str1) {
            this.Sysdocumentid = str1;
        }

        public String getEqname() {
            return Eqname;
        }

        public void setEqname(String str1) {
            this.Eqname = str1;
        }

        public String getSessionuser() {
            return Sessionuser;
        }

        public void setSessionuser(String str1) {
            this.Sessionuser = str1;
        }

        public String getEqid() {
            return Eqid;
        }

        public void setEqid(String str1) {
            this.Eqid = str1;
        }

        public String getValueunit() {
            return Valueunit;
        }

        public void setValueunit(String str1) {
            this.Valueunit = str1;
        }

    }


    private Button bt1, bt2, bt3, bt4, bt5;
    private LinearLayout lly1;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checklist);
        try {
            StrictMode.VmPolicy.Builder builder =new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();

            Bundle bundle = getIntent().getExtras();
            Sessionuser = bundle.getString("Sessionuser");

            final Staticdata app = (Staticdata) getApplication();
            Site = app.usersite;

            Eqid = bundle.getString("Eqid");
            Eqname = bundle.getString("Eqname");
            Checkdataid = bundle.getString("checkdataid");

            Eqopname = bundle.getString("Eqopname");
            Eqlinename = bundle.getString("Eqlinename");
            flag = bundle.getString("flag");
            Filerevsion = bundle.getString("FILEVERSION");
            Deviceno = bundle.getString("DEVICENO");

            //           mListView = (ListView) findViewById(R.id.lv_check_list);
            eqopnametv1 = (TextView) findViewById(R.id.eq_checklistopnametv1);
            eqlinanemtv1 = (TextView) findViewById(R.id.eq_checklistlinenametv1);
            mTv_deviceno = (TextView) findViewById(R.id.tv_deviceno);
            eqfileversiontv1 = (TextView) findViewById(R.id.eq_checklistmemotv1);
            eqcheckdheadertv1 = (TextView) findViewById(R.id.eq_checklisttv1);

            //eqopnametv1.setText(Eqopname);HP 要求顯示具體的機台號而不是機台類型
            eqopnametv1.setText(Eqname);
            eqlinanemtv1.setText(Eqlinename);
            eqfileversiontv1.setText(Filerevsion);
            mTv_deviceno.setText(bundle.getString("device"));

            bt1 = (Button) findViewById(R.id.eq_checklistbutton1);
            bt2 = (Button) findViewById(R.id.eq_checklistrejectbt3);
            bt3 = (Button) findViewById(R.id.eq_checklistbutton2);
            bt4 = (Button) findViewById(R.id.eq_checklistbutton4);
            bt5 = (Button) findViewById(R.id.eq_checklistbutton3);
            if ("MP".equals(flag)) {
                bt1.setVisibility(View.GONE);
                bt2.setVisibility(View.GONE);
                bt3.setVisibility(View.GONE);
                bt4.setVisibility(View.GONE);
                bt5.setVisibility(View.GONE);
                eqopnametv1.setVisibility(View.GONE);
                eqlinanemtv1.setVisibility(View.GONE);
                eqfileversiontv1.setVisibility(View.GONE);
                eqcheckdheadertv1.setVisibility(View.GONE);

            }

            lly1 = (LinearLayout) findViewById(R.id.eqchecklistLinearLayout1);
            execloadactivity.opendialog(this, "正在執行");
            MyThreadPool.pool.execute(
                    new Runnable() {

                        @Override
                        public void run() {
                            List<Slist> ls1 = new ArrayList<>();
                            ls1 = getRemoteInfo();
                            Message msg = handle.obtainMessage();
                            msg.what = 0;
                            msg.obj = ls1;
                            handle.sendMessage(msg);
                        }
                    }
            );
            MyThreadPool.pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            Message msg = handle111.obtainMessage();
                            msg.what = 0;
                            msg.obj = null;
                            handle111.sendMessage(msg);
                        }
                    }
            );

            String METHOD_NAME = "getchecktitleandfooter";
            String[] pn1 = {"flowid"};
            String[] pv1 = {Checkdataid};
            getsoapdata(1, METHOD_NAME, pn1, pv1, 1);

        } catch (Exception ex1) {
        }
    }

    private Handler handle111 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                loadtempdata();
            } catch (Exception ex1) {

            } finally {
                execloadactivity.canclediglog();
            }
        }

    };

    private Handler handle2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            String str_result = msg.obj.toString();
            if (str_result.equals("0")) {
                Toast.makeText(eqchecklist.this, "EOLQC预警邮件发送成功", Toast.LENGTH_LONG);
            } else {
                Toast.makeText(eqchecklist.this, "EOLQC预警邮件发送失败", Toast.LENGTH_LONG);
            }
        }
    };


    /**
     * 接口回調方法
     *
     * @param parmerobject 依用戶需求定義
     * @param o            回傳結果參數
     */
    @Override
    public void execobject(Object parmerobject, Object o) {
        try {


            switch ((int) parmerobject) {
                //Save Tempfile
                case 0:
                    Toast.makeText(this, ((BaseFuncation.rrtype) o).get_rmsg(), Toast.LENGTH_SHORT).show();
                    break;
                //Load Tempfileto UI
                case 1:
                    loadtempdatatoui(o);
                    break;
                //Clear all cache file
                case 2:
                    Toast.makeText(this, ((BaseFuncation.rrtype) o).get_rmsg(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
        execloadactivity.canclediglog();
    }


    /**
     * 載入暫存資料成功顯示至UI
     *
     * @param
     */
    private void loadtempdatatoui(Object obj) {
        try {
            List<String> ls = (List<String>) obj;
            String[] strArray;
            String tempa, tempb;
            for (String s : ls
            ) {
                if (s == null || s.trim().equals(""))
                    continue;

//                tempa = s.substring(1, s.indexOf("],["));
//                tempb = s.substring(s.indexOf("],[") + 3, s.length() - 1);

                strArray = s.split(",");
                try {
                    tempa = strArray[0].substring(1, strArray[0].length() - 1);
                    tempb = strArray[5].substring(1, strArray[5].length() - 1);

                } catch (Exception e1) {
                    tempa = "";
                    tempb = "";
                }

                for (int i = 0; i < lly1.getChildCount() - 1; i++) {
                    if (((TextView) lly1.getChildAt(i).findViewById(R.id.Mitemid1)).getText().toString().equals(tempa)) {
                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setText(tempb);
                    }
                }

                tempa = null;
                tempb = null;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 啟動載入暫存資料
     */

    public void loadtempdata() {
        try {
            BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
            r = app.dpapp.appcdl.Exectempfile.instance().getfileTwo(Eqid, this, 1);//getfile_string(Checkdataid, this, 1);            //
            Toast.makeText(this, r.get_rmsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void BackData(Object so, Context cc, int type) {
        soapfilldata(so, type);
    }

    private void getsoapdata(int stype, String METHOD_NAME, String[] parnames, String[] parvalues, int parcount) {
        execloadactivity.opendialog(this, "正在執行");
        PublicSOAP p = new PublicSOAP();
        p.getsopdata(this,
                this, stype, METHOD_NAME, parnames, parvalues, parcount
        );
    }

    private void soapfilldata(Object o, int ctype) {
        execloadactivity.canclediglog();
        if (o == null) {
            Toast.makeText(this, String.valueOf(ctype) + ":獲取資料失敗，獲取資料為Null", Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject so = null;
        try {
            so = (SoapObject) o;
        } catch (Exception ex1) {
            Toast.makeText(this, String.valueOf(ctype) + ":獲取資料失敗，非標準SOAP格式", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            switch (ctype) {
                // Reject Form
                case 0:
                    String r1 = so.getProperty(0).toString();
                    switch (r1) {
                        case "true":
                            Toast.makeText(this, Checkdataid + "駁回成功", Toast.LENGTH_SHORT).show();
                            this.finish();
                            break;
                        case "false":
                            Toast.makeText(this, Checkdataid + "駁回失敗", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 1:
                    String r2 = ((SoapObject) ((SoapObject) ((SoapObject) ((SoapObject) so.getProperty(0)).getProperty(1)).getProperty(0)).getProperty(0)).getProperty(0).toString();
                    eqcheckdheadertv1.setText(r2);
                    break;
                default:
                    Toast.makeText(this, "Unknow Methods Return Result", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 駁回按鈕
     *
     * @param v
     */
    public void rejetctformdata(View v) {

//        ReturnFormCheck
//        参数 userid 工号 source 原因 flowid: 点检单号

        String METHOD_NAME = "ReturnFormCheck";
        String[] spn = {"userid", "source", "flowid"};
        String[] spv = {Sessionuser, "", Checkdataid};
        getsoapdata(0, METHOD_NAME, spn, spv, 3);
        spn = null;
        spv = null;
    }

    /**
     * 暫存資料按鈕
     *
     * @param v
     */
    // TODO: 2023/10/28 暂存按钮
    public void savatempdata(View v) {
        execloadactivity.opendialog(this, "正在執行");
        final BaseHandler handler = new BaseHandler(this, 0, this);

        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                        try {
                            List<String> ls = new ArrayList<>();
                            if (lly1 != null) {
                                for (int i = 0; i < lly1.getChildCount() - 1; i++) {
                                    View recordView = lly1.getChildAt(i);
                                    String Mitem1 = ((TextView) recordView.findViewById(R.id.Mitem1)).getText().toString().replace("\r\n", "").replace("\n", "");

                                    ls.add("["
                                            + ((TextView) recordView.findViewById(R.id.Mitemid1)).getText().toString() + "],["
                                            + ((TextView) recordView.findViewById(R.id.Mdept1)).getText().toString() + "],["
                                            + Mitem1 + "],["
                                            + ((TextView) recordView.findViewById(R.id.Munit)).getText().toString() + "],["
                                            + ((TextView) recordView.findViewById(R.id.Mspec1)).getText().toString() + "],["
                                            + ((TextView) recordView.findViewById(R.id.Mvalue1)).getText().toString() + "],["
                                            + ((TextView) recordView.findViewById(R.id.MValuespec)).getText() +
                                            "]"
                                    );
                                }
                                ls.add(

                                        "[" + Eqid + "],["
                                                + Eqname + "],["
                                                + Checkdataid + "],["
                                                + Eqopname + "],["
                                                + Eqlinename + "],["
                                                + Filerevsion + "]"
                                );
                            }
                            if (ls.size() > 0) {
//                                r = Exectempfile.instance().savefile(Checkdataid, ls);
                                r = app.dpapp.appcdl.Exectempfile.instance().savefileTwo(Eqid, ls);
                                msg.what = 0;
                                msg.obj = r;
                                // Log.d("debug0", JSON.toJSONString(r));
//                                //ls = new ArrayList<String>();
                            }

                        } catch (Exception ex) {


                        }

                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * /**
     * 離線資料按鈕
     *
     * @param v
     */
    public void offlinesavatempdata(View v) {
        execloadactivity.opendialog(this, "正在執行");
        final BaseHandler b = new BaseHandler(this, 0, this);

        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                        try {
                            List<String> ls = new ArrayList<>();
                            if (lly1 != null) {

                                for (int i = 0; i < lly1.getChildCount() - 1; i++) {
                                    String Mitem1 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitem1)).getText().toString().replace("\r\n", "");
                                    Mitem1 = Mitem1.replace("\n", "");
                                    ls.add(

                                            "[" + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitemid1)).getText().toString() + "],["
                                                    + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mdept1)).getText().toString() + "],["
                                                    + Mitem1 + "],["
                                                    + ((TextView) lly1.getChildAt(i).findViewById(R.id.Munit)).getText().toString() + "],["
                                                    + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mspec1)).getText().toString() + "],["
                                                    + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).getText().toString() + "],["
                                                    + ((TextView) lly1.getChildAt(i).findViewById(R.id.MValuespec)).getText() + "]"
                                    );
                                }
                                ls.add(
                                        "[" + Eqid + "],["
                                                + Eqname + "],["
                                                + Checkdataid + "],["
                                                + Eqopname + "],["
                                                + Eqlinename + "],["
                                                + Filerevsion + "]"
                                );

                            }
                            if (ls != null && ls.size() > 0) {
                                r = app.dpapp.appcdl.Exectempfile.instance().savefile222(Eqid + "_" + Filerevsion, ls);
                            }

                            msg.what = 0;
                            msg.obj = r;

                        } catch (Exception ex) {
                            r.set_rstatus(false);
                            r.set_rmsg(ex.getMessage());
                            msg.what = 1;
                            msg.obj = r;
                        }
                        b.sendMessage(msg);
                    }
                }
        );
    }

    /**
     * 清除本機所有緩存資料按鈕
     *
     * @param v
     */
    public void clearlocalcache(View v) {
        execloadactivity.opendialog(this, "正在執行");
        final BaseHandler bh = new BaseHandler(this, 2, this);
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                Message m = new Message();
                try {
                    app.dpapp.appcdl.Exectempfile.instance().deleteDirectory(mFilePath);
                    r = app.dpapp.appcdl.Exectempfile.instance().clearcachefile();
                    m.what = 0;
                    m.obj = r;
                } catch (Exception e) {
                    r.set_rstatus(false);
                    r.set_rmsg(e.getMessage());
                    m.what = 1;
                    m.obj = e.getMessage();
                }
                bh.sendMessage(m);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void eqchecklistbt1onclick(View v) {

        try {
            if (lly1 != null) {
                String temp1 = null;
                String temp2 = null;
                String temp3 = null;
                String temp4 = null;
                String temp5 = null;
                TextView textViewImageSpan;
                ArrayList<String> list = new ArrayList<>();

                String[] strArray = null;
                Boolean checkdatastatus = true;
                Boolean checksendMail = false;
                String str_mail = Deviceno + ":" + Eqname + ":" + Filerevsion + ":" + Checkdataid + ":" + Sessionuser + "";

                //String rstr=Checkdataid+":"+Sessionuser+":"+lly1.getChildCount()+":";
                String rstr = "";
                int rcount = 0;
                clicktimes++;

                for (int i = 0; i < lly1.getChildCount() - 1; i++) {
                    //if(((EditText) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).getVisibility()==View.VISIBLE) {
                    if (((EditText) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).isEnabled() == true) {
                        temp1 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mspec1)).getText().toString();
                        temp2 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).getText().toString();
                        temp3 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitem1)).getText().toString();
                        temp4 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitemid1)).getText().toString();
                        temp5 = ((TextView) lly1.getChildAt(i).findViewById(R.id.checkvalues)).getText().toString();
                        temp2 = temp2.toUpperCase();
                        strArray = temp1.split(";");
                        rstr = rstr + "{" + temp4 + ";" + temp2 + "}";
                        rcount++;

                        if (temp2 == null || temp2.length() <= 0) {
                            checkdatastatus = false;
                        }
                        if (temp3.contains("OP ID") || temp3.contains("ME ID") || temp3.contains("QC ID") || temp3.contains("ME Check")) {
                            list.add(temp2);
                        }
                        switch (strArray[0]) {
                            case "1":
                                //by lyh
                                if (strArray[1].equals(">") || strArray[1].equals("<") || strArray[1].equals(">=") || strArray[1].equals("<=")
                                        || strArray[1].equals("≥") || strArray[1].equals("≤")) {
                                    if (strArray[1].equals(">")) {
                                        if (Float.parseFloat(temp2) > Float.parseFloat(strArray[2])) {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        } else {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            if (clicktimes < 2) {
                                                checkdatastatus = false;
                                            }
                                        }
                                    }

                                    if (strArray[1].equals("<")) {

                                        if (Float.parseFloat(temp2) < Float.parseFloat(strArray[2])) {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            if (clicktimes < 2) {
                                                checkdatastatus = false;
                                            }

                                        } else {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    }
                                    if (strArray[1].equals("<=") || strArray[1].equals("≤")) {

                                        if (Float.parseFloat(temp2) <= Float.parseFloat(strArray[2])) {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            if (clicktimes < 2) {
                                                checkdatastatus = false;
                                            }

                                        } else {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    }
                                    if (strArray[1].equals(">=") || strArray[1].equals("≥")) {
                                        if (Float.parseFloat(temp2) >= Float.parseFloat(strArray[2])) {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            if (clicktimes < 2) {
                                                checkdatastatus = false;
                                            }

                                        } else {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    }
                                } else {
                                    try {
                                        if (Float.parseFloat(strArray[1]) > Float.parseFloat(temp2) || Float.parseFloat(strArray[2]) < Float.parseFloat(temp2)) {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            if (clicktimes < 2) {
                                                checkdatastatus = false;
                                            }

                                        } else {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        }
                                    } catch (Exception e) {
                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                    }
                                }

                                break;

                            case "0":
                                if ("Y".equals(temp2) || "N".equals(temp2) || "y".equals(temp2) || "n".equals(temp2)) {
                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                } else {
                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                    if (clicktimes < 2) {
                                        checkdatastatus = false;
                                    }
                                }
                                break;
                            case "2":
                            case "3":
                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                break;
                            case "4":
                                if (!TextUtils.isEmpty(temp5)) {
                                    if (temp5.toUpperCase().equals(temp2)) {
                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                    } else {
                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        if (clicktimes < 2) {
                                            checkdatastatus = false;
                                        }
                                    }
                                } else {
                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                }
                                //lyh
                                if (temp2.equals("N")) {
                                    checksendMail = true;
                                    str_mail += "序号为" + temp4 + "的点检项目选择为N,请确认\r\n";
                                }
                                break;
                            case "5"://允許值為空，如果不為空，那麼檢測是否有上下限
                                if (strArray.length == 1 || temp2.trim().equals("/")) {
                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                } else {
                                    if (strArray[1].equals(">") || strArray[1].equals("<") || strArray[1].equals(">=") || strArray[1].equals("<=")
                                            || strArray[1].equals("≥") || strArray[1].equals("≤")) {
                                        if (strArray[1].equals(">")) {
                                            if (Float.parseFloat(temp2) > Float.parseFloat(strArray[2]) ) {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                if (clicktimes < 2) {
                                                    checkdatastatus = false;
                                                }

                                            } else {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        }
                                        if (strArray[1].equals(">=") || strArray[1].equals("≥")) {
                                            if (Float.parseFloat(temp2) >= Float.parseFloat(strArray[2])) {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                if (clicktimes < 2) {
                                                    checkdatastatus = false;
                                                }

                                            } else {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        }
                                        if (strArray[1].equals("<")) {

                                            if (Float.parseFloat(temp2) < Float.parseFloat(strArray[2]) ) {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                if (clicktimes < 2) {
                                                    checkdatastatus = false;
                                                }

                                            } else {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        }
                                        if (strArray[1].equals("<=") || strArray[1].equals("≤")) {

                                            if (Float.parseFloat(temp2) <= Float.parseFloat(strArray[2]) ) {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                if (clicktimes < 2) {
                                                    checkdatastatus = false;
                                                }

                                            } else {
                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        }
                                    } else {
                                        if (Float.parseFloat(strArray[1]) > Float.parseFloat(temp2) || Float.parseFloat(strArray[2]) < Float.parseFloat(temp2)) {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            if (clicktimes < 2) {
                                                checkdatastatus = false;
                                            }

                                        } else {
                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        }

                                    }

                                }

                                break;
                            case "6img"://上传图片
                                checkdatastatus = true;
                              /*  //((ImageSpan) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setImageResource(R.drawable.btn_dl);

                                //使用<img>标签在TextView上显示图像
                                textViewImageSpan=(TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1);
                                String html="<img src='"+R.mipmap.ic_launcher+"'/>";
                                CharSequence reslut= Html.fromHtml(html,new Html.ImageGetter(){
                                    @Override
                                    public Drawable getDrawable(String s){
                                        //获取资源ID
                                        int resld=Integer.parseInt(s);
                                        //装载图像
                                        Drawable drawable=getResources().getDrawable(resld);
                                        //设置图像按原始大小显示
                                        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                                        return drawable;
                                    }
                                },null);
                                textViewImageSpan.setText(reslut);

                                //使用ImageSpan对象在TextView中显示图像
                                textViewImageSpan=(TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1);
                                //根据资源ID获取资源图像Bitmap对象
                                Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                                //根据Bitmap对象创建ImageSpan对象
                                ImageSpan imageSpan = new ImageSpan(this,bitmap);
                                //创建一个SpinnableString对象，以便插入ImageSpan对象封装的图像
                                SpannableString spannableString = new SpannableString("replace");
                                //用ImageSpan对象替换replace字符串
                                spannableString.setSpan(imageSpan,0,7,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                //将图像显示在TextView上
                                textViewImageSpan.setText(spannableString);
                                */
                                break;


                            default:
                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                checkdatastatus = false;
                                break;
                        }
                    } else {

                    }
                }

                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).length() < 8) {
                        Toast.makeText(eqchecklist.this, "请输入正确格式的工号!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if(list.size() > 1) {
                    Set<String> set = new HashSet<>(list);
                    long count = set.size();
                    if(count < list.size()) {
                        Toast.makeText(eqchecklist.this, "OP ID，ME ID，QC ID，ME Check人不能相同!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                //              S20180102080170:S7151329:14:{0;20180103 10:12:22}{1;D}{2;V}{3;V}{4;V}{5;V}{6;V}{7;V}{8;V}{9;OK}{10;V}{11;V}{12;F1678260}{13;F1678260}
                String allrstr = Checkdataid + ":" + Sessionuser + ":" + rcount + ":" + rstr;
                SOAPSENDSTR = allrstr;
                if (!checkdatastatus) {
                    Toast.makeText(eqchecklist.this, "资料格式异常或未填寫完成，请重新填写", Toast.LENGTH_LONG).show();
                } else {
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            String a = getRemoteInfo_submit();
                            Message msg = handle1.obtainMessage();
                            msg.what = 0;
                            msg.obj = a;
                            handle1.sendMessage(msg);
                        }
                    });
                    clicktimes = 0;

                    if (Staticdata.email.equals("1")) {
                        final String str_mail1 = str_mail;
                        if (checksendMail) {
                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    String b = ps1.ChecksendMail(str_mail1);
                                    Message msg = handle1.obtainMessage();
                                    msg.what = 0;
                                    msg.obj = b;
                                    handle2.sendMessage(msg);
                                }
                            });
                        }
                    }
                }
            } else {
                Toast.makeText(eqchecklist.this, "未检查到资料，请退出重新获取", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex1) {
            Toast.makeText(this, "异常信息为" + ex1.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出此页面吗");
            // 添加选择按钮并注册监听
            isExit.setButton2("取消", listener);
            isExit.setButton("确定", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };


    public String getRemoteInfo_submit() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "UploadCheckdatanew";//"UploadCheckdata";
            String SOAP_ACTION = "http://tempuri.org/UploadCheckdatanew";//"http://tempuri.org/UploadCheckdata";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("checkdata", SOAPSENDSTR);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
                return "991";
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            //SoapObject soapchild=(SoapObject)r1.getProperty(0);

            //SoapObject soapchild=(SoapObject)((SoapObject)((SoapObject)r1.getProperty(0)).getProperty(1)).getProperty(0);
            // 获取返回的结果
            //soapresult1 = r1.getProperty(0).toString();

            SOAPRETURNSTR = r1.getProperty(0).toString();
            return SOAPRETURNSTR;
        } catch (Exception ex1) {
            return "990";
        }
    }

    private Handler handle1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String SOAPRETURNSTR1 = msg.obj.toString().trim();
                    if (SOAPRETURNSTR1 != null && SOAPRETURNSTR1 == "991") {
//                        Toast.makeText(eqchecklist.this, "获取资料异常", Toast.LENGTH_LONG);
                        return;
                    }

                    if (SOAPRETURNSTR1 == "990") {
                        Toast.makeText(eqchecklist.this, "未知异常", Toast.LENGTH_LONG);
                        return;
                    }

                    switch (SOAPRETURNSTR1) {
                        case "0":
                            BaseFuncation.rrtype rt = new BaseFuncation().new rrtype();
                            rt = Exectempfile.instance().removefile(Checkdataid);
                            if (rt.get_rstatus()) {
                                Toast.makeText(eqchecklist.this, "提交成功", Toast.LENGTH_LONG).show();
                                final BaseHandler bh = new BaseHandler(eqchecklist.this, 2, eqchecklist.this);
                                MyThreadPool.pool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                                        Message m = new Message();
                                        try {
                                            app.dpapp.appcdl.Exectempfile.instance().deleteDirectory(mFilePath);
                                            r = app.dpapp.appcdl.Exectempfile.instance().clearcachefile();
                                            m.what = 0;
                                            m.obj = r;
                                        } catch (Exception e) {
                                            r.set_rstatus(false);
                                            r.set_rmsg(e.getMessage());
                                            m.what = 1;
                                            m.obj = e.getMessage();
                                        }
                                        bh.sendMessage(m);
                                    }
                                });
                            } else {
                                Toast.makeText(eqchecklist.this, "提交成功,但清除暫存資料失敗", Toast.LENGTH_LONG).show();
                            }
                            //Exectempfile.instance().removefile(Checkdataid);
                            finish();
                            break;
                        case "1":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                            Toast.makeText(eqchecklist.this, "提交失败，请检查后重新提交", Toast.LENGTH_LONG).show();
                            break;
                        case "2":
                            Toast.makeText(eqchecklist.this, "該機台已經點檢過，或者不應該被點檢", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(eqchecklist.this, "系统定义未知返回值", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            } catch (Exception ex1) {
            }
        }
    };

    public List<Slist> getRemoteInfo() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "GetFlowStructtablenew"; //"GetFlowStructtable";
            String SOAP_ACTION = "http://tempuri.org/GetFlowStructtablenew"; //"http://tempuri.org/GetFlowStructtable";

            try {
                SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
                //rpc.addProperty("machinesysid", scanstr1);
                rpc.addProperty("machinesysid", Eqid);
                //rpc.addProperty("checktype", "0");    //0:点检表单
                rpc.addProperty("userid", Sessionuser);
                rpc.addProperty("flowid", Checkdataid);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);

                HttpTransportSE transport = new HttpTransportSE(URL);
                try {
                    // 调用WebService
                    transport.call(SOAP_ACTION, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                SoapObject r1 = (SoapObject) envelope.bodyIn;
                SoapObject soapchild;
                try {
                    soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    sysErrorcode = "2";
                    return null;
                }
                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                String tempsoap1;
                String tempsoap2;
                String tempsoap3;
                String tempsoap4;
                String tempsoap5;
                String tempsoap6;
                String tempsoap7;
                String tempsoap8;
                String tempsoap9;
                String tempsoap10;

                List<Slist> Sourcelist = new ArrayList<Slist>();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                    tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty("ITEMID").toString();
                    tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty("ITEMVALUE").toString();
                    tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("OWNER").toString();
                    tempsoap4 = ((SoapObject) soapchild.getProperty(i)).getProperty("STANDARDS").toString();
                    tempsoap5 = ((SoapObject) soapchild.getProperty(i)).getProperty("READONLY").toString();
                    tempsoap8 = ((SoapObject) soapchild.getProperty(i)).getProperty("STANDARD").toString();
                    tempsoap9 = ((SoapObject) soapchild.getProperty(i)).getProperty("TABLETYPE").toString();
                    try {
                        tempsoap6 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKVALUE").toString();
                    } catch (Exception ex) {
                        tempsoap6 = "";
                    }
                    try {
                        tempsoap7 = ((SoapObject) soapchild.getProperty(i)).getProperty("UNIT").toString();
                    } catch (Exception ex) {
                        tempsoap7 = "";
                    }
                    try {
                        tempsoap10 = ((SoapObject) soapchild.getProperty(i)).getProperty("ATT3").toString();
                    } catch (Exception ex) {
                        tempsoap10 = "";
                    }

                    Slist s1 = new Slist();
                    s1.Sessionuser = Sessionuser;
                    s1.Eqid = Eqid;
                    s1.Eqname = Eqname;
                    s1.Sysdocumentid = "";
                    s1.setIdept(tempsoap3);
                    s1.setItemid(tempsoap1);
                    s1.setAtt3(tempsoap10);

                    s1.setItemname(tempsoap2);
                    s1.setValue(tempsoap6);
                    s1.setValuespec(tempsoap4);
                    s1.setValueshowpoint(tempsoap5);
                    s1.setValueunit(tempsoap7);
                    s1.setTABLETYPE(tempsoap9);
                    Sourcelist.add(s1);
                }
//              int j=Sourcelist.size();
                return Sourcelist;
            } catch (Exception ex1) {
                return null;
            }

        } catch (Exception e1) {
            return null;
        }
    }

    class MyCheckAdapter extends BaseAdapter {
        private Context context;
        private List<CheckBean> list;

        public MyCheckAdapter(Context context, List<CheckBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_source2, null);
                holder = new ViewHolder();
//                ((TextView) convertView.findViewById(R.id.Mitemid1)).setText(Sourcelist.get(i).getItemid());
//                ((TextView) convertView.findViewById(R.id.Mdept1)).setText(Sourcelist.get(i).getIdept());
//                ((TextView) convertView.findViewById(R.id.Mitem1)).setText(Sourcelist.get(i).getItemname());
//                ((TextView) convertView.findViewById(R.id.Munit)).setText(Sourcelist.get(i).getValueunit());
//                ((TextView) convertView.findViewById(R.id.Mspec1)).setText(Sourcelist.get(i).getValuespec());
//                ((EditText) convertView.findViewById(R.id.Mvalue1)).setText(Sourcelist.get(i).getValue());
//                ((TextView) convertView.findViewById(R.id.MValuespec)).setText(Sourcelist.get(i).getValueshowpoint());
                holder.text1 = (TextView) convertView.findViewById(R.id.Mitemid1);
                holder.text2 = (TextView) convertView.findViewById(R.id.Mdept1);
                holder.text3 = (TextView) convertView.findViewById(R.id.Mitem1);
                holder.text4 = (TextView) convertView.findViewById(R.id.Munit);
                holder.text5 = (TextView) convertView.findViewById(R.id.Mspec1);
                holder.text6 = (TextView) convertView.findViewById(R.id.Mvalue1);
                holder.text7 = (TextView) convertView.findViewById(R.id.MValuespec);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            holder.
            return convertView;
        }
    }

    class ViewHolder {
        TextView text1, text2, text3, text4, text5, text6, text7;
    }

    public class CheckBean {
        private String Itemid;
        private String Idept;
        private String Itemname;
        private String Valueunit;
        private String Valuespec;
        private String Value;

        public String getItemid() {
            return Itemid;
        }

        public void setItemid(String itemid) {
            Itemid = itemid;
        }

        public String getIdept() {
            return Idept;
        }

        public void setIdept(String idept) {
            Idept = idept;
        }

        public String getItemname() {
            return Itemname;
        }

        public void setItemname(String itemname) {
            Itemname = itemname;
        }

        public String getValueunit() {
            return Valueunit;
        }

        public void setValueunit(String valueunit) {
            Valueunit = valueunit;
        }

        public String getValuespec() {
            return Valuespec;
        }

        public void setValuespec(String valuespec) {
            Valuespec = valuespec;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }

        public String getValueshowpoint() {
            return Valueshowpoint;
        }

        public void setValueshowpoint(String valueshowpoint) {
            Valueshowpoint = valueshowpoint;
        }

        private String Valueshowpoint;

    }


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
//            try {
            if (msg.what == 0) {
                List<Slist> Sourcelist = new ArrayList<>();

//                    try {
                Sourcelist = (List<Slist>) msg.obj;
//                    } catch (Exception ex1) {
//                        Toast.makeText(eqchecklist.this, "資料格式異常",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                if (Sourcelist.size() == 0) {
                    Toast.makeText(eqchecklist.this, "無資料",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                View convertView;
                String[] strArray = null;
                String temp1 = "";
                String userdept = "";
                if (Sourcelist.size() > 0) {
                    userdept = Sourcelist.get(0).Idept.toString();
                }
                for (int i = 0; i < Sourcelist.size(); i++) {
                    if ("1".equals(Sourcelist.get(i).getTABLETYPE())) {

                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source2, null);
                        ((TextView) convertView.findViewById(R.id.Mitemid1)).setText(Sourcelist.get(i).getItemid());
                        ((TextView) convertView.findViewById(R.id.Mdept1)).setText(Sourcelist.get(i).getIdept());
                        ((TextView) convertView.findViewById(R.id.Mitem1)).setText(Sourcelist.get(i).getItemname());
                        ((TextView) convertView.findViewById(R.id.Munit)).setText(Sourcelist.get(i).getValueunit());
                        ((TextView) convertView.findViewById(R.id.Mspec1)).setText(Sourcelist.get(i).getValuespec());
                        ((EditText) convertView.findViewById(R.id.Mvalue1)).setText(Sourcelist.get(i).getValue());
                        ((TextView) convertView.findViewById(R.id.checkvalues)).setText(Sourcelist.get(i).getAtt3());
                        ((TextView) convertView.findViewById(R.id.MValuespec)).setText(Sourcelist.get(i).getValueshowpoint());

                        if (Sourcelist.get(i).getValueshowpoint().equals("1")) {
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(true);
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusable(true);
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusableInTouchMode(true);

                            String tempspecvalue1 = Sourcelist.get(i).getValuespec().trim().substring(0, 1);
                            String tempspecvalue2;
//                            if (tempspecvalue1.equals("1") || tempspecvalue1.equals("2")||tempspecvalue1.equals("5") ||tempspecvalue1.equals("6") || tempspecvalue1.equals("4") || tempspecvalue1.equals("3")) {
//                                tempspecvalue2 = null;
//                            }
//                            else
//                            {
//                                Toast.makeText(eqchecklist.this,Sourcelist.get(i).Valuespec.toString(),Toast.LENGTH_SHORT).show();
//                                tempspecvalue2 = Sourcelist.get(i).Valuespec.toString().trim().substring(2, Sourcelist.get(i).Valuespec.toString().trim().length());
//                            }
                            if (tempspecvalue1.equals("1") || tempspecvalue1.equals("2") || tempspecvalue1.equals("5") || tempspecvalue1.equals("6")) {
                                if (tempspecvalue1.equals("2")) {
                                    if (Sourcelist.get(i).getItemname().contains("起始時間") || Sourcelist.get(i).getItemname().contains("完成時間")
                                            || Sourcelist.get(i).getItemname().contains("報廢時間")) {
                                        selectitems((EditText) convertView.findViewById(R.id.Mvalue1));
                                    }
                                }
                                tempspecvalue2 = null;
                            } else {
                                String str = Sourcelist.get(i).getValuespec();
                                if (str.length() > 1) {
                                    tempspecvalue2 = Sourcelist.get(i).getValuespec().trim().substring(2);
                                } else {
                                    tempspecvalue2 = null;
                                }
                            }
                            String[] chooseitems = null;
                            if (tempspecvalue2 != null) {
                                chooseitems = tempspecvalue2.split(";");
                            }
//                            showSoftInputFromWindow((EditText) convertView.findViewById(R.id.Mvalue1));

                            if (tempspecvalue1.equals("6")) {
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusable(false);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusableInTouchMode(false);
//                                selectitems(((EditText) convertView.findViewById(R.id.Mvalue1)), "upload");
//                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setVisibility(View.GONE);
//                                ((ImageView) convertView.findViewById(R.id.imageView)).setVisibility(View.VISIBLE);
//                                joinCameraPhoto(((ImageView) convertView.findViewById(R.id.imageView)), ((TextView) convertView.findViewById(R.id.Mitemid1)).getText().toString());
                                joinCameraPhoto(((EditText) convertView.findViewById(R.id.Mvalue1)), ((TextView) convertView.findViewById(R.id.Mitemid1)).getText().toString());
                            } else if (tempspecvalue1.equals("4")){
                                // 1:數字 2:任意文字 3:日期 4:ChooseItem{Y/N,Ass/Rej,S/N}
//                                hideSoftKeyBoard((EditText) convertView.findViewById(R.id.Mvalue1));
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusable(false);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusableInTouchMode(false);
                                if (Sourcelist.get(i).getItemname().contains("班別")) {
                                    selectitemsbb((EditText) convertView.findViewById(R.id.Mvalue1));
                                } else {
                                    selectitems((EditText) convertView.findViewById(R.id.Mvalue1), tempspecvalue1, chooseitems, tempspecvalue2);
                                }

                            } else {
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusable(true);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusableInTouchMode(true);
                                selectitems((EditText) convertView.findViewById(R.id.Mvalue1), tempspecvalue1, chooseitems, tempspecvalue2);
                            }
                        } else {
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(false);
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setBackgroundColor(Color.parseColor("#c0c0c0"));
                        }

                        ((TextView) convertView.findViewById(R.id.Mspec1)).setVisibility(View.GONE);
                        ((TextView) convertView.findViewById(R.id.MValuespec)).setVisibility(View.GONE);
                        if ("FA".equals(userdept)) {
                            ((TextView) convertView.findViewById(R.id.Mdept1)).setVisibility(View.GONE);
                        }

                        scontextview = convertView;
                        lly1.addView(convertView);
                    }
                    if ("2".equals(Sourcelist.get(i).getTABLETYPE())) {
                        TextView textView = new TextView(eqchecklist.this);
                        textView.setText(Sourcelist.get(i).getItemname());
                        textView.setTextSize(18);
                        lly1.addView(textView);
                    }
                }
            }

        }
//            catch(Exception ex1) {
//                Toast.makeText(eqchecklist.this, "表单资料异常",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//            finally {
//                execloadactivity.canclediglog();
//            }
//        }

    };
//    private void hideSoftKeyBoard(View view) {
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    /**
//     * EditText获取焦点并显示软键盘
//     */
//    public void showSoftInputFromWindow(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    // ((TextView) scontextview.findViewById(R.id.listviewsource11Mname9)).setText(scanResult.split(":")[1]);
                    ((EditText) scontextview.findViewById(R.id.Mvalue1)).setText(scanResult.split(":")[1]);
                    break;
                // 表示 调用照相机拍照
                case FROM_CAMERA:

                    String filename = path + "/" + camera_photo_name;
                    Bitmap bm = compressImageFromFile(filename);

//                    ImageItem takePhoto = new ImageItem();
//                    takePhoto.setBitmap(bm);
//                    Bimp.tempSelectBitmap.add(takePhoto);

//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    ((ImageView) scontextview.findViewById(R.id.imageView)).setImageBitmap(bitmap);
//                    String filename = ImageUtils.createFileName();
//                    ImageUtils.saveImage(bitmap, path, filename);
//                    String data2 = path + filename;
                    uploadImagefun(Checkdataid + ";" + mItem, filename);
//                    imgString = bitmapToBase64(bitmap);
//                    uploadImg();
                    break;
                // 选择图片库的图片
                case FROM_ALBUM:
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        Bitmap bitmap2 = ImageUtils.getBitmapFromUri(uri, this);
                        ((ImageView) scontextview.findViewById(R.id.imageView)).setImageBitmap(bitmap2);
                        if (null == uri)
                            return;
                        final String scheme = uri.getScheme();
                        String data1 = "";
                        if (scheme == null)
                            data1 = uri.getPath();
                        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                            data1 = uri.getPath();
                        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                            Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                            if (null != cursor) {
                                if (cursor.moveToFirst()) {
                                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                                    if (index > -1) {
                                        data1 = cursor.getString(index);
                                    }
                                }
                                cursor.close();
                            }
                        }
                        uploadImagefun(Checkdataid + ";" + mItem, data1);
//                    imgString = bitmapToBase64(bitmap2);
//                    uploadImg();
                    }
                    break;
            }
        }
    }

    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, null);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        // newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    private void uploadImagefun(String systemname, String data) {
        File file = new File(data);

        List<httprequestinputdata> li = new ArrayList<>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("systemname");
        hhi1.setDatavalue(systemname);
        //hhi1.setDatavalue("CMSFMGRBCEQCHECK");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("createid");
        hhi2.setDatavalue(Sessionuser);
        li.add(hhi2);
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(3, new FreedomDataCallBack() {
            @Override
            public void onStart(Context C1) {

            }

            @Override
            public void processData(Object paramObject, boolean paramBoolean) {
                returnstr = paramObject.toString();
                if(returnstr.contains("ERROR")) {
                    makeText(eqchecklist.this, "上传失败，需要重新上传!", Toast.LENGTH_SHORT).show();
                } else {
                    ((EditText) scontextview.findViewById(R.id.Mvalue1)).setText(returnstr.split(":")[1]);
                }
            }

            @Override
            public void onFinish(Context C1) {
//                callback();
                makeText(eqchecklist.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Object paramObject, Context C1) {
                makeText(eqchecklist.this, "上传失败，需要重新上传!", Toast.LENGTH_SHORT).show();
//                makeText(eqchecklist.this, "Fail-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoneImage(Context C1) {
                makeText(eqchecklist.this, "Fail2-", Toast.LENGTH_SHORT).show();
            }
        }, this, Staticdata.httpurl + "CMSF/Receiveimage.ashx", li, file);
    }

    private Calendar mCalendar;
    private int bbHour;

    protected void selectitemsbb(final View v) {
        ((EditText) v).setInputType(InputType.TYPE_NULL);
        ((EditText) v).setSingleLine(false);
        ((EditText) v).setHorizontallyScrolling(false);
        mCalendar = Calendar.getInstance();
        bbHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        if (bbHour >= 7 && bbHour < 19) {
            ((EditText) v).setText("D");
        } else {
            ((EditText) v).setText("N");
        }
    }

    private int mYear, mMonth, mDate, mHour, mMin;

    protected void selectitems(final View v) {
        ((EditText) v).setInputType(InputType.TYPE_NULL);
        ((EditText) v).setSingleLine(false);
        ((EditText) v).setHorizontallyScrolling(false);
        final String myDate;
        ((EditText) v).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v1) {
                        mCalendar = Calendar.getInstance();
                        mYear = mCalendar.get(Calendar.YEAR);
                        mMonth = mCalendar.get(Calendar.MONTH);
                        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);
                        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                        mMin = mCalendar.get(Calendar.MINUTE);
                        new DatePickerDialog(eqchecklist.this, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year
                                    , int monthOfYear, int dayOfMonth) {
                                final String myYear, myMonth, myDay;
                                myYear = "" + year;
                                if (monthOfYear < 9) {
                                    myMonth = "0" + (monthOfYear + 1);
                                } else {
                                    myMonth = "" + (monthOfYear + 1);
                                }
                                if (dayOfMonth < 10) {
                                    myDay = "0" + dayOfMonth;
                                } else {
                                    myDay = "" + dayOfMonth;
                                }
                                new TimePickerDialog(eqchecklist.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String myHour, myMin;
                                        if (hourOfDay < 10) {
                                            myHour = "0" + hourOfDay;
                                        } else {
                                            myHour = "" + hourOfDay;
                                        }
                                        if (minute < 10) {
                                            myMin = "0" + minute;
                                        } else {
                                            myMin = "" + minute;
                                        }
                                        ((EditText) v).setText(myYear + myMonth + myDay + " " + myHour + ":" + myMin + ":00");
                                    }
                                }, mHour, mMin, true).show();
                            }
                        }, mYear, mMonth, mDate).show();
                    }
                }
        );

    }

    protected void selectitems(View v, String specvalue, final String[] chooseitems1, String value1) {
        try {

            if (specvalue.equals("0") || specvalue.equals("4")) {
                ((EditText) v).setInputType(InputType.TYPE_NULL);
                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View v1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        eqchecklist.this);
                                builder.setTitle("選擇");
                                builder.setSingleChoiceItems(chooseitems1, 1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String select_item = chooseitems1[which].toString();
                                        ((EditText) v1).setText(select_item.toString());
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();
                            }
                        }
                );
            } else {
                if (specvalue.equals("3")) {
                    ((EditText) v).setText(value1);
                } else {

                }
            }
        } catch (Exception ex1) {

        }
    }

    protected void selectitems(final View v, final String cs) {
        try {
            if (cs.equals("upload")) {
                //v.setEnabled(false);
                ((EditText) v).setInputType(InputType.TYPE_NULL);
                //((EditText)v).setEnabled(true);
                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View v1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(eqchecklist.this);
                                builder.setTitle("選擇");
                                builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //調用相機拍照  邏輯在此實現

                                                Intent intent = new Intent(eqchecklist.this, uploadimage.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("sysname", "CMSFEQCHECKLIST");
                                                intent.putExtras(bundle);
                                                startActivityForResult(intent, 3);
                                                scontextview = v;
                                            }
                                        }
                                ).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                            }
                        }
                );
            }
        } catch (Exception ex1) {
        }
    }

    public void joinCameraPhoto(final View v1,final String item) {
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(eqchecklist.this);
                builder.setTitle("選擇");
                builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mItem = item;
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File path1 = new File(path);
                                if (!path1.exists()) {
                                    path1.mkdirs();
                                }
                                camera_photo_name = ImageUtils.createFileName();
                                File file = new File(path1, camera_photo_name);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(intent, FROM_CAMERA);
                                scontextview = v1;
                            }
                        }
                ).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        mItem = item;
//                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(intent, FROM_ALBUM);
//                        scontextview = v1;
                    }
                }).create().show();
            }
        });
    }
}