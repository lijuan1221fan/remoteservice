package com.visionvera.remoteservice.controller;

import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.pojo.StatisticalDataVo;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.pojo.StatisticsVo;
import com.visionvera.remoteservice.service.StatistService;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;

import com.visionvera.remoteservice.util.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: StatistController
 *
 * @author quboka
 * @Description: 统计
 * @date 2018年4月9日
 */
@RestController
@RequestMapping("statist")
public class StatistController {

  private static Logger logger = LoggerFactory.getLogger(StatistController.class);

  @Resource
  private StatistService statistService;

  /**
   * @param deptId
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return Map<String   ,   Object>
   * @Description: 获取统计数据
   * @author quboka
   * @date 2018年4月9日
   */
  @PostMapping(value = "getStatistData")
  public Map<String, Object> getStatistData(@RequestBody StatisticsVo statisticsVo) {
    ValidateUtil.validate(statisticsVo, QueryGroup.class);
    return statistService.getStatistData(statisticsVo);
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
  @PostMapping(value = "businessClassification")
  public Map<String, Object> businessClassification(@RequestBody StatisticsVo statisticsVo) {
    ValidateUtil.validate(statisticsVo, QueryGroup.class);
    return statistService.businessClassification(statisticsVo);
  }
  /**
   * @param businessTypeId
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return Map<String   ,   Object>
   * @Description: 获取业务分类
   * @author ljfan
   * @date 2018年12月9日
   */
  @RequestMapping(value = "getBusinessDetailByBusinessTypeId", method = RequestMethod.POST)
  public Map<String, Object> getBusinessDetailByBusinessTypeId(
      @RequestParam(value = "businessTypeId", required = true) Integer businessTypeId,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "startDate", required = true) Date startDate,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "endDate", required = true) Date endDate) {
    return statistService.getBusinessDetailByBusinessTypeId(businessTypeId, startDate, endDate);
  }

  /**
   * 获取热门业务前5名
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  @RequestMapping(value = "getHotBusinessTopFive")
  public Map<String, Object> getHotBusinessTopFive(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getHotBusinessTopFive(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 办理时段统计（近7日）
   */
  @RequestMapping(value = "getHandlingTimeStatistics")
  public Map<String, Object> getHandlingTimeStatistics(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getHandlingTimeStatistics(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 业务平均办理时长统计（近30日）
   */
  @RequestMapping(value = "getAverageHandlingTime")
  public Map<String, Object> getAverageHandlingTime(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getAverageHandlingTime(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 昨日今日同期业务量对比
   */
  @RequestMapping(value = "getHandingCountByDay")
  public Map<String, Object> getHandingCountByDay(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getHandingCountByDay(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 本周以及上周同期业务量对比
   */
  @RequestMapping(value = "getHandingCountByWeek")
  public Map<String, Object> getHandingCountByWeek(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getHandingCountByWeek(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 关键指标信息
   */
  @RequestMapping(value = "getKeyIndicatorInformation")
  public Map<String, Object> getKeyIndicatorInformation(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getKeyIndicatorInformation(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 实时办理监控
   */
  @RequestMapping(value = "getRealtimeMonitoringData")
  public Map<String, Object> getRealtimeMonitoringData(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getRealtimeMonitoringData(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 办理业务量统计
   */
  @RequestMapping(value = "getDealedCount")
  public Map<String, Object> getDealedCount(@RequestBody StatisticalDataVo statisticalDataVo) {
      return statistService.getDealedCount(statisticalDataVo.getServiceKey(), statisticalDataVo.getDeptId());
  }

  /**
   * 区域排行
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  @RequestMapping(value = "getAreaRanking", method = RequestMethod.POST)
  public Map<String, Object> getAreaRanking(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getAreaRanking(serviceKey, deptId);
  }

  /**
   * 业务量占比分析
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  @RequestMapping(value = "getDeptBusinessProportion", method = RequestMethod.POST)
  public Map<String, Object> getDeptBusinessProportion(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getDeptBusinessProportion(serviceKey, deptId);
  }

  /**
   * 获取地图点位和高亮区域
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  @RequestMapping(value = "getServiceCenterLal", method = RequestMethod.POST)
  public Map<String, Object> getServiceCenterLal(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getServiceCenterLal(serviceKey, deptId);
  }

  /**
   * 获取点位具体信息
   *
   * @param lal
   * @return
   */
  @RequestMapping(value = "getLalInformation", method = RequestMethod.POST)
  public Map<String, Object> getLalInformation(@RequestBody ServiceCenterBean serviceCenterBean) {
    String lal = serviceCenterBean == null ? null : serviceCenterBean.getLal();
    return statistService.getLalInformation(lal);
  }


  /**
   * 年龄分布
   *
   * @param statisticsVo
   * @return
   */
  @RequestMapping(value = "getAgeDistribution", method = RequestMethod.POST)
  public Map<String, Object> getAgeDistribution(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getAgeDistribution(serviceKey, deptId);
  }

  /**
   * 文化程度
   *
   * @param statisticsVo
   * @return
   */
  @RequestMapping(value = "getCultureDistribution", method = RequestMethod.POST)
  public Map<String, Object> getCultureDistribution(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getCultureDistribution(serviceKey, deptId);
  }

  /**
   * 业务办结占比
   *
   * @param statisticsVo
   * @return
   */
  @RequestMapping(value = "getHandleProportion", method = RequestMethod.POST)
  public Map<String, Object> getHandleProportion(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getHandleProportion(serviceKey, deptId);
  }

  /**
   * 业务量趋势
   *
   * @param statisticsVo
   * @return
   */
  @RequestMapping(value = "getHandleTrend", method = RequestMethod.POST)
  public Map<String, Object> getHandleTrend(@RequestBody StatisticsVo vo) {
    String serviceKey = vo == null ? null : vo.getServiceKey();
    Integer deptId = vo == null ? null : vo.getDeptId();
    return statistService.getHandleTrend(serviceKey, deptId);
  }
}
