package com.jxyq.model.health;

public class TerminalCatagoryTemplate {
    private long terminal_catagory_template_id;
    private long terminal_catagory_id;
    private String template;
    private int template_type;
    private int manufactory_id;
    private int product_type_id;
    private int device_type_id;
    private long uploaded_at;
    private int status;

    public long getTerminal_catagory_template_id() {
        return terminal_catagory_template_id;
    }

    public void setTerminal_catagory_template_id(long terminal_catagory_template_id) {
        this.terminal_catagory_template_id = terminal_catagory_template_id;
    }

    public long getTerminal_catagory_id() {
        return terminal_catagory_id;
    }

    public void setTerminal_catagory_id(long terminal_catagory_id) {
        this.terminal_catagory_id = terminal_catagory_id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(int template_type) {
        this.template_type = template_type;
    }

    public int getManufactory_id() {
        return manufactory_id;
    }

    public void setManufactory_id(int manufactory_id) {
        this.manufactory_id = manufactory_id;
    }

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public int getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(int device_type_id) {
        this.device_type_id = device_type_id;
    }

    public long getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(long uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
