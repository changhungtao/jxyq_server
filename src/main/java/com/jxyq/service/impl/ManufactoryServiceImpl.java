package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.ManufactoryMapper;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.health.TerminalCatagory;
import com.jxyq.service.inf.ManufactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManufactoryServiceImpl implements ManufactoryService {
    @Autowired
    private ManufactoryMapper manufactoryMapper;

    @Override
    public Manufactory selManufactoryByLoginName(String login_name) {
        Manufactory manufactory = new Manufactory();
        manufactory.setLogin_name(login_name);
        return manufactoryMapper.selManufactoryByLoginName(manufactory);
    }

    @Override
    public Manufactory selManufactoryWithRolesByLoginName(String login_name) {
        Manufactory manufactory = selManufactoryByLoginName(login_name);
        if (manufactory == null) return null;
        manufactory.setRoleList(manufactoryMapper.selRolesByManufactory(manufactory));
        return manufactory;
    }

    @Override
    public Manufactory selManufactoryById(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("manufactory_id", id);
        return manufactoryMapper.selManufacotryById(map);
    }

    @Override
    public Map<String, Object> selManufactorysById(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("manufactory_id", id);
        return manufactoryMapper.selManufacotrysById(map);
    }

    @Override
    public List<Map<String, Object>> selManufactoriesByPage(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return manufactoryMapper.selManufacotriesByPage(map);
    }

    @Override
    public void insertManufactory(Manufactory manufactory) {
        manufactoryMapper.insertManufactory(manufactory);
    }

    @Override
    public void upManufactoryStatus(int id, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("manufactory_id", id);
        map.put("status", status);
        manufactoryMapper.upManufactoryStatus(map);
    }

    @Override
    public void upManufactoryInf(Map<String, Object> map) {
        manufactoryMapper.upManufactoryInf(map);
    }


    @Override
    public void upManufactoryLogo(Map<String, Object> map) {
        manufactoryMapper.upManufactoryLogo(map);

    }

    @Override
    public void delManufactory(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("manufactory_id", id);
        manufactoryMapper.delManufactory(map);
    }

    @Override
    public List<Manufactory> selFactory4PermByPage(Map<String, Object> map) {
        return manufactoryMapper.selFactory4PermByPage(map);
    }

    //厂商
    @Override
    public void upManuPwd(Map<String, Object> map) {
        manufactoryMapper.upManuPwd(map);
    }

    @Override
    public void insertManDevice(Map<String, Object> map) {
        manufactoryMapper.insertManDevice(map);
    }

    @Override
    public List<Integer> selectManDevice(Map<String, Object> map) {
        return manufactoryMapper.selectManDevice(map);
    }

    @Override
    public List<Integer> selUserDeviceId(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return manufactoryMapper.selUserDeviceId(map);
    }

    @Override
    public void delManDevice(Map<String, Object> map) {
        manufactoryMapper.delManDevice(map);
    }

    @Override
    public void inTerminalCategory(TerminalCatagory category) {
        manufactoryMapper.inTerminalCategory(category);
    }

    @Override
    public void upTerminalCategory(TerminalCatagory category) {
        manufactoryMapper.upTerminalCategory(category);
    }

    @Override
    public TerminalCatagory selCategoryById(int cid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", cid);
        return manufactoryMapper.selCategory(map);
    }

    @Override
    public List<Map<String, Object>> selTerminalCatagory(Map<String, Object> map) {
        return manufactoryMapper.selTerminalCatagory(map);
    }

    @Override
    public List<TerminalCatagory> qryCategoryByPage(Map<String, Object> map) {
        return manufactoryMapper.qryCategoryByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryTerminalCatagoryByPage(Map<String, Object> map) {
        return manufactoryMapper.qryTerminalCatagoryByPage(map);
    }

    @Override
    public void inTemple(Map<String, Object> map) {
        manufactoryMapper.inTemple(map);
    }

    @Override
    public void upTerminalStatus(Map<String, Object> map) {
        manufactoryMapper.upTerminalStatus(map);
    }

    @Override
    public List<Map<String, Object>> qryFactoryIdAndName() {
        return manufactoryMapper.qryFactoryIdAndName();
    }

    @Override
    public void inFactoryRole(Map<String, Object> map) {
        manufactoryMapper.inFactoryRole(map);
    }

    @Override
    public Map<String, Object> selFactoryCount() {
        return manufactoryMapper.selFactoryCount();
    }
}
