package com.visionvera.remoteservice.bean;

import java.io.Serializable;

/**
 * 打印实体类
 *
 * @author Administrator
 * @date 2018年08月31日 13:51
 */
public class Print implements Serializable {

  private static final long serialVersionUID = 4569724157793400171L;
  private String printName;
  private String filePath;

  public Print(String printName, String filePath) {
    this.printName = printName;
    this.filePath = filePath;
  }

  public String getPrintName() {
    return printName;
  }

  public void setPrintName(String printName) {
    this.printName = printName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}
