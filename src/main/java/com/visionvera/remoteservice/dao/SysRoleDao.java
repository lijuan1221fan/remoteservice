package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.RoleBean;
import com.visionvera.remoteservice.bean.SysPermissionBean;
import com.visionvera.remoteservice.bean.SysRoleBean;
import com.visionvera.remoteservice.bean.SysRolePermissionBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 　　* @Description: TODO 　　* @author: xueshiqi 　　* @date: 2018/10/10
 */
public interface SysRoleDao {

  /**
   * @param userId
   * @return List<String>
   * @Description: 根据用户id获取角色名称列表
   * @author xueshiqi
   * @date 2018/10/10
   */
  List<String> getSysRoleNameByUserId(Integer userId);

  /**
   * @param userId
   * @return List<RoleBean>
   * @Description: 根据用户id获取角色列表
   * @author xueshiqi
   * @date 2018/10/10
   */
  List<RoleBean> getSysRoleByUserId(Integer userId);

  /**
   * @return List<RoleBean>
   * @Description: 角色列表
   * @author xueshiqi
   * @date 2018/10/10
   */
  List<SysRoleBean> getSysRoleList(@Param("roleName") String roleName,
      @Param("userId") Integer userId);

  /**
   * @param roleId
   * @param userId
   * @return Integer
   * @Description: 添加用户角色关联
   * @author xueshiqi
   * @date 2018/10/10
   */
  Integer addSysUserAndRoleRel(@Param(value = "roleId") Integer roleId,
      @Param(value = "userId") Integer userId);

  /**
   * @param ids
   * @return int
   * @Description: 根据用户id 批量删除角色关联
   * @author xueshiqi
   * @date 22018/10/10
   */
  int deleteSysRoleRel(Integer[] ids);

  /**
   * @param userId
   * @param roleId
   * @return int
   * @Description: 根据用户id 修改 角色关联
   * @author xueshiqi
   * @date 2018/10/10
   */
  int updateSysRelByUserId(@Param(value = "userId") Integer userId,
      @Param(value = "roleId") Integer roleId);

  /**
   * 　　* @Description: 查询所有权限 　　* @author: xueshiqi 　　* @date: 2018/10/10
   */
  List<SysPermissionBean> getSysPermissionList();

  /**
   * 　　* @Description: 一级权限列表 　　* @author: xueshiqi 　　* @date: 2018/10/10
   */
  List<SysPermissionBean> getParentSysPermissionList(Long parentId);

  /**
   * 　　* @Description: 二级权限列表 　　* @author: xueshiqi 　　* @date: 2018/10/10
   */
  List<SysPermissionBean> getChildSysPermissionList(Integer permissionId);

  /**
   * 　　* @Description: 获取角色信息 　　* @author: xueshiqi 　　* @date: 2018/10/11
   */
  SysRoleBean getSysRoleInfo(Integer roleId);

  /**
   * 　　* @Description: 更改角色信息 　　* @author: xueshiqi 　　* @date: 2018/10/11
   */
  Integer editSysRoleInfo(SysRoleBean sysRoleBean);

  /**
   * 　　* @Description: 删除角色表 　　* @author: xueshiqi 　　* @date: 2018/10/12
   */
  void deleteRoleById(@Param("roleIds") List<Integer> roleIds);

  /**
   * 　　* @Description: 删除角色用户关联表 　　* @author: xueshiqi 　　* @date: 2018/10/12
   */
  void deleteRoleUserRel(@Param("roleIds") List<Integer> roleIds);

  /**
   * 　　* @Description: 删除角色权限关系表 　　* @author: xueshiqi 　　* @date: 2018/10/12
   */
  void deleteRolePermissionRel(@Param("roleIds") List<Integer> roleIds);

  /**
   * 　　* @Description: 新增角色表 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  void addSysRole(SysRoleBean sysRoleBean);

  /**
   * 　　* @Description: 新增角色权限关系表 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  void addSysUserPermission(List<SysRolePermissionBean> list);

  /**
   * 　　* @Description: 根据用户id查询对应角色 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  SysRoleBean getRoleByUserId(Integer userId);

  /**
   * 　　* @Description: 根据用户id查询对应权限 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  List<SysPermissionBean> getPermissionListByUserId(Integer userId);

  /**
   * 查询用户所有的权限id
   *
   * @return
   */
  List<Long> getAllMenuId();

  /**
   * 根据用户id查询所有权限id
   *
   * @param userId
   * @return
   */
  List<Long> getMenuIdList(Integer userId);


  /**
   * 获取用户角色
   *
   * @param userId
   * @return
   */
  List<String> getRoles(Integer userId);

  /**
   * 查询所有权限
   *
   * @return
   */
  List<SysPermissionBean> getAllPermission();

  /**
   * 根据用户查询对应权限
   *
   * @param userId
   * @return
   */
  List<String> getPermsListByUserId(Integer userId);

  /**
   * 用户顶级菜单
   *
   * @param user
   * @return
   */
  List<SysPermissionBean> getFirstLevelMenu(SysUserBean user);

  /**
   * 用户二级菜单
   *
   * @param firstMenu
   * @return
   */
  List<SysPermissionBean> isHaveChildMenu(SysPermissionBean firstMenu);

  /**
   * 获取菜单下按钮
   *
   * @param permissionId
   * @return
   */
  List<SysPermissionBean> getUserMenuButton(@Param("permissionId") String permissionId,
      @Param("userId") Integer userId);

  /**
   * 根据角色id查询菜单
   *
   * @param roleId
   * @return
   */
  List<Long> getRoleMenuIdList(Integer roleId);

  /**
   * 根据用户查询对应角色
   *
   * @param userId
   * @return
   */
  Integer getUserRoleInfo(Integer userId);

  /**
   * 　　* @Description:根据父id查询按钮 　　* @author: xueshiqi 　　* @date: 2018/11/19 0019
   */
  List<SysPermissionBean> getUserMenuButtonByPermissionId(String permissionId);

  /**
   * 　　* @Description: 根据用户id获取用户权限按钮列表 　　* @author: xueshiqi 　　* @date: 2018/12/3
   */
  List<SysPermissionBean> queryUserButtonList(Integer userId);

  /**
   * 　　* @Description: 查询所有按钮权限 　　* @author: xueshiqi 　　* @date: 2018/12/3
   */
  List<SysPermissionBean> queeyAllUserButtonList();

  /**
  　　* @Description: 重名校验
  　　* @author: xueshiqi
  　　* @date: 2019/1/4
  　　*/
  SysRoleBean getSysRole(@Param("roleName") String roleName, @Param("userId") Integer userId);
}