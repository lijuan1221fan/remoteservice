package com.visionvera.app.controller;

import com.visionvera.app.annotation.AppLogin;
import com.visionvera.app.annotation.AppLoginUser;
import com.visionvera.app.entity.AppUser;
import com.visionvera.app.group.AddUserGroup;
import com.visionvera.app.group.UpdatePasswordGroup;
import com.visionvera.app.pojo.AppUserVo;
import com.visionvera.app.service.AppUserService;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jlm
 * @ClassName:
 * @Description: app登录用户Web端接口
 * @date 2019/3/14
 */
@RestController
@RequestMapping("/app")
public class AppUserController {

  @Resource
  private AppUserService appUserService;

  /**
   * 新增用户
   *
   * @param appUserVo
   * @return
   */
  @PostMapping("addUser")
  public Map<String, Object> addUser(@RequestBody AppUserVo appUserVo) {
    ValidateUtil.validate(appUserVo, AddUserGroup.class);
    return appUserService.addUser(appUserVo);
  }

  /**
   * 修改用户信息
   * @param appUserVo
   * @return
   */
  @PostMapping("updateUser")
  public Map<String, Object> updateUser(@RequestBody AppUserVo appUserVo) {
    ValidateUtil.validate(appUserVo, UpdateGroup.class);
    return appUserService.updateUser(appUserVo);
  }

  /**
   * 删除用户信息
   * @param appUserId
   * @return
   */
  @GetMapping("delUser")
  public Map<String, Object> delUser(Long appUserId) {
    ValidateUtil.validate(appUserId);
    return appUserService.delUser(appUserId);
  }

  /**
   * 获取用户信息列表
   * @param appUserVo
   * @return
   */
  @PostMapping("getUsers")
  public Map<String, Object> getUserList(@RequestBody AppUserVo appUserVo) {
    return appUserService.getUsers(appUserVo);
  }

  /**
   * 用户信息展示
   *
   * @param user
   * @return
   */
  @AppLogin
  @GetMapping("getInfo")
  public Map<String, Object> getInfo(@AppLoginUser AppUser user) {
    return appUserService.getInfo(user);
  }

  /**
   * 修改用户密码
   *
   * @param appUserVo
   * @return
   */
  @PostMapping("updatePassword")
  public Map<String, Object> updatePassword(@RequestBody AppUserVo appUserVo) {
    ValidateUtil.validate(appUserVo, UpdatePasswordGroup.class);
    return appUserService.updatePassword(appUserVo);
  }

  /**
   * 编辑用户时信息回显
   *
   * @param appUserId
   * @return
   */
  @GetMapping("showInfo")
  public Map<String, Object> showInfo(Long appUserId) {
    ValidateUtil.validate(appUserId);
    return appUserService.showInfo(appUserId);
  }
}
