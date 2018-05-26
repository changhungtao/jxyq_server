package com.jxyq.service.impl;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.mapper.normal_mapper.FamilyMapper;
import com.jxyq.model.family.Camera;
import com.jxyq.model.family.CameraMore;
import com.jxyq.model.family.TouchButton;
import com.jxyq.model.family.TouchButtonEvent;
import com.jxyq.service.inf.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FamilyServiceImpl implements FamilyService {
    @Autowired
    private FamilyMapper familyMapper;

    @Override
    public List<CameraMore> selCameraList(Map<String, Object> map) {
        return familyMapper.selCameraInfListMap(map);
    }

    @Override
    public List<Map<String, Object>> qryCameraByPage(Map<String, Object> map) {
        return familyMapper.qryCameraByPage(map);
    }

    @Override
    public void insertCamera(Camera camera) {
        familyMapper.insertCamera(camera);
    }

    @Override
    public void updateCamera(Camera camera) {
        familyMapper.updateCamera(camera);
    }

    @Override
    public void inCameraMore(CameraMore cameraMore) {
        familyMapper.inCameraMore(cameraMore);
    }

    @Override
    public void upCameraMore(CameraMore cameraMore) {
        familyMapper.upCameraMore(cameraMore);
    }

    @Override
    public void deleteCamera(Integer camera_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("camera_id", camera_id);
        familyMapper.deleteCamera(map);
    }

    @Override
    public void delCameraMore(Integer camera_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("camera_id", camera_id);
        familyMapper.delCameraMore(map);
    }

    @Override
    public Camera selectCamera(Integer camera_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("camera_id", camera_id);
        return familyMapper.selectCamera(map);
    }

    @Override
    public CameraMore selCameraMore(Integer camera_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("camera_id", camera_id);
        return familyMapper.selCameraMore(map);
    }

    @Override
    public List<TouchButton> selTouchButtonByUserId(int user_id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", user_id);
        return familyMapper.selTouchButton(map);
    }

    @Override
    public List<Map<String, Object>> qryTouchButtonByPage(Map<String, Object> map) {
        return familyMapper.qryTouchButtonByPage(map);
    }

    @Override
    public List<TouchButton> selTouchButtonByDevUid(String dev_uid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("dev_uid", dev_uid);
        return familyMapper.selTouchButton(map);
    }

    @Override
    public void inTouchButton(TouchButton touchButton) {
        familyMapper.inTouchButton(touchButton);
    }

    @Override
    public TouchButton selTouchButtonById(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("touch_button_id", id);
        return familyMapper.selTouchButtonById(map);
    }

    @Override
    public void delTouchButton(int id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("touch_button_id", id);
        familyMapper.delTouchButton(map);
    }

    @Override
    public void inTouchButtonEvent(TouchButtonEvent touchButtonEvent) {
        familyMapper.inTouchButtonEvent(touchButtonEvent);
    }

    @Override
    public List<Map<String, Object>> selTouchEvent(Map<String, Object> map, PagingCriteria pageInf) {
        map.put(BeanProperty.PAGING, pageInf);
        return familyMapper.qryTouchEventByPage(map);
    }
}
