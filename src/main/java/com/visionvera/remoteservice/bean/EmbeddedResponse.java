package com.visionvera.remoteservice.bean;

/**
 * @author hakimjun
 * @Description: TODO
 * @date 2019/8/13
 */
public class EmbeddedResponse {
  private Integer scuStatus;//scu状态
  private Integer idCardStatus;//身份证状态
  private Integer highPhotoStatus;//高拍仪状态
  private Integer signatureStatus;//签名版状态
  private Integer printerStatus;//打印机状态
  private String version;//scu版本号
  private Integer fingerPrintStatus;//指纹状态
  private Integer type;//指纹状态

  public Integer getScuStatus() {
    return scuStatus;
  }
  public void setScuStatus(Integer scuStatus) {
    this.scuStatus = scuStatus;
  }

  public Integer getIdCardStatus() {return idCardStatus;}
  public void setIdCardStatus(Integer idCardStatus) {
    this.idCardStatus = idCardStatus;
  }

  public Integer getHighPhotoStatus() {
    return highPhotoStatus;
  }
  public void setHighPhotoStatus(Integer highPhotoStatus) {
    this.highPhotoStatus = highPhotoStatus;
  }

  public Integer getSignatureStatus() {
    return signatureStatus;
  }
  public void setSignatureStatus(Integer signatureStatus) {
    this.signatureStatus = signatureStatus;
  }

  public Integer getPrinterStatus() {
    return printerStatus;
  }
  public void setPrinterStatus(Integer printerStatus) {
    this.printerStatus = printerStatus;
  }

  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }

  public Integer getFingerPrintStatus() {
    return fingerPrintStatus;
  }
  public void setFingerPrintStatus(Integer fingerPrintStatus) {
    this.fingerPrintStatus = fingerPrintStatus;
  }

  public Integer getType() {
    return type;
  }
  public void setType(Integer type) {
    this.type = type;
  }



}
