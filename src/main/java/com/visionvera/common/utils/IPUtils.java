package com.visionvera.common.utils;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP地址获取
 *
 * @Author jlm
 */
public class IPUtils {

  private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

  /**
   * 获取IP地址
   *
   * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
   * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
   */
  public static String getIpAddr(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        logger.info("IP地址：" + ip.substring(0, index));
        return ip.substring(0, index);
      } else {
        logger.info("IP地址：" + ip);
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      logger.info("IP地址：" + ip);
      return ip;
    }
    logger.info("IP地址：" + request.getRemoteAddr());
    return request.getRemoteAddr();
  }
}
