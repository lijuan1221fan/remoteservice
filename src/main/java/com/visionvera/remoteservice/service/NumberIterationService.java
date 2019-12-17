package com.visionvera.remoteservice.service;

import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * ClassName: NumberIterationService
 *
 * @author quboka
 * @Description: 号码递增
 * @date 2018年3月22日
 */
public interface NumberIterationService {

  /**
   * 叫号接口
   *
   * @param serviceKey 服务中心唯一标识
   * @param deptId 部门id
   * @param businessType 业务类型
   * @return
   * @
   */
  Map<String, Object> getNumberAndModify(String serviceKey, Integer deptId, String businessType);

  // /**
  //  * @return int
  //  * @Description: 号码归零
  //  * @author quboka
  //  * @date 2018年3月29日
  //  */
  // int updateNumberIsZero();


  // /**
  //  * @param serviceKey服务中心唯一标识
  //  * @param type 类型  1000：公安远程服务 2000：社保远程服务
  //  * @param businessType 业务类型，参照业务类型表
  //  * @param idCard 身份证号码
  //  * @param deviceType 硬件类型  01:pad 02:一体机
  //  * @return Map<String   ,   Object>
  //  * @Description: 取号
  //  * @author zhanghui
  //  * @date 2018年5月23日
  //  */
  // Map<String, Object> getPrintInfo(String serviceKey, String type, String businessType,
  //     String idCard, String deviceType);

  /**
   * @param session
   * @return Map<String   ,   Object>
   * @Description: 获取当天总任务数
   * @author quboka
   * @date 2018年5月24日
   */
  Map<String, Object> totalNumberOfTasks(HttpSession session);

  Map<String, Object> getWaitingNumber();

  /**
   * 　　* @Description: 获取首页统计数量 　　* @author: xueshiqi 　　* @date: 2018/10/30
   */
  Map<String, Object> totalNumberOfTasks(Map<String, Object> params, HttpSession session);
}
