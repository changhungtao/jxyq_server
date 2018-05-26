package com.jxyq.model.health;

public class TerminalCatagoryPattern {
    private long terminal_catagory_pattern_id;
    private long terminal_catagory_id;
    private String pattern;
    private int manufactory_id;
    private int product_type_id;
    private int device_type_id;
    private long created_at;
    private int status;

    public long getTerminal_catagory_pattern_id() {
        return terminal_catagory_pattern_id;
    }

    public void setTerminal_catagory_pattern_id(long terminal_catagory_pattern_id) {
        this.terminal_catagory_pattern_id = terminal_catagory_pattern_id;
    }

    public long getTerminal_catagory_id() {
        return terminal_catagory_id;
    }

    public void setTerminal_catagory_id(long terminal_catagory_id) {
        this.terminal_catagory_id = terminal_catagory_id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getManufactory_id() {
        return manufactory_id;
    }

    public void setManufactory_id(int manufactory_id) {
        this.manufactory_id = manufactory_id;
    }

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public int getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(int device_type_id) {
        this.device_type_id = device_type_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
