package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.ServiceCenterBean;

import java.util.List;
import java.util.Map;

/**
 * ClassName: ServiceCenterService
 *
 * @author quboka
 * @Description: 服务中心
 * @date 2018年4月11日
 */
public interface ServiceCenterService extends BaseService<ServiceCenterBean> {

  /**
   * @param fuzzyServiceName 模糊查询的服务中心名称
   * @param pageSize
   * @param pageNum
   * @param gradeId 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村
   * @param serviceId
   * @return Map<String       ,       Object>
   * @Description: 查询服务中心列表
   * @author quboka
   * @date 2018年4月11日
   */
  Map<String, Object> getServiceCenterList(String fuzzyServiceName, Integer pageNum,
      Integer pageSize,
      Integer gradeId, Integer serviceId, String serviceKey);

  Map<String, Object> getPageServiceCenterList(Integer pageNum,Integer pageSize,
                                               Long type,String serviceName,String serviceKey);

  /**
  * 取号机获取审批及审批中心
  */
   Map<String,Object> getOfferNumOfServiceCenterList(String serviceName);


  /**
  *
   * @param fuzzyServiceName 模糊查询的服务中心名称
   * @param pageSize
   * @param pageNum
   * @param gradeId 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村
   * @param serviceId
   * @return Map<String       ,       Object>
   * @Description: 查询服务中心列表
   * @author quboka
   * @date 2018年4月11日
   *//*
  Map<String, Object> getNumberServiceCenterList(String fuzzyServiceName, Integer pageNum,
      Integer pageSize,
      Integer gradeId, Integer serviceId, String serviceKey,Integer type);*/

  /**
   * @param serviceIds 服务中心id  多个用逗号相隔
   * @return Map<String       ,       Object>
   * @Description: 删除服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  Map<String, Object> deleteServiceCenter(String serviceIds);

  /**
   * @param serviceCenter
   * @return Map<String       ,       Object>
   * @Description: 添加服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  Map<String, Object> addServiceCenter(ServiceCenterBean serviceCenter);

  /**
   * @param serviceCenter
   * @return Map<String       ,       Object>
   * @Description: 修改服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  Map<String, Object> updateServiceCenter(
      ServiceCenterBean serviceCenter);

  /**
   * @param serviceCenter
   * @return List<Map       <       String       ,       Object>>
   * @Description: 查询子服务中心列表
   * @author zhanghui
   * @date 2018年5月26日
   */
  Map<String, Object> queryMapList(ServiceCenterBean serviceCenter, Integer pageNum,
      Integer pageSize);

  /**
   * @param regionKey
   * @return
   * @Description: 查询所有中心
   * @author JLM
   */
  Map<String, Object> queryCenterList(String regionKey);

  /**
   * 获取中心列表
   *
   * @param pageNum
   * @param pageSize
   * @param serviceCenterBean
   * @return
   */
  Map<String, Object> getPageCenterList(Map<String, Object> params);

  /**
   * 查询审核中心列表
   *
   * @author jlm
   * @date 2018.11.9
   */
  public Map<String, Object> getVerifyCenterList(Map<String, Object> paramMap);

  /**
   * 分页查询审批中心下所有的服务中心
   *
   * @param params
   * @return
   */
  Map<String, Object> getServiceCenterListByParentKey(Map<String, Object> params);

  /**
   * 　　* @Description: 获取树形数据 　　* @author: xueshiqi 　　* @date: 2018/11/13 0013
   */
  Map<String, Object> getServiceCenterTree(Boolean flag);

  /**
   * 　　* @Description: 根据servicekey查询服务中心 　　* @author: xueshiqi 　　* @date: 2018/11/22 0022
   */
  Map<String, Object> getServiceCenterByServiceCenterKey(String serviceCenterKey);

  /**
   * 　　* @Description: 查询审批中心以及子级服务中心 　　* @author: xueshiqi 　　* @date: 2018/11/28
   */
  Map<String, Object> getSecondServiceCenterAndChildren();
  /**
   * @Description: 多中心新建业务类别时，admin取得所有统筹中心及审批中心，当为中心管理员时，取得当前用户下所在的中心
   * @author: ljfan 　　
   * @date: 2019/1/16
   */
  Map<String, Object> getServiceCenterByUser(boolean flage);

    List<ServiceCenterBean> getServiceCenterStatus(List<ServiceCenterBean> list);
}
