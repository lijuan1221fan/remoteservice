package com.visionvera.remoteservice.constant;

/**
 * Created by 13624 on 2018/7/31. 正则表达式
 */
public interface RegularExpression {

  public String PROTOCOL_IP_PORT = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?):\\d{1,5}";

  public String IP_PORT = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?):\\d{1,5}";

  /**
   * 匹配http://或https://开头, ip加端口号; 如:http://192.168.1.1:8080
   */
  public String HTTP_IP_PORT = "^(http:\\/\\/|https:\\/\\/)[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{2,5}$";
}
