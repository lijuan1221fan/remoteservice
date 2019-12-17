package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.*;

import javax.validation.constraints.*;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/18
 */
public class StorageGatewayVo {

    @NotNull(groups = {ScanningPhotoGroup.class, DeleteGroup.class, GetGroup.class, CardGroup.class}, message = "业务id不能为空")
  private Integer businessId;
  @NotNull(groups = {ScanningPhotoGroup.class}, message = "终端号不能为空")
  private String deviceNumber;
  @NotNull(groups = {ScanningPhotoGroup.class, DeleteGroup.class}, message = "图片类型不能为空")
  private Integer pictureType;
  @NotBlank(groups = {ScanningPhotoGroup.class, CardGroup.class}, message = "身份证号不能为空")
  private String cardId;
  @NotBlank(groups = {ScanningPhotoGroup.class, CardGroup.class}, message = "身份证姓名不能为空")
  private String cardName;
  private Integer materialsId;
  @NotBlank(groups = {DeleteGroup.class}, message = "图片id不能为空")
  private String ids;

  public String getDeviceNumber() {
    return deviceNumber;
  }

  public void setDeviceNumber(String deviceNumber) {
    this.deviceNumber = deviceNumber;
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }

  public Integer getPictureType() {
    return pictureType;
  }

  public void setPictureType(Integer pictureType) {
    this.pictureType = pictureType;
  }

  public String getCardId() {
    return cardId;
  }

  public void setCardId(String cardId) {
    this.cardId = cardId;
  }

  public String getCardName() {
    return cardName;
  }

  public void setCardName(String cardName) {
    this.cardName = cardName;
  }

  public Integer getMaterialsId() {
    return materialsId;
  }

  public void setMaterialsId(Integer materialsId) {
    this.materialsId = materialsId;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }
}
