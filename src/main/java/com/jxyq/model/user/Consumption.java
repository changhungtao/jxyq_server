package com.jxyq.model.user;

public class Consumption {
    private Integer comsumption_id;
    private long happened_at;
    private Integer user_id;
    private Integer points;
    private Integer new_points;
    private Integer operation_type;
    private String description;

    public Integer getComsumption_id() {
        return comsumption_id;
    }

    public void setComsumption_id(Integer comsumption_id) {
        this.comsumption_id = comsumption_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getNew_points() {
        return new_points;
    }

    public void setNew_points(Integer new_points) {
        this.new_points = new_points;
    }

    public long getHappened_at() {
        return happened_at;
    }

    public void setHappened_at(long happened_at) {
        this.happened_at = happened_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(Integer operation_type) {
        this.operation_type = operation_type;
    }
}
