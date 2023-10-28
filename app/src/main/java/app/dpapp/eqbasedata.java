package app.dpapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import app.dpapp.zxing.activity.CaptureActivity;

//增加SOAP组建引入
//增加异步组建引入


// TODO: 2016/5/7 by tod 设备基础信息设定页面，捆绑设备ID和财产编号页面

public class eqbasedata extends AppCompatActivity {
   // public class eqbasedata extends ActionBarActivity{
    // TODO: 2016/5/7 设备基础信息设定  by tod
    protected String Eqid;
    protected TextView eqnametv1;
    protected TextView eqtypetv1;
    protected TextView eqassidtv1;  //资产编号
    protected TextView eqshoptv1;
    protected TextView eqlinetv1;
    protected TextView eqiptv1;
    protected Button eqlinkassbt1;

    String result;  //回归主线程返回结果
    String scanResult; //扫描结果
    eqbasedata_sdata es1;

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (result)
            {
                case "1":
                    Toast.makeText(eqbasedata.this, "捆绑成功", Toast.LENGTH_LONG).show();
                    eqassidtv1.setText(scanResult);
                    //eqlinkassbt1.setEnabled(true);
                    break;
                case "2":
                    Toast.makeText(eqbasedata.this, "此设备ID在系统不存在,请至系统设定", Toast.LENGTH_LONG).show();
                    //eqassidtv1.setText("");
                    break;
                case "3":
                    Toast.makeText(eqbasedata.this, "此设备已捆绑其它编号", Toast.LENGTH_LONG).show();
                    //eqassidtv1.setText("");
                    break;
                case "4":
                    Toast.makeText(eqbasedata.this, "此设备已捆绑", Toast.LENGTH_LONG).show();
                    //eqassidtv1.setText("");
                    break;
                default:
                    Toast.makeText(eqbasedata.this, "未知异常", Toast.LENGTH_LONG).show();
                    //eqassidtv1.setText("");
                    break;
            }

        }
    };
    private Handler handle1 = new Handler() {
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        super.handleMessage(msg);
        eqnametv1.setText(es1.eqname);
        eqtypetv1.setText(es1.eqtype);
        eqassidtv1.setText(es1.eqassid);
        eqshoptv1.setText(es1.eqshop);
        eqlinetv1.setText(es1.eqline);
        eqiptv1.setText(es1.eqip);
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_basedata);

        eqnametv1=(TextView)findViewById(R.id.eqbasedata_machineno_text1);
        eqtypetv1=(TextView)findViewById(R.id.eqbasedata_machinetyp_text1);
        eqassidtv1=(TextView)findViewById(R.id.eqbasedata_assid_text1);
        eqshoptv1=(TextView)findViewById(R.id.eqbasedata_shop_text1);
        eqlinetv1=(TextView)findViewById(R.id.eqbasedata_line_text1);
        eqiptv1=(TextView)findViewById(R.id.eqbasedata_ip_text1);
        eqlinkassbt1=(Button)findViewById(R.id.eqbasedata_createlink_bt1);

        Bundle bundle = this.getIntent().getExtras();
        Eqid = bundle.getString("Eqid");

        new Thread(new Runnable() {

            @Override
            public void run() {
                getRemoteInfo();
                Message msg = handle1.obtainMessage();
                msg.obj = es1;
                handle1.sendMessage(msg);
            }
        }).start();

    }
    public void getRemoteInfo() {

        String NAMESPACE = "http://tempuri.org/";

        String URL=Staticdata.soapurl;
        //String URL = "http://10.142.136.222:8107/Service.asmx";

        String METHOD_NAME = "geteqalldata";
        String SOAP_ACTION = "http://tempuri.org/geteqalldata";


        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("eqid", Eqid);
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
        SoapObject soapchild=(SoapObject)((SoapObject)((SoapObject)r1.getProperty(0)).getProperty(1)).getProperty(0);

        // 获取返回的结果
        //soapresult1 = r1.getProperty(0).toString();
        es1=new eqbasedata_sdata();
        es1.eqname=((SoapObject)soapchild.getProperty(0)).getProperty("MACHINENO").toString();
        es1.eqtype=((SoapObject)soapchild.getProperty(0)).getProperty("MACHINE").toString();
        es1.eqassid=((SoapObject)soapchild.getProperty(0)).getProperty("ASSET").toString();
        es1.eqshop=((SoapObject)soapchild.getProperty(0)).getProperty("SHOPNO").toString();
        es1.eqline=((SoapObject)soapchild.getProperty(0)).getProperty("LINE").toString();
        es1.eqip=((SoapObject)soapchild.getProperty(0)).getProperty("IP").toString();

    }

    public void getRemoteInfo_linkeqass() {

        String NAMESPACE = "http://tempuri.org/";

        String URL=Staticdata.soapurl;

        String METHOD_NAME = "seteqidbindingassid";
        String SOAP_ACTION = "http://tempuri.org/seteqidbindingassid";


        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("eqid", Eqid);
        rpc.addProperty("assid", scanResult);
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

    public void createnewlinkdata(View v)
    {
        // TODO: 2016/5/7 打开摄像头读取新的Assid
        scanResult=null;
        Intent openCameraIntent = new Intent(eqbasedata.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            scanResult = bundle.getString("result");
            if(scanResult==null)
            {
            }
            else {
                //尝试捆绑
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        getRemoteInfo_linkeqass();
                        Message msg = handle.obtainMessage();
                        msg.obj = result;
                        handle.sendMessage(msg);
                    }
                }).start();
            }
        }
    }

class eqbasedata_sdata {
    private String eqname;

    public void setEqname(String eqname1) {
        eqname = eqname1;
    }

    public String getEqname() {
        return eqname;
    }

    private String eqtype;

    public void setEqtype(String eqtype1) {
        eqtype = eqtype1;
    }

    public String getEqtype() {
        return eqtype;
    }

    private String eqassid;

    public void setEqassid(String eqassid1) {
        eqassid = eqassid1;
    }

    public String getEqassid() {
        return eqassid;
    }

    private String eqshop;

    public void setEqshop(String eqshop1) {
        eqshop = eqshop1;
    }

    public String getEqshop() {
        return eqshop;
    }

    private String eqline;

    public void setEqline(String eqline1) {
        eqline = eqline1;
    }

    public String getEqline() {
        return eqline;
    }

    private String eqip;

    public void setEqip(String eqip1) {
        eqip = eqip1;
    }

    public String getEqip() {
        return eqip;
    }
}

}



