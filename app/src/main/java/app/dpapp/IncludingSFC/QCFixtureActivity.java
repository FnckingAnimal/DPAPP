package app.dpapp.IncludingSFC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.gsonbean.FixtureBean;
import app.dpapp.gsonbean.FixtureData;
import app.dpapp.utils.AsynNetUtils;
import app.dpapp.zxing.activity.CaptureActivity;

public class QCFixtureActivity extends AppCompatActivity {
    private EditText mEditText;
    private List<FixtureData> mList;
    private MyAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture);
        mEditText = findViewById(R.id.et_fixture);
        mListView = findViewById(R.id.lv_fixture);
        mList = new ArrayList<>();
        mAdapter = new MyAdapter(this,mList);
        mListView.setAdapter(mAdapter);
        mEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(QCFixtureActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            mEditText.setText(scanResult);
            getFixtureData(scanResult);
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    FixtureBean bean = (FixtureBean) msg.obj;
                    FixtureData data = bean.getData().getData();
                    mList.add(data);
                    mAdapter.notifyDataSetChanged();
                    mListView.invalidate();
                    break;
            }
        }
    };

    public void getFixtureData(String formid) {
        //http://10.143.4.218/QC/Equipment/GetDeviceTool.ashx?method=GetMessage&Id=DEVT211213134129470
        String url = "http://10.143.4.218/QC/Equipment/GetDeviceTool.ashx?method=GetMessage&Id=" + formid;
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                FixtureBean bean = gson.fromJson(rspoonse, FixtureBean.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<FixtureData> mList;

        private MyAdapter(Context mContext, List<FixtureData> mList) {
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_fixture, null);
                holder.textView1 = convertView.findViewById(R.id.tv_area);
                holder.textView2 = convertView.findViewById(R.id.tv_dept);
                holder.textView3 = convertView.findViewById(R.id.tv_deviveno);
                holder.textView4 = convertView.findViewById(R.id.tv_name);
                holder.textView5 = convertView.findViewById(R.id.tv_vendor);
                holder.textView6 = convertView.findViewById(R.id.tv_number);
                holder.textView7 = convertView.findViewById(R.id.tv_material);
                holder.textView8 = convertView.findViewById(R.id.tv_userid);
                holder.textView9 = convertView.findViewById(R.id.tv_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(mList.get(position).getAREA());
            holder.textView2.setText(mList.get(position).getDEPT());
            holder.textView3.setText(mList.get(position).getDEV());
            holder.textView4.setText(mList.get(position).getNAME());
            holder.textView5.setText(mList.get(position).getVENDOR());
            holder.textView6.setText(mList.get(position).getNUMBER());
            holder.textView7.setText(mList.get(position).getMATERIAL());
            holder.textView8.setText(mList.get(position).getPERSON());
            holder.textView9.setText(mList.get(position).getSTATE());
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9;
    }
}
