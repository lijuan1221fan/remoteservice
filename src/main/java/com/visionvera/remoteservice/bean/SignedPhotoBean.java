package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: SignedPhotoBean
 *
 * @author quboka
 * @Description: 数字签名照片
 * @date 2018年4月30日
 */
public class SignedPhotoBean implements Serializable {


  /**
   * @Fields serialVersionUID : TODO
   */
  private static final long serialVersionUID = -5784874395699677212L;

  private Integer id;//图片ID
  private String guid;//业务ID
  private String v2vid;//终端号码
  private String photopath;//图片地址
  private Date createTime;//生成图片时间
  private Integer width;//图片宽度
  private Integer heigth;//图片高度
  private String format;//图片格式，默认bmp
  private Integer version;//版本号
  private String businessKey;//业务key
  private Integer status;//状态  0：正常 -1：删除

  public SignedPhotoBean() {
  }

  public SignedPhotoBean(Integer id, String guid, String v2vid, String photopath,
      Date createTime, Integer width, Integer heigth, String format,
      Integer version, String businessKey) {
    super();
    this.id = id;
    this.guid = guid;
    this.v2vid = v2vid;
    this.photopath = photopath;
    this.createTime = createTime;
    this.width = width;
    this.heigth = heigth;
    this.format = format;
    this.version = version;
    this.businessKey = businessKey;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getV2vid() {
    return v2vid;
  }

  public void setV2vid(String v2vid) {
    this.v2vid = v2vid;
  }

  public String getPhotopath() {
    return photopath;
  }

  public void setPhotopath(String photopath) {
    this.photopath = photopath;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeigth() {
    return heigth;
  }

  public void setHeigth(Integer heigth) {
    this.heigth = heigth;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


}
