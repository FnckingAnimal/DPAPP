package app.cmapp.appcdl;/**
 * Created by F5460007 on 2017/3/3.
 */

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import app.cmapp.parameterclass.httprequestinputdata;

/**
 * Owner:F5460007/tod
 * CreateDate:2017/3/3 08:46
 * Description:派生類，增加請求Http功能，適應請求要求
 */
public class MyAppComPatActivity extends AppCompatActivity
{
    /**
     * Author:Tod
     * Descirpiton:Get or Post發送Http請求
     * @param urlpath  訪問的HttpPath
     * @param li  參數列表
     * @param sendmode 訪問模式 1：Get 2：Post
     * @param f  返回數據接口
     * @param SourceContext  請求頁面Context
     */
    public  void getkeyindatainput(String urlpath,List<httprequestinputdata> li,int sendmode,FreedomDataCallBack f,Context SourceContext) {
        exechttprequest hf1 = new exechttprequest();
        hf1.getDataFromServer(sendmode, f, SourceContext, urlpath, li, null);
    }
}
