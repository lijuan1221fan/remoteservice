package com.visionvera.common.dao;

import com.visionvera.common.entity.SysLog;
import java.util.List;
import java.util.Map;

/**
 * @Description: 日志接口
 * @Date: 2018.10.11
 * @Autho: JLM
 */
public interface SysLogDao {

  /**
   * 保存日志
   *
   * @param sysLog
   * @return
   */
  int insertLog(SysLog sysLog);

  /**
   * 条件查询日志
   *
   * @return
   */
  List<SysLog> queryLog(Map<String, Object> paramMap);

}

