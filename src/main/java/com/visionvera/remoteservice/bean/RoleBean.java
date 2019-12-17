package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: Role
 *
 * @author quboka
 * @Description: 角色类
 * @date 2018年3月22日
 */
public class RoleBean implements Serializable {

  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = -7211829142467168094L;

  private Integer roleId;//角色id
  private String roleName;//角色名称
  private String description;//角色描述
  private Date createTime;//创建时间
  private Date updateTime;//更新时间
  private Integer status;//角色状态，1有效，0无效
  private Integer version;//版本号

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "Role [roleId=" + roleId + ", roleName=" + roleName
        + ", description=" + description + ", createTime=" + createTime
        + ", updateTime=" + updateTime + ", status=" + status
        + ", version=" + version + "]";
  }

}
