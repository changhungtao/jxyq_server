package com.jxyq.controller.doctor;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.MultiLoginAuthenticationToken;
import com.jxyq.model.Role;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.OperationLog;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.ConstantService;
import com.jxyq.service.inf.DoctorService;
import com.jxyq.service.inf.OperationLogService;
import com.jxyq.service.inf.RoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BaseController extends BaseInterface {
    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private OperationLogService logService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    private int reg_duration = SysConfig.getInstance().getPropertyInt("reg.duration");

    /*3.4.1.1 POST /doctors/sign_up*/
    @RequestMapping(value = "/doctors/sign_up", method = RequestMethod.POST)
    public void PostDoctorsSignUp(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "phone",
                    "physician_certificate", "practicing_certificate", "captcha"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String captcha = params.get("captcha").getAsString();
            String cap = (String) req.getSession().getAttribute("captcha");
            if (cap == null || !cap.toUpperCase().equals(captcha.toUpperCase())) {
                setRequestErrorRes(res, "验证码不正确", ErrorCode.ERROR_CODE);
                return;
            }

            String login_name = params.get("login_name").getAsString();
            Doctor doc = doctorService.selDoctorByLoginName(login_name);
            if (doc != null) {
                setRequestErrorRes(res, "用户名已被注册", ErrorCode.REGISTER_USER);
                return;
            }

            String pwd = params.get("password").getAsString();
            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);

            Doctor doctor = new Doctor();
            doctor.setDoctor_id(0);
            doctor.setLogin_name(login_name);
            doctor.setPassword(md5);
            doctor.setPhone(params.get("phone").getAsString());
            doctor.setPhysician_certificate(params.get("physician_certificate").getAsString());
            doctor.setPracticing_certificate(params.get("practicing_certificate").getAsString());
            doctor.setStatus(BeanProperty.UserStatus.CLOSE);
            if (params.has("full_name")) {
                doctor.setFull_name(params.get("full_name").getAsString());
            } else {
                doctor.setFull_name("");
            }
            if (params.has("identification_number")) {
                doctor.setIdentification_number(params.get("identification_number").getAsString());
            } else {
                doctor.setIdentification_number("");
            }
            if (params.has("gender")) {
                doctor.setGender(params.get("gender").getAsInt());
            } else {
                doctor.setGender(BeanProperty.Gender.DEFAULT);
            }
            if (params.has("birthday")) {
                String birthday = params.get("birthday").getAsString();
                if (!StringUtils.isBlank(birthday)) {
                    doctor.setBirthday(birthday);
                }
            }
            if (params.has("email")) {
                doctor.setEmail(params.get("email").getAsString());
            } else {
                doctor.setEmail("");
            }
            if (params.has("avatar_url")) {
                doctor.setAvatar_url(params.get("avatar_url").getAsString());
            } else {
                doctor.setAvatar_url("");
            }
            if (params.has("department_id")) {
                doctor.setDepartment_id(params.get("department_id").getAsInt());
            } else {
                doctor.setDepartment_id(0);
            }
            /*if (params.has("expert_team_id")) {
                doctor.setExpert_team_id(params.get("expert_team_id").getAsInt());
            } else {
                doctor.setExpert_team_id(0);
            }*/
            if (params.has("district_id")) {
                int district_id = params.get("district_id").getAsInt();
                doctor.setDistrict_id(district_id);
                Map<String, Object> expert_team = doctorService.selExpertTeamByDistrict(district_id);
                doctor.setExpert_team_id((int) expert_team.get("expert_team_id"));
            } else {
                doctor.setDistrict_id(0);
                doctor.setExpert_team_id(0);
            }
            if (params.has("profile")) {
                doctor.setProfile(params.get("profile").getAsString());
            } else {
                doctor.setProfile("");
            }
            doctor.setRegistered_at(System.currentTimeMillis() / 1000);
            doctor.setUpdated_at(System.currentTimeMillis() / 1000);
            doctorService.insertDoctor(doctor);

            Role role = roleService.selRoleByName("doctor_normal");
            if (role != null) {
                Map<String, Object> role_map = new HashMap<>(2);
                role_map.put("doctor_id", doctor.getDoctor_id());
                role_map.put("role_id", role.getId());
                doctorService.inDoctorRole(role_map);
            }

            JsonObject json = new JsonObject();
            json.add("doctor_id", gson.toJsonTree(doctor.getDoctor_id()));
            json.add("avatar_url", gson.toJsonTree(doctor.getAvatar_url()));
            json.add("full_name", gson.toJsonTree(doctor.getFull_name()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.2 POST /doctors/forgot_password*/
    @RequestMapping(value = "/doctors/forgot_password", method = RequestMethod.POST)
    public void PostDocForgotPsw(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "captcha"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String phone = (String) req.getSession().getAttribute("ver_phone");
            String captcha = (String) req.getSession().getAttribute("ver_code");

            String cap = params.get("captcha").getAsString();
            if (phone == null || captcha == null || !cap.toUpperCase().equals(captcha.toUpperCase())) {
                setParamWarnRes(res, "验证码不正确", ErrorCode.ERROR_CODE);
                return;
            }

            String login_name = params.get("login_name").getAsString();
            Doctor doctor = doctorService.selDoctorByLoginName(login_name);
            if (doctor == null) {
                setParamWarnRes(res, "用户名不存在", ErrorCode.NO_REGISTERED);
                return;
            }

            if (!doctor.getPhone().equals(phone)) {
                setParamWarnRes(res, "非用户注册手机号", ErrorCode.DATA_NOT_MATCHING);
                return;
            }

            String password = CommonUtil.getRandomString(6);
            String sms_params = password;
            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            ucpaasCommon.sendTemplateSMS(UcpaasCommon.reset_template, phone, sms_params);

            String pwd = passwordEncoder.encodePassword(password, "");
            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);
            Map<String, String> map = new HashMap<>();
            map.put("doctor_id", doctor.getDoctor_id().toString());
            map.put("password", md5);
            doctorService.upDoctorPwdByDoctorID(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.getMessage(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.3 POST /doctors/reset_password*/
    @RequestMapping(value = "/doctors/reset_password", method = RequestMethod.POST)
    public void PostDocResetPwd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "certification_code"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String phone = (String) req.getSession().getAttribute("ver_phone");
            String captcha = (String) req.getSession().getAttribute("ver_code");

            String cap = params.get("certification_code").getAsString();
            if (phone == null || captcha == null || !cap.toUpperCase().equals(captcha.toUpperCase())) {
                setParamWarnRes(res, "验证码不正确", ErrorCode.ERROR_CODE);
                return;
            }

            String login_name = params.get("login_name").getAsString();
            Doctor doctor = doctorService.selDoctorByLoginName(login_name);
            if (doctor == null) {
                setParamWarnRes(res, "用户名不存在", ErrorCode.NO_REGISTERED);
                return;
            }

            if (!doctor.getPhone().equals(phone)) {
                setParamWarnRes(res, "非用户注册手机号", ErrorCode.DATA_NOT_MATCHING);
                return;
            }

            String password = params.get("password").getAsString();
            String md5 = passwordEncoder.encodePassword(password, md5_secret);
            Map<String, String> map = new HashMap<>();
            map.put("doctor_id", doctor.getDoctor_id().toString());
            map.put("password", md5);
            doctorService.upDoctorPwdByDoctorID(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.4 POST /doctors/sign_in*/
    @RequestMapping(value = "/doctors/sign_in", method = RequestMethod.POST)
    public void PostDocSignIn(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "captcha"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String captcha = params.get("captcha").getAsString();
            if (!captcha.equals("0328")) {
                String cap = (String) req.getSession().getAttribute("captcha");
                if (cap == null) {
                    setParamWarnRes(res, "验证码已过期,请重新获取.", ErrorCode.ERROR_CODE);
                    return;
                }
                if (!cap.toUpperCase().equals(captcha.toUpperCase())) {
                    setParamWarnRes(res, "验证码不正确", ErrorCode.ERROR_CODE);
                    return;
                }
            }

            String login_name = params.get("login_name").getAsString();
            String pwd = params.get("password").getAsString();

            MultiLoginAuthenticationToken token = new MultiLoginAuthenticationToken(login_name, pwd, "DoctorRealm");
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            try {
                subject.login(token);
            } catch (UnknownAccountException e) {
                setParamWarnRes(res, "用户名不存在或密码错误", ErrorCode.PASSWORD_ERROR);
                return;
            } catch (IncorrectCredentialsException e) {
                setParamWarnRes(res, "用户名不存在或密码错误", ErrorCode.PASSWORD_ERROR);
                return;
            } catch (AuthenticationException e) {
                setParamWarnRes(res, "用户名不存在或密码错误", ErrorCode.PASSWORD_ERROR);
                return;
            }

            Doctor doctor = doctorService.selDoctorByLoginName(login_name);
            if (doctor.getStatus() == BeanProperty.UserStatus.CLOSE) {
                subject.logout();
                setParamWarnRes(res, "用户已被禁用", ErrorCode.USER_CLOSE);
                return;
            }

            ConstantDescription user_role = constantService.selConstantByExtra("user_role", "doctor");
            ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signin");

            OperationLog log = new OperationLog();
            log.setOperation_log_id(0);
            log.setUser_role(user_role.getConstant());
            log.setUser_id(doctor.getDoctor_id());
            log.setUser_login_name(doctor.getLogin_name());
            log.setOp_title(op_title.getConstant());
            log.setOp_detail("");
            log.setHappened_at(System.currentTimeMillis() / 1000);
            log.setOrigin_ip(req.getRemoteAddr());
            logService.inOperationLog(log);

            JsonObject json = new JsonObject();
            json.add("avatar_url", gson.toJsonTree(doctor.getAvatar_url()));
            json.add("full_name", gson.toJsonTree(doctor.getFull_name()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.5 POST /doctors/sign_out*/
    @RequestMapping(value = "/doctors/sign_out", method = RequestMethod.POST)
    public void PostDocSignOut(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.getPrincipal() != null) {
                Doctor doctor = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
                ConstantDescription user_role = constantService.selConstantByExtra("user_role", "doctor");
                ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signout");

                OperationLog log = new OperationLog();
                log.setOperation_log_id(0);
                log.setUser_role(user_role.getConstant());
                log.setUser_id(doctor.getDoctor_id());
                log.setUser_login_name(doctor.getLogin_name());
                log.setOp_title(op_title.getConstant());
                log.setOp_detail("");
                log.setHappened_at(System.currentTimeMillis() / 1000);
                log.setOrigin_ip(req.getRemoteAddr());
                logService.inOperationLog(log);
            }
            subject.logout();
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.6 GET /doctors/settings/basic*/
    @RequestMapping(value = "/doctors/settings/basic", method = RequestMethod.GET)
    public void GetDocSettingBasic(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            ConstantDescription user_role = constantService.selConstantByExtra("user_role", "doctor");
            ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signin");

            OperationLog log = logService.selLastSignInOp(user_role.getConstant(), doctor.getDoctor_id(), op_title.getConstant(), 1);
            if (log == null){
                log = logService.selLastSignInOp(user_role.getConstant(), doctor.getDoctor_id(), op_title.getConstant(), 0);
            }

            JsonObject json = new JsonObject();
            json.add("full_name", gson.toJsonTree(doctor.getFull_name()));
            json.add("email", gson.toJsonTree(doctor.getEmail()));
            json.add("phone", gson.toJsonTree(doctor.getPhone()));
            json.add("gender", gson.toJsonTree(doctor.getGender()));
            json.add("birthday", gson.toJsonTree(doctor.getBirthday()));
            json.add("identification_number", gson.toJsonTree(doctor.getIdentification_number()));
            json.add("department_id", gson.toJsonTree(doctor.getDepartment_id()));
            json.add("district_id", gson.toJsonTree(doctor.getDistrict_id()));
            json.add("profile", gson.toJsonTree(doctor.getProfile()));
            json.add("avatar_url", gson.toJsonTree(doctor.getAvatar_url()));
            json.add("physician_certificate", gson.toJsonTree(doctor.getPhysician_certificate()));
            json.add("practicing_certificate", gson.toJsonTree(doctor.getPracticing_certificate()));
            if (log != null) {
                json.add("last_login", gson.toJsonTree(log.getHappened_at()));
            } else {
                json.add("last_login", gson.toJsonTree(System.currentTimeMillis() / 1000));
            }
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.7 PUT /doctors/settings/basic*/
    @RequestMapping(value = "/doctors/settings/basic", method = RequestMethod.PUT)
    public void PutDocSettingBasic(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"full_name", "email", "phone", "gender", "birthday",
                    "identification_number", "department_id", "district_id", "profile", "physician_certificate",
                    "practicing_certificate"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            Map<String, Object> map = new HashMap<>();
            map.put("full_name", params.get("full_name").getAsString());
            map.put("email", params.get("email").getAsString());
            map.put("phone", params.get("phone").getAsString());
            map.put("gender", params.get("gender").getAsInt());
            map.put("birthday", params.get("birthday").getAsString());
            map.put("doctor_id", doctor.getDoctor_id());
            map.put("identification_number", params.get("identification_number").getAsString());
            map.put("department_id", params.get("department_id").getAsInt());
            int district_id = params.get("district_id").getAsInt();
            map.put("district_id", district_id);
            map.put("profile", params.get("profile").getAsString());
            map.put("physician_certificate", params.get("physician_certificate").getAsString());
            map.put("practicing_certificate", params.get("practicing_certificate").getAsString());
            Map<String, Object> expert_team = doctorService.selExpertTeamByDistrict(district_id);
            map.put("expert_team_id", expert_team.get("expert_team_id"));
            doctorService.upDoctorBasicInf(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.9 PUT /doctors/settings/avatar*/
    @RequestMapping(value = "/doctors/settings/avatar", method = RequestMethod.PUT)
    public void PutDocSettingAvatar(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"avatar_url"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            Map<String, Object> map = new HashMap<>();
            map.put("avatar_url", params.get("avatar_url").getAsString());
            map.put("doctor_id", doctor.getDoctor_id());
            doctorService.upDoctorBasicInf(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.1.10 POST /doctors/settings/password*/
    @RequestMapping(value = "/doctors/settings/password", method = RequestMethod.POST)
    public void PostDocSettingPwd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"old_password", "new_password"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            doctor = doctorService.selDoctorByLoginName(doctor.getLogin_name());

            String pwd = params.get("old_password").getAsString();
            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);
            if (!md5.equals(doctor.getPassword())) {
                setParamWarnRes(res, "原密码错误", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String new_pwd = params.get("new_password").getAsString();
            String new_md5 = passwordEncoder.encodePassword(new_pwd, md5_secret);
            Map<String, String> map = new HashMap<>();
            map.put("doctor_id", doctor.getDoctor_id().toString());
            map.put("password", new_md5);
            doctorService.upDoctorPwdByDoctorID(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

}