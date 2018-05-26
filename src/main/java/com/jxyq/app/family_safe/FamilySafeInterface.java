package com.jxyq.app.family_safe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.model.family.Camera;
import com.jxyq.model.family.CameraMore;
import com.jxyq.model.family.TouchButton;
import com.jxyq.model.family.TouchButtonEvent;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.FamilyService;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FamilySafeInterface extends BaseInterface {
    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserService userService;

    /*3.7.1	GET /api/user/cameras*/
    @RequestMapping(value = "/api/user/cameras", method = RequestMethod.GET)
    public void GetUserCameras(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            if (!StringUtils.isBlank(phone)) {
                user = userService.selUserByPhone(phone);
                if (user == null) {
                    setParamWarnRes(res, "需要查询的手机号未注册", ErrorCode.NO_REGISTERED);
                    return;
                }
            }
            Map<String, Object> map=new HashMap<>();
            map.put("user_id",user.getUser_id());
            List<CameraMore> cameraMore = familyService.selCameraList(map);

            JsonObject json = new JsonObject();
            json.add("cameras", gson.toJsonTree(cameraMore));

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.7.2	POST /api/user/cameras*/
    @RequestMapping(value = "/api/user/cameras", method = RequestMethod.POST)
    public void PostUserCameras(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"name", "dev_uid", "dev_name", "dev_pwd"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Camera camera = new Camera();
            camera.setCamera_id(0);
            camera.setDev_nickname(params.get("name").getAsString());
            camera.setUser_id(user.getUser_id());
            camera.setDev_uid(params.get("dev_uid").getAsString());
            camera.setDev_name(params.get("dev_name").getAsString());
            camera.setDev_pwd(params.get("dev_pwd").getAsString());
            camera.setView_acc("");
            camera.setView_pwd("");
            camera.setEvent_notification(-1);
            camera.setAsk_format_sdcard(-1);
            camera.setCamera_channel(-1);
            camera.setSnapshot("");
            familyService.insertCamera(camera);

            CameraMore cameraMore = new CameraMore();
            cameraMore.setCamera_id(camera.getCamera_id());
            cameraMore.setDev_uid(camera.getDev_uid());
            familyService.inCameraMore(cameraMore);

            JsonObject json = new JsonObject();
            json.add("camera_id", gson.toJsonTree(camera.getCamera_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.7.3	DELETE /api/user/cameras*/
    @RequestMapping(value = "/api/user/cameras", method = RequestMethod.DELETE)
    public void DelUserCameras(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String camera_id_str = req.getParameter("camera_id");
            String dev_uid = req.getParameter("dev_uid");
            if (StringUtils.isBlank(camera_id_str) || StringUtils.isBlank(dev_uid)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int camera_id = Integer.valueOf(camera_id_str);
            Camera camera = familyService.selectCamera(camera_id);
            if (camera == null || !camera.getDev_uid().toUpperCase().equals(dev_uid.toUpperCase())) {
                setParamWarnRes(res, "摄像头不存在,摄像头编号与UID不匹配", ErrorCode.PARAM_ERROR);
                return;
            }
            familyService.deleteCamera(camera_id);
            CameraMore cameraMore = familyService.selCameraMore(camera_id);
            if (cameraMore != null) {
                familyService.delCameraMore(camera_id);
            }

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.7.4	PUT /api/user/cameras*/
    @RequestMapping(value = "/api/user/cameras", method = RequestMethod.PUT)
    public void PutUserCameras(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            CameraMore cameraMore = getJSONParams(req, CameraMore.class);
            if (cameraMore == null) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Camera camera = familyService.selectCamera(cameraMore.getCamera_id());
            if (camera == null || camera.getUser_id() != user.getUser_id()) {
                setParamWarnRes(res, "需要修改的摄像头不存在或无权限", ErrorCode.NO_PERMISSION);
                return;
            }
            familyService.updateCamera(cameraMore);
            familyService.upCameraMore(cameraMore);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.7.5 GET /api/user/touch_buttons*/
    @RequestMapping(value = "/api/user/touch_buttons", method = RequestMethod.GET)
    public void GetUserTouchButtons(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String phone = req.getParameter("associated_phone");
            if (!StringUtils.isBlank(phone)) {
                user = userService.selUserByPhone(phone);
                if (user == null) {
                    setParamWarnRes(res, "需要查询的手机号未注册", ErrorCode.NO_REGISTERED);
                    return;
                }
            }

            JsonArray array = new JsonArray();
            List<TouchButton> buttons = familyService.selTouchButtonByUserId(user.getUser_id());
            for (TouchButton button : buttons) {
                JsonObject json = new JsonObject();
                json.add("id", gson.toJsonTree(button.getTouch_button_id()));
                json.add("name", gson.toJsonTree(button.getName()));
                json.add("uid", gson.toJsonTree(button.getDev_uid()));
                array.add(json);
            }

            JsonObject result = new JsonObject();
            result.add("touch_buttons", array);

            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.7.6	POST /api/user/touch_buttons*/
    @RequestMapping(value = "/api/user/touch_buttons", method = RequestMethod.POST)
    public void PostUserTouchButtons(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"name", "uid"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            TouchButton button = new TouchButton();
            button.setTouch_button_id(0);
            button.setUser_id(user.getUser_id());
            button.setName(params.get("name").getAsString());
            button.setDev_uid(params.get("uid").getAsString());
            button.setRegistered_at((new Date().getTime()) / 1000);
            familyService.inTouchButton(button);

            JsonObject json = new JsonObject();
            json.add("id", gson.toJsonTree(button.getTouch_button_id()));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.7.7	DELETE /api/user/touch_buttons*/
    @RequestMapping(value = "/api/user/touch_buttons", method = RequestMethod.DELETE)
    public void DeleteUserTouchButtons(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String id_str = req.getParameter("id");
            String uid = req.getParameter("uid");
            if (StringUtils.isBlank(id_str) || StringUtils.isBlank(uid)){
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int id = Integer.valueOf(id_str);

            TouchButton button = familyService.selTouchButtonById(id);
            if (button == null || button.getUser_id() != user.getUser_id() ||
                    !button.getDev_uid().toUpperCase().equals(uid.toUpperCase())) {
                setParamWarnRes(res, "按钮不存在或无权限", ErrorCode.NO_PERMISSION);
                return;
            }
            familyService.delTouchButton(id);

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*宜居通终端 POST /api/open/warning*/
    @RequestMapping(value = "/api/open/warning", method = RequestMethod.POST)
    public void PostUserWarning(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"uuid", "warnings"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            String dev_uid = params.get("uuid").getAsString();
            List<TouchButton> buttons = familyService.selTouchButtonByDevUid(dev_uid);
            if (buttons == null || buttons.size() <= 0){
                setParamWarnRes(res, "设备未与用户关联", ErrorCode.NO_RELATED);
                return;
            }

            JsonArray warnings = params.getAsJsonArray("warnings");
            for (JsonElement warning: warnings){
                JsonObject json = warning.getAsJsonObject();
                String sensor_id = json.get("sensor_id").getAsString();
                int sensor_type = json.get("sensor_type").getAsInt();
                TouchButtonEvent event = new TouchButtonEvent();
                event.setTouch_button_event_id(0);
                event.setDev_uid(dev_uid);
                event.setSensor_id(sensor_id);
                event.setSensor_type(sensor_type);
                event.setHappened_at((new Date()).getTime() / 1000);
                familyService.inTouchButtonEvent(event);
            }

            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            for (TouchButton button : buttons){
                String sms_params = button.getName();
                int user_id = button.getUser_id();
                User user = userService.selUserByUserId(user_id);
                String phone = user.getPhone();
                ucpaasCommon.sendTemplateSMS(UcpaasCommon.warning_template, phone, sms_params);
            }

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }
}
