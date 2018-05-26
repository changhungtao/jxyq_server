package com.jxyq.commons.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static HttpServletResponse setContext(HttpServletResponse res, Object obj){
        setContentType(res, "text/html; charset=utf-8");
        PrintWriter out = null;
        String objJson = gson.toJson(obj);
        try {
            out = res.getWriter();
            out.append(objJson);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (out != null) out.close();
        }
        return res;
    }



    public static HttpServletResponse setStatusCode(HttpServletResponse res, int code){
        res.setStatus(code);
        return res;
    }

    public static HttpServletResponse setContentType(HttpServletResponse res, String contentType){
        res.setContentType(contentType);
        return res;
    }
}
