package com.jxyq.model.consultation;

public class QuestionAndReplyResource extends QuestionAndReply {
    private long question_and_reply_resource_id;
    private long question_and_reply_id;
    private String image_url1;
    private String image_url2;
    private String image_url3;
    private String image_url4;
    private String audio_url;
    private int audio_duration;
    private int status;

    public long getQuestion_and_reply_resource_id() {
        return question_and_reply_resource_id;
    }

    public void setQuestion_and_reply_resource_id(long question_and_reply_resource_id) {
        this.question_and_reply_resource_id = question_and_reply_resource_id;
    }

    public long getQuestion_and_reply_id() {
        return question_and_reply_id;
    }

    public void setQuestion_and_reply_id(long question_and_reply_id) {
        this.question_and_reply_id = question_and_reply_id;
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

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public int getAudio_duration() {
        return audio_duration;
    }

    public void setAudio_duration(int audio_duration) {
        this.audio_duration = audio_duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
