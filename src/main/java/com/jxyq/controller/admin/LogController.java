package com.jxyq.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.others.OperationLog;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.OperationLogService;
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
public class LogController extends BaseInterface {
    @Autowired
    private OperationLogService logService;

    /*3.3.11.1 GET /admins/users/operation_logs*/
    @RequestMapping(value = "/admins/users/operation_logs", method = RequestMethod.GET)
    public void GetOperationLog(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String happened_from_str = req.getParameter("happened_from");
            String happened_to_str = req.getParameter("happened_to");
            String page_size_str = req.getParameter("page_size");
            String current_page_str = req.getParameter("current_page");

            if (StringUtils.isBlank(happened_from_str) || StringUtils.isBlank(happened_to_str)
                    || StringUtils.isBlank(page_size_str) || StringUtils.isBlank(current_page_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            long happen_from = Long.valueOf(happened_from_str);
            long happen_to = Long.valueOf(happened_to_str);
            map.put("happen_from", happen_from);
            map.put("happen_to", happen_to);

            int page_size = Integer.valueOf(page_size_str);
            int current_page = Integer.valueOf(current_page_str);
            PagingCriteria pageInf = new PagingCriteria(current_page, page_size, "", "");
            map.put(BeanProperty.PAGING, pageInf);

            long query_date = (new Date()).getTime() / 1000;
            String query_date_str = req.getParameter("query_date");
            if (!StringUtils.isBlank(query_date_str) && !query_date_str.equals("-1")) {
                query_date = Long.valueOf(query_date_str);
                map.put("query_date", query_date);
            }

            String user_role = req.getParameter("user_role");
            if (!StringUtils.isBlank(user_role)) {
                map.put("user_role", Integer.valueOf(user_role));
            }

            String user_login_name = req.getParameter("user_login_name");
            if (!StringUtils.isBlank(user_login_name)) {
                map.put("user_login_name", user_login_name);
            }

            String op_title = req.getParameter("op_title");
            if (!StringUtils.isBlank(op_title)) {
                map.put("op_title", Integer.valueOf(op_title));
            }

            List<OperationLog> list = logService.qryOperationLogByPage(map);
            JsonArray operation_list = new JsonArray();
            for (OperationLog log : list) {
                JsonObject item = new JsonObject();
                item.add("user_role", gson.toJsonTree(log.getUser_role()));
                item.add("user_login_name", gson.toJsonTree(log.getUser_login_name()));
                item.add("op_title", gson.toJsonTree(log.getOp_title()));
                item.add("happened_at", gson.toJsonTree(log.getHappened_at()));
                item.add("origin_ip", gson.toJsonTree(log.getOrigin_ip()));
                operation_list.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("page_size", gson.toJsonTree(page_size));
            json.add("current_page", gson.toJsonTree(current_page));
            json.add("total_page", gson.toJsonTree(CommonUtil.getPageCount(pageInf.getTotal(), page_size)));
            json.add("total_count", gson.toJsonTree(pageInf.getTotal()));
            json.add("query_date", gson.toJsonTree(query_date));
            json.add("operations", operation_list);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }
}