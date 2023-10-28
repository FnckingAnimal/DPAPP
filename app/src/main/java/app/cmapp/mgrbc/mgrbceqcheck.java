package app.cmapp.mgrbc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.Staticdata;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.exechttprequest;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.appcdl.jsontolist;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.parameterclass.httprequestinputdata;
import app.cmapp.zxing.activity.CaptureActivity;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by Tod on 2016/4/13.
 *
 * By 点检资料录入
 * 修改 handler，降低代碼耦合性
 */
public class mgrbceqcheck extends ActivityInteractive //AppCompatActivity
{
    //用户名
    private static String Sessionuser;
    private LinearLayout ll1;
    private ScrollView scrollv1;
    private TextView tvvalue2;
    private Spinner afloolinesp1;
    private Spinner floorsp1;
    private Spinner linesp1;
    private EditText meqidet1;
    private EditText afloolinetv1;
    private Boolean _loadingstatus=false;
    private String area;
    private String floor;
    //填充机种ListView  adapter适配器
    private static ArrayAdapter<String> As1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgr_bc_eqcheck);

        ll1=(LinearLayout)findViewById(R.id.mgrbcmgrbceqcheck_maindataddll3);
        scrollv1=(ScrollView)findViewById(R.id.mgrbcmgrbceqcheck_maindatadsv1);
        tvvalue2=(TextView)findViewById(R.id.mgrbcmgrbceqcheck_maindatadhtv2);
        afloolinesp1=(Spinner)findViewById(R.id.mgrbcmgrbceqcheck_areafloorlinetvsp);
        floorsp1=(Spinner)findViewById(R.id.mgrbcmgrbceqcheck_floortvsp);
        linesp1=(Spinner)findViewById(R.id.mgrbcmgrbceqcheck_linetvsp);
        meqidet1=(EditText)findViewById(R.id.mgrbcmgrbceqcheck_inmcidtv1);
        afloolinetv1=(EditText)findViewById(R.id.mgrbcmgrbceqcheck_areafloorlineet);

        final Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();

        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<String>();
                ls1 = ps1.getRemoteInfo_getmgreqcheckarea();
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ls1;
                handler.sendMessage(msg);
            }
        });
        //新開線程 去執行任務
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<String>();
                ls1 = ps1.getRemoteInfo_getmgreqcheckfloor();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = ls1;
                handler.sendMessage(msg);
            }
        });

        afloolinesp1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {

                                PublicSOAP ps1 = new PublicSOAP();
                                List<String> ls1 = new ArrayList<String>();
                                ls1 = ps1.getRemoteInfo_getmgreqcheckfloor();
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = ls1;
                                handler.sendMessage(msg);
                            }
                        });

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {

                                PublicSOAP ps1 = new PublicSOAP();
                                List<String> ls1 = new ArrayList<String>();
                                ls1 = ps1.getRemoteInfo_getmgreqcheckline(afloolinesp1.getSelectedItem().toString(), floorsp1.getSelectedItem().toString());
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = ls1;
                                handler.sendMessage(msg);
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        floorsp1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {

                                PublicSOAP ps1 = new PublicSOAP();
                                List<String> ls1 = new ArrayList<>();
                                ls1 = ps1.getRemoteInfo_getmgreqcheckline(afloolinesp1.getSelectedItem().toString(), floorsp1.getSelectedItem().toString());
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = ls1;
                                handler.sendMessage(msg);
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );


    }
    // 完全可以把這些 handle 整理成一個 或者單獨把handle拿出來放在 Application 或者寫一個公共的類  這樣寫很耗時又不簡潔  以後不好維護
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        List<String> ls1 = new ArrayList<>();
                        ls1 = (List<String>) msg.obj;
                        if (ls1 == null) {
                            return;
                        }
                        As1 = new ArrayAdapter<>(mgrbceqcheck.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                        afloolinesp1.setAdapter(As1);
                    } catch(Exception ex1)
                    {

                    }
                    break;
                case 1:
                    try {
                        List<String> ls1 = new ArrayList<>();
                        ls1 = (List<String>) msg.obj;
                        if (ls1 == null) {
                            return;
                        }
                        As1 = new ArrayAdapter<>(mgrbceqcheck.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                        floorsp1.setAdapter(As1);
                    }
                    catch(Exception ex1)
                    {
                    }
                    break;
                case 2:
                    try {
                        List<String> ls1 = new ArrayList<>();
                        ls1 = (List<String>) msg.obj;
                        if (ls1 == null) {
                            return;
                        }
                        As1 = new ArrayAdapter<>(mgrbceqcheck.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                        linesp1.setAdapter(As1);
                    }
                    catch(Exception ex1)
                    {
                    }
                    break;

            }
        }
    };

    private void floorsp1_SelectedValueChanged() throws Exception
    {
        area=afloolinesp1.getSelectedItem().toString();
        floor=floorsp1.getSelectedItem().toString();
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if(scanResult == null && "".equals(scanResult)) {
                Toast.makeText(this,"掃描結果為空",Toast.LENGTH_SHORT).show();
                return;
            }
            switch (requestCode) {
                case 4:
                    View context = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source11, null);
                    String location;
                    if(linesp1.getSelectedItem() != null && !"".equals(linesp1.getSelectedItem())) {
                       location=afloolinesp1.getSelectedItem().toString()+"  "+ floorsp1.getSelectedItem().toString()+"    " +linesp1.getSelectedItem().toString();
                    } else {
                        location=afloolinesp1.getSelectedItem().toString()+"  "+ floorsp1.getSelectedItem().toString();
                    }
                    tvvalue2.setText(scanResult);
                    ((TextView) context.findViewById(R.id.listviewsource11Mname1)).setText(scanResult);
                    ((TextView) context.findViewById(R.id.listviewsource11Mname5)).setText(Sessionuser);
                    ((TextView) context.findViewById(R.id.listviewsource11Mname6)).setText(location);

                    ll1.addView(context);
                    scrollv1.scrollTo(100, ll1.getBottom());
                    break;
                case 0:
                    break;
                case 3:
//                    if(scanResult.split(":")[0].equals("1")) {
//                        ((TextView) scontextview.findViewById(R.id.listviewsource11Mname9)).setText(scanResult.split(":")[1]);
//                        ((TextView) scontextview.findViewById(R.id.listviewsource11Mname10)).setText(s);
//                        onsaveeqcheckdatatolocal(null);
//                    }
//                    else
//                    {
//                        Toast.makeText(this,"Upload Image Error:"+scanResult.split(":")[1],Toast.LENGTH_SHORT).show();
//                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onsaveeqcheckdatatolocal(View v)
    {
        execloadactivity.opendialog(this,"正在執行");
//        String savestr=tvvalue1.getText().toString();
        String savestr=Sessionuser;
        if(savestr==null||savestr.equals(""))
        {
            Toast.makeText(this,"盤點單號不能為空",Toast.LENGTH_LONG).show();
            return;
        }
        savestr="{"+savestr+":"+ System.currentTimeMillis()+"},";
        for (int i=0;i<ll1.getChildCount();i++) {
            savestr += "{" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname1)).getText().toString() + ":";
//             vestr +=   ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname7)).getText().toString() + ":";
            savestr +=   ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname5)).getText().toString() + ":";
            savestr +=   ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname6)).getText().toString() + "},";
        }

        String filePATH= "/storage/emulated/0/CMSFLog/LocalData/";
        File file = new File(filePATH);
        if (!file.exists())
        {
            file.mkdir();
        }
        File saveFile = new File(filePATH+"machineeqbccheck.txt");
        try {
            FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(savestr.getBytes());

            if(outStream != null && !"".equals(outStream)) {
                outStream.flush();
                outStream.close();
            }
        }
        catch(IOException ex)
        {
        }
        finally {

        }
        Toast.makeText(mgrbceqcheck.this,"SaveLocal",Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }
    public void onloadreadeqchecklocaldata  (View v)
            throws FileNotFoundException, IOException {
        execloadactivity.opendialog(this,"正在執行");
        String readstr = "";
        BufferedReader reader = null;
        StringBuilder data = new StringBuilder();
        try {
            File file = new File("/storage/emulated/0/CMSFLog/LocalData/machineeqbccheck.txt");
            if(file.exists()) {


                FileInputStream inStream = new FileInputStream(file);

                reader = new BufferedReader(new InputStreamReader(inStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            }
            else
            {
                Toast.makeText(mgrbceqcheck.this, "NoneLocalData", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(mgrbceqcheck.this, "LoadLocalData Fail", Toast.LENGTH_SHORT).show();
        } finally {
            if(reader!=null) {
                reader.close();
            }
        }

        readstr = data.toString();
        String[] tsl = readstr.split(",");

        View convertView;
        ll1.removeAllViews();
        for (int i = 1; i < tsl.length; i++) {
            //MACHINECHECKID,ASSETID,MGRID,ASSETNAME,ASSETTYPE,ASIGNOWNER,SAVALOCATION,"
            //+"CREATEDATE,CREATEID,CORPORATEID,SHAREDEPT,ASSETSUMMRYNAME,ASSETPHYSICSTYPE,ASSETSTATUS,PRODUCTTYPE,"
            //+ "SAVEFLOOR,SAVELINE,SAVESTATION,SVAEOPNO,execstatus
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source11, null);
            String[] tempts2 = tsl[i].replace("{","").replace("}", "").split(":");
            if (tempts2.length == 11) {
                ((TextView) convertView.findViewById(R.id.listviewsource11Mname1)).setText(tempts2[0]);
                ((TextView) convertView.findViewById(R.id.listviewsource11Mname5)).setText(tempts2[1]);
            }
//            selectitems(convertView);
            ll1.addView(convertView);
        }
        execloadactivity.canclediglog();
        //Toast.makeText(mgrbceqcheck.this,"LoadLocal",Toast.LENGTH_SHORT).show();
    }
    public void onuploadeqcheckdata(View v)
    {
        if(ll1.getChildCount()==0)
        {
            Toast.makeText(mgrbceqcheck.this,"無數據",Toast.LENGTH_SHORT).show();
            return;
        }

        if(afloolinesp1.getSelectedItem().toString().equals("") || floorsp1.getSelectedItem().toString().equals("") ||linesp1.getSelectedItem().toString().equals(""))
        {
            Toast.makeText(mgrbceqcheck.this,"上傳時必須有位置信息",Toast.LENGTH_SHORT).show();
            return;
        }
        String savestr="[";
        for (int i=0;i<ll1.getChildCount();i++) {
//            if(((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname9)).getText().toString().equals("N")==false) {
            savestr += "{\"assetid\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname1)).getText().toString()
//                        + "\",\"imageid\":\"\",\"newlocation\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname6)).getText().toString()
                    + "\",\"imageid\":\"\",\"line\":\"" + linesp1.getSelectedItem().toString()
                    +"\",\"CREATEID\":\""+Sessionuser+"\",\"area\":\"" + afloolinesp1.getSelectedItem().toString()+"\",\"floor\":\"" +floorsp1.getSelectedItem().toString()
                    + "\"},";
//            }
        }
        if(savestr.length()==1)
        {
            Toast.makeText(this,"無需要上傳的記錄",Toast.LENGTH_SHORT).show();
            return;
        }
        savestr=savestr.substring(0,savestr.length()-1);
        savestr+="]";
//http://localhost:17570/unidll/machinedata/machine_eqbccheck.ashx?
// directpar=updatebceqchecktask&
// jsonstr=[{"machinecheckid":"F201608151017001","assetid":"D00834FY"},{"machinecheckid":"F201608151017001","assetid":"D01698FY"},{"machinecheckid":"F201608151017001","assetid":"D00854FY"},{"machinecheckid":"F201608151017001","assetid":"D00824FY"}]

        List<httprequestinputdata> li =new ArrayList<>();
        httprequestinputdata h1=new httprequestinputdata();
        h1.setDataname("directpar");
        h1.setDatavalue("updatebceqchecktask");
        li.add(h1);
        httprequestinputdata h2=new httprequestinputdata();
        h2.setDataname("jsonstr");
        h2.setDatavalue(savestr);
        li.add(h2);
        getkeyindatainput(Staticdata.httpurl+"/machinedata/machine_eqbccheck.ashx", li,2);
        //String url1=Staticdata.httpurl+"/machinedata/machine_eqbccheck.ashx";
        //Toast.makeText(mgrbceqcheck.this,"UploadEqData",Toast.LENGTH_LONG).show();
    }

    public void isfc_mgr_bc_eqcheck_submiteqid_onclick(View v)
    {
        String meqid=meqidet1.getText().toString();
        if(meqid.equals(""))
        {
            Toast.makeText(this, "財產編號不允空", Toast.LENGTH_SHORT).show();
            return;
        }
        FinalStaticCloass.SpinnerData sp=(FinalStaticCloass.SpinnerData)afloolinesp1.getSelectedItem();
        String s=sp.getValue();
        View context = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source11, null);
        //context.scrollTo(0, 0);
//                            scrollv1.scrollTo(100, context.getTop());
        ((TextView) context.findViewById(R.id.listviewsource11Mname1)).setText(meqid);
        ((TextView) context.findViewById(R.id.listviewsource11Mname5)).setText(Sessionuser);
        ((TextView) context.findViewById(R.id.listviewsource11Mname6)).setText(s);
        ll1.addView(context);
        scrollv1.scrollTo(100, ll1.getBottom());
        meqidet1.setText("");
//        scrollv1.scrollTo(100, context.getTop());
    }

    public void onmgreqcheckscancodeclick(View v)
    {
       /* FinalStaticCloass.SpinnerData sp=(FinalStaticCloass.SpinnerData)afloolinesp1.getSelectedItem();
        String s=sp.getValue();
        if(s==null||s.equals(""))
        {
            Toast.makeText(this, "必須要選擇新位置", Toast.LENGTH_SHORT).show();
            return;
        }*/
        Intent openCameraIntent = new Intent(mgrbceqcheck.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 4);
    }
    public void loadingdata()
    {
        String url1=Staticdata.httpurl+"/machinedata/machine_eqbccheck.ashx";
        //machinedata/machine_eqbccheck.ashx?directpar=getbceqchecktask&machinecheckid=F201608151017001&mtaskstatus=0
        List<httprequestinputdata> li=new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1=new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getbceqchecktask");
        li.add(hhi1);
        httprequestinputdata hhi2=new httprequestinputdata();
        hhi2.setDataname("machinecheckid");
        hhi2.setDatavalue("");
        li.add(hhi2);
        httprequestinputdata hhi3=new httprequestinputdata();
        hhi3.setDataname("mtaskstatus");
        hhi3.setDatavalue("0");
        li.add(hhi3);
        //getkeyindatainput(url1, li,1);
        getkeyindatainput(url1, li,2);
    }

    public  void getkeyindatainput(String urlpath,List<httprequestinputdata> li,int sendmode) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
        // List<httprequestinputdata> li=null;
        exechttprequest hf1=new exechttprequest();
        //Log.v("CMSF", hf1.toString());
        hf1.getDataFromServer(sendmode, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1,"正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        //Log.v("CMSF", paramObject.toString());
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        Toast.makeText(mgrbceqcheck.this
                                ,"操作失敗，請求異常-"+paramObject.toString(),Toast.LENGTH_LONG).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(mgrbceqcheck.this
                                ,"操作失敗，請求異常",Toast.LENGTH_LONG).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }

    public  void fillingdata(List<jsontolist._jsonarray> j1)
    {
        //Log.v("CMSF","F1");
        if(j1!=null)
        {

            String nextstep=j1.get(0).get_valuelist().get(0).get(0).get_value();
            //Log.v("CMSF",nextstep);
            switch (nextstep) {
                case "getbceqchecktask":

                    String status0=j1.get(0).get_valuelist().get(0).get(1).get_value();
                    String msg0=j1.get(0).get_valuelist().get(0).get(2).get_value();

                    switch (status0) {
//                            case "1":
//                                if(j1.get(1).get_valuelist()==null) {
//                                }
//                                else
//                                {
//                                View convertView;
//                                ll1.removeAllViews();
//                                for (int i = 0; i < j1.get(1).get_valuelist().size(); i++) {
//                                    //MACHINECHECKID,ASSETID,MGRID,ASSETNAME,ASSETTYPE,ASIGNOWNER,SAVALOCATION,"
//                                    //+"CREATEDATE,CREATEID,CORPORATEID,SHAREDEPT,ASSETSUMMRYNAME,ASSETPHYSICSTYPE,ASSETSTATUS,PRODUCTTYPE,"
//                                    //+ "SAVEFLOOR,SAVELINE,SAVESTATION,SVAEOPNO,execstatus
//                                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source11, null);
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname0)).setText(j1.get(1).get_valuelist().get(i).get(9).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname1)).setText(j1.get(1).get_valuelist().get(i).get(1).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname2)).setText(j1.get(1).get_valuelist().get(i).get(2).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname3)).setText(j1.get(1).get_valuelist().get(i).get(3).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname4)).setText(j1.get(1).get_valuelist().get(i).get(4).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname5)).setText(j1.get(1).get_valuelist().get(i).get(5).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname6)).setText(j1.get(1).get_valuelist().get(i).get(6).get_value()
//                                            + "-" + j1.get(1).get_valuelist().get(i).get(15).get_value()
//                                            + "-" + j1.get(1).get_valuelist().get(i).get(16).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname7)).setText(j1.get(1).get_valuelist().get(i).get(17).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname8)).setText(j1.get(1).get_valuelist().get(i).get(18).get_value());
//                                    ((TextView) convertView.findViewById(R.id.listviewsource11Mname9)).setText("N");
//                                    selectitems(convertView);
//                                    ll1.addView(convertView);
//                                }
//                                }
//                                Toast.makeText(mgrbceqcheck.this, "Get Data Success-" + msg0, Toast.LENGTH_SHORT).show();
//                                break;
                        case "0":
                            Toast.makeText(mgrbceqcheck.this, "Get Data Fail-" + msg0, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mgrbceqcheck.this, "Get Data Fail-" + msg0, Toast.LENGTH_SHORT).show();
                            break;
                    }

                    break;
                case "updatebceqchecktask":
                    String status1=j1.get(0).get_valuelist().get(0).get(1).get_value();
                    String msg1=j1.get(0).get_valuelist().get(0).get(2).get_value();

                    switch (status1) {
                        case "1":
                            Toast.makeText(mgrbceqcheck.this, "Upload Success-" + msg1, Toast.LENGTH_SHORT).show();
                            ll1.removeAllViews();
                            break;
                        case "0":
                            Toast.makeText(mgrbceqcheck.this, "Upload Fail-" + msg1, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mgrbceqcheck.this, "Upload Fail-" + msg1, Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;

            }


        }
    }

    public void eqrepairenginputsubmit(View v)
    {
        /*


                圖像上傳類型
        Intent testi=new Intent(this,uploadimage.class);
        startActivityForResult(testi, 0);
         */
        /*
                type: 指引類型(0:正常輸入;1:產線誤報修輸入;2:特定機臺報修輸入;)
                abnormal: 當機原因/誤報類型
                improvement: 改善方案/當機原因
                resolvent: 解決方法
                realreason: 異常真因/使用工具
                opno: 站位
                spareflag: 是否換備品
                spareid: 備品編號
                sparenum: 備品數量
                sparemodel: 備品型號
                nozzleid: Nozzle編號
                nozzlechange: Nozzle改變
                nozzlereason: 改變原因
              */
    }
    public  void eqengrepairdatainsubmit(String urlpath,List<httprequestinputdata> li) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
        // List<httprequestinputdata> li=null;
        exechttprequest hf1=new exechttprequest();
        //Log.v("CMSF", hf1.toString());
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1,"正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        execloadactivity.canclediglog();
                        Toast.makeText(mgrbceqcheck.this,"Fail+"+paramObject.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }
}