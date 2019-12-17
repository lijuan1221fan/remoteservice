package com.visionvera.remoteservice.bean;

import java.util.List;

/**
 * @author zhaolei
 * @ClassName: ScheduleBean
 * @Description: 用户查询预约列表bean
 * @date 2016年8月12日 下午3:29:58
 */
public class ScheduleVO {

  private String uuid; //uuid
  private String processId;//流程ID
  private String name;  //名称
  private String startTime;  //开始时间
  private String meetTime;  //会议开始时间
  private String endTime;   //结束时间
  private String createTime; //创建时间
  private String groupId;  //授权用户组ID
  private String groupName; //授权用户组名称
  private String creatorId;  //创建人ID
  private String creatorName;  //创建人名称
  private String accessor;  //审核人
  private String status;   //预约状态 1-审批中，2、4、5-审批通过，3-未通过, 6-变更申请操作
  private Integer pcmeetId;   //pamir会议id
  private String key;   //会议ID（已废除）
  private String ip;   //主席IP
  private Integer callMode;   //呼叫方式
  private String xmcu;   //xmcu
  private String dvrServerNumber;   //服务器号
  private String notice;   //通知消息
  private String fileUrl;   //附件地址
  private String description;   //备注
  private String devGroups;   //会场ID(多个用,分割)
  private String devices;   //终端ID(多个用,分割)
  private Integer signTotal;   //需要签到的会场总数
  private Integer signed;   //已签到的会场总数
  //    private List<MeetingDevVO> devList;
  private int createType; //会议创建类型
  private String editorName;     //编辑人账号 会议详细信息需要用到
  private String editType;       //编辑类型 会议详细信息需要用到
  private Integer level;         //会议等级
  private Integer modifyStatus;         //是否变更。0 否，1 是
  private String monitor;        //监控协转地址
    private String masterNo;       //主席号码
  private Integer planPersonNum;       //计划参会人数
  private Integer planDevNum;       //计划参会终端数
  private Integer personNum;       //实际参会人数
  private Integer devNum;       //实际参会终端数
  private String host;          //会议主持人
  private Integer memberLevel;  //参会人员级别
  private List<DeviceVO> devList;  //参会终端列表（前端以json字符串形式传递）
  private String devId;        //设备号码
  private Integer devSum;      //设备终端数量
  public ScheduleVO() {
  }

  public Integer getDevSum() {
    return devSum;
  }

  public void setDevSum(Integer devSum) {
    this.devSum = devSum;
  }

  public String getDevId() {
    return devId;
  }

  public void setDevId(String devId) {
    this.devId = devId;
  }

    public String getMasterNo() {
    return masterNo;
  }

    public void setMasterNo(String masterNo) {
    this.masterNo = masterNo;
  }

  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }

  public Integer getPlanPersonNum() {
    return planPersonNum;
  }

  public void setPlanPersonNum(Integer planPersonNum) {
    this.planPersonNum = planPersonNum;
  }

  public Integer getPlanDevNum() {
    return planDevNum;
  }

  public void setPlanDevNum(Integer planDevNum) {
    this.planDevNum = planDevNum;
  }

  public Integer getPersonNum() {
    return personNum;
  }

  public void setPersonNum(Integer personNum) {
    this.personNum = personNum;
  }

  public Integer getDevNum() {
    return devNum;
  }

  public void setDevNum(Integer devNum) {
    this.devNum = devNum;
  }

  public String getMonitor() {
    return monitor;
  }

  public void setMonitor(String monitor) {
    this.monitor = monitor;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public String getEditorName() {
    return editorName;
  }

  public void setEditorName(String editorName) {
    this.editorName = editorName;
  }

  public String getEditType() {
    return editType;
  }

  public void setEditType(String editType) {
    this.editType = editType;
  }

  public int getCreateType() {
    return createType;
  }

  public void setCreateType(int createType) {
    this.createType = createType;
  }

  //	public List<MeetingDevVO> getDevList() {
//		return devList;
//	}
//	public void setDevList(List<MeetingDevVO> devList) {
//		this.devList = devList;
//	}
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public void setCreatetime(String createTime) {
    this.createTime = createTime;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getAccessor() {
    return accessor;
  }

  public void setAccessor(String accessor) {
    this.accessor = accessor;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getCallMode() {
    return callMode;
  }

  public void setCallMode(Integer callMode) {
    this.callMode = callMode;
  }

  public String getXmcu() {
    return xmcu;
  }

  public void setXmcu(String xmcu) {
    this.xmcu = xmcu;
  }

  public String getDvrServerNumber() {
    return dvrServerNumber;
  }

  public void setDvrServerNumber(String dvrServerNumber) {
    this.dvrServerNumber = dvrServerNumber;
  }

  public String getNotice() {
    return notice;
  }

  public void setNotice(String notice) {
    this.notice = notice;
  }

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public String getDevGroups() {
    return devGroups;
  }

  public void setDevGroups(String devGroups) {
    this.devGroups = devGroups;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDevices() {
    return devices;
  }

  public void setDevices(String devices) {
    this.devices = devices;
  }

  public Integer getSignTotal() {
    return signTotal;
  }

  public void setSignTotal(Integer signTotal) {
    this.signTotal = signTotal;
  }

  public Integer getSigned() {
    return signed;
  }

  public void setSigned(Integer signed) {
    this.signed = signed;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Integer getPcmeetId() {
    return pcmeetId;
  }

  public void setPcmeetId(Integer pcmeetId) {
    this.pcmeetId = pcmeetId;
  }

  public Integer getModifyStatus() {
    return modifyStatus;
  }

  public void setModifyStatus(Integer modifyStatus) {
    this.modifyStatus = modifyStatus;
  }

  public String getMeetTime() {
    return meetTime;
  }

  public void setMeetTime(String meetTime) {
    this.meetTime = meetTime;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getMemberLevel() {
    return memberLevel;
  }

  public void setMemberLevel(Integer memberLevel) {
    this.memberLevel = memberLevel;
  }

  public List<DeviceVO> getDevList() {
    return devList;
  }

  public void setDevList(List<DeviceVO> devList) {
    this.devList = devList;
  }

}
