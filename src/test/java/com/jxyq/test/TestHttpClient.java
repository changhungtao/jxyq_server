package com.jxyq.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;


import com.jxyq.commons.util.JsonUtil;


public class TestHttpClient {
    public static void main(String[] args)
            throws IOException {
//      user_login();
//        reset();
//        register();
        change();
//        score();
    }
    public static void score() throws IOException {
        HttpClient httpClient = new HttpClient();
//        PostMethod hm = new PostMethod("http://193.160.18.41:8080/server/app/user_login");
        PostMethod hm = new PostMethod("http://localhost:8080/server/api/user/points");
        try {
            System.out.println(11);
            HttpConnectionManagerParams managerParams = httpClient
                    .getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(30000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(120000);


            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", "n7374921426219912588");
            map.put("type", "0");
           /*  map.put("clientType", "2");
            map.put("lang", "2052");
            map.put("versionNo", "36");
            map.put("deviceID", "11233");*/
            //map.put("token", "14404991403530572063");
            String key = "111";
            String resultStr = JsonUtil.Object2EncodeJsonSting(map);
            RequestEntity entry = new StringRequestEntity(resultStr, "text/xml", "utf-8");
            hm.setRequestEntity(entry);
            int statusCode = httpClient.executeMethod(hm);
            String strResponse = null;
            if (statusCode != HttpStatus.SC_OK) {
                throw new IllegalStateException("Method failed: "
                        + hm.getStatusLine());
            }
            strResponse = hm.getResponseBodyAsString();
            map = (HashMap<String, Object>) JsonUtil.JsonSting2DecodeObject(strResponse, map.getClass());
            System.out.println(map);
        } catch (Exception ex) {
            throw new IllegalStateException(ex.toString());
        } finally {
            // 释放连接
            hm.releaseConnection();
        }
    }

    public static void change() throws IOException {
        HttpClient httpClient = new HttpClient();
//        PostMethod hm = new PostMethod("http://193.160.18.41:8080/server/app/user_login");
        PostMethod hm = new PostMethod("http://localhost:8080/server/api/user/password");
        try {
            System.out.println(11);
            HttpConnectionManagerParams managerParams = httpClient
                    .getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(30000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(120000);


            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", "n7936341426236260840");
            map.put("oldPassword", "7e9b1c1e0355853d427ba0670bf25899");
            map.put("newPassword", "e10adc3949ba59abbe56e057f20f883e");
           /*  map.put("clientType", "2");
            map.put("lang", "2052");
            map.put("versionNo", "36");
            map.put("deviceID", "11233");*/
            //map.put("token", "14404991403530572063");
            String key = "111";
            String resultStr = JsonUtil.Object2EncodeJsonSting(map);
            RequestEntity entry = new StringRequestEntity(resultStr, "text/xml", "utf-8");
            hm.setRequestEntity(entry);
            int statusCode = httpClient.executeMethod(hm);
            String strResponse = null;
            if (statusCode != HttpStatus.SC_OK) {
                throw new IllegalStateException("Method failed: "
                        + hm.getStatusLine());
            }
            strResponse = hm.getResponseBodyAsString();
            map = (HashMap<String, Object>) JsonUtil.JsonSting2DecodeObject(strResponse, map.getClass());
            System.out.println(map);
        } catch (Exception ex) {
            throw new IllegalStateException(ex.toString());
        } finally {
            // 释放连接
            hm.releaseConnection();
        }
    }
    public static void login() throws IOException {
        HttpClient httpClient = new HttpClient();
//        PostMethod hm = new PostMethod("http://193.160.18.41:8080/server/app/user_login");
        PostMethod hm = new PostMethod("http://localhost:8080/server/api/open/user_login");
        try {
            System.out.println(11);
            HttpConnectionManagerParams managerParams = httpClient
                    .getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(30000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(120000);


            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("account", "18375325066");
            map.put("password", "e10adc3949ba59abbe56e057f20f883e");
            map.put("device", "0");
           /*  map.put("clientType", "2");
            map.put("lang", "2052");
            map.put("versionNo", "36");
            map.put("deviceID", "11233");*/
            //map.put("token", "14404991403530572063");
            String key = "111";
            String resultStr = JsonUtil.Object2EncodeJsonSting(map);
            RequestEntity entry = new StringRequestEntity(resultStr, "text/xml", "utf-8");

            hm.setRequestEntity(entry);

            int statusCode = httpClient.executeMethod(hm);
            String strResponse = null;
            if (statusCode != HttpStatus.SC_OK) {
                throw new IllegalStateException("Method failed: "
                        + hm.getStatusLine());
            }
            strResponse = hm.getResponseBodyAsString();
            map = (HashMap<String, Object>) JsonUtil.JsonSting2DecodeObject(strResponse, map.getClass());
            System.out.println(map);
        } catch (Exception ex) {
            throw new IllegalStateException(ex.toString());
        } finally {
            // 释放连接
            hm.releaseConnection();
        }
    }

    public static void reset() throws IOException {
        HttpClient httpClient = new HttpClient();
        PostMethod hm = new PostMethod("http://localhost:8080/server/app/reset_pwd");
        try {
            System.out.println(11);
            HttpConnectionManagerParams managerParams = httpClient
                    .getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(30000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(120000);


            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("mobile", "16666666666");
            map.put("newPwd", "7e9b1c1e0355853d427ba0670bf25899");
         /*   map.put("password", "123456");
            map.put("device", "0");*/
           /*  map.put("clientType", "2");
            map.put("lang", "2052");
            map.put("versionNo", "36");
            map.put("deviceID", "11233");*/
            //map.put("token", "14404991403530572063");
            String key = "111";
            String resultStr = JsonUtil.Object2EncodeJsonSting(map);
            RequestEntity entry = new StringRequestEntity(resultStr, "text/xml", "utf-8");
            hm.setRequestEntity(entry);
            int statusCode = httpClient.executeMethod(hm);
            String strResponse = null;
            if (statusCode != HttpStatus.SC_OK) {
                throw new IllegalStateException("Method failed: "
                        + hm.getStatusLine());
            }
            strResponse = hm.getResponseBodyAsString();
            map = (HashMap<String, Object>) JsonUtil.JsonSting2DecodeObject(strResponse, map.getClass());
            System.out.println(map);
        } catch (Exception ex) {
            throw new IllegalStateException(ex.toString());
        } finally {
            // 释放连接
            hm.releaseConnection();
        }
    }
    public static void register() throws IOException {
        HttpClient httpClient = new HttpClient();
        PostMethod hm = new PostMethod("http://localhost:8080/server/app/one_key_register");
        try {
            System.out.println(11);
            HttpConnectionManagerParams managerParams = httpClient
                    .getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(30000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(120000);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("mobile", "17777777777");
            map.put("verificationCode", "123456");
         /*   map.put("password", "123456");
            map.put("device", "0");*/
           /*  map.put("clientType", "2");
            map.put("lang", "2052");
            map.put("versionNo", "36");
            map.put("deviceID", "11233");*/
            //map.put("token", "14404991403530572063");
            String key = "111";
            String resultStr = JsonUtil.Object2EncodeJsonSting(map);
            RequestEntity entry = new StringRequestEntity(resultStr, "text/xml", "utf-8");
            hm.setRequestEntity(entry);
            int statusCode = httpClient.executeMethod(hm);
            String strResponse = null;
            if (statusCode != HttpStatus.SC_OK) {
                throw new IllegalStateException("Method failed: "
                        + hm.getStatusLine());
            }
            strResponse = hm.getResponseBodyAsString();
            map = (HashMap<String, Object>) JsonUtil.JsonSting2DecodeObject(strResponse, map.getClass());
            System.out.println(map);
        } catch (Exception ex) {
            throw new IllegalStateException(ex.toString());
        } finally {
            // 释放连接
            hm.releaseConnection();
        }
    }
}