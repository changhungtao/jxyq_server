package com.jxyq.model.user;

public class UserPupil {
    private Integer user_pupil_id;
    private int user_id;
    private String pupil_name;
    private String pupil_imei;
    private String pupil_avatar_url;

    public Integer getUser_pupil_id() {
        return user_pupil_id;
    }

    public void setUser_pupil_id(Integer user_pupil_id) {
        this.user_pupil_id = user_pupil_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPupil_name() {
        return pupil_name;
    }

    public void setPupil_name(String pupil_name) {
        this.pupil_name = pupil_name;
    }

    public String getPupil_imei() {
        return pupil_imei;
    }

    public void setPupil_imei(String pupil_imei) {
        this.pupil_imei = pupil_imei;
    }

    public String getPupil_avatar_url() {
        return pupil_avatar_url;
    }

    public void setPupil_avatar_url(String pupil_avatar_url) {
        this.pupil_avatar_url = pupil_avatar_url;
    }
}
