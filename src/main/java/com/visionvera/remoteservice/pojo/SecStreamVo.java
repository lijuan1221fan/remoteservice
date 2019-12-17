package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.QueryGroup;
import javax.validation.constraints.NotNull;

/**
 * @author ljfan
 * @ClassNameSecStreamVo 远程分享 TODO
 * @date 2018/12/26 version
 */
public class SecStreamVo {

  @NotNull(groups = {QueryGroup.class}, message = "业务ID不能为空")
  private Integer businessId;
  @NotNull(groups = {QueryGroup.class}, message = "请判断是开启还是关闭")
  private Integer type;

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }
}
