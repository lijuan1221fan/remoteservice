package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.AddBusinessDetailGroup;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.CheckGroup;
import com.visionvera.common.validator.group.DeleteGroup;
import com.visionvera.common.validator.group.UpdateBusinessDetailGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.Materials;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author ljfan
 * @ClassNameBusinessTypeVo 业务管理基类
 * @date 2018/12/24 version
 */
public class BusinessTypeVo extends BaseVo {

  /**
   * * 主键类型
   */
  @NotNull(groups = {UpdateGroup.class, UpdateBusinessDetailGroup.class}, message = "主键不能为空")
  private Integer id;

  /**
   * * 父类型
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class}, message = "业务类型id不能为空")
  private Integer parentId;

  /**
   * * 类型描述
   */
  @Length(min = 1, max = 16, groups = {AddGroup.class,
      UpdateGroup.class, AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class}, message = "名称长度在[1,16]之间")
  @NotEmpty(groups = {AddGroup.class, UpdateGroup.class, AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "名称不能为空")
  private String describes;

  /**
   * * 业务等级
   */
  private Integer grade;

  /**
   * * 是否是叶子节点  0:否 1:是
   */
  private Integer isLeaf;

  /**
   * * 版本号
   */
  private Integer version;

  /**
   * * 创建时间
   */
  private Date createTime;

  /**
   * * 修改时间
   */
  private Date updateTime;

  /**
   * * 是否需要申请单 0：不需要 1：需要
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否需要申请单，请选择")
  private Integer isForm;

  /**
   * * 是否需要附加申请单 0：不需要 1：需要
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否需要附加申请单，请选择")
  private Integer appendForm;

  /**
   * * 是否需要申请材料 0：不需要 1：需要
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否需要申请材料，请选择")
  private Integer isMaterials;

  /**
   * * 是否需要本地打印申请材料 0：不需要 1：需要
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否需要本地打印申请材料，请选择")
  private Integer materialsPrint;

  /**
   * * 是否需要电子签名 0：不需要 1：需要
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否需要电子签名，请选择")
  private Integer eSignature;

  /**
   * * 是否需要远程打印 0：不需要 1：需要
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否需要远程打印，请选择")
  private Integer remotePrint;

  /**
   * * 是否需要公安电子签名加盖打印 0：不需要 1：需要
   */
  private Integer policeSign;

  /**
   * * 是否需要公安信息确认单 0：不需要 1：需要
   */
  private Integer policeNotarize;

  /**
   * * 是否全时段  0：是  1：不是
   */
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class,}, message = "是否全时段，请选择")
  private Integer wholeTime;

  /**
   * * 业务受理月份
   */
  @NotEmpty(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class}, message = "业务受理月不能为空")
  private String businessMonth;

  /**
   * * 业务受理日
   */
  @NotEmpty(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class}, message = "业务受理日不能为空")
  private String businessDay;

  /**
   * * 材料
   */
  private List<Materials> materialsList;

  /**
   * 部门名称
   */
  private String deptName;

  /**
   * 工作流程
   */
  private String businessFlow;

  /**
   * 父类型描述
   */
  private String parentDescribes;

  /**
   * 业务类型： 0 ：普通   1：默认
   */
  private Integer type;
  /**
   * * 业务
   */
  private List<BusinessTypeBean> BusinessTypeBeanList;
  private Integer isRests;
  @NotEmpty(groups = {CheckGroup.class},message = "请选择中心")
  private String serviceKey;
  private List<String> serviceKeys;
  private String serviceName;
  private Integer state;
  @NotNull(groups = {AddBusinessDetailGroup.class,
      UpdateBusinessDetailGroup.class, CheckGroup.class}, message = "部门ID不能为空")
  private Integer deptId;
  private String materialsIds;
  @NotNull(groups = {DeleteGroup.class, UpdateGroup.class}, message = "id不能为空")
  private Integer[] ids;
  @NotNull(groups = {DeleteGroup.class}, message = "删除标示不能为空")
  private Integer isDel;
  //取号机判断有业务详情的业务类别校验标示
  private Integer offerNumberCheck;
  //安卓叫号模式：01，一窗多办，02 多部门
  private String showType;
  // 是否自定义签名盖章  1.后台配置  2.自由拖拽
  private Integer isCustom;
  // 是否仅支持统筹受理:1是，0否
  private Integer isComprehensive;
  //回执单模式：1:查看本地回执单，2单独上传电子签名模式，3无需提供业务回执
  @NotNull(groups = {AddBusinessDetailGroup.class, UpdateBusinessDetailGroup.class,}, message = "请选择回执信息查看模式")
  private Integer supReceipt;
  private Integer stampX;
  private Integer stampY;
  private Integer signX;
  private Integer signY;

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
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

  public Integer getSignX() {
    return signX;
  }

  public void setSignX(Integer signX) {
    this.signX = signX;
  }

  public Integer getSignY() {
    return signY;
  }

  public void setSignY(Integer signY) {
    this.signY = signY;
  }

  public Integer getIsCustom() {
    return isCustom;
  }

  public void setIsCustom(Integer isCustom) {
    this.isCustom = isCustom;
  }

  public String getShowType() {
    return showType;
  }

  public void setShowType(String showType) {
    this.showType = showType;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public String getDescribes() {
    return describes;
  }

  public void setDescribes(String describes) {
    this.describes = describes;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public Integer getIsLeaf() {
    return isLeaf;
  }

  public void setIsLeaf(Integer isLeaf) {
    this.isLeaf = isLeaf;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getIsForm() {
    return isForm;
  }

  public void setIsForm(Integer isForm) {
    this.isForm = isForm;
  }

  public Integer getAppendForm() {
    return appendForm;
  }

  public void setAppendForm(Integer appendForm) {
    this.appendForm = appendForm;
  }

  public Integer getIsMaterials() {
    return isMaterials;
  }

  public void setIsMaterials(Integer isMaterials) {
    this.isMaterials = isMaterials;
  }

  public Integer getMaterialsPrint() {
    return materialsPrint;
  }

  public void setMaterialsPrint(Integer materialsPrint) {
    this.materialsPrint = materialsPrint;
  }

  public Integer geteSignature() {
    return eSignature;
  }

  public void seteSignature(Integer eSignature) {
    this.eSignature = eSignature;
  }

  public Integer getRemotePrint() {
    return remotePrint;
  }

  public void setRemotePrint(Integer remotePrint) {
    this.remotePrint = remotePrint;
  }

  public Integer getPoliceSign() {
    return policeSign;
  }

  public void setPoliceSign(Integer policeSign) {
    this.policeSign = policeSign;
  }

  public Integer getPoliceNotarize() {
    return policeNotarize;
  }

  public void setPoliceNotarize(Integer policeNotarize) {
    this.policeNotarize = policeNotarize;
  }

  public Integer getWholeTime() {
    return wholeTime;
  }

  public void setWholeTime(Integer wholeTime) {
    this.wholeTime = wholeTime;
  }

  public String getBusinessMonth() {
    return businessMonth;
  }

  public void setBusinessMonth(String businessMonth) {
    this.businessMonth = businessMonth;
  }

  public String getBusinessDay() {
    return businessDay;
  }

  public void setBusinessDay(String businessDay) {
    this.businessDay = businessDay;
  }

  public List<Materials> getMaterialsList() {
    return materialsList;
  }

  public void setMaterialsList(List<Materials> materialsList) {
    this.materialsList = materialsList;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getBusinessFlow() {
    return businessFlow;
  }

  public void setBusinessFlow(String businessFlow) {
    this.businessFlow = businessFlow;
  }

  public String getParentDescribes() {
    return parentDescribes;
  }

  public void setParentDescribes(String parentDescribes) {
    this.parentDescribes = parentDescribes;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public List<BusinessTypeBean> getBusinessTypeBeanList() {
    return BusinessTypeBeanList;
  }

  public void setBusinessTypeBeanList(
      List<BusinessTypeBean> businessTypeBeanList) {
    BusinessTypeBeanList = businessTypeBeanList;
  }

  public Integer getIsRests() {
    return isRests;
  }

  public void setIsRests(Integer isRests) {
    this.isRests = isRests;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public String getMaterialsIds() {
    return materialsIds;
  }

  public void setMaterialsIds(String materialsIds) {
    this.materialsIds = materialsIds;
  }

  public Integer[] getIds() {
    return ids;
  }

  public void setIds(Integer[] ids) {
    this.ids = ids;
  }

  public Integer getIsDel() {
    return isDel;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public Integer getOfferNumberCheck() {
    return offerNumberCheck;
  }

  public void setOfferNumberCheck(Integer offerNumberCheck) {
    this.offerNumberCheck = offerNumberCheck;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public Integer getSupReceipt() {
    return supReceipt;
  }

  public void setSupReceipt(Integer supReceipt) {
    this.supReceipt = supReceipt;
  }

  public Integer getIsComprehensive() {
    return isComprehensive;
  }

  public void setIsComprehensive(Integer isComprehensive) {
    this.isComprehensive = isComprehensive;
  }
}
