package com.jxyq.service.inf.watch;

import com.jxyq.model.watch.PenSet;
import com.jxyq.model.watch.PenSetHexagon;
import com.jxyq.model.watch.Route;

import java.util.List;
import java.util.Map;

public interface RouteService {
    void insertRoute(Route route);

    List<Route> selRouteList(int user_id, String table_name);

    List<PenSetHexagon> selPenSetHexagonByUserId(int userId, int deleted_flag);

    List<PenSetHexagon> selPenSet(Map<String, Object> map);

    PenSetHexagon selPenSetById(int id);

    Map<String, Object> selPenSetCntByUserId(int userId);

    void repPenSet(PenSet penSet);

    void repPenSetHexagon(PenSetHexagon penSetHexagon);

    void delPenSetHexagon(int id);

    void delPenSetFlag(int id);

    void delPenSet(int id);

    Map<String, Object> selTableCnt(String schema_name, String table_name);
}
