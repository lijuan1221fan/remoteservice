package com.visionvera.remoteservice.service;

import java.util.Map;

/**
 * ClassName: TaskLoadService
 *
 * @author quboka
 * @Description: 任务负载
 * @date 2018年6月7日
 */
public interface TaskLoadService {


  /**
   * @param maximum 最大任务量
   * @return void
   * @Description: 设置任务负载量
   * @author quboka
   * @date 2018年6月7日
   */
  void setTaskLoadAndModify(Integer maximum);

  /**
   * 修改任务负载量  只作为修改常量值
   *
   * @param maximum 最大任务数
   */
  void changeTaskNum(Integer maximum);

  /**
   * @return Map<String   ,   Object>
   * @Description:查看任务负载
   * @author quboka
   * @date 2018年6月7日
   */
  Map<String, Object> getTaskLoad();

}
