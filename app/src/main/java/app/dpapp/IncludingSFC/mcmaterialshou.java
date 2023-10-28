package app.dpapp.IncludingSFC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.Interfacearray.Statusinterface;
import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.exechttprequest;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.appcdl.jsontolist;
import app.dpapp.parameterclass.httprequestinputdata;
import app.dpapp.utils.DialogShowUtil;

public class mcmaterialshou extends Activity implements FreedomDataCallBack {

    private String Eqid, resultstr,messgestr;
    private EditText et_machineno;
    private LinearLayout ll_materialinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcmaterialshou);

        Bundle bundle = this.getIntent().getExtras();
        Eqid = bundle.getString("Eqid");

        ll_materialinfo = (LinearLayout) findViewById(R.id.ll_materialinfo);
        et_machineno = (EditText) findViewById(R.id.et_machineno);
        et_machineno.setText(Eqid);

        loadingdata();
    }


    @Override
    protected void onResume() {
        super.onResume();
        onCreate(null);
    }

    public void loadingdata() {
        String url1 = Staticdata.httpurl + "BMFC/getbmfc_callmaterial.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getMachinelistnoinfo");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("Eqid");
        hhi2.setDatavalue(Eqid);
        li.add(hhi2);

        getkeyindatainput(url1, li, 1);
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
        jsontolist js = new jsontolist();
        fillingdata(js.jasontolist(paramObject.toString()));
    }

    @Override
    public void onFinish(Context C1) {
        execloadactivity.canclediglog();
    }

    @Override
    public void onFailed(Object paramObject, Context C1) {
        Toast.makeText(mcmaterialshou.this
                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }

    @Override
    public void onNoneImage(Context C1) {
        Toast.makeText(mcmaterialshou.this
                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
        execloadactivity.canclediglog();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    resultstr = (String) msg.obj;
                    messgestr=resultstr;
                    DialogShowUtil.dialogShow(mcmaterialshou.this,messgestr);
                default:
                    break;
            }
        }
    };

    public void fillingdata(List<jsontolist._jsonarray> j1) {
        try {
            if (j1 != null) {
                if (j1.get(0).get_arraystatus() == Statusinterface.STATUS_SEC) {
                    String nextstep = j1.get(0).get_arrayname();
                    switch (nextstep) {
                        case "getMachinelistnoinfo":
                            ll_materialinfo.removeAllViews();
                            View convertView;
                            for (int i = 0; i < j1.get(0).get_valuelist().size(); i++) {
                                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source17, null);
                                final String sysid = j1.get(0).get_valuelist().get(i).get(0).get_value();
                                String deviceno = j1.get(0).get_valuelist().get(i).get(1).get_value();
                                final String machinename = j1.get(0).get_valuelist().get(i).get(2).get_value();
                                final String lotno = j1.get(0).get_valuelist().get(i).get(3).get_value();
                                String materialname = j1.get(0).get_valuelist().get(i).get(4).get_value();
                                final String status = j1.get(0).get_valuelist().get(i).get(5).get_value();
                                final String ctime = j1.get(0).get_valuelist().get(i).get(6).get_value();

                                ((TextView) convertView.findViewById(R.id.list_view17_tv1)).setText(sysid);
                                ((TextView) convertView.findViewById(R.id.list_view17_tv2)).setText(deviceno);
                                ((TextView) convertView.findViewById(R.id.list_view17_tv3)).setText(machinename);
                                ((TextView) convertView.findViewById(R.id.list_view17_tv4)).setText(lotno);
                                ((TextView) convertView.findViewById(R.id.list_view17_tv5)).setText(materialname);
                                ((TextView) convertView.findViewById(R.id.list_view17_tv6)).setText(status);
                                ((TextView) convertView.findViewById(R.id.list_view17_tv7)).setText(ctime);
                                //j1.get(i).get_valuelist().get
                                convertView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (status.equals("0")) {
                                            Toast.makeText(mcmaterialshou.this, "等待发料中......", Toast.LENGTH_SHORT).show();
                                        } else if (status.equals("1")) {
                                            Intent intent = new Intent(v.getContext(), geterpissueorderdetail.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("sysid", sysid);
                                            bundle.putString("machinename", machinename);
                                            bundle.putString("lotno", lotno);
                                            bundle.putString("dtime", ctime);
                                            intent.putExtras(bundle);
                                            startActivityForResult(intent, 0, bundle);
                                        } else {
                                            Toast.makeText(mcmaterialshou.this, "状态错误......", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                ll_materialinfo.addView(convertView);
                            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String resultstr = bundle.getString("result");
            Message msg = new Message();
            msg.what = 0;
            msg.obj = resultstr;
            handler.sendMessage(msg);
        }
    }
}
