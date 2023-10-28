package app.cmapp.Repair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.Staticdata;
import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.exechttprequest;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.appcdl.jsontolist;
import app.cmapp.appcdl.uploadimage;
import app.cmapp.parameterclass.httprequestinputdata;
import app.dpapp.R;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqrepiaraccept extends AppCompatActivity
{
    //用户名
    private static String Sessionuser;
    private String Eqid;
    private String sowner;
    private String flowid;
    private LinearLayout ll1;
    private View scontextview;
    private TextView tvvalue1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_repairaccept);

        ll1=(LinearLayout)findViewById(R.id.eqreapireqaccept_maindataddll3);

        final Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();

        Bundle bundle = this.getIntent().getExtras();
        try {
            Eqid = bundle.getString("Eqid");
            //Eqid = "M1311E022-025";
            sowner=bundle.getString("sowner");
            //sowner = "ENG";
            flowid=bundle.getString("flowid");
            //flowid="18";

            tvvalue1=(TextView)this.findViewById(R.id.eqreapireqaccept_mainheadercet1);
            tvvalue1.setText(Eqid);
            //String url1 = "machinedata/machine_accept.ashx";
            //http://10.142.136.222:8107/machinedata/machine_accept.ashx?directpar=checkcreateaccept&machinesysid=M1311E022-025&flowid=18

            String url1=Staticdata.httpurl+"/machinedata/machine_accept.ashx";
            //machinedata/machine_eqbccheck.ashx?directpar=getbceqchecktask&machinecheckid=F201608151017001&mtaskstatus=0
            List<httprequestinputdata> li=new ArrayList<httprequestinputdata>();
            httprequestinputdata hhi1=new httprequestinputdata();
            hhi1.setDataname("directpar");
            hhi1.setDatavalue("getacceptitemlist");
            li.add(hhi1);
            httprequestinputdata hhi2=new httprequestinputdata();
            hhi2.setDataname("machinesysid");
            hhi2.setDatavalue(Eqid);
            li.add(hhi2);
            httprequestinputdata hhi3=new httprequestinputdata();
            hhi3.setDataname("flowid");
            hhi3.setDatavalue(flowid);
            li.add(hhi3);
            httprequestinputdata hhi4=new httprequestinputdata();
            hhi4.setDataname("owner");
            hhi4.setDatavalue(sowner);
            li.add(hhi4);
            //getkeyindatainput(url1, li, 2);
            getkeyindatainput(url1, li, 1);
        }
        catch(Exception ex1)
        {
            Toast.makeText(this,"設備信息異常",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            switch (requestCode) {
                /*
                case 4:
                    String tempstr = "";
                    for (int i = 0; i < ll1.getChildCount(); i++) {
                        tempstr = "";
                        tempstr = ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource11Mname1)).getText().toString();
                        if (tempstr.equals(scanResult)) {
                            View context = ((View) ll1.getChildAt(i));
                            //context.scrollTo(0, 0);
                            scrollv1.scrollTo(100,context.getTop());
                            tvvalue2.setText(scanResult);
                            ((TextView) context.findViewById(R.id.listviewsource11Mname9)).setText("Y");
                            onsaveeqcheckdatatolocal(null);
                            break;
                        }
                    }
                    break;
                case 0:
                    tvvalue1.setText(scanResult);
                    onloadingnewcheckid(null);
                    break;
                    */
                case 3:
                   // ((TextView) scontextview.findViewById(R.id.listviewsource11Mname9)).setText(scanResult.split(":")[1]);
                    ((EditText) scontextview.findViewById(R.id.listviewsource12Mname5)).setText(scanResult.split(":")[1]);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onuploadeqcheckdata(View v)
    {
        /*
        jsonstr = "[{\"machinenoid\":\"M1311E022-025\",\"areaid\":\" \",\"flowid\":\"18\",\"adjustmentid\":\"AD1607-021\",\"acceptid\":\"AC1607-024\",\"owner\":\"ENG\",\"modes\":\"select\",\"value1\":\"A\",\"createid\":\"F5460007\"},{\"machinenoid\":\"M1311E022-025\",\"areaid\":\" \",\"flowid\":\"18\",\"adjustmentid\":\"AD1607-021\",\"acceptid\":\"AC1607-025\",\"owner\":\"ENG\",\"modes\":\"upload\",\"value1\":\"1:F201608051644-001\",\"createid\":\"F5460007\"},{\"machinenoid\":\"M1311E022-025"
                + "\",\"areaid\":\" \",\"flowid\":\"18\",\"adjustmentid\":\"AD1607-021"
                + "\",\"acceptid\":\"AC1607-026"
                + "\",\"owner\":\"ENG\",\"modes\":\"upload\",\"value1\":\"1:F201608051644-002"
                + "\",\"createid\":\"F5460007\"},{\"machinenoid\":\"M1311E022-025"
                + "\",\"areaid\":\" \",\"flowid\":\"18\",\"adjustmentid\":\"AD1607-021"
                + "\",\"acceptid\":\"AC1607-030"
                + "\",\"owner\":\"ENG\",\"modes\":\"select\",\"value1"
                + "\":\"B\",\"createid\":\"F5460007\"}]";
                */
        if(ll1.getChildCount()==0)
        {
            Toast.makeText(eqrepiaraccept.this,"無數據",Toast.LENGTH_SHORT).show();
            return;
        }
        if(Eqid==null||Eqid.equals(""))
        {
            Toast.makeText(this,"系統設備編號不能為空",Toast.LENGTH_SHORT).show();
            return;
        }
        String savestr="[";
        boolean checkdatafinish=true;
        for (int i=0;i<ll1.getChildCount();i++) {
            if(((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource12Mname5)).getText().toString().equals("")==false) {
                savestr += "{\"machinenoid\":\"" + Eqid
                        + "\",\"areaid\":\"" + " "
                        + "\",\"flowid\":\"" + flowid
                        + "\",\"adjustmentid\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource12Mname0)).getText().toString()
                        + "\",\"acceptid\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource12Mname1)).getText().toString()
                        + "\",\"owner\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource12Mname2)).getText().toString()
                        + "\",\"modes\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource12Mname4)).getText().toString()
                        + "\",\"value1\":\"" + ((TextView) ll1.getChildAt(i).findViewById(R.id.listviewsource12Mname5)).getText().toString()
                        + "\",\"createid\":\"" + Sessionuser
                        + "\"},";
            }
            else
            {
                checkdatafinish=false;
                break;
            }
        }
        if(checkdatafinish==false)
        {
            Toast.makeText(this,"資料未填寫完整",Toast.LENGTH_SHORT).show();
            return;
        }
        if(savestr.length()==1)
        {
            Toast.makeText(this,"無需要上傳的記錄",Toast.LENGTH_SHORT).show();
            return;
        }
        savestr=savestr.substring(0,savestr.length()-1);
        savestr+="]";

        List<httprequestinputdata> li =new ArrayList<>();
        httprequestinputdata h1=new httprequestinputdata();
        h1.setDataname("directpar");
        h1.setDatavalue("submitacceptdata");
        li.add(h1);
        httprequestinputdata h2=new httprequestinputdata();
        h2.setDataname("jsonstr");
        h2.setDatavalue(savestr);
        li.add(h2);
        getkeyindatainput(Staticdata.httpurl+"machinedata/machine_accept.ashx", li,2);
    }

    public  void getkeyindatainput(String urlpath,List<httprequestinputdata> li,int sendmode) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
       // List<httprequestinputdata> li=null;
        exechttprequest hf1=new exechttprequest();
        //Log.v("CMSF", hf1.toString());
        hf1.getDataFromServer(sendmode, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1,"正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        //Log.v("CMSF", paramObject.toString());
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        Toast.makeText(eqrepiaraccept.this
                                ,"操作失敗，請求異常-"+paramObject.toString(),Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(eqrepiaraccept.this
                                ,"操作失敗，請求異常",Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }

    public  void fillingdata(List<jsontolist._jsonarray> j1)
    {
        try {
            if (j1 != null) {
                String nextstep = j1.get(0).get_valuelist().get(0).get(0).get_value();
                switch (nextstep) {
                    case "getacceptitemlist":

                        String status0 = j1.get(0).get_valuelist().get(0).get(1).get_value();
                        String msg0 = j1.get(0).get_valuelist().get(0).get(2).get_value();

                        switch (status0) {
                            case "0":
                                Toast.makeText(eqrepiaraccept.this, msg0, Toast.LENGTH_SHORT).show();
                                break;
                            case "1":
                                if (j1.get(1).get_valuelist() == null) {

                                } else {

                                    View convertView;
                                    ll1.removeAllViews();
                                    for (int i = 0; i < j1.get(1).get_valuelist().size(); i++) {
                                        //MACHINECHECKID,ASSETID,MGRID,ASSETNAME,ASSETTYPE,ASIGNOWNER,SAVALOCATION,"
                                        //+"CREATEDATE,CREATEID,CORPORATEID,SHAREDEPT,ASSETSUMMRYNAME,ASSETPHYSICSTYPE,ASSETSTATUS,PRODUCTTYPE,"
                                        //+ "SAVEFLOOR,SAVELINE,SAVESTATION,SVAEOPNO,execstatus
                                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_source12, null);
                                        ((TextView) convertView.findViewById(R.id.listviewsource12Mname0)).setText(j1.get(1).get_valuelist().get(i).get(3).get_value());
                                        ((TextView) convertView.findViewById(R.id.listviewsource12Mname1)).setText(j1.get(1).get_valuelist().get(i).get(4).get_value());
                                        ((TextView) convertView.findViewById(R.id.listviewsource12Mname2)).setText(j1.get(1).get_valuelist().get(i).get(5).get_value());
                                        ((TextView) convertView.findViewById(R.id.listviewsource12Mname3)).setText(j1.get(1).get_valuelist().get(i).get(7).get_value());
                                        ((TextView) convertView.findViewById(R.id.listviewsource12Mname4)).setText(j1.get(1).get_valuelist().get(i).get(6).get_value());
                                        ((EditText) convertView.findViewById(R.id.listviewsource12Mname5)).setText("");
                                        selectitems(((EditText) convertView.findViewById(R.id.listviewsource12Mname5)), j1.get(1).get_valuelist().get(i).get(7).get_value());
                                        scontextview=convertView;
                                        ll1.addView(convertView);
                                    }
                                }
                                break;
                            default:
                                Toast.makeText(eqrepiaraccept.this, msg0, Toast.LENGTH_SHORT).show();
                                break;
                        }

                        break;

                    case "submitacceptdata":
                        String status1 = j1.get(0).get_valuelist().get(0).get(1).get_value();
                        String msg1 = j1.get(0).get_valuelist().get(0).get(2).get_value();
                        Toast.makeText(eqrepiaraccept.this, msg1, Toast.LENGTH_SHORT).show();
                        eqrepiaraccept.this.finish();
                        break;
                }
            }
        }
        catch(Exception ex1)
        {
            Toast.makeText(eqrepiaraccept.this,"未知異常-"+ex1.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    protected void selectitems(final View v, final String cs)
    {
        try {
            if (cs.equals("upload"))
            {
                //v.setEnabled(false);
                ((EditText)v).setInputType(InputType.TYPE_NULL);
                //((EditText)v).setEnabled(true);
                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View v1) {
                                AlertDialog.Builder isnoticedialog = new AlertDialog.Builder(
                                        eqrepiaraccept.this);
                                isnoticedialog.setTitle("選擇");
                                isnoticedialog.setNeutralButton("上傳圖像", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent testi=new Intent(eqrepiaraccept.this,uploadimage.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("sysname", "CMSFEQREPAIRACCEPT");
                                                testi.putExtras(bundle);
                                                startActivityForResult(testi, 3);
                                                scontextview=v;
                                            }
                                        }
                                );

                                isnoticedialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }
                                );

                                isnoticedialog.show();
                            }
                        }
                );
        }
        }
        catch(Exception ex1)
        {
        }
    }

}



