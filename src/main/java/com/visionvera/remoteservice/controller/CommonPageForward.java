package com.visionvera.remoteservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 *
 * @ClassName: CommonPageForward
 * @Description: 页面跳转通用
 * @author wangruizhi
 * @date 2018年3月26日
 *
 */
@RestController
public class CommonPageForward {

  /***
   *
   * @Title: goToBusinessCountPage
   * @Description: 跳转到业务统计页面
   * @throws
   */
  @RequestMapping("business/count")
  public String goToBusinessCountPage() {
    return "foreground/count";
  }

  @RequestMapping("statist/countChart")
  public String goToCountChartPage() {
    return "foreground/countChart";
  }

  @RequestMapping("customTitle")
  public String goToCustomTitlePage() {
    return "background/customTitle";
  }

  @RequestMapping("serviceCenter")
  public String goToServiceCenterPage() {
    return "background/serviceCenter";
  }

  @RequestMapping("terminal")
  public String goToTerminalPage() {
    return "background/terminal";
  }

  @RequestMapping("user/accountManage")
  public String goToUserPage() {
    return "background/accountManage";
  }

  @RequestMapping("user/userManage")
  public String goToUserManage() {
    return "background/userManage";
  }

  @RequestMapping("businessAcceptance")
  public String goToBusinessAcceptance() {
    return "foreground/businessAcceptance";
  }

  @RequestMapping("businessDetails")
  public String goToBusinessTemplate() {
    return "background/businessDetails";
  }

  @RequestMapping("businessType")
  public String goToBusinessType() {
    return "background/businessType";
  }

  @RequestMapping("businessData")
  public String goToBusinessData() {
    return "background/businessData";
  }

}
