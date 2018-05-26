package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.AccountMapper;
import com.jxyq.model.user.User;
import com.jxyq.model.user.UserDetail;
import com.jxyq.service.inf.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/4/1.
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountMapper accountMapper;
//  注册
    @Override
    public void insertUser4Register(  Map<String, Object> map) {
        accountMapper.insertUserForRegister(map);
    }

    //    DB操作
    @Override
    public User selUserById(int id){
        return accountMapper.selUserById(id);
    }
    @Override
    public void  upUserStatus(int id,int status){
        Map<String, Object>map=new HashMap<>();
        map.put("id",id);
        map.put("status",status);
        accountMapper.upUserStatus(map);
    }
    @Override
    public List<Map<String, Object>> selUserByPage(Map<String,Object>map,PagingCriteria pageInf){
        map.put(BeanProperty.PAGING,pageInf);
        return accountMapper.selUserByPage(map);
    }
    @Override
    public void putUserInfo(int id,UserDetail userQuery){
        Map<String, Object>map=new HashMap<>();
        map.put("user_id",id);
        map.put("password",userQuery.getPassword());
        map.put("nick_name",userQuery.getNick_name());
        map.put("full_name",userQuery.getFull_name());
        map.put("avatar_url",userQuery.getAvatar_url());
        map.put("gender",userQuery.getGender());
        map.put("birthday",userQuery.getBirthday());
        map.put("address",userQuery.getAddress());
        map.put("qq",userQuery.getQq());
        map.put("email",userQuery.getEmail());
        map.put("height",userQuery.getHeight());
        map.put("weight",userQuery.getWeight());
        map.put("target_weight",userQuery.getTarget_weight());
        map.put("operation_type",userQuery.getOperation_type());
        map.put("delta_points",userQuery.getDelta_points());
        map.put("reason",userQuery.getReason());
        accountMapper.putUserInfo(map);
    }

    @Override
    public void upUserInf(Map<String, Object> map) {
        accountMapper.upUserInf(map);
    }

    @Override
    public Map<String, Object> selOnlineUserCnt() {
        return accountMapper.selOnlineUserCnt();
    }

    @Override
    public Map<String, Object> selTotalUserCnt() {
        return accountMapper.selTotalUserCnt();
    }
}
