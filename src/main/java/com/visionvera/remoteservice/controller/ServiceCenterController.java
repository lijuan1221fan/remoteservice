package com.visionvera.remoteservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.pojo.ServiceCenterVo;
import com.visionvera.remoteservice.service.ServiceCenterService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ServiceCenterController
 *
 * @author quboka
 * @Description: 服务中心
 * @date 2018年4月11日
 */
@RestController
@RequestMapping("serviceCenter")
public class ServiceCenterController {

  private static Logger logger = LoggerFactory.getLogger(ServiceCenterController.class);

  @Resource
  private ServiceCenterService serviceCenterService;

  //{"gradeId":4,"pageNum":-1}

  /**
   * @param serviceName 模糊查询的服务中心名称
   * @param gradeId 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村 记得页数传-1
   * @param serviceId 主键
   * @return Map<String       ,       Object>
   * @Description: 查询服务中心列表
   * @author quboka
   * @date 2018年4月11日
   */
  @RequestMapping("getServiceCenterList")
  public Map<String, Object> getServiceCenterList(
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @RequestParam(value = "serviceName", required = false) String serviceName,
      @RequestParam(value = "gradeId", required = false) Integer gradeId,
      Integer serviceId, String serviceKey) {
    return serviceCenterService
        .getServiceCenterList(serviceName, pageNum, pageSize, gradeId, serviceId, serviceKey);
  }

  @RequestMapping(value = "getPageServiceCenterList",method = RequestMethod.POST)
  public Map<String, Object> getPageServiceCenterList(@RequestBody ServiceCenterVo vo){
    Map<String, Object> paramMap = new HashMap<String, Object>();
    return serviceCenterService.getPageServiceCenterList(vo.getPageNum(),vo.getPageSize(),
            vo.getType(),vo.getServiceName(),vo.getServiceKey());
  }

  /**
   * @param serviceIds 服务中心id  多个用逗号相隔
   * @return Map<String       ,       Object>
   * @Description: 删除服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  @SysLogAnno("删除服务中心")
  @GetMapping("deleteServiceCenter")
  public Map<String, Object> deleteServiceCenter(
      @RequestParam(value = "serviceIds", required = true) String serviceIds) {
    AssertUtil.isNull(serviceIds,"参数缺失");
    return serviceCenterService.deleteServiceCenter(serviceIds);
  }

  //{"serviceName":"测试1","regionId":"110,111,11101,11121","regionAll":"北京市,市辖区,东城区,东华门街道","address":"歌华大厦","gradeId":"4","parentKey":"RS_efac8a5b3da943ba824a969339fe81b1","status":2}

  /**
   * @param serviceCenter
   * @return Map<String       ,       Object>
   * @Description: 添加服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  @SysLogAnno("添加服务中心")
  @RequiresPermissions("center:add")
  @RequestMapping("/addServiceCenter")
  public Map<String, Object> addServiceCenter(@RequestBody ServiceCenterBean serviceCenter) {
    ValidateUtil.validate(serviceCenter,AddGroup.class);
    return serviceCenterService.addServiceCenter(serviceCenter);
  }

  //{"serviceId":11,"version":4,"serviceName":"测试2","parentKey":"110","regionId":"221","regionAll":"222","address":"aaaa","status":0}

  /**
   * @param serviceCenter
   * @return Map<String       ,       Object>
   * @Description: 修改服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  @SysLogAnno("修改中心")
  @RequestMapping("updateServiceCenter")
  public Map<String, Object> updateServiceCenter(@RequestBody ServiceCenterBean serviceCenter) {
    ValidateUtil.validate(serviceCenter,UpdateGroup.class);
    return serviceCenterService.updateServiceCenter(serviceCenter);
  }


  /**
   * @param serviceCenter
   * @return Map<String       ,       Object>
   * @Description: 查询子服务中心列表
   * @author zhanghui
   * @date 2018年5月26日
   */
  @RequestMapping("getChildrenCenterList")
  public Map<String, Object> getChildrenCenterList(
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @RequestParam(value = "serviceKey", defaultValue = "") String serviceKey,
      String serviceName
  ) {
    ServiceCenterBean entity = new ServiceCenterBean();
    entity.setParentKey(serviceKey);
    entity.setServiceName(serviceName);
    Map<String, Object> map = serviceCenterService.queryMapList(entity, pageNum, pageSize);
    return ResultUtils.ok("查询子服务中心列表成功", map);
  }

  /**
   * 查询所有中心
   *
   * @param regionKey
   * @return Map
   * @author JLM
   */

  @RequestMapping("getAllCenterList")
  public Map<String, Object> getAllCenterList(@RequestParam String regionKey) {
    AssertUtil.isNull(regionKey,"参数缺失");
    Map<String, Object> map = serviceCenterService.queryCenterList(regionKey);
    return ResultUtils.ok("查询子所有中心列表成功", new JSONObject(map));
  }

  /**
   * 获取中心列表
   *
   * @param serviceCenterBean
   * @return
   */
  @RequiresPermissions("center:query")
  @PostMapping(value = "getPageCenterList")
  public Map<String, Object> getPageCenterList(@RequestBody Map<String, Object> params) {
    return serviceCenterService.getPageCenterList(params);
  }

  /**
   * 获取审核中心列表 用于窗口绑定
   *
   * @author jlm
   * @date 2018.11.9
   */

  @RequestMapping(value = "getVerifyCenterList")
  public Map<String, Object> getVerifyCenterList() {
    //获取当前登录用户
    SysUserBean user = ShiroUtils.getUserEntity();
    //判断用户级别,超管可以查看所有审核中心
    Map<String, Object> paramMap = new HashMap<>();
    if (CommonConstant.SUPER_ADMIN.equals(user.getType())) {
      //超管账户
      return serviceCenterService.getVerifyCenterList(paramMap);
    } else if (CommonConstant.FIRST_ADMIN.equals(user.getType())) {
      //统筹中心管理员账户
      paramMap.put("parentKey", user.getServiceKey());
      return serviceCenterService.getVerifyCenterList(paramMap);
    } else if (CommonConstant.SECOND_ADMIN.equals(user.getType())) {
      //审核中心管理员
      paramMap.put("serviceKey", user.getServiceKey());
      return serviceCenterService.getVerifyCenterList(paramMap);
    } else {
      return ResultUtils.error("无获取权限");
    }
  }

  /**
   * 分页查询审批中心下所有的服务中心
   *
   * @param params
   * @return
   */
  @PostMapping(value = "getServiceCenterListByParentKey")
  public Map<String, Object> getServiceCenterListByParentKey(
      @RequestBody Map<String, Object> params) {
    return serviceCenterService.getServiceCenterListByParentKey(params);
  }

  /**
   * 　　* @Description: 获取树形数据 　　* @author: xueshiqi 　　* @date: 2018/11/13
   */
  @GetMapping(value = "/getServiceCenterTree")
  public Map<String, Object> getServiceCenterTree(@RequestParam Boolean flag) {
    AssertUtil.isNull(flag,"参数缺失");
    return serviceCenterService.getServiceCenterTree(flag);
  }

  /**
   * 　　* @Description: 根据servicekey查询服务中心 　　* @author: xueshiqi 　　* @date: 2018/11/22
   */
  @GetMapping(value = "/getServiceCenterByServiceCenterKey")
  public Map<String, Object> getServiceCenterByServiceCenterKey(@RequestParam String serviceCenterKey) {
    AssertUtil.isNull(serviceCenterKey,"参数缺失");
    return serviceCenterService.getServiceCenterByServiceCenterKey(serviceCenterKey);
  }

  /**
   * 　　* @Description: 查询中心以及子级中心 　　* @author: xueshiqi 　　* @date: 2018/11/28
   */
  @GetMapping(value = "/getSecondServiceCenterAndChildren")
  public Map<String, Object> getSecondServiceCenterAndChildren() {
    return serviceCenterService.getSecondServiceCenterAndChildren();
  }
  /**
   * @Description: 多中心新建业务类别时，admin取得所有统筹中心及审批中心，当为中心管理员时，取得当前用户下所在的中心
   * @author: ljfan 　　
   * @date: 2019/1/16
   * param flage=true  全部
   */
  @GetMapping(value = "/getServiceCenterByUser")
  public Map<String, Object> getServiceCenterByUser(boolean flage) {
    return serviceCenterService.getServiceCenterByUser(flage);
  }
}
