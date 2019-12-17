package com.visionvera.app.pojo;


import com.visionvera.app.group.AddUserGroup;
import com.visionvera.app.group.AuthorizationGroup;
import com.visionvera.app.group.TokenGroup;
import com.visionvera.app.group.UpdatePasswordGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.remoteservice.constant.CommonConstant;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author jlm
 * @ClassName:
 * @Description: 用户信息接收类
 * @date 2019/3/14
 */
public class AppUserVo {

  /**
   * 用户ID
   */
  @NotNull(groups = {UpdateGroup.class, UpdatePasswordGroup.class}, message = "用户id不能为空")
  private Long appUserId;
  /**
   * 登录名
   */
  @NotBlank(groups = {AddUserGroup.class, UpdateGroup.class}, message = "登录名不能为空")
  @Pattern(groups = {AddUserGroup.class, UpdateGroup.class},regexp = CommonConstant.LOGINNAME_REGX,message = "登录名格式不正确")
  private String loginName;
  /**
   * token
   */
  @NotEmpty(groups = {TokenGroup.class},message = "token不能为空")
  private String token;
  /**
   * 密码
   */
  @NotBlank(groups = {AddUserGroup.class, UpdatePasswordGroup.class}, message = "密码不能为空")
  private String password;
  /**
   * 真实姓名
   */
  @NotEmpty(groups = {AuthorizationGroup.class},message = "姓名不能为空")
  @Pattern(groups = {AuthorizationGroup.class},regexp = CommonConstant.LOGINNAME_REGX,message = "姓名格式不正确")
  private String realName;
  /**
   * 身份证号码
   * 
   */
  @NotEmpty(groups = {AuthorizationGroup.class},message = "身份证号码不能为空")
  private String idCardNo;
  /**
   * 中心key
   */
  @NotBlank(groups = {AddUserGroup.class, UpdateGroup.class}, message = "中心key不能为空")
  private String serviceKey;

  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 更新时间
   */
  private Date updateTime;
  /**
   * 用户状态
   */
  private Integer state;
  /**
   * 页数
   */
  private Integer pageNum;
  /**
   * 每页显示数量
   */
  private Integer pageSize;

  public Long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(Long appUserId) {
    this.appUserId = appUserId;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
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

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
