package com.visionvera.remoteservice.pojo;

import java.io.Serializable;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2019/1/22
 */
public class StorageResVo implements Serializable {

  private Integer fileId;
  private String resId;
  private String storageUrl;
  private String fileUrl;
  private Integer type;

  public StorageResVo(String resId, String storageUrl, String fileUrl, Integer type) {
    this.resId = resId;
    this.storageUrl = storageUrl;
    this.fileUrl = fileUrl;
    this.type = type;
  }

  public StorageResVo(Integer fileId, String resId, String storageUrl, String fileUrl,
      Integer type) {
    this.fileId = fileId;
    this.resId = resId;
    this.storageUrl = storageUrl;
    this.fileUrl = fileUrl;
    this.type = type;
  }

  public StorageResVo() {
  }

  public String getResId() {
    return resId;
  }

  public void setResId(String resId) {
    this.resId = resId;
  }

  public String getStorageUrl() {
    return storageUrl;
  }

  public void setStorageUrl(String storageUrl) {
    this.storageUrl = storageUrl;
  }

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getFileId() {
    return fileId;
  }

  public void setFileId(Integer fileId) {
    this.fileId = fileId;
  }

  @Override
  public String toString() {
    return "StorageResVo{" +
        "fileId=" + fileId +
        ", resId='" + resId + '\'' +
        ", storageUrl='" + storageUrl + '\'' +
        ", fileUrl='" + fileUrl + '\'' +
        ", type=" + type +
        '}';
  }
}
