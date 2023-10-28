package app.dpapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import app.dpapp.R;

public class DialogShowUtil {

    public static void dialogShow(Context context,String messge){
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(context);
        alterDiaglog.setIcon(R.drawable.hint);//图标
        alterDiaglog.setTitle("提示信息");//文字
        alterDiaglog.setMessage(messge);
        alterDiaglog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alterDiaglog.show();
    }
}
