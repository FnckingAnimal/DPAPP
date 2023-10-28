package app.dpapp.bean;

/**
 * Created by Administrator on 2020/10/14.
 */
public class ListnoSaveBean {
    public String listno;//发料单
    public String jiano; //物料架编码
    public String lotno; //批号
    public String productno;//料号
    public String qiety;  //数量
    public String stocklistno;//备料清单号

    @Override
    public String toString() {
        return listno+","+jiano+","+lotno+","+productno+","+qiety+","+stocklistno;
    }
}
