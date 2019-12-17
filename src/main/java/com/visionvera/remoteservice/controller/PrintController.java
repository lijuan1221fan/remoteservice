package com.visionvera.remoteservice.controller;

import com.visionvera.common.validator.group.CheckGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.pojo.PrintVo;
import com.visionvera.remoteservice.pojo.StorageGatewayVo;
import com.visionvera.remoteservice.service.PrintService;
import java.util.Map;

import com.visionvera.remoteservice.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Administrator
 * @date 2018/8/15
 **/
@RestController
@RequestMapping("print")
public class PrintController {

  @Autowired
  private PrintService printService;

  @RequestMapping
  public Map<String, Object> index(@RequestBody PrintVo printVo) {
    ValidateUtil.validate(printVo, CheckGroup.class);
    return printService.print(printVo.getIds(), printVo.getBusinessId());

  }

  @RequestMapping("setPrint")
  public Map<String, Object> setPrint(@RequestParam(value = "businessId") Integer businessId) {
    return printService.setPrint(businessId);
  }

  @RequestMapping(value = "formPrint", method = RequestMethod.GET)
  public Map<String, Object> formPrint(@RequestParam(value = "businessId",required = false) Integer businessId) {
    if(businessId == null){
      return ResultUtils.error("业务id不能为空");
    }
    return printService.formPrint(businessId);
  }
}
