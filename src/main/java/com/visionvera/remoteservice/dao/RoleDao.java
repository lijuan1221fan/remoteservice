package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.RoleBean;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: RoleDao
 *
 * @author quboka
 * @Description: 角色
 * @date 2018年3月22日
 */
public interface RoleDao {

  /**
   * @param userId
   * @return List<String>
   * @Description: 根据用户id获取角色名称列表
   * @author quboka
   * @date 2018年3月23日
   */
  List<String> getRoleNameByUserId(Integer userId);

  /**
   * @param userId
   * @return List<RoleBean>
   * @Description: 根据用户id获取角色列表
   * @author quboka
   * @date 2018年3月25日
   */
  List<RoleBean> getRoleByUserId(Integer userId);

  /**
   * @return List<RoleBean>
   * @Description: 角色列表
   * @author quboka
   * @date 2018年5月15日
   */
  List<RoleBean> getRoleList();

  /**
   * @param roleId
   * @param userId
   * @return Integer
   * @Description: 添加用户角色关联
   * @author quboka
   * @date 2018年5月15日
   */
  Integer addUserAndRoleRel(@Param(value = "roleId") Integer roleId,
      @Param(value = "userId") Integer userId);

  /**
   * @param ids
   * @return int
   * @Description: 根据用户id 批量删除角色关联
   * @author quboka
   * @date 2018年6月4日
   */
  int deleteRoleRel(Integer[] ids);

  /**
   * @param userId
   * @param roleId
   * @return int
   * @Description: 根据用户id 修改 角色关联
   * @author quboka
   * @date 2018年6月4日
   */
  int updateRelByUserId(@Param(value = "userId") Integer userId,
      @Param(value = "roleId") Integer roleId);

}
