package com.jxyq.app.user_login;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.model.others.PushService;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class ExhibitionInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/open/exhibition", method = RequestMethod.GET)
    public void ExhibitionInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();

        try {
            List<Map<String, Object>> exc = userService.getExhibition();
            content.setSuccess_message(exc);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/api/user/push_service", method = RequestMethod.POST)
    public void PushServiceInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParams[] = {"device", "phone"};
            if (!checkNecessaryParam(params, necessaryParams)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            int device = params.get("device").getAsInt();
            String phone = params.get("phone").getAsString();
            PushService service = userService.selPushServiceByUid(user.getUser_id());
            boolean hasService = true;
            if (service == null) {
                hasService = false;
                service = new PushService();
                service.setPush_service_id(0);
            }

            service.setPhone(phone);
            service.setUid(user.getUser_id());
            service.setDevice(device);
            if (device == BeanProperty.ComputeType.ANDROID) {
                if (!params.has("channel_id") || !params.has("user_id")) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }
                service.setChannel_id(params.get("channel_id").getAsString());
                service.setUser_id(params.get("user_id").getAsString());
                service.setPush_token("");
            } else {
                if (!params.has("push_token")) {
                    setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                    return;
                }
                service.setChannel_id("");
                service.setUser_id("");
                service.setPush_token(params.get("push_token").getAsString());
            }
            service.setStatus(BeanProperty.UserStatus.NORMAL);

            if (hasService) {
                userService.upPushService(service);
            } else {
                userService.inPushService(service);
            }

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }
}
