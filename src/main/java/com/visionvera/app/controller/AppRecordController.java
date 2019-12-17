package com.visionvera.app.controller;

import com.visionvera.app.service.AppRecordService;

import java.text.ParseException;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2019/3/18
 */
@RestController
@RequestMapping("/app")
public class AppRecordController {

  @Resource
  private AppRecordService appRecordService;

  /**
   * 取预约号接口 如有预约记录返回true 直接取号 如没有返回false 页面返回正常取号页面
   * @param idCardNo
   * @param serviceKey
   * @return
   */
  @RequestMapping("/getNumber")
  public Map<String, Object> getNumber(String idCardNo, String serviceKey,String androidBusinessType) throws ParseException {
    return appRecordService.getNumber(idCardNo, serviceKey,androidBusinessType);
  }

}
