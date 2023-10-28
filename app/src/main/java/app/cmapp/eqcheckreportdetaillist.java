package app.cmapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.cmapp.appcdl.execloadactivity;
import app.cmapp.Interface.Iobjectrhandler;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.machinecheck.ImageActivity;
import app.dpapp.R;
import app.dpapp.appcdl.FreedomSOAPCallBack;
import app.dpapp.soap.PublicSOAP;

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

    private String sysErrorcode; // 1:获取资料异常

    //定义数据源
    protected List<Slist> Sourcelist;
    private String mFileversion;
    private TextView mTableName;
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
    protected TextView eqcheckdheadertv1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checkreportdetaillist);
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
            mTableName= (TextView) findViewById(R.id.tv_tablename);
            eqcheckdheadertv1 = (TextView) findViewById(R.id.eq_checklisttv1);

            eqopnametv1.setText(Eqopname);
            eqlinanemtv1.setText(Eqlinename);
            mTableName.setText(mFileversion);

            bt1 = (Button) findViewById(R.id.eq_checklistbutton1);

            lly1 = (LinearLayout) findViewById(R.id.eqchecklistLinearLayout);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    getRemoteInfo();
                    Message msg = handle.obtainMessage();
                    msg.obj = Sourcelist;
                    handle.sendMessage(msg);
                }
            }).start();

            String METHOD_NAME = "getchecktitleandfooter";
            String[] pn1={"flowid"};
            String[] pv1={Checkdataid};
            getsoapdata(1, METHOD_NAME, pn1, pv1, 1);
        }
        catch (Exception ex1) {

        }
    }

    @Override
    public  void BackData(Object so,Context cc,int type)
    {
        soapfilldata(so, type);
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
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        execloadactivity.canclediglog();
    }

    private void getsoapdata(int stype, String METHOD_NAME, String[] parnames, String[] parvalues, int parcount)
    {
        execloadactivity.opendialog(this,"正在執行");
        PublicSOAP p=new PublicSOAP();
        p.getsopdata(this,
                this, stype, METHOD_NAME, parnames, parvalues, parcount
        );
    }

    /**
     * 載入暫存資料成功顯示至UI
     * @param obj
     */
    private void loadtempdatatoui(Object obj)
    {
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
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    
    public void getRemoteInfo() {
        String NAMESPACE = "http://tempuri.org/";

        String URL=Staticdata.soapurl;

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
            sysErrorcode="1";
            return;
        }

        SoapObject r1 = (SoapObject) envelope.bodyIn;

        //SoapObject soapchild=(SoapObject)r1.getProperty(0);
        SoapObject soapchild;
        try {
            soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
        }
        catch (Exception e) {
            e.printStackTrace();
            sysErrorcode="2";
            return;
        }
        // 获取返回的结果
        //soapresult1 = r1.getProperty(0).toString();
        String  tempsoap1;
        String  tempsoap2;
        String  tempsoap3;
        String  tempsoap4;
        String  tempsoap5;
        String  tempsoap6;
        String  tempsoap7;
        Sourcelist = new ArrayList<>();

        for (int i = 0; i < soapchild.getPropertyCount(); i++) {

            tempsoap1= ((SoapObject)soapchild.getProperty(i)).getProperty("ITEMID").toString();
            tempsoap2=((SoapObject)soapchild.getProperty(i)).getProperty("ITEMVALUE").toString();
            tempsoap3= ((SoapObject)soapchild.getProperty(i)).getProperty("OWNER").toString();
            tempsoap4=((SoapObject)soapchild.getProperty(i)).getProperty("STANDARDS").toString();
            tempsoap5="0";
            try {
                tempsoap6 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKVALUE").toString();
            }
            catch(Exception ex)
            {
                tempsoap6="";
            }
            try {
                tempsoap7 = ((SoapObject) soapchild.getProperty(i)).getProperty("UNIT").toString();
            } catch (Exception ex) {
                tempsoap7 = "";
            }

            Slist s1 = new Slist();

                s1.Sessionuser = Sessionuser;
                s1.Eqid = Eqid;
                s1.Eqname = Eqname;
                s1.Sysdocumentid = "";
                s1.Idept = tempsoap3;
                s1.Itemid = tempsoap1; ;
                s1.Itemname =tempsoap2;
                s1.Value =tempsoap6;
                s1.Valuespec =tempsoap4;
                s1.Valueshowpoint = tempsoap5;
                s1.Valueunit=tempsoap7;
                Sourcelist.add(s1);
        }

    }


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if(sysErrorcode!=null && sysErrorcode=="1") {
                Toast.makeText(eqcheckreportdetaillist.this, "获取资料异常", Toast.LENGTH_LONG).show();
                return;
            }

            if(sysErrorcode=="2") {
                return;
            }

            View convertView;
            String temp1;
            for(int i=0;i<Sourcelist.size();i++) {
                temp1 = Sourcelist.get(i).Valuespec.toString();
                if (temp1.length()>2)
                {
                    temp1=temp1.substring(2,temp1.length()); //substring(3,temp1.length()-2);
                }
//                else
//                {
//                    temp1="";
//                }
                final String str = Sourcelist.get(i).Value.toString();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source2, null);
                ((TextView) convertView.findViewById(R.id.Mitemid1)).setText(Sourcelist.get(i).Itemid.toString());
                ((TextView) convertView.findViewById(R.id.Mdept1)).setText(Sourcelist.get(i).Idept.toString());
                ((TextView) convertView.findViewById(R.id.Mitem1)).setText(Sourcelist.get(i).Itemname.toString());
                ((TextView) convertView.findViewById(R.id.Munit)).setText(Sourcelist.get(i).Valueunit.toString());
                ((TextView) convertView.findViewById(R.id.Mspec1)).setText(temp1);
                ((EditText) convertView.findViewById(R.id.Mvalue1)).setText(str);
                ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(false);

                String[] strArray = Sourcelist.get(i).Valuespec.toString().split(";");
                if("6 ".equals(temp1)) {
                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setEnabled(true);
                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(eqcheckreportdetaillist.this,ImageActivity.class);
                            intent.putExtra("formid",str);
                            startActivity(intent);
                        }
                    });
                }

                switch (strArray[0]) {
                    case "1":
                        try {
                            if(Float.parseFloat(Sourcelist.get(i).Value.toString()) >= Float.parseFloat(strArray[1]) &&
                                    Float.parseFloat(Sourcelist.get(i).Value.toString()) <= Float.parseFloat(strArray[2])) {
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                            } else {
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                            }

                        } catch (Exception e) {
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                        }

                        break;
                    case "5":

                        if(strArray.length==1||Sourcelist.get(i).Value.toString().trim().equals("/"))
                        {
                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                        }
                        else
                        {
                            try {
                                if(Float.parseFloat(Sourcelist.get(i).Value.toString()) >= Float.parseFloat(strArray[1]) &&
                                        Float.parseFloat(Sourcelist.get(i).Value.toString()) <= Float.parseFloat(strArray[2])) {
                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
                                } else {
                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                                }
                            } catch (Exception e) {
                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                            }

                        }

                        break;
//                    case "6":
//
//                        break;

//                    case "2222222222":
//
//                        if(strArray.length==1||Sourcelist.get(i).Value.toString().trim().equals("/"))
//                        {
//                            ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
//                        }
//                        else
//                        {
//                            try {
//
////                                if (Float.parseFloat(strArray[1]) >= Float.parseFloat(Sourcelist.get(i).Value.toString()) ||
////                                        Float.parseFloat(strArray[2]) < Float.parseFloat(Sourcelist.get(i).Value.toString())) {
////                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
////                                } else {
////                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
////                                }
//                                Toast.makeText(eqcheckreportdetaillist.this,strArray[1] + "+++" + Sourcelist.get(i).Value.toString(),Toast.LENGTH_SHORT).show();
//
//                                if(Float.parseFloat(Sourcelist.get(i).Value.toString()) >= Float.parseFloat(strArray[1]) &&
//                                        Float.parseFloat(Sourcelist.get(i).Value.toString()) <= Float.parseFloat(strArray[2])) {
//                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.BLACK);
//                                } else {
//                                    ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
//                                }
//                            } catch (Exception e) {
//                                ((EditText) convertView.findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
//                            }
//
//                        }
//
//                        break;
                }
                lly1.addView(convertView);
            }

        }
    };


}