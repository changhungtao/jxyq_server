package com.jxyq.model.health;

public class UserSphygmomanometerData extends UserHealthModel {
    private int sphygmomanometer_data_id;
    private Integer systolic_pressure;
    private Integer diastolic_pressure;
    private Integer heart_rate;

    public Integer getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(Integer heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getSphygmomanometer_data_id() {
        return sphygmomanometer_data_id;
    }

    public void setSphygmomanometer_data_id(int sphygmomanometer_data_id) {
        this.sphygmomanometer_data_id = sphygmomanometer_data_id;
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
}
