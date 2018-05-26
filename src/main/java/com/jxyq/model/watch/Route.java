package com.jxyq.model.watch;

public class Route {
    private String table_name;
    private int id;
    private int user_id;
    private Double lon;
    private Double lat;
    private Double lon_of;
    private Double lat_of;
    private int user_pen_id;
    private String user_pen_flag;
    private String sub_time;
    private int sub_mode;
    private String sub_number;
    private String address;
    private String county;
    private String pic_url;
    private int type;
    private String display;

    public Route(){}

    public Route(int id, int user_id, Double lon, Double lat, Double lon_of,
                 Double lat_of, int user_pen_id, String user_pen_flag, String sub_time, int sub_mode,
                 String sub_number, String address, String county, String pic_url, int type,
                 String display) {
        this.id = id;
        this.user_id = user_id;
        this.lon = lon;
        this.lat = lat;
        this.lon_of = lon_of;
        this.lat_of = lat_of;
        this.user_pen_id = user_pen_id;
        this.user_pen_flag = user_pen_flag;
        this.sub_time = sub_time;
        this.sub_mode = sub_mode;
        this.sub_number = sub_number;
        this.address = address;
        this.county = county;
        this.pic_url = pic_url;
        this.type = type;
        this.display = display;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
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

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon_of() {
        return lon_of;
    }

    public void setLon_of(Double lon_of) {
        this.lon_of = lon_of;
    }

    public Double getLat_of() {
        return lat_of;
    }

    public void setLat_of(Double lat_of) {
        this.lat_of = lat_of;
    }

    public int getUser_pen_id() {
        return user_pen_id;
    }

    public void setUser_pen_id(int user_pen_id) {
        this.user_pen_id = user_pen_id;
    }

    public String getUser_pen_flag() {
        return user_pen_flag;
    }

    public void setUser_pen_flag(String user_pen_flag) {
        this.user_pen_flag = user_pen_flag;
    }

    public String getSub_time() {
        return sub_time;
    }

    public void setSub_time(String sub_time) {
        this.sub_time = sub_time;
    }

    public int getSub_mode() {
        return sub_mode;
    }

    public void setSub_mode(int sub_mode) {
        this.sub_mode = sub_mode;
    }

    public String getSub_number() {
        return sub_number;
    }

    public void setSub_number(String sub_number) {
        this.sub_number = sub_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
