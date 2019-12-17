package com.visionvera.remoteservice.bean;

import com.visionvera.common.validator.group.AddBusinessDetailGroup;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.ChangeGroup;
import com.visionvera.common.validator.group.CheckGroup;
import com.visionvera.common.validator.group.UpdateBusinessDetailGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;

/**
 * @author lijintao
 */
public class  BusinessTypeBean {

  /**
   * * 主键类型
   */
  @NotNull(groups = {UpdateGroup.class, CheckGroup.class, UpdateBusinessDetailGroup.class,
      ChangeGroup.class}, message = "主键不能为空")
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
      UpdateGroup.class, UpdateBusinessDetailGroup.class}, message = "length长度在[1,16]之间")
  @NotEmpty(groups = {AddGroup.class, AddBusinessDetailGroup.class, UpdateGroup.class,
      UpdateBusinessDetailGroup.class}, message = "名称不能为空")
  private String describes;

  /**
   * * 部门id
   */
  @NotNull(groups = {AddGroup.class, AddBusinessDetailGroup.class, UpdateGroup.class,
      UpdateBusinessDetailGroup.class}, message = "部门ID不能为空")
  private Integer deptId;

  /**
   * * 服务中心key
   */
  @NotEmpty(groups = {AddGroup.class, UpdateGroup.class}, message = "请选择中心")
  private String serviceKey;

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
  //@NotNull(groups = {UpdateBusinessDetailGroup.class, ChangeGroup.class}, message = "版本号不能为空")
  private Integer version;

  /**
   * * 状态 1:有效 0:无效 -1:禁用
   */
  @NotNull(groups = {UpdateBusinessDetailGroup.class, ChangeGroup.class}, message = "状态不能为空")
  private Integer state;

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
  private Integer isForm;

  /**
   * * 是否需要附加申请单 0：不需要 1：需要
   */
  private Integer appendForm;

  /**
   * * 是否需要申请材料 0：不需要 1：需要
   */
  private Integer isMaterials;

  /**
   * * 是否需要本地打印申请材料 0：不需要 1：需要
   */
  private Integer materialsPrint;

  /**
   * * 是否需要电子签名 0：不需要 1：需要
   */
  private Integer eSignature;

  /**
   * * 是否需要远程打印 0：不需要 1：需要
   */
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
   * 工作流程1 身份授权、2 材料收集、3 业务确认
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
  /**
   * 签名盖章位置列表
   */
  private List<PhotoConfig> photoConfigs;
  private Integer isRests;
  //取号机判断有业务详情的业务类别校验标示
  //当为审批管理员时，统筹和admin管理员所建当业务类别只能查看 show=false;
  private boolean show = true;
  private Integer offerNumberCheck;
  private String serviceName;
  //多中心
  private List<String> serviceKeys;
  //上午限制数
  private Integer morningLimitNumber;
  //下午限制数
  private Integer afternoonLimitNumber;
  // 是否自定义签名盖章  1.自定义  2.配置
  private Integer isCustom;
  //回执单模式：1:查看本地回执单，2单独上传电子签名模式，3无需提供业务回执
  private Integer supReceipt;
  // 是否仅支持统筹受理:1是，0否
  private Integer isComprehensive;
  private Integer stampX;
  private Integer stampY;
  private Integer signX;
  private Integer signY;

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

  public List<PhotoConfig> getPhotoConfigs() {
    return photoConfigs;
  }

  public void setPhotoConfigs(List<PhotoConfig> photoConfigs) {
    this.photoConfigs = photoConfigs;
  }

  public Integer getIsCustom() {
    return isCustom;
  }

  public void setIsCustom(Integer isCustom) {
    this.isCustom = isCustom;
  }

  public Integer getMorningLimitNumber() {
    return morningLimitNumber;
  }

  public void setMorningLimitNumber(Integer morningLimitNumber) {
    this.morningLimitNumber = morningLimitNumber;
  }

  public Integer getAfternoonLimitNumber() {
    return afternoonLimitNumber;
  }

  public void setAfternoonLimitNumber(Integer afternoonLimitNumber) {
    this.afternoonLimitNumber = afternoonLimitNumber;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getParentDescribes() {
    return parentDescribes;
  }

  public void setParentDescribes(String parentDescribes) {
    this.parentDescribes = parentDescribes;
  }

  public String getBusinessFlow() {
    return businessFlow;
  }

  public void setBusinessFlow(String businessFlow) {
    this.businessFlow = businessFlow;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public List<Materials> getMaterialsList() {
    return materialsList;
  }

  public void setMaterialsList(List<Materials> materialsList) {
    this.materialsList = materialsList;
  }

  public List<BusinessTypeBean> getBusinessTypeBeanList() {
    return BusinessTypeBeanList;
  }

  public void setBusinessTypeBeanList(List<BusinessTypeBean> businessTypeBeanList) {
    BusinessTypeBeanList = businessTypeBeanList;
  }

  /**
   * m 主键类型
   *
   * @return id 主键类型
   */
  public Integer getId() {
    return id;
  }

  /**
   * 主键类型
   *
   * @param id 主键类型
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 父类型
   *
   * @return parent_id 父类型
   */
  public Integer getParentId() {
    return parentId;
  }

  /**
   * 父类型
   *
   * @param parentId 父类型
   */
  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  /**
   * 类型描述
   *
   * @return describes 类型描述
   */
  public String getDescribes() {
    return describes;
  }

  /**
   * 类型描述
   *
   * @param describes 类型描述
   */
  public void setDescribes(String describes) {
    this.describes = describes == null ? null : describes.trim();
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
   * 业务等级
   *
   * @return grade 业务等级
   */
  public Integer getGrade() {
    return grade;
  }

  /**
   * 业务等级
   *
   * @param grade 业务等级
   */
  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  /**
     * 是否是叶子节点  0:否 1:是
   *
   * @return is_leaf 是否是叶子节点  0:否 1:是
   */
  public Integer getIsLeaf() {
    return isLeaf;
  }

  /**
   * 是否是叶子节点  0:否 1:是
   *
   * @param isLeaf 是否是叶子节点  0:否 1:是
   */
  public void setIsLeaf(Integer isLeaf) {
    this.isLeaf = isLeaf;
  }

  /**
   * 版本号
   *
   * @return version 版本号
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * 版本号
   *
   * @param version 版本号
   */
  public void setVersion(Integer version) {
    this.version = version;
  }

  /**
   * 状态 0：删除 -1禁用 1 有效
   *
   * @return state 状态 -2：删除 -1禁用
   */
  public Integer getState() {
    return state;
  }

  /**
   * 状态 -2：删除 -1禁用
   *
   * @param state 状态 -2：删除 -1禁用
   */
  public void setState(Integer state) {
    this.state = state;
  }

  /**
   * 创建时间
   *
   * @return create_time 创建时间
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * 创建时间
   *
   * @param createTime 创建时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 修改时间
   *
   * @return update_time 修改时间
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * 修改时间
   *
   * @param updateTime 修改时间
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * 是否需要申请单 0：不需要 1：需要
   *
   * @return is_form 是否需要申请单 0：不需要 1：需要
   */
  public Integer getIsForm() {
    return isForm;
  }

  /**
   * 是否需要申请单 0：不需要 1：需要
   *
   * @param isForm 是否需要申请单 0：不需要 1：需要
   */
  public void setIsForm(Integer isForm) {
    this.isForm = isForm;
  }

  /**
   * 是否需要附加申请单 0：不需要 1：需要
   *
   * @return append_form 是否需要附加申请单 0：不需要 1：需要
   */
  public Integer getAppendForm() {
    return appendForm;
  }

  /**
   * 是否需要附加申请单 0：不需要 1：需要
   *
   * @param appendForm 是否需要附加申请单 0：不需要 1：需要
   */
  public void setAppendForm(Integer appendForm) {
    this.appendForm = appendForm;
  }

  /**
   * 是否需要申请材料 0：不需要 1：需要
   *
   * @return is_materials 是否需要申请材料 0：不需要 1：需要
   */
  public Integer getIsMaterials() {
    return isMaterials;
  }

  /**
   * 是否需要申请材料 0：不需要 1：需要
   *
   * @param isMaterials 是否需要申请材料 0：不需要 1：需要
   */
  public void setIsMaterials(Integer isMaterials) {
    this.isMaterials = isMaterials;
  }

  /**
   * 是否需要本地打印申请材料 0：不需要 1：需要
   *
   * @return materials_print 是否需要本地打印申请材料 0：不需要 1：需要
   */
  public Integer getMaterialsPrint() {
    return materialsPrint;
  }


  /**
   * 是否需要本地打印申请材料 0：不需要 1：需要
   *
   * @param materialsPrint 是否需要本地打印申请材料 0：不需要 1：需要
   */
  public void setMaterialsPrint(Integer materialsPrint) {
    this.materialsPrint = materialsPrint;
  }

  /**
   * 是否需要电子签名 0：不需要 1：需要
   *
   * @return e_signature 是否需要电子签名 0：不需要 1：需要
   */
  public Integer geteSignature() {
    return eSignature;
  }

  /**
   * 是否需要电子签名 0：不需要 1：需要
   *
   * @param eSignature 是否需要电子签名 0：不需要 1：需要
   */
  public void seteSignature(Integer eSignature) {
    this.eSignature = eSignature;
  }

  /**
   * 是否需要远程打印 0：不需要 1：需要
   *
   * @return remote_print 是否需要远程打印 0：不需要 1：需要
   */
  public Integer getRemotePrint() {
    return remotePrint;
  }

  /**
   * 是否需要远程打印 0：不需要 1：需要
   *
   * @param remotePrint 是否需要远程打印 0：不需要 1：需要   */
  public void setRemotePrint(Integer remotePrint) {
    this.remotePrint = remotePrint;
  }

  /**
   * 是否需要公安电子签名加盖打印 0：不需要 1：需要
   *
   * @return police_sign 是否需要公安电子签名加盖打印 0：不需要 1：需要
   */
  public Integer getPoliceSign() {
    return policeSign;
  }

  /**
   * 是否需要公安电子签名加盖打印 0：不需要 1：需要
   *
   * @param policeSign 是否需要公安电子签名加盖打印 0：不需要 1：需要
   */
  public void setPoliceSign(Integer policeSign) {
    this.policeSign = policeSign;
  }

  /**
   * 是否需要公安信息确认单 0：不需要 1：需要
   *
   * @return police_notarize 是否需要公安信息确认单 0：不需要 1：需要
   */
  public Integer getPoliceNotarize() {
    return policeNotarize;
  }

  /**
   * 是否需要公安信息确认单 0：不需要 1：需要
   *
   * @param policeNotarize 是否需要公安信息确认单 0：不需要 1：需要
   */
  public void setPoliceNotarize(Integer policeNotarize) {
    this.policeNotarize = policeNotarize;
  }

  /**
   * 是否全时段  0：是  1：不是
   *
   * @return whole_time 是否全时段  0：是  1：不是
   */
  public Integer getWholeTime() {
    return wholeTime;
  }

  /**
   * 是否全时段  0：是  1：不是
   *
   * @param wholeTime 是否全时段  0：是  1：不是
   */
  public void setWholeTime(Integer wholeTime) {
    this.wholeTime = wholeTime;
  }

  /**
   * 业务受理月份
   *
   * @return business_month 业务受理月份
   */
  public String getBusinessMonth() {
    return businessMonth;
  }

  /**
   * 业务受理月份
   *
   * @param businessMonth 业务受理月份
   */
  public void setBusinessMonth(String businessMonth) {
    this.businessMonth = removesamestring(businessMonth);
  }

  /**
   * 业务受理日
   *
   * @return business_day 业务受理日
   */
  public String getBusinessDay() {
    return businessDay;
  }

  /**
   * 业务受理日
   *
   * @param businessDay 业务受理日
   */
  public void setBusinessDay(String businessDay) {
    this.businessDay = removesamestring(businessDay);
  }

  private static String removesamestring(String str) {
    Set<String> mlinkedset = new LinkedHashSet<String>();
    String[] strarray = str.split(",");
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strarray.length; i++) {
      if (!mlinkedset.contains(strarray[i])) {
        mlinkedset.add(strarray[i]);
        sb.append(strarray[i] + ",");
      }
    }
    return sb.toString().substring(0, sb.toString().length() - 1);
  }

  public Integer getIsRests() {
    return isRests;
  }

  public void setIsRests(Integer isRests) {
    this.isRests = isRests;
  }

  public Integer getOfferNumberCheck() {
    return offerNumberCheck;
  }

  public void setOfferNumberCheck(Integer offerNumberCheck) {
    this.offerNumberCheck = offerNumberCheck;
  }

  public boolean isShow() {
    return show;
  }

  public void setShow(boolean show) {
    this.show = show;
  }

  public List<String> getServiceKeys() {
    return serviceKeys;
  }

  public void setServiceKeys(List<String> serviceKeys) {
    this.serviceKeys = serviceKeys;
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
