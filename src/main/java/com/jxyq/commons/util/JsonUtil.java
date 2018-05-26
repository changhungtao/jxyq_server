package com.jxyq.commons.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    static boolean encodeFlag = false;

    public static String Object2EncodeJsonSting(Object object) {
        String jsonStr = gson.toJson(object);
        return jsonStr;
    }

    public static String Object2JsonSting(Object object) {
        String jsonStr = gson.toJson(object);
        return jsonStr;
    }

    public static Object JsonSting2DecodeObject(String jsonStr, Class type) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Object o = null;
        try {
            o = gson.fromJson(jsonStr, type);
        } catch (Exception e) {
            return null;
        }
        return o;
    }

    public static Map<String, Object> JsonSting2Map(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Map<String, Object> o = null;
        try {
            o = gson.fromJson(jsonStr, new HashMap<String, Object>().getClass());
        } catch (Exception e) {
            return null;
        }
        return o;
    }
}
