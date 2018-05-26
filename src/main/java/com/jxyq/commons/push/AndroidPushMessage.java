package com.jxyq.commons.push;

import com.google.gson.JsonObject;

public class AndroidPushMessage {
    private Integer msgType = 0;
    private String message;
    private Integer msgExpires;
    private Integer deployStatus;
    private Long sendTime;
    private Integer deviceType;
    private String channelId;
    private String topicId;
    /*通知类信息格式*/
    private String title;
    private Integer notification_builder_id;
    private Integer notification_basic_style;
    private Integer open_type = 3;
    private String url;
    private String pkg_content;
    private JsonObject custom_content;

    public AndroidPushMessage() {
    }

    public AndroidPushMessage(int msgType, String message) {
        this.msgType = msgType;
        this.message = message;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMsgExpires() {
        return msgExpires;
    }

    public void setMsgExpires(Integer msgExpires) {
        this.msgExpires = msgExpires;
    }

    public Integer getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(Integer deployStatus) {
        this.deployStatus = deployStatus;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNotification_builder_id() {
        return notification_builder_id;
    }

    public void setNotification_builder_id(Integer notification_builder_id) {
        this.notification_builder_id = notification_builder_id;
    }

    public Integer getNotification_basic_style() {
        return notification_basic_style;
    }

    public void setNotification_basic_style(Integer notification_basic_style) {
        this.notification_basic_style = notification_basic_style;
    }

    public Integer getOpen_type() {
        return open_type;
    }

    public void setOpen_type(Integer open_type) {
        this.open_type = open_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPkg_content() {
        return pkg_content;
    }

    public void setPkg_content(String pkg_content) {
        this.pkg_content = pkg_content;
    }

    public JsonObject getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(JsonObject custom_content) {
        this.custom_content = custom_content;
    }
}
