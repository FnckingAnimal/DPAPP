package app.cmapp.Repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import app.cmapp.Interfacearray.Statusinterface;
import app.cmapp.Staticdata;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.exechttprequest;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.appcdl.jsontolist;
import app.cmapp.parameterclass.httprequestinputdata;
import app.dpapp.R;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqrepairengdatain extends AppCompatActivity
{
    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;
    String Eqid;
    String Eqname;

    private TextView headerlabel1;
    private TextView headerlabel2;
    private TextView headerlabel3;

    private TextView tblabel1;
    private TextView tblabel2;
    private TextView tblabel3;
    private TextView tblabel4;
    private EditText tvvalue1;
    private EditText tvvalue2;
    private EditText tvvalue3;
    private EditText tvvalue4;


    private Spinner spvalue1;
    private Spinner spvalue2;
    private Spinner spvalue3;
    private Spinner spvalue4;

    private TextView tblabel5;
    private Spinner spvalue5;
    private EditText tvvalue5;

    private EditText etvlauespareqty;
    private Spinner spvaluespareflag;
    private Spinner spvaluespare;
    private Spinner spvaluesparemodel;

    private Spinner spvalueaccpet;

    private TextView tvvaluenozzle;
    private Spinner spvaluenozzle;
    private Spinner spvaluenozzlechange;
    private Spinner spvaluenozzlereason;

    private String finishrepairtype;  //1:維修完成功能正常，2:產線誤報;
    private String statictype;

    private String enginputstatus;//工程信息錄入
    private String acceptstatus;//調整項目錄入
    private String flowid;

    private int checkint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_repairengdatain);

        checkint=0;

        Bundle bundle = this.getIntent().getExtras();
        try {
            Eqid = bundle.getString("Eqid");
            finishrepairtype=bundle.getString("finishrepairtype");
            Eqname=bundle.getString("Eqname");
            enginputstatus = bundle.getString("enginputstatus");
            acceptstatus = bundle.getString("acceptstatus");
            flowid = bundle.getString("flowid");
        }
        catch(Exception ex1)
        {
            Toast.makeText(this,"設備信息異常",Toast.LENGTH_SHORT).show();
        }


        //Eqid="M1411T004-004";      //除table1 和  table5 都沒有數據
        //Eqid="M1411T004-003";      //table2 有內容
        //Eqid="M1411T003-002";      //可以看到 NOZZLE 編號那塊   table6 和table7
        //Eqid="M1607T001-001";      //30IMG   特殊機臺  table4有數據

        //finishrepairtype="1";
        //finishrepairtype="2";

        headerlabel1=(TextView)findViewById(R.id.eqrepairengdatain_headertvlabel1);
        headerlabel2=(TextView)findViewById(R.id.eqrepairengdatain_headertvlabel2);
        headerlabel3=(TextView)findViewById(R.id.eqrepairengdatain_headertvlabel3);

        headerlabel1.append(Eqid);
        switch (finishrepairtype) {
            case "1":
            headerlabel2.append("維修完成，功能正常");
                break;
            case "2":
                headerlabel2.append("產線誤報修");
                break;
        }
        headerlabel3.append(Eqname);

        tblabel1=(TextView)findViewById(R.id.eqrepairengdatain_tvlabel1);
        tblabel2=(TextView)findViewById(R.id.eqrepairengdatain_tvlabel2);
        tblabel3=(TextView)findViewById(R.id.eqrepairengdatain_tvlabel3);
        tblabel4=(TextView)findViewById(R.id.eqrepairengdatain_tvlabel4);
        tblabel5=(TextView)findViewById(R.id.eqrepairengdatain_tvlabel5);

        tvvalue1=(EditText)findViewById(R.id.eqrepairengdatain_tv1);
        tvvalue2=(EditText)findViewById(R.id.eqrepairengdatain_tv2);
        tvvalue3=(EditText)findViewById(R.id.eqrepairengdatain_tv3);
        tvvalue4=(EditText)findViewById(R.id.eqrepairengdatain_tv4);
        tvvalue5=(EditText)findViewById(R.id.eqrepairengdatain_tv5);

        tvvalue1.setEnabled(false);
        tvvalue2.setEnabled(false);
        tvvalue3.setEnabled(false);
        tvvalue4.setEnabled(false);
        tvvalue5.setEnabled(false);

        tvvalue1.setVisibility(View.INVISIBLE);
        tvvalue2.setVisibility(View.INVISIBLE);
        tvvalue3.setVisibility(View.INVISIBLE);
        tvvalue4.setVisibility(View.INVISIBLE);
        tvvalue5.setVisibility(View.INVISIBLE);

        spvalue1=(Spinner)findViewById(R.id.eqrepairengdatain_sp1);
        spvalue2=(Spinner)findViewById(R.id.eqrepairengdatain_sp2);
        spvalue3=(Spinner)findViewById(R.id.eqrepairengdatain_sp3);
        spvalue4=(Spinner)findViewById(R.id.eqrepairengdatain_sp4);
        spvalue5=(Spinner)findViewById(R.id.eqrepairengdatain_sp5);

        spvalue1.setEnabled(false);
        spvalue2.setEnabled(false);
        spvalue3.setEnabled(false);
        spvalue4.setEnabled(false);
        spvalue5.setEnabled(false);

        spvalue1.setVisibility(View.INVISIBLE);
        spvalue2.setVisibility(View.INVISIBLE);
        spvalue3.setVisibility(View.INVISIBLE);
        spvalue4.setVisibility(View.INVISIBLE);
        spvalue5.setVisibility(View.INVISIBLE);

        spvaluespareflag=(Spinner)findViewById(R.id.eqrepairengdatain_sparesp1);
        spvaluespare=(Spinner)findViewById(R.id.eqrepairengdatain_sparesp2);
        spvaluesparemodel=(Spinner)findViewById(R.id.eqrepairengdatain_sparesp3);
        etvlauespareqty=(EditText)findViewById(R.id.eqrepairengdatain_spareet1);

        spvaluespare.setEnabled(false);
        spvaluespare.setVisibility(View.INVISIBLE);
        spvaluespareflag.setEnabled(false);
        spvaluespareflag.setVisibility(View.INVISIBLE);
        spvaluesparemodel.setEnabled(false);
        spvaluesparemodel.setVisibility(View.INVISIBLE);

        spvaluenozzle=(Spinner)findViewById(R.id.eqrepairengdatain_nozsp1);
        spvaluenozzlechange=(Spinner)findViewById(R.id.eqrepairengdatain_nozchagesp1);
        spvaluenozzlereason=(Spinner)findViewById(R.id.eqrepairengdatain_nozreasonsp1);
        tvvaluenozzle=(TextView)findViewById(R.id.eqrepairengdatain_noztv1);

        spvaluenozzle.setEnabled(false);
        spvaluenozzle.setVisibility(View.INVISIBLE);
        spvaluenozzlechange.setEnabled(false);
        spvaluenozzlechange.setVisibility(View.INVISIBLE);
        spvaluenozzlereason.setEnabled(false);
        spvaluenozzlereason.setVisibility(View.INVISIBLE);

        spvalueaccpet = (Spinner) findViewById(R.id.eqrepairengdatain_accpetsp1);

        spvalueaccpet.setEnabled(false);
        spvalueaccpet.setVisibility(View.INVISIBLE);

        final Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();

        loadingdata();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void loadingdata()
    {
        /*
        String url1=Staticdata.httpurl+"getengconfirm.ashx";

        List<httprequestinputdata> li=new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1=new httprequestinputdata();
        hhi1.setDataname("machinenoid");
        hhi1.setDatavalue(Eqid);
        li.add(hhi1);
        httprequestinputdata hhi2=new httprequestinputdata();
        hhi2.setDataname("type");
        hhi2.setDatavalue(finishrepairtype);
        li.add(hhi2);
        getkeyindatainput(url1, li,1);
*/
        if (enginputstatus.equals("1")) {
            String url1 = Staticdata.httpurl + "machinedata/getengconfirm.ashx";
            List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
            httprequestinputdata hhi1 = new httprequestinputdata();
            hhi1.setDataname("machinenoid");
            hhi1.setDatavalue(Eqid);
            li.add(hhi1);
            httprequestinputdata hhi2 = new httprequestinputdata();
            hhi2.setDataname("type");
            hhi2.setDatavalue(finishrepairtype);
            li.add(hhi2);
            getkeyindatainput(url1, li,2);
        }

        if (acceptstatus.equals("1")) {
            String url2 = Staticdata.httpurl +  "machinedata/machine_accept.ashx";

            List<httprequestinputdata> li2 = new ArrayList<httprequestinputdata>();
            httprequestinputdata hhi21 = new httprequestinputdata();
            hhi21.setDataname("directpar");
            hhi21.setDatavalue("getadjustitemlist");
            li2.add(hhi21);
            httprequestinputdata hhi22 = new httprequestinputdata();
            hhi22.setDataname("machinesysid");
            hhi22.setDatavalue(Eqid);
            li2.add(hhi22);
            getkeyindatainput(url2, li2,2);

        }
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
                        Log.v("CMSF", paramObject.toString());
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        Toast.makeText(eqrepairengdatain.this
                                ,"操作失敗，請求異常-"+paramObject.toString(),Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(eqrepairengdatain.this
                                ,"操作失敗，請求異常",Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }

    public  void fillingdata(List<jsontolist._jsonarray> j1)
    {
        //Log.v("CMSF","F1");
        if(j1!=null)
        {
            if(j1.get(0).get_arraystatus()== Statusinterface.STATUS_SEC)
            {
                String nextstep=j1.get(0).get_valuelist().get(0).get(0).get_value();
                //Log.v("CMSF",nextstep);
                switch (nextstep)
                {
                   case "getengconfirm":
                   // default:
                        statictype=j1.get(0).get_valuelist().get(0).get(1).get_value();
                        tblabel1.setText(j1.get(0).get_valuelist().get(0).get(3).get_value());
                        tblabel2.setText(j1.get(0).get_valuelist().get(0).get(5).get_value());
                        //主參數確立Header名稱
                       tblabel3.setText(j1.get(0).get_valuelist().get(0).get(13).get_value());
                       tblabel4.setText(j1.get(0).get_valuelist().get(0).get(15).get_value());
                       tblabel5.setText(j1.get(0).get_valuelist().get(0).get(7).get_value());
                       /*
                        switch (statictype)
                        {
                            case "0":
                                tblabel3.setText("解決方案");
                                tblabel4.setText("異常真因");
                                tblabel5.setText("站位");
                                break;
                            case "1":
                                tblabel3.setText("解決方案");
                                tblabel4.setText("使用工具");
                                tblabel5.setText("站位");
                                break;
                            case "2":
                                tblabel3.setText("解決方案");
                                tblabel4.setText("異常真因");
                                tblabel5.setText("站位");
                                break;
                        }
*/
                       //獲得當機原因/誤報類型清單 并捆綁 by tod 2016/7/8
                       try {
                           String checkspvalue1type=j1.get(0).get_valuelist().get(0).get(2).get_value();
                           String[] tempa={ "geteng_resolvent","geteng_realreason" };
                           checketandsp(tvvalue1, spvalue1, j1.get(1).get_valuelist(), checkspvalue1type,tempa);
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁"+tblabel1.getText()+"數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }

                       //獲得改善方案/當機原因請單并捆綁  by tod 2016/7/8

                       try {
                           String checkspvalue2type=j1.get(0).get_valuelist().get(0).get(4).get_value();
                           checketandsp(tvvalue2, spvalue2, j1.get(2).get_valuelist(), checkspvalue2type, null);
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁"+tblabel2.getText()+"數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }

                       //獲得解決方案清單并捆綁  by tod 2016/7/12

                       try {
                           String checkspvalue3type=j1.get(0).get_valuelist().get(0).get(12).get_value();
                           checketandsp(tvvalue3, spvalue3, null, checkspvalue3type, null);
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁"+tblabel3.getText()+"數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }

                       //獲得異常真因清單并捆綁  by tod 2016/7/12
                       try {
                           String checkspvalue4type=j1.get(0).get_valuelist().get(0).get(14).get_value();
                           checketandsp(tvvalue4, spvalue4, null, checkspvalue4type, null);
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁"+tblabel4.getText()+"數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }

                       //獲得站位請單并捆綁  by tod 2016/7/8
                       try {
                           String checkoptype = j1.get(0).get_valuelist().get(0).get(6).get_value();
                           checkviewstate(tblabel5, checkoptype);
                           checketandsp(tvvalue5, spvalue5, j1.get(3).get_valuelist(), checkoptype, null);
                           tblabel5.setText(j1.get(0).get_valuelist().get(0).get(7).get_value());
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁"+tblabel5.getText()+"數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }

                       //獲得是否更換備品狀態并捆綁  by tod 2016/7/8
                       try {
                           String checksparetype = j1.get(0).get_valuelist().get(0).get(8).get_value();
                           if(statictype.equals("2")==false) {
                               String[] tempa={ "geteng_spare" };
                               checksp(spvaluespareflag, j1.get(4).get_valuelist(), checksparetype, tempa);
                           }
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁備品狀態數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }

                       //獲得是否更換Nozzle狀態并捆綁  by tod 2016/7/8
                       try {
                           String checknozzlenotype = j1.get(0).get_valuelist().get(0).get(10).get_value();
                           checksp(spvaluenozzle, j1.get(5).get_valuelist(), checknozzlenotype, null);

                           String checknozzlechangetype = j1.get(0).get_valuelist().get(0).get(11).get_value();
                           String[] tempa={ "geteng_nozzlereason" };
                           checksp(spvaluenozzlechange, j1.get(6).get_valuelist(), checknozzlechangetype,tempa);
                       }
                       catch(Exception ex1)
                       {
                           Toast.makeText(this,"捆綁Nozzle狀態數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                       }
                        break;
                    case "geteng_resolvent":
                        //獲得解決方案清單并捆綁  by tod 2016/7/9
                        try {
                            String checksolutiontype = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            if(statictype.equals("2")) {
                                String[] tempa= {"geteng_spare"};
                                checketandsp(tvvalue3, spvalue3, j1.get(1).get_valuelist(), checksolutiontype, tempa);
                            }
                            else
                            {
                                checketandsp(tvvalue3, spvalue3, j1.get(1).get_valuelist(), checksolutiontype, null);
                            }
                            //checksp(spvaluespare, j1.get(1).get_valuelist(), checksolutiontype, "geteng_realreason");
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(this,"捆綁解決方法數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "geteng_realreason":
                        //獲得異常真因-使用工具清單并捆綁  by tod 2016/7/9
                        try {
                            String checkrealreasontype = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            checketandsp(tvvalue4, spvalue4, j1.get(1).get_valuelist(), checkrealreasontype, null);
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(this,"捆綁解決方法數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "geteng_spare":
                        //獲得備品清單信息并捆綁  by tod 2016/7/9
                        try {
                            String checksparetype = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            String[] tempa= {"geteng_sparemodel"};
                            checksp(spvaluespare, j1.get(1).get_valuelist(), checksparetype, tempa);
                            checkviewstate(etvlauespareqty, checksparetype);
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(this,"捆綁解決方法數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "geteng_sparemodel":
                        //獲得備品清單信息并捆綁  by tod 2016/7/9
                        try {
                            String checksparemodeltype = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            checksp(spvaluesparemodel, j1.get(1).get_valuelist(), checksparemodeltype, null);
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(this,"捆綁解決方法數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "geteng_nozzlereason":
                        //獲得備品清單信息并捆綁  by tod 2016/7/9
                        try {
                            String checknozzlechange = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            checksp(spvaluenozzlereason, j1.get(1).get_valuelist(), checknozzlechange, null);
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(this,"捆綁解決方法數據源異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "insert_eng":
                        //獲得備品清單信息并捆綁  by tod 2016/7/9
                        try {
                            String checkinstrstatus = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            String checkinstrmsg=j1.get(0).get_valuelist().get(0).get(2).get_value();
                            Toast.makeText(this,checkinstrstatus+";"+checkinstrmsg+";",Toast.LENGTH_SHORT).show();
                            //checksp(spvaluenozzlereason, j1.get(1).get_valuelist(), checknozzlechange, null);

                            closeacvitity();
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(this,"提交異常，請聯絡MIS部門",Toast.LENGTH_SHORT).show();
                            this.finish();
                        }
                        break;
                    case "getadjustitemlist":
                        //獲得調整項目清單 by tod 2016/8/5
                        try {

                            checksp(spvalueaccpet, j1.get(1).get_valuelist(), "1", null);
                        } catch (Exception ex) {

                            Toast.makeText(this, "獲取調整項目清單異常", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "checkcreateaccept":
                        try {
                            String checkcreateacceptstatus = j1.get(0).get_valuelist().get(0).get(1).get_value();
                            String checkcreateacceptmsg = j1.get(0).get_valuelist().get(0).get(2).get_value();
                            Toast.makeText(this, checkcreateacceptstatus + ";" + checkcreateacceptmsg + ";", Toast.LENGTH_SHORT).show();

                            closeacvitity();
                            //checksp(spvaluenozzlereason, j1.get(1).get_valuelist(), checknozzlechange, null);
                        } catch (Exception ex) {

                            Toast.makeText(this, "創建調整項目異常", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
            }
        }
    }

    private  void closeacvitity()
    {

        checkint++;
        if(enginputstatus.equals("1")&&acceptstatus.equals("1"))
        {
            if(checkint==2)
            {
                this.finish();
            }
        }
        else
        {
            if(checkint==1)
            {
                this.finish();
            }
        }

    }

    public void checkviewstate(View v,String status)
    {
        switch (status)
        {
            case "N":
                v.setVisibility(View.INVISIBLE);
                v.setEnabled(false);
                break;
            default:
                v.setVisibility(View.VISIBLE);
                v.setEnabled(true);
                break;
        }
    }

    /**
     * 當機原因/誤報類型-改善方案/當機原因數據源捆綁
     * @param e
     * @param v
     * @param sends
     * @param status
     */
    public void checketandsp(final EditText e, final Spinner v, List<List<jsontolist._valueitem>> sends,String status,final String[] nextstep)
    {
        switch (status) {
            case "0":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                e.setEnabled(true);
                e.setVisibility(View.VISIBLE);
                break;
            case "N":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                e.setEnabled(false);
                e.setVisibility(View.INVISIBLE);
                break;
            default:

                v.setEnabled(true);
                v.setVisibility(View.VISIBLE);
                if(status.equals("1"))
                {
                    e.setEnabled(false);
                    e.setVisibility(View.VISIBLE);
                }
                else {
                    e.setEnabled(true);
                    e.setVisibility(View.VISIBLE);
                }
                if(sends!=null) {
                    List<FinalStaticCloass.SpinnerData> lf1 = new ArrayList<FinalStaticCloass.SpinnerData>();
                    //List<String> ls=new ArrayList<>();
                    for (int i = 0; i < sends.size(); i++) {
                        FinalStaticCloass s = new FinalStaticCloass();
                        FinalStaticCloass.SpinnerData ss = s.new SpinnerData(sends.get(i).get(0).get_value(), sends.get(i).get(1).get_value());
                        lf1.add(ss);
                    }
                   ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                            new ArrayAdapter<FinalStaticCloass.SpinnerData>(eqrepairengdatain.this, android.R.layout.simple_dropdown_item_1line,lf1);
                    as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    v.setAdapter(as1);
                    //v.setSelection(0, false);
                    v.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    e.setText(
                                            ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getText()
                                    );
                                    if(nextstep!=null) {
                                        for(int i=0;i<nextstep.length;i++) {
                                            nextstepfunction(nextstep[i].toString(), ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue());
                                            //Log.v("CMSF", nextstep + "-" + ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue().toString());
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            }
                    );
                }
                break;
        }
    }

    /**
     * Spare and nozzel child step data bind
     * @param v
     * @param sends
     * @param status
     * @param nextstep
     */
    public void checksp(final Spinner v, List<List<jsontolist._valueitem>> sends,String status,final String[] nextstep)
    {
        switch (status) {
            case "0":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                break;
            case "N":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                break;
            default:
                v.setEnabled(true);
                v.setVisibility(View.VISIBLE);
                if(sends!=null) {
                    List<FinalStaticCloass.SpinnerData> lf1 = new ArrayList<FinalStaticCloass.SpinnerData>();
                    //List<String> ls=new ArrayList<>();
                    for (int i = 0; i < sends.size(); i++) {
                        FinalStaticCloass s = new FinalStaticCloass();
                        FinalStaticCloass.SpinnerData ss = s.new SpinnerData(sends.get(i).get(0).get_value(), sends.get(i).get(1).get_value());
                        lf1.add(ss);
                    }
                    ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                            new ArrayAdapter<FinalStaticCloass.SpinnerData>(eqrepairengdatain.this, android.R.layout.simple_dropdown_item_1line,lf1);
                    as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    v.setAdapter(as1);
                    //v.setSelection(0, false);
                    v.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(nextstep!=null) {
                                        for(int i=0;i<nextstep.length;i++) {
                                            nextstepfunction(nextstep[i].toString(), ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue());
                                            //Log.v("CMSF", nextstep + "-" + ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue().toString());
                                        }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            }
                    );
                }
                break;
        }
    }

    /**
     * get next step link data
     * @param nextsteptype
     * @param o1
     */
  private void nextstepfunction(String nextsteptype,Object o1)
  {
      //Log.v("CMSF",nextsteptype.toString());
      switch (nextsteptype)
      {
          case "geteng_resolvent":
              String url1=Staticdata.httpurl+"geteng_resolvent.ashx";

              List<httprequestinputdata> li=new ArrayList<httprequestinputdata>();
              httprequestinputdata hhi1=new httprequestinputdata();
              hhi1.setDataname("machinenoid");
              hhi1.setDatavalue(Eqid);
              li.add(hhi1);
              httprequestinputdata hhi2=new httprequestinputdata();
              hhi2.setDataname("type");
              hhi2.setDatavalue(finishrepairtype);
              li.add(hhi2);
              httprequestinputdata hhi3=new httprequestinputdata();
              hhi3.setDataname("strdownflag");
              hhi3.setDatavalue(o1.toString());
              li.add(hhi3);
              getkeyindatainput(url1, li,1);
              //Log.v("CMSF", "geteng_resolvent-"+Eqid+"-"+finishrepairtype+"-"+o1.toString());

             break;
         case "geteng_realreason":
              String url2=Staticdata.httpurl+"geteng_realreason.ashx";
              List<httprequestinputdata> li2=new ArrayList<httprequestinputdata>();
              httprequestinputdata hhi21=new httprequestinputdata();
              hhi21.setDataname("machinenoid");
              hhi21.setDatavalue(Eqid);
              li2.add(hhi21);
              httprequestinputdata hhi22=new httprequestinputdata();
              hhi22.setDataname("type");
              hhi22.setDatavalue(finishrepairtype);
              li2.add(hhi22);
              httprequestinputdata hhi23=new httprequestinputdata();
              hhi23.setDataname("strdownflag");
              hhi23.setDatavalue(o1.toString());
              li2.add(hhi23);
              getkeyindatainput(url2, li2,1);
             //Log.v("CMSF", "geteng_realreason-"+Eqid+"-"+finishrepairtype+"-"+o1.toString());

              break;
              //realreason時同時獲取備品信息
          case "geteng_spare":

                  String url3 = Staticdata.httpurl + "geteng_spare.ashx";
                  List<httprequestinputdata> li3 = new ArrayList<httprequestinputdata>();

                  if (statictype.equals("2")) {
                      httprequestinputdata hhi31 = new httprequestinputdata();
                      hhi31.setDataname("machinenoid");
                      hhi31.setDatavalue(Eqid);
                      li3.add(hhi31);

                      httprequestinputdata hhi32 = new httprequestinputdata();
                      hhi32.setDataname("changeflag");
                      hhi32.setDatavalue("Y");
                      li3.add(hhi32);

                      httprequestinputdata hhi33 = new httprequestinputdata();
                      hhi33.setDataname("abnormalreasonid");
                      hhi33.setDatavalue(((FinalStaticCloass.SpinnerData) spvalue3.getSelectedItem()).getValue());
                      li3.add(hhi33);
                      getkeyindatainput(url3, li3,1);
                      //Log.v("CMSF",hhi33.getDatavalue().toString());
                  }
                  else
                  {
                      if(o1.toString().equals("Y")) {

                          httprequestinputdata hhi31 = new httprequestinputdata();
                          hhi31.setDataname("machinenoid");
                          hhi31.setDatavalue(Eqid);
                          li3.add(hhi31);

                          httprequestinputdata hhi32 = new httprequestinputdata();
                          hhi32.setDataname("changeflag");
                          hhi32.setDatavalue(o1.toString());
                          li3.add(hhi32);
                          httprequestinputdata hhi33 = new httprequestinputdata();
                          hhi33.setDataname("abnormalreasonid");
                          hhi33.setDatavalue("");
                          li3.add(hhi33);
                          getkeyindatainput(url3, li3,1);
                      }
                      else
                      {
                          checkviewstate(spvaluespare,"N");
                          checkviewstate(spvaluesparemodel,"N");
                          checkviewstate(etvlauespareqty,"N");
                      }
                  }
                  //getkeyindatainput(url3, li3);

              break;

          case "geteng_sparemodel":
              String url4=Staticdata.httpurl+"geteng_sparemodel.ashx";
              List<httprequestinputdata> li4=new ArrayList<httprequestinputdata>();
              httprequestinputdata hhi41=new httprequestinputdata();
              hhi41.setDataname("spareid");
              hhi41.setDatavalue(o1.toString());
              li4.add(hhi41);
              getkeyindatainput(url4, li4,1);
              break;

          case "geteng_nozzlereason":
              String url5=Staticdata.httpurl+"geteng_nozzlereason.ashx";
              List<httprequestinputdata> li5=new ArrayList<httprequestinputdata>();
              httprequestinputdata hhi51=new httprequestinputdata();
              hhi51.setDataname("nozzlename");
              hhi51.setDatavalue(o1.toString());
              li5.add(hhi51);
              getkeyindatainput(url5, li5,1);
              break;
      }
  }

    public void eqrepairenginputsubmit(View v)
    {
        /*
                type: 指引類型(0:正常輸入;1:產線誤報修輸入;2:特定機臺報修輸入;)
                abnormal: 當機原因/誤報類型
                improvement: 改善方案/當機原因
                resolvent: 解決方法
                realreason: 異常真因/使用工具
                opno: 站位
                spareflag: 是否換備品
                spareid: 備品編號
                sparenum: 備品數量
                sparemodel: 備品型號
                nozzleid: Nozzle編號
                nozzlechange: Nozzle改變
                nozzlereason: 改變原因
              */
        if (enginputstatus.equals("1")) {


            String machinenoid = Eqid;


            String type = finishrepairtype;

            String abnormal = tvvalue1.getText().toString();


            String improvement = tvvalue2.getText().toString();


            String resolvent = tvvalue3.getText().toString();

            String realreason = tvvalue4.getText().toString();

            String opno = tvvalue5.getText().toString();

            String spareflag = "";

            try {
                if (spvaluespareflag != null && spvaluespareflag.getAdapter().getCount() > 0) {
                    spareflag = ((FinalStaticCloass.SpinnerData) spvaluespareflag.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {

                spareflag = "";

            }


            String spareid = "";
            try {
                if (spvaluespare != null && spvaluespare.getAdapter().getCount() > 0) {
                    spareid = ((FinalStaticCloass.SpinnerData) spvaluespare.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {
                spareid = "";
            }


            String sparenum = etvlauespareqty.getText().toString();

            String sparemodel = "";
            try {
                if (spvaluesparemodel != null && spvaluesparemodel.getAdapter().getCount() > 0) {
                    sparemodel = ((FinalStaticCloass.SpinnerData) spvaluesparemodel.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {
                sparemodel = "";
            }


            String nozzleid = "";
            try {
                if (spvaluenozzle != null && spvaluenozzle.getAdapter().getCount() > 0) {
                    nozzleid = ((FinalStaticCloass.SpinnerData) spvaluenozzle.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {

                nozzleid = "";
            }


            String nozzlechange = "";
            try {
                if (spvaluenozzlechange != null && spvaluenozzlechange.getAdapter().getCount() > 0) {
                    nozzlechange = ((FinalStaticCloass.SpinnerData) spvaluenozzlechange.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {
                nozzlechange = "";

            }


            String nozzlereason = "";
            try {
                if (spvaluenozzlereason != null && spvaluenozzlereason.getAdapter().getCount() > 0) {
                    nozzlereason = ((FinalStaticCloass.SpinnerData) spvaluenozzlereason.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {
                nozzlereason = "";
            }

            String creator = Sessionuser;

            String abnormalid="";

            String resolventid="";
            String submitstr = "{insert_eng:[{\"machinenoid\":\"" + Eqid
                    + "\",\"creator\":\"" + Sessionuser
                    + "\",\"type\":\"" + statictype
                    + "\",\"abnormalid\":\"" + abnormalid
                    + "\",\"abnormal\":\"" + abnormal
                    + "\",\"improvement\":\"" + improvement
                    + "\",\"resolventid\":\"" + resolventid
                    + "\",\"resolvent\":\"" + resolvent
                    + "\",\"realreason\":\"" + realreason
                    + "\",\"opno\":\"" + opno
                    + "\",\"spareflag\":\"" + spareflag
                    + "\",\"spareid\":\"" + spareid
                    + "\",\"sparenum\":\"" + sparenum
                    + "\",\"sparemodel\":\"" + sparemodel
                    + "\",\"nozzleid\":\"" + nozzleid
                    + "\",\"nozzlechange\":\"" + nozzlechange
                    + "\",\"nozzlereason\":\"" + nozzlereason
                    + "\"}]}";


            List<httprequestinputdata> li = new ArrayList<>();
            httprequestinputdata h1 = new httprequestinputdata();
            h1.setDataname("strdata");
            h1.setDatavalue(submitstr);
            li.add(h1);
            httprequestinputdata h2 = new httprequestinputdata();
            h2.setDataname("machinesysid");
            h2.setDatavalue(Eqid);
            li.add(h2);
            // getkeyindatainput("machinedata/insert_eng.ashx", li);
            eqengrepairdatainsubmit(Staticdata.httpurl + "machinedata/insert_eng.ashx", li);
        }
        if (acceptstatus.equals("1")) {
            String adjustid = "";
            try {
                if (spvalueaccpet != null && spvalueaccpet.getAdapter().getCount() > 0) {
                    adjustid = ((FinalStaticCloass.SpinnerData) spvalueaccpet.getSelectedItem()).getValue();
                }
            } catch (Exception ex1) {
                adjustid = "";
            }
            if (adjustid != null && adjustid != "") {
                String url11 = "machinedata/machine_accept.ashx";

                List<httprequestinputdata> lia = new ArrayList<>();
                httprequestinputdata hhi11 = new httprequestinputdata();
                hhi11.setDataname("directpar");
                hhi11.setDatavalue("checkcreateaccept");
                lia.add(hhi11);
                httprequestinputdata hhi12 = new httprequestinputdata();
                hhi12.setDataname("machinesysid");
                hhi12.setDatavalue(Eqid);
                lia.add(hhi12);
                httprequestinputdata hhi13 = new httprequestinputdata();
                hhi13.setDataname("flowid");
                hhi13.setDatavalue(flowid);
                lia.add(hhi13);
                httprequestinputdata hhi14 = new httprequestinputdata();
                hhi14.setDataname("adjustid");
                hhi14.setDatavalue(adjustid);
                lia.add(hhi14);
                eqengrepairdatainsubmit(Staticdata.httpurl + url11, lia);

            }
        }
    }
    public  void eqengrepairdatainsubmit(String urlpath,List<httprequestinputdata> li) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
        // List<httprequestinputdata> li=null;
        exechttprequest hf1=new exechttprequest();
        //Log.v("CMSF", hf1.toString());
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1,"正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        //Log.v("CMSF",paramObject.toString());
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        execloadactivity.canclediglog();
                        Toast.makeText(eqrepairengdatain.this, "File-"+paramObject.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imagedata) {
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            return;
        }
        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 0)
        {
            Bundle bundle = imagedata.getExtras();
            String imageresult = bundle.getString("result");
            Toast.makeText(this,imageresult,Toast.LENGTH_SHORT).show();
        }
    }
    }

