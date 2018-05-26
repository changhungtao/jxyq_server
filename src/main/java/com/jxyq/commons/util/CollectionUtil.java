package com.jxyq.commons.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil
{
    public static Map<String, Object> convertList2Map(List<Map<String, Object>> param)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        for (int i = 0; i < param.size(); i++ )
        {
            Set<String> set = param.get(i).keySet();
            for (String key : set)
            {
                if (null != result.get(key))
                {
                    ((Object[])result.get(key))[i] = param.get(i).get(key);
                }
                else
                {
                    Object[] values = new Object[param.size()];
                    values[i] = param.get(i).get(key);
                    result.put(key, values);
                }
            }
        }
        return result;
    }

    public static List<Map<String, Object>> convertMap2List(Map<String, Object> param)
    {
        Set<String> keySet = param.keySet();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < ((ArrayList)param.get("workNo")).size(); i++ )
        {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : keySet)
            {
                map.put(key, ((ArrayList<Object>)param.get(key)).get(i));
            }
            resultList.add(map);
        }
        return resultList;
    }

}
