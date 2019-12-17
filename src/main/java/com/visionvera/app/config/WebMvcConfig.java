package com.visionvera.app.config;

import com.visionvera.app.interceptor.AuthorizationInterceptor;
import com.visionvera.app.resolver.LoginAppUserResolver;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置
 *
 * @author EricShen
 * @date 2019-03-13
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthorizationInterceptor authorizationInterceptor;
    @Resource
    private LoginAppUserResolver loginAppUserResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/app/**");
    }
                                
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginAppUserResolver);
    }
    }
