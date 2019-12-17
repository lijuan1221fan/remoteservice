package com.visionvera.remoteservice.bean;

/***
 *
 * @ClassName: MeetingOperation
 * @Description: 会议操作记录表
 * @author wangruizhi
 * @date 2018年3月24日
 *
 */
public class MeetingOperationBean extends BaseBean {

  /***
   * 业务唯一key
   */
  private String businessKey;
  /***
   * 会议id
   */
  private String scheduleId;
  /***
   * 会管登陆后用户id
   */
  private String uuid;
  /***
   * 操作人id
   */
  private Integer userId;
  /***
   * 会管登陆后token
   */
  private String access_token;
  /***
   * 终端列表
   */
  private String terminal_list;
  /***
   * 主席ip
   */
  private String ip;


  /***
   * 是否分享 0 ：未开启  1：已开启
   */
  private Integer isShare;
  /***
   * 动态入会得终端号
   */
  private String dynDeviceNumber;


  public MeetingOperationBean() {
    super();
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(String scheduleId) {
    this.scheduleId = scheduleId;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getTerminal_list() {
    return terminal_list;
  }

  public void setTerminal_list(String terminal_list) {
    this.terminal_list = terminal_list;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getIsShare() {
    return isShare;
  }

  public void setIsShare(Integer isShare) {
    this.isShare = isShare;
  }

  public String getDynDeviceNumber() {
    return dynDeviceNumber;
  }

  public void setDynDeviceNumber(String dynDeviceNumber) {
    this.dynDeviceNumber = dynDeviceNumber;
  }
}
