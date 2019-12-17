package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.pojo.StatisticsVo;
import java.util.Date;
import java.util.Map;

/**
 * ClassName: StatistService
 *
 * @author quboka
 * @Description: 统计
 * @date 2018年4月9日
 */
public interface StatistService {

  /**
   * @param paramsMap
   * @return Map<String   ,   Object>
   * @Description: 获取统计数据
   * @author quboka
   * @date 2018年4月9日
   */
  Map<String, Object>  getStatistData(StatisticsVo statisticsVo);

  /**
   * @param paramsMap
   * @return Map<String       ,       Object>
   * @Description: 获取办理业务分类统计
   * @author ljfan
   * @date 2018年12月12日
   */
  Map<String, Object> businessClassification(StatisticsVo statisticsVo);
  /**
   * @param paramsMap
   * @return Map<String,Object>
   * @Description: 获取办理业务详情统计
   * @author ljfan
   * @date 2018年12月12日
   */
  Map<String, Object> getBusinessDetailByBusinessTypeId(Integer businessTypeId, Date startDate, Date endDate);

  /**
   * 获取热门top5业务员
   *
   * @return
   */
  Map<String, Object> getHotBusinessTopFive(String handleServiceKey, Integer deptId);

  /**
   * 办理时段统计（近7日）
   */
  Map<String, Object> getHandlingTimeStatistics(String serviceKey, Integer deptId);

  /**
   * 业务平均办理时长
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  Map<String, Object> getAverageHandlingTime(String serviceKey, Integer deptId);

  /**
   * @param serviceKey
   * @param deptId
   * @return
   */
  Map<String, Object> getHandingCountByDay(String serviceKey, Integer deptId);

  /**
   * 本周以及上周同期业务量对比
   */
  Map<String, Object> getHandingCountByWeek(String serviceKey, Integer deptId);

    /**
     * 获取关键指标信息
     *
     * @param serviceKey
     * @param deptId
     * @return
     */
    Map<String, Object> getKeyIndicatorInformation(String serviceKey, Integer deptId);

  /**
   * 实时办理监控
   */

  Map<String, Object> getRealtimeMonitoringData(String serviceKey, Integer deptId);

  /**
   * 办理业务量统计
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  Map<String, Object> getDealedCount(String serviceKey, Integer deptId);
    /**
     * 获取热门top5业务员
     *
     * @return
     */
    Map<String, Object> getAreaRanking(String serviceKey, Integer deptId);

    /**
     * 部门业务量分类统计
     *
     * @param serviceKey
     * @param deptId
     * @return
     */
    Map<String, Object> getDeptBusinessProportion(String serviceKey, Integer deptId);

    /**
     * 获取服务点位
     *
     * @param serviceKey
     * @param deptId
     * @return
     */
    Map<String, Object> getServiceCenterLal(String serviceKey, Integer deptId);

    /**
     * 获取服务点位信息
     *
     * @param lal
     * @return
     */
    Map<String, Object> getLalInformation(String lal);

    Map<String, Object> getAgeDistribution(String serviceKey, Integer deptId);

    Map<String, Object> getCultureDistribution(String serviceKey, Integer deptId);

    Map<String, Object> getHandleProportion(String serviceKey, Integer deptId);

    Map<String, Object> getHandleTrend(String serviceKey, Integer deptId);
}
