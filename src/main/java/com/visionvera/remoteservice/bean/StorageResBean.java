package com.visionvera.remoteservice.bean;

import java.util.Date;

/**
 * 功能描述:
 *
 * @ClassName: StorageResBean
 * @Author: ljfan
 * @Date: 2019-08-28 20:49
 * @Version: V1.0
 */
public class StorageResBean {
  private int id;
  private int businessId;
  private Date createTime;
  private int fileId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getBusinessId() {
    return businessId;
  }

  public void setBusinessId(int businessId) {
    this.businessId = businessId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public int getFileId() {
    return fileId;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }
}
