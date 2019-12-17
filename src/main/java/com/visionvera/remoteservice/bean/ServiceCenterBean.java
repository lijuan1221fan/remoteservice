package com.visionvera.remoteservice.bean;

import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.UpdateGroup;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Length;

/**
 * ClassName: ServiceCenter
 *
 * @author quboka
 * @Description: 服务中心
 * @date 2018年3月22日
 */
public class ServiceCenterBean {

    @NotNull(groups = UpdateGroup.class, message = "主键不能为空")
    private Integer serviceId;//主键
    private String serviceKey;//唯一标识varchar类型
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "父级servicekey不能为空")
    private String parentKey;//父 唯一标识varchar类型
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "中心名称不能为空")
    @Length(groups = {AddGroup.class, UpdateGroup.class}, min = 1, max = 16, message = "中心名称长度只能为1~16位")
    private String serviceName;//服务中心名称
    private Integer gradeId;// 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村
    private String regionKey;//行政区域最后一级
    private String regionId;//全部行政区域id
    private String regionAll;//全部行政区域
    //省
    private String province;
    //市
    private String city;
    //区
    private String area;
    //街道
    private String street;
    //社区
    private String community;
    //经纬度
    private String lal;
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "地址不能为空")
    @Length(groups = {AddGroup.class, UpdateGroup.class}, min = 1, max = 50, message = "地址长度只能为1~50位")
    private String address;//详细地址
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "状态不能为空")
    private Integer state;//状态 1:有效 0:无效 -1:禁用
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Integer version;//版本号
    private String parentName;//父级名称
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "类型不能为空")
    private Long type; //类型 1：统筹中心 2：审核中心 3：服务中心
    /**
     * 服务中心标题
     */
    private String serviceTitle;

    private List<ServiceCenterBean> children;

    /**
     * x86地址
     *
     * @return
     */
    private String x86Url;

    /**
     * 区域
     */
    private List<AreaBean> areaList;

    /**
     * 中心状态  办理中 等待中  空闲中
     */
    private Integer Status;

    /**
     * 所属区域末级名称
     */
    private String areaName;

    public String getX86Url() {
        return x86Url;
    }

    public void setX86Url(String x86Url) {
        this.x86Url = x86Url;
    }

    public List<ServiceCenterBean> getChildren() {
        return children;
    }

    public void setChildren(List<ServiceCenterBean> children) {
        this.children = children;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getRegionKey() {
        return regionKey;
    }

    public void setRegionKey(String regionKey) {
        this.regionKey = regionKey;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionAll() {
        return regionAll;
    }

    public void setRegionAll(String regionAll) {
        this.regionAll = regionAll;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getLal() {
        return lal;
    }

    public void setLal(String lal) {
        this.lal = lal;
    }

    public List<AreaBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaBean> areaList) {
        this.areaList = areaList;
    }

    public Integer getStatus() {
        return Status;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "ServiceCenterBean{" +
                "serviceId=" + serviceId +
                ", serviceKey='" + serviceKey + '\'' +
                ", parentKey='" + parentKey + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", gradeId=" + gradeId +
                ", regionKey='" + regionKey + '\'' +
                ", regionId='" + regionId + '\'' +
                ", regionAll='" + regionAll + '\'' +
                ", address='" + address + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", parentName='" + parentName + '\'' +
                ", type=" + type +
                ", serviceTitle='" + serviceTitle + '\'' +
                '}';
    }
}
