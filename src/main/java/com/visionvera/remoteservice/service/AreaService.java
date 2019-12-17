package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.ServiceCenterBean;

import java.util.List;
import java.util.Map;

public interface AreaService {

    Map<String, Object> getArea(String id);

    void getAreaName(ServiceCenterBean bean);

    List<String> getAreaIdList(ServiceCenterBean bean);

}
