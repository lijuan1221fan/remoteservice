package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.service.TaskLoadService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TaskLoadController
 *
 * @author quboka
 * @Description: 任务负载控制器
 * @date 2018年6月7日
 */
@RestController
public class TaskLoadController {

  private static Logger logger = LoggerFactory.getLogger(TaskLoadController.class);

  @Resource
  private TaskLoadService taskLoadService;

  /**
   * @param maximum 最大任务量
   * @return Map<String   ,   Object>
   * @Description: 设置任务负载
   * @author quboka
   * @date 2018年6月7日
   */
  @RequestMapping("/setTaskLoad")
  public Map<String, Object> setTaskLoad(
      @RequestParam(value = "maximum", required = true) Integer maximum) {
    taskLoadService.setTaskLoadAndModify(maximum);
    return ResultUtils.ok("设置任务负载成功");
  }


  /**
   * @return Map<String   ,   Object>
   * @Description: 查看任务负载
   * @author quboka
   * @date 2018年6月7日
   */
  @RequestMapping("/getTaskLoad")
  @ResponseBody
  public Map<String, Object> getTaskLoad() {
    return taskLoadService.getTaskLoad();
  }
}
