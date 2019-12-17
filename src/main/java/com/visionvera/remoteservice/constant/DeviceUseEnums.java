package com.visionvera.remoteservice.constant;

/**
 * @author ljfan
 * @ClassNameUseEnums 使用状态 TODO
 * @date 2018/11/30 version
 * 终端设备使用状态 2:使用中 1:空闲，有效  0:删除 -1:禁用
 *
 */
public enum DeviceUseEnums {
  FREE(1,"空闲中"),
  WORKING(2,"使用中");
  DeviceUseEnums(Integer value,String name){
    this.name = name;
    this.value = value;
  }
  public String name;
  public Integer value;
  public String getName(){
    return name;
  }
  public void setName(String name){
    this.name = name;
  }
  public Integer getValue(){
    return value;
  }
}
