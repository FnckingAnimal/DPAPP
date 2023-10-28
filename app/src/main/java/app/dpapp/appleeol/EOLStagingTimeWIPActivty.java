package app.dpapp.appleeol;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;
import app.dpapp.utils.DialogShowUtil;
import app.dpapp.zxing.activity.CaptureActivity;

public class EOLStagingTimeWIPActivty extends AppCompatActivity {

    private EditText et_lotno, et_liaojia, et_deviecno, et_productno;
    private Spinner sp_select;
    private String flag_select;
    private LinearLayout ll_lotnobondliaojia;
    private String messgestr;
    private String flag_messge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eolstagingtimewip_activty);
        initView();
        initData();
    }

    private void initData() {
        String[] selectdata = {"IN PUT", "OUT PUT"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, selectdata);
        sp_select.setAdapter(adapter);
    }

    private void initView() {
        et_lotno = findViewById(R.id.et_eolstagingtimewip_lotno);
        et_liaojia = findViewById(R.id.et_eolstagingtimewip_liaojia);
        sp_select = findViewById(R.id.sp_eolstagingtimewip_select);
        et_deviecno = findViewById(R.id.et_eolstagingtimewip_deviceno);
        et_productno = findViewById(R.id.et_eolstagingtimewip_productno);
        ll_lotnobondliaojia = findViewById(R.id.ll_eolstagingtimewip_lotnobondliaojia);
        sp_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectstr = sp_select.getItemAtPosition(position).toString();
                flag_select = selectstr;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onclickScanlotno(View view) {
        ll_lotnobondliaojia.removeAllViews();
        Intent intent=new Intent(EOLStagingTimeWIPActivty.this,CaptureActivity.class);
        startActivityForResult(intent,0);
//        String resultstr = "K1233E033-05";
//        et_lotno.setText(resultstr);
//        //根據批號獲取批號的wip信息
//        getLotnoWipInfo(resultstr);
//        //根據批號獲取綁定信息
//        ll_lotnobondliaojia.removeAllViews();
//        getLotnoBondLiaojiaInfo(resultstr);
    }

    public void onclickScanliaojia(View view) {
        Intent intent=new Intent(EOLStagingTimeWIPActivty.this,CaptureActivity.class);
        startActivityForResult(intent,1);
//        String resultstr = "A-1-1-1";
//        et_liaojia.setText(resultstr);
    }

    public void onclickOK(View view) {
        Staticdata app = (Staticdata) getApplication();
        String userid = app.getLoginUserID();
        String lotno = "";
        String liaojia = "";
        String deviecno = "";
        String productno = "";
        String state = "";
        switch (flag_select) {
            case "IN PUT":
                lotno = et_lotno.getText().toString().trim();
                liaojia = et_liaojia.getText().toString().trim();
                deviecno = et_deviecno.getText().toString().trim();
                productno = et_productno.getText().toString().trim();
                state = "IN";
                if (lotno == "" || lotno == null) {
                    Toast.makeText(EOLStagingTimeWIPActivty.this, "請掃描批號", Toast.LENGTH_LONG).show();
                    return;
                }
                if (liaojia == "" || liaojia == null) {
                    Toast.makeText(EOLStagingTimeWIPActivty.this, "請掃描料架", Toast.LENGTH_LONG).show();
                    return;
                }

                if (deviecno == "" || deviecno == null) {
                    return;
                }
                if (productno == "" || productno == null) {
                    Toast.makeText(EOLStagingTimeWIPActivty.this, "批號wip信息為空", Toast.LENGTH_LONG).show();
                    return;
                }
                String datastr = lotno + ":" + liaojia + ":" + deviecno + ":" + productno + ":" + state + ":" + userid;
                savalotnobondliaojia(datastr);
                break;
            case "OUT PUT":
                lotno = et_lotno.getText().toString().trim();
                //首先要check批號是否IN
                updatelotnobondliaojia(lotno, userid);


                break;
            default:
                break;
        }
    }

    private void updatelotnobondliaojia(String lotno, String userid) {
        final String methodname = "updatelotnobondliaojia";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("lotno", lotno));
        parameterList.add(new SOAPParameter("userid", userid));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getStringBySOAP(methodname, parameterList);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private void savalotnobondliaojia(String datastr) {
        final String methodname = "savalotnobondliaojia";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("datastr", datastr));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getStringBySOAP(methodname, parameterList);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String resultstr = bundle.getString("result");
            if (requestCode == 0) {
                et_lotno.setText(resultstr);
                //根據母批批號獲取批號的wip信息
                String[] lotstrs=resultstr.split("-");
                String monlotno=lotstrs[0];
                getLotnoWipInfo(monlotno);
                //根據批號獲取綁定信息
                getLotnoBondLiaojiaInfo(resultstr);
            }
            if (requestCode == 1) {
                et_liaojia.setText(resultstr);
            }
        }
    }

    private void getLotnoBondLiaojiaInfo(String lotno) {
        final String methodname = "getLotnoBondLiaojiaInfo";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("lotno", lotno));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String> result = asop.getLotnoBondLiaojiaInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String[] result0 = (String[]) msg.obj;
                    if (result0 == null) {
                        //Toast.makeText(EOLStagingTimeWIPActivty.this,"獲取批號信息出錯",Toast.LENGTH_LONG).show();
                        messgestr = "獲取批號信息出錯";
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        return;
                    }
                    if(result0.equals("")){
                        messgestr = "没有该批號的信息";
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        return;
                    }
                    et_deviecno.setText(result0[1]);
                    et_productno.setText(result0[2]);
                    break;
                case 1:
                    List<String> results1 = (List<String>) msg.obj;
                    if (results1 == null) {
                        //Toast.makeText(EOLStagingTimeWIPActivty.this,"獲取批號綁定料架信息出錯",Toast.LENGTH_LONG).show();
//                        messgestr="獲取批號綁定料架信息出錯";
//                        flag_messge="0";
//                        dialogShow(messgestr,flag_messge);
                        return;
                    }
                    View view = LayoutInflater.from(EOLStagingTimeWIPActivty.this).inflate(R.layout.listview_eolstagingtimewip_item1, null);
                    TextView listview_tv_lotno = (TextView) view.findViewById(R.id.tv_listview_eolstagingtimewio_item1_lotno);
                    TextView listview_tv_liaojia = (TextView) view.findViewById(R.id.tv_listview_eolstagingtimewio_item1_liaojia);
                    TextView listview_tv_deviceno = (TextView) view.findViewById(R.id.tv_listview_eolstagingtimewio_item1_deviceno);
                    TextView listview_tv_productno = (TextView) view.findViewById(R.id.tv_listview_eolstagingtimewio_item1_productno);
                    TextView listview_tv_creator = (TextView) view.findViewById(R.id.tv_listview_eolstagingtimewio_item1_creator);
                    TextView listview_tv_state = (TextView) view.findViewById(R.id.tv_listview_eolstagingtimewio_item1_state);
                    listview_tv_lotno.setText(results1.get(0));
                    listview_tv_liaojia.setText(results1.get(1));
                    listview_tv_deviceno.setText(results1.get(2));
                    listview_tv_productno.setText(results1.get(3));
                    listview_tv_creator.setText(results1.get(4));
                    listview_tv_state.setText(results1.get(5));
                    ll_lotnobondliaojia.addView(view);

                    break;
                case 2:
                    String result2 = (String) msg.obj;
                    if(result2==null){
                        messgestr = "SOAP调用失敗";
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        return;
                    }
                    if (result2.equals("")) {
                        //Toast.makeText(EOLStagingTimeWIPActivty.this,"IN PUT失敗",Toast.LENGTH_LONG).show();
                        messgestr = "IN PUT失敗";
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        return;
                    } else {
                        String[] result2s = result2.split(":");
                        messgestr = result2;
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        if(result2s[0].equals("1")){
                            clearData();
                        }
                    }
                    break;

                case 3:
                    String result3 = (String) msg.obj;
                    if (result3 == "") {
                        //Toast.makeText(EOLStagingTimeWIPActivty.this,"OUT PUT失敗",Toast.LENGTH_LONG).show();
                        messgestr = "OUT PUT失敗";
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        return;
                    } else {
                        String[] result3s = result3.split(":");
                        //Toast.makeText(EOLStagingTimeWIPActivty.this,"OUT PUT失敗:"+result3,Toast.LENGTH_LONG).show();
                        messgestr = result3;
                        DialogShowUtil.dialogShow(EOLStagingTimeWIPActivty.this,messgestr);
                        if(result3s[0].equals("1")){
                            clearData();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void clearData() {
        et_lotno.setText("");
        et_liaojia.setText("");
        et_deviecno.setText("");
        et_productno.setText("");
        ll_lotnobondliaojia.removeAllViews();
    }

    private void getLotnoWipInfo(String lotno) {
        final List<SOAPParameter> parameterList = new ArrayList<>();
        final String methodname = "getLotnoWipInfo";
        parameterList.add(new SOAPParameter("lotno", lotno));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String[] result = asop.getLotnoWipInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }
}