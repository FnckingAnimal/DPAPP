package app.cmapp.Reserve;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import app.cmapp.zxing.activity.CaptureActivity;
import app.dpapp.R;

//增加SOAP组建引入
//增加异步组建引入


public class ritemmapj extends AppCompatActivity {


    String result;

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            result = msg.obj.toString();

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            String checkintenttemp1=null;
            if(scanResult==null)
            {
                //保持为当前页面
                checkintenttemp1="Current";
            }
            else {
                checkintenttemp1 = scanResult.substring(0, 1);
            }
                    Intent intent4 = new Intent(ritemmapj.this, rsstrocklist.class);
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("Rsid", scanResult);//傳入儲位ID
                    intent4.putExtras(bundle4);
                    startActivity(intent4);
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ritemmap);
    }

    public void linkeqcheck(View v) {

        Intent openCameraIntent = new Intent(ritemmapj.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }


}

