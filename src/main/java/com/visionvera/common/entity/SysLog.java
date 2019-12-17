package com.visionvera.common.entity;

import java.sql.Timestamp;

/**
 * <p> 系统日志 </p>
 *
 * @author jlm
 * @since 2018.10.11
 */
public class SysLog {

  public static final String USERNAME_COLUMN = "username";
  public static final String OPERATION_COLUMN = "operation";
  private static final long serialVersionUID = 1L;
  /**
   * 自增id
   */
  private Long id;
  /**
   * 用户id
   */
  private Long userId;
  /**
   * 用户名
   */
  private String username;
  /**
   * 用户操作
   */
  private String operation;
  /**
   * 响应时间
   */
  private Long time;
  /**
   * 请求方法
   */
  private String method;
  /**
   * 创建时间
   */
  private Timestamp cTime;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Timestamp getcTime() {
    return cTime;
  }

  public void setcTime(Timestamp cTime) {
    this.cTime = cTime;
  }


  @Override
  public String toString() {
    return "SysLog{" +
        "id=" + id +
        ", userId=" + userId +
        ", username='" + username + '\'' +
        ", operation='" + operation + '\'' +
        ", time=" + time +
        ", method='" + method + '\'' +
        ", cTime=" + cTime +
        '}';
  }
}
