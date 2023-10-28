package app.cmapp.machinecheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.Interface.Iobjectrhandler;
import app.cmapp.Staticdata;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.Exectempfile;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.appcdl.uploadimage;
import app.dpapp.R;
import app.dpapp.appcdl.FreedomSOAPCallBack;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by S7187445 on 2017/9/27.
 */
public class eqcheckformsave  extends AppCompatActivity implements Iobjectrhandler, FreedomSOAPCallBack {
    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
//    protected String Eqid;
    protected String Eqname;
    protected String Eqopname;
    protected String Eqlinename;
    protected String Checkdataid;
    protected String SOAPSENDSTR;
    protected String SOAPRETURNSTR;
    protected String Filerevsion;

    protected TextView eqopnametv1;
    protected TextView eqlinanemtv1;
    protected TextView eqfileversiontv1;
    protected TextView eqcheckdheadertv1;
    private String mMachinenoid;
    private String tablename;
    private Button mButton;
    private String deviceno;
    private String modletype;
    private String mUserId;
    private String sysErrorcode; // 1:获取资料异常
    protected int clicktimes;  //  提交次数  提交失败递增，提交成功就初始化为0

    private View scontextview;
    private String mNumber;
    private String flag;
    private String mFilePath = "/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/";
    //定义数据源
    //protected List<Slist> Sourcelist;
    private int saveTimes;
    private List<String> mList;
    private String number;

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


    Button bt1;
    LinearLayout lly1;
    private String txtname="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checkformsave);
        try {
            Bundle bundle = this.getIntent().getExtras();
            Sessionuser = bundle.getString("Sessionuser");
            mButton = (Button) findViewById(R.id.eq_checklistbutton2);
            Staticdata sc = (Staticdata) getApplication();
            mUserId = sc.getLoginUserID();
            flag = getIntent().getStringExtra("flag");
//            Eqid = bundle.getString("Eqid");
            tablename = getIntent().getStringExtra("tablename");
            mMachinenoid = getIntent().getStringExtra("machinenoid");
            deviceno = getIntent().getStringExtra("deviceno");
            modletype = getIntent().getStringExtra("modletype");
            mNumber = getIntent().getStringExtra("number");
            number = getIntent().getStringExtra("number2");

            eqopnametv1 = (TextView) findViewById(R.id.eq_checklistopnametv1);
            eqlinanemtv1 = (TextView) findViewById(R.id.eq_checklistlinenametv1);
            eqfileversiontv1 = (TextView) findViewById(R.id.eq_checklistmemotv1);
            eqcheckdheadertv1=(TextView) findViewById(R.id.eq_checklisttv1);

            bt1 = (Button) findViewById(R.id.eq_checklistbutton1);
            lly1 = (LinearLayout) findViewById(R.id.eqchecklistLinearLayout);
            if(isNetworkAvailable(this)) {
                mButton.setText("提交");
            } else {
                mButton.setText("暫存");
            }

            execloadactivity.opendialog(this, "正在執行");
            MyThreadPool.pool.execute(
                    new Runnable() {

                        @Override
                        public void run() {
                            Message msg = handle.obtainMessage();
                            msg.what = 0;
                            msg.obj = null;
                            handle.sendMessage(msg);

                        }
                    }
            );

        }
        catch(Exception ex1) {

        }
        mList = new ArrayList<>();
        mList = getfilenew();
        if(mList != null) {
            if(mList.size() > 0) {
                saveTimes = Integer.parseInt(mList.get(mList.size() - 1).split(".tx")[0].split("A")[1]);
            } else {
                saveTimes = 0;
            }
        } else {
            saveTimes = 0;
        }
    }

    public static boolean isNetworkAvailable(Context context){
        //獲取網絡狀態管理器
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm==null){
            return false;
        } else{
            //cm.getActiveNetworkInfo().isAvailable();
            //簡歷網絡數組
            NetworkInfo[] info =cm.getAllNetworkInfo();
            if(info!=null)
            {
                for (int i=0;i<info.length;i++){
                    //判斷獲得的網絡狀態是否是出於連接狀態
                    if (info[i].getState()==NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                loadtempdata();
            }
            catch(Exception ex1) {
                Toast.makeText(eqcheckformsave.this, "表单资料為空",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            finally {
                execloadactivity.canclediglog();
            }
        }

    };

    /**
     * 接口回調方法
     * @param parmerobject 依用戶需求定義
     * @param o 回傳結果參數
     */
    @Override
    public void execobject(Object parmerobject, Object o)
    {
        try {
            switch ((int)parmerobject)
            {
                //Save Tempfile
                case 0:
                    Toast.makeText(this,((BaseFuncation.rrtype)o).get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;
                //Load Tempfileto UI
                case 1:
                    loadtempdatatoui(o);
                    break;
                //Clear all cache file
                case 2:
                    Toast.makeText(this,((BaseFuncation.rrtype)o).get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    gettxtname(o);
                    break;
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        execloadactivity.canclediglog();
    }
    /**
     * 啟動載入暫存資料
     */
    public void loadtempdata()
    {
        try {
            //"machinecheck";
            BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
//            txtname=getfile("machinecheck",mMachinenoid);

            String fileurl = mMachinenoid + "/";
            String[] strArray = Exectempfile.instance().getfilenew(fileurl);

            if("888".equals(flag)) {
                r = Exectempfile.instance().getfile222(strArray[0].split("txt")[0].substring(0, strArray[0].split("txt")[0].length() - 1), this, 1);
            }else if("666".equals(flag)){
                r = Exectempfile.instance().getfile(strArray[0].split("txt")[0].substring(0, strArray[0].split("txt")[0].length() - 1) + mNumber, this, 1);
            }
               Toast.makeText(this, r.get_rmsg(), Toast.LENGTH_SHORT).show();

        }
        catch(Exception ex)
        {
            throw ex;
        }
    }



    /**
     * 載入暫存資料成功顯示至UI
     * @param obj
     */
    private void loadtempdatatoui(Object obj)
    {
        try {
            List<String> ls = (List<String>) obj;
            String Itemid="", Idept="",Itemname="",Valueunit="",Valuespec="",Value="",Valueshowpoint="";
            View convertView;
            String[] strArray=null;
            String userdept="";
            if(ls.size()>0)
            {
                strArray=ls.get(0).split(",");
                userdept=strArray[1];
            }

            //for (String s : ls) {
            for (int i=0;i<ls.size();i++) {
                String s=ls.get(i);
                if (s == null || s.trim().equals(""))
                    continue;
                strArray=s.split(",");

                if(i==ls.size()-1) {

                    Eqname = strArray[1].substring(1,strArray[1].length()-1);
                    Checkdataid = strArray[2].substring(1,strArray[2].length()-1);
                    Eqopname = strArray[3].substring(1,strArray[3].length()-1);
                    Eqlinename = strArray[4].substring(1,strArray[4].length()-1);
                    Filerevsion = strArray[5].substring(1,strArray[5].length()-1);

                    eqopnametv1.setText(Eqname);
                    eqlinanemtv1.setText(Eqlinename);
                    eqfileversiontv1.setText(Filerevsion);
                } else {
                    try {
                        Itemid = strArray[0].substring(1,strArray[0].length()-1);
                        Idept = strArray[1].substring(1,strArray[1].length()-1);
                        Itemname = strArray[2].substring(1,strArray[2].length()-1);
                        Valueunit = strArray[3].substring(1,strArray[3].length()-1);
                        Valuespec = strArray[4].substring(1,strArray[4].length()-1);
                        Value = strArray[5].substring(1,strArray[5].length()-1);
                        Valueshowpoint = strArray[6].substring(1,strArray[6].length()-1);
                    } catch (Exception e1) {

                    }

                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source2, null);
                    ((TextView) convertView.findViewById(R.id.Mitemid1)).setText(Itemid);
                    ((TextView) convertView.findViewById(R.id.Mdept1)).setText(Idept);
                    ((TextView) convertView.findViewById(R.id.Mitem1)).setText(Itemname);
                    ((TextView) convertView.findViewById(R.id.Munit)).setText(Valueunit);
                    ((TextView) convertView.findViewById(R.id.Mspec1)).setText(Valuespec);
                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setText(Value);
                    ((TextView) convertView.findViewById(R.id.MValuespec)).setText(Valueshowpoint);

                    if (Valueshowpoint.equals("1")) {
                        ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(true);

                        String tempspecvalue1 = Valuespec.toString().trim().substring(0, 1);
                        String tempspecvalue2;
                        if (tempspecvalue1.equals("1") || tempspecvalue1.equals("2") || tempspecvalue1.equals("5") || tempspecvalue1.equals("6")) {
                            tempspecvalue2 = null;
                        } else {
                            tempspecvalue2 = Valuespec.toString().trim().substring(2, Valuespec.toString().trim().length());
                        }
                        String[] chooseitems = null;
                        if (tempspecvalue2 != null) {
                            chooseitems = tempspecvalue2.split(";");
                        }

                        if (tempspecvalue1.equals("6")) {
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setText("");
                            selectitems(((EditText) convertView.findViewById(R.id.Mvalue1)), "upload");
                        } else {
                            // 1:數字 2:任意文字 3:日期 4:ChooseItem{Y/N,Ass/Rej,S/N}
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
                    } else {

                        ((TextView) convertView.findViewById(R.id.Munit)).setVisibility(View.GONE);
                    }
                   // scontextview = convertView;
                    lly1.addView(convertView);
                }
            }

        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    private void gettxtname(Object obj)
    {
        try {
            List<String> ls = (List<String>) obj;
            String tempa="", tempb="";

            String[] strArray=null;
            if(ls.size()>0)
            {

                strArray=ls.get(0).split(",");
                tempa=strArray[0].substring(1,strArray[0].length()-1);
                tempb=strArray[3].substring(1, strArray[3].length() - 1);
                txtname=tempa+"_"+tempb;
            }

        }
        catch(Exception ex)
        {
            throw ex;
        }
    }


    protected void selectitems(View v,String specvalue,final String[] chooseitems1,String value1)
    {
        try
        {

            if (specvalue.equals("0") || specvalue.equals("4"))
            {
                ((EditText)v).setInputType(InputType.TYPE_NULL);
                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View v1) {
                                AlertDialog.Builder isnoticedialog = new AlertDialog.Builder(
                                        eqcheckformsave.this);
                                isnoticedialog.setTitle("選擇");
                                isnoticedialog.setSingleChoiceItems(chooseitems1, 1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String select_item = chooseitems1[which].toString();
                                        ((EditText) v1).setText(select_item.toString());
                                        dialog.cancel();
                                    }
                                });
                                isnoticedialog.show();
                            }
                        }
                );
            }
            else {
                if (specvalue.equals("3")) {
                    ((EditText) v).setText(value1);
                }
                else
                {

                }
            }
        }
        catch(Exception ex1)
        {

        }
    }

    protected void selectitems(final View v, final String cs)
    {
        try {
            if (cs.equals("upload"))
            {
                //v.setEnabled(false);
                ((EditText)v).setInputType(InputType.TYPE_NULL);
                //((EditText)v).setEnabled(true);
                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View v1) {
                                AlertDialog.Builder isnoticedialog = new AlertDialog.Builder(
                                        eqcheckformsave.this);
                                isnoticedialog.setTitle("選擇");
                                isnoticedialog.setNeutralButton("上傳圖像", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent testi=new Intent(eqcheckformsave.this,uploadimage.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("sysname", "CMSFEQCHECKLIST");
                                                testi.putExtras(bundle);
                                                startActivityForResult(testi, 3);
                                                scontextview=v;
                                            }
                                        }
                                );

                                isnoticedialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }
                                );

                                isnoticedialog.show();
                            }
                        }
                );
            }
        }
        catch(Exception ex1)
        {
        }
    }

    public List<String> getfilenew()
    {
        List<String> list = new ArrayList<>();
        String filePATH= "/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/";
        File file;
        try {
            file = new File(filePATH);
            if(!file.exists())
            {
            }
            String[] strFile = file.list();
            if(strFile.length > 0) {
                for (int i = 0; i < strFile.length; i++) {
                    list.add(strFile[i]);
                }
            } else {
//                Toast.makeText(this, "緩存表單為空！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            return null;
        }
        finally {
            filePATH=null;
            file=null;
        }
        return list;
    }

    public void cancleForm(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(eqcheckformsave.this);
        builder.setTitle("選擇").setMessage("確定要刪除?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("刪除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Exectempfile.instance().deleteDirectory(mFilePath);
            }
        });
        builder.create().show();
    }

    /**
     * 暫存資料按鈕
     * @param v
     */
    public  void savatempdata(View v)
    {
        execloadactivity.opendialog(this, "正在執行");
        final BaseHandler b=new BaseHandler(this,0,this);
        if(!isNetworkAvailable(eqcheckformsave.this)) {
            MyThreadPool.pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                            saveTimes = saveTimes + 1;
                            try {
                                List<String> list = new ArrayList<>();
                                if (lly1 != null) {
                                    for (int i = 0; i < lly1.getChildCount(); i++) {
                                        String Mitem1=((TextView) lly1.getChildAt(i).findViewById(R.id.Mitem1)).getText().toString().replace("\r\n","");
                                        Mitem1=Mitem1.replace("\n","");
                                        list.add(
                                                "[" + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitemid1)).getText().toString() + "],["
                                                        + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mdept1)).getText().toString() + "],["
                                                        + Mitem1 + "],["
                                                        + ((TextView) lly1.getChildAt(i).findViewById(R.id.Munit)).getText().toString() + "],["
                                                        + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mspec1)).getText().toString() + "],["
                                                        + ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).getText().toString() + "],["
                                                        + ((TextView) lly1.getChildAt(i).findViewById(R.id.MValuespec)).getText().toString() + "]"
                                        );
                                    }
                                }
                                if (list != null && list.size() > 0) {
                                    list.add(
                                            "[" + mMachinenoid+ "],["
                                                    + Eqname + "],["
                                                    + Checkdataid + "],["

                                                    + Eqopname + "],["
                                                    + Eqlinename + "],["
                                                    + Filerevsion + "]"
                                    );
//                                    r = Exectempfile.instance().savefilenew(mMachinenoid + "_" + Filerevsion, list, mMachinenoid+"/");
                                    r = Exectempfile.instance().savefile(mMachinenoid + "_" + Filerevsion + saveTimes, list);
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
        } else {
            //這裡執行 提交操作;
            MyThreadPool.pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (lly1 != null) {
                                    String temp1 = null;
                                    String temp2 = null;
                                    String temp3 = null;
                                    String temp4 = null;
                                    TextView textViewImageSpan;

                                    String[] strArray = null;
                                    Boolean checkdatastatus = true;


                                    //String rstr=Checkdataid+":"+Sessionuser+":"+lly1.getChildCount()+":";
                                    String rstr = "";
                                    int rcount = 0;
                                    clicktimes++;

                                    for (int i = 0; i < lly1.getChildCount(); i++) {
                                        //if(((EditText) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).getVisibility()==View.VISIBLE) {
                                        if (((EditText) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).isEnabled() == true) {
                                            temp1 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mspec1)).getText().toString();
                                            temp2 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).getText().toString();
                                            temp3 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitem1)).getText().toString();
                                            temp4 = ((TextView) lly1.getChildAt(i).findViewById(R.id.Mitemid1)).getText().toString();
                                            temp2 = temp2.toUpperCase();
                                            strArray = temp1.split(";");

                                            rstr = rstr + "{" + temp4 + ";" + temp2 + "}";
                                            rcount++;

                                            if (temp2 == null || temp2.length() <= 0) {
                                                checkdatastatus = false;
                                            }
                                            switch (strArray[0]) {
                                                case "1":
                                                    try {

                                                        if (Float.parseFloat(strArray[1]) > Float.parseFloat(temp2) || Float.parseFloat(strArray[2]) < Float.parseFloat(temp2)) {
                                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                                            if(clicktimes<2) {
                                                                checkdatastatus = false;
                                                            }
                                                            break;
                                                        } else {
                                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                        }
                                                    } catch (Exception e) {
                                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                                        checkdatastatus = false;
                                                    }

                                                    break;
                                                case "0":
                                                    if ("Y".equals(temp2) || "N".equals(temp2) || "y".equals(temp2) || "n".equals(temp2)) {
                                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                    } else {
                                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                                        // checkdatastatus = false;
                                                    }
                                                    break;
                                                case "2":
                                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                    break;
                                                case "3":
                                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                    break;
                                                case "4":
                                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                    break;
                                                case "5"://允許值為空，如果不為空，那麼檢測是否有上下限
                                                    if (strArray.length == 1||temp2.trim().equals("/")) {
                                                        ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                    } else {
                                                        try {
                                                            if (Float.parseFloat(strArray[1]) > Float.parseFloat(temp2) || Float.parseFloat(strArray[2]) < Float.parseFloat(temp2)) {

                                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                                                if(clicktimes<2) {
                                                                    checkdatastatus = false;
                                                                }
                                                                //checkdatastatus = true;
                                                            } else {
                                                                ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                                            }

                                                        } catch (Exception e) {
                                                            ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                                            checkdatastatus = false;
                                                        }

                                                    }
                                                    break;
                                                case "6"://上传图片
                                                    break;

                                                default:
                                                    ((TextView) lly1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                                    checkdatastatus = false;
                                                    break;
                                            }
                                        } else {

                                        }
                                    }


                                    if (!checkdatastatus) {
                                        Toast.makeText(eqcheckformsave.this, "资料格式异常或未填寫完成，请重新填写", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {
//                                        SOAPSENDSTR = Checkdataid + ":" + mUserId + ":" + rcount + ":" + rstr;
                                        SOAPSENDSTR = rcount + ":" + rstr;
//                                        SOAPSENDSTR = allrstr;
                                        MyThreadPool.pool.execute(new Runnable() {

                                            @Override
                                            public void run() {
                                                PublicSOAP ps1 = new PublicSOAP();
//                                                String tablename1 = (tablename.split("tx")[0].substring(0,tablename.split("tx")[0].length()-1)).substring(0,(tablename.split("tx")[0].substring(0,tablename.split("tx")[0].length()-1)).length()-1);
                                                String tablename1 =  tablename.substring(tablename.length()-1,tablename.length());
                                                String tablename2 = tablename.substring(0,tablename.length()-1) + "(" + tablename1 + ")";
                                                String str = ps1.commit(mMachinenoid, tablename2, mUserId, deviceno, modletype,SOAPSENDSTR);
                                                Message msg = handler.obtainMessage();
                                                msg.what=0;
                                                msg.obj = str;
                                                handler.sendMessage(msg);

                                            }
                                        });
                                        clicktimes=0;
                                    }
                                } else {
                                    Toast.makeText(eqcheckformsave.this, "未检查到资料，请退出重新获取", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            catch(Exception ex1)
                            {

                            }
                        }
                    }
            );
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    execloadactivity.canclediglog();
                    String str = (String) msg.obj;
                    if (str != null && str.equals("0")) {
                        Toast.makeText(eqcheckformsave.this, "提交成功", Toast.LENGTH_LONG).show();
//                        Toast.makeText(eqcheckformsave.this,mMachinenoid + "_" + tablename + mNumber + ".txt",Toast.LENGTH_SHORT).show();
                        Exectempfile.instance().DeleteFolder(mFilePath + mMachinenoid + "_" + tablename + mNumber + ".txt");
                        Intent intent = new Intent();
                        intent.putExtra("flag","MoreForms");
                        intent.putExtra("number",number);
                        setResult(RESULT_OK, intent);
                        finish();
                        //return;
                    }



                    if (str.equals("2")) {
                        Toast.makeText(eqcheckformsave.this, "不應該點檢，或者點檢的不是本部門的", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (str != null && str.equals("3")) {
                        Toast.makeText(eqcheckformsave.this, "点检值不完整", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (str != null && str.equals("4")) {
                        Toast.makeText(eqcheckformsave.this, "点检流程编号不完整", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str.equals("5")) {
                        Toast.makeText(eqcheckformsave.this, " 点检数量不完整", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("6")) {
                        Toast.makeText(eqcheckformsave.this, "点检值错误", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("7")) {
                        Toast.makeText(eqcheckformsave.this, "点检表单尚未设置", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("8")) {
                        Toast.makeText(eqcheckformsave.this, "參數不完整", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("9")) {
                        Toast.makeText(eqcheckformsave.this, "機台編號不存在或編號異常", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("10")) {
                        Toast.makeText(eqcheckformsave.this, "群組ID不存在或群組ID異常", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("11")) {
                    Toast.makeText(eqcheckformsave.this, "工程師opname狀態錯誤", Toast.LENGTH_LONG).show();
                    //return;
                    }
                    if (str != null && str.equals("12")) {
                        Toast.makeText(eqcheckformsave.this, "沒有維修權限或密碼不正確", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("13")) {
                    Toast.makeText(eqcheckformsave.this, "更新啟修人員修機狀態失敗", Toast.LENGTH_LONG).show();
                    //return;
                    }
                    if (str != null && str.equals("14")) {
                    Toast.makeText(eqcheckformsave.this, "該機台狀態不為啟修", Toast.LENGTH_LONG).show();
                    //return;
                    }
                    if (str != null && str.equals("15")) {
                        Toast.makeText(eqcheckformsave.this, "创建次数超过", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("16")) {
                        Toast.makeText(eqcheckformsave.this, "系統已經生成過單號", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("99")) {
                        Toast.makeText(eqcheckformsave.this, "保存数据失败", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (str != null && str.equals("802")) {
                        Toast.makeText(eqcheckformsave.this, "更改机台状态信息失败", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    break;
            }
        }
    };
   // 提交
//     public void commit(String str) {
//         String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
//         List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
//         httprequestinputdata hhi1 = new httprequestinputdata();
//         hhi1.setDataname("directpar");
//         hhi1.setDatavalue("createcheckdata");
//         li.add(hhi1);
//         httprequestinputdata hhi2 = new httprequestinputdata();
//         hhi2.setDataname("jsonstr");
//         hhi2.setDatavalue(str);
//         li.add(hhi2);
//
//         exechttprequest hf1=new exechttprequest();
//         hf1.getDataFromServer(2, new FreedomDataCallBack() {
//                     @Override
//                     public void onStart(Context C1) {
//                         execloadactivity.opendialog(C1, "正在執行");
//                     }
//
//                     @Override
//                     public void processData(Object paramObject, boolean paramBoolean) {
//                         if (paramObject != null && !"".equals(paramObject)) {
//                             Toast.makeText(eqcheckformsave.this,paramObject.toString(),Toast.LENGTH_LONG).show();
//                             try {
//                                 JSONObject object = new JSONObject(paramObject.toString());
//                                 JSONArray array = object.getJSONArray("Table1");
//                                 JSONObject object2 = array.getJSONObject(0);
//                                 if(Integer.parseInt(object2.getString("item1")) == 0) {
//                                     Toast.makeText(eqcheckformsave.this, object2.getString("item2"), Toast.LENGTH_SHORT).show();
//                                     finish();
//                                 } else {
//                                     Toast.makeText(eqcheckformsave.this, object2.getString("item2"), Toast.LENGTH_SHORT).show();
//                                 }
//                             } catch (JSONException e) {
//                                 e.printStackTrace();
//                             }
//                             execloadactivity.canclediglog();
//
//                         } else {
//                             Toast.makeText(eqcheckformsave.this, "提交失敗!", Toast.LENGTH_SHORT).show();
//                             execloadactivity.canclediglog();
//                         }
//                     }
//
//                     @Override
//                     public void onFinish(Context C1) {
//                         execloadactivity.canclediglog();
//                     }
//
//                     @Override
//                     public void onFailed(Object paramObject, Context C1) {
//
//                     }
//
//                     @Override
//                     public void onNoneImage(Context C1) {
//
//                     }
//                 },
//                 this, url1, li, null);
//
//     }

    @Override
    public  void BackData(Object so,Context cc,int type)
    {
        soapfilldata(so, type);
    }


    private void getsoapdata(int stype, String METHOD_NAME, String[] parnames, String[] parvalues, int parcount)
    {
        execloadactivity.opendialog(this,"正在執行");
        PublicSOAP p=new PublicSOAP();
        p.getsopdata(this,
                this, stype, METHOD_NAME, parnames, parvalues, parcount
        );
    }

    private void soapfilldata(Object o,int ctype)
    {
        execloadactivity.canclediglog();
        if(o==null) {
            Toast.makeText(this, String.valueOf(ctype) + ":獲取資料失敗，獲取資料為Null", Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject so=null;
        try {
            so = (SoapObject) o;
        }
        catch(Exception ex1)
        {
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
                    String r2=((SoapObject)((SoapObject)((SoapObject)((SoapObject)so.getProperty(0)).getProperty(1)).getProperty(0)).getProperty(0)).getProperty(0).toString();
                    eqcheckdheadertv1.setText(r2);
                    break;
                default:
                    Toast.makeText(this, "Unknow Methods Return Result", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
