package com.jxyq.controller.admin.db_operate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.app.user_login.LoginInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.AccountService;
import com.jxyq.service.inf.ManufactoryService;
import com.jxyq.service.inf.UserHealthService;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wujj.fnst on 2015/4/12.
 */
@Controller
public class UserController extends BaseInterface {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserHealthService userHealthService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;
    @Autowired
    private ManufactoryService manufactoryService;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

    /*3.3.3.3.1 POST /admins/db/users*/
    @RequestMapping(value = "/admins/db/users", method = RequestMethod.POST)
    public void PostDbUsers(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"registered_from", "registered_to", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            long registered_from = params.get("registered_from").getAsLong();
            long registered_to = params.get("registered_to").getAsLong();

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            Map<String, Object> map = new HashMap<>();
            if (params.has("device_type_ids")) {
                JsonArray device_type_ids = params.get("device_type_ids").getAsJsonArray();
                if (device_type_ids != null && device_type_ids.size() > 0) {
                    List<Integer> ids = new ArrayList<>();
                    for (JsonElement item : device_type_ids) {
                        int device_type_id = item.getAsInt();
                        ids.add(device_type_id);
                    }
                    map.put("device_type_ids", ids);
                }
            }

            if (params.has("phone")) {
                String phone = params.get("phone").getAsString();
                map.put("phone", phone);
            }

            if (params.has("begin_age")) {
                int begin_age = params.get("begin_age").getAsInt();
                if (begin_age != 0) {
                    map.put("minAge", begin_age);
                }
            }
            if (params.has("end_age")) {
                int end_age = params.get("end_age").getAsInt();
                if (end_age != 0) {
                    map.put("maxAge", end_age);
                }
            }

            Map<String, Object> total_map = accountService.selTotalUserCnt();
            Map<String, Object> online_map = accountService.selOnlineUserCnt();
            long total_cnt = 0;
            long online_cnt = 0;
            if (total_map != null) {
                total_cnt = (long) total_map.get("count");
            }
            if (online_map != null) {
                online_cnt = (long) online_map.get("count");
            }

            map.put("registered_from", registered_from);
            map.put("registered_to", registered_to);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            List<Map<String, Object>> users = accountService.selUserByPage(map, pageInf);
            JsonArray lists = new JsonArray();
            for (Map<String, Object> user : users) {
                JsonObject item = new JsonObject();
                item.add("user_id", gson.toJsonTree(user.get("user_id")));
                item.add("nick_name", gson.toJsonTree(user.get("nick_name")));
                item.add("full_name", gson.toJsonTree(user.get("full_name")));
                item.add("phone", gson.toJsonTree(user.get("phone")));
                List<Integer> device_type_ids = manufactoryService.selUserDeviceId((int) user.get("user_id"));
                item.add("device_type_ids", gson.toJsonTree(device_type_ids));
                item.add("registered_at", gson.toJsonTree(user.get("registered_at")));
                item.add("status", gson.toJsonTree(user.get("status")));
                lists.add(item);
            }
            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("online_count", gson.toJsonTree(online_cnt));
            json.add("member_count", gson.toJsonTree(total_cnt));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("users", gson.toJsonTree(lists));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.3.3.2	POST /admins/db/users/fuzzy_search*/
    @RequestMapping(value = "/admins/db/users/fuzzy_search", method = RequestMethod.POST)
    public void PostUserFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            Map<String, Object> map = new HashMap<>();
            if (params.has("full_name")) {
                String full_name = params.get("full_name").getAsString();
                if (!StringUtils.isBlank(full_name)) {
                    map.put("name", full_name);
                }
            }

            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            Map<String, Object> total_map = accountService.selTotalUserCnt();
            Map<String, Object> online_map = accountService.selOnlineUserCnt();
            long total_cnt = 0;
            long online_cnt = 0;
            if (total_map != null) {
                total_cnt = (long) total_map.get("count");
            }
            if (online_map != null) {
                online_cnt = (long) online_map.get("count");
            }

            List<Map<String, Object>> users = accountService.selUserByPage(map, pageInf);
            JsonArray lists = new JsonArray();
            for (Map<String, Object> user : users) {
                JsonObject item = new JsonObject();
                item.add("user_id", gson.toJsonTree(user.get("user_id")));
                item.add("nick_name", gson.toJsonTree(user.get("nick_name")));
                item.add("full_name", gson.toJsonTree(user.get("full_name")));
                item.add("phone", gson.toJsonTree(user.get("phone")));
                List<Integer> device_type_ids = manufactoryService.selUserDeviceId((int) user.get("user_id"));
                item.add("device_type_ids", gson.toJsonTree(device_type_ids));
                item.add("registered_at", gson.toJsonTree(user.get("registered_at")));
                item.add("status", gson.toJsonTree(user.get("status")));
                lists.add(item);
            }
            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("online_count", gson.toJsonTree(online_cnt));
            json.add("member_count", gson.toJsonTree(total_cnt));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("users", gson.toJsonTree(lists));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //  3.2.2.3.3	GET /admins/db/users/{uid}
    @RequestMapping(value = "/admins/db/users/{uid}", method = RequestMethod.GET)
    public void users(@PathVariable("uid") int id,
                      HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = accountService.selUserById(id);
            user.setPassword(null);
            content.setSuccess_message(user);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.3.4	PUT /admins/db/users/{uid}/state
    @RequestMapping(value = "/admins/db/users/{uid}/state", method = RequestMethod.PUT)
    public void state(@PathVariable("uid") int id,
                      HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int status = params.get("status").getAsInt();
            accountService.upUserStatus(id, status);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.3.3.5 PUT /admins/db/users/{uid}*/
    @RequestMapping(value = "/admins/db/users/{uid}", method = RequestMethod.PUT)
    public void edit(@PathVariable("uid") int id,
                     HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", id);

            if (params.has("password")) {
                String password = params.get("password").getAsString();
                if (!StringUtils.isBlank(password)) {
                    String md5_password = passwordEncoder.encodePassword(password, LoginInterface.md5_secret);
                    map.put("password", md5_password);
                }
            }

            if (params.has("email")) {
                map.put("email", params.get("email").getAsString());
            }
            if (params.has("nick_name")) {
                map.put("nick_name", params.get("nick_name").getAsString());
            }
            if (params.has("full_name")) {
                map.put("full_name", params.get("full_name").getAsString());
            }
            if (params.has("avatar_url")) {
                map.put("avatar_url", params.get("avatar_url").getAsString());
            }
            if (params.has("gender")) {
                map.put("gender", params.get("gender").getAsInt());
            }
            if (params.has("birthday")) {
                map.put("birthday", params.get("birthday").getAsString());
            }
            if (params.has("height")) {
                map.put("height", params.get("height").getAsInt());
            }
            if (params.has("weight")) {
                map.put("weight", params.get("weight").getAsInt());
            }
            if (params.has("target_weight")) {
                map.put("target_weight", params.get("target_weight").getAsInt());
            }
            if (params.has("address")) {
                map.put("address", params.get("address").getAsString());
            }
            if (params.has("qq")) {
                map.put("qq", params.get("qq").getAsString());
            }
            if (params.has("operation_type")) {
                int mark = 1;
                int operation_type = params.get("operation_type").getAsInt();
                if (operation_type == BeanProperty.MarkType.ADD) {
                    mark = 1;
                } else {
                    mark = -1;
                }
                int delta_points = mark * params.get("delta_points").getAsInt();
                map.put("delta_points", delta_points);
            }
            map.put("updated_at", System.currentTimeMillis() / 1000);
            accountService.upUserInf(map);
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