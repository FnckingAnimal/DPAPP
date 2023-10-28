package app.dpapp.gsonbean;

import java.io.Serializable;

public class FixtureBean implements Serializable {
    private String code;
    private String msg;
    private Data Data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data data) {
        Data = data;
    }
}
