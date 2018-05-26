package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.doctor.DoctorDetail;

import java.util.List;
import java.util.Map;

public interface DoctorService {
    Doctor selDoctorByLoginName(String login_name);

    Doctor selDoctorWithRolesByLoginName(String login_name);

    void upDoctorPasspwdByPhone(Map<String, String> map);

    void upDoctorPwdByDoctorID(Map<String, String> map);

    boolean verifyUserLegal(Map<String, String> map);

    Map<String, Object> selDoctorDetail(int id);

    List<Map<String, Object>> selDoctorByPage(Map<String, Object> map, PagingCriteria pageInf);

    void insertDoctor(Doctor doctor);

    void upDoctorStatus(int id, String status);

    void updateDoctorByAdmin(Map<String, Object> map);

    void deleteDoctor(int id);

    List<Doctor> queryDoctor(Map<String, Object> map);

    List<Doctor> selDoctorByPermId(int data_permission_id);

    void upDoctorBasicInf(Map<String, Object> map);

    void inDoctorRole(Map<String, Object> map);

    Map<String, Object> selExpertTeamByDistrict(int district_id);

    Map<String, Object> selTotalDocCnt();
}
