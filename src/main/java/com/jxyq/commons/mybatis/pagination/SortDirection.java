/*
 * 文件名：SortDirection.java
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

public enum SortDirection
{
    ASC("ASC"), DESC("DESC");

    private String direction;

    private SortDirection(String direction)
    {
        this.direction = direction;
    }

    public static SortDirection valueOfCaseInsensitive(String value)
    {
        String valueUpper = value.toUpperCase();
        return SortDirection.valueOf(valueUpper);
    }

    public String getDirection()
    {
        return this.direction;
    }
}
