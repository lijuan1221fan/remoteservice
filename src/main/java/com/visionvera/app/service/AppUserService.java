package com.visionvera.app.service;

import com.visionvera.app.entity.AppUser;
import com.visionvera.app.pojo.AppUserVo;
import com.visionvera.app.request.LoginRequest;
import java.util.Map;

/**
 * @author EricShen
 * @date 2019-03-13
 */
public interface AppUserService {

    /**
     * 登录
     * @param request
     * @return
     */
    Map<String, Object> login(LoginRequest request);

    /**
     * 添加用户信息
     */
    Map<String, Object> addUser(AppUserVo appUserVo);

    /**
     * 根据用户名查询用户
     */
    AppUser getAppUserByLoginName(String loginName);

    /**
     * 修改用户信息
     *
     * @param appUserVo
     * @return
     */
    Map<String, Object> updateUser(AppUserVo appUserVo);

    /**
     * 删除用户信息
     *
     * @param appUserId
     * @return
     */
    Map<String, Object> delUser(Long appUserId);

    /**
     * 展示用户信息
     *
     * @param appUserVo
     * @return
     */
    Map<String, Object> getUsers(AppUserVo appUserVo);

    /**
     * 个人信息展示
     *
     * @param appUser
     * @return
     */
    Map<String, Object> getInfo(AppUser appUser);

    /**
     * 修改用户密码
     *
     * @param appUserVo
     * @return
     */
    Map<String, Object> updatePassword(AppUserVo appUserVo);

  Map<String, Object> showInfo(Long appUserId);
    AppUser getAppUserByAppUserId(Long appUserId);
  /**
   * app 信息认证
   */
  Map<String, Object> authentication(AppUser appUser,AppUserVo appUserVo);
}
