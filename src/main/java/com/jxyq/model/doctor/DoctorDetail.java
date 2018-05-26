package com.jxyq.model.doctor;

import com.jxyq.model.Role;

import java.util.Date;
import java.util.List;

public class DoctorDetail {
    private long doctor_id;
    private String login_name;
    private String password;
    private String full_name;
    private String identification_number;
    private int gender;
    private Date birthday;
    private String email;
    private String avatar_url;
    private String phone;
    private int department_id;
    private int expert_team_id;
    private int district_id;
    private String profile;
    private String physician_certificate;
    private String practicing_certificate;
    private long registered_at;
    private int status;
   private long updated_at;

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(long registered_at) {
        this.registered_at = registered_at;
    }

    public long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getIdentification_number() {
        return identification_number;
    }

    public void setIdentification_number(String identification_number) {
        this.identification_number = identification_number;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getExpert_team_id() {
        return expert_team_id;
    }

    public void setExpert_team_id(int expert_team_id) {
        this.expert_team_id = expert_team_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPhysician_certificate() {
        return physician_certificate;
    }

    public void setPhysician_certificate(String physician_certificate) {
        this.physician_certificate = physician_certificate;
    }

    public String getPracticing_certificate() {
        return practicing_certificate;
    }

    public void setPracticing_certificate(String practicing_certificate) {
        this.practicing_certificate = practicing_certificate;
    }
}

