package app.dpapp.appcdl.SFCBLLPack;/**
 * Created by F5460007 on 2017/5/2.
 */

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import app.dpapp.DataTable.DataTable;
import app.dpapp.IncludingSFC.Carrierscan;
import app.dpapp.IncludingSFC.SFCStaticdata;
import app.dpapp.IncludingSFC.errorinfoshow;
import app.dpapp.IncludingSFC.expendable;
import app.dpapp.IncludingSFC.testopnosnsumcheck;
import app.dpapp.WebAPIClient.CallWebapi;
import app.dpapp.appcdl.BaseFuncation;

/**
 * Owner:F5460007
 * CreateDate:2017/5/2 13:37
 */
public class checkoutsubmit {
    public static void checkout_submit1(ActivityInteractive _a, CallWebapi cwa, DataTable dttestcheck,
                                        String lotnotextBoxText,
                                        String dieqty,
                                        String opnametextBoxText,
                                        String _deviceno,
                                        String _newdeviceno,
                                        String _odbname,
                                        String lotnooldnndd,
                                        String opno,
                                        Boolean checkBox_podChecked ,
                                        Boolean radioButton_partChecked) throws Exception {
        if (dttestcheck == null || dttestcheck == null) {
        } else {
            if (dttestcheck.Rowscount() > 0) {
                String att4all = dttestcheck.Rows(0).get_CellValue(0).trim();
                String att5table = dttestcheck.Rows(0).get_CellValue(1).trim();
                if (att4all==null || att5table==null || att4all.equals("") ||  att5table.equals("")) {
                } else {
                    String[] checkatta = att4all.split(";");
                    String maxcheck = checkatta[1];
                    String mincheck = checkatta[0];
                    String sncheck = checkatta[2];
                    String lotchecksn = checkatta[3];
                    String lotnopod = lotnotextBoxText.toUpperCase();
                    String lotnopodtest = "";
                    if (lotnopod.length() == 12 || lotnopod.length() == 15) {
                        lotnopodtest = lotnopod;
                    } else if (lotnopod.length() == 13 || lotnopod.length() == 16) {
                        lotnopodtest = lotnopod.substring(1, 12);
                    } else {
                        _a.ShowMessage("批号位数不对，请联系MIS");
                        return;
                    }
                    try {
                        String testitemcount = cwa.CallRS("checkout_submit_44", lotchecksn, lotnopodtest, sncheck, att5table);
                        if (testitemcount.equals("") || testitemcount == null) {
                            _a.ShowMessage("没有该批号对应的测试站位数据");
                            return;
                        } else {
                            if (Integer.parseInt(testitemcount) == 0) {
                                Double qtymax = (Math.ceil(Double.parseDouble(dieqty) * (Double.parseDouble(maxcheck))));
                                //Integer.parseInt(dieqty) * (Integer.parseInt(maxcheck));
                                Double qtymin = (Math.ceil(Double.parseDouble(dieqty) * (Double.parseDouble(mincheck))));
                                String testitemerror = "站位" + opnametextBoxText + "测试资料不符合要求";
                                String testonocart = "< test qty now:" + testitemcount + "**" + qtymax + "% *" + qtymin + "%>";
                                _a.ShowMessage("站位" + opnametextBoxText + "尚未开始测试，请先进行测试作业！>");
                                int a = Integer.parseInt(testitemcount);

                                if (BaseFuncation.DialogResult.OK == _a.MessageBox("测试资料单颗数量不达标，是否需要強行過站  ?<" + testonocart + ">", "單擊繼續")) {
                                    SFCStaticdata.staticmember.testsumcheckflag = false;
                                    _a.CreatNewActivity(testopnosnsumcheck.class, _deviceno, _newdeviceno, _odbname,
                                            lotnooldnndd, opno, testitemerror, testonocart);
                                    if (SFCStaticdata.staticmember.testsumcheckflag) {
                                        _a.ShowMessage("成功记录不良信息");
                                    } else {
                                        _a.ShowMessage("记录不良信息失败");
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                int qtytest = Integer.parseInt(testitemcount);
                                Double qtymax = (Math.ceil(Double.parseDouble(dieqty) * (Double.parseDouble(maxcheck))));
                                Double qtymin = (Math.ceil(Double.parseDouble(dieqty) * (Double.parseDouble(mincheck))));
                                if ((qtymin <= qtytest && qtymax >= qtytest) || (checkBox_podChecked && radioButton_partChecked && (qtytest >= 144 && qtytest <= 500))) {
                                } else {
                                    String testitemerror = "站位" + opnametextBoxText + "测试资料不符合要求";
                                    String testonocart = "< test qty now:" + testitemcount + "**" + qtymax + "% *" + qtymin + "%>";
                                    if (BaseFuncation.DialogResult.OK
                                            == _a.MessageBox("测试资料单颗数量不达标，是否需要強行過站 ?<" + testonocart + ">", "單擊繼續")) {
                                        SFCStaticdata.staticmember.testsumcheckflag = false;
                                        _a.CreatNewActivity(testopnosnsumcheck.class, _deviceno, _newdeviceno, _odbname,
                                                lotnooldnndd, opno, testitemerror, testonocart);
                                        if (SFCStaticdata.staticmember.testsumcheckflag) {
                                            _a.ShowMessage("成功记录不良信息");
                                        } else {
                                            _a.ShowMessage("记录不良信息失败");
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        _a.ShowMessage("查询批号测试站位资料时发生错误");
                        return;
                    }
                }
            }
        }
    }

    public static  void checkout_submit2(ActivityInteractive _a,CallWebapi cwa,
                                         String opnoatt6,
                                         String lotnooldnndd,
                                         String _deviceno,
                                         String _newdeviceno,
                                         String _odbname,
                                         String opno,
                                         String pv1,
                                         String pv2,
                                         String pv3
    ) throws Exception {
        if (!opnoatt6.equals("") && opnoatt6 != null && !SFCStaticdata.staticmember.lotnouncheckBoolean) {
            String datetrunc = cwa.CallRS("checkout_submit_45", lotnooldnndd);
            if (Double.parseDouble(datetrunc) > Double.parseDouble(opnoatt6)) {
                String testitemerror = "當前批號進出站時間超出最小規定範圍！<規定：" + opnoatt6 + "；實際：" + datetrunc + ">";
                String testonocart = "< check hours now:" + datetrunc + "**" + opnoatt6 + ">";
                if (BaseFuncation.DialogResult.OK == _a.MessageBox("當前批號進出站時間超出最小規定範圍，是否需要強行過站 ?<" + testonocart + ">", "單擊繼續")) {
                    SFCStaticdata.staticmember.podtestsumcheck = false;
                    _a.CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                            "當前批號進出站時間超出最小規定範圍", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, pv1, pv2, pv3,"","");

                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                        _a.ShowMessage("验证强行出站失败！");
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }
    public static  void checkout_submit2_min(ActivityInteractive _a,CallWebapi cwa,
                                         String opnoatt9,
                                         String lotnooldnndd,
                                         String _deviceno,
                                         String _newdeviceno,
                                         String _odbname,
                                         String opno,
                                         String pv1,
                                         String pv2,
                                         String pv3
    ) throws Exception {
        if (!opnoatt9.equals("") && opnoatt9 != null && !SFCStaticdata.staticmember.lotnouncheckBoolean) {
            String datetrunc = cwa.CallRS("checkout_submit_45", lotnooldnndd);
            if (Double.parseDouble(datetrunc) < Double.parseDouble(opnoatt9)) {
                String testitemerror = "當前批號進出站時間不符合當前設定最小值！<規定：" + opnoatt9 + "；實際：" + datetrunc + ">";
                String testonocart = "< check hours now:" + datetrunc + "**" + opnoatt9 + ">";
                if (BaseFuncation.DialogResult.OK == _a.MessageBox("當前批號進出站時間超出最小規定範圍，是否需要強行過站 ?<" + testonocart + ">", "單擊繼續")) {
                    SFCStaticdata.staticmember.podtestsumcheck = false;
                    _a.CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                            "當前批號進出站時間不符合當前設定最小值", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, pv1, pv2, pv3,"","");

                    if (!SFCStaticdata.staticmember.podtestsumcheck) {
                        _a.ShowMessage("验证强行出站失败！");
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }
    public static void checkout_submit3(ActivityInteractive _a,CallWebapi cwa,String att14,String LhatxtText,String lotno,String opno,String showmsg,String fname1 ) throws Exception {
        if (att14.equals("1") || att14.equals("2")) {
            Boolean lhaflag = cwa.CallRB(fname1, lotno, opno, LhatxtText, SFCStaticdata.staticmember.hpuserid);
            if (lhaflag) {
            } else {
                _a.ShowMessage(showmsg);
                return;
            }
        }
    }

    public static void checkout_submit4(ActivityInteractive _a,CallWebapi cwa,String outinopno,String opno, String productno,String lotno,
                                        String opnoflowid,String _deviceno, String _newdeviceno, String _odbname
                                        ) throws Exception {
        if (outinopno.equals("") || outinopno == null) {
        } else {
            SFCStaticdata.expendm.expendpageflag = false;
            DataTable dtexpend = new DataTable();
            dtexpend = cwa.CallRDT("getexpendablelist", opno, productno);
            String opnoflowidnewin = lotno + "-" + BaseFuncation.padLeft(opnoflowid, 3, '0');
            if (dtexpend.Rowscount() > 0) {
                _a.CreatNewActivity(expendable.class, _deviceno, _newdeviceno, _odbname,
                        opno, lotno, productno, opnoflowidnewin, outinopno, "OUT");
                if (!SFCStaticdata.expendm.expendpageflag) {
                    _a.ShowMessage("未輸入耗材資料﹐請重新輸入");
                    return;
                }
            }
        }
    }

    public static void checkout_submit5(ActivityInteractive _a,String carrieropno,
                                        String _deviceno, String _newdeviceno, String _odbname,
                                        String lotno,String  opno) throws Exception
    {
        if (!carrieropno.equals("") && carrieropno != null) {
            SFCStaticdata.staticmember.carrierflag = "";
            _a.CreatNewActivity(Carrierscan.class, _deviceno, _newdeviceno, _odbname,
                    lotno, opno);
            if (!SFCStaticdata.staticmember.carrierflag.equals("1")) {
                _a.ShowMessage("掃描載板解綁失敗，不能過站");
                return;
            }
        }
    }

    public static void checkout_submit6(ActivityInteractive _a,CallWebapi cwa,
    String lotno,String opno,String dieqty,String defectqtytextBoxText,String opnoflowid,String lotnotextBoxText,String ip, DataTable wipqty,
                                        DataTable wipqty1
    ) throws Exception
    {
        if (SFCStaticdata.staticmember.eolspitflag.equals("1")) {
            String wipflag = cwa.CallRS("checkout_submit_59", lotno);
            if (wipflag == null) {
                _a.ShowMessage("查詢該批號是否需要分小子批時發生錯誤");
                return;
            }
            String att11 = cwa.CallRS("getatt11data", opno);  //FOL PACKING站位分批
            if (att11.equals("1")) {
                if (Integer.parseInt(dieqty) - Integer.parseInt(defectqtytextBoxText.trim()) > 700 && wipflag.equals("")) {
                    DataTable edt = cwa.CallRDT("getsfcspitdata", lotno);
                    if (edt.Rowscount() > 0) {

                    } else {
                        String lotsum = "";
//                        DataTable wipqty = getwiplotnosum(lotno);
                        if (wipqty.Rowscount() > 0) {
                            lotsum = String.valueOf(wipqty.Rowscount()).trim();
                        } else {
                            _a.ShowMessage("獲取WIP批號數量信息時失敗，請聯繫MIS部門");
                            return;
                        }
//                        DataTable wipqty1 = getwiplotnosum1(lotno);
                        String hpwono = wipqty1.Rows(0).get_CellValue("wono").trim();
                        String temsplitstr = BaseFuncation.padLeft(String.valueOf(Integer.parseInt(lotsum) + 1), 3, '0');
                        temsplitstr = temsplitstr.substring(temsplitstr.length() - 2, 2);
                        String splitlotno = lotno.substring(1, lotno.indexOf("-") - 1) + "-" + temsplitstr;
                        String sfcsplitlotno = lotno.substring(0, lotno.indexOf("-")) + "-" + temsplitstr;
                        String dataspitqty = String.valueOf(Integer.parseInt(wipqty1.Rows(0).get_CellValue("dieqty").trim()) - 600);
                        String statespitqty = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(defectqtytextBoxText.trim()) - 600);
                        String att13 = cwa.CallRS("getatt13data", opno);  //FOL PACKING站位分批
                        if (att13.equals("") || att13 == null) {
                            _a.ShowMessage("WIP列印的對應站位尚未維護，請聯繫MIS部門");
                            return;
                        }
                        Boolean blwip = cwa.CallRB("checkout_submit_60", lotno, splitlotno, att13,
                                dataspitqty, sfcsplitlotno, statespitqty);
                        if (!blwip) {
                            _a.ShowMessage("補充WIP資料時發生異常，請聯繫MIS進行處理");
                            return;
                        }
                        String eolfirstopno = cwa.CallRS("geteolfirstopno");
                        Boolean kitres = cwa.CallRB("checkout_submit_61", lotno, opnoflowid, opno, SFCStaticdata.staticmember.hpuserid,
                                ip, sfcsplitlotno, statespitqty, splitlotno, hpwono, SFCStaticdata.staticmember.hpproductno, eolfirstopno);
                        if (!kitres)
                            _a.ShowMessage("分批資料失敗");
                        return;
                    }
                }
            } else {
                String wiptemplotno = lotnotextBoxText.toUpperCase();
                String wiplotnostr = wiptemplotno.substring(1, (wiptemplotno.length() - 1));
                String curqtywip = String.valueOf(Integer.parseInt(dieqty) - Integer.parseInt(defectqtytextBoxText.trim()));
                Boolean blwip1 = cwa.CallRB("checkout_submit_62", curqtywip, wiplotnostr);
                if (!blwip1) {
                    _a.ShowMessage("補充WIP資料時發生異常，請聯繫MIS進行處理");
                    return;
                }
            }
        }
    }

    public static void checkout_submit7(ActivityInteractive _a,CallWebapi cwa,String staticproductno,String opno,String lotnotextBoxText,
                                        String lotno
                                        ) throws Exception {
        if (SFCStaticdata.staticmember.engRilotcheckflag || SFCStaticdata.staticmember.englotcheckflag || SFCStaticdata.staticmember.devicenomacinfo) {
            DataTable dtqcbackstation = cwa.CallRDT("FQCundodataDt", staticproductno, opno, lotnotextBoxText);
            if (dtqcbackstation == null) {
                String lotnooqcuse = lotnotextBoxText;
                Boolean rebackbl = cwa.CallRB("checkout_submit_63", lotnooqcuse, opno);
                if (!rebackbl) {
                    _a.ShowMessage("回復不良記錄失敗，請聯繫MIS");
                }
                return;
            } else if (dtqcbackstation.Rowscount() > 0)//該站位是Q退站位
            {
                DataTable OQCDt = cwa.CallRDT("getopnoforqc");
                String lotnooqcuse = lotnotextBoxText;
                if (dtqcbackstation.Rows(0).get_CellValue("att1").trim().equals(OQCDt.Rows(0).get_CellValue("opno").trim())) {
                    DataTable dtng = new DataTable();
                    if (SFCStaticdata.staticmember.deviceno.equals("NH-A")) {
                        dtng = cwa.CallRDT("checkout_submit_64", lotnooqcuse, dtqcbackstation.Rows(0).get_CellValue("att1").trim(), opno);
                    } else {
                        dtng = cwa.CallRDT("checkout_submit_65", lotnooqcuse, dtqcbackstation.Rows(0).get_CellValue("att1").trim(), opno);
                    }
                    if (dtng == null) {
                        _a.ShowMessage("查詢下一站(QC站位)Q退的報廢產品是否有全部打不良時發生異常，請聯繫MIS");
                        return;
                    }
                    StringBuilder Messagestr = new StringBuilder();
                    if (dtng.Rowscount() > 0) {
                        for (int i = 0; i < dtng.Rowscount(); i++) {
                            Messagestr = Messagestr.append(dtng.Rows(i).get_CellValue(0).trim() + "\r\n");
                        }

                        _a.ShowMessage(lotnooqcuse + "--以下產品\r\n\r\n" + Messagestr.toString() + "\r\n為對應QC站位Q退的報廢產品,但沒有在當前站位打不良，不可進行出站作業");
                        Boolean rebackbl = cwa.CallRB("checkout_submit_63", lotnooqcuse, opno);
                        if (!rebackbl) {
                            _a.ShowMessage("回復不良記錄失敗，請聯繫MIS");
                        }
                        return;
                    }
                    String dragonlot = lotnooqcuse;
                    if (dragonlot.length() % 2 != 0) {
                        dragonlot = dragonlot.substring(1, lotno.length() - 1);
                    }
                    DataTable dtunknow = cwa.CallRDT("checkout_submit_66", lotnooqcuse, dtqcbackstation.Rows(0).get_CellValue("att1").trim(), dragonlot, opno);
                    if (dtunknow == null) {
                        _a.ShowMessage("查詢下一站(QC站位)Q退的待定產品是否有全部進行PE復判掃描時發生異常，請聯繫MIS");
                        return;
                    }
                    StringBuilder unknowstr = new StringBuilder();
                    if (dtunknow.Rowscount() > 0) {
                        for (int j = 0; j < dtunknow.Rowscount(); j++) {
                            unknowstr = unknowstr.append(dtunknow.Rows(j).get_CellValue(0).trim() + "\r\n");
                        }
                        _a.ShowMessage(lotnooqcuse + "--以下產品\r\n\r\n" + unknowstr.toString() + "\r\n為對應QC站位Q退的待定產品,但沒有在當前站位進行PE復判掃描，不可進行出站作業");
                        Boolean rebackblother = cwa.CallRB("checkout_submit_63", lotnooqcuse, opno);
                        if (!rebackblother) {
                            _a.ShowMessage("回復不良記錄失敗，請聯繫MIS");
                        }
                        return;
                    }
                } else {
                    DataTable dtng = new DataTable();
                    if (SFCStaticdata.staticmember.deviceno.equals("NH-A")) {
                        dtng = cwa.CallRDT("checkout_submit_64", lotnooqcuse, dtqcbackstation.Rows(0).get_CellValue("att1").trim(), opno);
                    } else {
                        dtng = cwa.CallRDT("checkout_submit_65", lotnooqcuse, dtqcbackstation.Rows(0).get_CellValue("att1").trim(), opno);
                    }
                    if (dtng == null) {
                        _a.ShowMessage("查詢下一站(QC站位)Q退的報廢產品是否有全部打不良時發生異常，請聯繫MIS");
                        return;
                    }
                    StringBuilder Messagestr = new StringBuilder();
                    if (dtng.Rowscount() > 0) {
                        for (int i = 0; i < dtng.Rowscount(); i++) {
                            Messagestr = Messagestr.append(dtng.Rows(i).get_CellValue(0).trim() + "\r\n");
                        }
                        _a.ShowMessage(lotnooqcuse + "--以下產品\r\n" + Messagestr + "為對應QC站位Q退的報廢產品,但沒有在當前站位打不良，不可進行出站作業");
                        Boolean rebackbl = cwa.CallRB("checkout_submit_63", lotnooqcuse, opno);
                        if (!rebackbl) {
                            _a.ShowMessage("回復不良記錄失敗，請聯繫MIS");
                        }
                        return;
                    }
                }
            }
        }
    }

    public static void checkout_submit8(ActivityInteractive _a,CallWebapi cwa,Boolean checkBox_dbEnabled,String lotno,String opno,String defectqty,
                                        String lotnotextBoxText
                                        ) throws Exception {
        if (checkBox_dbEnabled && SFCStaticdata.staticmember.lotcheckeolflag) {
            String ycqty = cwa.CallRS("checkout_submit_67", lotno, opno);
            if (ycqty.equals("") || ycqty == null) {
                ycqty = "0";
            }
            DataTable dtchina = cwa.CallRDT("checkout_submit_68", SFCStaticdata.staticmember.deviceno);
            if (dtchina.Rowscount() > 0) {
                if (Integer.parseInt(defectqty) > Integer.parseInt(ycqty)) {
                    _a.ShowMessage("该站位所打不良大于一次不良,请确认");
                    String lotnooqcuse = lotnotextBoxText;
                    Boolean rebackbl = cwa.CallRB("checkout_submit_63", lotnooqcuse, opno);
                    if (!rebackbl) {
                        _a.ShowMessage("回復不良記錄失敗，請聯繫MIS");
                    }
                    return;
                }
            }
        }
    }

    public static void checkout_submit9(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String lotno,String opno) throws Exception {
        Boolean imageretestfalg =  cwa.CallRB("checkdefectretestdev",newdeviceno);
        if (imageretestfalg)
        //if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") || SFCStaticdata.staticmember.newdeviceno.equals("ATF001"))
        {
            DataTable re37dt = cwa.CallRDT("getretestholddata", lotno, opno);
            if (re37dt != null) {
                if (re37dt.Rowscount() > 0) {
                    int reatt2 = 0;
                    try {
                        reatt2 = Integer.parseInt(re37dt.Rows(0).get_CellValue("att2").trim());
                    } catch (Exception ex) {
                        reatt2 = 0;
                    }
                    String reatt1 = re37dt.Rows(0).get_CellValue("att1").trim();
                    if (reatt1.equals("1")) {
                        DataTable redadt = cwa.CallRDT("getretestqtydata", lotno);
                        if (redadt != null) {
                            String temptestqty = redadt.Rows(0).get_CellValue("snqty").trim();
                            if (Integer.parseInt(temptestqty) < reatt2) {
                                _a.ShowMessage("該批次的重工SN數量" + temptestqty + "少於應該重工的數量" + reatt2 + ",不能過站");
                                return;
                            } else {
                                Boolean uprefalg = cwa.CallRB("upre37data", lotno);
                            }
                        }
                    }
                }
            } else {
                _a.ShowMessage("獲取是否重測的數據時發生錯誤，清聯繫MIS部門");
                return;
            }
        }
    }

    public static void checkout_submit10(ActivityInteractive _a,CallWebapi cwa,String lotno,String opno,String loginputqty,String ip ) throws Exception {
        if (SFCStaticdata.staticmember.qcholddefalg.equals("1")) {
            Boolean hc0806alertmailsendBoolean = false;
            String messageString = "單項不良率超標情況:\r\n";
            DataTable hc0806alerdt = new DataTable();
            DataTable hc0806stopdt = new DataTable();
            hc0806alerdt.AddColumn("Deviceno");
            hc0806alerdt.AddColumn("Lotno");
            hc0806alerdt.AddColumn("Station Name");
            hc0806alerdt.AddColumn("Qty In");
            hc0806alerdt.AddColumn("ErrorNoName");
            hc0806alerdt.AddColumn("NGQty");
            hc0806alerdt.AddColumn("NGYield");
            hc0806alerdt.AddColumn("Alert Line NGYield");
            hc0806alerdt.AddColumn("Line");
            hc0806alerdt.AddColumn("ReMark");
            hc0806stopdt.AddColumn("Deviceno");
            hc0806stopdt.AddColumn("Lotno");
            hc0806stopdt.AddColumn("Station Name");
            hc0806stopdt.AddColumn("Qty In");
            hc0806stopdt.AddColumn("ErrorNoName");
            hc0806stopdt.AddColumn("NGQty");
            hc0806stopdt.AddColumn("NGYield");
            hc0806stopdt.AddColumn("Stop Line NGYield");
            hc0806stopdt.AddColumn("Line");
            hc0806stopdt.AddColumn("ReMark");
            String hc0806holdsql = "begin ";
            List<String> hc0806parmer = new ArrayList<String>();
            String sqlhc0806alert = " select a.lotno,a.opno,a.opnodefectno,b.opdefectname,ngqty from  t_lotdefectdata a inner join t_defectdata b on (a.opno=b.opno and a.opnodefectno=b.opdefectno)  where   a.lotno=:lot and a.opno=:opnonow and b.att3='10'";
            String[] parmerhc0806alert = {lotno, opno};
            DataTable dthc0806alert = cwa.CallRDT("basefunction_execsql_ora_d", sqlhc0806alert, BaseFuncation.SerializeObjectArrayString(parmerhc0806alert), SFCStaticdata.staticmember.odbname);
            if (dthc0806alert == null) {
                _a.ShowMessage("查詢該批號的不良記錄異常，請聯繫MIS");
                if (!cwa.CallRB("deldefectwipsn", lotno, opno, "查詢不良資料異常(HC0806)")) {
                    _a.ShowMessage("回溯不良資料異常，請聯繫MIS");
                }
                return;
            } else {
                if (dthc0806alert.Rowscount() > 0) {
                    for (int i = 0; i < dthc0806alert.Rowscount(); i++) {
                        String hc0806lot = dthc0806alert.Rows(i).get_CellValue("lotno");
                        String hc0806opno = dthc0806alert.Rows(i).get_CellValue("opno");
                        String hc0806opdefectno = dthc0806alert.Rows(i).get_CellValue("opnodefectno");
                        String hc0806opdefectname = dthc0806alert.Rows(i).get_CellValue("opdefectname");
                        String hc0806ngqty = dthc0806alert.Rows(i).get_CellValue("ngqty");
                        String ngsinglesql = "select att3 minngyieldlimit,att4 maxngyieldlimit from tblsfcallsetting  where type='0' and status='34' and att2=:opnonow and att1=:opdefectnonow ";
                        if (SFCStaticdata.staticmember.AllReworkFlag) {
                            ngsinglesql = "select att3 minngyieldlimit,att4 maxngyieldlimit from tblsfcallsetting  where type='1' and status='34' and att2=:opnonow and att1=:opdefectnonow ";
                        }
                        String[] ngsingleparmer = {hc0806opno, hc0806opdefectno};
                        DataTable ngsingledt = cwa.CallRDT("basefunction_execsql_ora_d", ngsinglesql, BaseFuncation.SerializeObjectArrayString(ngsingleparmer), SFCStaticdata.staticmember.odbname);
                        if (ngsingledt == null) {
                            _a.ShowMessage("查詢站位" + cwa.CallRS("getopname", hc0806opno) + "對應的不良項目" + hc0806opdefectname + "的Aler Line/Stop Line不良率異常，請聯繫MIS");
                            if (!cwa.CallRB("deldefectwipsn", lotno, opno, "查詢不良率上限異常(HC0806)")) {
                                _a.ShowMessage("回溯不良資料異常，請聯繫MIS");
                            }
                            return;
                        } else if (ngsingledt.Rowscount() <= 0) {
                            _a.ShowMessage("尚未設定站位" + cwa.CallRS("getopname", hc0806opno) + "對應的不良項目" + hc0806opdefectname + "的Aler Line/Stop Line不良率異常，請聯繫MIS");
                            if (!cwa.CallRB("deldefectwipsn", lotno, opno, "不良率上限未設定(HC0806)")) {
                                _a.ShowMessage("回溯不良資料異常，請聯繫MIS");
                            }
                            return;
                        }
                        String minyield = ngsingledt.Rows(0).get_CellValue("minngyieldlimit");
                        String maxyeild = ngsingledt.Rows(0).get_CellValue("maxngyieldlimit");
                        String ngyield = String.valueOf((Double.parseDouble(hc0806ngqty) / Double.parseDouble(loginputqty)) * 100);
                        String line = cwa.CallRS("checkout_submit_69", lotno);
                        if (line.equals("") || line == null) {
                            switch (hc0806lot.substring(0, 1)) {
                                case "A":
                                    line = "EOL";
                                    break;
                                case "P":
                                    line = "FOL";
                                    break;
                                case "S":
                                    line = "SMT";
                                    break;
                                default:
                                    break;
                            }
                        }
                        if ((Double.parseDouble(ngyield) > Double.parseDouble(minyield)) && (Double.parseDouble(ngyield) <= Double.parseDouble(maxyeild))) {
                            hc0806alerdt.AddRow(SFCStaticdata.staticmember.deviceno, hc0806lot, cwa.CallRS("getopname", hc0806opno), loginputqty, hc0806opdefectname, hc0806ngqty, ngyield + "%", minyield + "%", line, "各部門負責人進行改善");
                            if (!hc0806alertmailsendBoolean) {
                                hc0806alertmailsendBoolean = true;
                            }
                        } else if ((Double.parseDouble(ngyield) > Double.parseDouble(minyield)) && (Double.parseDouble(ngyield) > Double.parseDouble(maxyeild))) {
                            hc0806stopdt.AddRow(SFCStaticdata.staticmember.deviceno, hc0806lot, cwa.CallRS("getopname", hc0806opno), loginputqty, hc0806opdefectname, hc0806ngqty, ngyield + "%", maxyeild + "%", line, "請立即停機改善");
                            messageString = messageString + cwa.CallRS("getopname", hc0806opno) + "站位投入為" + loginputqty + "," + hc0806opdefectname + "的不良數量為" + hc0806ngqty + ",不良率為" + ngyield + ",超過停線不良率(Aler line:" + minyield + ";Stop line:" + maxyeild + ")\r\n";
                            if (!hc0806alertmailsendBoolean) {
                                hc0806alertmailsendBoolean = true;
                            }
                            hc0806holdsql = hc0806holdsql + "insert into t_qcholdlotno (lotno,opno,yield,inuptqty,dbqty,createdate,styleflag,valuesflag,errorno,checkuser,checkip,unholddate,unholduser,unholdip,att1,att2,att3,att4) values(:lotno" + i + ",:opno" + i + ",:yield" + i + ",:inputqty" + i + ",:dbqty" + i + ",sysdate,'0','1',:errorno" + i + ",:userid" + i + ",:ip" + i + ",null,null,null,null,null,null,null);";
                            hc0806parmer.add(hc0806lot);
                            hc0806parmer.add(hc0806opno);
                            hc0806parmer.add(ngyield + "%");
                            hc0806parmer.add(loginputqty);
                            hc0806parmer.add(hc0806ngqty);
                            hc0806parmer.add(hc0806opdefectno);
                            hc0806parmer.add(SFCStaticdata.staticmember.userid);
                            hc0806parmer.add(ip);
                        }
                    }
                }
            }
            if (hc0806alertmailsendBoolean) {
                if (SFCStaticdata.staticmember.lotcheckeolflag) {
                    if (hc0806alerdt.Rowscount() > 0) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(hc0806alerdt), "EM037", "LH SFC System Lotno预警通知郵件：" + SFCStaticdata.staticmember.deviceno + "  Alert Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "0");
                    }
                    if (hc0806stopdt.Rowscount() > 0) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(hc0806stopdt), "EM037", "LH SFC System Lotno停线通知郵件：" + SFCStaticdata.staticmember.deviceno + "  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                    }
                } else if (SFCStaticdata.staticmember.lotfolbingpicheck) {
                    if (hc0806alerdt.Rowscount() > 0) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(hc0806alerdt), "EM036", "LH SFC System Lotno预警通知郵件：" + SFCStaticdata.staticmember.deviceno + "  Alert Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "0");
                    }
                    if (hc0806stopdt.Rowscount() > 0) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(hc0806stopdt), "EM036", "LH SFC System Lotno停线通知郵件：" + SFCStaticdata.staticmember.deviceno + "  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                    }
                } else {
                    if (hc0806alerdt.Rowscount() > 0) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(hc0806alerdt), "EM035", "LH SFC System Lotno预警通知郵件：" + SFCStaticdata.staticmember.deviceno + "  Alert Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "0");
                    }
                    if (hc0806stopdt.Rowscount() > 0) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(hc0806stopdt), "EM035", "LH SFC System Lotno停线通知郵件：" + SFCStaticdata.staticmember.deviceno + "  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                    }
                }
                if (hc0806stopdt.Rowscount() > 0) {
                    _a.ShowMessage(messageString);
                    hc0806holdsql = hc0806holdsql + "end;";
                    String[] hc0806arrayString = new String[hc0806parmer.size()];
                    hc0806parmer.toArray(hc0806arrayString);
                    Boolean holdBoolean = cwa.CallRB("checkout_submit_70", hc0806holdsql, BaseFuncation.SerializeObjectArrayString(hc0806arrayString));
                    if (!holdBoolean) {
                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                        if (!cwa.CallRB("deldefectwipsn", lotno, opno, "批號Hold失敗(HC0806)")) {
                            _a.ShowMessage("回溯不良資料異常，請聯繫MIS");
                        }
                        return;
                    }
                }
            }
        }
    }


    public static void checkout_submit11(ActivityInteractive _a,CallWebapi cwa,String transfererp,Boolean lotfolbingpicheck,String lotno,String devicenotextBoxText,String lotnooldnndd) throws Exception{
        if (lotfolbingpicheck && (Integer.parseInt(transfererp) == 16)) {
            String sysdatenow = cwa.CallRS("getdbsystime");
            String[] charlotno = lotno.split("-");
            String lotnolike = charlotno[0] + "%";
            String checkqtyatt16 = cwa.CallRS("checkout_submit_19", lotnolike);
            if (Integer.parseInt(checkqtyatt16) > 1500) {
                _a.ShowMessage("该工单下已分批<未并批>数量超过 1.5 K，请督促执行并批动作！" + checkqtyatt16);
                DataTable dtfolcheckcom = cwa.CallRDT("checkout_submit_20", lotnolike);
                String html = "";
                String title = devicenotextBoxText + " " + "该工单下已分批<未并批>数量超过 1.5 K，请督促执行并批动作！ " + lotno + " " + sysdatenow;
                if (dtfolcheckcom.Rowscount() > 0) {
                    int dieqtyall = 0;
                    html = "<table style='width:100%;background-color: #BBD3EB;' cellpadding='0' cellspacing='1'>"
                            + "<tr><td align='center' colspan='8' style='height:27px;font-weight: bold;  '  bgcolor='#DFF1FB'>" + title + "</td></tr>"
                            + "<tr><td align='center' style='width:5%;height:27px; ' bgcolor='#DFF1FB'>No.</td>"
                            + "<td align='center' style='width:9%;height:27px;'bgcolor='#DFF1FB' >子批號</td>"
                            + "<td align='center' style='width:9%;height:27px;' bgcolor='#DFF1FB'>新分子批號</td>"
                            + "<td align='center' style='width:9%;height:27px;'bgcolor='#DFF1FB' >分批數量</td>"
                            + "<td align='center' style='width:9%;height:27px;' bgcolor='#DFF1FB'>分批操作人員</td>"
                            + "<td align='center' style='width:10%;height:27px; 'bgcolor='#DFF1FB' >分批時間</td>"
                            + "</tr>";
                    for (int i = 0; i < dtfolcheckcom.Rowscount(); i++) {
                        if (i < dtfolcheckcom.Rowscount() - 1) {
                            html = html + " <tr><td align='center' style='height:27px;' bgcolor='#FFFFFF'  >" + (i + 1) + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("newlotno").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("oldlotno").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("oldqty").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("creatuser").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("creattime").trim() + "</td>"
                                    + "</tr>";
                            dieqtyall = dieqtyall + Integer.parseInt(dtfolcheckcom.Rows(i).get_CellValue("oldqty").trim());
                        } else {
                            html = html + " <tr><td align='center' style='height:27px;' bgcolor='#FFFFFF'  >" + (i + 1) + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("newlotno").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("oldlotno").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("oldqty").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("creatuser").trim() + "</td>"
                                    + "<td align='center' style='height:27px;' bgcolor='#FFFFFF' >" + dtfolcheckcom.Rows(i).get_CellValue("creattime").trim() + "</td>"
                                    + "</tr>";
                            dieqtyall = dieqtyall + Integer.parseInt(dtfolcheckcom.Rows(i).get_CellValue("oldqty").trim());
                        }
                    }
                    html = html + " <tr><td align='center' style='width:23%;height:27px;'bgcolor='#DFF1FB' >ALL</td>"
                            + "<td align='center' style='width:9%;height:27px;' bgcolor='#DFF1FB'></td>"
                            + "<td align='center' style='width:19%;height:27px; 'bgcolor='#DFF1FB' ></td>"
                            + "<td align='center' style='width:9%;height:27px;'bgcolor='#DFF1FB' >" + dieqtyall + "</td>"
                            + "<td align='center' style='width:9%;height:27px;' bgcolor='#DFF1FB'></td>"
                            + "<td align='center' style='width:9%;height:27px;'bgcolor='#DFF1FB' ></td>"
                            + "</tr>";
                    html = html + "<tr><td align='center' colspan='8' style='height:27px;font-weight: bold;  '  bgcolor='#DFF1FB'>备注：还请及时并批，详细数据请参见【生产线管理系统】</td></tr>";
                    html = html + "</table></div>";
                }
                String newtitle = "FOL分批尚未并批<超過1.5K>，請督促產線儘快作業！" + lotnooldnndd + " " + sysdatenow;
                DataTable dtb2 = cwa.CallRDT("checkout_submit_21");
                if (dtb2.Rowscount() > 0) {
                    String NewContent = html;
                    cwa.CallRB("get_sendmail", BaseFuncation.SerializeObjectDataTable(dtb2), "30", newtitle, NewContent); // // 判斷每次收件人數，每次發30人,最多30人，不要發太多，有時候發不出去，分批發郵件
                }
                return;
            }
        }
    }

    public static void checkout_submit12(ActivityInteractive _a,CallWebapi cwa,String lotno, String _deviceno, String _newdeviceno,String _odbname,String  lotnooldnndd,String opno) throws Exception{

        SFCStaticdata.staticmember.fvicheckbagflag = false;
        String lotsncheck = "";
        if (lotno.length() == 13) {
            lotsncheck = lotno.substring(1, 12);
        } else {
            lotsncheck = lotno;
        }

        DataTable checkQdt = cwa.CallRDT("getopnoforqc");
        if (checkQdt.Rowscount() > 0) {
            String sncountqc = cwa.CallRS("checkout_submit_27", lotsncheck);
            if (Integer.parseInt(sncountqc) < 50) {
                DataTable dtpackqcchecklotdt = cwa.CallRDT("checkout_submit_28", lotsncheck);
                if (dtpackqcchecklotdt.Rowscount() <= 0) //正常批號
                {
                    if (BaseFuncation.DialogResult.OK == _a.MessageBox(lotsncheck + "--該批號QC抽檢的SN中已掃描到該批號的SN的數量為--" + String.valueOf(sncountqc) + "--小于標準數量--50--，不可出站。是否强行出站？", "")) {
                        SFCStaticdata.staticmember.podtestsumcheck = false;
                        _a.CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                "此批號尚未完成主壓抽檢記錄,是否需要强行过站？(" + sncountqc + "!= 50)", lotnooldnndd, opno, SFCStaticdata.staticmember.odbname, "5", "1", "31", "", "1");

                        if (!SFCStaticdata.staticmember.podtestsumcheck) {
                            _a.ShowMessage("验证强行出站失败！");
                            return;
                        }
                    }
                }
            }
        }
        DataTable dtlotsncheck = new DataTable();
        try {
            dtlotsncheck = cwa.CallRDT("checkout_submit_29", lotsncheck);
            if (dtlotsncheck.Rowscount() > 0) {
                String lotqcsum = dtlotsncheck.Rows(0).get_CellValue("snsum").trim();
                if (lotqcsum.equals("") || lotqcsum == null) {
                    _a.ShowMessage("Pack站位尚未包裝，請先進行包裝作業");
                    return;
                }
                String snchecklas = "";
                try {
                    snchecklas = cwa.CallRS("checkout_submit_30", lotsncheck);
                    if (snchecklas.equals("") || snchecklas == null) {
                        _a.ShowMessage("QC站位尚未執行抽檢動作");
                        return;
                    }
                    if (Integer.parseInt(snchecklas) > Integer.parseInt(lotqcsum)) {
                        DataTable dtaaaaaa = new DataTable();
                        try {
                            dtaaaaaa = cwa.CallRDT("checkout_submit_31", lotsncheck);
                            if (dtaaaaaa.Rowscount() > 0) {
                                String snaaaa = "";
                                for (int i = 0; i < dtaaaaaa.Rowscount(); i++) {
                                    if (i == 0) {
                                        snaaaa = dtaaaaaa.Rows(0).get_CellValue(0).trim();
                                    } else {
                                        snaaaa = snaaaa + " || " + dtaaaaaa.Rows(i).get_CellValue(0).trim();
                                    }
                                }
                                _a.ShowMessage("批號掃描SN數量與QC抽檢數量不符，PACK：" + lotqcsum + " || QC抽檢：" + snchecklas + "**" + snaaaa);
                                // return;
                            } else {
                                _a.ShowMessage("查询信息与批号信息不符");
                                return;
                            }
                        } catch (Exception ex) {
                            _a.ShowMessage("查询批号对应数据时发生错误");
                            return;
                        }
                    }
                } catch (Exception ex) {
                    _a.ShowMessage("查詢批號QC抽檢SN數量時發生錯誤");
                    return;
                }
            } else {
                _a.ShowMessage("請檢查該批號QC站位是否有抽測記錄");
                return;
            }
        } catch (Exception ex) {
            _a.ShowMessage("PACK 查詢 QC 抽測 SN 數據時發生錯誤");
            return;
        }
    }

    public static void checkout_submit13(ActivityInteractive _a,CallWebapi cwa,String lotno,String opno,String opnametextBoxText) throws Exception {
        if (SFCStaticdata.staticmember.deviceno.trim().equals("VT-Q")) {
            Boolean VTQsetupYJ = false;
            DataTable VTQsetupalerdt = new DataTable();
            VTQsetupalerdt.AddColumn("Deviceno");
            VTQsetupalerdt.AddColumn("Lotno");
            VTQsetupalerdt.AddColumn("Station Name");
            VTQsetupalerdt.AddColumn("ErrorNoName");
            VTQsetupalerdt.AddColumn("NGQty");
            VTQsetupalerdt.AddColumn("Line");
            VTQsetupalerdt.AddColumn("ReMark");
            DataTable VTQsetupalert = cwa.CallRDT("checkout_submit_75", lotno, opno);
            if (VTQsetupalert == null) {
                _a.ShowMessage("查詢該批號的不良記錄異常，請聯繫MIS");
                if (!cwa.CallRB("deldefectwipsn", lotno, opno, "查詢不良資料異常")) {
                    _a.ShowMessage("回溯不良資料異常，請聯繫MIS");
                }
                return;
            }
            if (VTQsetupalert.Rowscount() > 0) {
                String VTQsetuplot = VTQsetupalert.Rows(0).get_CellValue("lotno");
                String VTQsetupopno = VTQsetupalert.Rows(0).get_CellValue("opno");
                String VTQsetupopdefectno = VTQsetupalert.Rows(0).get_CellValue("opnodefectno");
                String VTQsetupopdefectname = VTQsetupalert.Rows(0).get_CellValue("opdefectname");
                String VTQsetupngqty = VTQsetupalert.Rows(0).get_CellValue("ngqty");
                String line = cwa.CallRS("checkout_submit_74", lotno);
                if (line.equals("") || line == null) {
                    switch (VTQsetuplot.substring(0, 1)) {
                        case "A":
                            line = "EOL";
                            break;
                        case "P":
                            line = "FOL";
                            break;
                        case "S":
                            line = "SMT";
                            break;
                        default:
                            break;
                    }
                }
                String setremark = "";
                DataTable redt = cwa.CallRDT("getsetrensondata", lotno, opno);
                if (redt != null && redt.Rowscount() > 0) {
                    String setuserid = redt.Rows(0).get_CellValue("usr").trim();
                    String setreason = redt.Rows(0).get_CellValue("reason").trim();
                    setremark = setuserid + " : " + setreason;
                } else {
                    setremark = SFCStaticdata.staticmember.userid;
                }
                VTQsetupalerdt.AddRow(SFCStaticdata.staticmember.deviceno, VTQsetuplot, opnametextBoxText, VTQsetupopdefectname, VTQsetupngqty, line, setremark);
                DataTable bianma = cwa.CallRDT("checkout_submit_76", VTQsetuplot, VTQsetupopno);
                String bianma1 = bianma.Rows(0).get_CellValue("opnodefectno");
                if (VTQsetuplot.substring(0, 1).equals("S")) {
                    if (Integer.parseInt(VTQsetupngqty) > 5 && bianma1.equals("0009-14")) {
                        VTQsetupYJ = true;
                    }
                } else {
                    VTQsetupYJ = true;
                }
            }
            if (lotno.contains("E")) {
                VTQsetupYJ = false;
            }
            if (VTQsetupYJ) {
                if (VTQsetupalerdt.Rowscount() > 0) {
                    String tempgroupid = "";
                    if (lotno.substring(0, 1).trim().equals("S")) {
                        tempgroupid = "ALERTS";
                    } else if (lotno.substring(0, 1).trim().equals("P")) {
                        tempgroupid = "ALERTP";
                    } else {
                        tempgroupid = "VTQST";
                    }
                    cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(VTQsetupalerdt), tempgroupid, "LH SFC System Lotno调机不良预警邮件：" + SFCStaticdata.staticmember.deviceno + "  Alert Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "0");
                }
            }
        }
    }

    public static void checkout_submit14(ActivityInteractive _a,CallWebapi cwa,String lotno,String opno,String loginputqty,String ngqty) throws Exception {
        if (SFCStaticdata.staticmember.englotcheckflag && opno.equals("0022")) {
            Boolean PodalertmailsendBoolean = false;
            String messageString = "單項不良率超標情況:\r\n";
            DataTable Podalerdt = new DataTable();
            DataTable Podstopdt = new DataTable();
            Podalerdt.AddColumn("Deviceno");
            Podalerdt.AddColumn("Lotno");
            Podalerdt.AddColumn("lottype");
            Podalerdt.AddColumn("NGQty");
            Podalerdt.AddColumn("ErrorNoName");
            Podalerdt.AddColumn("NGrate");
            Podalerdt.AddColumn("NGYieldReason");
            String IsLastLot = "";
            String lastlotstr = "";
            DataTable dtlasrlot = cwa.CallRDT("checkout_submit_77", lotno);
            if (dtlasrlot == null) {
                _a.ShowMessage("查詢該子批是否是尾數批");
                return;
            } else if (dtlasrlot.Rowscount() > 0) {
                IsLastLot = "Y";
                lastlotstr = "尾數批";
            } else {
                IsLastLot = "N";
                lastlotstr = "正常批";
            }
            DataTable dtfindopdef_nL = cwa.CallRDT("checkout_submit_78");
            if (dtfindopdef_nL == null) {
                _a.ShowMessage("查詢該子批不良預警線失敗，請聯繫MIS！");
                return;
            } else if (dtfindopdef_nL.Rowscount() == 0) {
                _a.ShowMessage("查詢該站位未設定特殊不良預警線，如需添加請聯繫MIS！");
                return;
            }
            for (int i = 0; i < dtfindopdef_nL.Rowscount(); i++) {
                String opdefectno = dtfindopdef_nL.Rows(i).get_CellValue("att4");
                String nowlotorder = dtfindopdef_nL.Rows(i).get_CellValue("att3");
                String minlimit = dtfindopdef_nL.Rows(i).get_CellValue("att1");
                String maxlimit = dtfindopdef_nL.Rows(i).get_CellValue("att2");
                String opdefectname = dtfindopdef_nL.Rows(i).get_CellValue("opdefectnamezh");
                String[] BB = opdefectname.split("(|)|（|）");
                opdefectname = BB[1];
                String stPodalert = cwa.CallRS("checkout_submit_79", lotno, opno, opdefectno);
                BaseFuncation.doubletostring(Double.parseDouble(stPodalert) / Double.parseDouble(loginputqty) * 100, null);
                String nowrate = BaseFuncation.doubletostring(Double.parseDouble(stPodalert) / Double.parseDouble(loginputqty) * 100, null);
                if (nowlotorder.equals(IsLastLot)) {
                    if (maxlimit.equals("")) {
                        if (Double.parseDouble(nowrate) > Double.parseDouble(minlimit)) {
                            Podalerdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, lastlotstr, stPodalert, opdefectname, nowrate + "%", lastlotstr + "不良率為：" + nowrate + "%，超過比率標準" + minlimit + "%,請ME/PE整改！");
                            messageString = messageString + "    " + lastlotstr + opdefectname + "不良數為：" + ngqty + "，超過不良標準" + minlimit + "%,請ME/PE整改！\r\n";
                        }
                    } else {
                        if (Double.parseDouble(nowrate) >= Double.parseDouble(minlimit) && Double.parseDouble(nowrate) < Double.parseDouble(maxlimit)) {
                            Podalerdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, lastlotstr, stPodalert, opdefectname, nowrate + "%", lastlotstr + "不良率為：" + nowrate + "%，超過比率標準" + minlimit + "%,請ME/PE整改！");
                            messageString = messageString + "    " + lastlotstr + opdefectname + "不良數為：" + ngqty + "，超過不良標準" + minlimit + "%,請ME/PE整改！\r\n";
                        } else {
                            if (Double.parseDouble(nowrate) >= Double.parseDouble(maxlimit)) {
                                Podalerdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, lastlotstr, stPodalert, opdefectname, nowrate + "%", lastlotstr + "不良率為：" + nowrate + "%，超過比率標準" + maxlimit + "%,請停线整改！");
                                messageString = messageString + "    " + lastlotstr + opdefectname + "不良數為：" + ngqty + "，超過不良標準" + maxlimit + "%,請停線整改！\r\n";
                            }
                        }
                    }
                }
            }
            if (Podalerdt.Rowscount() > 0) {
                _a.ShowMessage(messageString);
                if (IsLastLot.equals("Y")) {
                    cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(Podalerdt), "EM039", "LH SFC System Lotno良率預警郵件：" + SFCStaticdata.staticmember.deviceno + "  尾數批 POD 特殊不良超標 。 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                } else if (IsLastLot.equals("N")) {
                    cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(Podalerdt), "EM039", "LH SFC System Lotno良率預警郵件：" + SFCStaticdata.staticmember.deviceno + "  正常批 POD 特殊不良超標 。 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                }
            }
        }
    }


    public static void checkout_submit16(ActivityInteractive _a,CallWebapi cwa,String lotno,String opno,String devicenotextBoxText,String opnotextBoxText,String dietextBoxText,
     String lotnotextBoxText,String setupqty,String opnametextBoxText,String loginputqty,String lotstate,String nextopnoflowid,String nowprocessno,String nextopno,
      String curqty,String  userid,String productno,String checkoutpcname,String nextopnametextBoxText,String defectqtytextBoxText,Boolean OQCbackchcbChecked,
       String nextopnotextBoxText,String wono,Boolean reworknocheck) throws Exception {
        Boolean vtqallholdfalg = false;
        Boolean vtqalertmailsendBoolean = false;
        String ip=SFCStaticdata.staticmember.HDSerialNo;
        if (SFCStaticdata.staticmember.vtqnoholdfalg.equals("1")|| reworknocheck) {
            String att19 = cwa.CallRS("getatt19data", opno);
            if (att19.equals("1") || att19.equals("3"))
            {
                String renextopnoflowid =String.valueOf(Integer.parseInt(nextopnoflowid) + 1);
                String renextopno =  cwa.CallRS("getnextopno", nowprocessno, renextopnoflowid);
                String relotserial = lotno + "-" + String.format("%03d",Integer.parseInt(nextopnoflowid));
                Boolean renextflag =cwa.CallRB("jumpnextopnodata",lotno, renextopno, renextopnoflowid, relotserial, userid, curqty, productno, checkoutpcname, nextopnoflowid, opno, nextopno);
            }
            else
            {
            }
        }
        else
        {
            if (devicenotextBoxText.equals("HC0806") && opnotextBoxText.equals("0640_1")) {
                //检测HC0806是否需要单测eolpodpog
                String podpognum = cwa.CallRS("checkout_submit_94", lotno);
                if (podpognum.equals(""))
                    podpognum = "0";
                String lotnosum = cwa.CallRS("checkout_submit_95", lotno);
                if (lotnosum.equals(""))
                    lotnosum = "0";
                String checksimple = cwa.CallRS("checkout_submit_96");
                if (checksimple.equals(""))
                    checksimple = "0";
                if (lotnosum.equals("0")) {
                } else {
                    if ((Double.parseDouble(podpognum) / Double.parseDouble(lotnosum)) > Double.parseDouble(checksimple)) {
                        Boolean uplotstate = cwa.CallRB("checkout_submit_97", lotno);
                        if (uplotstate) {
                            _a.ShowMessage("podpog不良超标，请进行单测作业");
                            return;
                        } else {
                            _a.ShowMessage("podpog不良超标，请进行单测作业，更改状态失败，请联系MIS手动作业！");
                            return;
                        }
                    }
                }
            }
            String aholdopno = cwa.CallRS("getallholdopnodata", opno);  //在FOL，EOL的Pack站位才Hold
            String aholdopnoG = cwa.CallRS("getallholdopnodataG", opno);  //在EOL的PACK站Hold
            if (!aholdopno.equals("") && aholdopno.equals("") && lotno.substring(0, 1).trim().equals("A")) {
                if (SFCStaticdata.staticmember.qcholddefalg.equals("2") && SFCStaticdata.staticmember.deviceno.equals("VT-Q")) {
                    DataTable dtRIstopdt = new DataTable();
                    String messageString1 = "整體不良率超標情況:\r\n";
                    DataTable VTQalerdt = new DataTable();
                    DataTable VTQstopdt = new DataTable();
                    VTQalerdt.AddColumn("Deviceno");
                    VTQalerdt.AddColumn("Lotno");
                    VTQalerdt.AddColumn("Station Name");
                    VTQalerdt.AddColumn("Qty In");
                    VTQalerdt.AddColumn("ErrorNoName");
                    VTQalerdt.AddColumn("NGQty");
                    VTQalerdt.AddColumn("NGYield");
                    VTQalerdt.AddColumn("Alert Line NGYield");
                    VTQalerdt.AddColumn("Line");
                    VTQalerdt.AddColumn("ReMark");
                    VTQstopdt.AddColumn("Deviceno");
                    VTQstopdt.AddColumn("Lotno");
                    VTQstopdt.AddColumn("Station Name");
                    VTQstopdt.AddColumn("Qty In");
                    VTQstopdt.AddColumn("ErrorNoName");
                    VTQstopdt.AddColumn("NGQty");
                    VTQstopdt.AddColumn("NGYield");
                    VTQstopdt.AddColumn("Stop Line NGYield");
                    VTQstopdt.AddColumn("Line");
                    VTQstopdt.AddColumn("ReMark");
                    VTQstopdt.AddColumn("Code");
                    String dtRIqty = dietextBoxText.trim();
                    String num = cwa.CallRS("checkout_submit_98");
                    num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                    String numbe = cwa.CallRS("checkout_submit_99");
                    if (numbe.equals("") || numbe == null) {
                        numbe = "0001";
                    } else {
                        if (!num.equals(numbe.substring(0, 8))) {
                            numbe = "0001";
                        } else {
                            int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                            numbe = ("0000" + String.valueOf(number1)).toString();
                            numbe = numbe.substring(numbe.length() - 4, 4);
                        }
                    }
                    String vtqinqtyzt = "";
                    String VTQLOTNOZ = lotnotextBoxText;
                    String VTQLOTZ = VTQLOTNOZ.substring(1, 12);
                    DataTable vtqinqtysqlzt = cwa.CallRDT("checkout_submit_100", "P" + VTQLOTZ);
                    if (vtqinqtysqlzt != null) {
                        if (vtqinqtysqlzt.Rowscount() > 0) {
                            vtqinqtyzt = vtqinqtysqlzt.Rows(0).get_CellValue("INPUTQTY");
                        } else {
                            vtqinqtyzt = "0";
                        }
                    } else {
                        vtqinqtyzt = "0";
                    }
                    String setupsum = cwa.CallRS("getsetupsumdata", lotno);
                    if (setupsum.equals("") || setupqty == null) {
                        setupsum = "0";
                    }
                    String numberdt = "ATE001" + num + numbe;
                    String dtRIlot = lotnotextBoxText;
                    String dtRIopno = cwa.CallRS("getatt18data", opno);
                    if (dtRIopno.equals("") || dtRIopno == null) {
                        dtRIopno = opno;
                    }
                    String dtRIlotno = lotnotextBoxText;
                    DataTable dtRIdefqty = cwa.CallRDT("checkout_submit_101", VTQLOTZ);
                    int vtqstrz = 0;
                    if (dtRIdefqty != null) {
                        if (dtRIdefqty.Rowscount() > 0) {
                            try {
                                vtqstrz = Integer.parseInt(dtRIdefqty.Rows(0).get_CellValue("ngqty").trim());
                            } catch (Exception ex) {
                                vtqstrz = 0;
                            }
                        }
                    }
                    String line = cwa.CallRS("checkout_submit_102", dtRIlotno);
                    int ngsinglesql1 = 10;
                    List<String> VTQparmer = new ArrayList<String>();
                    String ngyield1 = "";
                    try {
                        ngyield1 = BaseFuncation.doubletostring((((vtqstrz) / (Double.parseDouble(vtqinqtyzt) - Double.parseDouble(setupsum))) * 100), "0.00");
                    } catch (Exception ex) {
                        ngyield1 = "0.00";
                    }
                    String VTQholdsql = "begin ";
                    if ((Double.parseDouble(ngyield1) > (ngsinglesql1))) {
                        VTQstopdt.AddRow(SFCStaticdata.staticmember.deviceno, dtRIlot, opnametextBoxText, vtqinqtyzt, "整體不良率", String.valueOf(vtqstrz), ngyield1 + "%", ngsinglesql1 + "%", line, "請立即停機改善", numberdt);
                        messageString1 = messageString1 + cwa.CallRS("getopname", dtRIopno) + "站位投入為" + loginputqty + "," + "" + "的不良數量為" + dtRIdefqty + ",不良率為" + ngyield1 + ",超過停線不良率(Stop line:" + ngsinglesql1 + ")\r\n";
                        VTQholdsql = VTQholdsql + "insert into t_qcholdlotno "
                                + " (lotno,opno,yield,inuptqty,dbqty,createdate,styleflag,valuesflag,errorno,checkuser,checkip,unholddate,unholduser,unholdip,att1,att2,att3,att4) "
                                + " values (:lotno,:opno,:VTQNG,:vtqinqty,:b,sysdate,'0','1',:errorno,:userid,:ip,null,null,null,'VTQ整體','Open',:odbname,:numberdt);";
                        VTQparmer.add(dtRIlot);
                        VTQparmer.add(dtRIopno);
                        VTQparmer.add(ngyield1 + "%");
                        VTQparmer.add(vtqinqtyzt);
                        VTQparmer.add(String.valueOf(vtqstrz));
                        VTQparmer.add("");
                        VTQparmer.add(SFCStaticdata.staticmember.userid);
                        VTQparmer.add(SFCStaticdata.staticmember.HDSerialNo);
                        VTQparmer.add(SFCStaticdata.staticmember.deviceno);
                        VTQparmer.add(numberdt);
                        vtqallholdfalg = true;
                    }
                    if (vtqallholdfalg) {
                        if (SFCStaticdata.staticmember.lotcheckeolflag) {
                            if (VTQstopdt.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(VTQstopdt), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBoxText.trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                            }
                        } else if (SFCStaticdata.staticmember.lotfolbingpicheck) {
                            if (VTQstopdt.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(VTQstopdt), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBoxText.trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                            }
                        } else {

                            if (VTQstopdt.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(VTQstopdt), "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + "  " + opnametextBoxText.trim() + " 整体良率超标ON-HOLD  Stop Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "1");
                            }
                        }
                        if (VTQstopdt.Rowscount() > 0) {
                            _a.ShowMessage(messageString1);
                            VTQholdsql = VTQholdsql + "end;";
                            String[] vtqparmerarray = new String[VTQparmer.size()];
                            Boolean holdBoolean = cwa.CallRB("checkout_submit_90", VTQholdsql, BaseFuncation.SerializeObjectArrayString(VTQparmer.toArray(vtqparmerarray)));
                            if (!holdBoolean) {
                                _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                return;
                            }
                        }
                    }
                }
            }

            Boolean _newycl =cwa.CallRB("newycldevflag",SFCStaticdata.staticmember.newdeviceno);//WZH 0513 N41 8 59
            if (_newycl)
            {
                Boolean newyclvalue =cwa.CallRB("YCLemail", lotno, opno, SFCStaticdata.staticmember.ip, SFCStaticdata.staticmember.newdeviceno, SFCStaticdata.staticmember.odbname, productno);
            }
            else {
                // vtqHOLD預警  董
                if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") || SFCStaticdata.staticmember.deviceno.equals("NH-A")) {
                    vtqallholdfalg = false;
                    String messageStringvtq = "單項不良率超標情況:\r\n";
                    DataTable vtqalerdt = new DataTable();
                    vtqalerdt.AddColumn("Deviceno");
                    vtqalerdt.AddColumn("Lotno");
                    vtqalerdt.AddColumn("Station Name");
                    vtqalerdt.AddColumn("Qty In");
                    vtqalerdt.AddColumn("ErrorNoName");
                    vtqalerdt.AddColumn("NGQty");
                    vtqalerdt.AddColumn("NGYield");
                    vtqalerdt.AddColumn("Alert Line NGYield");
                    vtqalerdt.AddColumn("Line");
                    vtqalerdt.AddColumn("ReMark");
                    DataTable vtqstopdtE = new DataTable();
                    vtqstopdtE.AddColumn("Deviceno");
                    vtqstopdtE.AddColumn("Lotno");
                    vtqstopdtE.AddColumn("Station Name");
                    vtqstopdtE.AddColumn("Qty In");
                    vtqstopdtE.AddColumn("ErrorNoName");
                    vtqstopdtE.AddColumn("NGQty");
                    vtqstopdtE.AddColumn("NGYield");
                    vtqstopdtE.AddColumn("Stop Line NGYield");
                    vtqstopdtE.AddColumn("Line");
                    vtqstopdtE.AddColumn("ReMark");
                    vtqstopdtE.AddColumn("Code");
                    DataTable vtqstopdtLCB = new DataTable();
                    vtqstopdtLCB.AddColumn("Deviceno");
                    vtqstopdtLCB.AddColumn("Lotno");
                    vtqstopdtLCB.AddColumn("Station Name");
                    vtqstopdtLCB.AddColumn("Qty In");
                    vtqstopdtLCB.AddColumn("ErrorNoName");
                    vtqstopdtLCB.AddColumn("NGQty");
                    vtqstopdtLCB.AddColumn("NGYield");
                    vtqstopdtLCB.AddColumn("Stop Line NGYield");
                    vtqstopdtLCB.AddColumn("Line");
                    vtqstopdtLCB.AddColumn("ReMark");
                    vtqstopdtLCB.AddColumn("Code");
                    DataTable vtqstopdtDP = new DataTable();
                    vtqstopdtDP.AddColumn("Deviceno");
                    vtqstopdtDP.AddColumn("Lotno");
                    vtqstopdtDP.AddColumn("Station Name");
                    vtqstopdtDP.AddColumn("Qty In");
                    vtqstopdtDP.AddColumn("ErrorNoName");
                    vtqstopdtDP.AddColumn("NGQty");
                    vtqstopdtDP.AddColumn("NGYield");
                    vtqstopdtDP.AddColumn("Stop Line NGYield");
                    vtqstopdtDP.AddColumn("Line");
                    vtqstopdtDP.AddColumn("ReMark");
                    vtqstopdtDP.AddColumn("Code");
                    DataTable vtqstopdtSFR37 = new DataTable();
                    vtqstopdtSFR37.AddColumn("Deviceno");
                    vtqstopdtSFR37.AddColumn("Lotno");
                    vtqstopdtSFR37.AddColumn("Station Name");
                    vtqstopdtSFR37.AddColumn("Qty In");
                    vtqstopdtSFR37.AddColumn("ErrorNoName");
                    vtqstopdtSFR37.AddColumn("NGQty");
                    vtqstopdtSFR37.AddColumn("NGYield");
                    vtqstopdtSFR37.AddColumn("Stop Line NGYield");
                    vtqstopdtSFR37.AddColumn("Line");
                    vtqstopdtSFR37.AddColumn("ReMark");
                    vtqstopdtSFR37.AddColumn("Code");
                    DataTable vtqpod = new DataTable();
                    vtqpod.AddColumn("Deviceno");
                    vtqpod.AddColumn("Lotno");
                    vtqpod.AddColumn("Station Name");
                    vtqpod.AddColumn("Qty In");
                    vtqpod.AddColumn("ErrorNoName");
                    vtqpod.AddColumn("NGQty");
                    vtqpod.AddColumn("NGYield");
                    vtqpod.AddColumn("Stop Line NGYield");
                    vtqpod.AddColumn("Line");
                    vtqpod.AddColumn("ReMark");
                    vtqpod.AddColumn("Code");
                    DataTable vtqpog = new DataTable();
                    vtqpog.AddColumn("Deviceno");
                    vtqpog.AddColumn("Lotno");
                    vtqpog.AddColumn("Station Name");
                    vtqpog.AddColumn("Qty In");
                    vtqpog.AddColumn("ErrorNoName");
                    vtqpog.AddColumn("NGQty");
                    vtqpog.AddColumn("NGYield");
                    vtqpog.AddColumn("Stop Line NGYield");
                    vtqpog.AddColumn("Line");
                    vtqpog.AddColumn("ReMark");
                    vtqpog.AddColumn("Code");
                    DataTable vtqpodg = new DataTable();
                    vtqpodg.AddColumn("Deviceno");
                    vtqpodg.AddColumn("Lotno");
                    vtqpodg.AddColumn("Station Name");
                    vtqpodg.AddColumn("Qty In");
                    vtqpodg.AddColumn("ErrorNoName");
                    vtqpodg.AddColumn("NGQty");
                    vtqpodg.AddColumn("NGYield");
                    vtqpodg.AddColumn("Stop Line NGYield");
                    vtqpodg.AddColumn("Line");
                    vtqpodg.AddColumn("ReMark");
                    vtqpodg.AddColumn("Code");
                    DataTable vtqpogg = new DataTable();
                    vtqpogg.AddColumn("Deviceno");
                    vtqpogg.AddColumn("Lotno");
                    vtqpogg.AddColumn("Station Name");
                    vtqpogg.AddColumn("Qty In");
                    vtqpogg.AddColumn("ErrorNoName");
                    vtqpogg.AddColumn("NGQty");
                    vtqpogg.AddColumn("NGYield");
                    vtqpogg.AddColumn("Stop Line NGYield");
                    vtqpogg.AddColumn("Line");
                    vtqpogg.AddColumn("ReMark");
                    vtqpogg.AddColumn("Code");
                    DataTable vtqSV = new DataTable();
                    vtqSV.AddColumn("Deviceno");
                    vtqSV.AddColumn("Lotno");
                    vtqSV.AddColumn("Station Name");
                    vtqSV.AddColumn("Qty In");
                    vtqSV.AddColumn("ErrorNoName");
                    vtqSV.AddColumn("NGQty");
                    vtqSV.AddColumn("NGYield");
                    vtqSV.AddColumn("Stop Line NGYield");
                    vtqSV.AddColumn("Line");
                    vtqSV.AddColumn("ReMark");
                    vtqSV.AddColumn("Code");
                    String vtqholdsql = "begin ";
                    String name = "";
                    List<String> vtqparmer = new ArrayList<String>();
                    DataTable dtvtqalert = cwa.CallRDT("checkout_submit_103", lotno, opno);
                    if (dtvtqalert == null) {
                        _a.ShowMessage("查詢該批號的不良記錄異常，請聯繫MIS");
                        return;
                    } else {
                        if (dtvtqalert.Rowscount() > 0 || (!aholdopno.equals("") && aholdopno != null)) {
                            String setupsum = cwa.CallRS("getsetupsumdata", lotno);
                            if (setupsum.equals("") || setupqty == null) {
                                setupsum = "0";
                            }
                            String vtqlot = "";
                            String vtqopno = "";
                            String vtqopdefectno = "";
                            String vtqopdefectname = "";
                            String vtqngqty = "0";
                            if (!aholdopno.equals("") && aholdopno != null) {
                                vtqlot = lotno;
                                vtqopno = opno;
                            } else {
                                vtqlot = dtvtqalert.Rows(0).get_CellValue("lotno");
                                vtqopno = dtvtqalert.Rows(0).get_CellValue("opno");
                                vtqopdefectno = dtvtqalert.Rows(0).get_CellValue("opnodefectno");
                                vtqopdefectname = dtvtqalert.Rows(0).get_CellValue("opdefectname");
                                vtqngqty = dtvtqalert.Rows(0).get_CellValue("ngqty");
                            }
                            DataTable VTQ1 = cwa.CallRDT("checkout_submit_104", lotno, opno);
                            DataTable VTQ2 = cwa.CallRDT("checkout_submit_105", lotno, opno);
                            DataTable VTQ3 = cwa.CallRDT("checkout_submit_106", lotno, opno);
                            DataTable VTQ4 = cwa.CallRDT("checkout_submit_108", vtqopno, vtqopdefectno);
                            DataTable VTQ5 = cwa.CallRDT("checkout_submit_109", vtqopno, vtqopdefectno);
                            DataTable VTQ6 = cwa.CallRDT("checkout_submit_107", lotno, opno);  //VTQ
                            if (SFCStaticdata.staticmember.lotcheckeolflag && SFCStaticdata.staticmember.newdeviceno.equals("ATF001")) {
                                if (!opno.equals("0002_1")) {
                                    VTQ2 = new DataTable();
                                    VTQ3 = new DataTable();
                                }
                            }
                            String line = cwa.CallRS("checkout_submit_110", lotno);
                            if (line.equals("") || line == null) {
                                switch (vtqlot.substring(0, 1)) {
                                    case "A":
                                        line = "EOL";
                                        break;
                                    case "P":
                                        line = "FOL";
                                        break;
                                    case "S":
                                        line = "SMT";
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (VTQ1.Rowscount() > 0 || VTQ2.Rowscount() > 0 || VTQ3.Rowscount() > 0 || (!aholdopno.equals("") && aholdopno != null) || VTQ4.Rowscount() > 0 || VTQ5.Rowscount() > 0 || VTQ6.Rowscount() > 0) {
                                String VTQLOTNO = lotnotextBoxText;
                                String VTQLOT = "";
                                if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") || SFCStaticdata.staticmember.newdeviceno.equals("ATF001")) {
                                    VTQLOT = VTQLOTNO.substring(1, 13);
                                } else {
                                    VTQLOT = VTQLOTNO.substring(1, 12);
                                }
                                DataTable vtqinqtysql1 = cwa.CallRDT("checkout_submit_111", VTQLOT);
                                String vtqinqty = "";
                                if (vtqinqtysql1.Rowscount() > 0) {
                                    vtqinqty = vtqinqtysql1.Rows(0).get_CellValue("INPUTQTY");
                                }
                                DataTable vtqngqtyE = cwa.CallRDT("checkout_submit_112", VTQLOT);
                                DataTable vtqngqtyLCB = cwa.CallRDT("checkout_submit_113", VTQLOT);
                                DataTable vtqngqtyDP = cwa.CallRDT("checkout_submit_114", VTQLOT);
                                DataTable vtqngqtypod = cwa.CallRDT("checkout_submit_115", lotno, opno);
                                DataTable vtqngqtypog = cwa.CallRDT("checkout_submit_116", lotno, opno);
                                DataTable vtqngqtyLCBF = cwa.CallRDT("checkout_submit_117", VTQLOT);
                                DataTable vtqngqtyDPF = cwa.CallRDT("checkout_submit_118", VTQLOT);
                                DataTable vtqngqtyLCBE = cwa.CallRDT("checkout_sumit_119", VTQLOT);
                                DataTable vtqngqtyDPE = cwa.CallRDT("checkout_submit_120", VTQLOT);
                                DataTable vtqngqtySV = cwa.CallRDT("checkout_submit_121", lotno, opno);
                                if (vtqngqtyE != null && VTQ1 != null && vtqngqtyE.Rowscount() > 0 && VTQ1.Rowscount() > 0) {
                                    String holderrorstrlcbe = vtqngqtyE.Rows(0).get_CellValue("opdefectno").trim();
                                    int vtqstr1 = 0;
                                    for (int a = 0; a < vtqngqtyE.Rowscount(); a++) {
                                        vtqstr1 = (Integer.parseInt(vtqngqtyE.Rows(a).get_CellValue("ngqty").trim())) + vtqstr1;
                                    }
                                    Double maxyeildE = Double.parseDouble(cwa.CallRS("getvtqyielddata", opno, holderrorstrlcbe));
                                    String VTQNGE = BaseFuncation.doubletostring(((Double.parseDouble(String.valueOf(vtqstr1)) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                    if ((Double.parseDouble(VTQNGE) > maxyeildE)) {
                                        String num = cwa.CallRS("checkout_submit_122");
                                        num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                        String[] numberparmer = {};
                                        String numbe = cwa.CallRS("checkout_submit_123");
                                        if (numbe.equals("") || numbe == null) {
                                            numbe = "0001";
                                        } else {
                                            if (!num.equals(numbe.substring(0, 8))) {
                                                numbe = "0001";
                                            } else {
                                                int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                numbe = ("0000" + String.valueOf(number1));
                                                numbe = numbe.substring(numbe.length() - 4, 4);
                                            }
                                        }
                                        String dev = devicenotextBoxText.trim();
                                        String NUM = cwa.CallRS("checkout_submit_124", dev);
                                        String numberdt = NUM + num + numbe;
                                        vtqstopdtE.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, vtqinqty, "電性不良", String.valueOf(vtqstr1), VTQNGE + "%", maxyeildE + "%", line, "請立即停機改善", numberdt);
                                        messageStringvtq = messageStringvtq + cwa.CallRS("getopname", vtqopno) + "站位投入為" + vtqinqty + ",E-FAIL的不良數量為" + vtqstr1 + ",不良率為" + VTQNGE + ",超過停線不良率(Stop line:" + maxyeildE + ")\r\n";
                                        if (!vtqalertmailsendBoolean) {
                                            vtqalertmailsendBoolean = true;
                                        }
                                        String vtqholdsqlE = "";
                                        String qcholdopno = getqcholdopno(cwa, "E", nextopnotextBoxText, opnotextBoxText);
                                        if (qcholdopno.equals("") || qcholdopno == null) {
                                        } else {
                                            vtqopno = qcholdopno;
                                        }
                                        _a.ShowMessage(messageStringvtq);
                                        Boolean holdBoolean = cwa.CallRB("checkout_submit_125", vtqlot, vtqopno, VTQNGE, loginputqty, String.valueOf(vtqstr1), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                        if (!holdBoolean) {
                                            _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                            return;
                                        }
                                        vtqallholdfalg = true;
                                    }
                                }
                                if (vtqngqtypod.Rowscount() > 0) {
                                    String holderrorstrlcbe = vtqngqtypod.Rows(0).get_CellValue("opdefectno").trim();
                                    int vtqstr1 = 0;
                                    for (int a = 0; a < vtqngqtypod.Rowscount(); a++) {
                                        vtqstr1 = (Integer.parseInt(vtqngqtypod.Rows(a).get_CellValue("ngqty").trim())) + vtqstr1;
                                    }
                                    Double maxyeildE = Double.parseDouble(cwa.CallRS("getvtqyielddata1", opno, holderrorstrlcbe));
                                    String VTQNGE = BaseFuncation.doubletostring(((Double.parseDouble(String.valueOf(vtqstr1)) / Double.parseDouble(loginputqty)) * 100), "0.00");
                                    if ((Double.parseDouble(VTQNGE) > maxyeildE)) {
                                        String num = cwa.CallRS("checkout_submit_122");
                                        num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                        String numbe = cwa.CallRS("checkout_submit_126");
                                        if (numbe.equals("") || numbe == null) {
                                            numbe = "0001";
                                        } else {
                                            if (!num.equals(numbe.substring(0, 8))) {
                                                numbe = "0001";
                                            } else {
                                                int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                numbe = ("0000" + String.valueOf(number1));
                                                numbe = numbe.substring(numbe.length() - 4, 4);
                                            }
                                        }
                                        String dev = devicenotextBoxText.trim();
                                        String NUM = cwa.CallRS("checkout_submit_124", dev);
                                        String numberdt = NUM + num + numbe;
                                        vtqpod.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, loginputqty, "POD不良", String.valueOf(vtqstr1), VTQNGE + "%", maxyeildE + "%", line, "請立即停機改善", numberdt);
                                        messageStringvtq = messageStringvtq + opnametextBoxText + "站位投入為" + vtqinqty + ",POD的不良數量為" + vtqstr1 + ",不良率為" + VTQNGE + ",超過停線不良率(Stop line:" + maxyeildE + ")\r\n";
                                        String vtqholdsqlE = "";
                                        String qcholdopno = getqcholdopno(cwa, "POD", nextopnotextBoxText, opnotextBoxText);
                                        if (qcholdopno.equals("") || qcholdopno == null) {
                                        } else {
                                            vtqopno = qcholdopno;
                                        }
                                        _a.ShowMessage(messageStringvtq);
                                        Boolean holdBoolean = cwa.CallRB("checkout_submit_127", vtqlot, vtqopno, VTQNGE, loginputqty, String.valueOf(vtqstr1), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                        if (!holdBoolean) {
                                            _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                            return;
                                        }
                                        vtqalertmailsendBoolean = true;
                                        vtqallholdfalg = true;
                                    }
                                }
                                if (vtqngqtypog.Rowscount() > 0) {
                                    String holderrorstrlcbe = vtqngqtypog.Rows(0).get_CellValue("opdefectno").trim();
                                    int vtqstr1 = 0;
                                    for (int a = 0; a < vtqngqtypog.Rowscount(); a++) {
                                        vtqstr1 = (Integer.parseInt(vtqngqtypog.Rows(a).get_CellValue("ngqty").trim())) + vtqstr1;
                                    }
                                    Double maxyeildE = Double.parseDouble(cwa.CallRS("getvtqyielddata1", opno, holderrorstrlcbe));
                                    String VTQNGE = BaseFuncation.doubletostring(vtqstr1 / Double.parseDouble(loginputqty) * 100, "0.00");
                                    if ((Double.parseDouble(VTQNGE) > maxyeildE)) {
                                        String num = cwa.CallRS("checkout_submit_122");
                                        num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                        String numbe = cwa.CallRS("checkout_submit_123");
                                        if (numbe.equals("") || numbe == null) {
                                            numbe = "0001";
                                        } else {
                                            if (!num.equals(numbe.substring(0, 8))) {
                                                numbe = "0001";
                                            } else {
                                                int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                numbe = ("0000" + String.valueOf(number1));
                                                numbe = numbe.substring(numbe.length() - 4, 4);
                                            }
                                        }
                                        String dev = devicenotextBoxText.trim();
                                        String NUM = cwa.CallRS("checkout_submit_124", dev);
                                        String numberdt = NUM + num + numbe;
                                        vtqpog.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, loginputqty, "POG不良", String.valueOf(vtqstr1), VTQNGE + "%", maxyeildE + "%", line, "請立即停機改善", numberdt);
                                        messageStringvtq = messageStringvtq + opnametextBoxText + "站位投入為" + vtqinqty + ",POG的不良數量為" + vtqstr1 + ",不良率為" + VTQNGE + ",超過停線不良率(Stop line:" + maxyeildE + ")\r\n";
                                        String vtqholdsqlE = "";
                                        String qcholdopno = getqcholdopno(cwa, "POG", nextopnotextBoxText, opnotextBoxText);
                                        if (qcholdopno.equals("") || qcholdopno == null) {
                                        } else {
                                            vtqopno = qcholdopno;
                                        }
                                        _a.ShowMessage(messageStringvtq);
                                        Boolean holdBoolean = cwa.CallRB("checkout_submit_128", vtqlot, vtqopno, VTQNGE, loginputqty, String.valueOf(vtqstr1), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                        if (!holdBoolean) {
                                            _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                            return;
                                        }
                                        vtqalertmailsendBoolean = true;
                                        vtqallholdfalg = true;
                                    }
                                }
                                if (vtqngqtySV.Rowscount() > 0) {
                                    String holderrorstrlcbe = vtqngqtySV.Rows(0).get_CellValue("opdefectno").trim();
                                    int vtqstr1 = 0;
                                    for (int a = 0; a < vtqngqtySV.Rowscount(); a++) {
                                        vtqstr1 = (Integer.parseInt(vtqngqtySV.Rows(a).get_CellValue("ngqty").trim())) + vtqstr1;
                                    }
                                    Double maxyeildE = Double.parseDouble(cwa.CallRS("getvtqyielddata", opno, holderrorstrlcbe));
                                    String VTQNGE = BaseFuncation.doubletostring(vtqstr1 / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum)) * 100, "0.00");
                                    if ((Double.parseDouble(VTQNGE) > maxyeildE)) {
                                        String num = cwa.CallRS("checkout_submit_122");
                                        num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                        String numbe = cwa.CallRS("checkout_submit_123");
                                        if (numbe.equals("") || numbe == null) {
                                            numbe = "0001";
                                        } else {
                                            if (!num.equals(numbe.substring(0, 8))) {
                                                numbe = "0001";
                                            } else {
                                                int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                numbe = ("0000" + String.valueOf(number1));
                                                numbe = numbe.substring(numbe.length() - 4, 4);
                                            }
                                        }
                                        String dev = devicenotextBoxText.trim();
                                        String NUM = cwa.CallRS("checkout_submit_124", dev);
                                        String numberdt = NUM + num + numbe;
                                        vtqSV.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, loginputqty, "Sealvoid不良", String.valueOf(vtqstr1), VTQNGE + "%", maxyeildE + "%", line, "請立即停機改善", numberdt);
                                        messageStringvtq = messageStringvtq + opnametextBoxText + "站位投入為" + vtqinqty + ",Sealvoid的不良數量為" + vtqstr1 + ",不良率為" + VTQNGE + ",超過停線不良率(Stop line:" + maxyeildE + ")\r\n";
                                        String vtqholdsqlE = "";
                                        String qcholdopno = getqcholdopno(cwa, "sealvoid", nextopnotextBoxText, opnotextBoxText);
                                        if (qcholdopno.equals("") || qcholdopno == null) {
                                        } else {
                                            vtqopno = qcholdopno;
                                        }
                                        _a.ShowMessage(messageStringvtq);
                                        Boolean holdBoolean = cwa.CallRB("checkout_submit_129", vtqlot, vtqopno, VTQNGE, loginputqty, String.valueOf(vtqstr1), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                        if (!holdBoolean) {
                                            _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                            return;
                                        }
                                        vtqalertmailsendBoolean = true;
                                        vtqallholdfalg = true;
                                    }
                                }
                                if (vtqngqtyLCB.Rowscount() > 0 && lotno.substring(0, 1).trim().equals("A")) {
                                    int vtqstr2F = 0;
                                    for (int c = 0; c < vtqngqtyLCBF.Rowscount(); c++) {
                                        vtqstr2F = (Integer.parseInt(vtqngqtyLCBF.Rows(c).get_CellValue("ngqty").trim())) + vtqstr2F;
                                    }
                                    int dF = vtqstr2F;
                                    String VTQNGLCBF = BaseFuncation.doubletostring((((dF) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                    if (vtqngqtyLCBF.Rowscount() > 0 && (Double.parseDouble(VTQNGLCBF)) > 2) {
                                        if (!SFCStaticdata.staticmember.vtqnoholdfalg.equals("1")) {
                                            String holderrorstrlcbG = vtqngqtyLCBE.Rows(0).get_CellValue("opdefectno").trim();
                                            int vtqstr2G = 0;
                                            for (int c = 0; c < vtqngqtyLCBE.Rowscount(); c++) {
                                                vtqstr2G = (Integer.parseInt(vtqngqtyLCBE.Rows(c).get_CellValue("ngqty").trim())) + vtqstr2G;
                                            }
                                            int dG = vtqstr2G;
                                            Double maxyeildLCBG = Double.parseDouble(cwa.CallRS("getvtqyielddata2", opno, holderrorstrlcbG));//設計的lcb良率
                                            String VTQNGLCBG = BaseFuncation.doubletostring((((dG) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");//當前良率
                                            if ((Double.parseDouble(VTQNGLCBG) > maxyeildLCBG)) {
                                                String numG = cwa.CallRS("checkout_submit_122");
                                                numG = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(numG, "yyyyMMdd").getTime(), "yyyyMMdd");
                                                String numbeG = cwa.CallRS("checkout_submit_123");
                                                if (numbeG.equals("") || numbeG == null) {
                                                    numbeG = "0001";
                                                } else {
                                                    if (!numG.equals(numbeG.substring(0, 8))) {
                                                        numbeG = "0001";
                                                    } else {
                                                        int number1G = Integer.parseInt(numbeG.substring(numbeG.length() - 4, 4)) + 1;
                                                        numbeG = "0000" + String.valueOf(number1G);
                                                        numbeG = numbeG.substring(numbeG.length() - 4, 4);
                                                    }
                                                }
                                                String devG = devicenotextBoxText.trim();//機種
                                                String NUMG = cwa.CallRS("checkout_submit_124", devG);
                                                String numberdtG = NUMG + numG + numbeG;
                                                vtqpodg.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, vtqinqty, "LCB不良", String.valueOf(dG), VTQNGLCBG + "%", "增幅" + maxyeildLCBG + "%", line, "請立即停機改善", numberdtG);
                                                messageStringvtq = messageStringvtq + cwa.CallRS("getopname", vtqopno) + "站位投入為" + vtqinqty + ",LCB的不良數量為" + dG + ",不良率為" + VTQNGLCBG + ",在前段POD超標基礎上超過停線不良率(Stop line:" + maxyeildLCBG + ")\r\n";
                                                String vtqholdsqlLCBG = "";
                                                String qcholdopno = getqcholdopno(cwa, "LCB", nextopnotextBoxText, opnotextBoxText);
                                                if (qcholdopno.equals("") || qcholdopno == null) {
                                                } else {
                                                    vtqopno = qcholdopno;
                                                }
                                                if (opno.equals("0002_1")) {
                                                    vtqopno = "FQC CHECK";
                                                }
                                                _a.ShowMessage(messageStringvtq);
                                                vtqalertmailsendBoolean = true;
                                                if (!aholdopnoG.equals("") && aholdopnoG != null) {
                                                    Boolean holdBooleanG = cwa.CallRB("checkout_submit_130", vtqlot, vtqopno, VTQNGLCBG, loginputqty, String.valueOf(dG), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdtG);
                                                    if (!holdBooleanG) {
                                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                                        return;
                                                    }
                                                    vtqallholdfalg = true;
                                                }
                                            }
                                        }
                                    } else if ((Double.parseDouble(VTQNGLCBF)) <= 2) {
                                        if (!SFCStaticdata.staticmember.vtqnoholdfalg.equals("1"))  //特殊批號放過去
                                        {
                                            String holderrorstrlcb = vtqngqtyLCB.Rows(0).get_CellValue("opdefectno").trim();
                                            int vtqstr2 = 0;
                                            for (int c = 0; c < vtqngqtyLCB.Rowscount(); c++) {
                                                vtqstr2 = (Integer.parseInt(vtqngqtyLCB.Rows(c).get_CellValue("ngqty").trim())) + vtqstr2;
                                            }
                                            int d = vtqstr2;
                                            Double maxyeildLCB = Double.parseDouble(cwa.CallRS("getvtqyielddata", opno, holderrorstrlcb));
                                            String VTQNGLCB = BaseFuncation.doubletostring((((d) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                            if ((Double.parseDouble(VTQNGLCB) > maxyeildLCB)) {
                                                String num = cwa.CallRS("checkout_submit_122");
                                                num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                                String numbe = cwa.CallRS("checkout_submit_123");
                                                if (numbe.equals("") || numbe == null) {
                                                    numbe = "0001";
                                                } else {
                                                    if (!num.equals(numbe.substring(0, 8))) {
                                                        numbe = "0001";
                                                    } else {
                                                        int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                        numbe = ("0000" + String.valueOf(number1));
                                                        numbe = numbe.substring(numbe.length() - 4, 4);
                                                    }
                                                }
                                                String dev = devicenotextBoxText.trim();
                                                String NUM = cwa.CallRS("checkout_submit_124", dev);
                                                String numberdt = NUM + num + numbe;
                                                vtqstopdtLCB.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, vtqinqty, "LCB不良", String.valueOf(d), VTQNGLCB + "%", maxyeildLCB + "%", line, "請立即停機改善", numberdt);
                                                messageStringvtq = messageStringvtq + cwa.CallRS("getopname", vtqopno) + "站位投入為" + vtqinqty + ",LCB的不良數量為" + d + ",不良率為" + VTQNGLCB + ",超過停線不良率(Stop line:" + maxyeildLCB + ")\r\n";
                                                String vtqholdsqlLCB = "";
                                                String qcholdopno = getqcholdopno(cwa, "LCB", nextopnotextBoxText, opnotextBoxText);
                                                if (qcholdopno.equals("") || qcholdopno == null) {
                                                } else {
                                                    vtqopno = qcholdopno;
                                                }
                                                if (opno.equals("0002_1")) {
                                                    vtqopno = "FQC CHECK";
                                                }
                                                _a.ShowMessage(messageStringvtq);
                                                vtqalertmailsendBoolean = true;
                                                if (!aholdopnoG.equals("") && aholdopnoG != null) {
                                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_131");
                                                    if (!holdBoolean) {
                                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                                        return;
                                                    }
                                                    vtqallholdfalg = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (vtqngqtyDP.Rowscount() > 0 && lotno.substring(0, 1).trim().equals("A")) {
                                    int vtqstr3F = 0;
                                    for (int c = 0; c < vtqngqtyDPF.Rowscount(); c++) {
                                        vtqstr3F = (Integer.parseInt(vtqngqtyDPF.Rows(c).get_CellValue("ngqty").trim())) + vtqstr3F;
                                    }
                                    int d1F = vtqstr3F;
                                    String VTQNGDPF = BaseFuncation.doubletostring((((d1F) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                    if (vtqngqtyDPF.Rowscount() > 0 && (Double.parseDouble(VTQNGDPF)) > 6) {
                                        if (!SFCStaticdata.staticmember.vtqnoholdfalg.equals("1")) {
                                            String holderrorstrlcbG1 = vtqngqtyDPE.Rows(0).get_CellValue("opdefectno").trim();
                                            int vtqstr3G = 0;
                                            for (int c = 0; c < vtqngqtyDPE.Rowscount(); c++) {
                                                vtqstr3G = (Integer.parseInt(vtqngqtyDPE.Rows(c).get_CellValue("ngqty").trim())) + vtqstr3G;
                                            }
                                            int d1G = vtqstr3G;
                                            Double maxyeildDPG = Double.parseDouble(cwa.CallRS("getvtqyielddata2", opno, holderrorstrlcbG1));
                                            String VTQNGDPG = BaseFuncation.doubletostring((((d1G) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");//後段當前良率
                                            if ((Double.parseDouble(VTQNGDPG) > maxyeildDPG)) {
                                                String numG = cwa.CallRS("checkout_submit_122");
                                                numG = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(numG, "yyyyMMdd").getTime(), "yyyyMMdd");
                                                String numbeG = cwa.CallRS("checkout_submit_123");
                                                if (numbeG.equals("") || numbeG == null) {
                                                    numbeG = "0001";
                                                } else {
                                                    if (numG != numbeG.substring(0, 8)) {
                                                        numbeG = "0001";
                                                    } else {
                                                        int number1G = Integer.parseInt(numbeG.substring(numbeG.length() - 4, 4)) + 1;
                                                        numbeG = ("0000" + String.valueOf(number1G));
                                                        numbeG = numbeG.substring(numbeG.length() - 4, 4);
                                                    }
                                                }
                                                String devG = devicenotextBoxText.trim();//機種
                                                String NUMG = cwa.CallRS("checkout_submit_124", devG);
                                                String numberdtG = NUMG + numG + numbeG;
                                                vtqpogg.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, vtqinqty, "DP不良", String.valueOf(d1G), VTQNGDPG + "%", "增幅" + maxyeildDPG + "%", line, "請立即停機改善", numberdtG);
                                                messageStringvtq = messageStringvtq + cwa.CallRS("getopname", vtqopno) + "站位投入為" + vtqinqty + ",DP的不良數量為" + d1G + ",不良率為" + VTQNGDPG + ",在前段POD超標基礎上超過停線不良率(Stop line:" + maxyeildDPG + ")\r\n";
                                                String vtqholdsqlDPG = "";
                                                String qcholdopno = getqcholdopno(cwa, "DP", nextopnotextBoxText, opnotextBoxText);
                                                if (qcholdopno.equals("") || qcholdopno == null) {
                                                } else {
                                                    vtqopno = qcholdopno;
                                                }

                                                _a.ShowMessage(messageStringvtq);
                                                vtqalertmailsendBoolean = true;

                                                if (!aholdopnoG.equals("") && aholdopnoG != null) {
                                                    Boolean holdBooleanG = cwa.CallRB("checkout_submit_132", vtqlot, vtqopno, VTQNGDPG, loginputqty, String.valueOf(d1G), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdtG);
                                                    if (!holdBooleanG) {
                                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                                        return;
                                                    }
                                                    vtqallholdfalg = true;
                                                }
                                            }
                                        }
                                    } else if ((Double.parseDouble(VTQNGDPF)) <= 6) {
                                        if (SFCStaticdata.staticmember.vtqnoholdfalg.equals("1")) {
                                        } else {
                                            String holderrorstr = vtqngqtyDP.Rows(0).get_CellValue("opdefectno").trim();
                                            int vtqstr3 = 0;
                                            for (int g = 0; g < vtqngqtyDP.Rowscount(); g++) {
                                                vtqstr3 = (Integer.parseInt(vtqngqtyDP.Rows(g).get_CellValue("ngqty").trim())) + vtqstr3;
                                            }
                                            int f = vtqstr3;
                                            Double maxyeildDP = Double.parseDouble(cwa.CallRS("getvtqyielddata", opno, holderrorstr));
                                            String VTQNGDP = BaseFuncation.doubletostring((((f) / (Double.parseDouble(vtqinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                            if ((Double.parseDouble(VTQNGDP) > maxyeildDP)) {
                                                String num = cwa.CallRS("checkout_submit_122");
                                                num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                                String numbe = cwa.CallRS("checkout_submit_123");
                                                if (numbe.equals("") || numbe == null) {
                                                    numbe = "0001";
                                                } else {
                                                    if (!num.equals(numbe.substring(0, 8))) {
                                                        numbe = "0001";
                                                    } else {
                                                        int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                        numbe = ("0000" + String.valueOf(number1));
                                                        numbe = numbe.substring(numbe.length() - 4, 4);
                                                    }
                                                }
                                                String dev = devicenotextBoxText.trim();
                                                String NUM = cwa.CallRS("checkout_submit_124", dev);
                                                String numberdt = NUM + num + numbe;
                                                vtqstopdtDP.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, vtqinqty, "DP不良", String.valueOf(f), VTQNGDP + "%", maxyeildDP + "%", line, "請立即停機改善", numberdt);
                                                messageStringvtq = messageStringvtq + cwa.CallRS("getopname", vtqopno) + "站位投入為" + vtqinqty + ",DP的不良數量為" + f + ",不良率為" + VTQNGDP + ",超過停線不良率(Stop line:" + maxyeildDP + ")\r\n";
                                                String vtqholdsqlDP = "";
                                                String qcholdopno = getqcholdopno(cwa, "DP", nextopnotextBoxText, opnotextBoxText);
                                                if (qcholdopno.equals("") || qcholdopno == null) {
                                                } else {
                                                    vtqopno = qcholdopno;
                                                }
                                                _a.ShowMessage(messageStringvtq);
                                                vtqalertmailsendBoolean = true;
                                                if (!aholdopnoG.equals("") && aholdopnoG != null) {
                                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_133", vtqlot, vtqopno, VTQNGDP + "%", loginputqty, String.valueOf(f), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                                    if (!holdBoolean) {
                                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                                        return;
                                                    }
                                                    vtqallholdfalg = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            DataTable SFR37 = cwa.CallRDT("checkout_submit_134", vtqopno, vtqopdefectno);
                            if (SFR37.Rowscount() > 0) {
                                if (SFCStaticdata.staticmember.vtqnoholdfalg.equals("1")) {
                                } else {
                                    DataTable SFR371 = cwa.CallRDT("checkout_submit_135", lotno);
                                    String sfr37ngqty = "0";
                                    if (SFR371.Rowscount() > 0) {
                                        sfr37ngqty = SFR371.Rows(0).get_CellValue("ngqty");
                                        int maxyeild37 = 1;
                                        String[] numparmer = {};
                                        String num = cwa.CallRS("checkout_submit_122");
                                        num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                        String numbe = cwa.CallRS("checkout_submit_123");
                                        if (numbe.equals("") || numbe == null) {
                                            numbe = "0001";
                                        } else {
                                            if (!num.equals(numbe.substring(0, 8))) {
                                                numbe = "0001";
                                            } else {
                                                int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                                numbe = ("0000" + String.valueOf(number1));
                                                numbe = numbe.substring(numbe.length() - 4, 4);
                                            }
                                        }
                                        String dev = devicenotextBoxText.trim();
                                        String NUM = cwa.CallRS("checkout_submit_124", dev);
                                        String numberdt = NUM + num + numbe;
                                        vtqstopdtSFR37.AddRow(SFCStaticdata.staticmember.deviceno, vtqlot, opnametextBoxText, loginputqty, "sfr37 center fail", vtqngqty, sfr37ngqty + "顆", maxyeild37 + "顆", line, "請立即停機改善", numberdt);
                                        messageStringvtq = messageStringvtq + cwa.CallRS("getopname", vtqopno) + "站位投入為" + loginputqty + "," + "sfr37 center fail" + "的不良數量為" + vtqngqty + ",超過停線不良率(Stop line:" + maxyeild37 + "顆)\r\n";
                                        String vtqholdsql37 = "";
                                        String qcholdopno = getqcholdopno(cwa, "SFR37", nextopnotextBoxText, opnotextBoxText);
                                        if (qcholdopno.equals("") || qcholdopno == null) {
                                        } else {
                                            vtqopno = qcholdopno;
                                        }
                                        _a.ShowMessage(messageStringvtq);
                                        Boolean holdBoolean = cwa.CallRB("checkout_submit_136", vtqlot, vtqopno, sfr37ngqty, loginputqty, vtqngqty, SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                        if (!holdBoolean) {
                                            _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                            return;
                                        }
                                        vtqalertmailsendBoolean = true;
                                        vtqallholdfalg = true;
                                    }
                                }
                            }
                            if (vtqalertmailsendBoolean) {
                                if (SFCStaticdata.staticmember.lotcheckeolflag) {
                                    if (vtqalerdt.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqalerdt), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + " " + name + " 良率超标ON-HOLD 系統郵件,請勿回覆.", "0");
                                    }
                                    if (vtqstopdtE.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtE), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "电性 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtLCB.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtLCB), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "LCB 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqpod.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqpod), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "POD 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqpog.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqpog), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "POG 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtDP.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtDP), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "DP 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtSFR37.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtSFR37), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "SFR37 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqpodg.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqpodg), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "LCB 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqpogg.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqpogg), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "DP 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqSV.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqSV), "VTQE", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "sealvoid 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                } else if (SFCStaticdata.staticmember.lotfolbingpicheck) {
                                    if (vtqalerdt.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqalerdt), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + " " + name + " 良率超标ON-HOLD 系統郵件,請勿回覆.", "0");
                                    }
                                    if (vtqstopdtE.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtE), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "电性 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtDP.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtDP), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "DP 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtSFR37.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtSFR37), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "SFR37 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqpod.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqpod), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "POD 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqpog.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqpog), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "POG 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqSV.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqSV), "VTQF", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "sealvoid 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");

                                    }
                                } else {
                                    if (vtqalerdt.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqalerdt), "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + " " + name + " 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtE.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtE), "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "电性 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtLCB.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtLCB), "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "LCB 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtDP.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtDP), "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "DP 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                    if (vtqstopdtSFR37.Rowscount() > 0) {
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(vtqstopdtSFR37), "VTQS", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "SFR37 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (SFCStaticdata.staticmember.deviceno.equals("RI-L PRQ") || SFCStaticdata.staticmember.deviceno.equals("RI-L") || SFCStaticdata.staticmember.deviceno.equals("RI-G")) {
                if (!(lotno.toUpperCase().contains("C0"))) {
                    if (lotno.substring(0, 1).trim().equals("A")) {
                        String aholdopnoRI = cwa.CallRS("getallholdopnodataG", opno);//可能為空
                        String aholdopnoRIE = cwa.CallRS("getallholdopnodataRI", opno);
                        Boolean rialertmailsendBoolean = false;
                        Boolean riallholdfalg = false;
                        String messageStringri = "單項不良率超標情況:\r\n";
                        DataTable ristopdtE = new DataTable();
                        ristopdtE.AddColumn("Deviceno");
                        ristopdtE.AddColumn("Lotno");
                        ristopdtE.AddColumn("Station Name");
                        ristopdtE.AddColumn("Qty In");
                        ristopdtE.AddColumn("ErrorNoName");
                        ristopdtE.AddColumn("NGQty");
                        ristopdtE.AddColumn("NGYield");
                        ristopdtE.AddColumn("Stop Line NGYield");
                        ristopdtE.AddColumn("Line");
                        ristopdtE.AddColumn("ReMark");
                        ristopdtE.AddColumn("Code");
                        DataTable ristopdtMTF = new DataTable();
                        ristopdtMTF.AddColumn("Deviceno");
                        ristopdtMTF.AddColumn("Lotno");
                        ristopdtMTF.AddColumn("Station Name");
                        ristopdtMTF.AddColumn("Qty In");
                        ristopdtMTF.AddColumn("ErrorNoName");
                        ristopdtMTF.AddColumn("NGQty");
                        ristopdtMTF.AddColumn("NGYield");
                        ristopdtMTF.AddColumn("Stop Line NGYield");
                        ristopdtMTF.AddColumn("Line");
                        ristopdtMTF.AddColumn("ReMark");
                        ristopdtMTF.AddColumn("Code");
                        DataTable ristopdtSFR60 = new DataTable();
                        ristopdtSFR60.AddColumn("Deviceno");
                        ristopdtSFR60.AddColumn("Lotno");
                        ristopdtSFR60.AddColumn("Station Name");
                        ristopdtSFR60.AddColumn("Qty In");
                        ristopdtSFR60.AddColumn("ErrorNoName");
                        ristopdtSFR60.AddColumn("NGQty");
                        ristopdtSFR60.AddColumn("NGYield");
                        ristopdtSFR60.AddColumn("Stop Line NGYield");
                        ristopdtSFR60.AddColumn("Line");
                        ristopdtSFR60.AddColumn("ReMark");
                        ristopdtSFR60.AddColumn("Code");
                        DataTable ristopdtSFR30 = new DataTable();
                        ristopdtSFR30.AddColumn("Deviceno");
                        ristopdtSFR30.AddColumn("Lotno");
                        ristopdtSFR30.AddColumn("Station Name");
                        ristopdtSFR30.AddColumn("Qty In");
                        ristopdtSFR30.AddColumn("ErrorNoName");
                        ristopdtSFR30.AddColumn("NGQty");
                        ristopdtSFR30.AddColumn("NGYield");
                        ristopdtSFR30.AddColumn("Stop Line NGYield");
                        ristopdtSFR30.AddColumn("Line");
                        ristopdtSFR30.AddColumn("ReMark");
                        ristopdtSFR30.AddColumn("Code");
                        DataTable ristopdtD50 = new DataTable();
                        ristopdtD50.AddColumn("Deviceno");
                        ristopdtD50.AddColumn("Lotno");
                        ristopdtD50.AddColumn("Station Name");
                        ristopdtD50.AddColumn("Qty In");
                        ristopdtD50.AddColumn("ErrorNoName");
                        ristopdtD50.AddColumn("NGQty");
                        ristopdtD50.AddColumn("NGYield");
                        ristopdtD50.AddColumn("Stop Line NGYield");
                        ristopdtD50.AddColumn("Line");
                        ristopdtD50.AddColumn("ReMark");
                        ristopdtD50.AddColumn("Code");
                        DataTable ristopdtALL = new DataTable();
                        ristopdtALL.AddColumn("Deviceno");
                        ristopdtALL.AddColumn("Lotno");
                        ristopdtALL.AddColumn("Station Name");
                        ristopdtALL.AddColumn("Qty In");
                        ristopdtALL.AddColumn("ErrorNoName");
                        ristopdtALL.AddColumn("NGQty");
                        ristopdtALL.AddColumn("NGYield");
                        ristopdtALL.AddColumn("Stop Line NGYield");
                        ristopdtALL.AddColumn("Line");
                        ristopdtALL.AddColumn("ReMark");
                        ristopdtALL.AddColumn("Code");
                        String setupsum = cwa.CallRS("getsetupsumdata", lotno);
                        if (setupsum.equals("") || setupqty == null) {
                            setupsum = "0";
                        }
                        String RILOTNO = lotnotextBoxText;
                        String RILOT = "";
                        if (RILOTNO.startsWith("AK") || RILOTNO.startsWith("PK")) {
                            RILOT = RILOTNO.substring(1, 12);
                        } else {
                            RILOT = RILOTNO;
                        }
                        DataTable riinqtysql1 = cwa.CallRDT("checkout_submit_137", RILOTNO);
                        String riinqty = "";
                        if (riinqtysql1.Rowscount() > 0) {
                            riinqty = riinqtysql1.Rows(0).get_CellValue("INPUTQTY");
                        }
                        if (riinqty.equals("") || riinqty == null) {
                            riinqty = "0";
                        }
                        DataTable ringqtyE = cwa.CallRDT("checkout_submit_138", RILOT, opno);
                        DataTable ringqtyMTF = cwa.CallRDT("checkout_submit_139", RILOT);
                        DataTable ringqtySFR60 = cwa.CallRDT("checkout_submit_140", RILOT);
                        DataTable ringqtySFR30 = cwa.CallRDT("checkout_submit_141", RILOT);
                        DataTable ringqtyD50 = cwa.CallRDT("checkout_submit_142", RILOT);
                        String riline = cwa.CallRS("checkout_submit_110", lotno);
                        if (riline.equals("") || riline == null) {
                            switch (lotno.substring(0, 1)) {
                                case "A":
                                    riline = "EOL";
                                    break;
                                case "P":
                                    riline = "FOL";
                                    break;
                                case "S":
                                    riline = "SMT";
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (ringqtyE.Rowscount() > 0) {
                            if (aholdopnoRIE.equals("") && aholdopnoRIE != null) {
                                int ristr1 = 0;
                                for (int a = 0; a < ringqtyE.Rowscount(); a++) {
                                    ristr1 = (Integer.parseInt(ringqtyE.Rows(a).get_CellValue("ngqty").trim())) + ristr1;
                                }
                                String holderrorstrlcbe = ringqtyE.Rows(0).get_CellValue("opdefectno").trim();
                                Double rimaxyeildE = 0.00;
                                rimaxyeildE = Double.parseDouble(cwa.CallRS("getvtqyielddata", opno, holderrorstrlcbe));//這裡改成ri的
                                String RINGE = "";
                                RINGE = BaseFuncation.doubletostring(((ristr1 / (Double.parseDouble(riinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                if ((Double.parseDouble(RINGE) > rimaxyeildE)) {
                                    String num = cwa.CallRS("checkout_submit_143");
                                    num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                    String numbe = cwa.CallRS("checkout_submit_144");
                                    if (numbe.equals("") || numbe == null) {
                                        numbe = "0001";
                                    } else {
                                        if (!num.equals(numbe.substring(0, 8))) {
                                            numbe = "0001";
                                        } else {
                                            int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                            numbe = ("0000" + String.valueOf(number1));
                                            numbe = numbe.substring(numbe.length() - 4, 4);
                                        }
                                    }
                                    String dev = devicenotextBoxText.trim();
                                    String NUM = cwa.CallRS("checkout_submit_124", dev);
                                    String numberdt = NUM + num + numbe;
                                    ristopdtE.AddRow(SFCStaticdata.staticmember.deviceno, RILOT, opnametextBoxText, riinqty, "電性不良", String.valueOf(ristr1), RINGE + "%", rimaxyeildE + "%", riline, "請立即停機改善", numberdt);
                                    messageStringri = messageStringri + cwa.CallRS("getopname", opno) + "站位投入為" + riinqty + ",E-FAIL的不良數量為" + ristr1 + ",不良率為" + RINGE + ",超過停線不良率(Stop line:" + rimaxyeildE + ")\r\n";
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                    _a.ShowMessage(messageStringri);
                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_145", lotno, opno, RINGE, riinqty, String.valueOf(ristr1), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                    if (!holdBoolean) {
                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                        return;
                                    }
                                    riallholdfalg = true;
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                }
                            }
                        }
                        if (ringqtyMTF.Rowscount() > 0) {
                            if (aholdopnoRI.equals("") && aholdopnoRI != null) {
                                int ristr2 = 0;
                                for (int a = 0; a < ringqtyMTF.Rowscount(); a++) {
                                    ristr2 = (Integer.parseInt(ringqtyMTF.Rows(a).get_CellValue("ngqty").trim())) + ristr2;
                                }
                                String holderrorstrlcbMTF = ringqtyMTF.Rows(0).get_CellValue("opdefectno").trim();
                                String lotnoMTF = ringqtyMTF.Rows(0).get_CellValue("opno").trim();
                                Double rimaxyeildMTF = 0.00;
                                rimaxyeildMTF = Double.parseDouble(cwa.CallRS("getvtqyielddata", lotnoMTF, holderrorstrlcbMTF));//設定良率
                                String RINGMTF = "";
                                RINGMTF = BaseFuncation.doubletostring(((ristr2 / (Double.parseDouble(riinqty) - Double.parseDouble(setupsum))) * 100), "0.00");//當前良率
                                if ((Double.parseDouble(RINGMTF) > rimaxyeildMTF)) {
                                    String num = cwa.CallRS("checkout_submit_143");
                                    num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                    String numbe = cwa.CallRS("checkout_submit_144");
                                    if (numbe.equals("") || numbe == null) {
                                        numbe = "0001";
                                    } else {
                                        if (!num.equals(numbe.substring(0, 8))) {
                                            numbe = "0001";
                                        } else {
                                            int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                            numbe = ("0000" + String.valueOf(number1));
                                            numbe = numbe.substring(numbe.length() - 4, 4);
                                        }
                                    }
                                    String dev = devicenotextBoxText.trim();
                                    String NUM = cwa.CallRS("checkout_submit_124", dev);
                                    String numberdt = NUM + num + numbe;
                                    ristopdtMTF.AddRow(SFCStaticdata.staticmember.deviceno, RILOT, lotnoMTF, riinqty, "MTF不良", String.valueOf(ristr2), RINGMTF + "%", rimaxyeildMTF + "%", riline, "請立即停機改善", numberdt);//預警設定
                                    messageStringri = messageStringri + cwa.CallRS("getopname", opno) + "站位投入為" + riinqty + ",MTF的不良數量為" + ristr2 + ",不良率為" + RINGMTF + ",超過停線不良率(Stop line:" + rimaxyeildMTF + ")\r\n";//過站提示
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                    _a.ShowMessage(messageStringri);
                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_146", lotno, opno, RINGMTF + "%", riinqty, String.valueOf(ristr2), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                    if (!holdBoolean) {
                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                        return;
                                    }
                                    riallholdfalg = true;
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                }
                            }
                        }
                        if (ringqtySFR60.Rowscount() > 0) {
                            if (aholdopnoRI.equals("") && aholdopnoRI != null) {
                                int ristr3 = 0;
                                for (int a = 0; a < ringqtySFR60.Rowscount(); a++) {
                                    ristr3 = (Integer.parseInt(ringqtySFR60.Rows(a).get_CellValue("ngqty").trim())) + ristr3;

                                }
                                String holderrorstrlcbSFR60 = ringqtySFR60.Rows(0).get_CellValue("opdefectno").trim();
                                String lotnoSFR60 = ringqtySFR60.Rows(0).get_CellValue("opno").trim();
                                Double rimaxyeildSFR60 = 0.00;
                                rimaxyeildSFR60 = Double.parseDouble(cwa.CallRS("getvtqyielddata", lotnoSFR60, holderrorstrlcbSFR60));//設定良率
                                String RINGSFR60 = "";
                                RINGSFR60 = BaseFuncation.doubletostring(((ristr3 / (Double.parseDouble(riinqty) - Double.parseDouble(setupsum))) * 100), "0.00");//當前良率
                                if ((Double.parseDouble(RINGSFR60) > rimaxyeildSFR60)) {
                                    String num = cwa.CallRS("checkout_submit_143");
                                    num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                    String numbe = cwa.CallRS("checkout_submit_144");
                                    if (numbe.equals("") || numbe == null) {
                                        numbe = "0001";
                                    } else {
                                        if (!num.equals(numbe.substring(0, 8))) {
                                            numbe = "0001";
                                        } else {
                                            int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                            numbe = ("0000" + String.valueOf(number1));
                                            numbe = numbe.substring(numbe.length() - 4, 4);
                                        }
                                    }
                                    String dev = devicenotextBoxText.trim();
                                    String NUM = cwa.CallRS("checkout_submit_124", dev);
                                    String numberdt = NUM + num + numbe;
                                    ristopdtSFR60.AddRow(SFCStaticdata.staticmember.deviceno, RILOT, lotnoSFR60, riinqty, "SFR60不良", String.valueOf(ristr3), RINGSFR60 + "%", rimaxyeildSFR60 + "%", riline, "請立即停機改善", numberdt);//預警設定
                                    messageStringri = messageStringri + cwa.CallRS("getopname", opno) + "站位投入為" + riinqty + ",SFR60的不良數量為" + ristr3 + ",不良率為" + RINGSFR60 + ",超過停線不良率(Stop line:" + rimaxyeildSFR60 + ")\r\n";//過站提示
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                    _a.ShowMessage(messageStringri);
                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_147", lotno, opno, RINGSFR60, riinqty, String.valueOf(ristr3), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                    if (!holdBoolean) {
                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                        return;
                                    }
                                    riallholdfalg = true;
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                }
                            }
                        }
                        if (ringqtySFR30.Rowscount() > 0) {
                            if (aholdopnoRI.equals("") && aholdopnoRI != null) {
                                int ristr4 = 0;
                                for (int a = 0; a < ringqtySFR30.Rowscount(); a++) {
                                    ristr4 = (Integer.parseInt(ringqtySFR30.Rows(a).get_CellValue("ngqty").trim())) + ristr4;
                                }
                                String holderrorstrlcbSFR30 = ringqtySFR30.Rows(0).get_CellValue("opdefectno").trim();
                                String lotnoSFR30 = ringqtySFR30.Rows(0).get_CellValue("opno").trim();
                                Double rimaxyeildSFR30 = 0.00;
                                rimaxyeildSFR30 = Double.parseDouble(cwa.CallRS("getvtqyielddata", lotnoSFR30, holderrorstrlcbSFR30));//設定良率 //讀取良率為空的時候會返回0.00
                                String RINGSFR30 = "";
                                RINGSFR30 = BaseFuncation.doubletostring(((ristr4 / (Double.parseDouble(riinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                                if ((Double.parseDouble(RINGSFR30) > rimaxyeildSFR30)) {
                                    String num = cwa.CallRS("checkout_submit_143");
                                    num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                    String numbe = cwa.CallRS("checkout_submit_144");
                                    if (numbe.equals("") || numbe == null) {
                                        numbe = "0001";
                                    } else {
                                        if (!num.equals(numbe.substring(0, 8))) {
                                            numbe = "0001";
                                        } else {
                                            int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                            numbe = ("0000" + String.valueOf(number1));
                                            numbe = numbe.substring(numbe.length() - 4, 4);
                                        }
                                    }
                                    String dev = devicenotextBoxText.trim();
                                    String NUM = cwa.CallRS("checkout_submit_124", dev);
                                    String numberdt = NUM + num + numbe;
                                    ristopdtSFR30.AddRow(SFCStaticdata.staticmember.deviceno, RILOT, lotnoSFR30, riinqty, "SFR30不良", String.valueOf(ristr4), RINGSFR30 + "%", rimaxyeildSFR30 + "%", riline, "請立即停機改善", numberdt);//預警設定
                                    messageStringri = messageStringri + cwa.CallRS("getopname", opno) + "站位投入為" + riinqty + ",SFR60的不良數量為" + ristr4 + ",不良率為" + RINGSFR30 + ",超過停線不良率(Stop line:" + rimaxyeildSFR30 + ")\r\n";//過站提示
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                    _a.ShowMessage(messageStringri);
                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_148", lotno, opno, RINGSFR30 + "%", riinqty, String.valueOf(ristr4), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                    if (!holdBoolean) {
                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                        return;
                                    }
                                    riallholdfalg = true;
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                }
                            }
                        }
                        if (ringqtyD50.Rowscount() > 0) {
                            if (!aholdopnoRI.equals("") && aholdopnoRI != null) {
                                int ristr5 = 0;
                                for (int a = 0; a < ringqtyD50.Rowscount(); a++) {
                                    ristr5 = (Integer.parseInt(ringqtyD50.Rows(a).get_CellValue("ngqty").trim())) + ristr5;
                                }
                                String holderrorstrlcbD50 = ringqtyD50.Rows(0).get_CellValue("opdefectno").trim();
                                String lotnoD50 = ringqtyD50.Rows(0).get_CellValue("opno").trim();
                                Double rimaxyeildD50 = 0.00;
                                rimaxyeildD50 = Double.parseDouble(cwa.CallRS("getvtqyielddata", lotnoD50, holderrorstrlcbD50));//設定良率
                                String RINGD50 = "";
                                RINGD50 = BaseFuncation.doubletostring(((ristr5 / (Double.parseDouble(riinqty) - Double.parseDouble(setupsum))) * 100), "0.00");//當前良率
                                if ((Double.parseDouble(RINGD50) > rimaxyeildD50)) {
                                    String num = cwa.CallRS("checkout_submit_143");
                                    num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                    String numbe = cwa.CallRS("checkout_submit_144");
                                    if (numbe.equals("") || numbe == null) {
                                        numbe = "0001";
                                    } else {
                                        if (!num.equals(numbe.substring(0, 8))) {
                                            numbe = "0001";
                                        } else {
                                            int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                            numbe = ("0000" + String.valueOf(number1));
                                            numbe = numbe.substring(numbe.length() - 4, 4);
                                        }
                                    }
                                    String dev = devicenotextBoxText.trim();
                                    String NUM = cwa.CallRS("checkout_submit_124");
                                    String numberdt = NUM + num + numbe;
                                    ristopdtD50.AddRow(SFCStaticdata.staticmember.deviceno, RILOT, lotnoD50, riinqty, "Blemish D50"
                                            + "D50不良", String.valueOf(ristr5), RINGD50 + "%", rimaxyeildD50 + "%", riline, "請立即停機改善", numberdt); //預警設定
                                    messageStringri = messageStringri + cwa.CallRS("getopname", opno) + "站位投入為" + riinqty + ",不良數量為" + String.valueOf(ristr5) + ",Blemish D50"
                                            + "不良率為" + RINGD50 + ",超過停線不良率(Stop line:" + rimaxyeildD50 + ")\r\n";//過站提示
                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                    _a.ShowMessage(messageStringri);

                                    // 插入hold數據
                                    Boolean holdBoolean = cwa.CallRB("checkout_submit_149", lotno, opno, RINGD50, riinqty, String.valueOf(ristr5), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                    if (!holdBoolean) {
                                        _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                        return;
                                    }
                                    riallholdfalg = true;

                                    if (!rialertmailsendBoolean) {
                                        rialertmailsendBoolean = true;
                                    }
                                }
                            }
                        }
                        if (!aholdopnoRI.equals("") && aholdopnoRI != null) {
                            DataTable RIdefqty = cwa.CallRDT("checkout_submit_150", lotno);
                            int RIstrz = 0;
                            if (RIdefqty != null && RIdefqty.Rowscount() > 0) {
                                RIstrz = Integer.parseInt(RIdefqty.Rows(0).get_CellValue("ngqty").trim());
                            }
                            int ngsingleri = 15;
                            String ngyieldri = "";
                            ngyieldri = BaseFuncation.doubletostring((((RIstrz) / (Double.parseDouble(riinqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                            if ((Double.parseDouble(ngyieldri) > (ngsingleri))) {
                                String num = cwa.CallRS("checkout_submit_143");
                                num = BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(num, "yyyyMMdd").getTime(), "yyyyMMdd");
                                String numbe = cwa.CallRS("checkout_submit_144");
                                if (numbe.equals("") || numbe == null) {
                                    numbe = "0001";
                                } else {
                                    if (!num.equals(numbe.substring(0, 8))) {
                                        numbe = "0001";
                                    } else {
                                        int number1 = Integer.parseInt(numbe.substring(numbe.length() - 4, 4)) + 1;
                                        numbe = ("0000" + String.valueOf(number1));
                                        numbe = numbe.substring(numbe.length() - 4, 4);
                                    }
                                }
                                String dev = devicenotextBoxText.trim();
                                String NUM = cwa.CallRS("checkout_submit_124", dev);
                                String numberdt = NUM + num + numbe;
                                ristopdtALL.AddRow(SFCStaticdata.staticmember.deviceno, RILOT, opnametextBoxText, riinqty, "整體不良", String.valueOf(RIstrz), ngyieldri + "%", "15%", riline, "請立即停機改善", numberdt);//預警設定
                                messageStringri = messageStringri + cwa.CallRS("getopname", opno) + "站位投入為" + riinqty + ",整體不良數量為" + RIstrz + ",不良率為" + ngyieldri + ",超過停線不良率(Stop line15%)\r\n";  //過站提示
                                if (!rialertmailsendBoolean) {
                                    rialertmailsendBoolean = true;
                                }
                                _a.ShowMessage(messageStringri);
                                Boolean holdBoolean = cwa.CallRB("checkout_submit_151", lotno, opno, ngyieldri + "%", riinqty, String.valueOf(RIstrz), SFCStaticdata.staticmember.userid, ip, SFCStaticdata.staticmember.deviceno, numberdt);
                                if (!holdBoolean) {
                                    _a.ShowMessage("執行Hold不良率超標批號動作異常，請聯繫MIS");
                                    return;
                                }
                                riallholdfalg = true;
                                if (!rialertmailsendBoolean) {
                                    rialertmailsendBoolean = true;
                                }
                            }
                        }
                        if (rialertmailsendBoolean) {
                            if (ristopdtE.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(ristopdtE), "RIST", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "E-fail 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                            }
                            if (ristopdtMTF.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(ristopdtMTF), "RIST", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "MTF 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                            }
                            if (ristopdtSFR60.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(ristopdtSFR60), "RIST", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "SFR60 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                            }
                            if (ristopdtSFR30.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(ristopdtSFR30), "RIST", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "SFR30 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                            }
                            if (ristopdtD50.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(ristopdtD50), "RIST", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "D50 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                            }
                            if (ristopdtALL.Rowscount() > 0) {
                                cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(ristopdtALL), "RIST", "YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + opnametextBoxText.trim() + "整體 良率超标ON-HOLD 系統郵件,請勿回覆.", "1");
                            }
                        }
                        if (rialertmailsendBoolean && riallholdfalg) {
                            String LOTNONEW = lotnotextBoxText;
                            Boolean lotstatusup = cwa.CallRB("checkout_submit_152", LOTNONEW);
                            if (!lotstatusup) {
                                _a.ShowMessage("更新批號狀態更改失敗");
                                return;
                            }
                        }
                    }
                }
            }
            if (SFCStaticdata.staticmember.deviceno.equals("")) {
                String overopno = cwa.CallRS("getallholdopnodata", opno);
                if (!overopno.equals("") && overopno != null) {
                    Boolean rialertmailsendBoolean = false;
                    String setupsum = cwa.CallRS("getsetupsumdata", lotno);
                    if (setupsum.equals("") || setupsum == null) {
                        setupsum = "0";
                    }
                    String PLOT = "";
                    if (lotnotextBoxText.length() == 13) {
                        String NALOTNO = lotnotextBoxText.substring(1, 12);
                        PLOT = "P" + NALOTNO;
                    } else {
                        PLOT = lotnotextBoxText;
                    }
                    DataTable inqtydt = cwa.CallRDT("checkout_submit_153", PLOT);
                    String inqty = "";
                    if (inqtydt.Rowscount() > 0) {
                        inqty = inqtydt.Rows(0).get_CellValue("INPUTQTY");
                    }
                    if (inqty.equals("") || inqty == null) {
                        inqty = "0";
                    }
                    String riline = cwa.CallRS("checkout_submit_110", lotno);
                    if (riline.equals("") || riline == null) {
                        switch (lotno.substring(0, 1)) {
                            case "A":
                                riline = "EOL";
                                break;
                            case "P":
                                riline = "FOL";
                                break;
                            case "S":
                                riline = "SMT";
                                break;
                            default:
                                break;
                        }
                    }
                    String NA1SQL = "";
                    String NA2SQL = "";
                    String NA3SQL = "";
                    String NA4SQL = "";
                    String NA5SQL = "";
                    DataTable ngqtyDP = new DataTable();
                    DataTable ngqtyLCB = new DataTable();
                    DataTable ngqtyEFAIL = new DataTable();
                    DataTable ngqtySFR = new DataTable();
                    DataTable ngqtyALL = new DataTable();
                    ngqtyDP = cwa.CallRDT("checkout_submit_154", lotno);
                    ngqtyLCB = cwa.CallRDT("checkout_submit_155", lotno);
                    ngqtyEFAIL = cwa.CallRDT("checkout_submit_156", lotno);
                    ngqtySFR = cwa.CallRDT("checkout_submit_157", lotno);
                    ngqtyALL = cwa.CallRDT("checkout_submit_158", lotno);
                    DataTable overdt = new DataTable();
                    overdt.AddColumn("Deviceno");
                    overdt.AddColumn("Lotno");
                    overdt.AddColumn("Qty In");
                    overdt.AddColumn("ErrorNoName");
                    overdt.AddColumn("NGQty");
                    overdt.AddColumn("Line");
                    overdt.AddColumn("Over NGYield");
                    overdt.AddColumn("NGYield");
                    if (ngqtyDP.Rowscount() > 0) {
                        int overqty = 0;
                        for (int a = 0; a < ngqtyDP.Rowscount(); a++) {
                            overqty = (Integer.parseInt(ngqtyDP.Rows(a).get_CellValue("ngqty").trim())) + overqty;
                        }
                        String overDP = ngqtyDP.Rows(0).get_CellValue("opdefectno").trim();
                        Double overDPyeild = 0.00;
                        overDPyeild = Double.parseDouble(cwa.CallRS("getvtqyielddata", null, overDP));
                        String DPyeild = "";
                        DPyeild = BaseFuncation.doubletostring(((overqty / (Double.parseDouble(inqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                        if (Double.parseDouble(DPyeild) > overDPyeild) {
                            overdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, inqty, "DP不良", String.valueOf(overqty), riline, overDPyeild + "%", DPyeild + "%");//預警設定
                            if (!rialertmailsendBoolean) {
                                rialertmailsendBoolean = true;
                            }
                        }
                    }
                    if (ngqtyLCB.Rowscount() > 0) {
                        int overqty = 0;
                        for (int a = 0; a < ngqtyLCB.Rowscount(); a++) {
                            overqty = (Integer.parseInt(ngqtyLCB.Rows(a).get_CellValue("ngqty").trim())) + overqty;
                        }
                        String overLCB = ngqtyLCB.Rows(0).get_CellValue("opdefectno").trim();
                        Double overLCByeild = 0.00;
                        overLCByeild = Double.parseDouble(cwa.CallRS("getvtqyielddata", null, overLCB));//設定良率
                        String LCByeild = "";
                        LCByeild = BaseFuncation.doubletostring(((overqty / (Double.parseDouble(inqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                        if (Double.parseDouble(LCByeild) > overLCByeild) {
                            overdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, inqty, "LCB不良", String.valueOf(overqty), riline, overLCByeild + "%", LCByeild + "%");//預警設定
                            if (!rialertmailsendBoolean) {
                                rialertmailsendBoolean = true;
                            }
                        }
                    }
                    if (ngqtyEFAIL.Rowscount() > 0) {
                        int overqty = 0;
                        for (int a = 0; a < ngqtyEFAIL.Rowscount(); a++) {
                            overqty = (Integer.parseInt(ngqtyEFAIL.Rows(a).get_CellValue("ngqty").trim())) + overqty;
                        }
                        String overEFAIL = ngqtyEFAIL.Rows(0).get_CellValue("opdefectno").trim();
                        Double overEFAILyeild = 0.00;
                        overEFAILyeild = Double.parseDouble(cwa.CallRS("getvtqyielddata", null, overEFAIL));//設定良率
                        String EFAILyeild = "";
                        EFAILyeild = BaseFuncation.doubletostring(((overqty / (Double.parseDouble(inqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                        if (Double.parseDouble(EFAILyeild) > overEFAILyeild) {
                            overdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, inqty, "EFAIL不良", String.valueOf(overqty), riline, overEFAILyeild + "%", EFAILyeild + "%");
                            if (!rialertmailsendBoolean) {
                                rialertmailsendBoolean = true;
                            }
                        }
                    }
                    if (ngqtySFR.Rowscount() > 0) {
                        int overqty = 0;
                        for (int a = 0; a < ngqtySFR.Rowscount(); a++) {
                            overqty = (Integer.parseInt(ngqtySFR.Rows(a).get_CellValue("ngqty").trim())) + overqty;
                        }
                        String overSFR = ngqtySFR.Rows(0).get_CellValue("opdefectno").trim();
                        Double overSFRyeild = 0.00;
                        overSFRyeild = Double.parseDouble(cwa.CallRS("getvtqyielddata", null, overSFR));//設定良率
                        String SFRyeild = "";
                        SFRyeild = BaseFuncation.doubletostring(((overqty / (Double.parseDouble(inqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                        if (Double.parseDouble(SFRyeild) > overSFRyeild) {
                            overdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, inqty, "SFR不良", String.valueOf(overqty), riline, overSFRyeild + "%", SFRyeild + "%");//預警設定
                            if (!rialertmailsendBoolean) {
                                rialertmailsendBoolean = true;
                            }
                        }
                    }
                    if (ngqtyALL.Rowscount() > 0) {
                        int overqty = 0;
                        if (ngqtyALL != null && ngqtyALL.Rowscount() > 0) {
                            overqty = Integer.parseInt(ngqtyALL.Rows(0).get_CellValue("ngqty").trim());
                        }
                        Double overALLyeild = 15.00;//設定良率
                        String ALLyeild = "";
                        ALLyeild = BaseFuncation.doubletostring(((overqty / (Double.parseDouble(inqty) - Double.parseDouble(setupsum))) * 100), "0.00");
                        if (Double.parseDouble(ALLyeild) > overALLyeild) {
                            overdt.AddRow(SFCStaticdata.staticmember.deviceno, lotno, inqty, "整體不良", String.valueOf(overqty), riline, overALLyeild + "%", ALLyeild + "%");//預警設定
                            if (!rialertmailsendBoolean) {
                                rialertmailsendBoolean = true;
                            }
                        }
                    }
                    if (rialertmailsendBoolean) {
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(overdt), "NAOVER", " YCL/VCL Control system：" + SFCStaticdata.staticmember.deviceno + " " + lotnotextBoxText + "良率超标系統郵件,請勿回覆.", "1");
                    }
                }
            }
            if (vtqalertmailsendBoolean && vtqallholdfalg && !nextopno.equals("INV")) {
                String LOTNONEW = lotnotextBoxText;
                Boolean lotstatusup = cwa.CallRB("checkout_submit_159", LOTNONEW);
                if (!lotstatusup) {
                    _a.ShowMessage("更新批號狀態更改失敗");
                    return;
                }
            }
            Boolean flag_retest = cwa.CallRB("checkdefectretestdev", SFCStaticdata.staticmember.newdeviceno);
            if (flag_retest){       //if (SFCStaticdata.staticmember.deviceno.equals("VT-Q") || SFCStaticdata.staticmember.newdeviceno.equals("ATF001")) {
                String att19 = cwa.CallRS("getatt19data", opno);
                if (att19.equals("1") || att19.equals("2")) {
                    DataTable dt37 = cwa.CallRDT("getvtqspecialopnodefect", opno, lotno); //查詢是否有打返回複測的不良項目數據
                    if (dt37 != null) {
                        String retestqty = cwa.CallRS("getretesttimedata", lotno, opno);  //獲取Q退重工的次數
                        if (dt37.Rowscount() > 0) {
                            String qcreflag = "";
                            String guoflag = "";
                            String retestqty1 = String.valueOf(Integer.parseInt(retestqty) + 1).trim();
                            String rehosql = "begin delete from tblsnlotsenseridbak where lotno ='" + lotno.substring(1, (lotno.length() - 1)) + "';";
                            String relesql = "begin ";
                            DataTable qredt = cwa.CallRDT("getqcreturndata", lotno);
                            if (qredt != null) {
                                if (qredt.Rowscount() > 0) {
                                    qcreflag = "1";
                                }
                            }
                            for (int r = 0; r < dt37.Rowscount(); r++) {
                                String reatt1 = "reatt1";
                                String opdefectno = dt37.Rows(r).get_CellValue("att12").trim();
                                String deqty = dt37.Rows(r).get_CellValue("ngqty").trim();
                                String lastdeqty = cwa.CallRS("getlast37data", lotno, opno, opdefectno, retestqty);
                                String datenew = cwa.CallRS("getdbsystime");
                                String[] bb = datenew.split(":|-| ");
                                String cc = bb[3] + bb[4] + bb[5];
                                if (att19.equals("2")) {
                                    if (Integer.parseInt(retestqty) >= 2) {
                                        if (Integer.parseInt(deqty) - Integer.parseInt(lastdeqty) >= 2) {
                                            rehosql = rehosql + "insert into t_retestlotnodata values"
                                                    + " ('" + lotno + "','" + opno + "','" + deqty + "','" + opdefectno + "',sysdate,'" + retestqty1 + "','1','" + curqty + "',null,null,null);";

                                            relesql = relesql + "insert into t_retestlotnodata values"
                                                    + " ('" + lotno + "','" + opno + "','" + deqty + "','" + opdefectno + "',sysdate,'" + retestqty1 + "','2','" + curqty + "','" + reatt1 + "',null,null);";
                                            guoflag = "1";
                                        }
                                    } else {
                                        if (Integer.parseInt(deqty) >= 2) {
                                            rehosql = rehosql + "insert into t_retestlotnodata values"
                                                    + " ('" + lotno + "','" + opno + "','" + deqty + "','" + opdefectno + "',sysdate,'" + retestqty1 + "','1','" + curqty + "',null,null,null);";

                                            relesql = relesql + "insert into t_retestlotnodata values"
                                                    + " ('" + lotno + "','" + opno + "','" + deqty + "','" + opdefectno + "',sysdate,'" + retestqty1 + "','2','" + curqty + "','" + reatt1 + "',null,null);";//2代表特殊放過的
                                            guoflag = "1";
                                        }
                                    }
                                } else {
                                    String qcrenum = "0";
                                    if (qcreflag.equals("1")) {
                                        DataTable turndt = cwa.CallRDT("getqturnerrorno", opdefectno);
                                        if (turndt != null) {
                                            if (turndt.Rowscount() > 0) {
                                                String errorno = "";
                                                for (int y = 0; y < turndt.Rowscount(); y++) {
                                                    if (turndt.Rowscount() == 1) {
                                                        errorno = "'" + turndt.Rows(y).get_CellValue("item").trim() + "'";
                                                    } else {
                                                        if (y == turndt.Rowscount() - 1) {
                                                            errorno = errorno + "'" + turndt.Rows(y).get_CellValue("item").trim() + "'";
                                                        } else {
                                                            errorno = errorno + "'" + turndt.Rows(y).get_CellValue("item").trim() + "'" + ",";
                                                        }
                                                    }
                                                }
                                                qcrenum = cwa.CallRS("getqcerrordata", lotno, errorno);
                                                if (qcrenum.equals("") || qcrenum == null) {
                                                    qcrenum = "0";
                                                }
                                            }
                                        }
                                    }
                                    if (Integer.parseInt(deqty) - Integer.parseInt(qcrenum) >= 2) {
                                        String lotnocc = lotno + cc;
                                        String opnocc = opno + cc;
                                        String defectqtycc = opdefectno + cc;
                                        rehosql = rehosql + "update t_retestlotnodata set lotno=lotno||'" + cc + "',opno=opno||'" + cc + "',defectqty=defectqty||'" + cc + "',times =times ||'" + cc + "' where lotno ='" + lotno + "';"
                                                + "insert into t_retestlotnodata values"
                                                + " ('" + lotno + "','" + nextopno + "','" + deqty + "','" + opdefectno + "',sysdate,'1','1','" + curqty + "',null,null,null);";

                                        relesql = relesql + "update t_retestlotnodata set lotno=lotno||'" + cc + "',opno=opno||'" + cc + "',defectqty=defectqty||'" + cc + "',times =times ||'" + cc + "' where lotno ='" + lotno + "';"
                                                + "insert into t_retestlotnodata values"
                                                + " ('" + lotno + "','" + nextopno + "','" + deqty + "','" + opdefectno + "',sysdate,'1','2','" + curqty + "','" + reatt1 + "',null,null);";
                                        guoflag = "1";
                                    } else {
                                        if (r == dt37.Rowscount() - 1 && guoflag.equals("1")) {
                                            String renextopnoflowid = String.valueOf(Integer.parseInt(nextopnoflowid) + 1).trim();
                                            String renextopno = cwa.CallRS("getnextopno", nowprocessno, renextopnoflowid);
                                            String relotserial = lotno + "-" + BaseFuncation.padLeft(String.valueOf(Integer.parseInt(nextopnoflowid)), 3, '0');
                                            Boolean renextflag = cwa.CallRB("jumpnextopnodata", lotno, renextopno, renextopnoflowid, relotserial, userid, curqty, productno, checkoutpcname, nextopnoflowid, opno, nextopno);
                                        }
                                    }
                                }
                                if (r == dt37.Rowscount() - 1) {
                                    rehosql = rehosql + " end;";
                                    relesql = relesql + " end;";
                                    if (guoflag.equals("1")) {
                                        if (BaseFuncation.DialogResult.OK == _a.MessageBox("該批次" + dt37.Rows(r).get_CellValue("att12").trim() + " DP/LCB不良數量大於1,請工程確認是否真的Fail？", "系統提示")) {
                                            Boolean holdflag = cwa.CallRB("holdupdatedata", rehosql);
                                            if (!holdflag) {
                                                _a.ShowMessage("HOLD数据失败，请联系MIS部门");
                                                return;
                                            } else {
                                                _a.ShowMessage("該批次" + dt37.Rows(r).get_CellValue("att12").trim() + " DP/LCB不良數量大於1，已經被QC hold住，請重工測試");
                                                return;
                                            }

                                        } else {
                                            SFCStaticdata.staticmember.rethressfalg = "";
                                            //todo create new class open no fol return
//                                                Respecialform reform = new Respecialform(lotno, opno);
//                                                reform.ShowDialog();
                                            if (SFCStaticdata.staticmember.rethressfalg.equals("") || SFCStaticdata.staticmember.rethressfalg == null) {
                                                _a.ShowMessage("強行過站失敗，已經被QC hold住，請重工測試");
                                                Boolean holdflag = cwa.CallRB("holdupdatedata", rehosql);
                                                if (!holdflag) {
                                                    _a.ShowMessage("HOLD数据失败，请联系MIS部门");
                                                }
                                                return;
                                            } else {
                                                relesql = relesql.replace("reatt1", SFCStaticdata.staticmember.rethressfalg);
                                                if (att19.equals("1")) {
                                                    String renextopnoflowid = String.valueOf(Integer.parseInt(nextopnoflowid) + 1).trim();
                                                    String renextopno = cwa.CallRS("getnextopno", nowprocessno, renextopnoflowid);
                                                    String relotserial = lotno + "-" + BaseFuncation.padLeft(String.valueOf(Integer.parseInt(nextopnoflowid)), 3, '0');
                                                    Boolean renextflag = cwa.CallRB("jumpnextopnodata", lotno, renextopno, renextopnoflowid, relotserial, userid, curqty, productno, checkoutpcname, nextopnoflowid, opno, nextopno);
                                                }
                                                Boolean holdflag = cwa.CallRB("holdupdatedata", relesql);
                                                if (!holdflag) {
                                                    _a.ShowMessage("HOLD数据失败，请联系MIS部门");
                                                    return;
                                                }
                                                _a.ShowMessage("DP/LCB不良數量小於1，無需重測37");
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (att19.equals("1")) {
                                String renextopnoflowid = String.valueOf(Integer.parseInt(nextopnoflowid) + 1).trim();
                                String renextopno = cwa.CallRS("getnextopno", nowprocessno, renextopnoflowid);
                                String relotserial = lotno + "-" + BaseFuncation.padLeft(String.valueOf(Integer.parseInt(nextopnoflowid)), 3, '0');
                            }
                        }
                    }
                }
            }

            if (curqty.equals("0")) {
                String datetime = BaseFuncation.getnowdatetime("yyyyMMddhhmm");
                Boolean uplotstate = cwa.CallRB("checkout_submit_160", datetime, SFCStaticdata.staticmember.ip, lotno);
                if (uplotstate) {
                    _a.ShowMessage("该批号数量为0，已经强行结批！");
                    return;
                } else {
                    _a.ShowMessage("该批号数量为0，强行结批失败，请联系MIS手动作业！");
                    return;
                }
            }

            //如果最终状态为 INV 时，加入更新批号大状态。
            if (nextopnametextBoxText.toUpperCase().equals("INV")) {
                if (Integer.parseInt(defectqtytextBoxText) > 0 && OQCbackchcbChecked) {
                    return;
                }
                String lotno_new = "";
                if (lotno.length() == 12 || lotno.length() == 15) {
                    return;
                } else if (lotno.length() == 13 || lotno.length() == 16) {
                    lotno_new = lotno.substring(1, lotno.length());
                }
                lotstate = "4";
                String eollotno = "";
                String lotnostate = "";
                if (lotno.substring(0, 1).trim().toUpperCase().equals("P")) {
                    eollotno = "A" + lotno.substring(1, lotno.length() - 1);
                    lotnostate = "2";
                } else if (lotno.substring(0, 1).trim().toUpperCase().equals("A")) {
                    eollotno = "INV";
                    lotnostate = "4";
                } else {
                    return;
                }
                wono = cwa.CallRS("checkout_submit_161", lotno_new);
                if (wono.equals("") || wono == null) {
                    _a.ShowMessage("请检查批号是否为WIP批号<未更新t_lotstatuspro表工单>");
                }
                try {
                    Boolean uplotstate = cwa.CallRB("checkout_submit_162", eollotno, lotnostate, wono, lotno_new);
                    if (!uplotstate) {
                        _a.ShowMessage("更新成品批號狀態失敗，請聯繫MIS！");
                        return;
                    }
                } catch (Exception ex) {
                    _a.ShowMessage("更新成品批號狀態時發生錯誤，請聯繫MIS！");
                    return;
                }
            }
        }
    }

    public static void checkout_submit17(ActivityInteractive _a,CallWebapi cwa,String deviceno,String opnotextBox,String lotno,Boolean AllReworkFlag,
                                         String dietextBoxtext,String opnametextBoxtext) throws Exception
    {
        DataTable dt_folyj = cwa.CallRDT("checkout_submit_71",deviceno,opnotextBox);
        if (dt_folyj == null)
        {
            _a.ShowMessage("FOLIMAGE良率预警邮件发生错误");
        }
        String opno=opnotextBox;
        if (dt_folyj.Rowscount() > 0)
        {
            Boolean VTQFOLYJ = false;

            DataTable VTQFOLalerdt = new DataTable();

            VTQFOLalerdt.AddColumn("Deviceno");
            VTQFOLalerdt.AddColumn("Lotno");
            VTQFOLalerdt.AddColumn("Station Name");
            VTQFOLalerdt.AddColumn("Qty In");
            VTQFOLalerdt.AddColumn("ErrorNoName");
            VTQFOLalerdt.AddColumn("NGQty");
            VTQFOLalerdt.AddColumn("NGYield");
            VTQFOLalerdt.AddColumn("Alert Line NGYield");
            VTQFOLalerdt.AddColumn("Line");
            VTQFOLalerdt.AddColumn("ReMark");

            String sqlVTQFOLalert = "";
            if (AllReworkFlag)
            {
                sqlVTQFOLalert = " select a.lotno,a.opno,a.opnodefectno,b.opdefectname,ngqty from t_lotdefectdata a left join t_defectdata b on (a.opno=b.opno and a.opnodefectno=b.opdefectno) inner join tblsfcallsetting c on (a.opno=c.att2 and a.opnodefectno=c.att1) where a.lotno =:lotno and a.opno=:opno and c.type='0' and c.status='38'";
            }
            else
            {
                sqlVTQFOLalert = " select a.lotno,a.opno,a.opnodefectno,b.opdefectname,ngqty from t_lotdefectdata a left join t_defectdata b on (a.opno=b.opno and a.opnodefectno=b.opdefectno) inner join tblsfcallsetting c on (a.opno=c.att2 and a.opnodefectno=c.att1) where a.lotno =:lotno and a.opno=:opno and c.type='1' and c.status='38'";
            }
            DataTable VTQFOLalert=cwa.CallRDT("checkout_submit_73",lotno,opnotextBox);

            if (VTQFOLalert == null)
            {
                _a.ShowMessage("查詢該批號的不良記錄異常，請聯繫MIS, 提示");
                if (!cwa.CallRB("deldefectwipsn",lotno, opnotextBox, "查詢不良資料異常"))
                {
                    _a.ShowMessage("回溯不良資料異常，請聯繫MIS ,提示");
                }
                return;
            }
            if (VTQFOLalert.Rowscount() > 0)
            {
                for (int i = 0; i < VTQFOLalert.Rowscount(); i++)
                {
                    String VTQFOLinqty =dietextBoxtext;
                    String VTQFOLlot = VTQFOLalert.Rows(i).get_CellValue("lotno");
                    String VTQFOLopno = VTQFOLalert.Rows(i).get_CellValue("opno");
                    String VTQFOLopdefectno = VTQFOLalert.Rows(i).get_CellValue("opnodefectno");
                    String VTQFOLopdefectname = VTQFOLalert.Rows(i).get_CellValue("opdefectname");
                    String VTQFOLngqty = VTQFOLalert.Rows(i).get_CellValue("ngqty");
                    String VTQFOLngsinglesql = "select att3 minngyieldlimit,att4 maxngyieldlimit from tblsfcallsetting  where type='0' and status='38' and att2=:opnonow and att1=:opdefectnonow ";
                    if (AllReworkFlag)
                    {
                        VTQFOLngsinglesql = "select att3 minngyieldlimit,att4 maxngyieldlimit from tblsfcallsetting  where type='1' and status='38' and att2=:opnonow and att1=:opdefectnonow ";
                    }

                    DataTable VTQFOLngsingledt =cwa.CallRDT("checkout_submit_73", VTQFOLopno, VTQFOLopdefectno);
                    if (VTQFOLngsingledt == null)
                    {
                        _a.ShowMessage("查詢站位" + cwa.CallRS("getopname",VTQFOLopno) + "對應的不良項目" + VTQFOLopdefectname + "的不良率異常，請聯繫MIS  提示");
                        if (!cwa.CallRB("deldefectwipsn", lotno, opnotextBox, "查詢不良率上限異常"))
                        {
                            _a.ShowMessage("回溯不良資料異常，請聯繫MIS, 提示");
                        }
                        return;
                    }
                    else if (VTQFOLngsingledt.Rowscount() <= 0)
                    {
                        _a.ShowMessage("尚未設定站位" + cwa.CallRS("getopname", VTQFOLopno) + "對應的不良項目" + VTQFOLopdefectname + "的不良率異常，請聯繫MIS , 提示");
                        if (!cwa.CallRB("deldefectwipsn", lotno, opno, "不良率上限未設定")) {
                            _a.ShowMessage("回溯不良資料異常，請聯繫MIS , 提示");
                        }
                        return;
                    }
                    String minyield = VTQFOLngsingledt.Rows(0).get_CellValue("minngyieldlimit");
                    String maxyeild = VTQFOLngsingledt.Rows(0).get_CellValue("maxngyieldlimit");
                    Double yield_ng=Math.round(Double.parseDouble(VTQFOLngqty)* 100.00)/Double.parseDouble(VTQFOLinqty);
                    String ngyield = Double.toString(yield_ng);

                    String line =cwa.CallRS("checkout_submit_74",lotno);
                    if (line == null || "".equals(line))
                    {
                        switch (VTQFOLlot.substring(0, 1))
                        {
                            case "A":
                                line = "EOL";
                                break;
                            case "P":
                                line = "FOL";
                                break;
                            case "S":
                                line = "SMT";
                                break;
                            default:
                                break;
                        }
                    }

                    if ((yield_ng >Double.parseDouble(minyield)) && (yield_ng <= Double.parseDouble(maxyeild)))//aler line
                    {
                        VTQFOLalerdt.AddRow(deviceno, VTQFOLlot, opnametextBoxtext, VTQFOLinqty, VTQFOLopdefectname, VTQFOLngqty, ngyield + "%", minyield + "%", line, "各部門負責人進行改善");

                        if (VTQFOLYJ == false)
                        {
                            VTQFOLYJ = true;
                        }

                    }

                }
                if (VTQFOLYJ == true)
                {
                    //执行预警邮件发送
                    if (VTQFOLalerdt.Rowscount() > 0)
                    {
                        cwa.CallRB("execSendMailAll",BaseFuncation.SerializeObjectDataTable(VTQFOLalerdt), dt_folyj.Rows(0).get_CellValue("att2"), "LH SFC System Lotno预警通知郵件：" + deviceno + "  Alert Lines by LotNo of SFCS 人數較多,分批發送 ; 系統郵件,請勿回覆.", "0");
                    }
                }

            }
        }
    }

    public static String getqcholdopno(CallWebapi cwa,String type, String nextopno, String nowopno) throws Exception {
        String opno = "";
        DataTable dt = new DataTable();
        dt = cwa.CallRDT("check_getqcholdopno", type, nowopno);
        if (dt == null) {
            opno = "";
            return opno;
        }
        if (dt.Rowscount() > 0) {
            opno = dt.Rows(0).get_CellValue("att1").trim();
        } else {
            opno = "";
        }
        return opno;
    }

    public static void checkLENSholdflag(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String opno,String lotno,String ip) throws Exception
    {
        DataTable dt_set = cwa.CallRDT("checkout_submit_163",opno);

        if (dt_set.Rowscount() > 0)
        {
            DataTable dt_lens =  cwa.CallRDT("checkout_submit_164", lotno);

            if (dt_lens.Rowscount() > 0)
            {
                String lenslot = "";
                for (int i = 0; i < dt_lens.Rowscount(); i++)
                {
                    if (i == dt_lens.Rowscount() - 1)
                    {
                        lenslot += dt_lens.Rows(i).get_CellValue(0);
                    }
                    else
                    {
                        lenslot +=  dt_lens.Rows(i).get_CellValue(0) + ",";
                    }
                }

                DataTable dt_hold = cwa.CallRDT("checkout_submit_165", lenslot);
                if (dt_hold.Rowscount() > 0)
                {
                    //----生成單號-----------------
                    String numberdtG = "";
                    String numG = cwa.CallRS("checkout_submit_166");
                    numG =BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(numG, "yyyy/MM/dd HH:mm:ss").getTime(),"yyyyMMdd");//Convert.ToDateTime(numG).toString("yyyyMMdd");//查時間

                    String numbeG =cwa.CallRS("checkout_submit_167");//也是時間

                    if (numbeG == null || numbeG.equals(""))
                    {
                        numbeG = "0001";
                    }
                    else
                    {
                        if (numG.equals(numbeG.substring(0, 8)))
                        {
                            numbeG = "0001";
                        }
                        else
                        {
                            int number1G = Integer.parseInt(numbeG.substring(numbeG.length() - 4, numbeG.length())) + 1;
                            numbeG ="0000" + Integer.toString(number1G);
                            numbeG = numbeG.substring(numbeG.length() - 4, numbeG.length());
                        }
                    }
                    numberdtG =newdeviceno + numG + numbeG;
                    //=============================

                    Boolean holdflag =cwa.CallRB("checkout_submit_168",lotno, opno, ip, numberdtG);
                    if (holdflag)
                    {
                        //execSendMailAll
                        DataTable dt_mail = new DataTable();
                        dt_mail.AddColumn("機種");
                        dt_mail.AddColumn("批號");
                        dt_mail.AddColumn("LENS");
                        dt_mail.AddColumn("狀態");
                        dt_mail.AddColumn("單據號");

                        String hold_lot = "";
                        for (int j = 0; j < dt_hold.Rowscount(); j++)
                        {
                            hold_lot += dt_hold.Rows(j).get_CellValue("TRAYNO") + " ,";
                        }

                        dt_mail.AddRow(newdeviceno, lotno, "LENS:"+lenslot+" \r\nTRAY:"+hold_lot, "HOLD", numberdtG);
                        //new BLL.basedata().execSendMailAll(dt_mail, dt_set.Rows(0).get_CellValue("att3"), "【預警郵件】" + newdeviceno + " Lens批號對應的Tray盤 AA OUT 超 6H ", "0");
                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(dt_mail),  dt_set.Rows(0).get_CellValue("att3"),  "【預警郵件】" + newdeviceno + " Lens批號對應的Tray盤 AA OUT 超 6H ", "1", "0");
                        _a.ShowMessage("Lens批號對應的Tray盤 AA OUT 超 6H \r\n" + hold_lot + "\r\n該批號將被HOLD，請聯繫QC解鎖");
                        return;
                    }
                    else
                    {
                        _a.ShowMessage("Lens批號對應的Tray盤 AA OUT 超 6H \r\n執行HOLD失敗，請聯繫MIS");
                        return;
                    }
                }
            }
            else
            {
                _a.ShowMessage("沒有查詢到該批號對應的LENS批號");
                return;
            }
        }
    }

    public static void checklensandlotqty(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String opno,String lotno) throws Exception
    {
        DataTable dt_set =cwa.CallRDT("checkout_submit_169",opno);

        if (dt_set.Rowscount() > 0)
        {
            DataTable dt_lens =cwa.CallRDT("checkout_submit_170", lotno);
            if (dt_lens.Rowscount() > 0)
            {
                String lenslot = "";
                for (int i = 0; i < dt_lens.Rowscount(); i++)
                {
                    if (i == dt_lens.Rowscount() - 1)
                    {
                        lenslot += dt_lens.Rows(i).get_CellValue(0);
                    }
                    else
                    {
                        lenslot +=  dt_lens.Rows(i).get_CellValue(0) + ",";
                    }
                }

                DataTable dt_noinv =cwa.CallRDT("checkout_submit_171", lenslot);
                if (dt_lens.Rowscount() > 0)
                {
                    String hold_lot = "";
                    for (int j = 0; j < dt_noinv.Rowscount(); j++)
                    {
                        hold_lot += dt_noinv.Rows(j).get_CellValue("trayno") + " ";
                    }

                    _a.ShowMessage(hold_lot + "\r\n在TRAY盤掃描中未過站完畢\r\n不可過站");
                    return;
                }
            }
            else
            {
                _a.ShowMessage("沒有查詢到該批號對應的LENS批號");
                return;
            }
        }
    }

    public static Boolean removecarrieropno(ActivityInteractive _a,CallWebapi cwa,String opno,String lotno) throws Exception{
        String carrieropno =cwa.CallRS("removecarrieropno_1",opno);
        Boolean flag=false;
        if (carrieropno==null || carrieropno.equals(""))
        {
            if (lotno.startsWith("PK") || lotno.startsWith("AK"))
            {
                lotno = lotno.substring(1, lotno.length());
            }

            flag =cwa.CallRB("removecarrieropno_2",lotno);

        }
        return  flag;
    }

    public static Boolean removebindingformagazine(CallWebapi cwa,String lotno,String opno) throws Exception
    {
         Boolean Rvalue =cwa.CallRB("removebindingformagazine_1", lotno, opno);
         String Rmsg="";
            if (Rvalue)
            {
                Rmsg = "彈夾解綁成功";
            }
            else
            {

                Rmsg = "彈夾解綁失敗";
            }
            return Rvalue;


    }

    public  static void checkintocheckouttimemail(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String lotno,String opno,String opname) throws Exception
    {
        DataTable maildt = new DataTable();
        maildt.AddColumn("機種");
        maildt.AddColumn("批號");
        maildt.AddColumn("當前站位");
        maildt.AddColumn("當前狀態");
        maildt.AddColumn("管控時間");
        maildt.AddColumn("實際停留時間");

        DataTable dt_set =cwa.CallRDT("checkintocheckouttimemail_1",opno);
        if (dt_set != null)
        {
            if (dt_set.Rowscount() > 0)
            {
                String opnoname1 = "", opnoname2 = "";
                for (int i = 0; i < dt_set.Rowscount(); i++)
                {
                    String att1=dt_set.Rows(i).get_CellValue("att1");
                    String att2=dt_set.Rows(i).get_CellValue("att2");
                    String att4=dt_set.Rows(i).get_CellValue("att4");

                    opnoname1 = cwa.CallRS("checkintocheckouttimemail_2",att1);
                    opnoname2 = cwa.CallRS("checkintocheckouttimemail_2", att2);

                    DataTable lotdata=new DataTable();

                    if (att1.equals(att2))
                    {
                        lotdata=cwa.CallRDT("checkintocheckouttimemail_3",lotno,att1);
                    }
                    else
                    {
                        lotdata=cwa.CallRDT("checkintocheckouttimemail_4",lotno,att1);
                    }
                  
                    if (lotdata!= null)
                    {
                        if (lotdata.Rowscount() > 0)
                        {
                            String hours=lotdata.Rows(i).get_CellValue("hours");
                            if (Double.parseDouble(hours) > Double.parseDouble(att4))
                            {
                                maildt.AddRow(newdeviceno, lotno, opname, "待出站", att4, hours);

                                cwa.CallRB("execSendMailAll",BaseFuncation.SerializeObjectDataTable(maildt), "checkincheckout", "【預警郵件】" + newdeviceno + " " + lotno + " " + opname + " 出站時間超時", "0");

                            }
                        }
                    }
                }
            }
        }
    }

    public static Boolean checkfoltestnum(ActivityInteractive _a,CallWebapi cwa,String _deviceno,String _newdeviceno, String _odbname,String lotno,String opno,String inputqty) throws Exception
    {
        Boolean flag = true;

        String proportion =cwa.CallRS("checkfoltestnum_1", lotno, opno);

        if (lotno.startsWith("PK") || lotno.startsWith("AK"))
        {
            lotno = lotno.substring(1, lotno.length());
        }
        if (proportion == null)
        {
           _a.ShowMessage("查詢FOL測試數量設置失敗,(N41 8 50)");
            flag = false;
        }
        else if (proportion.equals(""))
        {
            flag = true;
            return flag;
        }
        else
        {
            String qty = cwa.CallRS("checkfoltestnum_2", lotno);

            if (qty == null)
            {
                _a.ShowMessage("查詢該批次測試數據失敗, T_lotpodtestupbak");
                flag = false;
            }
            else
            {
                    double testqty =  Double.parseDouble(qty);
                    double min = Double.parseDouble(proportion.split(";")[0]);
                    double max = Double.parseDouble(proportion.split(";")[1]);
                    double inqty = Double.parseDouble(inputqty);

                    if (testqty < inqty * min / 100 || testqty > inqty * max / 100)
                    {
                        if (BaseFuncation.DialogResult.OK == _a.MessageBox("測試數量不符合設置的" + min + "%< >" + max + "%,是否強行過站 ", "系統提示")) {
                            SFCStaticdata.staticmember.podtestsumcheck = false;
                            _a.CreatNewActivity(errorinfoshow.class, _deviceno, _newdeviceno, _odbname,
                                    "測試數目不到進站總數的" + min + "%< >" + max + "%", lotno, opno, SFCStaticdata.staticmember.odbname, "5", "5", "20", "", "");
                            if (!SFCStaticdata.staticmember.podtestsumcheck) {
                                _a.ShowMessage("验证强行出站失败！");
                                flag = false;
                            }
                            else
                            {
                                flag = true;
                                return flag;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = true;
                        return flag;
                    }

            }
        }

        return flag;
    }

    public static void checkintonotouttime(ActivityInteractive _a,CallWebapi cwa,String _deviceno,String _newdeviceno,String _odbname,String lotno,String opno,String opname,String ip) throws Exception
    {
        DataTable maildt = new DataTable();
        maildt.AddColumn("機種");
        maildt.AddColumn("批號");
        maildt.AddColumn("當前站位");
        maildt.AddColumn("狀態");
        maildt.AddColumn("規定停留時間(H)");
        maildt.AddColumn("已停留時間(H)");

        DataTable maildt_qh = new DataTable();
        maildt_qh.AddColumn("機種");
        maildt_qh.AddColumn("批號");
        maildt_qh.AddColumn("當前站位");
        maildt_qh.AddColumn("狀態");
        maildt_qh.AddColumn("規定停留時間(H)");
        maildt_qh.AddColumn("已停留時間(H)");
        maildt_qh.AddColumn("單據號");

            DataTable dt_set=cwa.CallRDT("checkintonotouttime_1",opno);

            String s_opno = "", e_opno = "", sql = "", groupid = "", numberdtG = "";
            double settime, lottime;
            Boolean qcholdflag = false;
            if (dt_set.Rowscount() > 0)
            {
                Boolean unholdflag = unholdlog(cwa,lotno, opno, "stagingtime", _odbname);
                if (unholdflag)
                {
                    //rts.Rstatus = true;  rts.Rmsg = "過站時間超時，QC已解鎖";
                    //return rts;
                }

                for (int i = 0; i < dt_set.Rowscount(); i++)
                {
                    groupid = dt_set.Rows(i).get_CellValue("GROUPID");
                    s_opno = dt_set.Rows(i).get_CellValue("STRAT");
                    e_opno = dt_set.Rows(i).get_CellValue("END");
                    settime = Double.parseDouble(dt_set.Rows(i).get_CellValue("HOURS"));

                    String dt_opno = cwa.CallRS("getopname",s_opno);
                    String dt_opno1 = cwa.CallRS("getopname", e_opno);

                    if (dt_opno == null || dt_opno1 == null || dt_opno.equals("")  || dt_opno1.equals(""))
                    {
                        _a.ShowMessage("stagetime設置的OPNO錯誤(不存在)");
                        //rts.Rstatus = false; rts.Rmsg = "stagetime設置的OPNO錯誤(不存在)";
                        return;
                    }
                    else
                    {
                        DataTable lotdata =cwa.CallRDT("checkintonotouttime_2",lotno,s_opno,e_opno);

                        if (lotdata == null)
                        {
                            //rts.Rstatus = true;
                            //rts.Rmsg = "查詢該批次" + dt_opno.Rows[0]["opname"].toString() + " 進站時間失敗";
                            //return rts;
                        }
                        else
                        {
                            if (lotdata.Rowscount() > 0)
                            {
                                lottime = Double.parseDouble(lotdata.Rows(0).get_CellValue("hours"));
                                if (lottime > settime)
                                {
                                    if (groupid.contains("HOLD"))
                                    {
                                        numberdtG = getqcholdnumber(cwa,_newdeviceno, _odbname, "");
                                        maildt_qh.AddRow(_deviceno, lotno, opname, "停留時間超時", Double.toString(settime), Double.toString(lottime), numberdtG);
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(maildt_qh), groupid, "【預警郵件】" + _newdeviceno + " | " + dt_set.Rows(i).get_CellValue("name") + " | checkin 到  未checkout 超時，QC已HOLD ", "0", "0");
                                      // execSendMailAll(, , "【預警郵件】" + _newdeviceno + " | " + dt_set.Rows(i).get_CellValue("name"].toString() + " | checkin 到  未checkout 超時，QC已HOLD ", "0");

                                        String[] list = { lotno, opno, "1", "1", "1", "sys", ip, "stagingtime", _newdeviceno, numberdtG, _odbname };
                                       Boolean flagin = runqchold(cwa,list);

                                        if (!flagin)
                                        {
                                            _a.ShowMessage("出站時間超時，QCHOLD失敗");
                                            //rts.Rstatus = false;
                                            //rts.Rmsg = "出站時間超時，QCHOLD失敗";
                                            return;
                                        }
                                        else
                                        {
                                            qcholdflag = true;
                                        }
                                    }
                                    else
                                    {
                                        maildt.AddRow(_deviceno, lotno, opname, "停留時間超時", Double.toString(settime), Double.toString(lottime));
                                        //new BLL.basedata().execSendMailAll(maildt, groupid, "【預警郵件】" + _newdeviceno + " | " + dt_set.Rows(i).get_CellValue("name"].toString() + " | checkin 到  未checkout 超時 ", "0");
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(maildt_qh), groupid, "【預警郵件】" + _newdeviceno + " | " + dt_set.Rows(i).get_CellValue("name") + " | checkin 到  未checkout 超時 ", "0", "0");
                                    }
                                }
                                else
                                {
                                    //rts.Rstatus = true;
                                    //rts.Rmsg = "未超時";
                                }
                            }
                            else
                            {
                                _a.ShowMessage("\"未查詢到\" + dt_opno.Rows[0][\"opname\"].toString() + \"進站時間");
                                //rts.Rstatus = false;
                                //rts.Rmsg = "未查詢到" + dt_opno.Rows[0]["opname"].toString() + "進站時間";
                                return;
                            }
                        }
                    }
                }

                if (qcholdflag)
                {
                    _a.ShowMessage("該批號過站時間超時，已被HOLD，需QC解鎖");
                    return;
                    //rts.Rstatus = false;                    //rts.Rmsg = "該批號過站時間超時，已被HOLD";
                }
            }
            else
            {
                //rts.Rstatus = true;
                //rts.Rmsg = "該站位不管控stagetime";
            }
            //return rts;

    }

    public static void checkintonotintime(ActivityInteractive _a,CallWebapi cwa,String _deviceno,String _newdeviceno,String _odbname,String lotno,String opno,String opname,String ip) throws Exception {

        DataTable maildt = new DataTable();
        maildt.AddColumn("機種");
        maildt.AddColumn("批號");
        maildt.AddColumn("當前站位");
        maildt.AddColumn("狀態");
        maildt.AddColumn("規定停留時間(H)");
        maildt.AddColumn("已停留時間(H)");

        DataTable maildt_qh = new DataTable();
        maildt_qh.AddColumn("機種");
        maildt_qh.AddColumn("批號");
        maildt_qh.AddColumn("當前站位");
        maildt_qh.AddColumn("狀態");
        maildt_qh.AddColumn("規定停留時間(H)");
        maildt_qh.AddColumn("已停留時間(H)");
        maildt_qh.AddColumn("單據號");

            DataTable dt_set =cwa.CallRDT("checkintonotintime_1",opno);

            String s_opno = "", e_opno = "", sql = "", groupid = "", numberdtG = "";
            double settime, lottime;
            Boolean qcholdflag = false;
            if (dt_set.Rowscount() > 0)
            {
                Boolean unholdflag = unholdlog(cwa,lotno, opno, "stagingtime", _odbname);

                if (unholdflag)
                {
                    //rts.Rstatus = true;   rts.Rmsg = "過站時間超時，QC已解鎖";
                    //return rts;
                }

                for (int i = 0; i < dt_set.Rowscount(); i++)
                {
                    groupid = dt_set.Rows(i).get_CellValue("GROUPID");
                    s_opno = dt_set.Rows(i).get_CellValue("STRAT");
                    e_opno = dt_set.Rows(i).get_CellValue("END");
                    settime = Double.parseDouble(dt_set.Rows(i).get_CellValue("HOURS"));

                    String dt_opno = cwa.CallRS("getopname",s_opno);
                    String dt_opno1 = cwa.CallRS("getopname", e_opno);

                    if (dt_opno == null || dt_opno1 == null || dt_opno.equals("")  || dt_opno1.equals(""))
                    {
                        _a.ShowMessage("stagetime設置的OPNO錯誤(不存在)");
                        //rts.Rstatus = false; rts.Rmsg = "stagetime設置的OPNO錯誤(不存在)";
                        return;
                    }
                    else
                    {
                        DataTable lotdata =cwa.CallRDT("checkintonotintime_2",lotno,s_opno);

                        if (lotdata == null)
                        {
                            //rts.Rstatus = true;  rts.Rmsg = "查詢該批次" + dt_opno.Rows[0]["opname"].ToString() + " 進站時間失敗";
                            //return rts;
                        }
                        else
                        {
                            if (lotdata.Rowscount() > 0)
                            {
                                lottime = Double.parseDouble(lotdata.Rows(0).get_CellValue("hours"));
                                if (lottime > settime)
                                {
                                    if (groupid.contains("HOLD"))
                                    {
                                        numberdtG = getqcholdnumber(cwa,_newdeviceno, _odbname, "");
                                        maildt_qh.AddRow(_deviceno, lotno, opname, "停留時間超時", Double.toString(settime), Double.toString(lottime), numberdtG);

                                        //new BLL.basedata().execSendMailAll(maildt_qh, groupid, "【預警郵件】" + send1.newdeviceno + " | " + dt_set.Rows[i]["name"].ToString() + " | checkin 到  未checkin 超時，QC已HOLD ", "0");
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(maildt_qh), groupid, "【預警郵件】" + _newdeviceno + " | " + dt_set.Rows(i).get_CellValue("name") + " | checkin 到  未checkin 超時，QC已HOLD ", "0", "0");
                                        String[] list = { lotno, opno, "1", "1", "1", "sys", ip, "stagingtime", _newdeviceno, numberdtG, _odbname };
                                        Boolean flagin = runqchold(cwa,list);

                                        if (!flagin)
                                        {
                                            _a.ShowMessage("進站時間超時，QCHOLD失敗");
                                            //rts.Rstatus = false;                                            rts.Rmsg = "進站時間超時，QCHOLD失敗";
                                            return;
                                        }
                                        else
                                        {
                                            qcholdflag = true;
                                        }
                                    }
                                    else
                                    {
                                        maildt.AddRow(_deviceno, lotno, opname, "停留時間超時", Double.toString(settime), Double.toString(lottime));
                                        //new BLL.basedata().execSendMailAll(maildt, groupid, "【預警郵件】" + send1.newdeviceno + " | " + dt_set.Rows[i]["name"].ToString() + " | checkin 到  未checkin 超時 ", "0");
                                        cwa.CallRB("execSendMailAll", BaseFuncation.SerializeObjectDataTable(maildt_qh), groupid, "【預警郵件】" + _newdeviceno + " | " + dt_set.Rows(i).get_CellValue("name") + " | checkin 到  未checkin 超時 ", "0", "0");
                                    }
                                }
                                else
                                {
                                    //rts.Rstatus = true;                                    rts.Rmsg = "未超時";
                                }
                            }
                            else
                            {
                               // rts.Rstatus = false;
                                //rts.Rmsg = "未查詢到" + dt_opno + "進站時間";
                                _a.ShowMessage( "未查詢到" + dt_opno + "進站時間");
                                return;
                            }
                        }
                    }
                }


                if (qcholdflag)
                {
                   _a.ShowMessage("該批號過站時間超時，已被HOLD，需QC解鎖");
                    //rts.Rstatus = false;                    rts.Rmsg = "該批號過站時間超時，已被HOLD";
                }
            }
            else
            {
                //rts.Rstatus = true;                rts.Rmsg = "該站位不管控stagetime";
            }


    }


    public static String getqcholdnumber(CallWebapi cwa,String deviceno,String odbname,String type) throws Exception
    {
            String numberdtG="";
            String numG =cwa.CallRS("checkout_submit_166");
            //numG = Convert.ToDateTime(numG).toString("yyyyMMdd");//查時間
            numG =BaseFuncation.getnowdatetime(BaseFuncation.getnowdatetime(numG, "yyyy/MM/dd HH:mm:ss").getTime(), "yyyyMMdd");

            String numbeG =cwa.CallRS("checkintonotouttime_3",deviceno); //也是時間

            if (numbeG == null ||  numbeG.equals(""))
            {
                numbeG = "0001";
            }
            else
            {
                if (!numG.equals(numbeG.substring(0, 8)))
                {
                    numbeG = "0001";
                }
                else
                {
                    int number1G =  Integer.parseInt(numbeG.substring(numbeG.length() - 4, numbeG.length())) + 1;
                    numbeG = ("0000" + Integer.toString(number1G));
                    numbeG = numbeG.substring(numbeG.length() - 4, numbeG.length());
                }
            }

            numberdtG = deviceno + type + numG + numbeG;
            return numberdtG;
    }

    public static Boolean runqchold(CallWebapi cwa,String[] holdcondition)   throws Exception
    {
        String lotno = holdcondition[0].toString(); //批號
        String holdopno = holdcondition[1].toString(); //扣留站位
        String yield = holdcondition[2].toString(); //良率
        String inptqty = holdcondition[3].toString(); //投入數
        String dbqty = holdcondition[4].toString(); //不良數
        String userid = holdcondition[5].toString(); //userid
        String ip = holdcondition[6].toString(); //IP
        String type = holdcondition[7].toString(); //類別
        String deviceno = holdcondition[8].toString(); //機種
        String numberdtG = holdcondition[9].toString(); //單號
        String odbname = holdcondition[10].toString(); //odbname

        Boolean holdflag =cwa.CallRB("runqchold_1",lotno, holdopno, yield, inptqty, dbqty, userid, ip, type, deviceno, numberdtG);

        return holdflag;
    }
    
    public static Boolean unholdlog(CallWebapi cwa,String lotno, String opno,String type,String odbname)  throws Exception
    {
            DataTable dt=cwa.CallRDT("unholdlog_1", lotno, opno, type);
            Boolean flag = false;
            if (dt.Rowscount() > 0)
            {
                flag = true;
            }
            return flag;

    }

    public static boolean saveconfigdata(CallWebapi cwa,String send1lotno,String opno, String staticopnoflowid,boolean lotfoleolcheckflag) throws Exception
    {
        String lotno = send1lotno.split("-")[0];
        String config = "",  memo = "",insertsql = "", i_lot = send1lotno;
        String Rmsg="";
        boolean Rstatus=false;

        if (send1lotno.startsWith("PK") || send1lotno.startsWith("AK"))
        {
            lotno = lotno.substring(1, lotno.length());
            i_lot = send1lotno.substring(1, send1lotno.length());
        }

        DataTable dt =cwa.CallRDT("saveconfigdata_4",lotno);
        if (dt == null)
        {
            Rmsg = "查詢組合信息失敗";
        }
        else if (dt.Rowscount() > 0)
        {
            memo = dt.Rows(0).get_CellValue("memo");
            config = dt.Rows(0).get_CellValue("config");

            Rstatus =cwa.CallRB("saveconfigdata_3",i_lot, memo, config,opno);

            if (Rstatus)
            {
                Rmsg = "記錄組合信息成功";
            }
            else
            {
                Rmsg = "記錄組合信息失敗";
            }
        }
        else
        {
            Rmsg = "沒有維護組合信息";
        }
        Rstatus = true;

        return Rstatus;
    }

    public static void checkin_submit2(ActivityInteractive _a,CallWebapi cwa,String newdeviceno,String lotno,String opno)  throws Exception
    {
        if (!SFCStaticdata.staticmember.AllReworkFlag)
        {
            //新機種卡是否解析數據 重工不需要解析數據
            if (SFCStaticdata.staticmember.ip.equals("10.155.23.184"))
            {

            }
            else
            {
                if (opno.equals("0910"))
                {
                    DataTable dtanalyzeopno =cwa.CallRDT("checkout_submit_173",newdeviceno,opno);
                    if (dtanalyzeopno == null)
                    {
                        _a.ShowMessage("獲取解析站位異常 ~ 溫馨提示");
                        return;
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

                        DataTable dt_anazylerecord =cwa.CallRDT("checkout_submit_174",lotnoA);

                        DataTable dt_analyzeitem = cwa.CallRDT("checkout_submit_175");

                        List<String> list_anazylerecord=new List<String>() {
                            @Override
                            public void add(int location, String object) {

                            }

                            @Override
                            public boolean add(String object) {
                                return false;
                            }

                            @Override
                            public boolean addAll(int location, Collection<? extends String> collection) {
                                return false;
                            }

                            @Override
                            public boolean addAll(Collection<? extends String> collection) {
                                return false;
                            }

                            @Override
                            public void clear() {

                            }

                            @Override
                            public boolean contains(Object object) {
                                return false;
                            }

                            @Override
                            public boolean containsAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public String get(int location) {
                                return null;
                            }

                            @Override
                            public int indexOf(Object object) {
                                return 0;
                            }

                            @Override
                            public boolean isEmpty() {
                                return false;
                            }

                            @NonNull
                            @Override
                            public Iterator<String> iterator() {
                                return null;
                            }

                            @Override
                            public int lastIndexOf(Object object) {
                                return 0;
                            }

                            @Override
                            public ListIterator<String> listIterator() {
                                return null;
                            }

                            @NonNull
                            @Override
                            public ListIterator<String> listIterator(int location) {
                                return null;
                            }

                            @Override
                            public String remove(int location) {
                                return null;
                            }

                            @Override
                            public boolean remove(Object object) {
                                return false;
                            }

                            @Override
                            public boolean removeAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public boolean retainAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public String set(int location, String object) {
                                return null;
                            }

                            @Override
                            public int size() {
                                return 0;
                            }

                            @NonNull
                            @Override
                            public List<String> subList(int start, int end) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Object[] toArray() {
                                return new Object[0];
                            }

                            @NonNull
                            @Override
                            public <T> T[] toArray(T[] array) {
                                return null;
                            }
                        };
                        List<String> list_analyzeitem=new List<String>() {
                            @Override
                            public void add(int location, String object) {

                            }

                            @Override
                            public boolean add(String object) {
                                return false;
                            }

                            @Override
                            public boolean addAll(int location, Collection<? extends String> collection) {
                                return false;
                            }

                            @Override
                            public boolean addAll(Collection<? extends String> collection) {
                                return false;
                            }

                            @Override
                            public void clear() {

                            }

                            @Override
                            public boolean contains(Object object) {
                                return false;
                            }

                            @Override
                            public boolean containsAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public String get(int location) {
                                return null;
                            }

                            @Override
                            public int indexOf(Object object) {
                                return 0;
                            }

                            @Override
                            public boolean isEmpty() {
                                return false;
                            }

                            @NonNull
                            @Override
                            public Iterator<String> iterator() {
                                return null;
                            }

                            @Override
                            public int lastIndexOf(Object object) {
                                return 0;
                            }

                            @Override
                            public ListIterator<String> listIterator() {
                                return null;
                            }

                            @NonNull
                            @Override
                            public ListIterator<String> listIterator(int location) {
                                return null;
                            }

                            @Override
                            public String remove(int location) {
                                return null;
                            }

                            @Override
                            public boolean remove(Object object) {
                                return false;
                            }

                            @Override
                            public boolean removeAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public boolean retainAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public String set(int location, String object) {
                                return null;
                            }

                            @Override
                            public int size() {
                                return 0;
                            }

                            @NonNull
                            @Override
                            public List<String> subList(int start, int end) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Object[] toArray() {
                                return new Object[0];
                            }

                            @NonNull
                            @Override
                            public <T> T[] toArray(T[] array) {
                                return null;
                            }
                        };

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

                        String futre_item = "";//未解析的項目
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

                        if (futre_item == null  || futre_item.equals(""))
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

    public static boolean reworknocheckdev(CallWebapi cwa,String newdeviceno) throws Exception
    {
        DataTable dt =cwa.CallRDT("reworknocheckdev_1",newdeviceno);

        boolean Rstatus = false;
        String Rmsg="";
        if (dt == null)
        {
            Rstatus = false;
            Rmsg = "檢測是否為特殊重工機種失敗";
        }
        else
        {
            if (dt.Rowscount() > 0)
            {
                Rstatus = true;
                Rmsg = "特殊重工機種";
            }
            else
            {
                Rstatus = false;
                Rmsg = "不為特殊重工機種";
            }
        }
        return Rstatus;
    }

}
