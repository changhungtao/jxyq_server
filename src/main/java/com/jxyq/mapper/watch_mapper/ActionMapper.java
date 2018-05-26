package com.jxyq.mapper.watch_mapper;

import com.jxyq.model.watch.*;

import java.util.List;
import java.util.Map;

public interface ActionMapper {
    List<ActionSetting> selActionSetting(Map<String, Object> map);

    void upActionSettingRead(Map<String, Object> map);

    void inActionSetting(ActionSetting actionSetting);

    List<SendSms> selSendSms(Map<String, Object> map);

    void upSendSmsRead(Map<String, Object> map);

    List<PlayVoice> selAlarmSet(Map<String, Object> map);

    List<PlayVoice> selHealthSet(Map<String, Object> map);

    List<PlayVoice> selPillsSet(Map<String, Object> map);

    List<PlayVoice> selSleepSet(Map<String, Object> map);

    List<PlayVoice> selSportSet(Map<String, Object> map);

    void upAlarmSet(PlayVoice action);

    void upHealthSet(PlayVoice action);

    void upPillsSet(PlayVoice action);

    void upSleepSet(PlayVoice action);

    void upSportSet(PlayVoice action);

    void inEventPlayVoice(PlayVoice playVoice);

    void delAlarmSet(Map<String, Object> map);

    void delHealthSet(Map<String, Object> map);

    void delPillsSet(Map<String, Object> map);

    void delSleepSet(Map<String, Object> map);

    void delSportSet(Map<String, Object> map);

    List<Monitor> selMonitor(Map<String, Object> map);

    void inOnceMonitor(Monitor monitor);

    void upMonitorRead(Map<String, Object> map);

    List<OncePlayVoice> selPlayVoice(Map<String, Object> map);

    void upPlayVoice(Map<String, Object> map);

    void inOncePlayVoice(OncePlayVoice oncePlayVoice);

    List<LocationAction> selLocation(Map<String, Object> map);

    void inOnceLocation(LocationAction locationAction);

    void upLocationRead(Map<String, Object> map);

    List<RouteSet> selRouteSet(Map<String, Object> map);

    List<LogSet> selLogSet(Map<String, Object> map);

    List<OnceAction> selOncePowerOff(Map<String, Object> map);

    void upOncePowerOffRead(Map<String, Object> map);

    List<OnceAction> selOnceClearSms(Map<String, Object> map);

    void upOnceClearSmsRead(Map<String, Object> map);

    QingSet selQing1Set(Map<String, Object> map);

    QingSet selQing2Set(Map<String, Object> map);

    QingSet selQing3Set(Map<String, Object> map);

    QingSet selQing4Set(Map<String, Object> map);

    void inQing1Set(QingSet qingSet);

    void inQing2Set(QingSet qingSet);

    void inQing3Set(QingSet qingSet);

    void inQing4Set(QingSet qingSet);

    List<PhoneSetInfo> selBlackSetInf(Map<String, Object> map);

    void inBlackSetInf(PhoneSetInfo phoneSetInfo);

    Map<String, Object> selBlackSetCount(Map<String, Object> map);

    List<PhoneSetInfo> selWhiteSetInf(Map<String, Object> map);

    void inWhiteSetInf(PhoneSetInfo phoneSetInfo);

    Map<String, Object> selWhiteSetCount(Map<String, Object> map);

    List<ModeInf> selModeInf(Map<String, Object> map);

    Map<String, Object> selModeCnt(Map<String, Object> map);

    Map<String, Object> selSOSByUser(Map<String, Object> map);

    void upSOSNumber(Map<String, Object> map);

    Map<String, Object> selUserMode(Map<String, Object> map);

    Map<String, Object> selRouteSetCnt(Map<String, Object> map);

    void inRouteSet(RouteSet routeSet);

    void upRouteSet(RouteSet routeSet);

    void delRouteSet(Map<String, Object> map);

    void upUserMode(Map<String, Object> map);

    void inModeInf(ModeInf modeInf);
    public void delBlackSetInf(int id);
    void delWhiteSetInf(int id);
    void upModeInf(ModeInf modeInf);

    void delModeInf(Map<String, Object> map);

    Map<String, Object> selActionSettingCount(Map<String, Object> map);

    Map<String, Object> selOnceClearSmsCount(Map<String, Object> map);

    Map<String, Object> selOncePowerOffCount(Map<String, Object> map);

    Map<String, Object> selLocationCount(Map<String, Object> map);

    Map<String, Object> selPlayVoiceCount(Map<String, Object> map);

    Map<String, Object> selMonitorCount(Map<String, Object> map);

    Map<String, Object> selSendSmsCount(Map<String, Object> map);

}
