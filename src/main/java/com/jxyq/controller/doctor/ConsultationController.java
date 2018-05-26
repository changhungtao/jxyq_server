package com.jxyq.controller.doctor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.push.AndroidPushMessage;
import com.jxyq.commons.push.AndroidPushMessageForPhone;
import com.jxyq.commons.push.IosPushMessageForPhone;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.consultation.HealthConsultation;
import com.jxyq.model.consultation.QuestionAndReply;
import com.jxyq.model.consultation.QuestionAndReplyResource;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.others.PushService;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.ConsultationService;
import com.jxyq.service.inf.DoctorService;
import com.jxyq.service.inf.UserService;
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
public class ConsultationController extends BaseInterface {
    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    /*3.4.5.1 POST /doctors/consultations*/
    @RequestMapping(value = "/doctors/consultations", method = RequestMethod.POST)
    public void PostDocCon(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"created_from", "created_to",
                    "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (params.has("district_id")) {
                JsonArray district_id = params.get("district_id").getAsJsonArray();
                if (district_id != null && district_id.size() > 0) {
                    List ids = new ArrayList<>();
                    for (JsonElement item : district_id) {
                        ids.add(item.getAsInt());
                    }
                    map.put("district_id", ids);
                }
            }

            if (params.has("department_id")) {
                JsonArray department_id = params.get("department_id").getAsJsonArray();
                if (department_id != null && department_id.size() > 0) {
                    List ids = new ArrayList<>();
                    for (JsonElement item : department_id) {
                        ids.add(item.getAsInt());
                    }
                    map.put("department_id", ids);
                }
            }
            if (params.has("status")) {
                map.put("status", params.get("status").getAsInt());
            }
            map.put("created_from", params.get("created_from").getAsLong());
            map.put("created_to", params.get("created_to").getAsLong());

            int page_size = params.get("page_size").getAsInt();
            int current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = new Date().getTime() / 1000;
            if (params.has("query_date") && params.get("query_date").getAsLong() != -1) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            List<Map<String, Object>> consultation = consultationService.selHealthConByPage(map);
            JsonArray array = new JsonArray();
            for (Map<String, Object> item : consultation) {
                JsonObject con = new JsonObject();
                con.add("consultation_id", gson.toJsonTree(item.get("health_consultation_id")));
                con.add("user_name", gson.toJsonTree(item.get("full_name")));
                con.add("user_phone", gson.toJsonTree(item.get("phone")));
                con.add("district_id", gson.toJsonTree(item.get("district_id")));
                con.add("created_at", gson.toJsonTree(item.get("created_at")));
                con.add("status", gson.toJsonTree(item.get("status")));
                array.add(con);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("consultations", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.5.2 GET /doctors/consultations/{cid}*/
    @RequestMapping(value = "/doctors/consultations/{cid}", method = RequestMethod.GET)
    public void GetDocConById(@PathVariable("cid") long cid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            HealthConsultation con = consultationService.selHealthConById(cid);
            if (con == null) {
                setParamWarnRes(res, "健康咨询不存在", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<QuestionAndReplyResource> qa_list = consultationService.selQuestionAndReplyByConId(cid);
            JsonArray array = new JsonArray();
            for (QuestionAndReplyResource qa : qa_list) {
                JsonObject item = new JsonObject();
                item.add("question_and_reply_id", gson.toJsonTree(qa.getQuestion_and_reply_id()));
                item.add("created_at", gson.toJsonTree(qa.getCreated_at()));
                item.add("content", gson.toJsonTree(qa.getContent()));
                item.add("qa_type", gson.toJsonTree(qa.getQa_type()));
                item.add("doctor_name", gson.toJsonTree(qa.getDoctor_name()));
                item.add("image_url1", gson.toJsonTree(qa.getImage_url1()));
                item.add("image_url2", gson.toJsonTree(qa.getImage_url2()));
                item.add("image_url3", gson.toJsonTree(qa.getImage_url3()));
                item.add("image_url4", gson.toJsonTree(qa.getImage_url4()));
                item.add("audio_url", gson.toJsonTree(qa.getAudio_url()));
                item.add("audio_duration", gson.toJsonTree(qa.getAudio_duration()));
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("user_id", gson.toJsonTree(con.getUser_id()));
            json.add("created_at", gson.toJsonTree(con.getCreated_at()));
            json.add("district_id", gson.toJsonTree(con.getDistrict_id()));
            json.add("department_id", gson.toJsonTree(con.getDepartment_id()));
            json.add("doctor_id", gson.toJsonTree(con.getDoctor_id()));
            json.add("content", gson.toJsonTree(con.getContent()));
            json.add("comment", gson.toJsonTree(con.getComment()));
            json.add("score", gson.toJsonTree(con.getScore()));
            json.add("status", gson.toJsonTree(con.getStatus()));
            json.add("qas", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.5.3 POST /doctors/consultations/{cid}*/
    @RequestMapping(value = "/doctors/consultations/{cid}", method = RequestMethod.POST)
    public void PostDocConCid(@PathVariable("cid") long cid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Subject subject = SecurityUtils.getSubject();
            Doctor doc = (Doctor) subject.getPrincipals().getPrimaryPrincipal();
            Doctor doctor = doctorService.selDoctorByLoginName(doc.getLogin_name());

            JsonObject params = getJsonElementParams(req).getAsJsonObject();

            QuestionAndReply qa = new QuestionAndReply();
            qa.setHealth_consultation_id(cid);
            qa.setCreated_at(new Date().getTime() / 1000);
            if (params.has("content")) {
                qa.setContent(params.get("content").getAsString());
            } else {
                qa.setContent("");
            }
            qa.setQa_type(BeanProperty.QAType.ANSWER);
            qa.setStatus(BeanProperty.QAStatus.Pass);
            qa.setDoctor_id(doctor.getDoctor_id());
            consultationService.inQuestionAndReply(qa);

            Map<String, Object> map = new HashMap<>();
            if (params.has("image_url1")) {
                map.put("image_url1", params.get("image_url1").getAsString());
            }
            if (params.has("image_url2")) {
                map.put("image_url2", params.get("image_url2").getAsString());
            }
            if (params.has("image_url3")) {
                map.put("image_url3", params.get("image_url3").getAsString());
            }
            if (params.has("image_url4")) {
                map.put("image_url4", params.get("image_url4").getAsString());
            }
            if (!map.isEmpty()) {
                map.put("audio_url", "");
                map.put("audio_duration", 0);
                map.put("status", BeanProperty.QAStatus.Pass);
                map.put("question_and_reply_id", qa.getQuestion_and_reply_id());
                consultationService.createQAResource(map);
            }

            HealthConsultation con = consultationService.selHealthConById(cid);
            Map<String, Object> con_set = new HashMap<>(2);
            con_set.put("health_consultation_id", con.getHealth_consultation_id());
            con_set.put("status", BeanProperty.HealthStatus.Answering);
            consultationService.upConsultation(con_set);

            /*百度推送通知*/
            PushService push_service = userService.selPushServiceByUid(con.getUser_id());

            String msg_content = "您的健康咨询收到医生的回复，为了您的健康，请注意查看哦！";
            AndroidPushMessage msg = new AndroidPushMessage(1, msg_content);

            JsonObject ios_msg = new JsonObject();
            JsonObject aps = new JsonObject();
            aps.add("alert", gson.toJsonTree("Message From Baidu Cloud Push-Service"));
            aps.add("content-available", gson.toJsonTree("1"));
            ios_msg.add("aps", aps);
            JsonObject lightapp_ctrl_keys = new JsonObject();
            lightapp_ctrl_keys.add("display_in_notification_bar", gson.toJsonTree("1"));
            lightapp_ctrl_keys.add("enter_msg_center", gson.toJsonTree("0"));
            ios_msg.add("lightapp_ctrl_keys", lightapp_ctrl_keys);

            AndroidPushMessageForPhone aPush = new AndroidPushMessageForPhone();
            IosPushMessageForPhone iPush = IosPushMessageForPhone.getInstance();

            if (push_service.getDevice() == BeanProperty.ComputeType.ANDROID) {
                aPush.pushMsgToSingleDevice(msg, push_service);
            } else {
                iPush.sendMessage(push_service.getPush_token(), msg_content);
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