package app.dpapp;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import app.dpapp.zxing.activity.CaptureActivity;

//增加SOAP组建引入
//增加异步组建引入


public class nfcitemj extends AppCompatActivity {

    protected String Eqid;
    protected String Eqname;
    protected TextView eqnametv1;
    private NfcAdapter nfcAdapter;
    String result;
    private String readResult;
    private EditText writedata;
    private Button changebutton;
    private PendingIntent mPendingIntent;


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Log.i("handle", "into handle");
            result = msg.obj.toString();
            Log.i("TAG", result);

        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcitem);
        eqnametv1 = (TextView) findViewById(R.id.nfc2textView);
        writedata = (EditText) findViewById(R.id.nfcitem_nfc2_edittext);
        changebutton = (Button) findViewById(R.id.nfcitem_nfc1_button);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            //nfcTView.setText("设备不支持NFC！");
            finish();
            return;
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            //nfcTView.setText("请在系统设置中先启用NFC功能！");
            finish();
            return;
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        final Staticdata app = (Staticdata) getApplication();

        if (app.getLoginUserID() == null) {

            //isTopActivy("LoginActivity");
            if(isTopActivy("ComponentInfo{app.cmapp/app.cmapp.LoginActivity}")) {
                Toast.makeText(this, "请先登陆系统", Toast.LENGTH_SHORT).show();
                finish();
                app.setLoginUserID(null);
                return;
            }
            else
            {

                    Toast.makeText(this, "请先登陆系统CMAPP", Toast.LENGTH_SHORT).show();
                    app.setLoginUserID(null);
                    finish();
                    return;
            }
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {

            if (!app.getNFCWRB()) {
                readFromTag(getIntent());
            } else {
                Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Ndef ndef = Ndef.get(tag);
                try {
                    ndef.connect();

                    //NdefRecord ndefRecord=createTextRecord(data,Locale.US,true);
                    NdefRecord ndefRecord = createTextRecord(app.getWriteNFCDATA());
                    NdefRecord[] records = {ndefRecord};
                    NdefMessage ndefMessage = new NdefMessage(records);
                    ndef.writeNdefMessage(ndefMessage);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


    }

    public NdefRecord createTextRecord(String text) {
        //生成语言编码的字节数组，中文编码
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(
                Charset.forName("US-ASCII"));
        //将要写入的文本以UTF_8格式进行编码
        Charset utfEncoding = Charset.forName("UTF-8");
        //由于已经确定文本的格式编码为UTF_8，所以直接将payload的第1个字节的第7位设为0
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = 0;
        //定义和初始化状态字节
        char status = (char) (utfBit + langBytes.length);
        //创建存储payload的字节数组
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置状态字节
        data[0] = (byte) status;
        //设置语言编码
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置实际要写入的文本
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        //根据前面设置的payload创建NdefRecord对象
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }

    private boolean readFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if (mNdefRecord != null) {
                readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                eqnametv1.setText(readResult);

                finish();

                String checkintenttemp1 = null;
                try {
                    readResult = readResult.substring(3, readResult.length());
                    checkintenttemp1 = readResult.substring(0, 1);
                } catch (Exception e) {
                    Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT);
                    return false;
                }


                final Staticdata app = (Staticdata) getApplication();
                app.setTempeqlot(readResult);



                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ;
        return false;
    }

    //判定是否当前打开的页面
    public boolean isTopActivy(String cmdName) {
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if (null != runningTaskInfos) {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
        }
        if (null == cmpNameTemp) return false;
        return cmpNameTemp.equals(cmdName);
    }

    public String isTopStrActivy(String cmdName) {
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if (null != runningTaskInfos) {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
        }

        return cmpNameTemp.toString();
    }


    public boolean isRunActivy(String cmdName) {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.cyberblue.iitag";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    public void rednfc(View v) {
        //onResume();
        final Staticdata app = (Staticdata) getApplication();
        if (app.getNFCWRB()) {
            app.setNFCWRB(false);
            changebutton.setText("读取");
        } else {
            app.setNFCWRB(true);
            changebutton.setText("写入");
            Intent openCameraIntent = new Intent(nfcitemj.this, CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            writedata.setText(scanResult);
            final Staticdata app = (Staticdata) getApplication();
            app.setWriteNFCDATA(scanResult);
        }
    }
}