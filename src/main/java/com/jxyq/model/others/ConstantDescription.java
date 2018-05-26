package com.jxyq.model.others;

public class ConstantDescription {
    private int constant_description_id;
    private String catagory;
    private int constant;
    private String description;
    private String extra;

    public int getConstant_description_id() {
        return constant_description_id;
    }

    public void setConstant_description_id(int constant_description_id) {
        this.constant_description_id = constant_description_id;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
