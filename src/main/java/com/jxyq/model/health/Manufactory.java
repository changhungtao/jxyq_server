package com.jxyq.model.health;

import com.jxyq.model.Role;

import java.util.List;

public class Manufactory {
    private Integer manufactory_id;
    private String login_name;
    private String password;
    private String contactor;

    private int department;
    private String telephone;
    private String phone;
    private String email;

    private String full_name;
    private String code;
    private String profile;
    private String logo_url;

    private int province_id;
    private int city_id;
    private int zone_id;
    private String address;

    private int members;
    private int industry;
    private int nature;
    private String business_licence;

    private String internal_certificate;
    private String local_certificate;
    private String code_certificate;
    private long registered_at;

    private long updated_at;
    private String creator;
    private int status;
    private List<Role> roleList;

    private List<Integer> device_type_ids;

    public Integer getManufactory_id() {
        return manufactory_id;
    }

    public void setManufactory_id(Integer manufactory_id) {
        this.manufactory_id = manufactory_id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public int getNature() {
        return nature;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }

    public String getBusiness_licence() {
        return business_licence;
    }

    public void setBusiness_licence(String business_licence) {
        this.business_licence = business_licence;
    }

    public String getInternal_certificate() {
        return internal_certificate;
    }

    public void setInternal_certificate(String internal_certificate) {
        this.internal_certificate = internal_certificate;
    }

    public String getLocal_certificate() {
        return local_certificate;
    }

    public void setLocal_certificate(String local_certificate) {
        this.local_certificate = local_certificate;
    }

    public String getCode_certificate() {
        return code_certificate;
    }

    public void setCode_certificate(String code_certificate) {
        this.code_certificate = code_certificate;
    }

    public long getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(long registered_at) {
        this.registered_at = registered_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Integer> getDevice_type_ids() {
        return device_type_ids;
    }

    public void setDevice_type_ids(List<Integer> device_type_ids) {
        this.device_type_ids = device_type_ids;
    }
}