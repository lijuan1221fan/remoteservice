package com.visionvera.remoteservice.util;

import java.util.UUID;

/***
 *
 * ClassName: UUIDGenerator 
 * @Description: 用于生产UUID
 * @author wangrz
 * @date 2016年8月12日
 */
public class UUIDGenerator {

  public static String getJavaUuid() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().replaceAll("\\-", "");
  }

  public static void main(String[] args) {
    System.out.println(getJavaUuid());
  }
}