package com.visionvera.remoteservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visionvera.remoteservice.util.StringUtil;

/**
 * @author xiechs
 * @ClassName: DeviceVO
 * @Description: 会管终端表
 * @date 2016年9月14日
 */
// @XmlRootElement(name = "items")
// @XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class VcDevBean {

  private String id;          //设备视联号码（主键）
  private Integer msgId;        //主消息号（协转服务器号）
  private Integer monitorId;      //监控ID
  private Integer channelId;      //通道号
  private String name;        //设备名称
  private String mac;          //设备MAC
  private String ip;          //设备IP
  private String type;        //设备类型
  private Integer typeId;        //设备类型ID
  private String description;      //备注
  private String groupId;        //设备分组ID
  private String groupName;      //设备分组名
  private String alias;          //设备别名
  private Integer roleId;          //设备角色ID
  private String pX;              //gis x坐标值
  private String pY;              //gis y坐标值
  private String pZ;              //gis z坐标值
  @JsonProperty("pLayer")
  private String pLayer;          //gis 层级
  private String svrid;          //设备所属服务器ID
  private String updatetime;      //设备信息更新时间
  private String regionId;      //设备所属行政节点ID
  private int no;
  private Integer streamType;         //大小流，1大流，0小流，默认大流
  private Integer status;          //设备会议中的状态 0未在会议中 1在会议中
  private String address;             //设备地址
  private Integer dataType;           //数据属性：1默认，2私有
  private Integer devFunc;            //设备作用：0普通参会方，1主席，2发一，3发二
  private String systemTime;          //当前系统时间
  private Integer state; //状态 0：空闲 1：使用中 -1:不可用 -2：删除
  private String serviceKey; // 服务中心key
  private Integer userId; //用户id
  private String affiliation; //归属 表中无此字段， 村终端：村名 镇终端：用户真实姓名 其他为空
  private String associated; //关联字段。用于扩展关联，当form为4时此字段为高拍仪扩展字段
  private String deptId;//部门id
  private Integer tcType;//统筹类型  1：统筹中心（协助） 2：统筹中心（受理）
  private Integer scuState;//x86状态
  private Integer idCardState;//身份证状态
  private Integer highPhotoState;//高拍仪状态
  private Integer signatureState;//签名版状态

  public Integer getPrinterState() {
    return printerState;
  }

  public void setPrinterState(Integer printerState) {
    this.printerState = printerState;
  }

  private Integer printerState;//打印机状态

  public Integer getForm() {
    return form;
  }

  public void setForm(Integer form) {
    this.form = form;
  }

  private  Integer form;

  private String version;//版本号
  /**
   * @author jlm 窗口id
   */
  private Integer windowId;
  /**
   * @author jlm serviceName,windowName,deptName,typeName表中无此字段，用于管理端查询时页面信息展示
   */
  private String serviceName;
  private String windowName;
  private String deptName;
  private Long centerType;
  private Integer isSingleCamera;//是否是单摄像头  1：是  2：不是


  public Integer getScuState() {
    return scuState;
  }

  public void setScuState(Integer scuState) {
    this.scuState = scuState;
  }

  public Integer getIdCardState() {
    return idCardState;
  }

  public void setIdCardState(Integer idCardState) {
    this.idCardState = idCardState;
  }

  public Integer getHighPhotoState() {
    return highPhotoState;
  }

  public void setHighPhotoState(Integer highPhotoState) {
    this.highPhotoState = highPhotoState;
  }

  public Integer getSignatureState() {
    return signatureState;
  }

  public void setSignatureState(Integer signatureState) {
    this.signatureState = signatureState;
  }



  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public VcDevBean() {
  }

  public Integer getIsSingleCamera() {
    return isSingleCamera;
  }

  public void setIsSingleCamera(Integer isSingleCamera) {
    this.isSingleCamera = isSingleCamera;
  }

  public Long getCenterType() {
    return centerType;
  }

  public void setCenterType(Long centerType) {
    this.centerType = centerType;
  }

  public String getDeptId() {
    return deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getWindowName() {
    return windowName;
  }

  public void setWindowName(String windowName) {
    this.windowName = windowName;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Integer getWindowId() {
    return windowId;
  }

  public void setWindowId(Integer windowId) {
    this.windowId = windowId;
  }

  public String getAssociated() {
    return associated;
  }

  public void setAssociated(String associated) {
    this.associated = associated;
  }

  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getNo() {
    return no;
  }

  public void setNo(int no) {
    this.no = no;
  }

  public String getId() {
      if (id.length() < 6) {
      return getShortDevId(id);
    }
      if (id.length() > 11 && id.length() < 24) {
          return getLongDevid(id);
      }
    return id;
  }

    private String getLongDevid(String id) {
        Integer devno_1 = null;
        Integer devno_2 = null;
        Integer devno_3 = null;
        Integer devno_4 = null;

        if (id.contains("-")) {
            String[] devList = id.split("-");
            devno_1 = Integer.parseInt(devList[0]);
            devno_2 = Integer.parseInt(devList[1]);
            devno_3 = Integer.parseInt(devList[2]);
            devno_4 = Integer.parseInt(devList[3]);
        } else {
            String qsDevno = id.replaceAll("(.{5})", "$1-");
            String[] devList = qsDevno.split("-");
            devno_1 = Integer.parseInt(devList[0]);
            devno_2 = Integer.parseInt(devList[1]);
            devno_3 = Integer.parseInt(devList[2]);
            devno_4 = Integer.parseInt(devList[3]);
        }

        if (devno_1 == 0 && devno_2 == 0 && devno_3 == 0 && devno_4 == 0) {       // 0-0-0-0
            return id;
        }
        if (devno_1 != 0 && devno_2 == 0 && devno_3 == 0 && devno_4 == 0) {      // 1-0-0-0
            return id;
        }
        if (devno_1 != 0 && devno_2 != 0 && devno_3 == 0 && devno_4 == 0) {      // 1-2-0-0
            return id;
        }
        String qstr1 = "";
        String qstr2 = "";
        String qstr3 = "";

        if (devno_1 != 0 && devno_2 != 0 && devno_3 != 0 && devno_4 == 0)       // 1-2-3-0
        {
            qstr1 = String.format("%03d", devno_2);
            qstr2 = "000";
            qstr3 = String.format("%03d", devno_3);
        } else if (devno_1 != 0 && devno_2 != 0 && devno_3 != 0 && devno_4 != 0)  // 1-2-3-4
        {
            qstr1 = String.format("%03d", devno_2);
            qstr2 = String.format("%03d", devno_3);
            qstr3 = String.format("%05d", devno_4);
        }
        return qstr1 + qstr2 + qstr3;
    }

  private String getShortDevId(String id) {
    return id.replaceAll("^(0+)", "");
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getMsgId() {
    return msgId;
  }

  public void setMsgId(Integer msgId) {
    this.msgId = msgId;
  }

  public Integer getMonitorId() {
    return monitorId;
  }

  public void setMonitorId(Integer monitorId) {
    this.monitorId = monitorId;
  }

  public Integer getChannelId() {
    return channelId;
  }

  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
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

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getpX() {
    return pX;
  }

  public void setpX(String pX) {
    this.pX = pX;
  }

  public String getpY() {
    return pY;
  }

  public void setpY(String pY) {
    this.pY = pY;
  }

  public String getpZ() {
    return pZ;
  }

  public void setpZ(String pZ) {
    this.pZ = pZ;
  }

  @JsonIgnore
  public String getPLayer() {
    return pLayer;
  }

  public void setPLayer(String pLayer) {
    this.pLayer = pLayer;
  }

  public String getSvrid() {
    return svrid;
  }

  public void setSvrid(String svrid) {
    this.svrid = svrid;
  }

  public String getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(String updatetime) {
    this.updatetime = updatetime;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }



  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }



  public Integer getStreamType() {
    return streamType;
  }

  public void setStreamType(Integer streamType) {
    this.streamType = streamType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    this.dataType = dataType;
  }

  public Integer getDevFunc() {
    return devFunc;
  }

  public void setDevFunc(Integer devFunc) {
    this.devFunc = devFunc;
  }

  public String getSystemTime() {
    return systemTime;
  }

  public void setSystemTime(String systemTime) {
    this.systemTime = systemTime;
  }

    public Integer getTcType() {
        return tcType;
    }

    public void setTcType(Integer tcType) {
        this.tcType = tcType;
    }

    //	@Override
//	public String toString() {
//		return "VcDevBean [id=" + id + ", msgId=" + msgId + ", monitorId="
//				+ monitorId + ", channelId=" + channelId + ", name=" + name
//				+ ", mac=" + mac + ", ip=" + ip + ", type=" + type
//				+ ", typeId=" + typeId + ", description=" + description
//				+ ", groupId=" + groupId + ", groupName=" + groupName
//				+ ", alias=" + alias + ", roleId=" + roleId + ", pX=" + pX
//				+ ", pY=" + pY + ", pZ=" + pZ + ", pLayer=" + pLayer
//				+ ", svrId=" + svrId + ", updateTime=" + updateTime
//				+ ", regionId=" + regionId + ", no=" + no + ", streamType="
//				+ streamType + ", status=" + status + ", address=" + address
//				+ ", dataType=" + dataType + ", devFunc=" + devFunc
//				+ ", systemTime=" + systemTime + ", state=" + state
//				+ ", serviceKey=" + serviceKey + ", userId=" + userId
//				+ ", form=" + form + "]";
//	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    VcDevBean other = (VcDevBean) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }


}
