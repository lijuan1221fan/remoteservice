/*
 * BusinessInfo.java
 * ------------------*
 * 2018-08-22 created
 */
package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.List;
import javax.naming.Name;

/**
 * ----------------------* 2018-08-22created
 */
public class BusinessInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  /**
   * 号码
   */
  private String number;
  /**
   * 创建时间
   */
  private String createTime;
  /**
   * 更新时间
   */
  private String updateTime;
  /**
   * 业务开始时间
   */
  private String startTime;
  /**
   * 业务结束时间
   */
  private String endTime;
  /**
   * 业务状态:1 未处理 2 处理中 3 已处理 4  过号
   */
  private Integer state;
  /**
   * 服务中心key
   */
  private String serviceKey;
  /**
   * 服务中心名称
   */
  private String serviceName;
  /**
   * 部门id
   */
  private Integer deptId;
  /**
   * 业务类型
   */
  private Integer businessType;
  /**
   * 业务类型名称
   */
  private String businessTypeName;
  /**
   * 操作人id
   */
  private Integer operatorId;
  /**
   * 操作人姓名
   */
  private String operatorName;
  /**
   * 描述
   */
  private String remarks;
  /**
   * 版本
   */
  private Integer version;
  /**
   * 叫号前缀
   */
  private String numberPrefix;
  /**
   * 结束时的状态  0：未结束 1：已经处理  2：未处理
   */
  private Integer finalState;
  /**
   * 终端号码
   */
  private String terminalNumber;
  /**
   * 是否需要上传表单
   */
  private Boolean uploadForms;
  /**
   * 业务类型
   */
  private BusinessTypeBean businessTypeBean;
  /**
   * 证件号码
   */
  private String idcard;
  /**
   * 业务类别
   */
  private String describes;
  /**
   *预约表ID
   */
  private Integer appointmentId;
  /**
   *类型：1:现场取号，2:预约取号
   */
  private Integer type;
  /**
   *中心表
   */
  private String parentKey;
  private List<BusinessLog> businessLogList;

  /**
  * 未办理原因
   */
  private List<String> UnattendedCause;
  /**
   * 审批中心serviceKey
   */
  private List<String> serviceKeys;
  /**
   * 受理端已处理人数
   */
  private Integer doneCount;

  public Integer getDoneCount() {
    return doneCount;
  }

  // 是否自定义签名盖章  1.自定义  2.配置
  private Integer isCustom;

  /**
   * 受理服务中心id
   */
  private String handleServiceKey;

  /**
   * 受理服务中心名称
   */
  private String handleServiceName;

  private String name;

  /**
   * 非一窗宗办
   * @return
   */
  private Integer deptType;
    /**
     * 办理该业务的窗口id
     */
    private Integer handleWindowId;

    /**
     * 终端类型
     */
    private Integer vcDevType;

    public Integer getVcDevType() {
        return vcDevType;
    }

    public void setVcDevType(Integer vcDevType) {
        this.vcDevType = vcDevType;
    }

    public Integer getHandleWindowId() {
        return handleWindowId;
    }

    public void setHandleWindowId(Integer handleWindowId) {
        this.handleWindowId = handleWindowId;
    }

  public Integer getDeptType() {
    return deptType;
  }

  public void setDeptType(Integer deptType) {
    this.deptType = deptType;
  }

  public Integer getIsCustom() {
    return isCustom;
  }

  public void setIsCustom(Integer isCustom) {
    this.isCustom = isCustom;
  }

  public void setDoneCount(Integer doneCount) {
    this.doneCount = doneCount;
  }

  public BusinessInfo() {

  }

  public Integer getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Integer appointmentId) {
    this.appointmentId = appointmentId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public BusinessInfo(String number, String serviceKey, Integer deptId, Integer businessType,
                      String businessTypeName, Integer type, Integer appointmentId, String handleServiceKey) {
    this.number = number;
    this.serviceKey = serviceKey;
    this.deptId = deptId;
    this.businessType = businessType;
    this.businessTypeName = businessTypeName;
    this.type = type;
    this.appointmentId = appointmentId;
      this.handleServiceKey = handleServiceKey;
  }

  public BusinessInfo(String number, String serviceKey, Integer deptId, Integer businessType,
      String businessTypeName, Integer type, Integer appointmentId, String handleServiceKey,String name,String idCard) {
    this.number = number;
    this.serviceKey = serviceKey;
    this.deptId = deptId;
    this.businessType = businessType;
    this.businessTypeName = businessTypeName;
    this.type = type;
    this.appointmentId = appointmentId;
    this.handleServiceKey = handleServiceKey;
    this.name= name;
    this.idcard=idCard;
  }

  public List<BusinessLog> getBusinessLogList() {
    return businessLogList;
  }

  public void setBusinessLogList(List<BusinessLog> businessLogList) {
    this.businessLogList = businessLogList;
  }

  public String getIdcard() {
    return idcard;
  }

  public void setIdcard(String idcard) {
    this.idcard = idcard;
  }

  public Boolean getUploadForms() {
    return uploadForms;
  }

  public void setUploadForms(Boolean uploadForms) {
    this.uploadForms = uploadForms;
  }

  public BusinessTypeBean getBusinessTypeBean() {
    return businessTypeBean;
  }

  public void setBusinessTypeBean(BusinessTypeBean businessTypeBean) {
    this.businessTypeBean = businessTypeBean;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
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

  public Integer getBusinessType() {
    return businessType;
  }

  public void setBusinessType(Integer businessType) {
    this.businessType = businessType;
  }

  public String getBusinessTypeName() {
    return businessTypeName;
  }

  public void setBusinessTypeName(String businessTypeName) {
    this.businessTypeName = businessTypeName == null ? null : businessTypeName.trim();
  }

  public Integer getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Integer operatorId) {
    this.operatorId = operatorId;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName == null ? null : operatorName.trim();
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks == null ? null : remarks.trim();
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getTerminalNumber() {
    return terminalNumber;
  }

  public void setTerminalNumber(String terminalNumber) {
    this.terminalNumber = terminalNumber;
  }

  public String getNumberPrefix() {
    return numberPrefix;
  }

  public void setNumberPrefix(String numberPrefix) {
    this.numberPrefix = numberPrefix;
  }

  public Integer getFinalState() {
    return finalState;
  }

  public void setFinalState(Integer finalState) {
    this.finalState = finalState;
  }

  public String getDescribes() {
    return describes;
  }

  public void setDescribes(String describes) {
    this.describes = describes;
  }

  public String getParentKey() {
    return parentKey;
  }

  public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
  }

  public List<String> getUnattendedCause() {
    return UnattendedCause;
  }

  public void setUnattendedCause(List<String> unattendedCause) {
    UnattendedCause = unattendedCause;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getHandleServiceKey() {
    return handleServiceKey;
  }

  public void setHandleServiceKey(String handleServiceKey) {
    this.handleServiceKey = handleServiceKey;
  }

  public String getHandleServiceName() {
    return handleServiceName;
  }

  public void setHandleServiceName(String handleServiceName) {
    this.handleServiceName = handleServiceName;
  }

    @Override
    public String toString() {
        return "BusinessInfo{" +
                "id=" + id +
                ", serviceKey='" + serviceKey + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", deptId=" + deptId +
                ", businessType=" + businessType +
                ", businessTypeName='" + businessTypeName + '\'' +
                ", operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", finalState=" + finalState +
                ", type=" + type +
                ", handleServiceKey='" + handleServiceKey + '\'' +
                ", handleServiceName='" + handleServiceName + '\'' +
                ", handleWindowId=" + handleWindowId +
                '}';
    }
}
