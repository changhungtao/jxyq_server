package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.admin.AdministratorDistrict;
import com.jxyq.model.admin.OximeterFile;
import com.jxyq.model.admin.ProductTypeQuery;
import com.jxyq.model.user.User;

import java.util.List;
import java.util.Map;

public interface AdminService {
    Administrator selAdminByLoginName(String login_name);

    List<Map<String, Object>> selManagerByPage(Map<String, Object> map);

    Administrator selAdminWithRoleByLoginName(String login_name);

    Administrator selAdminById(int id);

    List<Map<String, Object>> selMeasurementsByPage(Map<String, Object> map);

    void updaAdminInfo(Map<String, Object> map);

    void updaAdminPass(Administrator admin);

    //    数据库测量类
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

    Map<String, Object> selProvinces(int province_id);

    List<Map<String, Object>> selCities(Map<String, Object> map);

    List<Map<String, Object>> selZones(Map<String, Object> map);

    List<Map<String, Object>> selProduct_types();

    List<Map<String, Object>> selDevice_types(Map<String, Object> map);

    //宣传页
    Map<String, Object> selWeb_templates(Map<String, Object> map);

    void upWeb_templates(Map<String, Object> map);

    List<Map<String, Object>> selExhibition();

    List<Map<String, Object>> qryExhibitionByPage(Map<String, Object> map);

    void addExhibition(Map<String, Object> map);

    void addExhibitionRes(Map<String, Object> map);

    void upExhibitionRes(Map<String, Object> map);

    void upExhibition(Map<String, Object> map);

    void delExhibition(int id);

    List<Map<String, Object>> qryNewsByPage(Map<String, Object> map);

    void addNews(Map<String, Object> map);

    void addNewsRes(Map<String, Object> map);

    void upNewsRes(Map<String, Object> map);

    void upNews(Map<String, Object> map);

    void delNewsResources(int id);

    //信息推送
    void postMessage(Map<String, Object> map);

    List<Map<String, Object>> postCatagory(Map<String, Object> map);

    List<Map<String, Object>> qryCategoryByPage(Map<String, Object> map);

    void delAdministrator(int id);

    void inAdmin(Map<String, Object> map);

    //    权限
    AdministratorDistrict selAdminDistrict(int mid);

    List<Integer> selAdminDevice(int mid);

    List<Role> qryAdminRoleByAdminId(int mid);

    Map<String, Object> selRolePermission(Map<String, Object> map);

    void delAdminDistrict(Map<String, Object> map);

    void inAdminDistrict(Map<String, Object> map);

    void delAdminDevice(Map<String, Object> map);

    void inAdminDevice(Map<String, Object> map);

    void upAdminPermission(Map<String, Object> map);

    void delAdminRoleByAdminId(int mid);

    void inAdminRole(Map<String, Object> map);
}
