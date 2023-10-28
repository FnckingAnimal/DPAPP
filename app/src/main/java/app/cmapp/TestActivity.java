package app.cmapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.cmapp.IncludingSFC.SFCStaticdata;
import app.cmapp.appcdl.MyThreadPool;
import app.cmapp.appcdl.NetUtils;
import app.cmapp.appcdl.execloadactivity;
import app.dpapp.PysBean;
import app.dpapp.PysBenTwo;
import app.dpapp.R;
import app.dpapp.TestBean;
import app.dpapp.soap.PublicSOAP;

/**
 * Created by tpp on 2018/3/7.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvDate;
    private EditText mTest, mMissing, mNumber, mName, mMeasure;
    private Spinner mManSpinner, mCategorySpinner,mXiangmu;
    private int mYear, mMonth, mDates;
    private Calendar mCalendar;
    private Button mCommit;
    private List<String>  mManList,  mCategoryList;
    private ArrayAdapter<String>  mManAdapter, mCategoryAdapter;
    private TextView mConfig;
    private List<PysBean> mList;
    private List<PysBenTwo> mList2;
    private String str;
    private String mId;
    private String zhubie;
    private String mUserId;
    private String HDSerialNo;
    private ArrayAdapter<String> mXiangMuAdapter;
    private List<String> list;
    private List<TestBean> list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_layout);
        init();
    }

    public void init() {
        mTvDate = (TextView) findViewById(R.id.tv_test_date_t);
        mTest = (EditText) findViewById(R.id.tv_test_test_t);
        mMissing = (EditText) findViewById(R.id.tv_test_missing_t);
        mNumber = (EditText) findViewById(R.id.tv_test_number_t);
        mName = (EditText) findViewById(R.id.tv_test_name_t);
        mMeasure = (EditText) findViewById(R.id.tv_test_measure_t);
        mManSpinner = (Spinner) findViewById(R.id.sp_test_man);
        mCategorySpinner = (Spinner) findViewById(R.id.sp_test_category);
        mXiangmu = (Spinner) findViewById(R.id.sp_test_xiangmu);
        mCommit = (Button) findViewById(R.id.bt_test_commit);
        mConfig = (TextView) findViewById(R.id.tv_test_commit);
        mConfig.setOnClickListener(this);

        Staticdata sc = (Staticdata) getApplication();
        mUserId = sc.getLoginUserID();
        HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");
        SFCStaticdata.staticmember.HDSerialNo=HDSerialNo;

        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDates));
        mTvDate.setOnClickListener(this);
        mCommit.setOnClickListener(this);
//        mFormat = new SimpleDateFormat("HH:mm:ss");
//        Date curDate = new Date(System.currentTimeMillis());
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
//      int date = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDates = mCalendar.get(Calendar.DAY_OF_MONTH);
        mTvDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDates));

        mXiangmu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str = mList2.get(position).getATT2();
                mId = mList2.get(position).getID();
//              zhubie = mList2.get(position).getZHUBIE();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mList = new ArrayList<>();
        mList2 = new ArrayList<>();
        list2 = new ArrayList<>();

        mManList = new ArrayList<>();
        mManList.add("");
        mManList.add("李威");
        mManList.add("邢文憑");
        mManList.add("陶先忠");
        mManList.add("謝雯潔");
        mManList.add("陳思思");
        mManList.add("陳潔");
        mManList.add("羅美玲");
        mManList.add("廖鳳宜");
        mManList.add("陳昭平");
        mManList.add("羅杰");
        mManAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mManList);
        mManSpinner.setAdapter(mManAdapter);

        mCategoryList = new ArrayList<>();
//        mCategoryList.add("主要缺失");
//        mCategoryList.add("次要缺失");
//        mCategoryList.add("一般缺失");
//        mCategoryAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mCategoryList);
//        mCategorySpinner.setAdapter(mCategoryAdapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_test_date_t:
                new DatePickerDialog(TestActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mTvDate.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, mYear, mMonth, mDates).show();

                break;
            case R.id.bt_test_commit:
                execloadactivity.opendialog(this, "正在執行");
                if (mNumber.getText() == null || "".equals(mNumber.getText())) {
                        Toast.makeText(TestActivity.this, "请填写責任人工號", Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    return;
                }
                if (mXiangmu.getSelectedItem() == null || "".equals(mXiangmu.getSelectedItem())) {
                    Toast.makeText(TestActivity.this, "项目为空", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if (mManSpinner.getSelectedItem() == null || "".equals(mManSpinner.getSelectedItem())) {
                    Toast.makeText(TestActivity.this, "请选择核查人", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if (mTest.getText() == null || "".equals(mTest.getText())) {
                    Toast.makeText(TestActivity.this, "请填写稽核站位", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if (mMissing.getText() == null || "".equals(mMissing.getText())) {
                    Toast.makeText(TestActivity.this, "请填写缺失描述", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                if (mMeasure.getText() == null || "".equals(mMeasure.getText())) {
                    Toast.makeText(TestActivity.this, "请填写改善措施", Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
//                Toast.makeText(TestActivity.this, mXiangmu.getSelectedItem().toString()+"-->"+mUserId+"-->"+mId+"-->"+mTvDate.getText().toString()+"-->"+mList.get(0).getSHIFT()+"-->"+
//                        mManSpinner.getSelectedItem().toString()+"-->"+mTest.getText().toString()+"-->"+mMissing.getText().toString()+"-->"+mName.getText().toString()+"-->"+mNumber.getText().toString()+"-->"+
//                        mCategorySpinner.getSelectedItem().toString()+"-->"+zhubie+"-->"+mMeasure.getText().toString()+"-->"+HDSerialNo, Toast.LENGTH_LONG).show();
                MyThreadPool.pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        PublicSOAP ps1 = new PublicSOAP();
                        String str2 = ps1.commitTest(mXiangmu.getSelectedItem().toString(),mUserId,mId,mTvDate.getText().toString(),mList.get(0).getSHIFT(),
                                mManSpinner.getSelectedItem().toString(),mTest.getText().toString(),mMissing.getText().toString(),mName.getText().toString(),mNumber.getText().toString(),
                                mCategorySpinner.getSelectedItem().toString(),zhubie,mMeasure.getText().toString(),HDSerialNo);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = str2;
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            case R.id.tv_test_commit:
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
                    Toast.makeText(TestActivity.this,"請輸入工號!",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    mList = (List<PysBean>) msg.obj;
                    if(mList != null) {
                        mName.setText(mList.get(0).getUSERNAME());

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {
                                PublicSOAP ps1 = new  PublicSOAP();
                                List<PysBenTwo> ls1 = new ArrayList<>();
                                ls1 = ps1.getpyssubject(mList.get(0).getGROUPNAME());
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = ls1;
                                mHandler2.sendMessage(msg);
                            }
                        });
                    } else {
                        Toast.makeText(TestActivity.this,"信息為空",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 1:
//                    16沒有評分權限,1獲取分值失敗 2保存失敗 0保存成功
                    execloadactivity.canclediglog();
                    String str = (String) msg.obj;
//                    Toast.makeText(TestActivity.this,"----->"+str,Toast.LENGTH_SHORT).show();
                    if("16".equals(str)) {
                        Toast.makeText(TestActivity.this,"用户没有评分权限",Toast.LENGTH_SHORT).show();
                    } else if("1".equals(str)){
                        Toast.makeText(TestActivity.this,"获取分值失败",Toast.LENGTH_SHORT).show();
                    } else if("2".equals(str)) {
                        Toast.makeText(TestActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                    } else if("0".equals(str)) {
                        Toast.makeText(TestActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    break;



            }
        }
    };

    private android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    mList2 = (List<PysBenTwo>) msg.obj;
                    if(mList2 != null) {
                        str = mList2.get(0).getATT2();
                        mId= mList2.get(0).getID();
                        zhubie = mList2.get(0).getZHUBIE();
//                        mWeight.setText("(-" + Float.parseFloat(mList2.get(0).getBIZHONG()) * 100 + "~" + Float.parseFloat(mList2.get(0).getBIZHONG()) * 100 + ")");
//                        zhubie = mList2.get(0).getZHUBIE();
                        list = new ArrayList<>();
                        for (int i = 0; i < mList2.size(); i++) {
                            list.add(mList2.get(i).getXIANGMU());
                        }
                        mXiangMuAdapter = new ArrayAdapter<>(TestActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
                        mXiangmu.setAdapter(mXiangMuAdapter);

                        MyThreadPool.pool.execute(new Runnable() {
                            @Override
                            public void run() {
                                PublicSOAP ps1 = new PublicSOAP();
                                List<TestBean> ls1 = new ArrayList<>();
                                ls1 = ps1.getpyscategory(mId);
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = ls1;
                                mHandler3.sendMessage(msg);
                            }
                        });
                    } else {
                        Toast.makeText(TestActivity.this,"無資料",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private android.os.Handler mHandler3 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    list2 = (List<TestBean>) msg.obj;
                    if(list2 != null) {
                        for (int i = 0; i < list2.size(); i++) {
                            mCategoryList.add(list2.get(i).getSTYLE());
                        }
                        mCategoryAdapter = new ArrayAdapter<>(TestActivity.this, R.layout.support_simple_spinner_dropdown_item, mCategoryList);
                        mCategorySpinner.setAdapter(mCategoryAdapter);

                    } else {
                        Toast.makeText(TestActivity.this,"缺失类别为空!",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
