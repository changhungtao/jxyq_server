package com.jxyq.commons.beans;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 6386020104518399989L;

    private JsonResult(int initialCapacity)
    {
        super(initialCapacity);
    }

    private JsonResult(Map<String, Object> map)
    {
        super(map);
    }

    public static JsonResult Json()
    {
        return null;
    }

    public static JsonResult Json(String errorCode)
    {
        if (StringUtils.isEmpty(errorCode))
        {
            return null;
        }

        JsonResult result = new JsonResult(1);
        result.put("errorCode", errorCode);

        return result;
    }

    public static JsonResult Json(Map<String, Object> map)
    {
        JsonResult result = new JsonResult(1);
        result.put("map", map);

        return result;
    }

    public static JsonResult Json(int total, List<Map<String, Object>> list)
    {
        JsonResult result = new JsonResult(2);
        result.put("total", total);
        result.put("rows", list);

        return result;
    }
    public static JsonResult Json(List<Map<String, Object>> list)
    {
        JsonResult result = new JsonResult(1);
        result.put("manulist", list);

        return result;
    }

    public static JsonResult Json(List<Object> list, int total)
    {
        JsonResult result = new JsonResult(2);
        result.put("total", total);
        result.put("rows", list);

        return result;
    }
}
