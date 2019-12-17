package com.visionvera.remoteservice.bean;


/**
 * 自定义webservice错误返回信息
 *
 * @author xiechengsuan 创建日期 2016.11.10
 */
public class ResponseInfo {

  private int errcode;
  private String errmsg;
  private String access_token;
  private String data;
  private String extra;

  public ResponseInfo() {
  }

  public int getErrcode() {
    return errcode;
  }

  public void setErrcode(int errcode) {
    this.errcode = errcode;
  }

  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  //	public T getData() {
//		return data;
//	}
//	public void setData(T data) {
//		this.data = data;
//	}
  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ResponseInfo [errcode=" + errcode + ", errmsg=" + errmsg
        + ", access_token=" + access_token + ", data=" + data
        + ", extra=" + extra + "]";
  }

}
