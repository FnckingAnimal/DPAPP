package app.dpapp.appcdl;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.Interfacearray.Statusinterface;

/**
 * Created by Administrator on 2016/7/6.
 */
public class jsontolist {

    /**
     * 解析Json字符為List集合
     * @param json
     * @return List<List<_valueitem></>></>
     */
    public  List<_jsonarray> jasontolist(String json)
    {
       // List<_jsonarray> llv1=null;
        //llv1=new ArrayList<_jsonarray>();
        //_jsonarray j1=new _jsonarray();

        List<_jsonarray> lj1=new ArrayList<>();
        JSONTokener jsonParser = new JSONTokener(json);
        while(jsonParser.more())
        {
            _jsonarray tempj=new _jsonarray();
            jsonParser.next();
            try
            {
                tempj.set_arrayname(jsonParser.nextValue().toString());
            }
            catch(JSONException je1)
            {
                tempj.set_arrayname("");
            }
            jsonParser.next();

            String jasonvalue="";
            try
            {
                jasonvalue=jsonParser.nextValue().toString();
            }
            catch(JSONException je1)
            {
                jasonvalue="ERROR";
            }
            if(jasonvalue.equals("ERROR")==false) {
                List<List<_valueitem>> llv2 = new ArrayList<List<_valueitem>>();
                JsonReader reader = new JsonReader(new StringReader(jasonvalue));
                try {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        List<_valueitem> lv1 = new ArrayList<_valueitem>();
                        while (reader.hasNext()) {
                            _valueitem tempvi1 = new _valueitem();
                            tempvi1.set_name(reader.nextName());
                            tempvi1.set_value(reader.nextString());
                            lv1.add(tempvi1);
                        }
                        reader.endObject();
                        llv2.add(lv1);
                        tempj.set_arraystatus(Statusinterface.STATUS_SEC);
                        tempj.set_valuelist(llv2);
                    }
                    reader.endArray();
                } catch (IOException ie1) {
                    tempj.set_arraystatus(Statusinterface.STATUS_FAIL);
                    tempj.set_valuelist(null);
                }
            }
            else
            {
                tempj.set_arraystatus(Statusinterface.STATUS_FAIL);
                tempj.set_valuelist(null);
            }

            lj1.add(tempj);
        }
        return lj1;
    }


    /**
     * 解析Json字符為List集合
     * @param json
     * @return List<List<_valueitem></>></>
     */
    public _jsonarray datasetjasontolist(String json)
    {
        // List<_jsonarray> llv1=null;
        //llv1=new ArrayList<_jsonarray>();
        _jsonarray j1=new _jsonarray();
        try {
            JSONTokener jsonParser = new JSONTokener(json);
            JSONObject person = (JSONObject) jsonParser.nextValue();
            JSONArray jaa1=person.names();
            if(jaa1==null)
            {
                j1._arrayname="";
                j1._arraystatus=Statusinterface.STATUS_FAIL;
                j1._valuelist=null;
                //llv1.add(j1);
                return j1;
            }
            String name=jaa1.getString(0);
            //person.getJSONObject(name).toString();
            //person.getJSONObject("LHLINKWIP").toString();
            JSONArray JA1=person.getJSONArray(name);
            if(JA1==null)
            {
                j1._arrayname=name;
                j1._arraystatus=Statusinterface.STATUS_NODATA;
                j1._valuelist=null;
                //llv1.add(j1);
                return j1;
            }

            // String jsonData1="[{\"name\":\"renhaili\",\"age\":20},{\"name\":\"zhouxiaodong\",\"age\":21}]";

            // for(int i=0;i<JA1.length();i++)
            // {
            List<List<_valueitem>> llv2=new ArrayList<List<_valueitem>>();
            JsonReader reader = new JsonReader(new StringReader(JA1.toString()));
            // JsonReader reader = new JsonReader(new StringReader(jsonData1));

            try {
                // _jsonarray j1=new _jsonarray();
                reader.beginArray();
                while(reader.hasNext()) {
                    reader.beginObject();
                    List<_valueitem> lv1=new ArrayList<_valueitem>();
                    while (reader.hasNext()) {
                        _valueitem tempvi1 = new _valueitem();
                        tempvi1._name = reader.nextName();
                        tempvi1._value = reader.nextString();
                        //tempvi1._name = reader.nextName();
                        lv1.add(tempvi1);
                        //String tagName = reader.nextName();
                        //String tagvalue=reader.nextString();
                    }
                    reader.endObject();
                    llv2.add(lv1);
                }
                reader.endArray();
                j1._arrayname=name;
                j1._arraystatus=Statusinterface.STATUS_SEC;
                j1._valuelist=llv2;
                // llv1.add(j1);
            }
            catch(IOException ie1)
            {
                j1._arrayname="";
                j1._arraystatus=Statusinterface.STATUS_RECKONFAIL;
                j1._valuelist=llv2;
                //llv1.add(j1);
                return j1;
            }
            // }
        } catch (Exception ex) {
            // 异常处理代码
            j1._arrayname="";
            j1._arraystatus=Statusinterface.STATUS_FAIL;
            j1._valuelist=null;
            //llv1.add(j1);
            return j1;
        }

        return j1;
    }

    public class _valueitem
    {
        private String _name;
        private String _value;
        public void set_name(String sendname)
        {
            _name=sendname;
        }
        public String get_name()
        {
            return _name;
        }
        public void set_value(String sendvalue)
        {
            _value=sendvalue;
        }
        public String get_value()
        {
            return _value;
        }
    }

    public  class _jsonarray
    {
        private String _arrayname;
        private int _arraystatus;
        private  List<List<_valueitem>> _valuelist;

        public void set_arrayname(String sendarrayname)
        {
            _arrayname=sendarrayname;
        }
        public String get_arrayname()
        {
            return _arrayname;
        }

        public void set_arraystatus(int sendarryastatus)
        {
            _arraystatus=sendarryastatus;
        }
        public int get_arraystatus()
        {
            return _arraystatus;
        }

        public void set_valuelist(List<List<_valueitem>> sendlv)
        {
            _valuelist=sendlv;
        }
        public List<List<_valueitem>> get_valuelist()
        {
            return _valuelist;
        }
    }
}
