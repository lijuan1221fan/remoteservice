package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.HandlingTimeCountBean;
import com.visionvera.remoteservice.bean.StatistBasics;
import com.visionvera.remoteservice.bean.StatistBasicsForBusinessType;
import java.util.Date;
import java.util.List;

import com.visionvera.remoteservice.bean.StatistBasicsForCount;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: StatistDao
 *
 * @author quboka
 * @Description: 统计
 * @date 2018年4月9日
 */
public interface StatistDao {


  /**
   * @param deptId 部门id
   * @param startDate
   * @param endDate
   * @return List<StatistBasics>
   * @Description: 区域统计
   * @author quboka
   * @date 2018年4月9日
   */
  List<StatistBasics> getRangeStatisticsData(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @param state 业务状态 ： 0 未处理 1 处理中 2 已处理 3  过号
   * @return List<StatistBasics>
   * @Description: 办理趋势统计
   * @author quboka
   * @date 2018年4月9日
   */
  List<StatistBasics> getHandleTheTrend(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate, @Param("state") Integer state);

    /**
     * 业务办结占比
     *
     * @param serviceKeys
     * @param deptId
     * @param finalState
     * @return
     */
    List<StatistBasics> getHandleProportion(@Param("serviceKeys") List<String> serviceKeys, @Param("deptId") Integer deptId,
                                            @Param("finalState") Integer finalState);

  /**
   * 业务办结占比
   *
   * @param serviceKeys
   * @param deptId
   * @param finalState
   * @return
   */
  List<StatistBasics> getBusinessHandleTheTrend(@Param("serviceKeys") List<String> serviceKeys, @Param("deptId") Integer deptId,
                                          @Param("finalState") Integer finalState);

  /**
   * @param deptId
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return Map<String   ,   Object>
   * @Description: 业务分类统计
   * @
   * @author quboka
   * @date 2018年4月9日
   */
  List<StatistBasicsForBusinessType> getBusinessClassification(@Param("serviceKeys")List<String> serviceKeys,
      @Param("deptId") Integer deptId, @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * @param parentId 类型  1000：公安远程服务 2000：社保远程服务  0:全部
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @Description: 业务分类统计 按照第二类型 统计
   */
  List<StatistBasics> getBusinessClassificationBySecondTypes( @Param("serviceKeys")List<String> serviceKeys,
      @Param("businessTypeId") Integer businessTypeId, @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * @param type 类型  1000：公安远程服务 2000：社保远程服务  0:全部
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return List<StatistBasics>
   * @Description: 办理状态统计
   * @
   * @author quboka
   * @date 2018年4月9日
   */
  List<StatistBasics> getHandleTheState(@Param("serviceKeys")List<String> serviceKeys,@Param("type") String type,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * @param deptId 部门id
   * @return List<StatistBasics>
   * @Description: 年龄分布统计
   * @
   * @author quboka
   * @date 2018年4月10日
   */
  List<StatistBasics> getAgeDistributionStatistics(@Param("serviceKeys") List<String> serviceKeys,
                                                   @Param("deptId") Integer deptId);

  /**
   * @param deptId 部门id
   * @return List<StatistBasics>
   * @Description: 文化程度统计
   * @
   * @author quboka
   * @date 2018年4月10日
   */
  List<StatistBasics> getCultureStandardStatistics(@Param("serviceKeys") List<String> serviceKeys,
                                                   @Param("deptId") Integer deptId);

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return StatistBasics
   * @Description: 业务处理完成状态统计
   * @
   * @author ljfan
   * @date 2018年11月8日
   */
  StatistBasics getProcessed(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return StatistBasics
   * @Description: 业务未处理状态统计
   * @
   * @author ljfan
   * @date 2018年11月8日
   */
  StatistBasics getUntreated(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * @param deptId 部门id
   * @param startDate 开始时间
   * @param endDate 结束时间
   * @return StatistBasics
   * @Description: 业务处理中状态统计
   * @
   * @author ljfan
   * @date 2018年11月8日
   */
  StatistBasics getProcessing(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  /**
   * 时间段统计
   *
   * @param deptId
   * @param startDate
   * @param endDate
   * @return
   */
  List<StatistBasics> getBusinessTimeStatistics(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate, @Param("endDate") Date endDate);

  /**
   * 办理业务统计
   *
   * @param deptId
   * @param startDate
   * @param endDate
   * @return
   */
  List<StatistBasics> businessClassification(@Param("serviceKeys")List<String> serviceKeys,@Param("deptId") Integer deptId,
      @Param("startDate") Date startDate, @Param("endDate") Date endDate);

  /**
   * 获取top5热门业务
   *
   * @param serviceKey
   * @param deptId
   * @return
   */
  List<StatistBasics> getHotBusinessTopFiveByCheck(@Param("serviceKey") String serviceKey, @Param("deptId") Integer deptId);

  List<StatistBasics> getHotBusinessTopFiveByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  List<StatistBasics> getHotBusinessTopFive(@Param("deptId") Integer deptId);

  /**
   * 近七天办理时段业务量统计
   *
   * @param handleServiceKey
   * @param deptId
   * @return
   */
  List<StatistBasicsForCount> getHandlingTimeStatisticsByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  List<StatistBasicsForCount> getHandlingTimeStatisticsByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  List<StatistBasicsForCount> getHandlingTimeStatistics(@Param("deptId") Integer deptId);

  /**
   * 近30天业务平均办理时长
   */
  HandlingTimeCountBean getAverageHandlingTimeByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  HandlingTimeCountBean getAverageHandlingTimeByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  HandlingTimeCountBean getAverageHandlingTime(@Param("deptId") Integer deptId);


  /**
   * 今日业务量分时段统计
   */
  List<StatistBasicsForCount> getToDayHandingCountByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  List<StatistBasicsForCount> getToDayHandingCountByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  List<StatistBasicsForCount> getToDayHandingCount(@Param("deptId") Integer deptId);

  /**
   * 昨日业务量分时段统计
   */
  List<StatistBasicsForCount> getYesterDayHandingCountByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  List<StatistBasicsForCount> getYesterDayHandingCountByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  List<StatistBasicsForCount> getYesterDayHandingCount(@Param("deptId") Integer deptId);


  /**
   * 本周业务量统计
   *
   * @param handleServiceKey
   * @param deptId
   * @return
   */
  List<StatistBasics> getThisWeekHandlingCountByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  List<StatistBasics> getThisWeekHandlingCountByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  List<StatistBasics> getThisWeekHandlingCount(@Param("deptId") Integer deptId);

  /**
   * 上周业务量统计
   * @param handleServiceKey
   * @param deptId
   * @return
   */
  List<StatistBasics> getLastWeekHandlingCountByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

    List<StatistBasics> getLastWeekHandlingCountByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

    List<StatistBasics> getLastWeekHandlingCount(@Param("deptId") Integer deptId);

    /**
     * 获取服务人数
     */
    Integer getPeopleCountByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

    Integer getPeopleCountByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

    Integer getPeopleCount(@Param("deptId") Integer deptId);

  /**
   * 正在办理人数
   */
  Integer getRealtimeMonitoringDataByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  Integer getRealtimeMonitoringDataByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getRealtimeMonitoringData(@Param("deptId") Integer deptId);

  /**
   * 排队等待人数
   */
  Integer getRealtimeMonitoringLineUpDataByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  Integer getRealtimeMonitoringLineUpDataByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getRealtimeMonitoringLineUpData(@Param("deptId") Integer deptId);

  /**
   * 办理业务量统计
   */
  Integer getTodayCount(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getSevenDayAverageCount(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getThisMonthCount(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getYesterDayCount(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getThirtyDayCount(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  Integer getLastMonthCount(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

    /**
     * 获取区域排行top5 统筹+审批
     *
     * @param handleServiceKeyList
     * @param deptId
     * @return
     */
    List<StatistBasics> getAreaRanking(@Param("handleServiceKeyList") List<String> handleServiceKeyList,
                                       @Param("deptId") Integer deptId);

    /**
     * 获取区域排行top5 服务中心
     *
     * @param serviceKeyList
     * @param deptId
     * @return
     */
    List<StatistBasics> getAreaRankingOfServer(@Param("deptId") Integer deptId,
                                               @Param("serviceKeyList") List<String> serviceKeyList);

    List<StatistBasics> getDeptBusinessProportion(@Param("deptIdList") List<Integer> deptIdList,
                                                  @Param("serviceKeyList") List<String> serviceKeyList);

    List<StatistBasics> getBusinessTypeProportion(@Param("deptId") Integer deptId,
                                                  @Param("serviceKeyList") List<String> serviceKeyList);
}
