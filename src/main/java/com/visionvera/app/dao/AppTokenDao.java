package com.visionvera.app.dao;

import com.visionvera.app.entity.AppToken;

/**
 * @author EricShen
 * @date 2019-03-13
 */
public interface AppTokenDao {
  //添加用户Token信息
  int addToken(AppToken appToken);

  //根据token查询token 信息
  AppToken getTokenInfoByToken(String token);

  //修改token信息
  int updateToken(AppToken appToken);

  //删除token信息
  int delToken(Long appUserId);

  //根据appUserId取得token信息
  AppToken getTokenInfoByAppUserId(Long appUserId);
  void deleteTokenByAppUserId(Long appUserId);
}
