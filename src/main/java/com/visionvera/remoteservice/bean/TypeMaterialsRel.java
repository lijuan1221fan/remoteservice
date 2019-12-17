package com.visionvera.remoteservice.bean;

/**
 * @Auther: quboka
 * @Date: 2018/8/27 16:03
 * @Description:
 */
public class TypeMaterialsRel {

  /**
   * 业务类型id
   */
  private Integer TypeId;

  /**
   * 材料id
   */
  private Integer materialsId;

  public Integer getTypeId() {
    return TypeId;
  }

  public void setTypeId(Integer typeId) {
    TypeId = typeId;
  }

  public Integer getMaterialsId() {
    return materialsId;
  }

  public void setMaterialsId(Integer materialsId) {
    this.materialsId = materialsId;
  }
}
