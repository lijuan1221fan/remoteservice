package com.visionvera.remoteservice.bean;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author xueshiqi
 * @Description: TODO
 * @date 2018/12/20
 */
public class UpdatePswd {

  @NotEmpty(message = "旧密码不能为空")
  private String oldPwd;
  @NotEmpty(message = "新密码不能为空")
  @Length(min = 6, max = 16, message = "新密码长度只能为6~16位")
  @Pattern(regexp = "^\\w+$", message = "新密码只允许输入数字、字母、下划线")
  private String newPwd;

  public String getOldPwd() {
    return oldPwd;
  }

  public void setOldPwd(String oldPwd) {
    this.oldPwd = oldPwd;
  }

  public String getNewPwd() {
    return newPwd;
  }

  public void setNewPwd(String newPwd) {
    this.newPwd = newPwd;
  }
}
