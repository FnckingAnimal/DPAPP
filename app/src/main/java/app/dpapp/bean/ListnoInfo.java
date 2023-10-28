package app.dpapp.bean;

/**
 * Created by Administrator on 2020/9/18.
 */
public class ListnoInfo {
    public String wono;
    public String listno;
    public String productno;
    public String diety;

    @Override
    public String toString() {
        return wono+";"+listno+";"+productno+";"+diety;
    }
}
