package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.BusinessLog;
import com.visionvera.remoteservice.dao.BusinessLogDao;
import com.visionvera.remoteservice.service.BusinessLogService;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author lijintao
 */
@Service
public class BusinessLogServiceImpl implements BusinessLogService {

  private static Logger logger = LoggerFactory.getLogger(BusinessLogServiceImpl.class);
  @Resource
  private BusinessLogDao businessLogDao;

  @Override
  public void insertLog(Integer businessId, Integer businessLogType, String remark,
      Integer photoId) {
    int result = businessLogDao
        .insertSelective(new BusinessLog(businessId, businessLogType, remark, photoId));
    if (result == 0) {
      logger.info("保存业务失败，插入业务日志失败");
    }
  }

  @Override
  public List<BusinessLog> getBusinessLogList(Integer businessId) {
    return businessLogDao.selectByBusinessId(businessId);
  }

}