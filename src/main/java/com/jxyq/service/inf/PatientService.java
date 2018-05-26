package com.jxyq.service.inf;

import com.jxyq.model.health.*;

import java.util.List;
import java.util.Map;


public interface PatientService {
    Patient selPatientById(int pid);

    Patient selPatientByPhone(String phone);

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

    PatWristbandFile selWristbandFile(long wid);

    void upWristbandFile(PatWristbandFile wFile);

    PatSphygmomanometerFile selSphygmomanometerFile(long sid);

    void upSphygmomanometerFile(PatSphygmomanometerFile sFile);

    PatOximeterFile selOximeterFile(long oid);

    void upOximeterFile(PatOximeterFile oFile);

    PatGlucosemeterFile selGlucosemeterFile(long gid);

    void upGlucosemeterFile(PatGlucosemeterFile gFile);

    PatThermometersFile selThermometersFile(long tid);

    void upThermometersFile(PatThermometersFile pFile);

    PatFatFile selFatFile(long fid);

    void upFatFile(PatFatFile fFile);

    PatOtherFile selOtherFile(long oid);

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
