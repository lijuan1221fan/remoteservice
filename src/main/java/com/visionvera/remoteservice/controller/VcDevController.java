package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.BindGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.EmbeddedServerInfo;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.common.lock.Lock;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.pojo.VcDevVo;
import com.visionvera.remoteservice.service.EmbeddedService;
import com.visionvera.remoteservice.service.VcDevService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: VcDevController
 *
 * @author quboka
 * @Description: 会管终端
 * @date 2018年5月17日
 */
@RestController
@RequestMapping("/vcDev")
public class VcDevController {

  private static Logger logger = LoggerFactory.getLogger(VcDevController.class);

  @Resource
  private VcDevService vcDevService;

  @Resource
  private ServiceCenterDao serviceCenterDao;

  @Resource
  private EmbeddedService embeddedService;

  @Resource
  private EmbeddedServerDao embeddedServerDao;

  /**
   * @return Map<String   ,   Object>
   * @Description: 同步会管终端
   * @author quboka
   * @date 2018年5月17日
   */
  @RequestMapping(value = "/synDevice", method = RequestMethod.GET)
  public Map<String, Object> synDevice() {
    try {
      return vcDevService.synDeviceAndModify();
    } catch (Exception e) {
      // 修改同步锁：表示当前允许业务
      Lock.synDevice.set(false);
      logger.error("同步错误", e);
      return ResultUtils.error("同步错误");
    }
  }

  /**
   * @param serviceKey 服务中心key
   * @param windowId 窗口id
   * @param form 形态 1：村终端 2：镇终端 3:默认终端
   * @param deviceId 终端id
   * @param associated 关联字段。用于扩展关联，当form为4时此字段为高拍仪扩展字段
   * @return Map<String, Object>
   * @Description: 绑定终端
   * @author jlm
   * @date 2018年11月5日
   */
  @SysLogAnno("绑定终端")
  @RequestMapping(value = "/bindingDevice", method = RequestMethod.POST)
  public Map<String, Object> bindingDevice(@RequestBody VcDevVo vcDevPojo) {
    ValidateUtil.validate(vcDevPojo, BindGroup.class);
    return vcDevService.bindingDeviceAndModify(vcDevPojo);
  }

  /**
   * @param deviceId
   * @return Map<String   ,   Object>
   * @Description: 解绑终端
   * @author quboka
   * @date 2018年5月21日
   */
  @SysLogAnno("解绑终端")
  @RequestMapping(value = "/unbindDevice", method = RequestMethod.GET)
  public Map<String, Object> unbindDevice(
      @RequestParam(value = "deviceId", required = true) String deviceId) {
    ValidateUtil.validate(deviceId);
    return vcDevService.unbindDeviceAndModify(deviceId);
  }


  /**
   * @param pageNum
   * @param pageSize
   * @param id 终端id 终端号码
   * @param name 终端名称
   * @param serviceKey 服务中心名称
   * @param form 形态 0：未分配 1：村终端 2：镇终端 3:默认终端  4:高拍仪终端
   * @param state 0：空闲 1：使用中 -1:不可用 -2：删除'
   * @return Map<String   ,   Object>
   * @Description: 管理端查询终端列表
   * @author jlm
   * @date 2018年11月6日
   */
  @RequestMapping(value = "/getDeviceList", method = RequestMethod.POST)
  public Map<String, Object> getDeviceList(@RequestBody VcDevVo vcDevPojo) {
    ValidateUtil.validate(vcDevPojo, QueryGroup.class);
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("pageNum", vcDevPojo.getPageNum());
    paramsMap.put("pageSize", vcDevPojo.getPageSize());
    paramsMap.put("name", vcDevPojo.getName());
    paramsMap.put("scuState", vcDevPojo.getScuState());
    //获取当前登录用户
    SysUserBean user = ShiroUtils.getUserEntity();
    String type =user.getType();
    //查询之前删除embeddedServer中的无效关联关系
    embeddedService.deleteFailureConnection();
    if (type.equals(CommonConstant.SUPER_ADMIN)) {
      //超管账户 查询所有终端
      Map<String,Object> map = vcDevService.superGetDevList(paramsMap);
      return map;
    } else if (type.equals(CommonConstant.FIRST_ADMIN)) {
      //统筹管理员 查看统筹以及旗下的终端
      String userServiceKey = user.getServiceKey();
      //总serviceKey集合
      List<String> serviceKeys = serviceCenterDao.getServiceKeysByParentServiceKey(userServiceKey);
      List<String> keyList = serviceCenterDao.getServiceKeysByParentKey(userServiceKey);
      if(CollectionUtils.isNotEmpty(serviceKeys)){
        keyList.addAll(serviceKeys);
      }
      keyList.add(userServiceKey);
      paramsMap.put("serviceKey", keyList);
      //0代表所有部门
      if (user.getDeptId() != CommonConstant.SUPER_ADMIN_DEPTID_TYPE) {
        paramsMap.put("deptId", user.getDeptId());
      }
      return vcDevService.getDeviceList(paramsMap);
    } else if (type.equals(CommonConstant.SECOND_ADMIN)) {
      //根据审批中心的serviceKey 取得所有村终端serviceKey
      String userServiceKey = user.getServiceKey();
      List<String> ServiceKeyList = serviceCenterDao
          .getServiceKeysByParentServiceKey(userServiceKey);
      ServiceKeyList.add(userServiceKey);
      paramsMap.put("serviceKey", ServiceKeyList);
      //0代表所有部门
      if (user.getDeptId() != CommonConstant.SUPER_ADMIN_DEPTID_TYPE) {
        paramsMap.put("deptId", user.getDeptId());
      }
      return vcDevService.getDeviceList(paramsMap);
    } else {
      logger.info("无查看权限");
      return ResultUtils.error("无查看权限");
    }
  }

  /**
   * 查询终端详情接口
   *
   * @param deviceId
   * @return
   */
  @RequestMapping(value = "getDeviceDetails", method = RequestMethod.GET)
  public VcDevBean getDeviceDetails(@RequestParam("deviceId") String deviceId) {
    ValidateUtil.validate(deviceId);
    return vcDevService.getDeviceDetails(deviceId);
  }

  /**
   * 修改终端信息
   *
   * @param deviceId
   * @param ip
   * @param type
   * @return
   */
  @RequestMapping(value = "updateDeviceDetail", method = RequestMethod.POST)
  public Map<String, Object> updateDeviceDetail(@RequestBody VcDevVo vcDevVo) {
    ValidateUtil.validate(vcDevVo, UpdateGroup.class);
    return vcDevService
        .updateDeviceDetail(vcDevVo.getDeviceId(), vcDevVo.getIp(), vcDevVo.getType());
  }

  /**
   * 将终端置为空闲状态
   *
   * @param deviceId
   * @return
   */
  @GetMapping(value = "setIdle")
  public Map<String, Object> setIdle(@RequestParam("deviceId") String deviceId) {
    int i = vcDevService.setIdle(deviceId);
    if (i > CommonConstant.zero) {
      logger.info("修改状态成功");
      return ResultUtils.ok("修改状态成功");
    }
    logger.info("修改状态失败");
    return ResultUtils.error("修改状态失败");
  }

  /**
   * scu设备重启
   *
   * @param deviceId
   * @return
   */
  @RequestMapping(value = "scuRestart",method = RequestMethod.GET)
  public Map<String, Object> scuRestart(String deviceId) {
    ValidateUtil.validate(deviceId);
    return vcDevService.scuRestart(deviceId);
  }

  @RequestMapping(value = "sendScuEquipmentStatus", method = RequestMethod.GET)
  public Map<String, Object> sendScuEquipmentStatus(String deviceId) {
    AssertUtil.isNull(deviceId,"设备id不能为空");
    embeddedServerDao.clearEmbeddedServerStatus(deviceId);
      Map<String, Object> scuAllState = vcDevService.getScuAllState(deviceId);
    return ResultUtils.ok("ok");
  }


  @RequestMapping(value = "getScuEquipmentStatus", method = RequestMethod.GET)
  public Map<String, Object> getScuEquipmentStatus(String deviceId) {
    AssertUtil.isNull(deviceId,"设备id不能为空");
    EmbeddedServerInfo otherStatus = embeddedServerDao.getOtherStatus(deviceId);
    if(otherStatus!=null){

      return ResultUtils.ok("获取成功", otherStatus);
    }
    return ResultUtils.error("获取失败");
  }

}













