package com.visionvera.remoteservice.request;

import com.visionvera.remoteservice.pojo.BaseVo;
import java.io.Serializable;
import java.util.List;

/**
 * @author EricShen
 * @date 2018-11-29
 */
public class GetDeviceListRequest extends BaseVo implements Serializable {

  /**
   * 设备私有名称
   */
  private String privateName;

  /**
   * 类型  1:pad 2:一体机 3:打印机
   */
  private String type;

  /**
   * @des 所属中心
   */
  private String serviceKey;
  private List<String> serviceKeys;

  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  private List<String> ids;
  public String getPrivateName() {
    return privateName;
  }

  public void setPrivateName(String privateName) {
    this.privateName = privateName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
  }
}
