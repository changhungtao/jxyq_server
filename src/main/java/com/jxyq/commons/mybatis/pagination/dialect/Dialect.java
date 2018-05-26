/*
 * 文件名：Dialect.java
 * 版权：Copyright 2014-2020 shiguwen Info Tech. Co. Ltd. All Rights Reserved. 
 * 描述： IM V100R001 企业微信服务平台
 * 修改人： zhangli
 * 修改时间：2014-5-31
 * 修改内容：新增 
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.jxyq.commons.mybatis.pagination.dialect;

import com.jxyq.commons.mybatis.pagination.SortDirection;

import java.util.Map;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 */
public interface Dialect {

    /**
     * 数据库本身是否支持分页当前的分页查询方式
     * 如果数据库不支持的话，则不进行数据库分页
     *
     * @return true：支持当前的分页查询方式
     */
    public boolean supportsPaging();

    /**
     * 将sql转换为分页SQL，分别调用分页sql
     *
     * @param sql    SQL语句
     * @param offset 开始条数
     * @param limit  每页显示多少纪录条数
     * @param sort  排序的字段
     * @param order  asc or desc
     * @return 分页查询的sql
     */
    public String getPagingSql(String sql, int offset, int limit, Map<String, SortDirection> sortField, String dmModel);

    /**
     * 将sql转换为总记录数SQL
     * @param sql SQL语句
     * @return 总记录数的sql
     */
    public String getCountSql(String sql);
}
