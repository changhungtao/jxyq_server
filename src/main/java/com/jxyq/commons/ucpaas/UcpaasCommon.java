package com.jxyq.commons.ucpaas;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jxyq.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UcpaasCommon {
    public static String reg_template = SysConfig.getInstance().getProperty("ucpaas.template_reg");
    public static String reset_template = SysConfig.getInstance().getProperty("ucpaas.reset_password");
    public static String init_template = SysConfig.getInstance().getProperty("ucpaas.set_password");
    public static String warning_template = SysConfig.getInstance().getProperty("ucpaas.user_warning");
    public static String watch_set = SysConfig.getInstance().getProperty("ucpaas.watch_set");


    private boolean isTest = Boolean.parseBoolean(SysConfig.getInstance().getProperty("ucpaas.is_test"));
    private String server = SysConfig.getInstance().getProperty("ucpaas.rest_server");
    private String sslIP = SysConfig.getInstance().getProperty("ucpaas.http_ssl_ip");
    private int sslPort = SysConfig.getInstance().getPropertyInt("ucpaas.http_ssl_port");
    private String version = SysConfig.getInstance().getProperty("ucpaas.version");

    private String accountSid = SysConfig.getInstance().getProperty("ucpaas.account_id");
    private String token = SysConfig.getInstance().getProperty("ucpaas.token");
    private String appId = SysConfig.getInstance().getProperty("ucpaas.app_id");

    private StringBuffer getHostAddress() {
        StringBuffer sb = new StringBuffer("https://");
        sb.append(server);
        return sb;
    }

    /*private DefaultHttpClient getDefaultHttpClient(){
        DefaultHttpClient httpClient = null;
        if (isTest) {
            try {
                SSLHttpClient chc = new SSLHttpClient();
                httpClient = chc.registerSSL(sslIP, "TLS", sslPort, "https");
                HttpParams hParams = new BasicHttpParams();
                hParams.setParameter("https.protocols", "SSLv3,SSLv2Hello");
                httpClient.setParams(hParams);
            } catch (KeyManagementException e) {
            }catch (NoSuchAlgorithmException e) {
            }
        }else {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }*/

    private static String dateToStr(Date date, String pattern) {
        if (date == null || date.equals(""))
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    private HttpResponse get(String url, String auth, DefaultHttpClient httpClient) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
        httpGet.setHeader("Authorization", auth);

        HttpResponse response = httpClient.execute(httpGet);
        return response;
    }

    private HttpResponse post(String url, String auth, String body, DefaultHttpClient httpClient) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httpPost.setEntity(requestBody);

        HttpResponse response = httpClient.execute(httpPost);
        return response;
    }

    /*rest_error: 1 符合
     ** |105110 |短信模板ID不存在|
     ** |105111 |短信模板未审核通过|
     ** |105112 |短信模板替换个数与实际参数个数不匹配|
     ** |105113 |短信模板ID为空| */
    private void checkSMSStatus(String result) throws Exception {
        if (StringUtils.isBlank(result)) return;
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();
        String respCode = json.get("resp").getAsJsonObject().get("respCode").getAsString();
        switch (respCode) {
            case "105110":
                throw new Exception("短信模板ID不存在, 短信发送失败.");
            case "105111":
                throw new Exception("短信模板未审核通过, 短信发送失败.");
            case "105112":
                throw new Exception("短信模板替换个数与实际参数个数不匹配, 短信发送失败.");
            case "105113":
                throw new Exception("短信模板ID为空, 短信发送失败.");
        }
    }

    public String findAccountInfo() throws Exception {
        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            EncryptUtil encryptUtil = new EncryptUtil();
            String timestamp = dateToStr(new Date(), "yyyyMMddHHmmss");
            String signature = encryptUtil.md5Digest(accountSid + token + timestamp);
            String url = getHostAddress().append("/").append(version).
                    append("/Accounts/").append(accountSid).append("")
                    .append("?sig=").append(signature).toString();
            String auth = encryptUtil.base64Encoder(accountSid + ":" + timestamp);
            HttpResponse response = get(url, auth, httpClient);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("服务器网络故障,短信发送失败");
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    public String sendTemplateSMS(String templateId, String phone, String param)
            throws Exception {
        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            EncryptUtil encryptUtil = new EncryptUtil();
            String timestamp = dateToStr(new Date(), "yyyyMMddHHmmss");
            String signature = encryptUtil.md5Digest(accountSid + token + timestamp);
            String url = getHostAddress().append("/").append(version)
                    .append("/Accounts/").append(accountSid)
                    .append("/Messages/templateSMS")
                    .append("?sig=").append(signature).toString();
            String auth = encryptUtil.base64Encoder(accountSid + ":" + timestamp);

            TemplateSMS templateSMS = new TemplateSMS();
            templateSMS.setAppId(appId);
            templateSMS.setTemplateId(templateId);
            templateSMS.setTo(phone);
            templateSMS.setParam(param);
            Gson gson = new Gson();
            String body = "{\"templateSMS\":" + gson.toJson(templateSMS) + "}";

            HttpResponse response = post(url, auth, body, httpClient);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("服务器网络故障,短信发送失败");
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        System.out.println(result);
        checkSMSStatus(result);
        return result;
    }
}
