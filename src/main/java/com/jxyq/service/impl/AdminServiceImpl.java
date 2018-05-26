package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.AccountMapper;
import com.jxyq.mapper.normal_mapper.AdministratorMapper;
import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.admin.AdministratorDistrict;
import com.jxyq.service.inf.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Administrator selAdminByLoginName(String login_name) {
        Administrator admin = new Administrator();
        admin.setLogin_name(login_name);
        return administratorMapper.selAdminByLoginName(admin);
    }

    @Override
    public List<Map<String, Object>> selManagerByPage(Map<String, Object> map) {

        return administratorMapper.selManagerByPage(map);
    }

    @Override
    public Administrator selAdminById(int id) {
        Administrator admin = new Administrator();
        admin.setAdministrator_id(id);
        return administratorMapper.selAdminById(admin);
    }

    @Override
    public Administrator selAdminWithRoleByLoginName(String login_name) {
        Administrator admin = selAdminByLoginName(login_name);
        if (admin == null) return null;
        admin.setRoleList(administratorMapper.selRolesByAdmin(admin));
        return admin;
    }

    @Override
    public void updaAdminInfo(Map<String, Object> map) {
        administratorMapper.updaAdminInfo(map);
    }

    @Override
    public void updaAdminPass(Administrator admin) {
        administratorMapper.updaAdminPass(admin);
    }

    //   measurements
    @Override
    public List<Map<String, Object>> selMeasurementsByPage(Map<String, Object> map) {
        return administratorMapper.selMeasurementsByPage(map);
    }

    //    Wristbands
    @Override
    public Map<String, Object> getWristbands(int id) {
        return administratorMapper.getWristbands(id);
    }

    @Override
    public void editWristbands(Map<String, Object> map) {
        administratorMapper.editWristbands(map);
    }

    //    Sphygmomanometers
    @Override
    public Map<String, Object> getSphygmomanometers(int id) {
        return administratorMapper.getSphygmomanometers(id);
    }

    @Override
    public void editSphygmomanometers(Map<String, Object> map) {
        administratorMapper.editSphygmomanometers(map);
    }

    //    Oximeter
    @Override
    public Map<String, Object> getOximeter(int id) {
        return administratorMapper.getOximeter(id);
    }

    @Override
    public void editOximeter(Map<String, Object> map) {
        administratorMapper.editOximeter(map);
    }

    //    Glucosemeters
    @Override
    public Map<String, Object> getGlucosemeters(int id) {
        return administratorMapper.getGlucosemeters(id);
    }

    @Override
    public void editGlucosemeters(Map<String, Object> map) {
        administratorMapper.editGlucosemeters(map);
    }

    //   Thermometers
    @Override
    public Map<String, Object> getThermometers(int id) {
        return administratorMapper.getThermometers(id);
    }

    @Override
    public void editThermometers(Map<String, Object> map) {
        administratorMapper.editThermometers(map);
    }


    //    通用接口
    @Override
    public List<Map<String, Object>> selDistricts() {
        return administratorMapper.selDistricts();
    }

    @Override
    public List<Map<String, Object>> selProvinces(Map<String, Object> map) {
        return administratorMapper.selProvinces(map);
    }

    @Override
    public Map<String, Object> selProvinces(int province_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("province_id", province_id);
        List<Map<String, Object>> province_list = administratorMapper.selProvinces(map);
        if (province_list == null || province_list.size() <= 0) return null;
        return province_list.get(0);
    }

    @Override
    public List<Map<String, Object>> selCities(Map<String, Object> map) {
        return administratorMapper.selCities(map);
    }

    @Override
    public List<Map<String, Object>> selZones(Map<String, Object> map) {
        return administratorMapper.selZones(map);
    }

    @Override
    public List<Map<String, Object>> selProduct_types() {
        return administratorMapper.selProduct_types();
    }

    @Override
    public List<Map<String, Object>> selDevice_types(Map<String, Object> map) {
        return administratorMapper.selDevice_types(map);
    }

    //    页面设置
    @Override
    public Map<String, Object> selWeb_templates(Map<String, Object> map) {
        return administratorMapper.selWeb_templates(map);
    }

    @Override
    public void upWeb_templates(Map<String, Object> map) {
        administratorMapper.upWeb_templates(map);
    }

    @Override
    public List<Map<String, Object>> selExhibition() {
        return administratorMapper.selExhibition();
    }

    @Override
    public List<Map<String, Object>> qryExhibitionByPage(Map<String, Object> map) {
        return administratorMapper.qryExhibitionByPage(map);
    }

    @Override
    public void addExhibition(Map<String, Object> map) {
        administratorMapper.addExhibition(map);
    }

    @Override
    public void addExhibitionRes(Map<String, Object> map) {
        administratorMapper.addExhibitionRes(map);
    }

    @Override
    public void upExhibitionRes(Map<String, Object> map) {
        administratorMapper.upExhibitionRes(map);
    }

    @Override
    public void upExhibition(Map<String, Object> map) {
        administratorMapper.upExhibition(map);
    }

    @Override
    public void delExhibition(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        administratorMapper.delExhibition(map);
    }

    @Override
    public List<Map<String, Object>> qryNewsByPage(Map<String, Object> map) {
        return administratorMapper.qryNewsByPage(map);
    }

    @Override
    public void addNews(Map<String, Object> map) {
        administratorMapper.addNews(map);
    }

    @Override
    public void addNewsRes(Map<String, Object> map) {
        administratorMapper.addNewsRes(map);
    }

    @Override
    public void upNewsRes(Map<String, Object> map) {
        administratorMapper.upNewsRes(map);
    }

    @Override
    public void upNews(Map<String, Object> map) {
        administratorMapper.upNews(map);
    }

    @Override
    public void delNewsResources(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        administratorMapper.delNewsResources(map);
    }

    //    信息推送
    @Override
    public void postMessage(Map<String, Object> map) {
        administratorMapper.postMessage(map);
    }

    @Override
    public List<Map<String, Object>> postCatagory(Map<String, Object> map) {
        return administratorMapper.postCatagory(map);
    }

    @Override
    public List<Map<String, Object>> qryCategoryByPage(Map<String, Object> map) {
        return administratorMapper.qryCategoryByPage(map);
    }

    @Override
    public void delAdministrator(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        administratorMapper.delAdministrator(map);

    }

    @Override
    public void inAdmin(Map<String, Object> map) {
        administratorMapper.inAdmin(map);
    }

    @Override
    public AdministratorDistrict selAdminDistrict(int mid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("administrator_id", mid);
        return administratorMapper.selAdminDistrict(map);
    }

    @Override
    public List<Integer> selAdminDevice(int mid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("administrator_id", mid);
        return administratorMapper.selAdminDevice(map);
    }

    @Override
    public List<Role> qryAdminRoleByAdminId(int mid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("administrator_id", mid);
        return administratorMapper.qryAdminRoleByAdminId(map);
    }

    @Override
    public Map<String, Object> selRolePermission(Map<String, Object> map) {
        return administratorMapper.selRolePermission(map);
    }

    @Override
    public void delAdminDistrict(Map<String, Object> map) {
        administratorMapper.delAdminDistrict(map);
    }

    @Override
    public void inAdminDistrict(Map<String, Object> map) {
        administratorMapper.inAdminDistrict(map);
    }

    @Override
    public void delAdminDevice(Map<String, Object> map) {
        administratorMapper.delAdminDevice(map);
    }

    @Override
    public void inAdminDevice(Map<String, Object> map) {
        administratorMapper.inAdminDevice(map);
    }

    @Override
    public void upAdminPermission(Map<String, Object> map) {
        administratorMapper.upAdminPermission(map);
    }

    @Override
    public void delAdminRoleByAdminId(int mid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("administrator_id", mid);
        administratorMapper.delAdminRoleByAdminId(map);
    }

    @Override
    public void inAdminRole(Map<String, Object> map) {
        administratorMapper.inAdminRole(map);

    }
}