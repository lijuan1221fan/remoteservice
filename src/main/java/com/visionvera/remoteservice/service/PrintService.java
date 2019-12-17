package com.visionvera.remoteservice.service;

import java.util.Map;

/**
 * @author Administrator
 * @date 2018/8/16
 **/
public interface PrintService {

  /**
   * 打印表单接口
   *
   * @param ids 打印文件的id 逗号分割
   * @param businessId 业务id
   * @return
   */
  Map<String, Object> print(String ids, Integer businessId);

  /**
   * 根据业务id 套打签名
   *
   * @param businessId 业务id
   * @return
   */
  Map<String, Object> setPrint(Integer businessId);

  Map<String, Object> formPrint(Integer businessId);
}
