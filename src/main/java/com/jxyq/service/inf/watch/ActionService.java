package com.jxyq.service.inf.watch;

import com.jxyq.model.watch.*;

import java.util.List;
import java.util.Map;

public interface ActionService {
    List<ActionSetting> selActionSettingByUser(int user_id, int readed);

    void upActionSettingReadByUser(int user_id, int readed);

    void upActionSettingRead(int id, int readed);

    void inActionSetting(ActionSetting actionSetting);

    List<SendSms> selSendSmsByUser(int user_id, int readed);

    void upSendSmsRead(int id, int readed);

    List<PlayVoice> selAlarmSetByUser(int user_id, int enable);

    List<PlayVoice> selHealthSetByUser(int user_id, int enable);

    List<PlayVoice> selPillsSetByUser(int user_id, int enable);

    List<PlayVoice> selSleepSetByUser(int user_id, int enable);

    List<PlayVoice> selSportSetByUser(int user_id, int enable);

    void upPlayVoiceByName(PlayVoice action, String name);

    void inEventPlayVoice(PlayVoice playVoice);

    void delPlayVoiceByName(int eventid, String eventname);

    List<OncePlayVoice> selPlayVoiceByUser(int user_id, int readed);

    void upOncePlayVoice(int id, int readed);

    void inOncePlayVoice(OncePlayVoice oncePlayVoice);

    List<Monitor> selMonitorByUser(int user_id, int readed);

    void inOnceMonitor(Monitor monitor);

    void upMonitorRead(int id, int readed);

    List<LocationAction> selLocationByUser(int user_id, int readed);

    void inOnceLocation(LocationAction locationAction);

    void upLocationRead(int id, int readed);

    List<RouteSet> selRouteSetByUser(int user_id, int enable);

    void inRouteSet(RouteSet routeSet);

    void upRouteSet(RouteSet routeSet);

    void delRouteSet(int route_period_id);

    Map<String, Object> selRouteSetCntByUser(int user_id);

    List<LogSet> selLogSetByUser(int user_id);

    List<OnceAction> selOncePowerOffByUser(int user_id, int readed);

    void upOncePowerOffRead(int id, int readed);

    List<OnceAction> selOnceClearSmsByUser(int user_id, int readed);

    void upOnceClearSmsRead(int id, int readed);

    QingSet selQingSetByUser(int user_id, int id);

    void inQingSet(QingSet qingSet, int id);

    Map<String, Object> selSOSByUser(int user_id);

    void upSOSNumber(int user_id, String sos_name, String sos_no);

    List<PhoneSetInfo> selBlackSetInfByUser(int user_id);

    void delBlackSetInf(int id);

    void inBlackSetInf(PhoneSetInfo phoneSetInfo);

    Map<String, Object> selBlackSetCntByUser(int user_id);

    List<PhoneSetInfo> selWhiteSetInfByUser(int user_id);

    void delWhiteSetInf(int id);

    void inWhiteSetInf(PhoneSetInfo phoneSetInfo);

    Map<String, Object> selWhiteSetCntByUser(int user_id);

    List<ModeInf> selModeInfByUser(int user_id);

    void inModeInf(ModeInf modeInf);

    void delModeInf(int mode_inf_id);

    void upModeInf(ModeInf modeInf);

    Map<String, Object> selModeCntByUser(int user_id);

    Map<String, Object> selUserMode(int user_id);

    void upUserMode(int user_id, int mode_type, int user_mode);

    ModeInf selModeInfById(int id);

    Map<String, Object> selSendSmsCntByUser(int user_id, int readed);

    Map<String, Object> selPlayVoiceCntByUser(int user_id, int readed);

    Map<String, Object> selMonitorCntByUser(int user_id, int readed);

    Map<String, Object> selLocationCntByUser(int user_id, int readed);

    Map<String, Object> selOncePowerOffCntByUser(int user_id, int readed);

    Map<String, Object> selOnceClearSmsCntByUser(int user_id, int readed);

    Map<String, Object> selActionSettingCntByUser(int user_id, int readed);

    Map<String, Object> selActionSettingCntByUserAndAction(int user_id, int readed, String event_name);
}
