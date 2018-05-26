package com.jxyq.app.user_login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.bbs.client.Client;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.BeanProperty.UserStatus;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.commons.util.TokenHandler;
import com.jxyq.model.Role;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.Consumption;
import com.jxyq.model.user.ConsumptionRule;
import com.jxyq.model.user.User;
import com.jxyq.model.user.VerificationCode;
import com.jxyq.service.inf.FileService;
import com.jxyq.service.inf.RoleService;
import com.jxyq.service.inf.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LoginInterface extends BaseInterface {
    public static final String AUTH_HEADER_NAME = "X-Auth-Token";
    public static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;
    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");
    private String token_secret = SysConfig.getInstance().getProperty("token.secret");


    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @RequestMapping(value = "/api/open/login", method = RequestMethod.POST)
    public void loginInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            JsonElement ePhone = params.get("phone");
            JsonElement ePassword = params.get("password");
            JsonElement eDevice = params.get("device");
            if (ePhone == null || ePassword == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            User user = userService.selUserByPhone(ePhone.getAsString());
            if (user == null) {
                setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                return;
            }

            String md5_psw = passwordEncoder.encodePassword(ePassword.getAsString(), md5_secret);
            if (!user.getPassword().equals(md5_psw)) {
                setParamWarnRes(res, "密码错误.", ErrorCode.INVALID_USER);
                return;
            }

            if (user.getStatus() == UserStatus.CLOSE) {
                setParamWarnRes(res, "用户被禁用.", ErrorCode.USER_CLOSE);
                return;
            }

            TokenHandler tokenHandler = new TokenHandler(token_secret);
            User token_user = new User();
            token_user.setUser_id(user.getUser_id());
            token_user.setPhone(user.getPhone());
            token_user.setPassword(user.getPassword());
            token_user.setExpires(System.currentTimeMillis() + TEN_DAYS);
            String token = tokenHandler.createTokenForUser(token_user);

            long cnt = getConsumptionTodayCount(user.getUser_id(), BeanProperty.OperationType.LOGIN_CLIENT);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.LOGIN_CLIENT);
            }

            if (eDevice != null) {
                String device = eDevice.getAsString();
                if (device.equals("2") || device.equals("3")) {
                    userService.upUserOnlineStatus(user.getUser_id(), BeanProperty.OnlineStatus.ONLINE);
                }
            }

            res.addHeader(AUTH_HEADER_NAME, token);
            JsonObject json = new JsonObject();
            json.add("user_id", gson.toJsonTree(user.getUser_id()));
            json.add("phone", gson.toJsonTree(user.getPhone()));
            json.add("points", gson.toJsonTree(user.getPoints()));
            json.add("avatar_url", gson.toJsonTree(user.getAvatar_url()));
            json.add("nick_name", gson.toJsonTree(user.getNick_name()));
            json.add("full_name", gson.toJsonTree(user.getFull_name()));
            json.add("gender", gson.toJsonTree(user.getGender()));
            json.add("birthday", gson.toJsonTree(user.getBirthday()));
            json.add("address", gson.toJsonTree(user.getAddress()));
            json.add("province_id", gson.toJsonTree(user.getProvince_id()));
            json.add("city_id", gson.toJsonTree(user.getCity_id()));
            json.add("zone_id", gson.toJsonTree(user.getZone_id()));
            json.add("qq", gson.toJsonTree(user.getQq()));
            json.add("email", gson.toJsonTree(user.getEmail()));
            json.add("height", gson.toJsonTree(user.getHeight()));
            json.add("weight", gson.toJsonTree(user.getWeight()));
            json.add("target_weight", gson.toJsonTree(user.getTarget_weight()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6 POST /api/user/logout*/
    @RequestMapping(value = "/api/user/logout", method = RequestMethod.POST)
    public void PostUserLogout(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            userService.upUserOnlineStatus(user.getUser_id(), BeanProperty.OnlineStatus.OFFLINE);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*检查用户TOKEN*/
    @RequestMapping(value = "/api/user/health", method = RequestMethod.POST)
    public void healthInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            content.setSuccess_message(user);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/api/user/password", method = RequestMethod.PUT)
    public void ChangPwdInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"old_password", "new_password"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String oldPwd = params.get("old_password").getAsString();
            String newPwd = params.get("new_password").getAsString();

            if (StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String pwdDb = user.getPassword();
            String oldPwdMd5 = passwordEncoder.encodePassword(oldPwd, md5_secret);

            if (!(pwdDb.equals(oldPwdMd5))) {
                setParamWarnRes(res, "旧密码错误", ErrorCode.INVALID_OLDPWD);
                return;
            }
            String newPwdMd5 = passwordEncoder.encodePassword(newPwd, md5_secret);
            userService.upUserPassword(user.getPhone(), newPwdMd5);
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/api/open/one_key_register", method = RequestMethod.POST)
    public void RegisterInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        Integer points = User.DEFAULT_POINTS;
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"phone", "verification_code"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String phone = params.get("phone").getAsString();
            String code = params.get("verification_code").getAsString();

            /*验证手机验证码*/
            int checkStatus = userService.checkVerificationCode(phone, code, VerificationCode.USAGE_REGISTER);
            if (checkStatus == 1) {
                setParamWarnRes(res, "请先获取注册验证码", ErrorCode.INVALID_CODE);
                return;
            }
            if (checkStatus == 2) {
                setParamWarnRes(res, "验证码已过期，请重新获取", ErrorCode.INVALID_CODE);
                return;
            }
            if (checkStatus == 3) {
                setParamWarnRes(res, "验证码错误，请重新输入", ErrorCode.INVALID_CODE);
                return;
            }

            /*判断手机号是否注册*/
            User checkUser = userService.selUserByPhone(phone);
            if (null != checkUser) {
                setParamWarnRes(res, "手机号已注册", ErrorCode.PHONEEXISTS);
                return;
            }

            String password = CommonUtil.getRandomString(6);
            String pwd = passwordEncoder.encodePassword(password, "");
            boolean random_password = true;
            if (params.has("password")) {
                String param_password = params.get("password").getAsString();
                if (!StringUtils.isBlank(param_password)) {
                    random_password = false;
                    pwd = param_password;
                }
            }
            Date date = new Date();
            /*插入数据库*/
            Map<String, String> map = new HashMap<>();
            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);
            User user = new User();
            user.setPhone(phone);
            user.setPassword(md5);
            user.setPoints(points);
            user.setRegistered_at(date.getTime() / 1000);
            user.setUpdated_at(date.getTime() / 1000);
            user.setStatus(UserStatus.NORMAL);//正常
            userService.insertUser4Register(user);

            Role role = roleService.selRoleByName("user_normal");
            if (role != null) {
                Map<String, Object> role_map = new HashMap<>(2);
                role_map.put("user_id", user.getUser_id());
                role_map.put("role_id", role.getId());
                userService.inUserRole(role_map);
            }

            if (random_password) {
                String sms_params = password;
                UcpaasCommon ucpaasCommon = new UcpaasCommon();
                ucpaasCommon.sendTemplateSMS(UcpaasCommon.init_template, phone, sms_params);
            }

            Client uc = new Client();
            String uc_user_register = uc.uc_user_register(phone, password, phone + "@mail.jiexunkeji.com");
            userService.upUserBbsPassword(user.getUser_id(), password);

            map.put("phone", phone);
            map.put("initial_pwd", password);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/api/open/password", method = RequestMethod.POST)
    public void ResetPassword(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"phone", "new_password", "verification_code"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String phone = params.get("phone").getAsString();
            String pwd = params.get("new_password").getAsString();
            String code = params.get("verification_code").getAsString();

            /*检查参数是否为空*/
            if (StringUtils.isBlank(phone) || StringUtils.isBlank(pwd) || StringUtils.isBlank(code)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            /*检查手机号是否正确*/
            if (isPhoneNum(phone)) {
                setParamWarnRes(res, "手机号格式错误", ErrorCode.INVALID_PHONE);
                return;
            }

            /*检查用户是否存在*/
            User checkUser = userService.selUserByPhone(phone);
            if (null == checkUser) {
                setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                return;
            }

            /*根据手机号获取验证码（同注册）*/
            VerificationCode vCodeDb = userService.selVerificationCode(phone, VerificationCode.USAGE_RESET_PASSWORD);
            if (null == vCodeDb) {
                setParamWarnRes(res, "验证码为空", ErrorCode.NONE_CODE);
                return;
            }
            long now = new Date().getTime() / 1000;
            if (vCodeDb.getCreated_at() + vCodeDb.getDuration() < now) {
                setParamWarnRes(res, "验证码过期", ErrorCode.INVALID_CODE);
                return;
            }

            /*验证码是否正确*/
            if (!(code.equals(vCodeDb.getContent()))) {
                setParamWarnRes(res, "验证码错误.", ErrorCode.ERROR_CODE);
                return;
            }

            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);
            userService.resetUserPassword(phone, md5);
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    public static boolean isPhoneNum(String phone) {
        if (phone == null) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d{11}$ (file://d%7b11%7d$/)");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public List<Consumption> getConsumptionToday(int user_id) throws Exception {
        Date date = new Date();
        Date begin_date = format.parse(format.format(date));
        long begin_time = begin_date.getTime() / 1000;
        long end_time = begin_time + 86399;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("happened_from", begin_time);
        map.put("happened_to", end_time);
        List<Consumption> consumption = userService.selConsumption(map);
        return consumption;
    }

    public long getConsumptionTodayCount(int user_id, int operation_type) throws Exception {
        Date date = new Date();
        Date begin_date = format.parse(format.format(date));
        long begin_time = begin_date.getTime() / 1000;
        long end_time = begin_time + 86399;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("happened_from", begin_time);
        map.put("happened_to", end_time);
        if (operation_type != -1) {
            map.put("operation_type", operation_type);
        }
        Map<String, Object> count = userService.selConsumptionCnt(map);
        return (long) count.get("count");
    }

    public void inConsumptionByRule(User user, int operation_type) {
        ConsumptionRule con_rule = userService.selConsumptionRule(operation_type);

        Consumption con = new Consumption();
        con.setComsumption_id(0);
        con.setHappened_at((new Date()).getTime() / 1000);
        con.setUser_id(user.getUser_id());
        con.setPoints(con_rule.getPoints());
        con.setNew_points(user.getPoints() + con_rule.getPoints());
        con.setOperation_type(operation_type);
        con.setDescription(con_rule.getDescription());
        userService.inConsumption(con);

        user.setPoints(user.getPoints() + con_rule.getPoints());
        userService.upUserPoints(user);
    }
}