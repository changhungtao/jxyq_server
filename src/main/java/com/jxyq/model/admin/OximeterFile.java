package com.jxyq.model.admin;

/**
 * Created by wujj-fnst on 2015/4/13.
 */
public class OximeterFile {
    private int oximeter_file_id;
    private int patient_id;
    private Long measured_at;
    private int oximeter_value;
    private String user_symptom;
    private String proposal;
    private int doctor_id;
    private int status;

    public int getOximeter_file_id() {
        return oximeter_file_id;
    }

    public void setOximeter_file_id(int oximeter_file_id) {
        this.oximeter_file_id = oximeter_file_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public Long getMeasured_at() {
        return measured_at;
    }

    public void setMeasured_at(Long measured_at) {
        this.measured_at = measured_at;
    }

    public int getOximeter_value() {
        return oximeter_value;
    }

    public void setOximeter_value(int oximeter_value) {
        this.oximeter_value = oximeter_value;
    }

    public String getUser_symptom() {
        return user_symptom;
    }

    public void setUser_symptom(String user_symptom) {
        this.user_symptom = user_symptom;
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
