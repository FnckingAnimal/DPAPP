package app.dpapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.PublicSOAP;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.Interface.Iobjectrhandler;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.FreedomSOAPCallBack;
import app.cmapp.machinecheck.ImageActivity;

/**
 * Created by Tod on 2016/4/15.
 */
public class eqcheckreportdetaillist extends AppCompatActivity implements Iobjectrhandler, FreedomSOAPCallBack {


    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String Eqid;
    protected String Eqname;
    protected String Eqopname;
    protected String Eqlinename;
    protected String Checkdataid;

    protected TextView eqopnametv1;
    protected TextView eqlinanemtv1;
    private TextView mDeviceno;

    private String sysErrorcode; // 1:获取资料异常

    //定义数据源
    protected List<Slist> Sourcelist;
    private String mFileversion;
    private TextView mTableName;
    private Bitmap bitmap;
    private View convertView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
        private String Valuespec2;
        private String Valueshowpoint;
        private String Valueunit;
        private String NEWSTANDARDS;

        public String getValuespec2() {
            return Valuespec2;
        }

        public void setValuespec2(String valuespec2) {
            Valuespec2 = valuespec2;
        }

        public String getNEWSTANDARDS() {
            return NEWSTANDARDS;
        }

        public void setNEWSTANDARDS(String NEWSTANDARDS) {
            this.NEWSTANDARDS = NEWSTANDARDS;
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


    Button bt1;
    LinearLayout lly1;
    protected TextView eqcheckdheadertv1;

    private Dialog dialog;
    private ImageView mImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checkreportdetaillist);
        init();
        try {
            Bundle bundle = this.getIntent().getExtras();
            Sessionuser = bundle.getString("Sessionuser");
            Eqid = bundle.getString("Eqid");
            Checkdataid = bundle.getString("checkdataid");
            Eqopname = bundle.getString("Eqopname");
            Eqlinename = bundle.getString("Eqlinename");
            mFileversion = bundle.getString("Fileversion");

            eqopnametv1 = (TextView) findViewById(R.id.eq_checkreportdetaillistopnametv1);
            eqlinanemtv1 = (TextView) findViewById(R.id.eq_checkreportdetaillistlinenametv1);
            mTableName = (TextView) findViewById(R.id.tv_tablename);
            mDeviceno = (TextView) findViewById(R.id.tv_device);
            eqcheckdheadertv1 = (TextView) findViewById(R.id.eq_checklisttv1);

            eqopnametv1.setText(Eqopname);
            eqlinanemtv1.setText(Eqlinename);
            mTableName.setText(mFileversion);
            mDeviceno.setText(bundle.getString("device"));

            bt1 = (Button) findViewById(R.id.eq_checklistbutton1);

            lly1 = (LinearLayout) findViewById(R.id.eqchecklistLinearLayout);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getRemoteInfo();
                    Message msg = handle.obtainMessage();
                    msg.obj = Sourcelist;
                    msg.what = 0;
                    handle.sendMessage(msg);
                }
            }).start();

            String METHOD_NAME = "getchecktitleandfooter";
            String[] pn1 = {"flowid"};
            String[] pv1 = {Checkdataid};
            getsoapdata(1, METHOD_NAME, pn1, pv1, 1);
        } catch (Exception ex1) {

        }
    }

    private void init() {
        //大图所依附的dialog
//        dialog = new Dialog(this, R.style.AlertDialog_AppCompat_Light_);
        dialog = new Dialog(this, R.style.dialog);
        mImageView = getImageView(bitmap);
        dialog.setContentView(mImageView);

        //大图的点击事件（点击让他消失）
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        //大图的长按监听
//        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //弹出的“保存图片”的Dialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        saveCroppedImage(((BitmapDrawable) mImageView.getDrawable()).getBitmap());
//                    }
//                });
//                builder.show();
//                return true;
//            }
//        });
    }

    //动态的ImageView
    private ImageView getImageView(Bitmap bitmap) {
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(5, 5, 5, 5);
        iv.setMaxHeight(1000);
        iv.setMaxWidth(1000);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setAdjustViewBounds(true);
        //imageView设置图片
        iv.setImageBitmap(bitmap);
        return iv;
    }

    @Override
    public void BackData(Object so, Context cc, int type) {
        soapfilldata(so, type);
    }

    private void soapfilldata(Object o, int ctype) {
        execloadactivity.canclediglog();
        if (o == null) {
//            Toast.makeText(this, String.valueOf(ctype) + ":獲取資料失敗，獲取資料為Null", Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject so;
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

    private void getsoapdata(int stype, String METHOD_NAME, String[] parnames, String[] parvalues, int parcount) {
        execloadactivity.opendialog(this, "正在執行");
        PublicSOAP p = new PublicSOAP();
        p.getsopdata(this, this, stype, METHOD_NAME, parnames, parvalues, parcount);
    }

    /**
     * 載入暫存資料成功顯示至UI
     *
     * @param obj
     */
    private void loadtempdatatoui(Object obj) {
        try {
            List<String> ls = (List<String>) obj;
            String tempa, tempb;
            for (String s : ls) {
                if (s == null || s.trim().equals(""))
                    continue;

                tempa = s.substring(1, s.indexOf("],["));
                tempb = s.substring(s.indexOf("],[") + 3, s.length() - 1);

                for (int i = 0; i < lly1.getChildCount(); i++) {
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

    public void getRemoteInfo() {
        String NAMESPACE = "http://tempuri.org/";

        String URL = Staticdata.soapurl;

        String METHOD_NAME = "GetCheckContentnew";// "GetCheckContent";
        String SOAP_ACTION = "http://tempuri.org/GetCheckContentnew"; //"http://tempuri.org/GetCheckContent";

        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        //rpc.addProperty("machinesysid", scanstr1);
        //rpc.addProperty("machinesysid",Eqid);
        //rpc.addProperty("checktype", "0");    //0:点检表单
        //rpc.addProperty("userid", Sessionuser);
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
            sysErrorcode = "1";
            return;
        }

        SoapObject r1 = (SoapObject) envelope.bodyIn;

        //SoapObject soapchild=(SoapObject)r1.getProperty(0);
        SoapObject soapchild;
        try {
            soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
        } catch (Exception e) {
            e.printStackTrace();
            sysErrorcode = "2";
            return;
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
        Sourcelist = new ArrayList<>();

        for (int i = 0; i < soapchild.getPropertyCount(); i++) {

            tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty("ITEMID").toString();
            tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty("ITEMVALUE").toString();
            tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("OWNER").toString();
            tempsoap4 = ((SoapObject) soapchild.getProperty(i)).getProperty("STANDARDS").toString();
            tempsoap9 = ((SoapObject) soapchild.getProperty(i)).getProperty("STANDARD").toString();
            tempsoap5 = "0";
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
                tempsoap8 = ((SoapObject) soapchild.getProperty(i)).getProperty("NEWSTANDARDS").toString();
            } catch (Exception ex) {
                tempsoap8 = "";
            }

            Slist s1 = new Slist();

            s1.Sessionuser = Sessionuser;
            s1.Eqid = Eqid;
            s1.Eqname = Eqname;
            s1.Sysdocumentid = "";
            s1.Idept = tempsoap3;
            s1.Itemid = tempsoap1;
            s1.Itemname = tempsoap2;
            s1.Value = tempsoap6;
            s1.Valuespec = tempsoap4;
            s1.Valuespec2 = tempsoap9;
            s1.Valueshowpoint = tempsoap5;
            s1.Valueunit = tempsoap7;
            s1.setNEWSTANDARDS(tempsoap8);
            Sourcelist.add(s1);
        }

    }


    private Handler handle = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (sysErrorcode != null && sysErrorcode == "1") {
                        Toast.makeText(eqcheckreportdetaillist.this, "获取资料异常", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (sysErrorcode == "2") {
                        return;
                    }
                    View convertView1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source2, null);
                    ((TextView) convertView1.findViewById(R.id.Mitemid1)).setText("Number");
                    ((TextView) convertView1.findViewById(R.id.Mdept1)).setText("Owner");
                    ((TextView) convertView1.findViewById(R.id.Mitem1)).setText("Item");
                    ((TextView) convertView1.findViewById(R.id.Munit)).setText("Unit");
                    ((TextView) convertView1.findViewById(R.id.Mspec1)).setText("Specs");
                    ((EditText) convertView1.findViewById(R.id.Mvalue1)).setText("Values");
                    ((EditText) convertView1.findViewById(R.id.Mvalue1)).setEnabled(false);
                    ((TextView) convertView1.findViewById(R.id.MValuespec)).setText("");
                    ((TextView) convertView1.findViewById(R.id.tv_guige_values)).setText("");
                    lly1.addView(convertView1);
                    String temp1;
                    for (int i = 0; i < Sourcelist.size(); i++) {
                        temp1 = Sourcelist.get(i).Valuespec;
                        if (temp1.length() > 2) {
                            temp1 = temp1.substring(2, temp1.length()); //substring(3,temp1.length()-2);
                        }
                        if (temp1.length() == 2 || temp1.length() == 1) {
                            temp1 = "";
                        }
                        final String str = Sourcelist.get(i).Value;
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source2, null);
                        ((TextView) convertView.findViewById(R.id.Mitemid1)).setText(Sourcelist.get(i).Itemid.toString());
                        ((TextView) convertView.findViewById(R.id.Mdept1)).setText(Sourcelist.get(i).Idept.toString());
                        ((TextView) convertView.findViewById(R.id.Mitem1)).setText(Sourcelist.get(i).Itemname.toString());
                        ((TextView) convertView.findViewById(R.id.Munit)).setText(Sourcelist.get(i).Valueunit.toString());
//                    ((EditText) convertView1.findViewById(R.id.Mvalue1)).setVisibility(View.VISIBLE);
//                  ((TextView) convertView.findViewById(R.id.tv_guige_values)).setText(Sourcelist.get(i).getNEWSTANDARDS());
                        ((TextView) convertView.findViewById(R.id.Mspec1)).setText(Sourcelist.get(i).NEWSTANDARDS);
                        ((EditText) convertView.findViewById(R.id.Mvalue1)).setVisibility(View.VISIBLE);
                        ((ImageView) convertView.findViewById(R.id.imageView)).setVisibility(View.GONE);
                        ((EditText) convertView.findViewById(R.id.Mvalue1)).setText(str);
                        ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(false);

                        String[] strArray;
                        strArray = Sourcelist.get(i).Valuespec.split(";");
                        if ("1".equals(strArray[0].trim()) || "5".equals(strArray[0].trim())) {
                            strArray = Sourcelist.get(i).Valuespec2.split(";");
                        } else {
                            strArray = Sourcelist.get(i).Valuespec.split(";");
                        }
                        if ("6 ".equals(temp1)) {
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(true);
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(eqcheckreportdetaillist.this, ImageActivity.class);
                                    intent.putExtra("formid", str);
                                    startActivity(intent);
                                }
                            });
                        }

                        switch (strArray[0].trim()) {
                            case "1":
                                if (strArray.length == 1) {
                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                } else if (strArray.length == 2) {
                                    if (strArray[1].contains("~")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1].split("~")[0]) &&
                                                    Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[1].split("~")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        }
                                    } else if (strArray[1].contains("±")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= (Float.parseFloat(strArray[1].split("±")[0]) - Float.parseFloat(strArray[1].split("±")[1])) &&
                                                    Float.parseFloat(Sourcelist.get(i).Value) <= (Float.parseFloat(strArray[1].split("±")[0]) + Float.parseFloat(strArray[1].split("±")[1]))) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains(">")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) > Float.parseFloat(strArray[1].split(">")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("<")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) < Float.parseFloat(strArray[1].split("<")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("<=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[1].split("<=")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains(">=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1].split(">=")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≥")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1].split("≥")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≤")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[1].split("≤")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) == Float.parseFloat(strArray[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        }
                                    }
                                } else if (strArray.length == 3) {
                                    if (">".equals(strArray[1])) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) > Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if ("<".equals(strArray[1])) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) < Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("<=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains(">=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≥")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≤")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1]) &&
                                                    Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    }
                                }
                                break;
                            case "5":
                                if (strArray.length == 1 || Sourcelist.get(i).Value.trim().equals("/") || Sourcelist.get(i).Value.trim().equals("NA")) {
                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                } else if (strArray.length == 2) {
                                    if (strArray[1].contains("~")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1].split("~")[0]) &&
                                                    Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[1].split("~")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        }
                                    } else if (strArray[1].contains("±")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= (Float.parseFloat(strArray[1].split("±")[0]) - Float.parseFloat(strArray[1].split("±")[1])) &&
                                                    Float.parseFloat(Sourcelist.get(i).Value) <= (Float.parseFloat(strArray[1].split("±")[0]) + Float.parseFloat(strArray[1].split("±")[1]))) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains(">")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) > Float.parseFloat(strArray[1].split(">")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("<")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) < Float.parseFloat(strArray[1].split("<")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("<=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[1].split("<=")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains(">=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1].split(">=")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≥")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1].split("≥")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≤")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[1].split("≤")[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) == Float.parseFloat(strArray[1])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        }
                                    }
                                } else if (strArray.length == 3) {
                                    if (">".equals(strArray[1])) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) > Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if ("<".equals(strArray[1])) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) < Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("<=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains(">=")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≥")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else if (strArray[1].contains("≤")) {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } else {
                                        try {
                                            if (Float.parseFloat(Sourcelist.get(i).Value) >= Float.parseFloat(strArray[1]) &&
                                                    Float.parseFloat(Sourcelist.get(i).Value) <= Float.parseFloat(strArray[2])) {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                            } else {
                                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                            }
                                        } catch (Exception e) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    }
                                }
                                break;
                            case "4":
                                if (strArray.length == 1) {
                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                } else {
                                    try {
                                        if (Sourcelist.get(i).Value.trim().toUpperCase().equals(strArray[1].toUpperCase())) {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                        } else {
                                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                        }
                                    } catch (Exception e) {
                                        ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                    }
                                }
                                break;
                            case "6":
//                                final int position = i;
                                final String str1 = Sourcelist.get(i).Value.toString();
                                final String item = ((TextView) convertView.findViewById(R.id.Mitemid1)).getText().toString();
//                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setVisibility(View.GONE);
//                                ((ImageView) convertView.findViewById(R.id.imageView)).setVisibility(View.VISIBLE);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(true);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setCursorVisible(false);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusable(false);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setFocusableInTouchMode(false);
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MyThreadPool.pool.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                View view = lly1.getChildAt(Integer.parseInt(item) + 1);
                                                String str = getImageAddress(str1);
                                                Message msg = handle.obtainMessage();
                                                msg.what = 2;
                                                msg.obj = str;
                                                handle.sendMessage(msg);
                                            }
                                        });
                                    }
                                });
                                break;
                        }
                        lly1.addView(convertView);
                    }
                    break;
                case 1:
                    String str = (String) msg.obj;
                    if (str != null && !"".equals(str)) {
                        String[] str1 = str.split("and");
                        if ("True".equals(str1[0])) {
                            ((TextView) lly1.getChildAt(Integer.parseInt(str1[1])).findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                        } else if ("False".equals(str1[0])) {
                            ((TextView) lly1.getChildAt(Integer.parseInt(str1[1])).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                        } else {
                            ((TextView) lly1.getChildAt(Integer.parseInt(str1[1])).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                        }
                    }
                    break;
                case 2:
                    try {
                        String fileName = (String) msg.obj;
//                        String[] strings = fileName.split("and");
//                        final int i = Integer.parseInt(strings[1]);
//                Upload upload = new Upload(fileName,i);
//                upload.start();
                        String str1 = fileName.replace("\\", ";");
                        String[] str2 = str1.split(";");
                        final String iPath = Staticdata.httpurl + "Upload/img/" + str2[5] + "/" + str2[6];
                        //新建线程加载图片信息，发送到消息队列中
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                bitmap = getURLimage(iPath);
                                Message msg = new Message();
                                msg.what = 111;
                                handle.sendMessage(msg);
                            }
                        }).start();
                    } catch (Exception e) {
                        Toast.makeText(eqcheckreportdetaillist.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case 111:
//                    int position = (int) msg.obj;
//                    ((ImageView) lly1.getChildAt(position + 1).findViewById(R.id.imageView)).setImageBitmap(bitmap);
//                    ((ImageView) lly1.getChildAt(position + 1).findViewById(R.id.imageView)).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
                    mImageView = getImageView(bitmap);
                    dialog.setContentView(mImageView);
                    dialog.show();
//                        }
//                    });
                    break;

            }

        }
    };

    /**
     * 獲取圖片存儲地址
     *
     * @return
     */
    public String getImageAddress(String formid) {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "getFilePath";//"UploadCheckdata";
            String SOAP_ACTION = "http://tempuri.org/getFilePath";//"http://tempuri.org/UploadCheckdata";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("FORMID", formid);

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
                return "";
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;
            return r1.getProperty(0).toString();
        } catch (Exception ex1) {
            return "";
        }
    }


// 检查点检是否超规

    public String isCheckstandards(String checkstandards, String checkvalue) {
        String NAMESPACE = "http://tempuri.org/";

        String URL = Staticdata.soapurl;

        String METHOD_NAME = "checklimitvaluestr";
        String SOAP_ACTION = "http://tempuri.org/checklimitvaluestr";

        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("checkstandards", checkstandards);
        rpc.addProperty("checkvalue", checkvalue);

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
        }
        String str;
        SoapObject r1 = (SoapObject) envelope.bodyIn;
        Object o = r1.getProperty(0);
        if (o != null && !"".equals(o)) {
            str = o.toString();
        } else {
            str = "False";
        }
        return str;
    }


//    private class Upload extends Thread {
//        private String filePath;
//        private int position;
//        public Upload(String filePath,int position) {
//            this.filePath = filePath;
//            this.position = position;
//        }
//        @Override
//        public void run() {
//            super.run();
//            //图片地址
//            //D:\WebApp\MobileAppAPI\Upload\img\20200613\F202006132254-005.jpg
////            String str = "D:\\WebApp\\MobileAppAPI\\Upload\\img\\20200613\\F202006132254-005.jpg";
//            if(TextUtils.isEmpty(filePath)) {
//                String str1 = filePath.replace("\\",";");
//                String[] str2 = str1.split(";");
//                String iPath = "http://10.142.136.222:8107/Upload/img/" + str2[5] + "/" + str2[6];
//                try {
//                    //对资源链接
//                    URL url = new URL(iPath);
//                    //打开输入流
//                    InputStream inputStream = url.openStream();
//                    //对网上资源进行下载并转换为位图图片
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                    Message msg = handle.obtainMessage();
//                    msg.what = 111;
//                    msg.obj = position;
//                    handle.sendMessage(msg);
//                    inputStream.close();
//                    //再打开一次
//                    inputStream = url.openStream();
//                    File file = new File(Environment.getExternalStorageDirectory() + "/newImage.png");
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    int hasRead = 0;
//                    while ((hasRead = inputStream.read()) != -1) {
//                        fileOutputStream.write(hasRead);
//                    }
//                    fileOutputStream.close();
//                    inputStream.close();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(eqcheckreportdetaillist.this,"異常!!!",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

}