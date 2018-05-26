package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.Role;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.health.TerminalCatagory;

import java.util.List;
import java.util.Map;

public interface ManufactoryMapper {
    Manufactory selManufactoryByLoginName(Manufactory manufactory);

    List<Role> selRolesByManufactory(Manufactory manufactory);

    Manufactory selManufacotryById(Map<String, Object> map);

    Map<String, Object> selManufacotrysById(Map<String, Object> map);

    List<Map<String, Object>> selManufacotriesByPage(Map<String, Object> map);

    void insertManufactory(Manufactory manufactory);

    void upManufactoryStatus(Map<String, Object> map);

    void upManufactoryInf(Map<String, Object> map);

    void upManufactoryLogo(Map<String, Object> map);

    void delManufactory(Map<String, Object> map);

    List<Manufactory> selFactory4PermByPage(Map<String, Object> map);

    //  厂商
    void upManuPwd(Map<String, Object> map);

    public void insertManDevice(Map<String, Object> map);

    List<Integer> selectManDevice(Map<String, Object> map);

    List<Integer> selUserDeviceId(Map<String, Object> map);

    void delManDevice(Map<String, Object> map);

    void inTerminalCategory(TerminalCatagory category);

    void upTerminalCategory(TerminalCatagory category);

    List<Map<String, Object>> selTerminalCatagory(Map<String, Object> map);

    List<TerminalCatagory> qryCategoryByPage(Map<String, Object> map);

    TerminalCatagory selCategory(Map<String, Object> map);

    void inTemple(Map<String, Object> map);

    void upTerminalStatus(Map<String, Object> map);

    List<Map<String, Object>> qryFactoryIdAndName();

    void inFactoryRole(Map<String, Object> map);

    Map<String, Object> selFactoryCount();

    List<Map<String, Object>> qryTerminalCatagoryByPage(Map<String, Object> map);
}
