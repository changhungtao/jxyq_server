/*
 * 文件名：PagingCriteria.java
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

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;



public class PagingCriteria implements Serializable
{
    private static final long serialVersionUID = -3268034789084546269L;

    private int pageIndex;

    private int pageSize;

    private int total;

    private LinkedHashMap<String, SortDirection> sortField;

    private HashMap<String, Object> condition;

    public PagingCriteria(int pageIndex, int pageSize, String field,
                          String direction)
    {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;

        setCondition(new HashMap<String, Object>());
        addSortField(field, direction);
    }
    public PagingCriteria(int pageIndex, int pageSize)
    {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;

        setCondition(new HashMap<String, Object>());
    }
    public void addSortField(String field, String direction)
    {
        if (StringUtils.isNotEmpty(field) && StringUtils.isNotEmpty(direction))
        {
            addSortField(field, SortDirection.valueOfCaseInsensitive(direction));
        }
    }

    public void addSortField(String field, SortDirection direction)
    {
        if (null == sortField)
        {
            sortField = new LinkedHashMap<String, SortDirection>();
        }

        if ( !sortField.containsKey(field))
        {
            sortField.put(field, direction);
        }
    }

    public Map<String, SortDirection> getSortField()
    {
        return sortField;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getOffset()
    {
        return pageSize * pageIndex;
    }

    public int getLimit()
    {
        return pageSize;
    }

    public HashMap<String, Object> getCondition()
    {
        return condition;
    }

    public void setCondition(HashMap<String, Object> condition)
    {
        this.condition = condition;
    }
}
