package com.visionvera.remoteservice.util;

import java.util.*;

/**
 * @ClassNameUrlUtil
 *description websocke安卓访问路径解析
 * @author ljfan
 * @date 2019/04/17
 *version
 */
public class UrlUtil {
  public static class UrlEntity {
    /**
     * 基础url
     */
    public String baseUrl;
    /**
     * url参数
     */
    public Map<String, String> params;

    /**
     * 业务类型
     */
    public String type;
    /**
     * redisKey
     */
    public  String queueKey;

    /**
     * 安卓序列号
     */
    public String deviceCode;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getQueueKey() {
      return queueKey;
    }

    public void setQueueKey(String queueKey) {
      this.queueKey = queueKey;
    }

    public String getDeviceCode() {
      return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
      this.deviceCode = deviceCode;
    }
  }

  /**
   * 解析url
   *
   * @param url
   * @return
   */
  public static UrlEntity parse(String url) {
    UrlEntity entity = new UrlEntity();
    if (url == null) {
      return entity;
    }
    url = url.trim();
    if (url.equals("")) {
      return entity;
    }
    String[] urlParts = url.split("\\?");
    entity.baseUrl = urlParts[0];
    //没有参数
    if (urlParts.length == 1) {
      return entity;
    }
    //有参数
    String[] params = urlParts[1].split("&");
    entity.params = new HashMap<>();
    for (String param : params) {
      String[] keyValue = param.split("=");
      if(keyValue.length < 2){
        return entity;
      }
      entity.params.put(keyValue[0], keyValue[1]);
    }

    return entity;
  }
}
