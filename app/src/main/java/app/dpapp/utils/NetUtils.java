package app.dpapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by S7187445 on 2018/9/28.
 */
public class NetUtils {
     public static  String post(String url,String content) {
         HttpURLConnection conn = null;
         try {
             URL mURL = new URL(url);
             try {
                 conn = (HttpURLConnection)mURL.openConnection();
                 conn.setRequestMethod("POST");
                 conn.setReadTimeout(5000);
                 conn.setConnectTimeout(10000);
                 conn.setDoOutput(true);
                 String data = content;
                 OutputStream out = conn.getOutputStream();
                 out.write(data.getBytes());
                 out.flush();
                 out.close();
                 int responseCode = conn.getResponseCode();
                 if(responseCode == 200) {
                     InputStream is = conn.getInputStream();
                     String response = getStringFromInputStream(is);
                     return response;
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } finally {
             if(conn != null) {
                 conn.disconnect();
             }
         }
           return null;
     }
    public static String get(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            try {
                conn = (HttpURLConnection)mURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(10000);
                int responseCode = conn.getResponseCode();
                if(responseCode == 200) {
                    InputStream is = conn.getInputStream();
                    String response = getStringFromInputStream(is);
                    return response;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
    public static String getStringFromInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = is.read(buffer)) != -1) {
            os.write(buffer,0,len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
    }

    public static void postRequest(String url,Map<String,String> params,String encode,OnRespondrListner listner) {
        StringBuilder sb = new StringBuilder();
        if(params != null && !params.isEmpty()){
            for (Map.Entry<String,String> entry: params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        if(listner != null) {
            try {
                URL path = new URL(url);
                if(path != null) {
                    HttpURLConnection con = (HttpURLConnection)path.openConnection();
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(3000);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    byte[] bytes = sb.toString().getBytes();
                    OutputStream out = con.getOutputStream();
                    out.write(bytes);
                    out.close();
                    if(con.getResponseCode() == 200) {
                        onSuccessRespond(encode,listner,con);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(listner,e);
            }
        }
     }

    private static void onError(OnRespondrListner listner,Exception onError) {
        listner.onError(onError.toString());
    }

    private static void onSuccessRespond(String encode,OnRespondrListner listner,HttpURLConnection con) {
        try {
            InputStream is = con.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            if(is != null) {
                while((len = is.read(bytes)) != -1) {
                    baos.write(bytes,0,len);
                }
                String str = new String(baos.toByteArray(),encode);
                listner.onSuccess(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnRespondrListner {
        void onSuccess(String response);
        void onError(String error);
    }

    public static String getDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }
}
