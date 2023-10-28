package app.dpapp.OtherForm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import app.dpapp.Interface.Iobjectrhandler;
import app.dpapp.R;
import app.dpapp.Staticdata;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.Exectempfile;
import app.dpapp.appcdl.FinalStaticCloass;
import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.FreedomSOAPCallBack;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.soap.PublicSOAP;
import app.dpapp.appcdl.exechttprequest;
import app.dpapp.appcdl.execloadactivity;
import app.dpapp.appcdl.jsontolist;
import app.dpapp.parameterclass.httprequestinputdata;

/**
 * Created by Tod on 2016/4/13.
 * By 点检资料录入
 */
public class jcwaferiqcinspectionreport extends AppCompatActivity  implements Iobjectrhandler,FreedomSOAPCallBack
{
    //用户名
    private static String Sessionuser;
    //填充显示等待点检的ListView数据源
    private static List<String[]> Dl2;

    private static String Checkdataid;
    /*
     * Header定義區
     */
    /**
     * 機種名稱
     */
    private String _deviceno;
    private Spinner _devicetv;
    /**
     * typeWafer   型號（更具機種帶出pvalue5）
     */
    private String _typewafer;
    private TextView _typewafertv;
    /**
     * Vendor   供應商名稱（更具機種帶出pvalue4）
     */
    private String _vendor;
    private TextView _vendortv;
    /**
     * Inspection   檢驗日期
     */
    private String _inspectiondate;
    private TextView _inspectiondatetv;
    /**
     * 班別 Spinner
     * 0:百班
     * 1:夜班
     */
    private String _shifttype;
    private Spinner _shifttypesp;
    /**
     *line 生產線別
     */
    private String _line;
    private TextView _linetv;
    /**
     * Stage 生產階段
     */
    private String _stage;
    private Spinner _stagetv;
    /**
     *MO  工單
     */
    private String _mo;
    private TextView _motv;
    /**
     * Cardid  流程卡號
     */
    private String _cardid;
    private TextView _cardidtv;
    /**
     * TotalWafer     Wafer總 片數
     */
    private String _totalwafer;
    private TextView _totalwafertv;
    /**
     * TotalSensor       下料Sensor 總數量
     */
    private String _totalsensor;
    private TextView _totalsensortv;
    /**
     * ActualInput          實際投線數量
     */
    private String _actualinput;
    private TextView _actualinputtv;
    /**
     * Return   應退庫數量
     */
    private String _returnqty;
    private TextView _returnqtytv;
    /**
     * Market    市場
     */
    private String _markettype;
    private Spinner _markettypesp;
    /**
     * Drawing   線圖編碼
     */
    private String _drawingno;
    private TextView _drawingnotv;

    /*
    CCD檢驗錄定義區
     */
    /**
     * WaferLot1 晶圓批號1
     */
    private String _cwaferlotno1;
    private TextView _cwaferlotno1tv;
    /**
     *WaferLot2 晶圓批號2
     */
    private String _cwaferlotno2;
    private TextView _cwaferlotno2tv;
    /**
 * WaferRing1      Wafer鐵環刻號1
 */
private String _cwaferring1;
    private TextView _cwaferring1tv;
    /**
     * att1     Wafer鐵環刻號2
     */
    private String _cwaferring2;
    private TextView _cwaferring2tv;
    /**
     * att1     Wafer鐵環刻號3
     */
    private String _cwaferring3;
    private TextView _cwaferring3tv;
    /**
     * att1     Wafer鐵環刻號4
     */
    private String _cwaferring4;
    private TextView _cwaferring4tv;
    /**
     *qty1c抽檢數量1
     */
    private String _cqty1;
    private TextView _cqty1tv;
    /**
     *qty1c抽檢數量2
     */
    private String _cqty2;
    private TextView _cqty2tv;
    /**
     *qty1c抽檢數量3
     */
    private String _cqty3;
    private TextView _cqty3tv;
    /**
     *qty1c抽檢數量4
     */
    private String _cqty4;
    private TextView _cqty4tv;
    /**
     *DefectQty1   不良數量1
     */
    private String _cdefectqty1;
    private TextView _cdefectqty1tv;
    /**
     *DefectQty2   不良數量2
     */
    private String _cdefectqty2;
    private TextView _cdefectqty2tv;
    /**
     *DefectQty3   不良數量3
     */
    private String _cdefectqty3;
    private TextView _cdefectqty3tv;
    /**
     *DefectQty4   不良數量1
     */
    private String _cdefectqty4;
    private TextView _cdefectqty4tv;
    /**
     *Totalqty1    總抽檢數量
     */
    private String _ctotalqty;
    /**
     *  TotalDefectQty1     總抽檢不良數量
     */
    private String _ctotaldefectqty;
    /**
     * mode1   不良模式1
     */
    private String _cmode1;
    private Spinner _cmode1sp;
    /**
     * mode2   不良模式2
     */
    private String _cmode2;
    private Spinner _cmode2sp;
    /**
     * mode3   不良模式3
     */
    private String _cmode3;
    private Spinner _cmode3sp;
    /**
     * mode4   不良模式4
     */
    private String _cmode4;
    private Spinner _cmode4sp;
    /**
     * yield1   不良率1
     */
    private String _cyield1;
    /**
     * yield2   不良率2
     */
    private String _cyield2;
    /**
     * yield3   不良率3
     */
    private String _cyield3;
    /**
     * yield4  不良率4
     */
    private String _cyield4;
    /**
     * Totalyield1   總不良率
     */
    private String _ctotalyield1;
    /**
     * beizhu
     */
    private String _cbeizhu;
    private TextView _cbeizhutv;

    /*
   高倍檢驗輸入區
   */
    /**
     * Waferlot1
     */
    private String _hwaferlotno1;
    private TextView _hwaferlotno2tv;
    /**
     * Waferlot2
     */
    private String _hwaferlotno2;
    private TextView _hwaferlotno1tv;
   /**
    * WaferRing1      hWafer鐵環刻號1
    */
    private String _hwaferring1;
    private TextView _hwaferring1tv;
    /**
     * att1     hWafer鐵環刻號2
     */
    private String _hwaferring2;
    private TextView _hwaferring2tv;
    /**
     * att1     hWafer鐵環刻號3
     */
    private String _hwaferring3;
    private TextView _hwaferring3tv;
    /**
     * att1     hWafer鐵環刻號4
     */
    private String _hwaferring4;
    private TextView _hwaferring4tv;
    /**
     *qty1h抽檢數量1
     */
    private String _hqty1;
    private TextView _hqty1tv;
    /**
     *qty1h抽檢數量2
     */
    private String _hqty2;
    private TextView _hqty2tv;
    /**
     *qty1h抽檢數量3
     */
    private String _hqty3;
    private TextView _hqty3tv;
    /**
     *qty1h抽檢數量4
     */
    private String _hqty4;
    private TextView _hqty4tv;
    /**
     *DefectQty1   h不良數量1
     */
    private String _hdefectqty1;
    private TextView _hdefectqty1tv;
    /**
     *DefectQty2   h不良數量2
     */
    private String _hdefectqty2;
    private TextView _hdefectqty2tv;
    /**
     *DefectQty3   h不良數量3
     */
    private String _hdefectqty3;
    private TextView _hdefectqty3tv;
    /**
     *DefectQty4   h不良數量1
     */
    private String _hdefectqty4;
    private TextView _hdefectqty4tv;
    /**
     *Totalqty1    h總抽檢數量
     */
    private String _htotalqty;
    /**
     *  TotalDefectQty1     h總抽檢數量
     */
    private String _htotaldefectqty;
    /**
     * mode1   h不良模式1
     */
    private String _hmode1;
    private Spinner _hmode1sp;
    /**
     * mode2   h不良模式2
     */
    private String _hmode2;
    private Spinner _hmode2sp;
    /**
     * mode3   h不良模式3
     */
    private String _hmode3;
    private Spinner _hmode3sp;
    /**
     * mode4   h不良模式4
     */
    private String _hmode4;
    private Spinner _hmode4sp;
    /**
     * yield1   h不良率1
     */
    private String _hyield1;
    /**
     * yield2   h不良率2
     */
    private String _hyield2;
    /**
     * yield3   h不良率3
     */
    private String _hyield3;
    /**
     * yield4  h不良率4
     */
    private String _hyield4;
    /**
     * Totalyield1   h總不良率
     */
    private String _htotalyield1;

    /*
    Die厚度量測區
     */
    /**
     *DWaferLot     Die 厚度量測晶圓批號
     */
    private String _dwaferlotno;
    private TextView _dwaferlotnotv;
    /**
     * DWaferRing     Wafer鐵環刻號
     */
    private String _dwafering;
    private TextView _dwaferingtv;
    /**
     *Measurements    量測數據S
     */
    private String _measurements;
    private TextView _measurementstv;
    /**
     *Measurementx    量測數據x
     */
    private String _measurementx;
    private TextView _measurementxtv;
    /**
     *Measurementz    量測數據z
     */
    private String _measurementz;
    private TextView _measurementztv;
    /**
     *Measurementy    量測數據y
     */
    private String _measurementy;
    private TextView _measurementytv;
    /**
     *Measurementc    量測數據c
     */
    private String _measurementc;
    private TextView _measurementctv;
    /**
     *Die 厚度量測
     */
    private String _diethickness;
    private TextView _diethicknesstv;
    /**
     *Spec   Die厚規格（pvalue7）
     */
    private String _diethicknessspec;
    private TextView _diethicknessspectv;
    /**
     *  Judgement   判定狀況
     */
    private String _judgement;
    private Spinner _judgementtv;
    /**
     *   QCInspector  QC檢驗員工號
     */
    private String _qcinsepector;
    private TextView _qcinsepectortv;
    /**
     * Checked  查核
     */
    private String _Checked;
    private TextView _Checkedtv;
    /**
     *Remark  dt 備註
     */
    private String _rremark;
    private TextView _rremarktv;

    private TextView _viewtypeccd;
    private TextView _viewtypehs;
    private TextView _viewtypedt;
    private TextView _viewtyperd;

    private RelativeLayout _rlccd;
    private RelativeLayout _rlhs;
    private RelativeLayout _rldt;
    private RelativeLayout _rlrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jcwaferiqcinspectionreport);

        final Staticdata app = (Staticdata)getApplication();
        Sessionuser = app.getLoginUserID();
        Checkdataid="jcwafer_inspection"+Sessionuser;
        BaseFuncation bf=new BaseFuncation();

        _devicetv=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_devicenosp);
        _typewafertv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_wafertypetv);
        _vendortv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_vendortv);
        _shifttypesp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_shfttypesp);
         BaseFuncation.createspdata bfc=bf.new createspdata();
         bfc.setvalue("1","D");
         bfc.setvalue("2", "N");
         _shifttypesp.setAdapter(bfc.getspdata(this));
        _linetv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_linetv);
        _stagetv=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_stagetv);
        BaseFuncation.createspdata bfc2=bf.new createspdata();
        bfc2.setvalue("1","工程批 Eng");
        bfc2.setvalue("2", "量產批 MP");
        _stagetv.setAdapter(bfc2.getspdata(this));
        _motv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_motv);
        _cardidtv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cardidtv);
        _totalwafertv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_totalwafertv);
        _totalsensortv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_totalsensortv);
        _actualinputtv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_actualinputtv);
        _returnqtytv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_returnqtytv);
        _returnqtytv.setEnabled(false);
        _markettypesp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_markettypesp);
        BaseFuncation.createspdata bfc3=bf.new createspdata();
        bfc3.setvalue("1","國內市場");
        bfc3.setvalue("2", "國際市場");
        _markettypesp.setAdapter(bfc3.getspdata(this));
        _drawingnotv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_drawingnotv);

        BaseFuncation.createspdata bfc4=bf.new createspdata();
        bfc4.setvalue("0","（為空）");
        bfc4.setvalue("1","刮傷Scratch");
        bfc4.setvalue("2","污染Contamination");
        bfc4.setvalue("3","像素點破損Pixel damage");
        bfc4.setvalue("4"," Pad異常Padissue");
        bfc4.setvalue("5","Die邊緣異常Die edge issue");
        bfc4.setvalue("6","其他Others");
        ArrayAdapter<FinalStaticCloass.SpinnerData> as=bfc4.getspdata(this);

        _cwaferlotno1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cwaferlotno1tv);
        _cwaferlotno2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cwaferlotno2tv);
        _cwaferring1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cwaferring1tv);
        _cwaferring2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cwaferring2tv);
        _cwaferring3tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cwaferring3tv);
        _cwaferring4tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cwaferring4tv);
        _cqty1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cqty1tv);
        _cqty2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cqty2tv);
        _cqty3tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cqty3tv);
        _cqty4tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cqty4tv);
        _cdefectqty1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cdefectqty1tv);
        _cdefectqty2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cdefectqty2tv);
        _cdefectqty3tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cdefectqty3tv);
        _cdefectqty4tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cdefectqty4tv);
        _cmode1sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cmodel1sp);
        _cmode1sp.setAdapter(as);
        _cmode2sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cmodel2sp);
        _cmode2sp.setAdapter(as);
        _cmode3sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cmodel3sp);
        _cmode3sp.setAdapter(as);
        _cmode4sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cmodel4sp);
        _cmode4sp.setAdapter(as);
        _cbeizhutv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_cbeizhutv);

        _hwaferlotno1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hwaferlotno1tv);
        _hwaferlotno2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hwaferlotno2tv);
        _hwaferring1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hwaferring1tv);
        _hwaferring2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hwaferring2tv);
        _hwaferring3tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hwaferring3tv);
        _hwaferring4tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hwaferring4tv);
        _hqty1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hqty1tv);
        _hqty2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hqty2tv);
        _hqty3tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hqty3tv);
        _hqty4tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hqty4tv);
        _hdefectqty1tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hdefectqty1tv);
        _hdefectqty2tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hdefectqty2tv);
        _hdefectqty3tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hdefectqty3tv);
        _hdefectqty4tv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hdefectqty4tv);
        _hmode1sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hmodel1sp);
        _hmode1sp.setAdapter(as);
        _hmode2sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hmodel2sp);
        _hmode2sp.setAdapter(as);
        _hmode3sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hmodel3sp);
        _hmode3sp.setAdapter(as);
        _hmode4sp=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_hmodel4sp);
        _hmode4sp.setAdapter(as);

        _dwaferlotnotv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dwaferlotno1tv);
        _dwaferingtv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dwaferring1tv);
        _measurementstv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dmeasurementstv);
        _measurementxtv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dmeasurementxtv);
        _measurementztv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dmeasurementztv);
        _measurementytv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dmeasurementytv);
        _measurementctv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dmeasurementctv);
        _diethicknessspectv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_ddieticknessspectv);

        _judgementtv=(Spinner)findViewById(R.id.otherFormjcwaferiqcinspectionreport_judgmentsp);
        BaseFuncation.createspdata bfc5=bf.new createspdata();
        bfc5.setvalue("1","允許 Acc");
        bfc5.setvalue("2", "叛退 Rej");
        _judgementtv.setAdapter(bfc5.getspdata(this));

        _qcinsepectortv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_qcinsepectortv);
        _Checkedtv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_checkedtv);
        _rremarktv=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_rremarktv);

        _viewtypeccd=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_viewtypeccdlabel);
        _viewtypehs=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_viewtypehslabel);
        _viewtypedt=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_viewtypedtlabel);
        _viewtyperd=(TextView)findViewById(R.id.otherFormjcwaferiqcinspectionreport_viewtyperclabel);

        _rlccd=(RelativeLayout)findViewById(R.id.otherFormjcwaferiqcinspectionreport_ccddata);
        _rlhs=(RelativeLayout)findViewById(R.id.otherFormjcwaferiqcinspectionreport_msdata);
        _rldt=(RelativeLayout)findViewById(R.id.otherFormjcwaferiqcinspectionreport_dtdata);
        _rlrd=(RelativeLayout)findViewById(R.id.otherFormjcwaferiqcinspectionreport_rdata);
        changeviewtype(0);
        loadingdata(0, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getsoapdata(int stype, String METHOD_NAME, String[] parnames, String[] parvalues, int parcount)
    {
        execloadactivity.opendialog(this,"正在執行");
        PublicSOAP p=new PublicSOAP();
        p.getsopdata(
                new FreedomSOAPCallBack() {
                    @Override
                    public void BackData(Object so, Context cc, int type) {
                        filldata(so, type);
                    }
                }, this, stype, METHOD_NAME, parnames, parvalues, parcount
        );
    }
    private void filldata(Object o,int ctype)
    {
        execloadactivity.canclediglog();
        if(o==null) {
            Toast.makeText(this, String.valueOf(ctype) +":獲取資料失敗，獲取資料為Null", Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject so=null;
        try {
            so = (SoapObject) o;
        }
        catch(Exception ex1)
        {
            Toast.makeText(this, String.valueOf(ctype) +":獲取資料失敗，非標準SOAP格式", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (ctype)
        {
            //Get Deviceno data
            case 0:
                // TODO: 2016/12/15
                getdevicedata(so);
                break;
            //Get Other Data by Deviceno;
            case 1:
                getotherdatabydeviceno(so);
                break;
            case 2:
                submitdata(so);
                break;
        }
    }

    public void typeview_1(View v)
    {
        changeviewtype(0);
    }
    public void typeview_2(View v)
    {
        changeviewtype(1);
    }
    public void typeview_3(View v)
    {
        changeviewtype(2);
    }
    public void typeview_4(View v)
    {
        changeviewtype(3);
    }
    public void changeviewtype(int v)
    {
        switch (v) {
            case 0:
                _viewtypeccd.setBackgroundColor(Color.parseColor("#bcec54"));
                _viewtypehs.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtypedt.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtyperd.setBackgroundColor(Color.parseColor("#b2c469"));
                _rlccd.setVisibility(View.VISIBLE);
                _rlhs.setVisibility(View.INVISIBLE);
                _rldt.setVisibility(View.INVISIBLE);
                _rlrd.setVisibility(View.INVISIBLE);
                break;
            case 1:
                _viewtypeccd.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtypehs.setBackgroundColor(Color.parseColor("#bcec54"));
                _viewtypedt.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtyperd.setBackgroundColor(Color.parseColor("#b2c469"));
                _rlccd.setVisibility(View.INVISIBLE);
                _rlhs.setVisibility(View.VISIBLE);
                _rldt.setVisibility(View.INVISIBLE);
                _rlrd.setVisibility(View.INVISIBLE);
                break;
            case 2:
                _viewtypeccd.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtypehs.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtypedt.setBackgroundColor(Color.parseColor("#bcec54"));
                _viewtyperd.setBackgroundColor(Color.parseColor("#b2c469"));
                _rlccd.setVisibility(View.INVISIBLE);
                _rlhs.setVisibility(View.INVISIBLE);
                _rldt.setVisibility(View.VISIBLE);
                _rlrd.setVisibility(View.INVISIBLE);
                break;
            case 3:
                _viewtypeccd.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtypehs.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtypedt.setBackgroundColor(Color.parseColor("#b2c469"));
                _viewtyperd.setBackgroundColor(Color.parseColor("#bcec54"));
                _rlccd.setVisibility(View.INVISIBLE);
                _rlhs.setVisibility(View.INVISIBLE);
                _rldt.setVisibility(View.INVISIBLE);
                _rlrd.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getotherdatabydeviceno(SoapObject so) {
        SoapObject soapchild=null;
        try {
            soapchild = (SoapObject) ((SoapObject) ((SoapObject) so.getProperty(0)).getProperty(1)).getProperty(0);
        }
        catch(Exception e)
        {
            soapchild=null;
        }

        try {
            _vendortv.setText(((SoapObject) soapchild.getProperty(0)).getProperty("VENDOR").toString());
            _typewafertv.setText(((SoapObject) soapchild.getProperty(0)).getProperty("TYPEWAFER").toString());
            _diethicknessspectv.setText(((SoapObject) soapchild.getProperty(0)).getProperty("SPEC").toString());
        }
        catch(Exception e) {
            Toast.makeText(this,"獲取其它Loading信息失敗", Toast.LENGTH_SHORT).show();
        }

        File file = new File("/storage/emulated/0/CMSF/Tempfile/machinecheck/"+Checkdataid+".txt");
        if(file.exists()) {
            loadtempdata();
        }

    }

    private void submitdata(SoapObject so) {
        SoapObject soapchild=null;

        try {
            String a = so.getProperty(0).toString();
            BaseFuncation.rrtype rt=new BaseFuncation().new rrtype();
            //rt=Exectempfile.instance().removefile(Checkdataid);
            if (a.equals("1")) {
                rt=Exectempfile.instance().removefile(Checkdataid);
                Toast.makeText(this,"提交成功", Toast.LENGTH_SHORT).show();
                this.finish();
            }
            else if(a.equals("ERS001"))
            {
                Toast.makeText(this,"提交失敗,日期格式錯誤,請檢查后重新提交-"+a, Toast.LENGTH_SHORT).show();

            }
            else if(a.equals("ERS002"))
            {
                Toast.makeText(this,"提交失敗,必填寫項目為空,請檢查后重新提交-"+a, Toast.LENGTH_SHORT).show();

            }
            else if(a.equals("ERS003"))
            {
                Toast.makeText(this,"提交失敗,數字格式錯誤,請檢查后重新提交-"+a, Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this,"提交失敗,請檢查后重新提交-"+a, Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e) {
            Toast.makeText(this,"提交失敗-"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getdevicedata( SoapObject so)
    {
        SoapObject soapchild=null;
        try {
            soapchild = (SoapObject) ((SoapObject) ((SoapObject) so.getProperty(0)).getProperty(1)).getProperty(0);
        }
        catch(Exception e)
        {
            Toast.makeText(this,"獲取機種資料失敗", Toast.LENGTH_SHORT).show();
            return;
        }

        List<FinalStaticCloass.SpinnerData> lf1 = new ArrayList<FinalStaticCloass.SpinnerData>();
        String v=null;
        for (int i = 0; i < soapchild.getPropertyCount(); i++) {
             v= null;
            try
            {
                v = ((SoapObject) soapchild.getProperty(i)).getProperty("PVALUE3").toString();
            }
            catch(Exception e)
            {
                v="ErrorDevice"+String.valueOf(i);
            }
            FinalStaticCloass s = new FinalStaticCloass();
            FinalStaticCloass.SpinnerData ss = s.new SpinnerData(v,v);
            lf1.add(ss);
        }
        ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                new ArrayAdapter<FinalStaticCloass.SpinnerData>(jcwaferiqcinspectionreport.this, android.R.layout.simple_dropdown_item_1line,lf1);
        as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _devicetv.setAdapter((as1));

        _devicetv.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        loadingdata(1, ((FinalStaticCloass.SpinnerData) _devicetv.getSelectedItem()).getValue());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        if(_devicetv.getCount()>0)
        {
            _devicetv.setSelection(0);
            //loadingdata(1, ((FinalStaticCloass.SpinnerData) _devicetv.getSelectedItem()).getValue());
        }
    }

    private class rcheckpagedata
    {
        public boolean rstatus;
        public String[] rsarray;
    }
    private rcheckpagedata checkpagedataintegrality()
    {
        boolean s=true;
        _deviceno= ((FinalStaticCloass.SpinnerData)_devicetv.getSelectedItem()).getValue();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_deviceno)) {
            checkviewboder(_devicetv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_devicetv, Color.parseColor("#a0a5ba"));
        }

        _typewafer=_typewafertv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_typewafer)) {
            checkviewboder(_typewafertv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_typewafertv, Color.WHITE);
        }

        _vendor=_vendortv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_vendor)) {
            checkviewboder(_vendortv, Color.RED);
            s = false;
        }
        else{
            checkviewboder(_vendortv,Color.WHITE);
        }
        _shifttype= ((FinalStaticCloass.SpinnerData)_shifttypesp.getSelectedItem()).getValue();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_shifttype)) {
            checkviewboder(_shifttypesp, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_shifttypesp, Color.parseColor("#a0a5ba"));
        }
        _line=_linetv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_line)) {
            checkviewboder(_linetv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_linetv, Color.WHITE);
        }
        _stage= ((FinalStaticCloass.SpinnerData)_stagetv.getSelectedItem()).getValue();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_stage)) {
            checkviewboder(_stagetv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_stagetv, Color.parseColor("#a0a5ba"));
        }

        _mo=_motv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_mo)) {
            checkviewboder(_motv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_motv, Color.WHITE);
        }
        _cardid=_cardidtv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_cardid)) {
            checkviewboder(_cardidtv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_cardidtv, Color.WHITE);
        }
        _totalwafer=_totalwafertv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_totalwafer)) {
            checkviewboder(_totalwafertv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_totalwafertv, Color.WHITE);
        }
        _totalsensor=_totalsensortv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_totalsensor)) {
            checkviewboder(_totalsensortv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_totalsensortv, Color.WHITE);
        }
        _actualinput=_actualinputtv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_actualinput)) {
            checkviewboder(_actualinputtv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_actualinputtv, Color.WHITE);
        }
        _markettype=((FinalStaticCloass.SpinnerData)_markettypesp.getSelectedItem()).getValue();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_markettype)) {
            checkviewboder(_markettypesp, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_markettypesp, Color.parseColor("#a0a5ba"));
        }
        _drawingno=_drawingnotv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_drawingno)) {
            checkviewboder(_drawingnotv, Color.RED);
            s = false;
        }
        else {
            checkviewboder(_drawingnotv, Color.WHITE);
        }
        _cwaferlotno1=_cwaferlotno1tv.getText().toString();
        _cwaferlotno2=_cwaferlotno2tv.getText().toString();
        _cwaferring1=_cwaferring1tv.getText().toString();
        _cwaferring3=_cwaferring3tv.getText().toString();
        _cqty1=_cqty1tv.getText().toString();
        _cqty2=_cqty2tv.getText().toString();
        _cqty3=_cqty3tv.getText().toString();
        _cqty4=_cqty4tv.getText().toString();
        _cdefectqty1=_cdefectqty1tv.getText().toString();
        _cdefectqty2=_cdefectqty2tv.getText().toString();
        _cdefectqty3=_cdefectqty3tv.getText().toString();
        _cdefectqty4=_cdefectqty4tv.getText().toString();
        _cmode1=((FinalStaticCloass.SpinnerData)_cmode1sp.getSelectedItem()).getValue();
        _cmode2=((FinalStaticCloass.SpinnerData)_cmode2sp.getSelectedItem()).getValue();
        _cmode3=((FinalStaticCloass.SpinnerData)_cmode3sp.getSelectedItem()).getValue();
        _cmode4=((FinalStaticCloass.SpinnerData)_cmode4sp.getSelectedItem()).getValue();
        _cbeizhu=_cbeizhutv.getText().toString();
        _hwaferlotno1=_hwaferlotno1tv.getText().toString();
        _hwaferlotno2=_hwaferlotno2tv.getText().toString();
        _hwaferring1=_hwaferring1tv.getText().toString();
        _hwaferring3=_hwaferring3tv.getText().toString();
        _hqty1=_hqty1tv.getText().toString();
        _hqty2=_hqty2tv.getText().toString();
        _hqty3=_hqty3tv.getText().toString();
        _hqty4=_hqty4tv.getText().toString();
        _hdefectqty1=_hdefectqty1tv.getText().toString();
        _hdefectqty2=_hdefectqty2tv.getText().toString();
        _hdefectqty3=_hdefectqty3tv.getText().toString();
        _hdefectqty4=_hdefectqty4tv.getText().toString();
        _hmode1=((FinalStaticCloass.SpinnerData)_hmode1sp.getSelectedItem()).getValue();
        _hmode2=((FinalStaticCloass.SpinnerData)_hmode2sp.getSelectedItem()).getValue();
        _hmode3=((FinalStaticCloass.SpinnerData)_hmode3sp.getSelectedItem()).getValue();
        _hmode4=((FinalStaticCloass.SpinnerData)_hmode4sp.getSelectedItem()).getValue();
        _dwaferlotno=_dwaferlotnotv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_dwaferlotno)) {
            checkviewboder(_dwaferlotnotv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_dwaferlotnotv,Color.WHITE);
        _dwafering=_dwaferingtv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_dwafering)) {
            checkviewboder(_dwaferingtv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_dwaferingtv,Color.WHITE);
        _measurements=_measurementstv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_measurements)) {
            checkviewboder(_measurementstv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementstv,Color.WHITE);
        _measurementx=_measurementxtv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_measurementx)) {
            checkviewboder(_measurementxtv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementxtv,Color.WHITE);
        _measurementz=_measurementztv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_measurementz)) {
            checkviewboder(_measurementztv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementztv,Color.WHITE);
        _measurementy=_measurementytv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_measurementy)) {
            checkviewboder(_measurementytv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementytv,Color.WHITE);
        _measurementc=_measurementctv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_measurementc)) {
            checkviewboder(_measurementctv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementctv,Color.WHITE);
        _diethicknessspec=_diethicknessspectv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_diethicknessspec)) {
            checkviewboder(_diethicknessspectv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_diethicknessspectv,Color.WHITE);
        if(!checkint(_measurements,_diethicknessspec)){
            checkviewboder(_measurementstv,Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementstv,Color.WHITE);
        if(!checkint(_measurementx,_diethicknessspec)) {
            checkviewboder(_measurementxtv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementxtv,Color.WHITE);
        if(!checkint(_measurementz,_diethicknessspec)) {
            checkviewboder(_measurementztv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementztv,Color.WHITE);
        if(!checkint(_measurementy,_diethicknessspec)) {
            checkviewboder(_measurementytv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementytv,Color.WHITE);
        if(!checkint(_measurementc,_diethicknessspec)) {
            checkviewboder(_measurementctv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_measurementctv,Color.WHITE);

        _judgement=((FinalStaticCloass.SpinnerData)_judgementtv.getSelectedItem()).getValue();
        _qcinsepector=_qcinsepectortv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_qcinsepector)) {
            checkviewboder(_qcinsepectortv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_qcinsepectortv,Color.WHITE);
        _Checked=_Checkedtv.getText().toString();
        if(!BaseFuncation.Basecheck.checkstringnullandlength(_Checked)) {
            checkviewboder(_Checkedtv, Color.RED);
            s = false;
        }
        else
            checkviewboder(_Checkedtv,Color.WHITE);
        _rremark=_rremarktv.getText().toString();

        String loginuserid=Sessionuser;
        _cwaferring2=_cwaferring2tv.getText().toString();
        _cwaferring4=_cwaferring4tv.getText().toString();
        _hwaferring2=_hwaferring2tv.getText().toString();
        _hwaferring4=_hwaferring4tv.getText().toString();

        if(!checkstring_1(_cwaferlotno1,_cwaferring1,_cqty1,_cdefectqty1,_cmode1,_cwaferring2,_cqty2,_cdefectqty2,_cmode2) &&
                !checkstring_1(_cwaferlotno2,_cwaferring3,_cqty3,_cdefectqty3,_cmode3,_cwaferring4,_cqty4,_cdefectqty4,_cmode4)) {
            checkviewboder(_cwaferlotno1tv,Color.RED);
            checkviewboder(_cwaferlotno2tv,Color.RED);
            checkviewboder(_cwaferring1tv,Color.RED);
            checkviewboder(_cwaferring2tv,Color.RED);
            checkviewboder(_cwaferring3tv,Color.RED);
            checkviewboder(_cwaferring4tv,Color.RED);
            checkviewboder(_cqty1tv,Color.RED);
            checkviewboder(_cqty2tv,Color.RED);
            checkviewboder(_cqty3tv,Color.RED);
            checkviewboder(_cqty4tv,Color.RED);
            checkviewboder(_cmode1sp,Color.RED);
            checkviewboder(_cmode2sp,Color.RED);
            checkviewboder(_cmode3sp,Color.RED);
            checkviewboder(_cmode4sp,Color.RED);
            checkviewboder(_cdefectqty1tv,Color.RED);
            checkviewboder(_cdefectqty2tv,Color.RED);
            checkviewboder(_cdefectqty3tv,Color.RED);
            checkviewboder(_cdefectqty4tv,Color.RED);
            s = false;
        } else{
            checkviewboder(_cwaferlotno1tv,Color.WHITE);
            checkviewboder(_cwaferlotno2tv,Color.WHITE);
            checkviewboder(_cwaferring1tv,Color.WHITE);
            checkviewboder(_cwaferring2tv,Color.WHITE);
            checkviewboder(_cwaferring3tv,Color.WHITE);
            checkviewboder(_cwaferring4tv,Color.WHITE);
            checkviewboder(_cqty1tv,Color.WHITE);
            checkviewboder(_cqty2tv,Color.WHITE);
            checkviewboder(_cqty3tv, Color.WHITE);
            checkviewboder(_cqty4tv, Color.WHITE);
            checkviewboder(_cmode1sp, Color.parseColor("#a0a5ba"));
            checkviewboder(_cmode2sp,Color.parseColor("#a0a5ba"));
            checkviewboder(_cmode3sp,Color.parseColor("#a0a5ba"));
            checkviewboder(_cmode4sp,Color.parseColor("#a0a5ba"));
            checkviewboder(_cdefectqty1tv,Color.WHITE);
            checkviewboder(_cdefectqty2tv,Color.WHITE);
            checkviewboder(_cdefectqty3tv,Color.WHITE);
            checkviewboder(_cdefectqty4tv,Color.WHITE);
        }

        if(!checkstring_1(_hwaferlotno1,_hwaferring1,_hqty1,_hdefectqty1,_hmode1,_hwaferring2,_hqty2,_hdefectqty2,_hmode2)&&
                !checkstring_1(_hwaferlotno2,_hwaferring3,_hqty3,_hdefectqty3,_hmode3,_hwaferring4,_hqty4,_hdefectqty4,_hmode4)) {
            checkviewboder(_hwaferlotno1tv,Color.RED);
            checkviewboder(_hwaferlotno2tv,Color.RED);
            checkviewboder(_hwaferring1tv,Color.RED);
            checkviewboder(_hwaferring2tv,Color.RED);
            checkviewboder(_hwaferring3tv,Color.RED);
            checkviewboder(_hwaferring4tv,Color.RED);
            checkviewboder(_hqty1tv,Color.RED);
            checkviewboder(_hqty2tv,Color.RED);
            checkviewboder(_hqty3tv,Color.RED);
            checkviewboder(_hqty4tv,Color.RED);
            checkviewboder(_hmode1sp,Color.RED);
            checkviewboder(_hmode2sp,Color.RED);
            checkviewboder(_hmode3sp,Color.RED);
            checkviewboder(_hmode4sp,Color.RED);
            checkviewboder(_hdefectqty1tv,Color.RED);
            checkviewboder(_hdefectqty2tv,Color.RED);
            checkviewboder(_hdefectqty3tv,Color.RED);
            checkviewboder(_hdefectqty4tv,Color.RED);
            s = false;
        }else{
            checkviewboder(_hwaferlotno1tv,Color.WHITE);
            checkviewboder(_hwaferlotno2tv,Color.WHITE);
            checkviewboder(_hwaferring1tv,Color.WHITE);
            checkviewboder(_hwaferring2tv,Color.WHITE);
            checkviewboder(_hwaferring3tv,Color.WHITE);
            checkviewboder(_hwaferring4tv,Color.WHITE);
            checkviewboder(_hqty1tv,Color.WHITE);
            checkviewboder(_hqty2tv,Color.WHITE);
            checkviewboder(_hqty3tv, Color.WHITE);
            checkviewboder(_hqty4tv, Color.WHITE);
            checkviewboder(_hmode1sp, Color.parseColor("#a0a5ba"));
            checkviewboder(_hmode2sp,Color.parseColor("#a0a5ba"));
            checkviewboder(_hmode3sp,Color.parseColor("#a0a5ba"));
            checkviewboder(_hmode4sp,Color.parseColor("#a0a5ba"));
            checkviewboder(_hdefectqty1tv,Color.WHITE);
            checkviewboder(_hdefectqty2tv,Color.WHITE);
            checkviewboder(_hdefectqty3tv,Color.WHITE);
            checkviewboder(_hdefectqty4tv,Color.WHITE);
        }
        String[] pv=
                {
                        _deviceno,
                        _typewafer,
                        _vendor,
                        _shifttype,
                        _line,
                        _stage,
                        _mo,
                        _cardid,
                        _totalwafer,
                        _totalsensor,
                        _actualinput,
                        _markettype,
                        _drawingno,
                        _cwaferlotno1,
                        _cwaferlotno2,
                        _cwaferring1,
                        _cwaferring3,
                        _cqty1,
                        _cqty2,
                        _cqty3,
                        _cqty4,
                        _cdefectqty1,
                        _cdefectqty2,
                        _cdefectqty3,
                        _cdefectqty4,
                        _cmode1,
                        _cmode2,
                        _cmode3,
                        _cmode4,
                        _cbeizhu,
                        _hwaferlotno1,
                        _hwaferlotno2,
                        _hwaferring1,
                        _hwaferring3,
                        _hqty1,
                        _hqty2,
                        _hqty3,
                        _hqty4,
                        _hdefectqty1,
                        _hdefectqty2,
                        _hdefectqty3,
                        _hdefectqty4,
                        _hmode1,
                        _hmode2,
                        _hmode3,
                        _hmode4,
                        _dwaferlotno,
                        _dwafering,
                        _measurements,
                        _measurementx,
                        _measurementz,
                        _measurementy,
                        _measurementc,
                        _diethicknessspec,
                        _judgement,
                        _qcinsepector,
                        _Checked,
                        _rremark,
                        Sessionuser,
                        _cwaferring2,
                        _cwaferring4,
                        _hwaferring2,
                        _hwaferring4
                };
        rcheckpagedata r=new rcheckpagedata();
        r.rsarray=pv;
        r.rstatus=s;
        return r;

    }

    public void otherFormjcwaferiqcinspectionreportsubmit(View v)
    {
        rcheckpagedata r=checkpagedataintegrality();
       if(!r.rstatus) {
           Toast.makeText(this,"提交表單資料不符", Toast.LENGTH_SHORT).show();
           return;
       }
        String[] pn={"devoceno",
               "type",
               "Vendor",
               "Shift",
               "line",
               "Stage",
               "MO",
               "Cardid",
               "TotalWafer",
               "TotalSensor",
               "ActualInput",
               "Market",
               "Drawing",
               "WaferLot1",
               "WaferLot2",
               "WaferRing1",
               "WaferRing2",
               "qty1",
               "qty2",
               "qty3",
               "qty4",
               "DefectQty1",
               "DefectQty2",
               "DefectQty3",
               "DefectQty4",
               "mode1",
               "mode2",
               "mode3",
               "mode4",
               "beizhu1",
               "WaferLot11",
               "WaferLot22",
               "WaferRing11",
               "WaferRing22",
               "qty11",
               "qty22",
               "qty33",
               "qty44",
               "DefectQty11",
               "DefectQty22",
               "DefectQty33",
               "DefectQty44",
               "mode11",
               "mode22",
               "mode33",
               "mode44",
               "DWaferLot",
               "DWaferRing",
               "Measurements",
               "Measurementx",
               "Measurementz",
               "Measurementy",
               "Measurementc",
               "Spec",
               "Judgement",
               "QCInspector",
               "Checked",
               "Remark",
               "loginuserid",
               "att1",
               "att2",
               "att3",
               "att4"
        };

        getsoapdata(2,"WafergetData", pn, r.rsarray, r.rsarray.length);
    }

    private void checkviewboder(View v,int i) {
        v.setBackgroundColor(i);
    }

    private boolean checkstring_1( String a,String b1,String b2,String b3,String b4,String c1,String c2,String c3,String c4) {
        if (a == null) {
            return false;
        }

        if (a.trim().length() > 0) {
            boolean b = false;
            if (b1 != null && b1.trim().length() > 0) {
                if (b2 == null || b2.trim().length() == 0) {
                    b = false;
                } else if (b3 == null || b3.trim().length() == 0) {
                    b = false;
                } else if (b4 == null) {
                    b = false;
                } else {
                    b = true;
                }
            }

            boolean c = false;
            if (c1 != null && c1.trim().length() > 0) {
                if (c2 == null || c2.trim().length() == 0) {
                    c = false;
                } else if (c3 == null || c3.trim().length() == 0) {
                    c = false;
                } else if (c4 == null) {
                    c = false;
                } else {
                    c = true;
                }
            }

            if (b || c) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    private boolean checkint(@NonNull String v,@NonNull String limit)
    {
        try {
            double s = Double.valueOf(limit.substring(0, 5));
            double e = Double.valueOf(limit.substring(6, 11));

            if (s-e <= Double.valueOf(v) && Double.valueOf(v) <= s+e) {
                return true;
            } else {
                return false;
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private void loadingdata(int ctype,Object pv) {
        String METHOD_NAME ="getdeviceno";
        //String[] spn=null;
        //String[] spv=null;
        switch (ctype) {
            case 0:
                METHOD_NAME ="getdeviceno";
                getsoapdata(ctype, METHOD_NAME, null, null, 0);
                break;
            case 1:
                METHOD_NAME ="getcarrier";
                String[] spn = {"deviceno"};
                String[] spv = {pv.toString()};
                getsoapdata(ctype, METHOD_NAME, spn, spv, 1);
                spn = null;
                spv = null;
                break;
        }
        METHOD_NAME = null;
    }

    public  void getkeyindatainput(String urlpath, List<httprequestinputdata> li, int sendmode) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
       // List<httprequestinputdata> li=null;
        exechttprequest hf1=new exechttprequest();
        //Log.v("CMSF", hf1.toString());
        hf1.getDataFromServer(sendmode, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1,"正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        Log.v("CMSF", paramObject.toString());
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        Toast.makeText(jcwaferiqcinspectionreport.this
                                ,"操作失敗，請求異常-"+paramObject.toString(),Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        Toast.makeText(jcwaferiqcinspectionreport.this
                                ,"操作失敗，請求異常",Toast.LENGTH_SHORT).show();
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }

    public  void fillingdata(List<jsontolist._jsonarray> j1)
    {

    }

    private  void closeacvitity()
    {

    }

    public void checkviewstate(View v,String status)
    {
        switch (status)
        {
            case"N":
                v.setVisibility(View.INVISIBLE);
                v.setEnabled(false);
                break;
            default:
                v.setVisibility(View.VISIBLE);
                v.setEnabled(true);
                break;
        }
    }

    /**
     * 當機原因/誤報類型-改善方案/當機原因數據源捆綁
     * @param e
     * @param v
     * @param sends
     * @param status
     */
    public void checketandsp(final EditText e, final Spinner v, List<List<jsontolist._valueitem>> sends,String status,final String[] nextstep)
    {
        switch (status) {
            case"0":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                e.setEnabled(true);
                e.setVisibility(View.VISIBLE);
                break;
            case"N":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                e.setEnabled(false);
                e.setVisibility(View.INVISIBLE);
                break;
            default:

                v.setEnabled(true);
                v.setVisibility(View.VISIBLE);
                if(status.equals("1"))
                {
                    e.setEnabled(false);
                    e.setVisibility(View.VISIBLE);
                }
                else {
                    e.setEnabled(true);
                    e.setVisibility(View.VISIBLE);
                }
                if(sends!=null) {
                    List<FinalStaticCloass.SpinnerData> lf1 = new ArrayList<FinalStaticCloass.SpinnerData>();
                    //List<String> ls=new ArrayList<>();
                    for (int i = 0; i < sends.size(); i++) {
                        FinalStaticCloass s = new FinalStaticCloass();
                        FinalStaticCloass.SpinnerData ss = s.new SpinnerData(sends.get(i).get(0).get_value(), sends.get(i).get(1).get_value());
                        lf1.add(ss);
                    }
                   ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                            new ArrayAdapter<FinalStaticCloass.SpinnerData>(jcwaferiqcinspectionreport.this, android.R.layout.simple_dropdown_item_1line,lf1);
                    as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    v.setAdapter(as1);
                    //v.setSelection(0, false);
                    v.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    e.setText(
                                            ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getText()
                                    );
                                    if(nextstep!=null) {
                                        for(int i=0;i<nextstep.length;i++) {
                                            nextstepfunction(nextstep[i].toString(), ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue());
                                            //Log.v("CMSF", nextstep +"-" + ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue().toString());
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            }
                    );
                }
                break;
        }
    }

    /**
     * Spare and nozzel child step data bind
     * @param v
     * @param sends
     * @param status
     * @param nextstep
     */
    public void checksp(final Spinner v, List<List<jsontolist._valueitem>> sends,String status,final String[] nextstep)
    {
        switch (status) {
            case"0":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                break;
            case"N":
                v.setEnabled(false);
                v.setVisibility(View.INVISIBLE);
                break;
            default:
                v.setEnabled(true);
                v.setVisibility(View.VISIBLE);
                if(sends!=null) {
                    List<FinalStaticCloass.SpinnerData> lf1 = new ArrayList<FinalStaticCloass.SpinnerData>();
                    //List<String> ls=new ArrayList<>();
                    for (int i = 0; i < sends.size(); i++) {
                        FinalStaticCloass s = new FinalStaticCloass();
                        FinalStaticCloass.SpinnerData ss = s.new SpinnerData(sends.get(i).get(0).get_value(), sends.get(i).get(1).get_value());
                        lf1.add(ss);
                    }
                    ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                            new ArrayAdapter<FinalStaticCloass.SpinnerData>(jcwaferiqcinspectionreport.this, android.R.layout.simple_dropdown_item_1line,lf1);
                    as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    v.setAdapter(as1);
                    //v.setSelection(0, false);
                    v.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(nextstep!=null) {
                                        for(int i=0;i<nextstep.length;i++) {
                                            nextstepfunction(nextstep[i].toString(), ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue());
                                            //Log.v("CMSF", nextstep +"-" + ((FinalStaticCloass.SpinnerData) v.getSelectedItem()).getValue().toString());
                                        }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            }
                    );
                }
                break;
        }
    }

    /**
     * get next step link data
     * @param nextsteptype
     * @param o1
     */
  private void nextstepfunction(String nextsteptype,Object o1)
  {
  }

    public void eqrepairenginputsubmit(View v)
    {
     
    }
    public  void eqengrepairdatainsubmit(String urlpath,List<httprequestinputdata> li) {
        //String urlpath="http://10.142.136.222:8107/SFCReportHandler.ashx";
        // List<httprequestinputdata> li=null;
        exechttprequest hf1=new exechttprequest();
        //Log.v("CMSF", hf1.toString());
        hf1.getDataFromServer(2, new FreedomDataCallBack() {
                    @Override
                    public void onStart(Context C1) {
                        execloadactivity.opendialog(C1,"正在執行");
                    }

                    @Override
                    public void processData(Object paramObject, boolean paramBoolean) {
                        //Log.v("CMSF",paramObject.toString());
                        jsontolist js = new jsontolist();
                        fillingdata(js.jasontolist(paramObject.toString()));
                    }

                    @Override
                    public void onFinish(Context C1) {
                        execloadactivity.canclediglog();
                    }

                    @Override
                    public void onFailed(Object paramObject,Context C1) {
                        execloadactivity.canclediglog();
                        Toast.makeText(jcwaferiqcinspectionreport.this,"File-"+paramObject.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNoneImage(Context C1) {
                        execloadactivity.canclediglog();
                    }
                },
                this, urlpath, li, null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imagedata) {
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            return;
        }
        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 0)
        {
            Bundle bundle = imagedata.getExtras();
            String imageresult = bundle.getString("result");
            Toast.makeText(this,imageresult,Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 接口回調方法
     * @param parmerobject 依用戶需求定義
     * @param o 回傳結果參數
     */
    @Override
    public void execobject(Object parmerobject, Object o)
    {
        try {
            switch ((int)parmerobject)
            {
                //Save Tempfile
                case 0:
                    Toast.makeText(this,((BaseFuncation.rrtype)o).get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;
                //Load Tempfileto UI
                case 1:
                    loadtempdatatoui(o);
                    break;
                //Clear all cache file
                case 2:
                    Toast.makeText(this,((BaseFuncation.rrtype)o).get_rmsg(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        execloadactivity.canclediglog();
    }

    @Override
    public  void BackData(Object so,Context cc,int type)
    {
        soapfilldata(so, type);
    }


    private void soapfilldata(Object o,int ctype)
    {
        execloadactivity.canclediglog();
        if(o==null) {
            Toast.makeText(this, String.valueOf(ctype) +":獲取資料失敗，獲取資料為Null", Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject so=null;
        try {
            so = (SoapObject) o;
        }
        catch(Exception ex1)
        {
            Toast.makeText(this, String.valueOf(ctype) +":獲取資料失敗，非標準SOAP格式", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            switch (ctype) {
                // Reject Form
                case 0:
                    String r1 = so.getProperty(0).toString();
                    switch (r1) {
                        case"true":
                            Toast.makeText(this, Checkdataid +"駁回成功", Toast.LENGTH_SHORT).show();
                            this.finish();
                            break;
                        case"false":
                            Toast.makeText(this, Checkdataid +"駁回失敗", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 1:
                    String r2=((SoapObject)((SoapObject)((SoapObject)((SoapObject)so.getProperty(0)).getProperty(1)).getProperty(0)).getProperty(0)).getProperty(0).toString();
                    //eqcheckdheadertv1.setText(r2);
                    break;
                default:
                    Toast.makeText(this,"Unknow Methods Return Result", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 暫存資料按鈕
     * @param v
     */
    public  void savatempdata(View v)
    {
        execloadactivity.opendialog(this,"正在執行");
        final BaseHandler b=new BaseHandler(this,0,this);

        MyThreadPool.pool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                        try {
                            List<String> ls =getsavelist();

                            if (ls != null && ls.size() > 0) {
                                r = Exectempfile.instance().savefile(Checkdataid, ls);
                            }

                            msg.what = 0;
                            msg.obj = r;

                        } catch (Exception ex) {
                            r.set_rstatus(false);
                            r.set_rmsg(ex.getMessage());
                            msg.what = 1;
                            msg.obj = r;
                        }
                        b.sendMessage(msg);
                    }
                }
        );
    }

    public List<String> getsavelist()
    {
        final  String[] pn={"devoceno",
               "type",
               "Vendor",
               "Shift",
               "line",
               "Stage",
               "MO",
               "Cardid",
               "TotalWafer",
               "TotalSensor",
               "ActualInput",
               "Market",
               "Drawing",
               "WaferLot1",
               "WaferLot2",
               "WaferRing1",
               "WaferRing2",
               "qty1",
               "qty2",
               "qty3",
               "qty4",
               "DefectQty1",
               "DefectQty2",
               "DefectQty3",
               "DefectQty4",
               "mode1",
               "mode2",
               "mode3",
               "mode4",
               "beizhu1",
               "WaferLot11",
               "WaferLot22",
               "WaferRing11",
               "WaferRing22",
               "qty11",
               "qty22",
               "qty33",
               "qty44",
               "DefectQty11",
               "DefectQty22",
               "DefectQty33",
               "DefectQty44",
               "mode11",
               "mode22",
               "mode33",
               "mode44",
               "DWaferLot",
               "DWaferRing",
               "Measurements",
               "Measurementx",
               "Measurementz",
               "Measurementy",
               "Measurementc",
               "Spec",
               "Judgement",
               "QCInspector",
               "Checked",
               "Remark",
               "att1",
               "att2",
               "att3",
               "att4"
        };

        _deviceno= ((FinalStaticCloass.SpinnerData)_devicetv.getSelectedItem()).getValue();
        _typewafer=_typewafertv.getText().toString();
        _vendor=_vendortv.getText().toString();
        _shifttype= ((FinalStaticCloass.SpinnerData)_shifttypesp.getSelectedItem()).getValue();
        _line=_linetv.getText().toString();
        _stage= ((FinalStaticCloass.SpinnerData)_stagetv.getSelectedItem()).getValue();
        _mo=_motv.getText().toString();
        _cardid=_cardidtv.getText().toString();
        _totalwafer=_totalwafertv.getText().toString();
        _totalsensor=_totalsensortv.getText().toString();
        _actualinput=_actualinputtv.getText().toString();
        _markettype=((FinalStaticCloass.SpinnerData)_markettypesp.getSelectedItem()).getValue();
        _drawingno=_drawingnotv.getText().toString();
        _cwaferlotno1=_cwaferlotno1tv.getText().toString();
        _cwaferlotno2=_cwaferlotno2tv.getText().toString();
        _cwaferring1=_cwaferring1tv.getText().toString();
        _cwaferring3=_cwaferring3tv.getText().toString();
        _cqty1=_cqty1tv.getText().toString();
        _cqty2=_cqty2tv.getText().toString();
        _cqty3=_cqty3tv.getText().toString();
        _cqty4=_cqty4tv.getText().toString();
        _cdefectqty1=_cdefectqty1tv.getText().toString();
        _cdefectqty2=_cdefectqty2tv.getText().toString();
        _cdefectqty3=_cdefectqty3tv.getText().toString();
        _cdefectqty4=_cdefectqty4tv.getText().toString();
        _cmode1=((FinalStaticCloass.SpinnerData)_cmode1sp.getSelectedItem()).getValue();
        _cmode2=((FinalStaticCloass.SpinnerData)_cmode2sp.getSelectedItem()).getValue();
        _cmode3=((FinalStaticCloass.SpinnerData)_cmode3sp.getSelectedItem()).getValue();
        _cmode4=((FinalStaticCloass.SpinnerData)_cmode4sp.getSelectedItem()).getValue();
        _cbeizhu=_cbeizhutv.getText().toString();
        _hwaferlotno1=_hwaferlotno1tv.getText().toString();
        _hwaferlotno2=_hwaferlotno2tv.getText().toString();
        _hwaferring1=_hwaferring1tv.getText().toString();
        _hwaferring3=_hwaferring3tv.getText().toString();
        _hqty1=_hqty1tv.getText().toString();
        _hqty2=_hqty2tv.getText().toString();
        _hqty3=_hqty3tv.getText().toString();
        _hqty4=_hqty4tv.getText().toString();
        _hdefectqty1=_hdefectqty1tv.getText().toString();
        _hdefectqty2=_hdefectqty2tv.getText().toString();
        _hdefectqty3=_hdefectqty3tv.getText().toString();
        _hdefectqty4=_hdefectqty4tv.getText().toString();
        _hmode1=((FinalStaticCloass.SpinnerData)_hmode1sp.getSelectedItem()).getValue();
        _hmode2=((FinalStaticCloass.SpinnerData)_hmode2sp.getSelectedItem()).getValue();
        _hmode3=((FinalStaticCloass.SpinnerData)_hmode3sp.getSelectedItem()).getValue();
        _hmode4=((FinalStaticCloass.SpinnerData)_hmode4sp.getSelectedItem()).getValue();
        _dwaferlotno=_dwaferlotnotv.getText().toString();
        _dwafering=_dwaferingtv.getText().toString();
        _measurements=_measurementstv.getText().toString();
        _measurementx=_measurementxtv.getText().toString();
        _measurementz=_measurementztv.getText().toString();
        _measurementy=_measurementytv.getText().toString();
        _measurementc=_measurementctv.getText().toString();
        _diethicknessspec=_diethicknessspectv.getText().toString();
        _judgement=((FinalStaticCloass.SpinnerData)_judgementtv.getSelectedItem()).getValue();
        _qcinsepector=_qcinsepectortv.getText().toString();
        _Checked=_Checkedtv.getText().toString();
        _rremark=_rremarktv.getText().toString();
        _cwaferring2=_cwaferring2tv.getText().toString();
        _cwaferring4=_cwaferring4tv.getText().toString();
        _hwaferring2=_hwaferring2tv.getText().toString();
        _hwaferring4=_hwaferring4tv.getText().toString();


        final String[] pv=
                {
                        _deviceno,
                        _typewafer,
                        _vendor,
                        _shifttype,
                        _line,
                        _stage,
                        _mo,
                        _cardid,
                        _totalwafer,
                        _totalsensor,
                        _actualinput,
                        _markettype,
                        _drawingno,
                        _cwaferlotno1,
                        _cwaferlotno2,
                        _cwaferring1,
                        _cwaferring3,
                        _cqty1,
                        _cqty2,
                        _cqty3,
                        _cqty4,
                        _cdefectqty1,
                        _cdefectqty2,
                        _cdefectqty3,
                        _cdefectqty4,
                        _cmode1,
                        _cmode2,
                        _cmode3,
                        _cmode4,
                        _cbeizhu,
                        _hwaferlotno1,
                        _hwaferlotno2,
                        _hwaferring1,
                        _hwaferring3,
                        _hqty1,
                        _hqty2,
                        _hqty3,
                        _hqty4,
                        _hdefectqty1,
                        _hdefectqty2,
                        _hdefectqty3,
                        _hdefectqty4,
                        _hmode1,
                        _hmode2,
                        _hmode3,
                        _hmode4,
                        _dwaferlotno,
                        _dwafering,
                        _measurements,
                        _measurementx,
                        _measurementz,
                        _measurementy,
                        _measurementc,
                        _diethicknessspec,
                        _judgement,
                        _qcinsepector,
                        _Checked,
                        _rremark,
                        _cwaferring2,
                        _cwaferring4,
                        _hwaferring2,
                        _hwaferring4
                };
        List<String> ls = new ArrayList<String>();
        if (pn != null) {
            for (int i = 0; i < pn.length; i++) {
                ls.add(
                       "[" + pn[i] +"],["+pv[i] +"]"
                );
            }
        }

        return ls;
    }

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value)
    {
        SpinnerAdapter apsAdapter=spinner.getAdapter();//得到SpinnerAdapter對象

        int k=apsAdapter.getCount();
        for (int i=0;i<k;i++)
        {
            if(value.equals(apsAdapter.getItem(i).toString()))
            {
                spinner.setSelection(i,true); //默認選中項
                break;
            }
        }
    }

    /**
     * 載入暫存資料成功顯示至UI
     * @param obj
     */
    private void loadtempdatatoui(Object obj)
    {
        try {
            List<String> ls = (List<String>) obj;
            String tempa, tempb;
            for (String s : ls
                    ) {
                if (s == null || s.trim().equals(""))
                    continue;

                tempa = s.substring(1, s.indexOf("],["));
                tempb = s.substring(s.indexOf("],[") + 3, s.length() - 1);


                if(tempa.equals("devoceno"))
                    setSpinnerItemSelectedByValue(_devicetv, tempb);
                
                if(tempa.equals("type"))
                _typewafertv.setText(tempb);

                if(tempa.equals("Vendor"))
                _vendortv.setText(_vendor);

                if(tempa.equals("Shift"))
                    setSpinnerItemSelectedByValue(_shifttypesp, tempb);

                if(tempa.equals("line"))
                _linetv.setText(tempb);

                if(tempa.equals("Stage"))
                    setSpinnerItemSelectedByValue(_stagetv, tempb);

                if(tempa.equals("MO"))
                _motv.setText(tempb);

                if(tempa.equals("Cardid"))
                _cardidtv.setText(tempb);

                if(tempa.equals("TotalWafer"))
                _totalwafertv.setText(tempb);

                if(tempa.equals("TotalSensor"))
                _totalsensortv.setText(tempb);

                if(tempa.equals("ActualInput"))
                _actualinputtv.setText(tempb);

                if(tempa.equals("Market"))
                    setSpinnerItemSelectedByValue(_markettypesp, tempb);

                if(tempa.equals("Drawing"))
                _drawingnotv.setText(tempb);

                if(tempa.equals("WaferLot1"))
                _cwaferlotno1tv.setText(tempb);

                if(tempa.equals("WaferLot2"))
                _cwaferlotno2tv.setText(tempb);

                if(tempa.equals("WaferRing1"))
                _cwaferring1tv.setText(tempb);

                if(tempa.equals("WaferRing2"))
                _cwaferring3tv.setText(tempb);

                if(tempa.equals("qty1"))
                _cqty1tv.setText(tempb);
                
                if(tempa.equals("qty2"))
                _cqty2tv.setText(tempb);

                if(tempa.equals("qty3"))
                _cqty3tv.setText(tempb);

                if(tempa.equals("qty4"))
                _cqty4tv.setText(tempb);

                if(tempa.equals("DefectQty1"))
                _cdefectqty1tv.setText(tempb);

                if(tempa.equals("DefectQty2"))
                _cdefectqty2tv.setText(tempb);

                if(tempa.equals("DefectQty3"))
                _cdefectqty3tv.setText(tempb);

                if(tempa.equals("DefectQty4"))
                _cdefectqty4tv.setText(tempb);

                if(tempa.equals("mode1"))
                    setSpinnerItemSelectedByValue(_cmode1sp, tempb);

                if(tempa.equals("mode2"))
                    setSpinnerItemSelectedByValue(_cmode2sp, tempb);

                if(tempa.equals("mode3"))
                    setSpinnerItemSelectedByValue(_cmode3sp, tempb);

                if(tempa.equals("mode4"))
                    setSpinnerItemSelectedByValue(_cmode4sp, tempb);

                if(tempa.equals("beizhu1"))
                _cbeizhutv.setText(tempb);

                if(tempa.equals("WaferLot11"))
                _hwaferlotno1tv.setText(tempb);

                if(tempa.equals("WaferLot22"))
                _hwaferlotno2tv.setText(tempb);

                if(tempa.equals("WaferRing11"))
                _hwaferring1tv.setText(tempb);

                if(tempa.equals("WaferRing22"))
                _hwaferring3tv.setText(tempb);

                if(tempa.equals("qty11"))
                _hqty1tv.setText(tempb);

                if(tempa.equals("qty22"))
                _hqty2tv.setText(tempb);

                if(tempa.equals("qty33"))
                _hqty3tv.setText(tempb);

                if(tempa.equals("qty44"))
                _hqty4tv.setText(tempb);

                if(tempa.equals("DefectQty11"))
                _hdefectqty1tv.setText(tempb);

                if(tempa.equals("DefectQty22"))
                _hdefectqty2tv.setText(tempb);

                if(tempa.equals("DefectQty33"))
                _hdefectqty3tv.setText(tempb);

                if(tempa.equals("DefectQty44"))
                _hdefectqty4tv.setText(tempb);

                if(tempa.equals("mode11"))
                    setSpinnerItemSelectedByValue(_hmode1sp, tempb);

                if(tempa.equals("mode22"))
                    setSpinnerItemSelectedByValue(_hmode2sp, tempb);

                if(tempa.equals("mode33"))
                    setSpinnerItemSelectedByValue(_hmode3sp, tempb);

                if(tempa.equals("mode44"))
                    setSpinnerItemSelectedByValue(_hmode4sp, tempb);

                if(tempa.equals("DWaferLot"))
                _dwaferlotnotv.setText(tempb);

                if(tempa.equals("DWaferRing"))
                _dwaferingtv.setText(tempb);

                if(tempa.equals("Measurements"))
                _measurementstv.setText(tempb);

                if(tempa.equals("Measurementx"))
                _measurementxtv.setText(tempb);

                if(tempa.equals("Measurementz"))
                _measurementztv.setText(tempb);

                if(tempa.equals("Measurementy"))
                _measurementytv.setText(tempb);

                if(tempa.equals("Measurementc"))
                _measurementctv.setText(tempb);

                if(tempa.equals("Spec"))
                _diethicknessspectv.setText(tempb);

                if(tempa.equals("Judgement"))
                    setSpinnerItemSelectedByValue(_judgementtv,tempb);

                if(tempa.equals("QCInspector"))
                _qcinsepectortv.setText(tempb);

                if(tempa.equals("Checked"))
                _Checkedtv.setText(tempb);

                if(tempa.equals("Remark"))
                _rremarktv.setText(tempb);

                if(tempa.equals("att1"))
                _cwaferring2tv.setText(tempb);

                if(tempa.equals("att2"))
                _cwaferring4tv.setText(tempb);

                if(tempa.equals("att3"))
                _hwaferring2tv.setText(tempb);

                if(tempa.equals("att4"))
                _hwaferring4tv.setText(tempb);
            }
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    /**
     * 啟動載入暫存資料
     */
    public void loadtempdata()
    {
        try {
            BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
            r = Exectempfile.instance().getfile(Checkdataid, this, 1);
            Toast.makeText(this, r.get_rmsg(), Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    /**
     * 清除本機所有緩存資料按鈕
     * @param v
     */
    public void clearlocalcache(View v) {
        execloadactivity.opendialog(this,"正在執行");
        final BaseHandler bh=new BaseHandler(this,2,this);
        MyThreadPool.pool.execute(new Runnable() {
            @Override
            public void run() {
               // BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
                BaseFuncation.rrtype r=new BaseFuncation().new rrtype();
                r=Exectempfile.instance().removefile(Checkdataid);
                Message m = new Message();
                try {
                    r = Exectempfile.instance().clearcachefile();
                    m.what = 0;
                    m.obj = r;
                } catch (Exception e) {
                    r.set_rstatus(false);
                    r.set_rmsg(e.getMessage());
                    m.what = 1;
                    m.obj = e.getMessage();
                }
                bh.sendMessage(m);
            }
        });
    }
}

