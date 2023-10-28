package app.cmapp;

import android.app.Application;

import app.cmapp.CMSFCrash.CrashHandler;

/**
 * Created by Administrator on 2016/4/22.
 */
public class Staticdata extends Application {
    //登陆UserID
    private String LoginUserID;
    public String getLoginUserID(){
        return this.LoginUserID;
    }
    public void setLoginUserID(String UID){
        this.LoginUserID= UID.toUpperCase();
    }

    /**
     * 獲取和保持本設備MAC地址
     */
    private String LOCALMACADDRESS;
    public String getLOCALMACADDRESS(){
        return this.LOCALMACADDRESS;
    }
    public void setLOCALMACADDRESS(String MACID){
        this.LOCALMACADDRESS=MACID;
    }

    //NFC状态
    private Boolean NFCWRB=false;
    public Boolean getNFCWRB()
    {
        return this.NFCWRB;
    }

    public void setNFCWRB(Boolean NFCWRBS)
    {
        this.NFCWRB=NFCWRBS;
    }

    //写入NFC的信息
    private String WriteNFCDATA;
    public String getWriteNFCDATA(){
        return this.WriteNFCDATA;
    }
    public void setWriteNFCDATA(String WriteNFCDATAS){
        this.WriteNFCDATA= WriteNFCDATAS;
    }
    //临时传参
    private String Tempeqlot;
    public String getTempeqlot(){
        return this.Tempeqlot;
    }
    public void setTempeqlot(String Templeqlot1){
        this.Tempeqlot= Templeqlot1;
    }

    public static String NAMESPACE = "http://tempuri.org/";

//    public static String lhurl="http://10.143.4.218:8085/Service.asmx";
//    public static String userloginurl="http://10.143.4.218:8085/Service.asmx";
//    public static String lhhttpurl="http://10.143.4.218:8085/";
//    public static String applesfcurl="http://10.143.4.218:8085/AppleSFCService.asmx";
//    public static String mssfcurl="http://10.143.4.218:8085/MSSFCService.asmx";

    public static String lhurl="http://10.151.128.228:8090/Service.asmx";
    public static String userloginurl="http://10.151.128.228:8090/Service.asmx";
    public static String lhhttpurl="http://10.151.128.228:8090/";
    public static String applesfcurl="http://10.151.128.228:8090/AppleSFCService.asmx";
    public static String mssfcurl="http://10.151.128.228:8090/MSSFCService.asmx";
    public static String URL = "http://10.151.128.35:8091/MESSettingMain/";


    public static String jchttpurl1="http://10.151.128.225:8080/SFCReport/MPReportRunning.html";
    public static String soapurl;
    public static String httpurl;
    public static String usersite;
    public static String type;
    public static String mgr;
    public static String email;
    @Override
    public void onCreate(){
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public static void setsoapurl(String usersite)
    {
        switch (usersite)
        {
            case "DP":
                soapurl=lhurl;
                httpurl=lhhttpurl;
                break;
        }
    }
}