package com.jxyq.service.impl;

import com.jxyq.mapper.normal_mapper.UserMapper;
import com.jxyq.model.health.UserTerminal;
import com.jxyq.model.others.OneTimeToken;
import com.jxyq.model.others.PushService;
import com.jxyq.model.user.*;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.selUserByPhone(user);
    }

    @Override
    public User selUserByUserId(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return userMapper.selUserInf(map);
    }

    @Override
    public void upUserPoints(User user) {
        userMapper.upUserPoints(user);
    }

    @Override
    public Consumption selConsumption(int user_id, int normal) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        map.put("operation_type", normal);
        return userMapper.selConsumption(map);
    }

    @Override
    public List<Consumption> selConsumption(Map<String, Object> map) {
        return userMapper.qryConsumption(map);
    }

    @Override
    public Map<String, Object> selConsumptionCnt(Map<String, Object> map) {
        return userMapper.selConsumptionCnt(map);
    }

    @Override
    public void inConsumption(Consumption consumption) {
        userMapper.inConsumption(consumption);
    }

    @Override
    public List<ConsumptionRule> qryConsumptionRule() {
        return userMapper.qryConsumptionRule();
    }

    @Override
    public ConsumptionRule selConsumptionRule(int operation_type) {
        Map<String, Object> map = new HashMap<>();
        map.put("operation_type", operation_type);
        return userMapper.selConsumptionRule(map);
    }

    @Override
    public VerificationCode selVerificationCode(String phone, int used_for) {
        VerificationCode code = new VerificationCode();
        code.setPhone(phone);
        code.setUsed_for(used_for);
        return userMapper.selVerificationCode(code);
    }

    @Override
    public void inVerificationCode(VerificationCode code) {
        userMapper.inVerifacationCode(code);
    }

    @Override
    public void upVerificationCode(VerificationCode code) {
        userMapper.upVerifacationCode(code);
    }

    @Override
    public int checkVerificationCode(String phone, String code, int used_for) {
        VerificationCode vCodeDb = selVerificationCode(phone, used_for);
        if (vCodeDb == null) {
            return 1; /*验证码不存在*/
        }
        long now = (new Date()).getTime() / 1000;
        long deadlineTime = vCodeDb.getCreated_at() + vCodeDb.getDuration();
        String code_db = vCodeDb.getContent();
        if (deadlineTime < now) {
            return 2; /*验证码过期*/
        }
        if (!code.equals(code_db)) {
            return 3; /*验证码错误*/
        }
        return 0;
    }

    @Override
    public User selUserWithRolesByPhone(String phone) {
        User user = selUserByPhone(phone);
        if (null == user) return null;
        user.setRoleList(userMapper.selRolesByUser(user));
        return user;
    }

    @Override
    public void insertUser4Register(User user) {
        userMapper.insertUserForRegister(user);
    }

    //  插入积分
    @Override
    public void insertPoints(long id, Integer points) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        map.put("points", points);
        userMapper.insertPoints(map);
    }

    //    更改密码
    @Override
    public void resetUserPassword(String phone, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pwd);
        userMapper.updateUserByPwd(map);
    }

    //    检查旧密码是否存在
    @Override
    public Map<String, String> getUser4Pwd(Long id) {
        return userMapper.selectUserByPwd(id);
    }

    @Override
    public void upUserPassword(String phone, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        userMapper.upUserPassword(map);
    }

    //    获取积分
    @Override
    public Map<String, Object> getUserScore(long id) {
        return userMapper.selectScoreById(id);
    }


    @Override
    public List<Map<String, Object>> getUserDetailScore(Map<String, Object> map) {
        return userMapper.selectDetailById(map);
    }

    @Override
    public List<Map<String, Object>> selPointsRule() {
        return userMapper.selPointsRule();
    }

    //  获取新闻
    @Override
    public List<Map<String, Object>> getNews() {
        return userMapper.selectNews();
    }

    //获取宣传页
    @Override
    public List<Map<String, Object>> getExhibition() {
        return userMapper.selectExhibition();
    }

    @Override
    public UserTerminal selUserTerminalByUserAndTer(Map<String, Object> map) {
        return userMapper.selUserTerminalByUserAndTer(map);
    }

    @Override
    public void inUserTerminal(UserTerminal userTerminal) {
        userMapper.inUserTerminal(userTerminal);
    }

    @Override
    public void upUserTerminalUpdateTm(UserTerminal userTerminal) {
        userMapper.upUserTerminalUpdateTm(userTerminal);
    }

    @Override
    public void inUserAssociated(UserAssociated associate) {
        userMapper.inUserAssociated(associate);
    }

    @Override
    public UserAssociated selUserAssociated(Map<String, Object> map) {
        return userMapper.selUserAssociated(map);
    }

    @Override
    public void upUserAssociated(UserAssociated associated) {
        userMapper.upUserAssociated(associated);
    }

    @Override
    public void delUserAssociated(int user_associated_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_associated_id", user_associated_id);
        userMapper.delUserAssociated(map);
    }

    @Override
    public List<Map<String, Object>> qryUserAssociated(int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        return userMapper.qryUserAssociated(map);
    }

    //获取推荐医院信息
    @Override
    public List<Map<String, Object>> getRecoHospitals(double latitude, double longitude, double radius, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        map.put("radius", String.valueOf(radius));
        map.put("limit", limit);
        return userMapper.selectRecoHospitals(map);
    }

    //获取app版本信息
    public Map<String, Object> getAppUpdate(int type) {
        return userMapper.selectAppUpdate(type);
    }

    @Override
    public void insertTest(Map<String, Object> map) {
        userMapper.insertTest(map);
    }

    @Override
    public int selUserCountByPupil(int pupil_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_pupil_id", pupil_id);
        Map<String, Object> map_result = userMapper.selUserCountByPupil(map);
        return (int) map_result.get("count");
    }

    @Override
    public void insertUserPupil(UserPupil userPupil) {
        userMapper.insertUserPupil(userPupil);
    }

    @Override
    public void upUserPupil(UserPupil userPupil) {
        userMapper.upUserPupil(userPupil);
    }

    @Override
    public List<UserPupil> selPupilsInf(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return userMapper.selPupilsInf(map);
    }

    @Override
    public List<UserPupil> selPupilsInf(int user_id, String imei) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("pupil_imei", imei);
        return userMapper.selPupilsInf(map);
    }

    @Override
    public List<UserPupil> selPupilsInf(String imei) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("pupil_imei", imei);
        return userMapper.selPupilsInf(map);
    }

    @Override
    public void deleteUserPupil(int user_pupil_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_pupil_id", user_pupil_id);
        userMapper.deleteUserPupil(map);
    }

    @Override
    public List<PushService> selPushServiceByTerCatId(int terminal_catagory_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("terminal_catagory_id", terminal_catagory_id);
        return userMapper.selPushServiceByTerCatId(map);
    }

    @Override
    public List<PushService> selPushServiceByUserId(List<Integer> user_id_list) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id_list", user_id_list);
        return userMapper.selPushServiceByUserId(map);
    }

    @Override
    public PushService selPushServiceByUid(int uid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("uid", uid);
        return userMapper.selPushServiceByUid(map);
    }

    @Override
    public void inPushService(PushService service) {
        userMapper.inPushService(service);
    }

    @Override
    public void upPushService(PushService service) {
        userMapper.upPushService(service);
    }

    @Override
    public void inUserRole(Map<String, Object> map) {
        userMapper.inUserRole(map);
    }

    @Override
    public OneTimeToken selOneTimeToken(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return userMapper.selOneTimeToken(map);
    }

    @Override
    public OneTimeToken selOneTimeToken(String token) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("token", token);
        return userMapper.selOneTimeToken(map);
    }

    @Override
    public void inOneTimeToken(OneTimeToken oneTimeToken) {
        userMapper.inOneTimeToken(oneTimeToken);
    }

    @Override
    public void delOneTimeToken(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        userMapper.delOneTimeToken(map);
    }

    @Override
    public Map<String, Object> selUserBbsPassword(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return userMapper.selUserBbsPassword(map);
    }

    @Override
    public void upUserBbsPassword(int user_id, String password) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("bbs_password", password);
        userMapper.upUserBbsPassword(map);
    }

    @Override
    public void upUserMeasuredTime(Map<String, Object> map) {
        userMapper.upUserMeasuredTime(map);
    }

    @Override
    public List<Integer> selUserIdByMaxMeasuredTime(long measured_at) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("max_measured_at", measured_at);
        return userMapper.selUserIdByMaxMeasuredTime(map);
    }

    @Override
    public void upUserOnlineStatus(int user_id, int status) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        map.put("online_status", status);
        userMapper.upUserOnlineStatus(map);
    }
}
