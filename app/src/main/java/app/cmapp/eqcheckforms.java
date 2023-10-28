package app.cmapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


import app.cmapp.appcdl.Exectempfile;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.machinecheck.eqcheckformsave;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by jiaojiao on 2016/12/14.
 */
public class eqcheckforms extends AppCompatActivity {

    Spinner D1s;
    Spinner D2s;
    Spinner D3s;
    //填充机种ListView 数据源
    private static List<String> Dl1;
    //填充机种ListView  adapter适配器
    private static ArrayAdapter<String> As1;

    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;

    private List<String> mtypelist;

    private String soapresult1;
    private String scanstr1;
    private String Eqid;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    EditText promt;
    Button submitbt;
    Spinner devicesp1;
    Spinner formssp1;
    Spinner typessp1;
    private ListView tlv1;
    private ListView mSaveListView;
    private SaveDdapter mSaveAdapter;
    private List<String> mList;
    //    private String[] strArray;
    private SharedPreferences mSharedPreferences;
    private Staticdata sc;
    private String su;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checkforms);
        promt = (EditText) findViewById(R.id.Mnametext);

        sc = new Staticdata();
        su = sc.usersite;

        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();
        tlv1 = (ListView) findViewById(R.id.Wlistview);
        mSaveListView = (ListView) findViewById(R.id.offlistview);
        mList = new ArrayList<>();
//            strArray = new String[100];
        mSaveAdapter = new SaveDdapter(mList, this);
        mtypelist = new ArrayList<>();
        mSharedPreferences = getSharedPreferences("filesave", 0);

        Bundle bundle = getIntent().getExtras();
        Eqid = bundle.getString("Eqid");
        promt.setText(bundle.getString("Eqid"));
        //promt.setText("J451-0002");
        submitbt = (Button) findViewById(R.id.Mrfidbutton);
        devicesp1 = (Spinner) findViewById(R.id.D1spinner);
        formssp1 = (Spinner) findViewById(R.id.D2spinner);
        typessp1 = (Spinner) findViewById(R.id.spinner_type);

        D1s = (Spinner) findViewById(R.id.D1spinner);
        D2s = (Spinner) findViewById(R.id.D2spinner);
        D3s = (Spinner) findViewById(R.id.spinner_type);

//            mtypelist.add("MP");
//            mtypelist.add("ENG");



//
//            As1 = new ArrayAdapter<>(eqcheckforms.this, R.layout.support_simple_spinner_dropdown_item, mtypelist);
//            D3s.setAdapter(As1);
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                String str = ps1.Checkmachineno(Eqid);
                Message msg = handle_check.obtainMessage();
                msg.what = 3;
                msg.obj = str;
                handle_check.sendMessage(msg);
            }
        });


        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<>();
                ls1 = ps1.getRemoteInfo_geteqcheckdevicenolh(Staticdata.type,Eqid);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ls1;
                handle2.sendMessage(msg);
            }
        });
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<>();
                ls1 = ps1.getRemoteInfo_geteqcheckforms(promt.getText().toString(), Sessionuser);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ls1;
                handle22.sendMessage(msg);
            }
        });
        D1s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (D1s.getSelectedItem() != null && !"".equals(D1s.getSelectedItem())) {
                    getCheckType(D1s.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//           switch (su) {
//               case "LH":
//
//                   break;
//               case "JC":
//                   loadtempdata();
//                    break;
//           }
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        List<String[]> dl5 = new ArrayList<>();
                        try {

                            dl5 = (List<String[]>) msg.obj;
                        } catch (Exception ex1) {
                            Toast.makeText(eqcheckforms.this, "获取资料异常", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (dl5 != null) {
                            tlv1.setAdapter(new myAdapter(getApplicationContext(), dl5));
                        }
                        break;
                    case 1:
                        List<String> list = new ArrayList<>();
                        list = (List<String>) msg.obj;
                        if (list.size() > 0) {
                            As1 = new ArrayAdapter<>(eqcheckforms.this, R.layout.support_simple_spinner_dropdown_item, list);
                            D3s.setAdapter(As1);
                        } else {

                        }

                        break;

                }
            } catch (Exception e1) {
                Toast.makeText(eqcheckforms.this, "未知异常", Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

    public void getCheckType(final String str) {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                mtypelist = ps1.getmachinechecktype(str);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = mtypelist;
                handle.sendMessage(msg);
            }
        });
    }

    private Handler handle222 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String[] str = (String[]) msg.obj;
                    if (str == null || "".equals(str)) {
//                        Toast.makeText(eqcheckforms.this,"緩存資料為空",Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < str.length; i++) {
                            mList.add(str[i]);
                        }
                        mSaveAdapter = new SaveDdapter(mList, eqcheckforms.this);
                        mSaveListView.setAdapter(mSaveAdapter);
                    }

                    break;
            }
        }

    };

    class SaveDdapter extends BaseAdapter {
        private List<String> mList;
        private Context context;

        public SaveDdapter(List<String> mList, Context context) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.save_layout, null);
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
//            Toast.makeText(context,machineId[1],Toast.LENGTH_SHORT).show();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (D1s.getSelectedItem() == null || "".equals(D1s.getSelectedItem())) {
                        Toast.makeText(eqcheckforms.this, "請選擇機種！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(eqcheckforms.this, eqcheckformsave.class);
                    intent.putExtra("machinenoid", machineId[0]);
                    intent.putExtra("tablename", machineId[1]);
                    intent.putExtra("deviceno", D1s.getSelectedItem().toString());
                    intent.putExtra("modl0etype", D3s.getSelectedItem().toString());
                    intent.putExtra("number", 0 + "");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        TextView mTvMachine, mTvForm;
    }

    /**
     * 啟動載入暫存資料
     */
    public void loadtempdata() {
        try {
            String fileurl = Eqid + "/";
            String[] strArray = Exectempfile.instance().getfilenew(fileurl);
//            for(int i=0 ; i <strArray.length; i++) {
//                Toast.makeText(eqcheckforms.this,strArray[i],Toast.LENGTH_SHORT).show();
//            }
//            String str = mSharedPreferences.getString("MACHINENOID_tablename","");
            Message msg = handle222.obtainMessage();
            msg.what = 0;
            msg.obj = strArray;
            handle222.sendMessage(msg);
            //Toast.makeText(this, r.get_rmsg(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Handler handle2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    List<String> ls1 = new ArrayList<>();
                    ls1 = (List<String>) msg.obj;
                    if (ls1 == null) {
                        return;
                    }
                    As1 = new ArrayAdapter<>(eqcheckforms.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D1s.setAdapter(As1);
                    //BindData(ls1);
                }
            } catch (Exception ex1) {

            }
        }
    };
    private Handler handle22 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    List<String> ls1 = new ArrayList<>();
                    ls1 = (List<String>) msg.obj;
                    if (ls1.size() == 0) {
                        return;
                    }
                    As1 = new ArrayAdapter<>(eqcheckforms.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D2s.setAdapter(As1);
                }
            } catch (Exception ex1) {

            }
        }
    };

    protected void onResume() {
        super.onResume();

        //       loadingwaitingdata() ;
    }

    public void nfconclick(View v) {

        switch (v.getId()) {
            case R.id.Mrfidbutton:

                submitbt.setText("創建中");
                submitbt.setEnabled(false);
                MyThreadPool.pool.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                String sr1 = getRemoteInfo_createdata();
                                Message msg = handle1.obtainMessage();
                                msg.what = 0;
                                msg.obj = sr1;
                                handle1.sendMessage(msg);
                            }
                        }
                );
                break;

            case R.id.Mrfidbutton1:
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        PublicSOAP ps1 = new PublicSOAP();
                        List<String> ls1 = new ArrayList<>();
                        ls1 = ps1.getRemoteInfo_geteqcheckforms(promt.getText().toString(), Sessionuser);
//                    Toast.makeText(eqcheckforms.this,ls1.size(),Toast.LENGTH_SHORT).show();
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle22.sendMessage(msg);
                    }
                });
                break;
            default:
                break;

        }
        if (devicesp1.getSelectedItem().toString() == null || "".equals(devicesp1.getSelectedItem().toString())) {
            Toast.makeText(eqcheckforms.this, "請選擇機種~", Toast.LENGTH_LONG).show();
            return;
        }
////        mSaveListView.setVisibility(View.GONE);
////        tlv1.setVisibility(View.VISIBLE);
//        submitbt.setText("創建中");
//        submitbt.setEnabled(false);
//        MyThreadPool.pool.execute(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        String sr1 = getRemoteInfo_createdata();
//                        Message msg = handle1.obtainMessage();
//                        msg.what=0;
//                        msg.obj = sr1;
//                        handle1.sendMessage(msg);
//                    }
//                }
//        );
    }

    public void loadingwaitingdata() {
        MyThreadPool.pool.execute(
                new Runnable() {

                    @Override
                    public void run() {
                        List<String[]> DL3 = new ArrayList<>();
                        DL3 = getRemoteInfo();
                        Message msg = handle.obtainMessage();
                        msg.what = 0;
                        msg.obj = DL3;
                        handle.sendMessage(msg);
                    }
                }
        );
    }

    class myAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private List<String[]> Las1;

        /*构造函数*/
        public myAdapter(Context context, List<String[]> As1) {
            this.mInflater = LayoutInflater.from(context);
            Las1 = As1;
        }

        @Override
        public Object getItem(int position) {
            return Las1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return Las1.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            //观察convertView随ListView滚动情况
            //Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_source1,
                        null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.Mnametext1);
                holder.text = (TextView) convertView.findViewById(R.id.Mdatetext1);
                holder.bt = (Button) convertView.findViewById(R.id.Mstatus1);
                holder.VH_Linename = (TextView) convertView.findViewById(R.id.listview_source1_linename1);
                holder.VH_opname = (TextView) convertView.findViewById(R.id.listview_source1_opname1);
                holder.VH_Userdept = (TextView) convertView.findViewById(R.id.listview_source1_userdept1);
                holder.VH_CHECKMOLD = (TextView) convertView.findViewById(R.id.listview_source1_checkmold1);
                holder.VH_DEVICENO = (TextView) convertView.findViewById(R.id.listview_source1_checkdeviceno);
                convertView.setTag(holder);//绑定ViewHolder对象

            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            //holder.title.setText(getDate().get(position).get("ItemTitle").toString());
            //holder.text.setText(getDate().get(position).get("ItemText").toString());


            holder.title.setText(Las1.get(position)[0]);
            holder.text.setText(Las1.get(position)[1]);
            holder.bt.setText(Las1.get(position)[2]);
            holder.VH_opname.setText(Las1.get(position)[3]);
            holder.VH_Linename.setText(Las1.get(position)[4]);
            holder.VH_Userdept.setText(Las1.get(position)[5]);
            holder.VH_CHECKMOLD.setText(Las1.get(position)[7]);
            holder.VH_DEVICENO.setText(Las1.get(position)[8]);
            //holder.bt.setText("A");
            /*为Button添加点击事件*/

            holder.bt.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    //Log.v("MyListViewBase", "你点击了按钮" + position);                                //打印Button的点击信息
                    Intent intent = new Intent(eqcheckforms.this
                            , eqchecklist.class);

                    Bundle bundle = new Bundle();
//                     Toast.makeText(eqcheckforms.this,promt.getText().toString() + "==>" +Las1.get(position)[1] + "==>" + Sessionuser,Toast.LENGTH_LONG).show();
                    bundle.putString("Sessionuser", Sessionuser);
                    bundle.putString("Eqid", promt.getText().toString());
                    bundle.putString("Eqname", Las1.get(position)[0]);
                    bundle.putString("checkdataid", Las1.get(position)[1]);
                    bundle.putString("Eqopname", Las1.get(position)[3]);
                    bundle.putString("Eqlinename", Las1.get(position)[4]);
                    bundle.putString("FILEVERSION", Las1.get(position)[6]);

                    bundle.putString("DEVICENO", Las1.get(position)[8]);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            });

            return convertView;
        }

        public final class ViewHolder {
            public TextView title;
            public TextView text;
            public TextView VH_opname;
            public TextView VH_Linename;
            public TextView VH_Userdept;
            public TextView VH_FILEVERSION;
            public TextView VH_CHECKMOLD;
            public TextView VH_DEVICENO;
            public Button bt;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出此页面吗");
            // 添加选择按钮并注册监听
            isExit.setButton2("取消", listener);
            isExit.setButton("确定", listener);
            // 显示对话框
            isExit.show();

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
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            promt.setText(scanResult);
            scanstr1 = scanResult;
        }
    }

    public String getRemoteInfo_createdata() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "createCheckDataIdnew";// "createCheckDataId ";

            String SOAP_ACTION = "http://tempuri.org/createCheckDataIdnew ";//"http://tempuri.org/createCheckDataId ";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("machinesysid", promt.getText().toString());
            rpc.addProperty("tablename", formssp1.getSelectedItem().toString());    //0:点检表单
            rpc.addProperty("userid", Sessionuser);
            rpc.addProperty("deviceno", devicesp1.getSelectedItem().toString());   //0:VTQ
            rpc.addProperty("modletype", typessp1.getSelectedItem().toString());   //点检类型

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
                //sysErrorcode="1";
                return "CMSF_1";
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            try {
                return r1.getProperty(0).toString();
            } catch (Exception ex1) {
                return "CMSF_1";
            }
        } catch (Exception e1) {
            return null;
        }
    }

    public List<String[]> getRemoteInfo() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "GetNewCheckStructheadAddTablename";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckStructheadAddTablename";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("machinesysid", promt.getText().toString());
            rpc.addProperty("tablename", formssp1.getSelectedItem().toString());    //点检表单
            rpc.addProperty("userid", Sessionuser);
            rpc.addProperty("deviceno", devicesp1.getSelectedItem().toString());   //0:VTQ
            rpc.addProperty("modletype", typessp1.getSelectedItem().toString());   //点检类型

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
                //sysErrorcode = "1";
                return null;

            }
            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            //soapresult1 = r1.getProperty(0).toString();

            SoapObject soapchild;
            try {

                soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

            } catch (Exception e) {
                e.printStackTrace();
                //sysErrorcode = "2";
                return null;
            }

            String tempsoap1 = null;
            String tempsoap2 = null;
            String tempsoap3 = null;
            String tempsoap4 = null;
            String tempsoap5 = null;
            String tempsoap6 = null;
            String tempsoap7 = null;
            String tempsoap8 = null;
            String tempsoap9 = null;
            List<String[]> Dl4 = new ArrayList<>();

            for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKDATAID").toString();
                tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("ISEND").toString();
                tempsoap4 = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString();
                tempsoap5 = ((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString();
                tempsoap6 = ((SoapObject) soapchild.getProperty(i)).getProperty("USERDEPT").toString();
                tempsoap7 = ((SoapObject) soapchild.getProperty(i)).getProperty("FILEVERSION").toString();
                tempsoap8 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKMODLE").toString();
                tempsoap9 = ((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString();
//                if(((SoapObject)soapchild.getProperty(i)).getProperty("DEVICENO").toString()==""
//                        ||((SoapObject)soapchild.getProperty(i)).getProperty("DEVICENO").toString()==null){
//                    Dl4.add(
//                            new String[]
//                                    {
//                                            tempsoap1,
//                                            tempsoap2,
//                                            "完成",
//                                            tempsoap4,
//                                            tempsoap5,
//                                            tempsoap6,
//                                            tempsoap7,
//                                            tempsoap8,
//                                            tempsoap9
//                                    }
//                    );
//
//                }else {
                Dl4.add(
                        new String[]
                                {
                                        tempsoap1,
                                        tempsoap2,
                                        "未完成",
                                        tempsoap4,
                                        tempsoap5,
                                        tempsoap6,
                                        tempsoap7,
                                        tempsoap8,
                                        tempsoap9
                                }
                );
            }
            //           }
            return Dl4;
        } catch (Exception e1) {
            return null;
        }
    }

    private Handler handle1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String sr1 = msg.obj.toString();
                    if (sr1 != null && sr1.equals("CMSF_1")) {
                        Toast.makeText(eqcheckforms.this, "获取资料异常", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    //=2为获取不到资料
                    if (sr1.equals("2")) {
                        Toast.makeText(eqcheckforms.this, "無資料", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (sr1 != null && sr1.equals("7")) {
                        Toast.makeText(eqcheckforms.this, "該表單模板未創建,請聯絡MIS部門", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (sr1 != null && sr1.equals("901")) {
                        Toast.makeText(eqcheckforms.this, "用戶所在群組無權限", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (sr1.equals("902")) {
                        Toast.makeText(eqcheckforms.this, "表单点检群组未设置", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (sr1 != null && sr1.equals("15")) {
                        Toast.makeText(eqcheckforms.this, "當日創建表單已超過最大次數限制,請聯絡MIS部門", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (sr1 != null && sr1.equals("16")) {
                        Toast.makeText(eqcheckforms.this, "當日創建表單尚未點檢完成,不能再次創建", Toast.LENGTH_LONG).show();
                        //by lyh 不卡前一张表单是否点检完
                        int size = getRemoteInfo().size();
//                        if(size<4) {
//                            if (getRemoteInfo().size() == 1) {
//                                Toast.makeText(eqcheckforms.this, "已经创建的1张表單尚未點檢完成,请速度完成点检", Toast.LENGTH_LONG).show();
//
//                            }
//                            if (getRemoteInfo().size() == 2) {
//                                Toast.makeText(eqcheckforms.this, "已经创建的2张表單尚未點檢完成,请速度完成点检", Toast.LENGTH_LONG).show();
//                            }
//                            if (getRemoteInfo().size() == 3) {
//                                Toast.makeText(eqcheckforms.this, "已经创建的3张表單尚未點檢完成,请速度完成点检", Toast.LENGTH_LONG).show();
//                            }
//                            loadingwaitingdata();
//                        }else {
//                            Toast.makeText(eqcheckforms.this, "最多村子3张未完成點檢的表单,请速度完成点检", Toast.LENGTH_LONG).show();
//                        }
                        //return;
                    }
                    if (sr1 != null && sr1.equals("99")) {
                        Toast.makeText(eqcheckforms.this, "您无权限创建点检清单", Toast.LENGTH_LONG).show();
                        //return;
                    } else {
                        if (sr1 != null && sr1.equals("8")) {
                            Toast.makeText(eqcheckforms.this, "表單信息尚未設定，請聯絡MIS人員", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            loadingwaitingdata();
                        }
                    }
                    submitbt.setText("创建");
                    submitbt.setEnabled(true);
                }
            } catch (Exception e1) {
            }
        }
    };

    private Handler handle_check = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String sr1 = msg.obj.toString();
                    //=2为获取不到资料
                    if (sr1.equals("0")) {
                        Toast.makeText(eqcheckforms.this, "無資料", Toast.LENGTH_LONG).show();
                        submitbt.setEnabled(true);
                        //return;
                    } else if (sr1.equals("806")) {
                        Toast.makeText(eqcheckforms.this, "機臺號不存在", Toast.LENGTH_LONG).show();
                        submitbt.setEnabled(false);
                        //return;8
                    } else if (sr1.equals("807")) {
                        Toast.makeText(eqcheckforms.this, "機臺號不可用", Toast.LENGTH_LONG).show();
                        submitbt.setEnabled(false);
                        //return;
                    } else {

                    }


                }
            } catch (Exception e1) {
            }
        }
    };
}
