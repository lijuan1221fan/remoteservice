package com.visionvera.remoteservice.constant;

import com.visionvera.remoteservice.util.EncryptionUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jlm
 * @ClassName: x86 入参封装
 * @Description:
 * @date 2018/12/24
 */
public class WebConstant {

  public static Map<String, Object> getWebConstant(Integer deviceType) throws Exception {
    Long timestamp = System.currentTimeMillis() / 1000L;
    String text = "device_type=" + deviceType + "&timestamp=" + timestamp + "&secret_key=ABC123";
    String md5 = EncryptionUtil.getMD5Str(text).toUpperCase();
    Map<String, Object> map = new HashMap<>();
    map.put("device_type", deviceType);
    map.put("timestamp", timestamp);
    map.put("md5", md5);
    return map;
  }

}
