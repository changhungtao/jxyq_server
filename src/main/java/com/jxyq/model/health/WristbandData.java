package com.jxyq.model.health;

public class WristbandData extends UserHealthModel {
    private long wristband_data_id;
    private Integer step_count;
    private Integer distance;
    private Integer calories;
    private Integer walk_count;
    private Integer walk_distance;
    private Integer walk_calories;
    private Integer run_count;
    private Integer run_distance;
    private Integer run_calories;
    private Integer deep_duration;
    private Integer shallow_duration;
    private Integer heart_rate;

    public long getWristband_data_id() {
        return wristband_data_id;
    }

    public void setWristband_data_id(long wristband_data_id) {
        this.wristband_data_id = wristband_data_id;
    }

    public Integer getStep_count() {
        return step_count;
    }

    public void setStep_count(Integer step_count) {
        this.step_count = step_count;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getWalk_count() {
        return walk_count;
    }

    public void setWalk_count(Integer walk_count) {
        this.walk_count = walk_count;
    }

    public Integer getWalk_distance() {
        return walk_distance;
    }

    public void setWalk_distance(Integer walk_distance) {
        this.walk_distance = walk_distance;
    }

    public Integer getWalk_calories() {
        return walk_calories;
    }

    public void setWalk_calories(Integer walk_calories) {
        this.walk_calories = walk_calories;
    }

    public Integer getRun_count() {
        return run_count;
    }

    public void setRun_count(Integer run_count) {
        this.run_count = run_count;
    }

    public Integer getRun_distance() {
        return run_distance;
    }

    public void setRun_distance(Integer run_distance) {
        this.run_distance = run_distance;
    }

    public Integer getRun_calories() {
        return run_calories;
    }

    public void setRun_calories(Integer run_calories) {
        this.run_calories = run_calories;
    }

    public Integer getDeep_duration() {
        return deep_duration;
    }

    public void setDeep_duration(Integer deep_duration) {
        this.deep_duration = deep_duration;
    }

    public Integer getShallow_duration() {
        return shallow_duration;
    }

    public void setShallow_duration(Integer shallow_duration) {
        this.shallow_duration = shallow_duration;
    }

    public Integer getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(Integer heart_rate) {
        this.heart_rate = heart_rate;
    }
}
