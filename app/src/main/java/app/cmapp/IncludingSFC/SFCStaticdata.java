package app.cmapp.IncludingSFC;/**
 * Created by F5460007 on 2017/4/7.
 */
import java.util.List;
import app.cmapp.DataTable.DataTable;

/**
 * Owner:F5460007
 * CreateDate:2017/4/7 09:01
 */
public class SFCStaticdata  {
    public static class  staticmember
    {
        public static String carrierflag;       //載幫解綁成功標記位
        public static String jiepifalg;         //结批标记位
        public static String vtqnoholdfalg;     //特殊工單不卡母批
        public static String qcholddefalg;      //QC管控良率机种标记位
        public static String fifoflag;          //QC管控良率机种标记位
        public static String devicetype;          //QC管控良率机种标记位
        public static String deviceno;
        public static String newdeviceno;
        public static String checkinqty;
        public static String defectflag;   //測試站位區分不良品是60還是30
        public static String hpuserid;    //人员的全局变量
        public static String hpproductno;    //人员的全局变量
        public static String odbname;
        public static String eolspitflag;    //分小子批标记位
        public static String userpwd;
        public static String sdbname;
        public static String pename;
        public static String str_lotno; //公共變量批號
        public static String str_opno; //公共變量站位
        public static String ip;
        public static String rethressfalg;  //37強制過站標記位
        public static String engconfirmstr;  // 針對重工批次打不良只執行一次Confirm確認動作！~
        public static Boolean engconfirmstrflag=false;//標誌位針對龍華重工品開始過站，不想掃描一顆打一顆不良功能標誌位進行判斷！~
        public static String userid;
        public static Boolean useflag=false;
        public static Boolean defngflag=false;
        public static Boolean feflag=false;
        public static Boolean pqecheckflag=false;//nh-a qc百測前PQE抽檢
        public static Boolean mfgcheckflag=false;//nh-a qc百測前線長抽檢

        public static List<defectdata> errorqtydata;
        public static Boolean engconfiginfo=false;
        public static Boolean controlconfigflag=false;
        public static Boolean Podtestflag=false;
        public static Boolean EolcheckFolLine=false;
        public static Boolean checklotsub=false;  // 批號強行進入權限認證
        public static Boolean updatelotcartflag=false;  // 批號 线边仓 状态更新 标志
        public static Boolean podtestsumcheck=false;  // pod 测试资料 是否 符合 数据
        public static Boolean Folinformationflag=false; //確認前端備註信息是否輸入完全
        public static Boolean qcchangefalg=false;  // QC變更中心值不良
        public static Boolean errorcharacterSetup=false;
        public static Boolean qcsntestsumcheck=false;  // pod 测试资料 是否 符合 数据
        public static Boolean englotcheckflag=false;   // 新机种  vtq
        public static Boolean engRilotcheckflag=false; //RI  a/b/c/f機種
        public static Boolean engmfglotcheckflag=false;  // 批号是否为工程批号
        public static Boolean testsumcheckflag=false;  // 批号 检测 测试站位 测试 数据 够不够
        public static Boolean checksmtqcflag=false;  // smt qc
        public static Boolean fvicheckbag=false;
        public static Boolean fvicheckbagflag=false;
        public static Boolean laserchecksnitem=false;
        public static Boolean lotcheckeolflag=false;
        public static Boolean lotpaoliaocheckeolflag=false;
        public static Boolean lotoutchecktimeflag=false;   // GA检测时间
        public static Boolean lotfoleolcheckflag=false;    // lot 当前 所处位置   true  FOL  false EOL
        public static Boolean lotacfchecklasertime=false;   // ACF 站位确认过站时间差
        public static Boolean lotfolbingpicheck=false; // FOL 分并批批号数量查询检测是否应该并批
        public static Boolean lotqcspecialinfo=false;  // qc 特殊作业记录标志位
        public static Boolean lotyicinginfo=false;    // 一次不良標誌位
        public static Boolean devicenomacinfo=false;    // 一次不良標誌位   J系列標識位
        public static Boolean updatelotuserflag=false;    // 批號登陸查詢
        public static Boolean wipopflag=false;    // 批號分批驗證標誌
        public static Boolean opdefectatt6flag=false;  // 是否特殊不良代碼需要輸入原因。
        public static Boolean lotnouncheckBoolean=false;  //批號是否需要卡進出站時間以及抽檢數量
        public static String insertNgSnLotSql; //對於掃描單顆的Ng SN信息，要求在點擊出站按鈕時同時插入系統
        public static Boolean nocheckmupicheckBoolean=false;//特殊工單不卡母批
        public static Boolean fqcflag=false;           //前段QC标记位
        //public static String qcbackstaticworkdayDN;//QC Q退記錄相關班別信息
        //public static String qcbackstaticmfgop;//QC Q退記錄相關MFG OP工號信息
        //public static String qcbackstaticmfgcheck;//QC Q退記錄相關MFG 確認人員工號信息
        //public static String qcbackstaticengcheck;//QC Q退記錄相關MFG 確認人員工號信息
        //public static String qcbackstaticqccheck;//QC Q退記錄相關MFG 確認人員工號信息
        //public static String qcbackstaticdefectgroup;//QC Q退的Defect Code
        public static List<String[]> qcstaticListString;
        public static int packcheckoutbagidscanqty;//Pack出站扫描包号总数量
        public static Boolean staticYieldLimitedOpno=false;// RI/RI+良率管控站位
        public static String FolCheckStationInsertSql ;//FOL 抽檢站位執行SQL語句
        public static String hwDeviceDBOnlineUser ;//HW 線上DB測試數據還原功能
        public static Boolean FolGaOvenStationNoInOutTimeIsOK=false;//FOL GAOven站位卡进出站55分钟
        public static Boolean CheckInMultiMachinenoSelectBoolean=false;//是否為需要進站選擇多機台號的站位
        public static List<String> CheckInMultiMachineList;//多機台號列表
        public static Boolean AllReworkFlag=false;  //是否为重工品过站
        public static Boolean EngLotnoNgManagerCheckBoolean=false;//eng機種需要進行不良品確認的站位
        public static Boolean IsQCBackStationSFConlineFlag=false;//QC是否Q退标志位
        public static Boolean IsOQCStationNeedCheckNGDeviceBoolean=false; //可以在QC站位打不良的机种
        public static Boolean IsMesDevice=false;  //是否导入mes
        public static Boolean IsQccheck=false;  //0002_4是否确认过站
        public static Boolean lineflag=false; //是否因线别被锁
        public static Boolean yieldflag=false;//良率hold
        public static Boolean lotsmtcheckflag=false;    // 是否是smt   true  smt  false not smt
        public static Boolean useridsave=false;   //工号是否录入成功

        public static String HDSerialNo; //平板的序列号
        public static DataTable weishulotnosenddt; //weishulotno class sendtable

        /// <summary>
        /// WZH 2017/03/16 tod update 
        /// </summary>
        public static Boolean OQCNGCheckinFVI=false; //QC不良在外观打掉
        public static Boolean FVICHECKOQCNG=false; //FVI打QC不良


        public static Boolean xbcandmfgcheck=false; // 線邊倉發料后需產線物料確認

        public static Boolean xbccheckoutdev=false; // 線邊倉出站時將FOL批號分的兩個子批一次出站


        public static Boolean magazineformflag=false;

    }
    public class defectdata
    {
        public String errorcode;
        public String errorname;
        public int errorqty;
    }
    public static class expendm
    {
        public static Boolean expendpageflag=false;
    }

    public static class staticparmer
    {
        public static String pathstatic;
    }
}
