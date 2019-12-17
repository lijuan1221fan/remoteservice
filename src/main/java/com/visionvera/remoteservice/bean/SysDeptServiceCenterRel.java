package com.visionvera.remoteservice.bean;

import java.util.Date;

/**
 * @Auther: ljfan
 * @Date: 2018/11/13 16:03
 * @Description:
 */
public class SysDeptServiceCenterRel {

  /**
   * 主键id
   */
  private Integer id;
  /**
   * 部门id
   */
  private Integer deptId;
  /**
   * 服务中心id
   */
  private Integer serviceCenterId;
  /**
   * 创建时间
   */
  private Date createTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public Integer getServiceCenterId() {
    return serviceCenterId;
  }

  public void setServiceCenterId(Integer serviceCenterId) {
    this.serviceCenterId = serviceCenterId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
