package app.dpapp.material;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.LotnoBean;
import app.dpapp.bean.MaterialjiaBean;
import app.dpapp.soap.AppleSOAP;

public class MateriallotnoinActivity extends AppCompatActivity {

    private MaterialjiaBean gv_mjb = new MaterialjiaBean();
    private String gv_lotno;

    private LotnoBean gv_lotnobean = new LotnoBean();

    private String gv_selectcangma,gv_selectjiano;

    private TextView tv_jiano, tv_jiapro, tv_jiadev, tv_jiakc,tv_cangma;
    private EditText et_lotno, et_lotnodev, et_lotnowono, et_lotnopro, et_lotnoqty;

    private Spinner sp_canama,sp_jiano;
    private ArrayAdapter<String> gv_adaptercangma,gv_adapterjiano;
    private List<String> gv_cangmalist, gv_jianolist;

    private String gv_Messgestr = "";

    private Handler  handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String lotnoInfo = (String) msg.obj;
                    if (lotnoInfo != null && lotnoInfo != "") {
                        String[] lotnoDatas = lotnoInfo.split(":");
                        if (lotnoDatas[0].equals("1")) {
                            String[] lotnostr = lotnoDatas[1].split(",");
                            gv_lotnobean.lotno = gv_lotno;
                            gv_lotnobean.deviceno = lotnostr[1];
                            gv_lotnobean.wono = lotnostr[2];
                            gv_lotnobean.productno = lotnostr[3];
                            gv_lotnobean.dieqty = lotnostr[4];

                            et_lotnodev.setText(gv_lotnobean.deviceno);
                            et_lotnowono.setText(gv_lotnobean.wono);
                            et_lotnopro.setText(gv_lotnobean.productno);
                            et_lotnoqty.setText(gv_lotnobean.dieqty);

                            //根据批号信息查找入库的物料架
                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    AppleSOAP asop = new AppleSOAP();
                                    List<String> list = asop.getMaterialjianoBypro(gv_lotnobean.productno);
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.obj = list;
                                    handler.sendMessage(msg);
                                }
                            });


                        } else {
                            gv_Messgestr = lotnoInfo;
                            Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case 1:
                    String saveresult = (String) msg.obj;
                    if (saveresult != null && saveresult != "") {
                        gv_Messgestr = saveresult;
                        Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
                    } else {
                        gv_Messgestr = "放入物料架失败";
                        Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
                    }
                    MateriallotnoinActivity.this.finish();
                    break;
                case 2:
                    gv_cangmalist = (List<String>) msg.obj;
                    if (gv_cangmalist != null) {
                        gv_adaptercangma = new ArrayAdapter<String>(MateriallotnoinActivity.this, R.layout.spinner_textview, gv_cangmalist);
                        sp_canama.setAdapter(gv_adaptercangma);

                    } else {
                        gv_Messgestr = "查询批号:" + gv_lotno + "对应的仓码失败";
                        Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 3:
                    gv_jianolist=(List<String>) msg.obj;
                    if (gv_jianolist != null) {
                        gv_adapterjiano = new ArrayAdapter<String>(MateriallotnoinActivity.this, R.layout.spinner_textview, gv_jianolist);
                        sp_jiano.setAdapter(gv_adapterjiano);
                    } else {
                        gv_Messgestr = "查询物料架失败";
                        Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 4:
                    gv_mjb= (MaterialjiaBean) msg.obj;
                    if (gv_mjb != null) {
                        tv_jiano.setText(gv_mjb.jiano);
                        tv_jiadev.setText(gv_mjb.deviceno);
                        tv_jiapro.setText(gv_mjb.productno);
                        tv_cangma.setText(gv_mjb.cangma);
                        tv_jiakc.setText(gv_mjb.qiety);

                        //查找批号的仓码信息
                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {
                                AppleSOAP asop = new AppleSOAP();
                                List<String> lv_resultlist = asop.getLotnocangma(gv_lotno,tv_cangma.getText().toString());
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = lv_resultlist;
                                handler.sendMessage(msg);
                            }
                        });

                    } else {
                        gv_Messgestr = "查询物料架信息失败";
                        Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiallotnoin);
        gv_lotno = getIntent().getStringExtra("lotno");
        tv_jiano = (TextView) findViewById(R.id.tv_jiano);
        tv_jiapro = (TextView) findViewById(R.id.tv_jiapro);
        tv_jiadev = (TextView) findViewById(R.id.tv_jiadev);
        tv_jiakc = (TextView) findViewById(R.id.tv_jiakc);
        tv_cangma = (TextView) findViewById(R.id.tv_cangma);

        et_lotno = (EditText) findViewById(R.id.et_lotno);
        et_lotno.setText(gv_lotno);

        et_lotnodev = (EditText) findViewById(R.id.et_lotnodev);
        et_lotnowono = (EditText) findViewById(R.id.et_lotnowono);
        et_lotnopro = (EditText) findViewById(R.id.et_lotnopro);
        et_lotnoqty = (EditText) findViewById(R.id.et_lotnoqty);
        sp_canama = (Spinner) findViewById(R.id.sp_canamg);
        sp_jiano=(Spinner) findViewById(R.id.sp_jiano);

        sp_canama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gv_selectcangma = sp_canama.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_jiano.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gv_selectjiano = sp_jiano.getItemAtPosition(position).toString();
                //根据选择的物料架获取物料架信息
                if(gv_selectjiano!=null){
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppleSOAP asop = new AppleSOAP();
                            MaterialjiaBean lv_mjb = asop.getMaterialjiaInfo(gv_selectjiano);
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = lv_mjb;
                            handler.sendMessage(msg);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //查找批号信息
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String lotnoInfo = asop.getLotnoInfo(gv_lotno);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = lotnoInfo;
                handler.sendMessage(msg);
            }
        });
    }

    public void bt_onclick_insubmit(View view) {
        final Staticdata app = (Staticdata) getApplication();
        if ( gv_lotnobean.productno.equals(gv_mjb.productno)) {

            //S04630125-34:RO-Y-A-03:009181002118:ARJ002:CAP57-3570:720:GD3B,G18連體X板良品--保稅,101:G1651251
            final String dataStr = gv_lotno + ":" + gv_mjb.jiano + ":" + gv_lotnobean.wono + ":" + gv_lotnobean.deviceno + ":" + gv_lotnobean.productno + ":" + gv_lotnobean.dieqty + ":" + gv_selectcangma+ ":" + app.getLoginUserID();
            MyThreadPool.pool.execute(new Runnable() {
                @Override
                public void run() {
                    AppleSOAP asop = new AppleSOAP();
                    String str1 = asop.saveLotnoData(dataStr);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = str1;
                    handler.sendMessage(msg);
                }
            });
        } else {
            gv_Messgestr = "物料架的料号与批号的不一致，入库失败";
            Toast.makeText(MateriallotnoinActivity.this, gv_Messgestr, Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    public void bt_onclick_inquxiao(View view) {
        this.finish();
    }
}
