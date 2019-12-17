/*
 * BusinessInfoDao.java
 * ------------------*
 * 2018-08-22 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.SendSmsBean;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author lijintao
 */
public interface BusinessInfoDao {

  int deleteByPrimaryKey(Integer id);

  int insert(BusinessInfo record);

  int insertSelective(BusinessInfo record);

  BusinessInfo selectByPrimaryKey(Integer id);

  List<BusinessInfo> selectByObject(BusinessInfo record);

  int updateByPrimaryKeySelective(BusinessInfo record);
  int insertCallInfomation(BusinessInfo record);

  int updateByPrimaryKey(BusinessInfo record);

  BusinessInfo getBusinessByUserId(Integer userId);

  /**
   * 将未办理业务做过号
   */
  void passNumber();

  /**
   * 根据业务类型 id 查询未完成的业务
   *
   * @return
   */
  int getUnfinishedBusinessByTypeIds(Integer[] typeIds);

  /**
   * 根据部门 id 查询未完成的业务
   *
   * @return
   */
  int getUnfinishedBusinessByDeptIds(Integer[] deptIds);

  List<BusinessInfo> getBusinessList(BusinessInfo businessInfo);

  BusinessInfo getBusinessInfoDetail(Integer id);
  /**
   * 获取业务统计数据
   * @param businessInfo
   * @return
   */
  List<BusinessInfo> getBusinessInfoList(BusinessInfo businessInfo);

  /*业务状态待处理*/
  Integer getBusinessByStatesBe(BusinessInfo businessInfo);

  /*业务状态处理中*/
  Integer getBusinessByStatesIng(BusinessInfo businessInfo);

  /*业务状态已处理*/
  Integer getBusinessByStatesed(BusinessInfo businessInfo);

  /*业务状态已处理，并且是处理成功的状态*/
  Integer getBusinessByStatesedAndSuccess(BusinessInfo businessInfo);

  /**
   * 查询当前服务中心正在办理和等待办理的业务个数
   * @param serviceKey
   * @return
   */
  Integer getBusinessListByserviceKey(String serviceKey);

  /**
   * 查询当前中心正在办理和等待办理的业务个数
   * @param handleServiceKey
   * @return
   */
  Integer getBusinessListByHandleServiceKey(String handleServiceKey);

  /**
   * 根据部门，servicekey，查询状态为处理中的业务
   *
   * @param deptId
   * @param serviceKey
   * @param
   * @return
   */
  List<BusinessInfo> getBusinessesByCondition(@Param("deptId") String deptId,
      @Param("serviceKey") String serviceKey, @Param("statu") int statu);

    /**
     * 查看服务中心的当前正在办理的业务
     *
     * @param serviceKey
     * @param state
     * @return
     */
    List<BusinessInfo> getBusinessListByCondition(@Param("serviceKey") String serviceKey,
                                                  @Param("state") int state);

  /**
   * 查询业务员空闲状态时待处理的业务
   *
   * @return 待处理数及业务员信息
   */
  List<SendSmsBean> checkUserState();

  /**
  　　* @Description: 查询业务员名下有无处理中的业务
  　　* @author: xueshiqi
  　　* @date: 2018/12/14
  　　*/
  List<BusinessInfo> getBusinessesByUserId(Integer userId);

  /**
   * 根据创建时间查询未处理的数据
   * @return
   */
  List<BusinessInfo> getTodoBusiness(@Param("serviceList") List<ServiceCenterBean> serviceList);
  /**
   * 根据取号类型type，部门ID，服务中心servicekey取得取号
   */
  List<BusinessInfo> businessesInfoByType(BusinessInfo businessInfo);
  /**
   * 根据部门ID，服务中心servicekeys取得待处理号
   */
  List<BusinessInfo> getWaitProcessBusinessInfo(BusinessInfo businessInfo);

  /**
   * 根据部门ID，服务中心servicekeys取得处理中的最后一个号
   * @param businessInfo
   * @return
   */
  List<BusinessInfo> getProcessingBusinessInfo(BusinessInfo businessInfo);

  //审批中心获取办理总量
  Integer getTotalAmountHandledByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

  //统筹中心获取办理总量
  Integer getTotalAmountHandledByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

  //全部中心获取办理总量
  Integer getTotalAmountHandled(@Param("deptId") Integer deptId);
  /**
   * 获取未办理当业务数
   */
  Integer getUntreated();
  /**
   * 获取根据办理中心获取每个村最先取并且村终端为空闲状态号
   */
  List<BusinessInfo> getBusinessInfoOfWaitingByServiceKey(@Param("serviceKeyList") List<String> serviceKeyList);

  Integer updateBusinessInfoById(BusinessInfo record);

  /**
   * 通过terminalNumber获取当前正在办理的业务
   * @param terminalNumber
   * @return
   */
  BusinessInfo getBusinessInfoOfTerminalNumber(String terminalNumber);

  /**
   * 获取当前排队和办理中的全部业务
   * @return
   */
  List<BusinessInfo> getAllWaitingAndDoingBusiness();

  BusinessInfo searchBusinessInfo(String idcard);

  List<BusinessInfo> getBusinessInfoList();
}
