package app.dpapp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/9/11.
 */
public class MaterialjiaBean implements Serializable{
    public String jiano; //物料架编码
    public String deviceno; //机种
    public String buildingno; //楼层
    public String cengshu; //层数
    public String productno; //料号
    public String qiety;  //数量
    public String cangma;  //仓码


    @Override
    public String toString() {
        return jiano+"--"+buildingno+"--"+deviceno+"--"+productno+"--"+qiety+"--"+cengshu;
    }
}
