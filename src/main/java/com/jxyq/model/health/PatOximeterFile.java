package com.jxyq.model.health;

public class PatOximeterFile extends PatientHealthModel {
    private Long oximeter_file_id;
    private Integer oximeter_value;

    public Long getOximeter_file_id() {
        return oximeter_file_id;
    }

    public void setOximeter_file_id(Long oximeter_file_id) {
        this.oximeter_file_id = oximeter_file_id;
    }

    public Integer getOximeter_value() {
        return oximeter_value;
    }

    public void setOximeter_value(Integer oximeter_value) {
        this.oximeter_value = oximeter_value;
    }
}
