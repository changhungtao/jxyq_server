package com.jxyq.app.user_safe;

import com.google.gson.*;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.push.AndroidPushMessageForPhone;
import com.jxyq.commons.push.AndroidPushMessage;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.model.user.UserPupil;
import com.jxyq.model.watch.*;
import com.jxyq.service.inf.UserService;
import com.jxyq.service.inf.watch.ActionService;
import com.jxyq.service.inf.watch.RouteService;
import com.jxyq.service.inf.watch.WatchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PupilInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    @Autowired
    private WatchUserService watchUserService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private RouteService routeService;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String apiKey = SysConfig.getInstance().getProperty("push.watch_heartbeat_apiKey");
    private String secretKey = SysConfig.getInstance().getProperty("push.watch_heartbeat_secretKey");

    /*3.5.1	POST /api/user/pupils*/
    @RequestMapping(value = "/api/user/pupils", method = RequestMethod.POST)
    public void postPupils(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"name", "imei", "password"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String name = params.get("name").getAsString();
            String imei = params.get("imei").getAsString();
            String password = params.get("password").getAsString();

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils != null && userPupils.size() > 0) {
                setParamWarnRes(res, "用户已经监护该监护人", ErrorCode.DUPLICATE_DONE);
                return;
            }

            UserInf watchUser = watchUserService.selUserInfByImei(imei);
            if (watchUser == null || !password.equals(watchUser.getPasswd())) {
                setParamWarnRes(res, "被监护人账号或密码错误.", ErrorCode.PASSWORD_ERROR);
                return;
            }

            UserPupil userPupil = new UserPupil();
            userPupil.setUser_pupil_id(0);
            userPupil.setUser_id(user.getUser_id());
            userPupil.setPupil_imei(imei);
            userPupil.setPupil_name(name);
            if (params.has("avatar_url")) {
                userPupil.setPupil_avatar_url(params.get("avatar_url").getAsString());
            } else {
                userPupil.setPupil_avatar_url("");
            }
            userService.insertUserPupil(userPupil);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.2	GET /api/user/pupils*/
    @RequestMapping(value = "/api/user/pupils", method = RequestMethod.GET)
    public void getPupils(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            List<UserPupil> mapList = userService.selPupilsInf(user.getUser_id());
            JsonArray array = new JsonArray();
            for (UserPupil userPupil : mapList) {
                JsonObject json = new JsonObject();
                String imei = userPupil.getPupil_imei();
                UserInf pupil = watchUserService.selUserInfByImei(imei);
                json.add("name", gson.toJsonTree(userPupil.getPupil_name()));
                json.add("avatar_url", gson.toJsonTree(userPupil.getPupil_avatar_url()));
                json.add("imei", gson.toJsonTree(imei));
                if (pupil.getMode_type() == WatchProperty.ModeType.COMPLEX_MODE) {
                    json.add("run_mode_id", gson.toJsonTree(WatchProperty.Mode.HIDE));
                } else {
                    json.add("run_mode_id", gson.toJsonTree(pupil.getUser_mode()));
                }
                array.add(json);
            }
            content.setSuccess_message(array);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.3	GET /api/user/pupils_detail*/
    @RequestMapping(value = "/api/user/pupils_detail", method = RequestMethod.GET)
    public void getPupilDetail(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }
            UserPupil userPupil = userPupils.get(0);

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            QingSet qingSet1 = actionService.selQingSetByUser(pupil.getUser_id(), 1);
            QingSet qingSet2 = actionService.selQingSetByUser(pupil.getUser_id(), 2);
            QingSet qingset3 = actionService.selQingSetByUser(pupil.getUser_id(), 3);
            QingSet qingSet4 = actionService.selQingSetByUser(pupil.getUser_id(), 4);

            int qing_numbers = 0;
            if (qingSet1 != null) qing_numbers++;
            if (qingSet2 != null) qing_numbers++;
            if (qingset3 != null) qing_numbers++;
            if (qingSet4 != null) qing_numbers++;

            long white = 0;
            Map<String, Object> whiteSetCnt = actionService.selWhiteSetCntByUser(pupil.getUser_id());
            if (whiteSetCnt != null) {
                white = (long) whiteSetCnt.get("count");
            }

            long black = 0;
            Map<String, Object> blackSetCnt = actionService.selBlackSetCntByUser(pupil.getUser_id());
            if (blackSetCnt != null) {
                black = (long) blackSetCnt.get("count");
            }

            long mode = 0;
            Map<String, Object> modeCnt = actionService.selModeCntByUser(pupil.getUser_id());
            if (modeCnt != null) {
                mode = (long) modeCnt.get("count");
            }

            long pen = 0;
            Map<String, Object> penCnt = routeService.selPenSetCntByUserId(pupil.getUser_id());
            if (penCnt != null) {
                pen = (long) penCnt.get("count");
            }

            long route = 0;
            Map<String, Object> routeCnt = actionService.selRouteSetCntByUser(pupil.getUser_id());
            if (routeCnt != null) {
                route = (long) routeCnt.get("count");
            }

            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree(userPupil.getPupil_name()));
            json.add("imei", gson.toJsonTree(userPupil.getPupil_imei()));
            json.add("avatar_url", gson.toJsonTree(userPupil.getPupil_avatar_url()));
            if (pupil.getMode_type() == WatchProperty.ModeType.COMPLEX_MODE) {
                json.add("run_mode_id", gson.toJsonTree(WatchProperty.Mode.HIDE));
            } else {
                json.add("run_mode_id", gson.toJsonTree(pupil.getUser_mode()));
            }
            json.add("qing_numbers", gson.toJsonTree(qing_numbers));
            json.add("whiteset_numbers", gson.toJsonTree(white));
            json.add("blackset_numbers", gson.toJsonTree(black));
            json.add("mode_periods", gson.toJsonTree(mode));
            json.add("pen_numbers", gson.toJsonTree(pen));
            json.add("route_periods", gson.toJsonTree(route));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.4	DELETE /api/user/pupils*/
    @RequestMapping(value = "/api/user/pupils", method = RequestMethod.DELETE)
    public void deletePupils(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }
            userService.deleteUserPupil(userPupils.get(0).getUser_pupil_id());

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.5	PUT /api/user/pupils*/
    @RequestMapping(value = "/api/user/pupils", method = RequestMethod.PUT)
    public void PutPupils(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            UserPupil userPupil = new UserPupil();
            userPupil.setUser_id(user.getUser_id());
            userPupil.setPupil_imei(imei);
            if (params.has("name")) {
                String name = params.get("name").getAsString();
                userPupil.setPupil_name(name);
            }
            if (params.has("avatar_url")) {
                String avatar_url = params.get("avatar_url").getAsString();
                userPupil.setPupil_avatar_url(avatar_url);
            }
            userService.upUserPupil(userPupil);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.6	GET /api/user/pupils/modeset*/
    @RequestMapping(value = "/api/user/pupils/modeset", method = RequestMethod.GET)
    public void getPupilModeSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject json = new JsonObject();
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.7	PUT /api/user/pupils/modeset*/
    @RequestMapping(value = "/api/user/pupils/modeset", method = RequestMethod.PUT)
    public void putPupilModeSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("defaultmode")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }


    /*3.5.8	POST /api/user/pupils/mode_periods*/
    @RequestMapping(value = "/api/user/pupils/mode_periods", method = RequestMethod.POST)
    public void postPupilModePeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("mode_periods")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.9	DELETE /api/user/pupils/mode_periods*/
    @RequestMapping(value = "/api/user/pupils/mode_periods", method = RequestMethod.DELETE)
    public void DelPupilModePeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String period_id_str = req.getParameter("period_id");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(period_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
                setRequestErrorRes(res, "模式时段不存在或无权限", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            actionService.delModeInf(period_id);

            insertEventAction(pupil.getUser_id(), "runmode");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.10	PUT /api/user/pupils/mode_periods*/
    @RequestMapping(value = "/api/user/pupils/mode_periods", method = RequestMethod.PUT)
    public void putPupilModePeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("period")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }


            JsonObject period = params.getAsJsonObject("period");
            ModeInf modeInf = actionService.selModeInfById(period.get("period_id").getAsInt());
            if (modeInf == null || modeInf.getUser_id() != pupil.getUser_id()) {
                setRequestErrorRes(res, "模式时段不存在或无权限", ErrorCode.REQUEST_PARAM_MISS);
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.11 POST /api/user/pupils/numbers*/
    @RequestMapping(value = "/api/user/pupils/numbers", method = RequestMethod.POST)
    public void postPupilNumbers(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "name1", "qing1", "name2", "qing2",
                    "name3", "qing3", "name4", "qing4", "sosname",
                    "sos"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            String name = params.get("name1").getAsString();
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

            name = params.get("name2").getAsString();
            qing = params.get("qing2").getAsString();
            qingSet.setId(0);
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            actionService.inQingSet(qingSet, 2);

            name = params.get("name3").getAsString();
            qing = params.get("qing3").getAsString();
            qingSet.setId(0);
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            actionService.inQingSet(qingSet, 3);

            name = params.get("name4").getAsString();
            qing = params.get("qing4").getAsString();
            qingSet.setId(0);
            qingSet.setRelation(name);
            qingSet.setPhone2(qing);
            actionService.inQingSet(qingSet, 4);

            String sosname = params.get("sosname").getAsString();
            String sos = params.get("sos").getAsString();
            actionService.upSOSNumber(pupil.getUser_id(), sosname, sos);

            insertEventAction(pupil.getUser_id(), "qingset");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.12 GET /api/user/pupils/numbers*/
    @RequestMapping(value = "/api/user/pupils/numbers", method = RequestMethod.GET)
    public void getPupilNumbers(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
                json.add("name1", gson.toJsonTree(qing1.getRelation()));
                json.add("qing1", gson.toJsonTree(qing1.getPhone2()));
            } else {
                json.add("name1", gson.toJsonTree(""));
                json.add("qing1", gson.toJsonTree(""));
            }

            if (qing2 != null) {
                json.add("name2", gson.toJsonTree(qing2.getRelation()));
                json.add("qing2", gson.toJsonTree(qing2.getPhone2()));
            } else {
                json.add("name2", gson.toJsonTree(""));
                json.add("qing2", gson.toJsonTree(""));
            }

            if (qing3 != null) {
                json.add("name3", gson.toJsonTree(qing3.getRelation()));
                json.add("qing3", gson.toJsonTree(qing3.getPhone2()));
            } else {
                json.add("name3", gson.toJsonTree(""));
                json.add("qing3", gson.toJsonTree(""));
            }

            if (qing4 != null) {
                json.add("name4", gson.toJsonTree(qing4.getRelation()));
                json.add("qing4", gson.toJsonTree(qing4.getPhone2()));
            } else {
                json.add("name4", gson.toJsonTree(""));
                json.add("qing4", gson.toJsonTree(""));
            }

            if (sos != null) {
                json.add("sosname", gson.toJsonTree(sos.get("name")));
                json.add("sos", gson.toJsonTree(sos.get("phone")));
            } else {
                json.add("sos", gson.toJsonTree(""));
                json.add("sosname", gson.toJsonTree("SOS"));
            }

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.13 POST /api/user/pupils/whitelist*/
    @RequestMapping(value = "/api/user/pupils/whitelist", method = RequestMethod.POST)
    public void PostPupilWhiteList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("whitelist")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.14 GET /api/user/pupils/whitelist*/
    @RequestMapping(value = "/api/user/pupils/whitelist", method = RequestMethod.GET)
    public void GetPupilWhiteList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.15 POST /api/user/pupils/blacklist*/
    @RequestMapping(value = "/api/user/pupils/blacklist", method = RequestMethod.POST)
    public void PostPupilBlackList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("blacklist")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.16 GET /api/user/pupils/blacklist*/
    @RequestMapping(value = "/api/user/pupils/blacklist", method = RequestMethod.GET)
    public void GetPupilBlackList(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.17 GET /api/user/pupils/routeset*/
    @RequestMapping(value = "/api/user/pupils/routeset", method = RequestMethod.GET)
    public void GetPupilRouteSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.18 POST /api/user/pupils/routeset*/
    @RequestMapping(value = "/api/user/pupils/routeset", method = RequestMethod.POST)
    public void PostPupilRouteSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "route_en", "gps_mode", "gps_interval"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.19 POST /api/user/pupils/routeset/period*/
    @RequestMapping(value = "/api/user/pupils/routeset/period", method = RequestMethod.POST)
    public void PostPupilRouteSetPer(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "set_en", "start", "stop", "weeks"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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

    /*3.5.20 PUT /api/user/pupils/routeset/period*/
    @RequestMapping(value = "/api/user/pupils/routeset/period", method = RequestMethod.PUT)
    public void PutPupilRouteSetPer(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "route_period_id", "set_en", "start", "stop", "weeks"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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

    /*3.5.21 DELETE /api/user/pupils/routeset/period*/
    @RequestMapping(value = "/api/user/pupils/routeset/period", method = RequestMethod.DELETE)
    public void DeletePupilRouteSetPer(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String route_period_id_str = req.getParameter("route_period_id");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(route_period_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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


    /*3.5.22 POST /api/user/pupils/penset*/
    @RequestMapping(value = "/api/user/pupils/penset", method = RequestMethod.POST)
    public void PostPupilPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "pen_name", "pen_type", "points",
                    "address", "rec_number"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
            penSet.setCenter_lon(point.get("longitute").getAsDouble());
            penSet.setCenter_lat(point.get("latitude").getAsDouble());
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.23 PUT /api/user/pupils/penset*/
    @RequestMapping(value = "/api/user/pupils/penset", method = RequestMethod.PUT)
    public void PutPupilPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "pen_id", "pen_name", "pen_type", "points",
                    "address", "rec_number"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.24 DELETE /api/user/pupils/penset*/
    @RequestMapping(value = "/api/user/pupils/penset", method = RequestMethod.DELETE)
    public void DelPupilPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String pen_id_str = req.getParameter("pen_id");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(pen_id_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.25 GET /api/user/pupils/penset*/
    @RequestMapping(value = "/api/user/pupils/penset", method = RequestMethod.GET)
    public void GetPupilPenSet(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String pen_id = req.getParameter("pen_id");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
            map.put("user_id", pupil.getUser_id());

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.26 POST /api/user/pupils/message*/
    @RequestMapping(value = "/api/user/pupils/message", method = RequestMethod.POST)
    public void PostPupilMessage(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            if (!params.has("imei") || !params.has("message")) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.27 GET /api/user/pupils/cur_location*/
    @RequestMapping(value = "/api/user/pupils/cur_location", method = RequestMethod.GET)
    public void GetPupilCurLocation(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.28 GET /api/user/pupils/monitor*/
    @RequestMapping(value = "/api/user/pupils/monitor", method = RequestMethod.GET)
    public void GetPupilMonitor(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String rec_number = req.getParameter("rec_number");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(rec_number)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.29 GET /api/user/pupils/locations*/
    @RequestMapping(value = "/api/user/pupils/locations", method = RequestMethod.GET)
    public void GetPupilLocations(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String begin_date_str = req.getParameter("begin_date");
            String end_date_str = req.getParameter("end_date");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(begin_date_str) || StringUtils.isBlank(end_date_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            Date today = new Date();
            long now = today.getTime() / 1000;
            SimpleDateFormat day_format = new SimpleDateFormat("yyyyMMdd");
            long begin_date = Long.valueOf(begin_date_str);
            long end_date = Long.valueOf(end_date_str);
            if (end_date > now) {
                end_date = now;
            }

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            gc.setTimeInMillis(now * 1000);
            String today_str = day_format.format(gc.getTime());
            JsonArray day_locations = new JsonArray();

            while (begin_date <= end_date) {
                gc.setTimeInMillis(begin_date * 1000);
                String day_str = day_format.format(gc.getTime());
                String table_name = "route";
                table_name += "_" + day_str;

                Map<String, Object> table_cnt = routeService.selTableCnt("gps_new", table_name);
                long cnt = (long) table_cnt.get("count");
                if (cnt <= 0) {
                    begin_date += 3600 * 24;
                    continue;
                }
                List<Route> routes = routeService.selRouteList(pupil.getUser_id(), table_name);
                if (routes == null || routes.size() <= 0) {
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

            content.setSuccess_message(day_locations);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.30 GET /api/user/pupils/commands*/
    @RequestMapping(value = "/api/user/pupils/commands", method = RequestMethod.GET)
    public void GetPupilCommands(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String begin_date_str = req.getParameter("begin_date");
            String end_date_str = req.getParameter("end_date");
            if (StringUtils.isBlank(imei) || StringUtils.isBlank(begin_date_str) || StringUtils.isBlank(end_date_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            long begin_date = Long.valueOf(begin_date_str);
            long end_date = Long.valueOf(end_date_str);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
            gc.setTimeInMillis(begin_date * 1000);
            String begin_str = format.format(gc.getTime());
            gc.setTimeInMillis(end_date * 1000);
            String end_str = format.format(gc.getTime());

            List<UserSms> sms_list = watchUserService.selUserSmsList(pupil.getUser_id(), begin_str, end_str);

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
            commands_json.add("commands", commands);

            content.setSuccess_message(commands_json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.31 GET /api/user/pupils/playvoice*/
    @RequestMapping(value = "/api/user/pupils/playvoice", method = RequestMethod.GET)
    public void GetPupilPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
            for (PlayVoice action : health) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree("jiankang"));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            for (PlayVoice action : alarm) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree("alarm"));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            for (PlayVoice action : pills) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree("pills"));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            for (PlayVoice action : sports) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree("sport"));
                json.add("eventid", gson.toJsonTree(action.getId()));
                json.add("start", gson.toJsonTree(action.getStart()));
                json.add("msg", gson.toJsonTree(action.getMsg()));
                json.add("times", gson.toJsonTree(action.getTimes()));
                json.add("interval", gson.toJsonTree(action.getGap()));
                json.add("weeks", gson.toJsonTree(action.getWeeks()));
                json.add("playvoice_en", gson.toJsonTree(action.getEnable()));
                array.add(json);
            }

            for (PlayVoice action : sleep) {
                JsonObject json = new JsonObject();
                json.add("eventname", gson.toJsonTree("sleep"));
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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.32 POST /api/user/pupils/playvoice*/
    @RequestMapping(value = "/api/user/pupils/playvoice", method = RequestMethod.POST)
    public void PostPupilPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "eventname", "start", "msg", "times",
                    "interval", "weeks", "playvoice_en"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            String eventname = params.get("eventname").getAsString();
            PlayVoice action = new PlayVoice();
            action.setId(0);
            action.setEventname(eventname);
            action.setStart(params.get("start").getAsString());
            action.setMsg(params.get("msg").getAsString());
            action.setTimes(params.get("times").getAsInt());
            action.setGap(params.get("interval").getAsInt());
            action.setWeeks(params.get("weeks").getAsInt());
            action.setEnable(params.get("playvoice_en").getAsInt());
            action.setUpdate_time(format.format(new Date()));
            action.setUser_id(pupil.getUser_id());
            actionService.inEventPlayVoice(action);

            insertEventAction(pupil.getUser_id(), "playvoice");

            JsonObject json = new JsonObject();
            json.add("eventname", gson.toJsonTree(eventname));
            json.add("eventid", gson.toJsonTree(action.getId()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.33 PUT /api/user/pupils/playvoice*/
    @RequestMapping(value = "/api/user/pupils/playvoice", method = RequestMethod.PUT)
    public void PutPupilPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei", "eventname", "eventid", "start", "msg",
                    "times", "interval", "weeks", "playvoice_en"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();
            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }

            String eventname = params.get("eventname").getAsString();
            PlayVoice action = new PlayVoice();
            action.setId(params.get("eventid").getAsInt());
            action.setUser_id(pupil.getUser_id());
            action.setStart(params.get("start").getAsString());
            action.setMsg(params.get("msg").getAsString());
            action.setTimes(params.get("times").getAsInt());
            action.setGap(params.get("interval").getAsInt());
            action.setWeeks(params.get("weeks").getAsInt());
            action.setEnable(params.get("playvoice_en").getAsInt());
            action.setUpdate_time(format.format(new Date()));
            actionService.upPlayVoiceByName(action, eventname);

            insertEventAction(pupil.getUser_id(), "playvoice");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.34 DELETE /api/user/pupils/playvoice*/
    @RequestMapping(value = "/api/user/pupils/playvoice", method = RequestMethod.DELETE)
    public void DelPupilPlayVoice(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            String eventname = req.getParameter("eventname");
            String eventid_str = req.getParameter("eventid");

            if (StringUtils.isBlank(imei) || StringUtils.isBlank(eventname) || StringUtils.isBlank(eventid_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

            UserInf pupil = watchUserService.selUserInfByImei(imei);
            if (pupil == null) {
                setParamWarnRes(res, "被监护人不存在", ErrorCode.PUPIL_NOT_EXIST);
                return;
            }
            int eventid = Integer.valueOf(eventid_str);

            actionService.delPlayVoiceByName(eventid, eventname);
            insertEventAction(pupil.getUser_id(), "playvoice");

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.35 PUT /api/user/pupils/heartbeat*/
    @RequestMapping(value = "/api/user/pupils/heartbeat", method = RequestMethod.PUT)
    public void PutPupilHeartbeat(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"imei"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String imei = params.get("imei").getAsString();

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
                return;
            }

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
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.5.36 GET /api/user/pupils/downloaded*/
    @RequestMapping(value = "/api/user/pupils/downloaded", method = RequestMethod.GET)
    public void GetPupilDownloaded(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String imei = req.getParameter("imei");
            if (StringUtils.isBlank(imei)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            List<UserPupil> userPupils = userService.selPupilsInf(user.getUser_id(), imei);
            if (userPupils == null || userPupils.size() <= 0) {
                setParamWarnRes(res, "用户未监护该被监护人", ErrorCode.NO_PERMISSION);
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
        } catch (Exception e) {
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

}
