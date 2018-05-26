package com.jxyq.model.health;

public class ProductType  {
    private long product_type_id;
    private String name;
    private long registered_at;
    private int status;

    public long getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(long product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
