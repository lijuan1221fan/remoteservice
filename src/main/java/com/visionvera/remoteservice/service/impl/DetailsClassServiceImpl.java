package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.BusinessDetailsClass;
import com.visionvera.remoteservice.dao.DetailsClassDao;
import com.visionvera.remoteservice.service.DetailsClassService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: DetailsClassServiceImpl
 *
 * @author quboka
 * @Description: 业务详细分类
 * @date 2018年5月8日
 */
@Service
public class DetailsClassServiceImpl implements DetailsClassService {

  private static Logger logger = LoggerFactory.getLogger(DetailsClassServiceImpl.class);

  @Resource
  private DetailsClassDao detailsClassDao;


  /**
   * @param businessType 业务类型。  10011 ：婚姻变更
   * @return Map<String   ,   Object>
   * @Description: 业务获取详情类别列表
   * @author quboka
   * @date 2018年5月8日
   */
  @Override
  public Map<String, Object> getClassificationList(
      Map<String, Object> paramsMap) {
    List<BusinessDetailsClass> list = detailsClassDao.getClassificationList(paramsMap);
    logger.info("获取业务详情类别列表成功");
    return ResultUtils.ok("获取业务详情类别列表成功", list);
  }

}
