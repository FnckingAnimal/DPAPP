package app.dpapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.PublicSOAP;
import app.cmapp.appcdl.exechttprequest;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.appcdl.jsontolist;
import app.cmapp.parameterclass.httprequestinputdata;

/**
 * F5460007    7y85WnRE
 * Owner:S7202916
 * CreateDate:2017/7/6 13:49
 */
public class Unusual extends AppCompatActivity implements View.OnClickListener {
    private Spinner mTimeSpinner, mAppearanceSpinner, mAppearanceTypeSpinner, mDevicenoSpinner;
    private EditText mEngineerEditText;
    private Button mCommit, mCheck;
    private ArrayAdapter<String> mAdapter, mAdapterTime;
    private List<String> mTimeList;
    private String[] mTimeStr;
    //  private ScrollView mScrollView;
//  private LinearLayout mBottom;
    //填充机种ListView  adapter适配器
//    private static ArrayAdapter<String> mMachineAdapter;
//    private static ArrayAdapter<String> mTypeAdapter;
    private static ArrayAdapter<String> mUnusualAdapter, mUnusualTypeAdapter, mDevicenoAdapter;
    private String mStr;
    private TextView mTvDate;
    private Button mBtDate;
    private int mYear, mMonth, mDate;
    //    private EditText mStartTime,mEndTime;
    private Calendar mCalendar;
    private Date mDateStart, mDateEnd;
    private String mUserId;
    private TextView mMachineId;
    private ListView mListView;
    private List<UnusualBean> mUnusualList;
    private List<String> mUnusualList2;
    private UnusualDdapter mUnusualDdapter;
    private EditText text1, text2, text3, text4, text5, text6, text7;


    private SimpleDateFormat mFormat;
    private String sysid, flowid;
    private List<String> unusualList = new ArrayList<>();
    private List<String> unusualTypeList = new ArrayList<>();
    private PopupWindow popupWindow;
    private LinearLayout mLinearLayout;
    private TextView mDeviceno;

    private LinearLayout mOtherUnusual;
    private EditText mOtherPut;
    private Spinner mTypeSpinner;
    private TextView mTv_startTime, mTv_endTime, text8, text9;
    private String person;
    private List<String> devicenoList = new ArrayList<>();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.unusual_layout);
            mTv_startTime = (TextView) findViewById(R.id.tv_unusual_starttime);
            mTv_endTime = (TextView) findViewById(R.id.tv_unusual_endtime);
            mStr = getIntent().getStringExtra("lotno");
            mDeviceno = (TextView) findViewById(R.id.tv_unusual_deviceno);
            mDevicenoSpinner = (Spinner) findViewById(R.id.sp_unusual_deviceno);
            mTimeSpinner = (Spinner) findViewById(R.id.sp_unusual_time);
            mAppearanceSpinner = (Spinner) findViewById(R.id.sp_unusual_appearance);
            mAppearanceTypeSpinner = (Spinner) findViewById(R.id.sp_unusual_appearance_type);
            mEngineerEditText = (EditText) findViewById(R.id.et_unusual_put);
            mCommit = (Button) findViewById(R.id.bt_unusual_commit);
            mCheck = (Button) findViewById(R.id.bt_unusual_check);
//            mStartTime =  findViewById(R.id.et_unusual_starttime);
//            mEndTime =  findViewById(R.id.et_unusual_endtime);
            mMachineId = (TextView) findViewById(R.id.tv_unusual_machineid);
            mListView = (ListView) findViewById(R.id.lv_unusual);
            mLinearLayout = (LinearLayout) findViewById(R.id.ll_function_below);
            mOtherUnusual = (LinearLayout) findViewById(R.id.ll_unusual_other);
            mTypeSpinner = (Spinner) findViewById(R.id.sp_unusual_type);
            mOtherPut = (EditText) findViewById(R.id.et_unusual_other);
            mMachineId.setText(mStr);
            mBtDate = (Button) findViewById(R.id.bt_unusual_date);
            mTvDate = (TextView) findViewById(R.id.tv_unusual_date);
            mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
//            mTvDate.setOnClickListener(this);
            Staticdata sc = (Staticdata) getApplication();
            mUserId = sc.getLoginUserID();
//            site = sc.usersite;
            mDevicenoSpinner.setVisibility(View.GONE);
            mDeviceno.setVisibility(View.VISIBLE);
            MyThreadPool.pool.execute(new Runnable() {
                @Override
                public void run() {
                    PublicSOAP ps1 = new PublicSOAP();
                    List<String> list = new ArrayList<>();
                    list = ps1.getRemoteInfo_geteqcheckdevicenolh("");
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = list;
                    mHandler.sendMessage(msg);
                }
            });
            MyThreadPool.pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            String date = getSystemTime();
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = date;
                            mHandler.sendMessage(msg);
                        }
                    }
            );
            mUnusualList = new ArrayList<>();
            mUnusualList2 = new ArrayList<>();
            mUnusualDdapter = new UnusualDdapter(this, mUnusualList);
//            mStartTime.setOnClickListener(this);
//            mEndTime.setOnClickListener(this);
            mOtherPut.setOnClickListener(this);
            mAppearanceSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mAppearanceTypeSpinner.setSelection(position);
                }
            });
//            mListView.setAdapter(mUnusualDdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setType();
        mCalendar = Calendar.getInstance();
        mCommit.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        setTime();
        int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mTimeSpinner.setSelection(mHour);

        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] hours = mTimeSpinner.getSelectedItem().toString().split("-");
                String[] minutes = hours[0].split(":");
                setTimeNew(mTv_startTime, "設置開始時間", Integer.parseInt(minutes[0]), Integer.parseInt(minutes[1]));
                setTimeNew(mTv_endTime, "設置結束時間", Integer.parseInt(minutes[0]), Integer.parseInt(minutes[1]));
                mTv_startTime.setText(hours[0]);
                mTv_endTime.setText(hours[0]);
                mUnusualList.clear();
//                checkOutput1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUnusualList.clear();
                mUnusualAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getMachine(mStr);

//        setTimeMin();
//        MyThreadPool.pool.execute(new Runnable() {
//            @Override
//            public void run() {
//                PublicSOAP ps1 = new PublicSOAP();
//                List<String> list = new ArrayList<String>();
//                list = ps1.getRemoteInfo_geteqcheckdevicenolh();
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = list;
//                mHandler.sendMessage(msg);
//            }
//        });
        getUnusual();
        String[] hours = mTimeSpinner.getSelectedItem().toString().split("-");
        String[] minutes = hours[0].split(":");
        setTimeNew(mTv_startTime, "設置開始時間", Integer.parseInt(minutes[0]), Integer.parseInt(minutes[1]));
        setTimeNew(mTv_endTime, "設置結束時間", Integer.parseInt(minutes[0]), Integer.parseInt(minutes[1]));
//        checkOutput1();

    }

    public void setTimeNew(final TextView textView, final String title, final Integer hour, final Integer minute) {
        //点击"时间"按钮布局 设置时间
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(Unusual.this);
                View view = getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                //初始化时间
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                //设置time布局
                builder.setView(view);
                builder.setTitle(title);
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer mHour = timePicker.getCurrentHour();
                        Integer mMinute = timePicker.getCurrentMinute();
                        //时间小于10的数字 前面补0 如01:12:00
                        textView.setText(new StringBuilder().append(mHour < 10 ? "0" + mHour : mHour).append(":")
                                .append(mMinute < 10 ? "0" + mMinute : mMinute));
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }


    public void showPopupWindow(Context context, View parent, List<UnusualBean> list, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.popupwindow_layout, null);
        text1 = (EditText) view.findViewById(R.id.et_unusual_machineid);
        text1.setText(list.get(position).getMACHINENOID());
        text1.setEnabled(false);
        text2 = (EditText) view.findViewById(R.id.et_popupwondow_deviceno);
        text2.setText(list.get(position).getDEVICENO());
        text2.setEnabled(false);
        text3 = (EditText) view.findViewById(R.id.et_popupwindow_time);
        text3.setText(list.get(position).getTIMESLOT());
        text3.setEnabled(false);
        text4 = (EditText) view.findViewById(R.id.et_popupwindow_unusual);
        text4.setText(list.get(position).getDEFECTNO());
        text4.setEnabled(false);
        text5 = (EditText) view.findViewById(R.id.et_upopupwindow_engineer);
        text5.setText(list.get(position).getENGINNER());
        text5.setEnabled(false);
        text6 = (EditText) view.findViewById(R.id.et_popupwindow_createid);
        text6.setText(list.get(position).getCREATEID());
        text6.setEnabled(false);
        text7 = (EditText) view.findViewById(R.id.et_popupwindow_createtime);
        text7.setText(list.get(position).getCREATETIME());
        text7.setEnabled(false);
        text8 = (TextView) view.findViewById(R.id.tv_popupwindow_starttime);
        text8.setText(list.get(position).getSTARTTIME());
        String[] startTime = list.get(position).getSTARTTIME().split(":");
        setTimeNew(text8, "設置開始時間!", Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
        text9 = (TextView) view.findViewById(R.id.tv_popupwindow_endtime);
        text9.setText(list.get(position).getENDTIME());
        String[] endTime = list.get(position).getENDTIME().split(":");
        setTimeNew(text9, "設置結束時間!", Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
        Button button = (Button) view.findViewById(R.id.bt_unusual_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveStr1 = "[";
                saveStr1 += "{\"machinenoid\":\"" + text1.getText()
                        + "\",\"deviceno\":\"" + text2.getText()
                        + "\",\"date\":\"" + mTvDate.getText()
                        + "\",\"timeslot\":\"" + text3.getText()
                        + "\",\"defectno\":\"" + text4.getText()
                        + "\",\"starttime\":\"" + text8.getText()
                        + "\",\"endtime\":\"" + text9.getText()
                        + "\",\"enginner\":\"" + text5.getText()
                        + "\",\"createid\":\"" + text6.getText()
                        + "\",\"sysid\":\"" + sysid
                        + "\",\"flowid\":\"" + flowid
                        + "\"},";
                saveStr1 = saveStr1.substring(0, saveStr1.length() - 1);
                saveStr1 += "]";
                change(saveStr1);
            }
        });

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(mLinearLayout, Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 修改
     */

    public void change1() {
        String url1 = Staticdata.jchttpurl1;
        List<httprequestinputdata> li = new ArrayList<>();
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(1, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        if (paramObject != null && !"".equals(paramObject)) {
                            Toast.makeText(Unusual.this, "請求數據為" + paramObject.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Unusual.this, "數據為空", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, li, null);

    }

    public void change(String str) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("updatemachine_defect");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("jsonstr");
        hhi2.setDatavalue(str);
        li.add(hhi2);
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        if (paramObject != null && !"".equals(paramObject)) {
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                JSONArray array = object.getJSONArray("Table1");
                                if (array != null && !"".equals(array)) {
                                    JSONObject jsonObject = array.getJSONObject(0);
                                    if ("1".equals(jsonObject.getString("item1"))) {
                                        Toast.makeText(Unusual.this, "修改成功!", Toast.LENGTH_SHORT).show();
                                        execloadactivity.canclediglog();
                                        mUnusualList.clear();
                                        mUnusualDdapter.notifyDataSetChanged();
                                        mListView.invalidate();
                                        checkOutput();
                                        popupWindow.dismiss();
                                    } else if ("0".equals(jsonObject.getString("item1"))) {
                                        Toast.makeText(Unusual.this, jsonObject.getString("item2"), Toast.LENGTH_SHORT).show();
                                        execloadactivity.canclediglog();
                                        popupWindow.dismiss();
                                    }

                                } else {
                                    Toast.makeText(Unusual.this, "修改失敗!", Toast.LENGTH_SHORT).show();
                                    execloadactivity.canclediglog();
                                    popupWindow.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(Unusual.this, "修改失敗!", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                            popupWindow.dismiss();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, li, null);
    }

    public void removeItem(int position) {
        mUnusualList.remove(position);
        mUnusualDdapter.notifyDataSetChanged();
        mListView.invalidate();
    }

    /**
     * 刪除
     */
    public void cancle(String str, final int position) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("deletemachine_defect");
        li.add(hhi1);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(str);
        li.add(httpTwo);

        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(1, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        if (paramObject != null && !"".equals(paramObject)) {
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                JSONArray array = object.getJSONArray("Table1");
                                JSONObject jsonObject = array.getJSONObject(0);
                                if ("1".equals(jsonObject.getString("item1"))) {
                                    Toast.makeText(Unusual.this, "刪除成功!", Toast.LENGTH_SHORT).show();
                                    removeItem(position);
                                    execloadactivity.canclediglog();
                                } else {
                                    Toast.makeText(Unusual.this, "刪除失敗!", Toast.LENGTH_SHORT).show();
                                    execloadactivity.canclediglog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(Unusual.this, "刪除失敗!", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, li, null);

    }

    public void getMachine(String str) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getdeviceno_machinenoid");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("machinenoid");
        hhi2.setDatavalue(str);
        li.add(hhi2);
        getDeviceno(url1, li, 2);

    }

    public void getDeviceno(String urlpath, List<httprequestinputdata> list, int sendmode) {
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(sendmode, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {

                        if (paramObject != null && !"".equals(paramObject)) {
                            DevicenoBean bean = null;
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                if (object != null && !"".equals(object)) {
                                    JSONArray array = object.getJSONArray("Table1");
                                    if (array != null && !"".equals(array)) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            bean = new DevicenoBean();
                                            bean.setDeviceno(jsonObject.getString("deviceno"));
                                        }
                                        if (bean.getDeviceno() == null || "".equals(bean.getDeviceno())) {
                                            Toast.makeText(Unusual.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mDeviceno.setText(bean.getDeviceno());
                                        }

                                    } else {
                                        Toast.makeText(Unusual.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Unusual.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {

                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, list, null);

    }

    public void setType() {
        List<String> mList = new ArrayList<>();
        mList.add("計劃性停機");
        mList.add("異常停機");
        mList.add("待料");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.myspinner, mList);
        mTypeSpinner.setAdapter(adapter);
    }

    public void setTime() {
        mTimeList = new ArrayList<>();
        mTimeStr = new String[]{
                "00:00-01:00", "01:00-02:00", "02:00-03:00", "03:00-04:00", "04:00-05:00", "05:00-06:00",
                "06:00-07:00", "07:00-08:00", "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
                "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00",
                "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00", "23:00-00:00",
        };

        for (int i = 0; i < mTimeStr.length; i++) {
            mTimeList.add(mTimeStr[i]);
        }
        mAdapterTime = new ArrayAdapter<>(this, R.layout.myspinner, mTimeList);
        mTimeSpinner.setAdapter(mAdapterTime);
        String[] str = mTimeSpinner.getSelectedItem().toString().split("-");
        mTv_startTime.setText(str[0]);
        mTv_endTime.setText(str[0]);
//      change1();

        try {
            mFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//          mCurDate = new Date(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_unusual_commit:
                execloadactivity.opendialog(this, "正在執行");

                if (TextUtils.isEmpty(mEngineerEditText.getText().toString())) {
                    person = "admin";
                } else {
                    person = mEngineerEditText.getText().toString();
                }
//                if(mStartTime.getText().toString() == null && "".equals(mStartTime.getText().toString())) {
//                    Toast.makeText(this,"請輸入開始時間",Toast.LENGTH_SHORT).show();
//                    execloadactivity.canclediglog();
//                    return;
//                }
//                if(mEndTime.getText().toString() == null && "".equals(mEndTime.getText().toString())) {
//                    Toast.makeText(this,"請輸入結束時間",Toast.LENGTH_SHORT).show();
//                    execloadactivity.canclediglog();
//                    return;
//                }
                String[] startTime = mTv_startTime.getText().toString().split(":");
                String[] endTime = mTv_endTime.getText().toString().split(":");
                String[] hours = mTimeSpinner.getSelectedItem().toString().split("-");
                String[] minutes = hours[0].split(":");
                if (!hours[1].equals(mTv_endTime.getText().toString())) {
                    if (!startTime[0].equals(minutes[0]) || !endTime[0].equals(minutes[0])) {
                        Toast.makeText(this, "開始結束時間應在時間段內，請檢查!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (!startTime[0].equals(minutes[0])) {
                        Toast.makeText(this, "開始結束時間應在時間段內，請檢查!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if ((Integer.parseInt(startTime[0]) * 3600 + Integer.parseInt(startTime[1]) * 60) >= (Integer.parseInt(endTime[0]) * 3600 + Integer.parseInt(endTime[1]) * 60)) {
                    Toast.makeText(this, "結束時間不能小於開始時間!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] str = mTimeSpinner.getSelectedItem().toString().split("-");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                try {
                    mDateStart = dateFormat.parse(mTv_startTime.getText().toString());
                    mDateEnd = dateFormat.parse(mTv_endTime.getText().toString());
                    if (mDateStart.getTime() > mDateEnd.getTime()) {
                        execloadactivity.canclediglog();
                        return;
                    }
                    if (mDateStart.getTime() < dateFormat.parse(str[0]).getTime() || mDateEnd.getTime() > dateFormat.parse(str[1]).getTime()) {
                        Toast.makeText(this, "請輸入選擇時間段內的時間", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (unusualList.size() == 0) {
                    Toast.makeText(Unusual.this, "異常不能為空!", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if (mUnusualList2 != null && mUnusualList2.size() > 0) {
                    mUnusualList2.clear();
                }

                MyThreadPool.pool.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                String string = mDeviceno.getText().toString();
                                List<String> list = getUnusualEndTime(mStr, string, mTvDate.getText().toString(), mTimeSpinner.getSelectedItem().toString());
                                Message msg = mHandler.obtainMessage();
                                msg.what = 2;
                                msg.obj = list;
                                mHandler.sendMessage(msg);
                            }
                        }
                );

                break;
            case R.id.tv_unusual_date:
                new DatePickerDialog(Unusual.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mTvDate.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, mYear, mMonth, mDate).show();
                mUnusualList.clear();
//                checkOutput1();
                break;
            case R.id.bt_unusual_check:
                mUnusualList.clear();
                mUnusualDdapter.notifyDataSetChanged();
                mListView.invalidate();
                checkOutput();
                break;
//            case R.id.et_unusual_endtime:
//              Toast.makeText(Unusual.this,"et_unusual_endtime",Toast.LENGTH_SHORT).show();
//                mUnusualList.clear();
//              checkOutput1();
//                break;
//            case R.id.et_unusual_starttime:
//              Toast.makeText(Unusual.this,"et_unusual_starttime",Toast.LENGTH_SHORT).show();
//                mUnusualList.clear();
//              checkOutput1();
//                break;
            case R.id.et_unusual_other:
//              Toast.makeText(Unusual.this,"et_unusual_other",Toast.LENGTH_SHORT).show();
                mUnusualList.clear();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void checkOutput() {
        String saveStr = "[";
        saveStr += "{\"machinenoid\":\"" + mStr
                + "\",\"deviceno\":\"" + mDeviceno.getText()
                + "\",\"date\":\"" + mTvDate.getText()
                + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                + "\"},";

        saveStr = saveStr.substring(0, saveStr.length() - 1);
        saveStr += "]";

        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("getmachine_defect_timeslot");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(saveStr);
        list.add(httpTwo);
//http://10.142.136.222:8107/machineoutput/machineoutput.ashx?directpar=getmachine_output_timeslot&jsonstr=[{"machinenoid":"M1610D009-013","deviceno":"APG002","date":"2017-9-28","timeslot":"08:00-09:00"}]

        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(1, new FreedomDataCallBack() {
            @Override
            public void onStart(Context C1) {

            }

            @Override
            public void processData(Object paramObject, boolean param) {
                if (paramObject != null && !"".equals(paramObject)) {
//                    Toast.makeText(Unusual.this,paramObject.toString(),Toast.LENGTH_LONG).show();
                    UnusualBean bean = null;
                    try {
                        JSONObject object = new JSONObject(paramObject.toString());
                        if (object != null && !"".equals(object)) {
                            JSONArray array = object.getJSONArray("Table1");

                            if (array != null && !"".equals(array)) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    if (jsonObject != null && !"".equals(jsonObject)) {
                                        bean = new UnusualBean();
                                        bean.setCREATEID(jsonObject.getString("CREATEID"));
                                        bean.setCREATETIME(jsonObject.getString("CREATETIME"));
                                        bean.setMACHINENOID(jsonObject.getString("MACHINENOID"));
                                        bean.setTIMESLOT(jsonObject.getString("TIMESLOT"));
                                        bean.setDATES(jsonObject.getString("DATES"));
                                        bean.setMODEL(jsonObject.getString("MODEL"));
                                        bean.setDEVICENO(jsonObject.getString("DEVICENO"));
                                        bean.setDEFECTNO(jsonObject.getString("DEFECTNO"));
                                        bean.setSTARTTIME(jsonObject.getString("STARTTIME"));
                                        bean.setENGINNER(jsonObject.getString("ENGINNER"));
                                        bean.setENDTIME(jsonObject.getString("ENDTIME"));
                                        bean.setFLOWID(jsonObject.getString("FLOWID"));
                                        bean.setSYSID(jsonObject.getString("SYSID"));
                                        bean.setATT1(jsonObject.getString("ATT1"));
                                    }
                                    mUnusualList.add(bean);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mUnusualList.size() == 0) {
                        Toast.makeText(Unusual.this, "查詢為空!", Toast.LENGTH_SHORT).show();
                    } else {
                        mUnusualDdapter = new UnusualDdapter(Unusual.this, mUnusualList);
                        mListView.setAdapter(mUnusualDdapter);
                    }

                } else {
                    Toast.makeText(Unusual.this, "查詢為空!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish(Context C1) {
                execloadactivity.canclediglog();
            }

            @Override
            public void onFailed(Object paramObject, Context C1) {
                Toast.makeText(Unusual.this
                        , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }

            @Override
            public void onNoneImage(Context C1) {
                Toast.makeText(Unusual.this
                        , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                execloadactivity.canclediglog();
            }
        }, this, url1, list, null);
    }


    public void getUnusual() {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("getmachinedefect");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("machinenoid");
        httpTwo.setDatavalue(mStr);
        list.add(httpTwo);
        getUnusualInfo(url1, list, 2);
    }

    public void getUnusualInfo(String urlpath, List<httprequestinputdata> list, int sendmode) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(sendmode, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        jsontolist js = new jsontolist();

                        if (paramObject != null && !"".equals(paramObject)) {
                            jsontolist._jsonarray _jsonarray = js.datasetjasontolist(paramObject.toString());
                            if (_jsonarray.get_valuelist() != null && !"".equals(_jsonarray.get_valuelist())) {
                                for (int i = 0; i < _jsonarray.get_valuelist().size(); i++) {
                                    unusualList.add(_jsonarray.get_valuelist().get(i).get(1).get_value());
                                    unusualTypeList.add(_jsonarray.get_valuelist().get(i).get(2).get_value());
                                }
                                mUnusualAdapter = new ArrayAdapter<>(Unusual.this, R.layout.myspinner, unusualList);
                                mAppearanceSpinner.setAdapter(mUnusualAdapter);
                                mUnusualTypeAdapter = new ArrayAdapter<>(Unusual.this, R.layout.myspinner, unusualTypeList);
                                mAppearanceTypeSpinner.setAdapter(mUnusualTypeAdapter);
                                mAppearanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mAppearanceTypeSpinner.setSelection(position);
                                        if ("其它".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                            mOtherUnusual.setVisibility(View.VISIBLE);
                                        } else {
                                            mOtherUnusual.setVisibility(View.GONE);
                                        }
                                        mUnusualList.clear();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else {
                                Toast.makeText(Unusual.this, "異常為空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Unusual.this, "異常為空", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, list, null);
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    new Thread(new MyThread()).start();
                    mUnusualList.clear();
                    mUnusualDdapter.notifyDataSetChanged();
                    checkOutput();
                    break;
                case 1:
                    mCommit.setEnabled(true);
                    break;
                case 2:
                    mUnusualList2 = (List<String>) msg.obj;
                    if (mUnusualList2.size() > 0) {
                        String eTime = mUnusualList2.get(mUnusualList2.size() - 1);
                        String sTime = mTv_startTime.getText().toString();
                        String[] eTimeStrings = eTime.split(":");
                        String[] sTimeStrings = sTime.split(":");
                        if (eTimeStrings[0].equals(sTimeStrings[0]) && Integer.parseInt(sTimeStrings[1]) < Integer.parseInt(eTimeStrings[1])) {
                            Toast.makeText(Unusual.this, "這個時間段已經存在異常記錄，不能重複提交！", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                            return;
                        } else if (!eTimeStrings[0].equals(sTimeStrings[0])) {
                            Toast.makeText(Unusual.this, "這個時間段已經存在異常記錄，不能重複提交！", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                            return;
                        } else {
                            if ("其它".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                mOtherUnusual.setVisibility(View.VISIBLE);
                                if (TextUtils.isEmpty(mOtherPut.getText().toString())) {
                                    Toast.makeText(Unusual.this, "請輸入其它異常", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String defectno;
                                if ("數據驗證中".equals(mOtherPut.getText().toString())) {
                                    defectno = "one";
                                } else {
                                    defectno = mOtherPut.getText() + "";
                                }
                                String saveStr = "[";
                                saveStr += "{\"machinenoid\":\"" + mStr
                                        + "\",\"deviceno\":\"" + mDeviceno.getText()
                                        + "\",\"date\":\"" + mTvDate.getText()
                                        + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                        + "\",\"defectno\":\"" + "其它"
                                        + "\",\"starttime\":\"" + mTv_startTime.getText().toString()
                                        + "\",\"endtime\":\"" + mTv_endTime.getText().toString()
                                        + "\",\"enginner\":\"" + person
                                        + "\",\"createid\":\"" + mUserId
                                        + "\",\"defectno_other\":\"" + defectno
                                        + "\",\"att2\":\"" + mAppearanceTypeSpinner.getSelectedItem().toString()
                                        + "\"},";

                                saveStr = saveStr.substring(0, saveStr.length() - 1);
                                saveStr += "]";
                                commit(saveStr);
                            } else {
                                String str3;
                                if ("二維碼掃描異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                    str3 = "one";
                                } else if ("PRI出料口senser異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                    str3 = "two";
                                } else if ("PRI進料口senser異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                    str3 = "three";
                                } else if ("收扳機抓彈匣異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                    str3 = "four";
                                } else if ("FC mapping圖讀取異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                    str3 = "five";
                                } else if ("清潔機台，良率超標".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                    str3 = "six";
                                } else {
                                    str3 = mAppearanceSpinner.getSelectedItem().toString();
                                }
                                String saveStr = "[";
                                saveStr += "{\"machinenoid\":\"" + mStr
                                        + "\",\"deviceno\":\"" + mDeviceno.getText()
                                        + "\",\"date\":\"" + mTvDate.getText()
                                        + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                        + "\",\"defectno\":\"" + str3
                                        + "\",\"starttime\":\"" + mTv_startTime.getText().toString()
                                        + "\",\"endtime\":\"" + mTv_endTime.getText().toString()
                                        + "\",\"enginner\":\"" + person
                                        + "\",\"createid\":\"" + mUserId
                                        + "\",\"defectno_other\":\"" + ""
                                        + "\",\"att2\":\"" + mAppearanceTypeSpinner.getSelectedItem().toString()
                                        + "\"},";

                                saveStr = saveStr.substring(0, saveStr.length() - 1);
                                saveStr += "]";
                                commit(saveStr);

                            }
                        }
                    } else {
                        if ("其它".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                            mOtherUnusual.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(mOtherPut.getText().toString())) {
                                Toast.makeText(Unusual.this, "請輸入其它異常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String defectno;
                            if ("數據驗證中".equals(mOtherPut.getText().toString())) {
                                defectno = "one";
                            } else {
                                defectno = mOtherPut.getText() + "";
                            }
                            String saveStr = "[";
                            saveStr += "{\"machinenoid\":\"" + mStr
                                    + "\",\"deviceno\":\"" + mDeviceno.getText()
                                    + "\",\"date\":\"" + mTvDate.getText()
                                    + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                    + "\",\"defectno\":\"" + "其它"
                                    + "\",\"starttime\":\"" + mTv_startTime.getText().toString()
                                    + "\",\"endtime\":\"" + mTv_endTime.getText().toString()
                                    + "\",\"enginner\":\"" + person
                                    + "\",\"createid\":\"" + mUserId
                                    + "\",\"defectno_other\":\"" + defectno
                                    + "\",\"att2\":\"" + mAppearanceTypeSpinner.getSelectedItem().toString()
                                    + "\"},";

                            saveStr = saveStr.substring(0, saveStr.length() - 1);
                            saveStr += "]";
                            commit(saveStr);
                        } else {
                            String str3;
                            if ("二維碼掃描異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                str3 = "one";
                            } else if ("PRI出料口senser異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                str3 = "two";
                            } else if ("PRI進料口senser異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                str3 = "three";
                            } else if ("收扳機抓彈匣異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                str3 = "four";
                            } else if ("FC mapping圖讀取異常".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                str3 = "five";
                            } else if ("清潔機台，良率超標".equals(mAppearanceSpinner.getSelectedItem().toString())) {
                                str3 = "six";
                            } else {
                                str3 = mAppearanceSpinner.getSelectedItem().toString();
                            }
                            String saveStr = "[";
                            saveStr += "{\"machinenoid\":\"" + mStr
                                    + "\",\"deviceno\":\"" + mDeviceno.getText()
                                    + "\",\"date\":\"" + mTvDate.getText()
                                    + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                    + "\",\"defectno\":\"" + str3
                                    + "\",\"starttime\":\"" + mTv_startTime.getText().toString()
                                    + "\",\"endtime\":\"" + mTv_endTime.getText().toString()
                                    + "\",\"enginner\":\"" + person
                                    + "\",\"createid\":\"" + mUserId
                                    + "\",\"defectno_other\":\"" + ""
                                    + "\",\"att2\":\"" + mAppearanceTypeSpinner.getSelectedItem().toString()
                                    + "\"},";

                            saveStr = saveStr.substring(0, saveStr.length() - 1);
                            saveStr += "]";
                            commit(saveStr);

                        }
                    }
                    break;
                case 3:
                    List<String> list = (List<String>) msg.obj;
                    if (list != null && list.size() > 0) {
                        list.remove(0);
                        devicenoList = list;
                        mDevicenoAdapter = new ArrayAdapter<>(Unusual.this, R.layout.myspinner, devicenoList);
                        mDevicenoSpinner.setAdapter(mDevicenoAdapter);
                    }
                    break;
                case 4:
                    String date = (String) msg.obj;
                    if(!TextUtils.isEmpty(date)) {
                        //2023-03-04
                        String[] dates = date.split("-");
                        mYear = Integer.parseInt(dates[0]);
                        mMonth = Integer.parseInt(dates[1]);
                        mDate = Integer.parseInt(dates[2]);
                        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth, mDate));
//                        mYear = mCalendar.get(Calendar.YEAR);
//                        mMonth = mCalendar.get(Calendar.MONTH);
//                        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);
//                        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
                    }
                    break;
            }
        }
    };

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(10000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // 获取服务器或数据库日期

    public  String getSystemTime() {

        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "getSystemTime";
            String SOAP_ACTION = "http://tempuri.org/getSystemTime";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("time", "");
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
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗
            try {
                returnstr = r1.getProperty(0).toString(); //1：已報修，2：已啟修，3：已完修，4：已驗收（正常）

            } catch (Exception ex1) {
                returnstr = "-1";
            }

            return returnstr;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    public void commit(String str) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("submitmachine_defect");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(str);
        list.add(httpTwo);

        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
//                        Toast.makeText(Unusual.this,paramObject.toString(),Toast.LENGTH_SHORT).show();
                        if (paramObject != null && !"".equals(paramObject)) {
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                JSONArray array = object.getJSONArray("Table1");
                                JSONObject jsonObject = array.getJSONObject(0);
                                if ("1".equals(jsonObject.getString("item1"))) {
                                    Toast.makeText(Unusual.this, "提交成功!", Toast.LENGTH_SHORT).show();
                                    execloadactivity.canclediglog();
                                    mCommit.setEnabled(false);
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = 0;
                                    mHandler.sendMessage(msg);
                                } else {
                                    Toast.makeText(Unusual.this, "提交失敗,請檢查!", Toast.LENGTH_SHORT).show();
                                    execloadactivity.canclediglog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(Unusual.this, "提交失敗!", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                        }
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Unusual.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, list, null);
    }

    class UnusualDdapter extends BaseAdapter {
        private Context context;
        private List<UnusualBean> mList;

        public UnusualDdapter(Context context, List<UnusualBean> mList) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_unusual, null);
                holder = new ViewHolder();
                holder.mTextView1 = (TextView) convertView.findViewById(R.id.tv_listview_pihao);
                holder.mTextView2 = (TextView) convertView.findViewById(R.id.tv_listview_jitaihao);
                holder.mTextView4 = (TextView) convertView.findViewById(R.id.tv_listview_creater);
                holder.mTextView3 = (TextView) (TextView) convertView.findViewById(R.id.tv_listview_time);
                holder.mTextView5 = (TextView) convertView.findViewById(R.id.tv_listview_one);
                holder.mTextView6 = (TextView) convertView.findViewById(R.id.tv_listview_two);
                holder.mTextView7 = (TextView) convertView.findViewById(R.id.tv_listview_three);
                holder.mTextView8 = (TextView) convertView.findViewById(R.id.tv_listview_starttime);
                holder.mTextView9 = (TextView) convertView.findViewById(R.id.tv_listview_endtime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final View view = convertView;
            holder.mTextView1.setText(mList.get(position).getMACHINENOID());
            holder.mTextView2.setText(mList.get(position).getDEVICENO());
            holder.mTextView3.setText(mList.get(position).getTIMESLOT());
            String str;
            if ("one".equals(mList.get(position).getDEFECTNO())) {
                str = "二維碼掃描異常";
            } else if ("two".equals(mList.get(position).getDEFECTNO())) {
                str = "PRI出料口senser異常";
            } else if ("three".equals(mList.get(position).getDEFECTNO())) {
                str = "PRI進料口senser異常";
            } else if ("four".equals(mList.get(position).getDEFECTNO())) {
                str = "收扳機抓彈匣異常";
            } else if ("five".equals(mList.get(position).getDEFECTNO())) {
                str = "FC mapping圖讀取異常";
            } else if ("six".equals(mList.get(position).getDEFECTNO())) {
                str = "清潔機台，良率超標";
            } else if ("其它".equals(mList.get(position).getDEFECTNO())) {
                if ("one".equals(mList.get(position).getATT1())) {
                    str = "其它:數據驗證中";
                } else {
                    str = "其它(" + mList.get(position).getATT1() + ")";
                }
            } else {
                str = mList.get(position).getDEFECTNO();
            }
            holder.mTextView4.setText(str);
            if ("admin".equals(mList.get(position).getENGINNER())) {
                holder.mTextView5.setText("");
            } else {
                holder.mTextView5.setText(mList.get(position).getENGINNER());
            }
            holder.mTextView6.setText(mList.get(position).getCREATEID());
            holder.mTextView7.setText(mList.get(position).getCREATETIME());
            holder.mTextView8.setText(mList.get(position).getSTARTTIME());
            holder.mTextView9.setText(mList.get(position).getENDTIME());

            sysid = mList.get(position).getSYSID();
            flowid = mList.get(position).getFLOWID();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示:").setMessage("請選擇要進 行的操作")
                            .setPositiveButton("修改", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showPopupWindow(Unusual.this, view, mList, position);

                                }
                            }).setNegativeButton("刪除", new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String saveStr = "[";
                            saveStr += "{\"flowid\":\"" + flowid
                                    + "\",\"sysid\":\"" + sysid
                                    + "\",\"createid\":\"" + mUserId
                                    + "\"},";
                            saveStr = saveStr.substring(0, saveStr.length() - 1);
                            saveStr += "]";
                            if (!mUserId.equals(mList.get(position).getCREATEID())) {
                                Toast.makeText(Unusual.this, "只能刪除本人提交的內容", Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            Toast.makeText(Unusual.this,saveStr,Toast.LENGTH_SHORT).show();
                            cancle(saveStr, position);
                        }
                    });
                    builder.create().show();
                }
            });


            return convertView;
        }
    }


    class ViewHolder {

        TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6, mTextView7, mTextView8, mTextView9;
    }

    /**
     * 獲取單位時間內的異常
     */
    public List<String> getUnusualEndTime(String machinenoid, String deviceno, String date, String timeslot) {
        List<String> list = new ArrayList<>();
        try {
            String NAMESPACE = "http://tempuri.org/";

            String URL = Staticdata.soapurl;

            String METHOD_NAME = "getmachine_defect";//"UploadCheckdata";
            String SOAP_ACTION = "http://tempuri.org/getmachine_defect";//"http://tempuri.org/UploadCheckdata";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinenoid", machinenoid);
            rpc.addProperty("deviceno", deviceno);
            rpc.addProperty("date", date);
            rpc.addProperty("timeslot", timeslot);

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
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;
            // 获取返回的结果
            SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
            int number = soapchild.getPropertyCount();
            for (int i = 0; i < number; i++) {
                list.add(((SoapObject) soapchild.getProperty(i)).getProperty("ENDTIME").toString());
            }
        } catch (Exception ex1) {

        }
        return list;
    }

}
