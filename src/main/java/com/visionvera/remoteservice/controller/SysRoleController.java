package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SysRoleBean;
import com.visionvera.remoteservice.service.SysRoleService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 　　* @Description: 角色controller 　　* @author: xueshiqi 　　* @date: 2018/10/10
 */
@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends AbstractController {

  private static Logger logger = LoggerFactory.getLogger(SysRoleController.class);

  @Resource
  private SysRoleService sysRoleService;

  /**
   * @return Map<String   ,   Object>
   * @Description: 获取角色列表
   * @author xueshiqi
   * @date 2018/10/10
   */
  @PostMapping(value = "/getSysRoleList")
  public Map<String, Object> getSysRoleList(@RequestBody Map<String, Object> params) {
    return sysRoleService.getSysRoleList(params);
  }

  /**
   * 　　* @Description: 获取角色信息 　　* @author: xueshiqi 　　* @date: 2018/10/11
   */
  @GetMapping(value = "/getSysRoleInfo")
  public Map<String, Object> getSysRoleInfo(@RequestParam Integer roleId) {
    AssertUtil.isNull(roleId,"参数缺失");
    return sysRoleService.getSysRoleInfo(roleId);
  }

  /**
   * 　　* @Description: 编辑角色信息 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  @SysLogAnno("编辑角色信息")
  @RequiresPermissions("role:edit")
  @PostMapping(value = "/editSysRoleInfo")
  public Map<String, Object> editSysRoleInfo(@RequestBody SysRoleBean sysRoleBean) {
    ValidateUtil.validate(sysRoleBean, UpdateGroup.class);
    return sysRoleService.editSysRoleInfo(sysRoleBean);
  }

  /**
   * 　　* @Description: 删除角色 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  @SysLogAnno("删除角色信息")
  @RequiresPermissions("role:delete")
  @PostMapping(value = "/deleteSysRole")
  public Map<String, Object> deleteSysRole(@RequestParam List<Integer> roleIds) {
    AssertUtil.isEmpty(roleIds,"参数缺失");
    return sysRoleService.deleteSysRole(roleIds);
  }

  /**
   * 　　* @Description: 获取菜单下按钮 　　* @author: xueshiqi 　　* @date: 2018/11/2
   */
  @GetMapping("/getUserMenuButton")
  public Map<String, Object> getUserMenuButton(@RequestParam String permissionId) {
    AssertUtil.isNull(permissionId,"参数缺失");
    return sysRoleService.getUserMenuButton(permissionId, getUserId());
  }

  /**
   * 　　* @Description: 添加角色 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  @SysLogAnno("添加角色信息")
  @RequiresPermissions("role:add")
  @PostMapping("/addSysRole")
  public Map<String, Object> addSysRole(@RequestBody SysRoleBean sysRoleBean) {
    // 角色名称 备注 权限集合
    ValidateUtil.validate(sysRoleBean,AddGroup.class);
    return sysRoleService.addSysRole(sysRoleBean);
  }
}
