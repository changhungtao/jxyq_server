package com.jxyq.service.inf;

import com.jxyq.model.health.*;
import com.jxyq.model.user.Consumption;

import java.util.List;
import java.util.Map;

public interface UserHealthService {
    void insertSphyg(UserSphygmomanometerData sphyg);

    void insertGlucose(UserGlucosemeterData glucose);

    void insertOximeter(UserOximeterData oximeter);

    void insertThermometer(UserThermometersData thermometer);

    void insertFat(UserFatData fat);

    //   基本信息完善
    void putUserInfo(Map<String, Object> map);

    void inPoints(Consumption com);

    //    上传手环数据
    void insertWristband(WristbandData exercise);

    //    查询手环详情
    List<UserWristbandData> selWristbandDetail(Map<String, Object> map);

    Map<String, Object> selWristbandSummary(Map<String, Object> map);

    //    查询血压信息记录
    List<UserSphygmomanometerData> selSDetail(Map<String, Object> map);

    UserSphygmomanometerData selSphygmomanometerData(int id);

    void upSphygmomanometerData(UserSphygmomanometerData data);

    UserSphygmomanometerData selLastSphygmomanometerData(long max_time, int user_id);

    List<UserThermometersData> selThermometerDetail(Map<String, Object> map);

    UserThermometersData selThermometerData(int thermometer_data_id);

    UserThermometersData selLastThermometerData(long max_time, int user_id);

    void upThermometersData(UserThermometersData data);

    List<UserFatData> selFatDetail(Map<String, Object> map);

    UserFatData selFatData(int fat_data_id);

    UserFatData selLastFatData(long max_time, int user_id);

    void upFatData(UserFatData data);

    List<UserOximeterData> selOximeterDetail(Map<String, Object> map);

    UserOximeterData selOximeterData(int oximeter_data_id);

    UserOximeterData selLastOximeterDataId(long max_time, int user_id);

    void upOximeterData(UserOximeterData data);

    List<UserGlucosemeterData> selGlucosemeterDetail(Map<String, Object> map);

    UserGlucosemeterData selGlucosemeterData(int glucosemeter_data_id);

    UserGlucosemeterData selLastGlucosemeterData(long max_time, int user_id);

    void upGlucosemeterData(UserGlucosemeterData data);

    List<Map<String, Object>> selMeasurementsByPage(Map<String, Object> map);

    WristbandData selWristbandData(int wristband_data_id);

    WristbandData selLastWristbandDataId(int wristband_data_id, int user_id);

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
