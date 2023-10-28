package app.cmapp.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.Toast;

/**
 * Created by F5460007 on 2016/11/15.
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                //Log.e("H3c", "isConnected" + isConnected);
                if (isConnected) {
//                    if(!(networkInfo.getExtraInfo().toString().equals("\"300301\"")||networkInfo.getExtraInfo().toString().equals("\"400402\"")||networkInfo.getExtraInfo().toString().equals("\"4h3402\"")))
//                    {
//                        Toast.makeText(context,"Unauthorized WiFi,about to close!",Toast.LENGTH_SHORT).show();
//                        ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).disconnect();disconnect
//                    }
                }
            }
        }

    }
}
