package com.jxyq.model;

import javax.net.ssl.SSLContext;

/**
 * Created by wujj-fnst on 2015/3/20.
 */
public class ConContext {
    private long id;
    private Integer user_id;//用户id
    private Integer district_id;//地区编号
    private Integer department_id;//科室信息
    private Integer audio_duration;//录音时长
    private Integer score;
    private String content;
    private Integer status;
    private long created_at;
    private String phone;
    private String user_name;
    private Integer doctor_id;
    private String doctor_name;
    private String doctor_profile;
    private Integer expert_team_id;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
        this.district_id = district_id;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    public Integer getAudio_duration() {
        return audio_duration;
    }

    public void setAudio_duration(Integer audio_duration) {
        this.audio_duration = audio_duration;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_profile() {
        return doctor_profile;
    }

    public void setDoctor_profile(String doctor_profile) {
        this.doctor_profile = doctor_profile;
    }

    public Integer getExpert_team_id() {
        return expert_team_id;
    }

    public void setExpert_team_id(Integer expert_team_id) {
        this.expert_team_id = expert_team_id;
    }
}
