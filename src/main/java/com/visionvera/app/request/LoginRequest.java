package com.visionvera.app.request;
import javax.validation.constraints.*;
/**
 * 登录表单
 *
 * @author EricShen
 * @date 2019-03-13
 */
public class LoginRequest {

    @NotBlank(message = "手机号不能为空")
    private String loginName;

    @NotBlank(message = "密码不能为空")
    private String password;

    public String getLoginName() {
        return loginName.trim();
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password.trim();
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
