package com.jxyq.model.health;

public class UserTerminal {
    private long user_device_id;
    private long terminal_id;
    private long user_id;
    private long manufactory_id;
    private long device_type_id;
    private long terminal_catagory_id;
    private int count;
    private long created_at;
    private long updated_at;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getUser_device_id() {
        return user_device_id;
    }

    public void setUser_device_id(long user_device_id) {
        this.user_device_id = user_device_id;
    }

    public long getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(long terminal_id) {
        this.terminal_id = terminal_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getManufactory_id() {
        return manufactory_id;
    }

    public void setManufactory_id(long manufactory_id) {
        this.manufactory_id = manufactory_id;
    }

    public long getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(long device_type_id) {
        this.device_type_id = device_type_id;
    }

    public long getTerminal_catagory_id() {
        return terminal_catagory_id;
    }

    public void setTerminal_catagory_id(long terminal_catagory_id) {
        this.terminal_catagory_id = terminal_catagory_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
