package com.jxyq.model.health;

/**
 * Created by wujj-fnst on 2015/4/28.
 */
public class UserFatData extends UserHealthModel {
    private long fat_data_id;
    private int bmi_value;
    private int fat_value;
    private int calorie_value;
    private int moisture_value;
    private int muscle_value;
    private int visceral_fat_value;
    private int bone_value;
    private int weight_value;

    public int getWeight_value() {
        return weight_value;
    }

    public void setWeight_value(int weight_value) {
        this.weight_value = weight_value;
    }

    public long getFat_data_id() {
        return fat_data_id;
    }

    public void setFat_data_id(long fat_data_id) {
        this.fat_data_id = fat_data_id;
    }

    public int getBmi_value() {
        return bmi_value;
    }

    public void setBmi_value(int bmi_value) {
        this.bmi_value = bmi_value;
    }

    public int getFat_value() {
        return fat_value;
    }

    public void setFat_value(int fat_value) {
        this.fat_value = fat_value;
    }

    public int getCalorie_value() {
        return calorie_value;
    }

    public void setCalorie_value(int calorie_value) {
        this.calorie_value = calorie_value;
    }

    public int getMoisture_value() {
        return moisture_value;
    }

    public void setMoisture_value(int moisture_value) {
        this.moisture_value = moisture_value;
    }

    public int getMuscle_value() {
        return muscle_value;
    }

    public void setMuscle_value(int muscle_value) {
        this.muscle_value = muscle_value;
    }

    public int getVisceral_fat_value() {
        return visceral_fat_value;
    }

    public void setVisceral_fat_value(int visceral_fat_value) {
        this.visceral_fat_value = visceral_fat_value;
    }

    public int getBone_value() {
        return bone_value;
    }

    public void setBone_value(int bone_value) {
        this.bone_value = bone_value;
    }
}
