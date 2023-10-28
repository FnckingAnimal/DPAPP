package app.cmapp.CMSFCrash;

import android.app.Application;
/**
 * Created by Administrator on 2016/7/4 by tod
 */
public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
