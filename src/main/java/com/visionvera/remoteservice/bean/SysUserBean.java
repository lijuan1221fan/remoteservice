package com.visionvera.remoteservice.bean;

import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.LockGroup;
import com.visionvera.common.validator.group.LoginGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.pojo.BaseVo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.constraints.Pattern;

/**
 * @Description: TODO
 * @author: xueshiqi
 * @date: 2018/10/12
 */
public class SysUserBean extends BaseVo implements Serializable {

  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = 9136121254905356626L;

  //用户id
  @NotNull(groups = {UpdateGroup.class, LockGroup.class}, message = "用户id不能为空")
  private Integer userId;
  //用户名或登录名
  @Pattern(groups = {LoginGroup.class, AddGroup.class,
      UpdateGroup.class}, regexp = CommonConstant.LOGINNAME_REGX, message = "用户名格式不正确")
  @NotBlank(groups = {LoginGroup.class, AddGroup.class, UpdateGroup.class}, message = "用户名不能为空")
  private String loginName;
  //真实姓名
  @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "真实姓名不能为空")
  @Pattern(groups = {AddGroup.class,
      UpdateGroup.class}, regexp = CommonConstant.USERNAME_REGX, message = "真实姓名格式不正确")
  private String userName;
  //用户密码
  @NotBlank(groups = {LoginGroup.class, AddGroup.class}, message = "密码不能为空")
  @Pattern(groups = {LoginGroup.class,
      AddGroup.class}, regexp = CommonConstant.PASSWORD_REGX, message = "密码只允许输入6~16位数字、字母、下划线")
  private String password;
  //用户状态 2:在线 1:离线，有效 0:无效 -1：禁用
  private String state;
  //用户的工作状态，0：空闲中 1：等待中 2：处理中
  private String workState;
  //手机号码
  @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "手机号码不能为空")
  @Pattern(groups = {AddGroup.class,
      UpdateGroup.class}, regexp = CommonConstant.MOBILEPHONE_REGX, message = "手机号码格式不正确")
  private String mobilePhone;
  //电子邮箱
  @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "邮箱不能为空")
  @Pattern(groups = {AddGroup.class,
      UpdateGroup.class}, regexp = CommonConstant.EMAIL_REGX, message = "邮箱格式不正确")
  private String email;
  //注册时间
  private Date createTime;
  //更新时间
  private Date updatetime;
  //最后登录时间
  private Date lastLoginTime;
  //版本号
  private Integer version;
  //角色信息
  private SysRoleBean role;
  //权限集合
  private List<SysPermissionBean> permissionList;
  //对应终端
  private List<TerminalBean> terminalList;
  //归属
  private String affiliation;
  //服务中心唯一key
  @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "归属中心不能为空")
  private String serviceKey;
  private List<String> serviceKeys;
  //部门id
  @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "归属部门不能为空")
  private Integer deptId;
  //部门名称
  private String deptName;
  @NotBlank(groups = {AddGroup.class}, message = "用户类型不能为空")
  private String type; //类型：0超管，1统筹中心管理员，2，审核中心管理员，3业务员
  private String salt;//盐
  private String roleName;
  private Integer roleId;
  private String serviceName;
  private String serviceCenterType;
  @NotBlank(groups = LockGroup.class, message = "用户状态不能为空")
  private String userState; //1可用 -1不可用
  private String parentKey;//父级中心key
  private Long sortTime;

  public Long getSortTime() {
    return sortTime;
  }

  public void setSortTime(Long sortTime) {
    this.sortTime = sortTime;
  }

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
  }

  public String getParentKey() {
    return parentKey;
  }

  public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }

  public String getUserState() {
    return userState;
  }

  public void setUserState(String userState) {
    this.userState = userState;
  }

  public String getServiceCenterType() {
    return serviceCenterType;
  }

  public void setServiceCenterType(String serviceCenterType) {
    this.serviceCenterType = serviceCenterType;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
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

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public SysRoleBean getRole() {
    return role;
  }

  public void setRole(SysRoleBean role) {
    this.role = role;
  }

  public List<TerminalBean> getTerminalList() {
    return terminalList;
  }

  public void setTerminalList(List<TerminalBean> terminalList) {
    this.terminalList = terminalList;
  }

  public String getWorkState() {
    return workState;
  }

  public void setWorkState(String workState) {
    this.workState = workState;
  }

  public List<SysPermissionBean> getPermissionList() {
    return permissionList;
  }

  public void setPermissionList(List<SysPermissionBean> permissionList) {
    this.permissionList = permissionList;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "SysUserBean{" + "userId=" + userId + ", loginName='" + loginName + '\'' + ", userName='"
        + userName + '\'' + ", password='" + password + '\'' + ", state=" + state
        + ", workState=" + workState + ", mobilePhone='" + mobilePhone + '\'' + ", email='"
        + email + '\'' + ", createTime=" + createTime + ", updatetime=" + updatetime
        + ", lastLoginTime=" + lastLoginTime + ", version=" + version + ", role=" + role
        + ", permissionList=" + permissionList + ", terminalList=" + terminalList
        + ", affiliation='" + affiliation + '\'' + ", serviceKey='" + serviceKey + '\''
        + ", deptId=" + deptId + ", deptName='" + deptName + '\'' + ", type='" + type + '\'' + '}';
  }
}
