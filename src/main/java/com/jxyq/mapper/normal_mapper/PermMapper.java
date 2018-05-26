package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.doctor.DataPermission;
import com.jxyq.model.doctor.Filter;

import java.util.List;
import java.util.Map;

public interface PermMapper {
    List<DataPermission> selDataPermByPage(Map<String, Object> map);

    List<DataPermission> selDataPermission(Map<String, Object> map);

    List<Filter> selFilter(Map<String, Object> map);

    List<DataPermission> selDataPermissionByPage(Map<String, Object> map);

    void inDataPermission(DataPermission perm);

    void inFilter(Filter filter);

    void inDoctorDataPermission(Map<String, Object> map);

    void delFilter(Map<String, Object> map);

    void delDoctorPermission(Map<String, Object> map);
    
    void delDataPermissionById(Map<String, Object> map);

    DataPermission selDataPermissionById(Map<String, Object> map);

    List<Map<String, Object>> selDocPermission(Map<String, Object> map);

}
