package app.dpapp.DataTable;/**
 * Created by F5460007 on 2017/3/28.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Owner:F5460007
 * CreateDate:2017/3/28 15:53
 */

public class DataRow {
    private List<String> _CellValue;
    private List<DataColumn> _ldc;
    public DataRow( List<DataColumn> Columns)  throws Exception{
        _ldc = Columns;
        _CellValue = new ArrayList<String>();
        for (int i = 0; i < Columns.size(); i++) {
            _CellValue.add(null);
        }
    }

    public void set_CellValue(int index, String value)  throws Exception{
        _CellValue.set(index, value);
    }

    public String get_CellValue(int index)  throws Exception {
        return _CellValue.get(index);
    }

    public String get_CellValue(String ColumnName)
            throws Exception
    {
        int index=-1;
        for(int i=0;i<_ldc.size();i++) {
            if (_ldc.get(i).getValue().toUpperCase().equals(ColumnName.toUpperCase())) {
                index=i;
                break;
            }
        }
        return _CellValue.get(index);
    }
    public void add_NewDefaultColumn()  throws Exception {
        _CellValue.add(null);
    }
}


