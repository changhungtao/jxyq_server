package com.jxyq.mapper.watch_mapper;

import com.jxyq.model.watch.PenSet;
import com.jxyq.model.watch.PenSetHexagon;
import com.jxyq.model.watch.Route;

import java.util.List;
import java.util.Map;

public interface RouteMapper {
    void insertRoute(Route route);

    List<Route> selRouteList(Map<String, Object> map);

    List<PenSetHexagon> selPenSetHexagon(Map<String, Object> map);

    Map<String, Object> selPenSetCnt(Map<String, Object> map);

    void repPenSetHexagon(PenSetHexagon penSetHexagon);

    void repPenSet(PenSet penSet);

    PenSetHexagon selPenSetById(Map<String, Object> map);

    void delPenSetHexagon(Map<String, Object> map);

    void delPenSet(Map<String, Object> map);

    void delPenSetFlag(Map<String, Object> map);

    Map<String, Object> selTableCnt(Map<String, Object> map);
}
