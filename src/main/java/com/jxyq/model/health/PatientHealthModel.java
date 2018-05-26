package com.jxyq.model.health;

public class PatientHealthModel {
    private Integer patient_id;
    private String measured_at;
    private String user_symptom;
    private String proposal;
    private Integer doctor_id;
    private Integer status;

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public String getMeasured_at() {
        return measured_at;
    }

    public void setMeasured_at(String measured_at) {
        this.measured_at = measured_at;
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

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
