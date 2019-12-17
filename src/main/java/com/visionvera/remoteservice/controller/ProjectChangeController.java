package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.service.ProjectChangeService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ProjectChangeController
 *
 * @author quboka
 * @Description: 项目变更
 * @date 2018年5月8日
 */
@RestController
@RequestMapping("/change")
public class ProjectChangeController {

  private static Logger logger = LoggerFactory.getLogger(ProjectChangeController.class);

  @Resource
  private ProjectChangeService projectChangeService;

  /**
   * @param businessKey
   * @return Map<String   ,   Object>
   * @Description: 查询变更列表
   * @author quboka
   * @date 2018年5月8日
   */
  @RequestMapping("getProjectChangeList")
  public Map<String, Object> getProjectChangeList(
      @RequestParam(value = "businessKey", required = false) String businessKey) {
    Map<String, Object> parasMap = new HashMap<String, Object>();
    parasMap.put("businessKey", businessKey);
    return projectChangeService.getProjectChangeList(parasMap);
  }

}
