package com.jxyq.commons.camera;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

public class CameraUtil {
    private static final int BUFFER_SIZE = 1024*20;

    public static String ctrlCamera(String url, String user, String password) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(url);
        get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        byte[] encodedBytes = Base64.encodeBase64((user + ":" + password).getBytes());
        get.addRequestHeader("Authorization", "Basic " + new String(encodedBytes));
        client.executeMethod(get);
        String result = new String(get.getResponseBodyAsString().getBytes("utf-8"));
        get.releaseConnection();
        return result;
    }

    public static HttpServletResponse snapCamera(String url, String user, String password, HttpServletResponse res) throws Exception{
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(url);
        get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        byte[] encodedBytes = Base64.encodeBase64((user + ":" + password).getBytes());
        get.addRequestHeader("Authorization", "Basic " + new String(encodedBytes));
        client.executeMethod(get);
        InputStream in = get.getResponseBodyAsStream();

        res.setContentType("image/jpeg");
        OutputStream out = res.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();
        return res;
    }
}
