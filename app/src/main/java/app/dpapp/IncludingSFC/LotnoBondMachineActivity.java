package app.dpapp.IncludingSFC;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.appleeol.EOLStagingTimeWIPActivty;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;
import app.dpapp.utils.DialogShowUtil;
import app.dpapp.zxing.activity.CaptureActivity;

public class LotnoBondMachineActivity extends AppCompatActivity {

    private EditText editText_lotno,editText_device,editText_sysid,editText_machineno;
    private String messgestr;
    private Button bt_scan;
    private String clickFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotno_bond_machine);
        initView();
        Bundle bundle = getIntent().getExtras();
        String lotno = bundle.getString("lotno");
        editText_lotno.setText(lotno);
        //根據批號查找批號信息
        getLotnoWipInfo(lotno);
    }

    private void getLotnoWipInfo(String lotno) {
        final List<SOAPParameter> parameterList = new ArrayList<>();
        final String methodname = "getLotnoWipInfo";
        parameterList.add(new SOAPParameter("lotno", lotno));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String[] result = asop.getLotnoWipInfo(methodname, parameterList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String[] result0 = (String[]) msg.obj;
                    if (result0 == null) {
                        //Toast.makeText(EOLStagingTimeWIPActivty.this,"獲取批號信息出錯",Toast.LENGTH_LONG).show();
                        messgestr = "獲取批號信息出錯";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        LotnoBondMachineActivity.this.finish();
                        return;
                    }
                    String device=result0[1];
                    editText_device.setText(device);
                    break;
                case 1:
                    String result1 =  msg.obj.toString();
                    if (result1 == null) {
                        messgestr = "獲取机台号信息出錯";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        return;
                    }
                    editText_machineno.setText(result1);
                    break;
                case 2:
                    String result2 =  msg.obj.toString();
                    if (result2 == null) {
                        messgestr = "Check信息出錯";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        return;
                    }
                    String state=result2;
                    String lotno2=editText_lotno.getText().toString();
                    String device2=editText_device.getText().toString();
                    String sysid2=editText_sysid.getText().toString();
                    String machineno2=editText_machineno.getText().toString();
                    if(clickFlag.equals("1")){ //进站
                        if(state.equals("1")){
                            messgestr = "批号"+lotno2+"已经在机台"+machineno2+"进站了，不能再进站";
                            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                            return;
                        }else if(state.equals("2")){
                            messgestr = "批号"+lotno2+"已经在机台"+machineno2+"出站了，不能再进站";
                            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                            return;
                        }else if(state.equals("0")){
                            saveLotnBondMachineCheckinInfo(lotno2,device2,sysid2,machineno2);
                        }
                    }else if(clickFlag.equals("2")){ //出站
                        if(state.equals("1")){
                            updateLotnBondMachineCheckOutInfo(lotno2,device2,sysid2,machineno2);
                        }else if(state.equals("2")){
                            messgestr = "批号"+lotno2+"已经在机台"+machineno2+"出站了，不能再出站";
                            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                            return;
                        }else if(state.equals("0")){
                            messgestr = "批号"+lotno2+"在机台"+machineno2+"没有记录，请先进站";
                            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                            return;
                        }
                    }
                    break;
                case 3:
                    String result3 =  msg.obj.toString();
                    String lotno3=editText_lotno.getText().toString();
                    String machineno3=editText_machineno.getText().toString();
                    if(result3==null){
                        messgestr = "批号"+lotno3+"在机台"+machineno3+"进站出错";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        return;
                    }
                    if(result3.equals("0")){
                        messgestr = "批号"+lotno3+"在机台"+machineno3+"进站失败";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        return;
                    }
                    if(result3.equals("1")){
                        messgestr = "批号"+lotno3+"在机台"+machineno3+"进站成功";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        LotnoBondMachineActivity.this.finish();
                    }
                    break;
                case 4:
                    String result4 =  msg.obj.toString();
                    String lotno4=editText_lotno.getText().toString();
                    String machineno4=editText_machineno.getText().toString();
                    if(result4==null){
                        messgestr = "批号"+lotno4+"在机台"+machineno4+"出站出错";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        return;
                    }
                    if(result4.equals("0")){
                        messgestr = "批号"+lotno4+"在机台"+machineno4+"出站失败";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        return;
                    }
                    if(result4.equals("1")){
                        messgestr = "批号"+lotno4+"在机台"+machineno4+"出站成功";
                        DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
                        LotnoBondMachineActivity.this.finish();
                    }
                    break;
                default:
                    break;
            }
        }

        private void saveLotnBondMachineCheckinInfo(String lotno2, String device2, String sysid2, String machineno2) {
            final List<SOAPParameter> parameterList = new ArrayList<>();
            final String methodname = "saveLotnBondMachineCheckinInfo";
            parameterList.add(new SOAPParameter("lotno", lotno2));
            parameterList.add(new SOAPParameter("device", device2));
            parameterList.add(new SOAPParameter("sysid", sysid2));
            parameterList.add(new SOAPParameter("machineno", machineno2));
            MyThreadPool.httppool.execute(new Runnable() {
                @Override
                public void run() {
                    AppleSOAP asop = new AppleSOAP();
                    String result = asop.saveLotnBondMachineCheckinInfo(methodname, parameterList);
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            });
        }

        private void updateLotnBondMachineCheckOutInfo(String lotno2, String device2, String sysid2, String machineno2) {
            final List<SOAPParameter> parameterList = new ArrayList<>();
            final String methodname = "updateLotnBondMachineCheckOutInfo";
            parameterList.add(new SOAPParameter("lotno", lotno2));
            parameterList.add(new SOAPParameter("device", device2));
            parameterList.add(new SOAPParameter("sysid", sysid2));
            parameterList.add(new SOAPParameter("machineno", machineno2));
            MyThreadPool.httppool.execute(new Runnable() {
                @Override
                public void run() {
                    AppleSOAP asop = new AppleSOAP();
                    String result = asop.updateLotnBondMachineCheckOutInfo(methodname, parameterList);
                    Message msg = new Message();
                    msg.what = 4;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            });
        }

    };

    private void initView() {
        editText_lotno=findViewById(R.id.et_lotnobondmachine_lotno);
        editText_device=findViewById(R.id.et_lotnobondmachine_device);
        editText_sysid=findViewById(R.id.et_lotnobondmachine_sysid);
        editText_machineno=findViewById(R.id.et_lotnobondmachine_machineno);
        bt_scan=findViewById(R.id.bt_lotnobondmachine_scan);
    }

    public void bt_onclick_scan(View view) {
        Intent intent=new Intent(this, CaptureActivity.class);
        this.startActivityForResult(intent,0);

//        String scanResult = "M20210714-001";
//        editText_sysid.setText(scanResult);
//        getmachinenoBysysid(scanResult);
    }

    public void bt_onclick_checkin(View view) {
        clickFlag="1";
        String lotno=editText_lotno.getText().toString();
        String sysid=editText_sysid.getText().toString();
        String device=editText_device.getText().toString();
        String machineno=editText_machineno.getText().toString();

        if(lotno==null || lotno.equals("")){
            messgestr = "批号不能为空";
            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
            return;
        }
        if(sysid==null || sysid.equals("")){
            messgestr = "机种不能为空";
            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
            return;
        }
        if(device==null || device.equals("")){
            messgestr = "SYSID不能为空";
            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
            return;
        }
        if(machineno==null || machineno.equals("")){
            messgestr = "机台号不能为空";
            DialogShowUtil.dialogShow(LotnoBondMachineActivity.this, messgestr);
            return;
        }
        checkLotnoBondMachine(lotno,sysid,device);
    }

    public void bt_onclick_checkout(View view) {
        clickFlag="2";
        String lotno=editText_lotno.getText().toString();
        String sysid=editText_sysid.getText().toString();
        String device=editText_device.getText().toString();
        String machineno=editText_machineno.getText().toString();
        checkLotnoBondMachine(lotno,sysid,device);
    }

    private void checkLotnoBondMachine(String lotno,String sysid,String device) {
        final List<SOAPParameter> parameterList = new ArrayList<>();
        final String methodname = "checkLotnoBondMachine";
        parameterList.add(new SOAPParameter("lotno", lotno));
        parameterList.add(new SOAPParameter("sysid", sysid));
        parameterList.add(new SOAPParameter("device", device));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.checkLotnoBondMachine(methodname, parameterList);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            editText_sysid.setText(scanResult);
            getmachinenoBysysid(scanResult);
        }
    }

    private void getmachinenoBysysid(String sysid) {
        final List<SOAPParameter> parameterList = new ArrayList<>();
        final String methodname = "getmachinenoBysysid";
        parameterList.add(new SOAPParameter("sysid", sysid));
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop = new AppleSOAP();
                String result = asop.getmachinenoBysysid(methodname, parameterList);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }
}