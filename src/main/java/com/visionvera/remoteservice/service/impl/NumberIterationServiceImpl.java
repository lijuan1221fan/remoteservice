package com.visionvera.remoteservice.service.impl;

import com.visionvera.common.enums.BusinessInfoStateEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.NumberBean;
import com.visionvera.remoteservice.bean.NumberIteration;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.DeviceInfoDao;
import com.visionvera.remoteservice.dao.FormsDao;
import com.visionvera.remoteservice.dao.NumberDao;
import com.visionvera.remoteservice.dao.NumberIterationDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.NumberIterationService;
import com.visionvera.remoteservice.util.DateUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.visionvera.remoteservice.service.NumberService;

/**
 * ClassName: NumberIterationServiceImpl
 *
 * @author quboka
 * @Description: 号码递增
 * @date 2018年3月22日
 */
@Service
public class NumberIterationServiceImpl implements NumberIterationService {

  private Logger logger = LogManager.getLogger(getClass());

  @Resource
  private NumberIterationDao numberIterationDao;

  @Resource
  private NumberDao numberDao;
  @Resource
  private BusinessInfoDao businessInfoDao;

  @Resource
  private ServiceCenterDao serviceCenterDao;

  @Resource
  private BusinessTypeDao businessTypeDao;

  @Resource
  private SysDeptBeanDao sysDeptBeanDao;

  // @Resource
  // private JedisClient jedisClient;

  @Resource
  private BusinessInfoService businessInfoService;

  @Resource
  private DeviceInfoDao deviceInfoDao;
  @Resource
  private FormsDao formsDao;
  @Resource
  private CommonConfigDao commonConfigDao;
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private NumberService numberService;


  /**
   * 叫号接口
   *
   * @param serviceKey 服务中心唯一标识
   * @param deptId 部门id
   * @param businessType 业务类型
   * @return
   * @
   */
  @Override
  public Map<String, Object> getNumberAndModify(String serviceKey, Integer deptId,
      String businessType) {
    return null;
  }



  /**
   * @param session
   * @return Map<String, Object>
   * @Description: 获取当天总任务数
   * @author quboka
   * @date 2018年5月24日
   */
  @Override
  public Map<String, Object> totalNumberOfTasks(HttpSession session) {
    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    //获取用户
    // UserBean user = (UserBean) session.getAttribute("user");
    SysUserBean user = ShiroUtils.getUserEntity();
    Integer deptId = user.getDeptId();
    if (deptId == null) {
      logger.info("获取任务失败。当前登陆用户无权限");
      return ResultUtils.error("获取任务失败。当前登陆用户无权限。");
    }
    String businessQueueKey = CommonConstant.QUEUE_KEY + user.getServiceKey() + ":" + deptId;
    // Long waitCount = jedisClient.llen(businessQueueKey);
    Long waitCount = redisUtils.listLength(businessQueueKey);
    //        Integer doneCount = businessDao.getTodayTasksNumByTypeAndState(null, type, 2);
    //等待人数
    resultMap.put("waitCount", waitCount);
    //完成人数
//        resultMap.put("doneCount", doneCount);
    return ResultUtils.ok("获取当前总任务数成功。", resultMap);

  }

  /**
   * 根据审批中心的业务员当前服务中心排队人数
   *
   * @param session 用户session
   * @param serviceKey 服务中心key
   * @return
   */
  @Override
  public Map<String, Object> getWaitingNumber() {
    SysUserBean userBean = ShiroUtils.getUserEntity();
    Map<String, Object> resultMap = new HashMap<>();
    if (StringUtil.isNotNull(userBean.getServiceKey())) {
      //   ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
      Map param = new HashMap();
      param.put("parentKey", userBean.getServiceKey());
      List<ServiceCenterBean> serviceCenter = serviceCenterDao
          .getServiceCenterByServiceCenterKey(param);
      if (serviceCenter.size() == CommonConstant.zero) {
        logger.info("服务中心不存在");
        return ResultUtils.error("服务中心不存在");
      }
      SysDeptBean sysDeptBean = new SysDeptBean();
      if(userBean.getDeptId() != 0){
        sysDeptBean = sysDeptBeanDao.selectByPrimaryKey(userBean.getDeptId());
        if (null == sysDeptBean) {
          return ResultUtils.error("部门不存在");
        }
      }
      //    String queueKey = CommonConstant.QUEUE_KEY + serviceCenter.getParentKey() + "*";
      Long totalWaitCount = 0L;
      Integer totalDoneCount = 0;
      if(userBean.getDeptId() == 0){
        //查询所有可用部门
        List<SysDeptBean> sysDeptListNotInDelete = sysDeptBeanDao.getSysDeptListNotInDelete();
        for(SysDeptBean dept : sysDeptListNotInDelete){
          String queueKey = CommonConstant.QUEUE_KEY + userBean.getServiceKey() + ":" + dept.getId();
          Long waitCount = redisUtils.listLength(queueKey);
          totalWaitCount += waitCount;
          //doneCount
          BusinessInfo businessInfo = new BusinessInfo();
          businessInfo.setDeptId(dept.getId());
          businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
          businessInfo.setStartTime(DateUtil.getDateInMinutesAndSecondsZero(new Date()));
          businessInfo.setEndTime(DateUtil.getDateInMinutesAndSecondsMax(new Date()));
          businessInfo.setParentKey(userBean.getServiceKey());
          businessInfo.setOperatorId(userBean.getUserId());
          Integer doneCount = businessInfoDao.selectByObject(businessInfo).size();
          // businessInfo.setState(BusinessInfoStateEnum.Processing.getValue());
          totalDoneCount += doneCount;
        }
        //根据办理人查询正在处理的业务
        BusinessInfo businessByUserId = businessInfoDao.getBusinessByUserId(ShiroUtils.getUserId());
        resultMap
            .put("doingCount", businessByUserId == null?CommonConstant.zero : businessByUserId.getNumber());
      }else{
        String queueKey = CommonConstant.QUEUE_KEY + userBean.getServiceKey() + ":" + userBean.getDeptId();
        Long waitCount = redisUtils.listLength(queueKey);
        resultMap.put("waitCount", waitCount);
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setDeptId(userBean.getDeptId());
        businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
        businessInfo.setStartTime(DateUtil.getDateInMinutesAndSecondsZero(new Date()));
        businessInfo.setEndTime(DateUtil.getDateInMinutesAndSecondsMax(new Date()));
        businessInfo.setParentKey(userBean.getServiceKey());
        businessInfo.setOperatorId(userBean.getUserId());
        Integer doneCount = businessInfoDao.selectByObject(businessInfo).size();
        // businessInfo.setState(BusinessInfoStateEnum.Processing.getValue());
        //已处理人数
        resultMap.put("doneCount", doneCount);

        NumberIteration numberbean = numberService.getNumberIteration(userBean.getDeptId(),userBean.getServiceKey());
        //排队号
        resultMap
            .put("doingCount", numberbean.getNumber() == CommonConstant.zero ? CommonConstant.zero
                : (sysDeptBean.getNumberPrefix() + numberbean.getNumber()));
      }
      if(totalWaitCount != 0L){
        resultMap.put("waitCount", totalWaitCount);
      }
      if(totalDoneCount != 0){
        resultMap.put("doneCount", totalDoneCount);
      }
      return ResultUtils.ok("查询成功", resultMap);
    }
    SysUserBean user = ShiroUtils.getUserEntity();
    if (null != user) {
      String queueKey = CommonConstant.QUEUE_KEY + user.getServiceKey() + ":" + user.getDeptId();
      // Long waitCount = jedisClient.llen(queueKey);
      Long waitCount = redisUtils.listLength(queueKey);

      BusinessInfo businessInfo = new BusinessInfo();
      businessInfo.setDeptId(user.getDeptId());
      businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
      Integer doneCount = businessInfoDao.selectByObject(businessInfo).size();
      //等待人数
      resultMap.put("waitCount", waitCount);
      //完成人数
      resultMap.put("doneCount", doneCount);
    }
    return ResultUtils.ok("获取当前总任务数成功。", resultMap);
  }

  @Override
  public Map<String, Object> totalNumberOfTasks(Map<String, Object> params, HttpSession session) {
    Map<String, Object> resultMap = new HashMap<>();
    SysUserBean sysUserBean = ShiroUtils.getUserEntity();
    //deptId为0表示查询全部部门
    String deptId = (String) params.get("deptId");
    String startDate = (String) params.get("startDate");
    String endDate = (String) params.get("endDate");
//        ServiceCenterBean condition = new ServiceCenterBean();
//        condition.setParentKey(sysUserBean.getServiceKey());
//        List<String> serviceCenterList = new ArrayList<>();
//        serviceCenterList.add(sysUserBean.getServiceKey());
//        List<ServiceCenterBean> serviceCenterByCondition = serviceCenterDao.getServiceCenterByCondition(condition);
//        if(serviceCenterByCondition.size() > 0){
//            for(){
//
//            }
//        }
    //管理端需要已处理，正在处理，待处理
    String type = sysUserBean.getType();
    if (CommonConstant.SUPER_ADMIN.equals(type) || CommonConstant.FIRST_ADMIN.equals(type)
        || CommonConstant.SECOND_ADMIN.equals(type)) {
      if ("0".equals(deptId)) {
        //全部部门
        List<SysDeptBean> deptList = sysDeptBeanDao.getDeptList(null);
        int totalWaitCount = CommonConstant.zero;
        int totalDoneCount = CommonConstant.zero;
        int totalDoingCount = CommonConstant.zero;
        for (SysDeptBean deptBean : deptList) {
          String queueKey = CommonConstant.QUEUE_KEY + sysUserBean.getServiceKey() + ":" + deptId;
          // Long waitCount = jedisClient.llen(queueKey);
          Long waitCount = redisUtils.listLength(queueKey);

          totalWaitCount += waitCount;
          BusinessInfo businessInfo = new BusinessInfo();
          businessInfo.setDeptId(Integer.valueOf(deptId));
          businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
          businessInfo.setServiceKey(sysUserBean.getServiceKey());
          Integer doneCount = businessInfoDao.selectByObject(businessInfo).size();
          totalDoneCount += doneCount;
          //根据部门，servicekey，查询状态为处理中的业务
          Integer doingCount = businessInfoService
              .getBusinessesByCondition(deptId, sysUserBean.getServiceKey(),
                  CommonConstant.BUSINESS_HANDING).size();
          totalDoingCount += doingCount;
        }
        //等待人数
        resultMap.put("waitCount", totalWaitCount);
        //完成人数
        resultMap.put("doneCount", totalDoneCount);
        //正在处理
        resultMap.put("doingCount", totalDoingCount);
        return resultMap;
      } else {
        //指定部门
        String queueKey = CommonConstant.QUEUE_KEY + sysUserBean.getServiceKey() + ":" + deptId;
        // Long waitCount = jedisClient.llen(queueKey);
        Long waitCount = redisUtils.listLength(queueKey);

        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setDeptId(Integer.valueOf(deptId));
        businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
        businessInfo.setServiceKey(sysUserBean.getServiceKey());
        Integer doneCount = businessInfoDao.selectByObject(businessInfo).size();
        //根据部门，servicekey，查询状态为处理中的业务
        Integer doingCount = businessInfoService
            .getBusinessesByCondition(deptId, sysUserBean.getServiceKey(),
                CommonConstant.BUSINESS_HANDING).size();
        //等待人数
        resultMap.put("waitCount", waitCount);
        //完成人数
        resultMap.put("doneCount", doneCount);
        //正在处理
        resultMap.put("doingCount", doingCount);
        return resultMap;
      }

    } else if (CommonConstant.COMMON_USER.equals(type)) {
      //业务端需要当前待办人数，今日已办人数，当前排队号
      String queueKey = CommonConstant.QUEUE_KEY + sysUserBean.getServiceKey() + ":" + deptId;
      // Long waitCount = jedisClient.llen(queueKey);
      Long waitCount = redisUtils.listLength(queueKey);

      BusinessInfo businessInfo = new BusinessInfo();
      businessInfo.setOperatorId(sysUserBean.getUserId());
      businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.set(Calendar.HOUR_OF_DAY, CommonConstant.zero);
      calendar.set(Calendar.MINUTE, CommonConstant.zero);
      calendar.set(Calendar.SECOND, CommonConstant.zero);
      Date zero = calendar.getTime();
      businessInfo.setUpdateTime(zero.toString());
      int doneCount = businessInfoDao.getBusinessList(businessInfo).size();
      //等待人数
      resultMap.put("waitCount", waitCount);
      //完成人数
      resultMap.put("doneCount", doneCount);
      return resultMap;
    }
    return new HashMap<>();
  }
}
