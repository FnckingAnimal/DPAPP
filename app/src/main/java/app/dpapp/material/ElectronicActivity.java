package app.dpapp.material;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.cmapp.Staticdata;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.zxing.activity.CaptureActivity;
import app.dpapp.R;
import app.dpapp.bean.InventoryBean;
import app.dpapp.bean.TResult;
import app.dpapp.utils.AsynNetUtils;

public class ElectronicActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener,View.OnKeyListener {
    private Spinner mSp_plant, mSp_lgort;
    private EditText mEt_material;
    private TextView mTv_inventory;
    private ListView mListView,mListView2;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String[] factoryStrings = new String[]{"6J01", "6J02","5F01"};
    private List<String> factoryList = new ArrayList<>();
    private List<String> lgortList = new ArrayList<>();
    private Button mBt_search;
    private MyElectronicAdapter mAdapter;
    private MyElectronicAdapter2 mAdapter2;
    private List<TResult> mList = new ArrayList<>();
    private List<TResult> mList2 = new ArrayList<>();
    private float totalNum = 0;
    private String lgort,userid;
    private HashMap<Integer,Float> hashMap = new HashMap<>();
    private HashMap<Integer,String> map = new HashMap<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic);
        mSp_lgort = (Spinner) findViewById(R.id.sp_electronic_cang);
        mSp_plant = (Spinner) findViewById(R.id.sp_electronic_factory);
        mEt_material = (EditText) findViewById(R.id.et_electronic_materialno);
        mEt_material.setOnKeyListener(this);
        mTv_inventory = (TextView) findViewById(R.id.tv_electronic_inventory);
        mBt_search = (Button) findViewById(R.id.bt_electronic_search);
        mBt_search.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.lv_electronic);
        mListView2 = (ListView) findViewById(R.id.lv_electronic2);
        mEt_material.setOnLongClickListener(this);
        Staticdata app = (Staticdata) getApplication();
        userid = app.getLoginUserID();
        for (int i = 0; i < factoryStrings.length; i++) {
            factoryList.add(factoryStrings[i]);
        }
        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.myspinner, factoryList);
        mSp_plant.setAdapter(mSpinnerAdapter);
        mSp_plant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lgortList.clear();
                mEt_material.setText("");
                mTv_inventory.setText("");
                mList.clear();
                mAdapter.notifyDataSetChanged();
                getLgort("ECP", mSp_plant.getSelectedItem().toString(), "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSp_lgort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEt_material.setText("");
                mTv_inventory.setText("");
                mList.clear();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAdapter = new MyElectronicAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mAdapter2 = new MyElectronicAdapter2(this,mList2);
        mListView2.setAdapter(mAdapter2);
        getLgort("ECP", mSp_plant.getSelectedItem().toString(), "");
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    execloadactivity.canclediglog();
                    InventoryBean bean1 = (InventoryBean) msg.obj;
                    mTv_inventory.setText("");
                    totalNum = 0;
                    if (bean1 != null && bean1.getBlRes()) {
                        for (int i = 0; i < bean1.getTResult().size(); i++) {
                            mList2 = bean1.getTResult();
                            mAdapter2 = new MyElectronicAdapter2(ElectronicActivity.this,mList2);
                            mListView2.setAdapter(mAdapter2);
                        }
                        for (int i = 0; i < bean1.getTResult().size(); i++) {
                            totalNum += Float.parseFloat(bean1.getTResult().get(i).getCLABS());
                        }
                        mTv_inventory.setText(totalNum + "");
//                        hashMap.put(0,totalNum);
                        //執行請求庫存記錄
                        if (TextUtils.isEmpty(mSp_lgort.getSelectedItem().toString())) {
                            lgort = "";
                        } else {
                            lgort = mSp_lgort.getSelectedItem().toString();
                        }
//                        searchInventory1(mSp_plant.getSelectedItem().toString(), mEt_material.getText().toString(), mSp_lgort.getSelectedItem().toString(), "", "");
                    } else {
                        execloadactivity.canclediglog();
                        Toast.makeText(ElectronicActivity.this, "數據為空!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 1:
                    execloadactivity.canclediglog();
                    InventoryBean bean = (InventoryBean) msg.obj;
                    if (bean != null && bean.getBlRes()) {
                        lgortList.add("");
                        for (int i = 0; i < bean.getTResult().size(); i++) {
                            lgortList.add(bean.getTResult().get(i).getLGORT());
                        }
                        mSpinnerAdapter = new ArrayAdapter<>(ElectronicActivity.this, R.layout.myspinner, lgortList);
                        mSp_lgort.setAdapter(mSpinnerAdapter);
                    }
                    break;
                case 2:
                    InventoryBean msegBean = (InventoryBean) msg.obj;
                    if (msegBean != null && msegBean.getBlRes() && msegBean.getTResult().size() > 0) {
                        mList = msegBean.getTResult();
                        mList = invertOrderList(mList);
                        mAdapter = new MyElectronicAdapter(ElectronicActivity.this, mList);
                        mListView.setAdapter(mAdapter);
                        execloadactivity.canclediglog();
                    } else {
                        execloadactivity.canclediglog();
                        Toast.makeText(ElectronicActivity.this, "查詢為空!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
//                case 3:
//                    execloadactivity.canclediglog();
//                    InventoryBean bean2 = (InventoryBean) msg.obj;
//                    if (bean2 != null && bean2.getBlRes()) {
//                        for (int i = 0; i < bean2.getTResult().size(); i++) {
//                            mList2 = bean2.getTResult();
//                            mAdapter2 = new MyElectronicAdapter2(ElectronicActivity.this,mList2);
//                            mListView2.setAdapter(mAdapter2);
//                        }
//                    } else {
//                        Toast.makeText(ElectronicActivity.this, "庫存記錄為空!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_electronic_search:
                //獲取異動信息
                execloadactivity.opendialog(this, "正在加載...");
                mList.clear();
                mAdapter.notifyDataSetChanged();
                if (TextUtils.isEmpty(mSp_lgort.getSelectedItem().toString())) {
                    lgort = "";
                } else {
                    lgort = mSp_lgort.getSelectedItem().toString();
                }
                getMseg("ECP", mSp_plant.getSelectedItem().toString(),
                        mEt_material.getText().toString(), lgort, "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mEt_material.setText(data.getStringExtra("result"));
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mList2.clear();
            mAdapter2.notifyDataSetChanged();
            if (TextUtils.isEmpty(mSp_lgort.getSelectedItem().toString())) {
                lgort = "";
            } else {
                lgort = mSp_lgort.getSelectedItem().toString();
            }
            searchInventory1(mSp_plant.getSelectedItem().toString(), mEt_material.getText().toString(), mSp_lgort.getSelectedItem().toString(), "", "");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.et_electronic_materialno:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            if(event.getAction()==KeyEvent.ACTION_UP) {
                if (TextUtils.isEmpty(mEt_material.getText().toString())) {
                    Toast.makeText(ElectronicActivity.this,"请输入料号!",Toast.LENGTH_SHORT).show();
                } else {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    mList2.clear();
                    mAdapter2.notifyDataSetChanged();
                    if (TextUtils.isEmpty(mSp_lgort.getSelectedItem().toString())) {
                        lgort = "";
                    } else {
                        lgort = mSp_lgort.getSelectedItem().toString();
                    }
                    searchInventory1(mSp_plant.getSelectedItem().toString(), mEt_material.getText().toString(), mSp_lgort.getSelectedItem().toString(), "", "");
                }
            }
        }
        return false;
    }

    class MyElectronicAdapter2 extends BaseAdapter {

        private Context context;
        private List<TResult> list;


        public MyElectronicAdapter2(Context context, List<TResult> list) {
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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_electronic2, null);
                holder = new ViewHolder();
                holder.textView1 = (TextView) convertView.findViewById(R.id.tv_electronic2_materialno);
                holder.textView2 = (TextView) convertView.findViewById(R.id.tv_electronic2_lgort);
                holder.textView3 = (TextView) convertView.findViewById(R.id.tv_electronic2_sapno);
                holder.textView4 = (TextView) convertView.findViewById(R.id.tv_electronic2_vsbthno);
                holder.textView5 = (TextView) convertView.findViewById(R.id.tv_electronic2_cuerrentnum);
                holder.textView6 = (TextView) convertView.findViewById(R.id.tv_electronic2_nopost);
                holder.textView7 = (TextView) convertView.findViewById(R.id.tv_electronic2_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(list.get(position).getMATNR());
            holder.textView2.setText(list.get(position).getLGORT());
            holder.textView3.setText(list.get(position).getCHARG());
            holder.textView4.setText(list.get(position).getVSBTH());
            holder.textView5.setText(list.get(position).getCLABS());
            holder.textView6.setText(list.get(position).getNOPOST_QTY());
            holder.textView7.setText(list.get(position).getBUDAT_MKPF());
            return convertView;
        }

        class ViewHolder {
            TextView textView1, textView2, textView3, textView4, textView5, textView6,textView7;
        }
    }

    class MyElectronicAdapter extends BaseAdapter {

        private Context context;
        private List<TResult> list;


        public MyElectronicAdapter(Context context, List<TResult> list) {
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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_electronic, null);
                holder = new ViewHolder();
                holder.textView1 = (TextView) convertView.findViewById(R.id.tv_date);
                holder.textView2 = (TextView) convertView.findViewById(R.id.tv_order);
                holder.textView3 = (TextView) convertView.findViewById(R.id.tv_lotno);
                holder.textView4 = (TextView) convertView.findViewById(R.id.tv_number_in);
                holder.textView5 = (TextView) convertView.findViewById(R.id.tv_inventory);
                holder.editText = (EditText) convertView.findViewById(R.id.et_userid);
                holder.textView7 = (TextView) convertView.findViewById(R.id.tv_number_out);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(list.get(position).getBUDAT_MKPF());
            if(TextUtils.isEmpty(list.get(position).getKUNNR())) {
                holder.textView2.setText(list.get(position).getLFBNR());
            } else {
                holder.textView2.setText(list.get(position).getVBELN_IM());
            }
            holder.textView3.setText(list.get(position).getSGTXT());
            map.put(position,userid);
            if (holder.editText.getTag() != null &&holder.editText.getTag() instanceof TextWatcher) {
                holder.editText.removeTextChangedListener((TextWatcher) holder.editText.getTag());
            }
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    map.remove(position);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                item.setApplyEmpName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    map.put(position,s.toString());
                }
            };
            if(position == 0) {
                if ("S".equals(list.get(position).getSHKZG())) {
                    hashMap.put(0,totalNum);
                    holder.textView4.setText(list.get(position).getMENGE());
                    holder.textView7.setText("");
                } else if ("H".equals(list.get(position).getSHKZG())) {
                    hashMap.put(0,totalNum);
                    holder.textView4.setText("");
                    holder.textView7.setText(list.get(position).getMENGE());
                }
            } else {
                if ("S".equals(list.get(position - 1).getSHKZG())) {
                    hashMap.put(position,hashMap.get(position - 1) - Float.parseFloat(list.get(position - 1).getMENGE()));
                    holder.textView4.setText(list.get(position).getMENGE());
                    holder.textView7.setText("");
                } else if ("H".equals(list.get(position - 1).getSHKZG())) {
                    hashMap.put(position,hashMap.get(position - 1) + Float.parseFloat(list.get(position - 1).getMENGE()));
                    holder.textView4.setText("");
                    holder.textView7.setText(list.get(position).getMENGE());
                }
            }

            holder.textView5.setText(hashMap.get(position) + "");
//            holder.editText.setText(userid);
            holder.editText.addTextChangedListener(watcher);
            holder.editText.setTag(watcher);
            return convertView;
        }

        class ViewHolder {
            TextView textView1, textView2, textView3, textView4, textView5, textView7;
            EditText editText;
        }
    }

    /**
     * 獲取倉碼
     *
     * @param strDBType
     * @param strPlant
     * @param strLgort
     */
    public void getLgort(String strDBType, String strPlant, String strLgort) {
        execloadactivity.opendialog(this, "正在獲取..");
        String url = "http://10.151.128.172:8086/MM/dtGetLgort?strDBType=" + strDBType + "&strPlant=" + strPlant + "&strLgort=" + strLgort;
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                InventoryBean bean = gson.fromJson(rspoonse, InventoryBean.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取异动资料
     *
     * @param strDBType ECP/ECQ
     * @param strPlant  工廠
     * @param strMatnr  //料號(可選)
     * @param strLgort  //倉碼(可選)
     * @param strCharg  //SAP批次(可選)
     * @return
     */
    public void getMseg(String strDBType, String strPlant, String strMatnr, String strLgort, String strCharg) {
        execloadactivity.opendialog(this, "正在獲取..");
        String url = "http://10.151.128.172:8086/MM/dtGetMseg?strDBType=" + strDBType + "&strPlant=" + strPlant + "&strMatnr=" + strMatnr + "&strLgort=" + strLgort + "&strCharg=" + strCharg;
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                InventoryBean bean = gson.fromJson(rspoonse.replace(" ", ""), InventoryBean.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 查詢库存明細
     * 參數:dbType  ECP/ECQ
     * WERKS   工廠
     * MATNR   料號(可選)
     * LGORT   倉碼 (可選)
     * CHARG   SAP批次(可選)
     * VSBTH   供應商批次(可選)
     * http://10.151.128.172:8086/MM/SapGetWmsStock?dbType=ECQ&WERKS=6J01&MATNR=CNK38-5000-00&CHARG=1911290001&LGORT=&VSBTH=
     */
    public void searchInventory1(String plant, String matnr, String lgort, String charg, String vsbth) {
        execloadactivity.opendialog(this, "正在獲取..");
        String url = "http://10.151.128.172:8086/MM/SapGetWmsStock1?dbType=ECP&WERKS=" + plant + "&MATNR=" + matnr + "&CHARG=" + charg + "&LGORT=" + lgort + "&VSBTH=" + vsbth;
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                InventoryBean bean = gson.fromJson(rspoonse, InventoryBean.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 查詢库存
     * 參數:dbType  ECP/ECQ
     * WERKS   工廠
     * MATNR   料號(可選)
     * LGORT   倉碼 (可選)
     * CHARG   SAP批次(可選)
     * VSBTH   供應商批次(可選)
     * http://10.151.128.172:8086/MM/SapGetWmsStock?dbType=ECQ&WERKS=6J01&MATNR=CNK38-5000-00&CHARG=1911290001&LGORT=&VSBTH=
     */
    public void searchInventory(String plant, String matnr, String lgort, String charg, String vsbth) {
        execloadactivity.opendialog(this, "正在獲取..");
        String url = "http://10.151.128.172:8086/MM/SapGetWmsStock?dbType=ECP&WERKS=" + plant + "&MATNR=" + matnr + "&CHARG=" + charg + "&LGORT=" + lgort + "&VSBTH=" + vsbth;
        AsynNetUtils.get(url, new AsynNetUtils.Callback() {
            @Override
            public void onResponse(String rspoonse) {
                Gson gson = new Gson();
                InventoryBean bean = gson.fromJson(rspoonse, InventoryBean.class);
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
    }

    //将List按照时间倒序排列
    @SuppressLint("SimpleDateFormat")
    private List<TResult> invertOrderList(List<TResult> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1;
        Date d2;
        TResult reuslt = new TResult();
        //做一个冒泡排序，大的在数组的前列
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                d1 = sdf.parse(list.get(i).getBUDAT_MKPF(), pos1);
                d2 = sdf.parse(list.get(j).getBUDAT_MKPF(), pos2);
                if (d1.before(d2)) {//如果队前日期靠前，调换顺序
                    reuslt = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, reuslt);
                }
            }
        }
        return list;
    }
}
