package com.visionvera.remoteservice.bean;

/**
 * @author jlm
 * @ClassName: 窗口
 * @Description: 审批中心下面的窗口实体类
 * @date 2018/10/31
 */
public class WindowBean {

  /**
   * 主键
   */
  private Integer id;
  /**
   * 窗口名
   */
  private String windowName;
  /**
   * 服务中心key
   */
  private String serviceKey;
  /**
   * 部门主键
   */
  private Integer deptId;
  /**
   * 状态 1有效 0无效 -1禁用
   */
  private Integer state;
  /**
   * 判断是否是使用中  1 正在办理业务  2 空闲中  3 等待中
   */
  private Integer isUse;

  /**
   * 用于页面展示 数据库无该字段  serviceName,deptName
   *
   * @return
   */
  private String serviceName;
  private String deptName;

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Integer getIsUse() {
    return isUse;
  }

  public void setIsUse(Integer isUse) {
    this.isUse = isUse;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getWindowName() {
    return windowName;
  }

  public void setWindowName(String windowName) {
    this.windowName = windowName;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "WindowBean{" +
            "id=" + id +
            ", windowName='" + windowName + '\'' +
            ", serviceKey='" + serviceKey + '\'' +
            ", deptId=" + deptId +
            ", state=" + state +
            ", isUse=" + isUse +
            ", serviceName='" + serviceName + '\'' +
            ", deptName='" + deptName + '\'' +
            '}';
  }
}
