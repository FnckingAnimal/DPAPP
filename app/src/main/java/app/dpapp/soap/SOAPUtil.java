package app.dpapp.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class SOAPUtil {
    public static int RESULT_STRING=1;
    public static int RESULT_DATATABLE=2;
    public static SoapObject loadDataBySOAP(String methodname, List<SOAPParameter> parameterList, String url,int resulttype) {
        try {
            SoapObject soapchild=null;
            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = methodname;
            String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            if (parameterList != null) {
                for (int i = 0; i < parameterList.size(); i++) {
                    SOAPParameter parameter = parameterList.get(i);
                    rpc.addProperty(parameter.get_key(), parameter.get_value());
                }
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE transport = new HttpTransportSE(url);
            try {
                // 调用WebService
                transport.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SoapObject r1 = (SoapObject) envelope.bodyIn;

            try {
                switch (resulttype) {
                    case 1:
                        soapchild = r1;
                        break;
                    case 2:
                        soapchild =(SoapObject) ((SoapObject) ((SoapObject) r1.getProperty(0)).getProperty(1)).getProperty(0);
                        break;
                    default:
                        break;
                }
            } catch (Exception ex1) {
                return null;
            }
            return soapchild;
        } catch (Exception ex2) {
            return null;
        }
    }
}
