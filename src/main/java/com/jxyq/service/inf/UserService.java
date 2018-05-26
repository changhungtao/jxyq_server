package com.jxyq.service.inf;

import com.jxyq.model.health.UserTerminal;
import com.jxyq.model.others.OneTimeToken;
import com.jxyq.model.others.PushService;
import com.jxyq.model.user.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    /*手机验证码相关*/
    VerificationCode selVerificationCode(String phone, int used_for);

    void inVerificationCode(VerificationCode code);

    void upVerificationCode(VerificationCode code);

    int checkVerificationCode(String phone, String code, int used_for);

    /*用户相关*/
    User selUserByPhone(String phone);

    User selUserByUserId(int user_id);

    void upUserPoints(User user);

    void upUserOnlineStatus(int user_id, int status);

    Consumption selConsumption(int user_id, int normal);

    List<Consumption> selConsumption(Map<String, Object> map);

    Map<String, Object> selConsumptionCnt(Map<String, Object> map);

    void inConsumption(Consumption consumption);

    ConsumptionRule selConsumptionRule(int operation_type);

    List<ConsumptionRule> qryConsumptionRule();

    List<Map<String, Object>> selPointsRule();

    void upUserPassword(String phone, String password);

    /*角色相关*/
    User selUserWithRolesByPhone(String phone);

    //    注册
    void insertUser4Register(User user);

    //    插入积分
    void insertPoints(long id, Integer points);

    //    重置密码
    void resetUserPassword(String phone, String pwd);

    //    检查旧密码是否存在
    Map<String, String> getUser4Pwd(Long id);

    //  获取用户积分
    Map<String, Object> getUserScore(long id);


    //    获取详细积分
    List<Map<String, Object>> getUserDetailScore(Map<String, Object> map);

    //    获取新闻
    List<Map<String, Object>> getNews();

    //    获取宣传页
    List<Map<String, Object>> getExhibition();

    UserTerminal selUserTerminalByUserAndTer(Map<String, Object> map);

    void inUserTerminal(UserTerminal userTerminal);

    void upUserTerminalUpdateTm(UserTerminal userTerminal);

    //账户关联
    void inUserAssociated(UserAssociated associate);

    UserAssociated selUserAssociated(Map<String, Object> map);

    void upUserAssociated(UserAssociated associated);

    void delUserAssociated(int user_associated_id);

    List<Map<String, Object>> qryUserAssociated(int user_id);

    //获取推荐医院的信息
    List<Map<String, Object>> getRecoHospitals(double latitude, double longitude, double radius, int limit);

    //获取app版本信息
    Map<String, Object> getAppUpdate(int type);

    void insertTest(Map<String, Object> map);

    /*监护人相关*/
    int selUserCountByPupil(int pupil_id);

    void insertUserPupil(UserPupil userPupil);

    void upUserPupil(UserPupil userPupil);

    List<UserPupil> selPupilsInf(int user_id);

    List<UserPupil> selPupilsInf(int user_id, String imei);

    List<UserPupil> selPupilsInf(String imei);

    void deleteUserPupil(int user_pupil_id);

    List<PushService> selPushServiceByTerCatId(int terminal_catagory_id);

    List<PushService> selPushServiceByUserId(List<Integer> user_id_list);

    PushService selPushServiceByUid(int uid);

    void inPushService(PushService service);

    void upPushService(PushService service);

    void inUserRole(Map<String, Object> map);

    OneTimeToken selOneTimeToken(int user_id);

    OneTimeToken selOneTimeToken(String token);

    void inOneTimeToken(OneTimeToken oneTimeToken);

    void delOneTimeToken(int user_id);

    void upUserBbsPassword(int user_id, String password);

    Map<String, Object> selUserBbsPassword(int user_id);

    void upUserMeasuredTime(Map<String, Object> map);

    List<Integer> selUserIdByMaxMeasuredTime(long measured_at);
}
