package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.ApiDeptSelectGroup;
import com.visionvera.common.validator.group.TakeNumberGroup;
import javax.validation.constraints.NotNull;

/**
 * @author hakimjun
 * @Description: TODO
 * @date 2019/8/22
 */
public class NewDeptListVo {
  //服务中心serviceKey
  @NotNull(groups = {ApiDeptSelectGroup.class, TakeNumberGroup.class}, message = "村服务中心serviceKey不能为空")
  private String serviceKey;
  public String getServiceKey() {
    return serviceKey;
  }
  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }


  //业务详情id
  @NotNull(groups = {TakeNumberGroup.class},message = "业务详情ID不能为空")
  private Integer typeId;
  public Integer getTypeId() {
    return typeId;
  }
  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  // public String getAndroidBusinessType() {
  //   return androidBusinessType;
  // }
  //
  // public void setAndroidBusinessType(String androidBusinessType) {
  //   this.androidBusinessType = androidBusinessType;
  // }
  //部门状态
  // private Integer state = 0;
  // public Integer getState() {
  //   return state;
  // }
  // public void setState(Integer state) {
  //   this.state = state;
  // }

  //取号者姓名
  private String numberName;
  public String getNumberName() {
    return numberName;
  }
  public void setNumberName(String numberName) {
    this.numberName = numberName;
  }
  // @NotNull(groups = {TakeNumberGroup.class}, message = "身份证号不能为空")
  //取号者身份证号
  private String idCardNumber;
  public String getIdCardNumber() {
    return idCardNumber;
  }
  public void setIdCardNumber(String idCardNumber) {
    this.idCardNumber = idCardNumber;
  }
}
