package com.jxyq.commons.push;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.model.others.PushService;

public class AndroidPushMessageForPhone {
    public static int DEVICE_TYPE_ANDROID = 3;
    private String apiKey = SysConfig.getInstance().getProperty("push.apiKey");
    private String secretKey = SysConfig.getInstance().getProperty("push.secretKey");

    private Gson gson = new GsonBuilder().create();

    public AndroidPushMessageForPhone() {
    }

    public AndroidPushMessageForPhone(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public boolean pushMsgToAll(AndroidPushMessage msg) {
        PushKeyPair pair = new PushKeyPair(this.apiKey, this.secretKey);
        BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            PushMsgToAllRequest request = new PushMsgToAllRequest();
            request.addMessageType(msg.getMsgType());
            if (msg.getMsgType() == 0) {
                request.addMessage(msg.getMessage());
            } else {
                JsonObject json = new JsonObject();
                if (msg.getTitle() != null) {
                    json.add("title", gson.toJsonTree(msg.getTitle()));
                }
                json.add("description", gson.toJsonTree(msg.getMessage()));
                if (msg.getNotification_builder_id() != null) {
                    json.add("notification_builder_id", gson.toJsonTree(msg.getNotification_builder_id()));
                }
                if (msg.getNotification_basic_style() != null){
                    json.add("notification_basic_style", gson.toJsonTree(msg.getNotification_basic_style()));
                }
                if (msg.getOpen_type() != null){
                    json.add("open_type", gson.toJsonTree(msg.getOpen_type()));
                }
                if (msg.getUrl() != null){
                    json.add("url", gson.toJsonTree(msg.getUrl()));
                }
                if (msg.getPkg_content() != null){
                    json.add("pkg_content", gson.toJsonTree(msg.getPkg_content()));
                }
                if (msg.getCustom_content() != null){
                    json.add("custom_content", msg.getCustom_content());
                }
                request.addMessage(gson.toJson(json));
                System.out.println(gson.toJson(json));
            }
            request.setDeviceType(DEVICE_TYPE_ANDROID);
            if (msg.getMsgExpires() != null) {
                request.addMsgExpires(msg.getMsgExpires());
            }
            if (msg.getDeployStatus() != null) {
                request.addDepolyStatus(msg.getDeployStatus());
            }
            if (msg.getSendTime() != null) {
                request.addSendTime(msg.getSendTime());
            }

            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());
            return true;
        } catch (PushClientException e) {
            e.printStackTrace();
        } catch (PushServerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean pushMsgToSingleDevice(AndroidPushMessage msg, PushService user) {
        PushKeyPair pair = new PushKeyPair(this.apiKey, this.secretKey);
        BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest();
            request.addMessageType(msg.getMsgType());
            if (msg.getMsgType() == 0) {
                request.addMessage(msg.getMessage());
            } else {
                JsonObject json = new JsonObject();
                if (msg.getTitle() != null) {
                    json.add("title", gson.toJsonTree(msg.getTitle()));
                }
                json.add("description", gson.toJsonTree(msg.getMessage()));
                if (msg.getNotification_builder_id() != null) {
                    json.add("notification_builder_id", gson.toJsonTree(msg.getNotification_builder_id()));
                }
                if (msg.getNotification_basic_style() != null){
                    json.add("notification_basic_style", gson.toJsonTree(msg.getNotification_basic_style()));
                }
                if (msg.getOpen_type() != null){
                    json.add("open_type", gson.toJsonTree(msg.getOpen_type()));
                }
                if (msg.getUrl() != null){
                    json.add("url", gson.toJsonTree(msg.getUrl()));
                }
                if (msg.getPkg_content() != null){
                    json.add("pkg_content", gson.toJsonTree(msg.getPkg_content()));
                }
                if (msg.getCustom_content() != null){
                    json.add("custom_content", msg.getCustom_content());
                }
                request.addMessage(gson.toJson(json));
            }
            request.setDeviceType(DEVICE_TYPE_ANDROID);
            if (msg.getMsgExpires() != null) {
                request.addMsgExpires(msg.getMsgExpires());
            }
            if (msg.getDeployStatus() != null) {
                request.addDeployStatus(msg.getDeployStatus());
            }
            if (msg.getTopicId() != null) {
                request.addTopicId(msg.getTopicId());
            }
            request.setChannelId(user.getChannel_id());

            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
            System.out.println("msgId: " + response.getMsgId()
                    + ",sendTime: " + response.getSendTime());
            return true;
        } catch (PushClientException e) {
            e.printStackTrace();
        } catch (PushServerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
