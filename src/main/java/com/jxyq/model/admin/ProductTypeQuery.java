package com.jxyq.model.admin;


import com.jxyq.commons.mybatis.pagination.dialect.PaginationQuery;

import java.util.List;

/**
 * Created by wujj-fnst on 2015/4/16.
 */
public class ProductTypeQuery extends PaginationQuery {
    private List<Integer> product_type_ids;
    private String name;
    private long registered_at;
    private int status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getProduct_type_ids() {
        return product_type_ids;
    }

    public void setProduct_type_ids(List<Integer> product_type_ids) {
        this.product_type_ids = product_type_ids;
    }

    public long getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(long registered_at) {
        this.registered_at = registered_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
