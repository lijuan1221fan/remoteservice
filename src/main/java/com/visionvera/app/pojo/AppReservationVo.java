package com.visionvera.app.pojo;

import com.visionvera.remoteservice.pojo.BaseVo;

/**
 * @ClassNameAppReservationVo
 *description TODO
 * @author ljfan
 * @date 2019/03/21
 *version
 */
public class AppReservationVo extends BaseVo {
  private String serviceName;

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
}
