package app.dpapp.material;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.ListnoSaveBean;
import app.dpapp.bean.MaterialjiaBean;
import app.dpapp.bean.MaterialjiaLotnoBean;
import app.dpapp.gsonbean.StocklistnoBean;
import app.dpapp.soap.AppleSOAP;

public class MateriallistnooutActivity extends Activity {

    private String jiano, jiapro, jiadev, jiakc, stocklistno, str_result, str_selectlistno, str_selectlistnopro, gv_selectjiano, sendqty;
    private TextView tv_jiano, tv_jiapro, tv_jiadev, tv_jiakc,tv_cangma;
    private EditText et_stocklistno, et_listnodev, et_listnoqty, et_listnosendedqty, et_listnoyfqty, et_selectqty;
    private Spinner sp_listno, sp_listnopro, sp_jiano1;
    private ListView lv_materiallotnoData;
    private List<MaterialjiaLotnoBean> list_lotno;
    private KClotnoAdapter kClotnoAdapter;

    private MaterialjiaBean gv_mjb;

    private StocklistnoBean stocklistnoBean;

    private ArrayAdapter<String> adapter_listno;
    private ArrayAdapter<String> adapter_listnopro;
    private ArrayAdapter<String> adapter_jiano;

    private Button bt_outsubmit;

    private int num = 0;
    private int maxnum = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String listnoStr = (String) msg.obj;
                    if (listnoStr != null && listnoStr != "") {
                        Gson gson = new Gson();
                        stocklistnoBean = gson.fromJson(listnoStr, StocklistnoBean.class);
                        if (stocklistnoBean.getResultflag() == 1) {
                            String str_listno = stocklistnoBean.getResultvalue().getOrderNos();
                            String[] listnos = str_listno.split(",");
                            List<String> list_listno = new ArrayList<>();
                            for (String listno : listnos) {
                                if (listno != null && listno != "") {
                                    list_listno.add(listno);
                                }
                            }
                            adapter_listno = new ArrayAdapter<String>(MateriallistnooutActivity.this, R.layout.spinner_textview, list_listno);
                            sp_listno.setAdapter(adapter_listno);

                        } else {
                            str_result = stocklistnoBean.getMessage();
                            Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        str_result = "查询领料单信息失败";
                        Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    String saveresult = (String) msg.obj;
                    if (saveresult != null && saveresult != "") {
                        str_result = saveresult;
                        Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                    } else {
                        str_result = "领料单取出失败";
                        Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                    }
                    onCreate(null);
                    break;
                case 2:
                    List<String> lv_jianolist = (List<String>) msg.obj;
                    if (lv_jianolist != null) {
                        adapter_jiano = new ArrayAdapter<String>(MateriallistnooutActivity.this, R.layout.spinner_textview, lv_jianolist);
                        sp_jiano1.setAdapter(adapter_jiano);
                    } else {
                        str_result = "查询物料架失败";
                        Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 3:
                    gv_mjb = (MaterialjiaBean) msg.obj;
                    if (gv_mjb != null) {
                        tv_jiano.setText(gv_mjb.jiano);
                        tv_jiadev.setText(gv_mjb.deviceno);
                        tv_jiapro.setText(gv_mjb.productno);
                        tv_cangma.setText(gv_mjb.cangma);
                        tv_jiakc.setText(gv_mjb.qiety);

                    } else {
                        str_result = "查询物料架信息失败";
                        Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 4:
                    sendqty = (String) msg.obj;
                    if (sendqty != null) {
                        et_listnosendedqty.setText(sendqty);
                        //应发数量=发料总数量-已发数量
                        int yfnum = Integer.parseInt(et_listnoqty.getText().toString()) - Integer.parseInt(et_listnosendedqty.getText().toString());
                        maxnum = yfnum;
                        et_listnoyfqty.setText(String.valueOf(yfnum));
                    } else {
                        str_result = "查询已发数量失败";
                        Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;

            }
        }
    };

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    list_lotno = (List<MaterialjiaLotnoBean>) msg.obj;
                    if (list_lotno != null && list_lotno.size() > 0) {
                        kClotnoAdapter = new KClotnoAdapter(MateriallistnooutActivity.this, list_lotno);
                        lv_materiallotnoData.setAdapter(kClotnoAdapter);
                    } else {
                        str_result = "库存查询失败";
                        Toast.makeText(MateriallistnooutActivity.this, "库存查询失败", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_materiallistnoout);
        stocklistno = getIntent().getStringExtra("stocklistno");

        tv_jiano = (TextView) findViewById(R.id.tv_listno_jiano);
        tv_jiapro = (TextView) findViewById(R.id.tv_listno_jiapro);
        tv_jiadev = (TextView) findViewById(R.id.tv_listno_jiadev);
        tv_jiakc = (TextView) findViewById(R.id.tv_listno_jiakc);
        tv_cangma= (TextView) findViewById(R.id.tv_listno_cangma);

        et_stocklistno = (EditText) findViewById(R.id.et_stocklistno);
        et_stocklistno.setText(stocklistno);

        sp_listno = (Spinner) findViewById(R.id.sp_listno_listno);
        sp_listnopro = (Spinner) findViewById(R.id.sp_listno_listnopro);
        sp_jiano1 = (Spinner) findViewById(R.id.sp_jiano1);

        et_listnodev = (EditText) findViewById(R.id.et_listno_listnodev);
        et_listnoqty = (EditText) findViewById(R.id.et_listno_listnoqty);
        et_listnosendedqty = (EditText) findViewById(R.id.et_listno_sendedqty);
        et_listnoyfqty = (EditText) findViewById(R.id.et_listno_yfqty);
        et_selectqty = (EditText) findViewById(R.id.et_listno_selectqty);

        lv_materiallotnoData = (ListView) findViewById(R.id.lv_lotnoInfo);
        list_lotno = new ArrayList<>();
        stocklistnoBean = new StocklistnoBean();

        bt_outsubmit = (Button) findViewById(R.id.bt_outsubmit);

        sp_listno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_selectlistno = sp_listno.getItemAtPosition(position).toString();
                if (str_selectlistno != null && str_selectlistno != "") {
                    StocklistnoBean.ResultvalueBean lsb = stocklistnoBean.getResultvalue();
                    List<StocklistnoBean.ResultvalueBean.LsstockBean> ls_lsstock = lsb.getLsstock();

                    List<String> list_listnopro = new ArrayList<>();
                    for (StocklistnoBean.ResultvalueBean.LsstockBean lsstock : ls_lsstock) {
                        String orderno = lsstock.getOrderno();
                        if (str_selectlistno.equals(orderno)) {
                            String listnopro = lsstock.getProductNo();
                            boolean flag_isexit = false;
                            for (String lisnostr : list_listnopro) {
                                if (listnopro.equals(lisnostr)) {
                                    flag_isexit = true;
                                }
                            }
                            if (!flag_isexit) {
                                list_listnopro.add(listnopro);
                            }
                        }
                    }
                    adapter_listnopro = new ArrayAdapter<String>(MateriallistnooutActivity.this, R.layout.spinner_textview, list_listnopro);
                    sp_listnopro.setAdapter(adapter_listnopro);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_listnopro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_selectlistnopro = sp_listnopro.getItemAtPosition(position).toString();
                if (str_selectlistnopro != null && str_selectlistnopro != "") {
                    StocklistnoBean.ResultvalueBean lsb = stocklistnoBean.getResultvalue();
                    List<StocklistnoBean.ResultvalueBean.LsstockBean> ls_lsstock = lsb.getLsstock();

                    String str_dev = "";
                    int qty = 0;
                    for (StocklistnoBean.ResultvalueBean.LsstockBean lsstock : ls_lsstock) {
                        if (str_selectlistno.equals(lsstock.getOrderno()) && str_selectlistnopro.equals(lsstock.getProductNo())) {
                            str_dev = lsstock.getMachineType();
                            qty += lsstock.getQty();
                        }
                    }
                    et_listnodev.setText(str_dev);
                    et_listnoqty.setText(String.valueOf(qty));

                    //根据料号获取物料架
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppleSOAP asop = new AppleSOAP();
                            List<String> list = asop.getMaterialjianoBypro(str_selectlistnopro);
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = list;
                            handler.sendMessage(msg);
                        }
                    });

                    //根据发料单和料号获取已发数量
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppleSOAP asop = new AppleSOAP();
                            String str = asop.getListnoSendedQty(str_selectlistno, str_selectlistnopro);
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = str;
                            handler.sendMessage(msg);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_jiano1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gv_selectjiano = sp_jiano1.getItemAtPosition(position).toString();
                //重新选择物料架，初始化数据
                num = 0;
                et_selectqty.setText(String.valueOf(num)); //
                lv_materiallotnoData.setAdapter(null);
                //根据选择的物料架获取物料架信息
                if (gv_selectjiano != null) {
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppleSOAP asop = new AppleSOAP();
                            MaterialjiaBean lv_mjb = asop.getMaterialjiaInfo(gv_selectjiano);
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = lv_mjb;
                            handler.sendMessage(msg);
                        }
                    });

                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppleSOAP asop = new AppleSOAP();
                            List<MaterialjiaLotnoBean> lst1 = asop.getMaterialjiaLotnoData("ALL", gv_selectjiano);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = lst1;
                            handler1.sendMessage(msg);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String str1 = asop.getListnoInfo(stocklistno);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = str1;
                handler.sendMessage(msg);
            }
        });
    }

    public void bt_onclick_outsubmit(View view) {
        String listnodev = et_listnodev.getText().toString();
        if (jiapro == str_selectlistnopro) {
            str_result = "物料架的料号与发料单不一致";
            Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
            return;
        } else {
            int sendednum = Integer.parseInt(sendqty);//已发数量
            int selectnum = Integer.parseInt(et_selectqty.getText().toString());//选取数量
            int listnonum = Integer.parseInt(et_listnoqty.getText().toString());//应发数量

            if (sendednum + selectnum > listnonum) {
                str_result = "发料单：" + str_selectlistno + "的选取数量+已发数量超过应发数量";
                Toast.makeText(MateriallistnooutActivity.this, str_result, Toast.LENGTH_LONG).show();
                return;
            }

            int count = lv_materiallotnoData.getChildCount();
            String uploadStr = "";
            for (int i = 0; i < count; i++) {
                View view1 = lv_materiallotnoData.getChildAt(i);
                CheckBox cb = (CheckBox) view1.findViewById(R.id.cb_select);
                TextView tv_lotno = (TextView) view1.findViewById(R.id.tv_lotno1);
                EditText et_num = (EditText) view1.findViewById(R.id.et_num);
                if (cb.isChecked()) {
                    ListnoSaveBean ls = new ListnoSaveBean();
                    ls.listno = str_selectlistno;
                    ls.jiano = gv_selectjiano;
                    ls.lotno = tv_lotno.getText().toString();
                    ls.productno = str_selectlistnopro;
                    ls.qiety = et_num.getText().toString();
                    ls.stocklistno = stocklistno;
                    if (uploadStr == "") {
                        uploadStr += ls.toString();
                    } else {
                        uploadStr += ":" + ls.toString();
                    }
                }
            }

            final String DataStr = uploadStr;
            MyThreadPool.pool.execute(new Runnable() {
                @Override
                public void run() {
                    AppleSOAP asop = new AppleSOAP();
                    String str = asop.saveListnoData(DataStr);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            });

        }


    }

    public void bt_onclick_outquxiao(View view) {
        this.finish();
    }

    class KClotnoAdapter extends BaseAdapter {

        private Context context;
        private List<MaterialjiaLotnoBean> list;

        public KClotnoAdapter(Context context, List<MaterialjiaLotnoBean> list) {
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
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_materiallotnodata, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_lotno = (TextView) convertView.findViewById(R.id.tv_lotno1);
                viewHolder.tv_qty = (TextView) convertView.findViewById(R.id.tv_qty1);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
                viewHolder.et_num = (EditText) convertView.findViewById(R.id.et_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_lotno.setText(list.get(position).lotno);
            viewHolder.tv_qty.setText(list.get(position).qiety);
            viewHolder.tv_time.setText(list.get(position).creadate);
            viewHolder.et_num.setText(list.get(position).qiety);
            viewHolder.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int tempnum = Integer.parseInt(viewHolder.et_num.getText().toString());
                    int lotnonum = Integer.parseInt(viewHolder.tv_qty.getText().toString());
                    if (isChecked) {
                        if (tempnum > lotnonum || tempnum<1) {   //如果输入的数量<1或者大于批号数量，不能选择
                            viewHolder.cb_select.setChecked(false);
                            viewHolder.et_num.setEnabled(true);
                            str_result = "批号" + viewHolder.tv_lotno.getText().toString() + "输入的数量大于该批次的库存数量";
                            Toast.makeText(context, str_result, Toast.LENGTH_LONG).show();
                        } else {
                            if (num >= maxnum) {
                                Toast.makeText(context, "选择数量已经大于等于发料单的应发数量", Toast.LENGTH_LONG).show();
                                viewHolder.cb_select.setChecked(false);
                                viewHolder.et_num.setEnabled(true);
                            } else {
                                if (num < maxnum && num + tempnum > maxnum) { //如果最后一个批次接近应发数量的时候
                                    tempnum = Integer.parseInt(et_listnoyfqty.getText().toString()) - num;
                                    viewHolder.et_num.setText(String.valueOf(tempnum));
                                }
                                num += tempnum;
                                viewHolder.et_num.setEnabled(false);
                            }
                        }
                    } else {
                        //viewHolder.et_num.setText("0");
                        num -= tempnum;
                        viewHolder.et_num.setEnabled(true);
                        viewHolder.et_num.setText(String.valueOf(lotnonum));
                    }
                    et_selectqty.setText(String.valueOf(num));
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_lotno, tv_qty, tv_time;
        CheckBox cb_select;
        EditText et_num;
    }
}
