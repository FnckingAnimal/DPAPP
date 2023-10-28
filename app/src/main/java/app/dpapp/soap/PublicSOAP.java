package app.dpapp.soap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.CheckNewBean;
import app.dpapp.PysBean;
import app.dpapp.PysBean3;
import app.dpapp.PysBenTwo;
import app.dpapp.Staticdata;
import app.dpapp.TestBean;
import app.dpapp.appcdl.FreedomSOAPCallBack;
import app.dpapp.machinecheck.MPBean;

/**
 * Created by Administrator on 2016/5/12.
 */

public class PublicSOAP {

    String URL=Staticdata.soapurl;
    public  SoapObject soapdal(String NAMESPACE, String lURL, String METHOD_NAME, String[] parnames, String[] parvalues, int parcount) {

        try {
            String SOAP_ACTION = NAMESPACE + METHOD_NAME;

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            for (int i = 0; i < parcount; i++) {
                rpc.addProperty(parnames[i], parvalues[i]);
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(lURL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;
            return r1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     * Create New Function by tod 2016/12/15  public callsoap function
     * @param METHOD_NAME
     * @param parnames
     * @param parvalues
     * @param parcount
     * @return
     */
    private   SoapObject newsoapdal( String METHOD_NAME, String[] parnames, String[] parvalues, int parcount) {

        try {
            //URL=Staticdata.jchttpurl;
            String NAMESPACE=Staticdata.NAMESPACE;
            String SOAP_ACTION = NAMESPACE + METHOD_NAME;

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            for (int i = 0; i < parcount; i++) {
                rpc.addProperty(parnames[i].toString(), parvalues[i].toString());
            }
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
            return r1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     * 調用SOAP方法
     * @param fscb    回傳接口
     * @param c  調用該方法的Context
     * @param type  調用該方法的用戶指向參數
     * @param METHOD_NAME
     * @param parnames
     * @param parvalues
     * @param parcount
     */
    public void  getsopdata(final FreedomSOAPCallBack fscb, final Context c, final int type, final String  METHOD_NAME, final String[] parnames, final String[] parvalues, final int parcount )
    {
        final Basesoapcallback bc=new Basesoapcallback(c,fscb,type);
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapObject so=newsoapdal(METHOD_NAME, parnames, parvalues, parcount);

                Message msg = new Message();
                msg.what=0;
                msg.obj = so;

                bc.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 消息回傳
     */
    private class Basesoapcallback extends  Handler
    {
        private Context callcontext;
        private FreedomSOAPCallBack  fc;
        private int rtype;

        public Basesoapcallback(Context c,FreedomSOAPCallBack f,int rt)
        {
            callcontext=c;
            fc=f;
            rtype=rt;
        }

        @Override
        public void  handleMessage(Message msg)
        {
            if(msg.what==0)
            {
                fc.BackData(msg.obj, callcontext,rtype);
            }
        }
    }
    /**
     * 2Dcode確認用戶登陸
     * @param usersn
     * @return
     */
    public  List<String> checkuserbarcode(String usersn) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getuserinfo";
            String SOAP_ACTION = "http://tempuri.org/getuserinfo";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("serialno", usersn);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(Staticdata.lhurl);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> lrsd = new ArrayList<String>();

            //reservedata rsd1;
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                    lrsd.add(((SoapObject) soapchild.getProperty(i)).getProperty("USERID").toString());
                    lrsd.add(((SoapObject) soapchild.getProperty(i)).getProperty("USERNAME").toString());
                    lrsd.add(((SoapObject) soapchild.getProperty(i)).getProperty("PASSFLAG").toString());
                }
            } catch (Exception ex1) {
                lrsd = null;
            }

            return lrsd;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }


    /**
     * 2016/6/14 獲取設備基信息 SOAP by tod
     * @param Eqid
     * @return
     */
    public  eqbasedata getRemoteInfo_eqbasedata(String Eqid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "geteqalldata";
            String SOAP_ACTION = "http://tempuri.org/geteqalldata";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("eqid", Eqid);
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
            eqbasedata es1;
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                es1 = new eqbasedata();
                es1.eqname = ((SoapObject) soapchild.getProperty(0)).getProperty("MACHINENO").toString();
                es1.eqtype = ((SoapObject) soapchild.getProperty(0)).getProperty("MACHINE").toString();
                es1.eqassid = ((SoapObject) soapchild.getProperty(0)).getProperty("ASSET").toString();
                es1.eqshop = ((SoapObject) soapchild.getProperty(0)).getProperty("SHOPNO").toString();
                es1.eqline = ((SoapObject) soapchild.getProperty(0)).getProperty("LINE").toString();
                es1.eqip = ((SoapObject) soapchild.getProperty(0)).getProperty("IP").toString();
            } catch (Exception ex1) {
                es1 = null;
            }
            return es1;
        }
        catch(Exception ex1)
        {
            return null;
        }

    }

    /**
     *2016/6/15 by tod 獲取設備狀態
     * @param Eqid  設備ID
     * @return
     */
    public  String getRemoteInfo_eqrepairstatus(String Eqid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "machinestate";
            String SOAP_ACTION = "http://tempuri.org/machinestate";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinenoid", Eqid);
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

    /**
     *2020/10/29 by Lyh EOL QC点检有问题发预警邮件
     * @param str_mail 邮件内容
     * @return
     */
    public String ChecksendMail(String str_mail) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "ChecksendMail";
            String SOAP_ACTION = "http://tempuri.org/ChecksendMail";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("str_mail", str_mail);
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

            String returnstr;             // -1=獲取資料失敗
            try {
                returnstr = r1.getProperty(0).toString(); // 0 发送邮件成功

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

    /**
     * 2016/06/18 by tod 報修系統報修接口
     * @param userid
     * @param errorno
     * @param Eqid
     * @param IP
     * @return
     */
    public  String getRemoteInfo_eqrepaircallrepair(String userid,String errorno,String Eqid,String IP)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "CallMachine";
            String SOAP_ACTION = "http://tempuri.org/CallMachine";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("opname", userid);
            rpc.addProperty("errorno", errorno);
            rpc.addProperty("machinenoid", Eqid);
            rpc.addProperty("ip", IP);
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
                returnstr = r1.getProperty(0).toString().trim();
                returnstr = new machinerepairmsg().getreasoncode(returnstr);
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

    /**
     * 2016/06/18 by tod 報修系統啟修接口
     * @param userid
     * @param Eqid
     * @param IP
     * @return
     */
    public  String getRemoteInfo_eqrepairstartrepair(String userid,String Eqid,String IP)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "sendmachine";
            String SOAP_ACTION = "http://tempuri.org/sendmachine";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("opname", userid);
            //rpc.addProperty("password", password);
            rpc.addProperty("machinenoid", Eqid);
            rpc.addProperty("ipaddress", IP);
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
                returnstr = r1.getProperty(0).toString().trim();
                returnstr = new machinerepairmsg().getreasoncode(returnstr);
            } catch (Exception ex1) {
                returnstr = "-1";
            }

            return returnstr;
        }
        catch(Exception ex1) {
            return null;
        }
    }

    /**
     * 2016/06/18 by tod 報修系統完修接口
     * @param userid
     * @param password
     * @param Eqid
     * @param IP
     * @param reason
     * @return
     */
    public  String getRemoteInfo_eqrepairfinishrepair(String userid,String password,String Eqid,String IP,String reason)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            //String METHOD_NAME = "servicemachine";
            //String SOAP_ACTION = "http://tempuri.org/servicemachine";
            String METHOD_NAME = "servicemachine";
            String SOAP_ACTION = "http://tempuri.org/servicemachine";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("opname", userid);
            //rpc.addProperty("password", password);
            rpc.addProperty("machinenoid", Eqid);
            rpc.addProperty("ipaddress", IP);
            rpc.addProperty("reason", reason);
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
                returnstr = r1.getProperty(0).toString().trim();
                returnstr = new machinerepairmsg().getreasoncode(returnstr);
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

    /**
     * 2016/06/21 by tod 報修系統驗收接口
     * @param userid
     * @param password
     * @param Eqid
     * @param IP
     * @return
     */
    public  String getRemoteInfo_eqrepaircloserepair(String userid,String password,String Eqid,String IP)
    {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

           // String METHOD_NAME = "endmachine";
           // String SOAP_ACTION = "http://tempuri.org/endmachine";
             String METHOD_NAME = "endmachine";
             String SOAP_ACTION = "http://tempuri.org/endmachine";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("opname", userid);
            rpc.addProperty("password", password);
            rpc.addProperty("machinenoid", Eqid);
            rpc.addProperty("ipaddress", IP);
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
                returnstr = r1.getProperty(0).toString().trim();
                returnstr = new machinerepairmsg().getreasoncode(returnstr);
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

    /**
     * 2016/06/22 by tod 報修系統任務列表
     * @param userid
     * @return
     */
    public  List<machinetaskcls> getRemoteInfo_eqrepairtask(String userid)
    {

        try {
            String NAMESPACE = "http://tempuri.org/";
            //String URL = "http://10.142.136.222:8107/Service.asmx";
            String METHOD_NAME = "machinetasklist";
            String SOAP_ACTION = "http://tempuri.org/machinetasklist";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("userid", userid);
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
            List<machinetaskcls> ls1;
            ls1 = new ArrayList<machinetaskcls>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    machinetaskcls ecls1 = new machinetaskcls();
                    try {
                        ecls1.MACHINENO = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.MACHINENO="";
                    }
                    try {
                        ecls1.STATE = ((SoapObject) soapchild.getProperty(i)).getProperty("STATE").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.STATE="";
                    }
                    try {
                        ecls1.REPAIRDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("REPAIRDATE").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.REPAIRDATE="";
                    }
                    try {
                        ecls1.REPAIROP = ((SoapObject) soapchild.getProperty(i)).getProperty("REPAIROP").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.REPAIROP="";
                    }
                    try {
                        ecls1.SENDDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("SENDDATE").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.SENDDATE="";
                    }
                    try {
                        ecls1.SENDOP = ((SoapObject) soapchild.getProperty(i)).getProperty("SENDOP").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.SENDOP="";
                    }
                    try {
                        ecls1.SERVICEDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("SERVICEDATE").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.SERVICEDATE="";
                    }
                    try {
                        ecls1.SERVICEOP = ((SoapObject) soapchild.getProperty(i)).getProperty("SERVICEOP").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.SERVICEOP="";
                    }
                    try {
                        ecls1.MACHINE = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINE").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.MACHINE="";
                    }
                    try {
                        ecls1.DOWNFLAG = ((SoapObject) soapchild.getProperty(i)).getProperty("DOWNFLAG").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.DOWNFLAG="";
                    }
                    try {
                        ecls1.ENDDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("ENDDATE").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.ENDDATE="";
                    }
                    try {
                        ecls1.ENDOP = ((SoapObject) soapchild.getProperty(i)).getProperty("ENDOP").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.ENDOP="";
                    }
                    try {
                        ecls1.PECONFIRMOP = ((SoapObject) soapchild.getProperty(i)).getProperty("PECONFIRMOP").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.PECONFIRMOP="";
                    }
                    try {
                        ecls1.QCFLAG = ((SoapObject) soapchild.getProperty(i)).getProperty("QCFLAG").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.QCFLAG="";
                    }
                    try {
                        ecls1.ATT9 = ((SoapObject) soapchild.getProperty(i)).getProperty("ATT9").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.ATT9="";
                    }
                    try {
                        ecls1.MACHINENOID = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENOID").toString();
                    }
                    catch(Exception ex1)
                    {
                        ecls1.MACHINENOID="";
                    }
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;

        }
        catch(Exception ex1)
        {
            return null;
        }
    }


    /**
     * 2016/06/18 by tod get ERRS call eq repair reason
     * @param Eqid
     * @return
     */
    public  List<String> getRemoteInfo_geterrepairreason(String Eqid)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "GetErrorno";
            String SOAP_ACTION = "http://tempuri.org/GetErrorno";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinenoid", Eqid);

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
            List<String> ls1;             // -1=獲取資料失敗
            try {
                ls1 = new ArrayList<String>();
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("DOWNFLAG").toString());
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1) {
            return null;
        }
    }

    /**
     * 2016/12/26 by jiaojiao get UF  GlueNo
     * @param Eqid
     * @return
     */
    public  String getRemoteInfo_geteqrepairufglueno(String Eqid)
    {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "searchglueid";
            String SOAP_ACTION = "http://tempuri.org/searchglueid";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinenoid", Eqid);
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
            String returnstr;
            try {
                returnstr = r1.getProperty(0).toString();

            } catch (Exception ex1) {
                returnstr = null;
            }

            return returnstr;
        }
        catch(Exception ex1)
        {
            return null;
        }

    }
    /**
     *  2016/6/16 獲取得備品儲位庫存清單 by tod
     * @param rssid
     * @return
     */
    public  List<reservedata> getRemoteInfo_getreservedata(String rssid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getbeipinlistbystoreno";
            String SOAP_ACTION = "http://tempuri.org/getbeipinlistbystoreno";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("storeno", rssid);
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
            List<reservedata> lrsd = new ArrayList<>();

            //reservedata rsd1;
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    reservedata rsd1 = new reservedata();
                    //es1=new eqbasedata_sdata();
                    rsd1.productid = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTID").toString();
                    rsd1.productname = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTNAME").toString();
                    rsd1.productnamecn = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTNAMEZH").toString();
                    rsd1.productno = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCT").toString();
                    rsd1.revrule = ((SoapObject) soapchild.getProperty(i)).getProperty("GUIGE").toString();
                    rsd1.machineno = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINE").toString();
                    rsd1.ropno = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNO").toString();
                    rsd1.storeqty = ((SoapObject) soapchild.getProperty(i)).getProperty("STOREQTY").toString();
                    rsd1.safeqty = ((SoapObject) soapchild.getProperty(i)).getProperty("SAFEQTY").toString();
                    rsd1.suppliermanu = ((SoapObject) soapchild.getProperty(i)).getProperty("MANUFACTURER").toString();
                    rsd1.site = ((SoapObject) soapchild.getProperty(i)).getProperty("SITE").toString();
                    rsd1.area = ((SoapObject) soapchild.getProperty(i)).getProperty("AREA").toString();
                    rsd1.floor = ((SoapObject) soapchild.getProperty(i)).getProperty("LOCATION").toString();

                    lrsd.add(rsd1);
                }
            } catch (Exception ex1) {
                lrsd = null;
            }

            return lrsd;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     * 獲得備品系統工程叫料清單
     * @param userid
     * @return
     */
    public  List<reservetask> getRemoteInfo_getreservetask(String userid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getbeipinshisendtask";
            String SOAP_ACTION = "http://tempuri.org/getbeipinshisendtask";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("loginuserid", userid);
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
            List<reservetask> lrsd = new ArrayList<>();

            //reservedata rsd1;
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    reservetask rsd1 = new reservetask();

                    rsd1.KEYNAME= ((SoapObject) soapchild.getProperty(i)).getProperty("KEYNAME").toString();
                    rsd1.productid = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTID").toString();
                    rsd1.productname = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTNAMEZH").toString();
                    rsd1.productnamecn = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTNAMEZH").toString();
                    //rsd1.productno = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCT").toString();
                    rsd1.revrule = ((SoapObject) soapchild.getProperty(i)).getProperty("GUIGE").toString();
                    rsd1.DEMANDQTY = ((SoapObject) soapchild.getProperty(i)).getProperty("DEMANDQTY").toString();
                    rsd1.STOREAREA = ((SoapObject) soapchild.getProperty(i)).getProperty("STOREAREA").toString();
                    rsd1.STORELOCAL = ((SoapObject) soapchild.getProperty(i)).getProperty("STORELOCAL").toString();
                    rsd1.LINENAME = ((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString();
                    rsd1.STORESTATION = ((SoapObject) soapchild.getProperty(i)).getProperty("STORESTATION").toString();
                    rsd1.DEMANDREASON = ((SoapObject) soapchild.getProperty(i)).getProperty("DEMANDREASON").toString();
                    rsd1.DEMANDUSER = ((SoapObject) soapchild.getProperty(i)).getProperty("DEMANDUSER").toString();
                    rsd1.DEMANDDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("DEMANDDATE").toString();

                    lrsd.add(rsd1);
                }
            } catch (Exception ex1) {
                lrsd = null;
            }

            return lrsd;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2016/6/16 by tod 獲取出入庫原因
     * @return
     */
    public  List<reservereasioninout> getRemoteInfo_getreserveinoutreasion() {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getchurureason";
            String SOAP_ACTION = "http://tempuri.org/getchurureason";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
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
            List<reservereasioninout> lrss1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    reservereasioninout rss1 = new reservereasioninout();
                    //es1=new eqbasedata_sdata();
                    rss1.reasonno = ((SoapObject) soapchild.getProperty(i)).getProperty("reasonno").toString();
                    rss1.reason = ((SoapObject) soapchild.getProperty(i)).getProperty("reason").toString();

                    lrss1.add(rss1);
                }
            } catch (Exception ex1) {
                lrss1 = null;
            }

            return lrss1;
        }
        catch(Exception ex1) {
            return  null;
        }
    }

    /**
     * 2016/6/16 by tod 備品出庫SOAP接口
     * @param shift 班別
     * @param area 區域FOL/EOL/SMT
     * @param location 樓層
     * @param linename 出庫線體
     * @param productid 部件編碼
     * @param reasonno 出庫原因
     * @param requireuser 需求者
      * @param qty  出/入库数量
     * @param userid 創建者
     * @return ”true“成功，其它則失敗
     */

    public  String getRemoteInfo_reserveoutstock(String shift,String area,String location,String linename,String productid,String reasonno,
                                                 String requireuser,String qty,String userid,String keynotice)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "beipinchukumethod";
            String SOAP_ACTION = "http://tempuri.org/beipinchukumethod";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("shift", shift);
            rpc.addProperty("area", area);
            rpc.addProperty("location", location);
            rpc.addProperty("linename", linename);
            rpc.addProperty("productid", productid);
            rpc.addProperty("reasonno", reasonno);
            rpc.addProperty("lenguser", requireuser);
            rpc.addProperty("qty", qty);
            rpc.addProperty("userid", userid);
            rpc.addProperty("engformid",keynotice);

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
                returnstr = r1.getProperty(0).toString();

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

    /**
     * 2016/6/16 by tod 備品入庫SOAP接口
     * @param shift 班別
     * @param area 區域FOL/EOL/SMT
     * @param location 樓層
     * @param productid 部件編碼
     * @param qty  出/入库数量
     * @param buytype 入库类型(Y/N  Y表示购买入库，N表示其他方式)
     * @param ecsid 请购单号
     * @param userid 創建者
     * @return ”true“成功，其它則失敗
     */
    public  String getRemoteInfo_reserveinstock(String shift,String area,String location,String productid,String qty,String buytype,String ecsid,String userid)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "beipinrukumethod";
            String SOAP_ACTION = "http://tempuri.org/beipinrukumethod";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("shift", shift);
            rpc.addProperty("area", area);
            rpc.addProperty("location", location);
            rpc.addProperty("productid", productid);
            rpc.addProperty("qty", qty);
            rpc.addProperty("buytype", buytype);
            rpc.addProperty("ecsid", ecsid);
            rpc.addProperty("userid", userid);

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
                returnstr = r1.getProperty(0).toString();

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


    /**
     * 20160616 by tod獲取車間樓層對應線體信息
     * @param area 車間FOL/EOL/SMT
     * @param location 樓層
     * @return 線體清單
     */
    public  List<String> getRemoteInfo_resergetline(String area,String location)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "bindlinename";
            String SOAP_ACTION = "http://tempuri.org/bindlinename";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("area", area);
            rpc.addProperty("location", location);

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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("LINE_NAME").toString());
                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }


    /**
     * 点击对应表单  使其变成正常状态
     * 田鹏鹏  20180601
     */
    public String updatetablestate(String checkdataid,String userid,String description) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "updatetablestate";
            String SOAP_ACTION = "http://tempuri.org/updatetablestate";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("checkdataid", checkdataid);
            rpc.addProperty("userid", userid);
            rpc.addProperty("description", description);

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
            String soapresult1 = r1.getProperty(0).toString();
            return soapresult1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }




    /**
     * 20180531 by tpp獲取MP點檢平臺任務列表
     * @return
     */
    public  List<MPBean> GetNewCheckStructhead_O()
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "GetNewCheckStructhead_O";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckStructhead_O";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);

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


            List<MPBean> ls1;
            ls1 = new ArrayList<MPBean>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    MPBean ecls1 = new MPBean();

                    ecls1.setMACHINENO(((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString());
                    ecls1.setCHECKTIME(((SoapObject) soapchild.getProperty(i)).getProperty("CHECKTIME").toString());
                    ecls1.setISEND(((SoapObject) soapchild.getProperty(i)).getProperty("ISEND").toString());
                    ecls1.setLINENAME(((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString());
                    ecls1.setOPNAME(((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString());
                    ecls1.setCHECKDATAID(((SoapObject) soapchild.getProperty(i)).getProperty("CHECKDATAID").toString());
                    ecls1.setMACHINESYSID(((SoapObject) soapchild.getProperty(i)).getProperty("MACHINESYSID").toString());
                    ecls1.setFILEVERSION(((SoapObject) soapchild.getProperty(i)).getProperty("FILEVERSION").toString());
                    ecls1.setPUBLISHDATE(((SoapObject) soapchild.getProperty(i)).getProperty("PUBLISHDATE").toString());
                    try {
                        ecls1.setUPDATEDATE(((SoapObject) soapchild.getProperty(i)).getProperty("UPDATEDATE").toString());
                    }
                    catch (Exception ex1) {
                        ecls1.setPUBLISHDATE("");
                    }
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }



    /**
     *20160621 by tod獲取點檢平臺任務列表
     * @param userid
     * @param checktype
     * @return
     */
    public  List<eqchecktaskcls> getRemoteInfo_eqchecktask(String userid,String checktype)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

           // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "GetNewCheckStructheaddept";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckStructheaddept";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("userid", userid);
            rpc.addProperty("checktype", checktype);

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


            List<eqchecktaskcls> ls1;
            ls1 = new ArrayList<eqchecktaskcls>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    eqchecktaskcls ecls1 = new eqchecktaskcls();

                    ecls1.MACHINENO = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                    ecls1.CHECKDATAID = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKDATAID").toString();
                    ecls1.ISEND = ((SoapObject) soapchild.getProperty(i)).getProperty("ISEND").toString();
                    ecls1.OPNAME = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString();
                    ecls1.LINENAME = ((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString();
                    ecls1.USERDEPT = ((SoapObject) soapchild.getProperty(i)).getProperty("USERDEPT").toString();
                    ecls1.FILEVERSION = ((SoapObject) soapchild.getProperty(i)).getProperty("FILEVERSION").toString();
                    ecls1.DEVICENO = ((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString();
                    ecls1.MACHINESYSID = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINESYSID").toString();
                    try {
                        ecls1.UPDATEDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("UPDATEDATE").toString();
                    }
                    catch (Exception ex1) {
                        ecls1.UPDATEDATE = "";
                    }

                    try {
                        ecls1.CHECKMODLE = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKMODLE").toString();
                    }
                    catch (Exception ex1) {
                        ecls1.CHECKMODLE = "";
                    }

                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }
    /**
    *201170108 by jiaojiao獲取點檢平臺任務列表
    * @param userid
    * @param
    * @return
            */
    public  List<eqchecktaskcls> getRemoteInfo_eqchecktasknew(String tablename, String userid, String location, String machine, String deviceno)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "GetNewCheckStructheaddeptAddTablenameSelect";
            String SOAP_ACTION = "http://tempuri.org/GetNewCheckStructheaddeptAddTablenameSelect";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("tablename", tablename);
            rpc.addProperty("userid", userid);
            rpc.addProperty("location", location);
            rpc.addProperty("machine", machine);
            rpc.addProperty("deviceno", deviceno);
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


            List<eqchecktaskcls> ls1;
            ls1 = new ArrayList<eqchecktaskcls>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    eqchecktaskcls ecls1 = new eqchecktaskcls();

                    ecls1.MACHINENO = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                    ecls1.CHECKDATAID = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKDATAID").toString();
                    ecls1.ISEND = ((SoapObject) soapchild.getProperty(i)).getProperty("ISEND").toString();
                    ecls1.OPNAME = ((SoapObject) soapchild.getProperty(i)).getProperty("OPNAME").toString();
                    ecls1.LINENAME = ((SoapObject) soapchild.getProperty(i)).getProperty("LINENAME").toString();
                    ecls1.USERDEPT = ((SoapObject) soapchild.getProperty(i)).getProperty("USERDEPT").toString();
                    ecls1.FILEVERSION = ((SoapObject) soapchild.getProperty(i)).getProperty("FILEVERSION").toString();
                    ecls1.DEVICENO = ((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString();
                    ecls1.MACHINESYSID = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINESYSID").toString();
                    try {
                        ecls1.UPDATEDATE = ((SoapObject) soapchild.getProperty(i)).getProperty("UPDATEDATE").toString();
                    }
                    catch (Exception ex1) {
                        ecls1.UPDATEDATE = "";
                    }

                    try {
                        ecls1.CHECKMODLE = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKMODLE").toString();
                    }
                    catch (Exception ex1) {
                        ecls1.CHECKMODLE = "";
                    }
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }



    /**
     *
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    public  List<PysBean> getpysuserinfo(String userid)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getpysuserinfo";
            String SOAP_ACTION = "http://tempuri.org/getpysuserinfo";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("userid", userid);
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


            List<PysBean> ls1;
            ls1 = new ArrayList<PysBean>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    PysBean ecls1 = new PysBean();

                    ecls1.setUSERID(((SoapObject) soapchild.getProperty(i)).getProperty("USERID").toString());
                    ecls1.setUSERNAME(((SoapObject) soapchild.getProperty(i)).getProperty("USERNAME").toString());
                    ecls1.setEITEM8(((SoapObject) soapchild.getProperty(i)).getProperty("EITEM8").toString());
                    ecls1.setGROUPNAME(((SoapObject) soapchild.getProperty(i)).getProperty("GROUPNAME").toString());
                    try {
                        ecls1.setOPNO(((SoapObject) soapchild.getProperty(i)).getProperty("OPNO").toString());
                    } catch(Exception ex1) {
                        ecls1.setOPNO("");
                    }
                    ecls1.setSHIFT(((SoapObject) soapchild.getProperty(i)).getProperty("SHIFT").toString());
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    public List<PysBean3> getpysusercodeinfo(String dates, String userid) {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getpysusercodeinfo";
            String SOAP_ACTION = "http://tempuri.org/getpysusercodeinfo";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("dates", dates);
            rpc.addProperty("userid", userid);

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


            List<PysBean3> ls1;
            ls1 = new ArrayList<PysBean3>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    PysBean3 ecls1 = new PysBean3();

                    ecls1.setUSERID(((SoapObject) soapchild.getProperty(i)).getProperty("USERID").toString());
                    ecls1.setUSERNAME(((SoapObject) soapchild.getProperty(i)).getProperty("USERNAME").toString());
                    ecls1.setSHIFT(((SoapObject) soapchild.getProperty(i)).getProperty("SHIFT").toString());
                    ecls1.setDEPARTNO(((SoapObject) soapchild.getProperty(i)).getProperty("DEPARTNO").toString());
                    try {
                        ecls1.setOPNO(((SoapObject) soapchild.getProperty(i)).getProperty("OPNO").toString());
                    } catch (Exception ex1) {
                        ecls1.setOPNO("");
                    }
                    ecls1.setID(((SoapObject) soapchild.getProperty(i)).getProperty("ID").toString());
                    ecls1.setXIANGMU(((SoapObject) soapchild.getProperty(i)).getProperty("XIANGMU").toString());
                    ecls1.setDATES(((SoapObject) soapchild.getProperty(i)).getProperty("DATES").toString());
                    ecls1.setFENZHI(((SoapObject) soapchild.getProperty(i)).getProperty("FENZHI").toString());
                    ecls1.setZHUBIE(((SoapObject) soapchild.getProperty(i)).getProperty("ZHUBIE").toString());
                    ecls1.setCREATETIME(((SoapObject) soapchild.getProperty(i)).getProperty("CREATETIME").toString());
                    ecls1.setCREATOR(((SoapObject) soapchild.getProperty(i)).getProperty("CREATOR").toString());
                    ecls1.setPADNO(((SoapObject) soapchild.getProperty(i)).getProperty("PADNO").toString());
                    try {
                        ecls1.setDESCRITION(((SoapObject) soapchild.getProperty(i)).getProperty("DESCRITION").toString());
                    } catch (Exception ex1) {
                        ecls1.setDESCRITION("");
                    }

                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    public  List<PysBenTwo> getpysproject(String groupname)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getpysproject";
            String SOAP_ACTION = "http://tempuri.org/getpysproject";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("groupname", groupname);

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


            List<PysBenTwo> ls1;
            ls1 = new ArrayList<PysBenTwo>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    PysBenTwo ecls1 = new PysBenTwo();

                    ecls1.setID(((SoapObject) soapchild.getProperty(i)).getProperty("ID").toString());
                    ecls1.setXIANGMU(((SoapObject) soapchild.getProperty(i)).getProperty("XIANGMU").toString());
                    ecls1.setBIZHONG(((SoapObject) soapchild.getProperty(i)).getProperty("BIZHONG").toString());
                    ecls1.setZLLY(((SoapObject) soapchild.getProperty(i)).getProperty("ZLLY").toString());
                    ecls1.setZLLRID(((SoapObject) soapchild.getProperty(i)).getProperty("ZLLRID").toString());
                    ecls1.setZHUBIE(((SoapObject) soapchild.getProperty(i)).getProperty("ZHUBIE").toString());
                    ecls1.setATT2(((SoapObject) soapchild.getProperty(i)).getProperty("ATT2").toString());
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    public  List<PysBenTwo> getpyssubject(String groupname)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getpyssubject";//getpysproject
            String SOAP_ACTION = "http://tempuri.org/getpyssubject";//getpysproject

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("groupname", groupname);

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


            List<PysBenTwo> ls1;
            ls1 = new ArrayList<PysBenTwo>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    PysBenTwo ecls1 = new PysBenTwo();

                    ecls1.setID(((SoapObject) soapchild.getProperty(i)).getProperty("ID").toString());
                    ecls1.setXIANGMU(((SoapObject) soapchild.getProperty(i)).getProperty("XIANGMU").toString());
                    ecls1.setBIZHONG(((SoapObject) soapchild.getProperty(i)).getProperty("BIZHONG").toString());
                    ecls1.setZLLY(((SoapObject) soapchild.getProperty(i)).getProperty("ZLLY").toString());
                    ecls1.setZLLRID(((SoapObject) soapchild.getProperty(i)).getProperty("ZLLRID").toString());
                    ecls1.setZHUBIE(((SoapObject) soapchild.getProperty(i)).getProperty("ZHUBIE").toString());
                    ecls1.setATT2(((SoapObject) soapchild.getProperty(i)).getProperty("ATT2").toString());
                    ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    public  List<TestBean> getpyscategory(String id)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getpyscategory";
            String SOAP_ACTION = "http://tempuri.org/getpyscategory";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("id", id);

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

            List<TestBean> ls1;
            ls1 = new ArrayList<TestBean>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    TestBean ecls1 = new TestBean();
                    ecls1.setID(((SoapObject) soapchild.getProperty(i)).getProperty("ID").toString());
                    ecls1.setSTYLE(((SoapObject) soapchild.getProperty(i)).getProperty("STYLE").toString());
                    ecls1.setJORJ(((SoapObject) soapchild.getProperty(i)).getProperty("JORJ").toString());
                    ecls1.setFENZHI(((SoapObject) soapchild.getProperty(i)).getProperty("FENZHI").toString());
                    ecls1.setMUST(((SoapObject) soapchild.getProperty(i)).getProperty("MUST").toString());
                    ecls1.setZHUBIE(((SoapObject) soapchild.getProperty(i)).getProperty("ZHUBIE").toString());
                    ecls1.setCREATOR(((SoapObject) soapchild.getProperty(i)).getProperty("CREATOR").toString());
                    ecls1.setCREATETIME(((SoapObject) soapchild.getProperty(i)).getProperty("CREATETIME").toString());
                    ecls1.setATT1("");
                    ecls1.setATT2("");
                    ecls1.setATT3("");
                    ecls1.setATT4("");
                    ecls1.setATT5("");
                    ecls1.setATT6("");
                    ecls1.setATT7("");
                    ecls1.setATT8("");
                    ecls1.setATT9("");
                    ecls1.setATT10("");

                        ls1.add(ecls1);
                }
            } catch (Exception ex1) {
                ex1.printStackTrace();
                //ls1 = null;
            }
        try {
            return ls1;
        } catch (Exception ex1) {
            ex1.printStackTrace();
            return null;
        }
        }
        catch(Exception ex1)
        {
            ex1.printStackTrace();
            return null;
        }
    }


 /**
     *
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    public  String commitPys(String pystypename,String id,String userid,String username,String shift,String dates,String fenzhi,String style,String zhubie,String creator,String descrition,String padno)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "insertpyscode";
            String SOAP_ACTION = "http://tempuri.org/insertpyscode";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("pystypename", pystypename);
            rpc.addProperty("id", id);
            rpc.addProperty("userid", userid);
            rpc.addProperty("username", username);
            rpc.addProperty("shift", shift);
            rpc.addProperty("dates", dates);
            rpc.addProperty("fenzhi", fenzhi);
            rpc.addProperty("style", style);
            rpc.addProperty("zhubie", zhubie);
            rpc.addProperty("creator", creator);
            rpc.addProperty("descrition", descrition);
            rpc.addProperty("padno", padno);


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
               String soapresult1 = r1.getProperty(0).toString();
               return soapresult1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *
     * @param xiangmu
     * @param creator
     * @param id
     * @param dates
     * @param shift
     * @param auditor
     * @param auditarea
     * @param defectdescribing
     * @param ownerconfirm
     * @param userid
     * @param defecttype
     * @param zhubie
     * @param descrition
     * @param padno
     * @return
     */

    public  String commitTest(String xiangmu,String creator,String id,String dates,String shift,
                              String auditor,String auditarea,String defectdescribing,String ownerconfirm,
                              String userid,String defecttype,String zhubie,String descrition,String padno)
    {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "insertpystrain";
            String SOAP_ACTION = "http://tempuri.org/insertpystrain";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("xiangmu", xiangmu);
            rpc.addProperty("creator", creator);
            rpc.addProperty("id", id);
            rpc.addProperty("dates", dates);
            rpc.addProperty("shift", shift);
            rpc.addProperty("auditor", auditor);
            rpc.addProperty("auditarea", auditarea);
            rpc.addProperty("defectdescribing", defectdescribing);
            rpc.addProperty("ownerconfirm", ownerconfirm);
            rpc.addProperty("userid", userid);
            rpc.addProperty("defecttype", defecttype);
            rpc.addProperty("zhubie", zhubie);
            rpc.addProperty("descrition", descrition);
            rpc.addProperty("padno", padno);


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
            String soapresult1 = r1.getProperty(0).toString();
            return soapresult1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }


    /**
     *  2016/9/17 by jiaojiao 獲取LH平板点检机种
     * @return
     */
    public  List<String> getRemoteInfo_geteqcheckdevicenolh(String type,String sysid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getmachinecheckdevicenolh";
            String SOAP_ACTION = "http://tempuri.org/getmachinecheckdevicenolh";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
//            rpc.addProperty("type", type);
            rpc.addProperty("sysid", sysid);
            rpc.addProperty("type", type);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENOTYPE").toString());

                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  根据sysid获取机台号等信息
     * @return
     */
    public  String getMachineNoBySysid(String sysid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getMachineOrNoBySysid";
            String SOAP_ACTION = "http://tempuri.org/getMachineOrNoBySysid";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("sysid", sysid);
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
            String res = "";
            SoapObject r1 = (SoapObject) envelope.bodyIn;
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    res = ((SoapObject) soapchild.getProperty(i)).getProperty("MACHINENO").toString();
                }
            } catch (Exception ex1) {
                res = "";
            }

            return res;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }


    /**
     *   根据机种获取点检类型
     * @return
     */
    public  List<String> getmachinechecktype(String deviceno) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getmachinechecktype";
            String SOAP_ACTION = "http://tempuri.org/getmachinechecktype";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("deviceno", deviceno);


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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
//                ls1.add("");
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                     Object o = ((SoapObject) soapchild.getProperty(i)).getProperty("CHECKTYPE");
                     ls1.add(o.toString());
                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2017/11/27 獲取LH平板點檢核查區域
     * @return
     */
    public  List<String> getCheckArea() {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getmachinecheck_location";
            String SOAP_ACTION = "http://tempuri.org/getmachinecheck_location";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
//                ls1.add("");
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("LOCATION").toString());
                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2018/03/31 檢查機台號是否可用
     * @return
     */
    public  String Checkmachineno(String machinenoid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getmachinecheck_location";
            String SOAP_ACTION = "http://tempuri.org/checkmachineflag";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinenoid", machinenoid);

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
            String str;

            try {
                // 获取返回的结果
                str = r1.getProperty(0).toString();

            } catch (Exception ex1) {
                return null;
            }
            return str;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2018/06/13 檢查是否有MP表单点检权限
     * @return
     */
    public  String checkuserauthority(String userid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "checkuserauthority";
            String SOAP_ACTION = "http://tempuri.org/checkuserauthority";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("RID", "R001");
            rpc.addProperty("userid", userid);

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
            String str;

            try {
                // 获取返回的结果
                str = r1.getProperty(0).toString();

            } catch (Exception ex1) {
                return null;
            }
            return str;
        } catch(Exception ex1){
            return null;
        }
    }


    /**
     *  2017/11/27 獲取LH平板點檢核查纖體
     * @return
     */
    public  List<String> getCheckLine(String line_name) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getline_namelh";
            String SOAP_ACTION = "http://tempuri.org/getline_namelh";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("location", line_name);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
//                ls1.add("");
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("LINE_NAME").toString());
                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     * 點檢核查查詢 數據
     * 2017/10/27 by pp
     */

    public List<CheckNewBean> getCheckData(String type,String deviceno,String searchdate,String location,String line ) {
        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getmachinechecklist_unfinish";
            String SOAP_ACTION = "http://tempuri.org/getmachinechecklist_unfinish";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("type", type);
            rpc.addProperty("deviceno", deviceno);
            rpc.addProperty("searchdate", searchdate);
            rpc.addProperty("location", location);
            rpc.addProperty("line", line);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(Staticdata.lhurl);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<CheckNewBean> list = new ArrayList<>();
            CheckNewBean bean;

            //reservedata rsd1;
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {


                }
            } catch (Exception ex1) {
                list = null;
            }

            return list;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }



    /**
     *  2017/10/17 by pp 提交
     * @return
     */
    public String commit(String machinesysid,String tablename,String userid,String deviceno,String modletype,String checkdata) {

        try {
            String NAMESPACE = "http://tempuri.org/";




            String METHOD_NAME = "createcheckdata";
            String SOAP_ACTION = "http://tempuri.org/createcheckdata";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinesysid", machinesysid);
            rpc.addProperty("tablename", tablename);
            rpc.addProperty("userid", userid);
            rpc.addProperty("deviceno", deviceno);
            rpc.addProperty("modletype", modletype);
            rpc.addProperty("checkdata", checkdata);
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

            return r1.getProperty(0).toString();
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2016/9/17 by jiaojiao 獲取LH平板点检樓層
     * @return
     */
    public  List<String> getRemoteInfo_geteqchecklocationlh(String type) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getLocationBulidinglhByLyh";
            String SOAP_ACTION = "http://tempuri.org/getLocationBulidinglhByLyh";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("type", type);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("LOCATIONSHOP").toString());
                }


            } catch (Exception ex1) {
                ls1 = null;

            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2016/9/17 by jiaojiao 獲取LH平板点检線體
     * @return
     */
    public  List<String> getRemoteInfo_geteqchecklinelh(String floor, String deviceno) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getline_namelhByLyh";
            String SOAP_ACTION = "http://tempuri.org/getline_namelhByLyh";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("location", floor);
            rpc.addProperty("deviceno", deviceno);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("LINE_NAME").toString());

                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *
     * @param userid
     * @param checkdataid
     * @return 刪除點檢表單
     */


    public  String delete(String userid,String checkdataid) {
        try {
            String NAMESPACE = "http://tempuri.org/";

            // String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "deletecheckdata";
            String SOAP_ACTION = "http://tempuri.org/deletecheckdata";
//            http://10.185.16.115:8096/Service.asmx?op=deletecheckdata

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("checkdataid", checkdataid);
            rpc.addProperty("userid", userid);

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
            String str;

            try {
                // 获取返回的结果
                str = r1.getProperty(0).toString();

            } catch (Exception ex1) {
                return null;
            }
               return str;

        } catch (Exception ex1) {
            return null;
        }
    }
    /**
     *  2016/9/17 by jiaojiao 獲取LH平板点检線體
     * @return
     */
    public  List<String> getRemoteInfo_geteqcheckopnamelh(String floor) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "getmachinenolhByLyh";
            String SOAP_ACTION = "http://tempuri.org/getmachinenolhByLyh";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("location", floor);
//            rpc.addProperty("line_name", linename);
//            rpc.addProperty("deviceno", deviceno);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {

                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("MACHINE").toString());

                }


            } catch (Exception ex1) {
                ls1 = null;
            }
            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }
    /**
     *  2016/12/14 by jiaojiao 獲取平板点检所有表單
     * @return
     */
    public  List<String> getRemoteInfo_geteqcheckforms(String Eqid,String userid) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME ="getTablenew";// "getTable";
            String SOAP_ACTION = "http://tempuri.org/getTablenew";// "http://tempuri.org/getTable";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("machinesysid", Eqid);
            rpc.addProperty("userid", userid);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("TABLENAME").toString());

                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }



    /**
     *  2016/9/17 bywll  獲取LH平板设备盘点区域
     * @return
     */
    public  List<String> getRemoteInfo_getmgreqcheckarea() {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "binddeptname";
            String SOAP_ACTION = "http://tempuri.org/binddeptname";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("dept").toString());
                }

            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2016/9/17 bywll  獲取LH平板设备盘点楼层
     * @return
     */
    public  List<String> getRemoteInfo_getmgreqcheckfloor() {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "bindfloorname";
            String SOAP_ACTION = "http://tempuri.org/bindfloorname";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("area", "");
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("BUILD_NAME").toString());
                }

            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *  2016/9/17 bywll  獲取LH平板设备盘点线体
     * @return
     */
    public  List<String> getRemoteInfo_getmgreqcheckline(String area,String floor) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            //String URL = "http://10.142.136.222:8107/Service.asmx";

            String METHOD_NAME = "bindlinename";
            String SOAP_ACTION = "http://tempuri.org/bindlinename";

            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("area", area);
            rpc.addProperty("location",floor);
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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("LINE_NAME").toString());
                }

            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**
     *2016/6/14 tod創建設備基本信息結構  by tod
     */
    public  class  eqbasedata
    {
        private String eqname;
        public String getEqname()
        {
            return eqname;
        }
        public void setEqname(String sendeqname)
        {
            eqname=sendeqname;
        }
        private String eqtype;
        public String getEqtype()
        {
            return eqtype;
        }
        public void setEqtype(String sendeqtype)
        {
            eqtype=sendeqtype;
        }
        private String eqassid;
        public String getEqassid()
        {
            return eqassid;
        }
        public void setEqassid(String sendeqassid)
        {
            eqassid=sendeqassid;
        }
        private String eqshop;
        public String getEqshop()
        {
            return eqshop;
        }
        public void setEqshop(String sendeqshop)
        {
            eqshop=sendeqshop;
        }

        private String eqline;
        public String getEqline()
        {
            return eqline;
        }
        public void setEqline(String sendeqline)
        {
            eqline=sendeqline;
        }
        private String eqip;
        public String getEqip()
        {
            return eqip;
        }
        public void setEqip(String sendeqid)
        {
            eqip=sendeqid;
        }
    }

    /**
     * 2016/6/16 tod創建儲位備品清單 by tod
     */
    public  class  reservedata
    {
        private String productid;
        public String getProductid() {
            return productid;
        }
        public void setProductid(String sendproductid) {
            productid = sendproductid;
        }

        private String productname;
        public String getProductname() {
            return productname;
        }
        public void setProductname(String sendproductname) {
            productname = sendproductname;
        }

        private String productnamecn;
        public String getProductnamecn() {
            return productnamecn;
        }
        public void setProductnamecn(String sendproductnamecn) {
            productnamecn = sendproductnamecn;
        }

        private String productno;
        public String getProductno() {
            return productno;
        }
        public void setProductno(String sendproductno) {
            productno = sendproductno;
        }

        private String revrule;
        public String getRevrule() {
            return revrule;
        }
        public void setRevrule(String sendrevrule) {
            revrule = sendrevrule;
        }

        private String machineno;
        public String getMachineno() {
            return machineno;
        }
        public void setMachineno(String sendmachineno) {
            machineno = sendmachineno;
        }

        private String ropno;
        public String getRopno() {
            return ropno;
        }
        public void setRopno(String sendropno) {
            ropno = sendropno;
        }

        private String storeqty;
        public String getStoreqty() {
            return storeqty;
        }
        public void setStoreqty(String sendstoreqty) {
            storeqty = sendstoreqty;
        }

        private String safeqty;
        public String getSafeqty() {
            return safeqty;
        }
        public void setSafeqty(String sendsafeqty) {
            safeqty = sendsafeqty;
        }

        private String suppliermanu;
        public String getSuppliermanu() {
            return suppliermanu;
        }
        public void setSuppliermanu(String sendsuppliermanu) {
            suppliermanu = sendsuppliermanu;
        }

        private String site;
        public String getSite() {
            return site;
        }
        public void setSite(String sendsite) {
            site = sendsite;
        }

        private String area;
        public String getArea() {
            return area;
        }
        public void setArea(String sendarea) {
            area = sendarea;
        }

        private String floor;
        public String getFloor() {
            return floor;
        }
        public void setFloor(String sendfloor) {
            floor = sendfloor;
        }
    }

    /**
     * 2016/6/23 by tod 創建備品任務列表
     */
    public  class  reservetask
    {

        private String KEYNAME;
        public String getKEYNAME() {
            return KEYNAME;
        }
        public void setKEYNAME(String sendKEYNAME) {
            KEYNAME = sendKEYNAME;
        }


        private String productid;
        public String getProductid() {
            return productid;
        }
        public void setProductid(String sendproductid) {
            productid = sendproductid;
        }

        private String productname;
        public String getProductname() {
            return productname;
        }
        public void setProductname(String sendproductname) {
            productname = sendproductname;
        }

        private String productnamecn;
        public String getProductnamecn() {
            return productnamecn;
        }
        public void setProductnamecn(String sendproductnamecn) {
            productnamecn = sendproductnamecn;
        }

        private String productno;
        public String getProductno() {
            return productno;
        }
        public void setProductno(String sendproductno) {
            productno = sendproductno;
        }

        private String revrule;
        public String getRevrule() {
            return revrule;
        }
        public void setRevrule(String sendrevrule) {
            revrule = sendrevrule;
        }


        private String DEMANDQTY;
        public String getDEMANDQTY() {
            return DEMANDQTY;
        }
        public void setDEMANDQTY(String sendDEMANDQTY) {
            DEMANDQTY = sendDEMANDQTY;
        }

        private String STOREAREA;
        public String getSTOREAREA() {
            return STOREAREA;
        }
        public void setSTOREAREA(String sendSTOREAREA) {
            STOREAREA = sendSTOREAREA;
        }

        private String STORELOCAL;
        public String getSTORELOCAL() {
            return STORELOCAL;
        }
        public void setSTORELOCAL(String sendSTORELOCAL) {
            STORELOCAL = sendSTORELOCAL;
        }

        private String LINENAME;
        public String getLINENAME() {
            return LINENAME;
        }
        public void setLINENAME(String sendLINENAME) {
            LINENAME = sendLINENAME;
        }

        private String STORESTATION;
        public String getSTORESTATION() {
            return STORESTATION;
        }
        public void setSTORESTATION(String sendSTORESTATION) {
            STORESTATION = sendSTORESTATION;
        }

        private String DEMANDREASON;
        public String getDEMANDREASON() {
            return DEMANDREASON;
        }
        public void setDEMANDREASON(String sendDEMANDREASON) {
            DEMANDREASON = sendDEMANDREASON;
        }

        private String DEMANDUSER;
        public String getDEMANDUSER() {
            return DEMANDUSER;
        }
        public void setDEMANDUSER(String sendDEMANDUSER) {
            DEMANDUSER = sendDEMANDUSER;
        }

        private String DEMANDDATE;
        public String getDEMANDDATE() {
            return DEMANDDATE;
        }
        public void setDEMANDDATE(String sendDEMANDDATE) {
            DEMANDDATE = sendDEMANDDATE;
        }
    }


    /**
     * 2016/6/16 by tod
     * 創建儲位庫存出入庫原因
     */
    public class reservereasioninout
    {
        private String reasonno;
        public String getReasonno()
        {
            return reasonno;
        }
        public void setReasonno(String sendreasonno)
        {
            reasonno=sendreasonno;
        }

        private String reason;
        public String getReason()
        {
            return reason;
        }
        public void setReason(String sendreason)
        {
            reason=sendreason;
        }
    }

    /**
     * 2016/6/18 by tod
     * 報修系統接口返回參數定義
     */
    public class machinerepairmsg
    {
        private String C802="更改機台狀態失敗";
        private String C803="參數不完整";
        private String C804="數據庫中不存在當機現象或異常現象異常";
        private String C805="該機台狀態錯誤";
        private String C806="機台編號不存在或編號異常";
        private String C807="群組ID不存在或群組ID異常";
        private String C808="工程師opname狀態錯誤";
        private String C809="沒有權限或密碼不正確";
        private String C810="更新人員狀態失敗";
        private String C811="啟修到完修時間要大於5分鐘";
        private String C812="維修結果不存在";
        private String C813="完成";
        private String C814="工程維修信息未錄入";
        private String C815="調整信息未錄入";

        public String getreasoncode(String reasoncode)
        {
            String rst=null;
            switch(reasoncode) {
                case "C802":
                    rst = reasoncode+":"+C802;
                    break;
                case "C803":
                    rst = reasoncode+":"+C803;
                    break;
                case "C804":
                    rst = reasoncode+":"+C804;
                    break;
                case "C805":
                    rst = reasoncode+":"+C805;
                    break;
                case "C806":
                    rst = reasoncode+":"+C806;
                    break;
                case "C807":
                    rst = reasoncode+":"+C807;
                    break;
                case "C808":
                    rst = reasoncode+":"+C808;
                    break;
                case "C809":
                    rst = reasoncode+":"+C809;
                    break;
                case "C810":
                    rst = reasoncode+":"+C810;
                    break;
                case "C811":
                    rst = reasoncode+":"+C811;
                    break;
                case "C812":
                    rst = reasoncode+":"+C812;
                    break;
                case "C813":
                    rst = reasoncode+":"+C813;
                    break;
                case "C814":
                    rst = reasoncode+":"+C814;
                    break;
                case "C815":
                    rst = reasoncode+":"+C815;
                    break;
                default:
                    rst=reasoncode;
                    /*
                    try
                    {
                        Integer.parseInt(reasoncode);
                        rst="C813:"+"完成";

                    }
                    catch(Exception e1) {
                        rst = reasoncode+":"+"CERR";
                    }
                    */
                    break;
            }
            return rst;
        }

    }

    /**
     * 2016/6/21 by tod
     *點檢平臺任務列表參數定義
     */
    public class eqchecktaskcls
    {
        public String MACHINENO;
        public String getMACHINENO()
        {
            return MACHINENO;
        }
        public void setMACHINENO(String sendmachineno)
        {
            MACHINENO=sendmachineno;
        }
        public String CHECKDATAID;
        public String getCHECKDATAID()
        {
            return CHECKDATAID;
        }
        public void setCHECKDATAID(String sendcheckdataid)
        {
            CHECKDATAID=sendcheckdataid;
        }
        public String ISEND;
        public String getISEND()
        {
            return ISEND;
        }
        public void setISEND(String sendisend)
        {
            ISEND=sendisend;
        }
        public String OPNAME;
        public String getOPNAME()
        {
            return OPNAME;
        }
        public void setOPNAME(String sendopname)
        {
            OPNAME=sendopname;
        }
        public String LINENAME;
        public String getLINENAME()
        {
            return LINENAME;
        }
        public void setLINENAME(String sendlinename)
        {
            LINENAME=sendlinename;
        }
        public String USERDEPT;
        public String getUSERDEPT()
        {
            return USERDEPT;
        }
        public void setUSERDEPT(String senduserdept)
        {
            USERDEPT=senduserdept;
        }
        public String FILEVERSION;
        public String getFILEVERSION()
        {
            return FILEVERSION;
        }
        public void setFILEVERSION(String sendfileversion)
        {
            FILEVERSION=sendfileversion;
        }
        public String DEVICENO;
        public String getDEVICENO()
        {
            return DEVICENO;
        }
        public void setDEVICENO(String sendDEVICENO)
        {
            DEVICENO=sendDEVICENO;
        }

        public String MACHINESYSID;
        public String getMACHINESYSID()
        {
            return MACHINESYSID;
        }
        public void setMACHINESYSID(String sendmachinesysid)
        {
            MACHINESYSID=sendmachinesysid;
        }//UPDATEDATE
        public String UPDATEDATE;
        public String getupdatedate()
        {
            return UPDATEDATE;
        }
        public void setupdatedate(String sendupdatedate)
        {
            UPDATEDATE=sendupdatedate;
        }

        public String CHECKMODLE;
        public String getCHECKMODLE()
        {
            return CHECKMODLE;
        }
        public void setCHECKMODLE(String sendCHECKMODLE)
        {
            CHECKMODLE=sendCHECKMODLE;
        }

    }

    /**
     * 2016/06/22 by tod
     * 報修系統任務列表參數定義
     */
    public class machinetaskcls
    {
        public String MACHINE;
        public String getMACHINE()
        {
            return MACHINE;
        }
        public void setMACHINE(String sendmachine)
        {
            MACHINE=sendmachine;
        }
        public String STATE;
        public String getSTATE()
        {
            return STATE;
        }
        public void setSTATE(String sendstate)
        {
            STATE=sendstate;
        }
        public String REPAIRDATE;
        public String getREPAIRDATE()
        {
            return REPAIRDATE;
        }
        public void setREPAIRDATE(String sendrepairdate)
        {
            REPAIRDATE=sendrepairdate;
        }
        public String REPAIROP;
        public String getREPAIROP()
        {
            return REPAIROP;
        }
        public void setREPAIROP(String sendrepairpop)
        {
            REPAIROP=sendrepairpop;
        }
        public String MACHINENO;
        public String getMACHINENO()
        {
            return MACHINENO;
        }
        public void setMACHINENO(String sendMACHINENO)
        {
            MACHINENO=sendMACHINENO;
        }
        public String DOWNFLAG;
        public String getDOWNFLAG()
        {
            return DOWNFLAG;
        }
        public void setDOWNFLAG(String sendDOWNFLAG)
        {
            DOWNFLAG=sendDOWNFLAG;
        }
        public String SENDDATE;
        public String getSENDDATE()
        {
            return SENDDATE;
        }
        public void setSENDDATE(String sendSENDDATE)
        {
            SENDDATE=sendSENDDATE;
        }
        public String SENDOP;
        public String getSENDOP()
        {
            return SENDOP;
        }
        public void setSENDOP(String sendSENDOP)
        {
            SENDOP=sendSENDOP;
        }
        public String SERVICEDATE;
        public String getSERVICEDATE()
        {
            return SERVICEDATE;
        }
        public void setSERVICEDATE(String sendSERVICEDATE)
        {
            SERVICEDATE=sendSERVICEDATE;
        }
        public String SERVICEOP;
        public String getSERVICEOP()
        {
            return SERVICEOP;
        }
        public void setSERVICEOP(String sendSERVICEOP)
        {
            SERVICEOP=sendSERVICEOP;
        }
        public String ENDDATE;
        public String getENDDATE()
        {
            return ENDDATE;
        }
        public void setENDDATE(String sendENDDATE)
        {
            ENDDATE=sendENDDATE;
        }
        public String ENDOP;
        public String getENDOP()
        {
            return ENDOP;
        }
        public void setENDOP(String sendENDOP)
        {
            ENDOP=sendENDOP;
        }
        public String PECONFIRMOP;
        public String getPECONFIRMOP()
        {
            return PECONFIRMOP;
        }
        public void setPECONFIRMOP(String sendPECONFIRMOP)
        {
            PECONFIRMOP=sendPECONFIRMOP;
        }
        public String ATT9;
        public String getATT9()
        {
            return ATT9;
        }
        public void setATT9(String sendATT9)
        {
            ATT9=sendATT9;
        }
        public String QCFLAG;
        public String getQCFLAG()
        {
            return QCFLAG;
        }
        public void setQCFLAG(String sendQCFLAG)
        {
            QCFLAG=sendQCFLAG;
        }
        public String MACHINENOID;
        public String getMACHINENOID()
        {
            return MACHINENOID;
        }
        public void setMACHINENOID(String sendmachinenoid)
        {
            MACHINENOID=sendmachinenoid;
        }
    }

}
