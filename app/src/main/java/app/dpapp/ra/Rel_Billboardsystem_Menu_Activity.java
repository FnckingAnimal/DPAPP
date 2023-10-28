package app.dpapp.ra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import app.dpapp.R;


public class Rel_Billboardsystem_Menu_Activity extends Activity {

    private String itemFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel_billboardsystem_menu);
    }

    public void bt_Rel_Billboardsystem_checkin(View view) {
        Intent intent=new Intent(this, Rel_Billboardsystem_Checkin_Activity.class);
        startActivity(intent);
    }

    public void bt_Rel_Billboardsystem_checkout(View view) {
        Intent intent=new Intent(this,Rel_Billboardsystem_Checkout_Activity.class);
        startActivity(intent);
    }

    public void bt_Rel_Billboardsystem_record(View view) {
        Intent intent=new Intent(this,Rel_Billboardsystem_Record_Activity.class);
        startActivity(intent);
    }

}