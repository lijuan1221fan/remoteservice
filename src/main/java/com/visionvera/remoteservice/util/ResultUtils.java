package com.visionvera.remoteservice.util;

import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.ObjectUtils;

/**
 * 返回json
 *
 * @param <T>
 * @author zzq 2016-05-24
 */
public class ResultUtils {

  private static <T> Map<String, Object> getResult(boolean result, String msg) {
    return getResult(result, msg, null);
  }

  private static Map<String, Object> getResult(boolean result, String msg, Object object) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("result", result);
    resultMap.put("msg", msg);
    if (object != null) {
      if (object instanceof PageInfo) {
        resultMap.put("resultList", object);
      } else {
        resultMap.put("data", object);
      }
    }
    return resultMap;
  }
  private static Map<String, Object> getResult(boolean result, String msg, String code) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("result", result);
    resultMap.put("msg", msg);
    resultMap.put("code", code);
    return resultMap;
  }

  /**
   * 失败
   *
   * @param msg 消息内容
   * @return
   */
  public static Map<String, Object> error(String msg) {
    return getResult(false, msg);
  }

  /**
   * 失败
   *
   * @param msg 消息内容
   * @return
   */
  public static Map<String, Object> error() {
    return getResult(false, "失败");
  }

  /**
   * 成功
   *
   * @param msg 消息内容
   * @return
   */
  public static Map<String, Object> ok(String msg) {
    return ok(msg, null);
  }

  /**
   * 成功
   *
   * @param msg 消息内容
   * @return
   */
  public static Map<String, Object> ok() {
    return ok("成功");
  }

  /**
   * 成功
   *
   * @param msg 消息内容
   * @param object 返回值对象
   * @return
   */
  public static Map<String, Object> ok(String msg, Object object) {
    return getResult(true, msg, object);
  }

  /**
   * 返回值对象为空 true 反之 ok   *
   *
   * @param msg 消息内容
   * @param object 返回值对象
   * @return
   */
  public static Map<String, Object> check(String msg, Object object) {
    if (ObjectUtils.isEmpty(object)) {
      return ok("查询成功，但结果为空");
    }
    return ok(msg, object);
  }

   /**
   * 返回值对象为空 true 反之 ok   *
   *
   * @param msg 消息内容
   * @param object 返回值对象
   * @return
   */
  public static Map<String, Object> check(Object object) {
    if (ObjectUtils.isEmpty(object)) {
      return ok("查询成功，但结果为空");
    }
    return ok("查询成功", object);
  }

  /**
   * 未登录
   *
   * @param msg 消息内容
   * @param object 返回值对象
   * @return
   */
  public static Map<String, Object> notLogin(String code, String msg) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("code", code);
    resultMap.put("msg", msg);
    return resultMap;
  }
    /**
     * 多用户踢出
     * @param msg 消息内容
     * @param object 返回值对象
     * @return
     */
    public static Map<String, String> kickout(String code, String msg){
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        return resultMap;
    }
    /**
     * 异常返回结果
     * @param msg 消息内容
     * @param object 返回值对象
     * @return
     */
    public static Map<String, Object> exeptionOut(boolean result,String code, String msg){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        return resultMap;
    }

  /**
   * 业务员在线办理业务人数限定
   *
   * @param msg 消息内容
   * @param object 返回值对象
   * @return
   */
  public static Map<String, Object> LimitNumber(String code, String msg) {
    return getResult(false, msg,code);
  }
}
