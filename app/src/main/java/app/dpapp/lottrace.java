package app.dpapp;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class lottrace extends AppCompatActivity {

    Spinner D1s;

    //填充机种ListView 数据源
    private static List<String> Dl1;
    //填充机种ListView  adapter适配器
    private static ArrayAdapter<String> As1;
    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;

    private String soapresult1;
    private String scanstr1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    TextView promt;  //批号
    TextView wonotv1;
    TextView devicenotv1;
    TextView inputtv1;
    TextView statustv1;
    TextView configtv1;

    LinearLayout ll1;
    List<Slist2> Sourcelist;
    Sdata1 S1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;
    private NfcAdapter mAdapter;

    private String sysErrorcode; // 1:获取资料异常


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottrace);

        try {
            promt = (TextView) findViewById(R.id.lottracetext_tv1);
            wonotv1 = (TextView) findViewById(R.id.lottracewotext_tv1);
            devicenotv1 = (TextView) findViewById(R.id.lottracedevicetext_tv1);
            inputtv1 = (TextView) findViewById(R.id.lottraceinputtext_tv1);
            statustv1 = (TextView) findViewById(R.id.lottracestatustext_tv1);
            configtv1 = (TextView) findViewById(R.id.lottraceconfigtext_tv1);
            ll1 = (LinearLayout) findViewById(R.id.lottraceLinearLayout1);

            final Staticdata app = (Staticdata) getApplication();
            Sessionuser = app.getLoginUserID();

            Bundle bundle = this.getIntent().getExtras();

            promt.setText(bundle.getString("Lot"));
            //promt.setText( "K61730139-01");

            new Thread(new Runnable() {

                @Override
                public void run() {
                    getRemoteInfo();
                    Message msg = handle.obtainMessage();
                    msg.obj = S1;
                    handle.sendMessage(msg);
                }
            }).start();
        }
        catch(Exception e1)
        {}
    }

    protected void onResume() {
        super.onResume();

        //nfconclick(null) ;
    }

    public void getRemoteInfo() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;
            String METHOD_NAME = "getwonoandstationinfo";
            String SOAP_ACTION = "http://tempuri.org/getwonoandstationinfo ";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("lotno", promt.getText().toString());  //输入批号信息

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

            // 获取返回的结果
            //soapresult1 = r1.getProperty(0).toString();
            SoapObject soapchild;
            SoapObject sopadata;
            try {

                soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
                sopadata = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

            } catch (Exception e) {
                e.printStackTrace();
                sysErrorcode = "2";
                return;
            }

            String tempsoap1 = null;
            String tempsoap2 = null;
            String tempsoap3 = null;
            String tempsoap4 = null;
            String tempsoap5 = null;
            String tempsoap6 = null;

            Sourcelist = new ArrayList<Slist2>();
            S1 = new Sdata1();

            S1.wono = ((SoapObject) soapchild.getProperty(0)).getProperty("wono").toString();
            S1.inputqty = ((SoapObject) soapchild.getProperty(0)).getProperty("dieqty").toString();
            S1.lotstatus = ((SoapObject) soapchild.getProperty(0)).getProperty("lotstate").toString();
            S1.deviceno = ((SoapObject) soapchild.getProperty(0)).getProperty("deviceno").toString();
            S1.config = ((SoapObject) soapchild.getProperty(0)).getProperty("config").toString();
            for (int i = 1; i < soapchild.getPropertyCount(); i++) {

                tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNO").toString();
                tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString();
                tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("INPUTQTY").toString();
                tempsoap4 = ((SoapObject) soapchild.getProperty(i)).getProperty("GOODQTY").toString();
                tempsoap5 = ((SoapObject) soapchild.getProperty(i)).getProperty("SETUPQTY").toString();
                tempsoap6 = ((SoapObject) soapchild.getProperty(i)).getProperty("PROCESSYIELD").toString();

                Slist2 s1 = new Slist2();

                s1.Opno = tempsoap1;
                s1.OPname = tempsoap2;
                s1.Inqty = tempsoap3;
                s1.Outqty = tempsoap4;
                s1.Setupqty = tempsoap5;
                s1.Yield = tempsoap6;
                ;
                Sourcelist.add(s1);
            }
            S1.slist2s = Sourcelist;
        }
        catch (Exception e1)
        {}
    }


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
           // Log.i("handle", "into handle");
            //Dl2 = (Dl2)msg.obj;
            //Log.i("TAG", result);
            try {
                if (sysErrorcode != null && sysErrorcode == "1") {
                    Toast.makeText(lottrace.this, "获取资料异常", Toast.LENGTH_LONG).show();
                    return;
                }

                //=2为获取不到资料
                if (sysErrorcode != null && sysErrorcode.equals("2")) {
                    Toast.makeText(lottrace.this, "無資料", Toast.LENGTH_LONG).show();
                    return;
                }

                View convertView;
                wonotv1.setText(S1.getWono());
                devicenotv1.setText(S1.getDeviceno());
                inputtv1.setText(S1.getInputqty());
                statustv1.setText(S1.getLotstatus());
                configtv1.setText(S1.getConfig());
                for (int i = 0; i < Sourcelist.size(); i++) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source3, ll1, false);
                    ((TextView) convertView.findViewById(R.id.ls3_opid)).setText(Sourcelist.get(i).Opno.toString());
                    ((TextView) convertView.findViewById(R.id.ls3_opname)).setText(Sourcelist.get(i).OPname.toString());
                    ((TextView) convertView.findViewById(R.id.ls3_inputqty)).setText(Sourcelist.get(i).Inqty.toString());
                    ((TextView) convertView.findViewById(R.id.ls3_outputqty)).setText(Sourcelist.get(i).Outqty.toString());
                    ((TextView) convertView.findViewById(R.id.ls3_setupqty)).setText(Sourcelist.get(i).Setupqty.toString());
                    ((TextView) convertView.findViewById(R.id.ls3_yield)).setText(Sourcelist.get(i).Yield.toString());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(lottrace.this
                                    , lottraceopdata.class);

                            Bundle bundle = new Bundle();

                            bundle.putString("Lot", promt.getText().toString());
                            bundle.putString("opname", ((TextView) v.findViewById(R.id.ls3_opname)).getText().toString());
                            bundle.putString("opno", ((TextView) v.findViewById(R.id.ls3_opid)).getText().toString());

                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    });


                    ll1.addView(convertView);
                }
                //testsoapet.setText(soapresult1);
            }
            catch(Exception e1)
            {

                Toast.makeText(lottrace.this, "無資料"+e1, Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

    class Slist2 {
        private String Opno;
        private String OPname;
        private String Inqty;
        private String Outqty;
        private String Setupqty;
        private String Yield;

        public String getOpno() {
            return Opno;
        }

        public void setOpno(String str1) {
            this.Opno = str1;
        }

        public String getOPname() {
            return OPname;
        }

        public void setOPname(String str1) {
            this.OPname = str1;
        }

        public String getInqty() {
            return Inqty;
        }

        public void setInqty(String str1) {
            this.Inqty = str1;
        }

        public String getOutqty() {
            return Outqty;
        }

        public void setOutqty(String str1) {
            this.Outqty = str1;
        }

        public String getSetupqty() {
            return Setupqty;
        }

        public void setSetupqty(String str1) {
            this.Setupqty = str1;
        }

        public String getYield() {
            return Yield;
        }

        public void setYield(String str1) {
            this.Yield = str1;
        }
    }

    class Sdata1{
        protected List<Slist2> slist2s;
        protected String wono;
        protected String inputqty;
        protected String lotstatus;
        protected String deviceno;
        protected String config;

        public List<Slist2> getSlist2s(){return slist2s;}
        public String getWono(){ return wono;}
        public String getInputqty(){return inputqty;}
        public String getLotstatus(){return lotstatus;}
        public String getDeviceno(){return deviceno;}
        public String getConfig(){return config;}

        public void setSlist2s(List<Slist2> s2){this.slist2s=s2;}
        public void setWono(String wono) {this.wono=wono;}
        public void setInputqty(String inputqty){this.inputqty=inputqty;}
        public void setLotstatus(String lotstuas){this.lotstatus=lotstuas;}
        public void setDeviceno(String deviceno){this.deviceno=deviceno;}
        public void setConfig(String config){this.deviceno=config;}


    }
}
