package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.PatientMapper;
import com.jxyq.model.health.*;
import com.jxyq.service.inf.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientMapper patientMapper;

    @Override
    public Patient selPatientById(int pid) {
        Map<String, Object> map = new HashMap<>();
        map.put("patient_id", pid);
        return patientMapper.selPatient(map);
    }

    @Override
    public Patient selPatientByPhone(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        return patientMapper.selPatient(map);
    }

    @Override
    public List<Map<String, Object>> qryWristbandPatientByPage(Map<String, Object> map) {
        return patientMapper.qryWristbandPatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qrySphyPatientByPage(Map<String, Object> map) {
        return patientMapper.qrySphyPatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryOxiPatientByPage(Map<String, Object> map) {
        return patientMapper.qryOxiPatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryGluPatientByPage(Map<String, Object> map) {
        return patientMapper.qryGluPatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryThePatientByPage(Map<String, Object> map) {
        return patientMapper.qryThePatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryFatPatientByPage(Map<String, Object> map) {
        return patientMapper.qryFatPatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryOtherPatientByPage(Map<String, Object> map) {
        return patientMapper.qryOtherPatientByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryWristbandFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qryWristbandFile4DocByPage(map);
    }

    @Override
    public List<Map<String, Object>> qrySphyFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qrySphyFile4DocByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryOxiFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qryOxiFile4DocByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryGluFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qryGluFile4DocByPage(map);

    }

    @Override
    public List<Map<String, Object>> qryFatFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qryFatFile4DocByPage(map);

    }

    @Override
    public List<Map<String, Object>> qryOtherFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qryOtherFile4DocByPage(map);
    }

    @Override
    public List<Map<String, Object>> qryTheFile4DocByPage(Map<String, Object> map) {
        return patientMapper.qryTheFile4DocByPage(map);

    }

    @Override
    public List<Map<String, Object>> qryWristbandFile4Doc(Map<String, Object> map) {
        return patientMapper.qryWristbandFile4Doc(map);
    }

    @Override
    public List<Map<String, Object>> qrySphyFile4Doc(Map<String, Object> map) {
        return patientMapper.qrySphyFile4Doc(map);
    }

    @Override
    public List<Map<String, Object>> qryOxiFile4Doc(Map<String, Object> map) {
        return patientMapper.qryOxiFile4Doc(map);
    }

    @Override
    public List<Map<String, Object>> qryGluFile4Doc(Map<String, Object> map) {
        return patientMapper.qryGluFile4Doc(map);

    }

    @Override
    public List<Map<String, Object>> qryFatFile4Doc(Map<String, Object> map) {
        return patientMapper.qryFatFile4Doc(map);

    }

    @Override
    public List<Map<String, Object>> qryTheFile4Doc(Map<String, Object> map) {
        return patientMapper.qryTheFile4Doc(map);

    }

    @Override
    public PatWristbandFile selWristbandFile(long wid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("wristband_file_id", wid);
        return patientMapper.selWristbandFile(map);
    }

    @Override
    public void upWristbandFile(PatWristbandFile wFile) {
        patientMapper.upWristbandFile(wFile);
    }

    @Override
    public PatSphygmomanometerFile selSphygmomanometerFile(long sid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("sphygmomanometer_file_id", sid);
        return patientMapper.selSphygmomanometerFile(map);
    }

    @Override
    public void upSphygmomanometerFile(PatSphygmomanometerFile sFile) {
        patientMapper.upSphygmomanometerFile(sFile);
    }

    @Override
    public PatOximeterFile selOximeterFile(long oid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("oximeter_file_id", oid);
        return patientMapper.selOximeterFile(map);
    }

    @Override
    public void upOximeterFile(PatOximeterFile oFile) {
        patientMapper.upOximeterFile(oFile);
    }

    @Override
    public PatGlucosemeterFile selGlucosemeterFile(long gid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("glucosemeter_file_id", gid);
        return patientMapper.selGlucosemeterFile(map);
    }

    @Override
    public void upGlucosemeterFile(PatGlucosemeterFile gFile) {
        patientMapper.upGlucosemeterFile(gFile);
    }

    @Override
    public PatThermometersFile selThermometersFile(long tid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("thermometer_file_id", tid);
        return patientMapper.selThermometersFile(map);
    }

    @Override
    public void upThermometersFile(PatThermometersFile pFile) {
        patientMapper.upThermometersFile(pFile);
    }

    @Override
    public PatFatFile selFatFile(long fid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("fat_file_id", fid);
        return patientMapper.selFatFile(map);
    }

    @Override
    public void upFatFile(PatFatFile fFile) {
        patientMapper.upFatFile(fFile);
    }

    @Override
    public void upOtherFile(PatOtherFile oFile) {
        patientMapper.upOtherFile(oFile);
    }

    @Override
    public PatOtherFile selOtherFile(long oid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("other_file_id", oid);
        return patientMapper.selOtherFile(map);
    }

    //    患者檔案
    @Override
    public Patient selPatientByPhone(Map<String, Object> map) {
        return patientMapper.selPatientByPhone(map);
    }

    @Override
    public List<Patient> qryPatientByPage(Map<String, Object> map) {
        return patientMapper.qryPatientByPage(map);
    }

    @Override
    public void insertPatient(Patient patient) {
        patientMapper.insertPatient(patient);
    }

    @Override
    public void putPatient(Patient patient) {
        patientMapper.putPatient(patient);
    }

    @Override
    public void insertWristbandFile(PatWristbandFile wFile) {
        patientMapper.insertWristbandFile(wFile);
    }

    @Override
    public void insertSphygmomanometerFile(PatSphygmomanometerFile sFile) {
        patientMapper.insertSphygmomanometerFile(sFile);
    }

    @Override
    public void insertOximeterFile(PatOximeterFile oFile) {
        patientMapper.insertOximeterFile(oFile);
    }

    @Override
    public void insertGlucosemeterFile(PatGlucosemeterFile gFile) {
        patientMapper.insertGlucosemeterFile(gFile);
    }

    @Override
    public void insertThermometersFile(PatThermometersFile pFile) {
        patientMapper.insertThermometersFile(pFile);
    }

    @Override
    public void insertFatFile(PatFatFile fFile) {
        patientMapper.insertFatFile(fFile);
    }

    @Override
    public void insertOtherFile(PatOtherFile oFile) {
        patientMapper.insertOtherFile(oFile);
    }
}
