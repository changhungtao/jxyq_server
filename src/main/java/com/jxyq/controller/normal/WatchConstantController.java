package com.jxyq.controller.normal;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.ConstantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WatchConstantController extends BaseInterface {
    @Autowired
    private ConstantService constantService;

    /*3.2.7.1 GET /common/constants/twatch_qing_names*/
    @RequestMapping(value = "/common/constants/twatch_qing_names", method = RequestMethod.GET)
    public void GetConstantQingName(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_qing_name");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("name_id", constant.getConstant());
                map.put("name_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("name_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.2 GET /common/constants/twatch_sos_names*/
    @RequestMapping(value = "/common/constants/twatch_sos_names", method = RequestMethod.GET)
    public void GetConstantSosNames(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_sos_name");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("name_id", constant.getConstant());
                map.put("name_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("name_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.3 GET /common/constants/twatch_work_modes*/
    @RequestMapping(value = "/common/constants/twatch_work_modes", method = RequestMethod.GET)
    public void GetConstantWorkModes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_work_mode");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("mode_id", constant.getConstant());
                map.put("mode_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("mode_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.4 GET /common/constants/twatch_work_types*/
    @RequestMapping(value = "/common/constants/twatch_work_types", method = RequestMethod.GET)
    public void GetConstantWorkTypes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_work_type");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("type_id", constant.getConstant());
                map.put("type_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("type_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.5 GET /common/constants/twatch_alarm_events*/
    @RequestMapping(value = "/common/constants/twatch_alarm_events", method = RequestMethod.GET)
    public void GetConstantAlarmEvents(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_alarm_event");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("event_id", constant.getConstant());
                map.put("event_name", constant.getDescription());
                map.put("event_content", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("event_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.6 GET /common/constants/twatch_period_states*/
    @RequestMapping(value = "/common/constants/twatch_period_states", method = RequestMethod.GET)
    public void GetConstantPeriodStates(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_period_state");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("state_id", constant.getConstant());
                map.put("state_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("state_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.7 GET /common/constants/twatch_route_states*/
    @RequestMapping(value = "/common/constants/twatch_route_states", method = RequestMethod.GET)
    public void GetConstantRouteStates(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_route_state");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("state_id", constant.getConstant());
                map.put("state_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("state_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.8 GET /common/constants/twatch_gps_modes*/
    @RequestMapping(value = "/common/constants/twatch_gps_modes", method = RequestMethod.GET)
    public void GetConstantGpsModes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_gps_mode");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("mode_id", constant.getConstant());
                map.put("mode_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("mode_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.9 GET /common/constants/twatch_pen_shapes*/
    @RequestMapping(value = "/common/constants/twatch_pen_shapes", method = RequestMethod.GET)
    public void GetConstantPenShapes(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("twatch_pen_shape");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("shape_id", constant.getConstant());
                map.put("shape_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("shape_list", gson.toJsonTree(list));
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
