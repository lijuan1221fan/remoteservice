package com.visionvera.remoteservice.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 加密工具类
 *
 * @author EricShen
 * @date 2017/6/29
 */
public class EncryptionUtil {

  public static final String BASE_SALT = "1qazxsw2";

  public static int HASH_ITERATIONS = 1024;

  public static String ALGORITH_NAME = "MD5";

  /**
   * 使用md5生成加密后的密码 MD5密码
   */
  public static String encrypt(String password) {
    return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(BASE_SALT),
        HASH_ITERATIONS).toHex();
  }

  /**
   * 使用md5生成加密后的密码 MD5盐+密码
   */
  public static String encrypt(String password, String salt) {
    return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(salt),
        HASH_ITERATIONS).toHex();
  }

  /**
   * jlm  x86 MD5生成方法
   *
   * @param str
   * @return
   */
  public static String getMD5Str(String str) throws Exception {
    try {
      // 生成一个MD5加密计算摘要
      MessageDigest md = MessageDigest.getInstance("MD5");
      // 计算md5函数
      md.update(str.getBytes());
      // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
      // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
      return new BigInteger(1, md.digest()).toString(16);
    } catch (Exception e) {
      throw new Exception("MD5加密出现错误，" + e.toString());
    }
  }

}
