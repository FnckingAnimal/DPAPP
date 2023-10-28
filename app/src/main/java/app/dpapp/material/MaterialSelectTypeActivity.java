package app.dpapp.material;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.MaterialTypeBean;
import app.dpapp.soap.AppleSOAP;

public class MaterialSelectTypeActivity extends AppCompatActivity {

    private Spinner sp_materialtype;
    private String materialtype,typeflag;
    private String str_messge;
    private List<MaterialTypeBean> list_materialtype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_select_type);
        //根据发料单和料号获取已发数量

        sp_materialtype= (Spinner) findViewById(R.id.sp_materialtype);

        list_materialtype=new ArrayList<>();

        sp_materialtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialtype=sp_materialtype.getItemAtPosition(position).toString(); //获取选择的物料类型
                typeflag=list_materialtype.get(position).typeflag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //获取物料类型
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<MaterialTypeBean> list1 = asop.getMaterialType();
                Message msg = new Message();
                msg.what = 0;
                msg.obj = list1;
                handler.sendMessage(msg);
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 0:
                    list_materialtype = (List<MaterialTypeBean>) msg.obj;
                    List<String> list1=new ArrayList<>();
                    if(list_materialtype!=null){
                        for(int i=0;i<list_materialtype.size();i++){
                            list1.add(list_materialtype.get(i).materialtype);
                        }
                        ArrayAdapter adapter=new ArrayAdapter(MaterialSelectTypeActivity.this, R.layout.spinner_textview,list1);
                        sp_materialtype.setAdapter(adapter);
                    }
                    break;

                default:
                    break;

            }
        }
    };

    public void onClick_materialin(View v){
        if(materialtype != null && materialtype != ""){
            //扫描批号，根据扫描的批号状态选择进入哪个页面
            Intent intentin = new Intent(MaterialSelectTypeActivity.this, MaterialInActivity.class);
            Bundle bundlein=new Bundle();
            bundlein.putString("typeflag",typeflag);
            intentin.putExtras(bundlein);
            startActivity(intentin);
        }
    }

    public void onClick_materialout(View v){
        if(materialtype != null && materialtype != ""){
            //扫描批号，根据扫描的批号状态选择进入哪个页面
            Intent intentout = new Intent(MaterialSelectTypeActivity.this, MaterialOutActivity.class);
            Bundle bundleout=new Bundle();
            bundleout.putString("typeflag",typeflag);
            intentout.putExtras(bundleout);
            startActivity(intentout);
        }
    }
}
