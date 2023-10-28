package app.dpapp.DataTable;/**
 * Created by F5460007 on 2017/3/28.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Owner:F5460007
 * CreateDate:2017/3/28 15:38
 */
public class DataTable {
    // 摘要:
    //     不带参数初始化 System.Data.DataTable 类的新实例。
    public String TableName;
    public DataTable(String Tablename) {
        this.TableName=Tablename;
        _Columns=new ArrayList<>();
        _Rows=new ArrayList<>();
    }
    public DataTable()
    {
        this.TableName="Table1";
        _Columns=new ArrayList<>();
        _Rows=new ArrayList<>();
    }
    private List<DataRow> _Rows;

    public DataRow Rows(int index)  throws Exception {
       return _Rows.get(index);
    }

    public int Rowscount()  throws Exception
    {
        return _Rows.size();
    }
    public int Columnscount()  throws Exception
    {
        return _Columns.size();
    }

    private List<DataColumn> _Columns;

    public DataColumn Columns(int index)  throws Exception{
        return _Columns.get(index);
    }
    public void AddRow(String... value)  throws Exception
    {
        DataRow dr = new DataRow(_Columns);
        for (int i = 0; i < value.length; i++) {
            dr.set_CellValue(i, value[i]);
        }
        _Rows.add(dr);
    }

    public void AddColumn(String value)  throws Exception
    {
        _Columns.add(new DataColumn(value));
        for (DataRow dr: _Rows
             ) {
            dr.add_NewDefaultColumn();
        }
    }
}

