package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.ProductTypeMapper;
import com.jxyq.model.health.ProductType;
import com.jxyq.service.inf.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/4/1.
 */
@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    private ProductTypeMapper productTypeMapper;

    //DB  分页操作
//    db product操作
    @Override
    public List<Map<String, Object>> selProducTypeByPage(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return productTypeMapper.selProductTypeByPage(map);
    }

    @Override
    public List<Map<String, Object>> selProductDetail(int id) {
        return productTypeMapper.selProductDetail(id);
    }


    @Override
    public void insertProductType(ProductType productType) {
        productTypeMapper.insertProductType(productType);
    }

    @Override
    public void upProductTypeInf(Map<String, Object> map) {
        productTypeMapper.upProductTypeInf(map);
    }

    @Override
    public void upProductName(int id, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        productTypeMapper.upProductName(map);
    }

    @Override
    public void delProductType(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("product_type_id", id);
        productTypeMapper.delProductType(map);
    }
}
