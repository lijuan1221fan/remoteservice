package com.visionvera.app.pojo;

import com.visionvera.app.group.BusinessDetailInfoGroup;
import com.visionvera.app.group.ReservationGroup;
import com.visionvera.app.group.ReservationRecordGroup;
import com.visionvera.remoteservice.pojo.BaseVo;
import javax.validation.constraints.*;

/**
 * @ClassNameAppParaVo
 *description app参数
 * @author ljfan
 * @date 2019/03/19
 *version
 */
public class AppParaVo extends BaseVo {
  @NotNull(groups = {ReservationGroup.class},message = "部门id不能为空")
  private Integer deptId;
  @NotEmpty(groups = {ReservationGroup.class, BusinessDetailInfoGroup.class},message = "服务中心serviceKey不能为空")
  private String serviceKey;
  //当前时间
  private String nowDay;
  //历史时间
  private String oldDay;
  //app用户id
  private Integer appUserId;
  @NotNull(groups = {ReservationRecordGroup.class},message = "app用户身份证ID不能为空")
  private String idCardNo;

  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
  }

  //业务详情id
  @NotNull(groups = {BusinessDetailInfoGroup.class},message = "业务详情id不能为空")
  private Integer businessesDetailId;

  public Integer getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(Integer appUserId) {
    this.appUserId = appUserId;
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

  public String getNowDay() {
    return nowDay;
  }

  public void setNowDay(String nowDay) {
    this.nowDay = nowDay;
  }

  public String getOldDay() {
    return oldDay;
  }

  public void setOldDay(String oldDay) {
    this.oldDay = oldDay;
  }

  public Integer getBusinessesDetailId() {
    return businessesDetailId;
  }

  public void setBusinessesDetailId(Integer businessesDetailId) {
    this.businessesDetailId = businessesDetailId;
  }
}
