package app.dpapp.material;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class MaterialInActivity extends Activity {

    private String messgeStr, materiallotno, materialtype,materialno,materialname,typeflag,deviceno,materialjiano;
    private LinearLayout ll_materiallotnolist, ll_selectdev, ll_selectbh;
    private TextView tv_bh,tv_result2;
    private Spinner sp_materialno,sp_materialjiano,sp_materialdeviceno;
    private EditText et_materialno,et_materiallotno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in);
        Bundle bundle = getIntent().getExtras();
        typeflag = bundle.getString("typeflag");
        deviceno="";
        materialjiano="";

        ll_materiallotnolist = (LinearLayout) findViewById(R.id.ll_materiallotnolist);
        ll_selectdev = (LinearLayout) findViewById(R.id.ll_selectdev);
        ll_selectbh = (LinearLayout) findViewById(R.id.ll_selectbh);
        tv_bh = (TextView) findViewById(R.id.tv_bh);
        tv_result2= (TextView) findViewById(R.id.tv_result2);
        sp_materialno= (Spinner) findViewById(R.id.sp_materialno);
        et_materialno = (EditText) findViewById(R.id.et_materialno);
        et_materiallotno = (EditText) findViewById(R.id.et_materiallotno);
        sp_materialjiano = (Spinner) findViewById(R.id.sp_materialjiano);
        sp_materialdeviceno = (Spinner) findViewById(R.id.sp_materialdeviceno);

        switch (typeflag) {
            case "2":
                ll_selectdev.setVisibility(View.GONE);
                ll_selectbh.setVisibility(View.GONE);
                initMaterialno();
                break;

            case "3":
                ll_selectdev.setVisibility(View.GONE);
                tv_bh.setText("選擇烤箱編號");
                initMaterialno();
                initBH();
                break;

            case "4":
                tv_bh.setText("選擇氮氣櫃編號");
                initDeviceno();
                break;
            default:
                break;
        }

        sp_materialno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialno=sp_materialno.getItemAtPosition(position).toString();
                if(materialno!=null){
                    initMaterialName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_materialjiano.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialjiano=sp_materialjiano.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_materialdeviceno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deviceno=sp_materialdeviceno.getItemAtPosition(position).toString();
                if(deviceno!=null){
                    initMaterialno();
                    initBH();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initDeviceno() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String> list1 = asop.getMaterialDeviceno(typeflag);
                Message msg = new Message();
                msg.what = 4;
                msg.obj = list1;
                handler.sendMessage(msg);
            }
        });
    }

    private void initBH() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String> list1 = asop.getBH(typeflag, deviceno);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = list1;
                handler.sendMessage(msg);
            }
        });
    }

    private void initMaterialName() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String str1 = asop.getMaterialname(materialno);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = str1;
                handler.sendMessage(msg);
            }
        });
    }

    private void initMaterialno() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String> list1 = asop.getMaterialno(typeflag, deviceno);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = list1;
                handler.sendMessage(msg);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String resultStr = (String) msg.obj;
                    if (resultStr != null) {
                        String[] temps=resultStr.split(":");
                        String flag=temps[0];
                        if(flag.equals("1")){
                            String materiallotstatus = temps[1];
                            String qty = temps[2];
                            if (materiallotstatus.equals("0")) {
                                final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_materiainactivity_materiallotno, null);
                                TextView tv_materiallotno = (TextView) view.findViewById(R.id.tv_materiallotno);
                                TextView tv_materialno = (TextView) view.findViewById(R.id.tv_materialno);
                                TextView tv_materialname = (TextView) view.findViewById(R.id.tv_materialname);
                                TextView tv_materiallotstatus = (TextView) view.findViewById(R.id.tv_lotnostatus);
                                TextView tv_materialdeviceno = (TextView) view.findViewById(R.id.tv_materialdeviceno);
                                TextView tv_materialbh = (TextView) view.findViewById(R.id.tv_materialbh);
                                TextView tv_materialnum = (TextView) view.findViewById(R.id.tv_materialnum);
                                TextView tv_typeflag = (TextView) view.findViewById(R.id.tv_typeflag);

                                et_materiallotno.setText(materiallotno);
                                tv_materiallotno.setText(materiallotno);
                                tv_materialno.setText(materialno);
                                tv_materialname.setText(materialname);
                                tv_materiallotstatus.setText(materiallotstatus);

                                if(typeflag.equals("2")){
                                    tv_materialdeviceno.setText("NULL");
                                    tv_materialbh.setText("NULL");
                                }else if(typeflag.equals("3")){
                                    tv_materialdeviceno.setText("NULL");
                                    tv_materialbh.setText(materialjiano);
                                }else if(typeflag.equals("4")){
                                    tv_materialdeviceno.setText(deviceno);
                                    tv_materialbh.setText(materialjiano);
                                }else{
                                    messgeStr = "typpflag错误";
                                    Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                tv_typeflag.setText(typeflag);
                                tv_materialnum.setText(qty);


                                view.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaterialInActivity.this);
                                        alertDialog.setTitle("溫馨提示");
                                        alertDialog.setMessage("是否從列表中刪除");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ll_materiallotnolist.removeView(view);
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

                                ll_materiallotnolist.addView(view);

                            } else if (materiallotstatus.equals("1")) {
                                messgeStr = "該物料批號已經有入庫記錄";
                                Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                            } else if (materiallotstatus.equals("2")) {
                                messgeStr = "該物料批號已經有出庫記錄";
                                Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                            } else {
                                messgeStr = "該物料批號狀態有問題";
                                Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            messgeStr = resultStr ;
                            Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                        }

                        }

                    break;

                case 1:
                    List<String> list_materialno= (List<String>) msg.obj;
                    if(list_materialno!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialInActivity.this, R.layout.spinner_textview,list_materialno);
                        sp_materialno.setAdapter(adapter);
                    }else {
                        messgeStr = "查詢物料料號出錯";
                        Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    break;

                case 2:
                    materialname= (String) msg.obj;
                    if(materialname!=null){
                        et_materialno.setText(materialname);
                    }else {
                        messgeStr = "查詢物料名稱出錯";
                        Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    break;

                case 3:
                    List<String> list_materialjiano= (List<String>) msg.obj;
                    if(list_materialjiano!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialInActivity.this, R.layout.spinner_textview,list_materialjiano);
                        sp_materialjiano.setAdapter(adapter);
                    }else {
                        messgeStr = "查詢編號出錯";
                        Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    break;

                case 4:
                    List<String> list_materialdeviceno= (List<String>) msg.obj;
                    if(list_materialdeviceno!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialInActivity.this, R.layout.spinner_textview,list_materialdeviceno);
                        sp_materialdeviceno.setAdapter(adapter);
                    }else {
                        messgeStr = "查詢物料機種出錯";
                        Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    break;

                case 5:
                    String resultstr= (String) msg.obj;
                    if(resultstr!=null){
                        messgeStr = resultstr;
                        Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                        String[] results=resultstr.split(":");
                        if(results[0].equals("1")){
                            ll_materiallotnolist.removeAllViews();
                            et_materiallotno.setText("");
                        }

                    }else {
                        messgeStr = "保存數據出錯";
                        Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    }
                    tv_result2.setText(messgeStr);
                    break;

                default:
                    break;

            }
        }
    };

    public void onClick_submit_in(View view) {
        List<MaterialLotnoBean> list_materiallotno=new ArrayList<>();
        if(ll_materiallotnolist.getChildCount()>0){
            for (int i=0;i<ll_materiallotnolist.getChildCount();i++){
                View view1=ll_materiallotnolist.getChildAt(i);
                TextView tv_materiallotno1 = (TextView) view1.findViewById(R.id.tv_materiallotno);
                TextView tv_materialno1 = (TextView) view1.findViewById(R.id.tv_materialno);
                TextView tv_materialname1 = (TextView) view1.findViewById(R.id.tv_materialname);
                TextView tv_materialdeviceno1 = (TextView) view1.findViewById(R.id.tv_materialdeviceno);
                TextView tv_materialbh1 = (TextView) view1 .findViewById(R.id.tv_materialbh);
                TextView tv_materialnum1 = (TextView) view1.findViewById(R.id.tv_materialnum);
                TextView tv_typeflag = (TextView) view1 .findViewById(R.id.tv_typeflag);

                String materiallotno1=tv_materiallotno1.getText().toString();
                String materialno1=tv_materialno1.getText().toString();
                String materialname1=tv_materialname1.getText().toString();
                String materialdeviceno1=tv_materialdeviceno1.getText().toString();
                String materialbh1=tv_materialbh1.getText().toString();
                String materialnum1 = tv_materialnum1.getText().toString();
                String typeflag1=tv_typeflag.getText().toString();

                MaterialLotnoBean mlb = new MaterialLotnoBean();
                mlb.setLotno(materiallotno1);
                mlb.setMaterialno(materialno1);
                mlb.setMaterialname(materialname1);
                mlb.setDeviceno(materialdeviceno1);
                mlb.setBh(materialbh1);
                mlb.setTypeflag(typeflag1);
                mlb.setQty(materialnum1);
                mlb.setLotnostatus("1");
                list_materiallotno.add(mlb);
            }
            String datastr = new Gson().toJson(list_materiallotno);
            insertintoData(datastr);
        }
    }

    private void insertintoData( final String datastr) {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String resultStr = asop.saveMaterialLotnos(datastr);
                Message msg = new Message();
                msg.what = 5;
                msg.obj = resultStr;
                handler.sendMessage(msg);
            }
        });
    }

    //掃描物料批號
    public void onClick_scan(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
//        materiallotno="P10430036-126";
//        if(checkmateriallotnoisExit()){
//            messgeStr = "該物料批號已經扫描";
//            Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
//            return;
//        }
//        checkMateriallotnoStatus();
    }

    //判断批号是否已经扫描
    private boolean checkmateriallotnoisExit() {
        boolean flag=false;
        for(int i=0;i<ll_materiallotnolist.getChildCount();i++){
            View view = ll_materiallotnolist.getChildAt(i);;
            TextView tv= (TextView) view.findViewById(R.id.tv_materiallotno);
            String mlotno=tv.getText().toString();
            if(materiallotno.equals(mlotno)){
                flag=true;
                break;
            }
        }
        return flag;
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
                    Toast.makeText(MaterialInActivity.this, messgeStr, Toast.LENGTH_LONG).show();
                    return;
                }
                checkMateriallotnoStatus();
            }
        }
    }

    //查詢物料批號的狀態
    private void checkMateriallotnoStatus() {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String resultStr = asop.getMateriallotStatus(materiallotno,materialno,deviceno);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = resultStr;
                handler.sendMessage(msg);
            }
        });
    }
}
