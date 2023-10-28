package app.cmapp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import app.cmapp.bean.E4DevicenoBean;
import app.cmapp.bean.E4LotnoStatusBean;
import app.dpapp.Staticdata;

public class SFCSOAP {
    String URL= Staticdata.httpurl;

    /**獲取平板过站机种
     * by Lyh
     * 2019-9-23
     */
    public List<String> sfc_getdeviceno(String type) {

        try {
            String NAMESPACE = "http://tempuri.org/";

            String METHOD_NAME = "getdeviceno";
            String SOAP_ACTION = "http://tempuri.org/getdeviceno";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("type", type);

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

            List<String> ls1;
            ls1 = new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {
                    ls1.add(((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString());

                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**獲取过站机种设置
     * by Lyh
     * 2019-9-23
     */
    public List<E4DevicenoBean> sfc_getdevicenoInfo(String deviceno, String type) {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String METHOD_NAME = "getdevicenoInfo";
            String SOAP_ACTION = "http://tempuri.org/getdevicenoInfo";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("deviceno",deviceno);
            rpc.addProperty("type", type);
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

            List<E4DevicenoBean> ls1=new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                    E4DevicenoBean e4Bean=new E4DevicenoBean();
                    e4Bean.deviceno=((SoapObject) soapchild.getProperty(i)).getProperty("DEVICENO").toString();
                    e4Bean.dblinkstr=((SoapObject) soapchild.getProperty(i)).getProperty("DBLINKSTR").toString();
                    e4Bean.lotnosubflag=((SoapObject) soapchild.getProperty(i)).getProperty("ATT1").toString();
                    ls1.add(e4Bean);
                }


            } catch (Exception ex1) {
                ls1 = null;
            }

            return ls1;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }

    /**獲取过站机种设置
     * by Lyh
     * 2019-9-23
     */
    public List<E4LotnoStatusBean> sfc_getlotstatus(String dbname) {
        try {
            String NAMESPACE = "http://tempuri.org/";

            String METHOD_NAME = "getlotstatus";
            String SOAP_ACTION = "http://tempuri.org/getlotstatus";


            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("dbname",dbname);
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

            List<E4LotnoStatusBean> ls=new ArrayList<>();
            try {
                SoapObject soapchild = (SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);

                // 获取返回的结果
                //soapresult1 = r1.getProperty(0).toString();
                for (int i = 0; i < soapchild.getPropertyCount(); i++) {

                    E4LotnoStatusBean lotstatus=new E4LotnoStatusBean();
                    lotstatus.setXh(String.valueOf(i+1));
                    lotstatus.setLotno(((SoapObject) soapchild.getProperty(i)).getProperty("LOTNO").toString());
                    lotstatus.setOpno(((SoapObject) soapchild.getProperty(i)).getProperty("OPNO").toString());
                    lotstatus.setQty(((SoapObject) soapchild.getProperty(i)).getProperty("DIEQTY").toString());
                    lotstatus.setTime(((SoapObject) soapchild.getProperty(i)).getProperty("UPDATEDATE").toString());
                    lotstatus.setZhuangtai(((SoapObject) soapchild.getProperty(i)).getProperty("LOTSTATE").toString());

                    ls.add(lotstatus);
                }


            } catch (Exception ex1) {
                ls = null;
            }

            return ls;
        }
        catch(Exception ex1)
        {
            return null;
        }
    }


}