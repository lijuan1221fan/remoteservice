package com.visionvera.app.pojo;

import java.util.List;

/**
 * 功能描述:
 *
 * @ClassName: AppWindowParaVo
 * @Author: ljfan
 * @Date: 2019-04-28 15:06
 * @Version: V1.0
 */
public class AppWindowParaVo {
  private List<String> serviceKeys;
  private List<Integer> deptIds;

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
  }

  public List<Integer> getDeptIds() {
    return deptIds;
  }

  public void setDeptIds(List<Integer> deptIds) {
    this.deptIds = deptIds;
  }
}
