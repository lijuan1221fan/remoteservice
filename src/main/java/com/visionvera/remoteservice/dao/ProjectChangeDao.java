package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.ProjectChangeBean;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ProjectChangeDao
 *
 * @author quboka
 * @Description: 项目变更
 * @date 2018年5月8日
 */
public interface ProjectChangeDao {

  /**
   * @param projectChange
   * @return int
   * @Description: 添加项目变更
   * @author quboka
   * @date 2018年5月8日
   */
  int addProjectChange(ProjectChangeBean projectChange);

  /**
   * @param parasMap
   * @return List<ProjectChangeBean>
   * @Description: 查询变更列表
   * @author quboka
   * @date 2018年5月8日
   */
  List<ProjectChangeBean> getProjectChangeList(
      Map<String, Object> parasMap);

}
