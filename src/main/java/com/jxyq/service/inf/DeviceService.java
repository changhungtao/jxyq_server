package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.health.*;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    Manufactory selManufactoryByCode(Map<String, Object> map);

    DeviceType selDeviceTypeById(Map<String, Object> map);

    TerminalCatagory selTerminalCatagoryByCode(Map<String, Object> map);

    TerminalCatagory selTerminalCatagoryById(int terminal_catagory_id);

    Terminal selTerminalByMap(Map<String, Object> map);

    void delTemplateResourceByCatId(int terminal_catagory_id);

    void delTemplateByCatId(int terminal_catagory_id);

    void inTemplate(TerminalCatagoryTemplate template);

    TerminalCatagoryTemplate selTemplateByTermCat(int terminal_catagory_id);

    ProductType selProductTypeById(Map<String, Object> map);

    void insertTerminal(Terminal terminal);

    void updateTerminal(Terminal terminal);

    Map<String, Object> selTerminalDetail( Map<String, Object> map);

    List<Map<String, Object>> selUsersByTerminalId(int tid);

    List<Map<String, Object>> selTerminalsByPage(Map<String, Object> map, PagingCriteria pageInf);

    List<Map<String, Object>> selTemplatesByPage(Map<String, Object> map);

    List<Map<String, Object>> selTerminalCat4MarketByPage(Map<String, Object> map, PagingCriteria pageInf);

    Map<String, Object> selTerminalCat4Market(int terminal_catagory_id);

    List<Map<String, Object>> selManuByDeviceType(int did);

    List<Map<String, Object>> selDeviceTypeByPage(Map<String, Object> map, PagingCriteria pageInf);

    List<Map<String, Object>> selDeviceTypeByPage(PagingCriteria pagingCriteria, DeviceType model);

    void insertDeviceType(DeviceType deviceType);

    void updateDeviceType(DeviceType deviceType);

    void deleteDeviceType(int id);

    Map<String, Object> selDeviceType4Edit(int id);

    List<Map<String, Object>> selTerminalCatagoryByDeviceTypeId(int device_type_id);

    List<TerminalCatagoryPattern> selPatternByCategoryId(int terminal_catagory_id);

    List<Map<String, Object>> selProductIdAndName(int status);

    List<Map<String, Object>> selDeviceIdAndName(Map<String, Object> map);

    Map<String, Object> selDeviceCountByFactoryId(long manufactory_id, int device_type_id);

    List<DeviceType> selDeviceTypeByFactoryId(long manufactory_id);

    void delFactoryDeviceTypeByFactoryId(long manufactory_id);

    void inFactoryDeviceType(int manufactory_device_type_id, int manufactory_id, int device_type_id);

    List<TerminalCatagory> selTerCatByFactoryId(long manufactory_id, int status);

}
