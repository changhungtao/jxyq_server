package com.jxyq.service.impl;

import com.jxyq.mapper.watch_mapper.TestMapper;
import com.jxyq.service.inf.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public Map<String, Object> selTestById(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        return testMapper.selTestById(map);
    }
}
