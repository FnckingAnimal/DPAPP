package app.dpapp.Repair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.cmapp.Staticdata;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.zxing.activity.CaptureActivity;
import app.dpapp.R;
import app.dpapp.bean.ScanMachineBean;
import app.dpapp.bean.ScanMachineTwo;
import app.dpapp.utils.AsynNetUtils;

/**
 * Created by S7202916 on 2018/11/29.
 */
public class ZaiBanActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mTvMachine_sao,mTvPihao_sao,mTvCode_sao,mTvUserid_sao;
    private EditText mTvMachine,mTvPihao,mTvCode,mTvUserid;
    private Spinner mDevicenoSpinner,mResaonSpinner,mWorktypeSpinner,mRunTypeSpinner;
    private Button mButton;
    private int flag;
    private ArrayAdapter<String> mAdapter,mWorkAdapter,mReasonAdapter,mRunAdapter;
    private List<String> list;
    private List<String> mReasonList;
    private String ip;
//    private String mUserId;
    private List<String> mDbnameList;
    private String mDbname;
    private EditText mTotalNumber,mErrorNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zaiban_layout);
//        Staticdata app = (Staticdata) getApplication();
//        mUserId = app.getLoginUserID();
        mTvUserid_sao = (TextView) findViewById(R.id.tv_zaiban_userid_sao);
        mTvMachine_sao = (TextView) findViewById(R.id.tv_zaiban_machine_sao);
        mTvPihao_sao = (TextView) findViewById(R.id.tv_zaiban_pihao_sao);
        mTvCode_sao = (TextView) findViewById(R.id.tv_zaiban_code_sao);
        mButton = (Button) findViewById(R.id.bt_zaiban_commit);
        mResaonSpinner = (Spinner) findViewById(R.id.sp_zaiban_reason);
        mDevicenoSpinner = (Spinner) findViewById(R.id.sp_zaiban_deviceno);
        mWorktypeSpinner = (Spinner) findViewById(R.id.sp_zaiban_worktype);
        mRunTypeSpinner = (Spinner) findViewById(R.id.sp_zaiban_runtype);
        mTotalNumber = (EditText) findViewById(R.id.et_zaiban_number);
        mErrorNumber = (EditText) findViewById(R.id.et_zaiban_number_error);
        mReasonList = new ArrayList<>();
        mDbnameList = new ArrayList<>();
        List<String> workList = new ArrayList<>();
        workList.add("取出");
        workList.add("放入");
        mWorkAdapter = new ArrayAdapter<>(this, R.layout.myspinner,workList);
        mWorktypeSpinner.setAdapter(mWorkAdapter);
        List<String> runList = new ArrayList<>();
        runList.add("拆板");
        runList.add("不拆板");
        mRunAdapter = new ArrayAdapter<>(this, R.layout.myspinner,runList);
        mRunTypeSpinner.setAdapter(mRunAdapter);
        mButton.setOnClickListener(this);
        mTvMachine_sao.setOnClickListener(this);
        mTvPihao_sao.setOnClickListener(this);
        mTvCode_sao.setOnClickListener(this);
        mTvUserid_sao.setOnClickListener(this);
        mTvMachine = (EditText) findViewById(R.id.tv_zaiban_machine);
        mTvPihao = (EditText) findViewById(R.id.tv_zaiban_pihao);
        mTvCode = (EditText) findViewById(R.id.tv_zaiban_code);
        mTvUserid = (EditText) findViewById(R.id.tv_zaiban_userid);
        list = new ArrayList<>();
        String url = "http://10.151.128.225:8095/SystemConfig?aname=TUVTU2VydmljZSwgVmVyc2lvbj0xLjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPW51bGw%3D&nsname=TUVTU2VydmljZS5CTEw%3D&classname=TUVTU2VydmljZS5CTEwuRGV2aWNlTGlzdA%3D%3D&i=1";
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                ScanMachineTwo bean = gson.fromJson(rspoonse, ScanMachineTwo.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
        ip = AsynNetUtils.getWifiMac(this);
        getReason();
        mDevicenoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDbname = mDbnameList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        ScanMachineTwo bean = (ScanMachineTwo) msg.obj;
                        int size1 = bean.getResultvalue().getLinkViewArray().get(0).getItem1().getRows().size();
                        for (int i = 0; i < size1; i++) {
                            String deviceno = bean.getResultvalue().getLinkViewArray().get(0).getItem1().getRows().get(i).get(2).getValue();
                            String dbname = bean.getResultvalue().getLinkViewArray().get(0).getItem1().getRows().get(i).get(5).getValue();
                            list.add(deviceno);
                            mDbnameList.add(dbname);
                        }
                        mAdapter = new ArrayAdapter<>(ZaiBanActivity.this, R.layout.myspinner, list);
                        mDevicenoSpinner.setAdapter(mAdapter);
                    }  catch (Exception e) {
                        Toast.makeText(ZaiBanActivity.this,"請求錯誤!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    try {
                        ScanMachineTwo reasonbean = (ScanMachineTwo) msg.obj;
                        int size2 = reasonbean.getResultvalue().getLinkViewArray().get(1).getItem1().getRows().size();
                        if(size2 > 0) {
                            for (int i = 0; i < size2; i++) {
                                String reason = reasonbean.getResultvalue().getLinkViewArray().get(1).getItem1().getRows().get(i).get(0).getValue();
                                mReasonList.add(reason);
                            }
                            mReasonAdapter = new ArrayAdapter<>(ZaiBanActivity.this,R.layout.myspinner,mReasonList);
                            mResaonSpinner.setAdapter(mReasonAdapter);
                        } else {
                            Toast.makeText(ZaiBanActivity.this,"原因為空!",Toast.LENGTH_SHORT).show();
                        }
                    }  catch (Exception e) {
                        Toast.makeText(ZaiBanActivity.this,"請求錯誤!",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };



    public void getReason()  {
    //      http://10.151.128.225:8095/MESSettingMain/Index?aname=Q01FU0xpYnJhcnksIFZlcnNpb249MS4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxs&nsname=Q01FU0xpYnJhcnkuU0ZDT25saW5l&classname=Q01FU0xpYnJhcnkuU0ZDT25saW5lLkZvcm1UYXNr&key=T181203001&dbname=BFMC&i=1
        String url = Staticdata.URL + "Index?aname=Q01FU0xpYnJhcnksIFZlcnNpb249MS4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxs&nsname=Q01FU0xpYnJhcnkuU0ZDT25saW5l&classname=Q01FU0xpYnJhcnkuU0ZDT25saW5lLkZvcm1UYXNr&key=T181203001&dbname=BFMC&i=1";
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                ScanMachineTwo bean = gson.fromJson(rspoonse, ScanMachineTwo.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
           case R.id.tv_zaiban_machine_sao:
               flag = 100;
               intent = new Intent(this,CaptureActivity.class);
               startActivityForResult(intent, 0);
               break;
           case R.id.tv_zaiban_pihao_sao:
               flag = 101;
               intent = new Intent(this,CaptureActivity.class);
               startActivityForResult(intent, 0);
               break;
           case R.id.tv_zaiban_code_sao:
               flag = 102;
               intent = new Intent(this,CaptureActivity.class);
               startActivityForResult(intent, 0);
               break;
            case R.id.tv_zaiban_userid_sao:
                flag = 103;
                intent = new Intent(this,CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
           case R.id.bt_zaiban_commit:
               if(TextUtils.isEmpty(mTvMachine.getText().toString())) {
                   Toast.makeText(this,"請輸入機台號!",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(TextUtils.isEmpty(mTvPihao.getText().toString())) {
                   Toast.makeText(this,"請輸入機批號!",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(TextUtils.isEmpty(mTvCode.getText().toString())) {
                   Toast.makeText(this,"請輸入載板編號!",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(TextUtils.isEmpty(mTvUserid.getText().toString())) {
                   Toast.makeText(this,"請輸入工號!",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(TextUtils.isEmpty(mTotalNumber.getText().toString())) {
                   Toast.makeText(this,"請輸入記錄數量!",Toast.LENGTH_SHORT).show();
                   return;
               }
               zaibanPost();
            break;
       }
    }
    public void zaibanPost() {
        execloadactivity.opendialog(this,"正在執行...");
        String str = "";
        if("取出".equals(mWorktypeSpinner.getSelectedItem().toString())) {
            str = "0";
        } else if("放入".equals(mWorktypeSpinner.getSelectedItem().toString())) {
            str = "1";
        }
        //= runtype
        String submitvalue="[";
        submitvalue += "{\"Key\":\"DEVICENO\",\"Value\":\"" + mDevicenoSpinner.getSelectedItem().toString()+ "\"},"+
                "{\"Key\":\"MACHINENO\",\"Value\":\"" + mTvMachine.getText().toString() + "\"}," +
                "{\"Key\":\"LOTNO\",\"Value\":\"" + mTvPihao.getText().toString() + "\"}," +
                "{\"Key\":\"CARRIERNO\",\"Value\":\"" + mTvCode.getText().toString() + "\"}," +
                "{\"Key\":\"REASON\",\"Value\":\"" + mResaonSpinner.getSelectedItem().toString() + "\"}," +
                "{\"Key\":\"IP\",\"Value\":\"" + ip + "\"}," +
                "{\"Key\":\"CREATOR\",\"Value\":\"" + mTvUserid.getText().toString() + "\"}," +
                "{\"Key\":\"WORKTYPE\",\"Value\":\"" + str + "\"}," +
                "{\"Key\":\"CARRIERQTY\",\"Value\":\"" + mTotalNumber.getText().toString() + "\"}," +
                "{\"Key\":\"NGQTY\",\"Value\":\"" + mErrorNumber.getText().toString() + "\"}," +
                "{\"Key\":\"RUNTYPE\",\"Value\":\"" + mRunTypeSpinner.getSelectedItem().toString() + "\"},";
        submitvalue=submitvalue.substring(0, submitvalue.length()-1);
        submitvalue+="]";
        String url = Staticdata.URL + "SubmitTask?key=&classname=CMESLibrary.MaterialContral.RecordTakeOutCarrier&dbname=" + mDbname + "&submitvalue=" + submitvalue;
        AsynNetUtils.post(submitvalue, url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                execloadactivity.canclediglog();
                Gson gson = new Gson();
                ScanMachineBean bean = gson.fromJson(rspoonse, ScanMachineBean.class);
                if ("1".equals(bean.getResultflag())) {
                    Toast.makeText(ZaiBanActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                } else if("0".equals(bean.getResultflag())) {
                    Toast.makeText(ZaiBanActivity.this,bean.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ZaiBanActivity.this, "提交失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            switch (flag) {
                case 100:
                    mTvMachine.setText(scanResult);
                    break;
                case 101:
                    mTvPihao.setText(scanResult);
                    break;
                case 102:
                    mTvCode.setText(scanResult);
                    break;
                case 103:
                    mTvUserid.setText(scanResult);
                    break;
            }
        }
    }
}
