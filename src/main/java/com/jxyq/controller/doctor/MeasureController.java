package com.jxyq.controller.doctor;

import com.google.gson.JsonArray;
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
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.ConstantService;
import com.jxyq.service.inf.DoctorService;
import com.jxyq.service.inf.PermService;
import com.jxyq.service.inf.UserHealthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class MeasureController extends BaseInterface {
    @Autowired
    private PermService permService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private UserHealthService userHealthService;

    @Autowired
    private DoctorService doctorService;

    /*3.4.4.1 POST /doctors/permissions*/
    @RequestMapping(value = "/doctors/permissions", method = RequestMethod.POST)
    public void PostDocPerm(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            map.put("doctor_id", doctor.getDoctor_id());
            if (params.has("permission_name")) {
                map.put("permission_name", params.get("permission_name").getAsString());
            }

            if (params.has("data_type")) {
                map.put("data_type", params.get("data_type").getAsInt());
            }
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

    /*3.4.4.2 GET /doctors/permissions/{pid}*/
    @RequestMapping(value = "/doctors/permissions/{pid}", method = RequestMethod.GET)
    public void GetDocPermById(@PathVariable("pid") int pid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            DataPermission perm = permService.selDataPermissionById(pid);

            List<Map<String, Object>> list = permService.selDocPermission(doctor.getDoctor_id(), perm.getData_permission_id());
            if (perm == null || list == null || list.size() <= 0) {
                setParamWarnRes(res, "无权限查看相应详情", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

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
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.4.3 POST /doctors/permissions/{pid}/query*/
    @RequestMapping(value = "/doctors/permissions/{pid}/query", method = RequestMethod.POST)
    public void PostDocPermQuery(@PathVariable("pid") int pid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            DataPermission perm = permService.selDataPermissionById(pid);
            List<Map<String, Object>> list = permService.selDocPermission(doctor.getDoctor_id(), perm.getData_permission_id());
            if (perm == null || list == null || list.size() <= 0) {
                setParamWarnRes(res, "无权限查看相应详情", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);
            map.put("district_id", perm.getDistrict_id());
            if (perm.getProvince_id() != 0){
                map.put("province_id", perm.getProvince_id());
            }
            if (perm.getCity_id() != 0){
                map.put("city_id", perm.getCity_id());
            }
            if (perm.getZone_id() != 0){
                map.put("zone_id", perm.getZone_id());
            }

            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            int data_type = perm.getData_type();
            ConstantDescription constant = constantService.selDescriptionByConstant("data_type", data_type);
            String catagory = constant.getDescription() + "_column";

            String filter_str = "";
            List<Filter> filter_list = permService.selFilterByPermId(perm.getData_permission_id());
            for (Filter filter : filter_list) {
                ConstantDescription logical = constantService.selDescriptionByConstant("logical_op", filter.getLogical_op_id());
                ConstantDescription column = constantService.selDescriptionByConstant(catagory, filter.getColumn_name_id());
                ConstantDescription comparison = constantService.selDescriptionByConstant("comparison_op", filter.getComparison_op_id());

                if (StringUtils.isBlank(filter_str)) {
                    filter_str += column.getDescription() + " " + comparison.getDescription() + " " + filter.getColumn_value() + " ";
                } else {
                    filter_str += logical.getDescription() + " " + column.getDescription() + " " + comparison.getDescription()
                            + " " + filter.getColumn_value() + " ";
                }
            }
            if (!StringUtils.isBlank(filter_str)) {
                map.put("filter", filter_str);
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

}