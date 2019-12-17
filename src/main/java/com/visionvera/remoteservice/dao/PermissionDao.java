package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.PermissionBean;
import java.util.List;

/**
 * ClassName: PermissionDao
 *
 * @author quboka
 * @Description: 权限
 * @date 2018年3月22日
 */
public interface PermissionDao {

  /**
   * @param userId
   * @return List<PermissionBean>
   * @Description: 根据用户id查询出权限集合
   * @author quboka
   * @date 2018年3月22日
   */
  List<PermissionBean> getPermissionListByUserId(Integer userId);

}
