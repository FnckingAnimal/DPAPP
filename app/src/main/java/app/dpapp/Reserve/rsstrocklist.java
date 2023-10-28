package app.dpapp.Reserve;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;
/**
 * Created by Tod on 2016/4/15.
 */
public class rsstrocklist extends AppCompatActivity {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String rssid;
    protected String site;
    protected String area;
    protected String floor;
    protected String productid;
    protected String productname;
    protected String rssname;



    LinearLayout lly1;

    protected int testtod;
    private String sysErrorcode; // 1:获取资料异常

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_strocklist);


        final Staticdata app = (Staticdata)getApplication();
        Sessionuser=app.getLoginUserID();

        Bundle bundle = this.getIntent().getExtras();
        rssid = bundle.getString("Rsid");
        lly1=(LinearLayout)findViewById(R.id.rs_strocklist_ll1);


        //dialog.setCancelable(true);
        //dialog.setOnCancelListener();
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

    /**
     * tod test code .test treade function ,2016/618   including handle1,testtod2,and testtod
     */
    protected  void testtod2()
    {

        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {

                        testtod=testtod+1;
                        try {
                            Thread.sleep(10000);
                        }
                        catch(Exception e1)
                        {
                            Toast.makeText(rsstrocklist.this,testtod,Toast.LENGTH_LONG).show();
                        }
                        Message msg = new Message();
                        msg.what=0;
                        msg.obj = testtod;
                        handle1.sendMessage(msg);
                    }
                }
        );
    }
    @Override
    protected void onResume() {
        super.onResume();

        lly1.removeAllViews();

        execloadactivity.opendialog(rsstrocklist.this,"正在執行");

           // ProgressDialog.show(rsstrocklist.this, "", "正在執行，请稍等...", false);
            MyThreadPool.pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {

                            PublicSOAP ps1 = new PublicSOAP();
                            List<PublicSOAP.reservedata> pe1 = new ArrayList<PublicSOAP.reservedata>();
                            //pe1= ps1.getRemoteInfo_eqbasedata(eqsysidtv.getText().toString());
                            pe1 = ps1.getRemoteInfo_getreservedata(rssid.trim());

                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = pe1;
                            handle.sendMessage(msg);
                        }
                    }
            );

    }

    /**
     * tod test code .test treade function ,2016/618   including handle1,testtod2,and testtod
     */
    private Handler handle1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==0)
            {

                Toast.makeText(rsstrocklist.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                testtod2();
            }
        }
    };

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            try {
                if (msg.what == 0) {

                    List<PublicSOAP.reservedata> pe1 = new ArrayList<PublicSOAP.reservedata>();
                    pe1 = (List<PublicSOAP.reservedata>) msg.obj;

                    if (pe1 == null) {
                        execloadactivity.canclediglog();
                        Toast.makeText(rsstrocklist.this, "無資料", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    View convertView;
                    for (int i = 0; i < pe1.size(); i++) {
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source6, null);
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname1)).setText(pe1.get(i).getProductid().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname2)).setText(pe1.get(i).getProductnamecn().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname3)).setText(pe1.get(i).getProductno().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname4)).setText(pe1.get(i).getRevrule().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname5)).setText(pe1.get(i).getMachineno().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname6)).setText(pe1.get(i).getRopno().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname7)).setText(pe1.get(i).getStoreqty().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname8)).setText(pe1.get(i).getSafeqty().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname9)).setText(pe1.get(i).getSuppliermanu().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname10)).setText(pe1.get(i).getSite().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname11)).setText(pe1.get(i).getArea().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource6Mname12)).setText(pe1.get(i).getFloor().toString());


                        convertView.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        area = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource6Mname11)).getText().toString();
                                        floor = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource6Mname12)).getText().toString();
                                        productid = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource6Mname1)).getText().toString();
                                        productname = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource6Mname2)).getText().toString();
                                        AlertDialog isExit = new AlertDialog.Builder(rsstrocklist.this).create();
                                        // 设置对话框标题
                                        isExit.setTitle("出入庫提示");
                                        // 设置对话框消息
                                        isExit.setMessage("請選擇出庫或者入庫");
                                        // 添加选择按钮并注册监听
                                        isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "出庫", listener);

                                        isExit.setButton(DialogInterface.BUTTON_POSITIVE, "入庫", listener);


                                        // 显示对话框
                                        isExit.show();
                                    }
                                }
                        );


                        lly1.addView(convertView);
                    }
                    execloadactivity.canclediglog();
                    //dialog.cancel();
                    //dialog.dismiss();
                }
            }
            catch(Exception ex1)
            {
                Toast.makeText(rsstrocklist.this, "未知異常", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }
        }
    };

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE: //入庫
                    Intent intent = new Intent(rsstrocklist.this
                            , rsstrockexeci.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("area", area);
                    bundle.putString("floor", floor);
                    bundle.putString("productid",productid);
                    bundle.putString("productname",productname);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    break;
                case AlertDialog.BUTTON_NEGATIVE:  //出庫
                    Intent intent1 = new Intent(rsstrocklist.this
                            , rsstrockexeco.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("area", area);
                    bundle1.putString("floor", floor);
                    bundle1.putString("productid",productid);
                    bundle1.putString("productname",productname);
                    bundle1.putString("reasonno", "");
                    bundle1.putString("outqty","0");
                    bundle1.putString("keynotice","");

                    intent1.putExtras(bundle1);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }
    };
}