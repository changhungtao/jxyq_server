package com.jxyq.mapper.normal_mapper;

import com.jxyq.model.family.Camera;
import com.jxyq.model.family.CameraMore;
import com.jxyq.model.family.TouchButton;
import com.jxyq.model.family.TouchButtonEvent;

import java.util.List;
import java.util.Map;

public interface FamilyMapper {
    List<CameraMore> selCameraList(Map<String, Object> map);

    List<Map<String, Object>> qryCameraByPage(Map<String, Object> map);

    CameraMore selCameraMore(Map<String, Object> map);

    Camera selectCamera(Map<String, Object> map);

    List<CameraMore> selCameraInfListMap(Map<String, Object> map);

    void insertCamera(Camera camera);

    void updateCamera(Camera camera);

    void inCameraMore(CameraMore cameraMore);

    void upCameraMore(CameraMore cameraMore);

    void deleteCamera(Map<String, Object> map);

    void delCameraMore(Map<String, Object> map);

    List<TouchButton> selTouchButton(Map<String, Object> map);

    List<Map<String, Object>> qryTouchButtonByPage(Map<String, Object> map);

    void inTouchButton(TouchButton touchButton);

    TouchButton selTouchButtonById(Map<String, Object> map);

    void delTouchButton(Map<String, Object> map);

    void inTouchButtonEvent(TouchButtonEvent touchButtonEvent);

    List<Map<String, Object>> selTouchEvent(Map<String, Object> map);

    List<Map<String, Object>> qryTouchEventByPage(Map<String, Object> map);
}
