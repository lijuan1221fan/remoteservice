package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.service.DetailsClassService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: DetailsClassificationController
 *
 * @author quboka
 * @Description: 业务详细分类控制器
 * @date 2018年5月8日
 */
@RestController
@RequestMapping(value = "/details")
public class DetailsClassController {

  private static Logger logger = LoggerFactory.getLogger(DetailsClassController.class);

  @Resource
  private DetailsClassService detailsClassService;

  /**
   * @param businessType 业务类型。  10011 ：婚姻变更
   * @return Map<String   ,   Object>
   * @Description: 业务获取详情类别列表
   * @author quboka
   * @date 2018年5月8日
   */
  @RequestMapping(value = "/getClassificationList", method = RequestMethod.POST)
  public Map<String, Object> getClassificationList(
      @RequestParam(value = "businessType", defaultValue = "10011") String businessType) {
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("businessType", businessType);
    return detailsClassService.getClassificationList(paramsMap);
  }

}
