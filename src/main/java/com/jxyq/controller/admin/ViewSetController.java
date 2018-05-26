package com.jxyq.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.BeanProperty.Using_default;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.push.AndroidPushMessage;
import com.jxyq.commons.push.AndroidPushMessageForPhone;
import com.jxyq.commons.push.IosPushMessageForPhone;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.admin.AdministratorDistrict;
import com.jxyq.model.others.PushService;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.AdminService;
import com.jxyq.service.inf.RoleService;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wujj-fnst on 2015/5/27.
 */
@Controller
public class ViewSetController extends BaseInterface {
    @Autowired
    private AdminService adminService;
    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");
    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //    页面设置
    @RequestMapping(value = "/admins/db/web_templates", method = RequestMethod.GET)
    public void GetDbWebTemplates(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Map<String, Object> map = new HashMap();
            map.put("template_type", Using_default.using_default);
            Map<String, Object> template = adminService.selWeb_templates(map);
            map.clear();
            if (template != null) {
                map.put("template", template.get("template"));
            } else {
                map.put("template", "");
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

    @RequestMapping(value = "/admins/db/web_templates", method = RequestMethod.PUT)
    public void PutDbWebTemplates(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"template"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Map<String, Object> map = new HashMap();
            map.put("template", params.get("template").getAsString());
            map.put("template_type", Using_default.using_default);
            map.put("uploaded_at", new Date().getTime() / 1000);
            map.put("status", BeanProperty.UserStatus.NORMAL);
            adminService.upWeb_templates(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.8.3 GET /admins/db/exhibition_resources*/
    @RequestMapping(value = "/admins/db/exhibition_resources", method = RequestMethod.GET)
    public void GetDbExhibitionResources(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");
            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap();
            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str) && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            List<Map<String, Object>> lists = adminService.qryExhibitionByPage(map);

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("resources", gson.toJsonTree(lists));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/admins/db/exhibition_resources", method = RequestMethod.POST)
    public void exhibition_resources2(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"pic_url"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String pic_url = params.get("pic_url").getAsString();
            /*Map<String, Object> exhibition = new HashMap();
            exhibition.put("uploaded_at", new Date().getTime() / 1000);
            exhibition.put("manager_id", admin.getAdministrator_id());
            adminService.addExhibition(exhibition);*/
            Map<String, Object> resource = new HashMap();
            resource.put("exhibition_id", 1);
            resource.put("pic_url", pic_url);
            adminService.addExhibitionRes(resource);
            Map<String, Object> result = new HashMap();
            result.put("exhibition_resource_id", resource.get("exhibition_resource_id"));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/admins/db/exhibition_resources", method = RequestMethod.PUT)
    public void exhibition_resources3(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"pic_url", "exhibition_resource_id"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int exhibition_resource_id = params.get("exhibition_resource_id").getAsInt();
            String pic_url = params.get("pic_url").getAsString();
            Map<String, Object> map = new HashMap();
            map.put("pic_url", pic_url);
            map.put("exhibition_resource_id", exhibition_resource_id);
            adminService.upExhibitionRes(map);
            /*Map<String, Object> exhibition = new HashMap();
            exhibition.put("exhibition_resource_id", exhibition_resource_id);
            exhibition.put("uploaded_at", new Date().getTime() / 1000);
            map.put("manager_id", admin.getAdministrator_id());
            adminService.upExhibition(exhibition);*/
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/admins/db/exhibition_resources/{eid}", method = RequestMethod.DELETE)
    public void exhibition_resources4(@PathVariable("eid") int id,
                                      HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            adminService.delExhibition(id);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.8.7 GET /admins/db/news_resources*/
    @RequestMapping(value = "/admins/db/news_resources", method = RequestMethod.GET)
    public void GetDbNewsResources(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");
            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap();
            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str) && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            List<Map<String, Object>> lists = adminService.qryNewsByPage(map);

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("news_resources", gson.toJsonTree(lists));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/admins/db/news_resources", method = RequestMethod.POST)
    public void news_resources2(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"pic_url", "news_link"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String pic_url = params.get("pic_url").getAsString();
            String news_link = params.get("news_link").getAsString();
            /*Administrator admin = (Administrator) req.getSession().getAttribute("user");
            Map<String, Object> news = new HashMap();
            news.put("uploaded_at", new Date().getTime() / 1000);
            news.put("manager_id", admin.getAdministrator_id());
            adminService.addNews(news);*/
            Map<String, Object> resource = new HashMap();
            resource.put("news_id", 1);
            resource.put("pic_url", pic_url);
            resource.put("news_link", news_link);
            adminService.addNewsRes(resource);
            Map<String, Object> result = new HashMap();
            result.put("news_resource_id", resource.get("news_resource_id"));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/admins/db/news_resources", method = RequestMethod.PUT)
    public void news_resources3(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"news_resource_id", "pic_url", "news_link"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int news_resource_id = params.get("news_resource_id").getAsInt();
            String pic_url = params.get("pic_url").getAsString();
            String news_link = params.get("news_link").getAsString();

            Map<String, Object> map = new HashMap();
            map.put("pic_url", pic_url);
            map.put("news_link", news_link);
            map.put("news_resource_id", news_resource_id);
            adminService.upNewsRes(map);
            /*Map<String, Object> news = new HashMap();
            news.put("news_resource_id", news_resource_id);
            news.put("uploaded_at", new Date().getTime() / 1000);
            news.put("manager_id", admin.getAdministrator_id());
            adminService.upNews(news);*/
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.8.10 DELETE /admins/db/news_resources/{nid}*/
    @RequestMapping(value = "/admins/db/news_resources/{nid}", method = RequestMethod.DELETE)
    public void DeleteNewsResources(@PathVariable("nid") int nid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            adminService.delNewsResources(nid);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.7.1 POST /admins/db/push_messages*/
    @RequestMapping(value = "/admins/db/push_messages", method = RequestMethod.POST)
    public void PostAdminPushMsg(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"manufactory_id", "terminal_catagory_id", "content"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int terminal_catagory_id = params.get("terminal_catagory_id").getAsInt();
            List<PushService> push_service_list = userService.selPushServiceByTerCatId(terminal_catagory_id);

            String msg_content = params.get("content").getAsString();
            AndroidPushMessage msg = new AndroidPushMessage(1, msg_content);

            AndroidPushMessageForPhone aPush = new AndroidPushMessageForPhone();
            IosPushMessageForPhone iPush = IosPushMessageForPhone.getInstance();
            for (PushService push_service : push_service_list) {
                if (push_service.getDevice() == BeanProperty.ComputeType.ANDROID) {
                    aPush.pushMsgToSingleDevice(msg, push_service);
                } else {
                    iPush.sendMessage(push_service.getPush_token(), msg_content);
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

    /*3.3.7.2 POST /admins/db/terminal_catagories*/
    /*@RequestMapping(value = "/admins/db/terminal_catagories", method = RequestMethod.POST)
    public void terminal_catagories(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int manufactory_id = params.get("manufactory_id").getAsInt();
            Map<String, Object> map = new HashMap();
            map.put("manufactory_id", manufactory_id);
            List<Map<String, Object>> Catagory = adminService.postCatagory(map);
            Map<String, Object> result = new HashMap();
            result.put("catagories", Catagory);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }*/

    /*3.3.7.3 POST /admins/db/terminal_catagories*/
    @RequestMapping(value = "/admins/db/terminal_catagories", method = RequestMethod.POST)
    public void terminal_catagories(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap();
            if (params.has("manufactory_name")) {
                map.put("manufactory_name", params.get("manufactory_name").getAsString());
            }

            if (params.has("device_type_id")) {
                map.put("device_type_id", params.get("device_type_id").getAsInt());
            }

            if (params.has("terminal_catagory_name")) {
                map.put("terminal_catagory_name", params.get("terminal_catagory_name").getAsString());
            }

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            List<Map<String, Object>> category_list = adminService.qryCategoryByPage(map);

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("catagories", gson.toJsonTree(category_list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.9.1 GET /admins/db/normal_managers*/
    @RequestMapping(value = "/admins/db/normal_managers", method = RequestMethod.GET)
    public void GetNormalManagers(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String login_name = req.getParameter("login_name");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");
            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap();
            if (!StringUtils.isBlank(login_name)) {
                map.put("login_name", login_name);
            }

            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str) && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<Map<String, Object>> manager_list = adminService.selManagerByPage(map);

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("normal_managers", gson.toJsonTree(manager_list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.3.9.2	PUT /admins/db/normal_managers/{mid}
    @RequestMapping(value = "/admins/db/normal_managers/{mid}", method = RequestMethod.PUT)
    public void basic2(@PathVariable("mid") int id,
                       HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Administrator admin = getJSONParams(req, Administrator.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("full_name", admin.getFull_name());
            map.put("email", admin.getEmail());
            map.put("gender", admin.getGender());
            map.put("birthday", admin.getBirthday());
            if (admin.getPassword() != null) {
                String mad_psw = passwordEncoder.encodePassword(admin.getPassword(), md5_secret);
                map.put("password", mad_psw);
            }
            map.put("avatar_url", admin.getAvatar_url());
            map.put("phone", admin.getPhone());
            map.put("administrator_id", id);
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

    //    3.3.9.3	POST /admins/db/normal_managers
    @RequestMapping(value = "/admins/db/normal_managers", method = RequestMethod.POST)
    public void AddAdmin(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Administrator admin = gson.fromJson(params, Administrator.class);
            String mad_psw = passwordEncoder.encodePassword(admin.getPassword(), md5_secret);

            Administrator admin_db = adminService.selAdminByLoginName(admin.getLogin_name());
            if (admin_db != null) {
                setParamWarnRes(res, "登录账号已被注册", ErrorCode.REGISTER_USER);
                return;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("login_name", admin.getLogin_name());
            map.put("password", mad_psw);
            map.put("full_name", admin.getFull_name());
            map.put("email", admin.getEmail());
            map.put("gender", admin.getGender());
            map.put("birthday", admin.getBirthday());
            map.put("avatar_url", admin.getAvatar_url());
            map.put("phone", admin.getPhone());
            map.put("status", BeanProperty.UserStatus.NORMAL);
            adminService.inAdmin(map);

            Role role = roleService.selRoleByName("admin_normal");
            if (role != null) {
                Map<String, Object> role_map = new HashMap<>(2);
                role_map.put("administrator_id", map.get("administrator_id"));
                role_map.put("role_id", role.getId());
                adminService.inAdminRole(role_map);
            }

            Map<String, Object> result = new HashMap();
            result.put("administrator_id", map.get("administrator_id"));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.3.9.4	PUT /admins/db/normal_managers/status/{mid}
    @RequestMapping(value = "/admins/db/normal_managers/status/{mid}", method = RequestMethod.PUT)
    public void status(@PathVariable("mid") int id,
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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", status);
            map.put("administrator_id", id);
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

    //    3.3.9.5	DELETE /admins/db/normal_managers/{mid}
    @RequestMapping(value = "/admins/db/normal_managers/{mid}", method = RequestMethod.DELETE)
    public void delManufactory(@PathVariable("mid") int id,
                               HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            /*adminService.delAdministrator(id);
            adminService.delAdminRoleByAdminId(id);
            Map<String, Object> map = new HashMap<>(1);
            map.put("administrator_id", id);
            adminService.delAdminDevice(map);
            adminService.delAdminDistrict(map);*/
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.9.6 GET /admins/permissions/normal_managers/{mid}*/
    @RequestMapping(value = "/admins/permissions/normal_managers/{mid}", method = RequestMethod.GET)
    public void GetPermManager(@PathVariable("mid") int mid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            AdministratorDistrict district = adminService.selAdminDistrict(mid);
            JsonObject district_json = new JsonObject();
            if (district != null) {
                district_json.add("district_id", gson.toJsonTree(district.getDistrict_id()));
                district_json.add("province_id", gson.toJsonTree(district.getProvince_id()));
                district_json.add("city_id", gson.toJsonTree(district.getCity_id()));
                district_json.add("zone_id", gson.toJsonTree(district.getZone_id()));
            }
            List<Integer> device_type_list = adminService.selAdminDevice(mid);
            List<Role> role_list = adminService.qryAdminRoleByAdminId(mid);
            boolean select = false;
            boolean update = false;
            boolean export = false;
            for (Role role : role_list) {
                List<String> perm_list = role.getPermissionList();
                if (perm_list.contains("*.read")) select = true;
                if (perm_list.contains("*.modify")) update = true;
                if (perm_list.contains("*.export")) export = true;
            }
            JsonObject json = new JsonObject();
            json.add("district", district_json);
            json.add("device_types", gson.toJsonTree(device_type_list));
            json.add("select", gson.toJsonTree(select));
            json.add("update", gson.toJsonTree(update));
            json.add("export", gson.toJsonTree(export));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    /*3.3.9.7 PUT /admins/permissions/normal_managers/{mid}*/
    @RequestMapping(value = "/admins/permissions/normal_managers/{mid}", method = RequestMethod.PUT)
    public void putNormal_managers(@PathVariable("mid") int mid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"district", "device_types", "select",
                    "update", "export"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            JsonObject district = params.get("district").getAsJsonObject();
            JsonArray device_types = params.get("device_types").getAsJsonArray();
            boolean select = params.get("select").getAsBoolean();
            boolean update = params.get("update").getAsBoolean();
            boolean export = params.get("export").getAsBoolean();

            Map<String, Object> map = new HashMap();
            map.put("administrator_id", mid);
            adminService.delAdminDistrict(map);
            if (district.has("district_id")) {
                map.put("district_id", district.get("district_id").getAsInt());
            } else {
                map.put("district_id", 0);
            }
            if (district.has("province_id")) {
                map.put("province_id", district.get("province_id").getAsInt());
            } else {
                map.put("province_id", 0);
            }
            if (district.has("city_id")) {
                map.put("city_id", district.get("city_id").getAsInt());
            } else {
                map.put("city_id", 0);
            }
            if (district.has("zone_id")) {
                map.put("zone_id", district.get("zone_id").getAsInt());
            } else {
                map.put("zone_id", 0);
            }
            adminService.inAdminDistrict(map);

            adminService.delAdminDevice(map);
            for (JsonElement item : device_types) {
                map.clear();
                map.put("administrator_id", mid);
                map.put("device_type_id", item.getAsInt());
                adminService.inAdminDevice(map);
            }

            adminService.delAdminRoleByAdminId(mid);
            if (select) {
                Role role = roleService.selRoleByPerm("*.read");
                if (role != null) {
                    Map<String, Object> role_map = new HashMap<>(2);
                    role_map.put("administrator_id", mid);
                    role_map.put("role_id", role.getId());
                    adminService.inAdminRole(role_map);
                }
            }

            if (update) {
                Role role = roleService.selRoleByPerm("*.modify");
                if (role != null) {
                    Map<String, Object> role_map = new HashMap<>(2);
                    role_map.put("administrator_id", mid);
                    role_map.put("role_id", role.getId());
                    adminService.inAdminRole(role_map);
                }
            }

            if (export) {
                Role role = roleService.selRoleByPerm("*.export");
                if (role != null) {
                    Map<String, Object> role_map = new HashMap<>(2);
                    role_map.put("administrator_id", mid);
                    role_map.put("role_id", role.getId());
                    adminService.inAdminRole(role_map);
                }
            }

            Role role = roleService.selRoleByName("admin_normal");
            if (role != null) {
                Map<String, Object> role_map = new HashMap<>(2);
                role_map.put("administrator_id", mid);
                role_map.put("role_id", role.getId());
                adminService.inAdminRole(role_map);
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

}