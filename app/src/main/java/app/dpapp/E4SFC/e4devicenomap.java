package app.dpapp.E4SFC;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.E4DevicenoBean;
import app.dpapp.soap.MSSFCSOAP;
import app.dpapp.staticBianliang.e4DevicenoStaticdata;

/*
*机种选择页面
* ByLyh
* 2019-09-23
*/
public class e4devicenomap extends AppCompatActivity {

    private Button bt_submit;
    private Spinner sp_deviceno;

    private ArrayAdapter As1;
    private String deviceno;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    List<String> ls = new ArrayList<>();
                    ls = (List<String>) msg.obj;
                    if (ls == null) {
                        return;
                    }
                    As1 = new ArrayAdapter<>(e4devicenomap.this, R.layout.spinner_textview, ls);
                    sp_deviceno.setAdapter(As1);
                }
            } catch (Exception ex1) {

            }
        }
    };

    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 0) {
                    List<E4DevicenoBean> ls1 = new ArrayList<>();
                    ls1 = (List<E4DevicenoBean>) msg.obj;
                    if (ls1 == null) {
                        return;
                    }
                    initdevicenoinfo(ls1);
                }
            } catch (Exception ex1) {

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e4devicenomap);
        sp_deviceno= (Spinner) findViewById(R.id.sp_deviceno);

        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                MSSFCSOAP sfcsoap = new MSSFCSOAP();
                List<String> ls = new ArrayList<>();
                ls = sfcsoap.sfc_getdeviceno(Staticdata.type);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ls;
                handler.sendMessage(msg);
            }
        });

        sp_deviceno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_deviceno.getSelectedItem() != null && !"".equals(sp_deviceno.getSelectedItem())) {
                    deviceno = sp_deviceno.getSelectedItem().toString();
                    Toast.makeText(e4devicenomap.this, deviceno, Toast.LENGTH_LONG).show();
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            MSSFCSOAP sfcsoap = new MSSFCSOAP();
                            List<E4DevicenoBean> ls1 = new ArrayList<>();
                            ls1 = sfcsoap.sfc_getdevicenoInfo(deviceno, Staticdata.type);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = ls1;
                            handler1.sendMessage(msg);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void btsubmit_Click(View view){
        //点击进入，根据机种表初始化e4DevicenoStaticData中的机种信息，跳转页面


        //跳转页面
        Intent onLinemapIntent = new Intent(this, OnlinemapActivity.class);
        startActivity(onLinemapIntent);

    }

    private void initdevicenoinfo(List<E4DevicenoBean> ls) {
        E4DevicenoBean devicenoInfo=ls.get(0);
        e4DevicenoStaticdata.deviceno=devicenoInfo.deviceno;
        e4DevicenoStaticdata.dblinkstr=devicenoInfo.dblinkstr;
        e4DevicenoStaticdata.lotnosubflag=devicenoInfo.lotnosubflag;

    }
}
