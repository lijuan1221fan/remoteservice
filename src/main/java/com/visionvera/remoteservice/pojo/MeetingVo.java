package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.DynDelDeviceGroup;
import com.visionvera.common.validator.group.StartGroup;
import javax.validation.constraints.NotNull;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/19
 */
public class MeetingVo {

  @NotNull(groups = {DynDelDeviceGroup.class, StartGroup.class}, message = "业务id不能为空")
  private Integer businessId;
  @NotNull(groups = {StartGroup.class}, message = "终端号不能为空")
  private String number;
  private Integer type;
  @NotNull(groups = {DynDelDeviceGroup.class}, message = "终端号不能为空")
  private String dynDeviceNumber;

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getDynDeviceNumber() {
    return dynDeviceNumber;
  }
  public void setDynDeviceNumber(String dynDeviceNumber) {
    this.dynDeviceNumber = dynDeviceNumber;
  }
}
