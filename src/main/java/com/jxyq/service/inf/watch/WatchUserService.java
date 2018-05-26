package com.jxyq.service.inf.watch;

import com.jxyq.model.watch.AppSms;
import com.jxyq.model.watch.HeartBeatInf;
import com.jxyq.model.watch.UserInf;
import com.jxyq.model.watch.UserSms;

import java.util.List;
import java.util.Map;

public interface WatchUserService {
    UserInf selUserInfByImei(String imei);

    void updateUserInf(UserInf userInf);

    void updateUserNumber(String imei, String phone);

    void inUserInfMore(int user_id);

    HeartBeatInf selUserHeartBeatByUserId(int user_id);

    void inUserHeartBeat(HeartBeatInf heartBeatInf);

    void upUserHeartBeat(HeartBeatInf heartBeat);

    void repUserHeartBeat(HeartBeatInf heartBeatInf);

    void inRouteInf(int user_id, int route_en, int gps_mode, int gps_interval);

    List<UserSms> selUserSmsList(int user_id, String begin_date, String end_date);

    List<UserSms> qryUserSmsListByPage(Map<String, Object> map);

    List<AppSms> selAppSmsList(int readed);

    void upAppSmsReaded(int id, int readed);
}
