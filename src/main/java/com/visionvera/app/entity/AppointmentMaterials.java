package com.visionvera.app.entity;

import java.sql.Timestamp;

public class AppointmentMaterials {

  /**
   *主键
   */
  private Integer id;
  /**
   * 预约id
   */
  private Integer appointmentId;

  /**
   * * 材料名称
   */
  private String materialsName;
  /**
   * * 1:申请单 2：附加申请材料 3：申请材料 4：业务确认书
   */
  private Integer materialsType;
  /**
   * * 文件路径
   */
  private String filePath;
  /**
   * * 状态 1有效 0删除
   */
  private Integer state;
  /**
   * * 创建日期
   */
  private Timestamp createTime;
  /**
   *  t_storage_res 主键id
   */
  private Integer fileId;
  /**
   * t_materials表主键
   */
  private Integer materialsId;

  public AppointmentMaterials(Integer appointmentId, String materialsName, Integer materialsType, String filePath, Integer fileId, Integer materialsId) {
    this.appointmentId = appointmentId;
    this.materialsName = materialsName;
    this.materialsType = materialsType;
    this.filePath = filePath;
    this.fileId = fileId;
    this.materialsId = materialsId;
  }

  public AppointmentMaterials(String materialsName,Integer id, Integer materialsType, String filePath) {
    this.materialsName = materialsName;
    this.appointmentId = id;
    this.materialsType = materialsType;
    this.filePath = filePath;
  }
  public AppointmentMaterials(Integer id, String materialsName, Integer materialsType) {
    this.id = id;
    this.materialsName = materialsName;
    this.materialsType = materialsType;
  }
  public AppointmentMaterials(Integer id,String filePath) {
    this.id = id;
    this.filePath = filePath;
  }


  public AppointmentMaterials(Integer id, Integer appointmentId, String materialsName, Integer materialsType, String filePath, Integer state, Timestamp createTime, Integer fileId) {
    this.id = id;
    this.appointmentId = appointmentId;
    this.materialsName = materialsName;
    this.materialsType = materialsType;
    this.filePath = filePath;
    this.state = state;
    this.createTime = createTime;
    this.fileId = fileId;
  }

  public AppointmentMaterials(Integer id, Integer appointmentId, String materialsName, Integer materialsType, String filePath,Integer materialsId,Integer fileId) {
    this.id = id;
    this.appointmentId = appointmentId;
    this.materialsName = materialsName;
    this.materialsType = materialsType;
    this.filePath = filePath;
    this.materialsId = materialsId;
    this.fileId = fileId;
  }

  public AppointmentMaterials(Integer appointmentId,Integer materialsId) {
    this.appointmentId = appointmentId;
    this.materialsId = materialsId;
  }
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Integer appointmentId) {
    this.appointmentId = appointmentId;
  }

  public String getMaterialsName() {
    return materialsName;
  }

  public void setMaterialsName(String materialsName) {
    this.materialsName = materialsName;
  }

  public Integer getMaterialsType() {
    return materialsType;
  }

  public void setMaterialsType(Integer materialsType) {
    this.materialsType = materialsType;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public Integer getFileId() {
    return fileId;
  }

  public void setFileId(Integer fileId) {
    this.fileId = fileId;
  }

  public Integer getMaterialsId() {
    return materialsId;
  }

  public void setMaterialsId(Integer materialsId) {
    this.materialsId = materialsId;
  }

  @Override
  public String toString() {
    return "AppointmentMaterials{" +
            "id=" + id +
            ", appointmentId=" + appointmentId +
            ", materialsName='" + materialsName + '\'' +
            ", materialsType=" + materialsType +
            ", filePath='" + filePath + '\'' +
            ", state=" + state +
            ", createTime=" + createTime +
            ", fileId=" + fileId +
            ", materialsId=" + materialsId +
            '}';
  }
}
