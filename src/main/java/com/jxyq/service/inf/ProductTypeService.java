package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.admin.ProductTypeQuery;
import com.jxyq.model.health.ProductType;

import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/4/1.
 */
public interface ProductTypeService {
    //    db product分页操作
    List<Map<String,Object>> selProducTypeByPage(Map<String, Object> map, PagingCriteria pageInf);

    List<Map<String,Object>> selProductDetail(int id);

    void insertProductType(ProductType productType);

    void upProductTypeInf(Map<String,Object>map);
    void upProductName(int id,String name);

    void delProductType(int id);
}
