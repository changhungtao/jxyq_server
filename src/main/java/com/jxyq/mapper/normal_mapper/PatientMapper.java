package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.health.*;

import java.util.List;
import java.util.Map;

public interface PatientMapper {
    Patient selPatient(Map<String, Object> map);

    List<Map<String, Object>> qryWristbandPatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qrySphyPatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qryOxiPatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qryGluPatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qryThePatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qryFatPatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qryOtherPatientByPage(Map<String, Object> map);

    List<Map<String, Object>> qryWristbandFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qrySphyFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qryOxiFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qryGluFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qryTheFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qryFatFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qryOtherFile4DocByPage(Map<String, Object> map);

    List<Map<String, Object>> qryWristbandFile4Doc(Map<String, Object> map);

    List<Map<String, Object>> qrySphyFile4Doc(Map<String, Object> map);

    List<Map<String, Object>> qryOxiFile4Doc(Map<String, Object> map);

    List<Map<String, Object>> qryGluFile4Doc(Map<String, Object> map);

    List<Map<String, Object>> qryTheFile4Doc(Map<String, Object> map);

    List<Map<String, Object>> qryFatFile4Doc(Map<String, Object> map);

    PatWristbandFile selWristbandFile(Map<String, Object> map);

    void upWristbandFile(PatWristbandFile wFile);

    PatSphygmomanometerFile selSphygmomanometerFile(Map<String, Object> map);

    void upSphygmomanometerFile(PatSphygmomanometerFile sFile);

    PatOximeterFile selOximeterFile(Map<String, Object> map);

    void upOximeterFile(PatOximeterFile oFile);

    PatGlucosemeterFile selGlucosemeterFile(Map<String, Object> map);

    void upGlucosemeterFile(PatGlucosemeterFile gFile);

    PatThermometersFile selThermometersFile(Map<String, Object> map);

    void upThermometersFile(PatThermometersFile pFile);

    PatFatFile selFatFile(Map<String, Object> map);

    void upFatFile(PatFatFile fFile);

    PatOtherFile selOtherFile(Map<String, Object> map);

    void upOtherFile(PatOtherFile oFile);

    Patient selPatientByPhone(Map<String, Object> map);

    List<Patient> qryPatientByPage(Map<String, Object> map);

    void insertPatient(Patient patient);

    void putPatient(Patient patient);

    void insertWristbandFile(PatWristbandFile wFile);

    void insertSphygmomanometerFile(PatSphygmomanometerFile sFile);

    void insertOximeterFile(PatOximeterFile oFile);

    void insertGlucosemeterFile(PatGlucosemeterFile gFile);

    void insertThermometersFile(PatThermometersFile pFile);

    void insertFatFile(PatFatFile fFile);

    void insertOtherFile(PatOtherFile oFile);
}
