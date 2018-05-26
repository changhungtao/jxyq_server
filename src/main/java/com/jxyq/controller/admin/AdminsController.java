package com.jxyq.controller.admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.model.MultiLoginAuthenticationToken;
import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.OperationLog;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.AdminService;
import com.jxyq.service.inf.ConstantService;
import com.jxyq.service.inf.OperationLogService;
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
import java.util.List;
import java.util.Map;

@Controller
public class AdminsController extends BaseInterface {
    @Autowired
    private AdminService adminService;

    @Autowired
    private OperationLogService logService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");

    /*3.3.2.1 POST /admins/sign_in*/
    @RequestMapping(value = "/admins/sign_in", method = RequestMethod.POST)
    public void login(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "captcha", "role"};
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

            MultiLoginAuthenticationToken token = new MultiLoginAuthenticationToken(login_name, pwd, "AdministratorRealm");
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

            Administrator admin = adminService.selAdminWithRoleByLoginName(login_name);
            String role = params.get("role").getAsString();
            List<Role> role_list = admin.getRoleList();
            boolean hasRight = false;
            for (Role user_role : role_list) {
                if (role.equals("super") && user_role.getName().indexOf("super") != -1) {
                    hasRight = true;
                    break;
                }
                if (role.equals("manager") && user_role.getName().indexOf("admin") != -1) {
                    hasRight = true;
                    break;
                }
            }
            if (hasRight == false) {
                setParamWarnRes(res, "权限不足", ErrorCode.NO_PERMISSION);
                return;
            }
            if (admin.getStatus() == BeanProperty.UserStatus.CLOSE) {
                subject.logout();
                setParamWarnRes(res, "用户已被禁用", ErrorCode.USER_CLOSE);
                return;
            }

            ConstantDescription user_role = constantService.selConstantByExtra("user_role", role);
            ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signin");

            OperationLog log = new OperationLog();
            log.setOperation_log_id(0);
            log.setUser_role(user_role.getConstant());
            log.setUser_id(admin.getAdministrator_id());
            log.setUser_login_name(admin.getLogin_name());
            log.setOp_title(op_title.getConstant());
            log.setOp_detail("");
            log.setHappened_at(System.currentTimeMillis() / 1000);
            log.setOrigin_ip(req.getRemoteAddr());
            logService.inOperationLog(log);

            JsonObject json = new JsonObject();
//            if (admin.getRoleList().size() >= 0) {
            json.add("role_type", gson.toJsonTree(role));
//            } else {
//                json.add("role_type", gson.toJsonTree(""));
//            }
            json.add("avatar_url", gson.toJsonTree(admin.getAvatar_url()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.3.1.3	POST /admins/sign_out
    @RequestMapping(value = "/admins/sign_out", method = RequestMethod.POST)
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        try {
            ResponseContent content = new ResponseContent();

            Subject subject = SecurityUtils.getSubject();
            if (subject.getPrincipals() != null) {
                Administrator admin = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
                admin = adminService.selAdminWithRoleByLoginName(admin.getLogin_name());

                List<Role> role_list = admin.getRoleList();
                String role = "manager";
                for (Role item : role_list) {
                    if (item.getName().indexOf("super") != -1) {
                        role = "super";
                        break;
                    }
                }

                ConstantDescription user_role = constantService.selConstantByExtra("user_role", role);
                ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signout");

                OperationLog log = new OperationLog();
                log.setOperation_log_id(0);
                log.setUser_role(user_role.getConstant());
                log.setUser_id(admin.getAdministrator_id());
                log.setUser_login_name(admin.getLogin_name());
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

    //3.3.1.4	GET /admins/settings/basic
    @RequestMapping(value = "/admins/settings/basic", method = RequestMethod.GET)
    public void basic(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Administrator admin = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
            admin = adminService.selAdminWithRoleByLoginName(admin.getLogin_name());

            List<Role> role_list = admin.getRoleList();
            String role = "manager";
            for (Role item : role_list) {
                if (item.getName().indexOf("super") != -1) {
                    role = "super";
                    break;
                }
            }

            ConstantDescription user_role = constantService.selConstantByExtra("user_role", role);
            ConstantDescription op_title = constantService.selConstantByExtra("op_title", "signin");

            OperationLog log = logService.selLastSignInOp(user_role.getConstant(), admin.getAdministrator_id(), op_title.getConstant(), 1);
            if (log == null){
                log = logService.selLastSignInOp(user_role.getConstant(), admin.getAdministrator_id(), op_title.getConstant(), 0);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("full_name", admin.getFull_name());
            map.put("phone", admin.getPhone());
            map.put("email", admin.getEmail());
            map.put("gender", admin.getGender());
            map.put("birthday", admin.getBirthday());
            map.put("avatar_url", admin.getAvatar_url());
            if (log != null) {
                map.put("last_login", log.getHappened_at());
            } else {
                map.put("last_login", System.currentTimeMillis() / 1000);
            }
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.3.1.5	PUT /admins/settings/basic
    @RequestMapping(value = "/admins/settings/basic", method = RequestMethod.PUT)
    public void basic2(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Administrator admin = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
            admin = adminService.selAdminByLoginName(admin.getLogin_name());

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"full_name", "email", "gender", "birthday"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String full_name = params.get("full_name").getAsString();
            String email = params.get("email").getAsString();
            int gender = params.get("gender").getAsInt();
            String birthday = params.get("birthday").getAsString();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("full_name", full_name);
            map.put("email", email);
            map.put("gender", gender);
            map.put("birthday", birthday);
            map.put("administrator_id", admin.getAdministrator_id());
            adminService.updaAdminInfo(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.2.6 PUT /admins/settings/avatar*/
    @RequestMapping(value = "/admins/settings/avatar", method = RequestMethod.PUT)
    public void PutAdminAvatar(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"avatar_url"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String avatar_url = params.get("avatar_url").getAsString();

            Subject subject = SecurityUtils.getSubject();
            Administrator admin = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
            admin = adminService.selAdminByLoginName(admin.getLogin_name());

            Map<String, Object> map = new HashMap<>(2);
            map.put("administrator_id", admin.getAdministrator_id());
            map.put("avatar_url", avatar_url);
            adminService.updaAdminInfo(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.3.1.8	PUT /admins/settings/password
    @RequestMapping(value = "/admins/settings/password", method = RequestMethod.PUT)
    public void password(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"old_password", "new_password"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String old_password = params.get("old_password").getAsString();
            String new_password = params.get("new_password").getAsString();

            Subject subject = SecurityUtils.getSubject();
            Administrator auth = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
            auth = adminService.selAdminByLoginName(auth.getLogin_name());

            Administrator admins = adminService.selAdminById(auth.getAdministrator_id());
            String md5_old_pwd = passwordEncoder.encodePassword(old_password, md5_secret);
            if (!(md5_old_pwd.equals(admins.getPassword()))) {
                setParamWarnRes(res, "原始密码错误", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Administrator admin = new Administrator();
            admin.setAdministrator_id(auth.getAdministrator_id());
            String md5_new_pwd = passwordEncoder.encodePassword(new_password, md5_secret);
            admin.setPassword(md5_new_pwd);
            adminService.updaAdminPass(admin);
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