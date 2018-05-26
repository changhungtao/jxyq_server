package com.jxyq.controller.shiro;

import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.model.others.OneTimeToken;
import com.jxyq.model.user.Consumption;
import com.jxyq.model.user.ConsumptionRule;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DiscuzController extends BaseInterface {

    @Autowired
    private UserService userService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @RequestMapping(value = "/bbs", method = RequestMethod.GET)
    public String GetBbs(HttpServletRequest req, HttpServletResponse res, Model model) {
        try {
            String credential = req.getParameter("credential");
            if (StringUtils.isBlank(credential)) {
                model.addAttribute("error_msg", "令牌不存在或已过期");
                return "WEB-INF/jsp/bbs_login.jsp";
            }


            long now = (new Date().getTime()) / 1000;
            OneTimeToken oneTimeToken = userService.selOneTimeToken(credential);
            if (oneTimeToken == null || oneTimeToken.getValid_before() < now) {
                model.addAttribute("error_msg", "令牌不存在或已过期");
                return "WEB-INF/jsp/bbs_login.jsp";
            }

            User user = userService.selUserByUserId(oneTimeToken.getUser_id());
            if (user == null) {
                model.addAttribute("error_msg", "用户不存在或被删除");
                return "WEB-INF/jsp/bbs_login.jsp";
            }

            Map<String, Object> map = userService.selUserBbsPassword(user.getUser_id());
            if (map == null) {
                model.addAttribute("error_msg", "用户未注册论坛");
                return "WEB-INF/jsp/bbs_login.jsp";
            }

            long cnt = getConsumptionTodayCount(user.getUser_id(), BeanProperty.OperationType.LOGIN_BLOG);
            if (cnt <= 0) {
                inConsumptionByRule(user, BeanProperty.OperationType.LOGIN_BLOG);
            }

            model.addAttribute("username", map.get("username"));
            model.addAttribute("password", map.get("password"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "WEB-INF/jsp/bbs_login.jsp";
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
        con.setHappened_at(System.currentTimeMillis() / 1000);
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
