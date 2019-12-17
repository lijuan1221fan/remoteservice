package com.visionvera.remoteservice.pojo;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/19
 */
public class SysLogVo extends BaseVo {

  private String userName;
  private String operation;
  private String startTime;
  private String endTime;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
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
}
