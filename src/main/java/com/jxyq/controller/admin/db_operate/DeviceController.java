package com.jxyq.controller.admin.db_operate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.health.DeviceType;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.health.TerminalCatagory;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.DeviceService;
import com.jxyq.service.inf.ManufactoryService;
import com.jxyq.service.inf.ProductTypeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class DeviceController extends BaseInterface {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ManufactoryService manufactoryService;

    //3.2.2.1.1	POST /admins/db/terminals
    @RequestMapping(value = "/admins/db/terminals", method = RequestMethod.POST)
    public void postTerminals(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_ids", "device_type_ids",
                    "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            JsonArray manufactory_ids = params.get("manufactory_ids").getAsJsonArray();
            JsonArray device_type_ids = params.get("device_type_ids").getAsJsonArray();

            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            Map<String, Object> map = new HashMap<>();
            if (manufactory_ids != null && manufactory_ids.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement item : manufactory_ids) {
                    int manufactory_id = item.getAsInt();
                    ids.add(manufactory_id);
                }
                map.put("manufactory_ids", ids);
            }
            if (device_type_ids != null && device_type_ids.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement item : device_type_ids) {
                    int device_type_id = item.getAsInt();
                    ids.add(device_type_id);
                }
                map.put("device_type_ids", ids);
            }
            if (params.has("status")) {
                map.put("status", params.get("status").getAsLong());
            }
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> terminalList = deviceService.selTerminalsByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> terminal : terminalList) {
                Map<String, Object> terminals = new HashMap<>();
                terminals.put("terminal_id", terminal.get("terminal_id"));
                terminals.put("terminal_name", terminal.get("terminal_name"));
                terminals.put("device_type_id", terminal.get("device_type_id"));
                terminals.put("manufactory_id", terminal.get("manufactory_id"));
                terminals.put("manufactory_name", terminal.get("manufactory_name"));
                terminals.put("activated_at", terminal.get("activated_at"));
                terminals.put("status", terminal.get("status"));
                lists.add(terminals);
            }
            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("terminals", gson.toJsonTree(lists));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //  3.2.2.1.2	POST /admins/db/terminals/fuzzy_search
    @RequestMapping(value = "/admins/db/terminals/fuzzy_search", method = RequestMethod.POST)
    public void postTerminalsFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
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
            map.put("full_name", full_name);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> terminalList = deviceService.selTerminalsByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> terminal : terminalList) {
                Map<String, Object> terminals = new HashMap<>();
                terminals.put("terminal_id", terminal.get("terminal_id"));
                terminals.put("terminal_name", terminal.get("terminal_name"));
                terminals.put("device_type_id", terminal.get("device_type_id"));
                terminals.put("manufactory_id", terminal.get("manufactory_id"));
                terminals.put("manufactory_name", terminal.get("manufactory_name"));
                terminals.put("activated_at", terminal.get("activated_at"));
                terminals.put("status", terminal.get("status"));
                lists.add(terminals);
            }
            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("terminals", gson.toJsonTree(lists));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.2.1.3	GET /admins/db/terminals/{tid}
    @RequestMapping(value = "/admins/db/terminals/{tid}", method = RequestMethod.GET)
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

    /*3.2.2.6.1	POST /admins/db/device_types*/
    @RequestMapping(value = "/admins/db/device_types", method = RequestMethod.POST)
    public void postDeviceTypes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"device_type_ids", "registered_from", "registered_to",
                    "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            long registered_from = params.get("registered_from").getAsLong();
            long registered_to = params.get("registered_to").getAsLong();
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            JsonArray device_type_ids = params.get("device_type_ids").getAsJsonArray();
            Map<String, Object> map = new HashMap<>();
            if (device_type_ids != null && device_type_ids.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement item : device_type_ids) {
                    int device_type_id = item.getAsInt();
                    ids.add(device_type_id);
                }
                map.put("device_type_ids", ids);
            }
            map.put("minRegisterTime", registered_from);
            map.put("maxRegisterTime", registered_to);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> deviceTypeList = deviceService.selDeviceTypeByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> deviceType : deviceTypeList) {
                Map<String, Object> device = new HashMap<>();
                device.put("device_type_id", deviceType.get("device_type_id"));
                device.put("product_type_id", deviceType.get("product_type_id"));
                device.put("registered_at", deviceType.get("registered_at"));
                lists.add(device);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("device_types", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.2.6.2	POST /admins/db/device_types/fuzzy_search*/
    @RequestMapping(value = "/admins/db/device_types/fuzzy_search", method = RequestMethod.POST)
    public void postDeviceTypesFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "registered_from", "registered_to",
                    "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String name = params.get("name").getAsString();
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            long registered_from = params.get("registered_from").getAsLong();
            long registered_to = params.get("registered_to").getAsLong();

            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("minRegisterTime", registered_from);
            map.put("maxRegisterTime", registered_to);
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            List<Map<String, Object>> deviceTypeList = deviceService.selDeviceTypeByPage(map, pageInf);
            ArrayList lists = new ArrayList();
            for (Map<String, Object> deviceType : deviceTypeList) {
                Map<String, Object> device = new HashMap<>();
                device.put("device_type_id", deviceType.get("device_type_id"));
                device.put("product_type_id", deviceType.get("product_type_id"));
                device.put("registered_at", deviceType.get("registered_at"));
                lists.add(device);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("device_types", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    /*3.3.2.7.2	GET /admins/db/device_types/{did}*/查看厂商
    @RequestMapping(value = "/admins/db/device_types/{did}", method = RequestMethod.GET)
    public void getDeviceTypeDetail(@PathVariable("did") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> manuList = deviceService.selManuByDeviceType(id);
            Map<String, Object> result = new HashMap<>();
            result.put("manufactories", manuList);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    /*3.3.2.7.6	POST /admins/db/device_types/add*/
    @RequestMapping(value = "/admins/db/device_types/add", method = RequestMethod.POST)
    public void postDeviceTypesAdd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "product_type_id"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String name = params.get("name").getAsString();
            int product_type_id = params.get("product_type_id").getAsInt();
            DeviceType deviceType = new DeviceType();
            deviceType.setDevice_type_id(0);
            deviceType.setName(name);
            deviceType.setProduct_type_id(product_type_id);
            deviceType.setRegistered_at(new Date().getTime() / 1000);
            deviceType.setStatus(BeanProperty.DeviceTypeStatus.NORMAL);
            deviceService.insertDeviceType(deviceType);
            Map<String, Object> map = new HashMap<>(1);
            map.put("device_type_id", deviceType.getDevice_type_id());
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.2.7.8	PUT /admins/db/device_types/{did}*/
    @RequestMapping(value = "/admins/db/device_types/{did}", method = RequestMethod.PUT)
    public void postDeviceTypesEdit(@PathVariable("did") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "product_type_id"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String name = params.get("name").getAsString();
            int product_type_id = params.get("product_type_id").getAsInt();
            DeviceType deviceType = new DeviceType();
            deviceType.setDevice_type_id(id);
            deviceType.setName(name);
            deviceType.setProduct_type_id(product_type_id);
            deviceService.updateDeviceType(deviceType);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.2.7.9	DELETE /admins/db/device_types/{did}*/
    @RequestMapping(value = "/admins/db/device_types/{did}", method = RequestMethod.DELETE)
    public void delDeviceType(@PathVariable("did") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            deviceService.deleteDeviceType(id);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.3.7.1 POST /admins/db/terminal_catagories_more*/
    @RequestMapping(value = "/admins/db/terminal_catagories_more", method = RequestMethod.POST)
    public void PostAdminTerminalCatMore(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
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
            long query_date = System.currentTimeMillis() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<Map<String, Object>> categories = manufactoryService.qryTerminalCatagoryByPage(map);

            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("catagories", gson.toJsonTree(categories));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.3.7.2 POST /admins/db/manufactories/{mid}/terminal_catagories/add*/
    @RequestMapping(value = "/admins/db/manufactories/{mid}/terminal_catagories/add", method = RequestMethod.POST)
    public void PostAdminTerminalCatAdd(@PathVariable("mid") int mid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"terminal_catagory_name", "code", "product_type_id", "device_type_id", "price",
                    "picture", "profile", "status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            TerminalCatagory category = new TerminalCatagory();
            category.setTerminal_catagory_id(0);
            category.setName(params.get("terminal_catagory_name").getAsString());
            category.setCode(params.get("code").getAsString());
            category.setManufactory_id(mid);
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

    /*3.3.3.7.3 PUT /admins/db/manufactories/{mid}/terminal_catagories/{tcid}*/
    @RequestMapping(value = "/admins/db/manufactories/{mid}/terminal_catagories/{tcid}", method = RequestMethod.PUT)
    public void PutCategory(@PathVariable("mid") int mid,
                            @PathVariable("tcid") int cid,
                            HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"terminal_catagory_name", "code", "price", "picture", "profile"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            TerminalCatagory category = manufactoryService.selCategoryById(cid);
            if (category == null || category.getManufactory_id() != mid){
                setRequestErrorRes(res, "需要修改的终端型号不存在或数据不匹配", ErrorCode.NO_PERMISSION);
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

    /*3.3.3.7.4 PUT /admins/db/manufactories/{mid}/terminal_catagories/{tcid}/status*/
    @RequestMapping(value = "/admins/db/manufactories/{mid}/terminal_catagories/{tcid}/status", method = RequestMethod.PUT)
    public void PutCategoryStatus(@PathVariable("mid") int mid,
                                  @PathVariable("tcid") int cid,
                                  HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            TerminalCatagory category = manufactoryService.selCategoryById(cid);
            if (category == null || category.getManufactory_id() != mid){
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
}