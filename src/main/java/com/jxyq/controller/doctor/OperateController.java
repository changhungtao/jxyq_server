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
import com.jxyq.commons.util.ExportExcelUtil;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.*;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.ConstantService;
import com.jxyq.service.inf.DoctorService;
import com.jxyq.service.inf.PatientService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OperateController extends BaseInterface {
    @Autowired
    private PatientService patientService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private DoctorService doctorService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

    /*3.4.2.1 POST /doctors/patients/files*/
    @RequestMapping(value = "/doctors/patients/files", method = RequestMethod.POST)
    public void PostDocPatFiles(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor)subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "age_from", "age_to", "file_type",
                    "measured_from", "measured_to", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("doctor_id", doctor.getDoctor_id());
            if (params.has("phone")) {
                map.put("phone", params.get("phone").getAsString());
            }
            if (params.has("name")) {
                map.put("name", params.get("name").getAsString());
            }
            if (params.has("age_from")) {
                map.put("age_from", params.get("age_from").getAsInt());
            }
            if (params.has("age_to")) {
                map.put("age_to", params.get("age_to").getAsInt());
            }
            if (params.has("gender")) {
                map.put("gender", params.get("gender").getAsInt());
            }
            if (params.has("measured_from")) {
                map.put("measured_from", params.get("measured_from").getAsLong());
            }
            if (params.has("measured_to")) {
                map.put("", params.get("measured_to").getAsLong());
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

            JsonArray file_type_list = params.getAsJsonArray("file_type");
            int file_type = file_type_list.get(0).getAsInt();
            ConstantDescription constant = constantService.selDescriptionByConstant("file_type", file_type);
            List<Map<String, Object>> map_list = null;
            switch (constant.getDescription()) {
                case "wristband":
                    map_list = patientService.qryWristbandPatientByPage(map);
                    break;
                case "sphygmomanometer":
                    map_list = patientService.qrySphyPatientByPage(map);
                    break;
                case "oximeter":
                    map_list = patientService.qryOxiPatientByPage(map);
                    break;
                case "glucosemeter":
                    map_list = patientService.qryGluPatientByPage(map);
                    break;
                case "thermometer":
                    map_list = patientService.qryThePatientByPage(map);
                    break;
                case "fat":
                    map_list = patientService.qryFatPatientByPage(map);
                    break;
                case "other":
                    map_list = patientService.qryOtherPatientByPage(map);
                    break;
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("files", gson.toJsonTree(map_list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.2 GET /doctors/patients/{pid}/files*/
    @RequestMapping(value = "/doctors/patients/{pid}/files", method = RequestMethod.GET)
    public void GetPatFiles(@PathVariable("pid") int pid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String file_type_str = req.getParameter("file_type");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date_str");

            if (StringUtils.isBlank(file_type_str) || StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Patient patient = patientService.selPatientById(pid);

            int file_type = Integer.valueOf(file_type_str);
            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            Map<String, Object> map = new HashMap<>();
            map.put("patient_id", pid);

            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str) && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            ConstantDescription constant = constantService.selDescriptionByConstant("file_type", file_type);
            List<Map<String, Object>> map_list = null;
            switch (constant.getDescription()) {
                case "wristband":
                    map_list = patientService.qryWristbandFile4DocByPage(map);
                    break;
                case "sphygmomanometer":
                    map_list = patientService.qrySphyFile4DocByPage(map);
                    break;
                case "oximeter":
                    map_list = patientService.qryOxiFile4DocByPage(map);
                    break;
                case "glucosemeter":
                    map_list = patientService.qryGluFile4DocByPage(map);
                    break;
                case "thermometer":
                    map_list = patientService.qryTheFile4DocByPage(map);
                    break;
                case "fat":
                    map_list = patientService.qryFatFile4DocByPage(map);
                    break;
                case "other":
                    map_list = patientService.qryOtherFile4DocByPage(map);
                    break;
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date birthday = format.parse(patient.getBirthday());
            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("patient_id", gson.toJsonTree(patient.getPatient_id()));
            json.add("patient_name", gson.toJsonTree(patient.getName()));
            json.add("patient_age", gson.toJsonTree(CommonUtil.calculateAge(birthday, new Date())));
            json.add("patient_gender", gson.toJsonTree(patient.getGender()));
            json.add("patient_phone", gson.toJsonTree(patient.getPhone()));
            json.add("file_type", gson.toJsonTree(file_type));
            json.add("files", gson.toJsonTree(map_list));

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.3 POST /doctors/patients/export*/
    @RequestMapping(value = "/doctors/patients/export", method = RequestMethod.POST)
    public void PostPatExport(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"file_type", "patient_id"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int pid = params.get("patient_id").getAsInt();
            int file_type = params.get("file_type").getAsInt();
            Patient patient = patientService.selPatientById(pid);

            Map<String, Object> map = new HashMap<>();
            map.put("patient_id", pid);

            ConstantDescription constant = constantService.selDescriptionByConstant("file_type", file_type);
            List<Map<String, Object>> map_list = null;
            Map<String, String> header = new HashMap<>();
            switch (constant.getDescription()) {
                case "wristband":
                    map_list = patientService.qryWristbandFile4Doc(map);
                    header.put("id", "档案编号");
                    header.put("measured_at", "测量时间");
                    header.put("step_count", "步数");
                    header.put("distance", "公里数");
                    header.put("calories", "消耗能量");
                    header.put("walk_count", "行走步数");
                    header.put("walk_distance", "行走公里数");
                    header.put("walk_calories", "行走消耗能量");
                    header.put("run_count", "跑步步数");
                    header.put("run_distance", "跑步公里数");
                    header.put("run_calories", "跑步消耗能量");
                    header.put("deep_duration", "深睡时间(秒)");
                    header.put("shallow_duration", "浅睡时间(秒)");
                    header.put("heart_rate", "心率(bpm)");
                    break;
                case "sphygmomanometer":
                    map_list = patientService.qrySphyFile4Doc(map);
                    header.put("id", "档案编号");
                    header.put("measured_at", "测量时间");
                    header.put("systolic_pressure", "收缩压");
                    header.put("diastolic_pressure", "舒张压");
                    header.put("heart_rate", "心率");
                    break;
                case "oximeter":
                    map_list = patientService.qryOxiFile4Doc(map);
                    header.put("id", "档案编号");
                    header.put("measured_at", "测量时间");
                    header.put("oximeter_value", "血氧值");
                    break;
                case "glucosemeter":
                    map_list = patientService.qryGluFile4Doc(map);
                    header.put("id", "档案编号");
                    header.put("measured_at", "测量时间");
                    header.put("period", "测量时段");
                    header.put("glucosemeter_value", "血糖值");
                    break;
                case "thermometer":
                    map_list = patientService.qryTheFile4Doc(map);
                    header.put("id", "档案编号");
                    header.put("measured_at", "测量时间");
                    header.put("thermometer_value", "体温值");
                    break;
                case "fat":
                    map_list = patientService.qryFatFile4Doc(map);
                    header.put("id", "档案编号");
                    header.put("measured_at", "测量时间");
                    header.put("bmi_value", "BMI");
                    header.put("weight_value", "体重");
                    header.put("fat_value", "脂肪含量");
                    header.put("calorie_value", "卡路里");
                    header.put("moisture_value", "水分含量");
                    header.put("muscle_value", "肌肉含量");
                    header.put("visceral_fat_value", "内脏脂肪含量");
                    header.put("bone_value", "骨骼");
                    break;
            }
            String fileName = patient.getPatient_id() + "_" + format.format(new Date()) + ".xls";
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=" + fileName);
            HSSFWorkbook workbook = ExportExcelUtil.exportExcel(map_list, header, patient.getName() + "的历史档案");
            OutputStream outputStream = res.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.4 GET /doctors/patients/{pid}/wristbands/{wid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/wristbands/{wid}", method = RequestMethod.GET)
    public void GetPatWristbands(@PathVariable("pid") int pid,
                                 @PathVariable("wid") long wid,
                                 HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatWristbandFile wristband = patientService.selWristbandFile(wid);

            JsonObject json = gson.toJsonTree(wristband).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.5 PUT /doctors/patients/{pid}/wristbands/{wid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/wristbands/{wid}", method = RequestMethod.PUT)
    public void PutPatWristbands(@PathVariable("pid") int pid,
                                 @PathVariable("wid") long wid,
                                 HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"step_count", "distance", "calories", "walk_count", "walk_distance",
                    "walk_calories", "run_count", "run_distance", "run_calories", "deep_duration",
                    "shallow_duration", "heart_rate", "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatWristbandFile wristband = gson.fromJson(params, PatWristbandFile.class);
            wristband.setWristband_file_id(wid);
            wristband.setPatient_id(pid);
            wristband.setUser_symptom(params.get("symptom").getAsString());
            wristband.setProposal(params.get("proposal").getAsString());
            patientService.upWristbandFile(wristband);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.6 GET /doctors/patients/{pid}/sphygmomanometers/{sid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/sphygmomanometers/{sid}", method = RequestMethod.GET)
    public void GetPatSphy(@PathVariable("pid") int pid,
                           @PathVariable("sid") long sid,
                           HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatSphygmomanometerFile sphygmomanometer = patientService.selSphygmomanometerFile(sid);

            JsonObject json = gson.toJsonTree(sphygmomanometer).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.7 PUT /doctors/patients/{pid}/sphygmomanometers/{sid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/sphygmomanometers/{sid}", method = RequestMethod.PUT)
    public void PutPatSphy(@PathVariable("pid") int pid,
                           @PathVariable("sid") long sid,
                           HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"systolic_pressure", "diastolic_pressure", "heart_rate", "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatSphygmomanometerFile sphygmomanometer = gson.fromJson(params, PatSphygmomanometerFile.class);
            sphygmomanometer.setSphygmomanometer_file_id(sid);
            sphygmomanometer.setPatient_id(pid);
            sphygmomanometer.setUser_symptom(params.get("symptom").getAsString());
            sphygmomanometer.setProposal(params.get("proposal").getAsString());
            patientService.upSphygmomanometerFile(sphygmomanometer);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.8 GET /doctors/patients/{pid}/oximeters/{oid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/oximeters/{oid}", method = RequestMethod.GET)
    public void GetPatOxi(@PathVariable("pid") int pid,
                          @PathVariable("oid") long oid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatOximeterFile oximeter = patientService.selOximeterFile(oid);

            JsonObject json = gson.toJsonTree(oximeter).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.9 PUT /doctors/patients/{pid}/oximeters/{oid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/oximeters/{oid}", method = RequestMethod.PUT)
    public void PutPatOxi(@PathVariable("pid") int pid,
                          @PathVariable("oid") long oid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"oximeter_value", "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatOximeterFile oximeter = gson.fromJson(params, PatOximeterFile.class);
            oximeter.setOximeter_file_id(oid);
            oximeter.setPatient_id(pid);
            oximeter.setUser_symptom(params.get("symptom").getAsString());
            oximeter.setProposal(params.get("proposal").getAsString());
            patientService.upOximeterFile(oximeter);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.10 GET /doctors/patients/{pid}/glucosemeters/{gid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/glucosemeters/{gid}", method = RequestMethod.GET)
    public void GetPatGlu(@PathVariable("pid") int pid,
                          @PathVariable("gid") long gid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatGlucosemeterFile glucosemeter = patientService.selGlucosemeterFile(gid);

            JsonObject json = gson.toJsonTree(glucosemeter).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.11 PUT /doctors/patients/{pid}/glucosemeters/{gid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/glucosemeters/{gid}", method = RequestMethod.PUT)
    public void PutPatGlu(@PathVariable("pid") int pid,
                          @PathVariable("gid") long gid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"glucosemeter_value", "symptom", "proposal", "period"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatGlucosemeterFile glucosemeter = gson.fromJson(params, PatGlucosemeterFile.class);
            glucosemeter.setGlucosemeter_file_id(gid);
            glucosemeter.setPatient_id(pid);
            glucosemeter.setUser_symptom(params.get("symptom").getAsString());
            glucosemeter.setProposal(params.get("proposal").getAsString());
            patientService.upGlucosemeterFile(glucosemeter);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.12 GET /doctors/patients/{pid}/thermometers/{tid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/thermometers/{tid}", method = RequestMethod.GET)
    public void GetPatThe(@PathVariable("pid") int pid,
                          @PathVariable("tid") long tid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatThermometersFile thermometers = patientService.selThermometersFile(tid);

            JsonObject json = gson.toJsonTree(thermometers).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.13 PUT /doctors/patients/{pid}/thermometers/{tid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/thermometers/{tid}", method = RequestMethod.PUT)
    public void PutPatThe(@PathVariable("pid") int pid,
                          @PathVariable("tid") long tid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"thermometer_value", "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatThermometersFile thermometers = gson.fromJson(params, PatThermometersFile.class);
            thermometers.setThermometer_file_id(tid);
            thermometers.setPatient_id(pid);
            thermometers.setUser_symptom(params.get("symptom").getAsString());
            thermometers.setProposal(params.get("proposal").getAsString());
            patientService.upThermometersFile(thermometers);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.14 GET /doctors/patients/{pid}/fats/{fid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/fats/{fid}", method = RequestMethod.GET)
    public void GetPatFat(@PathVariable("pid") int pid,
                          @PathVariable("fid") long fid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatFatFile fat = patientService.selFatFile(fid);

            JsonObject json = gson.toJsonTree(fat).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.15 PUT /doctors/patients/{pid}/fats/{fid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/fats/{fid}", method = RequestMethod.PUT)
    public void PutPatFat(@PathVariable("pid") int pid,
                          @PathVariable("fid") long fid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"bmi_value", "weight_value", "fat_value", "calorie_value", "moisture_value",
                    "muscle_value", "visceral_fat_value", "bone_value", "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatFatFile fat = gson.fromJson(params, PatFatFile.class);
            fat.setFat_file_id(fid);
            fat.setPatient_id(pid);
            fat.setUser_symptom(params.get("symptom").getAsString());
            fat.setProposal(params.get("proposal").getAsString());
            patientService.upFatFile(fat);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.16 GET /doctors/patients/{pid}/others/{otid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/others/{otid}", method = RequestMethod.GET)
    public void GetPatOther(@PathVariable("pid") int pid,
                          @PathVariable("otid") long otid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            PatOtherFile other = patientService.selOtherFile(otid);

            JsonObject json = gson.toJsonTree(other).getAsJsonObject();
            json.remove("patient_id");
            json.remove("status");
            json.remove("doctor_id");
            json.add("symptom", json.get("user_symptom"));
            json.remove("user_symptom");
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.2.17 PUT /doctors/patients/{pid}/others/{otid}*/
    @RequestMapping(value = "/doctors/patients/{pid}/others/{otid}", method = RequestMethod.PUT)
    public void PutPatOthers(@PathVariable("pid") int pid,
                          @PathVariable("otid") int otid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatOtherFile other = new PatOtherFile();
            other.setOther_file_id(otid);
            other.setPatient_id(pid);
            other.setUser_symptom(params.get("symptom").getAsString());
            other.setProposal(params.get("proposal").getAsString());
            patientService.upOtherFile(other);

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