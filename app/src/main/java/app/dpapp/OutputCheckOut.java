package app.dpapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.exechttprequest;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.parameterclass.httprequestinputdata;

/**
 * Created by S7202916 on 2017/9/12.
 */
public class OutputCheckOut extends AppCompatActivity implements View.OnClickListener{
    private TextView mMachineId;
    private TextView mDeviceno;
    private TextView mTvDate;
    private Button mBtDate;
    private Calendar mCalendar;
    private int mYear,mMonth,mDate;
    private List<OutputDayBean> mDayList;
    private ListView mListView;
    private OutputDdapter mAdapter;
    private Button mBtCheck;
    private String mStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_checkout);
        mMachineId = (TextView) findViewById(R.id.tv_output_machineid);
        mBtDate = (Button) findViewById(R.id.bt_output_date);
        mTvDate = (TextView) findViewById(R.id.tv_output_date);
        mDeviceno = (TextView) findViewById(R.id.tv_output_deviceno);
        mListView = (ListView) findViewById(R.id.lv_output);
        mBtCheck = (Button) findViewById(R.id.bt_output_check);
        mStr = getIntent().getStringExtra("machinenoid");
        mBtCheck.setOnClickListener(this);
        mMachineId.setText(mStr);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDate = mCalendar.get(Calendar.DAY_OF_MONTH);
        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDate));
        mTvDate.setOnClickListener(this);
        mDayList = new ArrayList<>();
        mAdapter = new OutputDdapter(this,mDayList);
        mListView.setAdapter(mAdapter);
        getMachine(mStr);
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
                                            Toast.makeText(OutputCheckOut.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mDeviceno.setText(bean.getDeviceno());
                                        }

                                    } else {
                                        Toast.makeText(OutputCheckOut.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(OutputCheckOut.this, "該機台號綁定的機鐘為空,請檢查!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OutputCheckOut.this
                                , "操作失敗，請求異常-" + paramObject.toString(), Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(OutputCheckOut.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, list, null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_output_date:
                new DatePickerDialog(OutputCheckOut.this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mTvDate.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                },mYear,mMonth,mDate).show();
                break;
            case R.id.bt_output_check:
                mDayList.clear();
                mAdapter.notifyDataSetChanged();
                mListView.invalidate();
                getDayOutput();
                break;
        }
    }

    /**
     * 獲取一天的產出
     *
     */

    public void getDayOutput() {
        String saveStr="[";
        saveStr += "{\"machinenoid\":\"" + mStr
                +"\",\"deviceno\":\"" + mDeviceno.getText().toString()
                + "\",\"date\":\"" + mTvDate.getText().toString()
                + "\"},";
//        Toast.makeText(OutputCheckOut.this, mStr + "," + mDeviceno.getText() + "," + mTvDate.getText(), Toast.LENGTH_SHORT).show();
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
                        if (paramObject != null && !"".equals(paramObject)) {
//                            Toast.makeText(OutputCheckOut.this,paramObject.toString(),Toast.LENGTH_SHORT).show();
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
                                        }
                                        for (int j = 0; j < mDayList.size() - 1; j++) {
                                            for (int h = mDayList.size() - 1; h > j ; h--) {
                                                if(mDayList.get(h).getOUTPUT().equals(mDayList.get(j).getOUTPUT())) {
                                                    mDayList.remove(h);
                                                }
                                            }

                                        }
                                        if(mDayList.size() == 0) {
                                            Toast.makeText(OutputCheckOut.this,"查詢為空!",Toast.LENGTH_SHORT).show();
                                        }
                                        mAdapter = new OutputDdapter(OutputCheckOut.this,mDayList);
                                        mListView.setAdapter(mAdapter);

                                    } else {
                                        Toast.makeText(OutputCheckOut.this,"查詢為空!",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(OutputCheckOut.this,"查詢為空!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OutputCheckOut.this
                                , "操作失敗，請求異常-", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(OutputCheckOut.this
                                , "操作失敗，請求異常", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },

                this, url1, list, null);
    }

    class ViewHolder2 {

        TextView mTextView1,mTextView2,mTextView3,mTextView4,mTextView5,mTextView6,mTextView7,mTextView8;
    }

    class OutputDdapter extends BaseAdapter {
        private Context context;
        private List<OutputDayBean> mList;

        public OutputDdapter (Context context,List<OutputDayBean> mList) {
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
            holder.mTextView1.setText(mList.get(position).getMACHINENOID());
            holder.mTextView2.setText(mList.get(position).getDEVICENO());
            holder.mTextView3.setText(mList.get(position).getMODEL());
            holder.mTextView4.setText(mList.get(position).getTIMESLOT());
            holder.mTextView5.setText(mList.get(position).getOUTPUT());
            holder.mTextView6.setText(mList.get(position).getLOTNO());
            holder.mTextView7.setText(mList.get(position).getENGINNER());
            holder.mTextView8.setText(mList.get(position).getOUTPUTCREATETIME());
            return convertView;
        }
    }
}
