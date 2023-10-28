package app.dpapp.ra;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.eqchecklist;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;
import app.dpapp.utils.DialogShowUtil;

public class Rel_Billboardsystem_Record_Activity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup rg1;
    private RadioButton rb_time, rb_dev, rb_lotno, rb_config;
    private LinearLayout ll_time, ll_dev, ll_lotno, ll_config;
    private int flag_select;
    private EditText et_starttime, et_endtime, et_dev, et_lotno, et_config;
    private String msg_dialog;

    private RecyclerView rcv;
    List<String[]> listdata;
    private ListView lv1;
    private MyAdpter myAdpter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel_billboardsystem_record);
        rg1 = findViewById(R.id.rg_Rel_Billboardsystem_record_rg1);
        rb_time = findViewById(R.id.rb_Rel_Billboardsystem_record_time);
        rb_dev = findViewById(R.id.rb_Rel_Billboardsystem_record_dev);
        rb_lotno = findViewById(R.id.rb_Rel_Billboardsystem_record_lotno);
        rb_config = findViewById(R.id.rb_Rel_Billboardsystem_record_config);

        ll_time = findViewById(R.id.ll_Rel_Billboardsystem_record_selecttime);
        ll_dev = findViewById(R.id.ll_Rel_Billboardsystem_record_selectdev);
        ll_lotno = findViewById(R.id.ll_Rel_Billboardsystem_record_selectlotno);
        ll_config = findViewById(R.id.ll_Rel_Billboardsystem_record_selectconfig);

        et_starttime = findViewById(R.id.et_Rel_Billboardsystem_record_starttime);
        et_endtime = findViewById(R.id.et_Rel_Billboardsystem_record_endtime);
        et_dev = findViewById(R.id.et_Rel_Billboardsystem_record_dev);
        et_lotno = findViewById(R.id.et_Rel_Billboardsystem_record_lotno);
        et_config = findViewById(R.id.et_Rel_Billboardsystem_record_config);

        lv1=findViewById(R.id.rc_Rel_Billboardsystem_record_rc1);
        listdata=new ArrayList<>();

        et_starttime.setOnClickListener(this);
        et_endtime.setOnClickListener(this);

        rg1.setOnCheckedChangeListener(this);
    }

    public void onclick_bt_Rel_Billboardsystem_record_search(View view) {
        if (flag_select == 0) {
            String starttime = et_starttime.getText().toString();
            String endtime = et_endtime.getText().toString();
            if (starttime == null || starttime == "" || endtime == null || endtime == "") {
                msg_dialog = "请选择开始时间和结束时间";
                DialogShowUtil.dialogShow(Rel_Billboardsystem_Record_Activity.this, msg_dialog);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                Date date_starttime = sdf.parse(starttime);
                Date date_endtime = sdf.parse(endtime);
                if (date_endtime.compareTo(date_starttime) == -1) {
                    msg_dialog = "结束时间不能大于开始时间";
                    DialogShowUtil.dialogShow(Rel_Billboardsystem_Record_Activity.this, msg_dialog);
                    return;
                }
                getRaTestLotnoInfoByTime(starttime, endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (flag_select == 1) {
            String dev = et_dev.getText().toString();
            if (dev == null || dev == "") {
                msg_dialog = "请选择机种";
                DialogShowUtil.dialogShow(Rel_Billboardsystem_Record_Activity.this, msg_dialog);
                return;
            }
            getRaTestLotnoInfoByDev(dev);
        }
        if (flag_select == 2) {
            String lotno = et_lotno.getText().toString();
            if (lotno == null || lotno == "") {
                msg_dialog = "请扫描批号";
                DialogShowUtil.dialogShow(Rel_Billboardsystem_Record_Activity.this, msg_dialog);
                return;
            }
            getRaTestLotnoInfoByLotno(lotno);
        }
        if (flag_select == 3) {
            String config = et_config.getText().toString();
            if (config == null || config == "") {
                msg_dialog = "请输入config";
                DialogShowUtil.dialogShow(Rel_Billboardsystem_Record_Activity.this, msg_dialog);
                return;
            }
            getRaTestLotnoInfoByConfig(config);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<String[]> listdata0 = (List<String[]>) msg.obj;

                    if (listdata == null) {
                        msg_dialog = "SOAP调用出错";
                        DialogShowUtil.dialogShow(Rel_Billboardsystem_Record_Activity.this, msg_dialog);
                        return;
                    }
                    listdata=listdata0;
                    myAdpter=new MyAdpter(Rel_Billboardsystem_Record_Activity.this,listdata);
                    lv1.setAdapter(myAdpter);
                    break;
                default:
                    break;
            }
        }
    };

    private void getRaTestLotnoInfoByConfig(String config) {
        final String methodname = "getRaTestLotnoInfoByConfig";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("config", config));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String[]> result3 = asop.getRaTestLotnoInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result3;
                handler.sendMessage(msg);
            }
        });
    }

    private void getRaTestLotnoInfoByLotno(String lotno) {
        final String methodname = "getRaTestLotnoInfoByLotno";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("lotno", lotno));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String[]> result2 = asop.getRaTestLotnoInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result2;
                handler.sendMessage(msg);
            }
        });
    }

    private void getRaTestLotnoInfoByDev(String dev) {
        final String methodname = "getRaTestLotnoInfoByDev";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("dev", dev));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String[]> result1 = asop.getRaTestLotnoInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result1;
                handler.sendMessage(msg);
            }
        });
    }

    private void getRaTestLotnoInfoByTime(String starttime, String endtime) {
        final String methodname = "getRaTestLotnoInfoByTime";
        final List<SOAPParameter> parameterList = new ArrayList<>();
        parameterList.add(new SOAPParameter("starttime", starttime));
        parameterList.add(new SOAPParameter("endtime", endtime));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                List<String[]> result0 = asop.getRaTestLotnoInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result0;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_Rel_Billboardsystem_record_time:
                ll_time.setVisibility(View.VISIBLE);
                ll_dev.setVisibility(View.GONE);
                ll_lotno.setVisibility(View.GONE);
                ll_config.setVisibility(View.GONE);
                flag_select = 0;
                break;
            case R.id.rb_Rel_Billboardsystem_record_dev:
                ll_time.setVisibility(View.GONE);
                ll_dev.setVisibility(View.VISIBLE);
                ll_lotno.setVisibility(View.GONE);
                ll_config.setVisibility(View.GONE);
                flag_select = 1;
                break;
            case R.id.rb_Rel_Billboardsystem_record_lotno:
                ll_time.setVisibility(View.GONE);
                ll_dev.setVisibility(View.GONE);
                ll_lotno.setVisibility(View.VISIBLE);
                ll_config.setVisibility(View.GONE);
                flag_select = 2;
                break;
            case R.id.rb_Rel_Billboardsystem_record_config:
                ll_time.setVisibility(View.GONE);
                ll_dev.setVisibility(View.GONE);
                ll_lotno.setVisibility(View.GONE);
                ll_config.setVisibility(View.VISIBLE);
                flag_select = 3;
                break;
            default:
                break;

        }
    }

    private int mYear, mMonth, mDate, mHour, mMin;
    private String myYear, myMonth, myDay, myHour, myMin;

    @Override
    public void onClick(final View v) {
        Calendar mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);

        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMin = mCalendar.get(Calendar.MINUTE);

        new DatePickerDialog(Rel_Billboardsystem_Record_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year
                    , int monthOfYear, int dayOfMonth) {
                myYear = "" + year;
                if (monthOfYear < 9) {
                    myMonth = "0" + (monthOfYear + 1);
                } else {
                    myMonth = "" + (monthOfYear + 1);
                }
                if (dayOfMonth < 10) {
                    myDay = "0" + dayOfMonth;
                } else {
                    myDay = "" + dayOfMonth;
                }
                new TimePickerDialog(Rel_Billboardsystem_Record_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10) {
                            myHour = "0" + hourOfDay;
                        } else {
                            myHour = "" + hourOfDay;
                        }
                        if (minute < 10) {
                            myMin = "0" + minute;
                        } else {
                            myMin = "" + minute;
                        }
                        String str_time = myYear + "/" + myMonth + "/" + myDay + "/" + " " + myHour + ":" + myMin;
                        switch (v.getId()) {
                            case R.id.et_Rel_Billboardsystem_record_starttime:
                                et_starttime.setText(str_time);
                                break;
                            case R.id.et_Rel_Billboardsystem_record_endtime:
                                et_endtime.setText(str_time);
                                break;
                            default:
                                break;
                        }

                    }
                }, mHour, mMin, true).show();
            }
        }, mYear, mMonth, mDate).show();


    }

    class MyAdpter extends BaseAdapter {

        private Context context;
        private List<String[]> list;

        public MyAdpter(Context context,List<String[]> list){
            this.context=context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.items_rel_billboardsystem_record, null);
                viewHolder = new ViewHolder();
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv1);
                viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv2);
                viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv3);
                viewHolder.tv4 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv4);
                viewHolder.tv5 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv5);
                viewHolder.tv6 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv6);
                viewHolder.tv7 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv7);
                viewHolder.tv8 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv8);
                viewHolder.tv9 = (TextView) convertView.findViewById(R.id.tv_Rel_billboardsystem_record_tv9);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv1.setText(String.valueOf(position));
            viewHolder.tv2.setText(list.get(position)[0]);
            viewHolder.tv3.setText(list.get(position)[1]);
            viewHolder.tv4.setText(list.get(position)[2]);
            viewHolder.tv5.setText(list.get(position)[3]);
            viewHolder.tv6.setText(list.get(position)[4]);
            viewHolder.tv7.setText(list.get(position)[5]);
            viewHolder.tv8.setText(list.get(position)[6]);
            viewHolder.tv9.setText(list.get(position)[7]);
            return convertView;
        }
    }

    class ViewHolder{
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    }
}
