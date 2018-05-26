package com.jxyq.app.setting;

import com.google.gson.*;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.JsonUtil;
import com.jxyq.model.Role;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.model.user.UserAssociated;
import com.jxyq.model.user.VerificationCode;
import com.jxyq.model.watch.AppSms;
import com.jxyq.service.inf.UserService;
import com.jxyq.service.inf.watch.WatchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
public class SettingInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    @Autowired
    private WatchUserService watchUserService;

    private static String api_url = SysConfig.getInstance().getProperty("hospital.apiurl");
    private static String api_key = SysConfig.getInstance().getProperty("hospital.apikey");

    /*3.10.1 POST /api/user/associated_users*/
    @RequestMapping(value = "/api/user/associated_users", method = RequestMethod.POST)
    public void PostAssociatedUser(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"associated_user_phone", "verification_code",
                    "associated_user_name", "associated_user_avatar"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String phone = params.get("associated_user_phone").getAsString();
            String code_http = params.get("verification_code").getAsString();
            int check_code = userService.checkVerificationCode(phone, code_http, VerificationCode.USAGE_ADD_ASSOCIATION);
            if (check_code == 1) {
                setParamWarnRes(res, "请先获取手机验证码", ErrorCode.NONE_CODE);
                return;
            } else if (check_code == 2) {
                setParamWarnRes(res, "验证码已过期，请重新获取！", ErrorCode.INVALID_CODE);
                return;
            } else if (check_code == 3) {
                setParamWarnRes(res, "验证码错误", ErrorCode.ERROR_CODE);
                return;
            }

            User associated_user = userService.selUserByPhone(phone);
            if (associated_user == null) {
                setParamWarnRes(res, "被关联手机未注册", ErrorCode.NO_REGISTERED);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user.getUser_id());
            map.put("associated_user_id", associated_user.getUser_id());
            UserAssociated check = userService.selUserAssociated(map);
            if (check != null) {
                setParamWarnRes(res, "该用户已经被关联,不能重复关联", ErrorCode.DUPLICATE_DONE);
                return;
            }

            UserAssociated associated = new UserAssociated();
            associated.setUser_associated_id(0);
            associated.setUser_id(user.getUser_id());
            associated.setAssociated_user_id(associated_user.getUser_id());
            associated.setAssociated_user_name(params.get("associated_user_name").getAsString());
            associated.setAssociated_user_avatar(params.get("associated_user_avatar").getAsString());
            associated.setLevel(0);
            userService.inUserAssociated(associated);

            JsonObject json = new JsonObject();
            json.add("associated_phone", gson.toJsonTree(associated_user.getPhone()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
            return;
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.10.2 PUT /api/user/associated_users*/
    @RequestMapping(value = "/api/user/associated_users", method = RequestMethod.PUT)
    public void PutAssociatedUser(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"associated_user_phone"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String ass_phone = params.get("associated_user_phone").getAsString();
            User associated_user = userService.selUserByPhone(ass_phone);
            if (associated_user == null) {
                setParamWarnRes(res, "该手机未注册", ErrorCode.NO_REGISTERED);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user.getUser_id());
            map.put("associated_user_id", associated_user.getUser_id());
            UserAssociated associated = userService.selUserAssociated(map);
            if (associated == null) {
                setParamWarnRes(res, "未关联该用户,无权限", ErrorCode.NO_PERMISSION);
                return;
            }
            if (params.has("associated_user_name")){
                associated.setAssociated_user_name(params.get("associated_user_name").getAsString());
            }
            if (params.has("associated_user_avatar")){
                associated.setAssociated_user_avatar(params.get("associated_user_avatar").getAsString());
            }
            userService.upUserAssociated(associated);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
            return;
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.10.2 DELETE /api/user/associated_users*/
    @RequestMapping(value = "/api/user/associated_users", method = RequestMethod.DELETE)
    public void DelAssociatedUser(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String ass_phone = req.getParameter("associated_phone");
            if (StringUtils.isBlank(ass_phone)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            User associated_user = userService.selUserByPhone(ass_phone);
            if (associated_user == null) {
                setParamWarnRes(res, "该手机未注册", ErrorCode.NO_REGISTERED);
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user.getUser_id());
            map.put("associated_user_id", associated_user.getUser_id());
            UserAssociated associated = userService.selUserAssociated(map);
            if (associated == null) {
                setParamWarnRes(res, "未关联该用户,无权限", ErrorCode.NO_PERMISSION);
                return;
            }
            userService.delUserAssociated(associated.getUser_associated_id());
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.10.3 GET /api/user/associated_users*/
    @RequestMapping(value = "/api/user/associated_users", method = RequestMethod.GET)
    public void GetAssociatedUser(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            List<Map<String, Object>> list = userService.qryUserAssociated(user.getUser_id());
            content.setSuccess_message(list);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    private List<Map<String, Object>> GetHospitalsByBaiduAPI(double longitude, double latitude, int limit) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer buffer = new StringBuffer();

        String api_arg = "x=" + longitude + "&y=" + latitude + "&page=1&limit=" + limit;
        String host_url = api_url + "?" + api_arg;
        try {
            URL url = new URL(host_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", api_key);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        result = buffer.toString();
        JsonParser parser = new JsonParser();
        JsonObject api_json = parser.parse(result).getAsJsonObject();
        JsonArray hospital_json = api_json.get("yi18").getAsJsonArray();

        List<Map<String, Object>> map_list = new ArrayList<Map<String, Object>>();
        map_list = (List<Map<String, Object>>) JsonUtil.JsonSting2DecodeObject(hospital_json.toString(), map_list.getClass());
        return map_list;
    }

    /*3.10.4 GET /api/hospitals*/
    @RequestMapping(value = "/api/hospitals", method = RequestMethod.GET)
    public void getRecoHospitalsInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String latitude_str = req.getParameter("latitude");
            String longitude_str = req.getParameter("longitude");
            String radius_str = req.getParameter("radius");
            if (StringUtils.isBlank(latitude_str) || StringUtils.isBlank(longitude_str)
                    || StringUtils.isBlank(radius_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            double latitude = Double.valueOf(latitude_str);
            double longitude = Double.valueOf(longitude_str);
            double radius = Double.valueOf(radius_str);
            String limit_str = req.getParameter("limit");
            int limit = -1;
            if (!StringUtils.isBlank(limit_str)) {
                limit = Integer.valueOf(limit_str);
            }
            List<Map<String, Object>> list = userService.getRecoHospitals(latitude, longitude, radius, limit);

            if (limit == -1 || list.size() >= limit) {
                content.setSuccess_message(list);
                setResponseContent(res, content);
                return;
            }

            List<Map<String, Object>> hospitals = GetHospitalsByBaiduAPI(longitude, latitude, limit);
            if (hospitals == null || hospitals.size() <= 0) {
                content.setSuccess_message(list);
                setResponseContent(res, content);
                return;
            }

            for (Map<String, Object> hospital : hospitals) {
                boolean has = false;
                for (Map<String, Object> item : list) {
                    if (item.get("name").equals(hospital.get("name"))) {
                        has = true;
                        break;
                    }
                }
                if (has) continue;
                Map<String, Object> map = new HashMap<>();
                map.put("name", hospital.get("name"));
                map.put("address", hospital.get("address"));
                map.put("phone", hospital.get("tel"));
                map.put("longitude", hospital.get("x"));
                map.put("latitude", hospital.get("y"));
                map.put("postcode", hospital.get("zipcode"));
                map.put("profile", "");
                map.put("homepage", hospital.get("url"));
                map.put("status", 0);
                list.add(map);
                if (list.size() >= limit) break;
            }
            content.setSuccess_message(list);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.10.5 GET /api/app_updates*/
    @RequestMapping(value = "/api/app_updates", method = RequestMethod.GET)
    public void getAppUpdateInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String type = req.getParameter("type");
            if (StringUtils.isBlank(type)) {
                setSystemErrorRes(res, "缺少必要请求参数.", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = userService.getAppUpdate(Integer.valueOf(type));
            JsonObject json = new JsonObject();
            if (map != null) {
                json.add("version_code", gson.toJsonTree((String) map.get("version_code")));
                json.add("description", gson.toJsonTree((String) map.get("description")));
                json.add("url", gson.toJsonTree((String) map.get("url")));
            }
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.10.9 GET /api/appsms*/
    @RequestMapping(value = "/api/appsms", method = RequestMethod.GET)
    public void getApiAppSms(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");

            boolean hasRight = false;
            user = userService.selUserWithRolesByPhone(user.getPhone());
            for (Role role : user.getRoleList()) {
                if (role.getPermissionList().contains("appsms")) {
                    hasRight = true;
                    break;
                }
            }
            if (!hasRight) {
                setParamWarnRes(res, "权限不足", ErrorCode.NO_PERMISSION);
                return;
            }

            List<AppSms> smses = watchUserService.selAppSmsList(WatchProperty.ReadStatus.UNREAD);
            JsonArray sms_list = new JsonArray();
            for (AppSms sms : smses) {
                JsonObject json = new JsonObject();
                json.add("number", gson.toJsonTree(sms.getNumber()));
                json.add("msg", gson.toJsonTree(sms.getMsg()));
                sms_list.add(json);
                watchUserService.upAppSmsReaded(sms.getId(), WatchProperty.ReadStatus.READ);
            }

            JsonObject json = new JsonObject();
            json.add("sms_list", sms_list);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.10.10 PUT /api/number*/
    @RequestMapping(value = "/api/number", method = RequestMethod.PUT)
    public void pubApiNumber(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");

            boolean hasRight = false;
            user = userService.selUserWithRolesByPhone(user.getPhone());
            for (Role role : user.getRoleList()) {
                if (role.getPermissionList().contains("number")) {
                    hasRight = true;
                    break;
                }
            }
            if (!hasRight) {
                setParamWarnRes(res, "权限不足", ErrorCode.NO_PERMISSION);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("numbers")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            JsonArray numbers = params.getAsJsonArray("numbers");
            for (JsonElement number : numbers) {
                JsonObject number_json = number.getAsJsonObject();
                String imei = number_json.get("imei").getAsString();
                String phone = number_json.get("number").getAsString();
                watchUserService.updateUserNumber(imei, phone);
            }

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }
}
