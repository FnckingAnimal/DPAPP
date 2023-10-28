package app.cmapp.Reserve;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.cmapp.Staticdata;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.execloadactivity;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by Tod on 2016/4/15.
 */
public class rsstrockexeco extends AppCompatActivity {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String Shift; //班別
    protected String area;//車間
    protected String floor; //樓層棟別，location
    protected String line; //線體，location
    protected String productid;// 部件編碼
    protected String productname;
    protected String outqty;// 入庫數量
    protected String reason;
    protected String requireuser;

    protected String reasonno;//9：工程叫料
    protected String keynotice;//工程自主叫料單號

    protected Spinner shiftsp1;
    protected TextView areatv1;
    protected TextView floortv1;
    protected Spinner linesp1;
    protected TextView productidtv1;
    protected TextView productnametv1;
    protected Spinner reasonsp1;
    protected TextView outqtytv1;
    protected TextView requiretv1;

    LinearLayout lly1;

    private String sysErrorcode; // 1:获取资料异常

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_stockexecout);

        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

        Bundle bundle = this.getIntent().getExtras();

        area = bundle.getString("area");
        floor = bundle.getString("floor");
        productid = bundle.getString("productid");
        productname = bundle.getString("productname");

        reasonno = bundle.getString("reasonno");
        outqty = bundle.getString("outqty");
        keynotice = bundle.getString("keynotice");


        shiftsp1 = (Spinner) findViewById(R.id.rs_strockexeco_shiftsp);
        areatv1 = (TextView) findViewById(R.id.rs_strockexeco_areatv);
        floortv1 = (TextView) findViewById(R.id.rs_strockexeco_floortv);
        linesp1 = (Spinner) findViewById(R.id.rs_strockexeco_linesp);
        productidtv1 = (TextView) findViewById(R.id.rs_strockexeco_productidtv);
        productnametv1 = (TextView) findViewById(R.id.rs_strockexeco_productnametv);
        reasonsp1 = (Spinner) findViewById(R.id.rs_strockexeco_reasonsp);
        outqtytv1 = (EditText) findViewById(R.id.rs_strocklist_execoqtyet);
        requiretv1 = (EditText) findViewById(R.id.rs_strocklist_execorequseret);

        areatv1.setText(area);
        floortv1.setText(floor);
        productidtv1.setText(productid);
        productnametv1.setText(productname);
        outqtytv1.setText(outqty);

        List<String> dl1 = new ArrayList<>();
        dl1.add("A");
        dl1.add("B");

        ArrayAdapter<String> as1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dl1);
        shiftsp1.setAdapter(as1);

        final PublicSOAP ps1 = new PublicSOAP();
        //獲得樓層編號
        new Thread(new Runnable() {

            @Override
            public void run() {
                List<String> pe1 = new ArrayList<String>();
                pe1 = ps1.getRemoteInfo_resergetline(area, floor);
                //pe1=ps1.getRemoteInfo_getreservedata(rssid);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = pe1;
                handle1.sendMessage(msg);
            }
        }).start();

        if (reasonno.equals("9")) {
            List<String> dl3 = new ArrayList<>();
            dl3.add("工程自主叫料");


            ArrayAdapter<String> as2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dl3);
            reasonsp1.setAdapter(as2);
            reasonsp1.setEnabled(false);
        }
        else {
            //獲得原因編碼
            new Thread(new Runnable() {

                @Override
                public void run() {
                    List<PublicSOAP.reservereasioninout> pe1 = new ArrayList<PublicSOAP.reservereasioninout>();
                    pe1 = ps1.getRemoteInfo_getreserveinoutreasion();
                    //pe1=ps1.getRemoteInfo_getreservedata(rssid);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = pe1;
                    handle2.sendMessage(msg);
                }
            }).start();
        }

    }
    public void submitoonclick(View v) {

        Shift=shiftsp1.getSelectedItem().toString();
        line=linesp1.getSelectedItem().toString();
        reason=reasonsp1.getContext().toString();
        if(reasonno.equals("9")) {
            reason ="9";
        }
        else
        {
            reason = ((FinalStaticCloass.SpinnerData) reasonsp1.getSelectedItem()).getValue();
        }
        requireuser=requiretv1.getText().toString();
        outqty=outqtytv1.getText().toString();

        if(line==null)
        {
            Toast.makeText(rsstrockexeco.this, "Line不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(reason==null)
        {
            Toast.makeText(rsstrockexeco.this, "reason不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(requireuser==null)
        {
            Toast.makeText(rsstrockexeco.this, "requireuser不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(outqty==null)
        {
            Toast.makeText(rsstrockexeco.this, "outqty不能為空", Toast.LENGTH_SHORT).show();
            return;
        }

        execloadactivity.opendialog(rsstrockexeco.this,"正在執行");

        new Thread(new Runnable() {

            @Override
            public void run() {
                PublicSOAP ps1=new PublicSOAP();
                String pe1= ps1.getRemoteInfo_reserveoutstock(Shift,area,floor,line,productid,reason,requireuser,outqty,Sessionuser,keynotice);
                Message msg = new Message();
                msg.what=0;
                msg.obj = pe1;
                handle.sendMessage(msg);
            }
        }).start();

    }
    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            try {
                if (msg.what == 0) {
                    String pe1 = msg.obj.toString();
                    if (pe1.equals("true")) {
                        Toast.makeText(rsstrockexeco.this, "出庫成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(rsstrockexeco.this, pe1, Toast.LENGTH_SHORT).show();
                    }

                    execloadactivity.canclediglog();
                }
            }
            catch(Exception ex1)
            {
                execloadactivity.canclediglog();
            }
        }
    };

    private Handler handle1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            try {
                if (msg.what == 0) {
                    List<String> pe1 = new ArrayList<String>();
                    pe1 = (List<String>) msg.obj;
                    if (pe1 == null) {
                        return;
                    }
                    ArrayAdapter<String> as1 = new ArrayAdapter<String>(rsstrockexeco.this, R.layout.support_simple_spinner_dropdown_item, pe1);
                    linesp1.setAdapter(as1);

                    execloadactivity.canclediglog();
                }
            }
            catch(Exception ex1)
            {
                execloadactivity.canclediglog();
            }
        }
    };

    private Handler handle2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            try {
                if (msg.what == 0) {
                    List<PublicSOAP.reservereasioninout> pe1 = new ArrayList<PublicSOAP.reservereasioninout>();
                    pe1 = (List<PublicSOAP.reservereasioninout>) msg.obj;
                    if (pe1 == null) {
                        execloadactivity.canclediglog();
                        return;
                    }
                    List<FinalStaticCloass.SpinnerData> pe2 = new ArrayList<FinalStaticCloass.SpinnerData>();
                    for (int i = 0; i < pe1.size(); i++) {
                        FinalStaticCloass s = new FinalStaticCloass();
                        FinalStaticCloass.SpinnerData ss = s.new SpinnerData(pe1.get(i).getReasonno(), pe1.get(i).getReason());
                        pe2.add(
                                ss
                        );
                    }

                    ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                            new ArrayAdapter<FinalStaticCloass.SpinnerData>(rsstrockexeco.this, android.R.layout.simple_spinner_item, pe2);
                    as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    reasonsp1.setAdapter(as1);

                    execloadactivity.canclediglog();
                }
            }
            catch(Exception ex1)
            {
                Toast.makeText(rsstrockexeco.this, "未知異常", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }
        }
    };
}