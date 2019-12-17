package com.visionvera.remoteservice.bean;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author xueshiqi
 * @Description: TODO
 * @e 2018/12/20 0020
 */
public class ResetPassWord {

  @NotEmpty(message = "密码不能为空")
  @Length(min = 6, max = 16, message = "新密码长度只能为6~16位")
  @Pattern(regexp = "^\\w+$", message = "新密码只允许输入数字、字母、下划线")
  private String pwd;

  @NotNull(message = "用户id不能为空")
  private Integer userId;

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
}
