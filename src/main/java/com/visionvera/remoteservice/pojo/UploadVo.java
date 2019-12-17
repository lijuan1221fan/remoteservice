package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.PrintGroup;
import com.visionvera.common.validator.group.SignPictureGroup;
import javax.validation.constraints.*;


/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/17
 */
public class UploadVo extends BaseVo {

  @NotNull(message = "业务id不能为空")
  private Integer businessId;
  private Boolean sing = false;
  private Boolean stamp = false;
  private Integer stampX;
  private Integer stampY;
  private Integer singX;
  private Integer singY;
  @NotNull(groups = {SignPictureGroup.class}, message = "图片类型不能为空")
  private Integer pictureType;
  @NotBlank(groups = {PrintGroup.class}, message = "图片路径不能为空")
  private String imgUrl;

  @NotNull
  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(@NotNull Integer businessId) {
    this.businessId = businessId;
  }

  public Boolean getSing() {
    return sing;
  }

  public void setSing(Boolean sing) {
    this.sing = sing;
  }

  public Boolean getStamp() {
    return stamp;
  }

  public void setStamp(Boolean stamp) {
    this.stamp = stamp;
  }

  public Integer getPictureType() {
    return pictureType;
  }

  public void setPictureType(Integer pictureType) {
    this.pictureType = pictureType;
  }

  public Integer getStampX() {
    return stampX;
  }

  public void setStampX(Integer stampX) {
    this.stampX = stampX;
  }

  public Integer getStampY() {
    return stampY;
  }

  public void setStampY(Integer stampY) {
    this.stampY = stampY;
  }

  public Integer getSingX() {
    return singX;
  }

  public void setSingX(Integer singX) {
    this.singX = singX;
  }

  public Integer getSingY() {
    return singY;
  }

  public void setSingY(Integer singY) {
    this.singY = singY;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}
