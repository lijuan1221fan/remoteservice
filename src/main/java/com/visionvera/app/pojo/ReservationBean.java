package com.visionvera.app.pojo;

import com.visionvera.remoteservice.bean.Materials;
import java.util.Date;
import java.util.List;

/**
 * @ClassNameReservationBean
 *description 预约对象属性
 * @author ljfan
 * @date 2019/03/22
 *version
 */
public class ReservationBean {
  /**
   *  预约id
   */
  private Integer id;
  /**
   * 服务中心名称
   */
  private String serviceName;
  /**
   *  预约名称
   */
  private String realName;
  /**
   *  业务类别名称
   */
  private String businessTypeName;
  /**
   *  业务详情
   */
  private String businessDetailName;
  /**
   *  开始时间
   */
  private Date startTime;
  /**
   *  预约状态
   */
  private Integer state;
  /**
   * 可预约日期
   */
  private Date date;
  /**
   *上午已预约号
   */
   private Integer morningNumber;
  /**
   * 上午可预约号
   */
  private Integer morningAppointmentNumber;
  /**
   * 下午可预约号
   */
  private Integer afternoonAppointmentNumber;
  /**
   * 下午已预约号
   */
  private Integer afternoonNumber;

  /**
   *上午限制号
   */
  private Integer morningLimitNumber;

  /**
   * 下午限制号
   */
  private Integer afternoonLimitNumber;
  /**
   * 部门名称
   */
  private String deptName;

  /**
   *服务中心名称
   */
  private String serviceAddress;

  /**
   * 待上传申请材料数
   * @return
   */
  private Integer materialsNumber;

  /**
   * 是否支持预约
   * @return
   */
  private boolean IsSupport;

  public Integer getMorningAppointmentNumber() {
    return morningAppointmentNumber;
  }

  public void setMorningAppointmentNumber(Integer morningAppointmentNumber) {
    this.morningAppointmentNumber = morningAppointmentNumber;
  }

  public Integer getAfternoonAppointmentNumber() {
    return afternoonAppointmentNumber;
  }

  public void setAfternoonAppointmentNumber(Integer afternoonAppointmentNumber) {
    this.afternoonAppointmentNumber = afternoonAppointmentNumber;
  }

  public boolean isSupport() {
    return IsSupport;
  }

  public void setSupport(boolean support) {
    IsSupport = support;
  }

  public Integer getMaterialsNumber() {
    return materialsNumber;
  }

  public void setMaterialsNumber(Integer materialsNumber) {
    this.materialsNumber = materialsNumber;
  }

  public String getServiceAddress() {
    return serviceAddress;
  }

  public void setServiceAddress(String serviceAddress) {
    this.serviceAddress = serviceAddress;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  private List<Materials> materialsList;

  public List<Materials> getMaterialsList() {
    return materialsList;
  }

  public void setMaterialsList(
      List<Materials> materialsList) {
    this.materialsList = materialsList;
  }

  public Integer getMorningLimitNumber() {
    return morningLimitNumber;
  }

  public void setMorningLimitNumber(Integer morningLimitNumber) {
    this.morningLimitNumber = morningLimitNumber;
  }

  public Integer getAfternoonLimitNumber() {
    return afternoonLimitNumber;
  }

  public void setAfternoonLimitNumber(Integer afternoonLimitNumber) {
    this.afternoonLimitNumber = afternoonLimitNumber;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getMorningNumber() {
    return morningNumber;
  }

  public void setMorningNumber(Integer morningNumber) {
    this.morningNumber = morningNumber;
  }

  public Integer getAfternoonNumber() {
    return afternoonNumber;
  }

  public void setAfternoonNumber(Integer afternoonNumber) {
    this.afternoonNumber = afternoonNumber;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getBusinessTypeName() {
    return businessTypeName;
  }

  public void setBusinessTypeName(String businessTypeName) {
    this.businessTypeName = businessTypeName;
  }

  public String getBusinessDetailName() {
    return businessDetailName;
  }

  public void setBusinessDetailName(String businessDetailName) {
    this.businessDetailName = businessDetailName;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
