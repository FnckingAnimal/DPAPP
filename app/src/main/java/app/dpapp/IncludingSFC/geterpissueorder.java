package app.dpapp.IncludingSFC;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.Interfacearray.Statusinterface;
import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.MyAppComPatActivity;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.appcdl.jsontolist;
import app.dpapp.parameterclass.httprequestinputdata;
import app.dpapp.zxing.activity.CaptureActivity;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class geterpissueorder extends MyAppComPatActivity implements FreedomDataCallBack {
    //用户名
    private static String Sessionuser;
    String lotno;
    LinearLayout lli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isfc_erpissueorder);
        lli=(LinearLayout) findViewById(R.id.isfc_geterpissueorderlist_dll1);

        Bundle bundle = this.getIntent().getExtras();
        try {
            lotno = bundle.getString("lotno");
            //Eqname=bundle.getString("Eqname");
            if (lotno == null || lotno.trim().equals("")) {
                Toast.makeText(this, "掃描的批號為空", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        } catch (Exception ex1) {
            Toast.makeText(this, "獲取批號信息異常", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

        loadingdata();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void isfc_geterpissueorderlist_scanclick(View v)
    {
        Intent openCameraIntent = new Intent(this,CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            switch (requestCode) {
                case 0:
                    lli.removeAllViews();
                    String url1 = Staticdata.httpurl + "BFMC/getbmfc_geterporder.ashx";
                    List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
                    httprequestinputdata hhi1 = new httprequestinputdata();
                    hhi1.setDataname("directpar");
                    hhi1.setDatavalue("getissueorderlist");
                    li.add(hhi1);
                    httprequestinputdata hhi2 = new httprequestinputdata();
                    hhi2.setDataname("lotno");
                    hhi2.setDatavalue(scanResult);
                    li.add(hhi2);
                    httprequestinputdata hhi3 = new httprequestinputdata();
                    hhi3.setDataname("type");
                    hhi3.setDatavalue("A");
                    li.add(hhi3);
                    getkeyindatainput(url1, li, 1, this, this);
                    break;

            }
        }
    }

    public void loadingdata() {
        //Loading 物料清單信息
        //http://10.142.136.222:8107/BMFC/getbmfc_callmaterial.ashx?directpar=geteqmaterialtable&Eqid=M1311F001-025
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_geterporder.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getissueorderlist");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("lotno");
        hhi2.setDatavalue(lotno);
        li.add(hhi2);
        httprequestinputdata hhi3 = new httprequestinputdata();
        hhi3.setDataname("type");
        hhi3.setDatavalue("A");
        li.add(hhi3);
        getkeyindatainput(url1, li, 1, this, this);
    }

    @Override
    public void onStart(Context C1) {
        execloadactivity.opendialog(C1,"正在執行");
    }

    @Override
    public void processData(Object paramObject, boolean paramBoolean) {
        jsontolist js = new jsontolist();
        fillingdata(js.jasontolist(paramObject.toString()));
    }

    @Override
    public void onFinish(Context C1) {
        execloadactivity.canclediglog();
    }

    @Override
    public void onFailed(Object paramObject, Context C1) {
        Toast.makeText(geterpissueorder.this
                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }


    @Override
    public void onNoneImage(Context C1) {
        Toast.makeText(geterpissueorder.this
                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }

    public void fillingdata(List<jsontolist._jsonarray> j1) {
        if (j1 != null) {
            try {
                if (j1.get(0).get_arraystatus() == Statusinterface.STATUS_SEC) {
                    String nextstep = j1.get(0).get_valuelist().get(0).get(0).get_value();
                    switch (nextstep) {
                        case "getissueorderlist":
                            View convertView;
                            for (int i = 0; i < j1.get(1).get_valuelist().size(); i++) {
                                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source17, null);
                                ((TextView) convertView.findViewById(R.id.ls14_item3name)).setText(lotno);
                                ((TextView) convertView.findViewById(R.id.ls14_item1name)).setText(j1.get(1).get_valuelist().get(i).get(0).get_value());
                                ((TextView) convertView.findViewById(R.id.ls14_item1value)).setText(j1.get(1).get_valuelist().get(i).get(1).get_value());
                                ((TextView) convertView.findViewById(R.id.ls14_item2value)).setText(j1.get(1).get_valuelist().get(i).get(2).get_value());
                                convertView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent getissueorderdetaili = new Intent(v.getContext(), geterpissueorderdetail.class);
                                        Bundle SFCERPORDERbundleer = new Bundle();
                                        SFCERPORDERbundleer.putString("lotno",
                                                lotno
                                        );
                                        SFCERPORDERbundleer.putString("issueno",
                                                ((TextView) v.findViewById(R.id.ls14_item1name)).getText().toString()
                                        );
                                        SFCERPORDERbundleer.putString("wono",
                                                ((TextView) v.findViewById(R.id.ls14_item1value)).getText().toString()
                                        );

                                        SFCERPORDERbundleer.putString("status",
                                                ((TextView) v.findViewById(R.id.ls14_item2value)).getText().toString()
                                        );

                                        getissueorderdetaili.putExtras(SFCERPORDERbundleer);
                                        startActivity(getissueorderdetaili);
                                    }
                                });
                                lli.addView(convertView);
                                //j1.get(i).get_valuelist().get
                            }
                            break;
                    }
                }
                else
                {
                    Toast.makeText(this,"查詢失敗",Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(this,"AnalyzeHttpResponseDataError",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

