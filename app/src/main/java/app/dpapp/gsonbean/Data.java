package app.dpapp.gsonbean;

import java.io.Serializable;

public class Data implements Serializable {
    private FixtureData data;

    public FixtureData getData() {
        return data;
    }

    public void setData(FixtureData data) {
        this.data = data;
    }
}
