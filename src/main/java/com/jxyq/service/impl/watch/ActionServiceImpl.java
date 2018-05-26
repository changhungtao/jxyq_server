package com.jxyq.service.impl.watch;

import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.mapper.watch_mapper.ActionMapper;
import com.jxyq.mapper.watch_mapper.UserInfMapper;
import com.jxyq.model.watch.*;
import com.jxyq.service.inf.watch.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private UserInfMapper userInfMapper;

    @Override
    public List<ActionSetting> selActionSettingByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selActionSetting(map);
    }

    @Override
    public void upActionSettingReadByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        actionMapper.upActionSettingRead(map);
    }

    @Override
    public void upActionSettingRead(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upActionSettingRead(map);
    }

    @Override
    public void inActionSetting(ActionSetting actionSetting) {
        actionMapper.inActionSetting(actionSetting);
    }

    @Override
    public List<SendSms> selSendSmsByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selSendSms(map);
    }

    @Override
    public void upSendSmsRead(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upSendSmsRead(map);
    }

    @Override
    public List<PlayVoice> selAlarmSetByUser(int user_id, int enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        if (enable != WatchProperty.EnableStatus.ALL) {
            map.put("alarm_en", enable);
        }
        return actionMapper.selAlarmSet(map);
    }

    @Override
    public List<PlayVoice> selHealthSetByUser(int user_id, int enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        if (enable != WatchProperty.EnableStatus.ALL) {
            map.put("jiankang_en", enable);
        }
        return actionMapper.selHealthSet(map);
    }

    @Override
    public List<PlayVoice> selPillsSetByUser(int user_id, int enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        if (enable != WatchProperty.EnableStatus.ALL) {
            map.put("pills_en", enable);
        }
        return actionMapper.selPillsSet(map);
    }

    @Override
    public List<PlayVoice> selSleepSetByUser(int user_id, int enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        if (enable != WatchProperty.EnableStatus.ALL) {
            map.put("sleep_en", enable);
        }
        return actionMapper.selSleepSet(map);
    }

    @Override
    public List<PlayVoice> selSportSetByUser(int user_id, int enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        if (enable != WatchProperty.EnableStatus.ALL) {
            map.put("sport_en", enable);
        }
        return actionMapper.selSportSet(map);
    }

    @Override
    public void upPlayVoiceByName(PlayVoice action, String name) {
        switch (name){
            case "alarm":
                actionMapper.upAlarmSet(action);
                break;
            case "pills":
                actionMapper.upPillsSet(action);
                break;
            case "sport":
                actionMapper.upSportSet(action);
                break;
            case "sleep":
                actionMapper.upSleepSet(action);
                break;
            case "jiankang":
                actionMapper.upHealthSet(action);
                break;
        }
    }

    @Override
    public void inEventPlayVoice(PlayVoice playVoice) {
        actionMapper.inEventPlayVoice(playVoice);
    }

    @Override
    public void delPlayVoiceByName(int eventid, String eventname) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", eventid);
        switch (eventname){
            case "alarm":
                actionMapper.delAlarmSet(map);
                break;
            case "pills":
                actionMapper.delPillsSet(map);
                break;
            case "sport":
                actionMapper.delSportSet(map);
                break;
            case "sleep":
                actionMapper.delSleepSet(map);
                break;
            case "jiankang":
                actionMapper.delHealthSet(map);
                break;
        }
    }

    @Override
    public List<Monitor> selMonitorByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selMonitor(map);
    }

    @Override
    public void inOnceMonitor(Monitor monitor) {
        actionMapper.inOnceMonitor(monitor);
    }

    @Override
    public void upMonitorRead(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upMonitorRead(map);
    }

    @Override
    public void upOncePlayVoice(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upPlayVoice(map);
    }

    @Override
    public void inOncePlayVoice(OncePlayVoice oncePlayVoice) {
        actionMapper.inOncePlayVoice(oncePlayVoice);
    }

    @Override
    public List<OncePlayVoice> selPlayVoiceByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selPlayVoice(map);
    }

    @Override
    public void upLocationRead(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upLocationRead(map);
    }

    @Override
    public List<LocationAction> selLocationByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selLocation(map);
    }

    @Override
    public void inOnceLocation(LocationAction locationAction) {
        actionMapper.inOnceLocation(locationAction);
    }

    @Override
    public List<RouteSet> selRouteSetByUser(int user_id, int enable) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        if (enable != WatchProperty.EnableStatus.ALL) {
            map.put("set_en", enable);
        }
        return actionMapper.selRouteSet(map);
    }

    @Override
    public List<LogSet> selLogSetByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        return actionMapper.selLogSet(map);
    }

    @Override
    public List<OnceAction> selOncePowerOffByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selOncePowerOff(map);
    }

    @Override
    public void upOncePowerOffRead(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upOncePowerOffRead(map);
    }

    @Override
    public List<OnceAction> selOnceClearSmsByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selOnceClearSms(map);
    }

    @Override
    public void upOnceClearSmsRead(int id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("readed", readed);
        actionMapper.upOnceClearSmsRead(map);
    }

    @Override
    public QingSet selQingSetByUser(int user_id, int id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("id", id);
        if (id == 1) {
            return actionMapper.selQing1Set(map);
        }

        if (id == 2) {
            return actionMapper.selQing2Set(map);
        }

        if (id == 3) {
            return actionMapper.selQing3Set(map);
        }

        if (id == 4) {
            return actionMapper.selQing4Set(map);
        }
        return null;
    }

    @Override
    public void inQingSet(QingSet qingSet, int id) {
        if (id == 1) {
            actionMapper.inQing1Set(qingSet);
        }

        if (id == 2) {
            actionMapper.inQing2Set(qingSet);
        }

        if (id == 3) {
            actionMapper.inQing3Set(qingSet);
        }

        if (id == 4) {
            actionMapper.inQing4Set(qingSet);
        }
    }

    @Override
    public Map<String, Object> selSOSByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selSOSByUser(map);
    }

    @Override
    public void upSOSNumber(int user_id, String sos_name, String sos_no) {
        Map<String, Object> old = selSOSByUser(user_id);

        Map<String, Object> map = new HashMap<>(3);
        map.put("user_id", user_id);
        map.put("sosno", sos_no);
        map.put("sosname", sos_name);

        if (old == null){
            userInfMapper.inUserInfMore(map);
        }
        actionMapper.upSOSNumber(map);
    }

    @Override
    public List<PhoneSetInfo> selBlackSetInfByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selBlackSetInf(map);
    }
    @Override
    public void delBlackSetInf(int id){
        actionMapper.delBlackSetInf(id);

    }
    @Override
    public void inBlackSetInf(PhoneSetInfo phoneSetInfo) {
        actionMapper.inBlackSetInf(phoneSetInfo);
    }

    @Override
    public List<PhoneSetInfo> selWhiteSetInfByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selWhiteSetInf(map);
    }


    @Override
    public void delWhiteSetInf(int id){
        actionMapper.delWhiteSetInf(id);
    }

    @Override
    public void inWhiteSetInf(PhoneSetInfo phoneSetInfo) {
        actionMapper.inWhiteSetInf(phoneSetInfo);
    }

    @Override
    public List<ModeInf> selModeInfByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selModeInf(map);
    }

    @Override
    public Map<String, Object> selModeCntByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selModeCnt(map);
    }

    @Override
    public Map<String, Object> selUserMode(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selUserMode(map);
    }

    @Override
    public Map<String, Object> selWhiteSetCntByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selWhiteSetCount(map);
    }

    @Override
    public Map<String, Object> selBlackSetCntByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selBlackSetCount(map);
    }

    @Override
    public Map<String, Object> selRouteSetCntByUser(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return actionMapper.selRouteSetCnt(map);
    }

    @Override
    public void inRouteSet(RouteSet routeSet) {
        actionMapper.inRouteSet(routeSet);
    }

    @Override
    public void upRouteSet(RouteSet routeSet) {
        actionMapper.upRouteSet(routeSet);
    }

    @Override
    public void delRouteSet(int route_period_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", route_period_id);
        actionMapper.delRouteSet(map);
    }

    @Override
    public void upUserMode(int user_id, int mode_type, int user_mode) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("user_id", user_id);
        map.put("mode_type", mode_type);
        map.put("user_mode", user_mode);
        actionMapper.upUserMode(map);
    }

    @Override
    public void inModeInf(ModeInf modeInf) {
        actionMapper.inModeInf(modeInf);
    }

    @Override
    public void delModeInf(int mode_inf_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", mode_inf_id);
        actionMapper.delModeInf(map);
    }

    @Override
    public ModeInf selModeInfById(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        List<ModeInf> modeInfs = actionMapper.selModeInf(map);
        if (modeInfs == null || modeInfs.size() <= 0) return null;
        return modeInfs.get(0);
    }

    @Override
    public void upModeInf(ModeInf modeInf) {
        actionMapper.upModeInf(modeInf);
    }

    @Override
    public Map<String, Object> selSendSmsCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selSendSmsCount(map);
    }

    @Override
    public Map<String, Object> selPlayVoiceCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selPlayVoiceCount(map);
    }

    @Override
    public Map<String, Object> selMonitorCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selMonitorCount(map);
    }

    @Override
    public Map<String, Object> selLocationCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selLocationCount(map);
    }

    @Override
    public Map<String, Object> selOncePowerOffCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selOncePowerOffCount(map);
    }

    @Override
    public Map<String, Object> selOnceClearSmsCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selOnceClearSmsCount(map);
    }

    @Override
    public Map<String, Object> selActionSettingCntByUser(int user_id, int readed) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("readed", readed);
        return actionMapper.selActionSettingCount(map);
    }

    @Override
    public Map<String, Object> selActionSettingCntByUserAndAction(int user_id, int readed, String event_name) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("user_id", user_id);
        map.put("readed", readed);
        map.put("name", event_name);
        return actionMapper.selActionSettingCount(map);
    }
}
