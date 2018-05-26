package com.jxyq.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.app.watch.CharUtils;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.push.AndroidPushMessage;
import com.jxyq.commons.push.AndroidPushMessageForPhone;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.model.user.UserPupil;
import com.jxyq.model.watch.*;
import com.jxyq.service.inf.ConstantService;
import com.jxyq.service.inf.watch.ActionService;
import com.jxyq.service.inf.watch.RouteService;
import com.jxyq.service.inf.watch.WatchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class WatchController extends BaseInterface {
    @Autowired
    private WatchUserService watchUserService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ConstantService constantService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String apiKey = SysConfig.getInstance().getProperty("push.watch_heartbeat_apiKey");
    private String secretKey = SysConfig.getInstance().getProperty("push.watch_heartbeat_secretKey");

    /*3.3.10.1 GET /admins/imei/modeset*/
    @RequestMapping(value = "/admins/imei/modeset", method = RequestMethod.GET)
    public void GetImeiModeSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            JsonObject json = new JsonObject();
            if (pupil.getMode_type() == WatchProperty.ModeType.COMPLEX_MODE) {
                json.add("defaultmode", gson.toJsonTree(WatchProperty.Mode.HIDE));
            } else {
                json.add("defaultmode", gson.toJsonTree(pupil.getUser_mode()));
            }

            List<ModeInf> modeSetList = actionService.selModeInfByUser(pupil.getUser_id());
            JsonArray array = new JsonArray();
            for (ModeInf modeInf : modeSetList) {
                JsonObject item = new JsonObject();
                item.add("period_id", gson.toJsonTree(modeInf.getId()));
                item.add("runmode", gson.toJsonTree(modeInf.getRunmode()));
                item.add("start", gson.toJsonTree(modeInf.getStart()));
                item.add("stop", gson.toJsonTree(modeInf.getStop()));
                item.add("weeks", gson.toJsonTree(modeInf.getWeeks()));
                array.add(item);
            }
            json.add("periods", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.2 PUT /admins/imei/modeset*/
    @RequestMapping(value = "/admins/imei/modeset", method = RequestMethod.PUT)
    public void PutImeiModeSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("defaultmode")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            int defaultmode = params.get("defaultmode").getAsInt();
            int mode_type = WatchProperty.ModeType.SINGLE_MODE;
            int user_mode = defaultmode;
            if (defaultmode == WatchProperty.Mode.HIDE) {
                mode_type = WatchProperty.ModeType.COMPLEX_MODE;
                user_mode = WatchProperty.Mode.SCHOOL;
            }
            actionService.upUserMode(pupil.getUser_id(), mode_type, user_mode);

            insertEventAction(pupil.getUser_id(), "runmode");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.3 POST /admins/imei/mode_periods*/
    @RequestMapping(value = "/admins/imei/mode_periods", method = RequestMethod.POST)
    public void PostImeiModePeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("mode_periods")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            JsonArray mode_periods = params.getAsJsonArray("mode_periods");
            JsonArray array = new JsonArray();
            for (JsonElement item : mode_periods) {
                JsonObject item_json = item.getAsJsonObject();
                ModeInf modeInf = new ModeInf();
                modeInf.setId(0);
                modeInf.setUser_id(pupil.getUser_id());
                modeInf.setRunmode(item_json.get("runmode").getAsInt());
                modeInf.setStart(item_json.get("start").getAsString());
                modeInf.setStop(item_json.get("stop").getAsString());
                modeInf.setWeeks(item_json.get("weeks").getAsInt());
                String now = format.format(new Date());
                modeInf.setUpdate_time(now);
                actionService.inModeInf(modeInf);
                array.add(gson.toJsonTree(modeInf.getId()));
            }

            insertEventAction(pupil.getUser_id(), "runmode");
            JsonObject json = new JsonObject();
            json.add("mode_period_ids", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.4 DELETE /admins/imei/mode_periods*/
    @RequestMapping(value = "/admins/imei/mode_periods", method = RequestMethod.DELETE)
    public void DelImeiModePeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String period_id_str = req.getParameter("period_id");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(period_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            int period_id = Integer.valueOf(period_id_str);
            ModeInf modeInf = actionService.selModeInfById(period_id);
            if (modeInf == null || modeInf.getUser_id() != pupil.getUser_id()) {
                setParamWarnRes(res, "模式时段不存在或无权限", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            actionService.delModeInf(period_id);

            insertEventAction(pupil.getUser_id(), "runmode");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.5 PUT /admins/imei/mode_periods*/
    @RequestMapping(value = "/admins/imei/mode_periods", method = RequestMethod.PUT)
    public void putImeiModePeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("period")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            JsonObject period = params.getAsJsonObject("period");
            ModeInf modeInf = actionService.selModeInfById(period.get("period_id").getAsInt());
            if (modeInf == null || modeInf.getUser_id() != pupil.getUser_id()) {
                setParamWarnRes(res, "模式时段不存在或无权限", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            modeInf.setRunmode(period.get("runmode").getAsInt());
            modeInf.setStart(period.get("start").getAsString());
            modeInf.setStop(period.get("stop").getAsString());
            modeInf.setWeeks(period.get("weeks").getAsInt());
            modeInf.setUpdate_time(format.format(new Date()));
            actionService.upModeInf(modeInf);

            insertEventAction(pupil.getUser_id(), "runmode");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.6 POST /admins/imei/numbers*/
    @RequestMapping(value = "/admins/imei/numbers", method = RequestMethod.POST)
    public void PostImeiNumbers(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "name1", "qing1", "name2", "qing2",
                    "name3", "qing3", "name4", "qing4", "sosname",
                    "sos"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            String name = "";
            int name_id = params.get("name1").getAsInt();
            ConstantDescription constant = null;
            if (name_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_qing_name", name_id);
                if (constant != null) {
                    name = constant.getExtra();
                }
            }
            String qing = params.get("qing1").getAsString();
            QingSet qingSet = new QingSet();
            qingSet.setId(0);
            qingSet.setUser_id(pupil.getUser_id());
            qingSet.setName("");
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            qingSet.setAddress("");
            qingSet.setQq("");
            qingSet.setEmail("");
            qingSet.setHomephone("");
            qingSet.setCompany("");
            qingSet.setCompanyhone("");
            qingSet.setCreate_time(format.format(new Date()));
            actionService.inQingSet(qingSet, 1);

            name = "";
            name_id = params.get("name2").getAsInt();
            if (name_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_qing_name", name_id);
                if (constant != null) {
                    name = constant.getExtra();
                }
            }
            qing = params.get("qing2").getAsString();
            qingSet.setId(0);
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            actionService.inQingSet(qingSet, 2);

            name = "";
            name_id = params.get("name3").getAsInt();
            if (name_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_qing_name", name_id);
                if (constant != null) {
                    name = constant.getExtra();
                }
            }
            qing = params.get("qing3").getAsString();
            qingSet.setId(0);
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            actionService.inQingSet(qingSet, 3);

            name = "";
            name_id = params.get("name4").getAsInt();
            if (name_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_qing_name", name_id);
                if (constant != null) {
                    name = constant.getExtra();
                }
            }
            qing = params.get("qing4").getAsString();
            qingSet.setId(0);
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            actionService.inQingSet(qingSet, 4);

            String sosname = "";
            int sosname_id = params.get("sosname").getAsInt();
            if (sosname_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_sos_name", sosname_id);
                if (constant != null) {
                    sosname = constant.getExtra();
                }
            }
            String sos = params.get("sos").getAsString();
            actionService.upSOSNumber(pupil.getUser_id(), sosname, sos);

            insertEventAction(pupil.getUser_id(), "qingset");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.7 GET /admins/imei/numbers*/
    @RequestMapping(value = "/admins/imei/numbers", method = RequestMethod.GET)
    public void GetImeiNumbers(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            QingSet qing1 = actionService.selQingSetByUser(pupil.getUser_id(), 1);
            QingSet qing2 = actionService.selQingSetByUser(pupil.getUser_id(), 2);
            QingSet qing3 = actionService.selQingSetByUser(pupil.getUser_id(), 3);
            QingSet qing4 = actionService.selQingSetByUser(pupil.getUser_id(), 4);
            Map<String, Object> sos = actionService.selSOSByUser(pupil.getUser_id());

            JsonObject json = new JsonObject();
            if (qing1 != null) {
                int name1 = 0;
                if (!StringUtils.isBlank(qing1.getRelation())) {
                    ConstantDescription constant = constantService.selConstantByExtra("twatch_qing_name", qing1.getRelation());
                    if (constant != null) {
                        name1 = constant.getConstant();
                    }
                }
                json.add("name1", gson.toJsonTree(name1));
                json.add("qing1", gson.toJsonTree(qing1.getPhone2()));
            } else {
                json.add("name1", gson.toJsonTree(0));
                json.add("qing1", gson.toJsonTree(""));
            }

            if (qing2 != null) {
                int name2 = 0;
                if (!StringUtils.isBlank(qing2.getRelation())) {
                    ConstantDescription constant = constantService.selConstantByExtra("twatch_qing_name", qing2.getRelation());
                    if (constant != null) {
                        name2 = constant.getConstant();
                    }
                }
                json.add("name2", gson.toJsonTree(name2));
                json.add("qing2", gson.toJsonTree(qing2.getPhone2()));
            } else {
                json.add("name2", gson.toJsonTree(0));
                json.add("qing2", gson.toJsonTree(""));
            }

            if (qing3 != null) {
                int name3 = 0;
                if (!StringUtils.isBlank(qing3.getRelation())) {
                    ConstantDescription constant = constantService.selConstantByExtra("twatch_qing_name", qing3.getRelation());
                    if (constant != null) {
                        name3 = constant.getConstant();
                    }
                }
                json.add("name3", gson.toJsonTree(name3));
                json.add("qing3", gson.toJsonTree(qing3.getPhone2()));
            } else {
                json.add("name3", gson.toJsonTree(0));
                json.add("qing3", gson.toJsonTree(""));
            }

            if (qing4 != null) {
                int name4 = 0;
                if (!StringUtils.isBlank(qing4.getRelation())) {
                    ConstantDescription constant = constantService.selConstantByExtra("twatch_qing_name", qing4.getRelation());
                    if (constant != null) {
                        name4 = constant.getConstant();
                    }
                }
                json.add("name4", gson.toJsonTree(name4));
                json.add("qing4", gson.toJsonTree(qing4.getPhone2()));
            } else {
                json.add("name4", gson.toJsonTree(0));
                json.add("qing4", gson.toJsonTree(""));
            }

            if (sos != null) {
                int sosname = 0;
                if (!StringUtils.isBlank((String) sos.get("name"))) {
                    ConstantDescription constant = constantService.selConstantByExtra("twatch_sos_name", (String) sos.get("name"));
                    if (constant != null) {
                        sosname = constant.getConstant();
                    }
                }
                json.add("sosname", gson.toJsonTree(sosname));
                json.add("sos", gson.toJsonTree(sos.get("phone")));
            } else {
                json.add("sos", gson.toJsonTree(""));
                json.add("sosname", gson.toJsonTree(0));
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

    /*3.3.10.8 POST /admins/imei/whitelist*/
    @RequestMapping(value = "/admins/imei/whitelist", method = RequestMethod.POST)
    public void PostImeiWhiteList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("whitelist")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            JsonArray array = params.getAsJsonArray("whitelist");
            if (array != null || array.size() != 0) {
                actionService.delWhiteSetInf(pupil.getUser_id());
                for (JsonElement item : array) {
                    JsonObject item_json = item.getAsJsonObject();
                    PhoneSetInfo phoneSetInfo = new PhoneSetInfo();
                    phoneSetInfo.setId(0);
                    phoneSetInfo.setUser_id(pupil.getUser_id());
                    phoneSetInfo.setName(item_json.get("name").getAsString());
                    phoneSetInfo.setPhone(item_json.get("number").getAsString());
                    phoneSetInfo.setCreate_time(format.format(new Date()));
                    actionService.inWhiteSetInf(phoneSetInfo);
                }
            }
            insertEventAction(pupil.getUser_id(), "whiteset");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.9 GET /admins/imei/whitelist*/
    @RequestMapping(value = "/admins/imei/whitelist", method = RequestMethod.GET)
    public void GetImeiWhiteList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            List<PhoneSetInfo> whitelist = actionService.selWhiteSetInfByUser(pupil.getUser_id());
            JsonArray array = new JsonArray();
            for (PhoneSetInfo phoneSetInfo : whitelist) {
                JsonObject item = new JsonObject();
                item.add("name", gson.toJsonTree(phoneSetInfo.getName()));
                item.add("number", gson.toJsonTree(phoneSetInfo.getPhone()));
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("whitelist", array);

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.10	POST /admins/imei/blacklist*/
    @RequestMapping(value = "/admins/imei/blacklist", method = RequestMethod.POST)
    public void PostImeiBlackList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("blacklist")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            JsonArray array = params.getAsJsonArray("blacklist");
            if (array != null || array.size() != 0) {
                actionService.delBlackSetInf(pupil.getUser_id());
                for (JsonElement item : array) {
                    JsonObject item_json = item.getAsJsonObject();
                    PhoneSetInfo phoneSetInfo = new PhoneSetInfo();
                    phoneSetInfo.setId(0);
                    phoneSetInfo.setUser_id(pupil.getUser_id());
                    phoneSetInfo.setName(item_json.get("name").getAsString());
                    phoneSetInfo.setPhone(item_json.get("number").getAsString());
                    phoneSetInfo.setCreate_time(format.format(new Date()));
                    actionService.inBlackSetInf(phoneSetInfo);
                }
            }
            insertEventAction(pupil.getUser_id(), "blackset");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.11	GET /admins/imei/blacklist*/
    @RequestMapping(value = "/admins/imei/blacklist", method = RequestMethod.GET)
    public void GetImeiBlackList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            List<PhoneSetInfo> blacklist = actionService.selBlackSetInfByUser(pupil.getUser_id());
            JsonArray array = new JsonArray();
            for (PhoneSetInfo phoneSetInfo : blacklist) {
                JsonObject item = new JsonObject();
                item.add("name", gson.toJsonTree(phoneSetInfo.getName()));
                item.add("number", gson.toJsonTree(phoneSetInfo.getPhone()));
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("blacklist", array);

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.12	POST /admins/imei/routeset*/
    @RequestMapping(value = "/admins/imei/routeset", method = RequestMethod.POST)
    public void PostImeiRouteSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "route_en", "gps_mode", "gps_interval"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            int route_en = params.get("route_en").getAsInt();
            int gps_mode = params.get("gps_mode").getAsInt();
            int gps_interval = params.get("gps_interval").getAsInt();
            /*JsonArray periods = params.getAsJsonArray("periods");*/

            watchUserService.inRouteInf(pupil.getUser_id(), route_en, gps_mode, gps_interval);

            /*for (JsonElement item : periods) {
                JsonObject item_json = item.getAsJsonObject();
                RouteSet routeSet = new RouteSet();
                routeSet.setId(0);
                routeSet.setUser_id(pupil.getUser_id());
                routeSet.setStart(item_json.get("start").getAsString());
                routeSet.setStop(item_json.get("stop").getAsString());
                routeSet.setWeeks(item_json.get("weeks").getAsInt());
                routeSet.setSet_en(item_json.get("set_en").getAsInt());
                actionService.inRouteSet(routeSet);
            }*/

            insertEventAction(pupil.getUser_id(), "location");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.13	GET /admins/imei/routeset*/
    @RequestMapping(value = "/admins/imei/routeset", method = RequestMethod.GET)
    public void GetImeiRouteSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            JsonObject json = new JsonObject();
            json.add("route_en", gson.toJsonTree(pupil.getRoute_en()));
            json.add("gps_mode", gson.toJsonTree(pupil.getGps_mode()));
            json.add("gps_interval", gson.toJsonTree(pupil.getGps_interval()));

            JsonArray array = new JsonArray();
            List<RouteSet> routeSets = actionService.selRouteSetByUser(pupil.getUser_id(), WatchProperty.EnableStatus.ALL);
            for (RouteSet routeSet : routeSets) {
                JsonObject item = new JsonObject();
                item.add("route_period_id", gson.toJsonTree(routeSet.getId()));
                item.add("set_en", gson.toJsonTree(routeSet.getSet_en()));
                item.add("start", gson.toJsonTree(routeSet.getStart()));
                item.add("stop", gson.toJsonTree(routeSet.getStop()));
                item.add("weeks", gson.toJsonTree(routeSet.getWeeks()));
                array.add(item);
            }
            json.add("periods", array);

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.14 POST /admins/imei/routeset/period*/
    @RequestMapping(value = "/admins/imei/routeset/period", method = RequestMethod.POST)
    public void PostImeiRouteSetPer(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "set_en", "start", "stop", "weeks"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            RouteSet routeSet = new RouteSet();
            routeSet.setId(0);
            routeSet.setUser_id(pupil.getUser_id());
            routeSet.setStart(params.get("start").getAsString());
            routeSet.setStop(params.get("stop").getAsString());
            routeSet.setWeeks(params.get("weeks").getAsInt());
            routeSet.setSet_en(params.get("set_en").getAsInt());
            routeSet.setUpdate_time(format.format(new Date()));
            actionService.inRouteSet(routeSet);

            insertEventAction(pupil.getUser_id(), "location");

            JsonObject json = new JsonObject();
            json.add("route_period_id", gson.toJsonTree(routeSet.getId()));

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.15 PUT /admins/imei/routeset/period*/
    @RequestMapping(value = "/admins/imei/routeset/period", method = RequestMethod.PUT)
    public void PutImeiRouteSetPer(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "route_period_id", "set_en", "start", "stop", "weeks"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            RouteSet routeSet = new RouteSet();
            routeSet.setId(params.get("route_period_id").getAsInt());
            routeSet.setUser_id(pupil.getUser_id());
            routeSet.setStart(params.get("start").getAsString());
            routeSet.setStop(params.get("stop").getAsString());
            routeSet.setWeeks(params.get("weeks").getAsInt());
            routeSet.setSet_en(params.get("set_en").getAsInt());
            routeSet.setUpdate_time(format.format(new Date()));
            actionService.upRouteSet(routeSet);

            insertEventAction(pupil.getUser_id(), "location");

            JsonObject json = new JsonObject();
            json.add("route_period_id", gson.toJsonTree(routeSet.getId()));

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.16 DELETE /admins/imei/routeset/period*/
    @RequestMapping(value = "/admins/imei/routeset/period", method = RequestMethod.DELETE)
    public void DeleteImeiRouteSetPer(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String route_period_id_str = req.getParameter("route_period_id");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(route_period_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            int route_period_id = Integer.valueOf(route_period_id_str);
            actionService.delRouteSet(route_period_id);

            insertEventAction(pupil.getUser_id(), "location");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.17 POST /admins/imei/penset*/
    @RequestMapping(value = "/admins/imei/penset", method = RequestMethod.POST)
    public void PostImeiPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "pen_name", "pen_type", "points",
                    "address", "rec_number"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            PenSet penSet = new PenSet();
            penSet.setUser_id(pupil.getUser_id());
            penSet.setPen_name(params.get("pen_name").getAsString());
            penSet.setPen_type(params.get("pen_type").getAsInt());
            penSet.setAddress1(params.get("address").getAsString());
            penSet.setRec_number(params.get("rec_number").getAsString());
            JsonArray points = params.getAsJsonArray("points");
            JsonObject point = points.get(0).getAsJsonObject();
            penSet.setCenter_lon(0.0);
            penSet.setCenter_lat(0.0);
            penSet.setBd_lon(point.get("longitute").getAsDouble());
            penSet.setBd_lat(point.get("latitude").getAsDouble());
            penSet.setCenter_lat_of(point.get("latitude").getAsDouble());
            penSet.setCenter_lon_of(point.get("longitute").getAsDouble());
            if (penSet.getPen_type() == WatchProperty.PenSetType.CIRCLE) {
                penSet.setRadius(params.get("radius").getAsInt());
            } else {
                penSet.setRadius(-1);
            }
            penSet.setUpdate_time(format.format(new Date()));
            penSet.setDeleted_flag(WatchProperty.DeletedFlag.UNDELETED);
            if (penSet.getPen_type() == WatchProperty.PenSetType.HEXAGON) {
                PenSetHexagon penSetHexagon = new PenSetHexagon();
                penSetHexagon.setId(0);
                penSetHexagon.setUser_id(pupil.getUser_id());
                penSetHexagon.setP2_lon(0.0);
                penSetHexagon.setP2_lon_of(points.get(1).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP2_bd_lon(points.get(1).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP2_lat(0.0);
                penSetHexagon.setP2_lat_of(points.get(1).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP2_bd_lat(points.get(1).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP3_lon(0.0);
                penSetHexagon.setP3_lon_of(points.get(2).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP3_bd_lon(points.get(2).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP3_lat(0.0);
                penSetHexagon.setP3_lat_of(points.get(2).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP3_bd_lat(points.get(2).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP4_lon(0.0);
                penSetHexagon.setP4_lon_of(points.get(3).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP4_bd_lon(points.get(3).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP4_lat(0.0);
                penSetHexagon.setP4_lat_of(points.get(3).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP4_bd_lat(points.get(3).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP5_lon(0.0);
                penSetHexagon.setP5_lon_of(points.get(4).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP5_bd_lon(points.get(4).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP5_lat(0.0);
                penSetHexagon.setP5_lat_of(points.get(4).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP5_bd_lat(points.get(4).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP6_lon(0.0);
                penSetHexagon.setP6_lon_of(points.get(5).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP6_bd_lon(points.get(5).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP6_lat(0.0);
                penSetHexagon.setP6_lat_of(points.get(5).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP6_bd_lat(points.get(5).getAsJsonObject().get("latitude").getAsDouble());

                routeService.repPenSetHexagon(penSetHexagon);

                penSet.setHexagon_id(penSetHexagon.getId());
            } else {
                penSet.setHexagon_id(-1);
            }
            routeService.repPenSet(penSet);

            JsonObject json = new JsonObject();
            json.add("pen_id", gson.toJsonTree(penSet.getId()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.18	PUT /admins/imei/penset*/
    @RequestMapping(value = "/admins/imei/penset", method = RequestMethod.PUT)
    public void PutImeiPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "pen_id", "pen_name", "pen_type", "points",
                    "address", "rec_number"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            PenSetHexagon penSet = routeService.selPenSetById(params.get("pen_id").getAsInt());
            if (penSet == null) {
                setParamWarnRes(res, "需要修改的围栏设置不存在", ErrorCode.PEN_NOT_EXIST);
                return;
            }

            if (penSet.getPen_type() == WatchProperty.PenSetType.HEXAGON) {
                routeService.delPenSetHexagon(penSet.getHexagon_id());
            }

            int pen_type = params.get("pen_type").getAsInt();
            JsonArray points = params.getAsJsonArray("points");
            if (pen_type == WatchProperty.PenSetType.HEXAGON) {
                PenSetHexagon penSetHexagon = new PenSetHexagon();
                penSetHexagon.setId(penSet.getHexagon_id());
                penSetHexagon.setUser_id(pupil.getUser_id());
                penSetHexagon.setP2_lon(0.0);
                penSetHexagon.setP2_lon_of(points.get(1).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP2_bd_lon(points.get(1).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP2_lat(0.0);
                penSetHexagon.setP2_lat_of(points.get(1).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP2_bd_lat(points.get(1).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP3_lon(0.0);
                penSetHexagon.setP3_lon_of(points.get(2).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP3_bd_lon(points.get(2).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP3_lat(0.0);
                penSetHexagon.setP3_lat_of(points.get(2).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP3_bd_lat(points.get(2).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP4_lon(0.0);
                penSetHexagon.setP4_lon_of(points.get(3).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP4_bd_lon(points.get(3).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP4_lat(0.0);
                penSetHexagon.setP4_lat_of(points.get(3).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP4_bd_lat(points.get(3).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP5_lon(0.0);
                penSetHexagon.setP5_lon_of(points.get(4).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP5_bd_lon(points.get(4).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP5_lat(0.0);
                penSetHexagon.setP5_lat_of(points.get(4).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP5_bd_lat(points.get(4).getAsJsonObject().get("latitude").getAsDouble());

                penSetHexagon.setP6_lon(0.0);
                penSetHexagon.setP6_lon_of(points.get(5).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP6_bd_lon(points.get(5).getAsJsonObject().get("longitute").getAsDouble());
                penSetHexagon.setP6_lat(0.0);
                penSetHexagon.setP6_lat_of(points.get(5).getAsJsonObject().get("latitude").getAsDouble());
                penSetHexagon.setP6_bd_lat(points.get(5).getAsJsonObject().get("latitude").getAsDouble());

                routeService.repPenSetHexagon(penSetHexagon);
                penSet.setHexagon_id(penSetHexagon.getId());
            } else {
                penSet.setHexagon_id(-1);
            }
            penSet.setPen_name(params.get("pen_name").getAsString());
            penSet.setAddress1(params.get("address").getAsString());
            penSet.setRec_number(params.get("rec_number").getAsString());
            JsonObject point = points.get(0).getAsJsonObject();
            penSet.setCenter_lon(0.0);
            penSet.setCenter_lat(0.0);
            penSet.setBd_lon(point.get("longitute").getAsDouble());
            penSet.setBd_lat(point.get("latitude").getAsDouble());
            penSet.setCenter_lat_of(point.get("latitude").getAsDouble());
            penSet.setCenter_lon_of(point.get("longitute").getAsDouble());
            if (pen_type == WatchProperty.PenSetType.CIRCLE) {
                penSet.setRadius(params.get("radius").getAsInt());
            } else {
                penSet.setRadius(-1);
            }
            penSet.setUpdate_time(format.format(new Date()));
            penSet.setDeleted_flag(WatchProperty.DeletedFlag.UNDELETED);
            routeService.repPenSet(penSet);

            JsonObject json = new JsonObject();
            json.add("pen_id", gson.toJsonTree(params.get("pen_id").getAsInt()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.19	DELETE /admins/imei/penset*/
    @RequestMapping(value = "/admins/imei/penset", method = RequestMethod.DELETE)
    public void DelImeiPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String pen_id_str = req.getParameter("pen_id");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(pen_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            int pen_id = Integer.valueOf(pen_id_str);
            PenSetHexagon penSet = routeService.selPenSetById(pen_id);
            if (penSet == null) {
                setParamWarnRes(res, "需要删除的围栏设置不存在", ErrorCode.PEN_NOT_EXIST);
                return;
            }

            if (penSet.getPen_type() == WatchProperty.PenSetType.HEXAGON) {
                routeService.delPenSetHexagon(penSet.getHexagon_id());
            }

            if (StringUtils.isBlank(penSet.getPen_name())) {
                routeService.delPenSet(penSet.getId());
            } else {
                routeService.delPenSetFlag(penSet.getId());
            }

            JsonObject json = new JsonObject();
            json.add("pen_id", gson.toJsonTree(pen_id));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.20	GET /admins/imei/penset*/
    @RequestMapping(value = "/admins/imei/penset", method = RequestMethod.GET)
    public void GetImeiPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String pen_id = req.getParameter("pen_id");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (!StringUtils.isBlank(pen_id)) {
                map.put("id", Integer.valueOf(pen_id));
            }
            JsonArray array = new JsonArray();
            List<PenSetHexagon> penSet_list = routeService.selPenSet(map);
            for (PenSetHexagon pen : penSet_list) {
                JsonObject json = new JsonObject();
                json.add("pen_id", gson.toJsonTree(pen.getId()));
                json.add("sign_in", gson.toJsonTree(""));
                json.add("pen_name", gson.toJsonTree(pen.getPen_name()));
                json.add("pen_type", gson.toJsonTree(pen.getPen_type()));
                JsonArray points = new JsonArray();
                JsonObject point = new JsonObject();
                point.add("latitude", gson.toJsonTree(pen.getCenter_lat_of()));
                point.add("longitute", gson.toJsonTree(pen.getCenter_lon_of()));
                points.add(point);

                if (pen.getPen_type() == WatchProperty.PenSetType.HEXAGON) {
                    JsonObject point2 = new JsonObject();
                    point2.add("latitude", gson.toJsonTree(pen.getP2_lat_of()));
                    point2.add("longitute", gson.toJsonTree(pen.getP2_lon_of()));
                    points.add(point2);

                    JsonObject point3 = new JsonObject();
                    point3.add("latitude", gson.toJsonTree(pen.getP3_lat_of()));
                    point3.add("longitute", gson.toJsonTree(pen.getP3_lon_of()));
                    points.add(point3);

                    JsonObject point4 = new JsonObject();
                    point4.add("latitude", gson.toJsonTree(pen.getP4_lat_of()));
                    point4.add("longitute", gson.toJsonTree(pen.getP4_lon_of()));
                    points.add(point4);

                    JsonObject point5 = new JsonObject();
                    point5.add("latitude", gson.toJsonTree(pen.getP5_lat_of()));
                    point5.add("longitute", gson.toJsonTree(pen.getP5_lon_of()));
                    points.add(point5);

                    JsonObject point6 = new JsonObject();
                    point6.add("latitude", gson.toJsonTree(pen.getP6_lat_of()));
                    point6.add("longitute", gson.toJsonTree(pen.getP6_lon_of()));
                    points.add(point6);
                }

                json.add("points", points);
                if (pen.getPen_type() == WatchProperty.PenSetType.CIRCLE) {
                    json.add("radius", gson.toJsonTree(pen.getRadius()));
                }
                json.add("address", gson.toJsonTree(pen.getAddress1()));
                json.add("rec_number", gson.toJsonTree(pen.getRec_number()));
                json.add("deleted", gson.toJsonTree(pen.getDeleted_flag()));
                array.add(json);
            }

            content.setSuccess_message(array);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.21	POST /admins/imei/message*/
    @RequestMapping(value = "/admins/imei/message", method = RequestMethod.POST)
    public void PostImeiMessage(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("message")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            OncePlayVoice oncePlayVoice = new OncePlayVoice();
            oncePlayVoice.setId(0);
            oncePlayVoice.setUser_id(pupil.getUser_id());
            oncePlayVoice.setName("playvoice");
            oncePlayVoice.setType("once");
            oncePlayVoice.setTimes(0);
            oncePlayVoice.setGap(0);
            oncePlayVoice.setMsg(params.get("message").getAsString());
            oncePlayVoice.setReaded(WatchProperty.ReadStatus.UNREAD);
            oncePlayVoice.setCreate_time(format.format(new Date()));
            actionService.inOncePlayVoice(oncePlayVoice);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.22	GET /admins/imei/cur_location*/
    @RequestMapping(value = "/admins/imei/cur_location", method = RequestMethod.GET)
    public void GetImeiCurLocation(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            LocationAction action = new LocationAction();
            action.setId(0);
            action.setUser_id(pupil.getUser_id());
            action.setName("location");
            action.setType("once");
            action.setOp("");
            action.setReaded(WatchProperty.ReadStatus.UNREAD);
            action.setCreate_time(format.format(new Date()));
            actionService.inOnceLocation(action);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.23	GET /admins/imei/monitor*/
    @RequestMapping(value = "/admins/imei/monitor", method = RequestMethod.GET)
    public void GetImeiMonitor(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String rec_number = req.getParameter("rec_number");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(rec_number)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            Monitor action = new Monitor();
            action.setId(0);
            action.setUser_id(pupil.getUser_id());
            action.setName("monitor");
            action.setType("once");
            action.setTimer(0);
            action.setNumber(rec_number);
            action.setReaded(WatchProperty.ReadStatus.UNREAD);
            action.setCreate_time(format.format(new Date()));
            actionService.inOnceMonitor(action);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.24	GET /admins/imei/locations*/
    @RequestMapping(value = "/admins/imei/locations", method = RequestMethod.GET)
    public void GetImeiLocations(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String begin_date_str = req.getParameter("begin_date");
            String end_date_str = req.getParameter("end_date");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");

            if (StringUtils.isBlank(imei) || StringUtils.isBlank(begin_date_str)
                    || StringUtils.isBlank(end_date_str) || StringUtils.isBlank(page_size_str)
                    || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            long query_date = (new Date()).getTime() / 1000;
            if (query_date_str != null && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
            }

            long begin_date = Long.valueOf(begin_date_str);
            long end_date = Long.valueOf(end_date_str);
            double day_cnt = 0;
            if (begin_date <= end_date) {
                day_cnt = Math.ceil((end_date - begin_date) * 1.0 / (3600 * 24));
                if ((end_date - begin_date) % (3600 * 24) == 0) {
                    day_cnt++;
                }
            }
            int total = (int) day_cnt;

            begin_date = begin_date + current_page * page_size * 3600 * 24;
            long page_end_date = begin_date + page_size * 3600 * 24 - 1;
            if (page_end_date < end_date) {
                end_date = page_end_date;
            }

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            SimpleDateFormat day_format = new SimpleDateFormat("yyyyMMdd");

            JsonArray day_locations = new JsonArray();
            while (begin_date <= end_date) {
                gc.setTimeInMillis(begin_date * 1000);
                String day_str = day_format.format(gc.getTime());
                String table_name = "route";
                table_name += "_" + day_str;

                /*判断表是否存在*/
                Map<String, Object> table_cnt = routeService.selTableCnt("gps_new", table_name);
                long cnt = (long) table_cnt.get("count");
                if (cnt <= 0) {
                    JsonObject day_json = new JsonObject();
                    day_json.add("date", gson.toJsonTree(begin_date));
                    day_json.add("count", gson.toJsonTree(0));
                    day_json.add("sub_mode", gson.toJsonTree(WatchProperty.RouteType.AUTO));
                    day_json.add("inner_count", gson.toJsonTree(0));
                    day_json.add("outer_count", gson.toJsonTree(0));
                    day_json.add("locations", new JsonArray());
                    day_locations.add(day_json);
                    begin_date += 3600 * 24;
                    continue;
                }

                List<Route> routes = routeService.selRouteList(pupil.getUser_id(), table_name);
                /*判断当天是否有数据*/
                if (routes == null || routes.size() <= 0) {
                    JsonObject day_json = new JsonObject();
                    day_json.add("date", gson.toJsonTree(begin_date));
                    day_json.add("count", gson.toJsonTree(0));
                    day_json.add("sub_mode", gson.toJsonTree(WatchProperty.RouteType.AUTO));
                    day_json.add("inner_count", gson.toJsonTree(0));
                    day_json.add("outer_count", gson.toJsonTree(0));
                    day_json.add("locations", new JsonArray());
                    day_locations.add(day_json);
                    begin_date += 3600 * 24;
                    continue;
                }
                int inner_count = 0;
                int outer_count = 0;

                JsonObject day_json = new JsonObject();
                day_json.add("date", gson.toJsonTree(begin_date));
                day_json.add("count", gson.toJsonTree(routes.size()));
                day_json.add("sub_mode", gson.toJsonTree(routes.get(0).getSub_mode()));
                JsonArray locations = new JsonArray();
                for (Route route : routes) {
                    if (route.getUser_pen_id() == 0) {
                        outer_count++;
                    } else {
                        inner_count++;
                    }
                    JsonObject location = new JsonObject();
                    location.add("longitute", gson.toJsonTree(route.getLon_of()));
                    location.add("latitude", gson.toJsonTree(route.getLat_of()));
                    Date d = format.parse(route.getSub_time());
                    location.add("uploaded_at", gson.toJsonTree(d.getTime() / 1000));
                    locations.add(location);
                }
                day_json.add("inner_count", gson.toJsonTree(inner_count));
                day_json.add("outer_count", gson.toJsonTree(outer_count));
                day_json.add("locations", locations);

                day_locations.add(day_json);
                begin_date += 3600 * 24;
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(total, page_size)));
            json.add("total_count", gson.toJsonTree(total));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("day_locations", day_locations);

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.25	GET /admins/imei/commands*/
    @RequestMapping(value = "/admins/imei/commands", method = RequestMethod.GET)
    public void GetImeiCommands(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String begin_date_str = req.getParameter("begin_date");
            String end_date_str = req.getParameter("end_date");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");

            if (StringUtils.isBlank(imei) || StringUtils.isBlank(begin_date_str)
                    || StringUtils.isBlank(end_date_str) || StringUtils.isBlank(page_size_str)
                    || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = (new Date()).getTime() / 1000;
            if (query_date_str != null && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            long begin_date = Long.valueOf(begin_date_str);
            long end_date = Long.valueOf(end_date_str);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            gc.setTimeInMillis(begin_date * 1000);
            String begin_str = format.format(gc.getTime());
            gc.setTimeInMillis(end_date * 1000);
            String end_str = format.format(gc.getTime());

            map.put("user_id", pupil.getUser_id());
            map.put("begin_date", begin_str);
            map.put("end_date", end_str);
            List<UserSms> sms_list = watchUserService.qryUserSmsListByPage(map);

            JsonArray commands = new JsonArray();
            for (UserSms userSms : sms_list) {
                JsonObject json = new JsonObject();
                json.add("number", gson.toJsonTree(userSms.getNumber()));
                json.add("happended_at", gson.toJsonTree(userSms.getSubtime()));
                json.add("content", gson.toJsonTree(userSms.getMsg()));
                json.add("command", gson.toJsonTree(userSms.getCommand()));
                commands.add(json);
            }

            JsonObject commands_json = new JsonObject();
            commands_json.add("page_size", gson.toJsonTree(page_size));
            commands_json.add("current_page", gson.toJsonTree(current_page));
            commands_json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            commands_json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            commands_json.add("query_date", gson.toJsonTree(query_date));
            commands_json.add("commands", commands);
            content.setSuccess_message(commands_json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.26	GET /admins/imei/playvoice*/
    @RequestMapping(value = "/admins/imei/playvoice", method = RequestMethod.GET)
    public void GetImeiPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }
            List<PlayVoice> health = actionService.selHealthSetByUser(pupil.getUser_id(), WatchProperty.EnableStatus.ALL);
            List<PlayVoice> alarm = actionService.selAlarmSetByUser(pupil.getUser_id(), WatchProperty.EnableStatus.ALL);
            List<PlayVoice> pills = actionService.selPillsSetByUser(pupil.getUser_id(), WatchProperty.EnableStatus.ALL);
            List<PlayVoice> sports = actionService.selSportSetByUser(pupil.getUser_id(), WatchProperty.EnableStatus.ALL);
            List<PlayVoice> sleep = actionService.selSleepSetByUser(pupil.getUser_id(), WatchProperty.EnableStatus.ALL);

            JsonArray array = new JsonArray();
            int alarm_event_id = 0;
            ConstantDescription constant = constantService.selConstantByExtra("twatch_alarm_event", "jiankang");
            if (constant != null) {
                alarm_event_id = constant.getConstant();
            }
            for (PlayVoice action : health) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree(alarm_event_id));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            alarm_event_id = 0;
            constant = constantService.selConstantByExtra("twatch_alarm_event", "alarm");
            if (constant != null) {
                alarm_event_id = constant.getConstant();
            }
            for (PlayVoice action : alarm) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree(alarm_event_id));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            alarm_event_id = 0;
            constant = constantService.selConstantByExtra("twatch_alarm_event", "pills");
            if (constant != null) {
                alarm_event_id = constant.getConstant();
            }
            for (PlayVoice action : pills) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree(alarm_event_id));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            alarm_event_id = 0;
            constant = constantService.selConstantByExtra("twatch_alarm_event", "sport");
            if (constant != null) {
                alarm_event_id = constant.getConstant();
            }
            for (PlayVoice action : sports) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree(alarm_event_id));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            alarm_event_id = 0;
            constant = constantService.selConstantByExtra("twatch_alarm_event", "sleep");
            if (constant != null) {
                alarm_event_id = constant.getConstant();
            }
            for (PlayVoice action : sleep) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree(alarm_event_id));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            content.setSuccess_message(array);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.27	POST /admins/imei/playvoice*/
    @RequestMapping(value = "/admins/imei/playvoice", method = RequestMethod.POST)
    public void PostImeiPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "eventname", "start", "times",
                    "interval", "weeks", "playvoice_en"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            String eventname = "";
            int eventname_id = params.get("eventname").getAsInt();
            ConstantDescription constant = null;
            if (eventname_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_alarm_event", eventname_id);
                if (constant != null) {
                    eventname = constant.getExtra();
                }
            }

            PlayVoice action = new PlayVoice();
            action.setId(0);
            action.setEventname(eventname);
            action.setStart(params.get("start").getAsString());
            if (params.has("msg")) {
                action.setMsg(params.get("msg").getAsString());
            } else {
                action.setMsg("");
            }
            action.setTimes(params.get("times").getAsInt());
            action.setGap(params.get("interval").getAsInt());
            action.setWeeks(params.get("weeks").getAsInt());
            action.setEnable(params.get("playvoice_en").getAsInt());
            action.setUpdate_time(format.format(new Date()));
            action.setUser_id(pupil.getUser_id());
            actionService.inEventPlayVoice(action);

            insertEventAction(pupil.getUser_id(), "playvoice");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.28	PUT /admins/imei/playvoice*/
    @RequestMapping(value = "/admins/imei/playvoice", method = RequestMethod.PUT)
    public void PutImeiPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "eventname", "eventid", "start",
                    "times", "interval", "weeks", "playvoice_en"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            String eventname = "";
            int eventname_id = params.get("eventname").getAsInt();
            ConstantDescription constant = null;
            if (eventname_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_alarm_event", eventname_id);
                if (constant != null) {
                    eventname = constant.getExtra();
                }
            }

            PlayVoice action = new PlayVoice();
            action.setId(params.get("eventid").getAsInt());
            action.setUser_id(pupil.getUser_id());
            action.setStart(params.get("start").getAsString());
            if (params.has("msg")) {
                action.setMsg(params.get("msg").getAsString());
            } else {
                action.setMsg("");
            }
            action.setTimes(params.get("times").getAsInt());
            action.setGap(params.get("interval").getAsInt());
            action.setWeeks(params.get("weeks").getAsInt());
            action.setEnable(params.get("playvoice_en").getAsInt());
            action.setUpdate_time(format.format(new Date()));
            actionService.upPlayVoiceByName(action, eventname);

            insertEventAction(pupil.getUser_id(), "playvoice");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.29	DELETE /admins/imei/playvoice*/
    @RequestMapping(value = "/admins/imei/playvoice", method = RequestMethod.DELETE)
    public void DelImeiPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            String eventname_str = req.getParameter("eventname");
            String eventid_str = req.getParameter("eventid");

            if (StringUtils.isBlank(imei) || StringUtils.isBlank(eventname_str)
                    || StringUtils.isBlank(eventid_str) || eventname_str.equals("0")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }
            int eventid = Integer.valueOf(eventid_str);

            String eventname = "";
            int eventname_id = Integer.valueOf(eventname_str);
            ConstantDescription constant = null;
            if (eventname_id != 0) {
                constant = constantService.selDescriptionByConstant("twatch_alarm_event", eventname_id);
                if (constant != null) {
                    eventname = constant.getExtra();
                }
            }

            actionService.delPlayVoiceByName(eventid, eventname);
            insertEventAction(pupil.getUser_id(), "playvoice");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.3.10.30	PUT /admins/imei/heartbeat*/
    @RequestMapping(value = "/admins/imei/heartbeat", method = RequestMethod.PUT)
    public void PutImeiHeartbeat(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            HeartBeatInf heartBeat = new HeartBeatInf();
            heartBeat.setId(0);
            heartBeat.setUser_id(pupil.getUser_id());
            heartBeat.setGap(20);
            heartBeat.setCreate_time(format.format(new Date()));
            heartBeat.setUpdate_time(format.format(new Date()));
            watchUserService.repUserHeartBeat(heartBeat);

            if (!StringUtils.isBlank(pupil.getNumber())) {
                JsonArray array = new JsonArray();
                JsonObject json = new JsonObject();
                json.add("msg", gson.toJsonTree("{HT#" + pupil.getPasswd() + "#}"));
                json.add("number", gson.toJsonTree(pupil.getNumber()));
                array.add(json);
                JsonObject push_json = new JsonObject();
                push_json.add("sms_list", array);

                AndroidPushMessageForPhone ap = new AndroidPushMessageForPhone(apiKey, secretKey);
                AndroidPushMessage msg = new AndroidPushMessage(0, gson.toJson(push_json));
                ap.pushMsgToAll(msg);
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

    /*3.3.10.31	GET /admins/imei/downloaded*/
    @RequestMapping(value = "/admins/imei/downloaded", method = RequestMethod.GET)
    public void GetImeiDownloaded(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            long count = 0;
            Map<String, Object> map = actionService.selSendSmsCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            map = actionService.selPlayVoiceCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            map = actionService.selMonitorCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            map = actionService.selLocationCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            map = actionService.selOncePowerOffCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            map = actionService.selOnceClearSmsCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            map = actionService.selActionSettingCntByUser(pupil.getUser_id(), WatchProperty.ReadStatus.UNREAD);
            if (map != null) count += (long) map.get("count");

            JsonObject json = new JsonObject();
            if (count >= 0) {
                json.add("downloaded", gson.toJsonTree(0));
            } else {
                json.add("downloaded", gson.toJsonTree(1));
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

    private void insertEventAction(int user_id, String event_name) {
        Map<String, Object> cnt = actionService.selActionSettingCntByUserAndAction(user_id,
                WatchProperty.ReadStatus.UNREAD, event_name);
        long count = (long) cnt.get("count");
        if (count > 0) return;

        ActionSetting actionSetting = new ActionSetting();
        actionSetting.setId(0);
        actionSetting.setUser_id(user_id);
        actionSetting.setName(event_name);
        actionSetting.setType("event");
        actionSetting.setReaded(WatchProperty.ReadStatus.UNREAD);
        actionSetting.setCreate_time(format.format(new Date()));
        actionService.inActionSetting(actionSetting);
    }

    /*定位地图*/
    @RequestMapping(value = "/s", method = RequestMethod.GET)
    public ModelAndView getLocMap(HttpServletRequest req) {
        String s = req.getParameter("s");
        req.getSession().removeAttribute("latlng");
        ModelAndView model = new ModelAndView("WEB-INF/jsp/bMap.jsp");
        if (StringUtils.isBlank(s)) {
            model.addObject("error", "错误的参数！");
        } else {
            try {
                s = CharUtils.CharToLatLng(s);
                req.getSession().setAttribute("latlng", s);
                model.addObject("latlng", s);
            } catch (Exception var2) {
                model.addObject("error", "格式不正确");
            }
        }
        return model;
    }

    /*谷歌地图*/
    @RequestMapping(value = "/gMap", method = RequestMethod.GET)
    public ModelAndView getLocGMap() {
        ModelAndView model = new ModelAndView("WEB-INF/jsp/gMap.jsp");
        return model;
    }

    /*百度地图*/
    @RequestMapping(value = "/bMap", method = RequestMethod.GET)
    public ModelAndView getLocBMap() {
        ModelAndView model = new ModelAndView("WEB-INF/jsp/bMap.jsp");
        return model;
    }
}