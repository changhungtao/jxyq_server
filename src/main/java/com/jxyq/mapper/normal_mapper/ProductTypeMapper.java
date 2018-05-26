package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.Role;
import com.jxyq.model.admin.ProductTypeQuery;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.health.ProductType;

import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/4/16.
 */
public interface ProductTypeMapper {
    //    db  product
    List<Map<String,Object>> selProductTypeByPage(Map<String,Object> map);

    void insertProductType(ProductType productType);
    List<Map<String,Object>> selProductDetail(int id);

    void upProductTypeStatus(Map<String, Object> map);

    void upProductTypeInf(Map<String,Object> map);
    void  upProductName(Map<String,Object> map);
    void delProductType(Map<String, Object> map);
}
