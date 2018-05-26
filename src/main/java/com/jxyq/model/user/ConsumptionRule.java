package com.jxyq.model.user;

public class ConsumptionRule {
    private Integer comsumption_rule_id;
    private Integer operation_type;
    private Integer points;
    private String description;

    public Integer getComsumption_rule_id() {
        return comsumption_rule_id;
    }

    public void setComsumption_rule_id(Integer comsumption_rule_id) {
        this.comsumption_rule_id = comsumption_rule_id;
    }

    public Integer getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(Integer operation_type) {
        this.operation_type = operation_type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
