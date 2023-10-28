package app.dpapp.material;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.MaterialLotnoBean;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.zxing.activity.CaptureActivity;

public class MaterialOutActivity extends AppCompatActivity {

    private String typeflag, materiallotno, messgeStr;
    private LinearLayout ll_out_materiallotnolist;
    private EditText et_out_materiallotno;
    private TextView tv_result3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out);
        Bundle bundle = getIntent().getExtras();
        typeflag = bundle.getString("typeflag");

        ll_out_materiallotnolist = (LinearLayout) findViewById(R.id.ll_out_materiallotnolist);
        et_out_materiallotno = (EditText) findViewById(R.id.et_out_materiallotno);
        tv_result3=(TextView) findViewById(R.id.tv_result3);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MaterialLotnoBean mlb = (MaterialLotnoBean) msg.obj;
                    if (mlb != null) {
                        if(mlb.getTypeflag().equals(typeflag)){
                            if (mlb.getLotnostatus().equals("1")) {
                                final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_materiainactivity_materiallotno, null);
                                TextView tv_materiallotno = (TextView) view.findViewById(R.id.tv_materiallotno);
                                TextView tv_materialno = (TextView) view.findViewById(R.id.tv_materialno);
                                TextView tv_materialname = (TextView) view.findViewById(R.id.tv_materialname);
                                TextView tv_lotnostatus = (TextView) view.findViewById(R.id.tv_lotnostatus);
                                TextView tv_materialdeviceno = (TextView) view.findViewById(R.id.tv_materialdeviceno);
                                TextView tv_materialbh = (TextView) view.findViewById(R.id.tv_materialbh);
                                TextView tv_materianum = (TextView) view.findViewById(R.id.tv_materialnum);
                                TextView tv_typeflag = (TextView) view.findViewById(R.id.tv_typeflag);

                                et_out_materiallotno.setText(materiallotno);
                                tv_materiallotno.setText(mlb.getLotno());
                                tv_materialno.setText(mlb.getMaterialno());
                                tv_materialname.setText(mlb.getMaterialname());
                                tv_lotnostatus.setText(mlb.getLotnostatus());
                                tv_materialbh.setText(mlb.getBh());
                                tv_materianum.setText(mlb.getQty());
                                tv_materialdeviceno.setText(mlb.getDeviceno());
                                tv_typeflag.setText(mlb.getTypeflag());

                                view.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaterialOutActivity.this);
                                        alertDialog.setTitle("溫馨提示");
                                        alertDialog.setMessage("是否從列表中刪除");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ll_out_materiallotnolist.removeView(view);
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
                                });
                                ll_out_materiallotnolist.addView(view);
                            } else if (mlb.getLotnostatus().equals("2")) {
                                messgeStr = "該物料批號已經有出庫記錄";
                                Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                            } else {
                                messgeStr = "該物料批號狀態有問題";
                                Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            messgeStr = "該物料批號typeflag与物料类型的typeflag不一致，请确认所选物料类型";
                            Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        messgeStr = "查詢物料批號狀態出錯";
                        Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    break;

                case 1:
                    String resultstr= (String) msg.obj;
                    if(resultstr!=null){
                        messgeStr = resultstr;
                        Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                        String[] results=resultstr.split(":");
                        if(results[0].equals("1")){
                            ll_out_materiallotnolist.removeAllViews();
                            et_out_materiallotno.setText("");
                        }

                    }else {
                        messgeStr = "保存數據出錯";
                        Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    tv_result3.setText(messgeStr);

                    break;

                default:
                    break;
            }
        }
    };


    public void onClick_submit_out(View view) {
        List<String> list1=new ArrayList<>();
        if(ll_out_materiallotnolist.getChildCount()>0){
            for(int i=0;i<ll_out_materiallotnolist.getChildCount();i++){
                View view1=ll_out_materiallotnolist.getChildAt(i);
                TextView tv_materiallotno1 = (TextView) view1.findViewById(R.id.tv_materiallotno);
                list1.add(tv_materiallotno1.getText().toString());
            }
            String datastr = new Gson().toJson(list1);
            updateData(datastr);
        }
    }

    private void updateData(final String datastr) {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String resultStr = asop.updateMaterialLotnos(datastr);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = resultStr;
                handler.sendMessage(msg);
            }
        });
    }

    public void onClick_out_scan(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
//        materiallotno = "222222";
//        if (checkmateriallotnoisExit()) {
//            messgeStr = "該物料批號已經扫描";
//            Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
//            return;
//        }
//        getMateriallotnoData();
    }

    private boolean checkmateriallotnoisExit() {
        boolean flag = false;
        for (int i = 0; i < ll_out_materiallotnolist.getChildCount(); i++) {
            View view = ll_out_materiallotnolist.getChildAt(i);
            ;
            TextView tv = (TextView) view.findViewById(R.id.tv_materiallotno);
            String mlotno = tv.getText().toString();
            if (materiallotno.equals(mlotno)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //查詢物料批號的狀態
    private void getMateriallotnoData() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                MaterialLotnoBean materialLotnoBean = asop.getMateriallotnoData(materiallotno,typeflag);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = materialLotnoBean;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            materiallotno = bundle.getString("result");//物料批號
            if (materiallotno != null) {
                if(checkmateriallotnoisExit()){
                    messgeStr = "該物料批號已經扫描";
                    Toast.makeText(MaterialOutActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    return;
                }
                getMateriallotnoData();
            }
        }
    }
}
