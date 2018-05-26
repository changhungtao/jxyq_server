package com.jxyq.model.health;

public class PatSphygmomanometerFile extends PatientHealthModel {
    private Long sphygmomanometer_file_id;
    private Integer systolic_pressure;
    private Integer diastolic_pressure;
    private Integer heart_rate;

    public Long getSphygmomanometer_file_id() {
        return sphygmomanometer_file_id;
    }

    public void setSphygmomanometer_file_id(Long sphygmomanometer_file_id) {
        this.sphygmomanometer_file_id = sphygmomanometer_file_id;
    }

    public Integer getSystolic_pressure() {
        return systolic_pressure;
    }

    public void setSystolic_pressure(Integer systolic_pressure) {
        this.systolic_pressure = systolic_pressure;
    }

    public Integer getDiastolic_pressure() {
        return diastolic_pressure;
    }

    public void setDiastolic_pressure(Integer diastolic_pressure) {
        this.diastolic_pressure = diastolic_pressure;
    }

    public Integer getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(Integer heart_rate) {
        this.heart_rate = heart_rate;
    }
}
