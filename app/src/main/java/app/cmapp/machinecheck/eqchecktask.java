package app.cmapp.machinecheck;

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

import app.cmapp.Interface.Iobjectrhandler;
import app.cmapp.Staticdata;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.eqchecklist;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqchecktask extends AppCompatActivity implements Iobjectrhandler {


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
    Spinner D3s;
    Spinner D4s;

    Spinner devicesp1;
    Spinner locationsp2;
    Spinner linesp3;
    Spinner opnamesp4;
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

        devicesp1 = (Spinner) findViewById(R.id.D1spinner);
        locationsp2 = (Spinner) findViewById(R.id.D2spinner);
        linesp3 = (Spinner) findViewById(R.id.D3spinner);
        opnamesp4 = (Spinner) findViewById(R.id.D4spinner);
        D1s = (Spinner) findViewById(R.id.D1spinner);
        D2s = (Spinner) findViewById(R.id.D2spinner);
        D3s = (Spinner) findViewById(R.id.D3spinner);
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


    }

    public void remove(int position) {
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
    }

    /**
     * 接口回調方法
     *
     * @param parmerobject 依用戶需求定義
     * @param o            回傳結果參數
     */
    @Override
    public void execobject(Object parmerobject, Object o) {
        try {
            List<String> ls1 = new ArrayList<>();
            ls1 = (List<String>) o;
            if (ls1 == null) {
                return;
            }
            switch ((int) parmerobject) {
                //綁定機種
                case 0:
                    As1 = new ArrayAdapter<>(eqchecktask.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D1s.setAdapter(As1);
                    final BaseHandler b1 = new BaseHandler(this, 1, this);
                    D1s.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    MyThreadPool.pool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            String str1;
                                            if (devicesp1.getSelectedItem() == null || "".equals(devicesp1.getSelectedItem())) {
                                                str1 = "";
                                            } else {
                                                str1 = devicesp1.getSelectedItem().toString();
                                            }
                                            PublicSOAP ps1 = new PublicSOAP();
                                            List<String> ls1 = new ArrayList<>();
                                            ls1 = ps1.getRemoteInfo_geteqchecklocationlh(str1);
                                            Message msg = new Message();
                                            msg.what = 0;
                                            msg.obj = ls1;
                                            b1.sendMessage(msg);
                                        }
                                    });
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            }
                    );
                    break;
                //绑定
                case 1:
                    As2 = new ArrayAdapter<>(eqchecktask.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D2s.setAdapter(As2);
                    final BaseHandler b2 = new BaseHandler(this, 2, this);
                    D2s.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    MyThreadPool.pool.execute(new Runnable() {
                                        @Override
                                        public void run() {

                                            String str;
                                            if (locationsp2.getSelectedItem() == null || "".equals(locationsp2.getSelectedItem())) {
                                                str = "";
                                            } else {
                                                str = locationsp2.getSelectedItem().toString();
                                            }

                                            String str1;
                                            if (devicesp1.getSelectedItem() == null || "".equals(devicesp1.getSelectedItem())) {
                                                str1 = "";
                                            } else {
                                                str1 = devicesp1.getSelectedItem().toString();
                                            }

                                            PublicSOAP ps1 = new PublicSOAP();
                                            List<String> ls1 = new ArrayList<>();
                                            ls1 = ps1.getRemoteInfo_geteqchecklinelh(str, str1);
                                            Message msg = new Message();
                                            msg.what = 0;
                                            msg.obj = ls1;
                                            b2.sendMessage(msg);
                                        }
                                    });
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            }
                    );
                    break;
                //Clear all cache file
                case 2:
                    As3 = new ArrayAdapter<String>(eqchecktask.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D3s.setAdapter(As3);
                    final BaseHandler b3 = new BaseHandler(this, 3, this);

                    D3s.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    MyThreadPool.pool.execute(new Runnable() {
                                        @Override
                                        public void run() {

                                            PublicSOAP ps1 = new PublicSOAP();
                                            List<String> ls1 = new ArrayList<>();
                                            String str;
                                            if (linesp3.getSelectedItem() == null || "".equals(linesp3.getSelectedItem())) {
                                                str = "";
                                            } else {
                                                str = linesp3.getSelectedItem().toString();
                                            }
                                            String str1;
                                            if (locationsp2.getSelectedItem() == null || "".equals(locationsp2.getSelectedItem())) {
                                                str1 = "";
                                            } else {
                                                str1 = locationsp2.getSelectedItem().toString();
                                            }
                                            String str2;
                                            if (devicesp1.getSelectedItem() == null || "".equals(devicesp1.getSelectedItem())) {
                                                str2 = "";
                                            } else {
                                                str2 = devicesp1.getSelectedItem().toString();
                                            }

                                            ls1 = ps1.getRemoteInfo_geteqcheckopnamelh(str1);
                                            Message msg = new Message();
                                            msg.what = 0;
                                            msg.obj = ls1;
                                            b3.sendMessage(msg);
                                        }
                                    });
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            }
                    );
                    break;
                case 3:

                    As4 = new ArrayAdapter<>(eqchecktask.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D4s.setAdapter(As4);

                    i = 1;

                    break;

            }
        } catch (Exception ex) {
            throw ex;
        }

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
                        String str;
                        if (linesp3.getSelectedItem() == null || "".equals(linesp3.getSelectedItem())) {
                            str = "";
                        } else {
                            str = linesp3.getSelectedItem().toString();
                        }

                        String str1;
                        if (locationsp2.getSelectedItem() == null || "".equals(locationsp2.getSelectedItem())) {
                            str1 = "";
                        } else {
                            str1 = locationsp2.getSelectedItem().toString();
                        }

                        String str2;
                        if (devicesp1.getSelectedItem() == null || "".equals(devicesp1.getSelectedItem())) {
                            str2 = "";
                        } else {
                            str2 = devicesp1.getSelectedItem().toString();
                        }

                        String str3;
                        if (opnamesp4.getSelectedItem() == null || "".equals(opnamesp4.getSelectedItem())) {
                            str3 = "";
                        } else {
                            str3 = opnamesp4.getSelectedItem().toString();
                        }
                        mList= ps1.getRemoteInfo_eqchecktasknew("", Sessionuser, str1, str3, str2);
                        Message msg = new Message();
                        msg.what = 1;
                        handle.sendMessage(msg);

                    }
                }
        );
    }

    public void bindSpinnerdata() {
        final BaseHandler b = new BaseHandler(this, 0, this);
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {

                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<>();
                ls1 = ps1.getRemoteInfo_geteqcheckdevicenolh(Staticdata.type,null);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ls1;
                b.sendMessage(msg);
            }
        });
    }

    //    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 0) {
//               String str = (String) msg.obj;
//                if("0".equals(str)) {
//                  Toast.makeText(eqchecktask.this,"保存数据成功",Toast.LENGTH_SHORT).show();
//                } else if("99".equals(str)) {
//                    Toast.makeText(eqchecktask.this,"保存数据失败",Toast.LENGTH_SHORT).show();
//                } else if("20".equals(str)) {
//                    Toast.makeText(eqchecktask.this,"點檢表單不存在",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    };
    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
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
                case 1:

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