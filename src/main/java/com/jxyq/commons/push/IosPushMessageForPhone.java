package com.jxyq.commons.push;

import com.jxyq.commons.ucpaas.SysConfig;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import java.io.InputStream;

public class IosPushMessageForPhone {
    private static IosPushMessageForPhone instance = null;
    private ApnsService apnService = null;

    boolean debug = Boolean.valueOf(SysConfig.getInstance().getProperty("apns.debug"));
    String dev_file = SysConfig.getInstance().getProperty("apns.dev_file");
    String dev_password = SysConfig.getInstance().getProperty("apns.dev_pwd");
    String pro_file = SysConfig.getInstance().getProperty("apns.product_file");
    String pro_password = SysConfig.getInstance().getProperty("apns.product_pwd");

    private IosPushMessageForPhone() {
        if (debug) {
            apnService = APNS.newService()
                            .withCert(dev_file, dev_password)
                            .withSandboxDestination()
                            .build();
        } else {
            apnService = APNS.newService()
                            .withCert(pro_file, pro_password)
                            .withProductionDestination()
                            .build();
        }
    }

    public static synchronized IosPushMessageForPhone getInstance() {
        if (instance == null) {
            instance = new IosPushMessageForPhone();
        }
        return instance;
    }


    public void sendMessage(String token, String body) {
        String payload = APNS.newPayload().alertBody(body).build();
        apnService.push(token, payload);
    }


    public void sendMessage(String token, String body, String secret, String secretValue) {
        String payload = APNS.newPayload()
                .customField(secret, secretValue)
                .alertBody(body)
                .build();
        apnService.push(token, payload);
    }

    public static void main(String[] args) {
        IosPushMessageForPhone service = IosPushMessageForPhone.getInstance();

        service.sendMessage("355bb806eb7192742635090f8d6720b835674e5dcfd1ec3d34c4a63ed14c834c", "远情捷迅");
        service.sendMessage("eefb5c05d7a1c29c6c508af835d88dfcd2733cade0162e54844a4598bd3de755", "远情捷迅测试2", "secret", "how are you!");
        service.sendMessage("28778df0766ba3aafdf56fadb8dfd0fc309ade18faf66d3fc8d4a03e58c82478", "远情捷迅");
        service.sendMessage("28778df0766ba3aafdf56fadb8dfd0fc309ade18faf66d3fc8d4a03e58c82478", "远情捷迅测试2", "secret", "how are you!");

    }
}
