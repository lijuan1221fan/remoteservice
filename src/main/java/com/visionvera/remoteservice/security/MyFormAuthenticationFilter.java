package com.visionvera.remoteservice.security;

import com.alibaba.fastjson.JSON;
import com.visionvera.remoteservice.util.ResultUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;


/**
 * @author xueshiqi
 * @Description: TODO
 * @date 2018/10/31 0031
 */
public class  MyFormAuthenticationFilter extends FormAuthenticationFilter {

  private Logger log = LogManager.getLogger(getClass());

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    if (this.isLoginRequest(request, response)) {
      if (this.isLoginSubmission(request, response)) {
        if (log.isTraceEnabled()) {
          log.trace("Login submission detected.  Attempting to execute login.");
        }

        return this.executeLogin(request, response);
      } else {
        if (log.isTraceEnabled()) {
          log.trace("Login page view.");
        }
        return true;
      }
    } else {
      if (log.isTraceEnabled()) {
        log.trace(
            "Attempting to access a path which requires authentication.  Forwarding to the Authentication url ["
                + this.getLoginUrl() + "]");
      }
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = null;
      try {
        out = response.getWriter();
      } catch (IOException e) {
        e.printStackTrace();
      }
        Map<String, Object> error = ResultUtils.notLogin("401", "账号身份已过期或是已在其他地方登录，请重新登录。");
//            String serverResponse = "未登录,无法访问该地址";
      String s = JSON.toJSONString(error);
      out.println(s);
      out.flush();
      out.close();
      return false;
    }
  }
}
