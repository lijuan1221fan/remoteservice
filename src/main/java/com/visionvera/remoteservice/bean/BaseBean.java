package com.visionvera.remoteservice.bean;

import java.util.Date;

/***
 *
 * @ClassName: BaseBean
 * @Description: 通用字段
 * @author wangruizhi
 * @date 2018年3月24日
 *
 */
public class BaseBean {

  /***
   * 主键
   */
  private Integer id;
  /***
   * 创建时间
   */
  private Date createTime;
  /***
   * 修改/更新时间
   */
  private Date updatetime;
  /***
   * 状态
   */
  private Integer status;
  /***
   * 版本号，用于并发修改时保证数据有效性
   */
  private Integer version;

  public BaseBean() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
