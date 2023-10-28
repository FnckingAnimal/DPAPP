package app.dpapp.appcdl.SFCBLLPack;

/**
 * Created by S7187445 on 2017/6/13.
 */
import app.dpapp.DataTable.DataTable;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;

public class expendable_input {

    public static void checksmtandfoljiban(ActivityInteractive _a, CallWebapi cwa, String inputcontent, String materialno)  throws Exception
    {
            DataTable dt = cwa.CallRDT("checksmtandfoljiban_1",inputcontent);

            if (dt.Rowscount() > 0)
            {
                if (dt.Rows(0).get_CellValue("PRODUCTNO").equals(materialno))
                {
//                    rts.Rstatus = true;
//                    rts.Rmsg = "基板料號OK";
                }
                else
                {
                    //FOL基板料號為SMT半成品料號
                    _a.ShowMessage("SMT批號對應的料號信息與所選基板料號信息不一致，請確認是否用錯物料");
                    return;
                }
            }
            else
            {
                _a.ShowMessage("該基板批號沒有過站記錄");
                return;
            }
    }

    public static void checklensplassmatime(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String opno,String inputtbtext,String keypartnocbtext) throws Exception
    {
        DataTable dt_checkoverWafer2_new=cwa.CallRDT("checklensplassmatime_1",newdeviceno,opno);
        if (dt_checkoverWafer2_new.Rowscount()>0) {
            String ATT2 = dt_checkoverWafer2_new.Rows(0).get_CellValue("ATT2");
            DataTable sqllaserfirsttime = cwa.CallRDT("checklensplassmatime_2", inputtbtext, ATT2);
            DataTable dt_productno = cwa.CallRDT("checklensplassmatime_3", keypartnocbtext);
            if (dt_productno.Rowscount() > 0) {
                if (sqllaserfirsttime.Rowscount() > 0) {
                    if (Double.parseDouble(sqllaserfirsttime.Rows(0).get_CellValue("Hours")) > Double.parseDouble(dt_checkoverWafer2_new.Rows(0).get_CellValue("ATT3"))) {
                        //ShowMessage("LENS PLASSMA 超時 " + sqllaserfirsttime.Rows(0).get_CellValue("Hours") + ">" + dt_checkoverWafer2_new.Rows(0).get_CellValue("ATT3"));

                        DataTable tbreaklistdt = cwa.CallRDT("getbackfronttasklist", sqllaserfirsttime.Rows(0).get_CellValue("classpid").trim(), "BFMC");
                        String[] breaklistss = new String[tbreaklistdt.Rowscount()];

                        for (int i = 0; i < tbreaklistdt.Rowscount(); i++) {
                            breaklistss[i] = tbreaklistdt.Rows(i).get_CellValue(1);
                        }
                        //Object o = MessageBox("BFMC", "P201610002", breaklistss);
                        Object o = _a.MessageBox("Reject List", "驳回节点", breaklistss);
                        Object[] os = (Object[]) o;
                        if (BaseFuncation.DialogResult.Cancel == (BaseFuncation.DialogResult) os[0]) {
                            _a.ShowMessage("請點擊SubMit按鈕，駁回到相應節點，從新過Lens流程！~");
                            return;
                        } else {
                            Boolean update_flag = cwa.CallRB("checklensplassmatime_4", sqllaserfirsttime.Rows(0).get_CellValue("classpid"), inputtbtext);
                            if (update_flag) {
                                _a.ShowMessage("執行成功，請去lens過站流程裏面過站");
                                return;
                            }
                        }
                    }
                }
                else
                {
                    _a.ShowMessage("請先過鏡頭過站流程至INV！~");
                    return;
                }
            }
        }
    }

    public static void checklengandlotno(ActivityInteractive _a,CallWebapi cwa,String inputcontent,String materialno,String lotno)   throws Exception
    {
         DataTable dt_1=cwa.CallRDT("checklengandlotno_1",materialno);
            if (dt_1 == null)
            {
                _a.ShowMessage("查詢該耗材類型時發生錯誤");
                return;
            }
            else
            {
                if (dt_1.Rowscount() > 0)
                {
                    DataTable dt_2=cwa.CallRDT("checklengandlotno_2",materialno,inputcontent);
                    if (dt_2 == null)
                    {
                        _a.ShowMessage("查詢該LENS批號掃描記錄時發生錯誤");
                        return;
                    }
                    else
                    {
                        if (dt_2.Rowscount() > 2)
                        {
                            _a.ShowMessage("LENS已綁定三個批號");
                            return;
                        }
                        else
                        {
                            DataTable dt_3=cwa.CallRDT("checklengandlotno_3", lotno);
                            if (dt_3 == null)
                            {
                                _a.ShowMessage("查詢該批號掃描LENS記錄時發生錯誤");
                                return;
                            }
                            else
                            {
                                if (dt_3.Rowscount() > 2)
                                {
                                    _a.ShowMessage("批號已綁定三個LENS");
                                    return;
                                }
                                else
                                {
//                                    rts.Rstatus = true;
//                                    rts.Rmsg = "批號與LENS 綁定數量OK";
//                                    rts.Rvalue = true;
                                }
                            }
                        }
                    }
                }
                else
                {
//                    rts.Rstatus = true;
//                    rts.Rmsg = "不為LENS，不檢測";
//                    rts.Rvalue = true;
                }
            }


    }

    public static void checkLENScheckinflag(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String now_opno,String inputtbtext,String keypartnocbtext) throws Exception {
        DataTable dt_mf =cwa.CallRDT("expendable_inputtb_keyup_27", keypartnocbtext);
        if (dt_mf.Rowscount()> 0)
        {
            DataTable dt_set =cwa.CallRDT("expendable_inputtb_keyup_28", now_opno);
            if (dt_set.Rowscount() > 0)
            {
                String dt_setatt2=dt_set.Rows(0).get_CellValue("ATT2");
                String dt_setatt3= dt_set.Rows(0).get_CellValue("ATT3");
                DataTable dt_m =cwa.CallRDT("expendable_inputtb_keyup_29", dt_setatt2, inputtbtext);
                if (dt_m.Rowscount() > 0)
                {
                }
                else
                {
                    _a.ShowMessage("該LENS批號不在" +dt_setatt3 + "站位或INV");
                    return;
                }
            }
        }

    }

    public static Boolean getreplacedta(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String materialno,String erpproductno) throws Exception
    {
        Boolean flag=false;
        DataTable dt =cwa.CallRDT("getreplacedta_1",materialno);
        if (dt != null)
        {
            if (dt.Rowscount() > 0)
            {
                for (int i = 0; i < dt.Rowscount(); i++)
                {
                    String str = dt.Rows(i).get_CellValue("att2");
                    if (str == erpproductno)
                    {
                        flag= true;
                        break;
                    }
                }
            }
        }
        else
        {
            flag=false;
            //rts.Rmsg = "獲取物料替代關係時發生錯誤";
        }

        return  flag;
    }

    public static Boolean Get_SCANMaterialNo_New_1(ActivityInteractive _a,CallWebapi cwa,String lotno)  throws Exception
    {
        Boolean flag=false;
        String slotno=cwa.CallRS("Get_SCANMaterialNo_New_1", lotno);
        if (slotno == null)
        {
            flag= false;
        }
        else if (!"".equals(slotno))
        {
            flag= true;
        }
        return  flag;
    }

    public static void Get_SCANMaterialNo_New_2(ActivityInteractive _a,CallWebapi cwa,String otherlotno)  throws Exception
    {
        DataTable dt= cwa.CallRDT("Get_SCANMaterialNo_New_2", otherlotno);
        if (dt != null)
        {
            if (dt.Rowscount() > 0)
            {

            }
            else
            {
                _a.ShowMessage("該批號" + otherlotno + "尚未在收料系統中掃描 ");
                return;
            }
        }
        else
        {
            //rts.Rstatus = false;
            _a.ShowMessage("查詢倉庫收料信息失敗");
            return;
        }

    }

    public static Boolean Get_SCANMaterialNo_New_3(ActivityInteractive _a,CallWebapi cwa,DataTable dt,String otherlotno,String materialno)  throws Exception
    {
                    Boolean Rstatus = false;
                    String erpmaterialstr = "";
                    for (int i = 0; i < dt.Rowscount(); i++)
                    {
                        String erpproductno = dt.Rows(i).get_CellValue("materialproductno");
                        erpmaterialstr = erpmaterialstr + erpproductno + ",";
                        if (erpproductno == materialno)
                        {
                            Rstatus = true;
                            break;
                        }
                    }
//                    if (Rstatus)
//                    {
//                        //rts.Rmsg = "料號與收料系統一致";
//                    }
//                    else
//                    {
//                        _a.ShowMessage("該批號" + otherlotno + "在收料系統中對應的料號是" + erpmaterialstr + ",與SFC所選料號不符");
//                        return;
//                    }

        return Rstatus;
    }

    public static Boolean LENScheckProductnoAndLotno(CallWebapi cwa,String materialno,String inputcontent) throws Exception
    {
        Boolean Rstatus=false;
        String Rmsg="";
        DataTable producttype =cwa.CallRDT("LENScheckProductnoAndLotno_1",materialno);

        if (producttype == null)
        {
            Rstatus = false;
            Rmsg = "查詢耗材料號對應的批號格式失敗";
            return Rstatus;
        }

        if (producttype.Rowscount() > 0)
        {
            String location = producttype.Rows(0).get_CellValue("att1");
            //int length = int.Parse(producttype.Rows[0]["att2"].ToString().Trim());
            String form = producttype.Rows(0).get_CellValue("att3");
            int length = form.length();
            Boolean flag = true;

            switch (location)
            {
                case "START":

                    if (inputcontent.substring(0, length).equals(form))
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                    }

                    break;
                case "CHANGELESS":

                    if (inputcontent.contains("form"))
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                    }

                    break;
                case "END":

                    if (inputcontent.substring(inputcontent.length() - length, inputcontent.length()).equals(form))
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                    }

                    break;
                default:
                    Rmsg = "耗材料號對應的批號格式設置錯誤,tblsfcallsetting 8 102";
                    flag = false;
            }
            if (flag)
            {
                Rstatus = true;
                Rmsg = "耗材批號格式正確";
            }
            else
            {
                Rstatus = false;
                Rmsg = materialno + " 對應的批號格式不正確  " + form;
            }
        }
        else
        {
            Rstatus = true;
            Rmsg = "該耗材料號不管控批號格式";
        }

        return Rstatus;
    }
}
