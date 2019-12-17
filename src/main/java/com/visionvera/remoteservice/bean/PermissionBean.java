package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: Permiss
 *
 * @author quboka
 * @Description: 权限类
 * @date 2018年3月22日
 */
public class PermissionBean implements Serializable {

  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = 1801215269861151732L;

  private Integer permissionId;//权限id
  private String permissionName;//权限名称
  private String description;//权限功能详细描述
  private Integer status;//权限状态，1有效，0无效
  private Date createTime;//创建时间
  private Date updateTime;//更新时间
  private Integer defaultPermission;//是否为默认权限，0否，1是
  private Integer version;//版本号
  private String url;//权限url地址，可以为空

  public Integer getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Integer permissionId) {
    this.permissionId = permissionId;
  }

  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  public Integer getDefaultPermission() {
    return defaultPermission;
  }

  public void setDefaultPermission(Integer defaultPermission) {
    this.defaultPermission = defaultPermission;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Permission [permissionId=" + permissionId + ", permissionName="
        + permissionName + ", description=" + description + ", status="
        + status + ", createTime=" + createTime + ", updateTime="
        + updateTime + ", defaultPermission=" + defaultPermission
        + ", version=" + version + ", url=" + url + "]";
  }

}
