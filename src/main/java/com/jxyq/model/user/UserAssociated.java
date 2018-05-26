package com.jxyq.model.user;

public class UserAssociated {
    private Integer user_associated_id;
    private Integer user_id;
    private Integer associated_user_id;
    private String associated_user_name;
    private String associated_user_avatar;
    private Integer level;

    public Integer getUser_associated_id() {
        return user_associated_id;
    }

    public void setUser_associated_id(Integer user_associated_id) {
        this.user_associated_id = user_associated_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getAssociated_user_id() {
        return associated_user_id;
    }

    public void setAssociated_user_id(Integer associated_user_id) {
        this.associated_user_id = associated_user_id;
    }

    public String getAssociated_user_name() {
        return associated_user_name;
    }

    public void setAssociated_user_name(String associated_user_name) {
        this.associated_user_name = associated_user_name;
    }

    public String getAssociated_user_avatar() {
        return associated_user_avatar;
    }

    public void setAssociated_user_avatar(String associated_user_avatar) {
        this.associated_user_avatar = associated_user_avatar;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
