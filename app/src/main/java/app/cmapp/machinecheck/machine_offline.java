package app.cmapp.machinecheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.cmapp.Interface.Iobjectrhandler;
import app.cmapp.Staticdata;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.Exectempfile;
import app.cmapp.appcdl.FreedomSOAPCallBack;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.execloadactivity;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by S7187445 on 2017/9/29.
 */
public class machine_offline  extends AppCompatActivity  implements  Iobjectrhandler,FreedomSOAPCallBack {
    protected String Sessionuser;

    Spinner spishopno;
    Spinner spiareano;
    Button btsearch;
    Button btoffline;

    private List<eqcheckmachinelistcls> mList;
    private ListView mListView;
    private OfflineAdapter mAdapter;
//    private List<eqcheckmachinelistcls> mList;
//    LinearLayout lly1;

    //填充區域spishopno  adapter适配器
    private static ArrayAdapter<String> As1;
    //填充區域spiareano  adapter适配器
    private static ArrayAdapter<String> As2;
    private SharedPreferences mSharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_machineoffline);

        try {
            Bundle bundle = getIntent().getExtras();
            final Staticdata app = (Staticdata)getApplication();
            Sessionuser = app.getLoginUserID();
            mListView = (ListView) findViewById(R.id.lv_offline);
            spishopno=(Spinner) findViewById(R.id.D1spinner);
            spiareano=(Spinner) findViewById(R.id.D2spinner);
            btsearch = (Button) findViewById(R.id.searchbutton);
            btoffline = (Button) findViewById(R.id.offlinebutton);
            mSharedPreferences = getSharedPreferences("filesave",0);
            mList = new ArrayList<>();
            mAdapter = new OfflineAdapter(this,mList);

//            lly1 = (LinearLayout) findViewById(R.id.eqchecklistLinearLayout1);
            bindNamedata();

            spishopno.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    List<String> ls1 = new ArrayList<>();
                                    String str;
                                    if (spishopno.getSelectedItem() == null || "".equals(spishopno.getSelectedItem())) {
                                        str = "";
                                    } else {
                                        str = spishopno.getSelectedItem().toString();
                                    }
                                    bindFloordata(str);
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
            );
        }
        catch(Exception ex1) {
        }
    }

         public void bindNamedata() {
             MyThreadPool.pool.execute(
                     new Runnable() {
                         @Override
                         public void run() {
                             List<String> ls1 = getRemoteInfo_getSHOPNO();
                             Message msg = new Message();
                             msg.what = 3;
                             msg.obj = ls1;
                             handle.sendMessage(msg);
                         }
                     }
             );

           }
         public void bindFloordata(final String shopno) {
             MyThreadPool.pool.execute(
                     new Runnable() {
                         @Override
                         public void run() {
                             List<String> ls1 = getRemoteInfo_getbulidingno(shopno);
                             Message msg = new Message();
                             msg.what = 4;
                             msg.obj = ls1;
                             handle.sendMessage(msg);
                         }
                     }
             );

      }

    public  List<String> getRemoteInfo_getSHOPNO() {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String URL = Staticdata.soapurl;
            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getshopno";
            String SOAP_ACTION = "http://tempuri.org/getshopno";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                try {
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("SHOPNO").toString());
                }
                } catch (Exception ex1) {
                    Toast.makeText(machine_offline.this,ex1.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    public  List<String> getRemoteInfo_getbulidingno(String shopno) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String URL = Staticdata.soapurl;
            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getbulidingno";
            String SOAP_ACTION = "http://tempuri.org/getbulidingno";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("shopno", shopno);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                ls1.add("ALL");
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("BULIDINGNO").toString());

                }

            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }
    /**
     * 查詢按鈕

     */
    public  void eqchecklistbt1onclick(View v)
    {
        loadingwaitingdatanew();
    }
    public void loadingwaitingdatanew(){
        execloadactivity.opendialog(machine_offline.this, "正在執行");

        MyThreadPool.pool.execute(
                new Runnable() {

                    @Override
                    public void run() {

                        List<eqcheckmachinelistcls> list = new ArrayList<>();
                        String str;
                        if (spishopno.getSelectedItem() == null || "".equals(spishopno.getSelectedItem())) {
                            str = "";
                        } else {
                            str = spishopno.getSelectedItem().toString();
                        }

                        String str1;
                        if (spiareano.getSelectedItem() == null || "".equals(spiareano.getSelectedItem())) {
                            str1 = "";
                        } else {
                            str1 = spiareano.getSelectedItem().toString();
                        }

                        list = getRemoteInfo_eqcheckmachinelist(str, str1);
                        Message msg = handle.obtainMessage();
                        msg.what = 0;
                        msg.obj = list;
                        handle.sendMessage(msg);

                    }
                }
        );

    }
    /**
     *20170930 by wll  獲取機台清單
     * @param shopno
     * @param bulidingno
     * @return
     */
    public  List<eqcheckmachinelistcls> getRemoteInfo_eqcheckmachinelist(String shopno, String bulidingno)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

             String URL=Staticdata.soapurl;

            String METHOD_NAME = "getmachinenolistdata";
            String SOAP_ACTION = "http://tempuri.org/getmachinenolistdata";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("shopno", shopno);
            rpc.addProperty("bulidingno", bulidingno);
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


            List<eqcheckmachinelistcls> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    eqcheckmachinelistcls ecls1=new eqcheckmachinelistcls();

                    ecls1.setMACHINENIDO(((SoapObject) soapchild.getProperty(i)).getProperty("SYSID").toString());
                    ecls1.setMACHINENO(((SoapObject) soapchild.getProperty(i)).getProperty("MACHINETYPE").toString());
                    ecls1.setMACHINETYPE(((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString());
                    ecls1.setSHOPNO(((SoapObject) soapchild.getProperty(i)).getProperty("SHOPNO").toString());
                    ecls1.setbulidingno(((SoapObject) soapchild.getProperty(i)).getProperty("BULIDINGNO").toString());
                    try {
                        ecls1.setwsysid(((SoapObject) soapchild.getProperty(i)).getProperty("WSYSID").toString());
                    }
                    catch (Exception e1)
                    {
//                        ecls1.wsysid ="";
                    }

                    try {
                    ecls1.setexcelname(((SoapObject) soapchild.getProperty(i)).getProperty("EXCELNAME").toString());
                    }
                    catch (Exception e1)
                    {
//                        ecls1.excelname ="";
                    }

                    try {
                    ecls1.setversion(((SoapObject) soapchild.getProperty(i)).getProperty("VERSION").toString());
                    }
                    catch (Exception e1)
                    {
//                        ecls1.version ="";
                    }

                    try {
                    ecls1.settablename(((SoapObject) soapchild.getProperty(i)).getProperty("TABLENAME").toString());
                    }
                    catch (Exception e1)
                    {
//                        ecls1.tablename ="";
                    }
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
//            try {
            switch (msg.what) {
                case 0:
                    execloadactivity.canclediglog();
                    mList = (List<eqcheckmachinelistcls>) msg.obj;

                    if (mList.size() == 0) {
                        Toast.makeText(machine_offline.this, "無資料",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAdapter = new OfflineAdapter(machine_offline.this,mList);
                    mListView.setAdapter(mAdapter);

                    break;
                case 1:
                    execloadactivity.canclediglog();
                    BaseFuncation.rrtype r =(BaseFuncation.rrtype)msg.obj;
                    Toast.makeText(machine_offline.this,r.get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    List<String> ls1 = (List<String>) msg.obj;
                    if (ls1 == null) {
                        return;
                    }
                    As1 = new ArrayAdapter<>(machine_offline.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    spishopno.setAdapter(As1);
                    if(spiareano.getSelectedItem() != null && !"".equals(spiareano.getSelectedItem())) {
                        bindFloordata(spiareano.getSelectedItem().toString());
                    }
                    break;
                case 4:
                    ls1 = (List<String>) msg.obj;
                    if (ls1 == null) {
                        return;
                    }
                    As2 = new ArrayAdapter<>(machine_offline.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    spiareano.setAdapter(As2);

                    break;

            }
        }

    };


      /**
     * 離線資料按鈕
     * @param v
     */
    public  void offlinesavatempdata(View v)
    {
        execloadactivity.opendialog(this, "正在執行");
//        List<String> list = new ArrayList<>();
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < mList.size(); i++) {
//            builder.append(mList.get(i).getMACHINENOID() + "_" + mList.get(i).gettablename() + ".txt" + "=");
//        }
////        Toast.makeText(this,builder.toString(),Toast.LENGTH_LONG).show();
//             SharedPreferences.Editor editor = mSharedPreferences.edit();
//             editor.putString("MACHINENOID_tablename",builder.toString());
//             editor.commit();
//             Message msg = handle.obtainMessage();
//             msg.what = 1;
//             handle.sendMessage(msg);
//        MyThreadPool.pool.execute(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
//                            List<String> list = new ArrayList<>();
//
//                                for (int i = 1; i < mList.size(); i++) {
//                                    list.add("[" + mList.get(i).getMACHINENOID() + "],["
//                                                    + mList.get(i).getMACHINENO() + "],["
////                                                    + ((TextView) lly1.getChildAt(i).findViewById(R.id.listviewsource10Mname6)).getText().toString() + "],["
//                                                    + mList.get(i).gettablename() + "]"
//                                    );
////                                    r = Exectempfile.instance().savefilenew(mList.get(i).getMACHINENOID() + "_" + mList.get(i).gettablename(), list, mList.get(i).getMACHINENOID() + "/");
//
//                                }
//                        r = Exectempfile.instance().savefile("machinecheck", list);
//                        Message msg = handle.obtainMessage();
//                        msg.what = 1;
//                        msg.obj = r;
//                        handle.sendMessage(msg);
//
//                    }
//                }
//        );

        final BaseHandler b=new BaseHandler(this,0,this);
        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                        try {
                            String str;
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < mList.size(); i++) {
                                    list.add("[" + mList.get(i).getMACHINENOID() + "],["
                                                    + mList.get(i).getMACHINENO() + "],["
                                                    + mList.get(i).gettablename() + "],["
                                                    + mList.get(i).gettablename() + "]");
                                  str = mList.get(i).getMACHINENOID() + "_" + mList.get(i).gettablename();
                                  r = Exectempfile.instance().savefilenew(str, list, mList.get(i).getMACHINENOID() + "/");
//                                r = Exectempfile.instance().savefile(mList.get(i).getMACHINENOID(), list);
                                }
                                msg.what = 0;
                                msg.obj = r;

                        } catch (Exception ex) {
                            r.set_rstatus(false);
                            r.set_rmsg(ex.getMessage());
                            msg.what = 1;
                            msg.obj = r;
                        }
                        b.sendMessage(msg);
                    }
                }
        );

    }
    /**
     * 接口回調方法
     * @param parmerobject 依用戶需求定義
     * @param o 回傳結果參數
     */
    @Override
    public void execobject(Object parmerobject, Object o)
    {
        try {
            List<String> ls1 = new ArrayList<>();


            switch ((int)parmerobject)
            {
                //Save Tempfile
                case 0:
                    Toast.makeText(this,((BaseFuncation.rrtype)o).get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;

                //spishopno
                case 1:
                    ls1 = (List<String>) o;
                    if (ls1 == null) {
                        return;
                    }
                    As1 = new ArrayAdapter<>(machine_offline.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    spishopno.setAdapter(As1);

                    final BaseHandler b3 = new BaseHandler(this,2, this);
                    spishopno.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    MyThreadPool.pool.execute(new Runnable() {
                                        @Override
                                        public void run() {

                                            PublicSOAP ps1 = new PublicSOAP();
                                            List<String> ls1 = new ArrayList<>();
                                            String str;
                                            if (spishopno.getSelectedItem() == null || "".equals(spishopno.getSelectedItem())) {
                                                str = "";
                                            } else {
                                                str = spishopno.getSelectedItem().toString();
                                            }
                                            ls1 = getRemoteInfo_getbulidingno(str);
                                            Message msg = new Message();
                                            msg.what = 3;
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
                case 2:
                    ls1 = (List<String>) o;
                    if (ls1 == null) {
                        return;
                    }
                    As2 = new ArrayAdapter<>(machine_offline.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    spiareano.setAdapter(As2);
                    break;

                case 3:
                    Toast.makeText(this,((BaseFuncation.rrtype)o).get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        execloadactivity.canclediglog();
    }

    @Override
    public  void BackData(Object so,Context cc,int type)
    {
        soapfilldata(so, type);
    }

    private void soapfilldata(Object o,int ctype)
    {
        execloadactivity.canclediglog();
        if(o==null) {
            Toast.makeText(this, String.valueOf(ctype) + ":獲取資料失敗，獲取資料為Null", Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject so=null;
        try {
            so = (SoapObject) o;
        }
        catch(Exception ex1)
        {
            Toast.makeText(this, String.valueOf(ctype) + ":獲取資料失敗，非標準SOAP格式", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            switch (ctype) {
                // Reject Form
                case 0:
                    String r1 = so.getProperty(0).toString();
                    switch (r1) {
                        case "true":
                            Toast.makeText(this,  "駁回成功", Toast.LENGTH_SHORT).show();
                            this.finish();
                            break;
                        case "false":
                            Toast.makeText(this,  "駁回失敗", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 1:
                    String r2=((SoapObject)((SoapObject)((SoapObject)((SoapObject)so.getProperty(0)).getProperty(1)).getProperty(0)).getProperty(0)).getProperty(0).toString();
                    //eqcheckdheadertv1.setText(r2);
                    break;
                default:
                    Toast.makeText(this, "Unknow Methods Return Result", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    class OfflineAdapter extends  BaseAdapter {
        Context context;
        List<eqcheckmachinelistcls> mList;

        public OfflineAdapter(Context context,List<eqcheckmachinelistcls> mList) {
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
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.offline_layout,null);
                holder = new ViewHolder();
                holder.textView1 = (TextView) convertView.findViewById(R.id.listviewsource10Mname1);
                holder.textView2 = (TextView) convertView.findViewById(R.id.listviewsource10Mname2);
                holder.textView3 = (TextView) convertView.findViewById(R.id.listviewsource10Mname3);
                holder.textView4 = (TextView) convertView.findViewById(R.id.listviewsource10Mname4);
                holder.textView5 = (TextView) convertView.findViewById(R.id.listviewsource10Mname5);
                holder.textView6 = (TextView) convertView.findViewById(R.id.listviewsource10Mname6);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(mList.get(position).getMACHINENOID());
            holder.textView2.setText(mList.get(position).getMACHINENO());
            holder.textView3.setText(mList.get(position).getMACHINETYPE());
            holder.textView4.setText(mList.get(position).getSHOPNO());
            holder.textView5.setText(mList.get(position).getbulidingno());
            holder.textView6.setText(mList.get(position).gettablename());

            return convertView;
        }
    }


        class ViewHolder {
          TextView textView1,textView2,textView3,textView4,textView5,textView6;
  }

    /**
     * 2016/6/21 by wll
     *點檢平臺機台信息參數定義
     */
    public class eqcheckmachinelistcls
    {
        public String MACHINENOID;
        public String getMACHINENOID()
        {
            return MACHINENOID;
        }
        public void setMACHINENIDO(String sendmachinenoid)
        {
            MACHINENOID=sendmachinenoid;
        }
        public String MACHINENO;
        public String getMACHINENO()
        {
            return MACHINENO;
        }
        public void setMACHINENO(String sendmachineno)
        {
            MACHINENO=sendmachineno;
        }
        public String MACHINETYPE;
        public String getMACHINETYPE()
        {
            return MACHINETYPE;
        }
        public void setMACHINETYPE(String sendMACHINETYPE)
        {
            MACHINETYPE=sendMACHINETYPE;
        }
        public String SHOPNO;
        public String getSHOPNO()
        {
            return SHOPNO;
        }
        public void setSHOPNO(String sendSHOPNO)
        {
            SHOPNO=sendSHOPNO;
        }
        public String bulidingno;
        public String getbulidingno()
        {
            return bulidingno;
        }
        public void setbulidingno(String sendbulidingno)
        {
            bulidingno=sendbulidingno;
        }
        public String machineopnameid;
        public String getmachineopnameid()
        {
            return machineopnameid;
        }
        public void setmachineopnameid(String sendmachineopnameid)
        {
            machineopnameid=sendmachineopnameid;
        }
        public String wsysid;
        public String getwsysid()
        {
            return wsysid;
        }
        public void setwsysid(String sendwsysid)
        {
            wsysid=sendwsysid;
        }
        public String excelname;
        public String getexcelname()
        {
            return excelname;
        }
        public void setexcelname(String sendexcelname)
        {
            excelname=sendexcelname;
        }

        public String version;
        public String getversion()
        {
            return version;
        }
        public void setversion(String sendversion)
        {
            version=sendversion;
        }
        public String tablename;
        public String gettablename()
        {
            return tablename;
        }
        public void settablename(String sendtablename)
        {
            tablename=sendtablename;
        }

    }
}
