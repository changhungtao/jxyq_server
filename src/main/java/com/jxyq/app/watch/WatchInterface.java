package com.jxyq.app.watch;

import com.google.gson.*;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.commons.push.AndroidPushMessage;
import com.jxyq.commons.push.AndroidPushMessageForPhone;
import com.jxyq.commons.push.IosPushMessageForPhone;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.model.others.PushService;
import com.jxyq.model.user.User;
import com.jxyq.model.user.UserPupil;
import com.jxyq.model.watch.*;
import com.jxyq.service.inf.TestService;
import com.jxyq.service.inf.UserService;
import com.jxyq.service.inf.watch.ActionService;
import com.jxyq.service.inf.watch.RouteService;
import com.jxyq.service.inf.watch.WatchOtherService;
import com.jxyq.service.inf.watch.WatchUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class WatchInterface {
    @Autowired
    private TestService testService;

    @Autowired
    private WatchUserService watchUserService;

    @Autowired
    private WatchOtherService watchOtherService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActionService actionService;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String phone = SysConfig.getInstance().getProperty("watch.rec_phone");

    private JsonObject getJSONParams(HttpServletRequest req) throws Exception {
        BufferedReader reader = null;
        String line = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
        String paramsStr = buffer.toString();
        JsonParser parser = new JsonParser();
        JsonObject params = parser.parse(paramsStr).getAsJsonObject();
        return params;
    }

    private HttpServletResponse setResponseContent(HttpServletResponse res, Object content) {
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = null;
        String contentJson = gson.toJson(content);
        try {
            writer = res.getWriter();
            writer.append(contentJson);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
        return res;
    }

    private JsonObject setErrorResponse(HttpServletResponse res) {
        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));

        JsonObject data = new JsonObject();
        data.add("status", gson.toJsonTree("fail"));
        data.add("heart", gson.toJsonTree(String.valueOf(300)));
        JsonArray action = new JsonArray();
        action.add(new JsonObject());
        data.add("action", action);

        body.add("data", data);
        setResponseContent(res, body);
        return body;
    }

    @RequestMapping(value = "/watch.do", method = RequestMethod.POST)
    public void postPupils(HttpServletRequest req, HttpServletResponse res) {
        try {
            JsonObject params = getJSONParams(req);
            System.out.println("POST watch.do request:" + gson.toJson(params));
            String command = params.get("command").getAsString();
            JsonObject data = params.get("data").getAsJsonObject();
            switch (command) {
                case "datasync":
                    DataSyncInterface(data, res);
                    break;
                case "heart":
                    HeartInterface(data, res);
                    break;
                case "location":
                    LocationInterface(req, data, res);
                    break;
                case "login":
                    LoginInterface(data, res);
                    break;
                case "log":
                    LogInterface(data, req, res);
                    break;
                case "poweroff":
                    PowerOffInterface(data, res);
                    break;
                default:
                    break;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            setResponseContent(res, e.toString());
        }
    }

    @RequestMapping(value = "/watch.do", method = RequestMethod.GET)
    public void getPupils(HttpServletRequest req, HttpServletResponse res) {
        try {
            String s = req.getParameter("s");
            String command = "";
            JsonObject data = new JsonObject();
            System.out.println("GET watch.do. ");
            if (!StringUtils.isBlank(s)) {
                System.out.println("request: " + s);
                JsonParser parser = new JsonParser();
                JsonObject params = parser.parse(s).getAsJsonObject();
                command = params.get("command").getAsString();
                data = params.get("data").getAsJsonObject();
            }
            switch (command) {
                case "datasync":
                    DataSyncInterface(data, res);
                    break;
                case "heart":
                    HeartInterface(data, res);
                    break;
                case "location":
                    LocationInterface(req, data, res);
                    break;
                case "login":
                    LoginInterface(data, res);
                    break;
                case "log":
                    LogInterface(data, req, res);
                    break;
                case "poweroff":
                    PowerOffInterface(data, res);
                    break;
                default:
                    break;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            setResponseContent(res, e.toString());
        }
    }

    private void DataSyncInterface(JsonObject reqData, HttpServletResponse res) {
        if (reqData == null || !reqData.has("imei")) {
            setErrorResponse(res);
            return;
        }

        String imei = reqData.get("imei").getAsString();
        UserInf user = watchUserService.selUserInfByImei(imei);
        CenterNumber centerNumber = watchOtherService.selCenterNumber();
        if (user == null || centerNumber == null) {
            setErrorResponse(res);
            return;
        }

        HeartBeatInf heartBeat = watchUserService.selUserHeartBeatByUserId(user.getUser_id());
        int interval = 3600;
        if (heartBeat != null) {
            interval = heartBeat.getGap();
        }

        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));
        JsonObject resData = new JsonObject();
        resData.add("status", gson.toJsonTree("ok"));
        resData.add("heart", gson.toJsonTree(String.valueOf(interval)));
        resData.add("imei", gson.toJsonTree(imei));
        resData.add("password", gson.toJsonTree(user.getPasswd()));
        resData.add("centernumber", gson.toJsonTree(centerNumber.getNumber()));
        resData.add("datetime", gson.toJsonTree(new Date()));
        resData.add("size", gson.toJsonTree("0"));
        resData.add("action", getDataSyncAction(user.getUser_id()));
        body.add("data", resData);
        System.out.println("watch.do response:" + gson.toJson(body));
        setResponseContent(res, body);
    }

    private void HeartInterface(JsonObject reqData, HttpServletResponse res) throws Exception {
        if (reqData == null || !reqData.has("imei")) {
            setErrorResponse(res);
            return;
        }

        String imei = reqData.get("imei").getAsString();
        UserInf user = watchUserService.selUserInfByImei(imei);
        CenterNumber centerNumber = watchOtherService.selCenterNumber();
        if (user == null || centerNumber == null) {
            setErrorResponse(res);
            return;
        }
        HeartBeatInf heartBeat = watchUserService.selUserHeartBeatByUserId(user.getUser_id());

        if (reqData.has("batlevel")) {
            user.setBatlevel(reqData.get("batlevel").getAsInt());
        }

        if (reqData.has("signal")) {
            user.setPhone_signal(reqData.get("signal").getAsInt());
        }

        if (reqData.has("batlevel") || reqData.has("signal")) {
            watchUserService.updateUserInf(user);
        }

        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));
        JsonObject resData = new JsonObject();
        resData.add("status", gson.toJsonTree("ok"));
        int interval = 3600;
        if (heartBeat != null && heartBeat.getGap() != 3600) {
            updateHeartBeatInf(heartBeat);
            interval = heartBeat.getGap();
        }
        resData.add("heart", gson.toJsonTree(String.valueOf(interval)));
        resData.add("imei", gson.toJsonTree(imei));
        resData.add("password", gson.toJsonTree(user.getPasswd()));
        resData.add("centernumber", gson.toJsonTree(centerNumber.getNumber()));
        resData.add("datetime", gson.toJsonTree(new Date()));
        resData.add("action", getHeartAction(user.getUser_id(), user.getImei()));
        body.add("data", resData);
        System.out.println("watch.do response:" + gson.toJson(body));
        setResponseContent(res, body);
    }

    private void LocationInterface(HttpServletRequest req, JsonObject reqData, HttpServletResponse res) throws Exception {
        if (reqData == null || !reqData.has("imei")) {
            setErrorResponse(res);
            return;
        }

        if (!reqData.has("imei") || !reqData.has("type") || !reqData.has("lbss")
                || !reqData.has("lbssize") || !reqData.has("gpssize")) {
            setErrorResponse(res);
            return;
        }

        if (reqData.get("type").getAsString().toUpperCase().equals("DW") && !reqData.has("number")) {
            setErrorResponse(res);
            return;
        }

        String imei = reqData.get("imei").getAsString();
        UserInf user = watchUserService.selUserInfByImei(imei);
        CenterNumber centerNumber = watchOtherService.selCenterNumber();
        if (user == null || centerNumber == null) {
            setErrorResponse(res);
            return;
        }
        HeartBeatInf heartBeat = watchUserService.selUserHeartBeatByUserId(user.getUser_id());

        String type = reqData.get("type").getAsString().toUpperCase();
        int route_type = 0;
        switch (type) {
            case "DW":
                route_type = WatchProperty.RouteType.DW;
                break;
            case "AUTO":
                route_type = WatchProperty.RouteType.AUTO;
                break;
            case "SOS":
                route_type = WatchProperty.RouteType.SOS;
                break;
        }

        Double wgsLon = 0.0;
        Double wgsLat = 0.0;
        Location wgsLoc = new Location();
        Location gcjLoc = new Location();
        Boolean lbs_flag = false;

        boolean has_gps = false;
        if (reqData.has("gpss")) {
            has_gps = true;
            JsonObject gpss = reqData.get("gpss").getAsJsonObject();
            wgsLon = gpss.get("lon").getAsDouble();
            wgsLat = gpss.get("lat").getAsDouble();
            wgsLoc = new Location(wgsLon, wgsLat);
            if (wgsLat == 0.0 && wgsLon == 0.0) {
                has_gps = false;
            }
        }

        if (has_gps == false) {
            JsonArray lbss = reqData.get("lbss").getAsJsonArray();

            List<LBSData> lbs_list = new ArrayList<>();
            for (JsonElement lbs : lbss) {
                JsonObject lbs_json = lbs.getAsJsonObject();
                LBSData lbs_data = new LBSData();
                lbs_data.setMcc(lbs_json.get("mcc").getAsInt());
                lbs_data.setMnc(lbs_json.get("mnc").getAsInt());
                lbs_data.setLac(lbs_json.get("lac").getAsInt());
                lbs_data.setCell(lbs_json.get("cell").getAsInt());
                lbs_data.setSignal(lbs_json.get("signal").getAsInt());
                lbs_list.add(lbs_data);
            }

            wgsLoc = Get3PLbsLatlng.getLbsLoc(lbs_list);

            if (wgsLoc == null) {
                setErrorResponse(res);
                return;
            }

            if (wgsLoc.getLatitude() < 0.0001 || wgsLoc.getLongitude() < 0.0001) {
                setErrorResponse(res);
                return;
            }
            wgsLon = wgsLoc.getLongitude();
            wgsLat = wgsLoc.getLatitude();

            lbs_flag = true;
        }

        gcjLoc = ConverterII.transformFromWGSToGCJ(wgsLoc);

        Location bdLoc = ConverterII.bd_encrypt(gcjLoc);

        String address = GetBaidu.getAddressByGPS(bdLoc.getLatitude() + "", bdLoc.getLongitude() + "");

        Double lon_of = gcjLoc.getLongitude();
        Double lat_of = gcjLoc.getLatitude();
        if (StringUtils.isBlank(address)) {
            address = "地址解析失败";
        }

        if (lbs_flag) {
            address = "大概位置：" + address;
        } else {
            address = "准确位置：" + address;
        }
        String pic_url = req.getScheme() + "://" + req.getServerName() + req.getContextPath()
                + "/s?s=" + CharUtils.LatLngToChar(bdLoc.getLatitude() + "," + bdLoc.getLongitude()).toString();

        String now = format.format(new Date());
        Route route = new Route(0, user.getUser_id(), wgsLon, wgsLat, bdLoc.getLongitude(),
                bdLoc.getLatitude(), 0, "", now, route_type,
                "", address, "", pic_url, route_type,
                "");

        List<PenSetHexagon> pens = routeService.selPenSetHexagonByUserId(user.getUser_id(), WatchProperty.DeletedFlag.UNDELETED);
        PenSetHexagon pen = inWhichPenSet(pens, bdLoc);
        if (pen != null) {
            route.setUser_pen_id(pen.getId());
            route.setUser_pen_flag(pen.getPen_name());
        }
        SimpleDateFormat day_format = new SimpleDateFormat("yyyyMMdd");
        String day_str = day_format.format(new Date());
        String table_name = "route_" + day_str;
        route.setTable_name(table_name);
        routeService.insertRoute(route);

        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));
        JsonObject resData = new JsonObject();
        resData.add("status", gson.toJsonTree("ok"));
        int interval = 3600;
        if (heartBeat != null) {
            interval = heartBeat.getGap();
        }
        resData.add("heart", gson.toJsonTree(String.valueOf(interval)));
        resData.add("imei", gson.toJsonTree(imei));
        resData.add("password", gson.toJsonTree(user.getPasswd()));
        resData.add("centernumber", gson.toJsonTree(centerNumber.getNumber()));
        resData.add("datetime", gson.toJsonTree(new Date()));
        String number = null;
        if (reqData.has("number")) {
            number = reqData.get("number").getAsString();
        }
        if (StringUtils.isBlank(number)) {
            number = getNumberByUser(user.getUser_id());
        }

        if ((reqData.get("type").getAsString().toUpperCase().equals("DW") ||
                reqData.get("type").getAsString().toUpperCase().equals("SOS"))
                && !StringUtils.isBlank(number)) {
            resData.add("action", getSendLocSMSAction(reqData.get("number").getAsString(),
                    address + "," + pic_url, bdLoc.getLongitude(), bdLoc.getLatitude()));
        } else {
            resData.add("action", getEmptyAction());
        }
        body.add("data", resData);
        System.out.println("watch.do response:" + gson.toJson(body));
        setResponseContent(res, body);
    }

    private String getNumberByUser(int user_id) {
        Map<String, Object> sos = actionService.selSOSByUser(user_id);
        if (sos != null) {
            String sos_number = (String) sos.get("phone");
            if (!StringUtils.isBlank(sos_number)) {
                return sos_number;
            }
        }
        QingSet set1 = actionService.selQingSetByUser(user_id, 1);
        if (set1 != null) {
            String qing1 = set1.getPhone2();
            if (!StringUtils.isBlank(qing1)) {
                return qing1;
            }
        }
        QingSet set2 = actionService.selQingSetByUser(user_id, 2);
        if (set2 != null) {
            String qing2 = set2.getPhone2();
            if (!StringUtils.isBlank(qing2)) {
                return qing2;
            }
        }
        QingSet set3 = actionService.selQingSetByUser(user_id, 3);
        if (set3 != null) {
            String qing3 = set3.getPhone2();
            if (!StringUtils.isBlank(qing3)) {
                return qing3;
            }
        }
        QingSet set4 = actionService.selQingSetByUser(user_id, 4);
        if (set4 != null) {
            String qing4 = set4.getPhone2();
            if (!StringUtils.isBlank(qing4)) {
                return qing4;
            }
        }
        return null;
    }

    private void LoginInterface(JsonObject reqData, HttpServletResponse res) throws Exception {
        if (reqData == null || !reqData.has("simid") || !reqData.has("imei")) {
            setErrorResponse(res);
            return;
        }

        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));

        String imei = reqData.get("imei").getAsString();
        UserInf user = watchUserService.selUserInfByImei(imei);
        CenterNumber centerNumber = watchOtherService.selCenterNumber();
        if (user == null || centerNumber == null) {
            setErrorResponse(res);
            return;
        }

        HeartBeatInf heartBeat = new HeartBeatInf();
        heartBeat.setId(0);
        heartBeat.setUser_id(user.getUser_id());
        heartBeat.setGap(20);
        heartBeat.setCreate_time(format.format(new Date()));
        heartBeat.setUpdate_time(format.format(new Date()));
        watchUserService.repUserHeartBeat(heartBeat);

        JsonObject resData = new JsonObject();
        resData.add("status", gson.toJsonTree("ok"));
        resData.add("heart", gson.toJsonTree(String.valueOf(20)));
        resData.add("imei", gson.toJsonTree(imei));
        resData.add("password", gson.toJsonTree(user.getPasswd()));
        resData.add("centernumber", gson.toJsonTree(centerNumber.getNumber()));
        resData.add("datetime", gson.toJsonTree(new Date()));

        String simid = reqData.get("simid").getAsString();
        if (!StringUtils.isBlank(simid) &&
                (StringUtils.isBlank(user.getNumber()) || StringUtils.isBlank(user.getSimid())
                        || !simid.equals(user.getSimid()))) {
            user.setSimid(simid);
            watchUserService.updateUserInf(user);
            resData.add("action", getSendSMSAction(phone, "bj#" + imei));
        } else {
            resData.add("action", getEmptyAction());
        }

        body.add("data", resData);
        System.out.println("watch.do response:" + gson.toJson(body));
        setResponseContent(res, body);
    }

    private void LogInterface(JsonObject reqData, HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (!reqData.has("logs") || !reqData.has("imei") || !reqData.has("size")) {
            setErrorResponse(res);
            return;
        }

        String imei = reqData.get("imei").getAsString();
        UserInf user = watchUserService.selUserInfByImei(imei);
        CenterNumber centerNumber = watchOtherService.selCenterNumber();
        if (user == null || centerNumber == null) {
            setErrorResponse(res);
            return;
        }
        HeartBeatInf heartBeat = watchUserService.selUserHeartBeatByUserId(user.getUser_id());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String log_path = req.getSession().getServletContext().getRealPath("/") + "../upload/logs/";
        String log_name = imei + "_" + format.format(new Date()) + ".log";
        File log_file = new File(log_path + log_name);
        if (!log_file.getParentFile().exists()) {
            log_file.getParentFile().mkdirs();
        }

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(log_file), "UTF-8");
        BufferedWriter writer = new BufferedWriter(out);
        JsonArray logs = reqData.get("logs").getAsJsonArray();
        try {
            for (JsonElement log : logs) {
                writer.write(gson.toJson(log));
                writer.newLine();
            }
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            setErrorResponse(res);
            return;
        }

        int interval = 3600;
        if (heartBeat != null) {
            interval = heartBeat.getGap();
        }

        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));

        JsonObject resData = new JsonObject();
        resData.add("status", gson.toJsonTree("ok"));
        resData.add("heart", gson.toJsonTree(String.valueOf(interval)));
        resData.add("imei", gson.toJsonTree(imei));
        resData.add("password", gson.toJsonTree(user.getPasswd()));
        resData.add("centernumber", gson.toJsonTree(centerNumber.getNumber()));
        resData.add("datetime", gson.toJsonTree(new Date()));
        resData.add("action", getEmptyAction());
        body.add("data", resData);
        System.out.println("watch.do response:" + gson.toJson(body));
        setResponseContent(res, body);
    }

    private void PowerOffInterface(JsonObject reqData, HttpServletResponse res) {
        if (reqData == null || !reqData.has("imei")) {
            setErrorResponse(res);
            return;
        }

        String imei = reqData.get("imei").getAsString();
        UserInf user = watchUserService.selUserInfByImei(imei);
        CenterNumber centerNumber = watchOtherService.selCenterNumber();
        if (user == null || centerNumber == null) {
            setErrorResponse(res);
            return;
        }

        HeartBeatInf heartBeat = new HeartBeatInf();
        heartBeat.setId(0);
        heartBeat.setUser_id(user.getUser_id());
        heartBeat.setGap(3600);
        heartBeat.setCreate_time(format.format(new Date()));
        heartBeat.setUpdate_time(format.format(new Date()));
        watchUserService.repUserHeartBeat(heartBeat);

        JsonObject body = new JsonObject();
        body.add("command", gson.toJsonTree("result"));
        JsonObject resData = new JsonObject();
        resData.add("status", gson.toJsonTree("ok"));
        resData.add("heart", gson.toJsonTree(String.valueOf(3600)));
        resData.add("imei", gson.toJsonTree(imei));
        resData.add("password", gson.toJsonTree(user.getPasswd()));
        resData.add("centernumber", gson.toJsonTree(centerNumber.getNumber()));
        resData.add("datetime", gson.toJsonTree(new Date()));
        resData.add("action", getEmptyAction());
        body.add("data", resData);
        System.out.println("watch.do response:" + gson.toJson(body));
        setResponseContent(res, body);
    }

    private void updateHeartBeatInf(HeartBeatInf heartBeat) throws Exception {
        Date createTime = format.parse(heartBeat.getCreate_time());
        Date updateTime = new Date();
        heartBeat.setUpdate_time(format.format(updateTime));
        if (updateTime.getTime() - createTime.getTime() > 15 * 60 * 1000) {
            heartBeat.setGap(3600);
        }
        watchUserService.upUserHeartBeat(heartBeat);
    }

    private boolean inCircle(PenSetHexagon pen, Location local) {
        if (pen == null || local == null) return false;
        Double distance = Distance.GetDistance(local.getLongitude(), local.getLatitude(),
                pen.getBd_lon(), pen.getBd_lat());
        if (distance < pen.getRadius()) {
            return true;
        }
        return false;
    }

    private PenSetHexagon inWhichPenSet(List<PenSetHexagon> pens, Location local) {
        if (pens == null || pens.size() <= 0) return null;
        for (PenSetHexagon pen : pens) {
            if (pen.getPen_type() == WatchProperty.PenSetType.CIRCLE) {
                if (inCircle(pen, local)) return pen;
                continue;
            }
            if (pen.getPen_type() == WatchProperty.PenSetType.HEXAGON) {
                if (inHexagon(pen, local)) return pen;
                continue;
            }
        }
        return null;
    }

    private boolean inHexagon(PenSetHexagon pen, Location local) {
        if (pen == null || local == null) return false;
        Double lon = local.getLongitude();
        Double lat = local.getLatitude();
        List<Double> lonList = new ArrayList<>();
        List<Double> latList = new ArrayList<>();
        lonList.add(pen.getCenter_lon_of());
        lonList.add(pen.getP2_lon_of());
        lonList.add(pen.getP3_lon_of());
        lonList.add(pen.getP4_lon_of());
        lonList.add(pen.getP5_lon_of());
        lonList.add(pen.getP6_lon_of());
        latList.add(pen.getCenter_lat_of());
        latList.add(pen.getP2_lat_of());
        latList.add(pen.getP3_lat_of());
        latList.add(pen.getP4_lat_of());
        latList.add(pen.getP5_lat_of());
        latList.add(pen.getP6_lat_of());

        Double minLon = getMin(lonList);
        Double minLat = getMin(latList);
        Double maxLon = getMax(lonList);
        Double maxLat = getMax(latList);
        if (lat < minLat || lat > maxLat || lon < minLon || lon > maxLon) {
            return false;
        }
        return pnpoly(6, lonList, latList, lon, lat);
    }

    private boolean pnpoly(int size, List<Double> lonList, List<Double> latList, Double lon, Double lat) {
        int i, j;
        boolean c = false;
        for (i = 0, j = size - 1; i < size; j = i++) {
            if (((latList.get(i) > lat) != (latList.get(j) > lat)) &&
                    (lon < (lonList.get(j) - lonList.get(i)) * (lat - latList.get(i)) / (latList.get(j) - latList.get(i)) + lonList.get(i)))
                c = !c;
        }
        return c;
    }

    private Double getMin(List<Double> list) {
        if (list == null || list.size() <= 0) return null;
        Double min = list.get(0);
        for (Double item : list) {
            if (item < min) min = item;
        }
        return min;
    }

    private Double getMax(List<Double> list) {
        if (list == null || list.size() <= 0) return null;
        Double max = list.get(0);
        for (Double item : list) {
            if (item > max) max = item;
        }
        return max;
    }

    private JsonArray SendSmsAction(int user_id) {
        List<SendSms> sms_list = actionService.selSendSmsByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (sms_list == null) {
            sms_list = new ArrayList<>();
        }
        JsonArray array = new JsonArray();
        for (SendSms sms : sms_list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("sendsms"));
            json.add("msg", gson.toJsonTree(sms.getMsg()));
            json.add("number", gson.toJsonTree(sms.getNumber()));
            json.add("repeat", makeRepeat(0, 0));
            json.add("type", gson.toJsonTree(sms.getType()));
            array.add(json);
            actionService.upSendSmsRead(sms.getId(), WatchProperty.ReadStatus.READ);
        }
        return array;
    }

    private JsonArray AlarmSetEvent(int user_id) {
        List<PlayVoice> alarm_set_list = actionService.selAlarmSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (alarm_set_list == null || alarm_set_list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (PlayVoice alarm_set : alarm_set_list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(alarm_set.getStart().substring(0, 5)));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(String.valueOf(alarm_set.getWeeks())));
            json.add("repeat", makeRepeat(alarm_set.getTimes(), alarm_set.getGap()));
            array.add(json);
        }
        return array;
    }

    private JsonArray HealthSetEvent(int user_id) {
        List<PlayVoice> health_set_list = actionService.selHealthSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (health_set_list == null || health_set_list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (PlayVoice health_set : health_set_list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(health_set.getStart().substring(0, 5)));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(String.valueOf(health_set.getWeeks())));
            json.add("repeat", makeRepeat(health_set.getTimes(), health_set.getGap()));
            array.add(json);
        }
        return array;
    }

    private JsonArray PillsSetEvent(int user_id) {
        List<PlayVoice> pills_set_list = actionService.selPillsSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (pills_set_list == null || pills_set_list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (PlayVoice pills_set : pills_set_list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(pills_set.getStart().substring(0, 5)));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(String.valueOf(pills_set.getWeeks())));
            json.add("repeat", makeRepeat(pills_set.getTimes(), pills_set.getGap()));
            array.add(json);
        }
        return array;
    }

    private JsonArray SleepSetEvent(int user_id) {
        List<PlayVoice> sleep_set_list = actionService.selSleepSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (sleep_set_list == null || sleep_set_list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (PlayVoice sleep_set : sleep_set_list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(sleep_set.getStart().substring(0, 5)));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(String.valueOf(sleep_set.getWeeks())));
            json.add("repeat", makeRepeat(sleep_set.getTimes(), sleep_set.getGap()));
            array.add(json);
        }
        return array;
    }

    private JsonArray SportSetEvent(int user_id) {
        List<PlayVoice> sport_set_list = actionService.selSportSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (sport_set_list == null || sport_set_list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (PlayVoice sport_set : sport_set_list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(sport_set.getStart().substring(0, 5)));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(String.valueOf(sport_set.getWeeks())));
            json.add("repeat", makeRepeat(sport_set.getTimes(), sport_set.getGap()));
            array.add(json);
        }
        return array;
    }

    private JsonObject PlayVoiceEventAction(int user_id) {
        JsonArray time = new JsonArray();
        JsonArray alarms = AlarmSetEvent(user_id);
        if (alarms != null && alarms.size() >= 0) {
            time.addAll(alarms);
        }

        JsonArray health = HealthSetEvent(user_id);
        if (health != null && health.size() >= 0) {
            time.addAll(health);
        }

        JsonArray pills = PillsSetEvent(user_id);
        if (pills != null && pills.size() >= 0) {
            time.addAll(pills);
        }

        JsonArray sleep = SleepSetEvent(user_id);
        if (sleep != null && sleep.size() >= 0) {
            time.addAll(sleep);
        }

        JsonArray sport = SportSetEvent(user_id);
        if (sport != null && sport.size() >= 0) {
            time.addAll(sport);
        }

        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("playvoice"));
        json.add("type", gson.toJsonTree("event"));
        json.add("time", time);
        json.add("eventname", gson.toJsonTree("voiceplay"));
        json.add("op", gson.toJsonTree("modify"));
        return json;
    }

    private JsonArray PlayVoiceOnceAction(int user_id) {
        List<OncePlayVoice> list = actionService.selPlayVoiceByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (OncePlayVoice voice : list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("playvoice"));
            json.add("msg", gson.toJsonTree(voice.getMsg()));
            json.add("repeat", makeRepeat(0, 0));
            json.add("type", gson.toJsonTree("once"));
            json.add("eventname", gson.toJsonTree(""));
            json.add("time", new JsonArray());
            array.add(json);
            actionService.upOncePlayVoice(voice.getId(), WatchProperty.ReadStatus.READ);
        }
        return array;
    }

    private JsonArray MonitorAction(int user_id) {
        List<Monitor> list = actionService.selMonitorByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (Monitor monitor : list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("monitor"));
            json.add("msg", gson.toJsonTree(""));
            json.add("type", gson.toJsonTree("once"));
            json.add("repeat", makeRepeat(0, 0));
            json.add("timer", gson.toJsonTree(monitor.getTimer()));
            json.add("number", gson.toJsonTree(monitor.getNumber()));
            array.add(json);
            actionService.upMonitorRead(monitor.getId(), WatchProperty.ReadStatus.READ);
        }
        return array;
    }

    private JsonArray LocationOnceAction(int user_id) {
        List<LocationAction> list = actionService.selLocationByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;

        for (LocationAction loc : list) {
            actionService.upLocationRead(loc.getId(), WatchProperty.ReadStatus.READ);
        }

        JsonArray array = new JsonArray();
        JsonArray time_array = new JsonArray();
        JsonObject time_item = new JsonObject();
        time_item.add("start", gson.toJsonTree(""));
        time_item.add("stop", gson.toJsonTree(""));
        time_item.add("weeks", gson.toJsonTree("0"));
        time_array.add(time_item);
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("location"));
        json.add("msg", gson.toJsonTree(""));
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", makeRepeat(0, 0));
        json.add("time", time_array);
        json.add("eventname", gson.toJsonTree(""));
        json.add("op", gson.toJsonTree("lbs"));
        array.add(json);
        return array;
    }

    private JsonObject LocationEventAction(int user_id) {
        List<RouteSet> list = actionService.selRouteSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (list == null) {
            list = new ArrayList<>();
        }
        JsonArray time = new JsonArray();
        for (RouteSet item : list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(item.getStart().substring(0, 5)));
            json.add("stop", gson.toJsonTree(item.getStop().substring(0, 5)));
            json.add("weeks", gson.toJsonTree(String.valueOf(item.getWeeks())));
            time.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("location"));
        json.add("msg", gson.toJsonTree(""));
        json.add("type", gson.toJsonTree("event"));
        json.add("repeat", makeRepeat(0, 120));
        json.add("op", gson.toJsonTree("modify"));
        json.add("eventname", gson.toJsonTree("autolocation"));
        json.add("time", time);
        return json;
    }

    private JsonObject LogAction(int user_id) {
        List<LogSet> list = actionService.selLogSetByUser(user_id);
        if (list == null || list.size() <= 0) return null;
        JsonArray time = new JsonArray();
        for (LogSet item : list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(item.getStart().substring(0, 5)));
            json.add("stop", gson.toJsonTree(item.getStop().substring(0, 5)));
            json.add("weeks", gson.toJsonTree(String.valueOf(item.getWeeks())));
            time.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("log"));
        json.add("op", gson.toJsonTree("add"));
        json.add("repeat", makeRepeat(0, 600));
        json.add("msg", gson.toJsonTree(""));
        json.add("type", gson.toJsonTree("event"));
        json.add("time", time);
        return json;
    }

    private JsonArray PowerOffAction(int user_id) {
        List<OnceAction> list = actionService.selOncePowerOffByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;

        for (OnceAction item : list) {
            actionService.upOncePowerOffRead(item.getId(), WatchProperty.ReadStatus.READ);
        }

        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("poweroff"));
        json.add("repeat", makeRepeat(0, 0));
        json.add("type", gson.toJsonTree("once"));
        json.add("time", new JsonArray());
        json.add("op", gson.toJsonTree(""));
        json.add("msg", gson.toJsonTree(""));
        array.add(json);
        return array;
    }

    private JsonArray ClearSmsAction(int user_id) {
        List<OnceAction> list = actionService.selOnceClearSmsByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;

        for (OnceAction item : list) {
            actionService.upOncePowerOffRead(item.getId(), WatchProperty.ReadStatus.READ);
        }
        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("clearsms"));
        json.add("msg", gson.toJsonTree(""));
        json.add("repeat", makeRepeat(0, 0));
        json.add("type", gson.toJsonTree("once"));
        array.add(json);
        return array;
    }

    private JsonObject QingSetAction(int user_id) {
        QingSet set1 = actionService.selQingSetByUser(user_id, 1);
        QingSet set2 = actionService.selQingSetByUser(user_id, 2);
        QingSet set3 = actionService.selQingSetByUser(user_id, 3);
        QingSet set4 = actionService.selQingSetByUser(user_id, 4);
        Map<String, Object> sos = actionService.selSOSByUser(user_id);
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("qingset"));
        json.add("msg", gson.toJsonTree(""));
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", makeRepeat(0, 0));
        JsonObject number = new JsonObject();
        if (set1 != null) {
            number.add("name1", gson.toJsonTree(set1.getRelation()));
            number.add("qing1", gson.toJsonTree(set1.getPhone2()));
        } else {
            number.add("name1", gson.toJsonTree(""));
            number.add("qing1", gson.toJsonTree(""));
        }

        if (set2 != null) {
            number.add("name2", gson.toJsonTree(set2.getRelation()));
            number.add("qing2", gson.toJsonTree(set2.getPhone2()));
        } else {
            number.add("name2", gson.toJsonTree(""));
            number.add("qing2", gson.toJsonTree(""));
        }

        if (set3 != null) {
            number.add("name3", gson.toJsonTree(set3.getRelation()));
            number.add("qing3", gson.toJsonTree(set3.getPhone2()));
        } else {
            number.add("name3", gson.toJsonTree(""));
            number.add("qing3", gson.toJsonTree(""));
        }

        if (set4 != null) {
            number.add("name4", gson.toJsonTree(set4.getRelation()));
            number.add("qing4", gson.toJsonTree(set4.getPhone2()));
        } else {
            number.add("name4", gson.toJsonTree(""));
            number.add("qing4", gson.toJsonTree(""));
        }

        if (sos != null) {
            number.add("sos", gson.toJsonTree(sos.get("phone")));
            number.add("sosname", gson.toJsonTree(sos.get("name")));
        } else {
            number.add("sos", gson.toJsonTree(""));
            number.add("sosname", gson.toJsonTree("SOS"));
        }

        json.add("numbers", number);
        return json;
    }

    private JsonObject BlackSetAction(int user_id) {
        List<PhoneSetInfo> list = actionService.selBlackSetInfByUser(user_id);
        if (list == null) {
            list = new ArrayList<>();
        }
        JsonArray blacklist = new JsonArray();
        for (PhoneSetInfo item : list) {
            JsonObject json = new JsonObject();
            json.add("number", gson.toJsonTree(item.getPhone()));
            json.add("name", gson.toJsonTree(item.getName()));
            blacklist.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("blackset"));
        json.add("msg", gson.toJsonTree(""));
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", makeRepeat(0, 0));
        json.add("blacklist", blacklist);
        return json;
    }

    private JsonObject WhiteSetAction(int user_id) {
        List<PhoneSetInfo> list = actionService.selWhiteSetInfByUser(user_id);
        if (list == null) {
            list = new ArrayList<>();
        }
        JsonArray whitelist = new JsonArray();
        for (PhoneSetInfo item : list) {
            JsonObject json = new JsonObject();
            json.add("number", gson.toJsonTree(item.getPhone()));
            json.add("name", gson.toJsonTree(item.getName()));
            whitelist.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("whiteset"));
        json.add("whitelist", whitelist);
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", makeRepeat(0, 0));
        return json;
    }

    private JsonObject RunModeAction(int user_id) {
        Map<String, Object> mode_inf = actionService.selUserMode(user_id);
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("runmode"));
        json.add("msg", gson.toJsonTree(""));
        if ((int) mode_inf.get("mode_type") == WatchProperty.ModeType.SINGLE_MODE) {
            json.add("type", gson.toJsonTree("once"));
        } else {
            json.add("type", gson.toJsonTree("event"));
        }
        json.add("repeat", makeRepeat(0, 0));

        List<ModeInf> list = actionService.selModeInfByUser(user_id);
        JsonArray time = new JsonArray();
        for (ModeInf item : list) {
            JsonObject time_item = new JsonObject();
            time_item.add("start", gson.toJsonTree(item.getStart().substring(0, 5)));
            time_item.add("stop", gson.toJsonTree(item.getStop().substring(0, 5)));
            time_item.add("weeks", gson.toJsonTree(String.valueOf(item.getWeeks())));
            time_item.add("runmode", gson.toJsonTree(String.valueOf(item.getRunmode())));
            time.add(time_item);
        }
        json.add("time", time);
        json.add("eventname", gson.toJsonTree("runmode"));
        json.add("op", gson.toJsonTree("modify"));
        json.add("defaultmode", gson.toJsonTree(String.valueOf(mode_inf.get("user_mode"))));
        return json;
    }

    private JsonObject makeRepeat(int times, int interval) {
        JsonObject json = new JsonObject();
        json.add("times", gson.toJsonTree(String.valueOf(times)));
        json.add("interval", gson.toJsonTree(String.valueOf(interval)));
        return json;
    }

    private JsonArray getHeartAction(int user_id, String imei) {
        JsonArray array = new JsonArray();

        JsonArray sendsms = SendSmsAction(user_id);
        if (sendsms != null) array.addAll(sendsms);

        JsonArray playvoice = PlayVoiceOnceAction(user_id);
        if (playvoice != null) array.addAll(playvoice);

        JsonArray monitor = MonitorAction(user_id);
        if (monitor != null) array.addAll(monitor);

        JsonArray locationOnce = LocationOnceAction(user_id);
        if (locationOnce != null) array.addAll(locationOnce);

        JsonArray poweroff = PowerOffAction(user_id);
        if (poweroff != null) array.addAll(poweroff);

        JsonArray clearsms = ClearSmsAction(user_id);
        if (clearsms != null) array.addAll(clearsms);

        List<ActionSetting> list = actionService.selActionSettingByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        for (ActionSetting item : list) {
            String name = item.getName();
            switch (name) {
                case "playvoice":
                    JsonObject voice = PlayVoiceEventAction(user_id);
                    if (voice != null) array.add(voice);
                    break;
                case "location":
                    JsonObject locationEvent = LocationEventAction(user_id);
                    if (locationEvent != null) array.add(locationEvent);
                    break;
                case "log":
                    JsonObject log = LogAction(user_id);
                    if (log != null) array.add(log);
                    break;
                case "qingset":
                    JsonObject qingSet = QingSetAction(user_id);
                    if (qingSet != null) array.add(qingSet);
                    break;
                case "blackset":
                    JsonObject blackSet = BlackSetAction(user_id);
                    if (blackSet != null) array.add(blackSet);
                    break;
                case "whiteset":
                    JsonObject whiteSet = WhiteSetAction(user_id);
                    if (whiteSet != null) array.add(whiteSet);
                    break;
                case "runmode":
                    JsonObject runMode = RunModeAction(user_id);
                    if (runMode != null) array.add(runMode);
                    break;
                default:
                    break;
            }
        }
        actionService.upActionSettingReadByUser(user_id, WatchProperty.ReadStatus.READ);
        if (array.size() > 0) {
            getSettingSuccess(user_id, imei);
        }
        return array;
    }

    private JsonArray getDataSyncAction(int user_id) {
        JsonArray array = new JsonArray();

        JsonArray sendsms = SendSmsAction(user_id);
        if (sendsms != null) array.addAll(sendsms);

        JsonArray playvoice = PlayVoiceOnceAction(user_id);
        if (playvoice != null) array.addAll(playvoice);

        JsonArray monitor = MonitorAction(user_id);
        if (monitor != null) array.addAll(monitor);

        JsonArray locationOnce = LocationOnceAction(user_id);
        if (locationOnce != null) array.addAll(locationOnce);

        JsonArray poweroff = PowerOffAction(user_id);
        if (poweroff != null) array.addAll(poweroff);

        JsonArray clearsms = ClearSmsAction(user_id);
        if (clearsms != null) array.addAll(clearsms);

        JsonObject voice = PlayVoiceEventAction(user_id);
        if (voice != null) array.add(voice);

        JsonObject locationEvent = LocationEventAction(user_id);
        if (locationEvent != null) array.add(locationEvent);

        JsonObject log = LogAction(user_id);
        if (log != null) array.add(log);

        JsonObject blackSet = BlackSetAction(user_id);
        if (blackSet != null) array.add(blackSet);

        JsonObject whiteSet = WhiteSetAction(user_id);
        if (whiteSet != null) array.add(whiteSet);

        JsonObject runMode = RunModeAction(user_id);
        if (runMode != null) array.add(runMode);

        JsonObject qingSet = QingSetAction(user_id);
        if (qingSet != null) array.add(qingSet);

        actionService.upActionSettingReadByUser(user_id, WatchProperty.ReadStatus.READ);
        return array;
    }

    private JsonArray getEmptyAction() {
        JsonArray action = new JsonArray();
        action.add(new JsonObject());
        return action;
    }

    private JsonArray getSendSMSAction(String number, String msg) {
        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("sendsms"));
        json.add("number", gson.toJsonTree(number));
        json.add("msg", gson.toJsonTree(msg));
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", makeRepeat(0, 0));
        array.add(json);
        return array;
    }

    private JsonArray getSendLocSMSAction(String number, String msg, double lon, double lat) {
        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("sendsms"));
        json.add("number", gson.toJsonTree(number));
        json.add("msg", gson.toJsonTree(msg));
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", makeRepeat(0, 0));
        json.add("lon_of", gson.toJsonTree(String.valueOf(lon)));
        json.add("lat_of", gson.toJsonTree(String.valueOf(lat)));
        array.add(json);
        return array;
    }

    private void getSettingSuccess(int user_id, String imei) {
        QingSet set1 = actionService.selQingSetByUser(user_id, 1);
        QingSet set2 = actionService.selQingSetByUser(user_id, 2);
        QingSet set3 = actionService.selQingSetByUser(user_id, 3);
        QingSet set4 = actionService.selQingSetByUser(user_id, 4);
        Map<String, Object> sos = actionService.selSOSByUser(user_id);

        List<String> phone_list = new ArrayList<>();
        if (set1 != null && !StringUtils.isBlank(set1.getPhone2())) {
            String phone = set1.getPhone2();
            if (!phone_list.contains(phone)) {
                phone_list.add(phone);
            }
        }
        if (set2 != null && !StringUtils.isBlank(set2.getPhone2())) {
            String phone = set2.getPhone2();
            if (!phone_list.contains(phone)) {
                phone_list.add(phone);
            }
        }
        if (set3 != null && !StringUtils.isBlank(set3.getPhone2())) {
            String phone = set3.getPhone2();
            if (!phone_list.contains(phone)) {
                phone_list.add(phone);
            }
        }
        if (set4 != null && !StringUtils.isBlank(set4.getPhone2())) {
            String phone = set4.getPhone2();
            if (!phone_list.contains(phone)) {
                phone_list.add(phone);
            }
        }

        if (sos != null && !StringUtils.isBlank((String) sos.get("phone"))) {
            String phone = (String) sos.get("phone");
            if (!phone_list.contains(phone)) {
                phone_list.add(phone);
            }
        }

        List<Integer> uid_list = new ArrayList<>();
        List<UserPupil> userPupils = userService.selPupilsInf(imei);
        for (UserPupil userPupil : userPupils) {
            int parent_id = userPupil.getUser_id();
            uid_list.add(parent_id);
            User parent = userService.selUserByUserId(parent_id);
            if (!phone_list.contains(parent.getPhone())) {
                phone_list.add(parent.getPhone());
            }
        }

        send_sms_inf(phone_list, imei);
        push_baidu_inf(uid_list, imei);
    }

    private void send_sms_inf(List<String> phone_list, String imei) {
        if (phone_list == null || phone_list.size() <= 0) return;

        try {
            String sms_params = imei;
            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            for (String phone : phone_list) {
                ucpaasCommon.sendTemplateSMS(UcpaasCommon.warning_template, phone, sms_params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void push_baidu_inf(List<Integer> uid_list, String imei){
        if (uid_list == null || uid_list.size() <= 0) return;

        List<PushService> push_service_list = userService.selPushServiceByUserId(uid_list);

        String msg_content = "对于Imei号为: " + imei + " 的手表设置下发成功";
        AndroidPushMessage msg = new AndroidPushMessage(1, msg_content);

        AndroidPushMessageForPhone aPush = new AndroidPushMessageForPhone();
        IosPushMessageForPhone iPush = IosPushMessageForPhone.getInstance();
        for (PushService push_service : push_service_list) {
            if (push_service.getDevice() == BeanProperty.ComputeType.ANDROID) {
                aPush.pushMsgToSingleDevice(msg, push_service);
            } else {
                iPush.sendMessage(push_service.getPush_token(), msg_content);
            }
        }
    }

}