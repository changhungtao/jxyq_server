/*
 * 文件名：OracleDialect.java
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

import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.SortDirection;

import java.util.Iterator;
import java.util.Map;

/**
 * Oracle的方言实现
 */
public class OracleDialect implements Dialect
{
    @Override
    public boolean supportsPaging()
    {
        return true;
    }

    @Override
    public String getPagingSql(String sql, int offset, int limit, Map<String, SortDirection> sortField,String dbModel)
    {
        StringBuffer sqlBuffer = null;
        
        if (null == sortField || sortField.isEmpty())
        {
            sqlBuffer = new StringBuffer(sql);
        }
        else
        {
            sqlBuffer = new StringBuffer(StringUtils.substringBeforeLast(sql.toUpperCase(), "ORDER"));
            sqlBuffer.append(" ORDER BY ");
            Iterator<Map.Entry<String, SortDirection>> iterator = sortField.entrySet().iterator();
            Map.Entry<String, SortDirection> entry = null;
            String column = null;
            
            while (iterator.hasNext())
            {
                entry = iterator.next();
                column = StringUtils.camelCaseToSnakeCase(entry.getKey());
                
                sqlBuffer.append(column.toUpperCase()).append(" ").append(entry.getValue()).append(",");
            }
            
            sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        }
        
        if(StringUtils.isNotBlank(dbModel)&&"mysql".equals(dbModel)){
        	  sqlBuffer.append(" limit ").append(offset).append(",").append(limit);  
        }else
        {
        	sqlBuffer.insert(0, "SELECT t_.*, rownum r_ FROM (").append(") t_ WHERE rownum <= ").append(offset + limit);  
            sqlBuffer.insert(0, "SELECT * FROM (").append(") WHERE r_ > ").append(offset);  
        }
        //sqlBuffer.insert(0, "SELECT * FROM (").append(") WHERE r_ > ").append(offset);  
        
        // 上面的Sql语句拼接之后大概是这个样子：  
        // select * from (select t_.*, rownum r_ from (select * from sys_user) t_  
        // where rownum <= 10) where r_ > 0
        return sqlBuffer.toString();  
    }

    @Override
    public String getCountSql(String sql)
    {
        /*String suffix = StringUtils.substringAfter(sql, "from");
        if (StringUtils.isBlank(suffix)) {
            suffix = StringUtils.substringAfter(sql.toUpperCase(), "FROM");
        }*/
        return "SELECT COUNT(1) FROM (" + sql + ") count";
    }
}