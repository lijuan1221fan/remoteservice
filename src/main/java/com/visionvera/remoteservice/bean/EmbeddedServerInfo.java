package com.visionvera.remoteservice.bean;

import java.sql.Date;

/**
 * Created by jlm on 2019-05-21 11:31
 */
public class EmbeddedServerInfo {
    //主键
    private Integer id;
    //终端号
    private String vcDevId;
    //session 唯一标识
    private String sessionId;
    /**
     * 用户ID
     */
    private Integer userId;

    public Integer getUserId() {
      return userId;
    }

    public void setUserId(Integer userId) {
      this.userId = userId;
    }
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVcDevId() {
        return vcDevId;
    }

    public void setVcDevId(String vcDevId) {
        this.vcDevId = vcDevId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getScuState() {
        return scuState;
    }

    public void setScuState(Integer scuState) {
        this.scuState = scuState;
    }

    public Integer getIdcardstate() {
        return idcardstate;
    }

    public void setIdcardstate(Integer idcardstate) {
        this.idcardstate = idcardstate;
    }

    public Integer getHighphotostatus() {
        return highphotostatus;
    }

    public void setHighphotostatus(Integer highphotostatus) {
        this.highphotostatus = highphotostatus;
    }

    public Integer getSignaturestate() {
        return signaturestate;
    }

    public void setSignaturestate(Integer signaturestate) {
        this.signaturestate = signaturestate;
    }

    public Integer getPrinterstate() {
        return printerstate;
    }

    public void setPrinterstate(Integer printerstate) {
        this.printerstate = printerstate;
    }

    /**
     * 指纹状态
     */
    private Integer scuState;//scu状态


    public Integer getFingerPrintStatus() {
        return fingerPrintStatus;
    }

    public void setFingerPrintStatus(Integer fingerPrintStatus) {
        this.fingerPrintStatus = fingerPrintStatus;
    }

    /**
     * x86设备状态
     */
    private Integer fingerPrintStatus;//scu状态


    /**
     * 身份证状态
     */
    private Integer idcardstate;


    /**
     * 高拍仪状态
     */
    private Integer highphotostatus;


    /**
     * 签名版状态
     */
    private Integer signaturestate;


    /**
     * 打印机状态
     */
    private Integer printerstate;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 版本号
     */
    private String version;












    @Override
    public String toString() {
        return "EmbeddedServerInfo{" +
                "id=" + id +
                ", vcDevId='" + vcDevId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
