package app.dpapp;

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
public class lottraceopdata extends AppCompatActivity {

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

    TextView Lotnotv1;  //批号
    TextView opnametv1;  //站位名称
    String opnostr1;

    LinearLayout ll1;
    List<LotopdataSlist1> Sourcelist;   //数据源
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private String sysErrorcode; // 1:获取资料异常

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottraceopdata);

        try {
            Lotnotv1 = (TextView) findViewById(R.id.lottraceopdatatext_tv1);
            opnametv1 = (TextView) findViewById(R.id.lottraceopnametext_tv1);

            ll1 = (LinearLayout) findViewById(R.id.lottraceopdataLinearLayout1);

            final Staticdata app = (Staticdata) getApplication();
            Sessionuser = app.getLoginUserID();

            Bundle bundle = this.getIntent().getExtras();

            Lotnotv1.setText(bundle.getString("Lot"));
            opnametv1.setText(bundle.getString("opname"));
            opnostr1 = bundle.getString("opno");

            //Lotnotv1.setText( "K61730139-01");

            new Thread(new Runnable() {

                @Override
                public void run() {
                    getRemoteInfo();
                    Message msg = handle.obtainMessage();
                    msg.obj = Sourcelist;
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
            String METHOD_NAME = "getlotinfo";
            String SOAP_ACTION = "http://tempuri.org/getlotinfo ";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("lotno", Lotnotv1.getText().toString());  //输入批号信息
            rpc.addProperty("opno", opnostr1);  //输入批号信息

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

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

            try

            {
                soapchild = (SoapObject) ((SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0));
            } catch (Exception ex) {
                ex.printStackTrace();
                sysErrorcode = "2";
                return;
            }
            String tempsoap1 = null;
            String tempsoap2 = null;
            String tempsoap3 = null;

            Sourcelist = new ArrayList<LotopdataSlist1>();
            //当站的投入
            LotopdataSlist1 s1;
            for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                if (i == 0) {
                    s1 = new LotopdataSlist1();
                    s1.Item1 = "Input";
                    s1.Item2 = ((SoapObject) soapchild.getProperty(0)).getProperty("INPUTQTY").toString();
                    s1.Item3 = "站位信息";
                    Sourcelist.add(s1);

                    s1 = new LotopdataSlist1();
                    s1.Item1 = "Good";
                    s1.Item2 = ((SoapObject) soapchild.getProperty(0)).getProperty("GOODQTY").toString();
                    s1.Item3 = "站位信息";
                    Sourcelist.add(s1);

                    s1 = new LotopdataSlist1();
                    s1.Item1 = "InDate";
                    s1.Item2 = ((SoapObject) soapchild.getProperty(0)).getProperty("CHECKINDATE").toString();
                    s1.Item3 = "站位信息";
                    Sourcelist.add(s1);

                    s1 = new LotopdataSlist1();
                    s1.Item1 = "InUser";
                    s1.Item2 = ((SoapObject) soapchild.getProperty(0)).getProperty("CHECKINUSERID").toString();
                    s1.Item3 = "站位信息";
                    Sourcelist.add(s1);

                    s1 = new LotopdataSlist1();
                    s1.Item1 = "OutDate";
                    s1.Item2 = ((SoapObject) soapchild.getProperty(0)).getProperty("CHECKOUTDATE").toString();
                    s1.Item3 = "站位信息";
                    Sourcelist.add(s1);

                    s1 = new LotopdataSlist1();
                    s1.Item1 = "OutUser";
                    s1.Item2 = ((SoapObject) soapchild.getProperty(0)).getProperty("CHECKOUTUSERID").toString();
                    s1.Item3 = "站位信息";
                    Sourcelist.add(s1);

                } else {
                    tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty(0).toString();
                    tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty(1).toString();
                    tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty(2).toString();

                    s1 = new LotopdataSlist1();

                    s1.Item1 = tempsoap1;
                    s1.Item2 = tempsoap2;
                    switch (tempsoap3) {
                        case "0":
                            s1.Item3 = "站位信息";
                            break;
                        case "1":
                            s1.Item3 = "不良信息";
                            break;
                        case "2":
                            s1.Item3 = "物料信息";
                            break;

                        default:
                            break;
                    }
                    Sourcelist.add(s1);
                }
            }
        }
        catch(Exception e1)
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
                    Toast.makeText(lottraceopdata.this, "获取资料异常", Toast.LENGTH_LONG).show();
                    return;
                }

                //=2为获取不到资料
                if (sysErrorcode != null && sysErrorcode.equals("2")) {
                    Toast.makeText(lottraceopdata.this, "無資料", Toast.LENGTH_LONG).show();
                    return;
                }

                View convertView;

                for (int i = 0; i < Sourcelist.size(); i++) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source4, ll1, false);
                    ((TextView) convertView.findViewById(R.id.ls4_item1name)).setText(Sourcelist.get(i).Item1.toString());
                    ((TextView) convertView.findViewById(R.id.ls4_item1value)).setText(Sourcelist.get(i).Item2.toString());
                    ((TextView) convertView.findViewById(R.id.ls4_item3name)).setText(Sourcelist.get(i).Item3.toString());

                    ll1.addView(convertView);
                }
                //testsoapet.setText(soapresult1);
            }
            catch(Exception e1)
            {}
        }
    };

    class LotopdataSlist1 {
        private String Item1;
        private String Item2;
        private String Item3;

        public void setItem1(String item1){ Item1=item1;}
        public String getItem1(){return Item1;}

        public void setItem2(String item2){Item2=item2;}
        public String getItem2(){return Item2;}

        public void setItem3(String item3){Item3=item3;}
        public String getItem3(){return Item3;}
    }

}
