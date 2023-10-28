package app.dpapp.appcdl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import java.lang.reflect.Method;
/**
 * Created by Administrator on 2016/7/4.
 */
 public class NetUtils {
    public static boolean checkNetWork(Context context) {
        try {


            ConnectivityManager connectactivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectactivity != null) {
                //获知网络管理的对象
                NetworkInfo info = connectactivity.getActiveNetworkInfo();
                //                判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public  static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            Method systemProperties_get = null;

            systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null) {
                return ret;
            }
            else
            {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getLocalIPAddress(Context c)
            throws Exception {
        int ipAddress = ((WifiManager) c.getSystemService(c.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

}
