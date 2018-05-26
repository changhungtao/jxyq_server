package com.jxyq.mapper.watch_mapper;

import com.jxyq.model.watch.AppSms;
import com.jxyq.model.watch.HeartBeatInf;
import com.jxyq.model.watch.UserInf;
import com.jxyq.model.watch.UserSms;

import java.util.List;
import java.util.Map;

public interface UserInfMapper {
    UserInf selUserInf(Map<String, Object> map);

    void updateUserInf(UserInf userInf);

    void updateUserNumber(Map<String, Object> map);

    void inUserInfMore(Map<String, Object> map);

    HeartBeatInf selUserHeartBeatInf(Map<String, Object> map);

    void inUserHeartBeat(HeartBeatInf heartBeatInf);

    void upUserHeartBeat(HeartBeatInf heartBeat);

    void inRouteInf(Map<String, Object> map);

    List<UserSms> selUserSmsList(Map<String, Object> map);

    List<UserSms> qryUserSmsListByPage(Map<String, Object> map);

    List<AppSms> selAppSmsList(Map<String, Object> map);

    void upAppSmsReaded(Map<String, Object> map);
}
