package app.dpapp.IncludingSFC;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.Interfacearray.Statusinterface;
import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.FinalStaticCloass;
import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.exechttprequest;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.appcdl.jsontolist;
import app.dpapp.parameterclass.httprequestinputdata;
import app.dpapp.zxing.activity.CaptureActivity;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class mcmaterialcall extends AppCompatActivity implements FreedomDataCallBack {
    //用户名
    private static String Sessionuser;
    String Eqid;
    String Eqname;

    private TextView eqidtv1;
    private TextView eqnametv2,unittv;
    private Spinner devicenosp1;
    private Spinner materialidsp1;
    private TextView lotnoet1;
    private EditText qtyet1,materialnameet1;

    private static ArrayAdapter<String> mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isfc_mcmaterialcall);

        Bundle bundle = getIntent().getExtras();
        try {
            Eqid = bundle.getString("Eqid");
            //Eqname=bundle.getString("Eqname");
            if (Eqid == null || Eqid.trim().equals("")) {
                Toast.makeText(this, "設備ID不允許為空", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception ex1) {
            Toast.makeText(this, "設備信息異常", Toast.LENGTH_SHORT).show();
            finish();
        }

        eqidtv1 = (TextView) findViewById(R.id.isfc_mamaterialcall_eqsysidlabel1);
        eqnametv2 = (TextView) findViewById(R.id.isfc_mamaterialcall_eqsysnamelabel1);

        devicenosp1 = (Spinner) findViewById(R.id.isfc_mamaterialcall_devicenosp1);
        materialidsp1 = (Spinner) findViewById(R.id.isfc_mamaterialcall_materialidsp1);
        materialnameet1 = (EditText) findViewById(R.id.isfc_mamaterialcall_materialnameet1);
        unittv= (TextView) findViewById(R.id.isfc_mamaterialcall_uniltv1);

        lotnoet1 = (TextView) findViewById(R.id.isfc_mamaterialcall_lotnoet1);
        qtyet1 = (EditText) findViewById(R.id.isfc_mamaterialcall_qtyet1);

        eqidtv1.setText(Eqid);
        qtyet1.setText("1");

        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

        loadingdata();

        materialidsp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String materialid = ((FinalStaticCloass.SpinnerData) materialidsp1.getSelectedItem()).getValue();
                getmaterialunit(materialid);
                geteqmaterialname(materialid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadingdata() {
        //Loading 物料清單信息
        //http://10.142.136.222:8107/BMFC/getbmfc_callmaterial.ashx?directpar=geteqmaterialtable&Eqid=M1311F001-025
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("geteqmaterialtable");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("Eqid");
        hhi2.setDatavalue(Eqid);
        li.add(hhi2);
        getkeyindatainput(url1, li, 1);

        //getdevicelist("");
        //getmaterialunit("");
//        List<httprequestinputdata> li1 =new ArrayList<>();
//        httprequestinputdata h11=new httprequestinputdata();
//        h11.setDataname("directpar");
//        h11.setDatavalue("geteqrepairmachinedata");
//        li1.add(h11);
//        httprequestinputdata h12=new httprequestinputdata();
//        h12.setDataname("machinesysid");
//        h12.setDatavalue(Eqid);
//        li1.add(h12);
//        getkeyindatainput(Staticdata.httpurl + "machinedata/machine_accept.ashx", li1, 2);

    }

    private void getdevicelist(String lotno) {
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getlotdeviceno");
        li.add(hhi1);
        if (lotno != null) {
            httprequestinputdata hhi2 = new httprequestinputdata();
            hhi2.setDataname("Lotno");
            hhi2.setDatavalue(lotno);
            li.add(hhi2);
        }
        getkeyindatainput(url1, li, 1);
    }

    private void getmaterialunit(String materialid) {
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("geteqmaterialunit");
        li.add(hhi1);
        if (materialid != null) {
            httprequestinputdata hhi2 = new httprequestinputdata();
            hhi2.setDataname("Materialid");
            hhi2.setDatavalue(materialid);
            li.add(hhi2);
        }
        getkeyindatainputnew(url1, li, 1,"unit");
    }

    private void geteqmaterialname(String materialid) {
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("geteqmaterialname");
        li.add(hhi1);
        if (materialid != null) {
            httprequestinputdata hhi2 = new httprequestinputdata();
            hhi2.setDataname("Materialid");
            hhi2.setDatavalue(materialid);
            li.add(hhi2);
        }
        getkeyindatainputnew(url1, li, 1,"name");
    }

    public void getkeyindatainputnew(String urlpath, List<httprequestinputdata> list, int sendmode, final String type) {
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(sendmode, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {

                        if (paramObject != null && !"".equals(paramObject)) {
                            String resultStr= (String) paramObject;
                            String[] results=resultStr.split(":");
                            if(results[0].equals("1")){
                                if(type.equals("unit")){
                                    if(results.length<3)
                                    unittv.setText("");
                                }
                                if(type.equals("name")){
                                    materialnameet1.setText(results[2]);
                                }

                            }else{
                                Toast.makeText(mcmaterialcall.this, results[2], Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mcmaterialcall.this, "請求數據為空", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(mcmaterialcall.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(mcmaterialcall.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, list, null);

    }

    public void getkeyindatainput(String urlpath, List<httprequestinputdata> li, int sendmode) {
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(sendmode, this, this, urlpath, li, null);
    }

    @Override
    public void onStart(Context C1) {
        execloadactivity.opendialog(C1, "正在執行");
    }

    @Override
    public void processData(Object paramObject, boolean paramBoolean) {
        if (paramObject != null && !"".equals(paramObject)) {
            jsontolist js = new jsontolist();
            fillingdata(js.jasontolist(paramObject.toString()));
        }
    }

    @Override
    public void onFinish(Context C1) {
        execloadactivity.canclediglog();
        Toast.makeText(mcmaterialcall.this
                , "提交成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailed(Object paramObject, Context C1) {
        if (paramObject != null && !"".equals(paramObject)) {
            Toast.makeText(mcmaterialcall.this
                    , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
            execloadactivity.canclediglog();
        }
    }

    @Override
    public void onNoneImage(Context C1) {
        Toast.makeText(mcmaterialcall.this
                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }

    public void fillingdata(List<jsontolist._jsonarray> j1) {
        if (j1 != null) {
            if (j1.get(0).get_arraystatus() == Statusinterface.STATUS_SEC) {
                String nextstep = null;
                if (j1.get(0).get_valuelist() != null) {
                    nextstep = j1.get(0).get_valuelist().get(0).get(0).get_value();
                }
                //Log.v("CMSF",nextstep);
                BaseFuncation.createspdata bc1 = new BaseFuncation().new createspdata();
                switch (nextstep) {
                    case "geteqmaterialtable":
                        if (j1.get(0).get_valuelist() != null) {

                            eqnametv2.setText(j1.get(1).get_valuelist().get(0).get(0).get_value());
                            for (int i = 0; i < j1.get(1).get_valuelist().size(); i++) {
                                bc1.setvalue(j1.get(1).get_valuelist().get(i).get(1).get_value(),
                                        j1.get(1).get_valuelist().get(i).get(1).get_value());
                                //j1.get(i).get_valuelist().get
                            }
                            materialidsp1.setAdapter(bc1.getspdata(this));
                        }
                        break;
                    case "geteqmaterialunit":
                        if (j1.get(1).get_valuelist() != null) {
                            unittv.setText(j1.get(1).get_valuelist().get(0).get(0).get_value());
                        }
                        break;
                    case "createcallmaterialtask":
                        String status1 = null;
                        String msg1 = null;
                        if (j1.get(0).get_valuelist() != null) {
                            status1 = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            msg1 = j1.get(0).get_valuelist().get(0).get(2).get_value();
                        }
                        switch (status1) {
                            case "1":
                                Toast.makeText(this, "叫料成功:" + msg1, Toast.LENGTH_LONG).show();
                                finish();
                                break;
                            default:
                                Toast.makeText(this, "叫料失敗:" + msg1, Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;
                    case "getlotdeviceno":
                        if (j1.get(1).get_valuelist() != null) {
                            for (int i = 0; i < j1.get(1).get_valuelist().size(); i++) {
                                bc1.setvalue(j1.get(1).get_valuelist().get(i).get(1).get_value(),
                                        j1.get(1).get_valuelist().get(i).get(0).get_value());
                                //j1.get(i).get_valuelist().get
                            }
                            devicenosp1.setAdapter(bc1.getspdata(this));
                        }
                        break;
                    case "geteqrepairmachinedata":
                        String status2 = null;
                        String msg2 = null;
                        if (j1.get(0).get_valuelist() != null) {
                            status2 = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            msg2 = j1.get(0).get_valuelist().get(0).get(2).get_value();
                        }
                        switch (status2) {
                            case "1":
                                if (j1.get(1).get_valuelist() == null) {
                                    Toast.makeText(this, "無資料", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    eqnametv2.setText(j1.get(1).get_valuelist().get(0).get(0).get_value());
                                }
                                break;
                            default:
                                Toast.makeText(this, msg2, Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void onscanclick(View v) {
        Intent openCameraIntent = new Intent(this,CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
//        String lotno = "S04730209-14";//--S04730209-14
//        lotnoet1.setText(lotno);
//        getdevicelist(lotno);
    }

    public void isfc_mamaterialcall_onsubmit(View v) {

//        Eqid:机台编号。Eg: M1311F001-025
//        Eqname:机台名称。Eg：FC25
//        Deviceno：机种。Eg：ATE001
//        Materialid：物料ID。Eg：M0001
//        Callqty：叫料数量。Eg：1
//        Userid：叫料人。Eg：S6811004
//        Lotno：批号。Eg：K65230131-08 ，可以为空
        String deviceno = null;
        String materialid = null;
        String eqname = eqnametv2.getText().toString();
        String qty = null;
        String lotno = null;

        try {
            deviceno = ((FinalStaticCloass.SpinnerData) devicenosp1.getSelectedItem()).getValue();
        } catch (Exception ex) {
            deviceno = null;
            Toast.makeText(this, "DeviceNo Error", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            materialid = ((FinalStaticCloass.SpinnerData) materialidsp1.getSelectedItem()).getValue();
        } catch (Exception ex) {
            materialid = null;
            Toast.makeText(this, "Mtype Error", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (qtyet1.getText() != null && !"".equals(qtyet1.getText())) {
                Integer.valueOf(qtyet1.getText().toString().trim());
                qty = qtyet1.getText().toString().trim();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Qty must int", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lotnoet1.getText() != null && !"".equals(lotnoet1.getText())) {
            lotno = lotnoet1.getText().toString().trim();
        }

        List<httprequestinputdata> li1 = new ArrayList<>();
        httprequestinputdata h11 = new httprequestinputdata();
        h11.setDataname("directpar");
        h11.setDatavalue("createcallmaterialtask");
        li1.add(h11);
        httprequestinputdata h12 = new httprequestinputdata();
        h12.setDataname("Eqid");
        h12.setDatavalue(Eqid);
        li1.add(h12);
        httprequestinputdata h13 = new httprequestinputdata();
        h13.setDataname("Eqname");
        h13.setDatavalue(eqname);
        li1.add(h13);
        httprequestinputdata h14 = new httprequestinputdata();
        h14.setDataname("Deviceno");
        h14.setDatavalue(deviceno);
        li1.add(h14);
        httprequestinputdata h15 = new httprequestinputdata();
        h15.setDataname("Materialid");
        h15.setDatavalue(materialid);
        li1.add(h15);
        httprequestinputdata h16 = new httprequestinputdata();
        h16.setDataname("Callqty");
        h16.setDatavalue(qty);
        li1.add(h16);
        httprequestinputdata h17 = new httprequestinputdata();
        h17.setDataname("Userid");
        h17.setDatavalue(Sessionuser);
        li1.add(h17);
        httprequestinputdata h18 = new httprequestinputdata();
        h18.setDataname("Lotno");
        h18.setDatavalue(lotno);
        li1.add(h18);

        getkeyindatainput(Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx", li1, 2);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imagedata) {
        try {
            if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
                return;
            }
            //此处的用于判断接收的Activity是不是你想要的那个
            if (requestCode == 0) {
                Bundle bundle = imagedata.getExtras();

//            String imageresult = bundle.getString("result");
//            Toast.makeText(this,imageresult,Toast.LENGTH_SHORT).show();
                String lotno= bundle.getString("result");
                if (lotno != null && !"".equals(lotno)) {
                    if (lotno.length() != 12)//length of lotno must be equal to 12
                    {
                        Toast.makeText(this, "請輸入正確的批號", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!lotno.contains("-")) {
                        Toast.makeText(this, "請輸入子批號", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "批號不能為空", Toast.LENGTH_SHORT).show();
                    return;
                }

                lotnoet1.setText(lotno);
                getdevicelist(lotno);
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Read Lotno Error", Toast.LENGTH_SHORT).show();
        }
    }
}

