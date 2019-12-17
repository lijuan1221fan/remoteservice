package com.visionvera.remoteservice.bean;/**
 * 功能描述:
 *
 * @ClassName: UnattendedCause
 * @Author: ljfan
 * @Date: 2019-02-25 13:28
 * @Version: V1.0
 */
public class UnattendedCause {
       private String causeId;
       private String causeName;

  public String getCauseId() {
    return causeId;
  }

  public void setCauseId(String causeId) {
    this.causeId = causeId;
  }

  public String getCauseName() {
    return causeName;
  }

  public void setCauseName(String causeName) {
    this.causeName = causeName;
  }
}
