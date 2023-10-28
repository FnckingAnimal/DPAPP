package app.dpapp.appcdl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.util.List;

import app.dpapp.parameterclass.httprequestinputdata;


/**
 * Created by Administrator on 2016/7/4.
 */

public  class exechttprequest
{

    public void getDataFromServer(int requestType,
                                  FreedomDataCallBack callBack, Context sourcontext, String Urlpath, List<httprequestinputdata> hi, File sendfile) {
        final BaseHandler handler = new BaseHandler(sourcontext,callBack);
        httprequestforsys httprequestforsys = new httprequestforsys(sourcontext,
                new FreedomHttpListener() {
                    @Override
                    public void action(int actionCode, Object object) {

                        Message msg = new Message();
                        switch (actionCode) {
                            case EVENT_NOT_NETWORD:
                                msg.what = EVENT_NOT_NETWORD;
                                break;

                            case EVENT_NETWORD_EEEOR:
                                msg.what = EVENT_NETWORD_EEEOR;
                                break;
                            case EVENT_CLOSE_SOCKET:
                                msg.what = EVENT_CLOSE_SOCKET;
                                break;

                            case EVENT_GET_DATA_EEEOR:
                                msg.what = EVENT_GET_DATA_EEEOR;
                                msg.obj = null;
                                break;
                            case EVENT_GET_DATA_SUCCESS:
                                msg.obj = object;
                                msg.what = EVENT_GET_DATA_SUCCESS;
                                break;
                            case EVENT_NONOIMAGEFILA:
                                msg.obj=object;
                                msg.what= EVENT_NONOIMAGEFILA;
                                break;
                            default:
                                break;
                        }
                        handler.sendMessage(msg);

                    }
                }, requestType,sendfile);
        callBack.onStart(sourcontext);
        // 选择不同的请求方法
        if (requestType == httprequestforsys.GET_MOTHOD) {
            httprequestforsys.getRequeest(Urlpath,hi);
        } else if (requestType == httprequestforsys.POST_MOTHOD) {
            httprequestforsys.postRequest(Urlpath,hi);
        }
        else if(requestType==httprequestforsys.POSTIMAGE_MOTHOD)
        {
            httprequestforsys.postimageRequest(Urlpath,hi);
        }

    }

    /**
     * @ClassName: BaseHandler
     * @Description: 消息处理器
     */
    private class BaseHandler extends Handler {
        private Context context;
        /** 事件回调接口处理 */
        private FreedomDataCallBack callBack;

        public BaseHandler(Context context, FreedomDataCallBack callBack) {
            this.context = context;
            this.callBack = callBack;
        }

        public void handleMessage(Message msg) {
            // 根据不同的结果触发不同的动作
            if (msg.what == FreedomHttpListener.EVENT_GET_DATA_SUCCESS) {
                if (msg.obj == null) {
                    callBack.onFailed(msg.obj,context);
                } else {
                    // 后台处理数据
                    callBack.processData(msg.obj, true);
                }
            } else if (msg.what == FreedomHttpListener.EVENT_NOT_NETWORD) {
                callBack.onFailed(msg.obj,context);
            } else if (msg.what == FreedomHttpListener.EVENT_NETWORD_EEEOR) {
                callBack.onFailed(msg.obj,context);

            } else if (msg.what == FreedomHttpListener.EVENT_GET_DATA_EEEOR) {
                callBack.onFailed(msg.obj,context);

            } else if (msg.what == FreedomHttpListener.EVENT_CLOSE_SOCKET) {
            }
            else if(msg.what==FreedomHttpListener.EVENT_NONOIMAGEFILA)
            {
                callBack.onNoneImage(context);
            }
            callBack.onFinish(context);
        }
    }

    //public abstract void callposthttprequest();
}
