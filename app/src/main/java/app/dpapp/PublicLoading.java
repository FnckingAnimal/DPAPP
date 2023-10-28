package app.dpapp;


import android.app.Activity;
import android.content.ServiceConnection;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/4/22.
 */
public class PublicLoading extends Activity {
    ServiceConnection mSc;
    public static PublicLoading instance = null;
    public static Boolean openflag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_loading);
        instance = this;
    }
}