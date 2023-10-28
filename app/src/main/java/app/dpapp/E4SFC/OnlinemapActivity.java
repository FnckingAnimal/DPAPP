package app.dpapp.E4SFC;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.E4DevicenoBean;
import app.dpapp.bean.E4LotnoStatusBean;
import app.dpapp.soap.MSSFCSOAP;
import app.dpapp.staticBianliang.e4DevicenoStaticdata;
import app.dpapp.R;

public class OnlinemapActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_userid, et_pw, et_lotno;
    private TextView tv_deviceno, tv_ip;
    private ListView lv_lotnoInfo;
    private LotnoInfoAdapter lotnoInfoAdapter;
    private Button bt_zx,bt_qx;
    private List<E4DevicenoBean> list_devicenoInfo;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 0) {
                    List<E4LotnoStatusBean> ls = new ArrayList<>();
                    ls = (List<E4LotnoStatusBean>) msg.obj;
                    if (ls == null) {
                        return;
                    }
                    lotnoInfoAdapter=new LotnoInfoAdapter(ls,OnlinemapActivity.this);
                    lv_lotnoInfo.setAdapter(lotnoInfoAdapter);

                }
            } catch (Exception ex1) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinemap);

        tv_deviceno = (TextView) findViewById(R.id.text_deviceno);
        tv_ip = (TextView) findViewById(R.id.text_ip);
        lv_lotnoInfo= (ListView) findViewById(R.id.list_lotnoinfo);
        bt_zx= (Button) findViewById(R.id.bt_zx);
        bt_qx= (Button) findViewById(R.id.bt_qx);

        et_userid= (EditText) findViewById(R.id.et_userid);
        et_pw= (EditText) findViewById(R.id.et_password);
        et_lotno=(EditText) findViewById(R.id.et_lotno);

        tv_deviceno.setText(e4DevicenoStaticdata.deviceno);

        list_devicenoInfo = new ArrayList<>();

        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                MSSFCSOAP sfcsoap = new MSSFCSOAP();
                List<E4LotnoStatusBean> list_lotnostatus = new ArrayList<E4LotnoStatusBean>();
                list_lotnostatus = sfcsoap.sfc_getlotstatus(e4DevicenoStaticdata.dblinkstr);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = list_lotnostatus;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_zx:
                zhixing();
                break;
            case R.id.bt_qx:

                break;
            default:
                break;
        }
    }

    private void zhixing() {
        String userid = et_userid.getText().toString().toUpperCase();
        String userpw = et_pw.getText().toString();
        String usergroup = "";
        String lotno = et_lotno.getText().toString().toUpperCase();


        if (e4DevicenoStaticdata.lotnosubflag == "1")    //  全程子批
        {
            //int saa = lotno.IndexOf("-");

        }

        // 批號轉換 加前綴 (不同車間，根據t_lotstatuspro表，重新獲取半成品/成品料號)
        //原流程的獲取


        //验证用户名密码是否正确
        //checkuserid();
    }

    class LotnoInfoAdapter extends BaseAdapter {
        private List<E4LotnoStatusBean> list;
        private Context context;

        public LotnoInfoAdapter(List<E4LotnoStatusBean> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int position) {
            return this.list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_lotnostatus, null);
                holder = new ViewHolder();
                holder.Text1 = (TextView) convertView.findViewById(R.id.tv_xh);
                holder.Text2 = (TextView) convertView.findViewById(R.id.tv_lotno);
                holder.Text3 = (TextView) convertView.findViewById(R.id.tv_opno);
                holder.Text4 = (TextView) convertView.findViewById(R.id.tv_qty);
                holder.Text5 = (TextView) convertView.findViewById(R.id.tv_time);
                holder.Text6 = (TextView) convertView.findViewById(R.id.tv_zhuangtai);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.Text1.setText(list.get(position).getXh());
            holder.Text2.setText(list.get(position).getLotno());
            holder.Text3.setText(list.get(position).getOpno());
            holder.Text4.setText(list.get(position).getQty());
            holder.Text5.setText(list.get(position).getTime());
            if (list.get(position).getZhuangtai().equals("1")) {
                holder.Text6.setText("check in");
            } else {
                holder.Text6.setText("check out");
            }
            return convertView;
        }
    }


    class ViewHolder {
        TextView Text1, Text2, Text3, Text4, Text5, Text6;
    }

}
