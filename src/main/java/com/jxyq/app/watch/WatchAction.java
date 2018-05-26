package com.jxyq.app.watch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.model.watch.*;
import com.jxyq.service.inf.watch.ActionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class WatchAction {
    @Autowired
    private ActionService actionService;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public WatchAction(int user_id) {
        this.user_id = user_id;
    }

    private JsonArray SendSmsAction(int user_id) {
        List<SendSms> sms_list = actionService.selSendSmsByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (sms_list == null || sms_list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (SendSms sms : sms_list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("sendsms"));
            json.add("msg", gson.toJsonTree(sms.getMsg()));
            json.add("number", gson.toJsonTree(sms.getNumber()));
            json.add("repeat", gson.toJsonTree("[0,0]"));
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
            json.add("start", gson.toJsonTree(alarm_set.getStart()));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(alarm_set.getWeeks()));
            JsonObject repeat = new JsonObject();
            repeat.add("times", gson.toJsonTree(alarm_set.getTimes()));
            repeat.add("interval", gson.toJsonTree(alarm_set.getGap()));
            json.add("repeat", repeat);
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
            json.add("start", gson.toJsonTree(health_set.getStart()));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(health_set.getWeeks()));
            JsonObject repeat = new JsonObject();
            repeat.add("times", gson.toJsonTree(health_set.getTimes()));
            repeat.add("interval", gson.toJsonTree(health_set.getGap()));
            json.add("repeat", repeat);
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
            json.add("start", gson.toJsonTree(pills_set.getStart()));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(pills_set.getWeeks()));
            JsonObject repeat = new JsonObject();
            repeat.add("times", gson.toJsonTree(pills_set.getTimes()));
            repeat.add("interval", gson.toJsonTree(pills_set.getGap()));
            json.add("repeat", repeat);
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
            json.add("start", gson.toJsonTree(sleep_set.getStart()));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(sleep_set.getWeeks()));
            JsonObject repeat = new JsonObject();
            repeat.add("times", gson.toJsonTree(sleep_set.getTimes()));
            repeat.add("interval", gson.toJsonTree(sleep_set.getGap()));
            json.add("repeat", repeat);
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
            json.add("start", gson.toJsonTree(sport_set.getStart()));
            json.add("msg", gson.toJsonTree("alarm"));
            json.add("weeks", gson.toJsonTree(sport_set.getWeeks()));
            JsonObject repeat = new JsonObject();
            repeat.add("times", gson.toJsonTree(sport_set.getTimes()));
            repeat.add("interval", gson.toJsonTree(sport_set.getGap()));
            json.add("repeat", repeat);
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

        if (time == null || time.size() <= 0) return null;

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
            json.add("repeat", gson.toJsonTree("[0,0]"));
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
            json.add("timer", gson.toJsonTree(monitor.getTimer()));
            json.add("number", gson.toJsonTree(monitor.getNumber()));
            json.add("type", gson.toJsonTree("once"));
            array.add(json);
            actionService.upMonitorRead(monitor.getId(), WatchProperty.ReadStatus.READ);
        }
        return array;
    }

    private JsonArray LocationOnceAction(int user_id) {
        List<LocationAction> list = actionService.selLocationByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (LocationAction loc : list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("location"));
            json.add("repeat", gson.toJsonTree("[0,0]"));
            json.add("type", gson.toJsonTree("once"));
            json.add("op", gson.toJsonTree(""));
            json.add("eventname", gson.toJsonTree(""));
            json.add("time", new JsonArray());
            array.add(json);
            actionService.upLocationRead(loc.getId(), WatchProperty.ReadStatus.READ);
        }
        return array;
    }

    private JsonObject LocationEventAction(int user_id) {
        List<RouteSet> list = actionService.selRouteSetByUser(user_id, WatchProperty.EnableStatus.ENABLE);
        if (list == null || list.size() <= 0) return null;
        JsonArray time = new JsonArray();
        for (RouteSet item : list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(item.getStart()));
            json.add("stop", gson.toJsonTree(item.getStop()));
            json.add("weeks", gson.toJsonTree(item.getWeeks()));
            time.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("location"));
        json.add("repeat", gson.toJsonTree("[0,0]"));
        json.add("type", gson.toJsonTree("event"));
        json.add("op", gson.toJsonTree("add"));
        json.add("eventname", gson.toJsonTree("route"));
        json.add("time", time);
        return json;
    }

    private JsonObject LogAction(int user_id) {
        List<LogSet> list = actionService.selLogSetByUser(user_id);
        if (list == null || list.size() <= 0) return null;
        JsonArray time = new JsonArray();
        for (LogSet item : list) {
            JsonObject json = new JsonObject();
            json.add("start", gson.toJsonTree(item.getStart()));
            json.add("stop", gson.toJsonTree(item.getStop()));
            json.add("weeks", gson.toJsonTree(item.getWeeks()));
            time.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("log"));
        json.add("op", gson.toJsonTree("add"));
        json.add("repeat", gson.toJsonTree("[0,600]"));
        json.add("msg", gson.toJsonTree(""));
        json.add("type", gson.toJsonTree("event"));
        json.add("time", time);
        return json;
    }

    private JsonArray PowerOffAction(int user_id) {
        List<OnceAction> list = actionService.selOncePowerOffByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (OnceAction item : list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("poweroff"));
            json.add("repeat", gson.toJsonTree("[0,0]"));
            json.add("type", gson.toJsonTree(item.getType()));
            json.add("time", new JsonArray());
            json.add("op", gson.toJsonTree(""));
            json.add("msg", gson.toJsonTree(""));
            array.add(json);
            actionService.upOncePowerOffRead(item.getId(), WatchProperty.ReadStatus.READ);
        }
        return array;
    }

    private JsonArray ClearSmsAction(int user_id) {
        List<OnceAction> list = actionService.selOnceClearSmsByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        if (list == null || list.size() <= 0) return null;
        JsonArray array = new JsonArray();
        for (OnceAction item : list) {
            JsonObject json = new JsonObject();
            json.add("name", gson.toJsonTree("clearsms"));
            json.add("repeat", gson.toJsonTree("[0,0]"));
            json.add("type", gson.toJsonTree(item.getType()));
            array.add(json);
            actionService.upOncePowerOffRead(item.getId(), WatchProperty.ReadStatus.READ);
        }
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
        JsonObject number = new JsonObject();
        if (set1 != null) {
            number.add("qing1", gson.toJsonTree(set1.getPhone2()));
            number.add("name1", gson.toJsonTree(set1.getName()));
        } else {
            number.add("qing1", gson.toJsonTree(""));
            number.add("name1", gson.toJsonTree(""));
        }

        if (set2 != null) {
            number.add("qing2", gson.toJsonTree(set2.getPhone2()));
            number.add("name2", gson.toJsonTree(set2.getName()));
        } else {
            number.add("qing2", gson.toJsonTree(""));
            number.add("name2", gson.toJsonTree(""));
        }

        if (set3 != null) {
            number.add("qing3", gson.toJsonTree(set3.getPhone2()));
            number.add("name3", gson.toJsonTree(set3.getName()));
        } else {
            number.add("qing3", gson.toJsonTree(""));
            number.add("name3", gson.toJsonTree(""));
        }

        if (set4 != null) {
            number.add("qing4", gson.toJsonTree(set4.getPhone2()));
            number.add("name4", gson.toJsonTree(set4.getName()));
        } else {
            number.add("qing4", gson.toJsonTree(""));
            number.add("name4", gson.toJsonTree(""));
        }

        if (sos != null) {
            number.add("sos", gson.toJsonTree(sos.get("phone")));
            number.add("sosname", gson.toJsonTree(sos.get("name")));
        } else {
            number.add("sos", gson.toJsonTree(""));
            number.add("sosname", gson.toJsonTree(""));
        }

        json.add("number", number);
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", gson.toJsonTree("[0,0]"));
        return json;
    }

    private JsonObject BlackSetAction(int user_id) {
        List<PhoneSetInfo> list = actionService.selBlackSetInfByUser(user_id);
        if (list == null || list.size() <= 0) return null;
        JsonArray blacklist = new JsonArray();
        for (PhoneSetInfo item : list) {
            JsonObject json = new JsonObject();
            json.add("number", gson.toJsonTree(item.getPhone()));
            json.add("name", gson.toJsonTree(item.getName()));
            blacklist.add(json);
        }
        JsonObject json = new JsonObject();
        json.add("name", gson.toJsonTree("blackset"));
        json.add("blacklist", blacklist);
        json.add("type", gson.toJsonTree("once"));
        json.add("repeat", gson.toJsonTree("[0,0]"));
        return json;
    }

    private JsonObject WhiteSetAction(int user_id) {
        List<PhoneSetInfo> list = actionService.selWhiteSetInfByUser(user_id);
        if (list == null || list.size() <= 0) return null;
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
        json.add("repeat", gson.toJsonTree("[0,0]"));
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
            time_item.add("start", gson.toJsonTree(item.getStart()));
            time_item.add("stop", gson.toJsonTree(item.getStop()));
            time_item.add("weeks", gson.toJsonTree(item.getWeeks()));
            time_item.add("runmode", gson.toJsonTree(item.getRunmode()));
            time.add(time_item);
        }
        json.add("time", time);
        json.add("eventname", gson.toJsonTree("runmode"));
        json.add("op", gson.toJsonTree("modify"));
        json.add("defaultmode", gson.toJsonTree(mode_inf.get("user_mode")));
        return json;
    }

    private JsonObject makeRepeat(int times, int interval) {
        JsonObject json = new JsonObject();
        json.add("times", gson.toJsonTree(times));
        json.add("interval", gson.toJsonTree(interval));
        return json;
    }

    public JsonArray ActionFactory(int user_id) {
        JsonArray array = new JsonArray();
        array.addAll(SendSmsAction(user_id));
        array.addAll(PlayVoiceOnceAction(user_id));
        array.addAll(MonitorAction(user_id));
        array.addAll(LocationOnceAction(user_id));
        array.addAll(PowerOffAction(user_id));
        array.addAll(ClearSmsAction(user_id));

        List<ActionSetting> list = actionService.selActionSettingByUser(user_id, WatchProperty.ReadStatus.UNREAD);
        for (ActionSetting item : list) {
            String name = item.getName();
            switch (name) {
                case "playvoice":
                    array.add(PlayVoiceEventAction(user_id));
                    break;
                case "location":
                    array.add(LocationEventAction(user_id));
                    break;
                case "log":
                    array.add(LogAction(user_id));
                    break;
                case "qingset":
                    array.add(QingSetAction(user_id));
                    break;
                case "blackset":
                    array.add(BlackSetAction(user_id));
                    break;
                case "whiteset":
                    array.add(WhiteSetAction(user_id));
                    break;
                case "runmode":
                    array.add(RunModeAction(user_id));
                    break;
                default:
                    break;
            }
        }
        return null;
    }
}
