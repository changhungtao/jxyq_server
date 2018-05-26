package com.jxyq.model.consultation;

public class QuestionAndReply {
    private long question_and_reply_id;
    private long health_consultation_id;
    private long created_at;
    private String content;
    private int qa_type;
    private int doctor_id;
    private String doctor_name;
    private int status;

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public long getQuestion_and_reply_id() {
        return question_and_reply_id;
    }

    public void setQuestion_and_reply_id(long question_and_reply_id) {
        this.question_and_reply_id = question_and_reply_id;
    }

    public long getHealth_consultation_id() {
        return health_consultation_id;
    }

    public void setHealth_consultation_id(long health_consultation_id) {
        this.health_consultation_id = health_consultation_id;
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

    public int getQa_type() {
        return qa_type;
    }

    public void setQa_type(int qa_type) {
        this.qa_type = qa_type;
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
