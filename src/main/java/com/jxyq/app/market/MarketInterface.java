package com.jxyq.app.market;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.app.user_login.LoginInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.TokenHandler;
import com.jxyq.model.others.OneTimeToken;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.DeviceService;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class MarketInterface extends BaseInterface {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    private String token_secret = SysConfig.getInstance().getProperty("token.secret");

    /*2.1 GET /api/open/user*/
    @RequestMapping(value = "/api/open/user", method = RequestMethod.GET)
    public void GetOpenUser(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String credential = req.getParameter("credential");
            if (StringUtils.isBlank(credential)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            long now = (new Date().getTime()) / 1000;
            OneTimeToken oneTimeToken = userService.selOneTimeToken(credential);
            if (oneTimeToken == null || oneTimeToken.getValid_before() < now) {
                setRequestErrorRes(res, "令牌不存在或已过期", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            User user = userService.selUserByUserId(oneTimeToken.getUser_id());

            TokenHandler tokenHandler = new TokenHandler(token_secret);
            User token_user = new User();
            token_user.setUser_id(user.getUser_id());
            token_user.setPhone(user.getPhone());
            token_user.setPassword(user.getPassword());
            token_user.setExpires(System.currentTimeMillis() + LoginInterface.TEN_DAYS);
            String token = tokenHandler.createTokenForUser(token_user);

            res.addHeader(LoginInterface.AUTH_HEADER_NAME, token);
            JsonObject json = new JsonObject();
            json.add("user_id", gson.toJsonTree(user.getUser_id()));
            json.add("phone", gson.toJsonTree(user.getPhone()));
            json.add("points", gson.toJsonTree(user.getPoints()));
            json.add("avatar_url", gson.toJsonTree(user.getAvatar_url()));
            json.add("nick_name", gson.toJsonTree(user.getNick_name()));
            json.add("full_name", gson.toJsonTree(user.getFull_name()));
            json.add("gender", gson.toJsonTree(user.getGender()));
            json.add("birthday", gson.toJsonTree(user.getBirthday()));
            json.add("address", gson.toJsonTree(user.getAddress()));
            json.add("province_id", gson.toJsonTree(user.getProvince_id()));
            json.add("city_id", gson.toJsonTree(user.getCity_id()));
            json.add("zone_id", gson.toJsonTree(user.getZone_id()));
            json.add("qq", gson.toJsonTree(user.getQq()));
            json.add("email", gson.toJsonTree(user.getEmail()));
            json.add("height", gson.toJsonTree(user.getHeight()));
            json.add("weight", gson.toJsonTree(user.getWeight()));
            json.add("target_weight", gson.toJsonTree(user.getTarget_weight()));

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*2.3 POST /api/goods/terminal_catagories*/
    @RequestMapping(value = "/api/goods/terminal_catagories", method = RequestMethod.POST)
    public void PostGoodsTerminal(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"device_type_ids", "page_size", "current_page"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Map<String, Object> map = new HashMap<>();
            JsonArray array = params.getAsJsonArray("device_type_ids");
            if (array != null && array.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (JsonElement item : array) {
                    int device_type_id = item.getAsInt();
                    ids.add(device_type_id);
                }
                map.put("device_type_ids", ids);
            }
            if (params.has("manufactory_name")) {
                String manufactory_name = params.get("manufactory_name").getAsString();
                map.put("manufactory_name", manufactory_name);
            }
            long query_date = (new Date()).getTime() / 1000;
            if (params.has("query_date")) {
                query_date = params.get("query_date").getAsLong();
                map.put("query_date", query_date);
            }
            map.put("status", BeanProperty.UserStatus.NORMAL);
            Integer page_size = params.get("page_size").getAsInt();
            Integer current_page = params.get("current_page").getAsInt();
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            List<Map<String, Object>> terminalList = deviceService.selTerminalCat4MarketByPage(map, pageInf);
            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("terminal_catagories", gson.toJsonTree(terminalList));

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*2.4 GET /api/goods/terminal_catagories/{cid}*/
    @RequestMapping(value = "/api/goods/terminal_catagories/{cid}", method = RequestMethod.GET)
    public void GetGoodsTerminal(@PathVariable("cid") int cid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            if (cid <= 0) {
                setRequestErrorRes(res, "终端型号参数有误", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Map<String, Object> category = deviceService.selTerminalCat4Market(cid);
            if (category == null) {
                setParamWarnRes(res, "查询的终端型号不存在", ErrorCode.NO_REGISTERED);
                return;
            }

            content.setSuccess_message(category);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }
}
