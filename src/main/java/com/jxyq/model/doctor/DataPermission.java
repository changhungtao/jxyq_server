package com.jxyq.model.doctor;

public class DataPermission {
    private int data_permission_id;
    private String name;
    private int district_id;
    private int province_id;
    private int city_id;
    private int zone_id;
    private int data_type;

    public int getData_permission_id() {
        return data_permission_id;
    }

    public void setData_permission_id(int data_permission_id) {
        this.data_permission_id = data_permission_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }
}
