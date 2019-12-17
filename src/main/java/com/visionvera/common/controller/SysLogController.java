package com.visionvera.common.controller;

import com.visionvera.common.service.SysLogService;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.pojo.SysLogVo;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dell
 * @ClassName:
 * @Description:
 * @date 2018/10/11
 */
@RestController
@RequestMapping("/log")
public class SysLogController {

  private static Logger logger = LoggerFactory.getLogger(SysLogController.class);

  @Resource
  private SysLogService sysLogService;


  /**
   * @Description:分页条件查询日志 用于页面浏览
   * @Date: 2018/10/11
   * @param: * @param
   * @return: java.util.List<com.visionvera.common.entity.SysLog>
   */
  @ResponseBody
  @RequiresPermissions("log:query")
  @RequestMapping(value = "/queryPagelog", method = RequestMethod.POST)
  public Map<String, Object> queryPageLog(@RequestBody SysLogVo sysLogVo) {
    ValidateUtil.validate(sysLogVo, QueryGroup.class);
    Map<String, Object> paramMap = new HashMap<String, Object>();
    try {
      paramMap.put("pageNum", sysLogVo.pageNum);
      paramMap.put("pageSize", sysLogVo.pageSize);
      paramMap.put("userName", sysLogVo.getUserName());
      paramMap.put("operation", sysLogVo.getOperation());
      paramMap.put("startTime", sysLogVo.getStartTime());
      paramMap.put("endTime", sysLogVo.getEndTime());
      return sysLogService.getPageLog(paramMap);
    } catch (Exception e) {
      logger.error("查询日志失败", e);
      return ResultUtils.error("查询日志失败");
    }
  }
}
