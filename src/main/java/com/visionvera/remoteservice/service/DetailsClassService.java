package com.visionvera.remoteservice.service;

import java.util.Map;

/**
 * ClassName: DetailsClassService
 *
 * @author quboka
 * @Description: 业务详细分类
 * @date 2018年5月8日
 */
public interface DetailsClassService {

  /**
   * @param businessType 业务类型。  10011 ：婚姻变更
   * @return Map<String   ,   Object>
   * @Description: 业务获取详情类别列表
   * @author quboka
   * @date 2018年5月8日
   */
  Map<String, Object> getClassificationList(Map<String, Object> paramsMap);

}
