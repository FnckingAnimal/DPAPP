package app.dpapp.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

/**
 * Created by S7202916 on 2018/9/28.
 */
public class AsynNetUtils {
    public interface Callback{
        void onResponse(String rspoonse);
    }
    public static void get(final String url,final Callback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtils.get(url);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }

    public static void post(final String str,final String url,final Callback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtils.post(url, str);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }

    public static String getWifiMac(Context context)  {
        WifiManager wifi = (WifiManager) context.getSystemService((Context.WIFI_SERVICE));
        WifiInfo info = wifi.getConnectionInfo();
        String str = info.getMacAddress();
        if(str == null) str = "";
        return str;
    }
    //取出转义
    public static String getDecodeJSONStr(String s) {
        s = s.replace("\\","");
        char[] array = s.toCharArray();
        char[] charArray = new char[array.length - 2];
        for (int i = 0; i < array.length - 1; i++) {
            charArray[i - 1] = array[i];
        }
        return new String(charArray);
    }
    //希尔算法  希尔排序不是稳定排序
    public static void sort(int[] array) {
     int d = array.length;
     while (d > 1) {
         d = d/2;
         for (int x = 0; x < d; x++) {
             for (int i = x+d; i < array.length; i = i+d) {
                 int temp = array[i];
                 int j;
                 for (j = i-d; j > 0 && array[j] > temp; j=j-d) {
                    array[j+d] = array[j];
                 }
                 array[j+d]=temp;
             }
         }
     }
    }
}
