package app.dpapp.material;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.ListnoBean;
import app.dpapp.soap.AppleSOAP;

public class SMTSapKZ extends AppCompatActivity {

    private EditText et_wono;
    private String resultstr;
    private String o_wono;
    private ListnoAdapter listnoAdapter;
    private ListView listView;
    private List<ListnoBean> list;
    private boolean flag_kz = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smtsap_kz);

        et_wono = (EditText) findViewById(R.id.et_wono);
        listView = (ListView) findViewById(R.id.lv_listno);
        list=new ArrayList<>();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    list = (List<ListnoBean>) msg.obj;
                    if (list != null) {
                        listnoAdapter = new ListnoAdapter(SMTSapKZ.this, list);
                        listView.setAdapter(listnoAdapter);
                        flag_kz = true;
                    } else {
                        resultstr = "查询工单：" + o_wono + "的入库单信息失败";
                        flag_kz = false;
                        Toast.makeText(SMTSapKZ.this, resultstr, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    resultstr = (String) msg.obj;
                    if (resultstr != null) {
                        Toast.makeText(SMTSapKZ.this, resultstr, Toast.LENGTH_SHORT).show();
                    }
                    flag_kz = false;
                    break;
                default:
                    break;
            }
        }
    };

    public void bt_onclick_cs(View view) {
        et_wono.setEnabled(true);
        flag_kz = false;
    }

    public void bt_onclick_qd(View view) {
        et_wono.setEnabled(false);
        o_wono = et_wono.getText().toString();
        if (o_wono == null || o_wono.equals("")) {
            resultstr = "工单号不能为空";
            Toast.makeText(SMTSapKZ.this, resultstr, Toast.LENGTH_SHORT).show();
            return;
        }

        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<ListnoBean> lists= asop.getListnoInfobywono(o_wono);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = lists;
                handler.sendMessage(msg);
            }
        });
    }

    public void bt_onclick_kz(View view) {

        o_wono = et_wono.getText().toString();
        if (o_wono == null || o_wono.equals("")) {
            resultstr = "工单号不能为空";
            Toast.makeText(SMTSapKZ.this, resultstr, Toast.LENGTH_SHORT).show();
            return;
        }

        if (flag_kz) {
            MyThreadPool.pool.execute(new Runnable() {
                @Override
                public void run() {
                    AppleSOAP asop = new AppleSOAP();
                    String str = asop.Listnoczbywono(o_wono);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            });

        } else {
            resultstr = "扣账状态不成立";
            Toast.makeText(SMTSapKZ.this, resultstr, Toast.LENGTH_SHORT).show();
            return;
        }
    }


    class ListnoAdapter extends BaseAdapter {

        private Context context;
        private List<ListnoBean> list;


        public ListnoAdapter(Context context, List<ListnoBean> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_listno, null);
                viewHolder = new ViewHolder();
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
                viewHolder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
                viewHolder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
                viewHolder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
                viewHolder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
                viewHolder.tv8 = (TextView) convertView.findViewById(R.id.tv8);
                viewHolder.tv9 = (TextView) convertView.findViewById(R.id.tv9);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv1.setText(list.get(position).wono);
            viewHolder.tv2.setText(list.get(position).listno);
            viewHolder.tv3.setText(list.get(position).listno1);
            viewHolder.tv4.setText(list.get(position).lotno);
            viewHolder.tv5.setText(list.get(position).productno);
            viewHolder.tv6.setText(list.get(position).qty);
            viewHolder.tv7.setText(list.get(position).cartno);
            viewHolder.tv8.setText(list.get(position).type);
            viewHolder.tv9.setText(list.get(position).ctime);

            return convertView;
        }
    }

    class ViewHolder {
        TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    }
}
