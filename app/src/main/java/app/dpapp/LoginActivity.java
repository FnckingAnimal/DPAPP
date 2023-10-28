package app.dpapp;

/**
 * Created by Administrator on 2016/4/20.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.os.Process;


import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.androidpn.ServiceManager;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.zxing.activity.CaptureActivity;

public class LoginActivity extends Activity {

    private EditText userName, password;
    private CheckBox rem_pw, auto_login;
    private Button btn_login;
    private ImageButton btnQuit;
    private String userNameValue, passwordValue;
    private SharedPreferences sp;
    private TextView login_text_revs;
    private String loginstatus;
    private String loginsite;
    private boolean revbool;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent i = new Intent(this, Scanimage.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startService(i);
//        stopService(i);

        startAndroidPNService();
        //去除标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        // TODO: 2016/5/31 填充版本號 
        String clientcurrentversion = getVersion();
        ((TextView) findViewById(R.id.login_tex_rev)).setText(getVersion());
        //获得实例对象
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        userName = (EditText) findViewById(R.id.et_zh);
        password = (EditText) findViewById(R.id.et_mima);
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);
        //auto_login = (CheckBox) findViewById(R.id.cb_auto);
        btn_login = (Button) findViewById(R.id.btn_login);
        //btnQuit = (ImageButton)findViewById(R.id.img_btn);


        //判断记住密码多选框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            /*
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
                LoginActivity.this.startActivity(intent);

            }
            */

        }

        // 登录监听事件  现在默认为用户名为：liu 密码：123
        btn_login.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                btn_login.setClickable(false);
                execloadactivity.opendialog(LoginActivity.this, "正在執行");
                userNameValue = userName.getText().toString().toUpperCase();
                passwordValue = password.getText().toString();

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        getRemoteInfo_str();
                        //getRemoteInfo();
                        Message msg = handle.obtainMessage();
                        msg.obj = loginstatus;
                        handle.sendMessage(msg);
                    }
                }).start();

            }
        });

        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                } else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                updatenewversion();
                Message msg = handler1.obtainMessage();
                msg.obj = revbool;
                handler1.sendMessage(msg);
            }
        }).start();

    }

    private void startAndroidPNService() {
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
    }

    public void scancodelogin(View v) {
        Intent openCameraIntent = new Intent(LoginActivity.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");

            MyThreadPool.pool.execute(
                    new Runnable() {

                        @Override
                        public void run() {

                            PublicSOAP ps1 = new PublicSOAP();
                            List<String> DL3 = new ArrayList<String>();
                            DL3 = ps1.checkuserbarcode(scanResult);
                            Message msg = handle2.obtainMessage();
                            msg.what = 0;
                            msg.obj = DL3;
                            handle2.sendMessage(msg);
                        }
                    }
            );
        }
    }

    private Handler handle3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String o = msg.obj.toString();
                    String[] lo = o.split(":");
                    String o1 = lo[0];
                    String o2 = lo[1];
                    String o3 = lo[2];
                    String o4 = lo[3];
                    String o5 = lo[4];

                    if (o1.equals("0")) {
                        Toast.makeText(LoginActivity.this, o2, Toast.LENGTH_LONG).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                        return;
                    }

                    final Staticdata app = (Staticdata) getApplication();
                    app.setLoginUserID(userNameValue);
//                    app.setLOCALMACADDRESS(getLocalMac());
//                    Toast.makeText(LoginActivity.this,app.getLOCALMACADDRESS(),Toast.LENGTH_SHORT).show();

                    Staticdata.setsoapurl(o2);
                    Staticdata.usersite = o2;
                    Staticdata.type = o3;
                    Staticdata.mgr = o4;
                    Staticdata.email = o5;

                    if (!"DP".equals(o2)) {
                        Toast.makeText(LoginActivity.this, "厂区设置错误，请联系MIS更改！", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                        return;
                    }
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    btn_login.setClickable(true);

//                    if (Staticdata.type.equals("MIS")) {

                        AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                        adb.setTitle("请选择产品：");
                        final String[] itemstrs = {"APPLE", "MICROSOFT"};
                        adb.setSingleChoiceItems(itemstrs, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String select_item = itemstrs[which].toString();
                                if (select_item.equals("APPLE")) {
                                    Staticdata.type = "APPLE";
                                } else {
                                    Staticdata.type = "MICROSOFT";
                                }
                                dialog.cancel();
                                Intent intent = new Intent(LoginActivity.this, LogoActivity.class);
                                LoginActivity.this.startActivity(intent);
                            }
                        });
                        adb.show();
//                    } else {
//                        //跳转界面
//                        Intent intent = new Intent(LoginActivity.this, LogoActivity.class);
//                        LoginActivity.this.startActivity(intent);
//                    }

                    //finish();

                } else {

                }


            } catch (Exception ex) {
                execloadactivity.canclediglog();
                Toast.makeText(LoginActivity.this, "登录失敗，請聯絡MIS部門", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 獲取平板MAC地址
     */
    public static String getLocalMac() throws SocketException, UnknownHostException {
        StringBuffer sb;
        InetAddress ia = InetAddress.getLocalHost();
        byte[] mac = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        }
        sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0" + str.toUpperCase());
            } else {
                sb.append(str.toUpperCase());
            }
        }
        return sb.toString();
    }

    /**
     * 獲取平板IP
     */
    public static String getLoaclIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private Handler handle2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    List<String> pe1 = null;
                    pe1 = (List<String>) msg.obj;
                    if (pe1 == null) {
                        Toast.makeText(LoginActivity.this, "無法獲取賬號信息,請聯絡MIS", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                        return;
                    }

                    String userid = "";
                    String username = "";
                    String loginflag = "";

                    try {
                        userid = pe1.get(0).toString();
                        username = pe1.get(1).toString();
                        loginflag = pe1.get(2).toString();

                    } catch (Exception ex1) {
                        Toast.makeText(LoginActivity.this, "賬號密碼異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                        return;
                    }
                    if (!loginflag.equals("1")) {
                        Toast.makeText(LoginActivity.this, "賬號已過期，請重新打印登陸條碼", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                        return;
                    }
                    userNameValue = userid;
                    MyThreadPool.pool.execute(
                            new Runnable() {

                                @Override
                                public void run() {

                                    String ts = getRemoteInfo_site();
                                    Message msg = handle3.obtainMessage();
                                    msg.what = 0;
                                    msg.obj = ts;
                                    handle3.sendMessage(msg);
                                }
                            }
                    );

                }
            } catch (Exception ex1) {
                Toast.makeText(LoginActivity.this, "未知異常", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
                btn_login.setClickable(true);
            }
        }
    };


    public void getRemoteInfo() {

        loginstatus = "true";
    }

    public void getRemoteInfo_str() {
        // Log.i("getRemoteInfo", "into getRemoteInfo");


        String NAMESPACE = "http://tempuri.org/";

        String URL = Staticdata.userloginurl;

        String METHOD_NAME = "checkuseridauthority";
        String SOAP_ACTION = "http://tempuri.org/checkuseridauthority";


        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("userid", userNameValue);
        rpc.addProperty("password", passwordValue);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            // 调用WebService
            transport.call(SOAP_ACTION, envelope);
            SoapObject r1 = (SoapObject) envelope.bodyIn;
            loginstatus = r1.getProperty(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            loginstatus = "Fail";
        }
    }

    public String getRemoteInfo_site() {
        // Log.i("getRemoteInfo", "into getRemoteInfo");


        String NAMESPACE = "http://tempuri.org/";

        String URL = Staticdata.userloginurl;//"http://10.142.136.222:8107/Service.asmx"; //

        String METHOD_NAME = "getaccountsite";
        String SOAP_ACTION = "http://tempuri.org/getaccountsite";


        SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
        rpc.addProperty("userid", userNameValue);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            // 调用WebService
            transport.call(SOAP_ACTION, envelope);
            SoapObject r1 = (SoapObject) envelope.bodyIn;
            return r1.getProperty(0).toString();
        } catch (Exception e) {
            return "0:獲取Site信息異常";
        }
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // Log.i("handle", "into handle");
            loginstatus = msg.obj.toString();

            //无网络调试程式必须  // TODO: 2016/5/6 tod
            //loginstatus="true";
            // TODO: 2016/5/6 tod
            if (userName.getText().toString() == null || "".equals(userName.getText().toString())) {
                Toast.makeText(LoginActivity.this, "請輸入帳號", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
                btn_login.setClickable(true);
                return;
            } else if (password.getText().toString() == null || "".equals(password.getText().toString())) {
                Toast.makeText(LoginActivity.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
                btn_login.setClickable(true);
                return;
            }
            if (userName.getText().toString() != null && password.getText().toString() != null) {

                if (loginstatus.equals("true")) {

                    //登录成功和记住密码框为选中状态才保存用户信息
                    if (rem_pw.isChecked()) {
                        //记住用户名、密码、
                        Editor editor = sp.edit();
                        editor.putString("USER_NAME", userNameValue);
                        editor.putString("PASSWORD", passwordValue);
                        editor.commit();
                    }

                    MyThreadPool.pool.execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    String str = getRemoteInfo_site();
                                    Message msg = handle3.obtainMessage();
                                    msg.obj = str;
                                    msg.what = 0;
                                    handle3.sendMessage(msg);
                                }
                            }
                    );

                    //跳转界面
                    //Intent intent = new Intent(LoginActivity.this, LogoActivity.class);
                    //LoginActivity.this.startActivity(intent);
                    //finish();

                } else {
                    if (loginstatus.equals("Fail")) {
                        Toast.makeText(LoginActivity.this, "网络连线异常", Toast.LENGTH_LONG).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
                        execloadactivity.canclediglog();
                        btn_login.setClickable(true);
                    }
                }
            }

        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            android.os.Process.killProcess(Process.myPid());
//            System.exit(0);
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
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    // TODO: 2016/5/12 获取程式版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }


    public void updatenewversion() {
        try {
            String clientcurrentversion = getVersion();
            String serviceversion;

            String METHOD_NAME = "getcmsfrev";
            String[] parnames = null;
            String[] parvalues = null;
            int parcount = 0;
            PublicSOAP ps1 = new PublicSOAP();
            SoapObject rs1 = ps1.soapdal(Staticdata.NAMESPACE, Staticdata.userloginurl, METHOD_NAME, parnames, parvalues, parcount);

            if (rs1 != null) {
                serviceversion = rs1.getProperty(0).toString().trim();
            } else {
                Toast.makeText(LoginActivity.this, "版本信息获取失败",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (serviceversion != null) {
                if (!serviceversion.equals(clientcurrentversion)) {
                    revbool = true;
                } else {
                    revbool = false;
                }
            } else {
                Toast.makeText(LoginActivity.this, "版本信息获取失败",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception ex1) {
            revbool = false;
            return;
        }
    }

    // 更新版本要用到的一些信息
    //private UpdateInfo info;

    private ProgressDialog pBar;

    public void downFile(final String url) {
        pBar = new ProgressDialog(LoginActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.show();
        new Thread() {
            public void run() {

                // HttpURLConnection client=(HttpURLConnection) url.openConnection();
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    int length = (int) entity.getContentLength();   //获取文件大小
                    pBar.setMax(length);                            //设置进度条的总长度
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                "app-release.apk");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1000];   //这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            pBar.setProgress(process);       //这里就是关键的实时更新进度了！
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    void down() {
        handler1.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (revbool) {   //在下面的代码段
                showUpdateDialog();  //下面的代码段
            }
        }

        ;
    };

    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至最新版本");
        //builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {

                    downFile("http://10.151.128.228:8091/app-release.apk");     //在下面的代码段
                } else {
                    Toast.makeText(LoginActivity.this, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //安装文件，一般固定写法
    void update() {
        File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android 7.0
            Uri apkUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags (Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType (apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType (Uri.fromFile (file), "application/vnd.android.package-archive");
        }
        this.startActivity (intent);
//        File apkFile = new File(Environment.getExternalStorageDirectory(),"app-release.apk");
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(this, "app.dpapp.fileprovider", apkFile);
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        }else{
//            intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
//        }
//        try {
//            startActivity(intent);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}