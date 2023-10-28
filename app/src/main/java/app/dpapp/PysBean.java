package app.dpapp;

/**
 * Created by S7187445 on 2018/1/29.
 */
public class PysBean {

    private String USERID;
    private String USERNAME;
    private String GROUPNAME;
    private String SHIFT;
    private String OPNO;

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public void setGROUPNAME(String GROUPNAME) {
        this.GROUPNAME = GROUPNAME;
    }

    public void setSHIFT(String SHIFT) {
        this.SHIFT = SHIFT;
    }

    public void setOPNO(String OPNO) {
        this.OPNO = OPNO;
    }

    public void setEITEM8(String EITEM8) {
        this.EITEM8 = EITEM8;
    }

    public String getUSERID() {
        return USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getGROUPNAME() {
        return GROUPNAME;
    }

    public String getSHIFT() {
        return SHIFT;
    }

    public String getOPNO() {
        return OPNO;
    }

    public String getEITEM8() {
        return EITEM8;
    }

    private String EITEM8;
}
