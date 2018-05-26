package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.Role;
import com.jxyq.model.health.UserTerminal;
import com.jxyq.model.others.OneTimeToken;
import com.jxyq.model.others.PushService;
import com.jxyq.model.user.*;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /*验证码相关*/
    VerificationCode selVerificationCode(VerificationCode code);

    void inVerifacationCode(VerificationCode code);

    void upVerifacationCode(VerificationCode code);

    void upUserPoints(User user);

    /*用户相关*/
    User selUserByPhone(User user);

    User selUserInf(Map<String, Object> map);

    Consumption selConsumption(Map<String, Object> map);

    void inConsumption(Consumption consumption);

    ConsumptionRule selConsumptionRule(Map<String, Object> map);

    List<ConsumptionRule> qryConsumptionRule();

    List<Consumption> qryConsumption(Map<String, Object> map);

    Map<String, Object> selConsumptionCnt(Map<String, Object> map);

    void upUserPassword(Map<String, Object> map);

    /*角色相关*/
    List<Role> selRolesByUser(User user);

    //    根据手机号获取用户信息
    Map<String, String> selectUserByMobile(String phone);

    //    注册信息
    void insertUserForRegister(User user);

    void insertPoints(Map<String, Object> map);

    //    更新密码
    void updateUserByPwd(Map<String, String> map);

    //    检查旧密码是否存在
    Map<String, String> selectUserByPwd(Long id);

    List<Map<String, Object>> selPointsRule();

    //    获取用户积分
    Map<String, Object> selectScoreById(long id);

    List<Map<String, Object>> selectDetailById(Map<String, Object> map);

    //    获取新闻
    List<Map<String, Object>> selectNews();

    List<Map<String, Object>> selectExhibition();

    // 服务推送绑定
    void insertService(Map<String, Object> map);

    UserTerminal selUserTerminalByUserAndTer(Map<String, Object> map);

    void upUserTerminalUpdateTm(UserTerminal userTerminal);

    void inUserTerminal(UserTerminal userTerminal);

    //账户关联、查询、删除
    void inUserAssociated(UserAssociated associate);

    UserAssociated selUserAssociated(Map<String, Object> map);

    void upUserAssociated(UserAssociated associated);

    void delUserAssociated(Map<String, Object> map);

    List<Map<String, Object>> qryUserAssociated(Map<String, Object> map);

    //获取医院推荐信息
    List<Map<String, Object>> selectRecoHospitals(Map<String, Object> map);

    //查询版本信息
    Map<String, Object> selectAppUpdate(int type);

    void insertTest(Map<String, Object> map);

    /*被监护人相关*/
    Map<String, Object> selUserCountByPupil(Map<String, Object> map);

    void insertUserPupil(UserPupil userPupil);

    void upUserPupil(UserPupil userPupil);

    List<UserPupil> selPupilsInf(Map<String, Object> map);

    void deleteUserPupil(Map<String, Object> map);

    List<PushService> selPushServiceByTerCatId(Map<String, Object> map);

    List<PushService> selPushServiceByUserId(Map<String, Object> map);

    PushService selPushServiceByUid(Map<String, Object> map);

    void inPushService(PushService service);

    void upPushService(PushService service);

    void inUserRole(Map<String, Object> map);

    OneTimeToken selOneTimeToken(Map<String, Object> map);

    void inOneTimeToken(OneTimeToken oneTimeToken);

    void delOneTimeToken(Map<String, Object> map);

    void upUserBbsPassword(Map<String, Object> map);

    Map<String, Object> selUserBbsPassword(Map<String, Object> map);

    void upUserMeasuredTime(Map<String, Object> map);

    List<Integer> selUserIdByMaxMeasuredTime(Map<String, Object> map);

    void upUserOnlineStatus(Map<String, Object> map);
}
