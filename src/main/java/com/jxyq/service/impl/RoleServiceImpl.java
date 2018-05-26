package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.RoleMapper;
import com.jxyq.model.Role;
import com.jxyq.service.inf.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role selRoleByPerm(String perm) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("permissions", perm);
        return roleMapper.selRole(map);
    }

    @Override
    public Role selRoleByName(String name) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("name", name);
        return roleMapper.selRole(map);
    }
}
