package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.FileMapper;
import com.jxyq.mapper.normal_mapper.OperationLogMapper;
import com.jxyq.model.others.OperationLog;
import com.jxyq.service.inf.FileService;
import com.jxyq.service.inf.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void inOperationLog(OperationLog log) {
        operationLogMapper.inOperationLog(log);
    }

    @Override
    public List<OperationLog> qryOperationLogByPage(Map<String, Object> map) {
        return operationLogMapper.qryOperationLogByPage(map);
    }

    @Override
    public OperationLog selLastSignInOp(int user_role, int user_id, int op_title, int offset) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("user_role", user_role);
        map.put("user_id", user_id);
        map.put("op_title", op_title);
        map.put("offset", offset);
        return operationLogMapper.selLastSignInOp(map);
    }
}
