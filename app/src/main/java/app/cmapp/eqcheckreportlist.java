package app.cmapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;

/**
 * Created by Tod on 2016/4/15.
 */
public class eqcheckreportlist extends AppCompatActivity {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String Eqid;
    protected String Checkdataid;
    protected String checktype;
    private String sysErrorcode; // 1:获取资料异常0

    //定义数据源
    /**
     * ATTENTION: This was auto-generated to implement the App In   dexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private List<String[]> mList;
    private ListView mListView;
    private CheckAdapter mAdapter;

    Button bt1;
//    LinearLayout lly1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checkreportlist);

        try {
            mListView = (ListView) findViewById(R.id.lv_checkre);
            mList = new ArrayList<>();
            mAdapter = new CheckAdapter(mList,this);
            mListView.setAdapter(mAdapter);
            Bundle bundle = this.getIntent().getExtras();
            final Staticdata app = (Staticdata) getApplication();
            Sessionuser = app.getLoginUserID();
            Eqid = bundle.getString("Eqid");
            //Eqid="GS10";
            Checkdataid = bundle.getString("checkdataid");
            //Deviceno = bundle.getString("Deviceno");
            checktype = bundle.getString("checktype");
            //checktype="0";
            bt1 = (Button) findViewById(R.id.eq_checklistbutton1);

//            lly1 = (LinearLayout) findViewById(R.id.eqchecklistLinearLayout1);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    getRemoteInfo();
                    Message msg = handle.obtainMessage();
                    msg.obj = mList;
                    msg.what = 0;
                    handle.sendMessage(msg);
                }
            }).start();
        } catch (Exception e1)

        {

        }

    }

    public void getRemoteInfo() {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "GetNewCheckStructheadTop20";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckStructheadTop20";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            //rpc.addProperty("machinesysid", scanstr1);
            rpc.addProperty("machinesysid", Eqid);
            rpc.addProperty("userid",Sessionuser);   //0:點檢 1:保養

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
                sysErrorcode = "1";
                return;
            }
            SoapObject r1 = (SoapObject) envelope.bodyIn;
            SoapObject soapchild;
            try {
                soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
            } catch (Exception e) {
                e.printStackTrace();
                sysErrorcode = "1";
                return;
            }

            String tempsoap1 = null;
            String tempsoap2 = null;
            String tempsoap3 = null;
            String tempsoap4 = null;
            String tempsoap5 = null;
            String tempsoap6 = null;
            String tempsoap7 = null;
            String tempsoap8 = null;
            String tempsoap9 = null;
            String tempsoap10 = null;

            try {
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                    tempsoap1 = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                    tempsoap2 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKDATAID").toString();
                    tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("ISEND").toString();
                    tempsoap4 = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString();
                    tempsoap5 = ((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString();
                    tempsoap10 = ((SoapObject) soapchild.getProperty(i)).getProperty("FILEVERSION").toString();

                    try {
                        tempsoap6 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKTIME").toString();
                    } catch (Exception e) {
                        tempsoap6 = "";
                        sysErrorcode = "2";

                    }
                    try {
                        tempsoap7 = ((SoapObject) soapchild.getProperty(i)).getProperty("UPDATEDATE").toString();
                    } catch (Exception e) {
                        tempsoap7 = "";
                        sysErrorcode = "3";

                    }
                    try {
                        tempsoap9 = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKMODLE").toString();
                    } catch (Exception e) {
                        tempsoap9 = "";
                        sysErrorcode = "4";

                    }
                    try {
                        tempsoap8 = ((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString();
                    } catch (Exception e) {
                        tempsoap8 = "";
                        sysErrorcode = "5";

                    }
                    switch (tempsoap3) {
                        case "Y":
                            tempsoap3 = "已完成";
                            break;
                        case "O":
                            tempsoap3 = "強制結束";
                            break;
                        case "N":
                            tempsoap3 = ((SoapObject) soapchild.getProperty(i)).getProperty("USERDEPT").toString();
                            break;
                        default:
                            break;
                    }

                    mList.add(
                            new String[]
                                    {
                                            tempsoap1,
                                            tempsoap2,
                                            tempsoap3,
                                            tempsoap4,
                                            tempsoap5,
                                            tempsoap6,
                                            tempsoap7,
                                            tempsoap8,
                                            tempsoap9,
                                            tempsoap10
                                    }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                sysErrorcode = "1";
                return;
            }
        } catch (Exception e1) {
            //return;
        }
    }


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what == 0) {

                if (sysErrorcode != null && sysErrorcode.equals("1")) {
                    Toast.makeText(eqcheckreportlist.this, "获取资料异常", Toast.LENGTH_LONG).show();
                    return;
                }

                //=2为获取不到资料
                if (sysErrorcode != null && sysErrorcode.equals("2")) {
                    Toast.makeText(eqcheckreportlist.this, "發起時間為空，請檢查！", Toast.LENGTH_LONG).show();
                }
                if (sysErrorcode != null && sysErrorcode.equals("3")) {
                    Toast.makeText(eqcheckreportlist.this, "更新時間為空，請檢查！", Toast.LENGTH_LONG).show();
                }
                if (sysErrorcode != null && sysErrorcode.equals("4")) {
                    Toast.makeText(eqcheckreportlist.this, "點檢模式為空，請檢查！", Toast.LENGTH_LONG).show();
                }
                if (sysErrorcode != null && sysErrorcode.equals("5")) {
                    Toast.makeText(eqcheckreportlist.this, "幾種為空，請檢查！", Toast.LENGTH_LONG).show();
                }

                if(mList.size() == 0) {
                    Toast.makeText(eqcheckreportlist.this,"",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter = new CheckAdapter(mList,eqcheckreportlist.this);
                mListView.setAdapter(mAdapter);

            }
        }
    };

        class CheckAdapter extends BaseAdapter {
            private List<String[]> mList;
            private Context mContext;

            public CheckAdapter(List<String[]> mList, Context mContext) {
                this.mList = mList;
                this.mContext = mContext;
            }

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_eqlist, null);
                    holder = new ViewHolder();
                    holder.Text1 = (TextView) convertView.findViewById(R.id.listviewsource5Mname1);     //設備名稱
                    holder.Text2 = (TextView) convertView.findViewById(R.id.listviewsource5Mname2);     //當前狀態
                    holder.Text3 = (TextView) convertView.findViewById(R.id.listviewsource5Mname7);     //機種
                    holder.Text4 = (TextView) convertView.findViewById(R.id.listviewsource5Mname3);     //站位
                    holder.Text5 = (TextView) convertView.findViewById(R.id.listviewsource5Mname4);     //纖體
                    holder.Text6 = (TextView) convertView.findViewById(R.id.listviewsource5Mname8);     //點檢模式
                    holder.Text7 = (TextView) convertView.findViewById(R.id.listviewsource5Mname5);     //發起時間
                    holder.Text8 = (TextView) convertView.findViewById(R.id.listviewsource5Mname6);     //更新時間
                    holder.mButton = (Button) convertView.findViewById(R.id.listviewsource5Mbutton1);   //表單編號
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                String tempmachinestatus = mList.get(position)[2].toString();
                int c = 0;
                if (tempmachinestatus.equals("已完成")) {
                    c = Color.rgb(144, 238, 144);//light green

                } else if (tempmachinestatus.equals("強制結束")) {
                    c = Color.rgb(255, 255, 0);//yellow

                } else {

                    c = Color.rgb(240, 128, 128);//like red

                }

                holder.Text1.setText(mList.get(position)[0]);
                holder.Text1.setBackgroundColor(c);
                holder.Text2.setText(mList.get(position)[2]);
                holder.Text2.setBackgroundColor(c);
                holder.Text3.setText(mList.get(position)[7]);
                holder.Text3.setBackgroundColor(c);
                holder.Text4.setText(mList.get(position)[3]);
                holder.Text4.setBackgroundColor(c);
                holder.Text5.setText(mList.get(position)[4]);
                holder.Text5.setBackgroundColor(c);
                holder.Text6.setText(mList.get(position)[8]);
                holder.Text6.setBackgroundColor(c);
                holder.Text7.setText(mList.get(position)[5]);
                holder.Text7.setBackgroundColor(c);
                holder.Text8.setText(mList.get(position)[6]);
                holder.Text8.setBackgroundColor(c);
                holder.mButton.setText(mList.get(position)[1]);
                holder.mButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(eqcheckreportlist.this
                                        , eqcheckreportdetaillist.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("Sessionuser", Sessionuser);
                                bundle.putString("Eqid", Eqid);
                                bundle.putString("checkdataid", ((Button) v).getText().toString());
                                bundle.putString("Eqopname", holder.Text4.getText().toString());
                                bundle.putString("Eqlinename", holder.Text5.getText().toString());
                                bundle.putString("Fileversion",mList.get(position)[9]);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        }
                );
                return convertView;
            }
        }

        class ViewHolder {
            TextView Text1, Text2, Text3, Text4, Text5, Text6, Text7, Text8;
            Button mButton;
        }
}