package com.jxyq.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.camera.CameraUtil;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.family.Camera;
import com.jxyq.model.family.CameraMore;
import com.jxyq.model.family.TouchButton;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.FamilyService;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FamilyController extends BaseInterface {
    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserService userService;

    //    3.3.5.1	GET /admins/cameras
    @RequestMapping(value = "/admins/cameras", method = RequestMethod.GET)
    public void GetCameras(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String phone = req.getParameter("user_phone");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");

            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (!StringUtils.isBlank(phone)) {
                map.put("phone", phone);
            }

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            long query_date = (new Date()).getTime() / 1000;
            if (query_date_str != null && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }
            map.put(BeanProperty.PAGING, pageInf);

            List<Map<String, Object>> cameraMore = familyService.qryCameraByPage(map);
            JsonArray array = new JsonArray();
            for (Map<String, Object> model : cameraMore) {
                JsonObject item = new JsonObject();
                item.add("user_phone", gson.toJsonTree(model.get("user_phone")));
                item.add("user_name", gson.toJsonTree(model.get("user_name")));
                item.add("camera_id", gson.toJsonTree(model.get("camera_id")));
                item.add("dev_nickname", gson.toJsonTree(model.get("dev_nickname")));
                item.add("dev_uid", gson.toJsonTree(model.get("dev_uid")));
                item.add("dev_name", gson.toJsonTree(model.get("dev_name")));
                item.add("dev_pwd", gson.toJsonTree(model.get("dev_pwd")));
                item.add("internet_ip", gson.toJsonTree(model.get("internet_ip")));
                item.add("http_port", gson.toJsonTree(model.get("http_port")));
                item.add("rtsp_port", gson.toJsonTree(model.get("rtsp_port")));
                /**
                 *  Fix compilation error of "model.get("rtsp_port") != 0" by Jiang Ting, 20158001
                 *  More details: http://stackoverflow.com/questions/4036167/comparing-integer-objects-vs-int
                 */
                if (!StringUtils.isBlank(String.valueOf(model.get("internet_ip"))) && !model.get("rtsp_port").equals(0)) {
                    item.add("rtsp_url_1", gson.toJsonTree("rtsp://" + String.valueOf(model.get("internet_ip"))
                            + "/" + model.get("rtsp_port")));
                } else {
                    item.add("rtsp_url_1", gson.toJsonTree(""));
                }
                array.add(item);
            }
            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("cameras", array);

            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    private String resolveHost(String host_add) throws Exception {
        URL url = new URL(host_add);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        return connection.getHeaderField("Location");
    }

    /*3.3.5.2 PUT /admins/cameras/{cid}*/
    @RequestMapping(value = "/admins/cameras/{cid}", method = RequestMethod.PUT)
    public void PutAdminCameras(@PathVariable("cid") int cid, HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String necessaryParam[] = {"act"};
            if (!checkNecessaryParam(params, necessaryParam)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Camera camera = familyService.selectCamera(cid);
            CameraMore camera_more = familyService.selCameraMore(cid);
            if (camera == null || camera_more == null) {
                setParamWarnRes(res, "摄像头不存在或信息不完整", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String action = params.get("act").getAsString();
            String cgi = "";
            if (action.equals("hscan") || action.equals("vscan")) {
                cgi = "param.cgi?cmd=ptzctrl&-step=1&-act=" + action + "&-speed=45";
            } else {
                cgi = "ptz" + action + ".cgi";
            }

            String url = "/cgi-bin/hi3510/" + cgi;

            String ddns_host = camera_more.getDdns_host();
            String ddns_username = camera_more.getDdns_username();
            int index = ddns_host.indexOf('.');
            String domain_host = "http://" + ddns_username + ddns_host.substring(index, ddns_host.length());
            String host = resolveHost(domain_host);
            /*String host = camera_more.getDdns_host();*/

            String result = CameraUtil.ctrlCamera(host + url, camera.getDev_name(), camera.getDev_pwd());
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    /*3.3.5.3 GET /admins/cameras/{cid}*/
    @RequestMapping(value = "/admins/cameras/{cid}", method = RequestMethod.GET)
    public void GetAdminCameras(@PathVariable("cid") int cid, HttpServletRequest req, HttpServletResponse res) {
        try {
            Camera camera = familyService.selectCamera(cid);
            CameraMore camera_more = familyService.selCameraMore(cid);
            if (camera == null || camera_more == null) {
                setParamWarnRes(res, "摄像头不存在或信息不完整", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            String url = "/web/tmpfs/snap.jpg";

            String ddns_host = camera_more.getDdns_host();
            String ddns_username = camera_more.getDdns_username();
            int index = ddns_host.indexOf('.');
            String domain_host = "http://" + ddns_username + ddns_host.substring(index, ddns_host.length());
            String host = resolveHost(domain_host);
            /*String host = camera_more.getDdns_host();*/

            CameraUtil.snapCamera(host + url, camera.getDev_name(), camera.getDev_pwd(), res);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }


    /*3.3.5.4 GET /admins/touch_buttons*/
    @RequestMapping(value = "/admins/touch_buttons", method = RequestMethod.GET)
    public void GetTouchButtons(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String phone = req.getParameter("user_phone");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            String query_date_str = req.getParameter("query_date");

            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            if (!StringUtils.isBlank(phone)) {
                map.put("phone", phone);
            }

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            long query_date = (new Date()).getTime() / 1000;
            if (query_date_str != null && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }
            map.put(BeanProperty.PAGING, pageInf);
            List<Map<String, Object>> buttons = familyService.qryTouchButtonByPage(map);

            JsonArray array = new JsonArray();
            for (Map<String, Object> button : buttons) {
                JsonObject json = new JsonObject();
                json.add("user_phone", gson.toJsonTree(button.get("user_phone")));
                json.add("user_name", gson.toJsonTree(button.get("user_name")));
                json.add("touch_button_id", gson.toJsonTree(button.get("touch_button_id")));
                json.add("name", gson.toJsonTree(button.get("name")));
                json.add("uid", gson.toJsonTree(button.get("dev_uid")));
                array.add(json);
            }

            JsonObject result = new JsonObject();
            result.add("page_size", gson.toJsonTree(page_size));
            result.add("current_page", gson.toJsonTree(current_page));
            result.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            result.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            result.add("query_date", gson.toJsonTree(query_date));
            result.add("touch_buttons", array);

            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }

    //    3.3.5.5	GET /admins/touch_buttons/{bid}
    @RequestMapping(value = "/admins/touch_buttons/{bid}", method = RequestMethod.GET)
    public void GetEventButtons(@PathVariable("bid") int bid,
                                HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String activated_from = req.getParameter("activated_from");
            String activated_to = req.getParameter("activated_to");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");
            if (StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("touch_button_id", bid);
            int current_page = Integer.parseInt(current_page_str);
            int page_size = Integer.parseInt(page_size_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");

            if (!StringUtils.isBlank(activated_from)) {
                map.put("activated_from", Long.valueOf(activated_from));
            }
            if (!StringUtils.isBlank(activated_to)) {
                map.put("activated_to", activated_to);
            }

            String query_date_str = req.getParameter("query_date");
            long query_date = (new Date()).getTime() / 1000;
            if (!StringUtils.isBlank(query_date_str) && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            List<Map<String, Object>> lists = familyService.selTouchEvent(map, pageInf);
            JsonArray array = new JsonArray();
            for (Map<String, Object> button : lists) {
                JsonObject json = new JsonObject();
                json.add("touch_button_event_id", gson.toJsonTree(button.get("touch_button_event_id")));
                json.add("uid", gson.toJsonTree(button.get("dev_uid")));
                json.add("sensor_id", gson.toJsonTree(button.get("sensor_id")));
                json.add("sensor_type", gson.toJsonTree(button.get("sensor_type")));
                json.add("happened_at", gson.toJsonTree(button.get("happened_at")));
                array.add(json);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("page_size", page_size);
            result.put("current_page", current_page);
            result.put("total_page", CommonUtil.getPageCount(pageInf.getTotal(), Integer.valueOf(page_size)));
            result.put("total_count", pageInf.getTotal());
            result.put("query_date", query_date);
            result.put("events", array);
            content.setSuccess_message(result);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
            return;
        }
    }
}
