package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.others.ConstantDescription;

import java.util.List;
import java.util.Map;

public interface ConstantMapper {
    ConstantDescription selConstantDescription(Map<String, Object> map);

    List<ConstantDescription> selConstantDescriptionList(Map<String, Object> map);

    List<Map<String, Object>> qryDepartment();
}
