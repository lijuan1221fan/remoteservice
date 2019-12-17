package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.SysUserBean;
import java.util.Map;

/**
 * Created by quboka on 2018/8/22.
 */
public interface TitleService {

  /**
   * @param [user]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询标题
   * @author quboka
   * @date 2018/8/22 14:07
   */
  Map<String, Object> getTitle(SysUserBean user, Integer deptId);

  /**
   * @param [user, deptId]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改标题
   * @author quboka
   * @date 2018/8/22 15:41
   */
  Map<String, Object> updateTitle(SysUserBean user, Integer deptId, String deptTitle);
}
