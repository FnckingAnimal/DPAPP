package app.cmapp.appcdl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by Tod on 2016/6/18.
 */
public  class MyThreadPool {

    public static ExecutorService pool = Executors.newSingleThreadExecutor();
    public static ExecutorService httppool = Executors.newSingleThreadExecutor();

}
