package app.cmapp.machinecheck;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.cmapp.Staticdata;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.execloadactivity;
import app.dpapp.R;

/**
 * Created by S7187445 on 2018/7/12.
 */
public class ImageActivity extends AppCompatActivity {

    private WebView mWebView;
    private String formid;
    private FormBean mBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        mWebView = (WebView) findViewById(R.id.wb_layout);
        formid = getIntent().getStringExtra("formid");
        mBean = new FormBean();
        execloadactivity.opendialog(this,"正在执行...");
        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        FormBean bean = getRemoteInfo();
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        msg.obj = bean;
                        mHandler.sendMessage(msg);
                    }
                }
        );
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                mBean = (FormBean) msg.obj;
                if(mBean != null && !"".equals(mBean)) {
                    String str = mBean.getFILEPATH();
                    //http://10.185.16.115:8096/
                   //D:\WebApp\MobileAppAPI\Upload\img\20170411\F201704111553-001.png
                    String str2 = "http://10.185.16.115:8096/Upload"+str.split("Upload")[1];
                    mWebView.loadUrl(str2);
                    execloadactivity.canclediglog();
//                    mWebView.addJavascriptInterface(ImageActivity.this, "android");
                    WebSettings settings = mWebView.getSettings();
                    settings.getJavaScriptEnabled();
                    settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                    settings.setSupportZoom(true);
                    settings.setBuiltInZoomControls(true);
                }
            }
        }
    };

    public FormBean getRemoteInfo() {
        String NAMESPACE = "http://tempuri.org/";

        String URL= Staticdata.soapurl;

        String METHOD_NAME = "getbceqimgpath";// "GetCheckContent";
        String SOAP_ACTION = "http://tempuri.org/getbceqimgpath"; //"http://tempuri.org/GetCheckContent";

        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("formid", formid);

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

        //SoapObject soapchild=(SoapObject)r1.getProperty(0);
        SoapObject soapchild;
        try {
            soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        FormBean bean = new FormBean();
        // 获取返回的结果
        //soapresult1 = r1.getProperty(0).toString();
        bean.setFILEID(((SoapObject) soapchild.getProperty(0)).getProperty("FILEID").toString());
        bean.setFILENAME(((SoapObject) soapchild.getProperty(0)).getProperty("FILENAME").toString());
        bean.setFILEPATH(((SoapObject) soapchild.getProperty(0)).getProperty("FILEPATH").toString());
        bean.setSYSTEMNAME(((SoapObject) soapchild.getProperty(0)).getProperty("SYSTEMNAME").toString());
        bean.setFORMID(((SoapObject) soapchild.getProperty(0)).getProperty("FORMID").toString());
        bean.setISEFFECT(((SoapObject) soapchild.getProperty(0)).getProperty("ISEFFECT").toString());
        bean.setCREATETIME(((SoapObject) soapchild.getProperty(0)).getProperty("CREATETIME").toString());
        bean.setLASTUPDATETIME(((SoapObject) soapchild.getProperty(0)).getProperty("LASTUPDATETIME").toString());
        bean.setLASTUPDATEID(((SoapObject) soapchild.getProperty(0)).getProperty("LASTUPDATEID").toString());
        bean.setCREATEID(((SoapObject) soapchild.getProperty(0)).getProperty("CREATEID").toString());
        return bean;
    }
}
