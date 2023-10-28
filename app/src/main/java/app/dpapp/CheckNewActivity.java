package app.dpapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.execloadactivity;

/**
 *  Created by TPP on 2017/11/8.
 *  點檢核查
 */
public class CheckNewActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner mAreaSpinner,mTypeSpinner,mDevicenoSpinner,mLineSpinner;
    private Button mButton;
    private ListView mListView;
    private CheckAdapter mAdapter;
    private List<CheckNewBean> mList;
    private ArrayAdapter<String> mAreaAdapter;
    private ArrayAdapter<String> mTypeAdapter;
    private ArrayAdapter<String> mDevicenoAdapter;
    private ArrayAdapter<String> mLineAdapter;

    private List<String> mAreaList,mTypeList,mDevicenoList,mLineList;
    private TextView mTvDate;
    private int mYear,mMonth,mDate;
    private Calendar mCalendar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checknew_layout);
        Staticdata sc = (Staticdata) getApplication();

        mCalendar = Calendar.getInstance();
        mAreaSpinner = (Spinner) findViewById(R.id.sp_check_area);
        mTypeSpinner = (Spinner) findViewById(R.id.sp_check_type);
        mDevicenoSpinner = (Spinner) findViewById(R.id.sp_check_machineno);
        mLineSpinner = (Spinner) findViewById(R.id.sp_check_line);
        mListView = (ListView) findViewById(R.id.lv_check);
        mTvDate = (TextView) findViewById(R.id.tv_check_date);
        mYear = mCalendar.get(Calendar.YEAR);

        mMonth = mCalendar.get(Calendar.MONTH);
        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);
        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
        mTvDate.setOnClickListener(this);
        mList = new ArrayList<>();
        mButton = (Button) findViewById(R.id.bt_check_check);
        mButton.setOnClickListener(this);
        mAreaList = new ArrayList<>();
        mAreaAdapter = new ArrayAdapter<>(this,R.layout.spinner_textview,mAreaList);

        mTypeList = new ArrayList<>();
        mTypeList.add("O");
        mTypeList.add("N");
        mTypeList.add("M");
        mTypeAdapter = new ArrayAdapter<>(this,R.layout.spinner_textview,mTypeList);
        mTypeSpinner.setAdapter(mTypeAdapter);

        mDevicenoList = new ArrayList<>();
        mDevicenoAdapter = new ArrayAdapter<>(this,R.layout.spinner_textview,mDevicenoList);

        mLineList = new ArrayList<>();
        mLineAdapter = new ArrayAdapter<>(this,R.layout.spinner_textview,mLineList);
        bindSpinnerdata();

        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      getLineData(mAreaSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_check_check:
                if(mDevicenoSpinner.getSelectedItem() == null || "".equals(mDevicenoSpinner.getSelectedItem())) {
                    Toast.makeText(CheckNewActivity.this,"請選擇幾種",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mAreaSpinner.getSelectedItem() == null || "".equals(mAreaSpinner.getSelectedItem())) {
                    Toast.makeText(CheckNewActivity.this,"請選擇區域",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mLineSpinner.getSelectedItem() == null || "".equals(mLineSpinner.getSelectedItem())) {
                    Toast.makeText(CheckNewActivity.this,"請選擇纖體",Toast.LENGTH_SHORT).show();
                    return;
                }
                execloadactivity.opendialog(this, "正在執行");
                Check(mTypeSpinner.getSelectedItem().toString(),
                        mDevicenoSpinner.getSelectedItem().toString(),
                        mTvDate.getText().toString(),
                        mAreaSpinner.getSelectedItem().toString(),
                        mLineSpinner.getSelectedItem().toString());
                break;
            case R.id.tv_check_date:
                new DatePickerDialog(CheckNewActivity.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year
                             , int monthOfYear, int dayOfMonth) {
                        mTvDate.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },mYear,mMonth,mDate).show();
                break;
        }
    }

    /**
     * 查詢按鈕
     *
     */

    public void Check(final String type,final String deviceno,final String searchdate,final String location,final String line) {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<CheckNewBean> list = new ArrayList<>();
                list = ps1.getCheckData(type,deviceno,searchdate,location,line);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void bindSpinnerdata()
    {
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {

                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<>();
                ls1 = ps1.getRemoteInfo_geteqcheckdevicenolh(Staticdata.type,null);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = ls1;
                mHandler.sendMessage(msg);
            }
        });
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<>();
                ls1 = ps1.getCheckArea();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = ls1;
                mHandler.sendMessage(msg);
            }
        });

    }

    public void getLineData(final String area) {

        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
                PublicSOAP ps1 = new PublicSOAP();
                List<String> ls1 = new ArrayList<>();
                ls1 = ps1.getCheckLine(area);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = ls1;
                mHandler.sendMessage(msg);
            }
        });
    }

      private Handler mHandler = new Handler() {

          @Override
          public void handleMessage(Message msg) {

              switch (msg.what) {
                  case 0:
                    mDevicenoList = (List<String>) msg.obj;
                    mDevicenoAdapter = new ArrayAdapter<>(CheckNewActivity.this, R.layout.spinner_textview, mDevicenoList);
                    mDevicenoSpinner.setAdapter(mDevicenoAdapter);
                      break;
                  case 1:
                    mAreaList = (List<String>) msg.obj;
                    mAreaAdapter = new ArrayAdapter<>(CheckNewActivity.this, R.layout.spinner_textview, mAreaList);
                    mAreaSpinner.setAdapter(mAreaAdapter);
                      if(mAreaSpinner.getSelectedItem()!= null && !"".equals(mAreaSpinner.getSelectedItem())) {
                          getLineData(mAreaSpinner.getSelectedItem().toString());
                      }
                    break;

                  case 2:
                      mLineList = (List<String>) msg.obj;
                      if(mLineList != null && mLineList.size() > 0) {
                          mLineAdapter = new ArrayAdapter<>(CheckNewActivity.this, R.layout.spinner_textview, mLineList);
                          mLineSpinner.setAdapter(mLineAdapter);
                      }
                      break;

                  case 3:
                      execloadactivity.canclediglog();
                      mList = (List<CheckNewBean>) msg.obj;
                      if(mList == null) {
                          Toast.makeText(CheckNewActivity.this,"查詢為空！",Toast.LENGTH_SHORT).show();
                      } else {

                          mAdapter = new CheckAdapter(mList,CheckNewActivity.this);
                          mListView.setAdapter(mAdapter);
                      }
                      break;
              }
              super.handleMessage(msg);
          }
      };


    class CheckAdapter extends BaseAdapter {

        private List<CheckNewBean> mList;
        private Context context;

        public CheckAdapter(List<CheckNewBean> mList,Context context) {
            this.mList = mList;
            this.context = context;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_check_item,null);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.tv_listview_area);
                holder.text2 = (TextView) convertView.findViewById(R.id.tv_listview_line);
                holder.text3 = (TextView) convertView.findViewById(R.id.tv_listview_machineno);
                holder.text4 = (TextView) convertView.findViewById(R.id.tv_listview_deviceno);
                holder.text5 = (TextView) convertView.findViewById(R.id.tv_listview_form);
                holder.text6 = (TextView) convertView.findViewById(R.id.tv_listview_time);
                holder.text7 = (TextView) convertView.findViewById(R.id.tv_listview_update);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text1.setText(mList.get(position).getBULIDINGNO());
            holder.text2.setText(mList.get(position).getLINE());
            holder.text3.setText(mList.get(position).getMACHINENOID());
            holder.text4.setText(mList.get(position).getATT2());
            holder.text5.setText(mList.get(position).getEXCELNAME());
            holder.text6.setText(mList.get(position).getUPDATEDATE());
            holder.text7.setText(mList.get(position).getCHECKDATAID());

            return convertView;
        }
    }

    class ViewHolder {

        TextView text1,text2,text3,text4,text5,text6,text7;
    }
}
