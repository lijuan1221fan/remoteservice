package com.visionvera.remoteservice.ws;

import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 功能描述:
 *
 * @ClassName: WebSocketConfig
 * @Author: ljfan
 * @Date: 2019-03-11 11:21
 * @Version: V1.0
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebMvcConfigurer {

  @Resource
  private AndroidWebSocketHandler androidWebSocketHandler;
  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
  @Resource
  private CallDeviceWebSocketHandler callDeviceWebSocketHandler;
  @Resource
  private H5WebSocketHandler h5WebSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(callDeviceWebSocketHandler, "/callDeviceServer")
         //.addHandler(androidWebSocketHandler, "/androidSocketServer")
        .addHandler(h5WebSocketHandler, "/h5SocketServer")
        .addHandler(embeddedServerWebSocketHandler, "/embeddedServer").setAllowedOrigins("*");
  }
}
