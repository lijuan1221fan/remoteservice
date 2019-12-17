package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.service.NumberIterationService;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: NumberIterationController
 *
 * @author quboka
 * @Description: 号码递增
 * @date 2018年3月22日
 */
@RestController
public class NumberIterationController {

  private static Logger logger = LoggerFactory.getLogger(NumberIterationController.class);

  @Resource
  private NumberIterationService numberIterationService;

  /**
   * @param serviceKey 服务中心唯一标识
   * @param type 类型  1000：公安远程服务 2000：社保远程服务
   * @param businessType 业务类型，参照业务类型表
   * @return Map<String   ,   Object>
   * @Description: 取号
   * @author quboka
   * @date 2018年3月22日
   */
  @RequestMapping("getNumber")
  public Map<String, Object> getNumber(
      @RequestParam(value = "serviceKey") String serviceKey,
      @RequestParam(value = "deptId") Integer deptId,
      @RequestParam(value = "businessType") String businessType) {
    return numberIterationService.getNumberAndModify(serviceKey, deptId, businessType);
  }

  /**
   * 获取等待人数
   *
   * @param session 用户session
   * @param serviceKey 服务中心key
   * @return
   */
  @RequestMapping("totalNumberOfTasks")
  public Map<String, Object> getWaitingNumber(HttpSession session,
      @RequestParam(value = "serviceKey", required = false) String serviceKey) {
    return numberIterationService.getWaitingNumber();
  }
}
