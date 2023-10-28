package app.dpapp.machinecheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.eqchecklist;

/**
 * Created by S7202916 on 2018/5/31.
 */
public class machine_MP extends AppCompatActivity {
    private List<MPBean> mList;
    private ListView mListView;
    private CheckAdapter mAdapter;
    //用户名
    private static String Sessionuser;
    private LinearLayout mLayout;
//    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_layout);
        Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();
        mList = new ArrayList();
        mListView = (ListView) findViewById(R.id.lv_mp);
//        mTextView = (TextView) findViewById(R.id.tv_mp_middle);
        mLayout = (LinearLayout) findViewById(R.id.ly_machine);
        mAdapter = new CheckAdapter(mList,this);
        loadingwaitingdata();
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(machine_MP.this);
                builder.setTitle("修改表单状态").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                                checkData(position);
                        dialog.dismiss();
                        View popupView = machine_MP.this.getLayoutInflater().inflate(R.layout.popupwindow, null);
                        Button button = (Button) popupView.findViewById(R.id.bt_popup);
                        final EditText edittext = (EditText) popupView.findViewById(R.id.et_popup);
                        final PopupWindow window = new PopupWindow(popupView, 500, 200);
                        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                        window.setFocusable(true);
                        window.setOutsideTouchable(true);
                        window.showAsDropDown(mLayout, 20, 20);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(edittext.getText() != null && !"".equals(edittext.getText())) {
                                    window.dismiss();
                                    execloadactivity.opendialog(machine_MP.this, "正在執行");
                                    checkData(position, edittext.getText().toString());
                                } else {
                                    Toast.makeText(machine_MP.this,"请输入备注",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(machine_MP.this, eqchecklist.class);
                Bundle bundle = new Bundle();
                bundle.putString("Sessionuser", Sessionuser);
                bundle.putString("Eqid", mList.get(position).getMACHINESYSID());
                bundle.putString("Eqname", mList.get(position).getMACHINENO());
                bundle.putString("checkdataid", mList.get(position).getCHECKDATAID());
                bundle.putString("Eqopname", mList.get(position).getOPNAME());
                bundle.putString("Eqlinename", mList.get(position).getLINENAME());
                bundle.putString("FILEVERSION", mList.get(position).getFILEVERSION());
                bundle.putString("flag","MP");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void checkData(final int position,final String description) {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                String str = ps1.updatetablestate(mList.get(position).getCHECKDATAID(), Sessionuser, description);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });

    }

    public void loadingwaitingdata(){
        execloadactivity.opendialog(this, "正在執行");
        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        PublicSOAP ps1 = new PublicSOAP();
                        mList = ps1.GetNewCheckStructhead_O();
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = mList;
                        handler.sendMessage(msg);
                    }
                }
        );
    }

    private Handler handler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    execloadactivity.canclediglog();
                    List<MPBean> list = new ArrayList<>();
                    list = (List<MPBean>) msg.obj;
                    if(list.size() > 0) {
                         mAdapter = new CheckAdapter(mList,machine_MP.this);
                         mListView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(machine_MP.this,"数据为空!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    execloadactivity.canclediglog();
                    String str = (String) msg.obj;
                    if("0".equals(str)) {
                        Toast.makeText(machine_MP.this,"保存数据成功",Toast.LENGTH_SHORT).show();
                    } else if("99".equals(str)) {
                        Toast.makeText(machine_MP.this,"保存数据失败",Toast.LENGTH_SHORT).show();
                    } else if("20".equals(str)) {
                        Toast.makeText(machine_MP.this,"點檢表單不存在",Toast.LENGTH_SHORT).show();
                    } else if("18".equals(str)) {
                        Toast.makeText(machine_MP.this,"該表單沒有强制結束，不能作業",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(machine_MP.this,"更改失败",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };


    class CheckAdapter extends BaseAdapter {
        private List<MPBean> mList;
        private Context mContext;

        public CheckAdapter(List<MPBean> mList,Context mContext) {
            this.mList = mList;
            this.mContext = mContext;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_source20,null);
                holder = new ViewHolder();
                holder.Text1 = (TextView) convertView.findViewById(R.id.ls8_Mnametext1);
                holder.Text2 = (TextView) convertView.findViewById(R.id.ls8_Mdevicetext1);
                holder.Text3 = (TextView) convertView.findViewById(R.id.ls8_Mdatetext1);
                holder.Text4 = (TextView) convertView.findViewById(R.id.ls8_userdept1);
                holder.Text5 = (TextView) convertView.findViewById(R.id.ls8_opname1);
                holder.Text6 = (TextView) convertView.findViewById(R.id.ls8_linename1);
                holder.Text7 = (TextView) convertView.findViewById(R.id.ls8_checkmold);
                holder.Text8 = (TextView) convertView.findViewById(R.id.ls8_fileversion);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            int c = 0;
            if(mList.get(position).getUPDATEDATE() != null && !"".equals(mList.get(position).getUPDATEDATE())){
                String tempmachinestatus = mList.get(position).getUPDATEDATE().toString();
                if (tempmachinestatus.equals("")) {
                    //c=Color.rgb(0,255,0);//gre en
                    c = Color.rgb(240, 128, 128);//light red
                } else {
                    c = Color.rgb(144, 238, 144);//light green
                }
            }
            holder.Text1.setText(mList.get(position).getMACHINENO());
            holder.Text1.setBackgroundColor(c);
            holder.Text2.setText(mList.get(position).getCHECKTIME());
            holder.Text2.setBackgroundColor(c);
            holder.Text3.setText(mList.get(position).getCHECKDATAID());
            holder.Text3.setBackgroundColor(c);
            holder.Text4.setText(mList.get(position).getLINENAME());
            holder.Text4.setBackgroundColor(c);
            holder.Text5.setText(mList.get(position).getOPNAME());
            holder.Text5.setBackgroundColor(c);
            holder.Text6.setText(mList.get(position).getLINENAME());
            holder.Text6.setBackgroundColor(c);
            holder.Text7.setText(mList.get(position).getISEND());
            holder.Text7.setBackgroundColor(c);
            holder.Text8.setText(mList.get(position).getFILEVERSION());
            holder.Text8.setBackgroundColor(c);
            return convertView;
        }
    }

    class ViewHolder {
        TextView Text1,Text2,Text3,Text4,Text5,Text6,Text7,Text8;
    }

}
