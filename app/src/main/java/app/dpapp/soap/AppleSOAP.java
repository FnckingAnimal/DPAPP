package app.dpapp.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.Staticdata;
import app.dpapp.bean.ListnoBean;
import app.dpapp.bean.MaterialLotnoBean;
import app.dpapp.bean.MaterialTypeBean;
import app.dpapp.bean.MaterialjiaBean;
import app.dpapp.bean.MaterialjiaLotnoBean;

/**
 * Created by Administrator on 2020/9/11.
 */
public class AppleSOAP {
    String URL = Staticdata.applesfcurl;
    String URL_VCHAR = Staticdata.lhurl;

    //获取物料架的信息
    public MaterialjiaBean getMaterialjiaInfo(String jiano) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialjiaInfo";
            String SOAP_ACTION = "http://tempuri.org/getMaterialjiaInfo";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("jiano", jiano);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            MaterialjiaBean mjb = new MaterialjiaBean();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                mjb.jiano = ((SoapObject) soapchild.getProperty(0)).getProperty("JIANO").toString();
                mjb.deviceno = ((SoapObject) soapchild.getProperty(0)).getProperty("DEVICENO").toString();
                mjb.buildingno = ((SoapObject) soapchild.getProperty(0)).getProperty("BUILDINGNO").toString();
                mjb.cengshu = ((SoapObject) soapchild.getProperty(0)).getProperty("CENGSHU").toString();
                mjb.productno = ((SoapObject) soapchild.getProperty(0)).getProperty("PRODUCTNO").toString();
                mjb.cangma = ((SoapObject) soapchild.getProperty(0)).getProperty("CANGMA").toString();
                mjb.qiety = ((SoapObject) soapchild.getProperty(0)).getProperty("QIETY").toString();

            } catch (Exception ex1) {
                mjb = null;
            }
            return mjb;
        } catch (Exception ex1) {
            return null;
        }
    }

    //获取物料架上的批号信息
    public List<String> getMaterialjiaLotno(String jiano) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialjiaLotno";
            String SOAP_ACTION = "http://tempuri.org/getMaterialjiaLotno";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("jiano", jiano);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> resultList1 = new ArrayList<String>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                resultList1.add("ALL");
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    String lotno = ((SoapObject) soapchild.getProperty(i)).getProperty("LOTNO").toString();
                    resultList1.add(lotno);
                }
            } catch (Exception ex1) {
                resultList1 = null;
            }
            return resultList1;
        } catch (Exception ex1) {
            return null;
        }
    }

    //获取仓码
    public List<String> getLotnocangma(String lotno, String cangmastr) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getLotnocangma";
            String SOAP_ACTION = "http://tempuri.org/getLotnocangma";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("lotno", lotno);
            rpc.addProperty("cangmastr", cangmastr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> resultList = new ArrayList<String>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    String lv_cangma = ((SoapObject) soapchild.getProperty(i)).getProperty(0).toString();
                    resultList.add(lv_cangma);
                }
            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    //获取物料架
    public List<String> getMaterialjianoBypro(String productno) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialjianoBypro";
            String SOAP_ACTION = "http://tempuri.org/getMaterialjianoBypro";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("productno", productno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> resultList = new ArrayList<String>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    String jiano = ((SoapObject) soapchild.getProperty(i)).getProperty(0).toString();
                    resultList.add(jiano);
                }
            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    //获取物料架上面每个库存批号的信息
    public List<MaterialjiaLotnoBean> getMaterialjiaLotnoData(String lotno, String jiano) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialjiaLotnoData";
            String SOAP_ACTION = "http://tempuri.org/getMaterialjiaLotnoData";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("lotno", lotno);
            rpc.addProperty("jiano", jiano);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<MaterialjiaLotnoBean> resultList = new ArrayList<MaterialjiaLotnoBean>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    MaterialjiaLotnoBean mjb = new MaterialjiaLotnoBean();
                    mjb.jiano = ((SoapObject) soapchild.getProperty(i)).getProperty("JIANO").toString();
                    mjb.wono = ((SoapObject) soapchild.getProperty(i)).getProperty("WONO").toString();
                    mjb.lotno = ((SoapObject) soapchild.getProperty(i)).getProperty("LOTNO").toString();
                    mjb.productno = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTNO").toString();
                    mjb.qiety = ((SoapObject) soapchild.getProperty(i)).getProperty("QIETY").toString();
                    mjb.creadate = ((SoapObject) soapchild.getProperty(i)).getProperty("CREATEDATE").toString();
                    resultList.add(mjb);
                }
            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    //getLotnocangma

    //获取批号相关信息
    public String getLotnoInfo(String lotno) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getLotnoInfo";
            String SOAP_ACTION = "http://tempuri.org/getLotnoInfo";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("lotno", lotno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String resultstr;

            try {
                resultstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                resultstr = null;
            }
            return resultstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String getListnoInfo(String listno) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getListnoInfo";
            String SOAP_ACTION = "http://tempuri.org/getListnoInfo";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("listno", listno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String resultstr;

            try {
                resultstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                resultstr = null;
            }
            return resultstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String getListnoSendedQty(String listno, String productno) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getListnoSendedQty";
            String SOAP_ACTION = "http://tempuri.org/getListnoSendedQty";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("listno", listno);
            rpc.addProperty("productno", productno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String resultstr;

            try {
                resultstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                resultstr = null;
            }
            return resultstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String saveLotnoData(String datastr) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "saveLotnoData";
            String SOAP_ACTION = "http://tempuri.org/saveLotnoData";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("datastr", datastr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }
            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String saveListnoData(String datastr) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "saveListnoData";
            String SOAP_ACTION = "http://tempuri.org/saveListnoData";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("datastr", datastr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }
            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public List<ListnoBean> getListnoInfobywono(String o_wono) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getListnoInfobywono";
            String SOAP_ACTION = "http://tempuri.org/getListnoInfobywono";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("wono", o_wono);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<ListnoBean> resultList = new ArrayList<ListnoBean>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ListnoBean lb = new ListnoBean();
                    lb.wono = ((SoapObject) soapchild.getProperty(i)).getProperty("WONO").toString();
                    lb.listno = ((SoapObject) soapchild.getProperty(i)).getProperty("LISTNO").toString();
                    lb.listno1 = ((SoapObject) soapchild.getProperty(i)).getProperty("LISTNO1").toString();
                    lb.lotno = ((SoapObject) soapchild.getProperty(i)).getProperty("LOTNO").toString();
                    lb.productno = ((SoapObject) soapchild.getProperty(i)).getProperty("PRODUCTNO").toString();
                    lb.cartno = ((SoapObject) soapchild.getProperty(i)).getProperty("CARTNO").toString();
                    lb.type = ((SoapObject) soapchild.getProperty(i)).getProperty("TYPE").toString();
                    lb.qty = ((SoapObject) soapchild.getProperty(i)).getProperty("QTY").toString();
                    lb.ctime = ((SoapObject) soapchild.getProperty(i)).getProperty("CREATEDATE").toString();
                    resultList.add(lb);
                }

            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String Listnoczbywono(String wono) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "Listnoczbywono";
            String SOAP_ACTION = "http://tempuri.org/Listnoczbywono";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("wono", wono);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }

            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    //查询物料类型
    public List<MaterialTypeBean> getMaterialType() {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialType";
            String SOAP_ACTION = "http://tempuri.org/getMaterialType";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<MaterialTypeBean> resultList = new ArrayList<MaterialTypeBean>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    MaterialTypeBean mtb = new MaterialTypeBean();
                    mtb.materialtype = ((SoapObject) soapchild.getProperty(i)).getProperty("MATERIALTYPE").toString();
                    mtb.typeflag = ((SoapObject) soapchild.getProperty(i)).getProperty("TYPEFLAG").toString();
                    resultList.add(mtb);
                }

            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    //查询物料批号状态
    public String getMateriallotStatus(String materiallotno, String materialno, String deviceno) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMateriallotStatus";
            String SOAP_ACTION = "http://tempuri.org/getMateriallotStatus";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("materiallotno", materiallotno);
            rpc.addProperty("materialno", materialno);
            rpc.addProperty("deviceno", deviceno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }
            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public List<String> getMaterialno(String materialtype, String deviceno) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialno";
            String SOAP_ACTION = "http://tempuri.org/getMaterialno";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("materialtype", materialtype);
            rpc.addProperty("deviceno", deviceno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> resultList = new ArrayList<String>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    resultList.add(((SoapObject) soapchild.getProperty(i)).getProperty("MATERIALNO").toString());
                }

            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String getMaterialname(String materialno) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialname";
            String SOAP_ACTION = "http://tempuri.org/getMaterialname";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("materialno", materialno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }

            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public List<String> getBH(String typeflag, String deviceno) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getBH";
            String SOAP_ACTION = "http://tempuri.org/getBH";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("typeflag", typeflag);
            rpc.addProperty("deviceno", deviceno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> resultList = new ArrayList<String>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    resultList.add(((SoapObject) soapchild.getProperty(i)).getProperty("JIANO").toString());
                }

            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    public List<String> getMaterialDeviceno(String typeflag) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMaterialDeviceno";
            String SOAP_ACTION = "http://tempuri.org/getMaterialDeviceno";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("typeflag", typeflag);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            List<String> resultList = new ArrayList<String>();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();

                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    resultList.add(((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString());
                }

            } catch (Exception ex1) {
                resultList = null;
            }
            return resultList;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String saveMaterialLotnos(String datastr) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "saveMaterialLotnos";
            String SOAP_ACTION = "http://tempuri.org/saveMaterialLotnos";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("datastr", datastr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }

            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public MaterialLotnoBean getMateriallotnoData(String materiallotno, String typeflag) {

        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "getMateriallotnoData";
            String SOAP_ACTION = "http://tempuri.org/getMateriallotnoData";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("materiallotno", materiallotno);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            MaterialLotnoBean resulmlb = new MaterialLotnoBean();

            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                resulmlb.setLotno(((SoapObject) soapchild.getProperty(0)).getProperty("MATERIALLOTNO").toString());
                resulmlb.setMaterialno(((SoapObject) soapchild.getProperty(0)).getProperty("MATERIALNO").toString());
                resulmlb.setMaterialname(((SoapObject) soapchild.getProperty(0)).getProperty("MATERIALNAME").toString());
                resulmlb.setDeviceno(((SoapObject) soapchild.getProperty(0)).getProperty("DEVICENO").toString());
                resulmlb.setBh(((SoapObject) soapchild.getProperty(0)).getProperty("JIANO").toString());
                resulmlb.setQty(((SoapObject) soapchild.getProperty(0)).getProperty("DIEQTY").toString());
                resulmlb.setTypeflag(((SoapObject) soapchild.getProperty(0)).getProperty("TYPE").toString());
                resulmlb.setLotnostatus(((SoapObject) soapchild.getProperty(0)).getProperty("LOTSTATUS").toString());

            } catch (Exception ex1) {
                resulmlb = null;
            }
            return resulmlb;
        } catch (Exception ex1) {
            return null;
        }
    }

    public String updateMaterialLotnos(String datastr) {
        try {
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "updateMaterialLotnos";
            String SOAP_ACTION = "http://tempuri.org/updateMaterialLotnos";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("datastr", datastr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SoapObject r1 = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String returnstr;             // -1=獲取資料失敗

            try {
                returnstr = r1.getProperty(0).toString();
            } catch (Exception ex1) {
                returnstr = null;
            }

            return returnstr;
        } catch (Exception ex1) {
            return null;
        }
    }

    public List<String> getMaterialjiadevicno(List<SOAPParameter> parameterList) {
        String methodname = "getMaterialjiadevicno";
        List<String> list = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    list.add(((SoapObject) soapObject.getProperty(i)).getProperty("DEVICENO").toString());
                }
            } else {
                list = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return list;
    }

    public List<String> getMaterialjiashopno(List<SOAPParameter> parameterList) {
        String methodname = "getMaterialjiashopno";
        List<String> list = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    list.add(((SoapObject) soapObject.getProperty(i)).getProperty("SHOPNO").toString());
                }
            } else {
                list = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return list;
    }

    public List<String> getMaterialjiaPro(List<SOAPParameter> parameterList) {
        String methodname = "getMaterialjiaPro";
        List<String> list = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    list.add(((SoapObject) soapObject.getProperty(i)).getProperty("PRODUCTNO").toString());
                }
            } else {
                list = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return list;
    }

    public List<String> getMaterialjiajiano(List<SOAPParameter> parameterList) {
        String methodname = "getMaterialjiajiano";
        List<String> list = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                list.add("ALL");
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    list.add(((SoapObject) soapObject.getProperty(i)).getProperty("JIANO").toString());
                }
            } else {
                list = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return list;
    }

    public List<String[]> getLotnoinDataByMaterialno(List<SOAPParameter> parameterList) {
        String methodname = "getLotnoinDataByMaterialno";
        List<String[]> list = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    String deviceno = ((SoapObject) soapObject.getProperty(i)).getProperty("DEVICENO").toString();
                    String shopno = ((SoapObject) soapObject.getProperty(i)).getProperty("SHOPNO").toString();
                    String materialno = ((SoapObject) soapObject.getProperty(i)).getProperty("MATERIALNO").toString();
                    String jiano = ((SoapObject) soapObject.getProperty(i)).getProperty("JIANO").toString();
                    String lotno = ((SoapObject) soapObject.getProperty(i)).getProperty("LOTNO").toString();
                    String qty = ((SoapObject) soapObject.getProperty(i)).getProperty("QIETY").toString();
                    String time = ((SoapObject) soapObject.getProperty(i)).getProperty("CTIME").toString();
                    String[] strings = new String[]{deviceno, shopno, materialno, jiano, lotno, qty, time};
                    list.add(strings);
                }
            } else {
                list = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return list;
    }

    public List<MaterialLotnoBean> getMaterialListData(List<SOAPParameter> parameterList) {
        String methodname = "getMaterialListData";
        List<MaterialLotnoBean> list = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    MaterialLotnoBean mlb = new MaterialLotnoBean();
                    mlb.setLotno(((SoapObject) soapObject.getProperty(i)).getProperty("MATERIALLOTNO").toString());
                    mlb.setMaterialno(((SoapObject) soapObject.getProperty(i)).getProperty("MATERIALNO").toString());
                    mlb.setMaterialname(((SoapObject) soapObject.getProperty(i)).getProperty("MATERIALNAME").toString());
                    mlb.setDeviceno(((SoapObject) soapObject.getProperty(i)).getProperty("DEVICENO").toString());
                    mlb.setBh(((SoapObject) soapObject.getProperty(i)).getProperty("JIANO").toString());
                    mlb.setQty(((SoapObject) soapObject.getProperty(i)).getProperty("DIEQTY").toString());
                    mlb.setTypeflag(((SoapObject) soapObject.getProperty(i)).getProperty("TYPE").toString());
                    mlb.setLotnostatus(((SoapObject) soapObject.getProperty(i)).getProperty("LOTSTATUS").toString());
                    list.add(mlb);
                }
            } else {
                list = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return list;
    }

    public String[] getLotnoWipInfo(String methodname, List<SOAPParameter> parameterList) {
        String[] result = new String[3];
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                result[0] = ((SoapObject) soapObject.getProperty(0)).getProperty("LOTNO").toString();
                result[1] = ((SoapObject) soapObject.getProperty(0)).getProperty("DEVICENO").toString();
                result[2] = ((SoapObject) soapObject.getProperty(0)).getProperty("PRODUCTNO").toString();
            } else {
                result = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public List<String> getLotnoBondLiaojiaInfo(String methodname, List<SOAPParameter> parameterList) {
        List<String> result = new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                String lotno = ((SoapObject) soapObject.getProperty(0)).getProperty("LOTNO").toString();
                String liaojia = ((SoapObject) soapObject.getProperty(0)).getProperty("LIAOJIA").toString();
                String deviceno = ((SoapObject) soapObject.getProperty(0)).getProperty("DEVICENO").toString();
                String productno = ((SoapObject) soapObject.getProperty(0)).getProperty("PRODUCTNO").toString();
                String creator = ((SoapObject) soapObject.getProperty(0)).getProperty("CREATOR").toString();
                String state = ((SoapObject) soapObject.getProperty(0)).getProperty("STATE").toString();
                result.add(lotno);
                result.add(liaojia);
                result.add(deviceno);
                result.add(productno);
                result.add(creator);
                result.add(state);
            } else {
                result = null;
            }
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    //获取机台号
    public String getmachinenoBysysid(String methodname, List<SOAPParameter> parameterList) {
        String result ;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String checkLotnoBondMachine(String methodname, List<SOAPParameter> parameterList) {
        String result ;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String saveLotnBondMachineCheckinInfo(String methodname, List<SOAPParameter> parameterList) {
        String result ;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String updateLotnBondMachineCheckOutInfo(String methodname, List<SOAPParameter> parameterList) {
        String result ;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = "";
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String getStringBySOAP (String methodname, List<SOAPParameter> parameterList){
        String result;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = "";
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public List<String[]> getRaTestLotnoInfo(String methodname, List<SOAPParameter> parameterList) {
        List<String[]> result=new ArrayList<>();
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_DATATABLE);
            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                   String testlotno = ((SoapObject) soapObject.getProperty(i)).getProperty("TESTLOTNO").toString();
                    String countvalue = ((SoapObject) soapObject.getProperty(i)).getProperty("COUNTVALUE").toString();
                    String machineno = ((SoapObject) soapObject.getProperty(i)).getProperty("MACHINENO").toString();
                    String checkindate = ((SoapObject) soapObject.getProperty(i)).getProperty("CHECKINDATE").toString();
                    String checkinuser = ((SoapObject) soapObject.getProperty(i)).getProperty("CHECKINUSER").toString();
                    String checkoutdate = ((SoapObject) soapObject.getProperty(i)).getProperty("CHECKOUTDATE").toString();
                    String checkoutuser = ((SoapObject) soapObject.getProperty(i)).getProperty("CHECKOUTUSER").toString();
                    String state = ((SoapObject) soapObject.getProperty(i)).getProperty("STATE").toString();
                    String[] strs={testlotno,countvalue,machineno,checkindate,checkinuser,checkoutdate,checkoutuser,state};
                    result.add(strs);
                }
            } else {
                result = null;
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String getRaInfoTESTROOM(String methodname, List<SOAPParameter> parameterList) {
        String result;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = "";
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String getRaInfoKUINO(String methodname, List<SOAPParameter> parameterList) {
        String result;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = "";
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }

    public String getRaInfoMACHINENO(String methodname, List<SOAPParameter> parameterList) {
        String result;
        try {
            SoapObject soapObject = SOAPUtil.loadDataBySOAP(methodname, parameterList, URL, SOAPUtil.RESULT_STRING);
            if (soapObject != null) {
                result = soapObject.getProperty(0).toString();
            } else {
                result = "";
            }
        } catch (Exception ex1) {
            return null;
        }
        return result;
    }
}
