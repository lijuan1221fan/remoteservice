package com.visionvera.remoteservice.dao;

    import com.visionvera.remoteservice.bean.ServiceCenterBean;
    import java.util.List;
    import java.util.Map;
    import org.apache.ibatis.annotations.Param;

/**
 * ClassName: ServiceCenterDao
 *
 * @author quboka
 * @Description: 服务中心
 * @date 2018年4月11日
 */
public interface ServiceCenterDao extends CrudDao<ServiceCenterBean> {

  /**
   * @param fuzzyServiceName 模糊查询的服务中心名称
   * @param gradeId 服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村
   * @param serviceId
   * @return Map<String   ,   Object>
   * @Description: 查询服务中心列表
   * @author quboka
   * @date 2018年4月11日
   */
  List<ServiceCenterBean> getServiceCenterList(
      @Param("fuzzyServiceName") String fuzzyServiceName,
      @Param("gradeId") Integer gradeId, @Param("serviceId") Integer serviceId,
      @Param("serviceKey") String serviceKey);


  List<ServiceCenterBean> getPageServiceCenterList(@Param("type") Long type,
                                                   @Param("serviceName") String serviceName,
                                                   @Param("serviceKey") String serviceKey,
                                                   @Param("userServiceKey")String userServiceKey);

  /**
   * 取号机获取中心列表
   * @param serviceName 服务中心名称
   * @date 2019/01/10
   */
  List<ServiceCenterBean> getOfferNumOfServiceCenterList(ServiceCenterBean serviceCenterBean);
  /**
   * @param serviceIds 服务中心id  多个用逗号相隔
   * @return Map<String   ,   Object>
   * @Description: 删除服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  int deleteServiceCenter(String[] serviceIdArray);

  /**
   * @param serviceCenter
   * @return int
   * @Description: 添加服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  int addServiceCenter(ServiceCenterBean serviceCenter);

  /**
   * @param paramsMap
   * @return int
   * @Description: 校验名称
   * @author quboka
   * @date 2018年4月11日
   */
  ServiceCenterBean getServiceCenterByName(String serviceName);

  /**
   * @param serviceCenter
   * @return int
   * @Description: 修改服务中心
   * @author quboka
   * @date 2018年4月11日
   */
  int updateServiceCenter(ServiceCenterBean serviceCenter);

  /**
   * @param serviceKey
   * @return int
   * @Description: 通过服务中心key查询级别
   * @author quboka
   * @date 2018年4月12日
   */
  int getServiceGradeIdByKey(String serviceKey);

  /**
   * @param serviceKey
   * @return ServiceCenterBean
   * @Description: 查询服务中心
   * @author quboka
   * @date 2018年5月21日
   */
  ServiceCenterBean getServiceCenter(String serviceKey);

  /**
   * @param serviceKey
   * @return ServiceCenterBean
   * @Description: 查询所有服务中心
   * @author ljfan
   * @date 2018年11月20日
   */
  List<ServiceCenterBean> getServiceCentList();

  /**
   * @param serviceKey
   * @return ServiceCenterBean
   * @Description: 根据服务中心的serviceKey查询统筹中心
   * @author ljfan
   * @date 2018年11月13日
   */
  Map getOverAllByServiceKey(@Param("serviceKey") String serviceKey);


  /**
   * @param serviceCenter
   * @Description: 查询服务中心列表
   * @author zhanghui
   * @date 2018年5月24日
   */
  List<ServiceCenterBean> getCenterListByParams(ServiceCenterBean serviceCenter);

  /**
   * @param gradeId 服务中心级别
   * @return int
   * @Description: 根据级别查询服务中心数量
   * @author quboka
   * @date 2018年6月7日
   */
  int getServiceCenterCountByGradeId(Integer gradeId);

  /**
   * @param serviceIs
   * @return List<Integer>
   * @Description: 根据serviceId 查询serviceKey
   * @author quboka
   * @date 2018年7月4日
   */
  List<String> getServiceKeysByServiceIds(String[] serviceIs);

  /**
   * @param parentKeys
   * @return int
   * @Description: 根据上级key删除服务中心
   * @author quboka
   * @date 2018年7月5日
   */
  int deleteByParentKey(List<String> parentKeys);

  /**
   * @param serviceKeys
   * @return List<String>
   * @Description: 查询子集服务servicekey
   * @author quboka
   * @date 2018年7月5日
   */
  List<String> getServiceKeysByParentServiceKeys(@Param("serviceKeys") List<String> serviceKeys);

  /**
   * @param [serviceKey]
   * @return java.lang.String
   * @description: 查询审批中心标题
   * @author quboka
   * @date 2018/8/22 14:26
   */
  String getServiceTitle(@Param("serviceKey") String serviceKey);

  /**
   * @param [serviceTitle, serviceKey]
   * @return int
   * @description: 修改标题
   * @author quboka
   * @date 2018/8/22 16:01
   */
  int updateServiceTitle(@Param("serviceTitle") String serviceTitle,
      @Param("serviceKey") String serviceKey);

  /**
   * @param [serviceKey]
   * @return java.lang.String
   * @description: 通过服务中心key 查询审批中心key
   * @author quboka
   * @date 2018/8/28 19:16
   */
  String getParentKey(String serviceKey);


  /**
   * @param regionKey
   * @return
   * @Description: 通过regionKey 查询所有中心
   * @author JLM
   */
  List<ServiceCenterBean> queryCenterList(@Param("regionKey") String regionKey);

  /**
   * 通过条件查询服务中心
   *
   * @param serviceCenterBean
   * @return
   */
  List<ServiceCenterBean> getServiceCenterByCondition(ServiceCenterBean serviceCenterBean);


    List<ServiceCenterBean> getServiceCenterListByCondition(ServiceCenterBean serviceCenterBean);
  /**
   * @param serviceKey
   * @return
   * @Description: 通过serviceKey查询所有子中心serviceKey
   * @author JLM
   */
  List<String> getServiceKeysByParentServiceKey(String serviceKey);

  /**
   * 用于查询审核中心 绑定窗口
   *
   * @param paramMap
   * @return
   * @author jlm
   */
  List<ServiceCenterBean> getVerifyCenterList(Map<String, Object> paramMap);

  /**
   * 分页查询审批中心下所有的服务中心
   *
   * @param parentKeys
   * @return
   */
  List<ServiceCenterBean> getServiceCenterListByParentKey(
      @Param("parentKeys") List<String> parentKeys);

  /**
   * 　　* @Description: 根据servicekey查询服务中心 　　* @author: xueshiqi 　　* @date: 2018/11/22 0022
   */
  List<ServiceCenterBean> getServiceCenterByServiceCenterKey(Map<String,Object> param);

  /**
   　　* @Description:根据servicekey查询中心，供中心详情使用
   　　* @author: xueshiqi
   　　* @date: 2018/12/28
   　　*/
  ServiceCenterBean getServiceCenterByServiceKey(String serviceCenterKey);
  /**
   　　* @Description:根据servicekey查询中心，供中心详情使用
   　　* @author: xueshiqi
   　　* @date: 2018/12/28
   　　*/
  List<ServiceCenterBean> getServiceCenterByUser(@Param("serviceKey")String serviceKey);
  /**
   　　* @Description:根据审批中心serviceKey查询所有村中心
   　　* @author: ljfan
   　　* @date: 2019/02/13
   　　*/
  List<String> getServiceListByParentKey(@Param("parentKey")String parentKey);
  /**
　　* @Description:根据统筹中心serviceKey取得村中心
　　* @author: ljfan
　　* @date: 2019/06/18
　　*/
  List<String> getServiceKeyListByWholeParentKey(@Param("serviceKey")String serviceKey);
  /**
　　* @Description:根据统筹中心serviceKey取得所有审批及村中心serviceKey
　　* @author: ljfan
　　* @date: 2019/06/18
　　*/
  List<String> getServiceKeyListByWholeServiceKey(@Param("serviceKey")String serviceKey);
  /**
　　* @Description:根据统筹统筹中心的serviceKey查询服务中心，供数据权限使用
　　* @author: ljfan
　　* @date: 2019/02/13
　　*/
  List<String> getServiceKeysByParentKey(@Param("parentKey")String parentKey);
  /**
　　* @Description:根据村中心的serviceKey查询服务中心
　　* @author: ljfan
　　* @date: 2019/06/20
　　*/
  List<String> getServiceKeyListByServiceKey(@Param("serviceKey")String serviceKey);
  /**
　　* @Description:根据parentKey查询中心，供数据权限使用
　　* @author: ljfan
　　* @date: 2019/02/13
　　*/
  List<ServiceCenterBean> getServiceCenterByParentKey(@Param("parentKey")String parentKey);

  List<ServiceCenterBean> queryList(ServiceCenterBean serviceCenterBean);
  /**
   * 根据村级服务中心查询审批中心
   */
  ServiceCenterBean getServiceListByChildServiceKey(@Param("serviceKey")String serviceKey);
  /**
   * 根据村级服务中心查询同一审批中心下的中心
   */
  List<ServiceCenterBean> getServiceCentListByserviceKey(@Param("serviceKey")String serviceKey);

  /**
   * 根据统筹中心查询所有服务中心
   * @param serviceKey
   * @return
   */
  List<ServiceCenterBean> getServiceCenterKeyByParentKey(String serviceKey);
  /**
   * 根据统筹中心查询所有服务中心
   * @param serviceKey
   * @date: 2019/06/18
   * @return
   */
  List<ServiceCenterBean> getServiceCenterListByWholeParentKey(@Param("serviceKey")String serviceKey);  /**
   * 根据子中心serviceKey查询父中心
   * @param serviceKey
   * @date: 2019/06/18
   * @return
   */
  ServiceCenterBean getParentServiceCenterByServiceKey(@Param("serviceKey")String serviceKey);

    /**
     * 根据经纬度获取中心
     *
     * @param lal
     * @return
     */
    ServiceCenterBean getServiceCenterByLal(@Param("lal") String lal);

  List<ServiceCenterBean> getServiceCenterListByServiceKeys(@Param("serviceKeyList") List<String> serviceKeyList);

    /**
     * 查询服务中心的状态 办理中 等待中  空闲中
     *
     * @param serviceKeyList
     * @return
     */
    List<ServiceCenterBean> getDoingStatus(@Param("serviceKeyList") List<String> serviceKeyList);

  List<ServiceCenterBean> getWaitingStatus(@Param("serviceKeyList") List<String> serviceKeyList);
  /**
   * 获取所有的村终端
   */
  List<ServiceCenterBean> getChirdServiceList();

    /**
     * 根据中心类型查询有区域数据的中心
     *
     * @return
     */
    List<String> getAreaServiceCenterOfType(@Param("serviceKeys") List<String> serviceKeys,
                                            @Param("typeList") List<Integer> typeList,
                                            @Param("number") Integer number);
  /**
   * 根据主键查询中心数据
   */
  ServiceCenterBean selecServiceCenterBeantByPrimaryKey(@Param("serviceId") Integer serviceId);
}
