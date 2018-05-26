package com.jxyq.service.inf;

import com.jxyq.model.others.OperationLog;

import java.util.List;
import java.util.Map;

public interface OperationLogService {
    List<OperationLog> qryOperationLogByPage(Map<String, Object> map);

    void inOperationLog(OperationLog log);

    OperationLog selLastSignInOp(int user_role, int user_id, int op_title, int offset);
}
