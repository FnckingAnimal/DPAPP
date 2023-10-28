package app.cmapp.appcdl;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;

/**
 * Created by Administrator on 2016/6/20.
 */
public class execloadactivity  {
   // public static Intent itent;
    protected static  ProgressDialog dialog;
    public static void opendialog(Context con1,String str) {

        if(dialog!=null) {
            if (dialog.isShowing()) {
                return;
            }
        }
       dialog=new ProgressDialog(con1);


        dialog.setTitle(str);
        dialog.setMessage("请稍候...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                       @Override
                                       public void onCancel(DialogInterface dialog) {

                                       }
                                   }
         );
        dialog.show();
    }
    public static void canclediglog()
    {
        dialog.cancel();
    }
}
