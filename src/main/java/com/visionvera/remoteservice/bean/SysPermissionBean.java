package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xueshiqi
 * @Description: 权限类
 * @date 2018/10/10
 */
public class SysPermissionBean implements Serializable {

  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = -7211829142467168094L;

  private Long permissionId;//权限id
  private Long parentId;//父级id
  private String label;//权限名称
  private String description;//权限描述
  private Integer state;//权限状态 1:有效 0:无效 -1:禁用
  private Integer type;//类型 0：目录   1：菜单   2：按钮'
  private Date createTime;//创建时间
  private Date updateTime;//更新时间
  private Integer defaultPermission;//是否是默认权限 0否，1是'
  private Integer version;//版本号
  private String url;//权限url
  private String perms;//授权(多个用逗号分隔，如：user:list,user:create)
  private List<SysPermissionBean> children; //子级权限
  private String icon;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getPerms() {
    return perms;
  }

  public void setPerms(String perms) {
    this.perms = perms;
  }

  public Long getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Long permissionId) {
    this.permissionId = permissionId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
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

  public List<SysPermissionBean> getChildren() {
    return children;
  }

  public void setChildren(List<SysPermissionBean> children) {
    this.children = children;
  }

  @Override
  public String toString() {
    return "SysPermissionBean{" +
        "permissionId=" + permissionId +
        ", parentId=" + parentId +
        ", label='" + label + '\'' +
        ", description='" + description + '\'' +
        ", state=" + state +
        ", type=" + type +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", defaultPermission=" + defaultPermission +
        ", version=" + version +
        ", url='" + url + '\'' +
        '}';
  }
}
