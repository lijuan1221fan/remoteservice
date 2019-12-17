package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.service.TaskLoadService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: TaskLoadServiceImpl
 *
 * @author quboka
 * @Description: 任务负载
 * @date 2018年6月7日
 */
@Service
public class TaskLoadServiceImpl implements TaskLoadService {

  private static Logger logger = LoggerFactory.getLogger(TaskLoadServiceImpl.class);

  @Resource
  private CommonConfigDao commonConfigDao;

  /**
   * @param maximum 最大任务量
   * @return void
   * @Description: 设置任务负载量
   * @author quboka
   * @date 2018年6月7日
   */
  @Override
  public synchronized void setTaskLoadAndModify(Integer maximum) {
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    if (maximum != null) {
      //修改数据库任务负载量
      paramsMap.put("commonValue", String.valueOf(maximum));
      paramsMap.put("commonName", CommonConstant.MAX_TASKS_NAME);
      int updateValueByKey = commonConfigDao.updateValueByKey(paramsMap);
      if (updateValueByKey == 0) {
        logger.info("修改数据库任务负载量失败");
      }
    } else {
      //获取数据库中任务负载量
      CommonConfigBean commonConfig = commonConfigDao
          .getCommonConfigByName(CommonConstant.MAX_TASKS_NAME);
      if (commonConfig != null && StringUtils.isNotEmpty(commonConfig.getCommonValue())) {
        String commonValue = commonConfig.getCommonValue();
        //设置最新任务负载最大值
        maximum = Integer.parseInt(commonValue);
      }
    }
    changeTaskNum(maximum);

  }

  @Override
  public void changeTaskNum(Integer maximum) {
    if (maximum == null) {
      return;
    }
    //原最大值
    int originalMaxValue = CommonConstant.MAX_TASKS.get();
    //原可用值
    int taskNumber = CommonConstant.taskNumber.get();
    //差值
    int difference = originalMaxValue - taskNumber;
    //设置最新任务负载最大值
    CommonConstant.MAX_TASKS.set(maximum);

    if (difference > 0) {
      //新值
      int newMaxValue = CommonConstant.MAX_TASKS.get();
      if (newMaxValue >= difference) {
        //可用任务数量
        CommonConstant.taskNumber.set(newMaxValue - difference);
      } else {
        //可用任务数量
        CommonConstant.taskNumber.set(0);
      }

    } else {
      //可用任务数量
      CommonConstant.taskNumber.set(CommonConstant.MAX_TASKS.get());
    }
  }

  /**
   * @return Map<String , Object>
   * @Description:查看任务负载
   * @author quboka
   * @date 2018年6月7日
   */
  @Override
  public Map<String, Object> getTaskLoad() {
    //最大任务数量
    int maxTasks = CommonConstant.MAX_TASKS.get();//10
    //可用任务数
    int taskNumber = CommonConstant.taskNumber.get();//3
    //当前任务数
    int currentTask = maxTasks - taskNumber;//7
    Map<String, Integer> resultMap = new HashMap<String, Integer>();
    resultMap.put("maxTasks", maxTasks);
    resultMap.put("taskNumber", taskNumber);
    resultMap.put("currentTask", currentTask);
    return ResultUtils.ok("查看任务负载成功", resultMap);
  }


}
