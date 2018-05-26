package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.health.*;

import java.util.List;
import java.util.Map;

public interface DeviceMapper {
    void insertTerminal(Terminal terminal);

    void updateTerminal(Terminal terminal);

    Manufactory selManufactoryByCode(Map<String, Object> map);

    DeviceType selDeviceTypeById(Map<String, Object> map);

    TerminalCatagory selTerminalCatagory(Map<String, Object> map);

    Terminal selTerminalByMap(Map<String, Object> map);

    void delTemplateResourceByCatId(Map<String, Object> map);

    void delTemplateByCatId(Map<String, Object> map);

    ProductType selProductTypeById(Map<String, Object> map);

    Map<String, Object> selTerminalDetail(Map<String, Object> map);

    List<Map<String, Object>> selUsersByTerminalId(Map<String, Object> map);

    List<Map<String, Object>> selTerminalsByPage(Map<String, Object> map);

    List<Map<String, Object>> selTemplatesByPage(Map<String, Object> map);

    TerminalCatagoryTemplate selTemplateByTermCat(Map<String, Object> map);

    void inTemplate(TerminalCatagoryTemplate template);

    List<Map<String, Object>> selManuByDeviceType(Map<String, Object> map);

    List<Map<String, Object>> selDeviceTypeByPage(Map<String, Object> map);

    void insertDeviceType(DeviceType deviceType);

    void updateDeviceType(DeviceType deviceType);

    void deleteDeviceType(Map<String, Object> map);

    List<DeviceType> selDeviceTypeByManu(Map<String, Object> map);

    Map<String, Object> selDeviceType4Edit(Map<String, Object> map);

    List<Map<String, Object>> selTerminalCatagoryByDeviceTypeId(Map<String, Object> map);

    List<TerminalCatagoryPattern> selCategoryPattern(Map<String, Object> map);

    List<Map<String, Object>> selTerminalCat4MarketByPage(Map<String, Object> map);

    Map<String, Object> selTerminalCat4Market(Map<String, Object> map);

    List<Map<String, Object>> selDeviceIdAndName(Map<String, Object> map);

    List<Map<String, Object>> selProductIdAndName(Map<String, Object> map);

    List<Map<String, Object>> selDevicePermByFactoryId(Map<String, Object> map);

    Map<String, Object> selDeviceCountByFactoryId(Map<String, Object> map);

    List<DeviceType> selDeviceTypeByFactoryId(Map<String, Object> map);

    void delFactoryDeviceType(Map<String, Object> map);

    void inFactoryDeviceType(Map<String, Object> map);

    List<TerminalCatagory> selTerCatByFactoryId(Map<String, Object> map);
}
