package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.app.dao.AppointmentInfoDao;
import com.visionvera.common.enums.AndroidBusinessTypeEnum;
import com.visionvera.common.enums.BusinessTypeIsComprehensiveEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysDeptTypeEnum;
import com.visionvera.common.enums.SysUserStateEnum;
import com.visionvera.common.enums.SysUserWorkStateEnum;
import com.visionvera.common.enums.WSWebReturnEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.NumberIteration;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.DeviceInfoDao;
import com.visionvera.remoteservice.dao.MaterialsDao;
import com.visionvera.remoteservice.dao.NumberDao;
import com.visionvera.remoteservice.dao.NumberIterationDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.pojo.DeptListVo;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.pojo.SysDeptVo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.NumberService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.ws.AcceptBusinessWebSocketHandler;
import com.visionvera.remoteservice.ws.AndroidWebSocketHandler;
import com.visionvera.remoteservice.ws.CallDeviceWebSocketHandler;
import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import com.visionvera.remoteservice.ws.H5WebSocketHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;


/**
 * @Auther: quboka
 * @Date: 2018/8/28 18:16
 * @Description:
 */
@Service
public class NumberServiceImpl implements NumberService {

  private static Logger logger = LoggerFactory.getLogger(NumberServiceImpl.class);

  @Resource
  private ServiceCenterDao serviceCenterDao;

  @Resource
  private AndroidWebSocketHandler androidWebSocketHandler;
  @Resource
  private H5WebSocketHandler h5WebSocketHandler;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;

  @Resource
  private BusinessTypeDao businessTypeDao;

  @Resource
  private BusinessInfoDao businessInfoDao;

  @Resource
  private NumberDao numberDao;

  @Resource
  private MaterialsDao materialsDao;

  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private AppointmentInfoDao appointmentInfoDao;

  @Resource
  private DeviceInfoDao deviceInfoDao;

  @Resource
  private VcDevDao vcDevDao;

  @Resource
  private RedisUtils redisUtils;
  @Resource
  private BusinessInfoService businessInfoService;
  @Resource
  private AcceptBusinessWebSocketHandler acceptBusinessWebSocketHandler;
  @Resource
  private NumberIterationDao numberIterationDao;
  @Value("${temp.save.docpath}")
  private String tempPath;
  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
  @Resource
  private CallDeviceWebSocketHandler callDeviceWebSocketHandler;

  /**
   * 业务受理月
   */
  private static boolean isBusinessMonth(String monthStr) {
    String[] strArr = monthStr.split(",");
    int[] months = new int[strArr.length];
    for (int i = 0; i < months.length; i++) {
      months[i] = Integer.valueOf(strArr[i]);
    }
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;
    if (!ArrayUtils.contains(months, month)) {
      return true;
    }
    return false;
  }

  /**
   * 业务受理日
   */
  private static boolean isBusinessDay(String datesStr) {
    String[] dateArr = datesStr.split(",");
    int[] dates = new int[2];
    dates[0] = Integer.valueOf(dateArr[0]);
    if (dateArr.length == 1) {
      dates[1] = Integer.valueOf(dateArr[0]);
    } else {
      dates[1] = Integer.valueOf(dateArr[1]);
    }
    int start = dates[0];
    int end = dates[1];
    Calendar cal = Calendar.getInstance();
    int date = cal.get(Calendar.DATE);
    if (date >= start && date <= end) {
      return false;
    }
    return true;
  }

  /**
   * @param deptListVo
   * @return java.util.Map<java.lang.String, java.lang.Object>
   * @description: 通过服务中心key查询部门列表
   * @author quboka
   * @date 2018/8/28 18:22
   */
  @Override
  public Map<String, Object> getDeptList(DeptListVo deptListVo) {

    //获取服务中心信息
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(deptListVo.getServiceKey());
    if (serviceCenterBean == null || StringUtils.isEmpty(serviceCenterBean.getServiceName())) {
      return ResultUtils.error("服务中心不存在");
    }
    Integer pageNum = deptListVo.pageNum;
    Integer pageSize = deptListVo.pageSize;
    Integer state = deptListVo.getState();
    String serviceKey = deptListVo.getServiceKey();

    String parentKey = serviceCenterDao.getParentKey(serviceKey);
    if (StringUtils.isEmpty(parentKey)) {
      logger.info("查询部门失败，请选择正确的服务中心");
      return ResultUtils.error("查询部门失败，请选择正确的服务中心");
    }

    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }

    SysDeptVo sysDeptVo = new SysDeptVo();
    sysDeptVo.setState(state);
    List<SysDeptBean> list = sysDeptBeanDao.getDeptList(sysDeptVo);
    for (SysDeptBean dept : list) {
      String queueKey = CommonConstant.QUEUE_KEY + parentKey + ":" + dept.getId();
      long waitnumber = redisUtils.listLength(queueKey);
      ServiceCenterBean bean = serviceCenterDao.getServiceCenter(parentKey);
      queueKey = CommonConstant.QUEUE_KEY + bean.getParentKey() + ":" + dept.getId();
      waitnumber += redisUtils.listLength(queueKey);
      dept.setWaitCount(waitnumber);
    }
    PageInfo<SysDeptBean> pageInfo = new PageInfo(list);

    return ResultUtils.ok("查询部门成功", pageInfo);
  }

  /**
   * @param serviceKey 村key
   * @param businessId 业务详情id
   * @param androidBusinessType 业务模式：01一窗多办，02 分部门处理
   * @return java.util.Map<java.lang.String, java.lang.Object>
   * @description: 获取业务号码
   * @author quboka
   * @date 2018/8/29 11:05
   */
  @Override
  public Map<String, Object> takeBusinessNumberAndModify(String serviceKey, Integer businessId,
      Integer type, Integer appointmentId, String androidBusinessType) {
    //获取服务中心信息
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenterBean == null || StringUtils.isEmpty(serviceCenterBean.getServiceName())) {
      return ResultUtils.error("派号失败,服务中心信息不存在");
    }
    //取得审批中心的数据
    ServiceCenterBean parentServiceCenter = serviceCenterDao
        .getServiceCenter(serviceCenterBean.getParentKey());
    if (parentServiceCenter == null || StringUtils.isEmpty(parentServiceCenter.getServiceName())) {
      return ResultUtils.error("派号失败,审批中心信息不存在");
    }
    //判断村中心是否有绑定终端
    List<VcDevBean> deviceList = vcDevDao.getDeviceByServiceKey(serviceKey);
    if (CollectionUtils.isEmpty(deviceList)) {
      ResultUtils.error("派号失败，服务中心未绑定终端");
    }
    // 获取业务详情
    BusinessTypeBean businessDeatil = businessTypeDao.selectByPrimaryKey(businessId);
    if (null == businessDeatil) {
      logger.info("派号失败,没有业务");
      return ResultUtils.error("派号失败,没有业务");
    }

    if (isBusinessMonth(businessDeatil.getBusinessMonth())) {
      logger.info(
          "取号失败，该业务办理月为“" + businessDeatil.getBusinessMonth() + "月" + "，当前暂时无法办理。");
      return ResultUtils.error(
          "取号失败，该业务办理月为“" + businessDeatil.getBusinessMonth() + "月" + "，当前暂时无法办理。");
    }
    if (isBusinessDay(businessDeatil.getBusinessDay())) {
      String businessDay = businessDeatil.getBusinessDay();
      businessDay = businessDay.replace(",", "-");
      logger.info(
          "取号失败，该业务办理日为“" + businessDay + "日”，当前暂时无法办理。");
      return ResultUtils.error(
          "取号失败，该业务办理日为“" + businessDay + "日”，当前暂时无法办理。");
    }
   /* logger.info(System.currentTimeMillis()+"打印开始时间-----------");
    if (businessDeatil.getIsForm() == 1) {
      List<Materials> materialsList = materialsDao
          .selectMaterialsByTypeIdAndMaterialsType(businessDeatil.getId(), 1);
      for (Materials materials : materialsList) {
        String outputUrl = null;
        if (StringUtil.isNotNull(materials.getFilePath())) {
          outputUrl = UrlFileUtil.downloadFile(materials.getFilePath(), tempPath);
          if (!StringUtil.isEmpty(outputUrl) && new File(outputUrl).exists()) {
            //通过websocket协议传输打印材料
            byte[] baseImage = FileUtil.imageTobyte(outputUrl);
            String shaFile = null;
            File file = new File(outputUrl);
            shaFile = FileUtil.getFileSha1(file);
            EmbeddedBean embeddedBean = new EmbeddedBean();
            //devType为2时，代表是叫号机调用的打印机
            embeddedBean.setDevType(DevTypeEnum.CueingMachine.getValue());
            embeddedBean.setFileData(baseImage);
            embeddedBean.setType(CallDeviceEnum.Printer.getValue());
            embeddedBean.setFormat(
                materials.getFilePath().substring(materials.getFilePath().lastIndexOf('.') + 1));
            embeddedBean.setFileName(
                materials.getFilePath().substring(materials.getFilePath().lastIndexOf('/') + 1,
                    materials.getFilePath().lastIndexOf('.')));
            embeddedBean.setShaValue(shaFile);
            try {
              embeddedServerWebSocketHandler
                  .sendMessageToEmbeddServer(new TextMessage(JSON.toJSONString(embeddedBean)),
                      deviceList.get(0).getId(), Boolean.FALSE);
              logger.info("排队叫号向x86发送打印材料");
            } catch (Exception e) {
              e.printStackTrace();
              return ResultUtils.error("调用打印机失败");
            }
            if (file.exists()) {
              file.delete();
            }
          }
        }
      }
    }
    logger.info(System.currentTimeMillis()+"打印结束时间-------------");*/
    //迭代号码
    //号前添加部门id,id转字母
    SysDeptBean sysDeptBean = sysDeptBeanDao.selectByPrimaryKey(businessDeatil.getDeptId());
    if (null == sysDeptBean) {
      return ResultUtils.error("部门不存在");
    }
    /**
     * 如果是一窗综办，deptId = 0 迭代号基础归属为：dept、审批中心，所有号为：A+number;
     */
    BusinessInfo businessInfo;
    NumberIteration numberBean;
    String handleServiceKey = null;
    String numberPrefix = "A";
    if (AndroidBusinessTypeEnum.AllProcess.getValue().equals(androidBusinessType)
        && SysDeptTypeEnum.Yes.getValue().equals(sysDeptBean.getType())) {
      //如果是统筹业务员，serviceKey为统筹中心
      if (BusinessTypeIsComprehensiveEnum.Yes.getValue()
          .equals(businessDeatil.getIsComprehensive())) {
        handleServiceKey = parentServiceCenter.getParentKey();
        numberBean = getNumber(Integer.valueOf(CommonConstant.SUPER_ADMIN_DEPTID_TYPE),
            parentServiceCenter.getServiceKey());
      } else {
        //如果是审批业务员
        handleServiceKey = serviceCenterBean.getParentKey();
        numberBean = getNumber(Integer.valueOf(CommonConstant.SUPER_ADMIN_DEPTID_TYPE),
            serviceCenterBean.getParentKey());
      }
      businessInfo = new BusinessInfo(
          (numberPrefix + numberBean.getNumber().toString()), serviceKey,
          businessDeatil.getDeptId(),
          businessDeatil.getId(), businessDeatil.getDescribes(),
          type, appointmentId, handleServiceKey);

    } else {
      /**
       * 多部门时，根据部门及审批中心serviceKey 取得号码
       */
      if (BusinessTypeIsComprehensiveEnum.Yes.getValue()
          .equals(businessDeatil.getIsComprehensive())) {
        handleServiceKey = parentServiceCenter.getParentKey();
        numberBean = getNumber(businessDeatil.getDeptId(), parentServiceCenter.getServiceKey());
      } else {
        //如果是审批业务员
        handleServiceKey = serviceCenterBean.getParentKey();
        numberBean = getNumber(businessDeatil.getDeptId(), serviceCenterBean.getParentKey());

      }
      if (SysDeptTypeEnum.No.getValue().equals(sysDeptBean.getType())) {
        numberPrefix = getNumberPrefix(sysDeptBean.getId());
      } else {
        numberPrefix = sysDeptBean.getNumberPrefix();
      }
      businessInfo = new BusinessInfo(
          (numberPrefix + numberBean.getNumber().toString()), serviceKey,
          businessDeatil.getDeptId(),
          businessDeatil.getId(), businessDeatil.getDescribes(),
          type, appointmentId, handleServiceKey);
    }
    logger.info(System.currentTimeMillis() + "往表插入数据开始时间");
    int insertResult = businessInfoDao.insertSelective(businessInfo);
    logger.info(System.currentTimeMillis() + "往表插入数据结束时间");
    if (insertResult == 0) {
      logger.info("派号失败");
      return ResultUtils.error("派号失败");
    }

    //修改预约业务记录状态
    if (appointmentId != CommonConstant.zero && appointmentId != null) {
      appointmentInfoDao.updateState(appointmentId);
    }
    //等待办理业务人数
    String queueKey;
    //如果是统筹业务，serviceKey为统筹中心
    if (BusinessTypeIsComprehensiveEnum.Yes.getValue()
        .equals(businessDeatil.getIsComprehensive())) {
      queueKey =
          CommonConstant.QUEUE_KEY + parentServiceCenter.getParentKey() + ":" + businessDeatil
              .getDeptId();
      //获取审批中心的queueKey
    } else {
      //如果是审批业务员
      queueKey =
          CommonConstant.QUEUE_KEY + serviceCenterBean.getParentKey() + ":" + businessDeatil
              .getDeptId();
    }
    String queueValue = serviceKey + ":" + businessInfo.getId();
    //T插入队列
    //前面排队人数
    //总代办数
    long beforeWaitNumber = businessInfoService
        .getAndroidWaitNumber(serviceKey, businessInfo.getDeptId());
    logger.info(System.currentTimeMillis() + "往redis存入数据开始时间");
    redisUtils.rightPush(queueKey, queueValue);
    logger.info(System.currentTimeMillis() + "往redis存入数据结束时间");
    long waitCount = businessInfoService
        .getAndroidWaitNumber(serviceKey, businessInfo.getDeptId());
    //推送业务
    businessInfoService.popBusinessInfo(serviceCenterBean.getParentKey());
    logger.info(System.currentTimeMillis() + "消息同步开始时间");
    sendMessage(serviceKey, businessInfo.getDeptId(), androidBusinessType);
    logger.info(System.currentTimeMillis() + "消息同步结束时间");
    Map<String, Object> resultMap = ResultUtils.ok("派号成功");
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH时mm分");
    resultMap.put("serviceName", serviceCenterBean.getServiceName());
    resultMap.put("businessName", businessDeatil.getDescribes());
    resultMap.put("waitNumber", waitCount);
    resultMap.put("beforeWaitNumber", beforeWaitNumber);
    resultMap.put("number", (numberPrefix + numberBean.getNumber().toString()));
    resultMap.put("dateTime", sdf.format(new Date()));
    return resultMap;
  }

  /**
   * 判断迭代号表中是否有中心及部门迭代基数，如果没有，添加基数, 如果存在基数，基数叠加 根据部门及审批中心取得迭代号码
   *
   * @param deptId
   * @param serviceKey
   * @return
   */
  public NumberIteration getNumber(Integer deptId, String serviceKey) {
    NumberIteration numberIteration = getNumberIteration(deptId, serviceKey);
    logger.info("获取业务号开始");
    //如果没有，添加基数
    if (numberIteration == null) {
      numberIteration = new NumberIteration();
      numberIteration.setServiceKey(serviceKey);
      numberIteration.setDeptId(deptId);
      numberIteration.setNumber(0);
      int zero = numberIterationDao.addNumberIteration(numberIteration);
      int numberResult = numberIterationDao.numberIteration(numberIteration);
      if (zero == 0 || numberResult == 0) {
        logger.info("取号繁忙，请稍后再取");
        throw new MyException("取号繁忙，请稍后再取");
      }
      //号从1开始
      numberIteration.setNumber(1);
      logger.info("获取业务号结束");
      return numberIteration;
    }
    //存在基数，基数叠加 根据部门及审批中心取得迭代号码
      int numberResult = numberIterationDao.numberIteration(numberIteration);
      if (numberResult == 0) {
        logger.info("取号繁忙，请稍后再取");
        throw new MyException("取号繁忙，请稍后再取");
      }
    //号的基数+1
    numberIteration.setNumber(numberIteration.getNumber() + 1);
    logger.info("获取业务号结束");
    return numberIteration;
  }

  /**
   * 根据村中心serviceKey 获取所有的服务中心 给受理端及叫号机同步排队号
   *
   * @param serviceKey deptId(业务部门)
   */
  @Override
  public void sendMessage(String serviceKey, Integer deptId, String androidBusinessType) {
    //根据审批中心查询服务中心
    ShowBusinessInfo showInfo = null;
    long countNumber = (long) 0;
    SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(deptId);
    //当为一窗综办时
    if (AndroidBusinessTypeEnum.AllProcess.getValue().equals(androidBusinessType)
        && SysDeptTypeEnum.Yes.getValue().equals(deptBean.getType())) {
      countNumber = businessInfoService.getAndroidAllWaitNumber(serviceKey);
    } else {
      //为分部门时
      countNumber = businessInfoService.getAndroidWaitNumber(serviceKey, deptId);
    }
    //查询该中心所有等待办理中的人数
    //根据审批中心的serviceKey 取得统筹中心待办数
    ServiceCenterBean centerBean = serviceCenterDao.getParentServiceCenterByServiceKey(serviceKey);
    List<String> serviceKeys = new ArrayList<>();
    serviceKeys.add(centerBean.getServiceKey());
    serviceKeys.add(centerBean.getParentKey());
    //没有在线等待业务人员时同步消息至安卓端
    sendMessageToAndroid(serviceKey, countNumber);
    //h5推送消息
    businessInfoService
        .sendMessageToAndroidDept(deptId, serviceKey);
    //受理端待处理号消息推送
    List<SysUserBean> userBeans = getOnLineUser(serviceKeys);
    for (SysUserBean user : userBeans) {
      showInfo = businessInfoService.getAcceptShowBusinessInfo(user);
      if (user.getDeptId().equals(deptId)) {
        showInfo.setDoneNumber(businessInfoService.getDoneNumber(user));
        showInfo.setType(CommonConstant.BUSINESS_COUNT);
        showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
        callDeviceWebSocketHandler
            .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), user.getUserId());
      }
      if (user.getDeptId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
        countNumber = businessInfoService.getAcceptAllWaitNumber(user.getServiceKey());
        showInfo = new ShowBusinessInfo(user.getDeptId(), countNumber);
        showInfo.setDoneNumber(businessInfoService.getDoneNumber(user));
        showInfo.setType(CommonConstant.BUSINESS_COUNT);
        showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
        callDeviceWebSocketHandler
            .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), user.getUserId());
      }

    }
  }


  /**
   * 获取在线用户
   */
  public List<SysUserBean> getOnLineUser(List<String> serviceKeys) {
    SysUserBean userBean = new SysUserBean();
    userBean.setState(SysUserStateEnum.OnLine.getValue().toString());
    userBean.setUserState(StateEnum.Effective.getValue().toString());
    userBean.setWorkState(SysUserWorkStateEnum.Processing.getValue().toString());
    userBean.setServiceKeys(serviceKeys);
    return sysUserDao.getUserByParam(userBean);
  }

  /**
   * 获取在线等待业务等用户
   */
  @Override
  public List<SysUserBean> getOnLineUserForWork(List<String> serviceKeys) {
    SysUserBean userBean = new SysUserBean();
    userBean.setState(SysUserStateEnum.OnLine.getValue().toString());
    userBean.setUserState(StateEnum.Effective.getValue().toString());
    userBean.setWorkState(SysUserWorkStateEnum.Waiting.getValue().toString());
    userBean.setServiceKeys(serviceKeys);
    return sysUserDao.getUserByParamAndNoBusinessInfo(userBean);
  }

  @Override
  public NumberIteration getNumberIteration(Integer deptId, String serviceKey) {
    NumberIteration numberParam = new NumberIteration();
    numberParam.setDeptId(deptId);
    numberParam.setServiceKey(serviceKey);
    List<NumberIteration> numberIterations = numberIterationDao.getNumberParam(numberParam);
    if (numberIterations.size() > 0) {
      return numberIterations.get(0);
    } else {
      return null;
    }

  }

  //根据村中心serviceKey,获取所有的村中心推送消息
  private void sendMessageToAndroid(String serviceKey, long countNumber) {
    List<ServiceCenterBean> beans = serviceCenterDao.getServiceCentListByserviceKey(serviceKey);
    for (ServiceCenterBean centerBean : beans) {
      //取得审批中心下所有服务中心叫号机服务接口
      String webSocketKey = CommonConstant.WEBSOCKET_KEY + centerBean.getServiceKey();
      List<String> queueList = redisUtils.listRangeAllStr(webSocketKey);
      for (String queues : queueList) {
        //单部门处理
        String[] queuesArray = queues.split(":");
        String deviceCode = "";
        String sessionId = "";
        if (queuesArray.length > 2) {
          deviceCode = queuesArray[1];
          sessionId = queuesArray[2];
        }
        String type = queuesArray[0];
        List<ShowBusinessInfo> showInfos;
        if (type.equals(AndroidBusinessTypeEnum.DepartmentalProcess.getValue())) {
          //消息推送
          //获取下一个待办理的号
          showInfos = businessInfoService
              .getShowBusinessInfoList(serviceKey, "");

        } else {
          showInfos = businessInfoService
              .getShowBusinessInfosForAndroid(CommonConstant.SUPER_ADMIN_DEPTID_TYPE,
                  centerBean.getServiceKey());
        }
        //消息推送
        androidWebSocketHandler
            .sendMessageToAndroid(new TextMessage(JSON.toJSONString(showInfos)),
                sessionId);
      }
    }
  }


  //根据村中心serviceKey,获取所有的村中心推送消息
  @Override
  public void sendMessageToH5(String serviceKey, Integer deptId) {
    List<ServiceCenterBean> beans = serviceCenterDao.getServiceCentListByserviceKey(serviceKey);
    for (ServiceCenterBean centerBean : beans) {
      //取得审批中心下所有服务中心叫号机服务接口
      String webSocketKey = CommonConstant.WEBSOCKET_KEY + centerBean.getServiceKey();
      //todo for jiangjun
//      List<String> queueList = redisUtils.listRangeAllStr(webSocketKey);
      String queues = (String) redisUtils.get(webSocketKey);
//      for (String queues : queueList) {
      if (queues != null) {
        //单部门处理
        String[] queuesArray = queues.split(":");
        String sessionId = queuesArray[1];
        List<ShowBusinessInfo> showInfos = businessInfoService
            .getShowBusinessInfosForAndroid(CommonConstant.SUPER_ADMIN_DEPTID_TYPE,
                centerBean.getServiceKey());
        //消息推送
        h5WebSocketHandler
            .sendMessageToH5(new TextMessage(JSON.toJSONString(showInfos)), sessionId);
        ShowBusinessInfo depinfos = new ShowBusinessInfo();
        Long waitNumber = businessInfoService.getAndroidWaitNumber(serviceKey, deptId);
        depinfos.setWaitNumber(waitNumber);
        depinfos.setDeptId(deptId);
        depinfos.setType(CommonConstant.DEP_UPDATE_TYPE);
        h5WebSocketHandler.sendMessageToH5(new TextMessage(JSON.toJSONString(depinfos)), sessionId);
      }
    }
  }

  @Override
  public void sendMessageForWorkingByServiceKey(String serviceKey, Integer deptId) {
    //受理端待处理号消息推送
    ServiceCenterBean serviceCenterBean = serviceCenterDao
        .getParentServiceCenterByServiceKey(serviceKey);
    List<String> serviceKeys = new ArrayList<>();
    serviceKeys.add(serviceCenterBean.getServiceKey());
    serviceKeys.add(serviceCenterBean.getParentKey());
    List<SysUserBean> userBeans = getOnLineUser(serviceKeys);
    for (SysUserBean user : userBeans) {
      ShowBusinessInfo showInfo = businessInfoService.getAcceptShowBusinessInfo(user);
      if (user.getDeptId().equals(deptId)) {
        showInfo.setDoneNumber(businessInfoService.getDoneNumber(user));
        showInfo.setType(CommonConstant.BUSINESS_COUNT);
        showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
        callDeviceWebSocketHandler
            .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), user.getUserId());
      }
      if (user.getDeptId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
        Long countNumber = businessInfoService.getAcceptAllWaitNumber(user.getServiceKey());
        showInfo = new ShowBusinessInfo(user.getDeptId(), countNumber);
        showInfo.setDoneNumber(businessInfoService.getDoneNumber(user));
        showInfo.setType(CommonConstant.BUSINESS_COUNT);
        showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
        callDeviceWebSocketHandler
            .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), user.getUserId());
      }

    }
  }

  /**
   * 叫号机为一窗综办时，特殊部门号的前缀字母生成，一窗综办的号是从A开始，特殊部门从B、C依次类推开始
   *
   * @param deptId
   * @return
   */
  private String getNumberPrefix(int deptId) {
    String numberPrefix = "";
    List<SysDeptBean> deptList = sysDeptBeanDao.specialDeptList(SysDeptTypeEnum.No.getValue());
    if (CollectionUtils.isNotEmpty(deptList)) {
      for (int i = 0; i < deptList.size(); i++) {
        if (deptList.get(i).getId() == deptId) {
          numberPrefix = StringUtil.numericalToChar(++i);
          break;
        }
      }
    }
    return numberPrefix;
  }


  @Override
  public Map<String, Object> getBusinessNumberAndModify(String serviceKey, Integer businessId,
      Integer type, Integer appointmentId, String name, String idCard) {
    //判断是否重复取号
    BusinessInfo businessInfo = null;
    if (!StringUtils.isEmpty(idCard)) {
      businessInfo = businessInfoDao.searchBusinessInfo(idCard);
      if (businessInfo != null) {
        return ResultUtils.error("派号失败,该用户已取过号");
      }
    }
    //获取服务中心信息
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenterBean == null || StringUtils.isEmpty(serviceCenterBean.getServiceName())) {
      return ResultUtils.error("派号失败,服务中心信息不存在");
    }
    //取得审批中心的数据
    ServiceCenterBean parentServiceCenter = serviceCenterDao
        .getServiceCenter(serviceCenterBean.getParentKey());
    if (parentServiceCenter == null || StringUtils.isEmpty(parentServiceCenter.getServiceName())) {
      return ResultUtils.error("派号失败,审批中心信息不存在");
    }
    //判断村中心是否有绑定终端
    List<VcDevBean> deviceList = vcDevDao.getDeviceByServiceKey(serviceKey);
    if (CollectionUtils.isEmpty(deviceList)) {
      ResultUtils.error("派号失败，服务中心未绑定终端");
    }
    // 获取业务详情
    BusinessTypeBean businessDeatil = businessTypeDao.selectByPrimaryKey(businessId);
    if (null == businessDeatil) {
      logger.info("派号失败,没有业务");
      return ResultUtils.error("派号失败,没有业务");
    }

    if (isBusinessMonth(businessDeatil.getBusinessMonth())) {
      logger.info(
          "取号失败，该业务办理月为“" + businessDeatil.getBusinessMonth() + "月" + "，当前暂时无法办理。");
      return ResultUtils.error(
          "取号失败，该业务办理月为“" + businessDeatil.getBusinessMonth() + "月" + "，当前暂时无法办理。");
    }
    if (isBusinessDay(businessDeatil.getBusinessDay())) {
      String businessDay = businessDeatil.getBusinessDay();
      businessDay = businessDay.replace(",", "-");
      logger.info(
          "取号失败，该业务办理日为“" + businessDay + "日”，当前暂时无法办理。");
      return ResultUtils.error(
          "取号失败，该业务办理日为“" + businessDay + "日”，当前暂时无法办理。");
    }
    //迭代号码
    //号前添加部门id,id转字母
    SysDeptBean sysDeptBean = sysDeptBeanDao.selectByPrimaryKey(businessDeatil.getDeptId());
    if (null == sysDeptBean) {
      return ResultUtils.error("部门不存在");
    }
    /**
     * 如果是一窗综办，deptId = 0 迭代号基础归属为：dept、审批中心，所有号为：A+number;
     */

    NumberIteration numberBean;
    String handleServiceKey = null;
    String numberPrefix = "A";
    //如果是统筹业务员，serviceKey为统筹中心
    if (AndroidBusinessTypeEnum.AllProcess.getValue()
        .equals(sysDeptBean.getType() == 0 ? "01" : "02")) {
      if (BusinessTypeIsComprehensiveEnum.Yes.getValue()
          .equals(businessDeatil.getIsComprehensive())) {
        handleServiceKey = parentServiceCenter.getParentKey();
        numberBean = getNumber(Integer.valueOf(CommonConstant.SUPER_ADMIN_DEPTID_TYPE),
            parentServiceCenter.getServiceKey());
      } else {
        //如果是审批业务员
        handleServiceKey = serviceCenterBean.getParentKey();
        numberBean = getNumber(Integer.valueOf(CommonConstant.SUPER_ADMIN_DEPTID_TYPE),
            serviceCenterBean.getParentKey());
      }
    } else {
      /**
       * 多部门时，根据部门及审批中心serviceKey 取得号码
       */
      if (BusinessTypeIsComprehensiveEnum.Yes.getValue()
          .equals(businessDeatil.getIsComprehensive())) {
        handleServiceKey = parentServiceCenter.getParentKey();
        numberBean = getNumber(businessDeatil.getDeptId(), parentServiceCenter.getServiceKey());
      } else {
        //如果是审批业务员
        handleServiceKey = serviceCenterBean.getParentKey();
        numberBean = getNumber(businessDeatil.getDeptId(), serviceCenterBean.getParentKey());

      }
      if (SysDeptTypeEnum.No.getValue().equals(sysDeptBean.getType())) {
        numberPrefix = getNumberPrefix(sysDeptBean.getId());
      } else {
        numberPrefix = sysDeptBean.getNumberPrefix();
      }

    }
    businessInfo = new BusinessInfo(
        (numberPrefix + numberBean.getNumber().toString()), serviceKey,
        businessDeatil.getDeptId(),
        businessDeatil.getId(), businessDeatil.getDescribes(),
        type, appointmentId, handleServiceKey, name, idCard);
    int insertResult = businessInfoDao.insertCallInfomation(businessInfo);
    if (insertResult == 0) {
      logger.info("派号失败");
      return ResultUtils.error("派号失败");
    }

    //修改预约业务记录状态
    if (appointmentId != CommonConstant.zero && appointmentId != null) {
      appointmentInfoDao.updateState(appointmentId);
    }
    //等待办理业务人数
    String queueKey;
    //如果是统筹业务，serviceKey为统筹中心
    if (BusinessTypeIsComprehensiveEnum.Yes.getValue()
        .equals(businessDeatil.getIsComprehensive())) {
      queueKey =
          CommonConstant.QUEUE_KEY + parentServiceCenter.getParentKey() + ":" + businessDeatil
              .getDeptId();
      //获取审批中心的queueKey
    } else {
      //如果是审批业务员
      queueKey =
          CommonConstant.QUEUE_KEY + serviceCenterBean.getParentKey() + ":" + businessDeatil
              .getDeptId();
    }
    String queueValue = serviceKey + ":" + businessInfo.getId();
    //T插入队列
    //前面排队人数
    //总代办数
    long beforeWaitNumber;
    long waitCount;
    if (sysDeptBeanDao.getDeptByDeptId(businessInfo.getDeptId()).getType() == 0) {
      beforeWaitNumber = businessInfoService
          .getAndroidAllWaitNumber(serviceKey);
      redisUtils.rightPush(queueKey, queueValue);
      waitCount = businessInfoService
          .getAndroidAllWaitNumber(serviceKey);
    } else {
      beforeWaitNumber = businessInfoService
          .getAndroidWaitNumber(serviceKey, businessInfo.getDeptId());
      redisUtils.rightPush(queueKey, queueValue);
      waitCount = businessInfoService
          .getAndroidWaitNumber(serviceKey, businessInfo.getDeptId());
    }
    //推送业务
    businessInfoService.popBusinessInfo(serviceCenterBean.getParentKey());
    sendMessageAll(serviceKey, businessInfo.getDeptId());
    Map<String, Object> resultMap = ResultUtils.ok("派号成功");
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH时mm分");
    resultMap.put("serviceName", serviceCenterBean.getServiceName());
    resultMap.put("businessName", businessDeatil.getDescribes());
    resultMap.put("waitNumber", waitCount);
    resultMap.put("beforeWaitNumber", beforeWaitNumber);
    resultMap.put("number", (numberPrefix + numberBean.getNumber().toString()));
    resultMap.put("dateTime", sdf.format(new Date()));
    resultMap.put("name", name);
    return resultMap;
  }


  @Override
  public void sendMessageAll(String serviceKey, Integer deptId) {
    //根据审批中心查询服务中心
    ShowBusinessInfo showInfo = null;
    long countNumber = (long) 0;
    SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(deptId);
    //当为一窗综办时
    countNumber = businessInfoService.getAndroidAllWaitNumber(serviceKey);
    //查询该中心所有等待办理中的人数
    //根据审批中心的serviceKey 取得统筹中心待办数
    ServiceCenterBean centerBean = serviceCenterDao.getParentServiceCenterByServiceKey(serviceKey);
    List<String> serviceKeys = new ArrayList<>();
    serviceKeys.add(centerBean.getServiceKey());
    serviceKeys.add(centerBean.getParentKey());
    //没有在线等待业务人员时同步消息至H5
    sendMessageToH5(serviceKey, deptId);
    //h5推送消息
    // businessInfoService
    //     .sendMessageToH5Dept(deptId, serviceKey);
    //受理端待处理号消息推送
    List<SysUserBean> userBeans = getOnLineUser(serviceKeys);
    for (SysUserBean user : userBeans) {
      showInfo = businessInfoService.getAcceptShowBusinessInfo(user);
      if (user.getDeptId().equals(deptId)) {
        showInfo.setDoneNumber(businessInfoService.getDoneNumber(user));
        showInfo.setType(CommonConstant.BUSINESS_COUNT);
        showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
        callDeviceWebSocketHandler
            .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), user.getUserId());
      }
      if (user.getDeptId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
        countNumber = businessInfoService.getAcceptAllWaitNumber(user.getServiceKey());
        showInfo = new ShowBusinessInfo(user.getDeptId(), countNumber);
        showInfo.setDoneNumber(businessInfoService.getDoneNumber(user));
        showInfo.setType(CommonConstant.BUSINESS_COUNT);
        showInfo.setCode(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
        callDeviceWebSocketHandler
            .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), user.getUserId());
      }

    }
  }


  @Override
  public Map<String, Object> searchBusinessInfo(String idCardNumber, String serviceKey) {

    BusinessInfo businessInfo = businessInfoService.searchBusinessInfo(idCardNumber);
    if (businessInfo == null) {
      return ResultUtils.error("当前用户没有取号信息");
    }

    //获取服务中心信息
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(serviceKey);
    //总代办数
    long beforeWaitNumber = businessInfoService
        .getAndroidWaitNumber(serviceKey, businessInfo.getDeptId());
    long waitCount = businessInfoService
        .getAndroidWaitNumber(serviceKey, businessInfo.getDeptId());
    Map<String, Object> resultMap = ResultUtils.ok("查询排队进度成功");
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH时mm分");
    resultMap.put("serviceName", serviceCenterBean.getServiceName());
    // resultMap.put("businessName", businessDeatil.getDescribes());
    resultMap.put("waitNumber", waitCount);
    resultMap.put("number", businessInfo.getNumber());
    resultMap.put("name", businessInfo.getName());
    resultMap.put("beforeWaitNumber", beforeWaitNumber);
    //resultMap.put("number", (numberPrefix + numberBean.getNumber().toString()));
    resultMap.put("dateTime", sdf.format(new Date()));
    return resultMap;
  }
}
