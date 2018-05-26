package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.user.User;
import com.jxyq.model.user.UserDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/4/1.
 */
public interface AccountService {
    /*//    根据手机号、
        User selUserByUser(String login_name);*/
    void putUserInfo(int id, UserDetail userQuery);

    void upUserInf(Map<String, Object> map);

    //    user注册
    void insertUser4Register(Map<String, Object> map);

    //   admin DB user操作
    User selUserById(int id);

    List<Map<String, Object>> selUserByPage(Map<String, Object> map, PagingCriteria pageInf);

    void upUserStatus(int id, int status);

    Map<String, Object> selTotalUserCnt();

    Map<String, Object> selOnlineUserCnt();
}
