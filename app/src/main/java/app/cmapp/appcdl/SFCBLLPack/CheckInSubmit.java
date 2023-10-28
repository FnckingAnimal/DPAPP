package app.cmapp.appcdl.SFCBLLPack;

/**
 * Created by S7187445 on 2017/6/19.
 */
import java.util.ArrayList;
import java.util.List;
import app.cmapp.DataTable.DataTable;
import app.cmapp.WebAPIClient.CallWebapi;
import app.cmapp.IncludingSFC.SFCStaticdata;

public class CheckInSubmit  {
    public static void checkin_submit1(ActivityInteractive _a,CallWebapi cwa,String _deviceno,String _newdeviceno,String _odbname,String lotno,String opno,String ip) throws Exception {
        if (ip.equals("10.155.23.184"))
        {

        }
        else
        {
            if (opno.equals("0910")) //BLL.staticmember.deviceno == "VT-Q"
            {
                DataTable dtanalyzeopno =cwa.CallRDT("checkin_submit1_1",opno);
                if (dtanalyzeopno == null)
                {
                   _a.ShowMessage("獲取解析站位異常"); return;
                }
                else if (dtanalyzeopno.Rowscount() > 0)
                {
                    String lotnoA = "";
                    if (lotno.startsWith("AK"))
                    {
                        lotnoA = lotno.substring(1, 13);
                    }
                    else
                    {
                        lotnoA = lotno;
                    }
                    DataTable dt_anazylerecord=cwa.CallRDT("checkin_submit1_2",lotnoA);
                    DataTable dt_analyzeitem=cwa.CallRDT("checkin_submit1_3");

                    List<String> list_anazylerecord= new ArrayList<>();
                    List<String> list_analyzeitem= new ArrayList<>();
                    String futre_item = "";//未解析的項目
                    if (dt_anazylerecord.Rowscount() > 0)
                    {
                        for (int i = 0; i < dt_anazylerecord.Rowscount(); i++)
                        {
                            list_anazylerecord.add(dt_anazylerecord.Rows(i).get_CellValue("avex"));
                        }
                    }

                    if (dt_analyzeitem.Rowscount() > 0)
                    {
                        for (int i = 0; i < dt_analyzeitem.Rowscount(); i++)
                        {
                            list_analyzeitem.add(dt_analyzeitem.Rows(i).get_CellValue("att5"));
                        }
                    }


                    for (int a = 0; a < list_analyzeitem.size(); a++)
                    {
                        if (list_anazylerecord.contains(list_analyzeitem.get(a)))
                        {

                        }
                        else
                        {
                            futre_item += list_analyzeitem.get(a) + ";";
                        }
                    }

                    if ( futre_item == null || futre_item.equals(""))
                    {
                    }
                    else
                    {
                        _a.ShowMessage("請解析未解析的項目" + futre_item);
                        return;
                    }
                }
            }
        }

    }


}
