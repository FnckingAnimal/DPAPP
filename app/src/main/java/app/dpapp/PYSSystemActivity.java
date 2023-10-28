package app.dpapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.dpapp.IncludingSFC.SFCStaticdata;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.appcdl.NetUtils;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;

/**
 * Created by S7187445 on 2018/1/29.
 */
public class PYSSystemActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mTvDate;
    private EditText mNumber,mBeizhu;
    private TextView mName,mClass,mZhanWei;
    private Spinner mSpinner,mGradeSpinner;
    private int mYear,mMonth,mDate;
    private Calendar mCalendar;
    private TextView mCommit;
    private List<PysBean> mList;
    private List<PysBenTwo> mList2;
    private Button mBt;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mGradeAdapter;
    private List<String> list2;
    private String str;
    private String mId;
//    private String zhubie;
    private String mUserId;
    private String HDSerialNo;
    private ListView mListView;
    private MyAdapter mMyAdapter;
    private List<PysBean3> mList3;
//    private TextView mWeight;
    private List<String> mGradeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pys_system_layout);
        init();
    }

    public void init() {
        mTvDate = (TextView) findViewById(R.id.pys_tv_date_t);
        mNumber = (EditText) findViewById(R.id.pys_et_number);
//        mGrades = (EditText) findViewById(R.id.pys_et_grades);
        mGradeSpinner = (Spinner) findViewById(R.id.pys_sp_grade);
        mName = (TextView) findViewById(R.id.pys_tv_name_t);
        mClass = (TextView) findViewById(R.id.pys_tv_class_t);
        mZhanWei = (TextView) findViewById(R.id.pys_tv_zhan_t);
        mSpinner = (Spinner) findViewById(R.id.pys_sp_item);
        mCommit = (TextView) findViewById(R.id.pys_tv_commit);
        mBeizhu = (EditText) findViewById(R.id.pys_et_notes);
        mBt = (Button) findViewById(R.id.bt_pys_commit);
        mListView = (ListView) findViewById(R.id.pys_lv);
//        mWeight = (TextView) findViewById(R.id.pys_tv_weight);
        mBt.setOnClickListener(this);
        mCommit.setOnClickListener(this);
//        mGrades.setOnClickListener(this);
        mBeizhu.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
        mTvDate.setOnClickListener(this);
        Staticdata sc = (Staticdata) getApplication();
        mUserId = sc.getLoginUserID();
        HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");
        SFCStaticdata.staticmember.HDSerialNo=HDSerialNo;

        mYear = mCalendar.get(Calendar.YEAR);
//      int date = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);
        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
        list2 = new ArrayList<>();  
        mList = new ArrayList<>();
        mList2 = new ArrayList<>();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str = mList2.get(position).getATT2();
                mId = mList2.get(position).getID();
                mBt.setClickable(true);
//              zhubie = mList2.get(position).getZHUBIE();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mGradeList = new ArrayList();
        mGradeList.add("50");
        mGradeList.add("30");
        mGradeList.add("-10");
        mGradeList.add("-30");
        mGradeList.add("-50");
        mGradeAdapter = new ArrayAdapter<>(PYSSystemActivity.this, R.layout.spinner_textview, mGradeList);
        mGradeSpinner.setAdapter(mGradeAdapter);


}

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                     mList = (List<PysBean>) msg.obj;
                    if(mList != null) {
                        mName.setText(mList.get(0).getUSERNAME());
                        mClass.setText(mList.get(0).getSHIFT());
                        mZhanWei.setText(mList.get(0).getOPNO());

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {
                                PublicSOAP ps1 = new PublicSOAP();
                                List<PysBenTwo> ls1 = new ArrayList<>();
                                ls1 = ps1.getpysproject(mList.get(0).getGROUPNAME());
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = ls1;
                                mHandler2.sendMessage(msg);
                            }
                        });
                    } else {
                        Toast.makeText(PYSSystemActivity.this,"信息為空",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 1:
                    execloadactivity.canclediglog();
                    String str = (String) msg.obj;
                     if("true".equals(str)) {
                         Toast.makeText(PYSSystemActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                         MyThreadPool.pool.execute(new Runnable() {
                             @Override
                             public void run() {
                                 PublicSOAP ps1 = new PublicSOAP();
                                 List<PysBean3> ls1 = new ArrayList<>();
                                 ls1 = ps1.getpysusercodeinfo(mTvDate.getText().toString(), mNumber.getText().toString());
                                 Message msg = new Message();
                                 msg.what = 2;
                                 msg.obj = ls1;
                                 mHandler.sendMessage(msg);
                             }
                         });
//                         finish();

                     } else {
                         Toast.makeText(PYSSystemActivity.this,"提交失敗，請檢查",Toast.LENGTH_SHORT).show();
                     }

                    break;
                case 2:
                    mList3 = (List<PysBean3>) msg.obj;
                    if(mList3 != null) {
                        mBt.setClickable(false);
                        mMyAdapter = new MyAdapter(PYSSystemActivity.this,mList3);
                        mListView.setAdapter(mMyAdapter);
                    } else {
                        Toast.makeText(PYSSystemActivity.this,"獲取信息失敗",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    mList2 = (List<PysBenTwo>) msg.obj;
                    if(mList2.size() != 0) {
                        str = mList2.get(0).getATT2();
                        mId= mList2.get(0).getID();
//                        mWeight.setText("(-" + Float.parseFloat(mList2.get(0).getBIZHONG()) * 100 + "~" + Float.parseFloat(mList2.get(0).getBIZHONG()) * 100 + ")");
//                        zhubie = mList2.get(0).getZHUBIE();
                        list2 = new ArrayList<>();
                        for (int i = 0; i < mList2.size(); i++) {
                            list2.add(mList2.get(i).getXIANGMU());
                        }
                        mAdapter = new ArrayAdapter<>(PYSSystemActivity.this, R.layout.spinner_textview, list2);
                        mSpinner.setAdapter(mAdapter);
                    } else {
                      Toast.makeText(PYSSystemActivity.this,"無資料",Toast.LENGTH_SHORT).show();
                    }
                 break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pys_tv_date_t:
                new DatePickerDialog(PYSSystemActivity.this,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mTvDate.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                }
            },mYear,mMonth,mDate).show();
            mBt.setClickable(true);
            break;

            case R.id.pys_tv_commit:
                if(mNumber.getText() != null && !"".equals(mNumber.getText())) {
                    MyThreadPool.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            PublicSOAP ps1 = new PublicSOAP();
                            List<PysBean> ls1 = new ArrayList<>();
                            ls1 = ps1.getpysuserinfo(mNumber.getText().toString());
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = ls1;
                            mHandler.sendMessage(msg);
                        }
                    });
                } else {
                    Toast.makeText(PYSSystemActivity.this,"請輸入工號!",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_pys_commit:
                execloadactivity.opendialog(this, "正在執行");
                if(TextUtils.isEmpty(mNumber.getText())) {
                   Toast.makeText(PYSSystemActivity.this,"請輸入工號",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
//                if(TextUtils.isEmpty(mGrades.getText())) {
//                    Toast.makeText(PYSSystemActivity.this,"請輸入分數",Toast.LENGTH_SHORT).show();
//                    execloadactivity.canclediglog();
//                    return;
//                }
                if(mBeizhu.getText() == null || "".equals(mBeizhu.getText())) {
                    Toast.makeText(PYSSystemActivity.this,"請輸入備註",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        PublicSOAP ps1 = new PublicSOAP();
                        String str2 = ps1.commitPys(str, mId, mNumber.getText().toString(), mName.getText().toString(), mClass.getText().toString(), mTvDate.getText().toString(), mGradeSpinner.getSelectedItem().toString(), mSpinner.getSelectedItem().toString(), mList.get(0).getEITEM8(), mUserId, mBeizhu.getText().toString(), HDSerialNo);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = str2;
                        mHandler.sendMessage(msg);
                    }
                });

                break;
//            case R.id.pys_et_grades:
//
//                mBt.setClickable(true);
//                break;
            case R.id.pys_et_notes:
//                mBt.setClickable(true);

                break;

        }
    }


    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<PysBean3> mList;

        public MyAdapter (Context context,List<PysBean3> mList) {
            this.context = context;
            this.mList = mList;
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
            ViewHolder2 holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_output,null);
                holder = new ViewHolder2();
                holder.mTextView1 = (TextView) convertView.findViewById(R.id.tv_listview_pihao);
                holder.mTextView2 = (TextView) convertView.findViewById(R.id.tv_listview_jitaihao);
                holder.mTextView3 = (TextView) convertView.findViewById(R.id.tv_listview_ip);
                holder.mTextView4 = (TextView) convertView.findViewById(R.id.tv_listview_time);
                holder.mTextView5 = (TextView) convertView.findViewById(R.id.tv_listview_creater);
                holder.mTextView6 = (TextView) convertView.findViewById(R.id.tv_listview_one);
                holder.mTextView7 = (TextView) convertView.findViewById(R.id.tv_listview_two);
                holder.mTextView8 = (TextView) convertView.findViewById(R.id.tv_listview_three);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder2) convertView.getTag();
            }

//            final View view = convertView;
            holder.mTextView1.setText(mList.get(position).getUSERID());
            holder.mTextView2.setText(mList.get(position).getUSERNAME());
            holder.mTextView3.setText(mList.get(position).getSHIFT());
            holder.mTextView4.setText(mList.get(position).getXIANGMU());
            holder.mTextView5.setText(mList.get(position).getDATES());
            holder.mTextView6.setText(mList.get(position).getFENZHI());
            holder.mTextView7.setText(mList.get(position).getZHUBIE());
            holder.mTextView8.setText(mList.get(position).getCREATETIME());

            return convertView;
        }
    }

    class ViewHolder2 {

        TextView mTextView1,mTextView2,mTextView3,mTextView4,mTextView5,mTextView6,mTextView7,mTextView8;
    }
}