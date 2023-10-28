package app.dpapp.Interface;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by F5460007 on 2016/12/26.
 */
public interface Iobjectrhandler<T> {
    void execobject(T parmerobject,Object o);
    class BaseHandler extends Handler
    {
        private Iobjectrhandler _ih;
        private int _i;
        private Context _c;
        public BaseHandler(Iobjectrhandler ih,int i,Context c )
        {
            this._ih=ih;
            this._i=i;
            this._c=c;
        }
        public void handleMessage(Message msg)
        {
            try {
                switch (msg.what) {
                    //Success
                    case 0:
                        _ih.execobject(_i, msg.obj);
                        break;
                    //Fail
                    case 1:
                        Toast.makeText(_c, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            catch (Exception e)
            {
                Toast.makeText(_c, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
