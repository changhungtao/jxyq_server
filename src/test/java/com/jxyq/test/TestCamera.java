package com.jxyq.test;

import com.jxyq.commons.ucpaas.UcpaasCommon;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

public class TestCamera {
    public static void main(String[] args) throws IOException {
        try {
            ctrlCamera();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void ctrlCamera() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod("http://192.168.20.163:81/cgi-bin/hi3510/ptzright.cgi");
        get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        byte[] encodedBytes = Base64.encodeBase64("admin:admin".getBytes());
        get.addRequestHeader("Authorization", "Basic " + new String(encodedBytes));
        client.executeMethod(get);
        String result = new String(get.getResponseBodyAsString().getBytes("utf-8"));
        System.out.println(result);
        get.releaseConnection();
    }
}