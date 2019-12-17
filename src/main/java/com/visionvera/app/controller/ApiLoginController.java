package com.visionvera.app.controller;

import com.visionvera.app.annotation.AppLogin;
import com.visionvera.app.annotation.AppLoginUser;
import com.visionvera.app.entity.AppUser;
import com.visionvera.app.group.AuthorizationGroup;
import com.visionvera.app.pojo.AppUserVo;
import com.visionvera.app.request.LoginRequest;
import com.visionvera.app.service.AppTokenService;
import com.visionvera.app.service.AppUserService;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 *
 * @author EricShen
 * @date 2019-03-13
 */
@RestController
@RequestMapping("/app")
public class ApiLoginController {

    @Resource
    private AppUserService userService;

    @Resource
    private AppTokenService appTokenService;


    @PostMapping("login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        //表单校验 预约
        ValidateUtil.validate(request);
        //用户登录
        Map<String, Object> map = userService.login(request);

        return ResultUtils.ok("登录成功", map);
    }

    @AppLogin
    @PostMapping("logout")
    public Map<String, Object> logout(HttpServletRequest request) {
      Object appUserId = request.getAttribute("appUserId");
      appTokenService.expireToken((Long) appUserId);
      return ResultUtils.ok("退出成功");
    }
    
    @AppLogin
    @PostMapping("authentication")
    public Map<String, Object> identityAuthentication(@AppLoginUser AppUser appUser,@RequestBody AppUserVo appUserVo) {
        //表单校验
        ValidateUtil.validate(appUserVo, AuthorizationGroup.class);
        //用户登录
       return  userService.authentication(appUser,appUserVo);
    }
}
