package com.visionvera.app.pojo;

import com.visionvera.app.group.AddUserGroup;
import java.util.Date;
import javax.validation.constraints.*;
/**
 * @ClassNameAppTokenVo
 *description TODO
 * @author ljfan
 * @date 2019/03/19
 *version
 */
public class AppTokenVo {
  /**
   * 用户ID
   */
  private Long appUserId;
  /**
   * token
   */
  @NotEmpty(groups = {AddUserGroup.class}, message = "token不能为空")
  private String token;
  /**
   * 过期时间
   */
  private Date expireTime;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 更新时间
   */
  private Date updateTime;
  /**
   * 登录用户名
   */
  private String loginName;

  public Long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(Long appUserId) {
    this.appUserId = appUserId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Date expireTime) {
    this.expireTime = expireTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }
}
