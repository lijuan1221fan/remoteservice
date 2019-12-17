package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.ProjectChangeBean;
import com.visionvera.remoteservice.dao.ProjectChangeDao;
import com.visionvera.remoteservice.service.ProjectChangeService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: ProjectChangeServiceImpl
 *
 * @author quboka
 * @Description: 项目变更
 * @date 2018年5月8日
 */
@Service
public class ProjectChangeServiceImpl implements ProjectChangeService {

  private static Logger logger = LoggerFactory.getLogger(ProjectChangeServiceImpl.class);

  @Resource
  private ProjectChangeDao projectChangeDao;

  /**
   * @param parasMap
   * @return Map<String   ,   Object>
   * @Description: 查询变更列表
   * @author quboka
   * @date 2018年5月8日
   */
  @Override
  public Map<String, Object> getProjectChangeList(Map<String, Object> parasMap) {
    List<ProjectChangeBean> list = projectChangeDao.getProjectChangeList(parasMap);
    if (list != null) {
      logger.info("查询变更列表成功" + list);
    } else {
      logger.info("查询变更列表成失败");
    }
    return ResultUtils.ok("查询变更列表成功", list);
  }

}
