package com.visionvera.common.validator.util;

import com.visionvera.remoteservice.exception.MyException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * 数据校验
 *
 * Created by EricShen on 2017/09/13
 * @author EricShen
 */
public abstract class AssertUtil {

  public static void isBlank(String str, String msg) {
    if (StringUtils.isBlank(str)) {
      throw new MyException(msg);
    }
  }

  public static void isNull(Object o, String msg) {
    if (Objects.isNull(o)) {
      throw new MyException(msg);
    }
  }

  public static void isEmpty(Object o, String msg) {
    if (ObjectUtils.isEmpty(o)) {
      throw new MyException(msg);
    }
  }

}