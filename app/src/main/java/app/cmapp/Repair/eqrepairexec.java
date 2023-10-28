package app.cmapp.Repair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.Staticdata;
import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.appcdl.exechttprequest;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.appcdl.jsontolist;
import app.dpapp.parameterclass.httprequestinputdata;
import app.dpapp.R;
import app.dpapp.soap.PublicSOAP;

//import android.content.pm.PackageInstaller;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqrepairexec extends AppCompatActivity {

    private static String Sessionuser;
    private static String finishreason;
    private static String starterrorcode;
    private static String passowrd;
    private String statusform;
    private String flowid;
    private String staticfailcode="Fail:-";

    private String Eqid;

    TextView eqsysidtv;
    TextView eqnametv;
    TextView eqoptv;
    TextView eqlinetv;
    TextView reasontv;
    Spinner callerrornosp;
    Button execbutton;
    TextView ufnotv;
    /// 2016/6/14  設備狀態參數4：正常（已驗收）;1：已報修;2：已啟修;21：維修信息錄入;3：已完修; 31:產線誤報修完修;32：QC驗收
    public String statusint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_repairexec);

        eqsysidtv = (TextView) findViewById(R.id.eqrepairexec_eqid);
        eqnametv = (TextView) findViewById(R.id.eqrepairexec_eqname);
        eqlinetv = (TextView) findViewById(R.id.eqrepairexec_eqline);
        eqoptv = (TextView) findViewById(R.id.eqrepairexec_eqop);
        reasontv=(TextView) findViewById(R.id.eqrepairexec_tv6);
        callerrornosp = (Spinner) findViewById(R.id.eqrepairexec_starterrorcodesp);
        execbutton=(Button)findViewById(R.id.eqrepairexec_execbutton);
        ufnotv = (TextView) findViewById(R.id.eqrepairexec_equfno);
        //Wifi网络申请，暂行赋值，VM测试
        final Staticdata app = (Staticdata) getApplication();
        Sessionuser = app.getLoginUserID();

        Bundle bundle = this.getIntent().getExtras();
        Eqid=bundle.getString("Eqid");
        //Eqid="M1311E022-005";
        eqsysidtv.setText(Eqid);

        statusint = "1";

        loadform();

    }


    @Override
    protected void onResume() {
        super.onResume();
        switch (statusint) {
            case "2":
                Toast.makeText(eqrepairexec.this, "請重新完修", Toast.LENGTH_SHORT).show();
                break;
            case "3":
                Toast.makeText(eqrepairexec.this,"請重新驗收",Toast.LENGTH_SHORT).show();
                break;
        }
    }
     private void loadform() {
         /*
         MyThreadPool.pool.execute(
                  //new Thread(
                          new Runnable() {
                              @Override
                              public void run() {

                                  PublicSOAP ps1 = new PublicSOAP();

                                  PublicSOAP.eqbasedata pe1 = null;
                                  pe1 = ps1.getRemoteInfo_eqbasedata(Eqid);

                                  Message msg = new Message();
                                  msg.what = 0;
                                  msg.obj = pe1;
                                  handle1.sendMessage(msg);
                              }
                          }
                  //).start();
         );
*/
         List<httprequestinputdata> li1 =new ArrayList<>();
         httprequestinputdata h11=new httprequestinputdata();
         h11.setDataname("directpar");
         h11.setDatavalue("geteqrepairmachinedata");
         li1.add(h11);

         httprequestinputdata h12=new httprequestinputdata();
         h12.setDataname("machinesysid");
         h12.setDatavalue(Eqid);
         li1.add(h12);
         getkeyindatainput(Staticdata.httpurl + "machinedata/machine_accept.ashx", li1, 2);

         MyThreadPool.pool.execute(
                 new Runnable() {
                     @Override

                     public void run() {
                         PublicSOAP ps1 = new PublicSOAP();
                         String Status = ps1.getRemoteInfo_eqrepairstatus(Eqid);
                         Message msg = new Message();
                         msg.what = 0;
                         msg.obj = Status;
                         handle.sendMessage(msg);
                     }
                 }
         );

         List<httprequestinputdata> li =new ArrayList<>();
         httprequestinputdata h1=new httprequestinputdata();
         h1.setDataname("directpar");
         h1.setDatavalue("getflowid");
         li.add(h1);
         httprequestinputdata h2=new httprequestinputdata();
         h2.setDataname("machinesysid");
         h2.setDatavalue(Eqid);
         li.add(h2);
         getkeyindatainput(Staticdata.httpurl + "machinedata/machine_accept.ashx", li, 2);

         MyThreadPool.pool.execute(
                 new Runnable() {
                     @Override
                     public void run() {

                         PublicSOAP ps1 = new PublicSOAP();

                         String Status = ps1.getRemoteInfo_geteqrepairufglueno(Eqid);

                         Message msg = new Message();
                         msg.what = 0;
                         msg.obj = Status;
                         handle4.sendMessage(msg);
                     }
                 }
         );
     }
    private Handler handle1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    PublicSOAP.eqbasedata pe1 = null;
                    pe1 = (PublicSOAP.eqbasedata) msg.obj;
                    if (pe1 == null) {
                        Toast.makeText(eqrepairexec.this, "設備信息未再一體化平臺設定", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    eqnametv.setText(pe1.getEqname());
                    eqoptv.setText(pe1.getEqtype());
                    eqlinetv.setText(pe1.getEqline());
                }
            }
            catch(Exception ex1)
            {
                Toast.makeText(eqrepairexec.this, "未知異常", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }
        }
    };
    private Handler handle4 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String rs1 = msg.obj.toString();
                    if (rs1 != null) {

                        ufnotv.setText(rs1.toString());
                    }
                }
            }
            catch(Exception ex1)
            {
                Toast.makeText(eqrepairexec.this, "未知異常", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }
        }
    };
    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    String rs1 = (String)msg.obj;
                    if (rs1 != null) {
                        statusint = rs1;
                    }

                    switch (statusint) {
                        case "1"://已報修
                            reasontv.setVisibility(View.INVISIBLE);
                            callerrornosp.setVisibility(View.INVISIBLE);
                            execbutton.setText("啟修");
                            break;
                        case "2"://已啟修
                            reasontv.setVisibility(View.VISIBLE);
                            reasontv.setText("完修原因：");
                            callerrornosp.setVisibility(View.VISIBLE);
                            List<String> ls1 = new ArrayList<>();
                            ls1.add("維修完成，功能正常");
                            ls1.add("產線誤報修");
                            ArrayAdapter<String> as1 = new ArrayAdapter<>(eqrepairexec.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                            callerrornosp.setAdapter(as1);
                            execbutton.setText("完修");
                            break;
                        case "21"://維修信息錄入
                            reasontv.setVisibility(View.INVISIBLE);
                            callerrornosp.setVisibility(View.INVISIBLE);
                            execbutton.setText("None");
                            execbutton.setEnabled(false);
                            break;
                        case "3"://已完修
                            reasontv.setVisibility(View.INVISIBLE);
                            callerrornosp.setVisibility(View.INVISIBLE);
                            execbutton.setText("驗收");
                            break;
                        case "31"://已完修、誤報修
                            reasontv.setVisibility(View.INVISIBLE);
                            callerrornosp.setVisibility(View.INVISIBLE);
                            execbutton.setText("驗收");
                            break;
                        case "32"://QC驗收
                            reasontv.setVisibility(View.INVISIBLE);
                            callerrornosp.setVisibility(View.INVISIBLE);
                            execbutton.setText("驗收");
                            //execbutton.setEnabled(false);
                            break;
                        case "4"://已驗收，正常
                            reasontv.setVisibility(View.VISIBLE);
                            reasontv.setText("報修原因：");
                            callerrornosp.setVisibility(View.VISIBLE);

                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    List<String> ls1 = new ArrayList<>();
                                    ls1 = ps1.getRemoteInfo_geterrepairreason(eqsysidtv.getText().toString());
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = ls1;
                                    handle2.sendMessage(msg);
                                }
                            });
                            execbutton.setText("報修");

                        case "41"://已驗收，正常，誤報修
                            reasontv.setVisibility(View.VISIBLE);
                            reasontv.setText("報修原因：");
                            callerrornosp.setVisibility(View.VISIBLE);

                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    List<String> ls1 = new ArrayList<>();
                                    ls1 = ps1.getRemoteInfo_geterrepairreason(eqsysidtv.getText().toString());
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = ls1;
                                    handle2.sendMessage(msg);
                                }
                            });
                            execbutton.setText("報修");

                        case "0"://未上線
                            reasontv.setVisibility(View.VISIBLE);
                            reasontv.setText("報修原因：");
                            callerrornosp.setVisibility(View.VISIBLE);

                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    List<String> ls1 = new ArrayList<>();
                                    ls1 = ps1.getRemoteInfo_geterrepairreason(eqsysidtv.getText().toString());
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = ls1;
                                    handle2.sendMessage(msg);
                                }
                            });
                            execbutton.setText("報修");

                            break;

                        case "6":
                            reasontv.setVisibility(View.VISIBLE);
                            reasontv.setText("報修原因：");
                            callerrornosp.setVisibility(View.VISIBLE);

                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    List<String> ls1 = new ArrayList<>();
                                    ls1 = ps1.getRemoteInfo_geterrepairreason(eqsysidtv.getText().toString());
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = ls1;
                                    handle2.sendMessage(msg);
                                }
                            });
                            execbutton.setText("報修");

                            break;
                        case "7":
                            reasontv.setVisibility(View.VISIBLE);
                            reasontv.setText("報修原因：");
                            callerrornosp.setVisibility(View.VISIBLE);

                            MyThreadPool.pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PublicSOAP ps1 = new PublicSOAP();
                                    List<String> ls1 = new ArrayList<>();
                                    ls1 = ps1.getRemoteInfo_geterrepairreason(eqsysidtv.getText().toString());
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = ls1;
                                    handle2.sendMessage(msg);
                                }
                            });
                            execbutton.setText("報修");

                            break;
                        default:
                            Toast.makeText(eqrepairexec.this, "獲取狀態異常關閉，請聯絡MIS異常", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }
            }
            catch(Exception ex1)
            {
                execloadactivity.canclediglog();
                Toast.makeText(eqrepairexec.this, "未知異常", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler handle2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    List<String> ls1 = new ArrayList<>();
                    ls1 = (List<String>) msg.obj;
                    if (ls1 == null) {
                        return;
                    }
                    ArrayAdapter<String> as1 = new ArrayAdapter<>(eqrepairexec.this, R.layout.support_simple_spinner_dropdown_item, ls1);
                    callerrornosp.setAdapter(as1);
                }
            }
            catch(Exception ex1)
            {
                execloadactivity.canclediglog();
                Toast.makeText(eqrepairexec.this, "未知異常", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void submitionclick(View v) {
        final PublicSOAP ps1 = new PublicSOAP();
        switch(statusint)
        {
            case "1"://已報修
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepairstartrepair(Sessionuser, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
            case "0"://已報修
                /*execloadactivity.opendialog(eqrepairexec.this);
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepairstartrepair(Sessionuser, passowrd, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
                */
                 //0为未上线状态，所以应该采用报修的方法，不能用启修的方法
                starterrorcode=callerrornosp.getSelectedItem().toString();
                if(starterrorcode==null||starterrorcode.length()<=0)
                {
                    Toast.makeText(eqrepairexec.this, "啟修原因必須選擇", Toast.LENGTH_SHORT).show();
                    return;
                }
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepaircallrepair(Sessionuser, starterrorcode, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
            case "2"://已啟修
                finishreason=callerrornosp.getSelectedItem().toString();
                if(finishreason==null||finishreason.length()<=0)
                {
                    Toast.makeText(eqrepairexec.this, "完修原因不能為空", Toast.LENGTH_SHORT).show();
                    return;
                }
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepairfinishrepair(Sessionuser, passowrd, eqsysidtv.getText().toString(), "NokiaN1", finishreason);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
            case "3"://已完修
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepaircloserepair(Sessionuser, passowrd, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
            case "31"://已完修
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepaircloserepair(Sessionuser, passowrd, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
            case "32"://已完修
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepaircloserepair(Sessionuser, passowrd, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;

            case "4"://已驗收，正常
                starterrorcode=callerrornosp.getSelectedItem().toString();
                if(starterrorcode==null||starterrorcode.length()<=0)
                {
                    Toast.makeText(eqrepairexec.this, "啟修原因必須選擇", Toast.LENGTH_SHORT).show();
                    return;
                }
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepaircallrepair(Sessionuser, starterrorcode, eqsysidtv.getText().toString(), "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);
                    }
                });
                break;
            case "41"://已驗收，正常
                starterrorcode=callerrornosp.getSelectedItem().toString();
                if(starterrorcode==null||starterrorcode.length()<=0)
                {
                    Toast.makeText(eqrepairexec.this, "啟修原因必須選擇", Toast.LENGTH_SHORT).show();
                    return;
                }
                execloadactivity.opendialog(eqrepairexec.this,"正在執行");
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
//                        PublicSOAP ps1 = new PublicSOAP();
                        String ls1 = ps1.getRemoteInfo_eqrepaircallrepair(Sessionuser, starterrorcode, eqsysidtv.getText().toString(),  "NokiaN1");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = ls1;
                        handle3.sendMessage(msg);


                    }
                });
                break;
        }
    }

    private Handler handle3 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {

                    String ls1 = msg.obj.toString();

                    if (ls1 == null) {
                        return;
                    }
                    execloadactivity.canclediglog();

                    try
                    {
                        Integer.parseInt(ls1);
                        ls1="C813:"+"完成";

                    }
                    catch(Exception e1) {
                       // Toast.makeText(eqrepairexec.this, "作業完成", Toast.LENGTH_SHORT).show();
                       // return;
                    }

                    if (ls1.substring(0, 4).equals("C813")) {
                        loadform();
                        Toast.makeText(eqrepairexec.this, "作業完成", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String statuscode=ls1.substring(0,4).toString();
                        switch (statuscode)
                        {
                            case "C814":

                                String[] ss0=ls1.split(";");
                                String enginputstatus=ss0[1];
                                String acceptstatus=ss0[2];
                                String finishrepairtype=callerrornosp.getSelectedItem().toString();
                                switch (finishrepairtype)
                                {
                                    case "維修完成，功能正常":
                                        finishrepairtype="1";
                                        break;
                                    case "產線誤報修":
                                        finishrepairtype="2";
                                        break;
                                }

                                Intent intent = new Intent(eqrepairexec.this
                                        , eqrepairengdatain.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Eqid", Eqid);
                                bundle.putString("finishrepairtype", finishrepairtype);
                                bundle.putString("Eqname", eqnametv.getText().toString());
                                bundle.putString("enginputstatus", enginputstatus);
                                bundle.putString("acceptstatus", acceptstatus);
                                bundle.putString("flowid", flowid);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case "C815":
                                String[] ss1=ls1.split(";");
                                String sowner=ss1[1];
                                String sownertaks=ss1[2];

                                boolean checkboolean=false;
                                switch (sownertaks)
                                {
                                    case "ALL":
                                        checkboolean=true;
                                        break;
                                    case "ENG":
                                        if(sowner.equals(sownertaks))
                                        {
                                            checkboolean=true;
                                        }
                                        else
                                        {
                                            Toast.makeText(eqrepairexec.this
                                            ,sownertaks+"未完成",Toast.LENGTH_SHORT).show();
                                            checkboolean=false;
                                        }
                                        break;
                                    case "MFG":
                                        if(sowner.equals(sownertaks))
                                        {
                                            checkboolean=true;
                                        }
                                        else
                                        {
                                            Toast.makeText(eqrepairexec.this
                                                    ,sownertaks+"未完成",Toast.LENGTH_SHORT).show();
                                            checkboolean=false;
                                        }
                                        break;
                                    case "IPQC":
                                        if(sowner.equals(sownertaks))
                                        {
                                            checkboolean=true;
                                        }
                                        else
                                        {
                                            Toast.makeText(eqrepairexec.this
                                                    ,sownertaks+"未完成",Toast.LENGTH_SHORT).show();
                                            checkboolean=false;
                                        }
                                        break;
                                    default:
                                        Toast.makeText(eqrepairexec.this
                                                ,sownertaks+"未完成",Toast.LENGTH_SHORT).show();
                                        checkboolean=false;
                                        break;
                                }
                                if(checkboolean)
                                {
                                    Intent intent1 = new Intent(eqrepairexec.this
                                            , eqrepiaraccept.class);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("Eqid", Eqid);
                                    bundle1.putString("sowner", sowner);
                                    bundle1.putString("flowid", flowid);
                                    intent1.putExtras(bundle1);
                                    startActivity(intent1);
                                }
                                break;
                            case "C816":
                                Toast.makeText(eqrepairexec.this, "該機台膠閥未上線，不能報修，知悉!", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(eqrepairexec.this, ls1, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }
            catch(Exception ex1)
            {
                execloadactivity.canclediglog();
                Toast.makeText(eqrepairexec.this, "未知異常", Toast.LENGTH_SHORT).show();
            }

        }
    };

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

                        /*
                        try {
                            String[] ss1 = paramObject.toString().split(":");
                            switch (ss1[0]) {
                                case "1":
                                    flowid = ss1[1];
                                    break;
                                case "0":
                                    Toast.makeText(eqrepairexec.this, staticfailcode + ss1[1], Toast.LENGTH_SHORT).show();
                                    eqrepairexec.this.finish();
                                    break;
                            }
                        }
                        catch(Exception ex1)
                        {
                            Toast.makeText(eqrepairexec.this,staticfailcode+ex1.getMessage(),Toast.LENGTH_SHORT).show();
                            eqrepairexec.this.finish();
                        }
                        */
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(eqrepairexec.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_LONG).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(eqrepairexec.this
                                , "操作失敗，請求異常", Toast.LENGTH_LONG).show();
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
                    case "getflowid":

                        String status0 = j1.get(0).get_valuelist().get(0).get(1).get_value();
                        String msg0 = j1.get(0).get_valuelist().get(0).get(2).get_value();

                        switch (status0) {
                            case "0":
                                Toast.makeText(eqrepairexec.this, msg0, Toast.LENGTH_SHORT).show();
                                break;
                            case "1":
                                flowid = msg0;
                                break;
                            default:
                                Toast.makeText(eqrepairexec.this, msg0, Toast.LENGTH_SHORT).show();
                                break;
                        }

                        break;

                    case "geteqrepairmachinedata":
                        String status1 = j1.get(0).get_valuelist().get(0).get(1).get_value();
                        String msg1 = j1.get(0).get_valuelist().get(0).get(2).get_value();
                        switch (status1) {
                            case "0":
                                Toast.makeText(eqrepairexec.this, msg1, Toast.LENGTH_SHORT).show();
                                eqrepairexec.this.finish();
                                break;
                            case "1":
                                if (j1.get(1).get_valuelist() == null) {
                                    Toast.makeText(eqrepairexec.this, "無資料", Toast.LENGTH_SHORT).show();
                                    eqrepairexec.this.finish();
                                } else {
                                    eqnametv.setText(j1.get(1).get_valuelist().get(0).get(0).get_value());
                                    eqoptv.setText(j1.get(1).get_valuelist().get(0).get(1).get_value());
                                    eqlinetv.setText(j1.get(1).get_valuelist().get(0).get(2).get_value());
                                }
                                    break;
                            default:
                                Toast.makeText(eqrepairexec.this, msg1, Toast.LENGTH_SHORT).show();
                                eqrepairexec.this.finish();
                                break;
                        }
                        break;
                }
            }
        }
        catch(Exception ex1)
        {
            Toast.makeText(eqrepairexec.this,"未知異常-"+ex1.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
