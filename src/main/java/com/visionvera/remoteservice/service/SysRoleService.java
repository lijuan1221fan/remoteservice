package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.SysRoleBean;
import java.util.List;
import java.util.Map;

/**
 * 　　* @Description: 角色service 　　* @author: xueshiqi 　　* @date: 2018/10/10
 */
public interface SysRoleService {

  /**
   * 　　* @Description: 获取角色列表 　　* @author: xueshiqi 　　* @date: 2018/10/10
   */
  Map<String, Object> getSysRoleList(Map<String, Object> params);

  /**
   * 　　* @Description: 获取角色信息 　　* @author: xueshiqi 　　* @date: 2018/10/11
   */
  Map<String, Object> getSysRoleInfo(Integer roleId);

  /**
   * 　　* @Description: 编辑角色信息 　　* @author: xueshiqi 　　* @date: 2018/10/11
   */
  Map<String, Object> editSysRoleInfo(SysRoleBean sysRoleBean);

  /**
   * 　　* @Description: 删除角色错误 　　* @author: xueshiqi 　　* @date: 2018/10/11
   */
  Map<String, Object> deleteSysRole(List<Integer> roleIds);

  /**
   * 　　* @Description: 添加角色 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  Map<String, Object> addSysRole(SysRoleBean sysRoleBean);

  /**
   * 　　* @Description: 获取菜单下按钮 　　* @author: xueshiqi 　　* @date: 2018/11/2
   */
  Map<String, Object> getUserMenuButton(String permissionId, Integer userId);
}