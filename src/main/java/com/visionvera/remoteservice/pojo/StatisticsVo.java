package com.visionvera.remoteservice.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.visionvera.common.validator.group.QueryGroup;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * @author ljfan
 * @ClassNameStatistics 统计查询参数
 * @date 2018/12/26 version
 */
public class StatisticsVo {

  private Integer deptId;
  @NotNull(groups = {QueryGroup.class}, message = "开始日期不能为空")
  @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
  private Date startDate;
  @NotNull(groups = {QueryGroup.class}, message = "结束日期不能为空")
  @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
  private Date endDate;
  private List<String> serviceKeys;

  private String serviceKey;

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKey(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
  }


  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }
}
