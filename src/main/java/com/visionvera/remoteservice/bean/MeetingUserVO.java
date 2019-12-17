/**
 * @Title: UserVO.java
 * @Package com.visionvera.cms.bean
 * @Description: TODO(用一句话描述该文件做什么)
 * @author zhaolei
 * @date 2016年8月12日 下午5:26:56
 * @version V1.0
 */
package com.visionvera.remoteservice.bean;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: MeetingUserVO
 * @Description: 会议管理用户查询VO
 * @author wangruizhi
 * @date 2016年8月12日 下午5:26:56
 *
 */
@XmlRootElement(name = "items")
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class MeetingUserVO {

  private String uuid;        //用户uuid
  private String name;        //用户名称
  private String loginName;      //登录名称
  private String loginPwd;      //登录密码
  private String role;        //用户角色
  private String roleId;        //用户角色ID
  private String phone;          //用户手机
  private String description;      //备注
  private Integer status;        //用户状态 0不在线 1在线
  private String groupId;          //用户组id
  private String groupName;      //用户组名
  private String operatorId;          //操作人ID（webservice调用时须传递该参数）
  private String orgId;               //工作单位编号
  private String orgName;             //工作单位名称
  private String areaId;              //所在区域编号
  private String areaName;            //所在区域名称
  private String departId;            //部门名称
  private String departName;          //所在部门名称
  private String oldPwd;              //旧密码   应用于重置密码接口
  private String newPwd;              //新密码   应用于重置密码接口
  private Integer isvalid;            //webservice专用，判断注册的用户是否生效（0无效，1有效）
  private Integer editType;           //用户修改操作的类型
  private Integer isWsLogin;          //webservice专用，是否从webservice登录过。0未登录过，1登录过
  private String imgUrl;              //用户头像url
  private String devId;               //终端号码，多个值用,分割
  private String postName;            //岗位
  private String rankName;            //职级
  private String idCard;              //身份证号
  private Integer webLogin;           //是否允许登录会管 0-不允许，1-允许
  private Integer infoOk;             //用户信息是否合法（终端号是否正确） 0-不合法，1-合法
  private String createTime;      //申请时间
  public MeetingUserVO() {
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  //
//	private List<Function> functions = new ArrayList<Function>();
//	
//	public List<Function> getFunctions() {
//		return functions;
//	}
//	public void setFunctions(List<Function> functions) {
//		this.functions = functions;
//	}
  public String getDevId() {
    return devId;
  }

  public void setDevId(String devId) {
    this.devId = devId;
  }

  public String getPostName() {
    return postName;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }

  public String getRankName() {
    return rankName;
  }

  public void setRankName(String rankName) {
    this.rankName = rankName;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public Integer getWebLogin() {
    return webLogin;
  }

  public void setWebLogin(Integer webLogin) {
    this.webLogin = webLogin;
  }

  public String getNewPwd() {
    return newPwd;
  }

  public void setNewPwd(String newPwd) {
    this.newPwd = newPwd;
  }

  public String getOldPwd() {
    return oldPwd;
  }

  public void setOldPwd(String oldPwd) {
    this.oldPwd = oldPwd;
  }

  public String getDepartName() {
    return departName;
  }

  public void setDepartName(String departName) {
    this.departName = departName;
  }

  public String getAreaId() {
    return areaId;
  }

  public void setAreaId(String areaId) {
    this.areaId = areaId;
  }

  public String getDepartId() {
    return departId;
  }

  public void setDepartId(String departId) {
    this.departId = departId;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getLoginPwd() {
    return loginPwd;
  }

  public void setLoginPwd(String loginPwd) {
    this.loginPwd = loginPwd;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public Integer getIsvalid() {
    return isvalid;
  }

  public void setIsvalid(Integer isvalid) {
    this.isvalid = isvalid;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public Integer getIsWsLogin() {
    return isWsLogin;
  }

  public void setIsWsLogin(Integer isWsLogin) {
    this.isWsLogin = isWsLogin;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public Integer getEditType() {
    return editType;
  }

  public void setEditType(Integer editType) {
    this.editType = editType;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public Integer getInfoOk() {
    return infoOk;
  }

  public void setInfoOk(Integer infoOk) {
    this.infoOk = infoOk;
  }

  @Override
  public String toString() {
    return "UserVO [uuid=" + uuid + ", name=" + name + ", loginName="
        + loginName + ", loginPwd=" + loginPwd + ", role=" + role
        + ", roleId=" + roleId + ", phone=" + phone + ", description="
        + description + ", status=" + status + ", groupId=" + groupId
        + ", groupName=" + groupName + ", operatorId=" + operatorId
        + ", orgId=" + orgId + ", orgName=" + orgName + ", areaId="
        + areaId + ", areaName=" + areaName + ", departId=" + departId
        + ", departName=" + departName + ", oldPwd=" + oldPwd
        + ", newPwd=" + newPwd + ", isvalid=" + isvalid + ", editType="
        + editType + ", isWsLogin=" + isWsLogin + ", imgUrl=" + imgUrl
        + ", devId=" + devId + ", postName=" + postName + ", rankName="
        + rankName + ", idCard=" + idCard + ", webLogin=" + webLogin
        + ", infoOk=" + infoOk + ", createTime=" + createTime + "]";
  }

}
