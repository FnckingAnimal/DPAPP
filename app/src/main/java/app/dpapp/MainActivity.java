package app.dpapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//增加SOAP组建引入
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//增加异步组建引入
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {

    protected EditText et1;
    protected EditText testsoapet;
    protected Button bt1;
    protected Button testSoapbt;
    String result;


    private Button okButton;
    private SoapObject detail;

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Log.i("handle", "into handle");
            result = msg.obj.toString();
            Log.i("TAG", result);

            testsoapet.setText(result);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        et1 = (EditText) findViewById(R.id.editText);
        bt1 = (Button) findViewById(R.id.button);
        bt1.setOnClickListener(listener1);

        testsoapet = (EditText) findViewById(R.id.editText2);
        testSoapbt = (Button) findViewById(R.id.button2);
        testSoapbt.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {

                        Log.i("Thread", "outof Thread");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                Log.i("Thread", "into Thread");
                                // TODO Auto-generated method stub
                                // 脤戙忒儂瘍鎢ㄗ僇ㄘ陓洘
                                getRemoteInfo();
                                Message msg = handle.obtainMessage();
                                msg.obj = result;
                                handle.sendMessage(msg);
                            }
                        }).start();
                    }
                }
        );

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    Button.OnClickListener listener1 = new Button.OnClickListener() {
        public void onClick(View view) {
            String s1 = "Test";
            et1.setText(s1);
        }

    };

    public void linkeqcheck(View v) {
        startActivity(new Intent("eq.check"));
    }

    @SuppressWarnings("deprecation")
    public String getWeather(String cityName) {
        try {

            String NAMESPACE = "http://tempuri.org/";
            //WebService地址
            String URL = "http://10.142.136.222:8103/Service.asmx";
            String METHOD_NAME = "getUserInfo";
            String SOAP_ACTION = "http://tempuri.org/getUserInfo";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("userid", "F5460007");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String result = r1.getProperty(0).toString();


            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public void getRemoteInfo() {
        Log.i("getRemoteInfo", "into getRemoteInfo");


        String NAMESPACE = "http://tempuri.org/";

        String URL = "http://10.142.136.222:8103/Service.asmx";

        String METHOD_NAME = "getdate";
        String SOAP_ACTION = "http://tempuri.org/getdate";


        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("theCityCode", "2419");
        rpc.addProperty("theUserID", "");
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

        SoapObject r1 = (SoapObject) envelope.bodyIn;

        // 获取返回的结果
        result = r1.getProperty(0).toString();
    }


    public void scanclick(View v)
    {
        startActivity(new Intent("barcodescan"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("系统提示")
                    .setMessage("确定要退出系统吗").setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
        return false;
    }
}