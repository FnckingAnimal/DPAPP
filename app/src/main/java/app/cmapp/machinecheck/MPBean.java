package app.cmapp.machinecheck;

import java.io.Serializable;

/**
 * Created by S7187445 on 2018/5/31.
 */
public class MPBean implements Serializable {

    private String MACHINENO;
    private String CHECKTIME;
    private String ISEND;
    private String LINENAME;
    private String OPNAME;
    private String CHECKDATAID;
    private String MACHINESYSID;
    private String UPDATEDATE;
    private String FILEVERSION;
    private String PUBLISHDATE;

    public String getMACHINENO() {
        return MACHINENO;
    }

    public void setMACHINENO(String MACHINENO) {
        this.MACHINENO = MACHINENO;
    }

    public String getCHECKTIME() {
        return CHECKTIME;
    }

    public void setCHECKTIME(String CHECKTIME) {
        this.CHECKTIME = CHECKTIME;
    }

    public String getISEND() {
        return ISEND;
    }

    public void setISEND(String ISEND) {
        this.ISEND = ISEND;
    }

    public String getLINENAME() {
        return LINENAME;
    }

    public void setLINENAME(String LINENAME) {
        this.LINENAME = LINENAME;
    }

    public String getOPNAME() {
        return OPNAME;
    }

    public void setOPNAME(String OPNAME) {
        this.OPNAME = OPNAME;
    }

    public String getCHECKDATAID() {
        return CHECKDATAID;
    }

    public void setCHECKDATAID(String CHECKDATAID) {
        this.CHECKDATAID = CHECKDATAID;
    }

    public String getMACHINESYSID() {
        return MACHINESYSID;
    }

    public void setMACHINESYSID(String MACHINESYSID) {
        this.MACHINESYSID = MACHINESYSID;
    }

    public String getUPDATEDATE() {
        return UPDATEDATE;
    }

    public void setUPDATEDATE(String UPDATEDATE) {
        this.UPDATEDATE = UPDATEDATE;
    }

    public String getFILEVERSION() {
        return FILEVERSION;
    }

    public void setFILEVERSION(String FILEVERSION) {
        this.FILEVERSION = FILEVERSION;
    }

    public String getPUBLISHDATE() {
        return PUBLISHDATE;
    }

    public void setPUBLISHDATE(String PUBLISHDATE) {
        this.PUBLISHDATE = PUBLISHDATE;
    }
}
