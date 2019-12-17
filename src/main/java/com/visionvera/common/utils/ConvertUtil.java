package com.visionvera.common.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 转换工具类
 * @author EricShen
 * @date 2018-12-05
 */
public class ConvertUtil {

  /**
   * obj list TO string list
   * @param objectList
   * @return
   */
  public static List<String> objList2StrList(List<Object> objectList) {
    return objectList.stream().map(object -> Objects.toString(object, null))
        .collect(Collectors.toList());
  }

}
