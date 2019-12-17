package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.remoteservice.bean.SysPermissionBean;
import com.visionvera.remoteservice.bean.SysRoleBean;
import com.visionvera.remoteservice.bean.SysRolePermissionBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.SysRoleDao;
import com.visionvera.remoteservice.service.SysMenuService;
import com.visionvera.remoteservice.service.SysRoleService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 　　* @Description: TODO 　　* @author: xueshiqi 　　* @date: 2018/10/10
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

  private static Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

  @Resource
  private SysRoleDao sysRoleDao;

  @Resource
  private SysMenuService sysMenuService;


  /**
   * 　　* @Description: 获取角色列表 　　* @author: xueshiqi 　　* @date: 2018/10/10
   */
  @Override
  public Map<String, Object> getSysRoleList(Map<String, Object> params) {
    List<SysRoleBean> list = new ArrayList<>();
    Integer pagaNum = (Integer) params.get("pageNum");
    Integer pageSize = (Integer) params.get("pageSize");
    String roleName = (String) params.get("roleName");
    Integer userId = ShiroUtils.getUserId();
    if (pagaNum != null && pageSize != null) {
      PageHelper.startPage(pagaNum, pageSize);
      list = sysRoleDao.getSysRoleList(roleName, userId);
    } else {
      list = sysRoleDao.getSysRoleList(roleName, userId);
    }

    if (list.size() == 0) {
      logger.info("获取角色列表成功");
      return ResultUtils.ok("获取角色列表成功", new PageInfo<SysRoleBean>(new ArrayList<SysRoleBean>()));
    }

    logger.info("获取角色列表成功");
    PageInfo<SysRoleBean> pageInfo = new PageInfo<SysRoleBean>(list);
    return ResultUtils.ok("获取角色列表成功", pageInfo);
  }

  @Override
  public Map<String, Object> getSysRoleInfo(Integer roleId) {
    // Integer roleId = (Integer) params.get("roleId");
    SysRoleBean sysRoleBean = sysRoleDao.getSysRoleInfo(roleId);
    if(sysRoleBean == null){
      return ResultUtils.ok("获取角色信息成功,该角色不存在");
    }
    Map<String, Object> stringObjectMap = sysMenuService.queryRoleMenuList(roleId);
    List<SysPermissionBean> allMenuList = (List<SysPermissionBean>) stringObjectMap.get("data");
    sysRoleBean.setMenuList(allMenuList);
    logger.info("获取角色信息成功");
    return ResultUtils.ok("获取角色信息成功", sysRoleBean);
  }

  @Override
  public Map<String, Object> editSysRoleInfo(SysRoleBean sysRoleBean) {
    SysRoleBean sysRole = sysRoleDao
        .getSysRole(sysRoleBean.getRoleName(), ShiroUtils.getUserId());
    if (sysRole != null &&
        !sysRole.getRoleId().equals(sysRoleBean.getRoleId())) {
      return ResultUtils.error("更改角色信息失败,角色名已存在.");
    }
    Integer count = sysRoleDao.editSysRoleInfo(sysRoleBean);

    //修改角色权限关系表
    List<Integer> roldList = new ArrayList<>();
    roldList.add(sysRoleBean.getRoleId());
    sysRoleDao.deleteRolePermissionRel(roldList);

    List<Integer> permissionIds = sysRoleBean.getPermissionIds();
    List<SysRolePermissionBean> list = new ArrayList<>();
    for (int permissionId : permissionIds) {
      SysRolePermissionBean sysRolePermissionBean = new SysRolePermissionBean();
      sysRolePermissionBean.setRoleId(sysRoleBean.getRoleId());
      sysRolePermissionBean.setPermissionId(permissionId);
      list.add(sysRolePermissionBean);
    }
    sysRoleDao.addSysUserPermission(list);
    if (count == 0) {
      logger.info("更改角色信息失败");
      return ResultUtils.error("更改角色信息失败");
    }
    logger.info("更改角色信息成功");
    return ResultUtils.ok("更改角色信息成功");
  }

  @Override
  public Map<String, Object> deleteSysRole(List<Integer> roleIds) {
    try {
      // List<Integer> roleIds = (List<Integer>) params.get("roleIds");
      // if(CollectionUtils.isEmpty(roleIds)){
      //   return com.visionvera.common.utils.ResultUtils.msg("角色id不能为空");
      // }
      //删除角色表
      sysRoleDao.deleteRoleById(roleIds);
      //删除角色用户关联表
      sysRoleDao.deleteRoleUserRel(roleIds);
      //删除角色权限关联表
      sysRoleDao.deleteRolePermissionRel(roleIds);
    } catch (Exception e) {
      logger.info("删除角色信息失败");
      return ResultUtils.error("删除角色信息失败");
    }
    logger.info("删除角色信息成功");
    return ResultUtils.ok("删除角色信息成功");
  }

  @Override
  public Map<String, Object> addSysRole(SysRoleBean sysRoleBean) {
    // 角色名称 备注 权限集合
    // 插入角色表
    SysRoleBean sysRole = sysRoleDao
        .getSysRole(sysRoleBean.getRoleName(), ShiroUtils.getUserId());
    if (sysRole != null) {
      return ResultUtils.error("新增角色信息失败,角色名已存在");
    }
    //        sysRoleBean.setType((Integer) params.get("type"));
    sysRoleBean.setCreater(ShiroUtils.getUserId());
    sysRoleDao.addSysRole(sysRoleBean);
    //插入角色权限关系表
    List<Integer> permissionIds = (List<Integer>) sysRoleBean.getPermissionIds();
    List<SysRolePermissionBean> list = new ArrayList<>();
    for (int permissionId : permissionIds) {
      SysRolePermissionBean sysRolePermissionBean = new SysRolePermissionBean();
      sysRolePermissionBean.setRoleId(sysRoleBean.getRoleId());
      sysRolePermissionBean.setPermissionId(permissionId);
      list.add(sysRolePermissionBean);
    }
    sysRoleDao.addSysUserPermission(list);
    return ResultUtils.ok("新增角色信息成功");
  }

  @Override
  public Map<String, Object> getUserMenuButton(String permissionId, Integer userId) {
    List<SysPermissionBean> buttonList = new ArrayList<>();
    if (userId.equals(CommonConstant.SUPER_ADMIN_ID)) {
      buttonList = sysRoleDao.getUserMenuButtonByPermissionId(permissionId);
    } else {
      buttonList = sysRoleDao.getUserMenuButton(permissionId, userId);
    }
    return ResultUtils.ok("获取用户菜单按钮成功", buttonList);
  }
}
