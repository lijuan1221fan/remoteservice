package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.pojo.BusinessTypeVo;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: quboka
 * @Date: 2018/8/24 09:45
 * @Description:
 */
public interface BusinessTypeService {

  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 加添业务类别
   * @author quboka
   * @date 2018/8/24 11:19
   */
  Map<String, Object> addBusinessClasses(BusinessTypeBean businessTypeBean);

  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改业务类别
   * @author quboka
   * @date 2018/8/24 17:08
   */
  Map<String, Object> updateBusinessClasses(BusinessTypeBean businessTypeBean);

  /**
   * @param [ids]
   * @param isDel
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除类别
   * @author quboka
   * @date 2018/8/24 17:28
   */
  Map<String, Object> deleteBusinessClasses(Integer[] ids, Integer isDel);

  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 业务类别查询
   * @author quboka
   * @date 2018/8/27 11:23
   */
  Map<String, Object> getBusinessClasses(BusinessTypeVo businessTypeVo);
  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 业务类别查询
   * @author quboka
   * @date 2018/8/27 11:23
   */
  Map<String, Object> getTakeNumberBusinessClasses(BusinessTypeVo businessTypeVo);


  /**
   * id查询业务详情
   *
   * @param businessDetailId
   * @return BusinessTypeBean
   */
  Map<String, Object> getBusinessDetailById(Integer businessDetailId);
  /**
   * id查询业务类别
   * @param businessTypeById
   * @return BusinessTypeBean
   */
  Map<String, Object> getBusinessTypeById(Integer businessTypeById);
  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 业务类别列表查询
   * @author ljfan
   * @date 2018/11/14 11:23
   */
  Map<String, Object> getBusinessClassesNoPage(BusinessTypeBean businessTypeBean);


  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 根据业务ID查询业务材料
   * @author ljfan
   * @date 2018/11/14 11:23
   */
  Map<String, Object> getBusinessTypeInfoById(@Param("id") Integer id);

  /**
   * @param [businessTypeBean]
   * @param typeMaterialsRel
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 添加业务详情
   * @author quboka
   * @date 2018/8/27 15:26
   */
  Map<String, Object> addBusinessType(BusinessTypeVo businessTypeVo);

  /**
   * @param [businessTypeBean]
   * @param materialsIds
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改业务详情
   * @author quboka
   * @date 2018/8/27 15:25
   */
  Map<String, Object> updateBusinessType(BusinessTypeVo businessTypeVo);

  /**
   * 根据父id获取所有业务类型 不传父id 为全部
   *
   * @param parentId 父id
   * @param session session对象
   * @return
   */
 /* Map<String, Object> getBusinessType(Integer parentId);*/

  /**
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改状态
   * @author quboka
   * @date 2018/8/30 17:48
   */
  Map<String, Object> updateState(BusinessTypeBean businessTypeBean);

  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除业务详情
   * @author quboka
   * @date 2018/8/31 10:56
   */
  Map<String, Object> deleteBusinessType(Integer[] ids);

  /**
   * 校验业务详情
   *
   * @param businessTypeBean
   * @return
   */
  Map<String, Object> checkoutType(BusinessTypeBean businessTypeBean);

  /**
   * 　　* @Description: 启用/禁用服务类别 　　* @author: xueshiqi 　　* @date: 2018/10/29
   */
  Map<String, Object> updateBusinessTypeState(BusinessTypeVo businessTypeVo);

  /**
   * 根据村级serviceKey 取得带办理及办理数据显示
   * @param businessTypeVo
   * @return
   */
  Map<String,Object> androidShow(BusinessTypeVo businessTypeVo);

  /**
   * 设定业务签名、盖章位置
   * @param businessTypeBean
   */
  void setBusinessTypeBeanForPhotoConfig(BusinessTypeBean businessTypeBean);
  /**
   * 业务配置添加位置配置
   */
  void insertPhotoConfig(Integer photoConfigType, Integer businessTypeId, Integer x, Integer y, Integer h, Integer w);
}
