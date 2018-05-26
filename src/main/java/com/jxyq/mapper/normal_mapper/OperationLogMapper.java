package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.others.OperationLog;

import java.util.List;
import java.util.Map;

public interface OperationLogMapper {
    List<OperationLog> qryOperationLogByPage(Map<String, Object> map);

    void inOperationLog(OperationLog log);

    OperationLog selLastSignInOp(Map<String, Object> map);
}
