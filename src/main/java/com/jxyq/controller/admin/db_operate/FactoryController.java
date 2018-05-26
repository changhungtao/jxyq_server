package com.jxyq.controller.admin.db_operate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.commons.util.OnlineCount;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.DeviceService;
import com.jxyq.service.inf.ManufactoryService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class FactoryController extends BaseInterface {
    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");

    @Autowired
    private ManufactoryService manufactoryService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    /*3.3.1.1 GET /admins/db/manufactories*/
    @RequestMapping(value = "/admins/db/manufactories", method = RequestMethod.GET)
    public void GetAdminFactory(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> list = manufactoryService.qryFactoryIdAndName();
            JsonObject json = new JsonObject();
            json.add("manufactories", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //   3.2.2.2.1	POST /admins/db/manufactories
    @RequestMapping(value = "/admins/db/manufactories", method = RequestMethod.POST)
    public void postManufactories(HttpServletRequest req, HttpServletResponse res) {
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
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            Map<String, Object> map = new HashMap<>();

            if (params.has("manufactory_name")) {
                map.put("name", params.get("manufactory_name").getAsString());
            }

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

            Map<String, Object> factoryCount = manufactoryService.selFactoryCount();
            long count = 0;
            if (factoryCount != null) {
                count = (long) factoryCount.get("count");
            }

            OnlineCount onlineCount = OnlineCount.getInstance();

            map.put("minRegisterTime", registered_from);
            map.put("maxRegisterTime", registered_to);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> manufactories = manufactoryService.selManufactoriesByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> manu : manufactories) {
                Map<String, Object> cnt = new HashMap<>();
                cnt.put("manufactory_id", manu.get("manufactory_id"));
                cnt.put("login_name", manu.get("login_name"));
                cnt.put("manufactory_name", manu.get("full_name"));
                Map<String, Object> factory_map = new HashMap<>();
                factory_map.put("manufactory_id", manu.get("manufactory_id"));
                List<Integer> deviceList = manufactoryService.selectManDevice(factory_map);
                cnt.put("device_type_ids", deviceList);
                cnt.put("registered_at", manu.get("registered_at"));
                cnt.put("status", manu.get("status"));
                lists.add(cnt);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("online_count", gson.toJsonTree(onlineCount.factory_count));
            result.put("member_count", gson.toJsonTree(count));
            result.put("manufactories", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2.2	POST /admins/db/manufactories/fuzzy_search
    @RequestMapping(value = "/admins/db/manufactories/fuzzy_search", method = RequestMethod.POST)
    public void postManufactoriesFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"full_name", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String full_name = params.get("full_name").getAsString();
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            Map<String, Object> map = new HashMap<>();
            if (!StringUtils.isBlank(full_name)) {
                map.put("name", full_name);
            }
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            Map<String, Object> factoryCount = manufactoryService.selFactoryCount();
            long count = 0;
            if (factoryCount != null) {
                count = (long) factoryCount.get("count");
            }

            OnlineCount onlineCount = OnlineCount.getInstance();

            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> manufactories = manufactoryService.selManufactoriesByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> manu : manufactories) {
                Map<String, Object> cnt = new HashMap<>();
                cnt.put("manufactory_id", manu.get("manufactory_id"));
                cnt.put("login_name", manu.get("login_name"));
                cnt.put("manufactory_name", manu.get("full_name"));
                Map<String, Object> factory_map = new HashMap<>();
                factory_map.put("manufactory_id", manu.get("manufactory_id"));
                List<Integer> deviceList = manufactoryService.selectManDevice(factory_map);
                cnt.put("device_type_ids", deviceList);
                cnt.put("registered_at", manu.get("registered_at"));
                cnt.put("status", manu.get("status"));
                lists.add(cnt);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("online_count", gson.toJsonTree(onlineCount.factory_count));
            result.put("member_count", gson.toJsonTree(count));
            result.put("manufactories", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2.3	GET /admins/db/manufactories/{mid}
    @RequestMapping(value = "/admins/db/manufactories/{mid}", method = RequestMethod.GET)
    public void getManufactoryDetail(@PathVariable("mid") int id,
                                     HttpServletRequest req, HttpServletResponse res) {
        try {
            ResponseContent content = new ResponseContent();
            Manufactory manufactory = manufactoryService.selManufactoryById(id);
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", id);
            List<Integer> deviceList = manufactoryService.selectManDevice(map);
            manufactory.setDevice_type_ids(deviceList);
            JsonObject json = gson.toJsonTree(manufactory).getAsJsonObject();
            json.remove("manufactory_id");
            json.remove("login_name");
            json.remove("password");
            json.remove("register");
            json.remove("updated_at");
            json.remove("creator");
            json.remove("status");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2.4	POST /admins/db/manufactories/add
    @RequestMapping(value = "/admins/db/manufactories/add", method = RequestMethod.POST)
    public void postManufactoryAdd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"login_name", "password", "contactor", "department", "telephone", "phone",
                    "full_name", "code", "device_type_ids"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            JsonArray device_type_ids = params.get("device_type_ids").getAsJsonArray();
            Manufactory m = gson.fromJson(params, Manufactory.class);
            String mad_psw = passwordEncoder.encodePassword(m.getPassword(), md5_secret);
            m.setPassword(mad_psw);
            m.setRegistered_at(new Date().getTime() / 1000);
            m.setUpdated_at(-1);
            Subject subject = SecurityUtils.getSubject();
            Administrator admin = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
            m.setCreator(admin.getLogin_name());
            m.setStatus(BeanProperty.UserStatus.NORMAL);
            manufactoryService.insertManufactory(m);
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_id", m.getManufactory_id());
            if (device_type_ids != null && device_type_ids.size() > 0) {
                for (JsonElement item : device_type_ids) {
                    map.put("device_type_id", item.getAsInt());
                    manufactoryService.insertManDevice(map);
                }
            }
            map.remove("device_type_id");
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.2.2.5	PUT /admins/db/manufactories/{mid}/state
    @RequestMapping(value = "/admins/db/manufactories/{mid}/state", method = RequestMethod.PUT)
    @ResponseBody
    public void putManufactoryState(@PathVariable("mid") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int status = params.get("status").getAsInt();
            manufactoryService.upManufactoryStatus(id, status);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //  3.2.2.2.6	PUT /admins/db/manufactories/{mid}
    @RequestMapping(value = "/admins/db/manufactories/{mid}", method = RequestMethod.PUT)
    public void postManufactoryEdit(@PathVariable("mid") int id,
                                    HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"contactor", "department", "telephone", "phone",
                    "email", "full_name", "code", "province_id",
                    "city_id", "zone_id", "address", "members",
                    "industry", "nature", "business_licence", "internal_certificate", "local_certificate",
                    "code_certificate", "logo_url"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Manufactory manufactory = gson.fromJson(params, Manufactory.class);
            Map<String, Object> map = new HashMap<>();
            if (params.has("password")) {
                String mad_psw = passwordEncoder.encodePassword(manufactory.getPassword(), md5_secret);
                map.put("password", mad_psw);
            }
            map.put("manufactory_id", id);
            map.put("contactor", manufactory.getContactor());
            map.put("department", manufactory.getDepartment());
            map.put("telephone", manufactory.getTelephone());
            map.put("phone", manufactory.getPhone());
            map.put("email", manufactory.getEmail());
            map.put("full_name", manufactory.getFull_name());
            map.put("code", manufactory.getCode());
            if (manufactory.getProfile() != null) {
                map.put("profile", manufactory.getProfile());
            }
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
            map.put("logo_url", manufactory.getLogo_url());
            map.put("updated_at", new Date().getTime() / 1000);
            Subject subject = SecurityUtils.getSubject();
            Administrator admin = (Administrator) subject.getPrincipals().getPrimaryPrincipal();
            map.put("creator", admin.getLogin_name());
            manufactoryService.upManufactoryInf(map);
//          负责销售
           /* Map<String, Object> manuMap = new HashMap<>();
            manuMap.put("manufactory_id", id);
            List<Integer> device_type_ids = manufactory.getDevice_type_ids();

            if (device_type_ids != null && device_type_ids.size() > 0) {
                manufactoryService.delManDevice(manuMap);
                for (Integer item : device_type_ids) {
                    manuMap.put("device_type_id", item);
                    manufactoryService.insertManDevice(manuMap);
                }
            }*/
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2.7	DELETE /admins/db/manufactories/{mid}
    @RequestMapping(value = "/admins/db/manufactories/{mid}", method = RequestMethod.DELETE)
    public void delManufactory(@PathVariable("mid") int id,
                               HttpServletRequest req, HttpServletResponse res) {
        try {
            ResponseContent content = new ResponseContent();
            /*manufactoryService.delManufactory(id);*/
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
