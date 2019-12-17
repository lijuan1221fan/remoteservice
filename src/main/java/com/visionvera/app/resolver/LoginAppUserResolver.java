package com.visionvera.app.resolver;

import com.visionvera.app.annotation.AppLoginUser;
import com.visionvera.app.dao.AppUserDao;
import com.visionvera.app.entity.AppUser;
import com.visionvera.app.interceptor.AuthorizationInterceptor;
import com.visionvera.app.service.AppUserService;
import javax.annotation.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@AppLoginUser注解的方法参数，注入当前登录用户
 *
 * @author EricShen
 * @date 2019-03-13
 */
@Component
public class LoginAppUserResolver implements HandlerMethodArgumentResolver {

    @Resource
    private AppUserDao appUserDao;
    @Resource
    private AppUserService appUserService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AppUser.class) && parameter
            .hasParameterAnnotation(AppLoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
        NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID
      Object object = request
          .getAttribute(AuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }
        Long appUserId = (Long) object;
        return appUserService.getAppUserByAppUserId(appUserId);
    }
}
