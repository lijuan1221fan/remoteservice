package com.visionvera.remoteservice.service;

import java.util.Map;

/**
 * ClassName: ProjectChangeService
 *
 * @author quboka
 * @Description: 项目变更
 * @date 2018年5月8日
 */
public interface ProjectChangeService {

  /**
   * @param parasMap
   * @return Map<String   ,   Object>
   * @Description: 查询变更列表
   * @author quboka
   * @date 2018年5月8日
   */
  Map<String, Object> getProjectChangeList(Map<String, Object> parasMap);

}
