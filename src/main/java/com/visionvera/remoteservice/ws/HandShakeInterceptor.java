package com.visionvera.remoteservice.ws;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * 功能描述:
 *
 * @ClassName: HandShakeInterceptor
 * @Author: ljfan
 * @Date: 2019-03-11 11:10
 * @Version: V1.0
 */

public class HandShakeInterceptor extends HttpSessionHandshakeInterceptor {
  private static Logger logger = LoggerFactory.getLogger(HandShakeInterceptor.class);
  /*
   * 在WebSocket连接建立之前的操作，以鉴权为例
   */
  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    logger.info("Handle before webSocket connected. ");

    // 获取url传递的参数，通过attributes在Interceptor处理结束后传递给WebSocketHandler
    // WebSocketHandler可以通过WebSocketSession的getAttributes()方法获取参数
 /*   ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      HttpSession session = servletRequest.getServletRequest().getSession(false);*/
      return super.beforeHandshake(request, response, wsHandler, attributes);
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception ex) {
    super.afterHandshake(request, response, wsHandler, ex);
  }
}
