package com.jxyq.service.inf;

import com.jxyq.commons.mybatis.pagination.PagingCriteria;
import com.jxyq.model.family.Camera;
import com.jxyq.model.family.CameraMore;
import com.jxyq.model.family.TouchButton;
import com.jxyq.model.family.TouchButtonEvent;

import java.util.List;
import java.util.Map;

public interface FamilyService {
    List<CameraMore> selCameraList(Map<String, Object> map);

    List<Map<String, Object>> qryCameraByPage(Map<String, Object> map);

    void insertCamera(Camera camera);

    void updateCamera(Camera camera);

    void inCameraMore(CameraMore cameraMore);

    void upCameraMore(CameraMore cameraMore);

    void deleteCamera(Integer camera_id);

    void delCameraMore(Integer camera_id);

    Camera selectCamera(Integer camera_id);

    CameraMore selCameraMore(Integer camera_id);

    List<TouchButton> selTouchButtonByUserId(int user_id);

    List<Map<String, Object>> qryTouchButtonByPage(Map<String, Object> map);

    List<TouchButton> selTouchButtonByDevUid(String dev_uid);

    void inTouchButton(TouchButton touchButton);

    TouchButton selTouchButtonById(int id);

    void delTouchButton(int id);

    void inTouchButtonEvent(TouchButtonEvent touchButtonEvent);

    List<Map<String, Object>> selTouchEvent(Map<String, Object> map, PagingCriteria pageInf);
}
