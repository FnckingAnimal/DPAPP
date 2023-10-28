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

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class geterpissueorderdetail extends MyAppComPatActivity implements FreedomDataCallBack {
    //用户名
    private static String Sessionuser;
    private LinearLayout lli;
    String sysid;
    String machinename;
    String lotno;
    String dtime;

    private TextView tv_sysid, tv_machineno, tv_lotno, tv_dtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isfc_erpissueorderdetail);

        Bundle bundle = this.getIntent().getExtras();
        try {
            sysid = bundle.getString("sysid");
            if (sysid == null || sysid.trim().equals("")) {
                Toast.makeText(this, "發料單號不允許為空", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }

            machinename = bundle.getString("machinename");
            if (machinename == null || machinename.trim().equals("")) {
                Toast.makeText(this, "機台號不允許為空", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }

            lotno = bundle.getString("lotno");
            if (lotno == null || lotno.trim().equals("")) {
                Toast.makeText(this, "批號不允許為空", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }

            dtime = bundle.getString("dtime");

        } catch (Exception ex1) {
            Toast.makeText(this, "批號發料單信息異常", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        tv_sysid = (TextView) findViewById(R.id.isfc_geterpissueorderdetail_ordernotv1);
        tv_machineno = (TextView) findViewById(R.id.isfc_geterpissueorderdetail_ordernotv2);
        tv_dtime = (TextView) findViewById(R.id.isfc_geterpissueorderdetail_ordernotv3);
        tv_lotno = (TextView) findViewById(R.id.isfc_geterpissueorderdetail_ordernotv4);
        lli = (LinearLayout) findViewById(R.id.isfc_geterpissueorderdetail_dll1);

        tv_sysid.setText(sysid);
        tv_machineno.setText(machinename);
        tv_dtime.setText(dtime);
        tv_lotno.setText(lotno);

        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

        loadingdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadingdata() {
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getsysidinfo");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("sysid");
        hhi2.setDatavalue(sysid);
        li.add(hhi2);

        getkeyindatainput(url1, li, 1, this, this);
    }


    @Override
    public void onStart(Context C1) {
        execloadactivity.opendialog(C1, "正在執行");
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
        Toast.makeText(geterpissueorderdetail.this
                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }

    @Override
    public void onNoneImage(Context C1) {
        Toast.makeText(geterpissueorderdetail.this
                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }

    public void listview13_bt_onClick_shouliao(View view) {

        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("submitshouliao");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("sysid");
        hhi2.setDatavalue(sysid);
        li.add(hhi2);
        httprequestinputdata hhi3 = new httprequestinputdata();
        hhi3.setDataname("userid");
        hhi3.setDatavalue(Sessionuser);
        li.add(hhi3);

        getkeyindatainput(url1, li, 1, this, this);
    }

    public void fillingdata(List<jsontolist._jsonarray> j1) {
        try {
            if (j1 != null) {
                if (j1.get(0).get_arraystatus() == Statusinterface.STATUS_SEC) {
                    String nextstep = j1.get(0).get_arrayname();
                    switch (nextstep) {
                        case "getsysidinfo":
                            View convertView;
                            for (int i = 0; i < j1.get(0).get_valuelist().size(); i++) {
                                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source13, null);

                                ((TextView) convertView.findViewById(R.id.listview13_tv1)).setText(j1.get(0).get_valuelist().get(i).get(0).get_value());
                                ((TextView) convertView.findViewById(R.id.listview13_tv2)).setText(j1.get(0).get_valuelist().get(i).get(1).get_value());
                                ((TextView) convertView.findViewById(R.id.listview13_tv3)).setText(j1.get(0).get_valuelist().get(i).get(2).get_value());
                                ((TextView) convertView.findViewById(R.id.listview13_tv4)).setText(j1.get(0).get_valuelist().get(i).get(3).get_value());
                                ((TextView) convertView.findViewById(R.id.listview13_tv5)).setText(j1.get(0).get_valuelist().get(i).get(4).get_value());
                                lli.addView(convertView);
                            }
                            break;
                        case "submitshouliao":
                            String msg = j1.get(0).get_valuelist().get(0).get(0).get_value();
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("result", msg);
                            resultIntent.putExtras(bundle);
                            this.setResult(RESULT_OK, resultIntent);
                            this.finish();
                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(this, "HttpRepsoneJsonToListError", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception ex) {
            Toast.makeText(this, "HttpRepsoneUnknowError-" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

}




