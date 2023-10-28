package app.cmapp.WebAPIClient;/**
 * Created by F5460007 on 2017/3/29.
 */

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import app.cmapp.DataTable.DataTable;
import app.cmapp.appcdl.FreedomDataCallBack;
import app.cmapp.appcdl.FreedomHttpListener;
import app.cmapp.appcdl.httprequestforsys;
import app.cmapp.parameterclass.httprequestinputdata;

/**
 * Owner:F5460007
 * CreateDate:2017/3/29 15:21
 */
public class CallWebapipublic {
    private String _url;
    private String _dbname;
    private Context _callContext;
    private String _rjsonstr;

    /**
     * 線程同步
     */
    private Object _locko=new Object();


    public CallWebapipublic(String sdbname, String surl, Context c)
            throws Exception {
        if (surl.isEmpty())
            throw new Exception("URL must not null");

        _callContext=c;
        _dbname = sdbname;
        _url = surl;
    }


    private DataTable getexec_old( String requestid,Hashtable otherparm)
            throws Exception {
        if (requestid.isEmpty()) {
            throw new Exception("requestit must not null");
        }
        List<httprequestinputdata> hi=new ArrayList<httprequestinputdata>();
        for(Iterator<String> itr = otherparm.keySet().iterator();itr.hasNext();){
            httprequestinputdata temphi=new httprequestinputdata();
            String key=(String)itr.next();
            temphi.setDataname(key);
            temphi.setDatavalue((String) otherparm.get(key));
            hi.add(temphi);
        }

        DataTable dt=new DataTable();
       synchronized (_locko) {
           getkeyindatainput_http(_url+requestid, hi, null, _callContext);
            _locko.wait();
           dt=jsonttovalue(_rjsonstr);

        }
        return dt;
    }


    private   DataTable jsonttovalue(String json)
            throws  JSONException,IOException,Exception
    {
        DataTable dt = new DataTable();
            JSONTokener jsonParser = new JSONTokener(json);
        JSONArray person = (JSONArray) jsonParser.nextValue();
        if(person.length()==0) {
            return dt;
        }
        JSONArray jaa =  person.getJSONObject(0).names();


            for (int j = 0; j < jaa.length(); j++) {
              dt.AddColumn(jaa.get(j).toString());
            }
            for (int i = 0; i < person.length(); i++) {
                JSONObject tempja = person.getJSONObject(i);
                String[] tempss = new String[tempja.length()];

                JSONArray tempjaa = tempja.names();
                for (int j = 0; j < tempja.length(); j++
                        ) {
                    tempss[j] =tempja.getString(jaa.get(j).toString());
                }
                dt.AddRow(tempss);
            }

        return dt;
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

    private  void getkeyindatainput_http(String urlpath, List<httprequestinputdata> hi,FreedomDataCallBack f,Context SourceContext)
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
                ,2,null);
        hf.postRequest(urlpath, hi);
    }

    /**
     *
     * @param rname
     * @param s
     * @return
     * @throws Exception
     */
    public DataTable CallRDT(String rname, Hashtable s)
    throws Exception
    {
        return getexec_old(rname, s);
    }
}