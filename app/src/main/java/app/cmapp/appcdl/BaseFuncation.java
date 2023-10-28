package app.cmapp.appcdl;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import app.cmapp.DataTable.DataTable;
import app.dpapp.R;

/**
 * Created by F5460007 on 2016/12/16.
 */
public  class  BaseFuncation {

    /**
     *
     * @param dt  數據源
     * @param ValueName  Value列名
     * @param TextName Text列名
     * @param c   Retunr Context
     * @return
     * @throws Exception
     */
    public static ArrayAdapter<FinalStaticCloass.SpinnerData> setvalue(DataTable dt,String ValueName,@NonNull String TextName,Context c) throws  Exception
    {
        List<FinalStaticCloass.SpinnerData> ls1 = new ArrayList<FinalStaticCloass.SpinnerData>();

        for(int i=0;i<dt.Rowscount();i++)
        {
            FinalStaticCloass s = new FinalStaticCloass();
            FinalStaticCloass.SpinnerData ss = s.new SpinnerData(
                    (ValueName==null?null:dt.Rows(i).get_CellValue(ValueName.toUpperCase())),
                    dt.Rows(i).get_CellValue(TextName.toUpperCase())
            );
            ls1.add(ss);
        }
        ArrayAdapter<FinalStaticCloass.SpinnerData> as2 =
                new ArrayAdapter<FinalStaticCloass.SpinnerData>(c, android.R.layout.simple_dropdown_item_1line,ls1);
        as2.setDropDownViewResource(R.layout.spinner_textview);
        return as2;
    }

    /**
     * 獲得SP當前的Value or Text
     * @param s 當前的SP
     * @param type 0:Value,1:Text
     * @return
     * @throws Exception
     */
    public static String getspvalue(Spinner s,int type) throws Exception
    {
        if(type==0) {
            return ((FinalStaticCloass.SpinnerData) s.getSelectedItem()).getValue();
        }
        if(type==1) {
            return ((FinalStaticCloass.SpinnerData) s.getSelectedItem()).getText();
        }
        return null;
    }

    public static String getnowdatetime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd    HH:mm:ss");
        return sDateFormat.format(new java.util.Date());
    }

    public static Date getnowdatetime(String sd,String Dateformat) throws Exception
    {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(Dateformat);
        return sDateFormat.parse(sd);
    }

    public static String getnowdatetime(Long sd,String fomratype) {
        Date dateOld = new Date(sd);
        return new SimpleDateFormat(fomratype).format(dateOld);
    }


    public static String getnowdatetime(String Format)
    {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(Format);
        return sDateFormat.format(new java.util.Date());
    }

    /** * Double轉換為按小數位格式的小數，如format為兩位，則裝換為兩位 *
     * @param value 需轉換的數字 *
     * @param format 轉換的格式，format==null時默認格式0.00， *
     * @return * @throws Exception */
    public static String doubletostring(Double value,String format) throws Exception
    {
        if (format == null)
            format = "0.00";
        DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
    }

    public static void showmessage(Context c,String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static String SerializeObjectArrayString ( String... s) throws  Exception{
        String rs="[";
        for (String ss:s
                ) {
            rs += "\\\"" + ss + "\\\"" + ",";
        }
        rs=rs.substring(0,rs.length()-1);
        rs+="]";
        return rs;
    }

    /**
     * 左補位
     * @param oriStr 原字符竄
     * @param len 目標長度
     * @param alexin 補位字符
     * @return
     */
    public static String padLeft(String oriStr,int len,char alexin) {
        String str = "";
        int strlen = oriStr.length();
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        }
        str = str + oriStr;
        return str;
    }

    public static String SerializeObjectDataTable ( DataTable s) throws  Exception{
        String rs="[";
        for (int i=0;i<s.Rowscount();i++)
        {
            rs+="{";
            for(int j=0;j<s.Columnscount();j++) {
                rs += "\"" + s.Columns(j).getValue() + "\":\"" + s.Rows(i).get_CellValue(j) + "\"" + ",";
            }
            rs=rs.substring(0,rs.length()-1);
            rs+="},";
        }
        rs=rs.substring(0,rs.length()-1);
        rs+="]";
        return rs;
    }

    public enum DialogResult
    {
        OK,Cancel
    };

    public class createspdata {
        private List<sipstring> _lsa;

        public createspdata() {
            _lsa = new ArrayList<sipstring>();
        }

        public ArrayAdapter<FinalStaticCloass.SpinnerData> getspdata(Context c) {
            try {
                if (_lsa == null) {
                    return null;
                }
                List<FinalStaticCloass.SpinnerData> ls = new ArrayList<FinalStaticCloass.SpinnerData>();
                for (sipstring sa : _lsa) {
                    FinalStaticCloass s = new FinalStaticCloass();
                    FinalStaticCloass.SpinnerData ss = s.new SpinnerData(sa.get_value(), sa.get_text());
                    ls.add(ss);
                }
                ArrayAdapter<FinalStaticCloass.SpinnerData> as1 =
                        new ArrayAdapter<FinalStaticCloass.SpinnerData>(c, android.R.layout.simple_dropdown_item_1line, ls);
                as1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return as1;
            } catch (Exception e) {
                return null;
            }
        }

        public void setvalue(String v, String t) {
            if (v == null || t == null) {
                return;
            }
            sipstring s = new sipstring();
            s.set_text(t);
            s.set_value(v);

            _lsa.add(s);
        }


        private  class sipstring {
            private String _value;

            public void set_value(String v) {
                _value = v;
            }

            public String get_value() {
                return _value;
            }

            private String _text;

            public void set_text(String v) {
                _text = v;
            }

            public String get_text() {
                return _text;
            }
        }


    }

    public static class Basecheck
    {
        public static boolean checkobjectisnull(Object o)
        {
            if(o!=null) {
                return true;
            }
            else {
                return false;
            }
        }
        public static  boolean checkstringnullandlength(String v) {
            if (v != null) {
                if (v.trim().length() > 0)
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        }
    }

    /**
     * 返回結果類
     */
    public class rrtype
    {
        private boolean _rstatus;
        private String _rmsg="Success";

        public void set_rmsg(String _rmsg) {
            this._rmsg = _rmsg;
        }
        public void set_rstatus(boolean _rstatus) {
            this._rstatus = _rstatus;
        }
        public boolean get_rstatus()
        {
            return this._rstatus;
        }
        public String get_rmsg()
        {
            return this._rmsg;
        }
    }

}

