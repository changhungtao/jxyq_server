package com.jxyq.service.impl.watch;

import com.jxyq.mapper.watch_mapper.UserInfMapper;
import com.jxyq.model.watch.AppSms;
import com.jxyq.model.watch.HeartBeatInf;
import com.jxyq.model.watch.UserInf;
import com.jxyq.model.watch.UserSms;
import com.jxyq.service.inf.watch.WatchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WatchUserServiceImpl implements WatchUserService {
    @Autowired
    private UserInfMapper userInfMapper;

    @Override
    public void updateUserInf(UserInf userInf) {
        userInfMapper.updateUserInf(userInf);
    }

    @Override
    public void updateUserNumber(String imei, String phone) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("imei", imei);
        map.put("number", phone);
        userInfMapper.updateUserNumber(map);
    }

    @Override
    public UserInf selUserInfByImei(String imei) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("imei", imei);
        return userInfMapper.selUserInf(map);
    }

    @Override
    public void inUserInfMore(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", 0);
        map.put("user_id", user_id);
        userInfMapper.inUserInfMore(map);
    }

    @Override
    public HeartBeatInf selUserHeartBeatByUserId(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return userInfMapper.selUserHeartBeatInf(map);
    }

    @Override
    public void inUserHeartBeat(HeartBeatInf heartBeatInf) {
        userInfMapper.inUserHeartBeat(heartBeatInf);
    }

    @Override
    public void upUserHeartBeat(HeartBeatInf heartBeat) {
        userInfMapper.upUserHeartBeat(heartBeat);
    }

    @Override
    public void repUserHeartBeat(HeartBeatInf heartBeatInf) {
        HeartBeatInf db = selUserHeartBeatByUserId(heartBeatInf.getUser_id());
        if (db == null) {
            inUserHeartBeat(heartBeatInf);
        } else {
            heartBeatInf.setId(db.getId());
            upUserHeartBeat(heartBeatInf);
        }
    }

    @Override
    public void inRouteInf(int user_id, int route_en, int gps_mode, int gps_interval) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("user_id", user_id);
        map.put("route_en", route_en);
        map.put("gps_mode", gps_mode);
        map.put("gps_interval", gps_interval);
        userInfMapper.inRouteInf(map);
    }

    @Override
    public List<UserSms> selUserSmsList(int user_id, String begin_date, String end_date) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        map.put("begin_date", begin_date);
        map.put("end_date", end_date);
        return userInfMapper.selUserSmsList(map);
    }

    @Override
    public List<UserSms> qryUserSmsListByPage(Map<String, Object> map) {
        return userInfMapper.qryUserSmsListByPage(map);
    }

    @Override
    public List<AppSms> selAppSmsList(int readed) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("readed", readed);
        return userInfMapper.selAppSmsList(map);

    }

    @Override
    public void upAppSmsReaded(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        userInfMapper.upAppSmsReaded(map);
    }
}
