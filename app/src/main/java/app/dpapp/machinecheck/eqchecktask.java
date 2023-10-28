package app.dpapp.machinecheck;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;


import java.util.ArrayList;
import java.util.List;

import app.dpapp.Interface.Iobjectrhandler;
import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.eqchecklist;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqchecktask extends AppCompatActivity {


    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;
    private static List<String[]> Dl1;
    private static List<String[]> Dl3;
    private static List<String[]> Dl4;
    //    LinearLayout listll2;
    //填充机种ListView  adapter适配器
    private static ArrayAdapter<String> As1;
    //填充區域ListView  adapter适配器
    private static ArrayAdapter<String> As2;
    //填充線體ListView  adapter适配器
    private static ArrayAdapter<String> As3;
    //填充站位ListView  adapter适配器
    private static ArrayAdapter<String> As4;
    String eqid;
    String eqname;
    String checkdataid;
    String Eqopname;
    String Eqlinename;
    String FILEVERSION;
    String DEVICENO;

    Spinner D1s;
    Spinner D2s;
//    Spinner D3s;
    Spinner D4s;

//    Spinner devicesp1;
//    Spinner locationsp2;
//    Spinner linesp3;
//    Spinner opnamesp4;
    int i = 0;

    private ListView mListView;
    private CheckAdapter mAdapter;
    private List<PublicSOAP.eqchecktaskcls> mList;
    private List<PublicSOAP.eqchecktaskcls> mList2;
    private Button mButton;
    private ListView mListView2;
    private Staticdata sc;
    private String su;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checktask);

        sc = new Staticdata();
        su = sc.usersite;

        mListView = (ListView) findViewById(R.id.lv_dianjian);
        mListView2 = (ListView) findViewById(R.id.lv_check);
        mList2 = new ArrayList<>();
        mList = new ArrayList<>();
        mAdapter = new CheckAdapter(mList, this);
        mButton = (Button) findViewById(R.id.eq_checklistbutton1);
        mButton.setOnClickListener(new OnClick());
        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

//        devicesp1 = (Spinner) findViewById(R.id.D1spinner);
//        locationsp2 = (Spinner) findViewById(R.id.D2spinner);
//        linesp3 = (Spinner) findViewById(R.id.D3spinner);
//        opnamesp4 = (Spinner) findViewById(R.id.D4spinner);
        D1s = (Spinner) findViewById(R.id.D1spinner);
        D2s = (Spinner) findViewById(R.id.D2spinner);
//        D3s = (Spinner) findViewById(R.id.D3spinner);
        D4s = (Spinner) findViewById(R.id.D4spinner);
        bindSpinnerdata();
        loadingwaitingdata();

        if (Staticdata.mgr.toString().equals("1") ) {
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(eqchecktask.this);
                    builder.setTitle("選擇").setMessage("確定要刪除?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            deleteForms(mList.get(position).getCHECKDATAID());
//                            remove(position);
                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    String str = ps1.delete(Sessionuser, mList.get(position).getCHECKDATAID());
                                    String str1 = str + "_" + position;
                                    Message msg = handle.obtainMessage();
                                    msg.what = 3;
                                    msg.obj = str1;
                                    handle.sendMessage(msg);
                                }
                            });
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eqid = mList.get(position).getMACHINESYSID();
                checkdataid = mList.get(position).getCHECKDATAID();
                Eqopname = mList.get(position).getOPNAME();
                Eqlinename = mList.get(position).getLINENAME();
                FILEVERSION = mList.get(position).getFILEVERSION();
                eqname = mList.get(position).getMACHINENO();
                DEVICENO=mList.get(position).getDEVICENO();

                Intent intent = new Intent(eqchecktask.this
                        , eqchecklist.class);
                Bundle bundle = new Bundle();
                bundle.putString("Sessionuser", Sessionuser);
                bundle.putString("Eqid", eqid);
                bundle.putString("Eqname", eqname);
                bundle.putString("checkdataid", checkdataid);
                bundle.putString("Eqopname", Eqopname);
                bundle.putString("Eqlinename", Eqlinename);
                bundle.putString("FILEVERSION", FILEVERSION);
                bundle.putString("DEVICENO", DEVICENO);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(eqchecktask.this
                        , eqchecklist.class);
                Bundle bundle = new Bundle();
                bundle.putString("Sessionuser", Sessionuser);
                bundle.putString("Eqid", mList2.get(position).getMACHINESYSID());
                bundle.putString("Eqname", mList2.get(position).getMACHINENO());
                bundle.putString("checkdataid", mList2.get(position).getCHECKDATAID());
                bundle.putString("Eqopname", mList2.get(position).getOPNAME());
                bundle.putString("Eqlinename", mList2.get(position).getLINENAME());
                bundle.putString("FILEVERSION", mList2.get(position).getFILEVERSION());
                bundle.putString("DEVICENO", mList2.get(position).getDEVICENO());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        D2s.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {

                                String str1;
                                if (D2s.getSelectedItem() == null || "".equals(D2s.getSelectedItem())) {
                                    str1 = "";
                                } else {
                                    str1 = D2s.getSelectedItem().toString();
                                }
                                PublicSOAP ps1 = new PublicSOAP();
                                List<String> ls1 = ps1.getRemoteInfo_geteqcheckopnamelh(str1);
                                Message msg = new Message();
                                msg.what = 6;
                                msg.obj = ls1;
                                handle.sendMessage(msg);
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );


    }

    public void remove(int position) {
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
    }

    protected void onResume() {
        super.onResume();

        loadingwaitingdata();
    }


    public void loadingwaitingdata() {
        execloadactivity.opendialog(eqchecktask.this, "正在執行");
        mList.clear();
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
        mListView.setVisibility(View.VISIBLE);
        mListView2.setVisibility(View.GONE);
        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        PublicSOAP ps1 = new PublicSOAP();
                        mList = ps1.getRemoteInfo_eqchecktask(Sessionuser, "0");
                        Message msg = new Message();
                        msg.what = 0;
                        handle.sendMessage(msg);
                    }
                }
        );
    }

    public void loadingwaitingdatanew() {
        execloadactivity.opendialog(eqchecktask.this, "正在執行");
        MyThreadPool.pool.execute(
                new Runnable() {

                    @Override
                    public void run() {

                        PublicSOAP ps1 = new PublicSOAP();
                        String str1;
                        if (D2s.getSelectedItem() == null || "".equals(D2s.getSelectedItem())) {
                            str1 = "";
                        } else {
                            str1 = D2s.getSelectedItem().toString();
                        }

                        String str2;
                        if (D1s.getSelectedItem() == null || "".equals(D1s.getSelectedItem())) {
                            str2 = "";
                        } else {
                            str2 = D1s.getSelectedItem().toString();
                        }

                        String str3;
                        if (D4s.getSelectedItem() == null || "".equals(D4s.getSelectedItem())) {
                            str3 = "";
                        } else {
                            str3 = D4s.getSelectedItem().toString();
                        }
                        mList= ps1.getRemoteInfo_eqchecktasknew("", Sessionuser, str1, str3, str2);
                        Message msg = new Message();
                        msg.what = 1;
                        handle.sendMessage(msg);

                    }
                }
        );
    }

    /**
     *  1.先查询表单的设置的周期  是日 周 月 季 年；2.在查询上一份表单的点检日期；3.跟当前时间比较确定是否点检
     */

    public void bindSpinnerdata() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {

                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = ps1.getRemoteInfo_geteqcheckdevicenolh(Staticdata.type,"");
                Message msg = new Message();
                msg.what = 4;
                msg.obj = ls1;
                handle.sendMessage(msg);
            }
        });

        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = ps1.getRemoteInfo_geteqchecklocationlh(Staticdata.type);
                Message msg = new Message();
                msg.what = 5;
                msg.obj = ls1;
                handle.sendMessage(msg);
            }
        });
    }
    private Handler handle = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                case 1:

                    //                        mList = (List<PublicSOAP.eqchecktaskcls>) msg.obj;
                    if (mList == null) {
                        execloadactivity.canclediglog();
                        Toast.makeText(eqchecktask.this, "無資料", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAdapter = new CheckAdapter(mList, eqchecktask.this);
                    mListView.setAdapter(mAdapter);
                    execloadactivity.canclediglog();
                    break;
                case 3:
                    String str = (String) msg.obj;
                    String[] str1 = str.split("_");
                    if ("0".equals(str1[0])) {
                        Toast.makeText(eqchecktask.this, "刪除成功!", Toast.LENGTH_SHORT).show();
                        remove(Integer.parseInt(str1[1]));
                    } else if ("99".equals(str1[0])) {
                        Toast.makeText(eqchecktask.this, "刪除失敗!", Toast.LENGTH_SHORT).show();
                    } else if ("4".equals(str1[0])) {
                        Toast.makeText(eqchecktask.this, "点检编号不正確", Toast.LENGTH_SHORT).show();
                    } else if ("17".equals(str1[0])) {
                        Toast.makeText(eqchecktask.this, "該用戶沒有該群組的權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    List<String> list = (List<String>) msg.obj;
                    As1 = new ArrayAdapter<>(eqchecktask.this, R.layout.spinner_textview, list);
                    D1s.setAdapter(As1);
                    break;
                case 5:
                    List<String> list1 = (List<String>) msg.obj;
                    As2 = new ArrayAdapter<>(eqchecktask.this, R.layout.spinner_textview, list1);
                    D2s.setAdapter(As2);
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {

                            String str1;
                            if (D2s.getSelectedItem() == null || "".equals(D2s.getSelectedItem())) {
                                str1 = "";
                            } else {
                                str1 = D2s.getSelectedItem().toString();
                            }
                            PublicSOAP ps1 = new PublicSOAP();
                            List<String> ls1 = ps1.getRemoteInfo_geteqcheckopnamelh(str1);
                            Message msg = new Message();
                            msg.what = 6;
                            msg.obj = ls1;
                            handle.sendMessage(msg);
                        }
                    });
                    break;
                case 6:
                    List<String> list3 = (List<String>) msg.obj;
                    As4 = new ArrayAdapter<>(eqchecktask.this, R.layout.spinner_textview, list3);
                    D4s.setAdapter(As4);
                    break;
            }

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            // 创建退出对话框
        }

        return false;

    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.eq_checklistbutton1:
//                    mListView.setVisibility(View.GONE);
//                    mListView2.setVisibility(View.VISIBLE);
                    if (mList == null) {
                        loadingwaitingdatanew();
                    } else {
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                        mListView.invalidate();
                        loadingwaitingdatanew();
                    }
                    break;
            }
        }
    }

    class CheckAdapter extends BaseAdapter {
        private List<PublicSOAP.eqchecktaskcls> mList;
        private Context mContext;

        public CheckAdapter(List<PublicSOAP.eqchecktaskcls> mList, Context mContext) {
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
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_source8, null);
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
            int c;
            String tempmachinestatus = mList.get(position).getupdatedate().toString();
            if (tempmachinestatus.equals("")) {
                //c=Color.rgb(0,255,0);//gre en
                c = Color.rgb(240, 128, 128);//like red
            } else {
                c = Color.rgb(144, 238, 144);//light green
            }
            holder.Text1.setText(mList.get(position).getMACHINENO());
            holder.Text1.setBackgroundColor(c);
            holder.Text2.setText(mList.get(position).getDEVICENO());
            holder.Text2.setBackgroundColor(c);
            holder.Text3.setText(mList.get(position).getCHECKDATAID());
            holder.Text3.setBackgroundColor(c);
            holder.Text4.setText(mList.get(position).getUSERDEPT());
            holder.Text4.setBackgroundColor(c);
            holder.Text5.setText(mList.get(position).getOPNAME());
            holder.Text5.setBackgroundColor(c);
            holder.Text6.setText(mList.get(position).getLINENAME());
            holder.Text6.setBackgroundColor(c);
            holder.Text7.setText(mList.get(position).getCHECKMODLE());
            holder.Text7.setBackgroundColor(c);
            holder.Text8.setText(mList.get(position).getFILEVERSION());
            holder.Text8.setBackgroundColor(c);
            return convertView;
        }


    }

    class ViewHolder {
        TextView Text1, Text2, Text3, Text4, Text5, Text6, Text7, Text8;
    }

}