package com.jxyq.model.family;

public class TouchButton {
    private int touch_button_id;
    private String name;
    private int user_id;
    private String dev_uid;
    private long registered_at;

    public int getTouch_button_id() {
        return touch_button_id;
    }

    public void setTouch_button_id(int touch_button_id) {
        this.touch_button_id = touch_button_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDev_uid() {
        return dev_uid;
    }

    public void setDev_uid(String dev_uid) {
        this.dev_uid = dev_uid;
    }

    public long getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(long registered_at) {
        this.registered_at = registered_at;
    }
}
