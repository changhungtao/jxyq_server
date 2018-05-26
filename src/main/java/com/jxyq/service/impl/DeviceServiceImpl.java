package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.DeviceMapper;
import com.jxyq.model.health.*;
import com.jxyq.service.inf.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Manufactory selManufactoryByCode(Map<String, Object> map) {
        return deviceMapper.selManufactoryByCode(map);
    }

    @Override
    public DeviceType selDeviceTypeById(Map<String, Object> map) {
        return deviceMapper.selDeviceTypeById(map);
    }

    @Override
    public TerminalCatagory selTerminalCatagoryByCode(Map<String, Object> map) {
        return deviceMapper.selTerminalCatagory(map);
    }

    @Override
    public TerminalCatagory selTerminalCatagoryById(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", terminal_catagory_id);
        return deviceMapper.selTerminalCatagory(map);
    }

    @Override
    public Terminal selTerminalByMap(Map<String, Object> map) {
        return deviceMapper.selTerminalByMap(map);
    }

    @Override
    public void insertTerminal(Terminal terminal) {
        deviceMapper.insertTerminal(terminal);
    }

    @Override
    public void updateTerminal(Terminal terminal) {
        deviceMapper.updateTerminal(terminal);
    }

    @Override
    public void delTemplateByCatId(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", terminal_catagory_id);
        deviceMapper.delTemplateByCatId(map);
    }

    @Override
    public TerminalCatagoryTemplate selTemplateByTermCat(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", terminal_catagory_id);
        return deviceMapper.selTemplateByTermCat(map);
    }

    @Override
    public void delTemplateResourceByCatId(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", terminal_catagory_id);
        deviceMapper.delTemplateResourceByCatId(map);
    }

    @Override
    public void inTemplate(TerminalCatagoryTemplate template) {
        deviceMapper.inTemplate(template);
    }

    @Override
    public ProductType selProductTypeById(Map<String, Object> map) {
        return deviceMapper.selProductTypeById(map);
    }

    @Override
    public Map<String, Object> selTerminalDetail( Map<String, Object> map) {
        return deviceMapper.selTerminalDetail(map);
    }

    @Override
    public List<Map<String, Object>> selUsersByTerminalId(int tid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("tid", tid);
        return deviceMapper.selUsersByTerminalId(map);
    }

    @Override
    public List<Map<String, Object>> selTerminalsByPage(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return deviceMapper.selTerminalsByPage(map);
    }

    @Override
    public List<Map<String, Object>> selTemplatesByPage(Map<String, Object> map) {
        return deviceMapper.selTemplatesByPage(map);
    }

    @Override
    public List<Map<String, Object>> selTerminalCat4MarketByPage(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return deviceMapper.selTerminalCat4MarketByPage(map);
    }

    @Override
    public Map<String, Object> selTerminalCat4Market(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("terminal_catagory_id", terminal_catagory_id);
        return deviceMapper.selTerminalCat4Market(map);
    }

    @Override
    public List<Map<String, Object>> selManuByDeviceType(int did) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("did", did);
        return deviceMapper.selManuByDeviceType(map);
    }

    @Override
    public List<Map<String, Object>> selDeviceTypeByPage(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return deviceMapper.selDeviceTypeByPage(map);
    }

    @Override
    public List<Map<String, Object>> selDeviceTypeByPage(PagingCriteria pagingCriteria, DeviceType model)
    {
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put(BeanProperty.PAGING, pagingCriteria);
        map.put("fullname", StringUtils.trim(model.getName()));
        map.put("register_at", model.getRegistered_at());
        return deviceMapper.selDeviceTypeByPage(map);
    }


    @Override
    public void insertDeviceType(DeviceType deviceType) {
        deviceMapper.insertDeviceType(deviceType);
    }

    @Override
    public void updateDeviceType(DeviceType deviceType) {
        deviceMapper.updateDeviceType(deviceType);
    }

    @Override
    public void deleteDeviceType(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("did", id);
        deviceMapper.deleteDeviceType(map);
    }

    @Override
    public Map<String, Object> selDeviceType4Edit(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        return deviceMapper.selDeviceType4Edit(map);
    }

    @Override
    public List<Map<String, Object>> selTerminalCatagoryByDeviceTypeId(int device_type_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("device_type_id", device_type_id);
        return deviceMapper.selTerminalCatagoryByDeviceTypeId(map);
    }

    @Override
    public List<TerminalCatagoryPattern> selPatternByCategoryId(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", terminal_catagory_id);
        return deviceMapper.selCategoryPattern(map);
    }

    @Override
    public List<Map<String, Object>> selDeviceIdAndName(Map<String, Object> map) {
        return deviceMapper.selDeviceIdAndName(map);
    }

    @Override
    public List<Map<String, Object>> selProductIdAndName(int status) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("status", status);
        return deviceMapper.selProductIdAndName(map);
    }

    @Override
    public Map<String, Object> selDeviceCountByFactoryId(long manufactory_id, int device_type_id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("manufactory_id", manufactory_id);
        map.put("device_type_id", device_type_id);
        return deviceMapper.selDeviceCountByFactoryId(map);
    }

    @Override
    public List<DeviceType> selDeviceTypeByFactoryId(long manufactory_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("manufactory_id", manufactory_id);
        return deviceMapper.selDeviceTypeByFactoryId(map);
    }

    @Override
    public void delFactoryDeviceTypeByFactoryId(long manufactory_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("manufactory_id", manufactory_id);
        deviceMapper.delFactoryDeviceType(map);
    }

    @Override
    public void inFactoryDeviceType(int manufactory_device_type_id, int manufactory_id, int device_type_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("manufactory_device_type_id", manufactory_device_type_id);
        map.put("manufactory_id", manufactory_id);
        map.put("device_type_id", device_type_id);
        deviceMapper.inFactoryDeviceType(map);
    }

    @Override
    public List<TerminalCatagory> selTerCatByFactoryId(long manufactory_id, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("manufactory_id", manufactory_id);
        map.put("status", status);
        return deviceMapper.selTerCatByFactoryId(map);
    }
}
