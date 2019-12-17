package com.visionvera.remoteservice.bean;

/**
 * ClassName: NumberIteration
 *
 * @author quboka
 * @Description: 号码递增
 * @date 2018年3月22日
 */
public class NumberIteration {
  private Integer deptId;//部门Id
  private String serviceKey;//服务中心唯一key
  private Integer number;//号码 从0开始
  private String type;//业务类型  1000：公安远程服务 2000：社保远程服务
  private String numberPrefix;//前缀

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public String getNumberPrefix() {
    return numberPrefix;
  }

  public void setNumberPrefix(String numberPrefix) {
    this.numberPrefix = numberPrefix;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


}
