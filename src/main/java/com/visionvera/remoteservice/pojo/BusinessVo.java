package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.BusinessStateGroup;
import com.visionvera.common.validator.group.ChangeGroup;
import com.visionvera.common.validator.group.CompletGroup;
import com.visionvera.common.validator.group.GetGroup;
import com.visionvera.common.validator.group.NextGroup;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.ClientInfo;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/18
 */
public class BusinessVo extends BaseVo {

  @NotNull(groups = {ChangeGroup.class, NextGroup.class, CompletGroup.class}, message = "业务id不能为空")
  private Integer businessId;
  @NotNull(groups = {CompletGroup.class}, message = "最终状态不能为空")
  private Integer finalStates;
  private String causeIds;
  private String remarks;
  private ClientInfo clientInfo;
  private String serviceKey;
  @NotNull(groups = {GetGroup.class, ChangeGroup.class}, message = "businessType不能为空")
  private Integer businessType;
  @NotNull(groups = {GetGroup.class}, message = "businessTypeId不能为空")
  private Integer businessTypeId;
  @NotBlank(groups = {ChangeGroup.class}, message = "业务详情不能为空")
  private String describes;
  @NotNull(groups = {BusinessStateGroup.class}, message = "businessInfo不能为空")
  private BusinessInfo businessInfo;
  private Integer deptId;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String startTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String endTime;
  private String operatorName;
  private Integer state;
  private String businessTypeName;
  private String cardName;
  private String cardId;
  public String getCardName() {
    return cardName;
  }

  public void setCardName(String cardName) {
    this.cardName = cardName;
  }

  public String getCardId() {
    return cardId;
  }

  public void setCardId(String cardId) {
    this.cardId = cardId;
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }

  public Integer getFinalStates() {
    return finalStates;
  }

  public void setFinalStates(Integer finalStates) {
    this.finalStates = finalStates;
  }

  public String getCauseIds() {
    return causeIds;
  }

  public void setCauseIds(String causeIds) {
    this.causeIds = causeIds;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public ClientInfo getClientInfo() {
    return clientInfo;
  }

  public void setClientInfo(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getBusinessType() {
    return businessType;
  }

  public void setBusinessType(Integer businessType) {
    this.businessType = businessType;
  }

  public Integer getBusinessTypeId() {
    return businessTypeId;
  }

  public void setBusinessTypeId(Integer businessTypeId) {
    this.businessTypeId = businessTypeId;
  }

  public String getDescribes() {
    return describes;
  }

  public void setDescribes(String describes) {
    this.describes = describes;
  }

  public BusinessInfo getBusinessInfo() {
    return businessInfo;
  }

  public void setBusinessInfo(BusinessInfo businessInfo) {
    this.businessInfo = businessInfo;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
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

  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getBusinessTypeName() {
    return businessTypeName;
  }

  public void setBusinessTypeName(String businessTypeName) {
    this.businessTypeName = businessTypeName;
  }

}
