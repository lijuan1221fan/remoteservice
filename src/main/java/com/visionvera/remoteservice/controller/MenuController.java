package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.service.SysMenuService;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueshiqi
 * @Description:
 * @date 2018/11/8
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

  private static Logger logger = LoggerFactory.getLogger(MenuController.class);

  @Resource
  private SysMenuService sysMenuService;

  @GetMapping(value = "/queryUserMenuList")
  public Map<String, Object> queryUserMenuList() {
    return sysMenuService.queryUserMenuList(ShiroUtils.getUserId());
  }

  @GetMapping(value = "/queryRoleMenuList")
  public Map<String, Object> queryRoleMenuList(Integer roleId) {
    return sysMenuService.queryRoleMenuList(roleId);
  }

  @GetMapping(value = "/queryUserButtonList")
  public Map<String, Object> queryUserButtonList() {
    return sysMenuService.queryUserButtonList(ShiroUtils.getUserId());
  }
}
