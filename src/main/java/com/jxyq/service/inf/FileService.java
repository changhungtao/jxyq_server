package com.jxyq.service.inf;

import java.util.Map;

public interface FileService {
    void insertFile(Map<String,Object> map);
    Map<String,Object> selectFileById(int file_id);
}
