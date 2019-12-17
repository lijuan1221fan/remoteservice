package com.visionvera.remoteservice.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomUtil {

  private static Logger logger = LoggerFactory.getLogger(CustomUtil.class);


  /***
   * 将json String 转为 java list
   * @param jsonString json字符串
   * @param jsonKey json key
   * @param clazz Java类型
   * @return
   */
  public static <T> List<T> stringToJavaList(String jsonString, String jsonKey, Class<T> clazz) {
    try {
      if (StringUtils.isBlank(jsonString) || StringUtils.isBlank(jsonKey) || null == clazz) {
        return new ArrayList<T>();
      }
      JSONObject jsonObj = JSONObject.parseObject(jsonString);
      JSONArray jsonArray = jsonObj.getJSONArray(jsonKey);
      if (jsonArray == null) {
        return new ArrayList<T>();
      }
      List<T> javaList = jsonArray.toJavaList(clazz);
      return javaList;
    } catch (Exception e) {
      logger.error("jsonString转javaList失败", e);
      e.printStackTrace();
      return new ArrayList<T>();
    }
  }

  /***
   * 将json类型中的string通过key获取其对应值
   * @param jsonString
   * @param key
   * @return
   */
  public static String getStringByJsonString(String jsonString, String key) {
    try {
      JSONObject jsonObject = JSONObject.parseObject(jsonString);
      return jsonObject.getString(key);
    } catch (Exception e) {
      logger.error("json类型中的string通过key获取其对应值失败", e);
      e.printStackTrace();
      return "";
    }
  }

  /***
   * 通过key获取json字符串中的json对象
   * @param jsonString
   * @param key
   * @return
   */
  public static JSONObject getJSONObjectByJsonString(String jsonString, String key) {
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    return jsonObject.getJSONObject(key);
  }

}
