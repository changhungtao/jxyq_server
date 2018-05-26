package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.ConstantMapper;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.service.inf.ConstantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConstantServiceImpl implements ConstantService {
    @Autowired
    private ConstantMapper constantMapper;

    @Override
    public ConstantDescription selDescriptionByConstant(String catagory, int constant) {
        Map<String, Object> map = new HashMap<>();
        map.put("catagory", catagory);
        map.put("constant", constant);
        return constantMapper.selConstantDescription(map);
    }

    @Override
    public ConstantDescription selConstantByExtra(String catagory, String extra) {
        Map<String, Object> map = new HashMap<>();
        map.put("catagory", catagory);
        map.put("extra", extra);
        return constantMapper.selConstantDescription(map);
    }

    @Override
    public List<ConstantDescription> selDescriptionByCategory(String catagory) {
        Map<String, Object> map = new HashMap<>();
        map.put("catagory", catagory);
        return constantMapper.selConstantDescriptionList(map);
    }

    @Override
    public List<Map<String, Object>> qryDepartment() {
        return constantMapper.qryDepartment();
    }
}
