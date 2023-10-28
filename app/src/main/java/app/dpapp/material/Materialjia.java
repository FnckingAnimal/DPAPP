package app.dpapp.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.MaterialjiaBean;
import app.dpapp.bean.MaterialjiaLotnoBean;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.zxing.activity.CaptureActivity;

public class Materialjia extends Activity {

    private MaterialjiaBean gv_mjb = new MaterialjiaBean();
    private String gv_jiano, gv_lotno, jiaqty;
    private EditText et_jiano, et_dev, et_lc, et_wljcs, et_pro, et_qty;
    private Spinner sp_lotno;
    private ListView Lv_materialjialotno;

    private TextView tv_result;

    private List<String> list_lotno;
    private List<MaterialjiaLotnoBean> list_materialjialotno;

    private ArrayAdapter<String> adapter_lotno;
    private MateriallotnoAdapter adapter_mw;

    private String shortmapid = "";
    private String gv_activityResultStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialjia);
        gv_jiano = getIntent().getStringExtra("jiano");

        et_jiano = (EditText) findViewById(R.id.et_mjno);
        et_dev = (EditText) findViewById(R.id.et_dev);
        et_lc = (EditText) findViewById(R.id.et_lc);
        et_wljcs = (EditText) findViewById(R.id.et_wljcs);
        et_pro = (EditText) findViewById(R.id.et_pro);
        et_qty = (EditText) findViewById(R.id.et_qty);


        tv_result = (TextView) findViewById(R.id.tv_result);

        sp_lotno = (Spinner) findViewById(R.id.sp_lotnoinfo);
        list_lotno = new ArrayList<>();

        Lv_materialjialotno = (ListView) findViewById(R.id.lv_materialjialotno);
        list_materialjialotno = new ArrayList<>();


        sp_lotno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gv_lotno = sp_lotno.getItemAtPosition(position).toString();
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        AppleSOAP asop = new AppleSOAP();
                        List<MaterialjiaLotnoBean> ls2 = new ArrayList<>();
                        ls2 = asop.getMaterialjiaLotnoData(gv_lotno, gv_jiano);
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = ls2;
                        handler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initView(gv_jiano);
    }

    private Handler handler = new Handler() {
        // TODO Auto-generated method stub
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    gv_mjb = (MaterialjiaBean) msg.obj;
                    if (gv_mjb != null) {
                        et_dev.setText(gv_mjb.deviceno);
                        et_lc.setText(gv_mjb.buildingno);
                        et_wljcs.setText(gv_mjb.cengshu);
                        et_pro.setText(gv_mjb.productno);
                        et_qty.setText(gv_mjb.qiety);

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {
                                AppleSOAP asop = new AppleSOAP();
                                List<String> list1 = asop.getMaterialjiaLotno(gv_jiano);
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = list1;
                                handler.sendMessage(msg);
                            }
                        });
                    } else {
                        Toast.makeText(Materialjia.this, "查询物料架信息失败", Toast.LENGTH_LONG).show();
                    }

                    break;
                case 1:
                    list_lotno = (List<String>) msg.obj;
                    if (list_lotno != null && list_lotno.size() > 0) {
                        adapter_lotno = new ArrayAdapter<String>(Materialjia.this, R.layout.spinner_textview, list_lotno);
                        sp_lotno.setAdapter(adapter_lotno);
                    } else {
                        Toast.makeText(Materialjia.this, "查询物料架批号失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    list_materialjialotno = (List<MaterialjiaLotnoBean>) msg.obj;
                    if (list_materialjialotno != null) {
                        adapter_mw = new MateriallotnoAdapter(Materialjia.this, list_materialjialotno);
                        Lv_materialjialotno.setAdapter(adapter_mw);
                    } else {
                        Toast.makeText(Materialjia.this, "查询物料架上批号信息失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    String lv_messgeStr= (String) msg.obj;
                    tv_result.setText(lv_messgeStr);
                default:
                    break;
            }
        }
    };

    private void initView(String jiano) {
        et_jiano.setText(jiano);
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                MaterialjiaBean lv_mjb = asop.getMaterialjiaInfo(et_jiano.getText().toString().trim());
                Message msg = new Message();
                msg.what = 0;
                msg.obj = lv_mjb;
                handler.sendMessage(msg);
            }
        });


    }

    public void btonclick_in(View view) {
        //放入物料架
        shortmapid = "in";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
//        Intent intent = new Intent(this, MateriallotnoinActivity.class);
//        Bundle bundle1 = new Bundle();
//        bundle1.putSerializable("materialjia", gv_mjb);
//        bundle1.putString("lotno", "S04830211-14");
//        intent.putExtras(bundle1);
//        startActivityForResult(intent, 1);
    }

    public void btonclick_out(View view) {
        //取出物料架
        shortmapid = "out";
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
         onCreate(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            gv_activityResultStr = bundle.getString("result");
            if (requestCode == 1) {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = gv_activityResultStr;
                handler.sendMessage(msg);
            } else {
                if (gv_activityResultStr != null) {

                    switch (shortmapid) {
                        case "in":
                            Intent intent = new Intent(this, MateriallotnoinActivity.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putSerializable("materialjia", gv_mjb);
                            bundle1.putString("lotno", gv_activityResultStr);
                            intent.putExtras(bundle1);
                            startActivityForResult(intent, 1);
                            break;
                        case "out":
                            Intent intent1 = new Intent(this, MateriallistnooutActivity.class);
                            Bundle bundle11 = new Bundle();
                            bundle11.putSerializable("materialjia", gv_mjb);
                            bundle11.putString("listno", gv_activityResultStr);
                            intent1.putExtras(bundle11);
                            startActivityForResult(intent1, 1);
                            break;
                        default:
                            break;
                    }
                }
                shortmapid="";
            }

        }
    }

    class MateriallotnoAdapter extends BaseAdapter {

        private Context context;
        private List<MaterialjiaLotnoBean> list;

        public MateriallotnoAdapter(Context context, List<MaterialjiaLotnoBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_materiallotnoadpter, null);
                viewHolder = new ViewHolder();
                viewHolder.Text1 = (TextView) convertView.findViewById(R.id.text_item_materialwonoadpter1);
                viewHolder.Text2 = (TextView) convertView.findViewById(R.id.text_item_materialwonoadpter2);
                viewHolder.Text3 = (TextView) convertView.findViewById(R.id.text_item_materialwonoadpter3);
                viewHolder.Text4 = (TextView) convertView.findViewById(R.id.text_item_materialwonoadpter4);
                viewHolder.Text5 = (TextView) convertView.findViewById(R.id.text_item_materialwonoadpter5);
                viewHolder.Text6 = (TextView) convertView.findViewById(R.id.text_item_materialwonoadpter6);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.Text1.setText(list.get(position).jiano);
            viewHolder.Text2.setText(list.get(position).wono);
            viewHolder.Text3.setText(list.get(position).lotno);
            viewHolder.Text4.setText(list.get(position).productno);
            viewHolder.Text5.setText(list.get(position).creadate);
            viewHolder.Text6.setText(list.get(position).qiety);

            return convertView;
        }
    }

    class ViewHolder {
        TextView Text1, Text2, Text3, Text4, Text5, Text6;
    }

}
