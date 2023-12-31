package asmack.org.jivesoftware.smack;

import asmack.org.jivesoftware.smack.util.DNSUtil;
import asmack.org.jivesoftware.smack.util.dns.DNSJavaResolver;
import asmack.org.jivesoftware.smackx.ConfigureProviderManager;
import asmack.org.jivesoftware.smackx.InitStaticCode;
import asmack.org.xbill.DNS.ResolverConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SmackAndroid {
    private static SmackAndroid sSmackAndroid = null;

    private BroadcastReceiver mConnectivityChangedReceiver;
    private Context mCtx;

    private SmackAndroid(Context ctx) {
        mCtx = ctx;
        DNSUtil.setDNSResolver(DNSJavaResolver.getInstance());
        InitStaticCode.initStaticCode(ctx);
        ConfigureProviderManager.configureProviderManager();
        maybeRegisterReceiver();
    }

    public static SmackAndroid init(Context ctx) {
        if (sSmackAndroid == null) {
            sSmackAndroid = new SmackAndroid(ctx);
        } else {
            sSmackAndroid.maybeRegisterReceiver();
        }
        return sSmackAndroid;
    }

    public void onDestroy() {
        if (mConnectivityChangedReceiver != null) {
            mCtx.unregisterReceiver(mConnectivityChangedReceiver);
            mConnectivityChangedReceiver = null;
        }
    }

    private void maybeRegisterReceiver() {
        if (mConnectivityChangedReceiver == null) {
            mConnectivityChangedReceiver = new ConnectivtyChangedReceiver();
            mCtx.registerReceiver(mConnectivityChangedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    class ConnectivtyChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ResolverConfig.refresh();
        }

    }
}
