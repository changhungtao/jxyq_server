package com.jxyq.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.doctor.DataPermission;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.doctor.Filter;
import com.jxyq.model.health.DeviceType;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class PermissionController extends BaseInterface {
    @Autowired
    private UserHealthService userHealthService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ManufactoryService manufactoryService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PermService permService;

    @Autowired
    private ConstantService constantService;

    /*3.3.6.1 POST /admins/permissions/manufactories*/
    @RequestMapping(value = "/admins/permissions/manufactories", method = RequestMethod.POST)
    public void PostPermFactory(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"manufactory_ids", "registered_from", "registered_to", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            JsonArray manufactory_ids = params.getAsJsonArray("manufactory_ids");
            if (manufactory_ids != null && manufactory_ids.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement item : manufactory_ids) {
                    ids.add(item.getAsInt());
                }
                map.put("manufactory_ids", ids);
            }
            if (params.has("registered_from")) {
                map.put("registered_from", params.get("registered_from").getAsLong());
            }
            if (params.has("registered_to")) {
                map.put("registered_to", params.get("registered_to").getAsLong());
            }
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<Manufactory> factory_list = manufactoryService.selFactory4PermByPage(map);
            JsonArray array = new JsonArray();
            Map<String, Object> search_device_map = new HashMap<>();
            search_device_map.put("status", BeanProperty.UserStatus.NORMAL);
            List<Map<String, Object>> device_type_list = deviceService.selDeviceIdAndName(search_device_map);
            for (Manufactory factory : factory_list) {
                JsonArray right_list = new JsonArray();
                List<DeviceType> device_type_right = deviceService.selDeviceTypeByFactoryId(factory.getManufactory_id());

                for (Map<String, Object> device_type : device_type_list) {
                    JsonObject right = new JsonObject();
                    int id = (int) device_type.get("device_type_id");
                    right.add("device_type_id", gson.toJsonTree(id));

                    boolean has_right = false;
                    for (DeviceType right_device_type : device_type_right) {
                        if (right_device_type.getDevice_type_id() == id) {
                            has_right = true;
                            break;
                        }
                    }
                    if (has_right) {
                        right.add("enabled", gson.toJsonTree(1));
                    } else {
                        right.add("enabled", gson.toJsonTree(0));
                    }
                    right_list.add(right);
                }

                JsonObject item = new JsonObject();
                item.add("manufactory_id", gson.toJsonTree(factory.getManufactory_id()));
                item.add("status", gson.toJsonTree(factory.getStatus()));
                item.add("registered_at", gson.toJsonTree(factory.getRegistered_at()));
                item.add("right_list", right_list);
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("permission_list", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.2 POST /admins/permissions/manufactories/fuzzy_search*/
    @RequestMapping(value = "/admins/permissions/manufactories/fuzzy_search", method = RequestMethod.POST)
    public void PostPermFactoryFuzzySearch(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (params.has("full_name")) {
                map.put("full_name", params.get("full_name").getAsString());
            }
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<Manufactory> factory_list = manufactoryService.selFactory4PermByPage(map);
            JsonArray array = new JsonArray();
            Map<String, Object> search_device_map = new HashMap<>();
            search_device_map.put("status", BeanProperty.UserStatus.NORMAL);
            List<Map<String, Object>> device_type_list = deviceService.selDeviceIdAndName(search_device_map);

            for (Manufactory factory : factory_list) {
                JsonArray right_list = new JsonArray();
                List<DeviceType> device_type_right = deviceService.selDeviceTypeByFactoryId(factory.getManufactory_id());

                for (Map<String, Object> device_type : device_type_list) {
                    JsonObject right = new JsonObject();
                    int id = (int) device_type.get("device_type_id");
                    right.add("device_type_id", gson.toJsonTree(id));

                    boolean has_right = false;
                    for (DeviceType right_device_type : device_type_right) {
                        if (right_device_type.getDevice_type_id() == id) {
                            has_right = true;
                            break;
                        }
                    }
                    if (has_right) {
                        right.add("enabled", gson.toJsonTree(1));
                    } else {
                        right.add("enabled", gson.toJsonTree(0));
                    }
                    right_list.add(right);
                }

                JsonObject item = new JsonObject();
                item.add("manufactory_id", gson.toJsonTree(factory.getManufactory_id()));
                item.add("status", gson.toJsonTree(factory.getStatus()));
                item.add("registered_at", gson.toJsonTree(factory.getRegistered_at()));
                item.add("right_list", right_list);
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("permission_list", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.3 GET /admins/permissions/manufactories/{mid}*/
    @RequestMapping(value = "/admins/permissions/manufactories/{mid}", method = RequestMethod.GET)
    public void GetPermManufactory(@PathVariable("mid") int mid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Manufactory factory = manufactoryService.selManufactoryById(mid);
            Map<String, Object> search_device_map = new HashMap<>();
            search_device_map.put("status", BeanProperty.UserStatus.NORMAL);
            List<Map<String, Object>> device_type_list = deviceService.selDeviceIdAndName(search_device_map);

            JsonArray right_list = new JsonArray();
            List<DeviceType> device_type_right = deviceService.selDeviceTypeByFactoryId(factory.getManufactory_id());

            for (Map<String, Object> device_type : device_type_list) {
                JsonObject right = new JsonObject();
                int id = (int) device_type.get("device_type_id");
                right.add("device_type_id", gson.toJsonTree(id));

                boolean has_right = false;
                for (DeviceType right_device_type : device_type_right) {
                    if (right_device_type.getDevice_type_id() == id) {
                        has_right = true;
                        break;
                    }
                }
                if (has_right) {
                    right.add("enabled", gson.toJsonTree(1));
                } else {
                    right.add("enabled", gson.toJsonTree(0));
                }
                right_list.add(right);
            }

            JsonObject json = new JsonObject();
            json.add("manufactory_name", gson.toJsonTree(factory.getFull_name()));
            json.add("right_list", right_list);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.4 PUT /admins/permissions/manufactories/{mid}*/
    @RequestMapping(value = "/admins/permissions/manufactories/{mid}", method = RequestMethod.PUT)
    public void PutPermManufactory(@PathVariable("mid") int mid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"right_list"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            deviceService.delFactoryDeviceTypeByFactoryId(mid);

            JsonArray right_list = params.getAsJsonArray("right_list");
            for (JsonElement right : right_list) {
                JsonObject right_json = right.getAsJsonObject();
                if (right_json.get("enabled").getAsInt() == 0) continue;
                deviceService.inFactoryDeviceType(0, mid, right_json.get("device_type_id").getAsInt());
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

    /*3.3.6.5 POST /admins/permissions/doctors*/
    @RequestMapping(value = "/admins/permissions/doctors", method = RequestMethod.POST)
    public void PostPermDoctors(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"full_name", "registered_from", "registered_to", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (params.has("full_name")) {
                map.put("full_name", params.get("full_name").getAsString());
            }
            if (params.has("registered_from")) {
                map.put("registered_from", params.get("registered_from").getAsLong());
            }
            if (params.has("registered_to")) {
                map.put("registered_to", params.get("registered_to").getAsLong());
            }
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<DataPermission> permission_list = permService.selDataPermissionByPage(map);

            JsonArray array = new JsonArray();
            for (DataPermission perm : permission_list) {
                JsonObject item = new JsonObject();
                List<Filter> filter_list = permService.selFilterByPermId(perm.getData_permission_id());
                JsonArray filters = new JsonArray();
                for (Filter filter : filter_list) {
                    JsonObject filter_json = new JsonObject();
                    filter_json.add("name_id", gson.toJsonTree(filter.getColumn_name_id()));
                    filter_json.add("comparison_op_id", gson.toJsonTree(filter.getComparison_op_id()));
                    filter_json.add("value", gson.toJsonTree(filter.getColumn_value()));
                    filter_json.add("logical_op_id", gson.toJsonTree(filter.getLogical_op_id()));
                    filters.add(filter_json);
                }

                List<Doctor> doctor_list = doctorService.selDoctorByPermId(perm.getData_permission_id());
                JsonArray doctors = new JsonArray();
                for (Doctor doctor : doctor_list) {
                    JsonObject doctor_json = new JsonObject();
                    doctor_json.add("doctor_id", gson.toJsonTree(doctor.getDoctor_id()));
                    doctor_json.add("doctor_name", gson.toJsonTree(doctor.getFull_name()));
                    doctor_json.add("status", gson.toJsonTree(doctor.getStatus()));
                    doctor_json.add("registered_at", gson.toJsonTree(doctor.getRegistered_at()));
                    doctors.add(doctor_json);
                }

                item.add("permission_id", gson.toJsonTree(perm.getData_permission_id()));
                item.add("permission_name", gson.toJsonTree(perm.getName()));
                item.add("district_id", gson.toJsonTree(perm.getDistrict_id()));
                item.add("province_id", gson.toJsonTree(perm.getProvince_id()));
                item.add("city_id", gson.toJsonTree(perm.getCity_id()));
                item.add("zone_id", gson.toJsonTree(perm.getZone_id()));
                item.add("data_type", gson.toJsonTree(perm.getData_type()));
                item.add("filters", filters);
                item.add("doctors", doctors);
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("permission_list", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.6 POST /admins/permissions/doctors/query*/
    @RequestMapping(value = "/admins/permissions/doctors/query", method = RequestMethod.POST)
    public void PostPermDoctorsQuery(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"permission", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            JsonObject permission = params.getAsJsonObject("permission");
            if (permission.has("district_id")) {
                map.put("district_id", permission.get("district_id").getAsInt());
            }

            if (permission.has("province_id") && permission.get("province_id").getAsInt() != 0) {
                map.put("province_id", permission.get("province_id").getAsInt());
            }

            if (permission.has("city_id") && permission.get("city_id").getAsInt() != 0) {
                map.put("city_id", permission.get("city_id").getAsInt());
            }

            if (permission.has("zone_id") && permission.get("zone_id").getAsInt() != 0) {
                map.put("zone_id", permission.get("zone_id").getAsInt());
            }

            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            int data_type = permission.get("data_type").getAsInt();
            ConstantDescription constant = constantService.selDescriptionByConstant("data_type", data_type);
            String catagory = constant.getDescription() + "_column";

            JsonArray filters = permission.getAsJsonArray("filters");
            String filter = "";
            for (JsonElement filter_element : filters) {
                JsonObject filter_json = filter_element.getAsJsonObject();
                int logical_op_id = filter_json.get("logical_op_id").getAsInt();
                int column_name_id = filter_json.get("name_id").getAsInt();
                int comparison_op_id = filter_json.get("comparison_op_id").getAsInt();
                int column_value = filter_json.get("value").getAsInt();

                ConstantDescription logical = constantService.selDescriptionByConstant("logical_op", logical_op_id);
                ConstantDescription column = constantService.selDescriptionByConstant(catagory, column_name_id);
                ConstantDescription comparison = constantService.selDescriptionByConstant("comparison_op", comparison_op_id);


                if (StringUtils.isBlank(filter)) {
                    filter += column.getDescription() + " " + comparison.getDescription() + " " + column_value + " ";
                } else {
                    filter += logical.getDescription() + " " + column.getDescription() + " " + comparison.getDescription()
                            + " " + column_value + " ";
                }
            }
            if (!StringUtils.isBlank(filter)) {
                map.put("filter", filter);
            }

            List<Map<String, Object>> map_list = null;
            switch (constant.getDescription()) {
                case "wristband":
                    map_list = userHealthService.selWristbandData4DoctorByPage(map);
                    break;
                case "sphygmomanometer":
                    map_list = userHealthService.selSphyData4DoctorByPage(map);
                    break;
                case "oximeter":
                    map_list = userHealthService.selOximeterData4DoctorByPage(map);
                    break;
                case "glucosemeter":
                    map_list = userHealthService.selGlucosemeterData4DoctorByPage(map);
                    break;
                case "thermometer":
                    map_list = userHealthService.selThermometerData4DoctorByPage(map);
                    break;
                case "fat":
                    map_list = userHealthService.selFatData4DoctorByPage(map);
                    break;
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("data_type", gson.toJsonTree(data_type));
            json.add("data_list", gson.toJsonTree(map_list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.7 POST /admins/permissions/doctors/new*/
    @RequestMapping(value = "/admins/permissions/doctors/new", method = RequestMethod.POST)
    public void PostPermDoctorsNew(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"permission", "doctors"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            JsonObject permission = params.getAsJsonObject("permission");
            DataPermission perm = new DataPermission();
            perm.setData_permission_id(0);
            perm.setName(permission.get("permission_name").getAsString());
            perm.setDistrict_id(permission.get("district_id").getAsInt());
            perm.setProvince_id(permission.get("province_id").getAsInt());
            perm.setCity_id(permission.get("city_id").getAsInt());
            perm.setZone_id(permission.get("zone_id").getAsInt());
            perm.setData_type(permission.get("data_type").getAsInt());
            permService.inDataPermission(perm);

            JsonArray filter_list = permission.getAsJsonArray("filters");
            for (JsonElement filter : filter_list) {
                JsonObject filter_json = filter.getAsJsonObject();
                Filter filter_class = new Filter();
                filter_class.setFilter_id(0);
                filter_class.setColumn_name_id(filter_json.get("name_id").getAsInt());
                filter_class.setComparison_op_id(filter_json.get("comparison_op_id").getAsInt());
                filter_class.setColumn_value(filter_json.get("value").getAsInt());
                filter_class.setLogical_op_id(filter_json.get("logical_op_id").getAsInt());
                filter_class.setData_permission_id(perm.getData_permission_id());
                permService.inFilter(filter_class);
            }

            JsonArray doctor_list = params.getAsJsonArray("doctors");
            for (JsonElement doctor_element : doctor_list) {
                int doctor_id = doctor_element.getAsInt();
                Map<String, Object> map = new HashMap<>();
                map.put("doctor_data_permission_id", 0);
                map.put("doctor_id", doctor_id);
                map.put("data_permission_id", perm.getData_permission_id());
                permService.inDoctorDataPermission(map);
            }

            JsonObject json = new JsonObject();
            json.add("permission_id", gson.toJsonTree(perm.getData_permission_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.8 GET /admins/permissions/doctors/{did}*/
    @RequestMapping(value = "/admins/permissions/doctors/{did}", method = RequestMethod.GET)
    public void GetPermDoctorsById(@PathVariable("did") int did, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");
            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("doctor_id", did);

            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str)) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<DataPermission> permission_list = permService.selDataPermByPage(map);

            JsonArray array = new JsonArray();
            for (DataPermission perm : permission_list) {
                JsonObject item = new JsonObject();
                List<Filter> filter_list = permService.selFilterByPermId(perm.getData_permission_id());
                JsonArray filters = new JsonArray();
                for (Filter filter : filter_list) {
                    JsonObject filter_json = new JsonObject();
                    filter_json.add("name_id", gson.toJsonTree(filter.getColumn_name_id()));
                    filter_json.add("comparison_op_id", gson.toJsonTree(filter.getComparison_op_id()));
                    filter_json.add("value", gson.toJsonTree(filter.getColumn_value()));
                    filter_json.add("logical_op_id", gson.toJsonTree(filter.getLogical_op_id()));
                    filters.add(filter_json);
                }

                item.add("permission_id", gson.toJsonTree(perm.getData_permission_id()));
                item.add("permission_name", gson.toJsonTree(perm.getName()));
                item.add("district_id", gson.toJsonTree(perm.getDistrict_id()));
                item.add("province_id", gson.toJsonTree(perm.getProvince_id()));
                item.add("city_id", gson.toJsonTree(perm.getCity_id()));
                item.add("zone_id", gson.toJsonTree(perm.getZone_id()));
                item.add("data_type", gson.toJsonTree(perm.getData_type()));
                item.add("filters", filters);
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("permission_list", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.9 POST /admins/permissions/doctors/{did}*/
    @RequestMapping(value = "/admins/permissions/doctors/{did}", method = RequestMethod.POST)
    public void PostPermDoctorById(@PathVariable("did") int did, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"permission"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            JsonObject permission = params.getAsJsonObject("permission");
            DataPermission perm = new DataPermission();
            perm.setData_permission_id(0);
            perm.setName(permission.get("permission_name").getAsString());
            perm.setDistrict_id(permission.get("district_id").getAsInt());
            if (permission.has("province_id")) {
                perm.setProvince_id(permission.get("province_id").getAsInt());
            } else {
                perm.setProvince_id(0);
            }
            if (permission.has("city_id")) {
                perm.setCity_id(permission.get("city_id").getAsInt());
            } else {
                perm.setCity_id(0);
            }
            if (permission.has("zone_id")) {
                perm.setZone_id(permission.get("zone_id").getAsInt());
            } else {
                perm.setZone_id(0);
            }
            perm.setData_type(permission.get("data_type").getAsInt());
            permService.inDataPermission(perm);

            JsonArray filter_list = permission.getAsJsonArray("filters");
            for (JsonElement filter : filter_list) {
                JsonObject filter_json = filter.getAsJsonObject();
                Filter filter_class = new Filter();
                filter_class.setFilter_id(0);
                filter_class.setColumn_name_id(filter_json.get("name_id").getAsInt());
                filter_class.setComparison_op_id(filter_json.get("comparison_op_id").getAsInt());
                filter_class.setColumn_value(filter_json.get("value").getAsInt());
                filter_class.setLogical_op_id(filter_json.get("logical_op_id").getAsInt());
                filter_class.setData_permission_id(perm.getData_permission_id());
                permService.inFilter(filter_class);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("doctor_data_permission_id", 0);
            map.put("doctor_id", did);
            map.put("data_permission_id", perm.getData_permission_id());
            permService.inDoctorDataPermission(map);

            JsonObject json = new JsonObject();
            json.add("permission_id", gson.toJsonTree(perm.getData_permission_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.10 PUT /admins/permissions/doctors/{did}/{pid}*/
    @RequestMapping(value = "/admins/permissions/doctors/{did}/{pid}", method = RequestMethod.PUT)
    public void PutPermDoctor(@PathVariable("did") int did, @PathVariable("pid") int pid,
                              HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"permission"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            JsonObject permission = params.getAsJsonObject("permission");
            DataPermission perm = new DataPermission();
            perm.setData_permission_id(pid);
            perm.setName(permission.get("permission_name").getAsString());
            perm.setDistrict_id(permission.get("district_id").getAsInt());
            if (permission.has("province_id")) {
                perm.setProvince_id(permission.get("province_id").getAsInt());
            } else {
                perm.setProvince_id(0);
            }
            if (permission.has("city_id")) {
                perm.setCity_id(permission.get("city_id").getAsInt());
            } else {
                perm.setCity_id(0);
            }
            if (permission.has("zone_id")) {
                perm.setZone_id(permission.get("zone_id").getAsInt());
            } else {
                perm.setZone_id(0);
            }
            perm.setData_type(permission.get("data_type").getAsInt());
            permService.inDataPermission(perm);

            permService.delFilterByPermId(pid);
            JsonArray filter_list = permission.getAsJsonArray("filters");
            for (JsonElement filter : filter_list) {
                JsonObject filter_json = filter.getAsJsonObject();
                Filter filter_class = new Filter();
                filter_class.setFilter_id(0);
                filter_class.setColumn_name_id(filter_json.get("name_id").getAsInt());
                filter_class.setComparison_op_id(filter_json.get("comparison_op_id").getAsInt());
                filter_class.setColumn_value(filter_json.get("value").getAsInt());
                filter_class.setLogical_op_id(filter_json.get("logical_op_id").getAsInt());
                filter_class.setData_permission_id(perm.getData_permission_id());
                permService.inFilter(filter_class);
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

    /*3.3.6.11 DELETE /admins/permissions/doctors/{did}/{pid}*/
    @RequestMapping(value = "/admins/permissions/doctors/{did}/{pid}", method = RequestMethod.DELETE)
    public void DelPermDoctorById(@PathVariable("did") int did, @PathVariable("pid") int pid,
                              HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            permService.delFilterByPermId(pid);
            permService.delDataPermissionById(pid);
            permService.delDoctorPermissionByPermId(pid);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.6.9 GET /admins/permissions/doctors/{pid}*/
    /*@RequestMapping(value = "/admins/permissions/doctors/{pid}", method = RequestMethod.GET)
    public void GetPermDoctor(@PathVariable("pid") int pid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            DataPermission perm = permService.selDataPermissionById(pid);


            List<Filter> filter_list = permService.selFilterByPermId(perm.getData_permission_id());
            JsonArray filters = new JsonArray();
            for (Filter filter : filter_list) {
                JsonObject filter_json = new JsonObject();
                filter_json.add("name_id", gson.toJsonTree(filter.getColumn_name_id()));
                filter_json.add("comparison_op_id", gson.toJsonTree(filter.getComparison_op_id()));
                filter_json.add("value", gson.toJsonTree(filter.getColumn_value()));
                filter_json.add("logical_op_id", gson.toJsonTree(filter.getLogical_op_id()));
                filters.add(filter_json);
            }

            List<Doctor> doctor_list = doctorService.selDoctorByPermId(perm.getData_permission_id());
            JsonArray doctors = new JsonArray();
            for (Doctor doctor : doctor_list) {
                JsonObject doctor_json = new JsonObject();
                doctor_json.add("doctor_id", gson.toJsonTree(doctor.getDoctor_id()));
                doctor_json.add("doctor_name", gson.toJsonTree(doctor.getFull_name()));
                doctor_json.add("status", gson.toJsonTree(doctor.getStatus()));
                doctor_json.add("registered_at", gson.toJsonTree(doctor.getRegistered_at()));
                doctors.add(doctor_json);
            }

            JsonObject permission = new JsonObject();
            permission.add("permission_name", gson.toJsonTree(perm.getName()));
            permission.add("district_id", gson.toJsonTree(perm.getDistrict_id()));
            permission.add("province_id", gson.toJsonTree(perm.getProvince_id()));
            permission.add("city_id", gson.toJsonTree(perm.getCity_id()));
            permission.add("zone_id", gson.toJsonTree(perm.getZone_id()));
            permission.add("data_type", gson.toJsonTree(perm.getData_type()));
            permission.add("filters", filters);

            JsonObject json = new JsonObject();
            json.add("permission", permission);
            json.add("doctors", doctors);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }*/
}