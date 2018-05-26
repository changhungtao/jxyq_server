package com.jxyq.model.user;

public class VerificationCode {
    public static int USAGE_REGISTER = 1;
    public static int USAGE_RESET_PASSWORD = 2;
    public static int USAGE_ADD_PUPIL = 3;
    public static int USAGE_ADD_ASSOCIATION = 4;

    private int verification_code_id;
    private String phone;
    private int used_for;
    private long created_at;
    private String content;
    private int duration;
    private int status;

    public int getVerification_code_id() {
        return verification_code_id;
    }

    public void setVerification_code_id(int verification_code_id) {
        this.verification_code_id = verification_code_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUsed_for() {
        return used_for;
    }

    public void setUsed_for(int used_for) {
        this.used_for = used_for;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
