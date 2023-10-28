package app.dpapp.bean;

/**
 * Created by S7202916 on 2018/9/27.
 */
public class ScanMachineBean {
    private String Resultflag;
    private Resultvalue Resultvalue;
    private String Status;
    private String Message;
    private String OtherValue;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getOtherValue() {
        return OtherValue;
    }

    public void setOtherValue(String otherValue) {
        OtherValue = otherValue;
    }

    public String getResultflag() {
        return Resultflag;
    }

    public void setResultflag(String resultflag) {
        Resultflag = resultflag;
    }

    public Resultvalue getResultvalue() {
        return Resultvalue;
    }

    public void setResultvalue(Resultvalue resultvalue) {
        Resultvalue = resultvalue;
    }

    @Override
    public String toString() {
        return "ScanMachineBean{" +
                "Status='" + Status + '\'' +
                ", Message='" + Message + '\'' +
                ", OtherValue='" + OtherValue + '\'' +
                ", Resultflag='" + Resultflag + '\'' +
                ", Resultvalue=" + Resultvalue +
                '}';
    }
}
