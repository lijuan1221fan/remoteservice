package com.visionvera.remoteservice.bean;

import java.io.Serializable;

/**
 * @author lijintao
 */
public class PhotoConfig implements Serializable {

  private static final long serialVersionUID = -6615308065061972104L;
  /**
   * *
   */
  private Integer id;

  /**
   * * 业务类型
   */
  private Integer businessType;

  /**
   * * 类型 参照t_common_config   photo_config value
   */
  private Integer type;

  /**
   * * x坐标
   */
  private Integer x;

  /**
   * * y坐标
   */
  private Integer y;

  /**
   * * 宽度
   */
  private Integer width;

  /**
   * * 高度
   */
  private Integer height;

  /**
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 业务key
   *
   * @return business_type 业务key
   */
  public Integer getBusinessType() {
    return businessType;
  }

  /**
   * 业务key
   *
   * @param businessType 业务key
   */
  public void setBusinessType(Integer businessType) {
    this.businessType = businessType;
  }

  /**
   * 类型 参照t_common_config   photo_config value
   *
   * @return type 类型 参照t_common_config   photo_config value
   */
  public Integer getType() {
    return type;
  }

  /**
   * 类型 参照t_common_config   photo_config value
   *
   * @param type 类型 参照t_common_config   photo_config value
   */
  public void setType(Integer type) {
    this.type = type;
  }

  /**
   * x坐标
   *
   * @return x x坐标
   */
  public Integer getX() {
    return x;
  }

  /**
   * x坐标
   *
   * @param x x坐标
   */
  public void setX(Integer x) {
    this.x = x;
  }

  /**
   * y坐标
   *
   * @return y y坐标
   */
  public Integer getY() {
    return y;
  }

  /**
   * y坐标
   *
   * @param y y坐标
   */
  public void setY(Integer y) {
    this.y = y;
  }

  /**
   * 宽度
   *
   * @return width 宽度
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * 宽度
   *
   * @param width 宽度
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * 高度
   *
   * @return height 高度
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * 高度
   *
   * @param height 高度
   */
  public void setHeight(Integer height) {
    this.height = height;
  }

}
