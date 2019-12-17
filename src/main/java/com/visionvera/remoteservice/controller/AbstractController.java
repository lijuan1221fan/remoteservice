package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.util.ShiroUtils;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller公共组件
 *
 * Created by EricShen on 2017/08/22
 */
public abstract class AbstractController {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  protected HttpServletRequest request;

  protected SysUserBean getUser() {
    if (!ShiroUtils.isLogin()) {
      throw new MyException("当前用户未登录");
    }
    return ShiroUtils.getUserEntity();
  }

  protected Integer getUserId() {
    if (!ShiroUtils.isLogin()) {
      throw new MyException("当前用户未登录");
    }
    return getUser().getUserId();
  }

  public String getServerUrl() {
    return "http://" + request.getServerName() + ":" + request.getServerPort() + request
        .getContextPath() + "/";
  }
}
