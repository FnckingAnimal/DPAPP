package app.dpapp.bean;

/**
 * Created by Administrator on 2021/1/6.
 */
public class MaterialLotnoBean {
    private String lotno;
    private String materialno;
    private String materialname;
    private String deviceno;
    private String bh;
    private String qty;
    private String typeflag;
    private String lotnostatus;

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getMaterialno() {
        return materialno;
    }

    public void setMaterialno(String materialno) {
        this.materialno = materialno;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getTypeflag() {
        return typeflag;
    }

    public void setTypeflag(String typeflag) {
        this.typeflag = typeflag;
    }

    public String getLotnostatus() {
        return lotnostatus;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setLotnostatus(String lotnostatus) {
        this.lotnostatus = lotnostatus;
    }
}
