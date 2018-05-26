package com.jxyq.test;

import com.jxyq.commons.ucpaas.UcpaasCommon;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

public class SmsWebChineseTest {
    public static void main(String[] args) throws IOException {
        try {
//            sendMsg("18045101576", "亲爱的用户,您的短信验证码为: 506722, 请于60秒内正确输入验证码.");
            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            ucpaasCommon.sendTemplateSMS(UcpaasCommon.reg_template, "18045101576", "123.60");
//            ucpaasCommon.findAccountInfo();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendMsg(String mbno, String msg) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] data = new NameValuePair[]{new NameValuePair("Uid", "coowanbaby"), new NameValuePair("Key", "99e4867a9b6dfbfff5d1"), new NameValuePair("smsMob", mbno), new NameValuePair("smsText", msg)};
        post.setRequestBody(data);
        client.executeMethod(post);
        String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
        System.out.println(result);
        post.releaseConnection();
    }

    public static void sendMsg1(String mbno, String msg) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] data = new NameValuePair[]{new NameValuePair("Uid", "EONETONE"), new NameValuePair("Key", "66a470e157232d182438"), new NameValuePair("smsMob", mbno), new NameValuePair("smsText", msg)};
        post.setRequestBody(data);
        client.executeMethod(post);
        String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
        System.out.println(result);
        post.releaseConnection();
    }
}
