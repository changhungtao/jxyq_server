package com.jxyq.model.others;

public class OneTimeToken {
    private long one_time_token_id;
    private int user_id;
    private String token;
    private long created_at;
    private long valid_before;
    private int status;

    public long getOne_time_token_id() {
        return one_time_token_id;
    }

    public void setOne_time_token_id(long one_time_token_id) {
        this.one_time_token_id = one_time_token_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getValid_before() {
        return valid_before;
    }

    public void setValid_before(long valid_before) {
        this.valid_before = valid_before;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
