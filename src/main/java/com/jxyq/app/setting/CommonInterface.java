package com.jxyq.app.setting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.others.OneTimeToken;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.DeviceService;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommonInterface extends BaseInterface {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmSS");
    private int duration = SysConfig.getInstance().getPropertyInt("one_time_token.duration");

    /*3.11.1 GET /api/product_types*/
    @RequestMapping(value = "/api/product_types", method = RequestMethod.GET)
    public void GetProductTypes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> products = deviceService.selProductIdAndName(BeanProperty.UserStatus.NORMAL);

            content.setSuccess_message(products);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.11.2 GET /api/device_types*/
    @RequestMapping(value = "/api/device_types", method = RequestMethod.GET)
    public void GetDeviceTypes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            Map<String, Object> map = new HashMap<>();
            String product_type_id_str = req.getParameter("product_type_id");
            if (!StringUtils.isBlank(product_type_id_str)) {
                map.put("product_type_id", Integer.valueOf(product_type_id_str));
            }
            map.put("status", BeanProperty.UserStatus.NORMAL);
            List<Map<String, Object>> devices = deviceService.selDeviceIdAndName(map);

            content.setSuccess_message(devices);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.11.3 GET /api/user/credential*/
    @RequestMapping(value = "/api/user/credential", method = RequestMethod.GET)
    public void GetCredential(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            long now = (new Date().getTime()) / 1000;

            JsonObject json = new JsonObject();

            OneTimeToken oneTimeToken = userService.selOneTimeToken(user.getUser_id());
            if (oneTimeToken != null && oneTimeToken.getValid_before() > now) {
                json.add("credential", gson.toJsonTree(oneTimeToken.getToken()));
                content.setSuccess_message(json);
                setResponseContent(res, content);
                return;
            }

            userService.delOneTimeToken(user.getUser_id());
            String credential = passwordEncoder.encodePassword(user.getUser_id() + "_" + now, CommonUtil.getRandomString(6));
            OneTimeToken token = new OneTimeToken();
            token.setOne_time_token_id(0);
            token.setUser_id(user.getUser_id());
            token.setToken(credential);
            token.setCreated_at(now);
            token.setValid_before(now + duration * 60 * 60);
            token.setStatus(BeanProperty.UserStatus.NORMAL);
            userService.inOneTimeToken(token);

            json.add("credential", gson.toJsonTree(credential));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }
}
