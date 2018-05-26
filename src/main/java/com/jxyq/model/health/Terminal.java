package com.jxyq.model.health;

public class Terminal {
    private long terminal_id;
    private String name;
    private String mac_address;
    private String uuid;
    private long manufactory_id;
    private long product_type_id;
    private long device_type_id;
    private long terminal_catagory_id;
    private int status;
    private long activated_at;

    public long getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(long terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getManufactory_id() {
        return manufactory_id;
    }

    public void setManufactory_id(long manufactory_id) {
        this.manufactory_id = manufactory_id;
    }

    public long getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(long product_type_id) {
        this.product_type_id = product_type_id;
    }

    public long getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(long device_type_id) {
        this.device_type_id = device_type_id;
    }

    public long getTerminal_catagory_id() {
        return terminal_catagory_id;
    }

    public void setTerminal_catagory_id(long terminal_catagory_id) {
        this.terminal_catagory_id = terminal_catagory_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getActivated_at() {
        return activated_at;
    }

    public void setActivated_at(long activated_at) {
        this.activated_at = activated_at;
    }
}
