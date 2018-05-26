package com.jxyq.app.user_login;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.config.SpringContextHolder;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.Consumption;
import com.jxyq.model.user.ConsumptionRule;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GetScoreInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @RequestMapping(value = "/api/user/points", method = RequestMethod.GET)
    public void GetScoreInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            String type = req.getParameter("verbose");

            JsonObject json = new JsonObject();
            json.add("points", gson.toJsonTree(user.getPoints()));
            if (!StringUtils.isBlank(type) && type.equals("1")) {
                List<ConsumptionRule> con_list = userService.qryConsumptionRule();
                JsonArray points_rule = new JsonArray();
                for (ConsumptionRule con_rule : con_list) {
                    if (con_rule.getOperation_type() == BeanProperty.OperationType.ADMIN_OPERATION) {
                        continue;
                    }
                    JsonObject con_rule_json = new JsonObject();
                    con_rule_json.add("operation_type", gson.toJsonTree(con_rule.getOperation_type()));
                    con_rule_json.add("points", gson.toJsonTree(con_rule.getPoints()));
                    points_rule.add(con_rule_json);
                }

                JsonArray points_details = new JsonArray();
                List<Consumption> con_today = getConsumptionToday(user.getUser_id());
                for (Consumption con_today_item : con_today) {
                    if (con_today_item.getOperation_type() == 1 || con_today_item.getOperation_type() == 4) {
                        continue;
                    }
                    JsonObject con_item_json = new JsonObject();
                    con_item_json.add("points", gson.toJsonTree(con_today_item.getPoints()));
                    con_item_json.add("new_points", gson.toJsonTree(con_today_item.getNew_points()));
                    con_item_json.add("operation_type", gson.toJsonTree(con_today_item.getOperation_type()));
                    con_item_json.add("description", gson.toJsonTree(con_today_item.getDescription()));
                    con_item_json.add("happened_at", gson.toJsonTree(con_today_item.getHappened_at()));
                    points_details.add(con_item_json);
                }

                Consumption consumption = userService.selConsumption(user.getUser_id(), BeanProperty.OperationType.IMPROVE_INFORMATION);
                if (consumption != null) {
                    JsonObject con_item_json = new JsonObject();
                    con_item_json.add("points", gson.toJsonTree(consumption.getPoints()));
                    con_item_json.add("new_points", gson.toJsonTree(consumption.getNew_points()));
                    con_item_json.add("operation_type", gson.toJsonTree(consumption.getOperation_type()));
                    con_item_json.add("description", gson.toJsonTree(consumption.getDescription()));
                    con_item_json.add("happened_at", gson.toJsonTree(consumption.getHappened_at()));
                    points_details.add(con_item_json);
                }

                json.add("points_details", points_details);
                json.add("points_rule", points_rule);
            }

            content.setSuccess_message(json);
            setResponseContent(res, content);
            return;
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    public List<Consumption> getConsumptionToday(int user_id) throws Exception {
        Date date = new Date();
        Date begin_date = format.parse(format.format(date));
        long begin_time = begin_date.getTime() / 1000;
        long end_time = begin_time + 86399;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("happened_from", begin_time);
        map.put("happened_to", end_time);
        List<Consumption> consumption = userService.selConsumption(map);
        return consumption;
    }

    public long getConsumptionTodayCount(int user_id, int operation_type) throws Exception {
        Date date = new Date();
        Date begin_date = format.parse(format.format(date));
        long begin_time = begin_date.getTime() / 1000;
        long end_time = begin_time + 86399;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("happened_from", begin_time);
        map.put("happened_to", end_time);
        if (operation_type != -1) {
            map.put("operation_type", operation_type);
        }
        Map<String, Object> count = userService.selConsumptionCnt(map);
        return (long) count.get("count");
    }

    public void inConsumptionByRule(User user, int operation_type) {
        ConsumptionRule con_rule = userService.selConsumptionRule(operation_type);

        Consumption con = new Consumption();
        con.setComsumption_id(0);
        con.setHappened_at((new Date()).getTime() / 1000);
        con.setUser_id(user.getUser_id());
        con.setPoints(con_rule.getPoints());
        con.setNew_points(user.getPoints() + con_rule.getPoints());
        con.setOperation_type(operation_type);
        con.setDescription(con_rule.getDescription());
        userService.inConsumption(con);

        user.setPoints(user.getPoints() + con_rule.getPoints());
        userService.upUserPoints(user);
    }
}