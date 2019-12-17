/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.visionvera.app.interceptor;


import com.visionvera.app.annotation.AppLogin;
import com.visionvera.app.entity.AppToken;
import com.visionvera.app.service.AppTokenService;
import com.visionvera.remoteservice.exception.MyException;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 权限(Token)验证
 *
 * @author EricShen
 * @date 2019-03-13
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private AppTokenService appTokenService;

    public static final String USER_KEY = "appUserId";
    /**
     * 12小时后过期
     *
     */
    @Value("${token.keepTime}")
    private Integer EXPIRE;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AppLogin annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AppLogin.class);
        }else{
            return true;
        }

        if(annotation == null){
            return true;
        }

        //从header中获取token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }

        //token为空
        if(StringUtils.isBlank(token)){
            throw new MyException("token不能为空");
        }

        //查询token信息
        AppToken appToken = appTokenService.queryByToken(token);
        if(appToken == null || appToken.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new MyException("token失效，请重新登录");
        }
        //过期时间
        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE * 3600 * 1000);
        appToken.setExpireTime(expireTime);
        appTokenService.updateExpire(appToken);
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, appToken.getAppUserId());
        return true;
    }
}
