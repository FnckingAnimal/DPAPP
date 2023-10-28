package app.dpapp.appcdl;

import android.content.Context;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import app.dpapp.parameterclass.httprequestinputdata;

/**
 * Created by Administrator on 2016/7/1.
 */
public class httprequestforsys implements  Runnable {
    final private static String ENCODING = "UTF-8";
    URL url=null;
    private Context context;
    /** http访问结果监听器 */
    private FreedomHttpListener listener;
    /** 访问链接 */
    HttpURLConnection conn = null;
    /** 当前访问线程 */
    //private Thread currentRequest = null;
    /** 拿到的流 */
    InputStream input = null;

    public static final int GET_MOTHOD = 1;
    //private static final int Time = 40 * 1000;
    private static final int Time = 100 * 1000;
    public static final int POST_MOTHOD = 2;
    public static final int POSTIMAGE_MOTHOD = 3;
    private   Object postdata=null;
    //private Map<String, String> postparams=null;
    private  File file;

    /**
     * 1： get请求 2： post请求 3:post image 4:post webapi
     */
    private int requestStatus = 1;

    public httprequestforsys(Context mContext, FreedomHttpListener listener,
                               int mRequeststatus,File sendfile) {
        this.context = mContext;
        this.requestStatus = mRequeststatus;
        this.listener = listener;
        if(sendfile!=null) {
            this.file = sendfile;
        }
    }

    public void seturl(String urlpath)
    {

        try {
            url = new URL(urlpath);
        }
        catch(MalformedURLException me1)
        {
            url=null;
        }
    }

    public void setpostdata(List<httprequestinputdata> sendli)
    {
        String rstring="";
        Map<String, String> map = new HashMap<String, String>();

        if(sendli==null)
        {
            return;
        }


        for(int i=0;i<sendli.size();i++)
        {
           // sendli.get(i).dataname+"="+sendli.get(i).datavalue;
            if(sendli.size()-1==i)
            {
            if(requestStatus==1) {
                rstring = rstring + sendli.get(i).getDataname() + "=" + sendli.get(i).getDatavalue();
            }
                if(requestStatus==2 || requestStatus==3) {
                    map.put( sendli.get(i).getDataname(), sendli.get(i).getDatavalue());
                }
            }
            else {
                if(requestStatus==1) {
                rstring = rstring + sendli.get(i).getDataname() + "=" + sendli.get(i).getDatavalue() + "&";
            }
                if(requestStatus==2 || requestStatus==3) {
                    map.put( sendli.get(i).getDataname(), sendli.get(i).getDatavalue());
                }
            }
        }
        if(requestStatus==1) {
            postdata = rstring;
        }
        if(requestStatus==2|| requestStatus==3) {
            postdata = map;
        }
        //return rstring;
    }

    /**
     * @Title: postRequest
     * @Description:Post请求触发
     * @throws
     */
    public void postRequest(String sendurlpath,List<httprequestinputdata> sendli) {
       requestStatus=2;
        setpostdata(sendli);
        seturl(sendurlpath);
        //requestStatus = 2;
        MyThreadPool.httppool.execute(this);

       // currentRequest = new Thread(this);
       // currentRequest.start();
    }

    public void postRequest_webapi(String sendurlpath,String postdata) {
        requestStatus=4;
        this.postdata=postdata;
        seturl(sendurlpath);
        //requestStatus = 2;
        MyThreadPool.httppool.execute(this);
        // currentRequest = new Thread(this);
        // currentRequest.start();
    }
    /**
     * @Title: postRequest
     * @Description:Post请求触发
     * @throws
     */
    public void postimageRequest(String sendurlpath,List<httprequestinputdata> sendli) {
        requestStatus=3;

        setpostdata(sendli);
        seturl(sendurlpath);
       // requestStatus = 3;
        MyThreadPool.httppool.execute(this);

        // currentRequest = new Thread(this);
        // currentRequest.start();
    }
    /**
     * @Title: getRequeest
     * @Description:GET请求触发
     * @throws
     */
    public void getRequeest(String sendurlpath,List<httprequestinputdata> sendli) {
        requestStatus=1;

        setpostdata(sendli);
        seturl(sendurlpath);
        //Log.v("CMSF", sendli.toString()+"-"+postdata);


        MyThreadPool.httppool.execute(this);
        //Log.v("CMSF", "GR-"+sendurlpath);
        //currentRequest = new Thread(this);
        //currentRequest.start();
    }

    /**
     * @Title: sendGetRequest
     * @Description: 发送get请求
     * @throws
     */
    private void sendGetRequest() {
        try {
            url=new URL(url.toString()+"?"+postdata.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(Time);
            conn.setReadTimeout(Time);
            int responseCode = conn.getResponseCode();
            Log.v("CMSF",url.toString()+"?"+postdata);
            Log.v("CMSF","RB-"+String.valueOf(responseCode));
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }

            } else {
                listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        }
    }


    /**
     * @Title: sendPostimageRequest
     * @Description: 发送post请求
     * @throws
     */
    private void sendPostRequestImage() {
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setChunkedStreamingMode(128 * 1024);// 128K
            conn.setConnectTimeout(Time);
            conn.setReadTimeout(Time);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不使用Cache

            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", ENCODING);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=8d3b14adc385c8b");
            //conn.setRequestProperty("Content-Type", "MULTIPART_FROM_DATA; boundary=8d3b14adc385c8b");

            // 使用POST方法
            String BOUNDARY="8d3b14adc385c8b";
            String PREFIX = "--";
            String LINE_END = "\r\n";

            StringBuilder stringBuilder = new StringBuilder();
            if ( ((Map<String,String>)postdata) != null && ! ((Map<String,String>)postdata).isEmpty()) {
                for (Map.Entry<String, String> entry :  ((Map<String,String>)postdata).entrySet()) {
                        stringBuilder.append(PREFIX);
                        stringBuilder.append(BOUNDARY);
                        stringBuilder.append(LINE_END);
                        stringBuilder.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                        //stringBuilder.append("Content-Type: text/plain; charset=" + ENCODING + LINE_END);
                        //stringBuilder.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                        stringBuilder.append(LINE_END);
                        stringBuilder.append(entry.getValue());
                        stringBuilder.append(LINE_END);
                }
            }
            if (file != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"imagename\";filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset= "+ENCODING + LINE_END);
                sb.append(LINE_END);

                StringBuffer se = new StringBuffer();
                se.append(LINE_END+PREFIX + BOUNDARY + PREFIX+LINE_END);

                FileInputStream is=new FileInputStream(file);
                byte[] bytes = new byte[1024];

                DataOutputStream outStream = new DataOutputStream(
                        conn.getOutputStream());
                outStream.write(stringBuilder.toString().getBytes());
                outStream.write(sb.toString().getBytes());
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    outStream.write(bytes, 0, len);
                }
                is.close();

                //outStream.write(stringBuilder.toString().getBytes());

                outStream.write(se.toString().getBytes());

                outStream.flush();
                outStream.close();

            }
            if (conn == null) {
                return;
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }
            } else if (responseCode == 404) {
                input = conn.getErrorStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                } else {
                    listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR,
                            null);
                }
            } else {
                listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        }
    }

    /**
     * @Title: sendPostimageRequest
     * @Description: 发送post请求
     * @throws
     */
    private void sendPostRequestImage_b() {
        try {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "******";
          conn = (HttpURLConnection) url
                    .openConnection();
            conn.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            // 使用POST方法
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            // 使用POST方法

            if (file != null) {


                DataOutputStream dos = new DataOutputStream(
                    conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"imagename\"; filename=\""
                    + file.getName() + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取<strong>文件</strong>
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            }
            if (conn == null) {
                return;
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }
            } else if (responseCode == 404) {
                input = conn.getErrorStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                } else {
                    listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR,
                            null);
                }
            } else {
                listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        }
    }


    private void sendPostRequest_webapi() {
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(Time);
            conn.setReadTimeout(Time);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不使用Cache
            conn.setRequestProperty("Charset", ENCODING);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("=");
            stringBuilder.append(postdata.toString());

                conn.setRequestProperty("Content-Length",
                        String.valueOf(stringBuilder.toString().getBytes().length));
                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    DataOutputStream outStream = new DataOutputStream(
                        conn.getOutputStream());

            outStream.write(stringBuilder.toString().getBytes(), 0, stringBuilder.toString().getBytes().length);
                    //outStream.write(stringBuilder.toString().getBytes());
            outStream.flush();
            outStream.close();


            if (conn == null) {
                return;
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }
            } else if (responseCode == 404) {
                input = conn.getErrorStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                } else {
                    listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR,
                            null);
                }
            } else {
                listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        }
    }

    private void sendPostRequest() {
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(Time);
            conn.setReadTimeout(Time);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不使用Cache
            conn.setRequestProperty("Charset", ENCODING);

            StringBuilder stringBuilder = new StringBuilder();
            if ( ((Map<String,String>)postdata) != null && ! ((Map<String,String>)postdata).isEmpty()) {
                for (Map.Entry<String, String> entry :  ((Map<String,String>)postdata).entrySet()) {

                    try {
                        stringBuilder
                                .append(entry.getKey())
                                .append("=")
                                        //.append(URLEncoder.encode(entry.getValue(), encode))
                                .append(URLEncoder.encode(entry.getValue(), ENCODING))
                                .append("&");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            if(((Map<String,String>)postdata)==null)
            {
                conn.setRequestProperty("Content-Length",
                        "0");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

            }
            else {
                conn.setRequestProperty("Content-Length",
                        String.valueOf(stringBuilder.toString().getBytes().length));
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                DataOutputStream outStream = new DataOutputStream(
                        conn.getOutputStream());
                outStream.write(stringBuilder.toString().getBytes(), 0, stringBuilder.toString().getBytes().length);
                //outStream.write(stringBuilder.toString().getBytes());
                outStream.flush();
                outStream.close();
            }

            if (conn == null) {
                return;
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }
            } else if (responseCode == 404) {
                input = conn.getErrorStream();
                if (input != null) {
                    listener.action(FreedomHttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                } else {
                    listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR,
                            null);
                }
            } else {
                listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(FreedomHttpListener.EVENT_NETWORD_EEEOR, null);
        }
    }
    /**
     * 读取数据
     *
     * @param inStream
     *            输入流
     * @return
     * @throws Exception
     */
    private Object readStream(InputStream inStream) throws Exception {
        String result;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        result = new String(outStream.toByteArray(), ENCODING);
        outStream.close();
        inStream.close();
        return result;
    }

    /**
     * 对请求的字符串进行编码
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String requestEncodeStr(String requestStr)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(requestStr, ENCODING);

    }

    /**
     * @Title: isRunning
     * @Description: 判断是否正在访问
     * @return
     * @throws
     */
    public boolean isRunning() {
        if(MyThreadPool.httppool!=null&&MyThreadPool.httppool.isShutdown()==false)
        {
            return true;
        }
        /*
        if (currentRequest != null && currentRequest.isAlive()) {
            return true;
        }
        */
        return false;
    }

    /**
     * 取消当前HTTP连接处理
     */
    public void cancelHttpRequest() {
        if(MyThreadPool.httppool!=null&&MyThreadPool.httppool.isShutdown()==false)
        {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            input = null;
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn = null;
            MyThreadPool.httppool.shutdown();
            System.gc();
        }
        /*
        if (currentRequest != null && currentRequest.isAlive()) {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            input = null;
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn = null;
            currentRequest = null;
            System.gc();
        }
        */
    }

    @Override
    public void run() {
        // 判断是否有网络
        boolean netType =  NetUtils.checkNetWork(context);
        if (netType) {
            switch (requestStatus)
            {
                case 1:
                    sendGetRequest();
                    break;
                case 2:
                    sendPostRequest();
                    break;
                case 3:
                    if(file!=null) {
                        sendPostRequestImage();
                        //sendPostRequestImage_b();
                    }
                    else
                    {
                        listener.action(FreedomHttpListener.EVENT_NONOIMAGEFILA,null);
                    }
                    break;
                //Post WebAPI
                case 4:
                    sendPostRequest_webapi();
                    break;
            }
           /* if (requestStatus == 1) {
                //Log.v("CMSF","RUN-1");
                sendGetRequest();
            } else if (requestStatus == 2) {
                sendPostRequest();
            }
            else if(requestStatus == 3)
            {
                if(file!=null) {
                    sendPostRequestImage();
                    //sendPostRequestImage_b();
                }
                else
                {
                    listener.action(FreedomHttpListener.EVENT_NONOIMAGEFILA,null);
                }
            }*/
        } else {
            listener.action(FreedomHttpListener.EVENT_NOT_NETWORD, null);
        }
    }
}




