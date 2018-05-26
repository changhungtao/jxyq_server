package com.jxyq.model.health;

public class UserOximeterData extends UserHealthModel {
    private long oximeter_data_id;
    private Integer oximeter_value;

    public long getOximeter_data_id() {
        return oximeter_data_id;
    }

    public void setOximeter_data_id(long oximeter_data_id) {
        this.oximeter_data_id = oximeter_data_id;
    }

    public Integer getOximeter_value() {
        return oximeter_value;
    }

    public void setOximeter_value(Integer oximeter_value) {
        this.oximeter_value = oximeter_value;
    }
}
