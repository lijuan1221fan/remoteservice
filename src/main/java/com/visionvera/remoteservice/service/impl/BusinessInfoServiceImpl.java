package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.annonation.ResourceFilter;
import com.visionvera.common.enums.AndroidBusinessTypeEnum;
import com.visionvera.common.enums.AndroidShowTypeEnum;
import com.visionvera.common.enums.BusinessInfoStateEnum;
import com.visionvera.common.enums.BusinessTypeIsComprehensiveEnum;
import com.visionvera.common.enums.FinalStateEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysDeptTypeEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.enums.SysUserWorkStateEnum;
import com.visionvera.common.enums.VcDevStateEnum;
import com.visionvera.common.enums.WSWebReturnEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.BusinessLog;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.ClientInfo;
import com.visionvera.remoteservice.bean.PhotoBean;
import com.visionvera.remoteservice.bean.PhotoConfig;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.StorageResBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UnattendedCause;
import com.visionvera.remoteservice.bean.UserSortVo;
import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.common.sms.SendsmsTask;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.constant.LogEnums;
import com.visionvera.remoteservice.constant.PictureConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.BusinessLogDao;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.ClientInfoDao;
import com.visionvera.remoteservice.dao.DeviceInfoDao;
import com.visionvera.remoteservice.dao.MaterialsDao;
import com.visionvera.remoteservice.dao.PhotoBeanDao;
import com.visionvera.remoteservice.dao.PhotoConfigDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.StorageResDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.UnattendedCauseDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.pojo.BusinessVo;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.BusinessLogService;
import com.visionvera.remoteservice.service.BusinessTypeService;
import com.visionvera.remoteservice.service.MeetingService;
import com.visionvera.remoteservice.service.NumberService;
import com.visionvera.remoteservice.util.DateUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.ws.AcceptBusinessWebSocketHandler;
import com.visionvera.remoteservice.ws.AndroidWebSocketHandler;
import com.visionvera.remoteservice.ws.CallDeviceWebSocketHandler;
import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import com.visionvera.remoteservice.ws.H5WebSocketHandler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;


/**
 * @author lijintao
 * @date
 */
@Service
public class BusinessInfoServiceImpl implements BusinessInfoService {

  private final Logger logger = LoggerFactory.getLogger(BusinessInfoServiceImpl.class);
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;
  @Resource
  private VcDevDao vcDevDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private WindowDao windowDao;
  @Autowired
  private BusinessLogService businessLogService;
  @Resource
  private ClientInfoDao clientInfoDao;
  @Resource
  private MeetingService meetingService;
  @Resource
  private UnattendedCauseDao unattendedCauseDao;
  @Resource
  private BusinessTypeDao businessTypeDao;
  @Resource
  private PhotoConfigDao photoConfigDao;
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private DeviceInfoDao deviceInfoDao;
  @Resource
  private MaterialsDao materialsDao;
  @Value("${temp.save.downloadpath}")
  private String picpath;
  @Autowired
  @Qualifier("taskExecutorPool")
  private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
  @Value("${sendSms.sleep}")
  protected Long sleepTime;
  @Resource
  private BusinessInfoService businessInfoService;
  @Resource
  private NumberService numberService;
  @Resource
  private SendsmsTask sendsmsTask;
  @Resource
  private AndroidWebSocketHandler androidWebSocketHandler;
  @Resource
  private H5WebSocketHandler h5WebSocketHandler;

  @Resource
  private AcceptBusinessWebSocketHandler acceptBusinessWebSocketHandler;
  @Resource
  private BusinessTypeService businessTypeService;
  @Value("${temp.save.docpath}")
  private String tempPath;
  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
  @Resource
  private PhotoBeanDao photoBeanDao;
  @Resource
  private CallDeviceWebSocketHandler callDeviceWebSocketHandler;
  @Resource
  private BusinessLogDao businessLogDao;
  @Resource
  private StorageResDao storageResDao;
  /**
   * 业务变更获取支持的业务类型
   *
   * @param deptId
   * @return 支持的业务类型
   * @auther ljfan
   */
  @Override
  public Map<String, Object> getBusinessTypeByDeptId(Integer deptId) {
    SysUserBean userBean = ShiroUtils.getUserEntity();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(userBean.getServiceKey());
    List<String> serviceKeyList = new ArrayList<>();
    BusinessTypeBean businessType = new BusinessTypeBean();
    if (SysUserTypeEnum.AuditCenterSalesman.getValue().equals(userBean.getType())) {
      businessType.setServiceKey(userBean.getServiceKey());
      businessType.setServiceKeys(null);
    } else {
      serviceKeyList.add(serviceCenter.getParentKey());
      serviceKeyList.add(userBean.getServiceKey());
      businessType.setServiceKey(null);
    }
    businessType.setDeptId(deptId);
    //取得业务详情为非空的业务类别
    businessType.setOfferNumberCheck(1);
    List<BusinessTypeBean> businessTypeBean = businessTypeDao.getBusinessTypeByDeptId(businessType);
    //受理端获取坐标位置
    if (businessTypeBean.size() > 0) {
      for (BusinessTypeBean bean : businessTypeBean) {
        //查询签名/盖章自定义位置
        businessTypeService.setBusinessTypeBeanForPhotoConfig(bean);
      }
    }
    return ResultUtils.ok("取得业务类型成功", businessTypeBean);

  }

  /**
   * 业务变更选择业务类型
   *
   * @param businessVo
   * @return 选择业务类型
   * @auther ljfan
   */
  @Override
  public Map<String, Object> getBussinessInfoList(BusinessVo businessVo) {
    SysUserBean userBean = ShiroUtils.getUserEntity();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(userBean.getServiceKey());
    List<String> serviceKeyList = new ArrayList<>();
    BusinessTypeBean businessType = new BusinessTypeBean();
    if (SysUserTypeEnum.AuditCenterSalesman.getValue().equals(userBean.getType())) {
      businessType.setServiceKey(userBean.getServiceKey());
      businessType.setServiceKeys(null);
      businessType.setIsComprehensive(BusinessTypeIsComprehensiveEnum.Yes.getValue());
    } else {
      //查询出非仅统筹办理业务
      businessType.setIsComprehensive(0);
      serviceKeyList.add(serviceCenter.getParentKey());
      serviceKeyList.add(userBean.getServiceKey());
      businessType.setServiceKeys(serviceKeyList);
      businessType.setServiceKey(null);
    }
    businessType.setDeptId(businessVo.getDeptId());
    businessType.setParentId(businessVo.getBusinessTypeId());
    businessType.setId(businessVo.getBusinessType());
    List<BusinessTypeBean> businessTypeBean = businessTypeDao.getBusinessInfoList(businessType);
    if (null == businessTypeBean) {
      return ResultUtils.error("未取到业务数据");
    }
    List<BusinessTypeBean> list = businessTypesOfDayOrMonth(businessTypeBean);
    if (list.size() == CommonConstant.zero) {
      return ResultUtils.error("该业务类型没有支持的可变更业务");
    }
    return ResultUtils.ok("查询成功", list);

  }

  /**
   * 业务变更选择业务
   *
   * @param id, describes, type
   * @return 根据已获取的业务信息，更新业务
   * @auther ljfan date 2018/11/05
   */
  @Override
  public Map<String, Object> changeBusinessInfo(Integer id, String describes,
      Integer businessType, String cardName, String cardId) {
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(id);
    if (businessInfo == null) {
      logger.error("业务详情不存在");
      throw new RuntimeException("业务详情不存在");
    }
    if (describes == null) {
      logger.error("业务不存在");
      throw new RuntimeException("业务不存在");
    }
    if (businessType == null) {
      logger.error("业务不存在");
      throw new RuntimeException("业务类型不存在");
    }
    BusinessTypeBean businessTypeBean = businessTypeDao.selectByPrimaryKey(businessType);
    if(null == businessTypeBean){
      logger.error("变更业务不存在");
      throw new RuntimeException("变更业务不存在");
    }
      if (businessInfo.getTerminalNumber() != null) {
          VcDevBean device = vcDevDao.getDeviceById(businessInfo.getTerminalNumber());
          if (device != null) {
              businessInfo.setVcDevType(device.getTypeId());
          }
      }
    businessInfo.setStartTime(StringUtil.dateString(new Date()));
    businessInfo.setState(BusinessInfoStateEnum.Processing.getValue());
    businessInfo.setBusinessTypeName(describes);
    businessInfo.setBusinessType(businessType);
    businessInfo.setDeptId(businessTypeBean.getDeptId());
    businessInfoDao.updateByPrimaryKeySelective(businessInfo);
    getBusinessInfoType(businessInfo);
    SysDeptBean deptBean = sysDeptBeanDao.selectByPrimaryKey(businessInfo.getDeptId());
    businessInfo.setNumberPrefix(deptBean.getNumberPrefix());
    //删除之前的操作记录
    businessLogDao.deleteByBusinessId(businessInfo.getId());
    List<StorageResBean> storageResBeans= storageResDao.selectStorageListByBusinessId(businessInfo.getId());
    if(CollectionUtils.isNotEmpty(storageResBeans)){
      for(StorageResBean bean:storageResBeans){
        storageResDao.deleteByPrimaryKey(bean.getFileId());
      }
      storageResDao.deleteByBusinessId(businessInfo.getId());
    }
    //查询出当前业务相关业务图片
    List<PhotoBean> photoBeans = photoBeanDao.selectByBusinessId(businessInfo.getId());
    if (photoBeans.size() > 0) {
      for (PhotoBean photoBean : photoBeans) {
        photoBeanDao.deleteByPrimaryKey(photoBean.getId());
      }
      logger.info("删除上一个业务的拍摄材料");
    }
      businessLogService.insertLog(businessInfo.getId(), LogEnums.BUSINESS_PROCESS.getCode(),
          LogEnums.BUSINESS_PROCESS.getValue(), null);
    return ResultUtils.ok("获取任务成功。", businessInfo);

  }

  /**
   * 身份证下一步接口
   *
   * @param businessId
   * @param clientInfo
   * @return
   */
  @Override
  public Map<String, Object> nextStepAndModify(Integer businessId, ClientInfo clientInfo) {
    // 1查询业务状态
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if (businessInfo == null || businessInfo.getState()
        .equals(BusinessInfoStateEnum.Processed.getValue())) {
      logger.info("下一步失败。业务已经完结。");
      return ResultUtils.error("下一步失败。业务已经完结。");
    }
    // 2保存客户信息
    saveClientInfo(businessId, clientInfo);

    logger.info("下一步成功。");
    return ResultUtils.ok("下一步成功");
  }

  private void saveClientInfo(Integer businessId, ClientInfo clientInfo) {
    if (clientInfo != null && StringUtil.isNotNull(clientInfo.getUsername())
        && StringUtil.isNotNull(clientInfo.getIdcard())
        && clientInfo.getBirthDate() != null
        && StringUtil.isNotNull(clientInfo.getSex())
        && StringUtil.isNotNull(clientInfo.getAddress())) {
      ClientInfo result = clientInfoDao.selectByIdcard(clientInfo.getIdcard());
      if (result != null) {
        //客户信息存在 更新最新数据                                 
        clientInfo.setId(result.getId());
        clientInfoDao.updateByPrimaryKeySelective(clientInfo);
      } else {
        // 客户信息插入数据库
        clientInfoDao.insertSelective(clientInfo);
      }
      // 客户信息与业务信息关联插入
      if (clientInfo.getEducationId() != null) {
        //同一个人时，学历关系删除，重新更新学历关系
        clientInfoDao.deleteByClientId(businessId, clientInfo.getId());
        clientInfoDao
            .addClientInfoRelevance(businessId, clientInfo.getId(), clientInfo.getEducationId(),
                clientInfo.getPhone());
      }
    }
  }

  /**
   * 完成业务接口
   *
   * @param businessId 业务id
   * @param finalState 最终状态
   * @param causeIds 未处理原因id ，多个中间用逗号隔开
   * @param remarks 描述
   * @param clientInfo 客户信息
   * @param user 登录用户
   * @return
   */
  @Override
  public Map<String, Object> completeBusinessAndModify(Integer businessId, Integer finalState,
      String causeIds, String remarks, ClientInfo clientInfo, SysUserBean user) {
    // 1验证业务状态
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if (businessInfo == null || businessInfo.getState()
        .equals(BusinessInfoStateEnum.Processed.getValue())) {
      logger.info("业务已经完结。");
      return ResultUtils.exeptionOut(false,WSWebReturnEnum.EXCEPTION_END.getCode(),
          WSWebReturnEnum.EXCEPTION_END.getName());
    }
    // 2停会
    Map<String, Object> stopMeetingAndModify = meetingService.stopMeetingAndModify(businessId);
    if (!(boolean) stopMeetingAndModify.get("result")) {
      logger.info("保存业务失败 " + stopMeetingAndModify.get("msg"));
      return ResultUtils.error("保存业务失败。" + stopMeetingAndModify.get("msg"));
    }
    //3 保存用户信息
    this.saveClientInfo(businessId, clientInfo);
    BusinessInfo business = new BusinessInfo();
    business.setId(businessId);
    business.setFinalState(finalState);
    if (StringUtil.isNotNull(remarks)) {
      business.setRemarks(remarks);
    }
    business.setState(BusinessInfoStateEnum.Processed.getValue());
    business.setEndTime(StringUtil.dateString(new Date()));
    //3.1.保存 未处理原因
    if (finalState != null && finalState == 2 && StringUtil.isNotNull(causeIds)) {
      String[] causeIdArray = causeIds.split(",");
      int insertResult = unattendedCauseDao.insertBusinessUnattendedRel(businessId, causeIdArray);
      logger.info("插入业务和未处理原因的条数：" + insertResult);
    }
    int result = businessInfoDao.updateByPrimaryKeySelective(business);
    if (result <= 0) {
      logger.info("保存业务失败，修改业务信息失败。");
    }
    updateUserState(user, CommonConstant.WORK_STATE_WAIT);
    //修改村终端状态
    updateVcDevState(businessInfo.getTerminalNumber(), VcDevStateEnum.Effective.getValue());
    logger.info("动态入会终端" + JSON.toJSONString(CommonConstant.dynDelMap));
    String dynDeviceNumber = CommonConstant.dynDelMap.get(businessId);
    if (null != dynDeviceNumber) {
      //修改动态入会得终端状态
      logger.info("存在动态入会终端，修改动态入会终端状态");
      updateVcDevState(dynDeviceNumber, VcDevStateEnum.Effective.getValue());
    }
    //8.插入业务日志
    businessLogService.insertLog(businessId, LogEnums.BUSINESS_COMPLETED.getCode(),
        LogEnums.BUSINESS_COMPLETED.getValue(), null);

    //9.修改任务负载量
    if (CommonConstant.taskNumber.get() < CommonConstant.MAX_TASKS.get()) {
      CommonConstant.taskNumber.incrementAndGet();
    }

      //将用户对应窗口置为等待状态
//      WindowBean window = windowDao.getUserWindowByUserId(user.getUserId());
//      windowDao.updateWindowUseStatus(window.getId(), WindowUseStateEnum.Wait.getValue());
    windowDao.updateWindowUseStatusByUserId(user.getUserId());

    String androidBusinessType = AndroidBusinessTypeEnum.DepartmentalProcess.getValue();
    if (user.getUserId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
      androidBusinessType = AndroidBusinessTypeEnum.AllProcess.getValue();
    }
    businessInfoService.popBusinessInfo(user.getServiceKey());
    logger.info("结束业务，开始推号");
    numberService.sendMessageAll(businessInfo.getServiceKey(), businessInfo.getDeptId());
    //收到业务直接结束业务办理时，应该将发短信给业务员的线程任务取消掉
    sendsmsTask.cancelSmsThreadTask(businessId);
    logger.info("结束业务，结束推号");
    logger.info("保存业务成功。");
    return ResultUtils.ok("保存业务成功。");
  }

  /***
   *
   *  查看业务列表
   * @param businessVo 查询对象
   * @return
   */
  @Override
  @ResourceFilter(subCenter = true, user = true)
  public Map<String, Object> getBusinessList(BusinessVo businessVo) {
    Integer pageNum = businessVo.getPageNum();
    Integer pageSize = businessVo.getPageSize();
    List<String> causeList = null;
    BusinessInfo businessInfo = new BusinessInfo();
    businessInfo.setDeptId(businessVo.getDeptId());
    businessInfo.setDescribes(businessVo.getDescribes());
    businessInfo.setStartTime(businessVo.getStartTime());
    businessInfo.setEndTime(businessVo.getEndTime());
    businessInfo.setState(businessVo.getState());
    businessInfo.setOperatorName(businessVo.getOperatorName());
    businessInfo.setFinalState(businessVo.getFinalStates());
    SysUserBean userBean = ShiroUtils.getUserEntity();
    if(SysUserTypeEnum.Admin.getValue().equals(userBean.getType())){
      businessInfo.setServiceKeys(null);
      businessInfo.setHandleServiceKey(null);
    }else if(SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType())){
      List<String> serviceKeys = serviceCenterDao.getServiceKeysByParentServiceKey(userBean.getServiceKey());
      if(CollectionUtils.isNotEmpty(serviceKeys)){
        serviceKeys.add(userBean.getServiceKey());
      }
      businessInfo.setHandleServiceKey(null);
      businessInfo.setServiceKeys(serviceKeys);
    }else{
      businessInfo.setHandleServiceKey(userBean.getServiceKey());
      businessInfo.setServiceKeys(null);
    }

    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<BusinessInfo> businessInfos = businessInfoDao.getBusinessList(businessInfo);
    //获取业务未办理的原因
    getbusinessUnattendCause(businessInfos);
    for (BusinessInfo entity : businessInfos) {
      List<BusinessLog> businessLogList = businessLogService.getBusinessLogList(entity.getId());
      entity.setBusinessLogList(businessLogList);
      if (entity.getServiceKey() != null) {
        ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(entity.getServiceKey());
        if (serviceCenter != null) {
          entity.setServiceName(serviceCenter.getServiceName());
        }
      }
    }
    Map<String, Object> map = getBusinessState(businessInfo);
    Map<String, Object> returnMap = new HashMap<>();
    PageInfo<BusinessInfo> pageInfo = new PageInfo<>(businessInfos);
    returnMap.put("map", map);
    returnMap.put("pageInfo", pageInfo);
    logger.info("查看业务列表成功");
    return ResultUtils.ok("查看业务列表成功！", returnMap);
  }

  /***
   *
   *  查看业务状态
   * @param businessInfo 查询参数
   * @return
   */
  @Override
  public Map<String, Object> getBusinessState(BusinessInfo businessInfo) {
    Map<String, Object> map = new HashMap<>();
    Integer statused = businessInfoDao.getBusinessByStatesed(businessInfo);
    Integer statusedAndSuccess = businessInfoDao.getBusinessByStatesedAndSuccess(businessInfo);
    Integer statusIng = businessInfoDao.getBusinessByStatesIng(businessInfo);
    Integer statusBe = businessInfoDao.getBusinessByStatesBe(businessInfo);
    map.put("statused", statused);
    map.put("statusedAndSuccess", statusedAndSuccess);
    map.put("statusIng", statusIng);
    map.put("statusBe", statusBe);
    return ResultUtils.ok("查看业务各状态成功！", map);
  }

  /**
   * 查询业务日志列表
   *
   * @param pageNum 页码
   * @param pageSize 条数
   * @param businessId 业务id
   * @return
   */
  @Override
  public Map<String, Object> getBusinessLogList(Integer pageNum, Integer pageSize,
      Integer businessId) {
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<BusinessLog> businessLogList = businessLogService.getBusinessLogList(businessId);
    PageInfo<BusinessLog> pageInfo = new PageInfo<>(businessLogList);
    logger.info("查询业务日志列表成功");
    return ResultUtils.ok("查询业务日志列表成功！", pageInfo);
  }

  @Override
  public Map<String, Object> isSign(Integer businessType) {
    PhotoConfig record = new PhotoConfig();
    record.setBusinessType(businessType);
    List<PhotoConfig> photoConfigs = photoConfigDao.selectByObject(record);
    Map<String, Boolean> map = new HashMap<>(CommonConstant.two);
    map.put("sing", false);
    map.put("stamp", false);
    for (PhotoConfig photoConfig : photoConfigs) {
      if (photoConfig.getType() == CommonConstant.SIGN_LOCATION) {
        map.put("sing", true);
      } else if (photoConfig.getType() == CommonConstant.STAMP_LOCATION) {
        map.put("stamp", true);
      }
    }
    return ResultUtils.ok("获取成功", map);
  }


  /**
   * 暂停受理（暂停业务）
   *
   * @param businessId 业务id
   * @param finalState 最终状态
   * @param causeIds 未处理原因id ，多个中间用逗号隔开
   * @param remarks 描述
   * @param clientInfo 客户信息
   * @param user 登录用户信息
   * @return
   */
  @Override
  public Map<String, Object> pauseToDealAndModify(Integer businessId, Integer finalState,
      String causeIds, String remarks, ClientInfo clientInfo, SysUserBean user) {
    if (businessId != null && finalState != null) {
      Map<String, Object> completionResult = this
          .completeBusinessAndModify(businessId, finalState, causeIds, remarks, clientInfo, user);
      if (!(boolean) completionResult.get("result")) {
        return completionResult;
      }
    }
      updateUserState(user, SysUserWorkStateEnum.Free.getValue());
      //查询业务员对应的窗口
      WindowBean userWindow = windowDao.getUserWindowByUserId(user.getUserId());
      if (userWindow != null) {
          //将窗口置为空闲状态
          windowDao.updateWindowUseStatus(userWindow.getId(), WindowUseStateEnum.Free.getValue());
          //删除用户和窗口关联关系
          windowDao.deleteWindowAndUserRelation(null, user.getUserId());
          //修改窗口终端状态
          VcDevBean vcDev = vcDevDao.getDeviceByWindowId(userWindow.getId());
          if (vcDev != null) {
              updateVcDevState(vcDev.getId(), VcDevStateEnum.Effective.getValue());
          }
    }
    //终止办理时，将业务员移除redis
    //场景：当业务员没有业务办理时，点终止办理时，业务员仍然在redis中
    String waitQueueKey =CommonConstant.WAITING_WORK_KEY+user.getServiceKey()+":"+user.getDeptId();
    UserSortVo vo = businessInfoService.getUserSortVo(user.getUserId());
    if (vo != null) {
      redisUtils.listRemove(waitQueueKey,0,JSON.toJSONString(vo));
    }
    logger.info("用户：" + user.getUserId() + "暂停业务成功。");
    return ResultUtils.ok("暂停业务成功。");

  }

  /**
   * 根据业务id查询业务信息
   *
   * @param businessId 业务Id
   * @return
   */
  @Override
  public BusinessInfo selectByPrimaryKey(Integer businessId) {
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    return businessInfo;
  }

  /**
   * 根据用户id获取当前用户未结束的任务
   *
   * @param userId 用户id
   * @return
   */
  @Override
  public BusinessInfo getBusinessByUserId(Integer userId) {
    BusinessInfo businessByUserId = businessInfoDao.getBusinessByUserId(userId);
    if (businessByUserId == null) {
      return null;
    }
    getBusinessInfoType(businessByUserId);
    return businessByUserId;
  }

  /**
   * 根据业务id获取客户信息
   *
   * @param businessId 业务id
   * @return
   */
  @Override
  public Map<String, Object> getClientInfo(Integer businessId) {
    ClientInfo clientInfo = clientInfoDao.selectByBusinessId(businessId);
    if (null == clientInfo) {
      return ResultUtils.error("不存在该信息");
    }
    return ResultUtils.ok("成功", clientInfo);
  }

  /**
   * 根据业务id获取客户信息
   *
   * @param businessId 业务id
   * @return
   */
  @Override
  public Map<String, Object> getBusinessDetail(Integer businessId) {
    //身份证信息
    ClientInfo clientInfo = clientInfoDao.selectByBusinessId(businessId);
      //获取年龄
      if (clientInfo != null) {
          clientInfo.getAgeByBirthDate();
    }
    //办理明细
    BusinessInfo businessInfo = businessInfoDao.getBusinessInfoDetail(businessId);
    if(businessInfo != null && businessInfo.getHandleServiceKey() != null){
      ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(businessInfo.getHandleServiceKey());
      businessInfo.setHandleServiceName(serviceCenter.getServiceName());
    }
    //获取未办结的原因
    getbusinessUnattendCause(Collections.singletonList(businessInfo));
    //时间线
    List<BusinessLog> businessLogList = businessLogService.getBusinessLogList(businessId);
      if (!CollectionUtils.isEmpty(businessLogList)) {
          businessLogList = businessLogList.stream().sorted(
                  Comparator.comparing(BusinessLog::getCreateTime)).collect(Collectors.toList());
      }
    businessInfo.setBusinessLogList(businessLogList);
    //获取图片列表
      ArrayList<Integer> pictureTypeList = new ArrayList<>();
      pictureTypeList.add(PictureConstant.AUTOGRAPHED_PHOTO);
      pictureTypeList.add(PictureConstant.CERTIFICATE_PHOTO);
      List<PhotoBean> photoList = photoBeanDao.selectByBusinessIdAndPictureTypeList(businessId, pictureTypeList);

      //获取业务回执单
      StorageResVo storageResVo = storageResDao.selectStorageByBusinessId(businessId);
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("clientInfo", clientInfo);
    returnMap.put("businessDetail", businessInfo);
      returnMap.put("photoList", photoList);
      returnMap.put("storageRes", storageResVo);
    return ResultUtils.ok("成功", returnMap);
  }

  /**
   * 更新用户状态
   *
   * @param user
   * @param workState
   */
  private void updateUserState(SysUserBean user, Integer workState) {
    Map<String, Integer> stateMap = new HashMap<String, Integer>();
    stateMap.put("workState", workState);
    stateMap.put("userId", user.getUserId());
    int workStateResult = sysUserDao.updateWorkState(stateMap);
    if (workStateResult == CommonConstant.zero) {
      logger.info("用户:" + user.getUserId() + "修改工作状态失败");
    }
  }

  /**
   * 更新终端状态
   *
   * @param vcDevId
   * @param state
   * @return
   */
  private int updateVcDevState(String vcDevId, Integer state) {
    Map<String, Object> deviceMap = new HashMap<>(CommonConstant.two);
    deviceMap.put("id", vcDevId);
    //0:空闲中 1使用中
    deviceMap.put("state", state);
    return vcDevDao.updateVcDecvice(deviceMap);
  }

  /**
   * 获取任务相关属性
   *
   * @param businessInfo
   */
  private void getBusinessInfoType(BusinessInfo businessInfo) {
    BusinessTypeBean businessTypeBean = businessTypeDao
        .selectByPrimaryKey(businessInfo.getBusinessType());
    businessTypeService.setBusinessTypeBeanForPhotoConfig(businessTypeBean);
    businessInfo.setBusinessTypeBean(businessTypeBean);
    PhotoConfig record = new PhotoConfig();
    record.setBusinessType(businessInfo.getBusinessType());
    List<PhotoConfig> photoConfigs = photoConfigDao.selectByObject(record);
    if (photoConfigs.isEmpty()) {
      businessInfo.setUploadForms(false);
    } else {
      businessInfo.setUploadForms(true);
    }
  }

  /*
   *根据当前日月，判断待选择业务类型是否可办理业务
   * @param businessTypeBean
   */
  public List<BusinessTypeBean> businessTypesOfDayOrMonth(List<BusinessTypeBean> list) {
    List<BusinessTypeBean> businessTypeBeanslist = new ArrayList<>();

    for (BusinessTypeBean bean : list) {
      //取得支持办理业务类型的日、月
      String[] months = bean.getBusinessMonth().split(",");
      String[] days = bean.getBusinessDay().split(",");
      Boolean monthFlag = false;
      Boolean dayFlag = false;
      //获取当前月，日
      Calendar now = Calendar.getInstance();
      String month = String.valueOf(now.get(Calendar.MONTH) + CommonConstant.one);
      String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
      for (String m : months) {
        if (month.equals(m)) {
          monthFlag = true;
        }
      }
      for (String d : days) {
        if (!day.equals(d)) {
          dayFlag = true;
        }
      }
      if (monthFlag && dayFlag) {
        businessTypeBeanslist.add(bean);
      }
    }
    return businessTypeBeanslist;

  }


  /**
   * 根据部门，servicekey，查询状态为处理中的业务
   *
   * @param deptId
   * @param serviceKey
   * @param i
   */
  @Override
  public List<BusinessInfo> getBusinessesByCondition(String deptId, String serviceKey, int i) {
    List<BusinessInfo> businessTypeBeanList = businessInfoDao
        .getBusinessesByCondition(deptId, serviceKey, BusinessInfoStateEnum.Processing.getValue());
    return businessTypeBeanList;
  }

  /**
   * 业务变更获取支持的业务类型
   *
   * @param deptId,serviceKey
   * @return 支持的业务类型
   * @auther ljfan
   */
  @Override
  public Map<String, Object> selectBusinessTypeByDeptId(Integer deptId) {
    BusinessTypeBean businessType = new BusinessTypeBean();
    businessType.setDeptId(deptId);
    List<BusinessTypeBean> businessTypeBean = businessTypeDao.getBusinessTypeByDeptId(businessType);
    return ResultUtils.ok("取得业务类型成功", businessTypeBean);

  }

  @Override
  public List<BusinessInfo> getBusinessesByUserId(Integer userId) {
    List<BusinessInfo> businessInfoList = businessInfoDao.getBusinessesByUserId(userId);
    return businessInfoList;
  }

  /**
   * @return 根据当前用户，获取所有村中心serviceKey
   */
  public List<String> getServiceKey() {

    List<String> serviceKeys = null;
    SysUserBean userBean = ShiroUtils.getUserEntity();
    //2.当为审批中心显示相应中心的数据
    if (SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType())) {
      serviceKeys = serviceCenterDao.getServiceListByParentKey(userBean.getServiceKey());
      serviceKeys.add(userBean.getServiceKey());
    } else if (SysUserTypeEnum.WholeCenterAdmin.getValue()
        .equals(userBean.getType())) {
      //2.当为统筹中心管理员时，显示该统筹中心的数据及下属级别的数据
      serviceKeys = serviceCenterDao.getServiceKeysByParentKey(userBean.getServiceKey());
      serviceKeys.add(userBean.getServiceKey());
    }
    return serviceKeys;
  }

  @Override
  public Integer getDoneCount(SysUserBean userBean) {
    Date date = new Date();
    BusinessInfo infoPara = new BusinessInfo();
    infoPara.setDeptId(null);
    if (userBean.getDeptId() != CommonConstant.zero) {
      infoPara.setDeptId(userBean.getDeptId());
    }
    infoPara.setState(BusinessInfoStateEnum.Processed.getValue());
    infoPara.setStartTime(DateUtil.getDateInMinutesAndSecondsZero(date));
    infoPara.setEndTime(DateUtil.getDateInMinutesAndSecondsMax(date));
    infoPara.setParentKey(userBean.getServiceKey());
    infoPara.setOperatorId(userBean.getUserId());
    return businessInfoDao.selectByObject(infoPara).size();

  }

  //更新办理业务信息
  private BusinessInfo updateBusinessInfo(BusinessInfo businessInfo, SysUserBean userBean,
                                          String vcDevId) {
    if (businessInfo == null || businessInfo.getTerminalNumber() != null) {
      logger.error("业务不存在");
      throw new RuntimeException("业务不存在");
    }
    businessInfo.setTerminalNumber(vcDevId);
    String date = new Timestamp(System.currentTimeMillis()).toString();
    businessInfo.setStartTime(date);
    businessInfo.setOperatorName(userBean.getUserName());
    businessInfo.setOperatorId(userBean.getUserId());
    businessInfo.setState(BusinessInfoStateEnum.Processing.getValue());
    businessInfo.setUpdateTime(date);
    businessInfo.setHandleServiceKey(userBean.getServiceKey());
      //根据用户id查询该用户目前所在窗口
      WindowBean userWindowBean = windowDao.getUserWindowByUserId(userBean.getUserId());
      if (userWindowBean != null) {
          logger.info("userId为" + userBean.getUserId() + "的用户所在窗口id为：" + userWindowBean.getId());
        businessInfo.setHandleWindowId(userWindowBean.getId());
      }
    businessInfoDao.updateBusinessInfoById(businessInfo);
    return businessInfo;
  }

  //发送获取成功消息
  @Override
  public void sendMessage(long waitnumber, BusinessInfo businessInfo, String code, String message) {
    ShowBusinessInfo showInfo = new ShowBusinessInfo(code, message, businessInfo, waitnumber);
    showInfo.setType(CommonConstant.BUSINESS_INFO);
    showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
    callDeviceWebSocketHandler.sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)),
        businessInfo.getOperatorId());
  }

  /**
   * 给安卓端推送消息封装 审批中心serviceKey
   */
  @Override
  public void sendMessageToAndroid(Integer userId, String callNumber) {
    //根据在线业务员
    SysUserBean userParam = new SysUserBean();
    userParam.setUserState(StateEnum.Effective.getValue().toString());
    userParam.setUserId(userId);
    SysUserBean userBean = sysUserDao.getUserByParam(userParam).get(0);
    //同审批中心下的业务是同步的，当为统筹业务员时，获取所有的
    List<ServiceCenterBean> centerBeans = new ArrayList<>();
    if (SysUserTypeEnum.AuditCenterSalesman.getValue().equals(userBean.getType())) {
      //根据统筹中心的业务员serviceKey获取所有的服务中心
      centerBeans = serviceCenterDao.getServiceCenterListByWholeParentKey(userBean.getServiceKey());
    } else {
      centerBeans = serviceCenterDao.getServiceCenterByParentKey(userBean.getServiceKey());
    }
    //根据村中心serviceKey取得对应审批中心下所有的村中心
    for (ServiceCenterBean centerBean : centerBeans) {
      getSendMessageForAndroid(centerBean.getServiceKey(),callNumber);
    }
  }
  @Override
  public void getSendMessageForAndroid(String serviceKey, String callNumber) {
    //根据村服务中心，获取连接叫号机
    String webSocketKey = CommonConstant.WEBSOCKET_KEY + serviceKey;
    //todo for jiangjun
//    List<String> queueList = redisUtils.listRangeAllStr(webSocketKey);
    String queues = (String) redisUtils.get(webSocketKey);
//    for (String queues : queueList) {
      if (queues != null) {
          String[] queuesArray = queues.split(":");
          //业务拓展之前的需求没有sessionID，兼容老数据的处理
          if (queuesArray.length == 2) {
              String sessionId = queuesArray[1];
              //单部门处理
              List<ShowBusinessInfo> showInfos;
              showInfos = getShowBusinessInfoList(serviceKey,
                      callNumber);
              //消息推送,新版叫号机推送方法调用
              h5WebSocketHandler
                      .sendMessageToH5(new TextMessage(JSON.toJSONString(showInfos)), sessionId);
              List<ShowBusinessInfo> takeinfo;
              //获取排队显示号
              takeinfo = getShowBusinessInfosForAndroid(CommonConstant.SUPER_ADMIN_DEPTID_TYPE,
                      serviceKey);
              //消息推送,新版叫号机推送方法调用
              h5WebSocketHandler
                      .sendMessageToH5(new TextMessage(JSON.toJSONString(takeinfo)), sessionId);
          }
      }
  }


 //叫号机删除消息推送
  @Override
  public void SendMessageForAndroidByJHJ(String serviceKey) {
    //根据村服务中心，获取连接叫号机
    String webSocketKey = CommonConstant.WEBSOCKET_KEY + serviceKey;
    //todo for jiangjun
//    List<String> queueList = redisUtils.listRangeAllStr(webSocketKey);
    String queues = (String) redisUtils.get(webSocketKey);
//    for (String queues : queueList) {
    if (queues != null) {
      String[] queuesArray = queues.split(":");
      //业务拓展之前的需求没有sessionID，兼容老数据的处理
      if (queuesArray.length == 2) {
        String sessionId = queuesArray[1];
        //消息推送,新版叫号机推送方法调用
        ShowBusinessInfo showBusinessInfo=new ShowBusinessInfo();
        showBusinessInfo.setType(CommonConstant.BUSINESS_JHJ_DELETE);
        h5WebSocketHandler
            .sendMessageToH5(new TextMessage(JSON.toJSONString(showBusinessInfo)), sessionId);
      }
    }
  }



  /**
   * 多部门消息推送 根据村中心查询待推送待数据
   */
  @Override
  public List<ShowBusinessInfo> getShowBusinessInfoList(String serviceKey, String callNumber) {
    List<ShowBusinessInfo> showInfos = new ArrayList<>();
    List<SysDeptBean> deptBeans = sysDeptBeanDao.getSysDeptListNotInDelete();
    for (SysDeptBean bean : deptBeans) {
      ShowBusinessInfo showNumber = showBusinessInfoForAndroid(serviceKey, bean.getId());
      Long waitNumbers = getAndroidWaitNumber(serviceKey, bean.getId());
      showNumber.setWaitNumber(waitNumbers);
      showNumber.setDeptName(bean.getDeptName());
      showNumber.setDeptId(bean.getId());
      showNumber.setCallNumber(callNumber);
      showInfos.add(showNumber);
    }
    return showInfos;
  }

  /**
   * 多部门消息推送 根据审批中心查询待推送待数据
   */
  @Override
  public ShowBusinessInfo getAcceptShowBusinessInfo(SysUserBean userBean) {
    Long waitNumbers;
    if (userBean.getDeptId().equals(0)) {
      waitNumbers = getAcceptAllWaitNumber(userBean.getServiceKey());
    } else {
      waitNumbers = getAcceptWaitNumber(userBean.getServiceKey(), userBean.getDeptId());
    }
    long doneNumber = getDoneNumber(userBean);
    ShowBusinessInfo showNumber = new ShowBusinessInfo();
    showNumber.setDoneNumber(doneNumber);
    showNumber.setWaitNumber(waitNumbers);
    showNumber.setDeptId(userBean.getDeptId());
    showNumber.setType(CommonConstant.BUSINESS_COUNT);
    return showNumber;
  }

  @Override
  public long getDoneNumber(SysUserBean userBean) {
    //doneCount
    BusinessInfo businessInfo = new BusinessInfo();
    businessInfo.setDeptId(userBean.getDeptId());
    if (userBean.getDeptId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
      businessInfo.setDeptId(null);
    }
    businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
    businessInfo.setStartTime(DateUtil.getDateInMinutesAndSecondsZero(new Date()));
    businessInfo.setEndTime(DateUtil.getDateInMinutesAndSecondsMax(new Date()));
    businessInfo.setOperatorId(userBean.getUserId());
    return businessInfoDao.selectByObject(businessInfo).size();
  }

  /**
   * 根据中心serviceKey 查询中心下所有的服务中心serviceKey
   */
  @Override
  public List<String> getServiceKeys(String serviceKey) {
    //当受理端业务员时，serviceKey可能是统筹的，其他事审批serviceKey
    ServiceCenterBean bean = serviceCenterDao.getParentServiceCenterByServiceKey(serviceKey);
    if (null == bean) {
      bean = serviceCenterDao.getServiceCenter(serviceKey);
    }
    return serviceCenterDao.getServiceKeysByParentKey(bean.getServiceKey());
  }

  /**
   * 一窗宗办andriod等待人数
   *
   * @param serviceKey 审批中心
   * @return
   */
  @Override
  public long getAndroidAllWaitNumber(String serviceKey) {
    List<SysDeptBean> deptBeans = sysDeptBeanDao.specialDeptList(SysDeptTypeEnum.Yes.getValue());
    long waitNumber = 0;
    for (SysDeptBean bean : deptBeans) {
      waitNumber += getAndroidWaitNumber(serviceKey, bean.getId());
    }
    return waitNumber;
  }

  /**
   * 一窗宗办受理端等待人数
   *
   * @param serviceKey
   * @param serviceKey 审批中心
   * @return
   */
  @Override
  public long getAcceptAllWaitNumber(String serviceKey) {
    List<SysDeptBean> deptBeans = sysDeptBeanDao.specialDeptList(SysDeptTypeEnum.Yes.getValue());
    long waitNumber = 0;
    for (SysDeptBean bean : deptBeans) {
      waitNumber += getAcceptWaitNumber(serviceKey, bean.getId());
    }
    return waitNumber;
  }

  /**
   * android叫号机部门待办数统计、根据村中心获取审批中心、获取统筹中心，统计待办数
   *
   * @param deptId
   * @param serviceKey 审批中心
   * @return
   */
  @Override
  public long getAndroidWaitNumber(String serviceKey, Integer deptId) {
    //根据村中心取得审批中心
    ServiceCenterBean serviceCenter = serviceCenterDao
        .getParentServiceCenterByServiceKey(serviceKey);
    //获取统筹中心的待办业务数
    String queueKey = CommonConstant.QUEUE_KEY + serviceCenter.getParentKey() + ":" + deptId;
    Long waitNumber = redisUtils.listLength(queueKey);
    //获取统筹中心下审批中心的待办业务数
    queueKey = CommonConstant.QUEUE_KEY + serviceCenter.getServiceKey() + ":" + deptId;
    waitNumber += redisUtils.listLength(queueKey);
    return waitNumber;
  }
  // /**
  //  * android叫号机部门待办数统计、根据村中心获取审批中心、获取统筹中心，统计待办数
  //  *
  //  * @param deptId
  //  * @param serviceKey 审批中心
  //  * @return
  //  */
  // @Override
  // public long getH5WaitNumber(String serviceKey, Integer deptId) {
  //   //根据村中心取得审批中心
  //   ServiceCenterBean serviceCenter = serviceCenterDao
  //       .getParentServiceCenterByServiceKey(serviceKey);
  //   //获取统筹中心的待办业务数
  //   String queueKey = CommonConstant.QUEUE_KEY + serviceCenter.getParentKey() + ":" + deptId;
  //   Long waitNumber = redisUtils.listLength(queueKey);
  //   //获取统筹中心下审批中心的待办业务数
  //   queueKey = CommonConstant.QUEUE_KEY + serviceCenter.getServiceKey() + ":" + deptId;
  //   waitNumber += redisUtils.listLength(queueKey);
  //   return waitNumber;
  // }


  /**
   * 受理端业务员待办数统计
   *
   * @param deptId
   * @param serviceKey 审批中心
   * @return
   */
  @Override
  public long getAcceptWaitNumber(String serviceKey, Integer deptId) {
    String queueKey = CommonConstant.QUEUE_KEY + serviceKey + ":" + deptId;
    return redisUtils.listLength(queueKey);
  }

  /**
   * 获取叫号机待显示数据,村中心serviceKey，部门deptId
   */
  @Override
  public List<ShowBusinessInfo> getShowBusinessInfosForAndroid(Integer deptId, String serviceKey) {
    List<ShowBusinessInfo> showBusinessInfoList = new ArrayList<>();
    //获取服务中心信息
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenterBean == null || StringUtils.isEmpty(serviceCenterBean.getServiceName())) {
      return null;
    }
    //非一窗综办
    List<SysDeptBean> sysDeptBeans = sysDeptBeanDao.specialDeptList(SysDeptTypeEnum.No.getValue());
    if (CollectionUtils.isNotEmpty(sysDeptBeans)) {
      for (SysDeptBean dept : sysDeptBeans) {
        ShowBusinessInfo info = showBusinessInfoForAndroid(serviceKey, dept.getId());
        info.setDeptName(dept.getDeptName());
        info.setDeptId(dept.getId());
        showBusinessInfoList.add(info);
      }
    }
    //一窗宗办
    List<SysDeptBean> sysAllDept = sysDeptBeanDao.specialDeptList(SysDeptTypeEnum.Yes.getValue());
    if(sysAllDept.size()>0){
      ShowBusinessInfo info = showBusinessInfoForAndroid(serviceKey, deptId);
      showBusinessInfoList.add(info);
    }
    return showBusinessInfoList;
  }



  //根据部门、中心获取安卓叫号机显示信息
  public ShowBusinessInfo showBusinessInfoForAndroid(String serviceKey, Integer deptId) {
    //根据村中心获取村中心上一级审批中心下所有的村中心
    List<String> serviceKeys = serviceCenterDao.getServiceKeyListByServiceKey(serviceKey);
    ShowBusinessInfo result = new ShowBusinessInfo();
    BusinessInfo info = new BusinessInfo();
    info.setServiceKeys(serviceKeys);
    //如果发布首版，因没有中心，所有serviceKeys为空
    if (CollectionUtils.isEmpty(serviceKeys)) {
      info.setServiceKeys(null);
    }
    info.setDeptId(deptId);
    if (deptId == 0) {
      //特殊部门业务除外
      info.setDeptType(SysDeptTypeEnum.Yes.getValue());
      info.setDeptId(null);
      //总等待数
      Long waitCount = getAndroidAllWaitNumber(serviceKey);
      result.setWaitNumber(waitCount);
      result.setDeptName(AndroidBusinessTypeEnum.AllProcess.getName());
    } else {
      long waitCount = getAndroidWaitNumber(serviceKey, deptId);
      result.setWaitNumber(waitCount);
    }
    //处理中的数
    info.setState(BusinessInfoStateEnum.Processing.getValue());
    List<BusinessInfo> businessInfos = businessInfoDao.getProcessingBusinessInfo(info);
    if (businessInfos.size() > 0) {
      ServiceCenterBean bean = serviceCenterDao
          .getServiceCenter(businessInfos.get(0).getServiceKey());
      result.setProceNumber(businessInfos.get(0).getNumber());
      result.setNumServiceKey(businessInfos.get(0).getServiceKey());
      result.setProcessServiceName(bean.getServiceName());
      result.setCallName(businessInfos.get(0).getName());
    } else {
      result.setProceNumber(null);
      result.setProcessServiceName("");
    }
    //待处理的号
    info.setState(BusinessInfoStateEnum.Untreated.getValue());
    businessInfos = businessInfoDao.getWaitProcessBusinessInfo(info);
    if (businessInfos.size() > 0) {
      ServiceCenterBean bean = serviceCenterDao
          .getServiceCenter(businessInfos.get(0).getServiceKey());
      result.setNextNumber(businessInfos.get(0).getNumber());
      result.setNextName(businessInfos.get(0).getName());
      result.setNextServiceName(bean.getServiceName());
    } else {
      result.setNextNumber(null);
      result.setNextServiceName("");
    }
    //获取当前服务中心的名称
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenterBean != null) {
      result.setServiceKey(serviceKey);
      result.setServiceName(serviceCenterBean.getServiceName());
    }
    return result;
  }

  @Override
  public String getCallNumberForAndroid(String serviceKey, Integer deptId) {
    //根据村中心获取同一个审批中心下所有村的serviceKey
    List<String> serviceKeys = serviceCenterDao.getServiceKeyListByServiceKey(serviceKey);
    BusinessInfo info = new BusinessInfo();
    info.setServiceKeys(serviceKeys);
    info.setDeptId(deptId);
    if (deptId == 0) {
      //特殊部门业务除外
      info.setDeptType(SysDeptTypeEnum.Yes.getValue());
      info.setDeptId(null);
    }
    //处理中的数
    info.setState(BusinessInfoStateEnum.Processing.getValue());
    List<BusinessInfo> businessInfos = businessInfoDao.getProcessingBusinessInfo(info);
    if (businessInfos.size() > 0) {
      return businessInfos.get(0).getNumber();
    }
    return "";
  }




  /**
   * 受理端根据业务员ID获取业务员待办统计初始化
   */
  @Override
  public void getWaitNumber(Integer userId) {
    //受理端待处理号消息推送
    SysUserBean userBean = sysUserDao.getSysUserInfo(userId);
    if (userBean != null) {
      ShowBusinessInfo show = getAcceptShowBusinessInfo(userBean);
      callDeviceWebSocketHandler
          .sendMessageForBusiness(new TextMessage(JSON.toJSONString(show)), userBean.getUserId());
    }
  }

  /**
   * @param deptId,村级中心serviceKey
   * @param serviceKey
   * @desc 分机仅取号  带办理业务
   */
  @Override
  public void sendMessageToAndroidDept(Integer deptId, String serviceKey) {
    List<ServiceCenterBean> serviceCenterBeans = serviceCenterDao
        .getServiceCentListByserviceKey(serviceKey);
    if (serviceCenterBeans.size() > 0) {
      for (ServiceCenterBean service : serviceCenterBeans) {
        String webSocketKey = CommonConstant.WEBSOCKET_KEY + service.getServiceKey();
        List<String> webSocketList = redisUtils.listRangeAllStr(webSocketKey);
        if (webSocketList.size() > 0) {
          for (String queues : webSocketList) {
            String[] queuesArray = queues.split(":");
            String sessionId = "";
            if (queuesArray.length > 2) {
              sessionId = queuesArray[2];
            }
            //安卓设定仅叫号时，给H5页面发送消息
            if (AndroidShowTypeEnum.OnlyTakeNumber.getValue().equals(queuesArray[3])) {
              //安卓机部门列表待处理号
              ShowBusinessInfo showInfo = new ShowBusinessInfo();
              //获取统筹中心的待办号数量
              Long waitNumber = getAndroidWaitNumber(serviceKey, deptId);
              showInfo.setWaitNumber(waitNumber);
              showInfo.setDeptId(deptId);
              showInfo.setType(CommonConstant.DEP_UPDATE_TYPE);
              androidWebSocketHandler
                  .sendMessageToAndroid(new TextMessage(JSON.toJSONString(showInfo)), sessionId);
            }
          }
        }
      }
    }
  }

  /**
   * @param deptId,村级中心serviceKey
   * @param serviceKey
   * @desc 分机仅取号  带办理业务
   */
  @Override
  public void sendMessageToH5Dept(Integer deptId, String serviceKey) {
  }

  /**
   * 获取任务开始受理
   *
   * @param userBean ,serviceKey(办理中心)
   * @return
   */
  @Override
  public ShowBusinessInfo getWebSocketTask(SysUserBean userBean, BusinessInfo info) {
    ShowBusinessInfo showInfo = new ShowBusinessInfo(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
    String queueKey = CommonConstant.QUEUE_KEY + userBean.getServiceKey() + ":" + info.getDeptId();
    String vcDevId = null;
    List<VcDevBean> vcDevList = vcDevDao.getAvailableDevice(info.getServiceKey());
    if (vcDevList != null && vcDevList.size() > 0) {
      VcDevBean slaveDev = vcDevList.get(0);
      int updateDeviceResult = updateVcDevState(slaveDev.getId(), VcDevStateEnum.InUse.getValue());
      if (updateDeviceResult > CommonConstant.zero) {
        vcDevId = slaveDev.getId();
        redisUtils.listRemove(queueKey, 0, info.getServiceKey() + ":" + info.getId().toString());
      }
    }
    if(StringUtil.isEmpty(vcDevId)){
      showInfo.setMessage("获取任务失败");
      return showInfo;
    }
    //更新业务信息
    info = updateBusinessInfo(info, userBean, vcDevId);
      //
    getBusinessInfoType(info);
    SysDeptBean deptBean = sysDeptBeanDao.selectByPrimaryKey(info.getDeptId());
    info.setNumberPrefix(deptBean.getNumberPrefix());
    businessLogService.insertLog(info.getId(), LogEnums.BUSINESS_PROCESS.getCode(),
        LogEnums.BUSINESS_PROCESS.getValue(), null);
    /**
     * 线程池管理线程
     * 推送任务后 等待设定时间后检测是否有未开始办理的任务
     */
    SendsmsTask task = new SendsmsTask();
    task.setUserName(userBean.getUserName());
    task.setPhoneNumber(userBean.getMobilePhone());
    task.setSmsUrl(InitializingBeanImpl.smsUrl);
    task.setSmsMessage(InitializingBeanImpl.smsMessage);
    task.setBusinessId(info.getId());
    ScheduledFuture<?> future = scheduledThreadPoolExecutor.schedule(task, sleepTime, TimeUnit.MINUTES);
    InitializingBeanImpl.map.put("smsThreadTask"+info.getId(),future);
    //redisUtils.add("smsThreadTask"+info.getId(),future);
    showInfo.setBusinessInfo(info);
    showInfo.setMessage("获取任务成功");
    return showInfo;
  }

  /**
   * redis中获取待办数、审批中心：统筹中心，部门ID
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  @Override
  public Integer getWaitBusinessInfo(String serviceKey, Integer deptId) {
    return businessInfoDao.getUntreated();
  }

  /**
   * 将等待业务员存入redis中
   *
   * @param userBean
   */
  @Override
  public void pushUserToqueue(SysUserBean userBean) {
    //待办业务员存入redis中
    String waitQueueKey =
        CommonConstant.WAITING_WORK_KEY + userBean.getServiceKey() + ":" + userBean.getDeptId();
    //删除受理端历史连接
    UserSortVo vo = getUserSortVo(userBean.getUserId());
    redisUtils.listRemove(waitQueueKey, 0, JSON.toJSONString(vo));
    UserSortVo userSortVo = new UserSortVo();
    userSortVo.setSortTime(System.currentTimeMillis());
    userSortVo.setUserId(userBean.getUserId());
    //添加新的受理端建立连接
    updateUserState(userBean, CommonConstant.WORK_STATE_WAIT);
    redisUtils.rightPush(waitQueueKey, JSON.toJSONString(userSortVo));
    popBusinessInfo(userBean.getServiceKey());
  }

  /**
   * @param serviceKey 根据当前serviceKey 取得所属中心
   */
  @Override
  public void popBusinessInfo(String serviceKey) {
    /**
     * 获取根据办理中心获取每个村最先取并且村终端为空闲状态号
     */
    ServiceCenterBean bean = serviceCenterDao.getServiceCenter(serviceKey);
    List<String> serviceKeys;
    //如果是统筹中心，获取统筹中心下的所有村中心serviceKey
    if(bean!=null&& CommonConstant.PARENT_KEY.equals(bean.getParentKey())){
      serviceKeys = serviceCenterDao.getServiceKeysByParentKey(serviceKey);
    }else{
      //如果是审批中心，获取审批中心下所有村中心serviceKey
      serviceKeys = serviceCenterDao.getServiceListByParentKey(serviceKey);
    }
    List<BusinessInfo> businessInfos = businessInfoDao
        .getBusinessInfoOfWaitingByServiceKey(serviceKeys);
    if (!CollectionUtils.isEmpty(businessInfos)) {
      //遍历当前业务如果业务为仅统筹办理  办理
      for (BusinessInfo info : businessInfos) {
        //
        String handleServiceKey;
        //获取队列里的业务员集合
        BusinessTypeBean businessTypeById = businessTypeDao.getBusinessTypeById(info.getBusinessType());
        if(BusinessTypeIsComprehensiveEnum.Yes.getValue()
            .equals(businessTypeById.getIsComprehensive())){
          handleServiceKey = businessTypeById.getServiceKey();
        }else{
          ServiceCenterBean centerBean = serviceCenterDao.getServiceCenter(info.getServiceKey());
          handleServiceKey = centerBean.getParentKey();
        }
        popMessageToUser(handleServiceKey,info);
      }
    }
  }
  /**
   * 根据业务办理中心、部门，取得第一个等待的办理员
   */
  private void popMessageToUser(String serviceKey,BusinessInfo info){
    SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(info.getDeptId());
    List<UserSortVo> userSortVoList = new ArrayList<>();
    //特殊部门业务
    if(SysDeptTypeEnum.No.getValue().equals(deptBean.getType())){
      List<String> userSorts = redisUtils
          .listRangeAllStr(CommonConstant.WAITING_WORK_KEY + serviceKey + ":" + info.getDeptId());
      userSortVoList = StringToBean(userSorts);
    }else{
      List<String> userSorts = redisUtils
          .listRangeAllStr(CommonConstant.WAITING_WORK_KEY + serviceKey + ":" + info.getDeptId());
      List<String> userSortsDept = redisUtils.listRangeAllStr(
          CommonConstant.WAITING_WORK_KEY + serviceKey + ":" + CommonConstant.SUPER_ADMIN);
      if (!CollectionUtils.isEmpty(userSorts)) {
        userSorts.addAll(userSortsDept);
        //将String转bean
        userSortVoList = StringToBean(userSorts);
      } else if (!CollectionUtils.isEmpty(userSortsDept)) {
        userSortVoList = StringToBean(userSortsDept);
      }
    }
    //将队列里的在线等待业务员进行排序
    if (CollectionUtils.isNotEmpty(userSortVoList)) {
      Collections.sort(
          userSortVoList,
          Comparator.comparing(UserSortVo::getSortTime));
      acceptBusinessWebSocketHandler
          .sendMessageForBusinessInfo(userSortVoList.get(0).getUserId(),info,userSortVoList.get(0));
    }
  }
  @Override
 public UserSortVo getUserSortVo(Integer userId){
    SysUserBean sysUserBean = sysUserDao.getSysUserInfo(userId);
   //获取队列里的业务员集合
   List<String> userSorts = redisUtils
       .listRangeAllStr(CommonConstant.WAITING_WORK_KEY + sysUserBean.getServiceKey() + ":" + sysUserBean.getDeptId());
   List<UserSortVo> userSortVoList = StringToBean(userSorts);
   if(!CollectionUtils.isEmpty(userSortVoList)){
     for(UserSortVo vo:userSortVoList){
       if(vo.getUserId().equals(userId)){
         return vo;
       }
     }
   }
   return null;
 }

  /**
   * 获取业务未办结的原因
   * @param list
   */
  private void getbusinessUnattendCause(List<BusinessInfo> list) {
    if (!CollectionUtils.isEmpty(list)) {
      for (BusinessInfo entity : list) {
        if (FinalStateEnum.NotHandled.getValue().equals(entity.getFinalState())) {
          List<String> causeList = new ArrayList<>();
          List<UnattendedCause> causes = unattendedCauseDao.getCause(entity.getId());
          for (UnattendedCause cause : causes) {
            if (StringUtil.isNotEmpty(cause.getCauseName())) {
              causeList.add(cause.getCauseName());
            }
          }
          entity.setUnattendedCause(causeList);
        }
      }
    }
  }
  /**
   * 将对象里的String转换成bean
   *
   * @param params
   * @return
   */
  private List<UserSortVo> StringToBean(List<String> params) {
    UserSortVo vo = new UserSortVo();
    List<UserSortVo> userSortVoList = new ArrayList<>();
    if (params.size() > 0) {
      for (String param : params) {
        UserSortVo userSortVo = StringUtil.stringToBean(param, vo.getClass());
        userSortVoList.add(userSortVo);
      }
      return userSortVoList;
    }
    return null;
  }

  @Override
  public BusinessInfo searchBusinessInfo(String idcard) {
    BusinessInfo businessInfo  =businessInfoDao.searchBusinessInfo(idcard);
    if(businessInfo!=null){
      return businessInfo;
    }
    return null;
  }
}
