package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/5.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import app.cmapp.DataTable.DataTable;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.appcdl.BaseFuncation;
import app.cmapp.appcdl.FinalStaticCloass;
import app.cmapp.appcdl.NetUtils;
import app.cmapp.appcdl.SFCBLLPack.ActivityInteractive;
import app.cmapp.appcdl.SFCBLLPack.SFCTaskVoidInterface;
import app.dpapp.R;

/**
 * Owner:F5460007
 * CreateDate:2017/4/5 15:01
 */
public class devicenomap extends ActivityInteractive {

    private Boolean _loadingstatus=false;
    private CallWebapi cwa;

    private Spinner newdeviceTypecb;
    private Spinner deviceTypecb;

    private String ipstatic;
    private String HDSerialNo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.isfc_devicenomap);

            ipstatic = NetUtils.getLocalIPAddress(this);
            SFCStaticdata.staticmember.ip=ipstatic;
            HDSerialNo = NetUtils.getAndroidOsSystemProperties("ro.boot.serialno");
            SFCStaticdata.staticmember.HDSerialNo=HDSerialNo;
            newdeviceTypecb = (Spinner) findViewById(R.id.isfc_deviceno_newdevicesp1);
            deviceTypecb = (Spinner) findViewById(R.id.isfc_deviceno_devicetypesp1);



            cwa = new CallWebapi("RI-F", "APB002", "N41CONN",this);

            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception {
                    //DataTable dt = cwa.CallRDT("getdball_new");
                    DataTable dt=new DataTable();
                    dt.AddColumn("DEVICENO");
                    dt.AddRow("ATF001");
                    dt.AddRow("APG002");
                    dt.AddRow("APG003");
                    dt.AddRow("A3G001");
                    if (dt.Rowscount() > 0) {
                        setAdapter(newdeviceTypecb,BaseFuncation.setvalue(dt,null,"deviceno",devicenomap.this));
                        //setSelection(newdeviceTypecb,1);
                    }
                   //newdeviceTypecb_SelectedValueChanged();
                    _loadingstatus = true;
                }
            }, "DevicenoMap Loading Task");

            newdeviceTypecb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                                try {
                                    if (_loadingstatus) {
                                    ExecTask(new SFCTaskVoidInterface() {
                                        @Override
                                        public void taskvoid(Object valueo) throws Exception {
                                            newdeviceTypecb_SelectedValueChanged();
                                        }
                                    }, "select Task");
                                    }
                                } catch (Exception ex) {
                                    BaseFuncation.showmessage(devicenomap.this, "DevicenoMap select Task Error:" + ex.getMessage());
                                }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
            );
        }
        catch(Exception ex)
        {
            BaseFuncation.showmessage(this,"DevicenoMap Loading Task Error:"+ex.getMessage());
            this.finish();
        }
    }

    private void newdeviceTypecb_SelectedValueChanged() throws Exception
    {
        String newdeviceTypecbText=((FinalStaticCloass.SpinnerData)getSelectedItem(newdeviceTypecb)).getText().toString().trim();
        DataTable dt = cwa.CallRDT("getdball", newdeviceTypecbText);

        if (dt.Rowscount() > 0)
        {
            setAdapter(deviceTypecb,BaseFuncation.setvalue(dt,null,"devicetype",devicenomap.this));
            setSelection(deviceTypecb,0);
        }

        deviceTypecb_SelectedIndexChanged();
    }

    private void deviceTypecb_SelectedIndexChanged() throws Exception
    {
        String device =((FinalStaticCloass.SpinnerData)getSelectedItem(deviceTypecb)).getText().toString().trim();
        String newdevice = ((FinalStaticCloass.SpinnerData)getSelectedItem(newdeviceTypecb)).getText().toString().trim();
        DataTable dtdbinfo = cwa.CallRDT("getdbnameall", device);
        if (dtdbinfo == null || dtdbinfo.Rowscount() == 0)
        {
            return;
        }
        SFCStaticdata.staticmember.devicetype = device;//dtdbinfo.Rows(0).get_CellValue("devicetype").toString().trim();
        SFCStaticdata.staticmember.fifoflag = dtdbinfo.Rows(0).get_CellValue("att12").toString().trim();
        SFCStaticdata.staticmember.qcholddefalg = dtdbinfo.Rows(0).get_CellValue("att11").toString().trim();
        SFCStaticdata.staticmember.deviceno = device;
        SFCStaticdata.staticmember.newdeviceno = newdevice;
        SFCStaticdata.staticmember.eolspitflag = dtdbinfo.Rows(0).get_CellValue("att6").toString().trim();
        SFCStaticdata.staticmember.defectflag = dtdbinfo.Rows(0).get_CellValue("att10").toString().trim();
        SFCStaticdata.staticmember.odbname = dtdbinfo.Rows(0).get_CellValue("dbname").toString().trim();
        String att4item = dtdbinfo.Rows(0).get_CellValue("att4").toString().trim();


        if (att4item== "1")//RI+系列
        {
            SFCStaticdata.staticmember.englotcheckflag = true;
            SFCStaticdata.staticmember.devicenomacinfo = false;
            SFCStaticdata.staticmember.engRilotcheckflag = false;
        }
        else if (att4item == "2")//J系列
        {
            SFCStaticdata.staticmember.englotcheckflag = false;
            SFCStaticdata.staticmember.devicenomacinfo = true;
            SFCStaticdata.staticmember.engRilotcheckflag = false;
        }
        else if (att4item == "0")//RI系列
        {
            SFCStaticdata.staticmember.engRilotcheckflag = true;//RI機種？
            SFCStaticdata.staticmember.englotcheckflag = false;//RI+新機種？
            SFCStaticdata.staticmember.devicenomacinfo = false;//Mac機種  J系列
        }
        else
        {
            SFCStaticdata.staticmember.englotcheckflag = false;
            SFCStaticdata.staticmember.devicenomacinfo = false;
            SFCStaticdata.staticmember.engRilotcheckflag = false;
        }
        SFCStaticdata.staticmember.sdbname = "SFCLHWIP";
    }

    public void button1_Click(View v) {
        try {
            ExecTask(new SFCTaskVoidInterface() {
                @Override
                public void taskvoid(Object valueo) throws Exception{
                    submitbutton_Click();
                }
            }, "devicenomap Submit Task");
        } catch (Exception ex) {
            BaseFuncation.showmessage(this, "devicenomap Submit Task Error:" + ex.getMessage());
        }
    }

    public void submitbutton_Click() {
        try {

            if (checkipaddress(HDSerialNo))//if(true)   //
            {
                /*String deviceTypecb = newdeviceTypecb.getSelectedItem().toString();//((FinalStaticCloass.SpinnerData)getSelectedItem(newdeviceTypecb)).getText().toString().trim();
                DataTable dtnewdevice =cwa.CallRDT("getdball", deviceTypecb);
                String newdeviceTypecb ="";

                if (dtnewdevice.Rowscount()>0)
                {
                    newdeviceTypecb=dtnewdevice.Rows(0).get_CellValue(0).toString();
                }*/

                Intent PublicLoading = new Intent(devicenomap.this, app.cmapp.IncludingSFC.sfconline.class);
                //Bundle bundler = new Bundle();
                //bundler.putString("deviceno", SFCStaticdata.staticmember.deviceno);
                // bundler.putString("newdeviceno", SFCStaticdata.staticmember.newdeviceno);
                //PublicLoading.putExtras(bundler);
                //Intent PublicLoading = new Intent(devicenomap.this, app.cmapp.IncludingSFC.checkin.class);
                startActivity(PublicLoading);

            }
            else
            {
                //Toast.makeText(this, "IP無權限進入此頁面﹐請聯系MIS部門", Toast.LENGTH_SHORT).show();
                ShowMessage("平板"+HDSerialNo+"無權限進入此頁面﹐請聯系MIS部門");
                return;
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private Boolean checkipaddress(String ipaddressstr)
    {
        try {
            if (SFCStaticdata.staticmember.odbname == null) {
                Toast.makeText(this, "數據庫不能為null", Toast.LENGTH_SHORT).show();
                return false;
            }
            cwa = new CallWebapi(SFCStaticdata.staticmember.devicetype,SFCStaticdata.staticmember.newdeviceno, SFCStaticdata.staticmember.odbname,this);
            String checkip =cwa.CallRS("devicenomap_submit_1",ipaddressstr);//devicenomap_submit_1
            //String checkip = "L2523029N1U2";
            if (checkip != null) {
                if (checkip.equals(ipaddressstr)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage() + SFCStaticdata.staticmember.odbname.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
