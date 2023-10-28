package app.cmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.cmapp.DataTable.DataTable;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.NetUtils;
import app.cmapp.appcdl.execloadactivity;
import app.cmapp.zxing.activity.CaptureActivity;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.R;

/**
 * Owner:S7202916
 * CreateDate:2017/6/26 15:16
 */
public class  NewFunctionActivity extends ActivityInteractive  implements View.OnClickListener{ //AppCompatActivity
    private EditText mEditText1;
    private Button mPbutton,mCommitButton;
    private int mId;
    private Boolean isSuccess;
    private CallWebapi mCallWebapi;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private SimpleDateFormat mFormat;
    private Date mCurDate;
    private DataTable mDataTable;
    private String mIp;
    private String mUserId;
    private String mStr;
    private Spinner mSpinner;
    private static ArrayAdapter<String> mAdapter;
    private List<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newfuction);
        mEditText1 = (EditText) findViewById(R.id.et_function_pihao);
        mPbutton = (Button) findViewById(R.id.bt_pihao);
        mSpinner = (Spinner) findViewById(R.id.sp_jitaohao);
        mCommitButton = (Button) findViewById(R.id.bt_commit);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_function_bundle);
        mScrollView = (ScrollView) findViewById(R.id.sv_function);
        mFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        mCurDate= new Date(System.currentTimeMillis());
        mIp = NetUtils.getLocalIPAddress(this);
        Staticdata sc = (Staticdata) getApplication();
        mUserId = sc.getLoginUserID();
        mStr = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");
        mPbutton.setOnClickListener(this);
        mCommitButton.setOnClickListener(this);
        mCallWebapi = new CallWebapi("VT-Q","ATF001","t_oven_parmer",this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    page_Load();
                }
            }, "LoadTask");

        } catch (Exception e) {
            e.printStackTrace();
        }
//        mList = new ArrayList<String>();
//        for (int i = 1; i< 17;i++) {
//            mList.add(i + "");
//        }
//        mAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,mList);
//        mSpinner.setAdapter(mAdapter);



    }
    private void page_Load() throws Exception {
        DataTable dtoven=mCallWebapi.CallRDT("getovendata");
        setAdapter(mSpinner,
                BaseFuncation.setvalue(dtoven, null, "MACHINENOID", NewFunctionActivity.this)
        );
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_pihao:
                mId = 0;
                intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.bt_commit:
                execloadactivity.opendialog(this, "正在執行");
                final String pihao = mEditText1.getText().toString();
                final String jitaihao = mSpinner.getSelectedItem().toString();
                if(pihao == null || "".equals(pihao)) {
                    Toast.makeText(this,"請輸入批號",Toast.LENGTH_SHORT).show();
                    execloadactivity.canclediglog();
                    return;
                }
                try {
                    isSuccess = mCallWebapi.CallRB("insertlotnomachinenoflag",pihao,jitaihao,mStr,mIp,mUserId);
                    if(isSuccess) {
                        mDataTable = mCallWebapi.CallRDT("lotnomachinenoinfo",pihao,jitaihao);
                        if("I".equals(mDataTable.Rows(0).get_CellValue("state"))) {
                            Toast.makeText(this,"進站成功",Toast.LENGTH_SHORT).show();
                        } else if("O".equals(mDataTable.Rows(0).get_CellValue("state"))){
                            Toast.makeText(this,"出站成功",Toast.LENGTH_SHORT).show();
                        }
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_item,null);
                        ((TextView)view.findViewById(R.id.tv_listview_pihao)).setText(mDataTable.Rows(0).get_CellValue("lotno"));
                        ((TextView)view.findViewById(R.id.tv_listview_ip)).setText(mDataTable.Rows(0).get_CellValue("ip"));
                        ((TextView)view.findViewById(R.id.tv_listview_state)).setText(mDataTable.Rows(0).get_CellValue("state"));
                        ((TextView)view.findViewById(R.id.tv_listview_time)).setText(mDataTable.Rows(0).get_CellValue("createtime"));
                        ((TextView)view.findViewById(R.id.tv_listview_creater)).setText(mDataTable.Rows(0).get_CellValue("createid"));
                        ((TextView)view.findViewById(R.id.tv_listview_jitaihao)).setText(mDataTable.Rows(0).get_CellValue("machinenoid"));

                        mLinearLayout.addView(view);
                        mScrollView.scrollTo(100, view.getBottom());
                        execloadactivity.canclediglog();
                    } else {
                        Toast.makeText(this,"進站失敗",Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    execloadactivity.canclediglog();
                }

                break;
       }
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
                switch (mId) {
                    case 0:
                        mEditText1.setText(scanResult);
                        break;
                }
            }
        }
    }
}