package app.cmapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import app.dpapp.R;

import app.cmapp.appcdl.Exectempfile;
import app.cmapp.appcdl.execloadactivity;

/**
 * Created by Administrator on 2017/10/14.
 */
public class CheckActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView mListView;
    private SaveDdapter mSaveAdapter;
    private List<String> mList;
    private String mFilePath;
    private Button mBtClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_layout);
        mListView = (ListView) findViewById(R.id.lv_offline);
        mList = new ArrayList<>();
        mBtClear = (Button) findViewById(R.id.bt_check_clear);
        mSaveAdapter = new SaveDdapter(mList,this);
        mListView.setAdapter(mSaveAdapter);
        execloadactivity.opendialog(this, "正在執行");
        loadtempdata();
        mFilePath = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
        mBtClear.setOnClickListener(this);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this);
                    builder.setTitle("選擇").setMessage("確定要刪除?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Exectempfile.instance().deleteDirectory(mFilePath + mList.get(position).split("_")[0]);
                            removeItem(position);
                            Toast.makeText(CheckActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();

                        }
                    });
                builder.create().show();
                return true;
            }
        });


        mListView.setOnItemClickListener(
                 new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] machineId = mList.get(position).split("_");
                Intent intent = new Intent(CheckActivity.this, MoreForms.class);
                intent.putExtra("machinenoid", machineId[0]);
                intent.putExtra("tablename", machineId[1]);
                startActivity(intent);
            }
        });
    }


    public void removeItem(int position) {
        mList.remove(position);
        mSaveAdapter.notifyDataSetChanged();
        mListView.invalidate();
    }

    /**
     * 啟動載入暫存資料
     */
    public void loadtempdata()
    {
        try {
            String fileurl = "";
            String[] strArray = Exectempfile.instance().getfilenew(fileurl);
            List<String> list = new ArrayList<>();
            if(strArray != null & !"".equals(strArray)) {
                for (int i = 0; i <strArray.length ; i++) {
                   list.add(strArray[i]);
                }
            } else {
                Toast.makeText(this,"緩存資料為空！",Toast.LENGTH_SHORT).show();
            }
            String str;
            String[] strArray2;
            List<String> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                str = list.get(i) + "/";
//
                list1.add(str);
            }
            List<String> list2 = new ArrayList<>();

            for (String s:list1
                 ) {
                if(!s.contains("txt")) {
                    list2.add(s);
                }
            }
            List<String> list3 = new ArrayList<>();
            for (int i = 0; i < list2.size(); i++) {
                strArray2 = Exectempfile.instance().getfilenew(list2.get(i));
                list3.add(strArray2[0]);
            }
            Message msg = handle222.obtainMessage();
            msg.what = 0;


            msg.obj = list3;
            handle222.sendMessage(msg);
        } catch(Exception ex) {
            throw ex;
        }
    }

    private Handler handle222 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    execloadactivity.canclediglog();
                    mList = (List<String>) msg.obj;
                    if(mList.size() == 0){
//                        Toast.makeText(CheckActivity.this, "緩存資料為空", Toast.LENGTH_SHORT).show();
                    } else {
                        mSaveAdapter = new SaveDdapter(mList,CheckActivity.this);
                        mListView.setAdapter(mSaveAdapter);
                    }
                    break;
            }
        }

    };

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mList.clear();
                    mSaveAdapter.notifyDataSetChanged();
                    mListView.invalidate();
                    Toast.makeText(CheckActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_check_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this);
                builder.setTitle("選擇").setMessage("確定要清理緩存?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Exectempfile.instance().deleteDirectory(mFilePath);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }
                });
                builder.create().show();

                break;
        }

    }

    class SaveDdapter extends BaseAdapter {
        private List<String> mList;
        private Context context;

        public SaveDdapter(List<String> mList,Context context) {
            this.mList = mList;
            this.context = context;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.save_layout,null);
                holder = new ViewHolder();
                holder.mTvMachine = (TextView) convertView.findViewById(R.id.tv_save_machine2);
                holder.mTvForm = (TextView) convertView.findViewById(R.id.tv_save_form2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String[] machineId = mList.get(position).split("_");
            holder.mTvMachine.setText(machineId[0]);
            holder.mTvForm.setText(machineId[1]);

            return convertView;
        }
    }

    class ViewHolder {
        TextView mTvMachine,mTvForm;
    }
}