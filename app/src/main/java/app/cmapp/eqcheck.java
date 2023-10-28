package app.cmapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;
import java.util.List;
import app.cmapp.appcdl.MyThreadPool;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqcheck extends AppCompatActivity {

    Spinner D1s;

    //填充机种ListView 数据源
    private static List<String> Dl1;
    //填充机种ListView  adapter适配器
    private static ArrayAdapter<String> As1;
    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;

    private String soapresult1;
    private String scanstr1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    EditText promt;
    Button submitbt;
    Spinner devicesp1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;
    private NfcAdapter mAdapter;

    private String sysErrorcode; // 1:获取资料异常
    private  String checktype;//0:點檢 1:保養

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_check);
        try {
            promt = (EditText) findViewById(R.id.Mnametext);

            final Staticdata app = (Staticdata) getApplication();
            Sessionuser = app.getLoginUserID();
            String Site = app.usersite;

            Bundle bundle = this.getIntent().getExtras();
            promt.setText(bundle.getString("Eqid"));
            //promt.setText("J451-0002");
            submitbt = (Button) findViewById(R.id.Mrfidbutton);
            devicesp1 = (Spinner) findViewById(R.id.D1spinner);
            checktype = bundle.getString("checktype");
            D1s = (Spinner) findViewById(R.id.D1spinner);

            Dl1 = new ArrayList<String>();
            //if(Site.equals("LH")) {
            MyThreadPool.pool.execute(new Runnable() {
                @Override
                public void run() {
                    PublicSOAP ps1 = new PublicSOAP();
                    List<String> ls1 = new ArrayList<String>();
                    ls1 = ps1.getRemoteInfo_geteqcheckdevicenolh(Staticdata.type,null);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = ls1;
                    handle2.sendMessage(msg);
                }
            });
        //}
            /*
            if(Site.equals("LH")) {
                Dl1.add("VT-Q");
                Dl1.add("RI-G");
                Dl1.add("RI-L");
                Dl1.add("RI-L PRQ");
                 Dl1.add("J94");
                  Dl1.add("J95");
            }

            if(Site.equals("JC"))
            {
                Dl1.add("SC0602");
                Dl1.add("BLACKCOMB1");
                Dl1.add("BLACKCOMB2");
                Dl1.add("CFH40");
                Dl1.add("CFH39");
                Dl1.add("CFH35");
                Dl1.add("CMT47");
                Dl1.add("DC0301A");
                Dl1.add("DC0213");
                Dl1.add("GC0201");
                Dl1.add("LANIKAI1");
                Dl1.add("LANIKAI2");
                Dl1.add("LANIKAI3");
                Dl1.add("KAIYANG");
                Dl1.add("MC0301_M");
                Dl1.add("HC0201");
                Dl1.add("HC0202");
                Dl1.add("HC1201");
                Dl1.add("SC1101");
                Dl1.add("SAANA");
                Dl1.add("SPHINX");
                Dl1.add("SC1102_P");
                Dl1.add("P246");
                Dl1.add("TUNTURILONG");
                Dl1.add("ZOOM");
                Dl1.add("Gaudi");
                Dl1.add("Dali");
                Dl1.add("SC1005");
                Dl1.add("CMT46");
                Dl1.add("Qin");
                Dl1.add("Tang");
                Dl1.add("Tang Front");
                Dl1.add("Oassis");
                Dl1.add("ALL");
                As1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Dl1);
                D1s.setAdapter(As1);
            }
*/




        }
        catch(Exception e1)
        {
        }
    }

    private Handler handle2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    List<String> ls1 = new ArrayList<String>();
                    ls1 = (List<String>) msg.obj;
                    if (ls1 == null) {
                        return;
                    }
                    As1 = new ArrayAdapter<String>(eqcheck.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    D1s.setAdapter(As1);

                }
            }
            catch(Exception ex1)
            {

            }
        }
    };
    protected void onResume() {
        super.onResume();

        loadingwaitingdata() ;
    }
    public void nfconclick(View v) {
        submitbt.setText("創建中");
        submitbt.setEnabled(false);
        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        String sr1 = getRemoteInfo_createdata();
                        Message msg = handle1.obtainMessage();
                        msg.what=0;
                        msg.obj = sr1;
                        handle1.sendMessage(msg);
                    }
                }
        );
    }

    public void loadingwaitingdata(){
        MyThreadPool.pool.execute(
                new Runnable() {

                    @Override
                    public void run() {
                        List<String[]> DL3=new ArrayList<String[]>();
                        DL3=getRemoteInfo();
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
                holder.VH_Userdept= (TextView) convertView.findViewById(R.id.listview_source1_userdept1);
                convertView.setTag(holder);//绑定ViewHolder对象

            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            //holder.title.setText(getDate().get(position).get("ItemTitle").toString());
            //holder.text.setText(getDate().get(position).get("ItemText").toString());


            holder.title.setText(Las1.get(position)[0].toString());
            holder.text.setText(Las1.get(position)[1].toString());
            holder.bt.setText(Las1.get(position)[2].toString());
            holder.VH_opname.setText(Las1.get(position)[3].toString());
            holder.VH_Linename.setText(Las1.get(position)[4].toString());
            holder.VH_Userdept.setText(Las1.get(position)[5].toString());
            //holder.bt.setText("A");
            /*为Button添加点击事件*/

            holder.bt.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    //Log.v("MyListViewBase", "你点击了按钮" + position);                                //打印Button的点击信息
                    Intent intent = new Intent(eqcheck.this
                            , eqchecklist.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("Sessionuser", Sessionuser);
                    bundle.putString("Eqid", promt.getText().toString());
                    bundle.putString("Eqname",Las1.get(position)[0].toString());
                    bundle.putString("checkdataid",Las1.get(position)[1].toString());
                    bundle.putString("Eqopname",Las1.get(position)[3].toString());
                    bundle.putString("Eqlinename",Las1.get(position)[4].toString());
                    bundle.putString("FILEVERSION",Las1.get(position)[6].toString());
                    bundle.putString("DEVICENO", "");
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
            public Button bt;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {

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

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            promt.setText(scanResult);
            scanstr1=scanResult;
        }
    }
    public String getRemoteInfo_createdata() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "GetNewCheckdataid ";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckdataid ";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("machinesysid", promt.getText().toString());
            rpc.addProperty("checktype", checktype);    //0:点检表单
            rpc.addProperty("userid", Sessionuser);
            rpc.addProperty("deviceno", devicesp1.getSelectedItem().toString());   //0:VTQ

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
        }
        catch(Exception e1)
        {
            return null;
        }
    }

    public List<String[]> getRemoteInfo() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "GetNewCheckStructhead";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckStructhead";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("machinesysid", promt.getText().toString());
            rpc.addProperty("checktype", checktype);    //0:点检表单
            rpc.addProperty("userid", Sessionuser);
            rpc.addProperty("deviceno", devicesp1.getSelectedItem().toString());   //0:VTQ

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

            List<String[]> Dl4 = new ArrayList<String[]>();

            for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKDATAID").toString();
                tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("ISEND").toString();
                tempsoap4 = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString();
                tempsoap5 = ((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString();
                tempsoap6 = ((SoapObject) soapchild.getProperty(i)).getProperty("USERDEPT").toString();
                tempsoap7 = ((SoapObject) soapchild.getProperty(i)).getProperty("FILEVERSION").toString();
                Dl4.add(
                        new String[]
                                {
                                        tempsoap1,
                                        tempsoap2,
                                        "未完成",
                                        tempsoap4,
                                        tempsoap5,
                                        tempsoap6,
                                        tempsoap7
                                }
                );
            }
            return  Dl4;
        }
        catch(Exception e1)
        {
            return null;
        }
    }


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {

                    List<String[]> dl5=new ArrayList<String[]>();
                    try {
                        dl5 = (List<String[]>) msg.obj;
                    }
                    catch(Exception ex1)
                    {
                        Toast.makeText(eqcheck.this, "获取资料异常", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(dl5!=null) {
                        ListView tlv1 = (ListView) findViewById(R.id.Wlistview);
                        tlv1.setAdapter(new myAdapter(getApplicationContext(), dl5));
                    }

                }
            }
            catch(Exception e1)
            {
                Toast.makeText(eqcheck.this, "未知异常", Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

    private Handler handle1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String sr1 = msg.obj.toString();
                    if (sr1 != null && sr1.equals("CMSF_1")) {
                        Toast.makeText(eqcheck.this, "获取资料异常", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    //=2为获取不到资料
                    if (sr1.equals("2")) {
                        Toast.makeText(eqcheck.this, "無資料", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (sr1 != null && sr1.equals("7")) {
                        Toast.makeText(eqcheck.this, "該表單模板未創建,請聯絡MIS部門", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (sr1 != null && sr1.equals("901")) {
                        Toast.makeText(eqcheck.this, "用戶所在群組無權限", Toast.LENGTH_LONG).show();
                        //return;
                    }

                    if (sr1 != null && sr1.equals("15")) {
                        Toast.makeText(eqcheck.this, "當日創建表單已超過最大次數限制,請聯絡MIS部門", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (sr1 != null && sr1.equals("16")) {
                        Toast.makeText(eqcheck.this, "當日創建表單尚未點檢完成,不能再次創建", Toast.LENGTH_LONG).show();
                        //return;
                    }
                    if (sr1 != null && sr1.equals("99")) {
                        Toast.makeText(eqcheck.this, "您无权限创建点检清单", Toast.LENGTH_LONG).show();
                        //return;
                    } else {
                        if (sr1 != null && sr1.equals("8")) {
                            Toast.makeText(eqcheck.this, "表單信息尚未設定，請聯絡MIS人員", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            loadingwaitingdata();
                        }
                    }
                    submitbt.setText("创建");
                    submitbt.setEnabled(true);
                }
            }
            catch(Exception e1)
            {}
        }
    };
}
