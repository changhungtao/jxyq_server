package com.jxyq.model.others;

public class OperationLog {
    private long operation_log_id;
    private int user_role;
    private int user_id;
    private String user_login_name;
    private int op_title;
    private String op_detail;
    private long happened_at;
    private String origin_ip;

    public long getOperation_log_id() {
        return operation_log_id;
    }

    public void setOperation_log_id(long operation_log_id) {
        this.operation_log_id = operation_log_id;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_login_name() {
        return user_login_name;
    }

    public void setUser_login_name(String user_login_name) {
        this.user_login_name = user_login_name;
    }

    public int getOp_title() {
        return op_title;
    }

    public void setOp_title(int op_title) {
        this.op_title = op_title;
    }

    public String getOp_detail() {
        return op_detail;
    }

    public void setOp_detail(String op_detail) {
        this.op_detail = op_detail;
    }

    public long getHappened_at() {
        return happened_at;
    }

    public void setHappened_at(long happened_at) {
        this.happened_at = happened_at;
    }

    public String getOrigin_ip() {
        return origin_ip;
    }

    public void setOrigin_ip(String origin_ip) {
        this.origin_ip = origin_ip;
    }
}
