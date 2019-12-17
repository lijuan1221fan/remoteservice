package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.BindGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import javax.validation.constraints.*;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/17
 */
public class VcDevVo extends BaseVo {

  @NotBlank(groups = {BindGroup.class}, message = "中心Key不能为空")
  private String serviceKey;
  private Integer deptId;
  @NotNull(groups = {BindGroup.class, UpdateGroup.class}, message = "终端id不能为空")
  private String deviceId;
  private String associated;
  private Integer windowId;
  private Integer id;
  private String name;
  private Integer state;
  private Integer scuState;
  @NotBlank(groups = {UpdateGroup.class}, message = "IP不能为空")
  private String ip;
  @NotNull(groups = {UpdateGroup.class}, message = "类型不能为空")
  private Integer type;
  //统筹中心类型  1：协助  2：受理
  private Integer tcType;
  //是否是单摄像头  1：是  2：不是
  private Integer isSingleCamera;

  public Integer getScuState() {
    return scuState;
  }

  public void setScuState(Integer scuState) {
    this.scuState = scuState;
  }

  public Integer getIsSingleCamera() {
    return isSingleCamera;
  }

  public void setIsSingleCamera(Integer isSingleCamera) {
    this.isSingleCamera = isSingleCamera;
  }



  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getWindowId() {
    return windowId;
  }

  public void setWindowId(Integer windowId) {
    this.windowId = windowId;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getAssociated() {
    return associated;
  }

  public void setAssociated(String associated) {
    this.associated = associated;
  }

    public Integer getTcType() {
        return tcType;
    }

    public void setTcType(Integer tcType) {
        this.tcType = tcType;
    }
}
