package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.CheckGroup;
import com.visionvera.common.validator.group.EditMaterialsGroup;
import com.visionvera.common.validator.group.MaterialsByParamterGroup;
import com.visionvera.common.validator.group.QueryMaterialsGroup;
import com.visionvera.common.validator.group.RelevanceMaterialsGroup;
import com.visionvera.common.validator.group.UploadGroup;
import com.visionvera.remoteservice.bean.Materials;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ljfan
 * @ClassNameMaterialsVo 材料基类  Integer id, String materialsName, String serviceKey, Integer deptId,
 * Integer materialsType, Integer isUpload
 * @date 2018/12/26 version
 */
public class MaterialsVo extends BaseVo {

  @NotNull(groups = {EditMaterialsGroup.class}, message = "id不能为空")
  private Integer id;
  @NotNull(groups = {AddGroup.class, UploadGroup.class,
      MaterialsByParamterGroup.class, CheckGroup.class}, message = "部门ID不能为空")
  private Integer deptId;
  @NotNull(groups = {AddGroup.class, UploadGroup.class,
      QueryMaterialsGroup.class,CheckGroup.class}, message = "材料类型不能为空")
  private Integer materialsType;
  @NotNull(groups = {AddGroup.class,}, message = "是否上传判断参数不能为空")
  private Integer isUpload;
  @NotNull(groups = {AddGroup.class}, message = "是否覆盖不能为空")
  private Integer isCoverage;
  @Length(min = 1, max = 16, groups = {EditMaterialsGroup.class}, message = "名称长度在[1,16]之间")
  @NotBlank(groups = {EditMaterialsGroup.class, UploadGroup.class,CheckGroup.class}, message = "材料名称不能为空")
  private String materialsName;
  @NotEmpty(groups = {EditMaterialsGroup.class}, message = "是否重新上传判断标示不能为空")
  private MultipartFile file;
  @NotNull(groups = {RelevanceMaterialsGroup.class}, message = "材料id不能为空")
  private Integer materialId;
  @NotEmpty(groups = {RelevanceMaterialsGroup.class}, message = "类型ID不能为空")
  private String typesId;

  private String[] typeIds;
  @NotNull(groups = {EditMaterialsGroup.class}, message = "是否重新上传判断标示不能为空")
  private Integer afreshUpload;
  @NotEmpty(groups = {EditMaterialsGroup.class}, message = "材料不能为空")
  private Materials materials;
  @NotBlank(groups = {EditMaterialsGroup.class, UploadGroup.class,CheckGroup.class}, message = "请选择审批中心")
  private String serviceKey;
  private List<String> serviceKeys;
  @NotNull(groups = {QueryMaterialsGroup.class}, message = "业务详情ID不能为空")
  private Integer businessTypeId;
  //是否仅支持统筹受理:1是，0否
  private Integer isComprehensive;
  private String serviceName;
  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public Integer getMaterialsType() {
    return materialsType;
  }

  public void setMaterialsType(Integer materialsType) {
    this.materialsType = materialsType;
  }

  public Integer getIsUpload() {
    return isUpload;
  }

  public void setIsUpload(Integer isUpload) {
    this.isUpload = isUpload;
  }

  public Integer getIsCoverage() {
    return isCoverage;
  }

  public void setIsCoverage(Integer isCoverage) {
    this.isCoverage = isCoverage;
  }

  public String getMaterialsName() {
    return materialsName;
  }

  public void setMaterialsName(String materialsName) {
    this.materialsName = materialsName;
  }

  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

  public Integer getMaterialId() {
    return materialId;
  }

  public void setMaterialId(Integer materialId) {
    this.materialId = materialId;
  }

  public String getTypesId() {
    return typesId;
  }

  public void setTypesId(String typesId) {
    this.typesId = typesId;
  }

  public String[] getTypeIds() {
    return typeIds;
  }

  public void setTypeIds(String[] typeIds) {
    this.typeIds = typeIds;
  }

  public Integer getAfreshUpload() {
    return afreshUpload;
  }

  public void setAfreshUpload(Integer afreshUpload) {
    this.afreshUpload = afreshUpload;
  }

  public Materials getMaterials() {
    return materials;
  }

  public void setMaterials(Materials materials) {
    this.materials = materials;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getBusinessTypeId() {
    return businessTypeId;
  }

  public void setBusinessTypeId(Integer businessTypeId) {
    this.businessTypeId = businessTypeId;
  }

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
  }

  public Integer getIsComprehensive() {
    return isComprehensive;
  }

  public void setIsComprehensive(Integer isComprehensive) {
    this.isComprehensive = isComprehensive;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
}
