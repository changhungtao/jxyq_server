package com.jxyq.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.admin.AdministratorDistrict;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.*;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.*;
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
public class CommonController extends BaseInterface {
    @Autowired
    private UserHealthService userHealthService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ManufactoryService manufactoryService;

    @Autowired
    private ConstantService constantService;

    /*3.3.4.1 POST/admins/db/measurements*/
    @RequestMapping(value = "/admins/db/measurements", method = RequestMethod.POST)
    public void PostMeasurements(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_read")) {
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree("manager"));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"data_type_ids", "status", "measured_from", "measured_to", "page_size",
                    "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            Administrator admin = (Administrator) subject.getPrincipal();
            AdministratorDistrict district = adminService.selAdminDistrict(admin.getAdministrator_id());
            if (district != null) {
                if (district.getDistrict_id() != 0) {
                    map.put("district_id", district.getDistrict_id());
                }
                if (district.getProvince_id() != 0) {
                    map.put("province_id", district.getProvince_id());
                }
                if (district.getCity_id() != 0) {
                    map.put("city_id", district.getCity_id());
                }
                if (district.getZone_id() != 0) {
                    map.put("zone_id", district.getZone_id());
                }
            }

            map.put("status", params.get("status").getAsInt());
            map.put("measured_from", params.get("measured_from").getAsLong());
            map.put("measured_to", params.get("measured_to").getAsLong());
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            if (params.has("phone")) {
                map.put("phone", params.get("phone").getAsString());
            }

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            JsonArray data_type_ids = params.getAsJsonArray("data_type_ids");
            int data_type_id = data_type_ids.get(0).getAsInt();
            ConstantDescription constant = constantService.selDescriptionByConstant("data_type", data_type_id);
            map.put("table_name", constant.getDescription() + "_data");
            map.put("data_type", data_type_id);
            List<Map<String, Object>> measurements = userHealthService.selMeasurementsByPage(map);

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("measurements", gson.toJsonTree(measurements));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.2 GET /common/measurements/wristbands/{wid}*/
    @RequestMapping(value = "/common/measurements/wristbands/{wid}", method = RequestMethod.GET)
    public void GetWristbands(@PathVariable("wid") int wid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("doctor_normal") && !subject.hasRole("admin_read")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            WristbandData wristband = userHealthService.selWristbandData(wid);
            if (wristband == null) {
                setRequestErrorRes(res, "不存在该数据", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            User user = userService.selUserByUserId(wristband.getUser_id());

            WristbandData last_id = userHealthService.selLastWristbandDataId(wid, user.getUser_id());

            Map<String, Object> map = new HashMap<>(1);
            map.put("terminal_id", wristband.getTerminal_id());
            Terminal terminal = deviceService.selTerminalByMap(map);

            Manufactory manufactory = manufactoryService.selManufactoryById(wristband.getManufactory_id());

            JsonObject json = gson.toJsonTree(wristband).getAsJsonObject();
            json.remove("user_id");
            if (manufactory != null) {
                json.add("manufactory_name", gson.toJsonTree(manufactory.getFull_name()));
            }
            json.add("terminal_name", gson.toJsonTree(terminal.getName()));
            json.add("user_full_name", gson.toJsonTree(user.getFull_name()));
            json.add("user_phone", gson.toJsonTree(user.getPhone()));
            if (last_id != null) {
                json.add("last_id", gson.toJsonTree(last_id.getWristband_data_id()));
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

    /*3.3.4.3 PUT /common/measurements/wristbands/{wid}*/
    @RequestMapping(value = "/common/measurements/wristbands/{wid}", method = RequestMethod.PUT)
    public void PutWristbands(@PathVariable("wid") int wid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_modify") && !subject.hasRole("doctor_normal")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"proposal"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            WristbandData wristband = new WristbandData();
            wristband.setWristband_data_id(wid);
            wristband.setProposal(params.get("proposal").getAsString());
            if (params.has("step_count")) {
                wristband.setStep_count(params.get("step_count").getAsInt());
            }

            if (params.has("distance")) {
                wristband.setDistance(params.get("distance").getAsInt());
            }

            if (params.has("calories")) {
                wristband.setCalories(params.get("calories").getAsInt());
            }

            if (params.has("walk_count")) {
                wristband.setWalk_count(params.get("walk_count").getAsInt());
            }

            if (params.has("walk_distance")) {
                wristband.setWalk_distance(params.get("walk_distance").getAsInt());
            }

            if (params.has("walk_calories")) {
                wristband.setWalk_calories(params.get("walk_calories").getAsInt());
            }

            if (params.has("run_count")) {
                wristband.setRun_count(params.get("run_count").getAsInt());
            }

            if (params.has("run_distance")) {
                wristband.setRun_distance(params.get("run_distance").getAsInt());
            }

            if (params.has("run_calories")) {
                wristband.setRun_calories(params.get("run_calories").getAsInt());
            }

            if (params.has("deep_duration")) {
                wristband.setDeep_duration(params.get("deep_duration").getAsInt());
            }

            if (params.has("shallow_duration")) {
                wristband.setShallow_duration(params.get("shallow_duration").getAsInt());
            }

            if (params.has("heart_rate")) {
                wristband.setHeart_rate(params.get("heart_rate").getAsInt());
            }
            if (subject.getPrincipal().getClass().getName().equals(Doctor.class.getName())) {
                Doctor doc = (Doctor) subject.getPrincipal();
                wristband.setDoctor_id(doc.getDoctor_id());
                wristband.setStatus(BeanProperty.ProposalStatus.HAS_PROPOSAL);
            }
            userHealthService.upWristbandData(wristband);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.4 GET /common/measurements/sphygmomanometers/{sid}*/
    @RequestMapping(value = "/common/measurements/sphygmomanometers/{sid}", method = RequestMethod.GET)
    public void GetSphygmomanometers(@PathVariable("sid") int sid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("doctor_normal") && !subject.hasRole("admin_read")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            UserSphygmomanometerData sphygmomanometer = userHealthService.selSphygmomanometerData(sid);
            if (sphygmomanometer == null) {
                setRequestErrorRes(res, "不存在该数据", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            User user = userService.selUserByUserId(sphygmomanometer.getUser_id());

            UserSphygmomanometerData last_id = userHealthService.selLastSphygmomanometerData(
                    sphygmomanometer.getMeasured_at(), user.getUser_id());

            Map<String, Object> map = new HashMap<>(1);
            map.put("terminal_id", sphygmomanometer.getTerminal_id());
            Terminal terminal = deviceService.selTerminalByMap(map);

            Manufactory manufactory = manufactoryService.selManufactoryById(sphygmomanometer.getManufactory_id());

            JsonObject json = gson.toJsonTree(sphygmomanometer).getAsJsonObject();
            json.remove("user_id");
            if (manufactory != null) {
                json.add("manufactory_name", gson.toJsonTree(manufactory.getFull_name()));
            }
            json.add("terminal_name", gson.toJsonTree(terminal.getName()));
            json.add("user_full_name", gson.toJsonTree(user.getFull_name()));
            json.add("user_phone", gson.toJsonTree(user.getPhone()));
            if (last_id != null) {
                json.add("last_id", gson.toJsonTree(last_id.getSphygmomanometer_data_id()));
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

    /*3.3.4.5 PUT /common/measurements/sphygmomanometers/{sid}*/
    @RequestMapping(value = "/common/measurements/sphygmomanometers/{sid}", method = RequestMethod.PUT)
    public void PutSphygmomanometers(@PathVariable("sid") int sid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_modify") && !subject.hasRole("doctor_normal")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"proposal"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            UserSphygmomanometerData sphygmomanometer = new UserSphygmomanometerData();
            sphygmomanometer.setSphygmomanometer_data_id(sid);
            sphygmomanometer.setProposal(params.get("proposal").getAsString());
            if (params.has("systolic_pressure")) {
                sphygmomanometer.setSystolic_pressure(params.get("systolic_pressure").getAsInt());
            }

            if (params.has("diastolic_pressure")) {
                sphygmomanometer.setDiastolic_pressure(params.get("diastolic_pressure").getAsInt());
            }

            if (params.has("heart_rate")) {
                sphygmomanometer.setHeart_rate(params.get("heart_rate").getAsInt());
            }

            if (subject.getPrincipal().getClass().getName().equals(Doctor.class.getName())) {
                Doctor doc = (Doctor) subject.getPrincipal();
                sphygmomanometer.setDoctor_id(doc.getDoctor_id());
                sphygmomanometer.setStatus(BeanProperty.ProposalStatus.HAS_PROPOSAL);
            }
            userHealthService.upSphygmomanometerData(sphygmomanometer);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.6 GET /common/measurements/oximeter/{oid}*/
    @RequestMapping(value = "/common/measurements/oximeter/{oid}", method = RequestMethod.GET)
    public void GetOximeter(@PathVariable("oid") int oid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("doctor_normal") && !subject.hasRole("admin_read")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            UserOximeterData oximeter = userHealthService.selOximeterData(oid);
            if (oximeter == null) {
                setRequestErrorRes(res, "不存在该数据", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            User user = userService.selUserByUserId(oximeter.getUser_id());

            UserOximeterData last_id = userHealthService.selLastOximeterDataId(
                    oximeter.getMeasured_at(), user.getUser_id());

            Map<String, Object> map = new HashMap<>(1);
            map.put("terminal_id", oximeter.getTerminal_id());
            Terminal terminal = deviceService.selTerminalByMap(map);

            Manufactory manufactory = manufactoryService.selManufactoryById(oximeter.getManufactory_id());

            JsonObject json = gson.toJsonTree(oximeter).getAsJsonObject();
            json.remove("user_id");
            if (manufactory != null) {
                json.add("manufactory_name", gson.toJsonTree(manufactory.getFull_name()));
            }
            json.add("terminal_name", gson.toJsonTree(terminal.getName()));
            json.add("user_full_name", gson.toJsonTree(user.getFull_name()));
            json.add("user_phone", gson.toJsonTree(user.getPhone()));
            if (last_id != null) {
                json.add("last_id", gson.toJsonTree(last_id.getOximeter_data_id()));
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

    /*3.3.4.7	PUT /common/measurements/oximeter/{oid}*/
    @RequestMapping(value = "/common/measurements/oximeter/{oid}", method = RequestMethod.PUT)
    public void PutOximeter(@PathVariable("oid") int oid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_modify") && !subject.hasRole("doctor_normal")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"proposal"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            UserOximeterData oximeter = new UserOximeterData();
            oximeter.setOximeter_data_id(oid);
            oximeter.setProposal(params.get("proposal").getAsString());
            if (params.has("oximeter_value")) {
                oximeter.setOximeter_value(params.get("oximeter_value").getAsInt());
            }
            if (subject.getPrincipal().getClass().getName().equals(Doctor.class.getName())) {
                Doctor doc = (Doctor) subject.getPrincipal();
                oximeter.setDoctor_id(doc.getDoctor_id());
                oximeter.setStatus(BeanProperty.ProposalStatus.HAS_PROPOSAL);
            }
            userHealthService.upOximeterData(oximeter);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.8 GET /common/measurements/glucosemeters/{gid}*/
    @RequestMapping(value = "/common/measurements/glucosemeters/{gid}", method = RequestMethod.GET)
    public void GetGlucosemeters(@PathVariable("gid") int gid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("doctor_normal") && !subject.hasRole("admin_read")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            UserGlucosemeterData glucosemeter = userHealthService.selGlucosemeterData(gid);
            if (glucosemeter == null) {
                setRequestErrorRes(res, "不存在该数据", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            User user = userService.selUserByUserId(glucosemeter.getUser_id());

            UserGlucosemeterData last_id = userHealthService.selLastGlucosemeterData(
                    glucosemeter.getMeasured_at(), user.getUser_id());

            Map<String, Object> map = new HashMap<>(1);
            map.put("terminal_id", glucosemeter.getTerminal_id());
            Terminal terminal = deviceService.selTerminalByMap(map);

            Manufactory manufactory = manufactoryService.selManufactoryById(glucosemeter.getManufactory_id());

            JsonObject json = new JsonObject();
            json.add("measured_at", gson.toJsonTree(glucosemeter.getMeasured_at()));
            json.add("period", gson.toJsonTree(glucosemeter.getPeriod()));
            json.add("glucosemeter_value", gson.toJsonTree(glucosemeter.getGlucosemeter_value()));
            if (manufactory != null) {
                json.add("manufactory_name", gson.toJsonTree(manufactory.getFull_name()));
            }
            json.add("terminal_name", gson.toJsonTree(terminal.getName()));
            json.add("user_full_name", gson.toJsonTree(user.getFull_name()));
            json.add("user_phone", gson.toJsonTree(user.getPhone()));
            if (last_id != null) {
                json.add("last_id", gson.toJsonTree(last_id.getGlucosemeter_data_id()));
            }
            json.add("proposal", gson.toJsonTree(glucosemeter.getProposal()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.9 PUT /common/measurements/glucosemeters/{gid}*/
    @RequestMapping(value = "/common/measurements/glucosemeters/{gid}", method = RequestMethod.PUT)
    public void PutGlucosemeters(@PathVariable("gid") int gid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_modify") && !subject.hasRole("doctor_normal")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"period", "proposal"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            UserGlucosemeterData glucosemeter = new UserGlucosemeterData();
            glucosemeter.setGlucosemeter_data_id(gid);
            glucosemeter.setProposal(params.get("proposal").getAsString());
            glucosemeter.setPeriod(params.get("period").getAsInt());
            if (params.has("glucosemeter_value")) {
                glucosemeter.setGlucosemeter_value(params.get("glucosemeter_value").getAsInt());
            }
            if (subject.getPrincipal().getClass().getName().equals(Doctor.class.getName())) {
                Doctor doc = (Doctor) subject.getPrincipal();
                glucosemeter.setDoctor_id(doc.getDoctor_id());
                glucosemeter.setStatus(BeanProperty.ProposalStatus.HAS_PROPOSAL);
            }
            userHealthService.upGlucosemeterData(glucosemeter);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.10 GET /common/measurements/thermometers/{tid}*/
    @RequestMapping(value = "/common/measurements/thermometers/{tid}", method = RequestMethod.GET)
    public void GetThermometers(@PathVariable("tid") int tid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("doctor_normal") && !subject.hasRole("admin_read")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            UserThermometersData thermometer = userHealthService.selThermometerData(tid);
            if (thermometer == null) {
                setRequestErrorRes(res, "不存在该数据", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            User user = userService.selUserByUserId(thermometer.getUser_id());

            UserThermometersData last_id = userHealthService.selLastThermometerData(
                    thermometer.getMeasured_at(), user.getUser_id());

            Map<String, Object> map = new HashMap<>(1);
            map.put("terminal_id", thermometer.getTerminal_id());
            Terminal terminal = deviceService.selTerminalByMap(map);

            Manufactory manufactory = manufactoryService.selManufactoryById(thermometer.getManufactory_id());

            JsonObject json = new JsonObject();
            json.add("measured_at", gson.toJsonTree(thermometer.getMeasured_at()));
            json.add("thermometer_value", gson.toJsonTree(thermometer.getThermometer_value()));
            if (manufactory != null) {
                json.add("manufactory_name", gson.toJsonTree(manufactory.getFull_name()));
            }
            json.add("terminal_name", gson.toJsonTree(terminal.getName()));
            json.add("user_full_name", gson.toJsonTree(user.getFull_name()));
            json.add("user_phone", gson.toJsonTree(user.getPhone()));
            if (last_id != null) {
                json.add("last_id", gson.toJsonTree(last_id.getThermometer_data_id()));
            }
            json.add("proposal", gson.toJsonTree(thermometer.getProposal()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.11 PUT /common/measurements/thermometers/{tid}*/
    @RequestMapping(value = "/common/measurements/thermometers/{tid}", method = RequestMethod.PUT)
    public void PutThermometers(@PathVariable("tid") int tid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_modify") && !subject.hasRole("doctor_normal")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"proposal"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            UserThermometersData thermometer = new UserThermometersData();
            thermometer.setThermometer_data_id(tid);
            thermometer.setProposal(params.get("proposal").getAsString());
            if (params.has("thermometer_value")) {
                thermometer.setThermometer_value(params.get("thermometer_value").getAsInt());
            }
            if (subject.getPrincipal().getClass().getName().equals(Doctor.class.getName())) {
                Doctor doc = (Doctor) subject.getPrincipal();
                thermometer.setDoctor_id(doc.getDoctor_id());
                thermometer.setStatus(BeanProperty.ProposalStatus.HAS_PROPOSAL);
            }
            userHealthService.upThermometersData(thermometer);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.4.12 GET /common/measurements/fat/{fid}*/
    @RequestMapping(value = "/common/measurements/fat/{fid}", method = RequestMethod.GET)
    public void GetFat(@PathVariable("fid") int fid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("doctor_normal") && !subject.hasRole("admin_read")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            UserFatData fat = userHealthService.selFatData(fid);
            if (fat == null) {
                setRequestErrorRes(res, "不存在该数据", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            User user = userService.selUserByUserId(fat.getUser_id());

            UserFatData last_id = userHealthService.selLastFatData(
                    fat.getMeasured_at(), user.getUser_id());

            Map<String, Object> map = new HashMap<>(1);
            map.put("terminal_id", fat.getTerminal_id());
            Terminal terminal = deviceService.selTerminalByMap(map);

            Manufactory manufactory = manufactoryService.selManufactoryById(fat.getManufactory_id());

            JsonObject json = gson.toJsonTree(fat).getAsJsonObject();
            json.remove("user_id");
            if (manufactory != null) {
                json.add("manufactory_name", gson.toJsonTree(manufactory.getFull_name()));
            }
            json.add("terminal_name", gson.toJsonTree(terminal.getName()));
            json.add("user_full_name", gson.toJsonTree(user.getFull_name()));
            json.add("user_phone", gson.toJsonTree(user.getPhone()));
            if (last_id != null) {
                json.add("last_id", gson.toJsonTree(last_id.getFat_data_id()));
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

    /*3.3.4.13 PUT /common/measurements/fat/{fid}*/
    @RequestMapping(value = "/common/measurements/fat/{fid}", method = RequestMethod.PUT)
    public void PutFat(@PathVariable("fid") int fid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin_modify") && !subject.hasRole("doctor_normal")) {
                String role = "";
                String auth_role = subject.getPrincipal().getClass().getName();
                if (auth_role.equals(Administrator.class.getName())) {
                    role = "manager";
                } else if (auth_role.equals(Manufactory.class.getName())) {
                    role = "manufactory";
                } else if (auth_role.equals(Doctor.class.getName())) {
                    role = "doctor";
                } else {
                    role = "other";
                }
                JsonObject json = new JsonObject();
                json.add("role", gson.toJsonTree(role));
                setResponseContent(res, json);
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"proposal"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            UserFatData fat = new UserFatData();
            fat.setFat_data_id(fid);
            fat.setProposal(params.get("proposal").getAsString());
            if (params.has("bmi_value")) {
                fat.setBmi_value(params.get("bmi_value").getAsInt());
            }

            if (params.has("weight_value")) {
                fat.setWeight_value(params.get("weight_value").getAsInt());
            }

            if (params.has("fat_value")) {
                fat.setFat_value(params.get("fat_value").getAsInt());
            }

            if (params.has("calorie_value")) {
                fat.setCalorie_value(params.get("calorie_value").getAsInt());
            }

            if (params.has("moisture_value")) {
                fat.setMoisture_value(params.get("moisture_value").getAsInt());
            }

            if (params.has("muscle_value")) {
                fat.setMuscle_value(params.get("muscle_value").getAsInt());
            }

            if (params.has("visceral_fat_value")) {
                fat.setVisceral_fat_value(params.get("visceral_fat_value").getAsInt());
            }

            if (params.has("bone_value")) {
                fat.setBone_value(params.get("bone_value").getAsInt());
            }
            if (subject.getPrincipal().getClass().getName().equals(Doctor.class.getName())) {
                Doctor doc = (Doctor) subject.getPrincipal();
                fat.setDoctor_id(doc.getDoctor_id());
                fat.setStatus(BeanProperty.ProposalStatus.HAS_PROPOSAL);
            }
            userHealthService.upFatData(fat);

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