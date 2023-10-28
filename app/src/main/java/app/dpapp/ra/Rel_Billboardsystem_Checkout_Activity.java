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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;
import app.dpapp.utils.DialogShowUtil;
import app.dpapp.zxing.activity.CaptureActivity;

public class Rel_Billboardsystem_Checkout_Activity extends Activity implements AdapterView.OnItemLongClickListener {

    private EditText et_remark,et_lotno;
    private String msg_dialog;
    private List<String[]> listdata;
    private ListView lv1;
    private MyAdpter myAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel_billboardsystem_checkout);

        et_remark=findViewById(R.id.et_Rel_Billboardsystem_Checkout_remark);
        et_lotno=findViewById(R.id.et_Rel_Billboardsystem_Checkout_lotno);

        listdata=new ArrayList<>();
        lv1=findViewById(R.id.rc_Rel_Billboardsystem_Checkout_rc1);
        myAdpter=new MyAdpter(this,listdata);
        lv1.setAdapter(myAdpter);

        lv1.setOnItemLongClickListener(this);

    }

    public void onclick_bt_Rel_Billboardsystem_checkout_scan(View view) {
        Intent intent=new Intent(Rel_Billboardsystem_Checkout_Activity.this, CaptureActivity.class);
        startActivityForResult(intent,0);
    }

    public void onclick_bt_Rel_Billboardsystem_checkout_cancle(View view) {
        this.finish();
    }

    public void onclick_bt_Rel_Billboardsystem_checkout_confirm(View view) {

        if(listdata.size()<1){
            msg_dialog = "请扫描批号";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
            return;
        }
        String remark=et_remark.getText().toString();
        if(remark==null || remark.equals("")){
            msg_dialog = "请输入开单人";
            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
            return;
        }
        String datastr="";
        for(int i =0;i<listdata.size();i++){
            if(i==listdata.size()-1){
                datastr += listdata.get(i)[0];
            }else{
                datastr += listdata.get(i)[0]+ ":";
            }
        }
        updateTestLotnoStatus(datastr,remark);
    }

    private void updateTestLotnoStatus(String datastr,String remark) {
        final String methodname = "updateTestLotnoStatus";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("datastr", datastr));
        parameterList.add(new SOAPParameter("checkoutuser", remark));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            String lotno = bundle.getString("result");
            et_lotno.setText(lotno);
            //获取实验批号的信息
            getRaTestLotnoInfo(lotno);
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String result0= (String) msg.obj;
                    if(result0==null){
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
                        return;
                    }
                    if(result0.equals("")){
                        msg_dialog = "该批号没有出入樻记录";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
                        return;
                    }else{
                        String[] result0s=result0.split(":");
                        String lotno=result0s[0];
                        String count=result0s[1];
                        String state=result0s[2];
                        if(state.equals("2")){
                            msg_dialog = "该批号已经有出樻记录了";
                            DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
                            return;
                        }
                        listdata.add(result0s);
                        myAdpter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    String result1= (String) msg.obj;
                    if(result1==null){
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
                        return;
                    }else{
                      if(result1.equals("0")){
                          msg_dialog = "更新状态调失败";
                          DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
                          return;
                      }else{
                          msg_dialog = "更新状态调成功";
                          DialogShowUtil.dialogShow(Rel_Billboardsystem_Checkout_Activity.this, msg_dialog);
                          Rel_Billboardsystem_Checkout_Activity.this.finish();
                      }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void getRaTestLotnoInfo(String lotno) {
        final String methodname = "getRaTestLotnoInfo";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("lotno", lotno));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getStringBySOAP(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Rel_Billboardsystem_Checkout_Activity.this);
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

    class MyAdpter extends BaseAdapter {

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


            return convertView;
        }
    }

    class ViewHolder{
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    }

}