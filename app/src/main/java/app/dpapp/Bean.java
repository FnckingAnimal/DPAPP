package app.dpapp;
/**
 * Owner:S7202916
 * CreateDate:2017/7/18 08:26
 */
public class Bean {
    private String CREATEID;
    private String CREATETIME;
    private String MACHINENOID;
    private String LOTNO;
    private String OUTPUT;
    private String TIMESLOT;
    private String SYSID;

    public String getSYSID() {
        return SYSID;
    }

    public void setSYSID(String SYSID) {
        this.SYSID = SYSID;
    }

    public String getFLOWID() {
        return FLOWID;
    }

    public void setFLOWID(String FLOWID) {
        this.FLOWID = FLOWID;
    }

    private String FLOWID;

    public String getCREATEID() {
        return CREATEID;
    }

    public void setCREATEID(String CREATEID) {
        this.CREATEID = CREATEID;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public String getMACHINENOID() {
        return MACHINENOID;
    }

    public void setMACHINENOID(String MACHINENOID) {
        this.MACHINENOID = MACHINENOID;
    }

    public String getLOTNO() {
        return LOTNO;
    }

    public void setLOTNO(String LOTNO) {
        this.LOTNO = LOTNO;
    }

    public String getOUTPUT() {
        return OUTPUT;
    }

    public void setOUTPUT(String OUTPUT) {
        this.OUTPUT = OUTPUT;
    }

    public String getTIMESLOT() {
        return TIMESLOT;
    }

    public void setTIMESLOT(String TIMESLOT) {
        this.TIMESLOT = TIMESLOT;
    }

    public String getDATES() {
        return DATES;
    }

    public void setDATES(String DATES) {
        this.DATES = DATES;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getDEVICENO() {
        return DEVICENO;
    }

    public void setDEVICENO(String DEVICENO) {
        this.DEVICENO = DEVICENO;
    }

    private String DATES;
    private String MODEL;
    private String DEVICENO;

}
