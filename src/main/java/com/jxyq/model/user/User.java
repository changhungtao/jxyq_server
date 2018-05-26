package com.jxyq.model.user;

import com.jxyq.model.Role;

import java.util.List;

public class User {
    public static Integer DEFAULT_POINTS = 300;
    public static Integer PASS_POINTS = 100;
    private int user_id;
    private String phone;
    private String password;
    private String email;
    private String nick_name;
    private String full_name;
    private String avatar_url;
    private String gender;
    private String birthday;
    private Integer height;
    private Integer weight;
    private Integer target_weight;
    private String address;
    private Integer district_id;
    private Integer province_id;
    private Integer city_id;
    private Integer zone_id;
    private String qq;
    private Integer points;
    private Long registered_at;
    private Long updated_at;
    private Integer status;
    private List<Role> roleList;
    private Integer authenticity;
    private Long filled_in_at;
    private Long last_measured_at;
    private Integer online_status;

    private Long expires;


    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(Long registered_at) {
        this.registered_at = registered_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Integer getTarget_weight() {
        return target_weight;
    }

    public void setTarget_weight(Integer target_weight) {
        this.target_weight = target_weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Integer getProvince_id() {
        return province_id;
    }

    public void setProvince_id(Integer province_id) {
        this.province_id = province_id;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public Integer getZone_id() {
        return zone_id;
    }

    public void setZone_id(Integer zone_id) {
        this.zone_id = zone_id;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
        this.district_id = district_id;
    }

    public Integer getAuthenticity() {
        return authenticity;
    }

    public void setAuthenticity(Integer authenticity) {
        this.authenticity = authenticity;
    }

    public Long getFilled_in_at() {
        return filled_in_at;
    }

    public void setFilled_in_at(Long filled_in_at) {
        this.filled_in_at = filled_in_at;
    }

    public Long getLast_measured_at() {
        return last_measured_at;
    }

    public void setLast_measured_at(Long last_measured_at) {
        this.last_measured_at = last_measured_at;
    }

    public Integer getOnline_status() {
        return online_status;
    }

    public void setOnline_status(Integer online_status) {
        this.online_status = online_status;
    }
}
