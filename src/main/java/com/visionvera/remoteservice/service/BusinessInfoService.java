package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.ClientInfo;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UserSortVo;
import com.visionvera.remoteservice.pojo.BusinessVo;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import java.util.List;
import java.util.Map;
/**
 * @author lijintao
 * @date
 */
public interface BusinessInfoService {

  /**
   * webSoeck获取任务开始受理
   *
   * @param userBean
   * @return
   */
  ShowBusinessInfo getWebSocketTask(SysUserBean userBean,BusinessInfo info);
/*  ShowBusinessInfo getWebSocketTaskAndModify(SysUserBean userBean);*/

  /**
   * 业务变更
   *
   * @param userBean
   * @return 变化后的业务
   * @auther ljfan
   */
  Map<String, Object> changeBusinessInfo(Integer id, String describes, Integer type,
      String cardName, String cardId);

  /**
   * 业务变更
   *
   * @param userBean
   * @return 获取支持的业务类型
   * @auther ljfan
   */
  Map<String, Object> getBusinessTypeByDeptId(Integer deptId);

  /**
   * 身份证下一步接口
   *
   * @param businessId
   * @param clientInfo
   * @return
   */
  Map<String, Object> nextStepAndModify(Integer businessId, ClientInfo clientInfo);

  /**
   * 完成业务接口
   *
   * @param businessId 业务id
   * @param finalStates 最终状态
   * @param causeIds 未处理原因id ，多个中间用逗号隔开
   * @param remarks 描述
   * @param clientInfo 客户信息
   * @param user
   * @return
   */
  Map<String, Object> completeBusinessAndModify(Integer businessId, Integer finalStates,
      String causeIds, String remarks, ClientInfo clientInfo, SysUserBean user);

  /***
   *
   *  查看业务列表
   * @param pageNum 当前页码
   * @param pageSize 每页显示条数
   * @param businessType 业务类型（公安、社保）
   * @param businessState 业务状态（正在处理、排队中、已处理）
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return
   */
  Map<String, Object> getBusinessList(BusinessVo businessVo);

  /***
   *
   *  查看业务状态
   * @param businessInfo 查询参数
   * @return
   */
  Map<String, Object> getBusinessState(BusinessInfo businessInfo);

  /**
   * 查询业务日志列表
   *
   * @param pageNum 页码
   * @param pageSize 条数
   * @param businessId 业务id
   * @return
   */
  Map<String, Object> getBusinessLogList(Integer pageNum, Integer pageSize, Integer businessId);

  /**
   * 根据业务类型判断表单是否需要签名签章
   *
   * @param businessType 业务类型id
   * @return
   */
  Map<String, Object> isSign(Integer businessType);

  /**
   * 暂停受理（暂停业务）
   *
   * @param businessId 业务id
   * @param finalStates 最终状态
   * @param causeIds 未处理原因id ，多个中间用逗号隔开
   * @param remarks 描述
   * @param clientInfo 客户信息
   * @param user 登录用户信息
   * @return
   */
  Map<String, Object> pauseToDealAndModify(Integer businessId, Integer finalStates, String causeIds,
      String remarks, ClientInfo clientInfo, SysUserBean user);

  /**
   * 根据业务id查询业务信息
   *
   * @param businessId 业务Id
   * @return
   */
  BusinessInfo selectByPrimaryKey(Integer businessId);

  /**
   * 根据用户id获取当前用户未结束的任务
   *
   * @param userId 用户id                                                                                       
   * @return
   */
  BusinessInfo getBusinessByUserId(Integer userId);

  /**
   * 根据业务id获取客户信息
   *
   * @param businessId 业务id
   * @return
   */
  Map<String, Object> getClientInfo(Integer businessId);

  /**
   * 根据业务id获取业务的明细报告
   * @param businessId
   * @return
   */
  Map<String, Object> getBusinessDetail(Integer businessId);

  /**
   * 　　* @Description: 根据部门，servicekey，查询状态为处理中的业务 　　* @author: xueshiqi 　　* @date: 2018/10/30
   */
  List<BusinessInfo> getBusinessesByCondition(String deptId, String serviceKey, int i);

  /**
   * @param businessTypeBean
   * @Description: 根据部门id及业务类型父级id：businessTypeId , 原业务详情id:businessType 取得业务名称
   * @date 2018/11/3
   */
  Map<String, Object> getBussinessInfoList(BusinessVo businessVo);

  /**
   * 业务变更获取支持的业务类型
   *
   * @param deptId,serviceKey
   * @return 支持的业务类型
   * @auther ljfan
   */
  Map<String, Object> selectBusinessTypeByDeptId(Integer deptId);

  /**
   * 　　* @Description: 查询业务员名下有没有处理中的业务 　　* @author: xueshiqi 　　* @date: 2018/12/14
   */
  List<BusinessInfo> getBusinessesByUserId(Integer userId);

  /* *//**
   * 等待处理号信息获取
   * @param deptId
   * @param serviceKeys
   * @return
   *//*
   ShowBusinessInfo getNextNumberInfo(Integer deptId,List<String> serviceKeys);*/

  /**
   * 一窗多办android等待人数
   */
  long getAndroidAllWaitNumber(String serviceKey);

  /**
   * 一窗宗办受理端等待人数
   * @param deptId
   * @param serviceKey 审批中心
   * @return
   */
  long getAcceptAllWaitNumber(String serviceKey);

  /**
   * android统筹中心待办理业务数
   *
   * @param deptId
   * @param serviceKey 审批中心
   * @return
   */
  long getAndroidWaitNumber(String serviceKey, Integer deptId);

  /**
   * 受理端业务员待办数统计
   *
   * @param deptId
   * @param serviceKey 审批中心
   * @return
   */
  long getAcceptWaitNumber(String serviceKey, Integer deptId);

  /**
   * 一办理人数
   */
  Integer getDoneCount(SysUserBean userBean);

  /**
   * 受理端消息推送
   *
   * @param waitnumber
   * @param businessInfo
   * @param code
   * @param message
   */
  void sendMessage(long waitnumber, BusinessInfo businessInfo, String code, String message);

  /**
   * 根据审批中心的serviceKey取得所有部门排队叫号信息
   *
   * @param serviceKey ,callNumber：受理端推送的业务号，呼叫办理号
   * @return
   */
  List<ShowBusinessInfo> getShowBusinessInfoList(String serviceKey, String callNumber);
  /**
   * 业务员id
   *
   * @param userId
   */
  void getWaitNumber(Integer userId);

  /**
   * 获取业务员已办理业务数
   *
   * @param userBean
   * @return
   */
  long getDoneNumber(SysUserBean userBean);

  /**
   *
   */
  void sendMessageToAndroid(Integer userId, String callNumber);

  /**
   * 叫号机待显示数据获取
   *
   * @param deptId
   * @param serviceKeys
   * @return
   */
  List<ShowBusinessInfo> getShowBusinessInfosForAndroid(Integer deptId, String serviceKey);
  /**
   * 同步部门等待号
   *
   * @param deptId,审批中心serviceKey
   */
  void sendMessageToAndroidDept(Integer deptId, String serviceKey);

  /**
   * 同步部门等待号
   *
   * @param deptId,审批中心serviceKey
   */
  void sendMessageToH5Dept(Integer deptId, String serviceKey);

  /**
   * 多部门消息推送 根据审批中心查询待推送待数据
   */
  ShowBusinessInfo getAcceptShowBusinessInfo(SysUserBean userBean);

  //根据村中心serviceKey，获取村中心上一级审批中心所有的村中心
  List<String> getServiceKeys(String serviceKey);
  /**
   * 根据审批：统筹中心获取redis待办业务
   */
  Integer getWaitBusinessInfo(String serviceKey, Integer deptId);

  /**
   * 将等待业务员存入redis中
   * @param userBean
   */
  void pushUserToqueue(SysUserBean userBean);
  /**
   *
   * @param serviceKey 审批中心的serviceKey
   * @param deptId
   */
  void popBusinessInfo(String serviceKey);

  /**
   * 根据userID 获取队列中的业务员
   * @param userId
   * @return
   */
 // UserSortVo getUserSortVo(Integer userId);
  UserSortVo getUserSortVo(Integer userId);

  /**
   * 根据村中心获取叫号机，并给叫号机推送叫号信息
   * @param serviceKey
   */
  public void getSendMessageForAndroid(String serviceKey,String callNumber);


  /**
   * 叫号机删除消息推送
   * @param serviceKey
   */
  public void SendMessageForAndroidByJHJ(String serviceKey);
  /**
   * 根据相应的村中心serviceKeys、部门ID 获取当前最后一个办理中的号
   */
  public String getCallNumberForAndroid(String serviceKey,Integer deptId);

  public BusinessInfo searchBusinessInfo(String idCardNumber);


}
