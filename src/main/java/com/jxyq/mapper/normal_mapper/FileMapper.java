package com.jxyq.mapper.normal_mapper;

import java.util.Map;

public interface FileMapper {
    int insertFile(Map<String, Object> map);
    Map<String,Object> selectFileById(Map<String, Object> map);
}
