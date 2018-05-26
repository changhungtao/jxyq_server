package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.admin.AdministratorDistrict;

import java.util.List;
import java.util.Map;

public interface AdministratorMapper {
    Administrator selAdminByLoginName(Administrator admin);

    List<Map<String, Object>> selManagerByPage(Map<String, Object> map);

    List<Role> selRolesByAdmin(Administrator admin);

    Administrator selAdminById(Administrator admin);

    void updaAdminInfo(Map<String, Object> map);

    void updaAdminPass(Administrator admin);

    List<Map<String, Object>> selMeasurementsByPage(Map<String, Object> map);

    //      数据库测量类
    Map<String, Object> getWristbands(int id);

    void editWristbands(Map<String, Object> map);

    Map<String, Object> getSphygmomanometers(int id);

    void editSphygmomanometers(Map<String, Object> map);

    Map<String, Object> getOximeter(int id);

    void editOximeter(Map<String, Object> map);

    Map<String, Object> getGlucosemeters(int id);

    void editGlucosemeters(Map<String, Object> map);

    Map<String, Object> getThermometers(int id);

    void editThermometers(Map<String, Object> map);

    //通用接口
    List<Map<String, Object>> selDistricts();

    List<Map<String, Object>> selProvinces(Map<String, Object> map);

    List<Map<String, Object>> selCities(Map<String, Object> map);

    List<Map<String, Object>> selZones(Map<String, Object> map);

    List<Map<String, Object>> selProduct_types();

    List<Map<String, Object>> selDevice_types(Map<String, Object> map);

    //    获取宣传页
    Map<String, Object> selWeb_templates(Map<String, Object> map);

    void upWeb_templates(Map<String, Object> map);

    List<Map<String, Object>> selExhibition();

    List<Map<String, Object>> qryExhibitionByPage(Map<String, Object> map);

    void addExhibition(Map<String, Object> map);

    void addExhibitionRes(Map<String, Object> map);

    void upExhibitionRes(Map<String, Object> map);

    void upExhibition(Map<String, Object> map);

    void delExhibition(Map<String, Object> map);

    List<Map<String, Object>> selNews();

    List<Map<String, Object>> qryNewsByPage(Map<String, Object> map);

    void addNews(Map<String, Object> map);

    void addNewsRes(Map<String, Object> map);

    void upNewsRes(Map<String, Object> map);

    void upNews(Map<String, Object> map);

    void delNewsResources(Map<String, Object> map);

    //信息推送
    void postMessage(Map<String, Object> map);

    List<Map<String, Object>> postCatagory(Map<String, Object> map);

    List<Map<String, Object>> qryCategoryByPage(Map<String, Object> map);

    void delAdministrator(Map<String, Object> map);

    void inAdmin(Map<String, Object> map);

    //    权限
    AdministratorDistrict selAdminDistrict(Map<String, Object> map);

    List<Integer> selAdminDevice(Map<String, Object> map);

    List<Role> qryAdminRoleByAdminId(Map<String, Object> map);

    Map<String, Object> selRolePermission(Map<String, Object> map);

    void delAdminDistrict(Map<String, Object> map);

    void inAdminDistrict(Map<String, Object> map);

    void delAdminDevice(Map<String, Object> map);

    void inAdminDevice(Map<String, Object> map);

    void upAdminPermission(Map<String, Object> map);

    void delAdminRoleByAdminId(Map<String, Object> map);

    void inAdminRole(Map<String, Object> map);
}
