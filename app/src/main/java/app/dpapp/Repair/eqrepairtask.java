package app.dpapp.Repair;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class eqrepairtask extends AppCompatActivity {


    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;
    String eqid;
    private ListView mListView;
    private List<PublicSOAP.machinetaskcls> mList;
    private CheckAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_repairtask);
         mListView = (ListView) findViewById(R.id.lv_repair);
         mList = new ArrayList<>();
         mAdapter = new CheckAdapter(mList,this);
         final Staticdata app = (Staticdata)getApplication();
         Sessionuser = app.getLoginUserID();
    }

    protected void onResume() {
        super.onResume();
        loadingwaitingdata() ;
    }

    public void loadingwaitingdata(){
        try {
            execloadactivity.opendialog(eqrepairtask.this,"正在執行");
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mListView.invalidate();

            MyThreadPool.pool.execute(
                    new Runnable() {

                        @Override
                        public void run() {
                            PublicSOAP ps1 = new PublicSOAP();
                            List<PublicSOAP.machinetaskcls> le1 = new ArrayList<PublicSOAP.machinetaskcls>();

                            le1 = ps1.getRemoteInfo_eqrepairtask(Sessionuser);

                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = le1;
                            handle.sendMessage(msg);
                        }
                    }
            );
        }
        catch(Exception ex1)
        {

        }
    }


    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

                if (msg.what == 0) {

                    mList = (List<PublicSOAP.machinetaskcls>) msg.obj;
                    if (mList == null || "".equals(mList)){
                        execloadactivity.canclediglog();
                        Toast.makeText(eqrepairtask.this, "無資料", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAdapter = new CheckAdapter(mList,eqrepairtask.this);
                    mListView.setAdapter(mAdapter);
                    execloadactivity.canclediglog();
                }
        }
    };

    class CheckAdapter extends BaseAdapter {
        private List<PublicSOAP.machinetaskcls> mList;
        private Context mContext;

        public CheckAdapter(List<PublicSOAP.machinetaskcls> mList,Context mContext) {
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
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_source9,null);
                holder = new ViewHolder();
                holder.Text1 = (TextView) convertView.findViewById(R.id.lv9_calltimetext1);
                holder.Text2 = (TextView) convertView.findViewById(R.id.lv9_callusertext1);
                holder.Text3 = (TextView) convertView.findViewById(R.id.lv9_machinenametext1);
                holder.Text4 = (TextView) convertView.findViewById(R.id.lv9_statustext1);
                holder.Text5 = (TextView) convertView.findViewById(R.id.lv9_issuememotext1);
                holder.Text6 = (TextView) convertView.findViewById(R.id.lv9_starttimetext1);
                holder.Text7 = (TextView) convertView.findViewById(R.id.lv9_startusertext1);
                holder.Text8 = (TextView) convertView.findViewById(R.id.lv9_endtimetext1);
                holder.Text9 = (TextView) convertView.findViewById(R.id.lv9_endusertext1);
                holder.Text10 = (TextView) convertView.findViewById(R.id.lv9_closetimetext1);
                holder.Text11 = (TextView) convertView.findViewById(R.id.lv9_closeusertext1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String tempmachinestatus=mList.get(position).getSTATE().toString();
            String temppeflag=mList.get(position).getPECONFIRMOP().toString();
            String tempQCFLAG=mList.get(position).getQCFLAG().toString();
            String tempATT9=mList.get(position).getATT9().toString();
            int c = 0;

            if (tempmachinestatus.equals("1") ) //報修
            {
                //LightCoral Color.rgb(240,128,128);
                c=Color.rgb(240,128,128);
            }
            else if (tempmachinestatus.equals("2") && temppeflag.equals("0"))  //啟修    未填寫工程維修信息錄入
            {
                //Pink Color.rgb(255,192,203);
                c=Color.rgb(255,192,203);
            }
            else if (tempmachinestatus.equals("2") && (temppeflag.equals("1") || temppeflag.equals("2")))  //啟修     已填寫工程維修信息錄入
            {
                //SkyBlue  Color.rgb(135,206,235);
                c=Color.rgb(135,206,235);
            }
            else if (tempmachinestatus.equals("3")  && tempQCFLAG.equals("1") && tempATT9.equals("C")==false) //完修  QC確認驗收
            {
                //dataGridView1.Rows[j].DefaultCellStyle.BackColor = Color.Yellow;
                //Yellow  Color.rgb(255,255,0);
                c = Color.rgb(255, 255, 0);

            }
            else if (tempmachinestatus.equals("3"))  //完修
            {
                //dataGridView1.Rows[j].DefaultCellStyle.BackColor = Color.LightBlue;
                //LightBlue  Color.rgb(173,216,230);
                c=Color.rgb(173,216,230);
            }
            else if (tempmachinestatus.equals("4")) //驗收
            {
                //dataGridView1.Rows[j].DefaultCellStyle.BackColor = Color.LightGreen;
                //LightGreen  Color.rgb(144,238,144);
                c= Color.rgb(144,238,144);
            }
            else if (tempmachinestatus.equals("31")) //產線誤報修
            {
                //dataGridView1.Rows[j].DefaultCellStyle.BackColor = Color.LightSalmon;
                //LightSalmon  Color.rgb(255,160,122);
                c=Color.rgb(255,160,122);
            }

            holder.Text1.setText(mList.get(position).getREPAIRDATE());
            holder.Text1.setBackgroundColor(c);
            holder.Text2.setText(mList.get(position).getREPAIROP());
            holder.Text2.setBackgroundColor(c);
            holder.Text3.setText(mList.get(position).getMACHINENO());
            holder.Text3.setBackgroundColor(c);
            holder.Text4.setText(tempmachinestatus + temppeflag + tempQCFLAG + tempATT9);
            holder.Text4.setBackgroundColor(c);
            holder.Text5.setText(mList.get(position).getDOWNFLAG());
            holder.Text5.setBackgroundColor(c);
            holder.Text6.setText(mList.get(position).getSENDDATE());
            holder.Text6.setBackgroundColor(c);
            holder.Text7.setText(mList.get(position).getSENDOP());
            holder.Text7.setBackgroundColor(c);
            holder.Text8.setText(mList.get(position).getSERVICEDATE());
            holder.Text8.setBackgroundColor(c);
            holder.Text9.setText(mList.get(position).getSERVICEOP());
            holder.Text9.setBackgroundColor(c);
            holder.Text10.setText(mList.get(position).getENDDATE());
            holder.Text10.setBackgroundColor(c);
            holder.Text11.setText(mList.get(position).getENDOP());
            holder.Text11.setBackgroundColor(c);
            convertView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eqid = mList.get(position).getMACHINENOID();
                            Intent intent = new Intent(eqrepairtask.this
                                    , eqrepairexec.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("Eqid", eqid);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
            );
            return convertView;
        }
    }

    class ViewHolder {
        TextView Text1,Text2,Text3,Text4,Text5,Text6,Text7,Text8,Text9,Text10,Text11;
    }
}