package com.visionvera.remoteservice.api.controller;

import com.visionvera.common.enums.QueueTypeEnum;
import com.visionvera.common.validator.group.ApiDeptSelectGroup;
import com.visionvera.common.validator.group.TakeNumberGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.pojo.BusinessTypeVo;
import com.visionvera.remoteservice.pojo.DeptListVo;
import com.visionvera.remoteservice.pojo.NewDeptListVo;
import com.visionvera.remoteservice.service.BusinessTypeService;
import com.visionvera.remoteservice.service.DeviceInfoService;
import com.visionvera.remoteservice.service.NumberService;
import com.visionvera.remoteservice.service.ServiceCenterService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

  private static Logger logger = LoggerFactory.getLogger(ApiController.class);

  @Resource
  private ServiceCenterService serviceCenterService;
  @Resource
  private DeviceInfoService deviceInfoService;
  @Autowired
  private NumberService numberService;
  @Autowired
  private BusinessTypeService businessTypeService;

  @RequestMapping("/accept")
  public Map<String, Object> accept() {
    try {
      logger.info("叫号机连接成功");
      return ResultUtils.ok("连接成功。");
    } catch (Exception e) {
      logger.info("叫号机连接失败");
      return ResultUtils.error("连接错误。");
    }
  }


  /**
   * @param serviceName 模糊查询的服务中心名称
   * @param gradeId 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村 记得页数传-1
   * @param serviceId 主键
   * @return Map<StringObject>
   * @Description: 查询服务中心列表
   * @author quboka
   * @date 2018年4月11日
   */
  @RequestMapping("/getServiceCenterList")
  public Map<String, Object> getServiceCenterList(
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @RequestParam(value = "fuzzyServiceName", required = false) String fuzzyServiceName,
      @RequestParam(value = "gradeId", defaultValue = "5") Integer gradeId, Integer serviceId,String serviceKey) {
    try {
      return serviceCenterService
          .getServiceCenterList(fuzzyServiceName, pageNum, pageSize, gradeId, serviceId, serviceKey);
    } catch (Exception e) {
      logger.error("查询服务中心列表异常。", e);
      return ResultUtils.error("查询服务中心列表异常。");
    }
  }

  /**
   * @param serviceName 模糊查询的服务中心名称
   * @Description: 取号机获取审批中心及服务中心列表
   * @author ljfan
   * @date 2019年01月10日
   */
  @RequestMapping("/getOfferNumOfServiceCenterList")
  public Map<String, Object> getOfferNumOfServiceCenterList(@RequestParam(value = "serviceName", required = false) String serviceName) {
    try {
      return serviceCenterService
          .getOfferNumOfServiceCenterList(serviceName);
    } catch (Exception e) {
      logger.error("查询服务中心列表异常。", e);
      return ResultUtils.error("查询服务中心列表异常。");
    }
  }
  /**
   * 同步连接叫号机
   *
   * @param deviceInfo
   * @return
   * @author ericshen
   * @date 2018年11月29日
   */
  @RequestMapping("syncDeviceInfo")
  public Map<String, Object> syncDeviceInfo(DeviceInfo deviceInfo) {
    ValidateUtil.validate(deviceInfo);
    logger.info("同步叫号机");
    int count = deviceInfoService.syncDevice(deviceInfo);
    if (count == 0) {
      return ResultUtils.error("保存失败");
    }
    return ResultUtils.ok("保存成功", deviceInfo.getId());

  }


  /**
   * @param deviceId password
   * @return Map<StringObject>
   * @Description: 验证设备密码
   * @author zhanghui
   * @date 2018年5月28日
   */
  @RequestMapping("checkPassword")
  public Map<String, Object> checkPassword(@RequestParam(value = "deviceId") String deviceId,
      @RequestParam(value = "password") String password) {
    try {
      Integer devid=Integer.parseInt(deviceId);
      DeviceInfo device = deviceInfoService.selectById(devid);
      if (device == null) {
        logger.info("叫号机不存在");
        return ResultUtils.error("设备不存在");
      }
      if (!password.equals(device.getPassword())) {
        logger.info("叫号机密码错误");
        return ResultUtils.error("密码不正确");
      }
      logger.info("叫号密码正确");
      return ResultUtils.ok("密码正确");
    } catch (Exception e) {
      logger.error("验证密码异常", e);
      return ResultUtils.error("验证密码异常");
    }

  }


  @RequestMapping("getSession")
  public Map<String, Object> getSession(HttpSession session) {
    Map<String, Object> result = new HashMap<>();
    try {
      Enumeration<String> enumeration = session.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        String key = enumeration.nextElement();
        Object value = session.getAttribute(key);
        logger.info(key + ":" + value);
        result.put(key, value);
      }
      return ResultUtils.ok("获取session中的值成功", result);
    } catch (Exception e) {
      logger.error("获取session中的值失败", e);
      return ResultUtils.error("获取session中的值失败");
    }
  }

  /**
   * @param serviceKey 村key
   * @param businessId 业务详情id
   * @return java.util.Map<java.lang.String,java.lang.Object>
   * @description: 获取业务号码
   * @author quboka
   * @date 2018/8/29 11:05
   */
  @RequestMapping("takeBusinessNumber")
  public Map<String, Object> takeBusinessNumber(@RequestBody DeptListVo deptListVo) {
    ValidateUtil.validate(deptListVo, TakeNumberGroup.class);
    try {
      Integer type = QueueTypeEnum.Scene.getValue();
      Integer appointmentId = 0;
      return numberService.takeBusinessNumberAndModify(deptListVo.getServiceKey(), deptListVo.getTypeId(),type,appointmentId,deptListVo.getAndroidBusinessType());
    } catch (Exception e) {
      logger.error("派号异常", e);
      return ResultUtils.error("派号异常");
    }
  }

  /**
   * @param serviceKey 村key
   * @param businessId 业务详情id
   * @return java.util.Map<java.lang.String,java.lang.Object>
   * @description: 新版叫号机获取业务号码
   * @author hakimjun
   * @date 2019/8/22
   */
  @RequestMapping("/getBusinessNumber")
  public Map<String, Object> getBusinessNumber(@RequestBody NewDeptListVo  newDeptListVo) {
    ValidateUtil.validate(newDeptListVo, TakeNumberGroup.class);
    try {
      Integer type = QueueTypeEnum.Scene.getValue();
      Integer appointmentId = 0;
      return numberService.getBusinessNumberAndModify(newDeptListVo.getServiceKey(), newDeptListVo.getTypeId(),type,appointmentId,newDeptListVo.getNumberName(),newDeptListVo.getIdCardNumber());
    } catch (Exception e) {
      logger.error("派号异常:"+e.getMessage());
      return ResultUtils.error("派号异常");
    }
  }

  @PostMapping("/searchBusinessInfo")
  public Map<String,Object> searchBusinessInfo(@RequestBody NewDeptListVo newDeptListVo) {
    int i=0;
    try {
      return numberService.searchBusinessInfo(newDeptListVo.getIdCardNumber(),newDeptListVo.getServiceKey());
    } catch (Exception e) {
      logger.error("查询当前排队进度", e);
      return null;
    }
  }



  /**
   * @param [pageNum, pageSize, serviceKey]
   * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
   * @description: 通过服务中心key查询部门列表
   * @author quboka
   * @date 2018/8/28 18:15
   */
  @RequestMapping("getDeptList")
  public Map<String, Object> getDeptList(@RequestBody DeptListVo deptListVo) {
    ValidateUtil.validate(deptListVo, ApiDeptSelectGroup.class);
    try {
      Map<String,Object> map= numberService.getDeptList(deptListVo);
      return map;
    } catch (Exception e) {
      logger.error("查询部门异常", e);
      return ResultUtils.error("查询部门异常");
    }
  }
  /**
   * @param [pageNum, pageSize, describes, deptId, serviceKey, id]
   * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
   * @description: 业务类别查询
   * @author quboka
   * @date 2018/8/27 11:22
   */
  @RequestMapping("/getBusinessClasses")
  public Map<String, Object> getBusinessClasses(@RequestBody BusinessTypeVo businessTypeVo) {
    try {
      //业务类别
      if (0 == businessTypeVo.getParentId()) {
        businessTypeVo.setOfferNumberCheck(1);
      }else{
       //业务详情
        businessTypeVo.setOfferNumberCheck(null);
      }
      return businessTypeService.getTakeNumberBusinessClasses(businessTypeVo);

    } catch (Exception e) {
      logger.error("查询业务类别失败", e);
      return ResultUtils.error("查询业务类别失败");
    }
  }

  /**
   * type:01,一窗多办 02：单部门
   * @param businessTypeVo
   * @return
   */
  @RequestMapping("androidShow")
  public Map<String, Object> androidShow(@RequestBody BusinessTypeVo businessTypeVo){
    return businessTypeService.androidShow(businessTypeVo);
  }
}
