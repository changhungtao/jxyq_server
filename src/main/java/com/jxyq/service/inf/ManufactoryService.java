package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.health.Terminal;
import com.jxyq.model.health.TerminalCatagory;

import java.util.List;
import java.util.Map;

public interface ManufactoryService {
    Manufactory selManufactoryByLoginName(String login_name);

    Manufactory selManufactoryWithRolesByLoginName(String login_name);

    Manufactory selManufactoryById(int id);

    Map<String, Object> selManufactorysById(int id);

    List<Map<String, Object>> selManufactoriesByPage(Map<String, Object> map, PagingCriteria pageInf);

    void insertManufactory(Manufactory manufactory);

    void upManufactoryStatus(int id, int status);


    //    void upManufactoryInf(Manufactory manufactory);
    void upManufactoryInf(Map<String, Object> map);

    void upManufactoryLogo(Map<String, Object> map);

    void delManufactory(int id);

    List<Manufactory> selFactory4PermByPage(Map<String, Object> map);

    //    厂商
    void upManuPwd(Map<String, Object> map);

    void insertManDevice(Map<String, Object> map);

    List<Integer> selectManDevice(Map<String, Object> map);

    List<Integer> selUserDeviceId(int user_id);

    void delManDevice(Map<String, Object> map);

    void inTerminalCategory(TerminalCatagory category);

    void upTerminalCategory(TerminalCatagory category);

    List<Map<String, Object>> selTerminalCatagory(Map<String, Object> map);

    List<TerminalCatagory> qryCategoryByPage(Map<String, Object> map);

    List<Map<String, Object>> qryTerminalCatagoryByPage(Map<String, Object> map);

    TerminalCatagory selCategoryById(int cid);

    void inTemple(Map<String, Object> map);

    void upTerminalStatus(Map<String, Object> map);

    List<Map<String, Object>> qryFactoryIdAndName();

    void inFactoryRole(Map<String, Object> map);

    Map<String, Object> selFactoryCount();
}
