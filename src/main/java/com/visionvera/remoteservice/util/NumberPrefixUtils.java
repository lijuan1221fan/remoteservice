package com.visionvera.remoteservice.util;

/**
 * ClassName: NumberPrefixUtils
 *
 * @author quboka
 * @Description: 号码前缀工具类
 * @date 2018年3月31日
 */
public class NumberPrefixUtils {

  /**
   * @param type
   * @return String
   * @Description: 根据业务类型type查询前缀
   * @author quboka
   * @date 2018年3月31日
   */
  public static String getNumberPrefix(String type) {
    switch (type) {
      case "1000":
        type = "A";
        break;
      case "2000":
        type = "B";
        break;
      default:
        break;
    }
    return type;
  }

  /**
   * @param type
   * @return String
   * @Description: 根据业务类型type查询名字
   * @author quboka
   * @date 2018年3月31日
   */
  public static String getTypeName(String type) {
    switch (type) {
      case "1000":
        type = "公安类";
        break;
      case "2000":
        type = "社保类";
        break;
      default:
        break;
    }
    return type;
  }
}
