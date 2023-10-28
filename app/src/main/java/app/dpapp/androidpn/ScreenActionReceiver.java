package app.dpapp.androidpn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ScreenActionReceiver extends BroadcastReceiver {

    private boolean isRegisterReceiver = false;

    private NotificationService notificationService;

    public ScreenActionReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("lyh", "屏幕解锁广播(即亮屏了)...");
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null) {
                if (networkInfo.isConnected()) {
                    notificationService.connect();
                }
            } else {
                notificationService.disconnect();
            }

        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d("lyh", "屏幕加锁广播(即灭屏了)...");
        }
    }

    /**
     * 广播注册
     *
     * @param mContext 上下文对象
     */
    public void registerReceiver(Context mContext) {
        if (!isRegisterReceiver) {
            isRegisterReceiver = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            Log.i("lyh", "注册屏幕解锁、加锁广播接收者...");
            mContext.registerReceiver(ScreenActionReceiver.this, filter);
        }
    }

    /**
     * 广播注销
     *
     * @param mContext 上下文对象
     */
    public void unRegisterReceiver(Context mContext) {
        if (isRegisterReceiver) {
            isRegisterReceiver = false;
            Log.i("lyh", "注销屏幕解锁、加锁广播接收者...");
            mContext.unregisterReceiver(ScreenActionReceiver.this);
        }
    }

}
