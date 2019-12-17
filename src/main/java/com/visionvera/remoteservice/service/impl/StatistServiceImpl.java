package com.visionvera.remoteservice.service.impl;

import com.visionvera.common.enums.BusinessInfoStateEnum;
import com.visionvera.common.enums.CenterTypeEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.FinalStateEnum;
import com.visionvera.common.enums.ServiceCenterTypeEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.*;
import com.visionvera.remoteservice.pojo.StatisticsVo;
import com.visionvera.remoteservice.pojo.SysDeptVo;
import com.visionvera.remoteservice.service.AreaService;
import com.visionvera.remoteservice.service.ServiceCenterService;
import com.visionvera.remoteservice.service.StatistService;
import com.visionvera.remoteservice.util.DateUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;


import com.visionvera.remoteservice.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * ClassName: StatistServiceImpl
 *
 * @author quboka
 * @Description: 统计
 * @date 2018年4月9日
 */
@Service
public class StatistServiceImpl implements StatistService {
    private static Logger logger = LoggerFactory.getLogger(StatistServiceImpl.class);
    @Resource
    private StatistDao statistDao;
    @Resource
    private SysDeptBeanDao sysDeptBeanDao;
    @Resource
    private ServiceCenterDao serviceCenterDao;
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private BusinessTypeDao businessTypeDao;
    @Resource
    private AreaDao areaDao;
    @Resource
    private ServiceCenterService serviceCenterService;
    @Resource
    private AreaService areaService;


    /**
    /**
     * @param paramsMap
     * @return Map<String, Object>
     * @Description: 获取统计数据
     * @author quboka
     * @date 2018年4月9日
     */
    @Override
    public Map<String, Object> getStatistData(StatisticsVo statisticsVo) {
        SysUserBean userBean = ShiroUtils.getUserEntity();
        Map<Object, Object> returnMap = new HashMap<Object, Object>();
        LinkedList list = new LinkedList();
        Map<Object, Object> statisByDept = new HashMap<Object, Object>();
        Map<Object, Object> partStatistByDept = new HashMap<Object, Object>();

    //部门
    Integer deptId = statisticsVo.getDeptId();
    //开始时间
    //Date startDate = statisticsVo.getStartDate();
    //结束时间
    //Date endDate = statisticsVo.getEndDate();
    //服务中心
    String serviceKey = statisticsVo.getServiceKey();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenter != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == serviceCenter.getType().intValue()) {
      statisticsVo.setServiceKey(serviceCenterDao.
              getServiceKeysByParentServiceKeys(Collections.singletonList(serviceKey)));
    }
    /*//不同管理员登陆时，查看的数据权限不一致：
    //1.admin登陆时，所有数据
    List<String> serviceKeys= null;
    //2.当为审批中心显示相应中心的数据
    if(SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType()))
    {
      serviceKeys = new ArrayList<>();
      serviceKeys.add(userBean.getServiceKey());

    }else if(SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType()))
    {  //2.当为统筹中心管理员时，显示相应中心的数据及下属级别的数据
      List<ServiceCenterBean> serviceCenterBeans = serviceCenterDao.getServiceCenterByParentKey(userBean.getServiceKey());
      serviceKeys = new ArrayList<>();
      serviceKeys.add(userBean.getServiceKey());
      for(ServiceCenterBean bean:serviceCenterBeans){
        serviceKeys.add(bean.getServiceKey());
      }

    }*/
    //区域统计
   /* List<StatistBasics> regionCount = this
        .getRangeStatisticsData(serviceKeys,deptId, startDate, endDate);
    partStatistByDept.put("regionCount", regionCount);*/

    Date endDate = new Date();
    Date startDate = new Date(endDate.getTime() - 15 * 24 * 60 * 60 * 1000);

    //办理趋势统计
    Map<String, Object> handleTrend = this.getHandleTheTrend(statisticsVo, deptId, startDate, endDate);
    partStatistByDept.put("handleTrend", handleTrend);

    //业务办结占比（近七日）
    startDate = new Date(endDate.getTime() - 7 * 24 * 60 * 60 * 1000);
    Map<String, Object> handleProportion = this.getHandleProportion(statisticsVo, deptId, startDate, endDate);
    partStatistByDept.put("handleProportion", handleProportion);

    //文化程度统计
    List<StatistBasics> education = this
            .getCultureStandardStatistics(statisticsVo, deptId);
    partStatistByDept.put("education", education);

    //年龄分布统计
    List<StatistBasics> ageDistribution = this
            .getAgeDistributionStatistics(statisticsVo, deptId);
    partStatistByDept.put("ageDistribution", ageDistribution);
    returnMap.put("partStatistByDept",partStatistByDept);
    //0代表所有部门
    if (!userBean.getDeptId().equals(Integer.valueOf(CommonConstant.SUPER_ADMIN_DEPTID_TYPE))) {
      deptId = userBean.getDeptId();
    }
    List<SysDeptBean> deptList = sysDeptBeanDao.getSysDeptListNoPage(deptId);

    for (SysDeptBean dept : deptList) {
      partStatistByDept = new HashMap<Object, Object>();
      statisByDept = new HashMap<Object, Object>();
    /*  //区域统计
      List<StatistBasics> regionCount = this
          .getRangeStatisticsData(dept.getId(), startDate, endDate);
      partStatistByDept.put("regionCount", regionCount);

      //办理趋势统计
      Map<String, Object> handleTrend = this.getHandleTheTrend(dept.getId(), startDate, endDate);
      partStatistByDept.put("handleTrend", handleTrend);

      //文化程度统计
      List<StatistBasics> education = this
          .getCultureStandardStatistics(dept.getId(), startDate, endDate);
      partStatistByDept.put("education", education);

      //年龄分布统计
      List<StatistBasics> ageDistribution = this
          .getAgeDistributionStatistics(dept.getId(), startDate, endDate);
      partStatistByDept.put("ageDistribution", ageDistribution);*/

      //业务分类统计部门下面的分类
      /*List<StatistBasics> businessType = this
          .getBusinessClassification(serviceKeys,dept.getId(), startDate, endDate);
      partStatistByDept.put("businessType", businessType);*/

      //办理状态统计
      statisByDept.put("deptId", dept.getId());
      statisByDept.put("deptName", dept.getDeptName());
      statisByDept.put("deptList", partStatistByDept);
      statisByDept.put("dept", dept);
      list.add(statisByDept);
    }
    returnMap.put("dataBusiness", list);
    /*List<StatistBasics> processed = this.getHandleTheState(serviceKeys,deptId, startDate, endDate);
    returnMap.put("BusinessStatus", processed);*/
    return ResultUtils.ok("获取统计数据成功。", returnMap);
  }

  /**
   * 时间段统计
   *
   * @param deptId
   * @param startDate
   * @param endDate
   * @return
   */
  private List<StatistBasics> getBusinessTimeStatistics(List<String> serviceKeys,Integer deptId, Date startDate,
      Date endDate) {
    List<StatistBasics> list = statistDao.getBusinessTimeStatistics(serviceKeys,deptId, startDate, endDate);
    logger.info("时间段统计:" + list);
    return list;
  }

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return StatistBasics
   * @Description: 区域统计
   * @author quboka
   * @date 2018年4月9日
   */
  public List<StatistBasics> getRangeStatisticsData(List<String> serviceKeys,Integer deptId, Date startDate, Date endDate) {
    //村名：业务总个数
    List<StatistBasics> statistBasicsList = statistDao
        .getRangeStatisticsData(serviceKeys,deptId, startDate, endDate);
    logger.info("区域统计:" + statistBasicsList);
    return statistBasicsList;
  }

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return
   * @Description: 办理趋势统计
   * @author quboka
   * @date 2018年4月9日
   */
  public Map<String, Object> getHandleTheTrend(StatisticsVo vo, Integer deptId, Date startDate, Date endDate) {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      if (vo.getServiceKeys() != null && vo.getServiceKeys().size() == 0) {
          List<StatistBasics> list = new ArrayList<>();
          logger.info("该统筹或审批中心下没有服务中心");
          resultMap.put("handleTrend", complement(startDate, endDate, list));
          return resultMap;
      }
      //日期：业务总个数
      List<StatistBasics> handleTrendList = complement(startDate, endDate,
              statistDao.getBusinessHandleTheTrend(vo.getServiceKeys(), deptId, null));
      logger.info("业务趋势数量:" + handleTrendList);
      resultMap.put("handleTrend", handleTrendList);
      return resultMap;
  }

  /**
   * 业务办结占比
   *
   * @param vo
   * @param deptId
   * @param startDate
   * @param endDate
   * @return
   */
  public Map<String, Object> getHandleProportion(StatisticsVo vo, Integer deptId, Date startDate, Date endDate) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    //日期：业务总个数
      if (vo.getServiceKeys() != null && vo.getServiceKeys().size() == 0) {
          List<StatistBasics> list = new ArrayList<>();
          logger.info("该统筹或审批中心下没有服务中心");
          resultMap.put("failNumber", complement(startDate, endDate, list));
          resultMap.put("successNumber", complement(startDate, endDate, list));
          return resultMap;
      }
    List<StatistBasics> untreatedList = complement(startDate, endDate,
            statistDao.getHandleProportion(vo.getServiceKeys(), deptId,
                    FinalStateEnum.NotHandled.getValue()));
    logger.info("业务数量:"+untreatedList);
    //日期:已完成个数
    List<StatistBasics> handledList = complement(startDate, endDate,
            statistDao.getHandleProportion(vo.getServiceKeys(), deptId,
                    FinalStateEnum.Processed.getValue()));
    logger.info("已完成数量:"+handledList);
      resultMap.put("failNumber", untreatedList);
      resultMap.put("successNumber", handledList);
    return resultMap;
  }

  private List<StatistBasics> complement(Date startDate, Date endDate,
      List<StatistBasics> untreatedList) {
    List<StatistBasics> lisData = new ArrayList();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    while (startDate.getTime() <= endDate.getTime()) {
      Boolean flag = true;
      for (StatistBasics statistBasics : untreatedList) {
        if (statistBasics.getName().equals(df.format(startDate))) {
          lisData.add(statistBasics);
          flag = false;
        }
      }
      if (flag) {
        lisData.add(new StatistBasics(df.format(startDate), 0));
      }
      Calendar calstart = Calendar.getInstance();
      calstart.setTime(startDate);
      calstart.add(java.util.Calendar.DAY_OF_WEEK, 1);
      startDate = calstart.getTime();
    }
    return lisData;
  }

  /**
   * @param deptId
   * @param startDate 开始时间
   * @param endDate 结束时间   @return
   * @return Map<String   ,   Object>
   * @Description: 业务分类统计
   * @
   * @author quboka
   * @date 2018年4月9日
   */
  public List getBusinessClassification(List<String> serviceKeys,Integer deptId, Date startDate, Date endDate) {
    //  业务类别 : 业务总个数
    List<StatistBasicsForBusinessType> list = statistDao
        .getBusinessClassification(serviceKeys,deptId, startDate, endDate);
    logger.info("业务类别:" + list);
    List businessList = new ArrayList();
    Map map = null;
    for (StatistBasicsForBusinessType businessType : list) {
      List<StatistBasics> businessTypes = statistDao
          .getBusinessClassificationBySecondTypes(serviceKeys,businessType.getId(), startDate, endDate);
      businessType.setBusinessStatisStatus(businessTypes);
    }

    logger.info("业务类别及业务统计:" + list);
    return list;
  }
  /**
   * @param 业务类型ID
   * @param startDate 开始时间
   * @param endDate 结束时间   @return
   * @return Map<String   ,   Object>
   * @Description: 业务分类统计
   * @
   * @author quboka
   * @date 2018年4月9日
   */
  public Map<String, Object> getBusinessDetailByBusinessTypeId(Integer businessTypeId, Date startDate, Date endDate) {
    //不同管理员登陆时，查看的数据权限不一致：
    //1.admin登陆时，所有数据  2.当为审批中心或者统筹中心管理员时，显示相应中心的数据及下属级别的数据
    List<String> serviceKeys = null;
    SysUserBean userBean = ShiroUtils.getUserEntity();
    //2.当为审批中心显示相应中心的数据
    if(SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType()))
    { serviceKeys = new ArrayList<>();
      serviceKeys.add(userBean.getServiceKey());

    }
    else if(SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType()))
    {  //2.当为统筹中心管理员时，显示相应中心的数据及下属级别的数据
      List<ServiceCenterBean> serviceCenterBeans = serviceCenterDao.getServiceCenterByParentKey(userBean.getServiceKey());
      serviceKeys = new ArrayList<>();
      for(ServiceCenterBean bean:serviceCenterBeans){
        serviceKeys.add(bean.getServiceKey());
      }

    }
    List<StatistBasics> businessTypes = statistDao
        .getBusinessClassificationBySecondTypes(serviceKeys,businessTypeId, startDate, endDate);
    logger.info("业务类别及业务统计:" + businessTypes);
    return ResultUtils.check("查询业务统计成功",businessTypes);
  }

  /**
   * @param deptId 类型  1000：公安远程服务 2000：社保远程服务  0:全部
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return List<StatistBasics>
   * @Description: 办理状态统计
   * @
   * @author quboka
   * @date 2018年4月9日
   */
  public List<StatistBasics> getHandleTheState(List<String> serviceKeys,Integer deptId, Date startDate, Date endDate) {
    List<StatistBasics> statistBasicsList = new ArrayList<>();
    StatistBasics businessStatusProcessed = statistDao.getProcessed(serviceKeys,deptId, startDate, endDate);
    StatistBasics businessStatusProcessing = statistDao.getProcessing(serviceKeys,deptId, startDate, endDate);
    StatistBasics businessStatusUntreated = statistDao.getUntreated(serviceKeys,deptId, startDate, endDate);
    statistBasicsList.add(businessStatusProcessed);
    statistBasicsList.add(businessStatusProcessing);
    statistBasicsList.add(businessStatusUntreated);
    return statistBasicsList;
  }

  /**
   * @param deptId 部门id
   * @return List<StatistBasics>
   * @Description: 年龄分布统计
   * @
   * @author quboka
   * @date 2018年4月10日
   */
  public List<StatistBasics> getAgeDistributionStatistics(StatisticsVo vo, Integer deptId) {
    //年龄区间：已完成个数
    List<StatistBasics> statistBasicsList = null;
      statistBasicsList = statistDao
              .getAgeDistributionStatistics(vo.getServiceKeys(), deptId);
    logger.info("年龄分布统计:" + statistBasicsList);
    return statistBasicsList;
  }

  /**
   * @param deptId 部门id
   * @return List<StatistBasics>
   * @Description: 文化程度统计
   * @
   * @author quboka
   * @date 2018年4月10日
   */
  public List<StatistBasics> getCultureStandardStatistics(StatisticsVo vo, Integer deptId) {
      List<StatistBasics> statistBasicsList = null;
      //学历层次 ：已完成个数
      statistBasicsList = statistDao.getCultureStandardStatistics(
              vo.getServiceKeys(), deptId);
      logger.info("文化程度统计:" + statistBasicsList);
      return statistBasicsList;
  }

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return List<StatistBasics>
   * @Description: 办理业务统计  总业务详情办理统计
   * @author ljfan
   * @date 2018年12月12日
   */
  public Map<String, Object> businessClassification(StatisticsVo statisticsVo) {
    SysUserBean    userBean = ShiroUtils.getUserEntity();
    //不同管理员登陆时，查看的数据权限不一致：
    //1.admin登陆时，所有数据
    List<String> serviceKeys= null;
    //2.当为审批中心显示相应中心的数据
    if (SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType())) {
      serviceKeys = new ArrayList<>();
      serviceKeys.add(userBean.getServiceKey());

    } else if (SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType())) {  //2.当为统筹中心管理员时，显示相应中心的数据及下属级别的数据
      List<ServiceCenterBean> serviceCenterBeans = serviceCenterDao.getServiceCenterByParentKey(userBean.getServiceKey());
      serviceKeys = new ArrayList<>();
      for(ServiceCenterBean bean:serviceCenterBeans){
        serviceKeys.add(bean.getServiceKey());
      }

        }
        List<StatistBasics> statistBasicsList = statistDao
                .businessClassification(serviceKeys, statisticsVo.getDeptId(), statisticsVo.getStartDate(),
                        statisticsVo.getEndDate());
        logger.info("办理业务统计:" + statistBasicsList);
        return ResultUtils.ok("查询成功", statistBasicsList);
    }

    /**
     * 获取热门top5业务
     *
     * @return
     */
    @Override
    public Map<String, Object> getHotBusinessTopFive(String handleServiceKey, Integer deptId) {
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心 显示对应审批中心下的热门业务
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                List<StatistBasics> hotBusinessTopFive = statistDao.getHotBusinessTopFiveByCheck(handleServiceKey, deptId);
                return ResultUtils.ok("获取审批中心热门业务成功", hotBusinessTopFive);
            }
            //统筹中心 显示统筹中心以及其审批中心下的业务
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                //最终查询的中心key集合
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                List<StatistBasics> hotBusinessTopFive = statistDao.getHotBusinessTopFiveByAdmin(serviceKeyList, deptId);
                return ResultUtils.ok("获取统筹及其下审批中心热门业务成功", hotBusinessTopFive);
            }
        }
        List<StatistBasics> hotBusinessTopFive = statistDao.getHotBusinessTopFive(deptId);
        return ResultUtils.ok("获取全部热门业务成功", hotBusinessTopFive);
    }

    /**
     * 近7天 办理时段业务量统计
     *
     * @param handleServiceKey
     * @param deptId
     * @return
     */
    @Override
    public Map<String, Object> getHandlingTimeStatistics(String handleServiceKey, Integer deptId) {
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心 显示对应审批中心下的热门业务
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                List<StatistBasics> dataList = getHandlingTimeStatisticsDataListByCheck(handleServiceKey, deptId);
                return ResultUtils.ok("获取审批中心小时统计业务量成功", dataList);
            }
            //统筹中心 显示统筹中心以及其审批中心下的业务
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                //最终查询的中心key集合
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                List<StatistBasics> dataList = getHandlingTimeStatisticsDataListByAdmin(deptId, serviceKeyList);
                return ResultUtils.ok("获取统筹中心小时统计业务量成功", dataList);
            }
        }
        //查询全部中心
        List<StatistBasics> dataList = getHandlingTimeStatisticsDataList(deptId);
        return ResultUtils.ok("获取全部中心小时统计业务量成功", dataList);
    }

    /**
     * 业务平均办理时长统计（近30日）
     */
    @Override
    public Map<String, Object> getAverageHandlingTime(String handleServiceKey, Integer deptId) {
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心 显示对应审批中心下的热门业务
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                HandlingTimeCountBean averageHandlingTimeByCheck = statistDao.getAverageHandlingTimeByCheck(handleServiceKey, deptId);
                List<StatistBasics> dataList = getAverageHandlingTimeDataList(averageHandlingTimeByCheck);
                return ResultUtils.ok("获取审批中心平均办理时长成功", dataList);
            }
            //统筹中心 显示统筹中心以及其审批中心下的业务
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                //最终查询的中心key集合
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                HandlingTimeCountBean averageHandlingTimeByAdmin = statistDao.getAverageHandlingTimeByAdmin(serviceKeyList, deptId);
                List<StatistBasics> dataList = getAverageHandlingTimeDataList(averageHandlingTimeByAdmin);
                return ResultUtils.ok("获取统筹及其下审批中心平均办理时长成功", dataList);
            }
        }
        HandlingTimeCountBean averageHandlingTime = statistDao.getAverageHandlingTime(deptId);
        List<StatistBasics> dataList = getAverageHandlingTimeDataList(averageHandlingTime);
        return ResultUtils.ok("获取全部业务平均办理时长成功", dataList);
    }

    /**
     * 获取昨日今日同时段数据对比
     *
     * @param serviceKey
     * @param deptId
     * @return
     */
    @Override
    public Map<String, Object> getHandingCountByDay(String handleServiceKey, Integer deptId) {
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心 显示对应审批中心下的热门业务
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                // 今日数据
                List<StatistBasics> toDayDataList = getTodayDataListByCheck(handleServiceKey, deptId);
                // 昨日数据
                List<StatistBasics> yesterDayDataList = getYesterDayDataListByCheck(handleServiceKey, deptId);
                Map<String, Object> dataMap = new HashMap<>(2);
                dataMap.put("toDay", toDayDataList);
                dataMap.put("yesterDay", yesterDayDataList);
                return ResultUtils.ok("获取审批中心昨日今日业务量成功", dataMap);
            }
            //统筹中心 显示统筹中心以及其审批中心下的业务
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                //今日数据
                List<StatistBasics> toDayDataList = getTodayDataListByAdmin(deptId, serviceKeyList);
                //昨日数据
                List<StatistBasics> yesterDayDataList = getYesterDayDataListByAdmin(deptId, serviceKeyList);
                Map<String, Object> dataMap = new HashMap<>(2);
                dataMap.put("toDay", toDayDataList);
                dataMap.put("yesterDay", yesterDayDataList);
                return ResultUtils.ok("获取统筹中心昨日今日业务量成功", dataMap);
            }
        }
        //查询全部中心
        //今日数据
        List<StatistBasics> toDayDataList = getTodayDataList(deptId);
        //昨日数据
        List<StatistBasics> yesterDayDataList = getYesterDayDataList(deptId);
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("toDay", toDayDataList);
        dataMap.put("yesterDay", yesterDayDataList);
        return ResultUtils.ok("获取全部中心昨日今日业务量成功", dataMap);
    }

    /**
     * 本周以及上周同期业务量对比
     */
    @Override
    public Map<String, Object> getHandingCountByWeek(String handleServiceKey, Integer deptId) {
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心 显示对应审批中心下的热门业务
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                //本周数据
                List<StatistBasics> thisWeekHandlingCountByCheck = statistDao.getThisWeekHandlingCountByCheck(handleServiceKey, deptId);
                //上周数据
                List<StatistBasics> lastWeekHandlingCountByCheck = statistDao.getLastWeekHandlingCountByCheck(handleServiceKey, deptId);
                Map<String, Object> dataMap = new HashMap<>(2);
                dataMap.put("thisWeek", thisWeekHandlingCountByCheck);
                dataMap.put("lastWeek", lastWeekHandlingCountByCheck);
                return ResultUtils.ok("获取审批中心本周上周业务量成功", dataMap);
            }
            //统筹中心 显示统筹中心以及其审批中心下的业务
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                //本周数据
                List<StatistBasics> thisWeekHandlingCountByAdmin = statistDao.getThisWeekHandlingCountByAdmin(serviceKeyList, deptId);
                //上周数据
                List<StatistBasics> lastWeekHandlingCountByAdmin = statistDao.getLastWeekHandlingCountByAdmin(serviceKeyList, deptId);
                Map<String, Object> dataMap = new HashMap<>(2);
                dataMap.put("thisWeek", thisWeekHandlingCountByAdmin);
                dataMap.put("lastWeek", lastWeekHandlingCountByAdmin);
                return ResultUtils.ok("获取统筹中心本周上周业务量成功", dataMap);
            }
        }
        List<StatistBasics> thisWeekHandlingCount = statistDao.getThisWeekHandlingCount(deptId);
        List<StatistBasics> lastWeekHandlingCount = statistDao.getLastWeekHandlingCount(deptId);
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("thisWeek", thisWeekHandlingCount);
        dataMap.put("lastWeek", lastWeekHandlingCount);
        return ResultUtils.ok("获取所有中心本周上周业务量成功", dataMap);
    }

    /**
     * 获取关键指标信息
     */
    @Override
    public Map<String, Object> getKeyIndicatorInformation(String handleServiceKey, Integer deptId) {
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                //查询服务点位
                List<String> serviceKeyList = serviceCenterDao.getServiceKeysByParentServiceKey(handleServiceKey);
                //查询业务支持
                Integer businessTypeCount = businessTypeDao.getBusinessTypeDetailCountByCheck(handleServiceKey, deptId);
                //查询业务办理总数
                Integer totalAmountHandled = businessInfoDao.getTotalAmountHandledByCheck(handleServiceKey, deptId);
                //查询服务人数
                Integer peopleCountByCheck = statistDao.getPeopleCountByCheck(handleServiceKey, deptId);
                Map<String, Object> dataMap = getDataMap(totalAmountHandled, serviceKeyList.size(), businessTypeCount, peopleCountByCheck);
                return ResultUtils.ok("获取审批中心关键指标信息成功", dataMap);
            }
            //统筹中心
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                //获取统筹中心以及其下所有审批中心key集合
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                //查询服务点位
                List<String> serviceList = serviceCenterDao.getServiceKeyListByWholeParentKey(handleServiceKey);
                //查询业务支持
                Integer businessTypeCount = businessTypeDao.getBusinessTypeDetailCountByAdmin(serviceKeyList, deptId);
                //查询业务办理总数
                Integer totalAmountHandled = businessInfoDao.getTotalAmountHandledByAdmin(serviceKeyList, deptId);
                //查询服务人数
                Integer peopleCountByAdmin = statistDao.getPeopleCountByAdmin(serviceKeyList, deptId);
                Map<String, Object> dataMap = getDataMap(totalAmountHandled, serviceList.size(), businessTypeCount, peopleCountByAdmin);
                return ResultUtils.ok("获取统筹中心关键指标信息成功", dataMap);
            }
        }
        //查询服务点位
        ServiceCenterBean serviceCenterBean = new ServiceCenterBean();
        serviceCenterBean.setType(CenterTypeEnum.SERVER.getType());
        serviceCenterBean.setState(StateEnum.Invalid.getValue());
        List<ServiceCenterBean> serviceCenterByCondition = serviceCenterDao.getServiceCenterByCondition(serviceCenterBean);
        //查询业务支持
        Integer businessTypeCount = businessTypeDao.getBusinessTypeDetailCount(deptId);
        //查询业务办理总数
        Integer totalAmountHandled = businessInfoDao.getTotalAmountHandled(deptId);
        //查询服务人数
        Integer peopleCount = statistDao.getPeopleCount(deptId);
        Map<String, Object> dataMap = getDataMap(totalAmountHandled, serviceCenterByCondition.size(), businessTypeCount, peopleCount);
        return ResultUtils.ok("获取所有中心关键指标信息成功", dataMap);

    }

    /**
     * 实时业务监控
     *
     * @param serviceKey
     * @param deptId
     * @return
     */
    @Override
    public Map<String, Object> getRealtimeMonitoringData(String handleServiceKey, Integer deptId) {
        Map<String, Object> dataMap = new HashMap<>();
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                //正在办理的人数
                Integer realtimeMonitoringDataByCheck = statistDao.getRealtimeMonitoringDataByCheck(handleServiceKey, deptId);
                //等待办理人数
                Integer realtimeMonitoringLineUpDataByCheck = statistDao.getRealtimeMonitoringLineUpDataByCheck(handleServiceKey, deptId);
                dataMap.put("handling", realtimeMonitoringDataByCheck);
                dataMap.put("wait", realtimeMonitoringLineUpDataByCheck);
                return ResultUtils.ok("审批中心获取数据成功", dataMap);
            }
            //统筹中心
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                List<String> serviceKeyList = getServiceKeyList(handleServiceKey);
                //正在办理
                Integer realtimeMonitoringDataByAdmin = statistDao.getRealtimeMonitoringDataByAdmin(serviceKeyList, deptId);
                //等待办理
                Integer realtimeMonitoringLineUpDataByAdmin = statistDao.getRealtimeMonitoringLineUpDataByAdmin(serviceKeyList, deptId);
                dataMap.put("handling", realtimeMonitoringDataByAdmin);
                dataMap.put("wait", realtimeMonitoringLineUpDataByAdmin);
                return ResultUtils.ok("统筹中心获取数据成功", dataMap);
            }
        }
        Integer realtimeMonitoringData = statistDao.getRealtimeMonitoringData(deptId);
        Integer realtimeMonitoringLineUpData = statistDao.getRealtimeMonitoringLineUpData(deptId);
        dataMap.put("handling", realtimeMonitoringData);
        dataMap.put("wait", realtimeMonitoringLineUpData);
        return ResultUtils.ok("全部中心获取数据成功", dataMap);
    }

    /**
     * 办理业务量统计
     *
     * @param serviceKey
     * @param deptId
     * @return
     */
    @Override
    public Map<String, Object> getDealedCount(String handleServiceKey, Integer deptId) {
        List<String> serviceKeyList = null;
        if (handleServiceKey != null) {
            //根据serviceKey查询中心
            ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(handleServiceKey);
            //审批中心
            if (serviceCenter != null && CenterTypeEnum.CHECK.getType().equals(serviceCenter.getType())) {
                serviceKeyList = new ArrayList<>();
                serviceKeyList.add(handleServiceKey);
                Map<String, Object> dataMap = getDealedCountMap(deptId, serviceKeyList);
                return ResultUtils.ok("审批中心获取数据成功", dataMap);
            }
            //统筹中心
            if (serviceCenter != null && CenterTypeEnum.ADMIN.getType().equals(serviceCenter.getType())) {
                List<String> serviceKeyListAll = getServiceKeyList(handleServiceKey);
                Map<String, Object> dealedCountMap = getDealedCountMap(deptId, serviceKeyListAll);
                return ResultUtils.ok("统筹中心获取数据成功", dealedCountMap);
            }
        }
        Map<String, Object> dealedCountMap = getDealedCountMap(deptId, serviceKeyList);
        return ResultUtils.ok("全部中心获取数据成功", dealedCountMap);
    }

    private Map<String, Object> getDealedCountMap(Integer deptId, List<String> serviceKeyList) {
        //今日办理业务量统计
        Integer todayCount = statistDao.getTodayCount(serviceKeyList, deptId);
        //前7日平均
        Integer sevenDayCount = statistDao.getSevenDayAverageCount(serviceKeyList, deptId);
        Integer averageSevenCount = (int) Math.round(sevenDayCount / 7.0);
        //本月
        Integer thisMonthCount = statistDao.getThisMonthCount(serviceKeyList, deptId);
        //昨日
        Integer yesterDayCount = statistDao.getYesterDayCount(serviceKeyList, deptId);
        //前30日平均
        Integer thirtyDayCount = statistDao.getThirtyDayCount(serviceKeyList, deptId);
        Integer averageThirtyCount = (int) Math.round(thirtyDayCount / 30.0);
        //上月
        Integer lastMonthCount = statistDao.getLastMonthCount(serviceKeyList, deptId);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("today", todayCount);
        dataMap.put("averageSeven", averageSevenCount);
        dataMap.put("thisMonth", thisMonthCount);
        dataMap.put("yesterday", yesterDayCount);
        dataMap.put("averageThirty", averageThirtyCount);
        dataMap.put("lastMonth", lastMonthCount);
        return dataMap;
    }

    private Map<String, Object> getDataMap(Integer totalAmountHandledCount, Integer servicePointCount, Integer businessTypeCount, Integer peopleCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("servicePointCount", servicePointCount);
        map.put("businessTypeCount", businessTypeCount);
        map.put("peopleCount", peopleCount);
        map.put("totalAmountHandledCount", totalAmountHandledCount);
        return map;
    }


    private List<StatistBasics> getHandlingTimeStatisticsDataList(Integer deptId) {
        List<StatistBasicsForCount> handlingTimeStatistics = statistDao.getHandlingTimeStatistics(deptId);
        List<StatistBasics> dataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() < 9 && handlingTimeStatistics.get(i).getName() != 0) {
                dataCount += handlingTimeStatistics.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNine = new StatistBasics("<9", dataCount);
        dataList.add(lessThanNine);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() > 16 || handlingTimeStatistics.get(i).getName() == 0) {
                dataCount += handlingTimeStatistics.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtenn = new StatistBasics(">17", dataCount);
        dataList.add(maoreThanSixtenn);
        //其他数据
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() >= 9 && handlingTimeStatistics.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + handlingTimeStatistics.get(i).getName() + "-" + (handlingTimeStatistics.get(i).getName() + 1));
                otherStatistBasics.setValue(handlingTimeStatistics.get(i).getValue());
                dataList.add(otherStatistBasics);
            }
        }
        return dataList;
    }

    private List<StatistBasics> getHandlingTimeStatisticsDataListByAdmin(Integer deptId, List<String> serviceKeyList) {
        List<StatistBasicsForCount> handlingTimeStatistics = statistDao.getHandlingTimeStatisticsByAdmin(serviceKeyList, deptId);
        List<StatistBasics> dataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() < 9 && handlingTimeStatistics.get(i).getName() != 0) {
                dataCount += handlingTimeStatistics.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNine = new StatistBasics("<9", dataCount);
        dataList.add(lessThanNine);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() > 16 || handlingTimeStatistics.get(i).getName() == 0) {
                dataCount += handlingTimeStatistics.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtenn = new StatistBasics(">17", dataCount);
        dataList.add(maoreThanSixtenn);
        //其他数据
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() >= 9 && handlingTimeStatistics.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + handlingTimeStatistics.get(i).getName() + "-" + (handlingTimeStatistics.get(i).getName() + 1));
                otherStatistBasics.setValue(handlingTimeStatistics.get(i).getValue());
                dataList.add(otherStatistBasics);
            }
        }
        return dataList;
    }

    private List<StatistBasics> getHandlingTimeStatisticsDataListByCheck(String handleServiceKey, Integer deptId) {
        List<StatistBasicsForCount> handlingTimeStatistics = statistDao.getHandlingTimeStatisticsByCheck(handleServiceKey, deptId);
        List<StatistBasics> dataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() < 9 && handlingTimeStatistics.get(i).getName() != 0) {
                dataCount += handlingTimeStatistics.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNine = new StatistBasics("<9", dataCount);
        dataList.add(lessThanNine);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() > 16 || handlingTimeStatistics.get(i).getName() == 0) {
                dataCount += handlingTimeStatistics.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtenn = new StatistBasics(">17", dataCount);
        dataList.add(maoreThanSixtenn);
        //其他数据
        for (int i = 0; i < handlingTimeStatistics.size(); i++) {
            if (handlingTimeStatistics.get(i).getName() >= 9 && handlingTimeStatistics.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + handlingTimeStatistics.get(i).getName() + "-" + (handlingTimeStatistics.get(i).getName() + 1));
                otherStatistBasics.setValue(handlingTimeStatistics.get(i).getValue());
                dataList.add(otherStatistBasics);
            }
        }
        return dataList;
    }

    private List<StatistBasics> getAverageHandlingTimeDataList(HandlingTimeCountBean averageHandlingTime) {
        List<StatistBasics> dataList = new ArrayList<>();
        StatistBasics zeroToFive = new StatistBasics("0-5", averageHandlingTime.getZeroToFive());
        StatistBasics fiveToTen = new StatistBasics("5-10", averageHandlingTime.getFiveToTen());
        StatistBasics tenToFifteen = new StatistBasics("10-15", averageHandlingTime.getTenToFifteen());
        StatistBasics fifTeenToTwenty = new StatistBasics("15-20", averageHandlingTime.getFifTeenToTwenty());
        StatistBasics twentyToTwentyFive = new StatistBasics("20-25", averageHandlingTime.getTwentyToTwentyFive());
        StatistBasics twentyFiveToThirty = new StatistBasics("25-30", averageHandlingTime.getTwentyFiveToThirty());
        StatistBasics moreThanThirty = new StatistBasics(">30", averageHandlingTime.getMoreThanThirty());
        dataList.add(zeroToFive);
        dataList.add(fiveToTen);
        dataList.add(tenToFifteen);
        dataList.add(fifTeenToTwenty);
        dataList.add(twentyToTwentyFive);
        dataList.add(twentyFiveToThirty);
        dataList.add(moreThanThirty);
        return dataList;
    }

    private List<StatistBasics> getYesterDayDataList(Integer deptId) {
        List<StatistBasicsForCount> yesterDayHandingCount = statistDao.getYesterDayHandingCount(deptId);
        List<StatistBasics> yesterDayDataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < yesterDayHandingCount.size(); i++) {
            if (yesterDayHandingCount.get(i).getName() < 9 && yesterDayHandingCount.get(i).getName() != 0) {
                dataCount += yesterDayHandingCount.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNineYesterDay = new StatistBasics("<9", dataCount);
        yesterDayDataList.add(lessThanNineYesterDay);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < yesterDayHandingCount.size(); i++) {
            if (yesterDayHandingCount.get(i).getName() > 16 || yesterDayHandingCount.get(i).getName() == 0) {
                dataCount += yesterDayHandingCount.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtennYesterDay = new StatistBasics(">17", dataCount);
        yesterDayDataList.add(maoreThanSixtennYesterDay);
        //其他数据
        for (int i = 0; i < yesterDayHandingCount.size(); i++) {
            if (yesterDayHandingCount.get(i).getName() >= 9 && yesterDayHandingCount.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + yesterDayHandingCount.get(i).getName() + "-" + (yesterDayHandingCount.get(i).getName() + 1));
                otherStatistBasics.setValue(yesterDayHandingCount.get(i).getValue());
                yesterDayDataList.add(otherStatistBasics);
            }
        }
        return yesterDayDataList;
    }

    private List<StatistBasics> getTodayDataList(Integer deptId) {
        List<StatistBasicsForCount> toDayHandingCount = statistDao.getToDayHandingCount(deptId);
        List<StatistBasics> toDayDataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < toDayHandingCount.size(); i++) {
            if (toDayHandingCount.get(i).getName() < 9 && toDayHandingCount.get(i).getName() != 0) {
                dataCount += toDayHandingCount.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNine = new StatistBasics("<9", dataCount);
        toDayDataList.add(lessThanNine);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < toDayHandingCount.size(); i++) {
            if (toDayHandingCount.get(i).getName() > 16 || toDayHandingCount.get(i).getName() == 0) {
                dataCount += toDayHandingCount.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtenn = new StatistBasics(">17", dataCount);
        toDayDataList.add(maoreThanSixtenn);
        //其他数据
        for (int i = 0; i < toDayHandingCount.size(); i++) {
            if (toDayHandingCount.get(i).getName() >= 9 && toDayHandingCount.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + toDayHandingCount.get(i).getName() + "-" + (toDayHandingCount.get(i).getName() + 1));
                otherStatistBasics.setValue(toDayHandingCount.get(i).getValue());
                toDayDataList.add(otherStatistBasics);
            }
        }
        return toDayDataList;
    }

    private List<String> getServiceKeyList(String handleServiceKey) {
        //最终查询的中心key集合
        List<String> serviceKeyList = new ArrayList<>();
        //获取该统筹中心下所有的审批中心key值
        List<String> serviceKeysByParentServiceKey = serviceCenterDao.getServiceKeysByParentServiceKey(handleServiceKey);
        //集合中添加统筹的key值
        if (serviceKeysByParentServiceKey != null) {
            serviceKeyList.addAll(serviceKeysByParentServiceKey);
        }
        serviceKeyList.add(handleServiceKey);
        return serviceKeyList;
    }

    private List<StatistBasics> getYesterDayDataListByAdmin(Integer deptId, List<String> serviceKeyList) {
        List<StatistBasicsForCount> yesterDayHandingCountByAdmin = statistDao.getYesterDayHandingCountByAdmin(serviceKeyList, deptId);
        List<StatistBasics> yesterDayDataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < yesterDayHandingCountByAdmin.size(); i++) {
            if (yesterDayHandingCountByAdmin.get(i).getName() < 9 && yesterDayHandingCountByAdmin.get(i).getName() != 0) {
                dataCount += yesterDayHandingCountByAdmin.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNineYesterDay = new StatistBasics("<9", dataCount);
        yesterDayDataList.add(lessThanNineYesterDay);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < yesterDayHandingCountByAdmin.size(); i++) {
            if (yesterDayHandingCountByAdmin.get(i).getName() > 16 || yesterDayHandingCountByAdmin.get(i).getName() == 0) {
                dataCount += yesterDayHandingCountByAdmin.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtennYesterDay = new StatistBasics(">17", dataCount);
        yesterDayDataList.add(maoreThanSixtennYesterDay);
        //其他数据
        for (int i = 0; i < yesterDayHandingCountByAdmin.size(); i++) {
            if (yesterDayHandingCountByAdmin.get(i).getName() >= 9 && yesterDayHandingCountByAdmin.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + yesterDayHandingCountByAdmin.get(i).getName() + "-" + (yesterDayHandingCountByAdmin.get(i).getName() + 1));
                otherStatistBasics.setValue(yesterDayHandingCountByAdmin.get(i).getValue());
                yesterDayDataList.add(otherStatistBasics);
            }
        }
        return yesterDayDataList;
    }

    private List<StatistBasics> getTodayDataListByAdmin(Integer deptId, List<String> serviceKeyList) {
        List<StatistBasicsForCount> toDayHandingCountByAdmin = statistDao.getToDayHandingCountByAdmin(serviceKeyList, deptId);
        List<StatistBasics> toDayDataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < toDayHandingCountByAdmin.size(); i++) {
            if (toDayHandingCountByAdmin.get(i).getName() < 9 && toDayHandingCountByAdmin.get(i).getName() != 0) {
                dataCount += toDayHandingCountByAdmin.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNine = new StatistBasics("<9", dataCount);
        toDayDataList.add(lessThanNine);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < toDayHandingCountByAdmin.size(); i++) {
            if (toDayHandingCountByAdmin.get(i).getName() > 16 || toDayHandingCountByAdmin.get(i).getName() == 0) {
                dataCount += toDayHandingCountByAdmin.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtenn = new StatistBasics(">17", dataCount);
        toDayDataList.add(maoreThanSixtenn);
        //其他数据
        for (int i = 0; i < toDayHandingCountByAdmin.size(); i++) {
            if (toDayHandingCountByAdmin.get(i).getName() >= 9 && toDayHandingCountByAdmin.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + toDayHandingCountByAdmin.get(i).getName() + "-" + (toDayHandingCountByAdmin.get(i).getName() + 1));
                otherStatistBasics.setValue(toDayHandingCountByAdmin.get(i).getValue());
                toDayDataList.add(otherStatistBasics);
            }
        }
        return toDayDataList;
    }

    private List<StatistBasics> getYesterDayDataListByCheck(String handleServiceKey, Integer deptId) {
        List<StatistBasicsForCount> yesterDayHandingCountByCheck = statistDao.getYesterDayHandingCountByCheck(handleServiceKey, deptId);
        List<StatistBasics> yesterDayDataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < yesterDayHandingCountByCheck.size(); i++) {
            if (yesterDayHandingCountByCheck.get(i).getName() < 9 && yesterDayHandingCountByCheck.get(i).getName() != 0) {
                dataCount += yesterDayHandingCountByCheck.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNineYesterDay = new StatistBasics("<9", dataCount);
        yesterDayDataList.add(lessThanNineYesterDay);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < yesterDayHandingCountByCheck.size(); i++) {
            if (yesterDayHandingCountByCheck.get(i).getName() > 16 || yesterDayHandingCountByCheck.get(i).getName() == 0) {
                dataCount += yesterDayHandingCountByCheck.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtennYesterDay = new StatistBasics(">17", dataCount);
        yesterDayDataList.add(maoreThanSixtennYesterDay);
        //其他数据
        for (int i = 0; i < yesterDayHandingCountByCheck.size(); i++) {
            if (yesterDayHandingCountByCheck.get(i).getName() >= 9 && yesterDayHandingCountByCheck.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + yesterDayHandingCountByCheck.get(i).getName() + "-" + (yesterDayHandingCountByCheck.get(i).getName() + 1));
                otherStatistBasics.setValue(yesterDayHandingCountByCheck.get(i).getValue());
                yesterDayDataList.add(otherStatistBasics);
            }
        }
        return yesterDayDataList;
    }

    private List<StatistBasics> getTodayDataListByCheck(String handleServiceKey, Integer deptId) {
        List<StatistBasicsForCount> toDayHandingCountByCheck = statistDao.getToDayHandingCountByCheck(handleServiceKey, deptId);
        List<StatistBasics> toDayDataList = new ArrayList<>();
        Integer dataCount = 0;
        for (int i = 0; i < toDayHandingCountByCheck.size(); i++) {
            if (toDayHandingCountByCheck.get(i).getName() < 9 && toDayHandingCountByCheck.get(i).getName() != 0) {
                dataCount += toDayHandingCountByCheck.get(i).getValue();
            }
        }
        //小于9数据
        StatistBasics lessThanNine = new StatistBasics("<9", dataCount);
        toDayDataList.add(lessThanNine);
        //归0
        dataCount = 0;
        //大于17数据
        for (int i = 0; i < toDayHandingCountByCheck.size(); i++) {
            if (toDayHandingCountByCheck.get(i).getName() > 16 || toDayHandingCountByCheck.get(i).getName() == 0) {
                dataCount += toDayHandingCountByCheck.get(i).getValue();
            }
        }
        StatistBasics maoreThanSixtenn = new StatistBasics(">17", dataCount);
        toDayDataList.add(maoreThanSixtenn);
        //其他数据
        for (int i = 0; i < toDayHandingCountByCheck.size(); i++) {
            if (toDayHandingCountByCheck.get(i).getName() >= 9 && toDayHandingCountByCheck.get(i).getName() <= 16) {
                StatistBasics otherStatistBasics = new StatistBasics();
                otherStatistBasics.setName("" + toDayHandingCountByCheck.get(i).getName() + "-" + (toDayHandingCountByCheck.get(i).getName() + 1));
                otherStatistBasics.setValue(toDayHandingCountByCheck.get(i).getValue());
                toDayDataList.add(otherStatistBasics);
            }
        }
        return toDayDataList;
    }

  @Override
  public Map<String, Object> getAreaRanking(String serviceKey, Integer deptId) {
     SysUserBean user = ShiroUtils.getUserEntity();
     if (StringUtil.isEmpty(serviceKey)) {
        serviceKey = user.getServiceKey();
     }
     if(deptId == null){
        deptId = user.getDeptId();
     }
    List<String> serviceKeyList = null;
      List<StatistBasics> basicsList = new ArrayList<>();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    //admin或者统筹中心管理员
    if (serviceCenter == null || ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == (serviceCenter.getType().intValue())) {
      if (serviceCenter != null) {
          serviceKeyList = serviceCenterDao.getServiceListByParentKey(serviceKey);
          serviceKeyList.add(serviceKey);
      }
      basicsList = statistDao.getAreaRanking(serviceKeyList, deptId);
    } else if (ServiceCenterTypeEnum.CHECK.getType().intValue()
            == (serviceCenter.getType().intValue())) {
      //审批中心管理员
      serviceKeyList = serviceCenterDao.getServiceListByParentKey(serviceKey);
        if (!CollectionUtils.isEmpty(serviceKeyList)) {
            basicsList = statistDao.getAreaRankingOfServer(deptId, serviceKeyList);
        }
    }
    setStatistBasicsName(basicsList);
    return ResultUtils.ok("获取区域排行top5数据成功", basicsList);
  }


  @Override
  public Map<String, Object> getDeptBusinessProportion(String serviceKey, Integer deptId) {
    SysUserBean user = ShiroUtils.getUserEntity();
    if (deptId == null) {
      deptId = user.getDeptId();
    }
    if (serviceKey == null) {
      serviceKey = user.getServiceKey();
    }
    Map<String, Object> map = new HashMap<>();
    List<SysDeptBean> deptList = null;
    List<String> serviceIds = null;
      List<StatistBasics> list = new ArrayList<>();
    if (deptId == 0) {
      //查询所有的部门列表
      deptList = sysDeptBeanDao.getDeptList(new SysDeptVo());
    }
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenter != null && ServiceCenterTypeEnum.CHECK.getType().intValue()
            == serviceCenter.getType().intValue()) {
      //审批中心
      serviceIds = serviceCenterDao.getServiceKeysByParentServiceKey(serviceKey);
    } else if (serviceCenter != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == serviceCenter.getType().intValue()) {
      //统筹中心
      serviceIds = serviceCenterDao.getServiceKeyListByWholeParentKey(serviceKey);
    }
      if (serviceCenter != null && CollectionUtils.isEmpty(serviceIds)) {
          return ResultUtils.ok("获取业务量统计数据为空", list);
      }
    if (!CollectionUtils.isEmpty(deptList) && deptList.size() > 1) {
      List<Integer> deptIdList = deptList.stream().map(
              SysDeptBean::getId).collect(Collectors.toList());
      //走部门统计的逻辑
      list = statistDao.getDeptBusinessProportion(deptIdList, serviceIds);
    } else {
      //走业务类别统计的逻辑
      list = statistDao.getBusinessTypeProportion(deptId, serviceIds);
    }
    return ResultUtils.ok("获取业务量统计数据成功", list);
  }

  @Override
  public Map<String, Object> getServiceCenterLal(String serviceKey, Integer deptId) {
    if (serviceKey == null) {
      SysUserBean user = ShiroUtils.getUserEntity();
      serviceKey = user.getServiceKey();
    }
    List<ServiceCenterBean> list = null;
    //用户为admin时，serviceKey仍然为null
    if (!StringUtil.isEmpty(serviceKey)) {
      ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
      list = Collections.singletonList(serviceCenter);
    } else {
      ServiceCenterBean bean = new ServiceCenterBean();
      bean.setType((long) ServiceCenterTypeEnum.ADMIN.getType());
      bean.setState(1);
        list = serviceCenterDao.getServiceCenterListByCondition(bean);
    }
    //获取中心下面的服务点位
    getServiceLal(list);
    if (!CollectionUtils.isEmpty(list)) {
      for (ServiceCenterBean bean : list) {
        areaService.getAreaName(bean);
      }
    }
    return ResultUtils.ok("获取服务中心点位数据成功", list);
  }

  @Override
  public Map<String, Object> getLalInformation(String lal) {
    if (!StringUtil.isEmpty(lal)) {
      ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenterByLal(lal);
      ServiceCenterDetailBean detail = new ServiceCenterDetailBean();
      if (serviceCenter != null) {
        detail.setName(serviceCenter.getServiceName());
        //正在受理业务名称
        List<BusinessInfo> processingBusiness = businessInfoDao.getBusinessListByCondition(serviceCenter.getServiceKey(),
                BusinessInfoStateEnum.Processing.getValue());
        if (!CollectionUtils.isEmpty(processingBusiness)) {
          detail.setBusinessName(processingBusiness.get(0).getBusinessTypeName());
        }
        //等待受理的人数
        List<BusinessInfo> untreatedBusiness = businessInfoDao.getBusinessListByCondition(serviceCenter.getServiceKey(),
                BusinessInfoStateEnum.Untreated.getValue());
        detail.setWaitNumber(CollectionUtils.isEmpty(untreatedBusiness) ? 0 : untreatedBusiness.size());
        //今天办理业务
        //累计办理业务
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setState(BusinessInfoStateEnum.Processed.getValue());
        businessInfo.setServiceKey(serviceCenter.getServiceKey());
        detail.setTotalBusinessNumber(businessInfoDao.selectByObject(businessInfo).size());
        businessInfo.setStartTime(DateUtil.getDateInMinutesAndSecondsZero(new Date()));
        businessInfo.setEndTime(DateUtil.getDateInMinutesAndSecondsMax(new Date()));
        detail.setTodayBusinessNumber(businessInfoDao.selectByObject(businessInfo).size());
        return ResultUtils.ok("获取服务中心详情数据成功", detail);
      }
    }
    return ResultUtils.error("获取服务中心详情数据为空");
  }

  /**
   * 获取服务中心的点位
   *
   * @param list
   */
  private void getServiceLal(List<ServiceCenterBean> list) {
    if (!CollectionUtils.isEmpty(list)) {
      for (ServiceCenterBean serviceCenterBean : list) {
        if (ServiceCenterTypeEnum.ADMIN.getType().equals(
                (Integer) serviceCenterBean.getType().intValue())) {
          List<ServiceCenterBean> serviceList = serviceCenterDao.getServiceCenterKeyByParentKey(serviceCenterBean.getServiceKey());
          serviceList = serviceCenterService.getServiceCenterStatus(serviceList);
          serviceCenterBean.setChildren(serviceList);
        } else if (ServiceCenterTypeEnum.CHECK.getType().equals(
                (Integer) serviceCenterBean.getType().intValue())) {
            //若选择审批中心，地图仍展示其所属的统筹中心的所属区域范围
            ServiceCenterBean adminServiceCenter = serviceCenterDao.getServiceCenter(serviceCenterBean.getParentKey());
            serviceCenterBean.setProvince(adminServiceCenter.getProvince());
            serviceCenterBean.setCity(adminServiceCenter.getCity());
            serviceCenterBean.setArea(adminServiceCenter.getArea());
            serviceCenterBean.setStreet(adminServiceCenter.getStreet());
            serviceCenterBean.setCommunity(adminServiceCenter.getCommunity());
            List<ServiceCenterBean> serviceList = serviceCenterDao.getServiceCenterByParentKey(serviceCenterBean.getServiceKey());
          //获取点位的状态
          serviceList = serviceCenterService.getServiceCenterStatus(serviceList);
          serviceCenterBean.setChildren(serviceList);
        }
      }
    }
  }

  /**
   * 将原来的name为serviceKey转化成area的name
   */
  private void setStatistBasicsName(List<StatistBasics> list) {
    if (!CollectionUtils.isEmpty(list)) {
      ArrayList<StatistBasics> removeList = new ArrayList<>();
      for (StatistBasics statistBasics : list) {
        String name = statistBasics.getName();
        String areaId = null;
        ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(name);
        if (serviceCenter != null) {
          if (!StringUtil.isEmpty(serviceCenter.getProvince())) {
            areaId = serviceCenter.getProvince();
          }
          if (!StringUtil.isEmpty(serviceCenter.getCity())) {
            areaId = serviceCenter.getCity();
          }
          if (!StringUtil.isEmpty(serviceCenter.getArea())) {
            areaId = serviceCenter.getArea();
          }
          if (!StringUtil.isEmpty(serviceCenter.getStreet())) {
            areaId = serviceCenter.getStreet();
          }
          if (!StringUtil.isEmpty(serviceCenter.getCommunity())) {
            areaId = serviceCenter.getCommunity();
          }
        }
          if (!StringUtil.isEmpty(areaId)) {
          statistBasics.setName(areaDao.getAreaName(
                  Collections.singletonList(areaId)).get(0).getName());
        } else {
          removeList.add(statistBasics);
        }
      }
      list.removeAll(removeList);
    }
  }

  @Override
  public Map<String, Object> getAgeDistribution(String serviceKey, Integer deptId) {
    SysUserBean user = ShiroUtils.getUserEntity();
    if (deptId == null) {
      deptId = user.getDeptId();
    }
    if (serviceKey == null) {
      serviceKey = user.getServiceKey();
    }
    StatisticsVo statisticsVo = new StatisticsVo();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenter != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == serviceCenter.getType().intValue()) {
        statisticsVo.setServiceKey(serviceCenterDao.getServiceKeysByParentKey(serviceKey));
    }
      if (serviceCenter != null && ServiceCenterTypeEnum.CHECK.getType().intValue()
              == serviceCenter.getType().intValue()) {
          statisticsVo.setServiceKey(serviceCenterDao.getServiceListByParentKey(serviceKey));
    }
      if (statisticsVo.getServiceKeys() != null && statisticsVo.getServiceKeys().size() == 0) {
          return ResultUtils.ok("获取年龄分布数据为空", getAgeList());
      }
    //年龄分布统计
    List<StatistBasics> ageDistribution = this
            .getAgeDistributionStatistics(statisticsVo, deptId);
    return ResultUtils.ok("获取年龄分布数据成功", ageDistribution);
  }

    private List<StatistBasics> getAgeList(){
        List<StatistBasics> list = new ArrayList<>();
        list.add(new StatistBasics("0-15",0));
        list.add(new StatistBasics("16-24",0));
        list.add(new StatistBasics("25-34",0));
        list.add(new StatistBasics("35-44",0));
        list.add(new StatistBasics("45-59",0));
        list.add(new StatistBasics("60+",0));
        return list;
    }

  @Override
  public Map<String, Object> getCultureDistribution(String serviceKey, Integer deptId) {
    SysUserBean user = ShiroUtils.getUserEntity();
    if (deptId == null) {
      deptId = user.getDeptId();
    }
    if (serviceKey == null) {
      serviceKey = user.getServiceKey();
    }
    StatisticsVo statisticsVo = new StatisticsVo();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
      if (serviceCenter != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
              == serviceCenter.getType().intValue()) {
          statisticsVo.setServiceKey(serviceCenterDao.getServiceKeysByParentKey(serviceKey));
      }
      if (serviceCenter != null && ServiceCenterTypeEnum.CHECK.getType().intValue()
              == serviceCenter.getType().intValue()) {
          statisticsVo.setServiceKey(serviceCenterDao.getServiceListByParentKey(serviceKey));
      }
      if (statisticsVo.getServiceKeys() != null && statisticsVo.getServiceKeys().size() == 0) {
          return ResultUtils.ok("获取文化程度数据为空", getCultureList());
      }
    //文化程度统计
    List<StatistBasics> education = this
            .getCultureStandardStatistics(statisticsVo, deptId);
    return ResultUtils.ok("获取文化程度数据成功", education);
  }

    private List<StatistBasics> getCultureList(){
        List<StatistBasics> list = new ArrayList<>();
        list.add(new StatistBasics("小学",0));
        list.add(new StatistBasics("初中",0));
        list.add(new StatistBasics("高中",0));
        list.add(new StatistBasics("专科",0));
        list.add(new StatistBasics("本科",0));
        list.add(new StatistBasics("硕士",0));
        list.add(new StatistBasics("博士",0));
        return list;
    }

  @Override
  public Map<String, Object> getHandleProportion(String serviceKey, Integer deptId) {
      Date endDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    Date startDate = new Date(endDate.getTime() - 6 * 24 * 60 * 60 * 1000);
    SysUserBean user = ShiroUtils.getUserEntity();
    if (deptId == null) {
      deptId = user.getDeptId();
    }
    if (serviceKey == null) {
      serviceKey = user.getServiceKey();
    }
      List<String> list = new ArrayList<>();
    StatisticsVo statisticsVo = new StatisticsVo();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenter != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == serviceCenter.getType().intValue()) {
        list = serviceCenterDao.getServiceKeysByParentKey(serviceKey);
        statisticsVo.setServiceKey(serviceCenterDao.getServiceKeysByParentKey(serviceKey));
    }
      if (serviceCenter != null && ServiceCenterTypeEnum.CHECK.getType().intValue()
              == serviceCenter.getType().intValue()) {
          statisticsVo.setServiceKey(serviceCenterDao.getServiceListByParentKey(serviceKey));
      }
    Map<String, Object> handleProportion = this.getHandleProportion(statisticsVo, deptId, startDate, endDate);
    return ResultUtils.ok("获取业务办结占比数据成功", handleProportion);
  }

  @Override
  public Map<String, Object> getHandleTrend(String serviceKey, Integer deptId) {
      Date endDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    Date startDate = new Date(endDate.getTime() - 14 * 24 * 60 * 60 * 1000);
    SysUserBean user = ShiroUtils.getUserEntity();
    if (deptId == null) {
      deptId = user.getDeptId();
    }
    if (serviceKey == null) {
      serviceKey = user.getServiceKey();
    }
    StatisticsVo statisticsVo = new StatisticsVo();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    if (serviceCenter != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == serviceCenter.getType().intValue()) {
        statisticsVo.setServiceKey(serviceCenterDao.getServiceKeysByParentKey(serviceKey));
    }
      if (serviceCenter != null && ServiceCenterTypeEnum.CHECK.getType().intValue()
              == serviceCenter.getType().intValue()) {
          statisticsVo.setServiceKey(serviceCenterDao.getServiceListByParentKey(serviceKey));
      }
    Map<String, Object> handleTrend = this.getHandleTheTrend(statisticsVo, deptId, startDate, endDate);
    return ResultUtils.ok("获取业务量趋势数据成功", handleTrend);
  }
}

	
