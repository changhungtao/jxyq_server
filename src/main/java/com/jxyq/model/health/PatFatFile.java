package com.jxyq.model.health;

public class PatFatFile extends PatientHealthModel {
    private Long fat_file_id;
    private Integer bmi_value;
    private Integer fat_value;
    private Integer calorie_value;
    private Integer moisture_value;
    private Integer muscle_value;
    private Integer visceral_fat_value;
    private Integer bone_value;
    private Integer weight_value;

    public Long getFat_file_id() {
        return fat_file_id;
    }

    public void setFat_file_id(Long fat_file_id) {
        this.fat_file_id = fat_file_id;
    }

    public Integer getBmi_value() {
        return bmi_value;
    }

    public void setBmi_value(Integer bmi_value) {
        this.bmi_value = bmi_value;
    }

    public Integer getFat_value() {
        return fat_value;
    }

    public void setFat_value(Integer fat_value) {
        this.fat_value = fat_value;
    }

    public Integer getCalorie_value() {
        return calorie_value;
    }

    public void setCalorie_value(Integer calorie_value) {
        this.calorie_value = calorie_value;
    }

    public Integer getMoisture_value() {
        return moisture_value;
    }

    public void setMoisture_value(Integer moisture_value) {
        this.moisture_value = moisture_value;
    }

    public Integer getMuscle_value() {
        return muscle_value;
    }

    public void setMuscle_value(Integer muscle_value) {
        this.muscle_value = muscle_value;
    }

    public Integer getVisceral_fat_value() {
        return visceral_fat_value;
    }

    public void setVisceral_fat_value(Integer visceral_fat_value) {
        this.visceral_fat_value = visceral_fat_value;
    }

    public Integer getBone_value() {
        return bone_value;
    }

    public void setBone_value(Integer bone_value) {
        this.bone_value = bone_value;
    }

    public Integer getWeight_value() {
        return weight_value;
    }

    public void setWeight_value(Integer weight_value) {
        this.weight_value = weight_value;
    }
}
