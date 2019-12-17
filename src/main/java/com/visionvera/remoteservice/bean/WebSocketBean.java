package com.visionvera.remoteservice.bean;

import com.visionvera.remoteservice.pojo.BaseVo;

import java.io.Serializable;

/**
 * Created by jlm on 2019-05-29 17:22
 */
public class WebSocketBean extends BaseVo implements Serializable {

    //1 身份证 2 高拍仪 3 签名 4 打印 5 人证比对 6 H5检测安卓模块
    private String type;
    private String code;
    private String vcDevId;
    private String userId;
    private Integer state;
    //一窗综办、分部门
    private String businessType ;
    private String serviceKey;
    private String deviceCode;
    //人证比对x86返回的带框的图片路径
    private String imgUrl;
    private String showType;
    //x86 返回信息
    private String message;
    //人证比对是否成功
    private Integer matchValue;
    //叫号机身份证模块
    private Integer cardReader;
    //叫号机打印模块
    private Integer printMode;

    public Integer getCardReader() {
        return cardReader;
    }

    public void setCardReader(Integer cardReader) {
        this.cardReader = cardReader;
    }

    public Integer getPrintMode() {
        return printMode;
    }

    public void setPrintMode(Integer printMode) {
        this.printMode = printMode;
    }

    public String getShowType() {
      return showType;
    }

    public void setShowType(String showType) {
      this.showType = showType;
    }

    public String getBusinessType() {
        return businessType;
      }

    public void setBusinessType(String businessType) {
      this.businessType = businessType;
    }

    public String getServiceKey() {
        return serviceKey;
      }

    public void setServiceKey(String serviceKey) {
      this.serviceKey = serviceKey;
    }

    public String getDeviceCode() {
      return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
      this.deviceCode = deviceCode;
    }

    public Integer getMatchValue() {
          return matchValue;
      }

    public void setMatchValue(Integer matchValue) {
        this.matchValue = matchValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVcDevId() {
        return vcDevId;
    }

    public void setVcDevId(String vcDevId) {
        this.vcDevId = vcDevId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "WebSocketBean{" +
                "type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", vcDevId='" + vcDevId + '\'' +
                ", userId='" + userId + '\'' +
                ", state=" + state +
                ", businessType='" + businessType + '\'' +
                ", serviceKey='" + serviceKey + '\'' +
                ", deviceCode='" + deviceCode + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", showType='" + showType + '\'' +
                ", message='" + message + '\'' +
                ", matchValue=" + matchValue +
                ", cardReader=" + cardReader +
                ", printMode=" + printMode +
                '}';
    }
}
