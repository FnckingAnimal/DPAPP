package app.cmapp.DataTable;/**
 * Created by F5460007 on 2017/3/28.
 */

/**
 * Owner:F5460007
 * CreateDate:2017/3/28 15:42
 */

public class DataColumn {
    String value;
    public DataColumn(String v)  throws Exception
    {
        value = v;
    }
    public String getValue()  throws Exception
    { return value; }
    public void setValue(String value)  throws Exception
    { this.value = value; } }
