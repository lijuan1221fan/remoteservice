package com.visionvera.remoteservice.exception;

import com.visionvera.remoteservice.util.ResultUtils;
import java.util.Map;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 异常处理器
 *
 * Created by EricShen on 2017/08/22
 */
@RestControllerAdvice
public class MyExceptionHandler {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 自定义异常
   */
  @ExceptionHandler(MyException.class)
  public Map<String, Object> handleRRException(MyException e) {
    return ResultUtils.error(e.getMsg());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Map<String, Object> handleRRException(HttpRequestMethodNotSupportedException e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("不支持的请求方法");
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public Map<String, Object> handleRRException(HttpMediaTypeNotSupportedException e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("不支持的Content-Type");
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public Map<String, Object> handleRRException(MissingServletRequestParameterException e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("缺少请求参数");
  }

  @ExceptionHandler(DuplicateKeyException.class)
  public Map<String, Object> handleDuplicateKeyException(DuplicateKeyException e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("数据库中已存在该记录");
  }

  @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
  public Map<String, Object> handleAuthorizationException(AuthorizationException e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("没有权限，请联系管理员授权");
  }

  @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
  public Map<String, Object> handleUnauthenticatedException(AuthenticationException e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("登录认证过期");
  }

  @ExceptionHandler(Exception.class)
  public Map<String, Object> handleException(Exception e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("系统未知异常");
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public Map<String, Object> handleMaxUploadSizeExceededException(Exception e) {
    logger.error(e.getMessage(), e);
    return ResultUtils.error("上传文件过大，请联系管理员修改上传限制");
  }
}
