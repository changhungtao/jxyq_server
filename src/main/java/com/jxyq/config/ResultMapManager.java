package com.jxyq.config;

import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ResultMapManager
{
    private SqlSessionFactory sqlSessionFactory;

    public ResultMapping getResultMapping(String resultMapId, String property)
    {
        List<ResultMapping> mappings = sqlSessionFactory.getConfiguration().getResultMap(resultMapId).getResultMappings();

        for (ResultMapping m : mappings)
        {
            if (property.equals(m.getProperty()))
            {
                return m;
            }
        }

        return null;
    }
    
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
