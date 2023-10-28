package app.cmapp.Reserve;

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
import app.cmapp.Staticdata;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.execloadactivity;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by Tod on 2016/4/15.
 */
public class rsstrocktask extends AppCompatActivity {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String rssid;
    protected String keynotice;
    protected String area;
    protected String floor;
    protected String productid;
    protected String productname;
    protected String outqty;



    LinearLayout lly1;

    protected int testtod;
    private String sysErrorcode; // 1:获取资料异常

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_strocktask);

        final Staticdata app = (Staticdata)getApplication();
        Sessionuser=app.getLoginUserID();
        Toast.makeText(this,Sessionuser,Toast.LENGTH_SHORT).show();

        //rssid = bundle.getString("Rsid");
        lly1=(LinearLayout)findViewById(R.id.rs_strocktask_ll1);
        try {
            lly1.removeAllViews();

            execloadactivity.opendialog(rsstrocktask.this,"正在執行");

            // ProgressDialog.show(rsstrocklist.this, "", "正在執行，请稍等...", false);
            MyThreadPool.pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {

                            PublicSOAP ps1 = new PublicSOAP();
                            List<PublicSOAP.reservetask> pe1 = new ArrayList<>();
                            //pe1= ps1.getRemoteInfo_eqbasedata(eqsysidtv.getText().toString());
                            pe1 = ps1.getRemoteInfo_getreservetask(Sessionuser);

                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = pe1;
                            handle.sendMessage(msg);
                        }
                    }
            );
        }
        catch(Exception ex1)
        {}
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        try {
//            lly1.removeAllViews();
//
//            execloadactivity.opendialog(rsstrocktask.this,"正在執行");
//
//            // ProgressDialog.show(rsstrocklist.this, "", "正在執行，请稍等...", false);
//            MyThreadPool.pool.execute(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//
//                            PublicSOAP ps1 = new PublicSOAP();
//                            List<PublicSOAP.reservetask> pe1 = new ArrayList<PublicSOAP.reservetask>();
//                            //pe1= ps1.getRemoteInfo_eqbasedata(eqsysidtv.getText().toString());
//                            pe1 = ps1.getRemoteInfo_getreservetask(Sessionuser);
//
//                            Message msg = new Message();
//                            msg.what = 0;
//                            msg.obj = pe1;
//                            handle.sendMessage(msg);
//                        }
//                    }
//            );
//        }
//        catch(Exception ex1)
//        {}
//    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {

                    List<PublicSOAP.reservetask> pe1 = (List<PublicSOAP.reservetask>) msg.obj;

                    if (pe1 == null || "".equals(pe1)) {
                        execloadactivity.canclediglog();
                        Toast.makeText(rsstrocktask.this, "無資料", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    View convertView;
                    for (int i = 0; i < pe1.size(); i++) {
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source10, null);
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname1)).setText(pe1.get(i).getProductid().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname2)).setText(pe1.get(i).getProductnamecn().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname3)).setText(pe1.get(i).getRevrule().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname4)).setText(pe1.get(i).getDEMANDQTY().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname5)).setText(pe1.get(i).getLINENAME().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname6)).setText(pe1.get(i).getSTORESTATION().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname7)).setText(pe1.get(i).getDEMANDREASON().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname8)).setText(pe1.get(i).getDEMANDUSER().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname9)).setText(pe1.get(i).getDEMANDDATE().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname10)).setText(pe1.get(i).getKEYNAME().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname11)).setText(pe1.get(i).getSTOREAREA().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname12)).setText(pe1.get(i).getSTORELOCAL().toString());
                        ((TextView) convertView.findViewById(R.id.listviewsource10Mname13)).setText(pe1.get(i).getKEYNAME().toString());

                        convertView.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        area = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource10Mname11)).getText().toString();
                                        floor = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource10Mname12)).getText().toString();
                                        productid = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource10Mname1)).getText().toString();
                                        productname = ((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource10Mname2)).getText().toString();
                                        outqty=((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource10Mname4)).getText().toString();
                                        keynotice=((TextView) ((LinearLayout) v).findViewById(R.id.listviewsource10Mname10)).getText().toString();

                                        Intent intent1 = new Intent(rsstrocktask.this
                                                , rsstrockexeco.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("area", area);
                                        bundle1.putString("floor", floor);
                                        bundle1.putString("productid",productid);
                                        bundle1.putString("productname",productname);
                                        bundle1.putString("reasonno","9");
                                        bundle1.putString("outqty",outqty);
                                        bundle1.putString("keynotice",keynotice);

                                        intent1.putExtras(bundle1);
                                        startActivity(intent1);
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
                execloadactivity.canclediglog();
            }
        }
    };
}