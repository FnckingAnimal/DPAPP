package app.dpapp.ra;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.material.MaterialInActivity;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;
import app.dpapp.utils.DialogShowUtil;
import app.dpapp.zxing.activity.CaptureActivity;

public class Rel_Billboardsystem_Checkin_Activity extends Activity implements AdapterView.OnItemLongClickListener {

    private EditText et_lotno;
    private Spinner sp_testroomno, sp_kuino,sp_machineno;
    private EditText et_remark, et_count;
    private String msg_dialog;
    private ListView lv1;
    private List<String[]> listdata;
    private MyAdpter myAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel_billboardsystem_checkin);

        et_lotno = findViewById(R.id.et_Rel_Billboardsystem_Checkin_lotno);

        sp_testroomno = findViewById(R.id.sp_Rel_Billboardsystem_Checkin_testroomno);
        sp_kuino = findViewById(R.id.sp_Rel_Billboardsystem_Checkin_kuino);
        et_remark = findViewById(R.id.et_Rel_Billboardsystem_Checkin_remark);
        et_count = findViewById(R.id.et_Rel_Billboardsystem_Checkin_count);
        sp_machineno = findViewById(R.id.sp_Rel_Billboardsystem_Checkin_machineno);
        sp_kuino = findViewById(R.id.sp_Rel_Billboardsystem_Checkin_kuino);


        lv1 = findViewById(R.id.rc_Rel_Billboardsystem_Checkin_rc1);
        listdata=new ArrayList<>();
        myAdpter=new MyAdpter(this,listdata);
        lv1.setAdapter(myAdpter);

        lv1.setOnItemLongClickListener(this);

        //获取实验室信息
        getRaInfoTESTROOM();
        //获取樻号的信息
        getRaInfoKUINO();
        //获取机台号信息
        getRaInfoMACHINENO();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String result0 = (String) msg.obj;
                    if (result0 == null) {
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                        return;
                    } else {
                        if (!result0.equals("")) {
                            String[] resultstrs = result0.split(":");
                            List<String> list0 = new ArrayList<>();
                            for (int i = 0; i < resultstrs.length; i++) {
                                    list0.add(resultstrs[i]);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(Rel_Billboardsystem_Checkin_Activity.this, R.layout.spinner_textview, list0);
                            sp_testroomno.setAdapter(adapter);
                        }
                    }
                    break;
                case 1:
                    String result1 = (String) msg.obj;
                    if (result1 == null) {
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                        return;
                    }else{
                        if(result1.equals("1")){
                            msg_dialog = "保存信息成功";
                            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                            Rel_Billboardsystem_Checkin_Activity.this.finish();
                        }else if (result1.equals("0")){
                            msg_dialog = "保存信息失败";
                            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                            return;
                        }
                    }
                    break;
                case 2:
                    String result2 = (String) msg.obj;
                    if (result2 == null) {
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                        return;
                    }else{
                        if(result2.equals("1")){
                            msg_dialog = "该批号已经有记录了，不能再进行出入樻";
                            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                            Rel_Billboardsystem_Checkin_Activity.this.finish();
                            return;
                        }
                    }
                    break;
                case 3:
                    String result3 = (String) msg.obj;
                    if (result3 == null) {
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                        return;
                    } else {
                        if (!result3.equals("")) {
                            String[] resultstrs3 = result3.split(":");
                            List<String> list3 = new ArrayList<>();
                            for (int i = 0; i < resultstrs3.length; i++) {
                                    list3.add(resultstrs3[i]);
                            }
                            ArrayAdapter adapter3 = new ArrayAdapter(Rel_Billboardsystem_Checkin_Activity.this, R.layout.spinner_textview, list3);
                            sp_kuino.setAdapter(adapter3);
                        }
                    }
                    break;
                case 4:
                    String result4 = (String) msg.obj;
                    if (result4 == null) {
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
                        return;
                    } else {
                        if (!result4.equals("")) {
                            String[] resultstrs4 = result4.split(":");
                            List<String> list4 = new ArrayList<>();
                            for (int i = 0; i < resultstrs4.length; i++) {
                                    list4.add(resultstrs4[i]);
                            }
                            ArrayAdapter adapter4 = new ArrayAdapter(Rel_Billboardsystem_Checkin_Activity.this, R.layout.spinner_textview, list4);
                            sp_machineno.setAdapter(adapter4);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void getRaInfoTESTROOM() {
        final String methodname = "getRaInfoTESTROOM";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getRaInfoTESTROOM(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private void getRaInfoKUINO() {
        final String methodname = "getRaInfoKUINO";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getRaInfoKUINO(methodname, parameterList);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private void getRaInfoMACHINENO() {
        final String methodname = "getRaInfoMACHINENO";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getRaInfoMACHINENO(methodname, parameterList);
                Message msg = new Message();
                msg.what = 4;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    public void onclick_bt_Rel_Billboardsystem_checkin_confirm(View view) {
        if(listdata.size()>0){
            String datastr = "";
            for(int i=0;i<listdata.size();i++){
                String lotno = listdata.get(i)[0];
                String count = listdata.get(i)[1];
                String testroomno = listdata.get(i)[2];
                String kuino = listdata.get(i)[3];
                String machineno = listdata.get(i)[4];
                String remark = listdata.get(i)[5];

                if(i==listdata.size()-1){
                    datastr += lotno+":"+count+":"+testroomno+":"+ kuino+":"+ machineno+":"+remark;
                }else{
                    datastr += lotno+":"+count+":"+testroomno+":"+ kuino+":"+ machineno+":"+remark+";";
                }
            }
            saveRaCheckInfo(datastr);
        }
    }

    private void saveRaCheckInfo(String datastr) {
        final String methodname = "saveRaCheckInfo";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("datastr", datastr));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getStringBySOAP(methodname, parameterList);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    public void onclick_bt_Rel_Billboardsystem_checkin_cancle(View view) {
        this.finish();
    }

    public void onclick_bt_Rel_Billboardsystem_checkin_scan(View view) {
        String testroomno = sp_testroomno.getSelectedItem().toString();
        String kuino = sp_kuino.getSelectedItem().toString();
        String machineno = sp_machineno.getSelectedItem().toString();
        String count = et_count.toString();
        String remark = et_remark.toString();
        if (testroomno == null || testroomno.equals("")) {
            msg_dialog = "请选择实验室";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
            return;
        }
        if (kuino == null || kuino.equals("")) {
            msg_dialog = "请选择樻";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
            return;
        }
        if (machineno == null || machineno.equals("")) {
            msg_dialog = "请输入机台号";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
            return;
        }
        if (count == null || count.equals("")) {
            msg_dialog = "请输入数量";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
            return;
        }
        if (remark == null || remark.equals("")) {
            msg_dialog = "请输出操作用户名";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkin_Activity.this, msg_dialog);
            return;
        }

        Intent intent = new Intent(Rel_Billboardsystem_Checkin_Activity.this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String lotno = bundle.getString("result");
            et_lotno.setText(lotno);
            String count = et_count.getText().toString();
            String testroomno = sp_testroomno.getSelectedItem().toString();
            String kuino = sp_kuino.getSelectedItem().toString();
            String machineno = sp_machineno.getSelectedItem().toString();
            String remark = et_remark.getText().toString();
            String[] info = {lotno, count, testroomno, kuino, machineno, remark};
            listdata.add(info);
            myAdpter.notifyDataSetChanged();
        }
    }

    private void checkRaLotnoState(String lotno) {
        final String methodname = "checkRaLotnoState";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("lotno", lotno));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getStringBySOAP(methodname, parameterList);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Rel_Billboardsystem_Checkin_Activity.this);
        alertDialog.setTitle("溫馨提示");
        alertDialog.setMessage("是否從列表中刪除");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listdata.remove(position);
                myAdpter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        return true;
    }

    class MyAdpter extends BaseAdapter{

        private Context context;
        private List<String[]> list;

        public MyAdpter(Context context,List<String[]> list){
            this.context=context;
            this.list=list;
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
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.items_rel_billboardsystem_checkin_listview, null);
                viewHolder = new ViewHolder();
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv1);
                viewHolder.tv2 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv2);
                viewHolder.tv3 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv3);
                viewHolder.tv4 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv4);
                viewHolder.tv5 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv5);
                viewHolder.tv6 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv6);
                viewHolder.tv7 = (TextView) convertView.findViewById(R.id.items_rel_billboardsystem_checkin_tv7);

                viewHolder.tv4.setVisibility(View.GONE);
                viewHolder.tv5.setVisibility(View.GONE);
                viewHolder.tv6.setVisibility(View.GONE);
                viewHolder.tv7.setVisibility(View.GONE);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv1.setText(String.valueOf(position));
            viewHolder.tv2.setText(list.get(position)[0]);
            viewHolder.tv3.setText(list.get(position)[1]);
            viewHolder.tv4.setText(list.get(position)[2]);
            viewHolder.tv5.setText(list.get(position)[3]);
            viewHolder.tv6.setText(list.get(position)[4]);
            viewHolder.tv7.setText(list.get(position)[5]);

            return convertView;
        }
    }

    class ViewHolder{
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    }
}