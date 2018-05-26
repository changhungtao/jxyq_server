package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.PermMapper;
import com.jxyq.model.doctor.DataPermission;
import com.jxyq.model.doctor.Filter;
import com.jxyq.service.inf.PermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermServiceImpl implements PermService {
    @Autowired
    private PermMapper permMapper;

    @Override
    public List<DataPermission> selDataPermissionByDoctorId(int doctor_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("doctor_id", doctor_id);
        return permMapper.selDataPermission(map);
    }

    @Override
    public List<DataPermission> selDataPermByPage(Map<String, Object> map) {
        return permMapper.selDataPermByPage(map);
    }

    @Override
    public List<Filter> selFilterByPermId(int data_permission_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("data_permission_id", data_permission_id);
        return permMapper.selFilter(map);
    }

    @Override
    public List<DataPermission> selDataPermissionByPage(Map<String, Object> map) {
        return permMapper.selDataPermissionByPage(map);
    }

    @Override
    public void inDataPermission(DataPermission perm) {
        permMapper.inDataPermission(perm);
    }

    @Override
    public void inFilter(Filter filter) {
        permMapper.inFilter(filter);
    }

    @Override
    public void inDoctorDataPermission(Map<String, Object> map) {
        permMapper.inDoctorDataPermission(map);
    }

    @Override
    public void delFilterByPermId(int data_permission_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("data_permission_id", data_permission_id);
        permMapper.delFilter(map);
    }

    @Override
    public void delDoctorPermissionByPermId(int data_permission_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("data_permission_id", data_permission_id);
        permMapper.delDoctorPermission(map);
    }

    @Override
    public DataPermission selDataPermissionById(int data_permission_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("data_permission_id", data_permission_id);
        return permMapper.selDataPermissionById(map);
    }

    @Override
    public List<Map<String, Object>> selDocPermission(int doctor_id, int data_permission_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("doctor_id", doctor_id);
        map.put("data_permission_id", data_permission_id);
        return permMapper.selDocPermission(map);
    }

    @Override
    public void delDataPermissionById(int data_permission_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("data_permission_id", data_permission_id);
        permMapper.delDataPermissionById(map);
    }
}
