package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.BusinessLog;
import java.util.List;

public interface BusinessLogService {

  void insertLog(Integer businessId, Integer businessLogType, String remark, Integer photoId);

  List<BusinessLog> getBusinessLogList(Integer businessId);
}
