package com.jxyq.model;

import java.util.List;

/**
 * Created by wujj-fnst on 2015/3/19.
 */
public class QA {
   private Long question_and_reply_id;
   private Long health_consultation_id;
   private Integer qa_type;
   private Integer doctor_id;
   private String doctor_profile;
   private String doctor_name;
   private String content;
   private String audio_url;
   private String image_url1;
   private String image_url2;
   private String image_url3;
   private String image_url4;
   private Long created_at;
   private List<String> image_urls;
   private int  status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getQuestion_and_reply_id() {
        return question_and_reply_id;
    }

    public void setQuestion_and_reply_id(Long question_and_reply_id) {
        this.question_and_reply_id = question_and_reply_id;
    }

    public Integer getQa_type() {
        return qa_type;
    }

    public void setQa_type(Integer qa_type) {
        this.qa_type = qa_type;
    }

    public long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }


    public String getDoctor_profile() {
        return doctor_profile;
    }

    public void setDoctor_profile(String doctor_profile) {
        this.doctor_profile = doctor_profile;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getImage_url1() {
        return image_url1;
    }

    public void setImage_url1(String image_url1) {
        this.image_url1 = image_url1;
    }

    public String getImage_url2() {
        return image_url2;
    }

    public void setImage_url2(String image_url2) {
        this.image_url2 = image_url2;
    }

    public String getImage_url3() {
        return image_url3;
    }

    public void setImage_url3(String image_url3) {
        this.image_url3 = image_url3;
    }

    public String getImage_url4() {
        return image_url4;
    }

    public void setImage_url4(String image_url4) {
        this.image_url4 = image_url4;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public List<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(List<String> image_urls) {
        this.image_urls = image_urls;
    }

    public Long getHealth_consultation_id() {
        return health_consultation_id;
    }

    public void setHealth_consultation_id(Long health_consultation_id) {
        this.health_consultation_id = health_consultation_id;
    }
}
