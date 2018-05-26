package com.jxyq.app.user_health;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.BeanProperty.OperationType;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.model.health.*;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.Consumption;
import com.jxyq.model.user.ConsumptionRule;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.AdminService;
import com.jxyq.service.inf.DeviceService;
import com.jxyq.service.inf.UserHealthService;
import com.jxyq.service.inf.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserHealthInterface extends BaseInterface {
    @Autowired
    private UserHealthService userHealthService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DeviceService deviceService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    //3.6.1	PUT /api/user
    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public void UserInfoInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"nick_name", "gender", "birthday", "height",
                    "province_id", "city_id", "zone_id", "weight", "target_weight"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            User userInfo = (User) req.getAttribute("user");
            int user_id = userInfo.getUser_id();
            User users = userService.selUserByUserId(user_id);

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user_id);
            map.put("nick_name", params.get("nick_name").getAsString());
            map.put("gender", params.get("gender").getAsInt());
            map.put("birthday", params.get("birthday").getAsString());
            int province_id = params.get("province_id").getAsInt();
            Map<String, Object> province = adminService.selProvinces(province_id);
            map.put("district_id", (int) province.get("district_id"));
            map.put("province_id", province_id);
            map.put("city_id", params.get("city_id").getAsInt());
            map.put("zone_id", params.get("zone_id").getAsInt());
            map.put("height", params.get("height").getAsInt());
            map.put("weight", params.get("weight").getAsInt());
            map.put("target_weight", params.get("target_weight").getAsInt());
            if (params.has("full_name")) {
                map.put("full_name", params.get("full_name").getAsString());
                map.put("authenticity", BeanProperty.RealStatus.REAL);
                map.put("filled_in_at", System.currentTimeMillis() / 1000);
            }
            if (params.has("avatar_url")) {
                map.put("avatar_url", params.get("avatar_url").getAsString());
            }
            if (params.has("address")) {
                map.put("address", params.get("address").getAsString());
            }
            if (params.has("qq")) {
                map.put("qq", params.get("qq").getAsString());
            }
            if (params.has("email")) {
                map.put("email", params.get("email").getAsString());
            }
            Consumption consumption = userService.selConsumption(user_id, OperationType.IMPROVE_INFORMATION);
            if (consumption == null) {
                map.put("points", users.getPoints() + 20);
            }
            userHealthService.putUserInfo(map);

            if (consumption == null) {
                Consumption com = new Consumption();
                com.setPoints(20);
                com.setNew_points(users.getPoints() + 20);
                com.setUser_id(user_id);
                com.setDescription("完善个人资料");
                com.setHappened_at(new Date().getTime() / 1000);
                com.setOperation_type(1);
                userHealthService.inPoints(com);
            }

            User user = userService.selUserByUserId(user_id);
            user.setPassword(null);
            user.setRegistered_at(null);
            user.setUpdated_at(null);
            content.setSuccess_message(user);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.2	POST /api/user/wristbands
    @RequestMapping(value = "/api/user/wristbands", method = RequestMethod.POST)
    public void PostWristbandsInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id", "terminal_id", "records"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Integer manufactoryId = params.get("manufactory_id").getAsInt();
            Integer terminalId = params.get("terminal_id").getAsInt();
            JsonArray records = params.get("records").getAsJsonArray();

            String itemParams[] = {"measured_at", "step_count", "distance", "calories", "walk_count",
                    "walk_distance", "walk_calories", "run_count", "run_distance", "run_calories",
                    "deep_duration", "shallow_duration", "heart_rate"};
            for (JsonElement record : records) {
                if (!checkNecessaryParam(record.getAsJsonObject(), itemParams)) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }
                WristbandData wristband = new WristbandData();
                wristband.setUser_id(user.getUser_id());
                long measured_at = record.getAsJsonObject().get("measured_at").getAsLong();
                wristband.setMeasured_at(measured_at);
                wristband.setStep_count(record.getAsJsonObject().get("step_count").getAsInt());
                wristband.setDistance(record.getAsJsonObject().get("distance").getAsInt());
                wristband.setCalories(record.getAsJsonObject().get("calories").getAsInt());
                wristband.setWalk_count(record.getAsJsonObject().get("walk_count").getAsInt());
                wristband.setWalk_distance(record.getAsJsonObject().get("walk_distance").getAsInt());
                wristband.setWalk_calories(record.getAsJsonObject().get("walk_calories").getAsInt());
                wristband.setRun_count(record.getAsJsonObject().get("run_count").getAsInt());
                wristband.setRun_distance(record.getAsJsonObject().get("run_distance").getAsInt());
                wristband.setRun_calories(record.getAsJsonObject().get("run_calories").getAsInt());
                wristband.setDeep_duration(record.getAsJsonObject().get("deep_duration").getAsInt());
                wristband.setShallow_duration(record.getAsJsonObject().get("shallow_duration").getAsInt());
                wristband.setHeart_rate(record.getAsJsonObject().get("heart_rate").getAsInt());
                wristband.setTerminal_id(terminalId);
                wristband.setManufactory_id(manufactoryId);
                wristband.setProposal("");
                wristband.setDoctor_id(-1);
                wristband.setStatus(BeanProperty.ProposalStatus.UN_PROPOSAL);
                userHealthService.insertWristband(wristband);
            }

            upUserMeasureTime(user);
            long cnt = getConsumptionTodayCount(user.getUser_id(), OperationType.HEALTH_MONITOR);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.HEALTH_MONITOR);
            }
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.3	POST /api/user/sphygmomanometers
    @RequestMapping(value = "/api/user/sphygmomanometers", method = RequestMethod.POST)
    public void PostSphygInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            int id = user.getUser_id();
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id", "terminal_id", "records"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Integer manufactoryId = params.get("manufactory_id").getAsInt();
            Integer terminalId = params.get("terminal_id").getAsInt();
            JsonArray records = params.get("records").getAsJsonArray();

            String itemParams[] = {"measured_at", "systolic_pressure", "diastolic_pressure", "heart_rate"};
            for (JsonElement record : records) {
                if (!checkNecessaryParam(record.getAsJsonObject(), itemParams)) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }

                UserSphygmomanometerData sphygModel = new UserSphygmomanometerData();
                sphygModel.setSphygmomanometer_data_id(0);
                sphygModel.setMeasured_at(record.getAsJsonObject().get("measured_at").getAsLong());
                sphygModel.setSystolic_pressure(record.getAsJsonObject().get("systolic_pressure").getAsInt());
                sphygModel.setDiastolic_pressure(record.getAsJsonObject().get("diastolic_pressure").getAsInt());
                sphygModel.setHeart_rate(record.getAsJsonObject().get("heart_rate").getAsInt());
                sphygModel.setUser_id(id);
                sphygModel.setTerminal_id(terminalId);
                sphygModel.setManufactory_id(manufactoryId);
                sphygModel.setProposal("");
                sphygModel.setDoctor_id(-1);
                sphygModel.setStatus(BeanProperty.ProposalStatus.UN_PROPOSAL);
                userHealthService.insertSphyg(sphygModel);
            }
            upUserMeasureTime(user);
            long cnt = getConsumptionTodayCount(user.getUser_id(), OperationType.HEALTH_MONITOR);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.HEALTH_MONITOR);
            }
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    //3.6.4	POST /api/user/glucosemeters
    @RequestMapping(value = "/api/user/glucosemeters", method = RequestMethod.POST)
    public void PostGlucoseInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id", "terminal_id", "records"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Integer manufactoryId = params.get("manufactory_id").getAsInt();
            Integer terminalId = params.get("terminal_id").getAsInt();
            JsonArray records = params.get("records").getAsJsonArray();

            String itemParams[] = {"measured_at", "period", "glucosemeter_value"};
            for (JsonElement record : records) {
                if (!checkNecessaryParam(record.getAsJsonObject(), itemParams)) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }

                UserGlucosemeterData gMeterModel = new UserGlucosemeterData();
                gMeterModel.setGlucosemeter_data_id(0);
                gMeterModel.setMeasured_at(record.getAsJsonObject().get("measured_at").getAsLong());
                gMeterModel.setGlucosemeter_value(record.getAsJsonObject().get("glucosemeter_value").getAsInt());
                gMeterModel.setPeriod(record.getAsJsonObject().get("period").getAsInt());
                gMeterModel.setUser_id(user.getUser_id());
                gMeterModel.setTerminal_id(terminalId);
                gMeterModel.setManufactory_id(manufactoryId);
                gMeterModel.setProposal("");
                gMeterModel.setDoctor_id(-1);
                gMeterModel.setStatus(BeanProperty.ProposalStatus.UN_PROPOSAL);
                userHealthService.insertGlucose(gMeterModel);
            }
            upUserMeasureTime(user);
            long cnt = getConsumptionTodayCount(user.getUser_id(), OperationType.HEALTH_MONITOR);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.HEALTH_MONITOR);
            }
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.5	POST /api/user/oximeters
    @RequestMapping(value = "/api/user/oximeters", method = RequestMethod.POST)
    public void PostOximetersInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id", "terminal_id", "records"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Integer manufactoryId = params.get("manufactory_id").getAsInt();
            Integer terminalId = params.get("terminal_id").getAsInt();
            JsonArray records = params.get("records").getAsJsonArray();

            String itemParams[] = {"measured_at", "oximeter_value"};
            for (JsonElement record : records) {
                if (!checkNecessaryParam(record.getAsJsonObject(), itemParams)) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }

                UserOximeterData oximeterModel = new UserOximeterData();
                oximeterModel.setOximeter_data_id(0);
                oximeterModel.setMeasured_at(record.getAsJsonObject().get("measured_at").getAsLong());
                oximeterModel.setOximeter_value(record.getAsJsonObject().get("oximeter_value").getAsInt());
                oximeterModel.setUser_id(user.getUser_id());
                oximeterModel.setTerminal_id(terminalId);
                oximeterModel.setManufactory_id(manufactoryId);
                oximeterModel.setProposal("");
                oximeterModel.setDoctor_id(-1);
                oximeterModel.setStatus(BeanProperty.ProposalStatus.UN_PROPOSAL);
                userHealthService.insertOximeter(oximeterModel);
            }
            upUserMeasureTime(user);
            long cnt = getConsumptionTodayCount(user.getUser_id(), OperationType.HEALTH_MONITOR);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.HEALTH_MONITOR);
            }
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.6	POST /api/user/thermometers
    @RequestMapping(value = "/api/user/thermometers", method = RequestMethod.POST)
    public void PostThermometersInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id", "terminal_id", "records"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Integer manufactoryId = params.get("manufactory_id").getAsInt();
            Integer terminalId = params.get("terminal_id").getAsInt();
            JsonArray records = params.get("records").getAsJsonArray();

            String itemParams[] = {"measured_at", "thermometer_value"};
            for (JsonElement record : records) {
                if (!checkNecessaryParam(record.getAsJsonObject(), itemParams)) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }

                UserThermometersData thermometerModel = new UserThermometersData();
                thermometerModel.setThermometer_data_id(0);
                thermometerModel.setMeasured_at(record.getAsJsonObject().get("measured_at").getAsLong());
                thermometerModel.setThermometer_value(record.getAsJsonObject().get("thermometer_value").getAsInt());
                thermometerModel.setUser_id(user.getUser_id());
                thermometerModel.setTerminal_id(terminalId);
                thermometerModel.setManufactory_id(manufactoryId);
                thermometerModel.setProposal("");
                thermometerModel.setDoctor_id(-1);
                thermometerModel.setStatus(BeanProperty.ProposalStatus.UN_PROPOSAL);
                userHealthService.insertThermometer(thermometerModel);
            }
            upUserMeasureTime(user);
            long cnt = getConsumptionTodayCount(user.getUser_id(), OperationType.HEALTH_MONITOR);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.HEALTH_MONITOR);
            }
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }
//    3.6.7	POST /api/user/fats

    @RequestMapping(value = "/api/user/fats", method = RequestMethod.POST)
    public void PostFatsInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"manufactory_id", "terminal_id", "records"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Integer manufactoryId = params.get("manufactory_id").getAsInt();
            Integer terminalId = params.get("terminal_id").getAsInt();
            JsonArray records = params.get("records").getAsJsonArray();

            String itemParams[] = {"measured_at", "bmi_value", "fat_value", "calorie_value",
                    "moisture_value", "muscle_value", "visceral_fat_value", "bone_value", "weight_value"};
            for (JsonElement record : records) {
                if (!checkNecessaryParam(record.getAsJsonObject(), itemParams)) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }

                UserFatData fatModel = new UserFatData();
                fatModel.setFat_data_id(0);
                fatModel.setMeasured_at(record.getAsJsonObject().get("measured_at").getAsLong());
                fatModel.setBmi_value(record.getAsJsonObject().get("bmi_value").getAsInt());
                fatModel.setFat_value(record.getAsJsonObject().get("fat_value").getAsInt());
                fatModel.setCalorie_value(record.getAsJsonObject().get("calorie_value").getAsInt());
                fatModel.setMoisture_value(record.getAsJsonObject().get("moisture_value").getAsInt());
                fatModel.setMuscle_value(record.getAsJsonObject().get("muscle_value").getAsInt());
                fatModel.setVisceral_fat_value(record.getAsJsonObject().get("visceral_fat_value").getAsInt());
                fatModel.setBone_value(record.getAsJsonObject().get("bone_value").getAsInt());
                fatModel.setWeight_value(record.getAsJsonObject().get("weight_value").getAsInt());
                fatModel.setUser_id(user.getUser_id());
                fatModel.setTerminal_id(terminalId);
                fatModel.setManufactory_id(manufactoryId);
                fatModel.setProposal("");
                fatModel.setDoctor_id(-1);
                fatModel.setStatus(BeanProperty.ProposalStatus.UN_PROPOSAL);
                userHealthService.insertFat(fatModel);
            }
            upUserMeasureTime(user);
            long cnt = getConsumptionTodayCount(user.getUser_id(), OperationType.HEALTH_MONITOR);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.HEALTH_MONITOR);
            }
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.13	GET /api/user/fats
    @RequestMapping(value = "/api/user/fats", method = RequestMethod.GET)
    public void GetFatInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            String dataType = req.getParameter("data_type");
            String beginDate = req.getParameter("begin_date");
            String endDate = req.getParameter("end_date");
            if (dataType == null || beginDate == null || endDate == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int userId = 0;
            if (StringUtils.isBlank(phone)) {
                userId = user.getUser_id();
            } else {
                User associatedUser = userService.selUserByPhone(phone);
                if (associatedUser == null) {
                    setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                    return;
                }
                userId = associatedUser.getUser_id();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", Long.valueOf(userId));
            map.put("begin_date", Long.valueOf(beginDate));
            map.put("end_date", Long.valueOf(endDate));
            List<UserFatData> fatDetails = userHealthService.selFatDetail(map);
            if (dataType.equals("2")) {
                List<Map> details = new ArrayList<>();
                for (UserFatData fatDetail : fatDetails) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("measured_at", fatDetail.getMeasured_at());
                    detail.put("bmi_value", fatDetail.getBmi_value());
                    detail.put("fat_value", fatDetail.getFat_value());
                    detail.put("calorie_value", fatDetail.getCalorie_value());
                    detail.put("moisture_value", fatDetail.getMoisture_value());
                    detail.put("muscle_value", fatDetail.getMuscle_value());
                    detail.put("visceral_fat_value", fatDetail.getVisceral_fat_value());
                    detail.put("bone_value", fatDetail.getBone_value());
                    detail.put("weight_value", fatDetail.getWeight_value());
                    details.add(detail);
                }
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("details", details);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            } else if (dataType.equals("1")) {
                if (fatDetails == null || fatDetails.size() <= 0) {
                    Map<String, Object> resultMap = new HashMap<>(1);
                    resultMap.put("summaries", new Object());
                    content.setSuccess_message(resultMap);
                    setResponseContent(res, content);
                    return;
                }
                List<Map> f_summaries = new ArrayList();
                long count = 0;
                long sum_heartBeat = 0;
                long sum_heartBeat2 = 0;
                long sum_heartBeat3 = 0;
                long sum_heartBeat4 = 0;
                long sum_heartBeat5 = 0;
                long sum_heartBeat6 = 0;
                long sum_heartBeat7 = 0;
                long sum_heartBeat8 = 0;
                long day_count = 0;
                long day_sum_heartBeat = 0;
                long day_sum_heartBeat2 = 0;
                long day_sum_heartBeat3 = 0;
                long day_sum_heartBeat4 = 0;
                long day_sum_heartBeat5 = 0;
                long day_sum_heartBeat6 = 0;
                long day_sum_heartBeat7 = 0;
                long day_sum_heartBeat8 = 0;
                String day_str = "yyyyMMdd";
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                for (UserFatData sDetail : fatDetails) {
                    sum_heartBeat += sDetail.getBmi_value();
                    sum_heartBeat2 += sDetail.getFat_value();
                    sum_heartBeat3 += sDetail.getCalorie_value();
                    sum_heartBeat4 += sDetail.getMoisture_value();
                    sum_heartBeat5 += sDetail.getMuscle_value();
                    sum_heartBeat6 += sDetail.getVisceral_fat_value();
                    sum_heartBeat7 += sDetail.getBone_value();
                    sum_heartBeat8 += sDetail.getWeight_value();
                    count++;//条数
                    gc.setTimeInMillis(sDetail.getMeasured_at() * 1000);
                    String day_tmp = format.format(gc.getTime());
//                    day_str当前扫描的一天
                    if (day_count == 0 || day_str.equals(day_tmp)) {
                        day_sum_heartBeat += sDetail.getBmi_value();
                        day_sum_heartBeat2 += sDetail.getFat_value();
                        day_sum_heartBeat3 += sDetail.getCalorie_value();
                        day_sum_heartBeat4 += sDetail.getMoisture_value();
                        day_sum_heartBeat5 += sDetail.getMuscle_value();
                        day_sum_heartBeat6 += sDetail.getVisceral_fat_value();
                        day_sum_heartBeat7 += sDetail.getBone_value();
                        day_sum_heartBeat8 += sDetail.getWeight_value();
                        day_count++;//天条数
                        day_str = day_tmp;
                        continue;
                    }

                    Map<String, Object> summary = new HashMap<>();
                    gc.setTime(format.parse(day_str));
                    summary.put("measured_at", gc.getTimeInMillis() / 1000);
                    summary.put("average_bmi", (int) (day_sum_heartBeat / day_count));
                    summary.put("average_fat", (int) (day_sum_heartBeat2 / day_count));
                    summary.put("average_cal", (int) (day_sum_heartBeat3 / day_count));
                    summary.put("average_moi", (int) (day_sum_heartBeat4 / day_count));
                    summary.put("average_mus", (int) (day_sum_heartBeat5 / day_count));
                    summary.put("average_vft", (int) (day_sum_heartBeat6 / day_count));
                    summary.put("average_bne", (int) (day_sum_heartBeat7 / day_count));
                    summary.put("average_weight", (int) (day_sum_heartBeat8 / day_count));
                    f_summaries.add(summary);

                    day_sum_heartBeat = sDetail.getBmi_value();
                    day_sum_heartBeat2 = sDetail.getFat_value();
                    day_sum_heartBeat3 = sDetail.getCalorie_value();
                    day_sum_heartBeat4 = sDetail.getMoisture_value();
                    day_sum_heartBeat5 = sDetail.getMuscle_value();
                    day_sum_heartBeat6 = sDetail.getVisceral_fat_value();
                    day_sum_heartBeat7 = sDetail.getBone_value();
                    day_sum_heartBeat8 = sDetail.getWeight_value();
                    day_count = 1;//
                    day_str = day_tmp;
                }
                Map<String, Object> summary = new HashMap<>();
                gc.setTime(format.parse(day_str));
                summary.put("measured_at", gc.getTimeInMillis() / 1000);
                summary.put("average_bmi", (int) (day_sum_heartBeat / day_count));
                summary.put("average_fat", (int) (day_sum_heartBeat2 / day_count));
                summary.put("average_cal", (int) (day_sum_heartBeat3 / day_count));
                summary.put("average_moi", (int) (day_sum_heartBeat4 / day_count));
                summary.put("average_mus", (int) (day_sum_heartBeat5 / day_count));
                summary.put("average_vft", (int) (day_sum_heartBeat6 / day_count));
                summary.put("average_bne", (int) (day_sum_heartBeat7 / day_count));
                summary.put("average_weight", (int) (day_sum_heartBeat8 / day_count));
                f_summaries.add(summary);

                Integer average_heart_rate = (int) (sum_heartBeat / count);
                Integer average_heart_rate2 = (int) (sum_heartBeat2 / count);
                Integer average_heart_rate3 = (int) (sum_heartBeat3 / count);
                Integer average_heart_rate4 = (int) (sum_heartBeat4 / count);
                Integer average_heart_rate5 = (int) (sum_heartBeat5 / count);
                Integer average_heart_rate6 = (int) (sum_heartBeat6 / count);
                Integer average_heart_rate7 = (int) (sum_heartBeat7 / count);
                Integer average_heart_rate8 = (int) (sum_heartBeat8 / count);
                JsonObject json = new JsonObject();
                json.add("average_bmi", gson.toJsonTree(average_heart_rate));
                json.add("average_fat", gson.toJsonTree(average_heart_rate2));
                json.add("average_cal", gson.toJsonTree(average_heart_rate3));
                json.add("average_moi", gson.toJsonTree(average_heart_rate4));
                json.add("average_mus", gson.toJsonTree(average_heart_rate5));
                json.add("average_vft", gson.toJsonTree(average_heart_rate6));
                json.add("average_bne", gson.toJsonTree(average_heart_rate7));
                json.add("average_weight", gson.toJsonTree(average_heart_rate8));
                json.add("f_summaries", gson.toJsonTree(f_summaries));
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("summaries", json);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
            }
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.7 GET /api/user/wristbands
    @RequestMapping(value = "/api/user/wristbands", method = RequestMethod.GET)
    public void GetWristbandsInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            String dataType = req.getParameter("data_type");
            String beginDate = req.getParameter("begin_date");
            String endDate = req.getParameter("end_date");
            if (dataType == null || beginDate == null || endDate == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int userId = 0;
            if (StringUtils.isBlank(phone)) {
                userId = user.getUser_id();
            } else {
                User associatedUser = userService.selUserByPhone(phone);
                if (associatedUser == null) {
                    setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                    return;
                }
                userId = associatedUser.getUser_id();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", Long.valueOf(userId));
            map.put("begin_date", beginDate);
            map.put("end_date", endDate);
            List<UserWristbandData> wristbandDetail = userHealthService.selWristbandDetail(map);
            Map<String, Object> resultMap = new HashMap<>(1);
            if (dataType.equals("2")) {
                if (wristbandDetail == null || wristbandDetail.size() <= 0) {
                    resultMap.put("details", new Object());
                    content.setSuccess_message(resultMap);
                    setResponseContent(res, content);
                    return;
                }
                resultMap.put("details", wristbandDetail);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            } else if (dataType.equals("1")) {
                Map<String, Object> wristbandSummary = userHealthService.selWristbandSummary(map);
                if (wristbandSummary == null || wristbandSummary.size() <= 0) {
                    resultMap.put("summaries", new Object());
                    content.setSuccess_message(resultMap);
                    setResponseContent(res, content);
                    return;
                }
                wristbandSummary.put("e_summaries", wristbandDetail);
                resultMap.put("summaries", wristbandSummary);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
            }
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.8	GET /api/user/sphygmomanometers
    @RequestMapping(value = "/api/user/sphygmomanometers", method = RequestMethod.GET)
    public void GetSphygInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            String dataType = req.getParameter("data_type");
            String beginDate = req.getParameter("begin_date");
            String endDate = req.getParameter("end_date");
            if (dataType == null || beginDate == null || endDate == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int userId = 0;
            if (StringUtils.isBlank(phone)) {
                userId = user.getUser_id();
            } else {
                User associatedUser = userService.selUserByPhone(phone);
                if (associatedUser == null) {
                    setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                    return;
                }
                userId = associatedUser.getUser_id();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", Long.valueOf(userId));
            map.put("begin_date", Long.valueOf(beginDate));
            map.put("end_date", Long.valueOf(endDate));
            List<UserSphygmomanometerData> sDetails = userHealthService.selSDetail(map);
            if (dataType.equals("2")) {
                List<Map> details = new ArrayList<>();
                for (UserSphygmomanometerData sDetail : sDetails) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("measured_at", sDetail.getMeasured_at());
                    detail.put("systolic_pressure", sDetail.getSystolic_pressure());
                    detail.put("diastolic_pressure", sDetail.getDiastolic_pressure());
                    detail.put("heart_rate", sDetail.getHeart_rate());
                    details.add(detail);
                }
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("details", details);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            } else if (dataType.equals("1")) {
                if (sDetails == null || sDetails.size() <= 0) {
                    Map<String, Object> resultMap = new HashMap<>(1);
                    resultMap.put("summaries", new Object());
                    content.setSuccess_message(resultMap);
                    setResponseContent(res, content);
                    return;
                }
                List<Map> e_summaries = new ArrayList();
                long count = 0;
                long sum_heartBeat = 0;
                long sum_heartBeat2 = 0;
                long sum_heartBeat3 = 0;
                long day_count = 0;
                long day_sum_heartBeat = 0;
                long day_sum_heartBeat2 = 0;
                long day_sum_heartBeat3 = 0;
                String day_str = "yyyyMMdd";
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                for (UserSphygmomanometerData sDetail : sDetails) {
                    sum_heartBeat += sDetail.getDiastolic_pressure();
                    sum_heartBeat2 += sDetail.getSystolic_pressure();
                    sum_heartBeat3 += sDetail.getHeart_rate();
                    count++;//条数
                    gc.setTimeInMillis(sDetail.getMeasured_at() * 1000);
                    String day_tmp = format.format(gc.getTime());
//                    day_str当前扫描的一天
                    if (day_count == 0 || day_str.equals(day_tmp)) {
                        day_sum_heartBeat += sDetail.getDiastolic_pressure();
                        day_sum_heartBeat2 += sDetail.getSystolic_pressure();
                        day_sum_heartBeat3 += sDetail.getHeart_rate();
                        day_count++;//天条数
                        day_str = day_tmp;
                        continue;
                    }
                    Map<String, Object> summary = new HashMap<>();
                    gc.setTime(format.parse(day_str));
                    summary.put("measured_at", gc.getTimeInMillis() / 1000);
                    summary.put("average_dp", (int) (day_sum_heartBeat / day_count));
                    summary.put("average_sp", (int) (day_sum_heartBeat2 / day_count));
                    summary.put("average_heart_rate", (int) (day_sum_heartBeat3 / day_count));
                    e_summaries.add(summary);

                    day_sum_heartBeat = sDetail.getDiastolic_pressure();
                    day_sum_heartBeat2 = sDetail.getSystolic_pressure();
                    day_sum_heartBeat3 = sDetail.getHeart_rate();
                    day_count = 1;//下一条数据
                    day_str = day_tmp;
                }
                Map<String, Object> summary = new HashMap<>();
                gc.setTime(format.parse(day_str));
                summary.put("measured_at", gc.getTimeInMillis() / 1000);
                summary.put("average_dp", (int) (day_sum_heartBeat / day_count));
                summary.put("average_sp", (int) (day_sum_heartBeat2 / day_count));
                summary.put("average_heart_rate", (int) (day_sum_heartBeat3 / day_count));
                e_summaries.add(summary);

                Integer average_heart_rate = (int) (sum_heartBeat / count);
                Integer average_heart_rate2 = (int) (sum_heartBeat2 / count);
                Integer average_heart_rate3 = (int) (sum_heartBeat3 / count);
                JsonObject json = new JsonObject();
                json.add("average_sp", gson.toJsonTree(average_heart_rate2));
                json.add("average_dp", gson.toJsonTree(average_heart_rate));
                json.add("average_heart_rate", gson.toJsonTree(average_heart_rate3));
                json.add("s_summaries", gson.toJsonTree(e_summaries));
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("summaries", json);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
            }
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.9	GET /api/user/glucosemeters
    @RequestMapping(value = "/api/user/glucosemeters", method = RequestMethod.GET)
    public void GetGlucosemetersInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            String dataType = req.getParameter("data_type");
            String beginDate = req.getParameter("begin_date");
            String endDate = req.getParameter("end_date");
            if (dataType == null || beginDate == null || endDate == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int userId = 0;
            if (phone == null) {
                userId = user.getUser_id();
            } else {
                User associatedUser = userService.selUserByPhone(phone);
                if (associatedUser == null) {
                    setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                    return;
                }
                userId = associatedUser.getUser_id();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", Long.valueOf(userId));
            map.put("begin_date", Long.valueOf(beginDate));
            map.put("end_date", Long.valueOf(endDate));
            List<UserGlucosemeterData> glucosemeters = userHealthService.selGlucosemeterDetail(map);

            if (dataType.equals("2")) {
                List<Map> details = new ArrayList<>();
                for (UserGlucosemeterData glucosemeter : glucosemeters) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("measured_at", glucosemeter.getMeasured_at());
                    detail.put("glucosemeter_value", glucosemeter.getGlucosemeter_value());
                    detail.put("period", glucosemeter.getPeriod());
                    details.add(detail);
                }
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("details", details);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            }

            if (glucosemeters == null || glucosemeters.size() <= 0) {
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("summaries", new Object());
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            }

            /*计算概要*/
            List<Map> g_summaries = new ArrayList();
            long count = 0;
            long sum_glucosemeter = 0;
            long day_count = 0;
            long day_sum_glucosemeter = 0;
            String day_str = "yyyyMMdd";
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            for (UserGlucosemeterData glucosemeter : glucosemeters) {
                sum_glucosemeter += glucosemeter.getGlucosemeter_value();
                count++;

                gc.setTimeInMillis(glucosemeter.getMeasured_at() * 1000);
                String day_tmp = format.format(gc.getTime());
                if (day_count == 0 || day_str.equals(day_tmp)) {
                    day_sum_glucosemeter += glucosemeter.getGlucosemeter_value();
                    day_count++;
                    day_str = day_tmp;
                    continue;
                }

                Map<String, Object> summary = new HashMap<>();
                gc.setTime(format.parse(day_str));
                summary.put("measured_at", gc.getTimeInMillis() / 1000);
                summary.put("average_gv", (int) (day_sum_glucosemeter / day_count));
                g_summaries.add(summary);

                day_sum_glucosemeter = glucosemeter.getGlucosemeter_value();
                day_count = 1;
                day_str = day_tmp;
            }

            Map<String, Object> summary = new HashMap<>();
            gc.setTime(format.parse(day_str));
            summary.put("measured_at", gc.getTimeInMillis() / 1000);
            summary.put("average_gv", (int) (day_sum_glucosemeter / day_count));
            g_summaries.add(summary);

            Integer average_glucosemeter = (int) (sum_glucosemeter / count);
            JsonObject json = new JsonObject();
            json.add("average_gv", gson.toJsonTree(average_glucosemeter));
            json.add("g_summaries", gson.toJsonTree(g_summaries));
            Map<String, Object> resultMap = new HashMap<>(1);
            resultMap.put("summaries", json);
            content.setSuccess_message(resultMap);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.10 GET /api/user/oximeters
    @RequestMapping(value = "/api/user/oximeters", method = RequestMethod.GET)
    public void GetOximetersInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            String dataType = req.getParameter("data_type");
            String beginDate = req.getParameter("begin_date");
            String endDate = req.getParameter("end_date");
            if (dataType == null || beginDate == null || endDate == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int userId = 0;
            if (phone == null) {
                userId = user.getUser_id();
            } else {
                User associatedUser = userService.selUserByPhone(phone);
                if (associatedUser == null) {
                    setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                    return;
                }
                userId = associatedUser.getUser_id();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", Long.valueOf(userId));
            map.put("begin_date", Long.valueOf(beginDate));
            map.put("end_date", Long.valueOf(endDate));
            List<UserOximeterData> oximeters = userHealthService.selOximeterDetail(map);

            if (dataType.equals("2")) {
                List<Map> details = new ArrayList<>();
                for (UserOximeterData oximeter : oximeters) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("measured_at", oximeter.getMeasured_at());
                    detail.put("oximeter_value", oximeter.getOximeter_value());
                    details.add(detail);
                }
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("details", details);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            }

            if (oximeters == null || oximeters.size() <= 0) {
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("summaries", new Object());
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            }

            /*计算概要*/
            List<Map> o_summaries = new ArrayList();
            long count = 0;
            long sum_oximeter = 0;
            long day_count = 0;
            long day_sum_oximeter = 0;
            String day_str = "yyyyMMdd";
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            for (UserOximeterData oximeter : oximeters) {
                sum_oximeter += oximeter.getOximeter_value();
                count++;

                gc.setTimeInMillis(oximeter.getMeasured_at() * 1000);
                String day_tmp = format.format(gc.getTime());
                if (day_count == 0 || day_str.equals(day_tmp)) {
                    day_sum_oximeter += oximeter.getOximeter_value();
                    day_count++;
                    day_str = day_tmp;
                    continue;
                }

                Map<String, Object> summary = new HashMap<>();
                gc.setTime(format.parse(day_str));
                summary.put("measured_at", gc.getTimeInMillis() / 1000);
                summary.put("average_ov", (int) (day_sum_oximeter / day_count));
                o_summaries.add(summary);

                day_sum_oximeter = oximeter.getOximeter_value();
                day_count = 1;
                day_str = day_tmp;
            }
            Map<String, Object> summary = new HashMap<>();
            gc.setTime(format.parse(day_str));
            summary.put("measured_at", gc.getTimeInMillis() / 1000);
            summary.put("average_ov", (int) (day_sum_oximeter / day_count));
            o_summaries.add(summary);

            Integer average_oximeter = (int) (sum_oximeter / count);
            JsonObject json = new JsonObject();
            json.add("average_ov", gson.toJsonTree(average_oximeter));
            json.add("o_summaries", gson.toJsonTree(o_summaries));

            Map<String, Object> resultMap = new HashMap<>(1);
            resultMap.put("summaries", json);
            content.setSuccess_message(resultMap);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.11 GET /api/user/thermometers
    @RequestMapping(value = "/api/user/thermometers", method = RequestMethod.GET)
    public void GetThermometersInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            String dataType = req.getParameter("data_type");
            String beginDate = req.getParameter("begin_date");
            String endDate = req.getParameter("end_date");
            if (dataType == null || beginDate == null || endDate == null) {
                setParamWarnRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int userId = 0;
            if (phone == null) {
                userId = user.getUser_id();
            } else {
                User associatedUser = userService.selUserByPhone(phone);
                if (associatedUser == null) {
                    setParamWarnRes(res, "用户不存在", ErrorCode.INVALID_USER);
                    return;
                }
                userId = associatedUser.getUser_id();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", Long.valueOf(userId));
            map.put("begin_date", Long.valueOf(beginDate));
            map.put("end_date", Long.valueOf(endDate));

            List<UserThermometersData> thermometers = userHealthService.selThermometerDetail(map);

            if (dataType.equals("2")) {
                List<Map> details = new ArrayList<>();
                for (UserThermometersData thermometer : thermometers) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("measured_at", thermometer.getMeasured_at());
                    detail.put("thermometer_value", thermometer.getThermometer_value());
                    details.add(detail);
                }
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("details", details);
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            }

            if (thermometers == null || thermometers.size() <= 0) {
                Map<String, Object> resultMap = new HashMap<>(1);
                resultMap.put("summaries", new Object());
                content.setSuccess_message(resultMap);
                setResponseContent(res, content);
                return;
            }

            /*计算概要*/
            List<Map> t_summaries = new ArrayList();
            long count = 0;
            long sum_thermometer = 0;
            long day_count = 0;
            long day_sum_thermometer = 0;
            String day_str = "yyyyMMdd";
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            for (UserThermometersData thermometer : thermometers) {
                sum_thermometer += thermometer.getThermometer_value();
                count++;

                gc.setTimeInMillis(thermometer.getMeasured_at() * 1000);
                String day_tmp = format.format(gc.getTime());
                if (day_count == 0 || day_str.equals(day_tmp)) {
                    day_sum_thermometer += thermometer.getThermometer_value();
                    day_count++;
                    day_str = day_tmp;
                    continue;
                }

                Map<String, Object> summary = new HashMap<>();
                gc.setTime(format.parse(day_str));
                summary.put("measured_at", gc.getTimeInMillis() / 1000);
                summary.put("average_tv", (int) (day_sum_thermometer / day_count));
                t_summaries.add(summary);

                day_sum_thermometer = thermometer.getThermometer_value();
                day_count = 1;
                day_str = day_tmp;
            }
            Map<String, Object> summary = new HashMap<>();
            gc.setTime(format.parse(day_str));
            summary.put("measured_at", gc.getTimeInMillis() / 1000);
            summary.put("average_tv", (int) (day_sum_thermometer / day_count));
            t_summaries.add(summary);

            Integer average_heart_beat = (int) (sum_thermometer / count);
            JsonObject json = new JsonObject();
            json.add("average_tv", gson.toJsonTree(average_heart_beat));
            json.add("t_summaries", gson.toJsonTree(t_summaries));
            Map<String, Object> resultMap = new HashMap<>(1);
            resultMap.put("summaries", json);
            content.setSuccess_message(resultMap);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.6.12 POST /api/terminals
    @RequestMapping(value = "/api/terminals", method = RequestMethod.POST)
    public void PostTerminalsInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"device_type_id", "manufactory_code", "terminal_catagory_code", "type"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Integer deviceTypeId = params.get("device_type_id").getAsInt();
            String manufactoryCode = params.get("manufactory_code").getAsString();
            String terminalCatagoryCode = params.get("terminal_catagory_code").getAsString();
            Integer type = params.get("type").getAsInt();

            Date date = new Date();
            /*查厂商表(manufactory)*/
            Map<String, Object> map = new HashMap<>();
            map.put("manufactory_code", manufactoryCode);
            Manufactory factory = deviceService.selManufactoryByCode(map);
            if (factory == null || factory.getStatus() != BeanProperty.UserStatus.NORMAL) {
                setParamWarnRes(res, "厂商不存在", ErrorCode.INVALID_FACTORY);
                return;
            }

            /*查设备类型表(device_type)*/
            map.clear();
            map.put("device_type_id", deviceTypeId);
            DeviceType deviceType = deviceService.selDeviceTypeById(map);
            if (deviceType == null || deviceType.getStatus() != BeanProperty.UserStatus.NORMAL) {
                setParamWarnRes(res, "相关设备类型不存在或状态异常.", ErrorCode.INVALID_DEVICE);
                return;
            }

            /*查产品类型表(product_type)*/
            map.clear();
            map.put("product_type_id", deviceType.getProduct_type_id());
            ProductType productType = deviceService.selProductTypeById(map);
            if (productType == null || productType.getStatus() != BeanProperty.UserStatus.NORMAL) {
                setParamWarnRes(res, "相关产品类型不存在或状态异常.", ErrorCode.INVALID_PRODUCT);
                return;
            }

            /*查终端类型表(terminal_catagory)*/
            map.clear();
            map.put("terminal_catagory_code", terminalCatagoryCode);
            TerminalCatagory terminalCatagory = deviceService.selTerminalCatagoryByCode(map);
            if (terminalCatagory == null || terminalCatagory.getStatus() != BeanProperty.UserStatus.NORMAL) {
                setParamWarnRes(res, "相关终端型号不存在或状态异常.", ErrorCode.INVALID_TERMINALCATAGORY);
                return;
            }

            /*查终端表*/
            map.clear();
            String macAddress = "";
            String uuid = "";
            if (type == 1 || type == 2) { /*PC || Android*/
                macAddress = params.get("mac_address").getAsString();
                map.put("mac_address", macAddress);
            } else if (type == 3) { /*IOS*/
                uuid = params.get("uuid").getAsString();
                map.put("uuid", uuid);
            }
            Terminal terminal = deviceService.selTerminalByMap(map);
            if (terminal == null) {
                /*插入终端表*/
                terminal = new Terminal();
                terminal.setTerminal_id(0);
                terminal.setName(terminalCatagory.getName() + macAddress);
                terminal.setMac_address(macAddress);
                terminal.setUuid(uuid);
                terminal.setManufactory_id(factory.getManufactory_id());
                terminal.setProduct_type_id(deviceType.getProduct_type_id());
                terminal.setDevice_type_id(deviceType.getDevice_type_id());
                terminal.setTerminal_catagory_id(terminalCatagory.getTerminal_catagory_id());
                terminal.setStatus(BeanProperty.UserStatus.NORMAL);
                terminal.setActivated_at(date.getTime() / 1000);
                deviceService.insertTerminal(terminal);
            } else {
                boolean change = false;
                if (terminal.getDevice_type_id() != deviceType.getDevice_type_id()) {
                    change = true;
                    terminal.setDevice_type_id(deviceType.getDevice_type_id());
                }
                if (terminal.getManufactory_id() != factory.getManufactory_id()) {
                    change = true;
                    terminal.setManufactory_id(factory.getManufactory_id());
                }
                if (terminal.getTerminal_catagory_id() != terminalCatagory.getTerminal_catagory_id()) {
                    change = true;
                    terminal.setTerminal_catagory_id(terminalCatagory.getTerminal_catagory_id());
                }
                if (change) {
                    deviceService.updateTerminal(terminal);
                }
            }

            /*查终端用户关联表*/
            map.clear();
            map.put("terminal_id", terminal.getTerminal_id());
            map.put("user_id", user.getUser_id());
            UserTerminal userTerminal = userService.selUserTerminalByUserAndTer(map);
            if (userTerminal == null) {
                /*插入*/
                userTerminal = new UserTerminal();
                userTerminal.setUser_device_id(0);
                userTerminal.setTerminal_id(terminal.getTerminal_id());
                userTerminal.setUser_id(user.getUser_id());
                userTerminal.setManufactory_id(factory.getManufactory_id());
                userTerminal.setDevice_type_id(deviceType.getDevice_type_id());
                userTerminal.setTerminal_catagory_id(terminalCatagory.getTerminal_catagory_id());
                userTerminal.setCount(1);
                userTerminal.setCreated_at(date.getTime() / 1000);
                userTerminal.setUpdated_at(date.getTime() / 1000);
                userService.inUserTerminal(userTerminal);
            } else {
                /*更新*/
                userTerminal.setUpdated_at(date.getTime() / 1000);
                userService.upUserTerminalUpdateTm(userTerminal);
            }

            map.clear();
            map.put("manufactory_id", factory.getManufactory_id());
            map.put("manufactory_name", factory.getFull_name());
            map.put("product_type_id", productType.getProduct_type_id());
            map.put("product_type_name", productType.getName());
            map.put("device_type_id", deviceType.getDevice_type_id());
            map.put("device_type_name", deviceType.getName());
            map.put("terminal_catagory_id", terminalCatagory.getTerminal_catagory_id());
            map.put("terminal_catagory_name", terminalCatagory.getName());
            map.put("terminal_id", terminal.getTerminal_id());
            map.put("terminal_name", terminal.getName());
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.6.15 GET /api/terminal_catagories*/
    @RequestMapping(value = "/api/terminal_catagories", method = RequestMethod.GET)
    public void GetTerminalCategory(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String device_type_id_str = req.getParameter("device_type_id");
            if (StringUtils.isBlank(device_type_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<Map<String, Object>> list = deviceService.selTerminalCatagoryByDeviceTypeId(Integer.valueOf(device_type_id_str));
            for (Map<String, Object> map : list) {
                int terminal_catagory_id = (int) map.get("terminal_catagory_id");
                List<TerminalCatagoryPattern> pattern_list = deviceService.selPatternByCategoryId(terminal_catagory_id);
                List<String> terminal_catagory_patterns = new ArrayList<>();
                for (TerminalCatagoryPattern pattern : pattern_list) {
                    terminal_catagory_patterns.add(pattern.getPattern());
                }
                map.put("terminal_catagory_patterns", terminal_catagory_patterns);
            }
            Map<String, Object> resultMap = new HashMap<>(1);
            resultMap.put("catagories", list);

            content.setSuccess_message(resultMap);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.6.16 GET /api/terminal_catagory_patterns*/
    @RequestMapping(value = "/api/terminal_catagory_patterns", method = RequestMethod.GET)
    public void GetTerminalCategoryPatterns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String terminal_catagory_id_str = req.getParameter("terminal_catagory_id");
            if (StringUtils.isBlank(terminal_catagory_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int terminal_catagory_id = Integer.valueOf(terminal_catagory_id_str);
            List<TerminalCatagoryPattern> pattern_list = deviceService.selPatternByCategoryId(terminal_catagory_id);
            JsonArray patterns = new JsonArray();
            for (TerminalCatagoryPattern pattern : pattern_list) {
                patterns.add(gson.toJsonTree(pattern.getPattern()));
            }
            JsonObject json = new JsonObject();
            json.add("patterns", patterns);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    public List<Consumption> getConsumptionToday(int user_id) throws Exception {
        Date date = new Date();
        Date begin_date = format.parse(format.format(date));
        long begin_time = begin_date.getTime() / 1000;
        long end_time = begin_time + 86399;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("happened_from", begin_time);
        map.put("happened_to", end_time);
        List<Consumption> consumption = userService.selConsumption(map);
        return consumption;
    }

    public long getConsumptionTodayCount(int user_id, int operation_type) throws Exception {
        Date date = new Date();
        Date begin_date = format.parse(format.format(date));
        long begin_time = begin_date.getTime() / 1000;
        long end_time = begin_time + 86399;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("happened_from", begin_time);
        map.put("happened_to", end_time);
        if (operation_type != -1) {
            map.put("operation_type", operation_type);
        }
        Map<String, Object> count = userService.selConsumptionCnt(map);
        return (long) count.get("count");
    }

    public void inConsumptionByRule(User user, int operation_type) {
        ConsumptionRule con_rule = userService.selConsumptionRule(operation_type);

        Consumption con = new Consumption();
        con.setComsumption_id(0);
        con.setHappened_at((new Date()).getTime() / 1000);
        con.setUser_id(user.getUser_id());
        con.setPoints(con_rule.getPoints());
        con.setNew_points(user.getPoints() + con_rule.getPoints());
        con.setOperation_type(operation_type);
        con.setDescription(con_rule.getDescription());
        userService.inConsumption(con);

        user.setPoints(user.getPoints() + con_rule.getPoints());
        userService.upUserPoints(user);
    }

    public void upUserMeasureTime(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user.getUser_id());
        map.put("last_measured_at", System.currentTimeMillis() / 1000);
        userService.upUserMeasuredTime(map);
    }
}
