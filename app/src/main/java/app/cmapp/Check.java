package app.cmapp;/**
 * Created by F5460007 on 2017/6/22.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

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

import app.dpapp.soap.PublicSOAP;


/**
 * Owner:S7202916
 * CreateDate:2017/6/22 14:16
 * 聯網更新工具類
 */
public class Check {
    private Context contect;
    private boolean revbool;
    public Check(Context context,final boolean revbool) {
       this.contect = context;
        this.revbool = revbool;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (revbool) {   //在下面的代码段
                showUpdateDialog();  //下面的代码段
            }
        }
    };

    // 更新版本要用到的一些信息
    //private UpdateInfo info;

    private ProgressDialog pBar;

    void downFile(final String url) {
        pBar = new ProgressDialog(contect);    //进度条，在下载的时候实时更新进度，提高用户友好度
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
        handler.post(new Runnable() {
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
        contect.startActivity(intent);
    }

    public void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(contect);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至最新版本");
        //builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile("http://10.151.128.97:305/AppDownload/app-release.apk");     //在下面的代码段
                } else {
                    Toast.makeText(contect, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.create().show();
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
                Toast.makeText(contect, "版本信息获取失败",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!serviceversion.equals(clientcurrentversion)) {
                revbool = true;

            } else {
                revbool = false;

            }
        }
        catch(Exception ex1)
        {
            revbool=false;
            return;
        }
    }

    // TODO: 2016/5/12 获取程式版本号
    public  String getVersion() {
        try {
            PackageManager packageManager = contect.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    contect.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

}