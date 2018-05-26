package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.user.Consumption;
import com.jxyq.model.health.*;

import java.util.List;
import java.util.Map;

public interface UserHealthMapper {
    //  基本信息完善
    void updateUserInfo(Map<String, Object> map);
    void inPoints(Consumption com);

    //   上传手环数据
    void insertWristband(WristbandData exercise);

    //   获取详情
    List<UserWristbandData> selWristbandDetail(Map<String, Object> map);

    Map<String, Object> selWristbandSummary(Map<String, Object> map);

    //   查询血压
    List<UserSphygmomanometerData> selSDetail(Map<String, Object> map);

    UserSphygmomanometerData selSphygmomanometerData(Map<String, Object> map);

    UserSphygmomanometerData selLastSphygmomanometerData(Map<String, Object> map);

    void upSphygmomanometerData(UserSphygmomanometerData data);

    void insertSphyg(UserSphygmomanometerData sphyg);

    void insertHeartBeat(UserHeartBeatData heartBeat);

    void insertGlucose(UserGlucosemeterData glucose);

    void insertOximeter(UserOximeterData oximeter);

    void insertThermometer(UserThermometersData thermometer);

    void insertFat(UserFatData fat);

    List<UserHeartBeatData> selHeartBeatDetail(Map<String, Object> map);

    List<UserThermometersData> selThermometerDetail(Map<String, Object> map);

    UserThermometersData selThermometerData(Map<String, Object> map);

    UserThermometersData selLastThermometerData(Map<String, Object> map);

    void upThermometersData(UserThermometersData data);

    List<UserFatData> selFatDetail(Map<String, Object> map);

    UserFatData selFatData(Map<String, Object> map);

    UserFatData selLastFatData(Map<String, Object> map);

    void upFatData(UserFatData data);

    List<UserOximeterData> selOximeterDetail(Map<String, Object> map);

    UserOximeterData selOximeterData(Map<String, Object> map);

    UserOximeterData selLastOximeterDataId(Map<String, Object> map);

    void upOximeterData(UserOximeterData data);

    List<UserGlucosemeterData> selGlucosemeterDetail(Map<String, Object> map);

    UserGlucosemeterData selGlucosemeterData(Map<String, Object> map);

    UserGlucosemeterData selLastGlucosemeterData(Map<String, Object> map);

    void upGlucosemeterData(UserGlucosemeterData data);

    List<Map<String, Object>> selMeasurementsByPage(Map<String, Object> map);

    WristbandData selWristbandData(Map<String, Object> map);

    WristbandData selLastWristbandDataId(Map<String, Object> map);

    void upWristbandData(WristbandData data);

    List<Map<String, Object>> selWristbandData4DoctorByPage(Map<String, Object> map);

    List<Map<String, Object>> selSphyData4DoctorByPage(Map<String, Object> map);

    List<Map<String, Object>> selOximeterData4DoctorByPage(Map<String, Object> map);

    List<Map<String, Object>> selGlucosemeterData4DoctorByPage(Map<String, Object> map);

    List<Map<String, Object>> selThermometerData4DoctorByPage(Map<String, Object> map);

    List<Map<String, Object>> selFatData4DoctorByPage(Map<String, Object> map);

    List<Map<String, Object>> selWristbandData4FactoryByPage(Map<String, Object> map);

    List<Map<String, Object>> selSphyData4FactoryByPage(Map<String, Object> map);

    List<Map<String, Object>> selOximeterData4FactoryByPage(Map<String, Object> map);

    List<Map<String, Object>> selGlucosemeterData4FactoryByPage(Map<String, Object> map);

    List<Map<String, Object>> selThermometerData4FactoryByPage(Map<String, Object> map);

    List<Map<String, Object>> selFatData4FactoryByPage(Map<String, Object> map);
}
