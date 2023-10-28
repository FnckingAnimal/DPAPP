package app.dpapp;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.appcdl.Exectempfile;
import app.dpapp.machinecheck.eqcheckformsave;

/**
 * Created by TPP on 2017/11/3.
 *  今天點檢保存的所有表單
 */
public class MoreForms extends AppCompatActivity implements View.OnClickListener{
    private ListView mListView;
    private List<String> mList;
    private MyAdapter mAdapter;
    private Button mBtClear;
    private String mFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moreforms_layout);
        mBtClear = (Button) findViewById(R.id.bt_check_clear);
        mBtClear.setOnClickListener(this);
        mFilePath = "/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/";
        mList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.lv_checkform);
        mAdapter = new MyAdapter(this,mList);
        getfilenew();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str = mList.get(position).split("_");
                Intent intent = new Intent(MoreForms.this, eqcheckformsave.class);
                intent.putExtra("flag", "666");
                intent.putExtra("machinenoid", str[0]);
                intent.putExtra("tablename", str[1].split(".tx")[0].split("A")[0]+"A");
                intent.putExtra("deviceno", "ALL");
                intent.putExtra("modletype", "MP");

//                intent.putExtra("number", str[1].split("tx")[0].substring(str[1].split("tx")[0].length() - 2, str[1].split("tx")[0].length() - 1));
                intent.putExtra("number", str[1].split(".tx")[0].split("A")[1]);
//                startActivity(intent);
                intent.putExtra("number2",position+"");
                startActivityForResult(intent, 0);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreForms.this);
                builder.setTitle("選擇").setMessage("確定要刪除?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Exectempfile.instance().DeleteFolder(mFilePath + mList.get(position));
                        removeItem(position);
                        Toast.makeText(MoreForms.this, "SUCCESS!", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && "MoreForms".equals(data.getStringExtra("flag"))) {
            String number = data.getStringExtra("number");
            removeItem(Integer.parseInt(number));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void removeItem(int position) {
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
    }
//    public void getSaveForms() {
//        List<String> list = new ArrayList<>();
//        list = getfile(mMachineId + "_" + mTablename.split("txt")[0].substring(0,mTablename.split("txt")[0].length()-1)+"0");
//        if(list != null) {
//            getfilenew();
//        } else {
//            Toast.makeText(this, "緩存表單為空！", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public List<String> getfile(String filename)
//    {
//        BufferedReader reader = null;
//        List<String> data=new ArrayList<>();
//        File file;
//        FileInputStream inStream;
//        String line;
//        try {
//            file = new File("/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/"+filename+".txt");
//            if(!file.exists())
//            {
//                return null;
//            }
//            inStream = new FileInputStream(file);
//            reader = new BufferedReader(new InputStreamReader(inStream));
//            while ((line = reader.readLine()) != null) {
//                data.add(line);
//            }
//            reader.close();
//            inStream.close();
//
//            return data;
//        } catch (Exception ex) {
//            return null;
//        } finally {
//            reader = null;
//            file = null;
//            inStream = null;
//            line = null;
//        }
//    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    mListView.invalidate();
                    Toast.makeText(MoreForms.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void getfilenew()
    {
        String filePATH= "/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/";
        File file;
        try {
            file = new File(filePATH);
            if(!file.exists())
            {
            }
            String[] strFile = file.list();
            if(strFile.length > 0) {
                for (int i = 0; i < strFile.length; i++) {
                    mList.add(strFile[i]);
                }
                mAdapter = new MyAdapter(MoreForms.this,mList);
                mListView.setAdapter(mAdapter);
            } else {
                Toast.makeText(this, "緩存表單為空！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {

        }
        finally {
            filePATH=null;
            file=null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_check_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    class  MyAdapter extends BaseAdapter {
        private Context context;
        private List<String> mList;

        public MyAdapter(Context context,List<String> mList) {
            this.context = context;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.moreform_item_layout,null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_moreform);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(mList.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView mTextView;
    }
}
