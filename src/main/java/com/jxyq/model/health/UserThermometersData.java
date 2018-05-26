package com.jxyq.model.health;

public class UserThermometersData extends UserHealthModel {
    private long thermometer_data_id;
    private Integer thermometer_value;

    public long getThermometer_data_id() {
        return thermometer_data_id;
    }

    public void setThermometer_data_id(long thermometer_data_id) {
        this.thermometer_data_id = thermometer_data_id;
    }

    public Integer getThermometer_value() {
        return thermometer_value;
    }

    public void setThermometer_value(Integer thermometer_value) {
        this.thermometer_value = thermometer_value;
    }
}
