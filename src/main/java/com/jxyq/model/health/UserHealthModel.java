package com.jxyq.model.health;

public class UserHealthModel {
    private long measured_at;
    private int user_id;
    private int terminal_id;
    private int manufactory_id;
    private String proposal;
    private int doctor_id;
    private int status;

    public long getMeasured_at() {
        return measured_at;
    }

    public void setMeasured_at(long measured_at) {
        this.measured_at = measured_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(int terminal_id) {
        this.terminal_id = terminal_id;
    }

    public int getManufactory_id() {
        return manufactory_id;
    }

    public void setManufactory_id(int manufactory_id) {
        this.manufactory_id = manufactory_id;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
