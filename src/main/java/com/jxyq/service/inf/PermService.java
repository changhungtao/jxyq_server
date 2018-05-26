package com.jxyq.service.inf;

import com.jxyq.model.doctor.DataPermission;
import com.jxyq.model.doctor.Filter;

import java.util.List;
import java.util.Map;

public interface PermService {
    List<DataPermission> selDataPermissionByDoctorId(int doctor_id);

    List<DataPermission> selDataPermByPage(Map<String, Object> map);

    List<Filter> selFilterByPermId(int data_permission_id);

    List<DataPermission> selDataPermissionByPage(Map<String, Object> map);

    DataPermission selDataPermissionById(int data_permission_id);

    void inDataPermission(DataPermission perm);

    void inFilter(Filter filter);

    void delFilterByPermId(int data_permission_id);

    void inDoctorDataPermission(Map<String, Object> map);

    void delDoctorPermissionByPermId(int data_permission_id);

    void delDataPermissionById(int data_permission_id);

    List<Map<String, Object>> selDocPermission(int doctor_id, int data_permission_id);
}
