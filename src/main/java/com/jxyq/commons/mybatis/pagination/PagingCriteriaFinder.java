/*
 * 文件名：PagingCriteriaFinder.java
 * 版权：Copyright 2014-2020 shiguwen Info Tech. Co. Ltd. All Rights Reserved. 
 * 描述： IM V100R001 企业微信服务平台
 * 修改人： zhangli
 * 修改时间：2014-5-31
 * 修改内容：新增 
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.jxyq.commons.mybatis.pagination;

import java.util.Map;

public enum PagingCriteriaFinder
{
    instance;

    private PagingCriteriaFinder()
    {

    }

    public PagingCriteria find(Object object)
    {
        if (object == null)
        {
            return null;
        }

        return findFromObject(object);
    }

    private PagingCriteria findFromObject(Object object)
    {
        PagingCriteria p = null;

        if (object instanceof PagingCriteria)
        {
            p = (PagingCriteria) object;
        }
        else if (object instanceof Map)
        {
            p = findFromMap((Map<?, ?>) object);
        }
        else
        {
            p = null;
        }

        return p;
    }

    private PagingCriteria findFromMap(Map<?, ?> map)
    {
        PagingCriteria p = null;

        for (Object o : map.values())
        {
            p = findFromObject(o);

            if (null != p)
            {
                return p;
            }
        }

        return null;
    }
}