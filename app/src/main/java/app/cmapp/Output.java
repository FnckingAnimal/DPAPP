package app.cmapp;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.exechttprequest;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.appcdl.jsontolist;
import app.cmapp.parameterclass.httprequestinputdata;
import app.cmapp.zxing.activity.CaptureActivity;
import app.dpapp.R;

/**
 * Owner:S7202916
 * CreateDate:2017/7/6 13:49
 */
public class Output extends AppCompatActivity implements View.OnClickListener{
    private Spinner mTypeSpinner,mTimeSpinner;
    private EditText mOutput;
    private Button mCommit,mCheck;
    private LinearLayout mScanLayout;
    private List<String> mTimeList;
    public String[] mTimeStr;
    private ArrayAdapter<String> mAdapterTime;
    private TextView mTvDate;
    private Button mBtDate;
    private Calendar mCalendar;
    private int mYear,mMonth,mDate;
    //    private ScrollView mScrollView;
//    private LinearLayout mBottom;
    //填充机种ListView  adapter适配器
//    private static ArrayAdapter<String> mMachineAdapter;
    private static ArrayAdapter<String> mTypeAdapter;
    private StringBuilder builder;
    private String mUserId;
    private String mMachinenoid;
    private TextView mMachineId;
    private GridView mGridView;
    private MyAdapter mAdapter;
    private List<String> mList;
    private SimpleDateFormat mFormat;
    private Date mTimeLot;
    private ListView mListView;
    private EditText text5,text6,text7,text8;
    private TextView text1,text2,text3,text4;
    private List<Bean> mOutputlList;
    private OutputDdapter mOutputDdapter;
    private String sysid,flowid;
    private PopupWindow popupWindow;
    private LinearLayout mLinearLayout;
    private TextView mDeviceno;
    private List<OutputDayBean> mDayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_layout);
//      mWeightSpinner = (Spinner) findViewById(R.id.sp_output_weihgt);
        mDeviceno = (TextView) findViewById(R.id.tv_output_deviceno);
        mTypeSpinner = (Spinner) findViewById(R.id.sp_output_type);
        mTimeSpinner = (Spinner) findViewById(R.id.sp_output_time);
        mOutput = (EditText) findViewById(R.id.et_output_put);
        mCommit = (Button) findViewById(R.id.bt__output_commit);
        mCheck = (Button) findViewById(R.id.bt_output_check);
        mScanLayout = (LinearLayout) findViewById(R.id.ll_output_scan);
        mBtDate = (Button) findViewById(R.id.bt_output_date);
        mTvDate = (TextView) findViewById(R.id.tv_output_date);
        mScanLayout = (LinearLayout) findViewById(R.id.ll_output_scan);
        mMachineId = (TextView) findViewById(R.id.tv_output_machineid);
        mGridView = (GridView) findViewById(R.id.gv_output);
        mListView = (ListView) findViewById(R.id.lv_output);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_function_below);
        mMachinenoid = getIntent().getStringExtra("machinenoid");
        mMachineId.setText(mMachinenoid);
        mTvDate.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mScanLayout.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);
        mOutputlList = new ArrayList<>();
        mOutputDdapter = new OutputDdapter(this,mOutputlList);
        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
        builder = new StringBuilder();
        Staticdata sc = (Staticdata) getApplication();
        mUserId = sc.getLoginUserID();
        getMachine(mMachinenoid);
//      getDayOutput();
        setTime();
        int mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        mTimeSpinner.setSelection(mHour);
        mList = new ArrayList<>();
        mDayList = new ArrayList<>();
//        mWeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                getType(mWeightSpinner.getSelectedItem().toString());
//                mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        mOutputlList.clear();
//                        String saveStr2 = "[";
//                        saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
//                                + "\",\"deviceno\":\"" + mWeightSpinner.getSelectedItem().toString()
//                                + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
//                                + "\",\"date\":\"" + mTvDate.getText()
//                                + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
//                                + "\"},";
//                        saveStr2 = saveStr2.substring(0, saveStr2.length() - 1);
//                        saveStr2 += "]";
//                        checkOutput2(saveStr2);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {
                    mOutputlList.clear();
                    String saveStr2="[";
                    saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
                            +"\",\"deviceno\":\"" + mDeviceno.getText()
                            + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
                            + "\",\"date\":\"" + mTvDate.getText()
                            + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                            + "\"},";
                    saveStr2=saveStr2.substring(0,saveStr2.length()-1);
                    saveStr2+="]";
                    checkOutput2(saveStr2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mOutputlList.clear();
//                String saveStr2="[";
//                saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
//                        +"\",\"deviceno\":\"" + mWeightSpinner.getSelectedItem().toString()
//                        + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
//                        + "\",\"date\":\"" + mTvDate.getText()
//                        + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
//                        + "\"},";
//                saveStr2=saveStr2.substring(0,saveStr2.length()-1);
//                saveStr2+="]";
//                checkOutput2(saveStr2);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    /**
     * 修改
     */

    public void change(String str,final int position) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("updatemachine_output");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("jsonstr");
        hhi2.setDatavalue(str);
        li.add(hhi2);

        exechttprequest hf1=new exechttprequest();
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
                                        Toast.makeText(Output.this, "修改成功!", Toast.LENGTH_SHORT).show();
                                        execloadactivity.canclediglog();
                                        String saveStr1 = "[";
                                        saveStr1 += "{\"machinenoid\":\"" + mMachinenoid
                                                + "\",\"deviceno\":\"" + mDeviceno.getText()
                                                + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
                                                + "\",\"date\":\"" + mTvDate.getText()
                                                + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                                + "\"},";
                                        saveStr1 = saveStr1.substring(0, saveStr1.length() - 1);
                                        saveStr1 += "]";
                                        mOutputlList.clear();
                                        checkOutput(saveStr1);
                                        popupWindow.dismiss();
                                    }

                                } else {
                                    Toast.makeText(Output.this, "修改失敗!", Toast.LENGTH_SHORT).show();
                                    execloadactivity.canclediglog();
                                    popupWindow.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(Output.this, "修改失敗!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, li, null);

    }

    public void removeItem(int position) {
        mOutputlList.remove(position);
        mOutputDdapter.notifyDataSetChanged();
        mListView.invalidate();
    }
    /**
     * 刪除
     */
    public void cancle(String str, final int position) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("deletemachine_output");
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
                        //  查看数据返回格式并解析
                        if (paramObject != null && !"".equals(paramObject)) {
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                JSONArray array = object.getJSONArray("Table1");
                                JSONObject jsonObject = array.getJSONObject(0);
                                if ("1".equals(jsonObject.getString("item1"))) {
                                    Toast.makeText(Output.this, "刪除成功!", Toast.LENGTH_SHORT).show();
                                    removeItem(position);
                                    execloadactivity.canclediglog();
                                } else {
                                    Toast.makeText(Output.this, "刪除失敗!", Toast.LENGTH_SHORT).show();
                                    execloadactivity.canclediglog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(Output.this, "刪除失敗!", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, li, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getMachine(String str) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
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

    public void getDeviceno(String urlpath,List<httprequestinputdata> list,int sendmode) {
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
                                            Toast.makeText(Output.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mDeviceno.setText(bean.getDeviceno());
                                            getType(mDeviceno.getText().toString());
                                            mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    mOutputlList.clear();
                                                    if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {
                                                        String saveStr2 = "[";
                                                        saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
                                                                + "\",\"deviceno\":\"" + mDeviceno.getText()
                                                                + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
                                                                + "\",\"date\":\"" + mTvDate.getText()
                                                                + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                                                + "\"},";
                                                        saveStr2 = saveStr2.substring(0, saveStr2.length() - 1);
                                                        saveStr2 += "]";
                                                        checkOutput2(saveStr2);
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }

                                    } else {
                                        Toast.makeText(Output.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Output.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, list, null);

    }

    public void getType(String str) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> li = new ArrayList<httprequestinputdata>();
        httprequestinputdata hhi1 = new httprequestinputdata();
        hhi1.setDataname("directpar");
        hhi1.setDatavalue("getmachinemodel");
        li.add(hhi1);
        httprequestinputdata hhi2 = new httprequestinputdata();
        hhi2.setDataname("deviceno");
        hhi2.setDatavalue(str);
        li.add(hhi2);
        getkeyindatainput(url1, li, 2);
    }


    public  void getkeyindatainput(String urlpath,List<httprequestinputdata> list,int sendmode) {
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
                            List<String> typeList = new ArrayList<>();
                            jsontolist._jsonarray _jsonarray = js.datasetjasontolist(paramObject.toString());
                            for (int i = 0; i < _jsonarray.get_valuelist().size(); i++) {
                                String str = _jsonarray.get_valuelist().get(i).get(0).get_value();
                                typeList.add(str);
                            }
                            mTypeAdapter = new ArrayAdapter<>(Output.this, R.layout.support_simple_spinner_dropdown_item, typeList);
                            mTypeAdapter.setDropDownViewResource(R.layout.spinner_textview);
                            mTypeSpinner.setAdapter(mTypeAdapter);
                            if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {
                                String saveStr2 = "[";
                                saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
                                        + "\",\"deviceno\":\"" + mDeviceno.getText()
                                        + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
                                        + "\",\"date\":\"" + mTvDate.getText()
                                        + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                        + "\"},";
                                saveStr2 = saveStr2.substring(0, saveStr2.length() - 1);
                                saveStr2 += "]";
                                checkOutput2(saveStr2);
                            }

                        } else {
                            Toast.makeText(Output.this, "請求數據為空", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, list, null);
    }

    public void setTime() {
        mTimeList = new ArrayList<String>();
        mTimeStr = new String[] {
                "00:00-01:00", "01:00-02:00","02:00-03:00","03:00-04:00","04:00-05:00","05:00-06:00",
                "06:00-07:00", "07:00-08:00","08:00-09:00","09:00-10:00","10:00-11:00","11:00-12:00",
                "12:00-13:00", "13:00-14:00","14:00-15:00","15:00-16:00","16:00-17:00","17:00-18:00",
                "18:00-19:00", "19:00-20:00","20:00-21:00","21:00-22:00","22:00-23:00","23:00-00:00",
        };
        for (int i = 0; i < mTimeStr.length; i++) {
            mTimeList.add(mTimeStr[i]);
        }
        mAdapterTime = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,mTimeList);
        mAdapterTime.setDropDownViewResource(R.layout.spinner_textview);
        mTimeSpinner.setAdapter(mAdapterTime);
        try {
            mFormat = new SimpleDateFormat("HH:mm-HH:mm");
            mTimeLot = mFormat.parse(mTimeSpinner.getSelectedItem().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * [{"machinenoid":"M1311E034-084","deviceno":"A3G001","model":"Largan+kyocera","date":"2017-7-14","timeslot":"00:00 - 01:00","output":"hhnn","lotno":"M1311E034-084,M1311E034-084","createid":"F5460007"}]
     *
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt__output_commit:
                execloadactivity.opendialog(this, "正在執行");
                if(TextUtils.isEmpty(mOutput.getText())) {
                    Toast.makeText(this,"請輸入產出",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if(TextUtils.isEmpty(builder)) {
                    Toast.makeText(this,"請掃描批號",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
//                mOutputlList.clear();
//                String saveStr2="[";
//                saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
//                        +"\",\"deviceno\":\"" + mWeightSpinner.getSelectedItem().toString()
//                        + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
//                        + "\",\"date\":\"" + mTvDate.getText()
//                        + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
//                        + "\"},";
//                saveStr2=saveStr2.substring(0,saveStr2.length()-1);
//                saveStr2+="]";
//                checkOutput2(saveStr2);
                if(mDeviceno.getText() == null || "".equals(mDeviceno.getText())) {
                    Toast.makeText(Output.this,"機種為空，請檢查！",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if(mOutputlList.size() != 0) {
                    Toast.makeText(Output.this,"這個時間段已經存在產出記錄，不能重複提交！",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                } else {
                    if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {

                        String str = mTypeSpinner.getSelectedItem().toString();
                        try {
                            String str2 = URLDecoder.decode(str,"utf-8");
                            String saveStr="[";
                            saveStr += "{\"machinenoid\":\"" + mMachinenoid
                                    +"\",\"deviceno\":\"" + mDeviceno.getText()
                                    + "\",\"model\":\"" + str
                                    + "\",\"date\":\"" + mTvDate.getText()
                                    + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                    + "\",\"output\":\"" + mOutput.getText()
                                    + "\",\"lotno\":\"" + builder.substring(0,builder.length()-1).toString()
                                    + "\",\"createid\":\"" + mUserId
                                    + "\"},";
                            saveStr=saveStr.substring(0,saveStr.length()-1);
                            saveStr+="]";
                            submit(saveStr);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case R.id.ll_output_scan:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_output_date:
                new DatePickerDialog(Output.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mTvDate.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                },mYear,mMonth,mDate).show();
                if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {
                    mOutputlList.clear();
                    String saveStr2="[";
                    saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
                            +"\",\"deviceno\":\"" + mDeviceno.getText()
                            + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
                            + "\",\"date\":\"" + mTvDate.getText()
                            + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                            + "\"},";
                    saveStr2=saveStr2.substring(0,saveStr2.length()-1);
                    saveStr2+="]";
                    checkOutput2(saveStr2);
                }
                break;
            case R.id.bt_output_check:
                if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {
                    String str1 = mTypeSpinner.getSelectedItem().toString();
//                String str3 = "";
//                if(str.contains("+")) {
//                    String str1 = str.split("\\+")[0];
//                    String str2 = str.split("\\+")[1];
//                    str3 = str1 + "%2B" + str2;
//                }
//                    String str3 = URLEncoder.encode(str, "utf-8");
//                    Toast.makeText(Output.this,str3,Toast.LENGTH_LONG).show();
//                    Toast.makeText(Output.this,str + "---" +URLEncoder.encode(str,"utf-8")+ "---"+URLDecoder.decode(str,"utf-8"),Toast.LENGTH_LONG).show();
                    String saveStr1="[";
                    saveStr1 += "{\"machinenoid\":\"" + mMachinenoid
                            +"\",\"deviceno\":\"" + mDeviceno.getText()
                            + "\",\"model\":\"" + str1
                            + "\",\"date\":\"" + mTvDate.getText()
                            + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                            + "\"},";
                    saveStr1=saveStr1.substring(0,saveStr1.length()-1);
                    saveStr1+="]";
                    mOutputlList.clear();
                    mOutputDdapter.notifyDataSetChanged();
                    mListView.invalidate();
                    checkOutput(saveStr1);
                } else {
                    Toast.makeText(Output.this,"機種類型為空，請檢查！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void checkOutput(String str) {

        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<httprequestinputdata>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("getmachine_output_timeslot");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(str);
        list.add(httpTwo);

//http://10.142.136.222:8107/machineoutput/machineoutput.ashx?directpar=getmachine_output_timeslot&jsonstr=[{"machinenoid":"M1311E034-084","deviceno":"A3G001","model":"Largan+kyocera","date":"2017-7-14","timesolt":"21600000"}]
        exechttprequest hf1=new exechttprequest();
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                    }
                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        if (paramObject != null && !"".equals(paramObject)) {
                            Bean bean = null;
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                if(object != null && !"".equals(object)) {
                                    JSONArray array = object.getJSONArray("Table1");
                                    if(array != null && !"".equals(array)) {
                                        for (int i = 0; i<array.length();i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            if(jsonObject != null && !"".equals(jsonObject)) {
                                                bean = new Bean();
                                                bean.setCREATEID(jsonObject.getString("CREATEID"));
                                                bean.setCREATETIME(jsonObject.getString("CREATETIME"));
                                                bean.setMACHINENOID(jsonObject.getString("MACHINENOID"));
                                                bean.setLOTNO(jsonObject.getString("LOTNO"));
                                                bean.setOUTPUT(jsonObject.getString("OUTPUT"));
                                                bean.setTIMESLOT(jsonObject.getString("TIMESLOT"));
                                                bean.setDATES(jsonObject.getString("DATES"));
                                                bean.setMODEL(jsonObject.getString("MODEL"));
                                                bean.setDEVICENO(jsonObject.getString("DEVICENO"));
                                                bean.setSYSID(jsonObject.getString("SYSID"));
                                                bean.setFLOWID(jsonObject.getString("FLOWID"));
                                            }
                                            mOutputlList.add(bean);
//                                            Toast.makeText(Output.this, mOutputlList.get(0).getCREATEID()+","+mOutputlList.get(0).getCREATETIME()+","+mOutputlList.get(0).getSYSID()+","+mOutputlList.get(0).getFLOWID()
//                                                    +","+mOutputlList.get(0).getDATES()+","+mOutputlList.get(0).getDEVICENO()+","+mOutputlList.get(0).getLOTNO()+","+mOutputlList.get(0).getMACHINENOID()+","+mOutputlList.get(0).getMODEL()
//                                                    +","+mOutputlList.get(0).getOUTPUT()+","+mOutputlList.get(0).getTIMESLOT(),Toast.LENGTH_SHORT).show();
                                        }
                                        if(mOutputlList.size() == 0) {
                                            Toast.makeText(Output.this,"查詢為空!",Toast.LENGTH_SHORT).show();
                                        }
                                        mOutputDdapter = new OutputDdapter(Output.this,mOutputlList);
                                        mListView.setAdapter(mOutputDdapter);
                                    } else {
                                        Toast.makeText(Output.this,"查詢為空!",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Output.this,"查詢為空!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-" , Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, list, null);
    }



    public void checkOutput2 (String str) {

        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<httprequestinputdata>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("getmachine_output_timeslot");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(str);
        list.add(httpTwo);

//http://10.142.136.222:8107/machineoutput/machineoutput.ashx?directpar=getmachine_output_timeslot&jsonstr=[{"machinenoid":"M1311E034-084","deviceno":"A3G001","model":"Largan+kyocera","date":"2017-7-14","timesolt":"21600000"}]


        exechttprequest hf1=new exechttprequest();
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                    }
                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        if (paramObject != null && !"".equals(paramObject)) {
                            Bean bean = null;
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                if(object != null && !"".equals(object)) {
                                    JSONArray array = object.getJSONArray("Table1");
                                    if(array != null && !"".equals(array)) {
                                        for (int i = 0; i<array.length();i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            if(jsonObject != null && !"".equals(jsonObject)) {
                                                bean = new Bean();
                                                bean.setCREATEID(jsonObject.getString("CREATEID"));
                                                bean.setCREATETIME(jsonObject.getString("CREATETIME"));
                                                bean.setMACHINENOID(jsonObject.getString("MACHINENOID"));
                                                bean.setLOTNO(jsonObject.getString("LOTNO"));
                                                bean.setOUTPUT(jsonObject.getString("OUTPUT"));
                                                bean.setTIMESLOT(jsonObject.getString("TIMESLOT"));
                                                bean.setDATES(jsonObject.getString("DATES"));
                                                bean.setMODEL(jsonObject.getString("MODEL"));
                                                bean.setDEVICENO(jsonObject.getString("DEVICENO"));
                                                bean.setSYSID(jsonObject.getString("SYSID"));
                                                bean.setFLOWID(jsonObject.getString("FLOWID"));
                                            }
                                            mOutputlList.add(bean);
                                        }
                                    } else {

                                    }
                                } else {

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
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-" , Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, list, null);
    }

    public void submit(String str) {
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("submitmachineputput");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(str);
        list.add(httpTwo);

        exechttprequest hf1=new exechttprequest();
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
                                JSONObject object2 = array.getJSONObject(0);
                                String item1 = object2.getString("item1");
                                if(Integer.parseInt(item1) == 1) {
                                    Toast.makeText(Output.this, object2.getString("item2"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Output.this, object2.getString("item2"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            execloadactivity.canclediglog();

                            if(mTypeSpinner.getSelectedItem()!=null && !"".equals(mTypeSpinner.getSelectedItem())) {
                                String saveStr2="[";
                                saveStr2 += "{\"machinenoid\":\"" + mMachinenoid
                                        +"\",\"deviceno\":\"" + mDeviceno.getText()
                                        + "\",\"model\":\"" + mTypeSpinner.getSelectedItem().toString()
                                        + "\",\"date\":\"" + mTvDate.getText()
                                        + "\",\"timeslot\":\"" + mTimeSpinner.getSelectedItem().toString()
                                        + "\"},";
                                saveStr2=saveStr2.substring(0,saveStr2.length()-1);
                                saveStr2+="]";
                                checkOutput2(saveStr2);
                            }
                        } else {
                            Toast.makeText(Output.this, "提交失敗!", Toast.LENGTH_SHORT).show();
                            execloadactivity.canclediglog();
                        }
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject, Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, list, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if(scanResult == null || "".equals(scanResult)) {
                Toast.makeText(this, "掃描為空", Toast.LENGTH_SHORT).show();
            } else {
                String str = scanResult.substring(0,1);
                if("A".equals(str) || "P".equals(str) || "S".equals(str) || "K".equals(str)) {
//                }
//                if(!isNumber(scanResult)) {
                    if(scanResult.length() == 12 || scanResult.length() == 15 ||
                            scanResult.length() == 13 || scanResult.length() == 16) {

                        if(mList.size() == 0){
                            mList.add(scanResult);
                        } else {
                            for (int i = 0; i < mList.size(); i++) {
                                String regEx = "[^a-zZ-Z0-9]";
                                Pattern p = Pattern.compile(regEx);
                                Matcher m1 = p.matcher(scanResult);
                                Matcher m2 = p.matcher(mList.get(i));
                                if(m1.replaceAll("").trim().equals(m2.replaceAll("").trim())) {
                                    Toast.makeText(Output.this,"批號重複，請檢查！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            mList.add(scanResult);
                        }
                        builder.append(scanResult + ",");
                        mAdapter = new MyAdapter(mList,Output.this);
                        mGridView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(Output.this,"批號格式不正確,請檢查!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Output.this,"批號格式不正確,請檢查!",Toast.LENGTH_SHORT).show();
                }
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mList.remove(position);
                        builder.deleteCharAt(position);
                        mAdapter.notifyDataSetChanged();
                        mGridView.invalidate();
                    }
                });
            }
        }
    }

    public boolean isNumber(String str) {
        for(int i = 0;i<str.length();i++) {
            if(!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<String> mList;
        public MyAdapter(List<String> mList,Context context) {
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
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.output_item,null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_output_open);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(mList.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView mTextView;
    }

    class OutputDdapter extends BaseAdapter {
        private Context context;
        private List<Bean> mList;

        public OutputDdapter (Context context,List<Bean> mList) {
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
            final View view = convertView;
            holder.mTextView1.setText(mList.get(position).getMACHINENOID());
            holder.mTextView2.setText(mList.get(position).getDEVICENO());
            holder.mTextView3.setText(mList.get(position).getMODEL());
            holder.mTextView4.setText(mList.get(position).getTIMESLOT());
            holder.mTextView5.setText(mList.get(position).getOUTPUT());
            holder.mTextView6.setText(mList.get(position).getLOTNO());
            holder.mTextView7.setText(mList.get(position).getCREATEID());
            holder.mTextView8.setText(mList.get(position).getCREATETIME());

            sysid = mList.get(position).getSYSID();
            flowid = mList.get(position).getFLOWID();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示:").setMessage("請選擇要進行的操作")
                            .setPositiveButton("修改", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showPopupWindow(Output.this, view, mList, position);

                                }
                            }).setNegativeButton("刪除",new AlertDialog.OnClickListener() {

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
                                Toast.makeText(Output.this, "只能刪除本人提交的內容", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            cancle(saveStr,position);
                        }
                    });
                    builder.create().show();
                }
            });
            return convertView;
        }
    }

    public void showPopupWindow(Context context,View parent,List<Bean> list, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.popupwindow_layout2,null);
        text1 = (TextView) view.findViewById(R.id.et_unusual_machineid);
        text1.setText(list.get(position).getMACHINENOID());
        text2 = (TextView) view.findViewById(R.id.et_popupwondow_deviceno);
        text2.setText(list.get(position).getDEVICENO());
        text3 = (TextView) view.findViewById(R.id.et_popupwindow_model);
        text3.setText(list.get(position).getMODEL());
        text4 = (TextView) view.findViewById(R.id.et_popupwindow_timelot);
        text4.setText(list.get(position).getTIMESLOT());
        text5 = (EditText) view.findViewById(R.id.et_popupwindow_output);
        text5.setText(list.get(position).getOUTPUT());
        text6 = (EditText) view.findViewById(R.id.et_popupwindow_createid);
        text6.setText(list.get(position).getLOTNO());
        text7 = (EditText) view.findViewById(R.id.et_popupwindow_person);
        text7.setText(list.get(position).getCREATEID());
        text8 = (EditText) view.findViewById(R.id.et_popupwindow_creattime);
        text8.setText(list.get(position).getCREATETIME());
        Button button = (Button) view.findViewById(R.id.bt_popupwindow_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveStr2="[";
                saveStr2 += "{\"machinenoid\":\"" + text1.getText()
                        +"\",\"deviceno\":\"" + text2.getText()
                        + "\",\"model\":\"" + text3.getText()
                        + "\",\"date\":\"" + mTvDate.getText()
                        + "\",\"timeslot\":\"" + text4.getText()
                        + "\",\"output\":\"" + text5.getText()
                        + "\",\"lotno\":\"" + text6.getText()
                        + "\",\"createid\":\"" + text7.getText()
                        + "\",\"sysid\":\"" + sysid
                        + "\",\"flowid\":\"" + flowid
                        + "\"},";
                saveStr2=saveStr2.substring(0,saveStr2.length()-1);
                saveStr2+="]";
                change(saveStr2, position);
            }
        });

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(mLinearLayout, Gravity.CENTER_HORIZONTAL,0,0);

    }

    class ViewHolder2 {

        TextView mTextView1,mTextView2,mTextView3,mTextView4,mTextView5,mTextView6,mTextView7,mTextView8;
    }

    /**
     * 獲取一天的產出
     *
     */

    public void getDayOutput() {
        String saveStr="[";
        saveStr += "{\"machinenoid\":\"" + "M1610D009-001"
                +"\",\"deviceno\":\"" + "NH-A"
                + "\",\"date\":\"" + "2017-8-31"
                + "\"},";
        saveStr=saveStr.substring(0,saveStr.length()-1);
        saveStr+="]";
        String url1 = Staticdata.httpurl + "machineoutput/machineoutput.ashx";
        List<httprequestinputdata> list = new ArrayList<httprequestinputdata>();
        httprequestinputdata httpOne = new httprequestinputdata();
        httpOne.setDataname("directpar");
        httpOne.setDatavalue("getmachine_output_day");
        list.add(httpOne);
        httprequestinputdata httpTwo = new httprequestinputdata();
        httpTwo.setDataname("jsonstr");
        httpTwo.setDatavalue(saveStr);
        list.add(httpTwo);

        exechttprequest hf1=new exechttprequest();
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1, "正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        Toast.makeText(Output.this,paramObject.toString(),Toast.LENGTH_SHORT).show();
                        if (paramObject != null && !"".equals(paramObject)) {
                            OutputDayBean bean = null;
                            try {
                                JSONObject object = new JSONObject(paramObject.toString());
                                if(object != null && !"".equals(object)) {
                                    JSONArray array = object.getJSONArray("Table1");
                                    if(array != null && !"".equals(array)) {
                                        for (int i = 0; i<array.length();i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            if(jsonObject != null && !"".equals(jsonObject)) {
                                                bean = new OutputDayBean();
                                                bean.setMACHINENOID(jsonObject.getString("MACHINENOID"));
                                                bean.setMACHINENO(jsonObject.getString("MACHINENO"));
                                                bean.setDATES(jsonObject.getString("DATES"));
                                                bean.setTIMESLOT(jsonObject.getString("TIMESLOT"));
                                                bean.setDEVICENO(jsonObject.getString("DEVICENO"));
                                                bean.setMODEL(jsonObject.getString("MODEL"));
                                                bean.setLOTNO(jsonObject.getString("LOTNO"));
                                                bean.setOUTPUT(jsonObject.getString("OUTPUT"));
                                                bean.setDEVICENO(jsonObject.getString("DEVICENO"));
                                                bean.setOUTPUTCREATEID(jsonObject.getString("OUTPUTCREATEID"));
                                                bean.setOUTPUTCREATETIME(jsonObject.getString("OUTPUTCREATETIME"));
                                                bean.setDEFECTNO(jsonObject.getString("DEFECTNO"));
                                                bean.setSTARTTIME(jsonObject.getString("STARTTIME"));
                                                bean.setENDTIME(jsonObject.getString("ENDTIME"));
                                                bean.setENGINNER(jsonObject.getString("ENGINNER"));
                                                bean.setDEFECTCREATEID(jsonObject.getString("DEFECTCREATEID"));
                                                bean.setDEFECTCREATETIME(jsonObject.getString("DEFECTCREATETIME"));
                                            }
                                            mDayList.add(bean);
//                                            Toast.makeText(Output.this, mOutputlList.get(0).getCREATEID()+","+mOutputlList.get(0).getCREATETIME()+","+mOutputlList.get(0).getSYSID()+","+mOutputlList.get(0).getFLOWID()
//                                                    +","+mOutputlList.get(0).getDATES()+","+mOutputlList.get(0).getDEVICENO()+","+mOutputlList.get(0).getLOTNO()+","+mOutputlList.get(0).getMACHINENOID()+","+mOutputlList.get(0).getMODEL()
//                                                    +","+mOutputlList.get(0).getOUTPUT()+","+mOutputlList.get(0).getTIMESLOT(),Toast.LENGTH_SHORT).show();
                                        }
//                                        if(mOutputlList.size() == 0) {
//                                            Toast.makeText(Output.this,"查詢為空!",Toast.LENGTH_SHORT).show();
//                                        }
//                                        mOutputDdapter = new OutputDdapter(Output.this,mOutputlList);
//                                        mListView.setAdapter(mOutputDdapter);
                                    } else {
                                        Toast.makeText(Output.this,"查詢為空!",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Output.this,"查詢為空!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(Output.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, url1, list, null);
    }
}