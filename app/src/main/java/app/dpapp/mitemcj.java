package app.dpapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import app.dpapp.E4SFC.e4devicenomap;
import app.dpapp.IncludingSFC.LotnoBondMachineActivity;
import app.dpapp.IncludingSFC.QCFixtureActivity;
import app.dpapp.IncludingSFC.devicenomap;
import app.dpapp.IncludingSFC.expendable;
import app.dpapp.IncludingSFC.mcmaterialshou;
import app.dpapp.Repair.ShelfActivity;
import app.dpapp.Repair.ZaiBanActivity;
import app.dpapp.Repair.eqrepairexec;
import app.dpapp.Repair.eqrepairtask;
import app.dpapp.Reserve.rsstrocklist;
import app.dpapp.Reserve.rsstrocktask;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.appleeol.EOLStagingTimeWIPActivty;
import app.dpapp.material.ElectronicActivity;
import app.dpapp.material.MaterialListActivity;
import app.dpapp.material.MaterialLotnoListActivity;
import app.dpapp.material.MaterialSelectTypeActivity;
import app.dpapp.material.MateriallistnooutActivity;
import app.dpapp.material.MateriallotnoinActivity;
import app.dpapp.material.SMTSapKZ;
import app.dpapp.ra.Rel_Billboardsystem_Menu_Activity;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.machinecheck.eqchecktask;
import app.dpapp.machinecheck.machine_MP;
import app.dpapp.machinecheck.machine_offline;
import app.dpapp.mgrbc.mgrbceqcheck;
import app.dpapp.zxing.activity.CaptureActivity;
import app.dpapp.IncludingSFC.mcmaterialcall;
import app.dpapp.OtherForm.jcwaferiqcinspectionreport;

//增加SOAP组建引入
//增加异步组建引入


public class mitemcj extends AppCompatActivity {

    protected EditText testsoapet;
    String result;
    protected String Sessionuser;
    private String shortmapid;
    private ImageButton eqcheckib;
    private TextView eqchecktv;
    private ImageButton eqcheckreportib;
    private TextView eqcheckreporttv;
    private ImageButton eqchecktaskib;
    private TextView eqchecktasktv;

    private ImageButton eqrepairib;
    private TextView eqrepairtv;
    private ImageButton eqrepairtaskib;
    private TextView eqrepairtasktv;
    private ImageButton eqrepairenginib;
    private TextView eqrepairengintv;

    private ImageButton sfclotib;
    private TextView sfclottv;

    private ImageButton rsstockmib;
    private TextView rsstockmtv;
    private ImageButton rsstrocktaskib;
    private TextView rsstrocktasktv;

    private ImageButton eqsettingib;
    private TextView eqsettingtv;
    private ImageButton eqsettingcheckib;
    private TextView eqsettingchecktv;

    private ImageButton eqcheckpmib;
    private TextView eqcheckpmtv;
    private ImageButton eqcheckpmreportib;
    private TextView eqcheckpmreporttv;
    private ImageButton eqcheckpmtaskib;
    private TextView eqcheckpmtasktv;

    private ImageButton eqcheckforms;
    private TextView eqcheckformswi;

    private ImageButton calloffmaterialib;
    private TextView calloffmaterialtv;

    private ImageButton iqcreportib;
    private TextView iqcreporttv;

    private ImageButton lhsfconlineib;
    private TextView lhsfconlinetv;

    private ImageButton machineoutib;
    private TextView machineouttv;

    private ImageButton machinedefectib;
    private TextView machinedefecttv;

    private ImageButton output_dayib;
    private TextView output_daytv;

    private ImageButton pyscheckib;
    private TextView pyschecktv;

    private ImageButton eqcheckofflineib;
    private TextView eqcheckofflinetv;

    private TextView tvcheckmaintain;
    private TextView tvmachine;
    private TextView tvsfc;
    private TextView tvspares;
    private TextView tvasset;
    private boolean revbool;

    private ImageButton mImageButton;
    private TextView mTextView;

    private ImageButton AAButton;
    private TextView AATextView;

    /**
     * SFC獲取ERP單據信息按鈕
     */
    ImageButton geterpdataib;
    /**
     * 獲取ERP單據信息文字信息
     */
    TextView geterpdatatv;


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            result = msg.obj.toString();

            testsoapet.setText(result);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See                                         for more information.
     */
    private GoogleApiClient client;
    private Staticdata sc;
    private String su;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mitemc);
        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

        sc = new Staticdata();
        su = sc.usersite;
        eqcheckib = (ImageButton) findViewById(R.id.mitemc_button4);
        eqchecktv = (TextView) findViewById(R.id.mitemc_tv4);
        eqcheckreportib = (ImageButton) findViewById(R.id.mitemc_button5);
        eqcheckreporttv = (TextView) findViewById(R.id.mitemc_tv5);
        eqchecktaskib = (ImageButton) findViewById(R.id.mitemc_button6);
        eqchecktasktv = (TextView) findViewById(R.id.mitemc_tv6);

        eqrepairib = (ImageButton) findViewById(R.id.mitemcbutton_eqrepair);
        eqrepairtv = (TextView) findViewById(R.id.mitemctv_eqrepair);
        eqrepairtaskib = (ImageButton) findViewById(R.id.mitemcbutton_eqrepairtask);
        eqrepairtasktv = (TextView) findViewById(R.id.mitemctv_eqrepairtask);
        eqrepairenginib = (ImageButton) findViewById(R.id.mitemcbutton_eqrepairengdatain);
        eqrepairengintv = (TextView) findViewById(R.id.mitemctv_eqrepairengdatain);

        sfclotib = (ImageButton) findViewById(R.id.mitemcbutton_lot);
        sfclottv = (TextView) findViewById(R.id.mitemctv_lot);

        rsstockmib = (ImageButton) findViewById(R.id.mitemcbutton_stocklocation);
        rsstockmtv = (TextView) findViewById(R.id.mitemctv_stocklocation);
        rsstrocktaskib = (ImageButton) findViewById(R.id.mitemcbutton_stockengtask);
        rsstrocktasktv = (TextView) findViewById(R.id.mitemctv_stockengtask);

        eqsettingib = (ImageButton) findViewById(R.id.mitemcbutton_eqbinddata);
        eqsettingtv = (TextView) findViewById(R.id.mitemctv_eqbinddata);
        eqsettingcheckib = (ImageButton) findViewById(R.id.mitemcbutton_mgrbceqcheck);
        eqsettingchecktv = (TextView) findViewById(R.id.mitemctv_mgrbceqcheck);

        eqcheckpmib = (ImageButton) findViewById(R.id.mitemcbutton_pm);
        eqcheckpmtv = (TextView) findViewById(R.id.mitemctv_pm);
        eqcheckpmreportib = (ImageButton) findViewById(R.id.mitemcbutton_pmsearch);
        eqcheckpmreporttv = (TextView) findViewById(R.id.mitemc_pmsearch);
        eqcheckpmtaskib = (ImageButton) findViewById(R.id.mitemcbutton_pmtask);
        eqcheckpmtasktv = (TextView) findViewById(R.id.mitemc_pmtask);

        eqcheckforms = (ImageButton) findViewById(R.id.mitemcbutton_wi);
        eqcheckformswi = (TextView) findViewById(R.id.mitemctv_wi);

        calloffmaterialib = (ImageButton) findViewById(R.id.mitemcbutton_calloff);
        calloffmaterialtv = (TextView) findViewById(R.id.mitemctv_material);

        iqcreportib = (ImageButton) findViewById(R.id.mitemcbutton_iqc);
        iqcreporttv = (TextView) findViewById(R.id.mitemctv_iqc1);

        geterpdataib = (ImageButton) findViewById(R.id.mitemcbutton_erp);
        geterpdatatv = (TextView) findViewById(R.id.mitemctv_erp);

        lhsfconlineib = (ImageButton) findViewById(R.id.mitemcbutton_lhsfconine);
        lhsfconlinetv = (TextView) findViewById(R.id.mitemctv_lhsfconine);

        tvcheckmaintain = (TextView) findViewById(R.id.tv_checkmaintain);
        tvmachine = (TextView) findViewById(R.id.tv_machine);
        tvsfc = (TextView) findViewById(R.id.tv_sfc);
        tvspares = (TextView) findViewById(R.id.tv_spares);
        tvasset = (TextView) findViewById(R.id.tv_asset);

        mImageButton = (ImageButton) findViewById(R.id.ib_new_method);
        mTextView = (TextView) findViewById(R.id.tv_new_method);

        machineoutib = (ImageButton) findViewById(R.id.ib_output);
        machineouttv = (TextView) findViewById(R.id.tv_output);

        machinedefectib = (ImageButton) findViewById(R.id.ib_unusual);
        machinedefecttv = (TextView) findViewById(R.id.tv_unusual);

        eqcheckofflineib = (ImageButton) findViewById(R.id.mitemc_button14);
        eqcheckofflinetv = (TextView) findViewById(R.id.mitemc_tv14);

        AAButton = (ImageButton) findViewById(R.id.ib_new_method);
        AATextView = (TextView) findViewById(R.id.tv_new_method);

        output_dayib = (ImageButton) findViewById(R.id.ib_output_day);
        output_daytv = (TextView) findViewById(R.id.tv_output_day);

        pyscheckib = (ImageButton) findViewById(R.id.mitemcbutton_mgrbceqcheck_pys);
        pyschecktv = (TextView) findViewById(R.id.mitemctv_mgrbceqcheck_pys);

//        loadform();

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

    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (revbool) {   //在下面的代码段
                showUpdateDialog();  //下面的代码段
            }
        }
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
                    Toast.makeText(mitemcj.this, "SD卡不可用，请插入SD卡",
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

    // 更新版本要用到的一些信息
    //private UpdateInfo info;

    private ProgressDialog pBar;

    void downFile(final String url) {
        pBar = new ProgressDialog(this);    //进度条，在下载的时候实时更新进度，提高用户友好度
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

    //安装文件，一般固定写法
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "app-release.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public void updatenewversion() {
        try {
            String clientcurrentversion = getVersion();
            String serviceversion = null;

            String METHOD_NAME = "getcmsfrev";
            String[] parnames = null;
            String[] parvalues = null;
            int parcount = 0;
            PublicSOAP ps1 = new PublicSOAP();
            SoapObject rs1 = ps1.soapdal(Staticdata.NAMESPACE, Staticdata.userloginurl, METHOD_NAME, parnames, parvalues, parcount);

            if (rs1 != null) {
                serviceversion = rs1.getProperty(0).toString().trim();
            } else {
                Toast.makeText(this, "版本信息获取失败",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!serviceversion.equals(clientcurrentversion)) {
                revbool = true;

            } else {
                revbool = false;

            }
        } catch (Exception ex1) {
            revbool = false;
            return;
        }
    }

    // TODO: 2016/5/12 获取程式版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }


    protected void onResume() {
        super.onResume();

    }

    public void sfctest(View v) {
        Intent intentr = new Intent(this, expendable.class);
        Bundle bundler = new Bundle();
        bundler.putString("deviceno", "BC-L");
        bundler.putString("newdeviceno", "ANF002");
        bundler.putString("odbname", "BCLCONN");
        bundler.putString("lotno", "S98765432-02");
        bundler.putString("opno", "0045_1");
        bundler.putString("productno", "CAP47-5207");
        intentr.putExtras(bundler);
        startActivity(intentr);
    }

    public void new_function(View v) {
        Intent intent = new Intent(this, NewFunctionActivity.class);
        startActivity(intent);
    }

    public void unusual(View v) {
        shortmapid = "unusual";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    public void output(View v) {
        shortmapid = "output";
        Intent intent = new Intent(this, KanBanWebActivity.class);
        startActivityForResult(intent, 0);
    }

    public void output2(View v) {
        shortmapid = "output2";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    public void zaiban_onclick(View v) {
        Intent intent = new Intent(this, ZaiBanActivity.class);
        startActivity(intent);
    }

    public void scanclick(View v) {
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void lottrace_click(View v) {
        shortmapid = "SFCLOTTRACE";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void reservestrock_click(View v) {
        shortmapid = "RESTOCKLOCATION";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void reserveengtask_click(View v) {
        shortmapid = "RESTOCKENGTASK";
        Intent openCameraIntent = new Intent(this, rsstrocktask.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void eqcheckoffline(View v) {
        Intent openCameraIntent = new Intent(this, machine_offline.class);
        startActivity(openCameraIntent);
    }

    public void scanclick1(View v) {
        Intent openCameraIntent = new Intent(this, nfcitemj.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void servicetest(View v) {
        Intent PublicLoading = new Intent(this, PublicLoading.class);

        startActivity(PublicLoading);
    }


    public void lhsfconlineclick(View v) {

        if (Staticdata.type.equals("APPLE")) {
            Intent PublicLoading = new Intent(this, devicenomap.class);
            Bundle bundler = new Bundle();
            bundler.putString("userid", Sessionuser);
            PublicLoading.putExtras(bundler);
            startActivity(PublicLoading);
        }

        if (Staticdata.type.equals("MICROSOFT")) {
            Intent PublicLoading = new Intent(this, e4devicenomap.class);
            Bundle bundler = new Bundle();
            bundler.putString("userid", Sessionuser);
            PublicLoading.putExtras(bundler);
            startActivity(PublicLoading);

        }
    }

    // 點檢核查介面，晉城跳到緩存界面，龍華跳到點檢保養
    public void onclick(View v) {
        Intent intent = null;
        if ("DP".equals(su)) {
            intent = new Intent(this, CheckNewActivity.class);
        }
        startActivity(intent);
    }

    public void mponclick(View v) {
        checkMP(Sessionuser);
    }

    // 判断是否有MP 点检权限
    public void checkMP(final String userid) {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                String str = ps1.checkuserauthority(userid);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = str;
                mHandler.sendMessage(msg);
            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String str = (String) msg.obj;
                if ("true".equals(str)) {
                    Intent intent = new Intent(mitemcj.this, machine_MP.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mitemcj.this, "没有权限，请联系MIS！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    public void mfglinkeqcheck(View v) {

        shortmapid = "MFGEQCHECK";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void linkeqcheckrepory(View v) {
        shortmapid = "EQCHECKREPORT";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    // TODO: 2016/6/21 未完成，依任務列表生成
    public void linkeqchecktask(View v) {
        shortmapid = "EQCHECKREPORT";
        Intent openCameraIntent = new Intent(this, eqchecktask.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void linkeqrepair(View v) {
        shortmapid = "EQREPAIR";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);

    }

    public void linkeqrepairtask(View v) {
        shortmapid = "EQREPAIRTASK";
        Intent openCameraIntent = new Intent(this, eqrepairtask.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void onmgrbceqcheckclick(View v) {
        shortmapid = "MGRBCEQCHECK";
        Intent openCameraIntent = new Intent(this, mgrbceqcheck.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void linkeqrepairengdatain(View v) {
        shortmapid = "EQREPAIRENGDATAIN";
        Intent openCameraIntent = new Intent(this, systest.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void bindeqbasedata(View v) {
        shortmapid = "EQBASEDATA";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);

    }

    public void onClickFixture(View v) {
        Intent fixtureIntent = new Intent(this, QCFixtureActivity.class);
        startActivity(fixtureIntent);
    }

    public void mfglinkeqpm(View v) {
        shortmapid = "MFGEQPM";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void linkeqpmrepory(View v) {
        shortmapid = "EQPMREPORT";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void linkeqpmtask(View v) {
        shortmapid = "EQPMREPORT";
        Intent openCameraIntent = new Intent(this, eqchecktask.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void linkeqcheckforms(View v) {
        shortmapid = "MFGEQCHECKFORMS";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);

    }

    public void calloffmaterial_click(View v) {
        shortmapid = "MFGCALLMATERIAL";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
//        Intent intenterp = new Intent(this, mcmaterialcall.class);
//        Bundle bundleerp = new Bundle();
//        bundleerp.putString("Eqid", "M20210120-034");
//
//        intenterp.putExtras(bundleerp);
//        startActivityForResult(intenterp, 1);
    }

    public void erpissueorder_click(View v) {
        shortmapid = "ERPISSUEORDER";
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void electronicOnClick(View v) {
        Intent botIntent = new Intent(this, ElectronicActivity.class);
        startActivity(botIntent);
    }


    public void waferiqcreport_click(View v) {
        shortmapid = "WAFERIQCREPORT";
        Intent openCameraIntent = new Intent(this, jcwaferiqcinspectionreport.class);
        startActivityForResult(openCameraIntent, 0);

    }

    public void onPhyClick(View v) {
        shortmapid = "PYSSYSTEM";
        Intent openCameraIntent = new Intent(this, PYSSystemActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void onTest(View v) {
        Intent openCameraIntent = new Intent(this, TestActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    public void output_click(View v) {
        shortmapid = "outputcheckout";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onClickSMTRK(View v) {
        shortmapid = "SMTRK";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
//        Intent intentmaterialjia = new Intent(this, MateriallotnoinActivity.class);
//        intentmaterialjia.putExtra("lotno", "S10530130-29");
//        startActivity(intentmaterialjia);
    }

    public void onClickSMTCK(View v) {
        shortmapid = "SMTCK";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
//        Intent intentmaterialjia = new Intent(this, MateriallistnooutActivity.class);
//        intentmaterialjia.putExtra("stocklistno", "DPG1_202012210010");
//        startActivity(intentmaterialjia);
    }

    public void onClicksmtsapkz(View v) {
        Intent intentmaterialjia = new Intent(this, SMTSapKZ.class);
        startActivity(intentmaterialjia);
    }

    public void onClickTypSelect(View v) {
        Intent intentmaterialjia = new Intent(this, MaterialSelectTypeActivity.class);
        startActivity(intentmaterialjia);
    }

    public void onClickMaterialLotnoSearch(View view) {
        Intent intent = new Intent(this, MaterialLotnoListActivity.class);
        startActivity(intent);
    }

    public void onClickMaterialSearch(View view) {
        Intent intent = new Intent(this, MaterialListActivity.class);
        startActivity(intent);
    }

    public void onClickEOLStagingTimeWIP(View view) {
        Intent intent = new Intent(this, EOLStagingTimeWIPActivty.class);
        startActivity(intent);
    }

    public void lotnobondmachine_click(View view) {
        shortmapid = "lotnobondmachine";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    public void machineshelf_click(View view) {
        Intent intent = new Intent(this, ShelfActivity.class);
        startActivity(intent);
    }

    public void onClick_Rel_billboardsystem_menu(View view) {
        Intent intent = new Intent(this, Rel_Billboardsystem_Menu_Activity.class);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            String checkintenttemp1 = null;
            if (scanResult == null) {
                //保持为当前页面
                checkintenttemp1 = "Current";
            } else {
                checkintenttemp1 = scanResult.substring(0, 1);
            }
            switch (shortmapid) {
                case "MFGEQCHECK":
                    Intent intent = new Intent(this, eqcheck.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Eqid", scanResult);
                    bundle1.putString("checktype", "0");
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    break;
                case "MFGEQCHECKFORMS":

                    if ("DP".equals(su)) {
                        Intent intentforms = new Intent(this, eqcheckforms.class);
                        Bundle bundleforms = new Bundle();
                        bundleforms.putString("Eqid", scanResult);

                        intentforms.putExtras(bundleforms);
                        startActivity(intentforms);
                    }

                    break;
                case "EQCHECKREPORT":
                    Intent intentr = new Intent(this, eqcheckreportlist.class);
                    Bundle bundler = new Bundle();
                    bundler.putString("Eqid", scanResult);
                    bundler.putString("checktype", "0");
                    intentr.putExtras(bundler);
                    startActivity(intentr);
                    break;
                case "EQREPAIR":
                    Intent intenter = new Intent(this, eqrepairexec.class);
                    Bundle bundleer = new Bundle();
                    bundleer.putString("Eqid", scanResult);
                    intenter.putExtras(bundleer);
                    startActivity(intenter);
                    break;
                case "EQBASEDATA":
                    Intent intenteqbinddata = new Intent(this, eqbasedata.class);
                    Bundle bundleeqbinddata = new Bundle();
                    bundleeqbinddata.putString("Eqid", scanResult);
                    intenteqbinddata.putExtras(bundleeqbinddata);
                    startActivity(intenteqbinddata);
                    break;
                case "output2":
                case "output":
                    Intent intentOutput2 = new Intent(this, Output.class);
                    intentOutput2.putExtra("machinenoid", scanResult);
                    startActivity(intentOutput2);
                    break;
                case "SFCLOTTRACE":
                    switch (checkintenttemp1) {
                        case "K":
                            Intent intent2 = new Intent(mitemcj.this, lottrace.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("Lot", scanResult);
                            intent2.putExtras(bundle2);
                            startActivity(intent2);
                            break;
                        case "A":
                        case "P":
                        case "S":
                            Intent intent3 = new Intent(mitemcj.this, lottrace.class);
                            Bundle bundle3 = new Bundle();
                            switch (scanResult.substring(1, 2)) {
                                case "K":
                                    bundle3.putString("Lot", scanResult.substring(1, scanResult.length()));
                                    break;
                                default:
                                    bundle3.putString("Lot", scanResult);
                                    break;
                            }
                            intent3.putExtras(bundle3);
                            startActivity(intent3);
                            break;
                    }
                    break;
                default:
                    break;

                case "RESTOCKLOCATION":
                    Intent intentrestocklocation = new Intent(mitemcj.this, rsstrocklist.class);
                    Bundle bundlerestocklocation = new Bundle();
                    bundlerestocklocation.putString("Rsid", scanResult);//傳入儲位ID
                    intentrestocklocation.putExtras(bundlerestocklocation);
                    startActivity(intentrestocklocation);
                    break;
                case "MFGEQPM":
                    Intent intentpm = new Intent(this, eqcheck.class);
                    Bundle bundlepm = new Bundle();
                    bundlepm.putString("Eqid", scanResult);
                    bundlepm.putString("checktype", "1");
                    intentpm.putExtras(bundlepm);
                    startActivity(intentpm);
                    break;
                case "EQPMREPORT":
                    Intent intentrpmr = new Intent(this, eqcheckreportlist.class);
                    Bundle bundlerpmr = new Bundle();
                    bundlerpmr.putString("Eqid", scanResult);
                    bundlerpmr.putString("checktype", "1");
                    intentrpmr.putExtras(bundlerpmr);
                    startActivity(intentrpmr);
                    break;
                case "MFGCALLMATERIAL":
                    Intent intentmaterial = new Intent(this, mcmaterialcall.class);
                    Bundle bundlematerial = new Bundle();
                    bundlematerial.putString("Eqid", scanResult);

                    intentmaterial.putExtras(bundlematerial);
                    startActivityForResult(intentmaterial, 1);
                    break;
                case "ERPISSUEORDER":
                    Intent intenterp = new Intent(this, mcmaterialshou.class);
                    Bundle bundleerp = new Bundle();
                    bundleerp.putString("Eqid", scanResult);

                    intenterp.putExtras(bundleerp);
                    startActivityForResult(intenterp, 1);
                    break;
                case "unusual":
                    Intent intentUnusual = new Intent(this, Unusual.class);
                    intentUnusual.putExtra("lotno", scanResult);
                    startActivity(intentUnusual);
                    break;
                case "outputcheckout":
                    Intent intentOutputCheck = new Intent(this, OutputCheckOut.class);
                    intentOutputCheck.putExtra("machinenoid", scanResult);
                    startActivity(intentOutputCheck);
                    break;
                case "SMTRK":
                    Intent intentSMTRK = new Intent(this, MateriallotnoinActivity.class);
                    intentSMTRK.putExtra("lotno", scanResult);
                    startActivity(intentSMTRK);
                    break;
                case "SMTCK":
                    Intent intentSMTCK = new Intent(this, MateriallistnooutActivity.class);
                    intentSMTCK.putExtra("stocklistno", scanResult);
                    startActivity(intentSMTCK);
                    break;

                case "lotnobondmachine":
                    Intent intentLBM = new Intent(this, LotnoBondMachineActivity.class);
                    intentLBM.putExtra("lotno", scanResult);
                    startActivity(intentLBM);
                    break;
            }
            shortmapid = "";
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("系统提示")
                    .setMessage("确定要退出系统吗").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        //獲取網絡狀態管理器
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            //cm.getActiveNetworkInfo().isAvailable();
            //簡歷網絡數組
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    //判斷獲得的網絡狀態是否是出於連接狀態
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

        }
        return false;
    }


}