package com.jxyq.model.family;

public class TouchButtonEvent {
    private int touch_button_event_id;
    private String dev_uid;
    private String sensor_id;
    private int sensor_type;
    private long happened_at;

    public int getTouch_button_event_id() {
        return touch_button_event_id;
    }

    public void setTouch_button_event_id(int touch_button_event_id) {
        this.touch_button_event_id = touch_button_event_id;
    }

    public String getDev_uid() {
        return dev_uid;
    }

    public void setDev_uid(String dev_uid) {
        this.dev_uid = dev_uid;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    public int getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(int sensor_type) {
        this.sensor_type = sensor_type;
    }

    public long getHappened_at() {
        return happened_at;
    }

    public void setHappened_at(long happened_at) {
        this.happened_at = happened_at;
    }
}
