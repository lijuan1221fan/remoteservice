package com.visionvera.remoteservice.bean;

/**
 * ClassName: CertificateBean
 *
 * @author quboka
 * @Description: 证件
 * @date 2018年3月25日
 */
public class CertificateBean {

  /**
   * @Fields certificateId : 证件id
   */
  private Integer certificateId;
  /**
   * @Fields certificateTitle : 证件标题
   */
  private String certificateTitle;

  public Integer getCertificateId() {
    return certificateId;
  }

  public void setCertificateId(Integer certificateId) {
    this.certificateId = certificateId;
  }

  public String getCertificateTitle() {
    return certificateTitle;
  }

  public void setCertificateTitle(String certificateTitle) {
    this.certificateTitle = certificateTitle;
  }

  @Override
  public String toString() {
    return "CertificateBean [certificateId=" + certificateId
        + ", certificateTitle=" + certificateTitle + "]";
  }

}
