package com.jxyq.service.impl.watch;

import com.jxyq.commons.constants.WatchProperty;
import com.jxyq.mapper.watch_mapper.RouteMapper;
import com.jxyq.model.watch.PenSet;
import com.jxyq.model.watch.PenSetHexagon;
import com.jxyq.model.watch.Route;
import com.jxyq.service.inf.watch.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public void insertRoute(Route route) {
        routeMapper.insertRoute(route);
    }

    @Override
    public List<Route> selRouteList(int user_id, String table_name) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", user_id);
        map.put("table_name", table_name);
        return routeMapper.selRouteList(map);
    }

    @Override
    public List<PenSetHexagon> selPenSetHexagonByUserId(int userId, int deleted_flag) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", userId);
        map.put("deleted_flag", deleted_flag);
        return routeMapper.selPenSetHexagon(map);
    }

    @Override
    public List<PenSetHexagon> selPenSet(Map<String, Object> map) {
        return routeMapper.selPenSetHexagon(map);
    }

    @Override
    public PenSetHexagon selPenSetById(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        return routeMapper.selPenSetById(map);
    }

    @Override
    public Map<String, Object> selPenSetCntByUserId(int userId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", userId);
        map.put("deleted_flag", WatchProperty.DeletedFlag.UNDELETED);
        return routeMapper.selPenSetCnt(map);
    }

    @Override
    public void repPenSetHexagon(PenSetHexagon penSetHexagon) {
        routeMapper.repPenSetHexagon(penSetHexagon);
    }

    @Override
    public void repPenSet(PenSet penSet) {
        routeMapper.repPenSet(penSet);
    }

    @Override
    public void delPenSetHexagon(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        routeMapper.delPenSetHexagon(map);
    }

    @Override
    public void delPenSet(int id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        routeMapper.delPenSet(map);
    }

    @Override
    public void delPenSetFlag(int id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("deleted_flag", WatchProperty.DeletedFlag.DELETED);
        routeMapper.delPenSetFlag(map);
    }

    @Override
    public Map<String, Object> selTableCnt(String schema_name, String table_name) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("schema_name", schema_name);
        map.put("table_name", table_name);
        return routeMapper.selTableCnt(map);
    }
}
