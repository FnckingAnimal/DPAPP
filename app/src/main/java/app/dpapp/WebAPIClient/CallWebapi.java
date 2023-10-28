package app.dpapp.WebAPIClient;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import app.dpapp.DataTable.DataTable;
import app.dpapp.appcdl.FreedomDataCallBack;
import app.dpapp.appcdl.FreedomHttpListener;
import app.dpapp.appcdl.httprequestforsys;

/**
 * Owner:F5460007
 * CreateDate:2017/3/29 15:21
 */
public class CallWebapi  {
    private String _deviceno;
    private String _newdeviceno;
    private String _url = "http://10.151.128.78/Controllers/sfccontrol_2/getdata/";
    private String _dbname;
    private Context _callContext;
    private String _rjsonstr;

    /**
     * 線程同步
     */
    private Object _locko=new Object();

    public CallWebapi(String sdeviceno, String snewdeviceno, String sdbname,Context c)
            throws Exception {
        if (sdeviceno.isEmpty())
            throw new Exception("sdeviceno must not null");
        if (snewdeviceno.isEmpty())
            throw new Exception("snewdeviceno must not null");

        _callContext=c;
        _deviceno = sdeviceno;
        _newdeviceno = snewdeviceno;
        _dbname = sdbname;
    }

    public CallWebapi(String sdeviceno, String snewdeviceno, String sdbname, String surl,Context c)
            throws Exception {
        if (sdeviceno.isEmpty())
            throw new Exception("sdeviceno must not null");
        if (snewdeviceno.isEmpty())
            throw new Exception("snewdeviceno must not null");

        _callContext=c;
        _deviceno = sdeviceno;
        _newdeviceno = snewdeviceno;
        _dbname = sdbname;
        _url = surl;
    }

    private webapivalue getexec_old( String requestid,  String[] otherparam)
            throws Exception {
        if (requestid.isEmpty()) {
            throw new Exception("requestit must not null");
        }
             String Jsonstr = "{";
            Jsonstr += "\"static\":[{\"pname\":\"" + requestid + "\",\"dbname\":\"" + _dbname + "\"}],";
            Jsonstr += "\"Table1\":[{\"deviceno\":\"" + _deviceno + "\",\"newdeviceno\":\"" + _newdeviceno + "\",";

            if (otherparam != null) {
                for (int i = 0; i < otherparam.length; i++) {
                    if (i == otherparam.length - 1)
                        Jsonstr += "\"" + String.valueOf(i) + "\":\"" + otherparam[i] + "\"";
                    else
                        Jsonstr += "\"" + String.valueOf(i) + "\":\"" + otherparam[i] + "\",";
                }
            }
            Jsonstr += "}]}";

        webapivalue wr=new webapivalue();
       synchronized (_locko) {
           getkeyindatainput(_url, Jsonstr, null, _callContext);
            _locko.wait();
             wr=jsonttovalue(_rjsonstr);
            if (wr.status == "0") {
                throw new Exception(wr.msg);
            }
        }
        return wr;
    }


    private   webapivalue jsonttovalue(String json)  throws  JSONException,IOException,Exception
    {
        webapivalue wr = new webapivalue();

        webapivalue o = new webapivalue();
            JSONTokener jsonParser = new JSONTokener(json);
            JSONObject person = (JSONObject) jsonParser.nextValue();

            wr.status = person.get("status").toString();
            wr.msg = person.get("msg").toString();
            wr.requestname = person.get("requestname").toString();
            wr.rvalue = person.get("rvalue").toString();

           if (wr.status.equals("0"))
               throw  new Exception(wr.msg);

            JSONTokener jsonParser2 = new JSONTokener(wr.rvalue.toString());
            JSONObject person2 = (JSONObject) jsonParser2.nextValue();

            JSONArray jaa = person2.names();
            jaa.get(0).toString();

            switch (person2.getJSONArray("typedt").getJSONObject(0).get("RType").toString()) {
                case "String":
                    o.status = "1";
                    o.rvalue = person2.getJSONArray(jaa.get(1).toString()).getJSONObject(0).get("value").toString();
                    return o;
                case "DataTable":
                    o.status = "1";
                    DataTable dt = new DataTable();
                    if(person2.getJSONArray(jaa.get(1).toString()).length()>0)
                    {
                        for (int j = 0; j < person2.getJSONArray(jaa.get(1).toString()).getJSONObject(0).length(); j++) {
                            dt.AddColumn(person2.getJSONArray(jaa.get(1).toString()).getJSONObject(0).names().get(j).toString());
                        }
                        for (int i = 0; i < person2.getJSONArray(jaa.get(1).toString()).length(); i++) {
                            JSONObject tempja = person2.getJSONArray(jaa.get(1).toString()).getJSONObject(i);
                            String[] tempss = new String[tempja.length()];

                            JSONArray tempjaa = tempja.names();
                            for (int j = 0; j < tempja.length(); j++
                                    ) {
                                if("null".equals(tempja.getString(tempjaa.get(j).toString())))
                                {
                                    tempss[j] ="";
                                }
                                else {
                                    tempss[j] = tempja.getString(tempjaa.get(j).toString());
                                }
                            }
                            dt.AddRow(tempss);
                        }
                    }
                    o.rvalue = dt;
                    return o;
                case "Bool":
                    o.status = "1";
                    Boolean rb = false;
                    switch (person2.getJSONArray(jaa.get(1).toString()).getJSONObject(0).get("value").toString()) {
                        case "0":
                            rb = false;
                            break;
                        case "1":
                            rb = true;
                            break;
                        default:
                            rb = false;
                            break;
                    }
                    o.rvalue = rb;
                    return o;
                case "Int32":
                    o.status = "1";
                    o.rvalue = Integer.valueOf(person2.getJSONArray(jaa.get(1).toString()).getJSONObject(0).get("value").toString());
                    return o;
                case "Int64":
                    o.status = "1";
                    o.rvalue = Long.valueOf(person2.getJSONArray(jaa.get(1).toString()).getJSONObject(0).get("value").toString());
                    return o;
                default:
                    o.status = "0";
                    o.msg = "Unknow Return Type";
                    o.rvalue = null;
                    return o;
            }
    }
    /**
     * Author:Tod
     * Descirpiton:Get or Post發送Http請求
     * @param urlpath  訪問的HttpPath
     * @param li  參數列表
     * @param f  返回數據接口
     * @param SourceContext  請求頁面Context
     */
    private  void getkeyindatainput(String urlpath,String li,FreedomDataCallBack f,Context SourceContext)
            throws Exception{
        httprequestforsys hf=new httprequestforsys(_callContext, new FreedomHttpListener() {
            @Override
            public void action(int actionCode, Object object) {
                switch (actionCode) {
                    case FreedomHttpListener.EVENT_NOT_NETWORD:
                        break;

                    case FreedomHttpListener.EVENT_NETWORD_EEEOR:
                        break;
                    case FreedomHttpListener.EVENT_CLOSE_SOCKET:
                        break;
                    case FreedomHttpListener.EVENT_GET_DATA_EEEOR:
                        break;
                    case FreedomHttpListener.EVENT_GET_DATA_SUCCESS:
                        synchronized (_locko) {
                            _rjsonstr=null;
                            _rjsonstr=object.toString();
                            _locko.notify();
                        }
                        break;
                    case FreedomHttpListener.EVENT_NONOIMAGEFILA:
                        break;
                    default:
                        break;
                }
            }
        }
        ,4,null);
        hf.postRequest_webapi(urlpath, li);
    }

    /**
     *
     * @param rname
     * @param s
     * @return
     * @throws Exception
     */
    public DataTable CallRDT(String rname, String... s)
    throws Exception
    {
        String[] ss=s.clone();
        return (DataTable)getexec_old(rname, ss).rvalue;
    }


    /**
     * 呼叫WEBAPI,返回String
     * @param rname 請求時返回的識別碼,用戶自定與,用于用戶識別
     * @param s 传入参数，String，可传多个
     * @return
     * @throws Exception
     */
    public String CallRS(String rname, String... s)
    throws Exception
    {
        String[] ss=s.clone();
        return getexec_old(rname, ss).rvalue.toString();
    }

    /**
     * 呼叫WEBAPI,返回int
     * @param rname 請求時返回的識別碼,用戶自定與,用于用戶識別
     * @param s 传入参数，String，可传多个
     * @return
     * @throws Exception
     */
    public int CallRI(String rname, String... s) throws Exception{
        String[] ss=s.clone();
        return (int) getexec_old(rname, ss).rvalue;
    }

    /**
     * 呼叫WEBAPI,返回long
     * @param rname 請求時返回的識別碼,用戶自定與,用于用戶識別
     * @param s 传入参数，String，可传多个
     * @return
     * @throws Exception
     */
    public long CallRI64(String rname, String... s) throws Exception{
        String[] ss=s.clone();
        return (long) getexec_old(rname, ss).rvalue;
    }

    /**
     * 呼叫WEBAPI,返回bool
     * @param rname 請求時返回的識別碼,用戶自定與,用于用戶識別
     * @param s 传入参数，String，可传多个
     * @return
     * @throws Exception
     */
    public Boolean CallRB(String rname, String... s) throws Exception {
        String[] ss = s.clone();
        return (Boolean) getexec_old(rname, ss).rvalue;
    }
}