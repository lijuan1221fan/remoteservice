package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.ApiDeptSelectGroup;
import com.visionvera.common.validator.group.TakeNumberGroup;
import javax.validation.constraints.NotNull;

/**
 * @ClassNameDeptListVo
 *description TODO
 * @author ljfan
 * @date 2019/03/21
 *version
 */
public class DeptListVo extends BaseVo {
  //服务中心serviceKey
  @NotNull(groups = {ApiDeptSelectGroup.class, TakeNumberGroup.class}, message = "村服务中心serviceKey不能为空")
  private String serviceKey;
  //部门状态
  private Integer state = 0;

  //业务详情id
  @NotNull(groups = {TakeNumberGroup.class},message = "业务详情ID不能为空")
  private Integer typeId;
  //安卓端业务模式
  private String androidBusinessType;

  public String getAndroidBusinessType() {
    return androidBusinessType;
  }

  public void setAndroidBusinessType(String androidBusinessType) {
    this.androidBusinessType = androidBusinessType;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
