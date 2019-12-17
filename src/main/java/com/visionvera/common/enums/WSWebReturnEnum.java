package com.visionvera.common.enums;
/**
 * 功能描述:
 *
 * @ClassName: WSWebReturnEnum
 * @Author: ljfan    未完成
 * @Date: 2019-07-13 15:55
 * @Version: V1.0
 */

public enum WSWebReturnEnum {
  RESPONSE_SUCCESS("0","请求成功"),
  EXCEPTION_FAIL("1","异常失败"),
  CONNECTION_RELATION("8","开始业务关联关系建立成功！"),
  EXCEPTION_END("3","业务已经完结。"),
  UNFINISHED_BUSINESS("4","有未完成业务。"),
  LIMIT_ONLINE("408","获取任务失败。当前办理人数已达系统上限，请稍后再试。"),
  NOT_WAITING("5","非等待业务中"),
  RESULT_SUCCESS("2","返回结果成功");
  private String code;
  private String name;
  WSWebReturnEnum(String code, String name) {
    this.code = code;
    this.name = name;
  }
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
