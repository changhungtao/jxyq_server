package com.jxyq.service.inf;

import com.jxyq.model.others.ConstantDescription;

import java.util.List;
import java.util.Map;

public interface ConstantService {
    ConstantDescription selDescriptionByConstant(String catagory, int constant);

    ConstantDescription selConstantByExtra(String catagory, String extra);

    List<ConstantDescription> selDescriptionByCategory(String catagory);

    List<Map<String, Object>> qryDepartment();
}
