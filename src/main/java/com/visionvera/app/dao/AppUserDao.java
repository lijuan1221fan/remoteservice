package com.visionvera.app.dao;

import com.visionvera.app.entity.AppUser;
import com.visionvera.app.pojo.AppUserVo;
import java.util.List;

/**
 * @author EricShen
 * @date 2019-03-13
 */
public interface AppUserDao {

  //添加用户
  int addUser(AppUserVo appUserVo);

  //根据用户名查询用户
  AppUser getAppUserByName(String loginName);

  //根据用户id查询用户
  AppUser getAppUserByAppUserId(Long appUserId);

  //修改用户信息
  int updateUser(AppUserVo appUserVo);
  //修改用户信息
  int updateAppUser(AppUser appUser);

  //根据用户身份证号查询用户
  AppUser getAppUserByCardNum(String cardNum);

  //删除用户信息
  int delUser(Long appUserId);

  //展示用户列表
  List<AppUser> getUsers(AppUserVo appUserVo);

  //展示用户信息
  AppUser getInfo(String loginName);

  //修改密码
  int updatePassword(AppUserVo appUserVo);

  AppUser showInfo(Long appUserId);
}
