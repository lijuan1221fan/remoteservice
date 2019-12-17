package com.visionvera.common.service;

import java.util.Map;

/**
 * @author jlm
 * @ClassName:
 * @Description: 日志服务接口
 * @date 2018/10/12
 */
public interface SysLogService {

  /**
   * 分页查询 用于页面显示
   *
   * @param paramMap
   * @return
   * @
   */
  Map<String, Object> getPageLog(Map<String, Object> paramMap);

}
