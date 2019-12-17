/*
 * ClientInfo.java
 * ------------------*
 * 2018-09-02 created
 */
package com.visionvera.remoteservice.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.visionvera.api.handler.bean.EmbeddedEntity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ----------------------* 2018-09-02created
 */
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 申请人姓名
     */
    private String username;
    /**
     * 性别 1:男 2:女 9：其他
     */
    private String sex;
    /**
     * 出生日期
     */
    private Date birthDate;
    /**
     * 家庭地址
     */
    private String address;
    /**
     * 证件类型 1.身份证 2.户口本 3.护照
     */
    private Integer idType;
    /**
     * 证件号码 主键
     */
    private String idcard;
    /**
     * 发证机关
     */
    private String issuingOrgan;
    /**
     * 有效期限:依次是起始日期的年，月，日，终止日期的年，月，日
     */
    private String expirationDate;
    /**
     * 最新家庭住址
     */
    private String newAddr;
    /**
     * 民族名称
     */
    private String nationName;
    /**
     * 民族 参照名族表
     */
    private String nation;
    /**
     * 出生地
     */
    private String birthplace;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 扩展
     */
    private String extend;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 教育id 文化程度
     */
    private Integer educationId;
    /**
     * 来源  1：本地人口  2：外来人口
     */
    private Integer source;
    /**
     * 证件头像
     */
    private String iconPath;
    /**
     * 学历名称
     */
    private String educationHierarchy;
    /**
     * 户口性质 1：本地户口，2：外来户口
     */
    private String registration;

    /**
     * 年龄
     */
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @JSONField(name = "name")
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    @JSONField(name = "address")
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getIdcard() {
        return idcard;
    }

    @JSONField(name = "id_card_number")
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getIssuingOrgan() {
        return issuingOrgan;
    }

    @JSONField(name = "issued_at")
    public void setIssuingOrgan(String issuingOrgan) {
        this.issuingOrgan = issuingOrgan == null ? null : issuingOrgan.trim();
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    @JSONField(name = "expiration_date")
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate == null ? null : expirationDate.trim();
    }

    public String getNewAddr() {
        return newAddr;
    }

    public void setNewAddr(String newAddr) {
        this.newAddr = newAddr == null ? null : newAddr.trim();
    }

    public String getNationName() {
        return nationName;
    }

    @JSONField(name = "nation_name")
    public void setNationName(String nationName) {
        this.nationName = nationName == null ? null : nationName.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace == null ? null : birthplace.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
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

    public Integer getEducationId() {
        return educationId;
    }

    public void setEducationId(Integer educationId) {
        this.educationId = educationId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath == null ? null : iconPath.trim();
    }

    public String getEducationHierarchy() {
        return educationHierarchy;
    }

    public void setEducationHierarchy(String educationHierarchy) {
        this.educationHierarchy = educationHierarchy;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static ClientInfo convert(EmbeddedEntity entity) throws ParseException {
        ClientInfo clientInfo = new ClientInfo();
        if (entity == null) {
            return clientInfo;
        }
        clientInfo.setUsername(entity.getName());
        clientInfo.setSex(entity.getGender());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        clientInfo.setBirthDate(format.parse(entity.getBirthday()));
        clientInfo.setNationName(entity.getNation());
        clientInfo.setAddress(entity.getAddress());
        clientInfo.setIdcard(entity.getIdCardNumber());
        clientInfo.setIssuingOrgan(entity.getMaker());
        clientInfo.setExpirationDate(entity.getStartDate() + "-" + entity.getEndDate());

        return clientInfo;
    }

    public void getAgeByBirthDate() {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDate)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        cal.setTime(birthDate);
        int yearBirth = cal.get(Calendar.YEAR);
        age = yearNow - yearBirth;   //计算整岁数
    }
}