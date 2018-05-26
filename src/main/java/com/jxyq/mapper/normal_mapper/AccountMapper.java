package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.user.User;

import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/4/1.
 */
public interface AccountMapper {

    int checkPhone(Map<String, String> map);

    int checkEmail(Map<String, String> map);

    //    注册信息
    void insertUserForRegister(Map<String, Object> map);

    //     admin db操作
    User selUserById(int id);

    List<Map<String, Object>> selUserByPage(Map<String, Object> map);

    void upUserStatus(Map<String, Object> map);

    void putUserInfo(Map<String, Object> map);

    int checkEmails(Map<String, Object> map);

    void upUserInf(Map<String, Object> map);

    Map<String, Object> selTotalUserCnt();

    Map<String, Object> selOnlineUserCnt();
}
