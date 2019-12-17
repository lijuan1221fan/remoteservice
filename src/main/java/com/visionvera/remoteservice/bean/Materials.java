package com.visionvera.remoteservice.bean;

import com.visionvera.app.entity.AppointmentMaterials;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class Materials {

  /**
   * *
   */
  private Integer id;

  /**
   * * 材料名称
   */
  @Length(min = 1, max = 16, groups = {AddGroup.class,
      UpdateGroup.class}, message = "length长度在[1,16]之间")
  @NotEmpty(groups = {AddGroup.class, UpdateGroup.class}, message = "名称不能为空")
  private String materialsName;

  /**
   * * 服务中心key
   */
  private String serviceKey;

  /**
   * * 部门id
   */
  private Integer deptId;

  /**
   * * 1:申请单 2：附加申请材料 3：申请材料 4：业务确认书
   */
  private Integer materialsType;

  /**
   * * 是否上传 0：不上传 1：上传
   */
  private Integer isUpload;

  /**
   * * 文件路径
   */
  private String filePath;

  /**
   * * 创建日期
   */
  private Date createTime;

  /**
   * * 修改日期
   */
  private Date updateTime;

  /**
   * * 版本号
   */
  private Integer materialsVersion;

  /**
   * * 状态 1:有效 0:无效 -1:禁用
   */
  private Integer state;

  /**
   * * 业务
   */
  private List<BusinessTypeBean> businessTypeList;

  /**
   * 部门名称
   */
  private String deptName;

  /**
   * 中心名称
   */
  private String serviceName;

  /**
   * 当为审批管理员时，统筹中心的数据不能操作
   */
  private boolean show= true;

  /**
   * 预约材料图片地址
   */
  private  List<AppointmentMaterials> filePaths;

  public List<AppointmentMaterials> getFilePaths() {
    return filePaths;
  }

  public void setFilePaths(List<AppointmentMaterials> filePaths) {
    this.filePaths = filePaths;
  }

  public Materials() {
  }

  public Materials(String materialsName, String serviceKey, Integer deptId, Integer materialsType,
      Integer isUpload, String filePath) {
    this.materialsName = materialsName;
    this.serviceKey = serviceKey;
    this.deptId = deptId;
    this.materialsType = materialsType;
    this.isUpload = isUpload;
    this.filePath = filePath;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public List<BusinessTypeBean> getBusinessTypeList() {
    return businessTypeList;
  }

  public void setBusinessTypeList(List<BusinessTypeBean> businessTypeList) {
    this.businessTypeList = businessTypeList;
  }

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
   * 材料名称
   *
   * @return materials_name 材料名称
   */
  public String getMaterialsName() {
    return materialsName;
  }

  /**
   * 材料名称
   *
   * @param materialsName 材料名称
   */
  public void setMaterialsName(String materialsName) {
    this.materialsName = materialsName == null ? null : materialsName.trim();
  }

  /**
   * 服务中心key
   *
   * @return service_key 服务中心key
   */
  public String getServiceKey() {
    return serviceKey;
  }

  /**
   * 服务中心key
   *
   * @param serviceKey 服务中心key
   */
  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey == null ? null : serviceKey.trim();
  }

  /**
   * 部门id
   *
   * @return dept_id 部门id
   */
  public Integer getDeptId() {
    return deptId;
  }

  /**
   * 部门id
   *
   * @param deptId 部门id
   */
  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  /**
   * 1:申请单 2：附加申请材料 3：申请材料 4：业务确认书
   *
   * @return materials_type 1:申请单 2：附加申请材料 3：申请材料 4：业务确认书
   */
  public Integer getMaterialsType() {
    return materialsType;
  }

  /**
   * 1:申请单 2：附加申请材料 3：申请材料 4：业务确认书
   *
   * @param materialsType 1:申请单 2：附加申请材料 3：申请材料 4：业务确认书
   */
  public void setMaterialsType(Integer materialsType) {
    this.materialsType = materialsType;
  }

  /**
   * 是否上传 0：不上传 1：上传
   *
   * @return is_upload 是否上传 0：不上传 1：上传
   */
  public Integer getIsUpload() {
    return isUpload;
  }

  /**
   * 是否上传 0：不上传 1：上传
   *
   * @param isUpload 是否上传 0：不上传 1：上传
   */
  public void setIsUpload(Integer isUpload) {
    this.isUpload = isUpload;
  }

  /**
   * 文件路径
   *
   * @return file_path 文件路径
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * 文件路径
   *
   * @param filePath 文件路径
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath == null ? null : filePath.trim();
  }

  /**
   * 创建日期
   *
   * @return create_time 创建日期
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * 创建日期
   *
   * @param createTime 创建日期
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 修改日期
   *
   * @return update_time 修改日期
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * 修改日期
   *
   * @param updateTime 修改日期
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * 版本号
   *
   * @return materials_version 版本号
   */
  public Integer getMaterialsVersion() {
    return materialsVersion;
  }

  /**
   * 版本号
   *
   * @param materialsVersion 版本号
   */
  public void setMaterialsVersion(Integer materialsVersion) {
    this.materialsVersion = materialsVersion;
  }

  /**
   * 状态 -1删除
   *
   * @return state 状态 -1删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 状态 1:有效 0:无效 -1:禁用
   *
   * @param state 状态 -1删除
   */
  public void setState(Integer state) {
    this.state = state;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public boolean isShow() {
    return show;
  }

  public void setShow(boolean show) {
    this.show = show;
  }
}
