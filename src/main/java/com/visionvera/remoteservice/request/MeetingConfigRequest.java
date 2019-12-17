package com.visionvera.remoteservice.request;

import com.visionvera.remoteservice.constant.RegularExpression;
import javax.validation.constraints.*;


/**
 * 会管配置请求体
 *
 * @author EricShen
 * @date 2018-12-20
 */
public class MeetingConfigRequest {

  /**
   * 会管url
   */
  @Pattern(regexp = RegularExpression.HTTP_IP_PORT, message = "请填写正确的会管地址")
  private String baseUrl;
  /**
   * 会管登录名
   */
  @NotBlank(message = "请输入会管登录名")
  private String loginName;
  /**
   * 会管登录密码
   */
  @NotBlank(message = "请输入会管登录密码")
  private String loginPwd;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getLoginPwd() {
    return loginPwd;
  }

  public void setLoginPwd(String loginPwd) {
    this.loginPwd = loginPwd;
  }
}
