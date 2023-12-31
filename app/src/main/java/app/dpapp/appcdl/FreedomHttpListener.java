package app.dpapp.appcdl;

/**
 * Created by Administrator on 2016/7/4.
 */
/**
 * @ClassName: FreedomHttpListener
 * @Description: 监听器
 */
public interface FreedomHttpListener {

    public static final int EVENT_BASE = 0x100;

    /**
     * 没有网络的信息提示
     * */
    public static final int EVENT_NOT_NETWORD = EVENT_BASE + 1;

    /**
     * 网络异常的信息提示
     * */
    public static final int EVENT_NETWORD_EEEOR = EVENT_BASE + 2;

    /**
     * 获取网络数据失败
     * */
    public static final int EVENT_GET_DATA_EEEOR = EVENT_BASE + 3;

    /**
     * 获取网络数据成功
     * */
    public static final int EVENT_GET_DATA_SUCCESS = EVENT_BASE + 4;
    /**
     * 获取网络数据成功
     * */
    public static final int EVENT_CLOSE_SOCKET = EVENT_BASE + 5;

    public static final int EVENT_NONOIMAGEFILA=EVENT_BASE+6;

    public void action(int actionCode, Object object);
}
