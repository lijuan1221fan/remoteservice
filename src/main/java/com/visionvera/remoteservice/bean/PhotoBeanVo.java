package com.visionvera.remoteservice.bean;


import org.apache.commons.lang3.StringUtils;


/**
 * 图片页面展示vo
 *
 * @author lijintao
 */
public class PhotoBeanVo extends PhotoBean {

  private static final long serialVersionUID = 8794160079552223497L;
  /**
   * 大图的地址
   */
  private String framePath;
  /**
   * 缩略图地址
   */
  private String iconPath;

  public PhotoBeanVo() {
  }

  @Override
  public String getFramePath() {
    return parsePath("frame_path", this.framePath);
  }

  @Override
  public void setFramePath(String framepath) {
    this.framePath = framepath;
  }

  @Override
  public String getIconPath() {
    return parsePath("icon_path", this.iconPath);
  }

  @Override
  public void setIconPath(String iconpath) {
    this.iconPath = iconpath;
  }

  private String parsePath(String field, String path) {
    if (StringUtils.isNotEmpty(field) && StringUtils.isNotEmpty(path)) {
      return "storage/" + this.getId() + "/" + field + "/img.jpg";
    }
    return "";
  }
}
