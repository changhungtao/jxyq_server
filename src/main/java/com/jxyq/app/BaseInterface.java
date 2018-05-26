package com.jxyq.app;

import com.google.gson.*;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.model.others.ErrorMessage;
import com.jxyq.model.others.ResponseContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseInterface {
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    public String getRequestContent(HttpServletRequest req)
            throws Exception {
        BufferedReader read = null;
        StringBuffer bufferString = new StringBuffer();
        try {
            //获取请求字符串
            read = req.getReader();
            String sTotalString;
            while ((sTotalString = read.readLine()) != null) {
                bufferString.append(sTotalString);
            }
        } catch (Exception e) {
        } finally {
            read.close();
        }
        return bufferString.toString();

    }


    public JsonObject getJSONParams(HttpServletRequest req) throws Exception {
        BufferedReader reader = null;
        String line = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
        String paramsStr = buffer.toString();
        JsonParser parser = new JsonParser();
        JsonObject params = parser.parse(paramsStr).getAsJsonObject();
        return params;
    }

    public <T> T getJSONParams(HttpServletRequest req, Class<T> clazz) throws Exception {
        BufferedReader reader = null;
        String line = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
        String paramsStr = buffer.toString();
        return new Gson().fromJson(paramsStr, clazz);
    }

    public JsonElement getJsonElementParams(HttpServletRequest req) throws Exception {
        BufferedReader reader = null;
        String line = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
        String paramsStr = buffer.toString();
        JsonParser parser = new JsonParser();

        JsonElement params = parser.parse(paramsStr);
        return params;
    }

    protected Boolean checkNecessaryParam(JsonObject params, String necessaryParam[]) {
        if (params == null) return false;
        for (String param_name : necessaryParam) {
            if (!params.has(param_name)) {
                return false;
            }
        }
        return true;
    }

    protected HttpServletResponse setResponseContent(HttpServletResponse res, Object content) {
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = null;
        String contentJson = gson.toJson(content);
        try {
            writer = res.getWriter();
            writer.append(contentJson);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
        return res;
    }

    private void setErrorResContent(HttpServletResponse res, String str, int code) {
        ResponseContent content = new ResponseContent();
        ErrorMessage msg = new ErrorMessage(code, str);
        content.setError_message(msg);
        setResponseContent(res, content);
    }

    /*系统错, StateCode = 500*/
    protected void setSystemErrorRes(HttpServletResponse res, String str, int code) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        setErrorResContent(res, str, code);
    }

    /*条件错, StateCode = 200*/
    protected void setParamWarnRes(HttpServletResponse res, String str, int code) {
        res.setStatus(HttpServletResponse.SC_OK);
        setErrorResContent(res, str, code);
    }

    /*参数错, StateCode = 400*/
    protected void setRequestErrorRes(HttpServletResponse res) {
        setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
    }

    /*参数错, StateCode = 400*/
    protected void setRequestErrorRes(HttpServletResponse res, String str, int code) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        setErrorResContent(res, str, code);
    }

    protected void setAjaxRes(HttpServletResponse res, Object obj, int res_status) {
        res.setStatus(res_status);
        setResponseContent(res, obj);
    }

    /*Ajax 400错*/
    protected void setAjaxResE400(HttpServletResponse res, String str) {
        ResponseContent content = new ResponseContent();
        ErrorMessage msg = new ErrorMessage(str);
        content.setError_message(msg);
        setAjaxRes(res, content, HttpServletResponse.SC_BAD_REQUEST);
    }

    /*Ajax 500错*/
    protected void setAjaxResE500(HttpServletResponse res, String str) {
        ResponseContent content = new ResponseContent();
        ErrorMessage msg = new ErrorMessage(str);
        content.setError_message(msg);
        setAjaxRes(res, content, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    protected void setPermissionErrorRes(HttpServletResponse res, String str, int code) {
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        setErrorResContent(res, str, code);
    }
}
