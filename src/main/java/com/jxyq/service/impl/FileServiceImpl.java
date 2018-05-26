package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.FileMapper;
import com.jxyq.service.inf.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Override
    public void insertFile(Map<String,Object> map) {
        fileMapper.insertFile(map);
    }

    @Override
    public Map<String, Object> selectFileById(int file_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("file_id", file_id);
        return fileMapper.selectFileById(map);
    }
}
