package app.dpapp.bean;

/**
 * Created by S7187445 on 2018/9/28.
 */
public class ScanMachineTwo {
    private String Resultflag;
    private Resultvalue1 Resultvalue;

    public Resultvalue1 getResultvalue() {
        return Resultvalue;
    }

    public void setResultvalue(Resultvalue1 resultvalue) {
        Resultvalue = resultvalue;
    }

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
