package app.cmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.Repair.eqrepairengdatain;
import app.cmapp.Repair.eqrepiaraccept;
import app.dpapp.R;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class systest extends AppCompatActivity {

    Spinner et1;
    Spinner et2;
    Spinner et3;
    Spinner et4;
    Spinner et5;
    Spinner et6;
    Spinner et7;

    Button  bt1;
    Button  bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sys_test);
        et1 = (Spinner) findViewById(R.id.systest_indata1et);
        et2 = (Spinner) findViewById(R.id.systest_indata2et);
        et3 = (Spinner) findViewById(R.id.systest_indata3et);
        et4 = (Spinner) findViewById(R.id.systest_indata4et);
        et5 = (Spinner) findViewById(R.id.systest_indata5et);
        et6=(Spinner)findViewById(R.id.systest_indata6et);
        et7=(Spinner)findViewById(R.id.systest_indata7et);

        bt1=(Button)findViewById(R.id.systest_indata1bt);
        bt2=(Button)findViewById(R.id.systest_indata2bt);
        List<String> ls=new ArrayList<>();
        ls.add("M1311E022-025");

        List<String> ls2=new ArrayList<>();
        ls2.add("1");
        ls2.add("2");

        List<String> ls3=new ArrayList<>();
        ls3.add("ENG");
        ls3.add("MFG");

        List<String> ls4=new ArrayList<>();
        ls4.add("18");

        ArrayAdapter aad=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ls);
        et1.setAdapter(aad);

        ArrayAdapter aad2=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ls2);
        et2.setAdapter(aad2);

        ArrayAdapter aad3=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ls3);
        et6.setAdapter(aad3);

        ArrayAdapter aad4=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, ls4);
        et7.setAdapter(aad4);
    }

    protected void onResume() {
        super.onResume();
    }
    public void sysbtsubmit(View v) {

        String eqid=et1.getSelectedItem().toString();
        String finishrepairtype=et2.getSelectedItem().toString();

        Intent intent = new Intent(this
                , eqrepairengdatain.class);

        Bundle bundle = new Bundle();
        bundle.putString("Eqid", eqid);
        bundle.putString("finishrepairtype", finishrepairtype);
        bundle.putString("Eqname", "Test");
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void sysbtsubmittest(View v) {

        String eqid=et1.getSelectedItem().toString();
        String sowner=et6.getSelectedItem().toString();
        String flowid=et7.getSelectedItem().toString();

        Intent intent = new Intent(this
                , eqrepiaraccept.class);

        Bundle bundle = new Bundle();
        bundle.putString("Eqid", eqid);
        bundle.putString("sowner", sowner);
        bundle.putString("flowid", flowid);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}