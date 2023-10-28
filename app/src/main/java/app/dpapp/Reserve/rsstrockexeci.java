package app.dpapp.Reserve;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;

/**
 * Created by Tod on 2016/4/15.
 */
public class rsstrockexeci extends AppCompatActivity {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String Shift; //班別
    protected String area;//車間
    protected String floor; //樓層棟別，location
    protected String productid;// 部件編碼
    protected String productname;
    protected String inqty;// 入庫數量
    protected String ecsid;
    protected String intype;// 入庫類型

    protected Spinner shiftsp1;
    protected TextView areatv1;
    protected TextView floortv1;
    protected TextView productidtv1;
    protected TextView productnametv1;
    protected TextView inqtytv1;
    protected TextView ecsidtv1;


    private String sysErrorcode; // 1:获取资料异常

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_stockexecin);

        final Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();
        Bundle bundle = this.getIntent().getExtras();


        area = bundle.getString("area");
        floor = bundle.getString("floor");
        productid = bundle.getString("productid");
        productname=bundle.getString("productname");

        shiftsp1=(Spinner)findViewById(R.id.rs_strockexec_shiftsp);
        areatv1=(TextView)findViewById(R.id.rs_strockexec_areatv);
        floortv1=(TextView)findViewById(R.id.rs_strockexec_floortv);
        productidtv1=(TextView)findViewById(R.id.rs_strockexec_productidtv);
        productnametv1=(TextView)findViewById(R.id.rs_strockexec_productnametv);
        inqtytv1=(EditText)findViewById(R.id.rs_strocklist_execqtyet);
        ecsidtv1=(TextView)findViewById(R.id.rs_strocklist_exececsidet);

        areatv1.setText(area);
        floortv1.setText(floor);
        productidtv1.setText(productid);
        productnametv1.setText(productname);

        List<String> dl1=new ArrayList<>();
        dl1.add("A");
        dl1.add("B");

        ArrayAdapter<String> as1=new ArrayAdapter<String>(this, R.layout.spinner_textview, dl1);
        shiftsp1.setAdapter(as1);
/*
        new Thread(new Runnable() {

            @Override
            public void run() {
                PublicSOAP ps1=new PublicSOAP();

                List<PublicSOAP.reservedata> pe1=new ArrayList<PublicSOAP.reservedata>();
                //pe1= ps1.getRemoteInfo_eqbasedata(eqsysidtv.getText().toString());
                pe1=ps1.getRemoteInfo_getreservedata(rssid);

                Message msg = new Message();
                msg.what=0;
                msg.obj = pe1;
                handle.sendMessage(msg);
            }
        }).start();
*/
    }

    public void submitionclick(View v) {

        Shift=shiftsp1.getSelectedItem().toString();
        ecsid=ecsidtv1.getText().toString();
        inqty=inqtytv1.getText().toString();

        if(Shift==null)
        {
            Toast.makeText(rsstrockexeci.this, "班別不能為空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(inqty==null)
        {
            Toast.makeText(rsstrockexeci.this, "入庫數量不能為空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ecsid==null||ecsid.length()<=0)
        {
            intype="N";
        }
        else
        {
            intype="Y";
        }


        execloadactivity.opendialog(rsstrockexeci.this,"正在執行");
        new Thread(new Runnable() {

            @Override
            public void run() {
                PublicSOAP ps1=new PublicSOAP();
                String pe1= ps1.getRemoteInfo_reserveinstock(Shift,area,floor,productid,inqty,intype,ecsid,Sessionuser);
                //pe1=ps1.getRemoteInfo_getreservedata(rssid);
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
                        Toast.makeText(rsstrockexeci.this, "入庫成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(rsstrockexeci.this, pe1.toString(), Toast.LENGTH_SHORT).show();
                        //return;
                    }

                    execloadactivity.canclediglog();
                }
            }
            catch(Exception ex1)
            {
                Toast.makeText(rsstrockexeci.this, "未知異常", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }
        }
    };
}