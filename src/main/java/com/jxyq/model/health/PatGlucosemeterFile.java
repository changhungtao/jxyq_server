package com.jxyq.model.health;

public class PatGlucosemeterFile extends PatientHealthModel {
    private Long glucosemeter_file_id;
    private Integer glucosemeter_value;
    private Integer period;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getGlucosemeter_file_id() {
        return glucosemeter_file_id;
    }

    public void setGlucosemeter_file_id(Long glucosemeter_file_id) {
        this.glucosemeter_file_id = glucosemeter_file_id;
    }

    public Integer getGlucosemeter_value() {
        return glucosemeter_value;
    }

    public void setGlucosemeter_value(Integer glucosemeter_value) {
        this.glucosemeter_value = glucosemeter_value;
    }
}
