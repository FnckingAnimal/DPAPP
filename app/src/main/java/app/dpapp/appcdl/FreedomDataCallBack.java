package app.dpapp.appcdl;

import android.content.Context;

/**
 * @ClassName: FreedomDataCallBack
 * @Description: 回调接口，处理返回数据
 * @param <T>
 */
public interface FreedomDataCallBack<T> {

    public abstract void onStart(Context C1);

    public abstract void processData(T paramObject, boolean paramBoolean);

    public abstract void onFinish(Context C1);

    public abstract void onFailed(T paramObject,Context C1);

    public abstract void onNoneImage(Context C1);
}