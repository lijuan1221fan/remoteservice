package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.SysPermissionBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.SysRoleDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.service.SysMenuService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> 菜单管理 服务实现类 </p>
 *
 * @author EricShen
 * @since 2017-08-24
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

  @Resource
  private SysUserDao sysUserDao;

  @Resource
  private SysRoleDao sysRoleDao;

  @Override
  public Map<String, Object> queryUserMenuList(Integer userId) {
    List<Long> menuIdList;
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SUPER_ADMIN)) {
      // 根用户
      menuIdList = sysUserDao.getAllMenuId();
    } else {
      menuIdList = sysUserDao.getMenuIdList(userId);
    }

    List<SysPermissionBean> allMenuList = getAllMenuList(menuIdList);
    return ResultUtils.ok("获取用户权限列表成功", allMenuList);
  }

  @Override
  public Map<String, Object> queryRoleMenuList(Integer roleId) {
    List<Long> menuIdList = sysRoleDao.getRoleMenuIdList(roleId);
    List<SysPermissionBean> allMenuList = getAllMenuList(menuIdList);
    return ResultUtils.ok("获取角色权限列表成功", allMenuList);

  }

  @Override
  public Map<String, Object> queryUserButtonList(Integer userId) {
    List<SysPermissionBean> buttonList = new ArrayList<>();
    if(ShiroUtils.getUserEntity().getType().equals(CommonConstant.SUPER_ADMIN)){
      buttonList = sysRoleDao.queeyAllUserButtonList();
    }else{
      buttonList = sysRoleDao.queryUserButtonList(userId);
    }
    return ResultUtils.ok("获取用户权限按钮列表成功", buttonList);
  }

  /**
   * 获取所有菜单列表
   */
  private List<SysPermissionBean> getAllMenuList(List<Long> menuIdList) {
    //查询根菜单列表
    List<SysPermissionBean> menuList = getParentMenu(0L, menuIdList);
    //递归获取子菜单
    getMenuTreeList(menuList, menuIdList);
    return menuList;
  }

  /**
   * 查询根菜单列表
   */
  private List<SysPermissionBean> getParentMenu(Long parentId, List<Long> menuIdList) {
    // 父权限菜单
    List<SysPermissionBean> menuList = sysRoleDao.getParentSysPermissionList(parentId);
    if (menuIdList == null) {
      return menuList;
    }

    List<SysPermissionBean> userMenuList = new ArrayList<>();
    for (SysPermissionBean menu : menuList) {
      if (menuIdList.contains(menu.getPermissionId())) {
        userMenuList.add(menu);
      }
    }
    return userMenuList;
  }

  /**
   * 递归
   */
  private List<SysPermissionBean> getMenuTreeList(List<SysPermissionBean> menuList,
      List<Long> menuIdList) {
    List<SysPermissionBean> subMenuList = new ArrayList<>();

    for (SysPermissionBean entity : menuList) {
      if (entity.getType() == 0 || entity.getType() == 1) {
        //目录
        entity.setChildren(
            getMenuTreeList(getParentMenu(entity.getPermissionId(), menuIdList), menuIdList));
      }
      subMenuList.add(entity);
    }
    return subMenuList;
  }
}
