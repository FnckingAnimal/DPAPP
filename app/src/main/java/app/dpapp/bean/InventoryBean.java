package app.dpapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create by TPP at 2021/6/11 下午 02:50
 * Email:15701656981@163.com
 */
public class InventoryBean implements Serializable {
    private String strMsg;
    private boolean blRes;
    private List<TResult> TResult;

    public String getStrMsg() {
        return strMsg;
    }

    public void setStrMsg(String strMsg) {
        this.strMsg = strMsg;
    }

    public boolean getBlRes() {
        return blRes;
    }

    public void setBlRes(boolean blRes) {
        this.blRes = blRes;
    }

    public List<TResult> getTResult() {
        return TResult;
    }

    public void setTResult(List<TResult> TResult) {
        this.TResult = TResult;
    }
}
