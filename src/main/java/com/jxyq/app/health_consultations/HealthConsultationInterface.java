package com.jxyq.app.health_consultations;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.BeanProperty.HealthStatus;
import com.jxyq.commons.constants.BeanProperty.QAStatus;
import com.jxyq.commons.constants.BeanProperty.QAType;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.ConContext;
import com.jxyq.model.QA;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.ConsultationService;
import com.jxyq.service.inf.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Controller
public class HealthConsultationInterface extends BaseInterface {
    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    private FileService insertFileInfo;

    //3.4.1	GET /api/expert_teams
    @RequestMapping(value = "/api/open/expert_teams", method = RequestMethod.GET)
    public void ExpertInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> expertTeams = consultationService.getExpertTeams();
            content.setSuccess_message(expertTeams);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.4.2	GET /api/expert_teams/(expert_team_id)/phone
    @RequestMapping(value = "/api/expert_teams/{expert_team_id}/phone", method = RequestMethod.GET)
    public void ExpertInterface(@PathVariable("expert_team_id") int id,
                                HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        Integer points = User.PASS_POINTS;
        try {
            User user = (User) req.getAttribute("user");
            if (user.getAuthenticity() == null || user.getAuthenticity().equals(BeanProperty.RealStatus.UNREAL)) {
                setParamWarnRes(res, "用户没有填写真实姓名", ErrorCode.UNREAL_FULL_NAME);
                return;
            }
            if (user.getFilled_in_at() == null || user.getFilled_in_at() == 0 ||
                    System.currentTimeMillis() / 1000 - user.getFilled_in_at() < 24 * 3600) {
                setParamWarnRes(res, "用户填写真实姓名不超过24小时", ErrorCode.REAL_NAME_UNCHECK);
                return;
            }

            int userId = user.getUser_id();
            String phone = consultationService.getExpertPhone(id);
            Integer userPoints = consultationService.getUserPoints(userId);
//          更新后怎么获取最新的积分
            Integer new_points = userPoints - points;
            if (new_points < 0) {
                setParamWarnRes(res, "积分已用完，请充值", ErrorCode.INVALID_POINTS);
                return;
            }
            consultationService.upUserPoints(new_points, userId);
            Map<String, Object> map = new HashMap<>();
            map.put("phone", phone);
            map.put("points", points);
            map.put("new_points", new_points);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.3	POST /api/open/health_consultations*/
    @RequestMapping(value = "/api/open/health_consultations", method = RequestMethod.POST)
    public void GetConsultContext(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int pageSize = params.get("page_size").getAsInt();
            int currentPage = params.get("current_page").getAsInt();
            PagingCriteria pagingCriteria = new PagingCriteria(currentPage, pageSize, "", "");

            Map<String, Object> map = new HashMap<>();
            if (params.has("submit_date")) {
                String submitDate = params.get("submit_date").getAsString();
                if (StringUtils.isBlank(submitDate)) {
                    map.put("submit_date", submitDate);
                }
            }

            if (params.has("district_id")) {
                int districtId = params.get("district_id").getAsInt();
                if (districtId != 0) {
                    map.put("district_id", districtId);
                }
            }

            if (params.has("department_id")) {
                int departmentId = params.get("department_id").getAsInt();
                if (departmentId != 0) {
                    map.put("department_id", departmentId);
                }
            }

            if (params.has("begin_date")) {
                long beginDate = params.get("begin_date").getAsLong();
                map.put("begin_date", beginDate);
            }

            if (params.has("end_date")) {
                long endDate = params.get("end_date").getAsLong();
                map.put("end_date", endDate);
            }

            if (params.has("status")) {
                int status = params.get("status").getAsInt();
                if (status != 0) {
                    map.put("status", status);
                }
            }
            map.put("open", true);

            List<ConContext> list = consultationService.selConsultation(map, pagingCriteria);
            int count = pagingCriteria.getTotal();
            int pageCount = 0;
            if (pageSize != 0) {
                pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            }
            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(pageSize));
            result.add("current_page", gson.toJsonTree(currentPage));
            result.add("total_page", gson.toJsonTree(pageCount));
            result.add("total_count", gson.toJsonTree(count));
            result.add("results", gson.toJsonTree(list));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.4	POST /api/health_consultations/history*/
    @RequestMapping(value = "/api/health_consultations/history", method = RequestMethod.POST)
    public void GetConsultHistory(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int pageSize = params.get("page_size").getAsInt();
            int currentPage = params.get("current_page").getAsInt();
            PagingCriteria pagingCriteria = new PagingCriteria(currentPage, pageSize, "", "");

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user.getUser_id());
            if (params.has("submit_date")) {
                String submitDate = params.get("submit_date").getAsString();
                if (StringUtils.isBlank(submitDate)) {
                    map.put("submit_date", submitDate);
                }
            }

            if (params.has("district_id")) {
                int districtId = params.get("district_id").getAsInt();
                if (districtId != 0) {
                    map.put("district_id", districtId);
                }
            }

            if (params.has("department_id")) {
                int departmentId = params.get("department_id").getAsInt();
                if (departmentId != 0) {
                    map.put("department_id", departmentId);
                }
            }

            if (params.has("begin_date")) {
                long beginDate = params.get("begin_date").getAsLong();
                map.put("begin_date", beginDate);
            }

            if (params.has("end_date")) {
                long endDate = params.get("end_date").getAsLong();
                map.put("end_date", endDate);
            }

            if (params.has("status")) {
                int status = params.get("status").getAsInt();
                if (status != 0) {
                    map.put("status", status);
                }
            }
            map.put("open", false);

            List<ConContext> list = consultationService.selConsultation(map, pagingCriteria);
            int count = pagingCriteria.getTotal();
            int pageCount = 0;
            if (pageSize != 0) {
                pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            }
            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(pageSize));
            result.add("current_page", gson.toJsonTree(currentPage));
            result.add("total_page", gson.toJsonTree(pageCount));
            result.add("total_count", gson.toJsonTree(count));
            result.add("results", gson.toJsonTree(list));
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.4.5	GET /api/health_consultations/(health_consultation_id)
    @RequestMapping(value = "/api/health_consultations/{health_consultation_id}", method = RequestMethod.GET)
    public void GetQA(@PathVariable("health_consultation_id") int id, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            if (id == 0) {
                setRequestErrorRes(res);
                return;
            }
            List<Map<String, Object>> mapList = consultationService.selConsultDetail(id);
            if (mapList != null || mapList.size() > 0) {
                for (Map<String, Object> map : mapList) {
                    List<String> image_urls = new ArrayList<>();
                    if (map.containsKey("image_url1")) {
                        String image_url = map.get("image_url1").toString();
                        if (!StringUtils.isBlank(image_url)) {
                            image_urls.add(image_url);
                        }
                        map.remove("image_url1");
                    }
                    if (map.containsKey("image_url2")) {
                        String image_url = map.get("image_url2").toString();
                        if (!StringUtils.isBlank(image_url)) {
                            image_urls.add(image_url);
                        }
                        map.remove("image_url2");
                    }
                    if (map.containsKey("image_url3")) {
                        String image_url = map.get("image_url3").toString();
                        if (!StringUtils.isBlank(image_url)) {
                            image_urls.add(image_url);
                        }
                        map.remove("image_url3");
                    }
                    if (map.containsKey("image_url4")) {
                        String image_url = map.get("image_url4").toString();
                        if (!StringUtils.isBlank(image_url)) {
                            image_urls.add(image_url);
                        }
                        map.remove("image_url4");
                    }
                    if (image_urls.size() > 0) {
                        map.put("image_urls", image_urls);
                    }
                }
            }
            content.setSuccess_message(mapList);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.6	POST /api/health_consultations*/
    @Transactional
    @RequestMapping(value = "/api/health_consultations", method = RequestMethod.POST)
    public void CreateConsultation(@RequestParam("images") MultipartFile[] images,
                                   @RequestParam("audio") MultipartFile[] audios,
                                   @RequestParam("json") String json,
                                   HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        User user = (User) req.getAttribute("user");
        try {
            JsonParser parser = new JsonParser();
            JsonObject params = parser.parse(json).getAsJsonObject();
            String necessaryParam[] = {"district_id", "department_id"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int distId = params.get("district_id").getAsInt();
            int deptId = params.get("department_id").getAsInt();
            String contents = "";
            if (params.has("content")) {
                contents = params.get("content").getAsString();
            }

            /*插入健康咨询表*/
            ConContext conContext = new ConContext();
            conContext.setUser_id(user.getUser_id());
            conContext.setCreated_at((new Date().getTime()) / 1000);
            conContext.setStatus(HealthStatus.NoAnswer);//默认设置状态为1，未回答
            conContext.setDistrict_id(distId);
            conContext.setDepartment_id(deptId);
            conContext.setContent(contents);
            consultationService.createConsultation(conContext);

            /*插入问答表*/
            Long id = conContext.getId();
            QA QA = new QA();
            QA.setHealth_consultation_id(id);
            QA.setCreated_at(new Date().getTime() / 1000);
            QA.setContent(contents);
            QA.setQa_type(QAType.QUESTION);//默认提问 0
            QA.setStatus(QAStatus.Pass);//默认审核通过 2
            consultationService.createQA(QA);

            /*插入问答资源表*/
            Long qa_id = QA.getQuestion_and_reply_id();
            String resPath = req.getScheme() + "://" + req.getServerName() + req.getContextPath()
                    + "/api/open/download?file_id=";
            Map<String, Object> map = new HashMap<>();
            //初始化image和audio对应的map
            for (int i = 1; i < 5; i++) {
                map.put("image_url" + i, "");
            }
            map.put("audio_url", "");
            //处理上传图片
            ArrayList listImg = saveFile(images, req.getSession().getServletContext().getRealPath("/") + "../upload/images/");
            if (listImg != null) {
                for (int i = 0; i < listImg.size(); i++) {
                    String urlSql = "image_url" + (i + 1);
                    map.put(urlSql, resPath + listImg.get(i));
                }
            }
            //处理音频上传
            ArrayList listAuo = saveFile(audios, req.getSession().getServletContext().getRealPath("/") + "../upload/audios/");
            if (listAuo != null) {
                map.put("audio_url", resPath + listAuo.get(0));//存在且有一个音频
                if (params.has("audio_duration")) {
                    int audio_duration = params.get("audio_duration").getAsInt();
                    map.put("audio_duration", audio_duration);
                }
            }
            map.put("question_and_reply_id", qa_id);
            consultationService.createQAResource(map);
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("id", id);
            content.setSuccess_message(model);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.7	POST /api/health_consultations/(health_consultation_id)*/
    @Transactional
    @RequestMapping(value = "/api/health_consultations/{health_consultation_id}", method = RequestMethod.POST)
    public void AddConsultation(@PathVariable("health_consultation_id") long id,
                                @RequestParam("images") MultipartFile[] images,
                                @RequestParam("audio") MultipartFile[] audios,
                                @RequestParam("json") String json,
                                HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonParser parser = new JsonParser();
            JsonObject params = parser.parse(json).getAsJsonObject();

            /*插入问答表*/
            QA QA = new QA();
            QA.setHealth_consultation_id(id);
            QA.setCreated_at(new Date().getTime() / 1000);
            if (params.has("content")) {
                QA.setContent(params.get("content").getAsString());
            } else {
                QA.setContent("");
            }
            QA.setQa_type(QAType.QUESTION);//默认提问0
            QA.setStatus(QAStatus.Pass);//默认审核通过2
            consultationService.createQA(QA);

            /*插入问答资源表*/
            Long qa_id = QA.getQuestion_and_reply_id();
            String resPath = req.getScheme() + "://" + req.getServerName() + req.getContextPath()
                    + "/api/open/download?file_id=";
            Map<String, Object> map = new HashMap<>();
            //初始化image和audio对应的map
            for (int i = 1; i < 5; i++) {
                map.put("image_url" + i, "");
            }
            map.put("audio_url", "");
            //处理上传图片
            ArrayList listImg = saveFile(images, req.getSession().getServletContext().getRealPath("/") + "../upload/images/");
            if (listImg != null) {
                for (int i = 0; i < listImg.size(); i++) {
                    String urlSql = "image_url" + (i + 1);
                    map.put(urlSql, resPath + listImg.get(i));
                }
            }
            //处理音频上传
            ArrayList listAuo = saveFile(audios, req.getSession().getServletContext().getRealPath("/") + "../upload/audios/");
            if (listAuo != null) {
                map.put("audio_url", resPath + listAuo.get(0));//存在且有一个音频
                if (params.has("audio_duration")) {
                    map.put("audio_duration", params.get("audio_duration").getAsInt());
                }
            }
            map.put("question_and_reply_id", qa_id);
            consultationService.createQAResource(map);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.4.8	PUT /api/health_consultations/(health_consultation_id)*/
    @RequestMapping(value = "/api/health_consultations/{health_consultation_id}", method = RequestMethod.PUT)
    public void CloseConsultation(@PathVariable("health_consultation_id") int id, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJSONParams(req);
            String necessaryParams[] = {"score"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res);
                return;
            }
            String score = params.get("score").getAsString();
            Map<String, Object> map = new HashMap<>();
            map.put("health_consultation_id", id);
            if (params.has("comment")) {
                map.put("comment", params.get("comment").getAsString());
            }
            map.put("score", score);
            map.put("status", HealthStatus.Comment);//已评价3
            consultationService.closeContent(map);
            content.setSuccess_message(null);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //上传多文件
    public ArrayList saveFile(MultipartFile[] files, String filePath) {
        ArrayList fileId = new ArrayList();
        if (files == null || files.length == 0) {
            return null;
        }
        try {
            CommonUtil.ifNotExistsCreate(filePath);
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String suffix = file.getOriginalFilename().substring(
                        file.getOriginalFilename().lastIndexOf(".", file.getOriginalFilename().length()));
                String fileHashName = passwordEncoder.encodePassword(file.getOriginalFilename(), CommonUtil.getRandomString(6));
                file.transferTo(new File(filePath + fileHashName + suffix));
                String subStr = filePath.substring(filePath.lastIndexOf("upload") + 7) + fileHashName + suffix;
                Map<String, Object> map = new HashMap<>();
                map.put("file_path", subStr);
                insertFileInfo.insertFile(map);
                fileId.add(map.get("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return fileId;
    }
}
