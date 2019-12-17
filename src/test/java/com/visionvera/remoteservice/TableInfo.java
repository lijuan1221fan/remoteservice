package com.visionvera.remoteservice;
/**
 * @ClassNameTableInfo
 *description TODO
 * @author ljfan
 * @date 2019/03/26
 *version
 */
public class TableInfo {
  /**
   * info.setComment(resultSet.getString("Comment"));
   *       info.setFiled(resultSet.getString("Field"));
   *       info.setKey(resultSet.getString("Key"));
   *       info.setType
   */
  private String comment;
  private String filed;
  private String key;
  private String type;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getFiled() {
    return filed;
  }

  public void setFiled(String filed) {
    this.filed = filed;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
