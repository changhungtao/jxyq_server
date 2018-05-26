package com.jxyq.model.watch;

public class PenSet {
    private int id;
    private int user_id;
    private String pen_name;
    private int pen_type;
    private Double center_lon;
    private Double center_lat;
    private Double center_lon_of;
    private Double center_lat_of;
    private Double bd_lon;
    private Double bd_lat;
    private int radius;
    private String rec_number;
    private String address1;
    private String country;
    private String update_time;
    private int mode;
    private int hexagon_id;
    private int deleted_flag;

    public String getPen_name() {
        return pen_name;
    }

    public void setPen_name(String pen_name) {
        this.pen_name = pen_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPen_type() {
        return pen_type;
    }

    public void setPen_type(int pen_type) {
        this.pen_type = pen_type;
    }

    public Double getCenter_lon() {
        return center_lon;
    }

    public void setCenter_lon(Double center_lon) {
        this.center_lon = center_lon;
    }

    public Double getCenter_lat() {
        return center_lat;
    }

    public void setCenter_lat(Double center_lat) {
        this.center_lat = center_lat;
    }

    public Double getCenter_lon_of() {
        return center_lon_of;
    }

    public void setCenter_lon_of(Double center_lon_of) {
        this.center_lon_of = center_lon_of;
    }

    public Double getCenter_lat_of() {
        return center_lat_of;
    }

    public void setCenter_lat_of(Double center_lat_of) {
        this.center_lat_of = center_lat_of;
    }

    public Double getBd_lon() {
        return bd_lon;
    }

    public void setBd_lon(Double bd_lon) {
        this.bd_lon = bd_lon;
    }

    public Double getBd_lat() {
        return bd_lat;
    }

    public void setBd_lat(Double bd_lat) {
        this.bd_lat = bd_lat;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getRec_number() {
        return rec_number;
    }

    public void setRec_number(String rec_number) {
        this.rec_number = rec_number;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getHexagon_id() {
        return hexagon_id;
    }

    public void setHexagon_id(int hexagon_id) {
        this.hexagon_id = hexagon_id;
    }

    public int getDeleted_flag() {
        return deleted_flag;
    }

    public void setDeleted_flag(int deleted_flag) {
        this.deleted_flag = deleted_flag;
    }
}
