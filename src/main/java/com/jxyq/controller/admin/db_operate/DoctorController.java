package com.jxyq.controller.admin.db_operate;

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
import com.jxyq.commons.util.OnlineCount;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class DoctorController extends BaseInterface {
    public static String md5_secret = SysConfig.getInstance().getProperty("md5.secret");
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    /*3.3.3.4.2 POST /admins/db/doctors*/
    @RequestMapping(value = "/admins/db/doctors", method = RequestMethod.POST)
    public void PostDbDoctors(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (params.has("full_name")) {
                String full_name = params.get("full_name").getAsString();
                if (!StringUtils.isBlank(full_name)) {
                    map.put("name", full_name);
                }
            }

            if (params.has("registered_from")) {
                map.put("minRegisterTime", params.get("registered_from").getAsLong());
            }

            if (params.has("registered_to")) {
                map.put("maxRegisterTime", params.get("registered_to").getAsLong());
            }

            long query_date = System.currentTimeMillis() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }

            Map<String, Object> total_map = doctorService.selTotalDocCnt();
            long total_cnt = 0;
            long online_cnt = 0;
            if (total_map != null) {
                total_cnt = (long) total_map.get("count");
            }
            OnlineCount onlineCount = OnlineCount.getInstance();
            online_cnt = onlineCount.doctor_count;

            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            List<Map<String, Object>> doctorList = doctorService.selDoctorByPage(map, pageInf);

            JsonArray lists = new JsonArray();
            for (Map<String, Object> doctor : doctorList) {
               JsonObject item = new JsonObject();
                item.add("doctor_id", gson.toJsonTree(doctor.get("doctor_id")));
                item.add("login_name", gson.toJsonTree(doctor.get("login_name")));
                item.add("full_name", gson.toJsonTree(doctor.get("full_name")));
                item.add("registered_at", gson.toJsonTree(doctor.get("registered_at")));
                item.add("status", gson.toJsonTree(doctor.get("status")));
                lists.add(item);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), page_size));
            result.put("total_count", pageInf.getTotal());
            result.put("online_count", online_cnt);
            result.put("member_count", total_cnt);
            result.put("query_date", query_date);
            result.put("doctors", lists);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.2.4.2	GET /admins/db/doctors/{did}*/
    @RequestMapping(value = "/admins/db/doctors/{did}", method = RequestMethod.GET)
    public void getDoctorDetail(@PathVariable("did") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Map<String, Object> doctor = doctorService.selDoctorDetail(id);
            content.setSuccess_message(doctor);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }

    }

    /*3.2.2.4.3	POST /admins/db/doctors/add*/
    @RequestMapping(value = "/admins/db/doctors/add", method = RequestMethod.POST)
    public void postDoctorsAdd(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Doctor d = getJSONParams(req, Doctor.class);
            if (d.getLogin_name() == null || d.getPassword() == null
                    || d.getPhone() == null || d.getPhysician_certificate() == null
                    || d.getPracticing_certificate() == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String doc_psw = passwordEncoder.encodePassword(d.getPassword(), md5_secret);
            d.setPassword(doc_psw);
            if (d.getGender() == null){
                d.setGender(BeanProperty.Gender.DEFAULT);
            }
            if (d.getDistrict_id() != null) {
                int district_id = d.getDistrict_id();
                Map<String, Object> expert_team = doctorService.selExpertTeamByDistrict(district_id);
                d.setExpert_team_id((int) expert_team.get("expert_team_id"));
            } else {
                d.setDistrict_id(0);
                d.setExpert_team_id(0);
            }
            d.setUpdated_at(System.currentTimeMillis() / 1000);
            d.setRegistered_at(System.currentTimeMillis() / 1000);
            d.setStatus(BeanProperty.UserStatus.NORMAL);
            doctorService.insertDoctor(d);

            JsonObject json = new JsonObject();
            json.add("doctor_id", gson.toJsonTree(d.getDoctor_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.2.4.4	PUT /admins/db/doctors/{did}/state*/
    @RequestMapping(value = "/admins/db/doctors/{did}/state", method = RequestMethod.PUT)
    public void putDoctorsStatus(@PathVariable("did") int id,
                                 HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"status"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String status = params.get("status").getAsString();
            doctorService.upDoctorStatus(id, status);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    //3.2.2.4.5	PUT /admins/db/doctors/{did}
    @RequestMapping(value = "/admins/db/doctors/{did}", method = RequestMethod.PUT)
    public void postDoctorsEdit(@PathVariable("did") int id,
                                HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"full_name", "identification_number", "gender", "birthday",
                    "email", "avatar_url", "phone", "department_id", "district_id", "profile",
                    "physician_certificate", "practicing_certificate"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Doctor doctor = gson.fromJson(params, Doctor.class);
            Map<String, Object> map = new HashMap<>();
            if (params.has("password")) {
                String mad_psw = passwordEncoder.encodePassword(doctor.getPassword(), md5_secret);
                map.put("password", mad_psw);
            }
            map.put("doctor_id", id);
            map.put("full_name", doctor.getFull_name());
            map.put("identification_number", doctor.getIdentification_number());
            map.put("gender", doctor.getGender());

            map.put("birthday", doctor.getBirthday());
            map.put("email", doctor.getEmail());
            map.put("avatar_url", doctor.getAvatar_url());
            map.put("phone", doctor.getPhone());

            map.put("department_id", doctor.getDepartment_id());
            if (params.has("district_id")) {
                int district_id = params.get("district_id").getAsInt();
                Map<String, Object> expert_team = doctorService.selExpertTeamByDistrict(district_id);
                map.put("district_id", district_id);
                map.put("expert_team_id", (int) expert_team.get("expert_team_id"));
            } else {
                map.put("district_id", 0);
                map.put("expert_team_id", 0);
            }

            map.put("profile", doctor.getProfile());
            map.put("physician_certificate", doctor.getPhysician_certificate());
            map.put("practicing_certificate", doctor.getPracticing_certificate());
            map.put("updated_at", new Date().getTime() / 1000);
            doctorService.updateDoctorByAdmin(map);
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.4.6	DELETE /admins/db/doctors/{did}
    @RequestMapping(value = "/admins/db/doctors/{did}", method = RequestMethod.DELETE)
    public void deleteDoctor(@PathVariable("did") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            /*doctorService.deleteDoctor(id);*/
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