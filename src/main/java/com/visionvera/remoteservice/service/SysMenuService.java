package com.visionvera.remoteservice.service;

import java.util.Map;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author EricShen
 * @since 2017-08-24
 */
public interface SysMenuService {

  /**
   * 用户id查询其权限菜单列表
   *
   * @param userId
   * @return 根用户返回所有菜单
   */
  Map<String, Object> queryUserMenuList(Integer userId);

  /**
   * 根据角色id查询对用权限列表
   *
   * @param roleId
   * @return
   */
  Map<String, Object> queryRoleMenuList(Integer roleId);

  /**
  　　* @Description: 查询用户所有权限按钮
  　　* @author: xueshiqi
  　　* @date: 2018/12/3
  　　*/
  Map<String,Object> queryUserButtonList(Integer userId);
}
