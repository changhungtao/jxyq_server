package com.jxyq.model.health;

public class UserHeartBeatData extends UserHealthModel {
    private long heart_beat_data_id;
    private int heart_beat;

    public long getHeart_beat_data_id() {
        return heart_beat_data_id;
    }

    public void setHeart_beat_data_id(long heart_beat_data_id) {
        this.heart_beat_data_id = heart_beat_data_id;
    }

    public int getHeart_beat() {
        return heart_beat;
    }

    public void setHeart_beat(int heart_beat) {
        this.heart_beat = heart_beat;
    }
}
