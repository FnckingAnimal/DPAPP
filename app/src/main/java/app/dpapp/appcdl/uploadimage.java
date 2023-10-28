package app.dpapp.appcdl;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import app.dpapp.Staticdata;
import app.dpapp.parameterclass.httprequestinputdata;

/**
 * Created by Administrator on 2016/7/11.
 */
public  class uploadimage  extends Activity {


    //用户名
    private static String Sessionuser;
    protected final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
    protected  String returnstr;
    private String Sysname;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();


        Bundle bundle = this.getIntent().getExtras();
        try {
            Sysname = bundle.getString("sysname");
        }
        catch(Exception ex1)
        {
            Toast.makeText(this,"上傳文件需傳入系統名稱",Toast.LENGTH_SHORT).show();
            this.finish();

        }
        Intent getAlbum = new Intent(Intent.ACTION_PICK);
        getAlbum.setType("image/*");//相片类型
        startActivityForResult(getAlbum, IMAGE_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imagedata) {

        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            return;
        }
        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == IMAGE_CODE) {
            Uri uri = imagedata.getData();        //获得图片的uri
            if (null == uri)
                return;
            final String scheme = uri.getScheme();
            String data = null;
            if (scheme == null)
                data = uri.getPath();
            else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                data = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1) {
                            data = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            }
            File file = new File(data);

            List<httprequestinputdata> li = new ArrayList<>();
            httprequestinputdata hhi1 = new httprequestinputdata();
            hhi1.setDataname("systemname");
            hhi1.setDatavalue(Sysname);
            //hhi1.setDatavalue("CMSFMGRBCEQCHECK");
            li.add(hhi1);
            httprequestinputdata hhi2 = new httprequestinputdata();
            hhi2.setDataname("createid");
            hhi2.setDatavalue(Sessionuser);
            li.add(hhi2);
            exechttprequest hf1 = new exechttprequest();
            hf1.getDataFromServer(3, new FreedomDataCallBack() {
                @Override
                public void onStart(Context C1) {

                }

                @Override
                public void processData(Object paramObject, boolean paramBoolean) {
                    returnstr = paramObject.toString();
                }

                @Override
                public void onFinish(Context C1) {
                    callback();
                }

                @Override
                public void onFailed(Object paramObject,Context C1) {
                    Toast.makeText(uploadimage.this,"Fail-"+paramObject.toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNoneImage(Context C1) {

                }
                },this,Staticdata.httpurl+"CMSF/Receiveimage.ashx",li,file);
            //}, this, Staticdata.httpurl + "CMSF/ReceiveimageHandler.ashx", null, file);
            //},this.Staticdata.httpurl+"CMSF/ReceiveimageHandler.ashx",null,file);
        }
    }

    public void callback()
    {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", returnstr);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
