package com.jxyq.controller.doctor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.*;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.ConstantService;
import com.jxyq.service.inf.DoctorService;
import com.jxyq.service.inf.PatientService;
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

/**
 * Created by wujj-fnst on 2015/6/1.
 */

@Controller
public class PatientsController extends BaseInterface {
    @Autowired
    private PatientService patientService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private DoctorService doctorService;

    /*3.4.3.1 GET /doctors/patients*/
    @RequestMapping(value = "/doctors/patients", method = RequestMethod.GET)
    public void patients(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor)subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            String phone = req.getParameter("phone");
            String name = req.getParameter("name");
            String gender = req.getParameter("gender");
            String begin_age = req.getParameter("begin_age");
            String end_age = req.getParameter("end_age");

            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");

            Map<String, Object> map = new HashMap<>();
            map.put("doctor_id", doctor.getDoctor_id());
            if (!StringUtils.isBlank(phone)) {
                map.put("phone", phone);
            }
            if (!StringUtils.isBlank(name)) {
//                name = new String(name.getBytes("iso8859-1"), "utf-8");
                map.put("name", name);
            }
            if (!StringUtils.isBlank(gender) && !gender.equals("-1")) {
                map.put("gender", gender);
            }
            if (!StringUtils.isBlank(begin_age)) {
                if (!begin_age.equals("0")) {
                    map.put("begin_age", begin_age);
                }
            }
            if (!StringUtils.isBlank(end_age)) {
                if (!end_age.equals("0")) {
                    map.put("end_age", end_age);
                }
            }

            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str) && Long.valueOf(query_date_str) != -1) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            List<Patient> patient_list = patientService.qryPatientByPage(map);
            JsonArray array = new JsonArray();
            for (Patient pat : patient_list) {
                JsonObject item = new JsonObject();
                item.add("patient_id", gson.toJsonTree(pat.getPatient_id()));
                item.add("name", gson.toJsonTree(pat.getName()));
                item.add("birthday", gson.toJsonTree(pat.getBirthday()));
                item.add("gender", gson.toJsonTree(pat.getGender()));
                item.add("phone", gson.toJsonTree(pat.getPhone()));
                item.add("address", gson.toJsonTree(pat.getAddress()));
                item.add("profile", gson.toJsonTree(pat.getProfile()));
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("patients", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.4.3.2	POST /doctors/patients
    @RequestMapping(value = "/doctors/patients", method = RequestMethod.POST)
    public void PostPatients(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "birthday", "gender", "phone", "address",
                    "profile"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Patient patient = gson.fromJson(params, Patient.class);
            Patient hasPatient = patientService.selPatientByPhone(patient.getPhone());
            if (hasPatient != null){
                setRequestErrorRes(res, "该手机号已被添加，不能重复使用", ErrorCode.DUPLICATE_DONE);
                return;
            }
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            patient.setDoctor_id(doctor.getDoctor_id());
            patient.setStatus(0);
            patient.setPatient_id(0);
            patientService.insertPatient(patient);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.4.3.2	PUT /doctors/patients/{pid}
    @RequestMapping(value = "/doctors/patients/{pid}", method = RequestMethod.PUT)
    public void PutPatients(@PathVariable("pid") int id,
                            HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "birthday", "gender", "address",
                    "profile"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Patient patient = gson.fromJson(params, Patient.class);
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            patient.setDoctor_id(doctor.getDoctor_id());
            patient.setStatus(0);
            patient.setPatient_id(id);
            patientService.putPatient(patient);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.4.3.3	POST /doctors/patients/{pid}/wristbands/new
    @RequestMapping(value = "/doctors/patients/{pid}/wristbands/new", method = RequestMethod.POST)
    public void AddWristbands(@PathVariable("pid") int pid,
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
            wristband.setPatient_id(pid);
            wristband.setMeasured_at(String.valueOf((new Date()).getTime() / 1000));
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            wristband.setDoctor_id(doctor.getDoctor_id());
            wristband.setStatus(0);
            wristband.setUser_symptom(params.get("symptom").getAsString());
            patientService.insertWristbandFile(wristband);
            content.setSuccess_message(wristband.getWristband_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.4.3.4	POST /doctors/patients/{pid}/sphygmomanometers/new
    @RequestMapping(value = "/doctors/patients/{pid}/sphygmomanometers/new", method = RequestMethod.POST)
    public void PutPatSphy(@PathVariable("pid") int pid,
                           HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"systolic_pressure", "diastolic_pressure", "heart_rate",
                    "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatSphygmomanometerFile sphygmomanometer = gson.fromJson(params, PatSphygmomanometerFile.class);
            sphygmomanometer.setPatient_id(pid);
            sphygmomanometer.setMeasured_at(String.valueOf((new Date()).getTime() / 1000));
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            sphygmomanometer.setUser_symptom(params.get("symptom").getAsString());
            sphygmomanometer.setDoctor_id(doctor.getDoctor_id());
            sphygmomanometer.setStatus(0);
            patientService.insertSphygmomanometerFile(sphygmomanometer);
            content.setSuccess_message(sphygmomanometer.getSphygmomanometer_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.4.3.5	POST /doctors/patients/{pid}/oximeters/new
    @RequestMapping(value = "/doctors/patients/{pid}/oximeters/new", method = RequestMethod.POST)
    public void PutPatOxi(@PathVariable("pid") int pid,
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
            oximeter.setPatient_id(pid);
            oximeter.setMeasured_at(String.valueOf((new Date()).getTime() / 1000));
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            oximeter.setDoctor_id(doctor.getDoctor_id());
            oximeter.setUser_symptom(params.get("symptom").getAsString());
            oximeter.setStatus(0);
            patientService.insertOximeterFile(oximeter);
            content.setSuccess_message(oximeter.getOximeter_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.4.3.6	POST /doctors/patients/{pid}/glucosemeters/new
    @RequestMapping(value = "/doctors/patients/{pid}/glucosemeters/new", method = RequestMethod.POST)
    public void PutPatGlu(@PathVariable("pid") int pid,
                          HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"glucosemeter_value", "symptom", "proposal"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            PatGlucosemeterFile glucosemeter = gson.fromJson(params, PatGlucosemeterFile.class);
            glucosemeter.setPatient_id(pid);
            glucosemeter.setMeasured_at(String.valueOf((new Date()).getTime() / 1000));
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            glucosemeter.setDoctor_id(doctor.getDoctor_id());
            glucosemeter.setUser_symptom(params.get("symptom").getAsString());
            glucosemeter.setStatus(0);
            patientService.insertGlucosemeterFile(glucosemeter);

            content.setSuccess_message(glucosemeter.getGlucosemeter_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.4.3.7	POST /doctors/patients/{pid}/thermometers/new
    @RequestMapping(value = "/doctors/patients/{pid}/thermometers/new", method = RequestMethod.POST)
    public void PutPatThe(@PathVariable("pid") int pid,
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
            thermometers.setPatient_id(pid);
            thermometers.setMeasured_at(String.valueOf((new Date()).getTime() / 1000));
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            thermometers.setDoctor_id(doctor.getDoctor_id());
            thermometers.setUser_symptom(params.get("symptom").getAsString());
            thermometers.setStatus(0);
            patientService.insertThermometersFile(thermometers);

            content.setSuccess_message(thermometers.getThermometer_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.4.3.8	POST /doctors/patients/{pid}/fats/new
    @RequestMapping(value = "/doctors/patients/{pid}/fats/new", method = RequestMethod.POST)
    public void PutPatFat(@PathVariable("pid") int pid,
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
            fat.setPatient_id(pid);
            fat.setMeasured_at(String.valueOf((new Date()).getTime() / 1000));
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            fat.setUser_symptom(params.get("symptom").getAsString());
            fat.setDoctor_id(doctor.getDoctor_id());
            fat.setStatus(0);
            patientService.insertFatFile(fat);

            content.setSuccess_message(fat.getFat_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.3.10 POST /doctors/patients/{pid}/others/new*/
    @RequestMapping(value = "/doctors/patients/{pid}/others/new", method = RequestMethod.POST)
    public void PostPatientsOther(@PathVariable("pid") int pid,
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
            other.setOther_file_id(0);
            other.setUser_symptom(params.get("symptom").getAsString());
            other.setProposal(params.get("proposal").getAsString());
            if (params.has("measured_at")){
                other.setMeasured_at(params.get("measured_at").getAsString());
            } else {
                other.setMeasured_at(String.valueOf(System.currentTimeMillis() / 1000));
            }
            other.setStatus(0);
            other.setPatient_id(pid);
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());
            other.setDoctor_id(doctor.getDoctor_id());
            patientService.insertOtherFile(other);

            content.setSuccess_message(other.getOther_file_id());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }
}