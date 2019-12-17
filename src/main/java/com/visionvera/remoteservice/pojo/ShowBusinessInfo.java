package com.visionvera.remoteservice.pojo;

import com.visionvera.remoteservice.bean.BusinessInfo;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassNameShowNumber
 *description 叫号展示
 * @author ljfan
 * @date 2019/04/16
 *version
 */
public class ShowBusinessInfo {
  public String getNumServiceKey() {
    return numServiceKey;
  }

  public void setNumServiceKey(String numServiceKey) {
    this.numServiceKey = numServiceKey;
  }

  /**
   * 号码对应的servicekey
   */
  private  String numServiceKey;
  /**
   * 部门id
   */
  private Integer deptId;
  /**
   * 部门名称
   */
  private String deptName;
  /**
   * 部门等待数
   */
  private Long waitNumber=(long)0;
  /**
   * 办理/呼叫号
   */
  private String proceNumber;
  /**
   * 受理端信息展示
   */
  private BusinessInfo businessInfo;
  /**
   * 下一个待处理待号
   */
  private String nextNumber;
  /**
   * 已办人数
   */
  private long doneNumber;
  /**
   * 下一个带办理号的服务中心名称
   */
  private String nextServiceName;
  /**
   * 处理中的号的服务中心名称
   */
  private String processServiceName;
  /**
   * 提示编码
   */
  private String code;
  /**
   * 消息
   */
  private String message;
  /**
   * 呼叫中的号
   */
  private String callNumber = "";
  /**
   * 呼叫中的人名称
   */
  private String callName="";
  public String getCallName() {
    return callName;
  }

  public void setCallName(String callName) {
    this.callName = callName;
  }
  /**
   * 等待的人名称
   */
  private String nextName="";
  public String getNextName() {
    return nextName;
  }
  public void setNextName(String nextName) {
    this.nextName = nextName;
  }
  private String type;
  /**
   * 服务中心servicekey
   */
  private String serviceKey;
  /**
   * 服务中心名称
   */
  private String serviceName;

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Long getWaitNumber() {
    return waitNumber;
  }

  public void setWaitNumber(Long waitNumber) {
    this.waitNumber = waitNumber;
  }

  public String getProceNumber() {
    return proceNumber;
  }

  public void setProceNumber(String proceNumber) {
    this.proceNumber = proceNumber;
  }

  public BusinessInfo getBusinessInfo() {
    return businessInfo;
  }

  public void setBusinessInfo(BusinessInfo businessInfo) {
    this.businessInfo = businessInfo;
  }

  public String getNextNumber() {
    return nextNumber;
  }

  public void setNextNumber(String nextNumber) {
    this.nextNumber = nextNumber;
  }

  public long getDoneNumber() {
    return doneNumber;
  }

  public void setDoneNumber(long doneNumber) {
    this.doneNumber = doneNumber;
  }

  public String getNextServiceName() {
    return nextServiceName;
  }

  public void setNextServiceName(String nextServiceName) {
    this.nextServiceName = nextServiceName;
  }

  public String getProcessServiceName() {
    return processServiceName;
  }

  public void setProcessServiceName(String processServiceName) {
    this.processServiceName = processServiceName;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCallNumber() {
    return callNumber;
  }

  public void setCallNumber(String callNumber) {
    this.callNumber = callNumber;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ShowBusinessInfo(){
    super();
  }
  public ShowBusinessInfo(String code){
    this.code = code;
  }
  public ShowBusinessInfo(String code,String message){
    this.code = code;
    this.message = message;
  }
  public ShowBusinessInfo(Integer deptId,long waitNumber,String nextNumber,String nextServiceName){
    this.deptId = deptId;
    this.waitNumber = waitNumber;
    this.nextNumber = nextNumber;
    this.nextServiceName = nextServiceName;
  }
  public ShowBusinessInfo(Integer deptId,long waitNumber){
     this.deptId = deptId;
     this.waitNumber = waitNumber;
  }
  public ShowBusinessInfo(String code,String message,BusinessInfo businessInfo,long waitNumber){
    this.message = message;
    this.code = code;
    this.businessInfo = businessInfo;
    this.waitNumber = waitNumber;
  }
  public ShowBusinessInfo(Integer deptId,long waitNumber,String code,String message,String proceNumber){
    this.message = message;
    this.code = code;
    this.deptId = deptId;
    this.waitNumber = waitNumber;
    this.proceNumber = proceNumber;
  }
  public ShowBusinessInfo(long waitNumber,String nextNumber,String nextServiceName,String callNumber){
    this.waitNumber = waitNumber;
    this.waitNumber = waitNumber;
    this.nextNumber = nextNumber;
    this.nextServiceName = nextServiceName;
    this.callNumber = callNumber;
  }
  public ShowBusinessInfo(long waitNumber,String proceNumber,String nextNumber,String processServiceName,String nextServiceName,String callNumber){
    this.waitNumber = waitNumber;
    this.proceNumber = proceNumber;
    this.waitNumber = waitNumber;
    this.nextNumber = nextNumber;
    this.processServiceName = processServiceName;
    this.nextServiceName = nextServiceName;
    this.callNumber = callNumber;
  }
  public  String getJsonToString(ShowBusinessInfo info){
    JSONObject json = new JSONObject();
    json.put("result",info);
    return json.toJSONString();
  }
}
