package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.enums.*;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.*;
import com.visionvera.remoteservice.service.AreaService;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.CrudService;
import com.visionvera.remoteservice.service.ServiceCenterService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.util.UUIDGenerator;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * ClassName: ServiceCenterServiceImpl
 *
 * @author quboka
 * @Description: 服务中心
 * @date 2018年4月11日
 */
@Service
public class ServiceCenterServiceImpl extends
    CrudService<ServiceCenterDao, ServiceCenterBean> implements ServiceCenterService {

  private static Logger logger = LoggerFactory.getLogger(ServiceCenterServiceImpl.class);

  @Resource
  private ServiceCenterDao serviceCenterDao;

  @Resource
  private VcDevDao vcDevDao;

  @Resource
  private SysUserDao sysUserDao;

  @Resource
  private DeviceInfoDao deviceInfoDao;

  @Resource
  private WindowDao windowDao;

  @Resource
  private BusinessTypeDao businessTypeDao;

  @Resource
  private AreaService areaService;
  @Resource
  private BusinessInfoService businessInfoService;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private AreaDao areaDao;

  /**
   * @param fuzzyServiceName 模糊查询的服务中心名称
   * @param gradeId 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村
   * @return Map<String                                                               ,                                                               Object>
   * @Description: 查询服务中心列表
   * @author quboka
   * @date 2018年4月11日
   */
  @Override
  public Map<String, Object> getServiceCenterList(String fuzzyServiceName, Integer pageNum,
      Integer pageSize, Integer gradeId, Integer serviceId, String serviceKey) {

    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<ServiceCenterBean> serviceCenterList = serviceCenterDao
        .getServiceCenterList(fuzzyServiceName, gradeId, serviceId, serviceKey);
    PageInfo<ServiceCenterBean> pageInfo = new PageInfo<ServiceCenterBean>(serviceCenterList);

    logger.info("查询服务中心列表成功");
    return ResultUtils.ok("查询服务中心列表成功！", pageInfo);
  }

  @Override
  public Map<String, Object> getPageServiceCenterList(Integer pageNum, Integer pageSize,
                                                      Long type, String serviceName, String serviceKey) {
    //获取登陆人信息
    SysUserBean user = ShiroUtils.getUserEntity();
    //获取不同用户的serviceKey  超管时为null
    String userServiceKey = null;
    if(StringUtil.isEmpty(serviceKey)){
      userServiceKey = StringUtil.isEmpty(user.getServiceKey()) ? null : user.getServiceKey();
    }
    PageHelper.startPage(pageNum, pageSize);
    List<ServiceCenterBean> centerList = serviceCenterDao.
            getPageServiceCenterList(type, serviceName, serviceKey,userServiceKey);
    if(!CollectionUtils.isEmpty(centerList)){
      for (ServiceCenterBean center : centerList) {
        //取区域末级名称
        List<String> areaIdList = areaService.getAreaIdList(center);
        if(!CollectionUtils.isEmpty(areaIdList)) {
          String area = areaIdList.get(areaIdList.size() - 1);
          List<AreaBean> areaNameList = areaDao.getAreaName(Collections.singletonList(area));
          center.setAreaName(areaNameList.get(0).getName());
        }
        if(CenterTypeEnum.ADMIN.getType().equals(center.getType())){
          center.setParentName("");
        }else if(CenterTypeEnum.SERVER.getType().equals(center.getType())){
          //服务中心查找归属中心  统筹-审批
          ServiceCenterBean bean = serviceCenterDao.getServiceListByChildServiceKey(center.getParentKey());
          center.setParentName(bean.getServiceName()+"-"+center.getParentName());
        }
      }
    }
    PageInfo<ServiceCenterBean> pageInfo = new PageInfo<ServiceCenterBean>(centerList);
    return ResultUtils.ok("中心列表查询成功",pageInfo);
  }

  /** @Description 根据中心类型查询出审批中心，根据审批中心的serviceKey 及serviceName查询审批中心下服务中心列表
   * @param serviceName
   * @return
   */
  @Override
  public Map<String, Object> getOfferNumOfServiceCenterList(String serviceName){
    ServiceCenterBean serviceCenter = new ServiceCenterBean();
    serviceCenter.setType(Integer.valueOf(ServiceCenterTypeEnum.CHECK.getType()).longValue());
    List<ServiceCenterBean> serviceCenters =null;
    List<ServiceCenterBean> serviceCenterList = serviceCenterDao.getOfferNumOfServiceCenterList(serviceCenter);
    if(serviceCenterList.size()>0){
      for(ServiceCenterBean bean:serviceCenterList){
        serviceCenter = new ServiceCenterBean();
        serviceCenter.setServiceName(serviceName);
        serviceCenter.setParentKey(bean.getServiceKey());
         serviceCenters = serviceCenterDao.getOfferNumOfServiceCenterList(serviceCenter);
         bean.setChildren(serviceCenters);

      }
    }
    return ResultUtils.check("查询服务中心列表成功！", serviceCenterList);
  }
  /**
   * @param serviceIds 服务中心id  多个用逗号相隔
   * @return Map<String                                                               ,                                                               Object>
   * @Description: 删除服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  @Override
  public Map<String, Object> deleteServiceCenter(String serviceIds) {
    String[] serviceIdArray = serviceIds.split(",");
    //不能删除自己所在得审批中心
//		UserBean user = (UserBean) session.getAttribute("user");
    SysUserBean user = ShiroUtils.getUserEntity();
    //查询servicekey
    List<String> serviceKeys = serviceCenterDao.getServiceKeysByServiceIds(serviceIdArray);
    ServiceCenterBean serviceCenter = serviceCenterDao
            .getServiceCenterByServiceKey(serviceKeys.get(0));
    //现在实际就是单个删除，不支持批量删除
    Integer count = 0;
    if(serviceCenter.getType().intValue() == ServiceCenterTypeEnum.SERVER.getType()){
      //服务中心  是否在使用中
      count = businessInfoDao.getBusinessListByserviceKey(serviceKeys.get(0));
    }else{
      //统筹中心或者审批中心
      count = businessInfoDao.getBusinessListByHandleServiceKey(serviceKeys.get(0));
    }
    if(count > 0){
      return ResultUtils.error("该中心正在使用中，请稍后再试");
    }
    //查询子级服务 servicekey
    List<String> childrenServiceKeys = serviceCenterDao
        .getServiceKeysByParentServiceKeys(serviceKeys);
    if (childrenServiceKeys.size() > 0) {
      logger.info("删除失败，当前中心下存在子级中心，请先删除子级中心");
      return ResultUtils.error("删除失败，当前中心下存在子级中心，请先删除子级中心");
    }
    //查询所属用户
    List<Integer> userIdList = sysUserDao.getUserIdsByServiceIds(serviceKeys);
    if (!CollectionUtils.isEmpty(userIdList) || userIdList.contains(user.getUserId())) {
      logger.info("删除失败，当前中心下有用户，请先删除用户");
      return ResultUtils.error("删除失败，当前中心下有用户，请先删除用户");
    }
    //2.查询服务中心终端状态
    List<String> devIds = vcDevDao.getIdsByServices(serviceKeys);
    if (!CollectionUtils.isEmpty(devIds)) {
      logger.info("删除失败，审中心终端还在使用中，请先解绑终端");
      return ResultUtils.error("删除失败，中心终端还在使用中，请先解绑终端");
    }
    //3.设备
    List<DeviceInfo> deviceList = deviceInfoDao.selectByServiceKeys(serviceKeys);
    if (!CollectionUtils.isEmpty(deviceList)) {
      logger.info("删除失败，中心下有绑定的设备，请先删除设备");
      return ResultUtils.error("删除失败，中心下有绑定的设备，请先删除设备");
    }
    //4.窗口
    List<WindowBean> windowList = windowDao.getWindowByServiceKeys(serviceKeys);
    if (!CollectionUtils.isEmpty(windowList)) {
      logger.info("删除失败，中心下有绑定的窗口，请先删除设备");
      return ResultUtils.error("删除失败，中心下有绑定的窗口，请先删除设备");
    }
    //业务类别
    List<BusinessTypeBean> businessTypeList = businessTypeDao
        .selectBusinessTypeByServiceKeys(serviceKeys);
    if (!CollectionUtils.isEmpty(businessTypeList)) {
      logger.info("删除失败，中心下有绑定的业务关系，请先删除业务");
      return ResultUtils.error("删除失败，中心下有绑定的业务关系，请先删除业务");
    }
    //8.删除服务中心
    serviceCenterDao.deleteServiceCenter(serviceIdArray);


    logger.info("删除服务中心成功");
    return ResultUtils.ok("删除成功");
  }

  /**
   * @param serviceCenter
   * @return Map<String                                                               ,                                                               Object>
   * @Description: 添加服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  @Override
  public Map<String, Object> addServiceCenter(ServiceCenterBean serviceCenter) {
//		private String parentKey ;//父 唯一标识varchar类型
//		private String serviceName ;//服务中心名称
//		private Integer gradeId ;// 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村
//		private String regionId ;//行政区域
//		private String regionAll ;//全部行政区域
//		private String address ;//详细地址
//		private Integer status ;//状态 0：可用 -1:删除  2：不可用
    if (serviceCenter == null || serviceCenter.getServiceName() == null
        || serviceCenter.getType() == null || serviceCenter.getAddress() == null
        || serviceCenter.getState() == null) {
      logger.info("添加服务中心失败，缺少参数");
      return ResultUtils.error("添加失败，缺少参数");
    }
//		if(serviceCenter.getGradeId() != 4 && serviceCenter.getGradeId() != 5 ){
//			logger.info("添加服务中心失败，级别错误");
//			return ResultUtils.error("添加失败，级别错误");
//		}
//		if(serviceCenter.getGradeId() == 5 && serviceCenter.getParentKey() == null ){
//			logger.info("添加服务中心失败，缺少归属服务中心名称");
//			return ResultUtils.error("添加失败，缺少归属服务中心名称");
//		}
    //一镇限制
//		if(serviceCenter.getGradeId() == 4){
//			int count = serviceCenterDao.getServiceCenterCountByGradeId(serviceCenter.getGradeId());
//			if(count > 0){
//				logger.info("添加服务中心失败，目前只支持一个审批中心");
//				return ResultUtils.error("添加失败，目前只支持一个审批中心。");
//			}
//		}
    //一期版本只支持一个统筹中心，一个审批中心
  /*  List<ServiceCenterBean> serviceCenterBeansList = serviceCenterDao.getServiceCentList();
    for (ServiceCenterBean bean : serviceCenterBeansList) {
      if (serviceCenter.getType().equals(bean.getType()) && (bean.getType() == 1)) {
        return ResultUtils.error("添加失败，统筹中心只能添加一个");
      }
      if (serviceCenter.getType().equals(bean.getType()) && (bean.getType() == 2)) {
        return ResultUtils.error("添加失败，审批中心只能添加一个");
      }
    }*/

    //校验是否同名
    ServiceCenterBean serviceCenterByName = serviceCenterDao
        .getServiceCenterByName(serviceCenter.getServiceName());
    if (serviceCenterByName != null) {
      logger.info("添加服务中心失败，名称已存在");
      return ResultUtils.error("添加失败，名称已存在");
    }
      if (!StringUtil.isEmpty(serviceCenter.getLal())) {
          ServiceCenterBean serviceCenterByLal = serviceCenterDao.getServiceCenterByLal(serviceCenter.getLal());
          if (serviceCenterByLal != null) {
              if (!serviceCenterByLal.getServiceId().equals(serviceCenter.getServiceId())) {
                  logger.info("添加服务中心失败，该经纬度已经存在");
                  return ResultUtils.error("添加失败，该经纬度已经存在");
              }
          }
      }
    //校验中心是否可用,统筹中心不校验
    if(!serviceCenter.getType().equals(CommonConstant.CENTER_ONE)){
      if (checkIsExist(serviceCenter)) {
        return ResultUtils.error("添加失败，父级中心不存在");
      }
    }
//    //如果是服务中心，需要校验x86url
//    if(serviceCenter.getType().equals(CommonConstant.CENTER_THREE)){
//      if(serviceCenter.getX86Url() == null){
//        return ResultUtils.error("添加失败，x86Url不能为空");
//      }
//      if(!serviceCenter.getX86Url().matches(RegularExpression.HTTP_IP_PORT)){
//        return ResultUtils.error("添加失败，x86Url格式不正确");
//      }
//    }
    //统筹中心的归属设成0
//		if( serviceCenter.getType() == 1){
//			serviceCenter.setParentKey("0");
//		}else{
//			serviceCenter.setParentKey(serviceCenter.getParentKey());
//		}
    //生成唯一标识
    String serviceKey = CommonConstant.SYSTEM_PREFIX + UUIDGenerator.getJavaUuid();
    serviceCenter.setServiceKey(serviceKey);
    //添加
    int addResult = serviceCenterDao.addServiceCenter(serviceCenter);
    if (addResult == 0) {
      logger.info("添加服务中心失败");
      return ResultUtils.error("添加失败");
    }
    // TODO 添加对应的号码生成       j
//		if(serviceCenter.getGradeId() == 5){
////			NumberIteration numberIteration = new NumberIteration();
////			numberIteration.setServiceKey(serviceKey);
////			numberIteration.setType(CommonConstant.PUBLIC_TYPE);
////			numberIterationDao.addNumberIteration(numberIteration);

////			numberIteration.setType(CommonConstant.SOCIAL_TYPE);
////			numberIterationDao.addNumberIteration(numberIteration);
////		}

    logger.info("添加服务中心成功");
    return ResultUtils.ok("添加成功");
  }

  /**
   * @param serviceCenter
   * @return Map<String                                                               ,                                                               Object>
   * @Description: 修改服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  @Override
  public Map<String, Object> updateServiceCenter(
      ServiceCenterBean serviceCenter) {
    if (serviceCenter == null || serviceCenter.getServiceId() == null) {
      logger.info("修改服务中心失败，缺少参数");
      return ResultUtils.error("修改失败，缺少参数");
    }
    ServiceCenterBean CenterBean = serviceCenterDao.selecServiceCenterBeantByPrimaryKey(serviceCenter.getServiceId());
    Integer count = 0;
    if(CenterBean.getType().intValue() == ServiceCenterTypeEnum.SERVER.getType()){
      //服务中心  是否在使用中
      count = businessInfoDao.getBusinessListByserviceKey(CenterBean.getServiceKey());
    }else{
      //统筹中心或者审批中心
      count = businessInfoDao.getBusinessListByHandleServiceKey(CenterBean.getServiceKey());
    }
    if(count > 0){
      return ResultUtils.error("该中心正在使用中，请稍后再试");
    }
    //校验是否同名
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenterByName(serviceCenter.getServiceName());
    if(serviceCenterBean != null){
      if(!serviceCenterBean.getServiceId().equals(serviceCenter.getServiceId())){
        logger.info("修改服务中心失败，名称已存在");
        return ResultUtils.error("修改失败，名称已存在");
      }
    }
    //校验中心是否可用,统筹中心不校验
    if(!serviceCenter.getType().equals(CommonConstant.CENTER_ONE)){
      if (checkIsExist(serviceCenter)) {
        return ResultUtils.error("编辑失败，父级中心不存在");
      }
    }
      if (!StringUtil.isEmpty(serviceCenter.getLal())) {
          ServiceCenterBean serviceCenterByLal = serviceCenterDao.getServiceCenterByLal(serviceCenter.getLal());
          if (serviceCenterByLal != null) {
              if (!serviceCenterByLal.getServiceId().equals(serviceCenter.getServiceId())) {
                  logger.info("修改服务中心失败，该经纬度已经存在");
                  return ResultUtils.error("修改失败，该经纬度已经存在");
              }
          }
      }
//    //如果是服务中心，需要校验x86url
//    if(serviceCenter.getType().equals(CommonConstant.CENTER_THREE)){
//      if(serviceCenter.getX86Url() == null){
//        return ResultUtils.error("添加失败，x86Url不能为空");
//      }
//      if(!serviceCenter.getX86Url().matches(RegularExpression.HTTP_IP_PORT)){
//        return ResultUtils.error("添加失败，x86Url格式不正确");
//      }
//    }

    //修改
    int updateResult = serviceCenterDao.updateServiceCenter(serviceCenter);
    if (updateResult == 0) {
      logger.info("修改服务中心失败");
      return ResultUtils.error("修改失败");
    }
    //根据主键ID获取中心数据
    ServiceCenterBean centerBean = serviceCenterDao.selecServiceCenterBeantByPrimaryKey(serviceCenter.getServiceId());
    if(null != centerBean.getType()){
      if(ServiceCenterTypeEnum.SERVER.getType().equals(centerBean.getType().intValue())){
        //根据村服务中心，获取连接叫号机，获取所有部门中的最新一个处理中的号
        String proceing  = businessInfoService.getCallNumberForAndroid(centerBean.getServiceKey(),0) ;
        businessInfoService.getSendMessageForAndroid(centerBean.getServiceKey(),proceing);
      }
    }
    logger.info("修改服务中心成功");
    return ResultUtils.ok("修改成功");
  }
  
  private boolean checkIsExist(ServiceCenterBean serviceCenter) {
    //校验中心是否可用
    ServiceCenterBean parentServiceCenter = serviceCenterDao
        .getServiceCenterByServiceKey(serviceCenter.getParentKey());
    if(parentServiceCenter == null || StateEnum.Invalid.getValue().equals(parentServiceCenter.getState())){
      logger.info("添加失败，父级中心不存在");
      return true;
    }
    return false;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> queryMapList(ServiceCenterBean entity, Integer pageNum,
      Integer pageSize) {

    Map<String, Object> resultMap = new HashMap<String, Object>();
    List<ServiceCenterBean> list = serviceCenterDao.queryList(entity);
    List<Map<String, Object>> changeList = (List<Map<String, Object>>) JSON
        .parse(JSON.toJSONString(list));
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> map : changeList) {
      map.put("name", map.get("serviceName"));
      resultList.add(map);
    }
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<ServiceCenterBean> centerPageList = serviceCenterDao.getCenterListByParams(entity);
    PageInfo<ServiceCenterBean> pageInfo = new PageInfo<ServiceCenterBean>(centerPageList);
    resultMap.put("centerList", resultList);
    resultMap.put("pageInfo", pageInfo);
    return resultMap;
  }

  /**
   * 查询所有中心
   *
   * @param regionKey
   * @return
   * @author JLM
   */
  @Override
  public Map<String, Object> queryCenterList(String regionKey) {
    List<ServiceCenterBean> list = serviceCenterDao.queryCenterList(regionKey);
    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("centerlist", list);
    return resultMap;
  }

  @Override
  public Map<String, Object> getPageCenterList(Map<String, Object> params) {
    List<ServiceCenterBean> centerList = new ArrayList<>();
    ServiceCenterBean serviceCenterBean = new ServiceCenterBean();
    serviceCenterBean.setState(Integer.valueOf(params.get("state").toString()));
    serviceCenterBean.setType(Long.valueOf(params.get("type").toString()));
    if (params.get("parentKey") != null) {
      serviceCenterBean.setParentKey(params.get("parentKey").toString());
    }
    if (params.get("serviceKey") != null) {
      serviceCenterBean.setServiceKey(params.get("serviceKey").toString());
    }
    if (params.get("pageNum") != null && params.get("pageSize") != null) {
      PageHelper.startPage(Integer.valueOf(params.get("pageNum").toString()),
          Integer.valueOf(params.get("pageSize").toString()));
      centerList = serviceCenterDao.getServiceCenterByCondition(serviceCenterBean);
      logger.info("获取中心列表成功");
      PageInfo<ServiceCenterBean> pageInfo = new PageInfo<>(centerList);
      return ResultUtils.ok("获取中心列表成功", pageInfo);
    } else {
      centerList = serviceCenterDao.getServiceCenterByCondition(serviceCenterBean);
      logger.info("获取中心列表成功");
      return ResultUtils.ok("获取中心列表成功", centerList);
    }

  }

  @Override
  public Map<String, Object> getVerifyCenterList(Map<String, Object> paramMap) {
    List<ServiceCenterBean> verifyCenterList = serviceCenterDao.getVerifyCenterList(paramMap);
    logger.info("获取审批下拉列表成功");
    return ResultUtils.ok("获取中心列表成功", verifyCenterList);
  }

  @Override
  public Map<String, Object> getServiceCenterListByParentKey(Map<String, Object> params) {
    List<String> parentKeys = (List<String>) params.get("parentKeys");
    if(CollectionUtils.isEmpty(parentKeys)){
      return ResultUtils.error("父级servicekey不能为空");
    }
    Integer pageNum = (Integer) params.get("pageNum");
    Integer pageSize = (Integer) params.get("pageSize");
    PageHelper.startPage(pageNum, pageSize);
    List<ServiceCenterBean> list = serviceCenterDao.getServiceCenterListByParentKey(parentKeys);
    PageInfo<ServiceCenterBean> pageInfo = new PageInfo<>(list);
    return ResultUtils.ok("分页查询审批中心下所有的服务中心成功", pageInfo);
  }

  @Override
  public Map<String, Object> getServiceCenterTree(Boolean flag) {
    if (flag == null) {
      return ResultUtils.error("参数缺失");
    }
    if (flag) {
      ServiceCenterBean serviceCenterBean = new ServiceCenterBean();
      serviceCenterBean.setState(-2);
      if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SUPER_ADMIN)) {
        serviceCenterBean.setType(1L);
        List<ServiceCenterBean> firstList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        for (ServiceCenterBean first : firstList) {
          serviceCenterBean.setType(2L);
          serviceCenterBean.setParentKey(first.getServiceKey());
          List<ServiceCenterBean> secondList = serviceCenterDao
              .getServiceCenterByCondition(serviceCenterBean);
          first.setChildren(secondList);
          for (ServiceCenterBean second : secondList) {
            serviceCenterBean.setType(3L);
            serviceCenterBean.setParentKey(second.getServiceKey());
            List<ServiceCenterBean> thirdList = serviceCenterDao
                .getServiceCenterByCondition(serviceCenterBean);
            second.setChildren(thirdList);
          }
        }
        return ResultUtils.ok("获取树形数据成功", firstList);
      }
      //统筹中心管理员
      if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.FIRST_ADMIN)) {
        serviceCenterBean.setType(2L);
        serviceCenterBean.setParentKey(ShiroUtils.getUserEntity().getServiceKey());
        List<ServiceCenterBean> secondList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        for (ServiceCenterBean second : secondList) {
          serviceCenterBean.setType(3L);
          serviceCenterBean.setParentKey(second.getServiceKey());
          List<ServiceCenterBean> thirdList = serviceCenterDao
              .getServiceCenterByCondition(serviceCenterBean);
          second.setChildren(thirdList);
        }
        ServiceCenterBean serviceCenter = serviceCenterDao
            .getServiceCenter(ShiroUtils.getUserEntity().getServiceKey());
        serviceCenter.setChildren(secondList);
        List<ServiceCenterBean> list = new ArrayList<>();
        list.add(serviceCenter);
        return ResultUtils.ok("获取树形数据成功", list);
      }
      //审批中心管理员
      if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SECOND_ADMIN)) {
        SysDeptBean sysDeptBean = new SysDeptBean();
        serviceCenterBean.setType(3L);
        serviceCenterBean.setParentKey(ShiroUtils.getUserEntity().getServiceKey());
        List<ServiceCenterBean> thirdList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        ServiceCenterBean serviceCenter = serviceCenterDao
            .getServiceCenter(ShiroUtils.getUserEntity().getServiceKey());
        serviceCenter.setChildren(thirdList);
        List<ServiceCenterBean> list = new ArrayList<>();
        list.add(serviceCenter);
        return ResultUtils.ok("获取树形数据成功", list);
      }
    }
    if (!flag) {
      ServiceCenterBean serviceCenterBean = new ServiceCenterBean();
      //-2查询非删除状态数据
      serviceCenterBean.setState(-2);
      serviceCenterBean.setType(1L);
      List<ServiceCenterBean> firstList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      for (ServiceCenterBean first : firstList) {
        serviceCenterBean.setType(2L);
        serviceCenterBean.setParentKey(first.getServiceKey());
        List<ServiceCenterBean> secondList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        first.setChildren(secondList);
        for (ServiceCenterBean second : secondList) {
          serviceCenterBean.setType(3L);
          serviceCenterBean.setParentKey(second.getServiceKey());
          List<ServiceCenterBean> thirdList = serviceCenterDao
              .getServiceCenterByCondition(serviceCenterBean);
          second.setChildren(thirdList);
        }
      }
      return ResultUtils.ok("获取树形数据成功", firstList);
    }

    return ResultUtils.error("获取树形数据失败，用户类型错误");
  }

  @Override
  public Map<String, Object> getServiceCenterByServiceCenterKey(String serviceCenterKey) {
    ServiceCenterBean serviceCenter = serviceCenterDao
        .getServiceCenterByServiceKey(serviceCenterKey);
    //获取中心的区域名称
    areaService.getAreaName(serviceCenter);
    logger.info("根据servicekey查询服务中心:" + serviceCenter);
    return ResultUtils.check(serviceCenter);
  }

  @Override
  public Map<String, Object> getSecondServiceCenterAndChildren() {
    ServiceCenterBean serviceCenterBean = new ServiceCenterBean();
    serviceCenterBean.setState(StateEnum.Effective.getValue());
    //超管 看见所有中心
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SUPER_ADMIN)) {
      serviceCenterBean.setType(1L);
      List<ServiceCenterBean> firstList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      for (ServiceCenterBean first : firstList) {
        serviceCenterBean.setType(2L);
        serviceCenterBean.setParentKey(first.getServiceKey());
        List<ServiceCenterBean> secondList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        first.setChildren(secondList);
        for (ServiceCenterBean second : secondList) {
          serviceCenterBean.setType(3L);
          serviceCenterBean.setParentKey(second.getServiceKey());
          List<ServiceCenterBean> thirdList = serviceCenterDao
              .getServiceCenterByCondition(serviceCenterBean);
          second.setChildren(thirdList);
        }
      }
      return ResultUtils.ok("获取中心列表成功", firstList);
    }
    //统筹中心管理员看见本中心以下
    //bug864
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.FIRST_ADMIN)) {
      serviceCenterBean.setType(2L);
      serviceCenterBean.setParentKey(ShiroUtils.getUserEntity().getServiceKey());
      List<ServiceCenterBean> secondList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      for (ServiceCenterBean second : secondList) {
        serviceCenterBean.setType(3L);
        serviceCenterBean.setParentKey(second.getServiceKey());
        List<ServiceCenterBean> thirdList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        second.setChildren(thirdList);
      }
      ServiceCenterBean self = serviceCenterDao
          .getServiceCenter(ShiroUtils.getUserEntity().getServiceKey());
      self.setChildren(secondList);
      List<ServiceCenterBean> list = new ArrayList<>();
      list.add(self);
      return ResultUtils.ok("获取中心列表成功", list);
    }
    //审批中心管理员看见本中心以下所有部门数据
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SECOND_ADMIN)) {
      serviceCenterBean.setType(3L);
      serviceCenterBean.setParentKey(ShiroUtils.getUserEntity().getServiceKey());
      List<ServiceCenterBean> thirdList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      ServiceCenterBean serviceCenter = serviceCenterDao
          .getServiceCenter(ShiroUtils.getUserEntity().getServiceKey());
      serviceCenter.setChildren(thirdList);
      List<ServiceCenterBean> list = new ArrayList<>();
      list.add(serviceCenter);
      return ResultUtils.ok("获取中心列表成功", list);
    }
    return ResultUtils.error("用户类型错误");
  }
  @Override
  public Map<String, Object> getServiceCenterByUser(boolean flage){
    SysUserBean userBean = ShiroUtils.getUserEntity();
    if(flage){
      List<ServiceCenterBean> list = serviceCenterDao.getServiceCenterByUser("");
      return ResultUtils.ok("获取中心列表成功", list);
    }

    if(SysUserTypeEnum.Admin.getValue().equals(userBean.getType())){
      List<ServiceCenterBean> list = serviceCenterDao.getServiceCenterByUser("");
      return ResultUtils.ok("获取中心列表成功", list);
    }else if(!SysUserTypeEnum.Admin.getValue().equals(userBean.getType())){
      List<ServiceCenterBean> list = serviceCenterDao.getServiceCenterByUser(userBean.getServiceKey());
      return ResultUtils.check("获取中心列表成功", list);
      }

    return ResultUtils.error("获取中心列表失败");
  }

  @Override
  public List<ServiceCenterBean> getServiceCenterStatus(List<ServiceCenterBean> list) {
    if (!CollectionUtils.isEmpty(list)) {
      ArrayList<ServiceCenterBean> statusList = new ArrayList<>();
      List<String> serviceKeyList = list.stream().map(
              ServiceCenterBean::getServiceKey).collect(Collectors.toList());
      //办理中
      List<ServiceCenterBean> doingServiceCenterList = serviceCenterDao.getDoingStatus(
              serviceKeyList);
      setServiceStatus(doingServiceCenterList, ServiceCenterStatusEnum.Doing.getStatus());
      statusList.addAll(doingServiceCenterList);
        serviceKeyList.removeAll(statusList.stream().map(
                ServiceCenterBean::getServiceKey).collect(Collectors.toList()));
      //等待中
        if (!CollectionUtils.isEmpty(serviceKeyList)) {
            List<ServiceCenterBean> waitServiceCenterList = serviceCenterDao.getWaitingStatus(
                    serviceKeyList);
            setServiceStatus(waitServiceCenterList, ServiceCenterStatusEnum.Waiting.getStatus());
            statusList.addAll(waitServiceCenterList);
            //空闲中
            serviceKeyList.removeAll(statusList.stream().map(
                    ServiceCenterBean::getServiceKey).collect(Collectors.toList()));
        }
      if (!CollectionUtils.isEmpty(serviceKeyList)) {
        List<ServiceCenterBean> freeServiceCenterList = serviceCenterDao.getServiceCenterListByServiceKeys(serviceKeyList);
        setServiceStatus(freeServiceCenterList, ServiceCenterStatusEnum.Free.getStatus());
        statusList.addAll(freeServiceCenterList);
      }
      return statusList;
    }
    return list;
  }

  private void setServiceStatus(List<ServiceCenterBean> list, Integer status) {
    if (!CollectionUtils.isEmpty(list)) {
      for (ServiceCenterBean bean : list) {
        bean.setStatus(status);
      }
    }
  }
}
