package com.visionvera.app.service;

import com.visionvera.app.entity.AppointmentMaterials;
import com.visionvera.remoteservice.bean.PhotoBean;
import java.util.List;
import java.util.Map;

/**
 * Created by jlm on 2019-03-25 18:48
 */
public interface AppMaterialsService {

    Map<String, Object> uploadAppointMaterial(AppointmentMaterials appointmentMaterials);

    Map<String, Object> delAppointMaterial(Integer id);

    Map<String, Object> getMaterials(Integer appointMentId);

    List<PhotoBean> getAppointmentMaterialsList(Integer businessId);
}
