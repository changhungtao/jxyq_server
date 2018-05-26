package com.jxyq.controller.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.MultiLoginAuthenticationToken;
import com.jxyq.model.Role;
import com.jxyq.model.health.DeviceType;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.health.TerminalCatagory;
import com.jxyq.model.health.TerminalCatagoryTemplate;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.OperationLog;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by wujj-fnst on 2015/6/2.
 */
@Controller
public class ManufactoryController extends BaseInterface {
    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");
    @Autowired
    private ManufactoryService manufactoryService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OperationLogService logService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserHealthService userHealthService;

    private int reg_duration = SysConfig.getInstance().getPropertyInt("reg.duration");

    //    3.5.1.1	POST /manufactories/sign_up
    @RequestMapping(value = "/manufactories/sign_up", method = RequestMethod.POST)
    public void PostManufactoriesignUp(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "contactor",
                    "department", "telephone", "phone",
                    "full_name", "code", "province_id",
                    "city_id", "zone_id", "address",
                    "device_type_ids", "captcha", "business_licence"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String captcha = params.get("captcha").getAsString();
            if (!captcha.equals("0328")) {
                String cap = (String) req.getSession().getAttribute("captcha");
                if (cap == null || !cap.toUpperCase().equals(captcha.toUpperCase())) {
                    setParamWarnRes(res, "验证码不正确", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }

                String login_name = params.get("login_name").getAsString();
                Manufactory fac = manufactoryService.selManufactoryByLoginName(login_name);
                if (fac != null) {
                    setRequestErrorRes(res, "用户名已被注册", ErrorCode.REGISTER_USER);
                    return;
                }
            }

            Manufactory m = gson.fromJson(params, Manufactory.class);
            String mad_psw = passwordEncoder.encodePassword(m.getPassword(), md5_secret);
            m.setPassword(mad_psw);
            m.setRegistered_at(System.currentTimeMillis() / 1000);
            m.setUpdated_at(System.currentTimeMillis() / 1000);
            m.setStatus(BeanProperty.UserStatus.NORMAL);
            manufactoryService.insertManufactory(m);

            List<Integer> device_type_ids = m.getDevice_type_ids();
            Map<String, Object> factory_map = new HashMap<>();
            factory_map.put("manufactory_id", m.getManufactory_id());
            if (device_type_ids != null && device_type_ids.size() > 0) {
                manufactoryService.delManDevice(factory_map);
                for (Integer item : device_type_ids) {
                    factory_map.put("device_type_id", item);
                    manufactoryService.insertManDevice(factory_map);
                }
            }

            Role role = roleService.selRoleByName("factory_normal");
            if (role != null) {
                Map<String, Object> role_map = new HashMap<>(2);
                role_map.put("manufactory_id", m.getManufactory_id());
                role_map.put("role_id", role.getId());
                manufactoryService.inFactoryRole(role_map);
            }

            content.setSuccess_message(m.getManufactory_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.5.1.2	POST /manufactories/forgot_password
    @RequestMapping(value = "/manufactories/forgot_password", method = RequestMethod.POST)
    public void PostManufactoriesPsw(HttpServletRequest req, HttpServletResponse res) {
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
            Manufactory manufactory = manufactoryService.selManufactoryByLoginName(login_name);
            if (manufactory == null) {
                setParamWarnRes(res, "用户名不存在", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            if (!manufactory.getPhone().equals(phone)) {
                setParamWarnRes(res, "非用户注册手机号", ErrorCode.DATA_NOT_MATCHING);
                return;
            }

            String password = CommonUtil.getRandomString(6);
            String sms_params = password;
            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            ucpaasCommon.sendTemplateSMS(UcpaasCommon.reset_template, phone, sms_params);

            String pwd = passwordEncoder.encodePassword(password, "");
            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manufactory.getManufactory_id());
            map.put("password", md5);
            manufactoryService.upManuPwd(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    //3.5.1.3	POST /manufactories/reset_password
    @RequestMapping(value = "/manufactories/reset_password", method = RequestMethod.POST)
    public void PostMuResetPwd(HttpServletRequest req, HttpServletResponse res) {
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
            Manufactory manufactory = manufactoryService.selManufactoryByLoginName(login_name);
            if (manufactory == null) {
                setParamWarnRes(res, "用户名不存在", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            if (!manufactory.getPhone().equals(phone)) {
                setParamWarnRes(res, "非用户注册手机号", ErrorCode.DATA_NOT_MATCHING);
                return;
            }

            String password = params.get("password").getAsString();
            String md5 = passwordEncoder.encodePassword(password, md5_secret);
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manufactory.getManufactory_id());
            map.put("password", md5);
            manufactoryService.upManuPwd(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.1.4 POST /manufactories/sign_in*/
    @RequestMapping(value = "/manufactories/sign_in", method = RequestMethod.POST)
    public void PostDocSignIn(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "captcha"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setAjaxResE400(res, "缺少必要请求参数");
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

            MultiLoginAuthenticationToken token = new MultiLoginAuthenticationToken(login_name, pwd, "ManufactoryRealm");
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

            Manufactory manufactory = manufactoryService.selManufactoryByLoginName(login_name);
            if (manufactory.getStatus() == BeanProperty.UserStatus.CLOSE) {
                subject.logout();
                setParamWarnRes(res, "用户已被禁用", ErrorCode.USER_CLOSE);
                return;
            }

            ConstantDescription user_role = constantService.selConstantByExtra("user_role", "manufactory");
            ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signin");

            OperationLog log = new OperationLog();
            log.setOperation_log_id(0);
            log.setUser_role(user_role.getConstant());
            log.setUser_id(manufactory.getManufactory_id());
            log.setUser_login_name(manufactory.getLogin_name());
            log.setOp_title(op_title.getConstant());
            log.setOp_detail("");
            log.setHappened_at(System.currentTimeMillis() / 1000);
            log.setOrigin_ip(req.getRemoteAddr());
            logService.inOperationLog(log);

            JsonObject json = new JsonObject();
            json.add("logo_url", gson.toJsonTree(manufactory.getLogo_url()));
            json.add("full_name", gson.toJsonTree(manufactory.getFull_name()));
            json.add("manufactory_id", gson.toJsonTree(manufactory.getManufactory_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.5.1.5	POST /manufactories/sign_out
    @RequestMapping(value = "/manufactories/sign_out", method = RequestMethod.POST)
    public void PostManuSignOut(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.getPrincipal() != null) {
                Manufactory fac = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
                ConstantDescription user_role = constantService.selConstantByExtra("user_role", "manufactory");
                ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signout");

                OperationLog log = new OperationLog();
                log.setOperation_log_id(0);
                log.setUser_role(user_role.getConstant());
                log.setUser_id(fac.getManufactory_id());
                log.setUser_login_name(fac.getLogin_name());
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

    /*3.5.1.6 GET /manufactories/settings/basic*/
    @RequestMapping(value = "/manufactories/settings/basic", method = RequestMethod.GET)
    public void GetManuSettingBasic(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Manufactory fac = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            Manufactory manu = manufactoryService.selManufactoryByLoginName(fac.getLogin_name());

            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manu.getManufactory_id());
            List<Integer> deviceList = manufactoryService.selectManDevice(map);
            manu.setDevice_type_ids(deviceList);

            ConstantDescription user_role = constantService.selConstantByExtra("user_role", "manufactory");
            ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signin");

            OperationLog log = logService.selLastSignInOp(user_role.getConstant(), manu.getManufactory_id(), op_title.getConstant(), 1);
            if (log == null) {
                log = logService.selLastSignInOp(user_role.getConstant(), manu.getManufactory_id(), op_title.getConstant(), 0);
            }

            JsonObject json = new JsonObject();
            json.add("contactor", gson.toJsonTree(manu.getContactor()));
            json.add("department", gson.toJsonTree(manu.getDepartment()));
            json.add("telephone", gson.toJsonTree(manu.getTelephone()));
            json.add("phone", gson.toJsonTree(manu.getPhone()));
            json.add("email", gson.toJsonTree(manu.getEmail()));
            json.add("full_name", gson.toJsonTree(manu.getFull_name()));
            json.add("code", gson.toJsonTree(manu.getCode()));
            json.add("profile", gson.toJsonTree(manu.getProfile()));
            json.add("province_id", gson.toJsonTree(manu.getProvince_id()));
            json.add("city_id", gson.toJsonTree(manu.getCity_id()));
            json.add("zone_id", gson.toJsonTree(manu.getZone_id()));
            json.add("address", gson.toJsonTree(manu.getAddress()));
            json.add("device_type_ids", gson.toJsonTree(manu.getDevice_type_ids()));
            json.add("members", gson.toJsonTree(manu.getMembers()));
            json.add("industry", gson.toJsonTree(manu.getIndustry()));
            json.add("nature", gson.toJsonTree(manu.getNature()));
            json.add("business_licence", gson.toJsonTree(manu.getBusiness_licence()));
            json.add("internal_certificate", gson.toJsonTree(manu.getInternal_certificate()));
            json.add("local_certificate", gson.toJsonTree(manu.getLocal_certificate()));
            json.add("code_certificate", gson.toJsonTree(manu.getCode_certificate()));
            json.add("logo_url", gson.toJsonTree(manu.getLogo_url()));
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

    //    3.5.1.7	PUT /manufactories/settings/basic
    @RequestMapping(value = "/manufactories/settings/basic", method = RequestMethod.PUT)
    public void PutMnuSettingBasic(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"contactor", "department", "telephone", "phone",
                    "email", "full_name", "code", "province_id",
                    "city_id", "zone_id", "address", "device_type_ids", "members",
                    "industry", "nature", "business_licence", "internal_certificate", "local_certificate",
                    "code_certificate"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Manufactory manufactory = gson.fromJson(params, Manufactory.class);
            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();

            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manu.getManufactory_id());
            map.put("contactor", manufactory.getContactor());
            map.put("department", manufactory.getDepartment());
            map.put("telephone", manufactory.getTelephone());
            map.put("phone", manufactory.getPhone());
            map.put("email", manufactory.getEmail());
            map.put("full_name", manufactory.getFull_name());
            map.put("code", manufactory.getCode());
            map.put("profile", manufactory.getProfile());

            map.put("province_id", manufactory.getProvince_id());
            map.put("city_id", manufactory.getCity_id());
            map.put("zone_id", manufactory.getZone_id());
            map.put("address", manufactory.getAddress());

            map.put("members", manufactory.getMembers());
            map.put("industry", manufactory.getIndustry());
            map.put("nature", manufactory.getNature());

            map.put("business_licence", manufactory.getBusiness_licence());
            map.put("internal_certificate", manufactory.getInternal_certificate());
            map.put("local_certificate", manufactory.getLocal_certificate());
            map.put("code_certificate", manufactory.getCode_certificate());
            map.put("updated_at", new Date().getTime() / 1000);
            manufactoryService.upManufactoryInf(map);
//          负责销售
            Map<String, Object> manuMap = new HashMap<>();
            manuMap.put("manufactory_id", manu.getManufactory_id());
            List<Integer> device_type_ids = manufactory.getDevice_type_ids();
            if (device_type_ids != null && device_type_ids.size() > 0) {
                manufactoryService.delManDevice(manuMap);
                for (Integer item : device_type_ids) {
                    manuMap.put("device_type_id", item);
                    manufactoryService.insertManDevice(manuMap);
                }
            }
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.5.1.8	PUT /manufactories/settings/avatar
    @RequestMapping(value = "/manufactories/settings/avatar", method = RequestMethod.PUT)
    public void PutManuSettingAvatar(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"avatar_url"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();

            Map<String, Object> map = new HashMap<>();
            map.put("logo_url", params.get("avatar_url").getAsString());
            map.put("manufactory_id", manu.getManufactory_id());
            manufactoryService.upManufactoryLogo(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.5.1.9	POST /manufactories/settings/password
    @RequestMapping(value = "/manufactories/settings/password", method = RequestMethod.POST)
    public void PostManSettingPwd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"old_password", "new_password"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            manu = manufactoryService.selManufactoryByLoginName(manu.getLogin_name());
            String pwd = params.get("old_password").getAsString();
            String md5 = passwordEncoder.encodePassword(pwd, md5_secret);
            if (!md5.equals(manu.getPassword())) {
                setParamWarnRes(res, "原密码错误", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String new_pwd = params.get("new_password").getAsString();
            String new_md5 = passwordEncoder.encodePassword(new_pwd, md5_secret);
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manu.getManufactory_id());
            map.put("password", new_md5);
            manufactoryService.upManuPwd(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.2.1 POST /manufactories/terminal_catagories*/
    @RequestMapping(value = "/manufactories/terminal_catagories", method = RequestMethod.POST)
    public void PostTerminalCategories(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manu.getManufactory_id());
            if (params.has("device_type_id")) {
                map.put("device_type_id", params.get("device_type_id").getAsInt());
            }
            if (params.has("name")) {
                map.put("name", params.get("name").getAsString());
            }
            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            long query_date = System.currentTimeMillis() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<TerminalCatagory> categories = manufactoryService.qryCategoryByPage(map);

            JsonArray category_list = new JsonArray();
            for (TerminalCatagory category : categories) {
                JsonObject item = gson.toJsonTree(category).getAsJsonObject();
                item.remove("manufactory_id");
                item.remove("product_type_id");
                item.add("terminal_catagory_name", item.get("name"));
                item.remove("name");
                category_list.add(item);
            }

            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("terminal_catagories", gson.toJsonTree(category_list));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.2.2 POST /manufactories/terminal_catagories/add*/
    @RequestMapping(value = "/manufactories/terminal_catagories/add", method = RequestMethod.POST)
    public void PostCategoriesAdd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"terminal_catagory_name", "code", "product_type_id", "device_type_id", "price",
                "picture", "profile", "status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            TerminalCatagory category = new TerminalCatagory();
            category.setTerminal_catagory_id(0);
            category.setName(params.get("terminal_catagory_name").getAsString());
            category.setCode(params.get("code").getAsString());
            category.setManufactory_id(manu.getManufactory_id());
            category.setProduct_type_id(params.get("product_type_id").getAsInt());
            category.setDevice_type_id(params.get("device_type_id").getAsInt());
            category.setPrice(params.get("price").getAsInt());
            category.setPicture(params.get("picture").getAsString());
            category.setProfile(params.get("profile").getAsString());
            category.setCreated_at(System.currentTimeMillis() / 1000);
            category.setStatus(params.get("status").getAsInt());

            manufactoryService.inTerminalCategory(category);

            JsonObject json = new JsonObject();
            json.add("terminal_catagory_id", gson.toJsonTree(category.getTerminal_catagory_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.2.3 PUT /manufactories/terminal_catagories/{tcid}*/
    @RequestMapping(value = "/manufactories/terminal_catagories/{tcid}", method = RequestMethod.PUT)
    public void PutCategory(@PathVariable("tcid") int cid,
                            HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"terminal_catagory_name", "code", "price", "picture", "profile"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();

            TerminalCatagory category = manufactoryService.selCategoryById(cid);
            if (category == null || category.getManufactory_id() != manu.getManufactory_id()){
                setRequestErrorRes(res, "需要修改的终端型号不存在或无修改权限", ErrorCode.NO_PERMISSION);
                return;
            }

            category.setName(params.get("terminal_catagory_name").getAsString());
            category.setCode(params.get("code").getAsString());
            category.setPrice(params.get("price").getAsInt());
            category.setPicture(params.get("picture").getAsString());
            category.setProfile(params.get("profile").getAsString());

            manufactoryService.upTerminalCategory(category);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.2.4 PUT /manufactories/terminal_catagories/{tcid}/status*/
    @RequestMapping(value = "/manufactories/terminal_catagories/{tcid}/status", method = RequestMethod.PUT)
    public void PutCategoryStatus(@PathVariable("tcid") int cid,
                            HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();

            TerminalCatagory category = manufactoryService.selCategoryById(cid);
            if (category == null || category.getManufactory_id() != manu.getManufactory_id()){
                setRequestErrorRes(res, "需要修改的终端型号不存在或无修改权限", ErrorCode.NO_PERMISSION);
                return;
            }

            category.setStatus(params.get("status").getAsInt());
            manufactoryService.upTerminalCategory(category);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.5.3.1	GET /manufactories/terminal_catagories
    @RequestMapping(value = "/manufactories/terminal_catagories", method = RequestMethod.GET)
    public void GetTerminals(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String device_type_id = req.getParameter("device_type_id");
            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            Map<String, Object> map = new HashMap<>();
            map.put("device_type_id", device_type_id);
            map.put("manufactory_id", manu.getManufactory_id());
//        map.put("manufactory_id",9);
            List<Map<String, Object>> Terminal_Catagory = manufactoryService.selTerminalCatagory(map);
            ArrayList array = new ArrayList();
            Map<String, Object> terminal = new HashMap<>();
            for (Map<String, Object> lists : Terminal_Catagory) {
                terminal.put("terminal_catagory_id", lists.get("terminal_catagory_id"));
                terminal.put("terminal_catagory_name", lists.get("terminal_catagory_name"));
                array.add(terminal);
            }
            content.setSuccess_message(array);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*//   3.5.3.2	POST /manufactories/terminal_catagory/templates
    @RequestMapping(value = "/manufactories/terminal_catagory/templates", method = RequestMethod.POST)
    public void AddTerminalsTem(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"terminal_catagory_id", "using_default",
                    "device_type_id", "product_type_id",};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int terminal_catagory_id = params.get("terminal_catagory_id").getAsInt();
            int device_type_id = params.get("device_type_id").getAsInt();
            int product_type_id = params.get("product_type_id").getAsInt();
            int using_default = params.get("using_default").getAsInt();
            String template = params.get("template").getAsString();

            Subject subject = SecurityUtils.getSubject();
            Manufactory manu = (Manufactory)subject.getPrincipals().getPrimaryPrincipal();
            Map<String, Object> map = new HashMap<>();
            map.put("terminal_catagory_id", terminal_catagory_id);
            map.put("device_type_id", device_type_id);
            map.put("product_type_id", product_type_id);
            map.put("template_type", using_default);
//            map.put("manufactory_id",9);
            map.put("manufactory_id", manu.getManufactory_id());
            map.put("uploaded_at", new Date().getTime() / 1000);
            map.put("status", BeanProperty.DeviceTypeStatus.NORMAL);
            if (using_default == Using_default.using_default) {
                manufactoryService.inTemple(map);
                return;
            }
            map.put("template", template);
            manufactoryService.inTemple(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }*/

    /*3.5.3.3 POST /manufactories/terminal_catagory/templates*/
    @RequestMapping(value = "/manufactories/terminal_catagory/templates", method = RequestMethod.POST)
    public void PostTermTemplates(HttpServletRequest req, HttpServletResponse res) {
        /*联调*/
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Subject subject = SecurityUtils.getSubject();
            Manufactory manufactory = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manufactory.getManufactory_id());
            if (params.has("device_type_id")) {
                map.put("device_type_id", params.get("device_type_id").getAsInt());
            }
            if (params.has("terminal_catagory_name")) {
                map.put("terminal_catagory_name", params.get("terminal_catagory_name").getAsString());
            }
            long query_date = new Date().getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);
            List<Map<String, Object>> templates_list = deviceService.selTemplatesByPage(map);
            for (Map<String, Object> template : templates_list) {
                if (template.get("template_type") == null) {
                    template.put("template_type", BeanProperty.Template_Type.SYSTEM);
                    template.put("template_url", "");
                }
            }

            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("terminals", gson.toJsonTree(templates_list));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.3.4 PUT /manufactories/terminal_catagory/templates*/
    @RequestMapping(value = "/manufactories/terminal_catagory/templates", method = RequestMethod.PUT)
    public void PutTermTemplates(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"device_type_id", "terminal_catagory_id", "using_default", "template"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int device_type_id = params.get("device_type_id").getAsInt();
            int terminal_catagory_id = params.get("terminal_catagory_id").getAsInt();
            boolean using_default = params.get("using_default").getAsBoolean();
            String template = null;
            if (!using_default) {
                template = params.get("template").getAsString();
            }
            TerminalCatagory termCat = deviceService.selTerminalCatagoryById(terminal_catagory_id);
            if (termCat == null) {
                setRequestErrorRes(res, "终端型号不存在", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            deviceService.delTemplateResourceByCatId(terminal_catagory_id);
            deviceService.delTemplateByCatId(terminal_catagory_id);
            if (!using_default) {
                TerminalCatagoryTemplate tem = new TerminalCatagoryTemplate();
                tem.setTerminal_catagory_template_id(0);
                tem.setTerminal_catagory_id(termCat.getTerminal_catagory_id());
                tem.setTemplate(template);
                tem.setTemplate_type(BeanProperty.Template_Type.FACTORY);
                tem.setManufactory_id(termCat.getManufactory_id());
                tem.setProduct_type_id(termCat.getProduct_type_id());
                tem.setDevice_type_id(termCat.getDevice_type_id());
                tem.setUploaded_at(System.currentTimeMillis() / 1000);
                tem.setStatus(BeanProperty.UserStatus.NORMAL);
                deviceService.inTemplate(tem);
            }

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.4.1 POST /manufactories/db/terminals*/
    @RequestMapping(value = "/manufactories/db/terminals", method = RequestMethod.POST)
    public void postTerminals(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Manufactory manufactory = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();

            Map<String, Object> map = new HashMap<>();
            List<Integer> factory_ids = new ArrayList<>();
            factory_ids.add(manufactory.getManufactory_id());
            map.put("manufactory_ids", factory_ids);

            if (params.has("device_type_ids")) {
                JsonArray device_type_ids = params.get("device_type_ids").getAsJsonArray();
                if (device_type_ids != null && device_type_ids.size() > 0) {
                    List<Integer> ids = new ArrayList<>();
                    for (JsonElement item : device_type_ids) {
                        ids.add(item.getAsInt());
                    }
                    map.put("device_type_ids", ids);
                }
            }

            if (params.has("status")) {
                map.put("status", params.get("status").getAsInt());
            }

            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            long query_date = new Date().getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            List<Map<String, Object>> terminalList = deviceService.selTerminalsByPage(map, pageInf);

            JsonArray list = new JsonArray();
            for (Map<String, Object> terminal : terminalList) {
                JsonObject item = new JsonObject();
                item.add("terminal_id", gson.toJsonTree(terminal.get("terminal_id")));
                item.add("terminal_name", gson.toJsonTree(terminal.get("terminal_name")));
                item.add("terminal_catagory_id", gson.toJsonTree(terminal.get("terminal_catagory_id")));
                item.add("terminal_catagory_name", gson.toJsonTree(terminal.get("catagory_name")));
                item.add("device_type_id", gson.toJsonTree(terminal.get("device_type_id")));
                item.add("activated_at", gson.toJsonTree(terminal.get("activated_at")));
                item.add("status", gson.toJsonTree(terminal.get("status")));
                list.add(item);
            }

            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("terminals", list);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.4.2 POST /manufactories/db/terminals/fuzzy_search*/
    @RequestMapping(value = "/manufactories/db/terminals/fuzzy_search", method = RequestMethod.POST)
    public void PostTerminalsFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Manufactory manufactory = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();

            Map<String, Object> map = new HashMap<>();
            List<Integer> factory_ids = new ArrayList<>();
            factory_ids.add(manufactory.getManufactory_id());
            map.put("manufactory_ids", factory_ids);

            if (params.has("name")) {
                map.put("full_name", params.get("name").getAsString());
            }

            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            long query_date = new Date().getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            List<Map<String, Object>> terminalList = deviceService.selTerminalsByPage(map, pageInf);

            JsonArray list = new JsonArray();
            for (Map<String, Object> terminal : terminalList) {
                JsonObject item = new JsonObject();
                item.add("terminal_id", gson.toJsonTree(terminal.get("terminal_id")));
                item.add("terminal_name", gson.toJsonTree(terminal.get("terminal_name")));
                item.add("terminal_catagory_id", gson.toJsonTree(terminal.get("terminal_catagory_id")));
                item.add("terminal_catagory_name", gson.toJsonTree(terminal.get("catagory_name")));
                item.add("device_type_id", gson.toJsonTree(terminal.get("device_type_id")));
                item.add("activated_at", gson.toJsonTree(terminal.get("activated_at")));
                item.add("status", gson.toJsonTree(terminal.get("status")));
                list.add(item);
            }

            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("terminals", gson.toJsonTree(list));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.5.4.3	GET /manufactories/db/terminals/{tid}
    @RequestMapping(value = "/manufactories/db/terminals/{tid}", method = RequestMethod.GET)
    public void getTerminalDetail(@PathVariable("tid") int id,
                                  HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Map<String, Object> detail = new HashMap<>();
            detail.put("terminal_id", id);
            Map<String, Object> terminalDetail = deviceService.selTerminalDetail(detail);
            List<Map<String, Object>> userList = deviceService.selUsersByTerminalId(id);
            ArrayList lists = new ArrayList();
            if (userList != null || userList.size() != 0) {
                for (Map<String, Object> user : userList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("user_phone", user.get("phone"));
                    map.put("user_name", user.get("full_name"));
                    map.put("used_count", user.get("count"));
                    map.put("last_used_at", user.get("updated_at"));
                    lists.add(map);
                }
            }
            JsonObject result = new JsonObject();
            if (terminalDetail != null || terminalDetail.size() != 0) {
                result.add("product_type_id", gson.toJsonTree(terminalDetail.get("product_type_id")));
                result.add("device_type_id", gson.toJsonTree(terminalDetail.get("device_type_id")));
                result.add("terminal_catagory_id", gson.toJsonTree(terminalDetail.get("terminal_catagory_id")));
                result.add("terminal_name", gson.toJsonTree(terminalDetail.get("terminal_name")));
                result.add("manufactory_name", gson.toJsonTree(terminalDetail.get("manufactory_name")));
                result.add("activated_at", gson.toJsonTree(terminalDetail.get("activated_at")));
            }
            result.add("used_by", gson.toJsonTree(lists));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.5.4.4	PUT /manufactories/db/terminals/{tid}/state
    @RequestMapping(value = "/manufactories/db/terminals/{tid}/state", method = RequestMethod.PUT)
    public void editTerminalState(@PathVariable("tid") int id,
                                  HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("status", params.get("status").getAsInt());
            map.put("terminal_id", id);
            manufactoryService.upTerminalStatus(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.5.1 POST /manufactories/db/health_datas*/
    @RequestMapping(value = "/manufactories/db/health_datas", method = RequestMethod.POST)
    public void PostDbHealthData(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"data_type_id", "begin_age", "end_age", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Manufactory fac = (Manufactory) subject.getPrincipals().getPrimaryPrincipal();
            Manufactory manu = manufactoryService.selManufactoryByLoginName(fac.getLogin_name());

            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", manu.getManufactory_id());

            if (params.has("begin_age")) {
                map.put("begin_age", params.get("begin_age").getAsInt());
            }

            if (params.has("end_age")) {
                map.put("end_age", params.get("end_age").getAsInt());
            }

            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = new Date().getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            if (params.has("terminal_catagory_name")) {
                map.put("terminal_catagory_name", params.get("terminal_catagory_name").getAsString());
            }
            if (params.has("terminal_name")) {
                map.put("terminal_name", params.get("terminal_name").getAsString());
            }
            if (params.has("user_phone")) {
                map.put("user_phone", params.get("user_phone").getAsString());
            }

            int data_type_id = params.get("data_type_id").getAsInt();
            map.put("data_type_id", data_type_id);
            ConstantDescription constant = constantService.selDescriptionByConstant("data_type", data_type_id);
            List<Map<String, Object>> map_list = null;
            switch (constant.getDescription()) {
                case "wristband":
                    map_list = userHealthService.selWristbandData4FactoryByPage(map);
                    break;
                case "sphygmomanometer":
                    map_list = userHealthService.selSphyData4FactoryByPage(map);
                    break;
                case "oximeter":
                    map_list = userHealthService.selOximeterData4FactoryByPage(map);
                    break;
                case "glucosemeter":
                    map_list = userHealthService.selGlucosemeterData4FactoryByPage(map);
                    break;
                case "thermometer":
                    map_list = userHealthService.selThermometerData4FactoryByPage(map);
                    break;
                case "fat":
                    map_list = userHealthService.selFatData4FactoryByPage(map);
                    break;
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("health_data_list", gson.toJsonTree(map_list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

}

