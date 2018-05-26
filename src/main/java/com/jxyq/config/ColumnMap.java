/*
 * 文件名：ColumnMap.java
 * 版权：Copyright 2014-2020 shiguwen Info Tech. Co. Ltd. All Rights Reserved. 
 * 描述： IM V100R001 企业微信服务平台
 * 修改人： zhangli
 * 修改时间：2014-5-31
 * 修改内容：新增 
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.jxyq.config;

import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;

public class ColumnMap extends HashMap<String, HashMap<String, String>>
{
    private static final long serialVersionUID = -1145724396537217887L;

    public ColumnMap(SqlSessionFactory sqlSessionFactory, List<String> resultMaps)
    {   
        HashMap<String, String> columnMap = null;
        
        for (String id : resultMaps)
        {
            columnMap = new HashMap<String, String>();
            
            List<ResultMapping> mappings = sqlSessionFactory.getConfiguration().getResultMap(id).getResultMappings();

            for (ResultMapping m : mappings)
            {
               
                columnMap.put(m.getProperty(), m.getColumn());
            }
            
            this.put(id, columnMap);
        }
    }
}