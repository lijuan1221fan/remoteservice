package com.visionvera.remoteservice.bean;

import java.util.Date;

/**
 * ClassName: Regionb
 *
 * @author quboka
 * @Description: 行政区域表
 * @date 2017年12月19日
 */
public class RegionbBean {

  private String id;//行政区域id
  private String pid;//上级行政区域id
  private String name;//行政区域名称
  private Integer gradeid;//行政区域级别
  private String isleaf;//叶节点
  private Date updatetime;//更新时间

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getGradeid() {
    return gradeid;
  }

  public void setGradeid(Integer gradeid) {
    this.gradeid = gradeid;
  }

  public String getIsleaf() {
    return isleaf;
  }

  public void setIsleaf(String isleaf) {
    this.isleaf = isleaf;
  }

  public Date getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }


}
