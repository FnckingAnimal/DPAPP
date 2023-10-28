package app.dpapp.material;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;

public class MaterialLotnoListActivity extends AppCompatActivity {

    private String deviceno,shopno,productno,jiano;
    private Spinner sp_deviceno,sp_shopno,sp_productno,sp_jiano;
    private LinearLayout ll_showdata;
    private EditText et_count;
    private String loadflag;//設置一個標識，來標識加載順序。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_lotno_list);

        sp_deviceno= (Spinner) findViewById(R.id.material_lotno_list_sp_materialdeviceno);
        sp_shopno= (Spinner) findViewById(R.id.material_lotno_list_sp_materialshopno);
        sp_productno= (Spinner) findViewById(R.id.material_lotno_list_sp_productno);
        sp_jiano= (Spinner) findViewById(R.id.material_lotno_list_sp_materiajiano);

        ll_showdata= (LinearLayout) findViewById(R.id.material_lotno_ll_list);
        et_count= (EditText) findViewById(R.id.material_lotno_list_et_count);

        sp_deviceno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deviceno=sp_deviceno.getSelectedItem().toString();
                if(deviceno!=null && !deviceno.equals("")){
                    List<SOAPParameter> list=new ArrayList<>();
                    list.add(new SOAPParameter("deviceno",deviceno));
                    loadshopnodata(list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_shopno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shopno=sp_shopno.getSelectedItem().toString();
                if(sp_shopno!=null && !sp_shopno.equals("")){
                    List<SOAPParameter> list=new ArrayList<>();
                    list.add(new SOAPParameter("deviceno",deviceno));
                    list.add(new SOAPParameter("shopno",shopno));
                    loadproductnodata(list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_productno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productno=sp_productno.getSelectedItem().toString();
                if(productno!=null && !productno.equals("")){
                    List<SOAPParameter> list=new ArrayList<>();
                    list.add(new SOAPParameter("deviceno",deviceno));
                    list.add(new SOAPParameter("shopno",shopno));
                    list.add(new SOAPParameter("productno",productno));
                    loadjianodata(list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_jiano.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jiano=sp_jiano.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loaddevicenodata();
    }

    private void loadjianodata(final List<SOAPParameter> parameterList) {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<String> list = asop.getMaterialjiajiano(parameterList);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = list;
                handle.sendMessage(msg);
            }
        });
    }

    private void loadproductnodata(final List<SOAPParameter> parameterList) {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<String> list = asop.getMaterialjiaPro(parameterList);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = list;
                handle.sendMessage(msg);
            }
        });
    }

    private void loadshopnodata(final List<SOAPParameter> parameterList) {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<String> list = asop.getMaterialjiashopno(parameterList);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = list;
                handle.sendMessage(msg);
            }
        });
    }

    private void loaddevicenodata() {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<String> list = asop.getMaterialjiadevicno(null);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = list;
                handle.sendMessage(msg);
            }
        });
    }

    private Handler handle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<String> list_dev= (List<String>) msg.obj;
                    if(list_dev!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialLotnoListActivity.this,R.layout.spinner_textview,list_dev);
                        sp_deviceno.setAdapter(adapter);
                    }
                    break;
                case 1:
                    List<String> list_shopno= (List<String>) msg.obj;
                    if(list_shopno!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialLotnoListActivity.this,R.layout.spinner_textview,list_shopno);
                        sp_shopno.setAdapter(adapter);
                    }
                    break;

                case 2:
                    List<String> list_productno= (List<String>) msg.obj;
                    if(list_productno!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialLotnoListActivity.this,R.layout.spinner_textview,list_productno);
                        sp_productno.setAdapter(adapter);
                    }
                    break;
                case 3:
                    List<String> list_jiano= (List<String>) msg.obj;
                    if(list_jiano!=null){
                        ArrayAdapter adapter=new ArrayAdapter(MaterialLotnoListActivity.this,R.layout.spinner_textview,list_jiano);
                        sp_jiano.setAdapter(adapter);
                    }
                    break;
                case 4:
                    List<String[]> list_info= (List<String[]>) msg.obj;
                    if(list_info!=null){
                        int sum=0;
                        for(int i=0;i<list_info.size();i++){
                            View view = LayoutInflater.from(MaterialLotnoListActivity.this).inflate(R.layout.item1_material_lotno_list,null);
                            TextView tv0= (TextView) view.findViewById(R.id.item_material_lotno_list_tv0);
                            TextView tv1= (TextView) view.findViewById(R.id.item_material_lotno_list_tv1);
                            TextView tv2= (TextView) view.findViewById(R.id.item_material_lotno_list_tv2);
                            TextView tv3= (TextView) view.findViewById(R.id.item_material_lotno_list_tv3);
                            TextView tv4= (TextView) view.findViewById(R.id.item_material_lotno_list_tv4);
                            TextView tv5= (TextView) view.findViewById(R.id.item_material_lotno_list_tv5);
                            TextView tv6= (TextView) view.findViewById(R.id.item_material_lotno_list_tv6);
                            tv0.setText(list_info.get(i)[0]);
                            tv1.setText(list_info.get(i)[1]);
                            tv2.setText(list_info.get(i)[2]);
                            tv3.setText(list_info.get(i)[3]);
                            tv4.setText(list_info.get(i)[4]);
                            tv5.setText(list_info.get(i)[5]);
                            tv6.setText(list_info.get(i)[6]);
                            sum += Integer.parseInt(list_info.get(i)[5]);
                            ll_showdata.addView(view);
                        }
                        et_count.setText(String.valueOf(sum));
                    }
                    break;
                default:

                    break;
            }
        }
    };

    public void material_lotno_list_onclick_query(View view) {
        if(deviceno==null || deviceno.equals("")){
            Toast.makeText(MaterialLotnoListActivity.this,"機種不能為空",Toast.LENGTH_LONG);
            return;
        }
        if(shopno==null || shopno.equals("")){
            Toast.makeText(MaterialLotnoListActivity.this,"段別不能為空",Toast.LENGTH_LONG);
            return;
        }
        if(productno==null || productno.equals("")){
            Toast.makeText(MaterialLotnoListActivity.this,"物料料號不能為空",Toast.LENGTH_LONG);
            return;
        }
        if(jiano==null || jiano.equals("")){
            Toast.makeText(MaterialLotnoListActivity.this,"物料架編碼不能為空",Toast.LENGTH_LONG);
            return;
        }

        ll_showdata.removeAllViews();
        List<SOAPParameter> list=new ArrayList<>();
        list.add(new SOAPParameter("deviceno",deviceno));
        list.add(new SOAPParameter("shopno",shopno));
        list.add(new SOAPParameter("productno",productno));
        list.add(new SOAPParameter("jiano",jiano));
        getLotnoinDataByMaterialno(list);
    }

    private void getLotnoinDataByMaterialno(final List<SOAPParameter> parameterList) {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<String[]> list = asop.getLotnoinDataByMaterialno(parameterList);
                Message msg = new Message();
                msg.what = 4;
                msg.obj = list;
                handle.sendMessage(msg);
            }
        });
    }
}