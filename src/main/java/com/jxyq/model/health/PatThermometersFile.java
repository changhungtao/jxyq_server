package com.jxyq.model.health;

public class PatThermometersFile extends PatientHealthModel {
    private Long thermometer_file_id;
    private Integer thermometer_value;

    public Integer getThermometer_value() {
        return thermometer_value;
    }

    public void setThermometer_value(Integer thermometer_value) {
        this.thermometer_value = thermometer_value;
    }

    public Long getThermometer_file_id() {
        return thermometer_file_id;
    }

    public void setThermometer_file_id(Long thermometer_file_id) {
        this.thermometer_file_id = thermometer_file_id;
    }
}
