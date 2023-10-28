package app.cmapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import app.cmapp.Repair.eqrepairexec;
import app.dpapp.R;

//增加SOAP组建引入
//增加异步组建引入


public class oitemcj extends AppCompatActivity {

    protected String Eqid;
    protected String Eqname;
    protected TextView eqnametv1;
    String result;




    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            result = msg.obj.toString();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oitemc);
        eqnametv1=(TextView)findViewById(R.id.eqcodetextView);
        Bundle bundle = this.getIntent().getExtras();
        Eqid = bundle.getString("Eqid");
        eqnametv1.setText(Eqid);
    }

    public void linkeqcheck(View v) {

        Intent intent = new Intent(this, eqcheck.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("Eqid", eqnametv1.getText().toString());
        intent.putExtras(bundle1);
        startActivity(intent);
    }

    public void linkeqrepair(View v) {

        Intent intent = new Intent(this, eqrepairexec.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("Eqid", eqnametv1.getText().toString());
        intent.putExtras(bundle1);
        startActivity(intent);
    }

    public void linkeqdata(View v)
    {
        Intent intent = new Intent(this, eqbasedata.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("Eqid", eqnametv1.getText().toString());
        intent.putExtras(bundle1);
        startActivity(intent);
    }

    public void linkeqcheckrepory(View v)
    {
        Intent intent = new Intent(this, eqcheckreportlist.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("Eqid", eqnametv1.getText().toString());
        intent.putExtras(bundle1);
        startActivity(intent);
    }


    public void getRemoteInfo() {
        //Log.i("getRemoteInfo", "into getRemoteInfo");


        String NAMESPACE = "http://tempuri.org/";

        String URL=Staticdata.soapurl;

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
}