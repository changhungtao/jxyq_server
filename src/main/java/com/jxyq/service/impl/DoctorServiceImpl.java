package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.ConsultationMapper;
import com.jxyq.mapper.normal_mapper.DoctorMapper;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.doctor.DoctorDetail;
import com.jxyq.service.inf.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private ConsultationMapper consultationMapper;

    @Override
    public Doctor selDoctorWithRolesByLoginName(String login_name) {
        Doctor doctor = selDoctorByLoginName(login_name);
        if(doctor == null) return null;
        doctor.setRoleList(doctorMapper.selRolesByDoctor(doctor));
        return doctor;
    }

    @Override
    public Doctor selDoctorByLoginName(String login_name) {
        Doctor doctor = new Doctor();
        doctor.setLogin_name(login_name);
        return doctorMapper.selDoctorByLoginName(doctor);
    }

    public void upDoctorPasspwdByPhone(Map<String, String> map){
        doctorMapper.upDoctorPwdByPhone(map);
    }
    public void upDoctorPwdByDoctorID(Map<String,String>map){
        doctorMapper.upDoctorPwdById(map);
    }

    public boolean verifyUserLegal(Map<String,String> map){
        int legal = 0;
        legal = doctorMapper.verifyUser(map);
        if(legal != 0){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> selDoctorDetail(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("did", id);
        return doctorMapper.selDoctorDetail(map);
    }

    @Override
    public List<Map<String, Object>> selDoctorByPage(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return doctorMapper.selDoctorByPage(map);
    }

    @Override
    public void insertDoctor(Doctor doctor) {
        doctorMapper.insertDoctor(doctor);
    }

    @Override
    public void upDoctorStatus(int id, String status) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("did", id);
        map.put("status", status);
        doctorMapper.upDoctorStatus(map);
    }

    @Override
    public void updateDoctorByAdmin(Map<String, Object> map) {
        doctorMapper.updateDoctorByAdmin(map);
    }

    @Override
    public void deleteDoctor(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("doctor_id", id);
        doctorMapper.deleteDoctor(map);
    }

    @Override
    public List<Doctor> queryDoctor(Map<String, Object> map) {
        return doctorMapper.queryDoctor(map);
    }

    @Override
    public List<Doctor> selDoctorByPermId(int data_permission_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("data_permission_id", data_permission_id);
        return doctorMapper.selDoctorByPermId(map);
    }

    @Override
    public void upDoctorBasicInf(Map<String, Object> map) {
        doctorMapper.upDoctorBasicInf(map);
    }

    @Override
    public void inDoctorRole(Map<String, Object> map) {
        doctorMapper.inDoctorRole(map);
    }

    @Override
    public Map<String, Object> selExpertTeamByDistrict(int district_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("district_id", district_id);
        return consultationMapper.selExpertTeam(map);
    }

    @Override
    public Map<String, Object> selTotalDocCnt() {
        return doctorMapper.selTotalDocCnt();
    }
}
