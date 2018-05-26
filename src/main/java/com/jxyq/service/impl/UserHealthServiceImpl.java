package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.UserHealthMapper;
import com.jxyq.model.user.Consumption;
import com.jxyq.model.health.*;
import com.jxyq.model.user.UserQuery;
import com.jxyq.service.inf.UserHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserHealthServiceImpl implements UserHealthService {
    @Autowired
    private UserHealthMapper userHealthMapper;

    //  基本信息完善
    @Override
    public void putUserInfo( Map<String,Object>map) {
        userHealthMapper.updateUserInfo(map);
    }
    @Override
    public void inPoints(Consumption com){
        userHealthMapper.inPoints(com);
    }
    //    上传手环数据
    @Override
    public void insertWristband(WristbandData exercise) {
        userHealthMapper.insertWristband(exercise);
    }

    //  获取手环详情
    @Override
    public List<UserWristbandData> selWristbandDetail(Map<String, Object> map) {
        return userHealthMapper.selWristbandDetail(map);
    }

    @Override
    public Map<String, Object> selWristbandSummary(Map<String, Object> map) {
        return userHealthMapper.selWristbandSummary(map);
    }

    //    查询血压信息记录
    public List<UserSphygmomanometerData> selSDetail(Map<String, Object> map) {
        return userHealthMapper.selSDetail(map);
    }

    @Override
    public UserSphygmomanometerData selSphygmomanometerData(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("sphygmomanometer_data_id", id);
        return userHealthMapper.selSphygmomanometerData(map);
    }

    @Override
    public UserSphygmomanometerData selLastSphygmomanometerData(long max_time, int user_id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("measured_at", max_time);
        map.put("user_id", user_id);
        return userHealthMapper.selLastSphygmomanometerData(map);
    }

    @Override
    public void upSphygmomanometerData(UserSphygmomanometerData data) {
        userHealthMapper.upSphygmomanometerData(data);
    }

    @Override
    public void insertSphyg(UserSphygmomanometerData sphyg) {
        userHealthMapper.insertSphyg(sphyg);
    }

    @Override
    public void insertGlucose(UserGlucosemeterData glucose) {
        userHealthMapper.insertGlucose(glucose);
    }

    @Override
    public void insertOximeter(UserOximeterData oximeter) {
        userHealthMapper.insertOximeter(oximeter);
    }

    @Override
    public void insertThermometer(UserThermometersData thermometer) {
        userHealthMapper.insertThermometer(thermometer);
    }

    @Override
    public void insertFat(UserFatData fat) {
        userHealthMapper.insertFat(fat);
    }

    @Override
    public List<UserThermometersData> selThermometerDetail(Map<String, Object> map) {
        return userHealthMapper.selThermometerDetail(map);
    }

    @Override
    public UserThermometersData selThermometerData(int thermometer_data_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("thermometer_data_id", thermometer_data_id);
        return userHealthMapper.selThermometerData(map);
    }

    @Override
    public UserThermometersData selLastThermometerData(long max_time, int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("measured_at", max_time);
        map.put("user_id", user_id);
        return userHealthMapper.selLastThermometerData(map);
    }

    @Override
    public void upThermometersData(UserThermometersData data) {
        userHealthMapper.upThermometersData(data);
    }

    @Override
    public List<UserFatData> selFatDetail(Map<String, Object> map) {
        return userHealthMapper.selFatDetail(map);
    }

    @Override
    public UserFatData selFatData(int fat_data_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("fat_data_id", fat_data_id);
        return userHealthMapper.selFatData(map);
    }

    @Override
    public UserFatData selLastFatData(long max_time, int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("measured_at", max_time);
        map.put("user_id", user_id);
        return userHealthMapper.selLastFatData(map);
    }

    @Override
    public void upFatData(UserFatData data) {
        userHealthMapper.upFatData(data);
    }

    @Override
    public List<UserOximeterData> selOximeterDetail(Map<String, Object> map) {
        return userHealthMapper.selOximeterDetail(map);
    }

    @Override
    public UserOximeterData selOximeterData(int oximeter_data_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("oximeter_data_id", oximeter_data_id);
        return userHealthMapper.selOximeterData(map);
    }

    @Override
    public UserOximeterData selLastOximeterDataId(long max_time, int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("measured_at", max_time);
        map.put("user_id", user_id);
        return userHealthMapper.selLastOximeterDataId(map);
    }

    @Override
    public void upOximeterData(UserOximeterData data) {
        userHealthMapper.upOximeterData(data);
    }

    @Override
    public List<UserGlucosemeterData> selGlucosemeterDetail(Map<String, Object> map) {
        return userHealthMapper.selGlucosemeterDetail(map);
    }

    @Override
    public UserGlucosemeterData selGlucosemeterData(int glucosemeter_data_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("glucosemeter_data_id", glucosemeter_data_id);
        return userHealthMapper.selGlucosemeterData(map);
    }

    @Override
    public UserGlucosemeterData selLastGlucosemeterData(long max_time, int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("measured_at", max_time);
        map.put("user_id", user_id);
        return userHealthMapper.selLastGlucosemeterData(map);
    }

    @Override
    public void upGlucosemeterData(UserGlucosemeterData data) {
        userHealthMapper.upGlucosemeterData(data);
    }

    @Override
    public List<Map<String, Object>> selMeasurementsByPage(Map<String, Object> map) {
        return userHealthMapper.selMeasurementsByPage(map);
    }

    @Override
    public WristbandData selWristbandData(int wristband_data_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("wristband_data_id", wristband_data_id);
        return userHealthMapper.selWristbandData(map);
    }

    @Override
    public WristbandData selLastWristbandDataId(int wristband_data_id, int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("wristband_data_id", wristband_data_id);
        map.put("user_id", user_id);
        return userHealthMapper.selLastWristbandDataId(map);
    }

    @Override
    public void upWristbandData(WristbandData data) {
        userHealthMapper.upWristbandData(data);
    }

    @Override
    public List<Map<String, Object>> selWristbandData4DoctorByPage(Map<String, Object> map) {
        return userHealthMapper.selWristbandData4DoctorByPage(map);
    }

    @Override
    public List<Map<String, Object>> selSphyData4DoctorByPage(Map<String, Object> map) {
        return userHealthMapper.selSphyData4DoctorByPage(map);
    }

    @Override
    public List<Map<String, Object>> selOximeterData4DoctorByPage(Map<String, Object> map) {
        return userHealthMapper.selOximeterData4DoctorByPage(map);
    }

    @Override
    public List<Map<String, Object>> selGlucosemeterData4DoctorByPage(Map<String, Object> map) {
        return userHealthMapper.selGlucosemeterData4DoctorByPage(map);
    }

    @Override
    public List<Map<String, Object>> selThermometerData4DoctorByPage(Map<String, Object> map) {
        return userHealthMapper.selThermometerData4DoctorByPage(map);
    }

    @Override
    public List<Map<String, Object>> selFatData4DoctorByPage(Map<String, Object> map) {
        return userHealthMapper.selFatData4DoctorByPage(map);
    }

    @Override
    public List<Map<String, Object>> selFatData4FactoryByPage(Map<String, Object> map) {
        return userHealthMapper.selFatData4FactoryByPage(map);
    }

    @Override
    public List<Map<String, Object>> selThermometerData4FactoryByPage(Map<String, Object> map) {
        return userHealthMapper.selThermometerData4FactoryByPage(map);
    }

    @Override
    public List<Map<String, Object>> selGlucosemeterData4FactoryByPage(Map<String, Object> map) {
        return userHealthMapper.selGlucosemeterData4FactoryByPage(map);
    }

    @Override
    public List<Map<String, Object>> selOximeterData4FactoryByPage(Map<String, Object> map) {
        return userHealthMapper.selOximeterData4FactoryByPage(map);
    }

    @Override
    public List<Map<String, Object>> selSphyData4FactoryByPage(Map<String, Object> map) {
        return userHealthMapper.selSphyData4FactoryByPage(map);
    }

    @Override
    public List<Map<String, Object>> selWristbandData4FactoryByPage(Map<String, Object> map) {
        return userHealthMapper.selWristbandData4FactoryByPage(map);
    }
}
