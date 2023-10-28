package app.dpapp.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/7/5.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent newIntent = new Intent(context, Scanimage.class);
            context.startService(newIntent);
        }
    }
}