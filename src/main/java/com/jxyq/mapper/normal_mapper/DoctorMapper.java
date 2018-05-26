package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.Role;
import com.jxyq.model.doctor.Doctor;

import java.util.List;
import java.util.Map;

public interface DoctorMapper {
    Doctor selDoctorByLoginName(Doctor doctor);

    List<Role> selRolesByDoctor(Doctor doctor);

    //更新密码
    void upDoctorPwdByPhone(Map<String, String> map);

    void upDoctorPwdById(Map<String, String> map);

    //验证用户合法
    int verifyUser(Map<String, String> map);

    Map<String, Object> selDoctorDetail(Map<String, Object> map);

    List<Map<String, Object>> selDoctorByPage(Map<String, Object> map);

    void insertDoctor(Doctor doctor);

    void upDoctorStatus(Map<String, Object> map);

    void updateDoctorByAdmin(Map<String, Object> map);

    void deleteDoctor(Map<String, Object> map);

    List<Doctor> queryDoctor(Map<String, Object> map);

    List<Doctor> selDoctorByPermId(Map<String, Object> map);

    void upDoctorBasicInf(Map<String, Object> map);

    void inDoctorRole(Map<String, Object> map);

    Map<String, Object> selTotalDocCnt();
}
