package com.jxyq.app.filter;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.User;
import com.jxyq.model.watch.PenSetHexagon;
import com.jxyq.model.watch.Route;
import com.jxyq.service.inf.TestService;
import com.jxyq.service.inf.UserService;
import com.jxyq.service.inf.watch.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TestInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @Autowired
    private RouteService routeService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @RequestMapping(value = "/api/user/test", method = RequestMethod.GET)
    public void TestInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            User user = (User) req.getAttribute("user");
            JsonObject params = getJsonElementParams(req).getAsJsonObject();
            String text = params.get("text").getAsString();
            Map<String, Object> map = new HashMap<>();
            map.put("text", text);
            userService.insertTest(map);
            content.setSuccess_message(user);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/api/open/test", method = RequestMethod.POST)
    public void TestOpenInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            double lon = 118.78014477;
            double lat = 31.97503857;
            double lon_of = 118.78014477;
            double lat_of = 31.97503857;
            long time = format.parse("2015-06-30 00:00:30").getTime() / 1000;
            long end_time = format.parse("2015-07-01 00:00:01").getTime() / 1000;
            Random random = new Random();

            Route route = new Route(0, 982455, lon, lat, lon_of,
                    lat_of, 0, "", "", 2,
                    "", "", "", "", 2,
                    "");
            String table_name = "route_20150630";
            route.setTable_name(table_name);


            while (time < end_time){
                double delta_lon = random.nextInt(1000000);
                double delta_lat = random.nextInt(1000000);

                route.setSub_time(format.format(new Date(time * 1000)));
                route.setLon_of(lon_of + delta_lon * 0.00000001);
                route.setLat_of(lat_of + delta_lat * 0.00000001);
                routeService.insertRoute(route);

                time += 60;
            }

            content.setSuccess_message(new Object());
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }
}
