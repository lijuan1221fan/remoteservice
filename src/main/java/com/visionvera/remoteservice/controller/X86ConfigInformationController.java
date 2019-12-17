package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.X86ConfigInformationBean;
import com.visionvera.remoteservice.service.X86ConfigInformationService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述:
 *
 * @ClassName: X86ConfigInformationController
 * @Author: ljfan
 * @Date: 2019-06-27 14:07
 * @Version: V1.0
 */
@RestController
@RequestMapping("/X86")
public class X86ConfigInformationController {
   @Resource
  X86ConfigInformationService x86ConfigInformationService;
  @SysLogAnno("修改SCU配置信息")
  @RequestMapping(value = "/updateX86", method = RequestMethod.POST)
   public Map<String,Object> updateX86ConfigInformation(@RequestBody List<X86ConfigInformationBean> x86ConfigInformationBeanList){
    ValidateUtil.validate(x86ConfigInformationBeanList, UpdateGroup.class);
     return x86ConfigInformationService.updateX86ConfigInformation(x86ConfigInformationBeanList);
   }
  @RequestMapping(value = "/getX86", method = RequestMethod.GET)
  public Map<String,Object> getX86ConfigInformation(){
    return x86ConfigInformationService.getX86ConfigInformation();
  }
  @SysLogAnno("更新SCU配置包")
  @RequestMapping(value = "/uploadZip", method = RequestMethod.POST)
  public Map<String,Object> uploadZip(MultipartFile X86zip){
   return x86ConfigInformationService.uploadX86zip(X86zip);
  }
}
