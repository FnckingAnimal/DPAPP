package app.dpapp.Repair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.zxing.activity.CaptureActivity;

public class ShelfActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mLotno,mLocation;
    private List<shelfBean> mList;
    private MyAdapter mAdapter;
    private ListView mListView;
    private String mFlag,mUserId;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);
        Staticdata sc = (Staticdata) getApplication();
        mUserId = sc.getLoginUserID();
        mLotno = findViewById(R.id.tv_lotno);
        mLocation = findViewById(R.id.tv_location);
        mListView = findViewById(R.id.lv_shelf);
        mButton = findViewById(R.id.bt_commit);
        mList = new ArrayList<>();
        mAdapter = new MyAdapter(this,mList);
        mListView.setAdapter(mAdapter);
        mLotno.setOnClickListener(this);
        mLocation.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if("lotno".equals(mFlag)) {
                mLotno.setText(scanResult);
            } else if("location".equals(mFlag)) {
                mLocation.setText(scanResult);
                MyThreadPool.pool.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                String Status = submit(mLotno.getText().toString(),mLocation.getText().toString(),mUserId);
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = Status;
                                mHandler.sendMessage(msg);
                            }
                        }
                );
            }
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String status = (String) msg.obj;
                    if(!TextUtils.isEmpty(status) && status.contains("Success")) {
                        mList.clear();
                        shelfBean bean = new shelfBean();
                        bean.setLocation(mLocation.getText().toString());
                        bean.setLotno(mLotno.getText().toString());
                        bean.setTime(refFormatNowDate());
                        bean.setUserid(mUserId);
                        mList.add(bean);
                        mAdapter.notifyDataSetChanged();
                        mLotno.setText("");
                        mLocation.setText("");
                    }
                    break;
            }
        }
    };

    public String refFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmm");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lotno:
                mFlag = "lotno";
                Intent intent = new Intent(ShelfActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_location:
                if(TextUtils.isEmpty(mLotno.getText().toString())) {
                    Toast.makeText(ShelfActivity.this,"请先扫描批号!",Toast.LENGTH_SHORT).show();
                    return;
                }
                mFlag = "location";
                Intent intent1 = new Intent(ShelfActivity.this, CaptureActivity.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.bt_commit:
                if(TextUtils.isEmpty(mLocation.getText().toString())) {
                    Toast.makeText(ShelfActivity.this,"请扫描位置",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mLotno.getText().toString())) {
                    Toast.makeText(ShelfActivity.this,"请扫描批号",Toast.LENGTH_SHORT).show();
                    return;
                }
                MyThreadPool.pool.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                String Status = submit(mLotno.getText().toString(),mLocation.getText().toString(),mUserId);
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = Status;
                                mHandler.sendMessage(msg);
                            }
                        }
                );
                break;
        }
    }

    public  String submit(String lotno,String location,String userid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "shelfSubmit";
            String SOAP_ACTION = "http://tempuri.org/shelfSubmit";
            String URL= Staticdata.soapurl;

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("lotno", lotno);
            rpc.addProperty("location", location);
            rpc.addProperty("userid", userid);
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
            String returnstr;             // -1=獲取資料失敗
            try {
                returnstr = r1.getProperty(0).toString(); //1：已報修，2：已啟修，3：已完修，4：已驗收（正常）

            } catch (Exception ex1) {
                returnstr = "-1";
            }

            return returnstr;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<shelfBean> mList;

        private MyAdapter(Context mContext, List<shelfBean> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_shelf, null);
                holder.textView1 = convertView.findViewById(R.id.tv_lotno);
                holder.textView2 = convertView.findViewById(R.id.tv_location);
                holder.textView3 = convertView.findViewById(R.id.tv_userid);
                holder.textView4 = convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(mList.get(position).getLotno());
            holder.textView2.setText(mList.get(position).getLocation());
            holder.textView3.setText(mList.get(position).getUserid());
            holder.textView4.setText(mList.get(position).getTime());
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView1, textView2, textView3, textView4;
    }
    class shelfBean implements Serializable {
        private String lotno;
        private String location;
        private String userid;
        private String time;

        public String getLotno() {
            return lotno;
        }

        public void setLotno(String lotno) {
            this.lotno = lotno;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
