package com.jxyq.model.health;

public class UserGlucosemeterData extends UserHealthModel {
    private long glucosemeter_data_id;
    private Integer period;
    private Integer glucosemeter_value;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public long getGlucosemeter_data_id() {
        return glucosemeter_data_id;
    }

    public void setGlucosemeter_data_id(long glucosemeter_data_id) {
        this.glucosemeter_data_id = glucosemeter_data_id;
    }

    public Integer getGlucosemeter_value() {
        return glucosemeter_value;
    }

    public void setGlucosemeter_value(Integer glucosemeter_value) {
        this.glucosemeter_value = glucosemeter_value;
    }
}
