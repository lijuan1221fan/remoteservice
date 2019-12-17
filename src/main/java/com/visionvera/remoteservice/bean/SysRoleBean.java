package com.visionvera.remoteservice.bean;

import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.remoteservice.pojo.BaseVo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * ClassName: Role
 *
 * @author quboka
 * @Description: 角色类
 * @date 2018年3月22日
 */
public class SysRoleBean extends BaseVo implements Serializable {

  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = -7211829142467168094L;

  @NotNull(groups = UpdateGroup.class, message = "角色ID不能为空")
  private Integer roleId;//角色id
  @Length(groups = {UpdateGroup.class,
      AddGroup.class}, min = 1, max = 16, message = "角色名称长度只能为1~16位")
  @NotBlank(groups = {UpdateGroup.class, AddGroup.class}, message = "角色名称不能为空")
  private String roleName;//角色名称
  @Length(min = 1, max = 16, message = "角色描述长度只能为1~16位")
  private String description;//角色描述

  private Date createTime;//创建时间

  private Date updateTime;//更新时间

  private Integer state;//角色状态 1:有效 0:无效 -1:禁用

  private Integer version;//版本号

  private Integer type;//类型 0管理员  1业务员

  private String roleSign;//角色标识

  private Integer creater;//创建人

  @NotEmpty(groups = {UpdateGroup.class, AddGroup.class}, message = "角色权限不能为空")
  private List<Integer> permissionIds; //权限id集合

  private List<SysPermissionBean> menuList;

  public List<Integer> getPermissionIds() {
    return permissionIds;
  }

  public void setPermissionIds(List<Integer> permissionIds) {
    this.permissionIds = permissionIds;
  }

  public Integer getCreater() {
    return creater;
  }

  public void setCreater(Integer creater) {
    this.creater = creater;
  }

  public List<SysPermissionBean> getMenuList() {
    return menuList;
  }

  public void setMenuList(List<SysPermissionBean> menuList) {
    this.menuList = menuList;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getRoleSign() {
    return roleSign;
  }

  public void setRoleSign(String roleSign) {
    this.roleSign = roleSign;
  }

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

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
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
        + ", updateTime=" + updateTime + ", state=" + state
        + ", version=" + version + "]";
  }

}
