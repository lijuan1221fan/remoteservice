package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.pojo.BusinessTypeVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface BusinessTypeDao {

  /**
   * @mbg.generated Fri Aug 24 09:40:15 CST 2018
   */
  int deleteByPrimaryKey(Integer id);

  /**
   * @mbg.generated Fri Aug 24 09:40:15 CST 2018
   */
  int insert(BusinessTypeBean record);

  /**
   * @mbg.generated Fri Aug 24 09:40:15 CST 2018
   */
  int insertSelective(BusinessTypeBean record);

  /**
   * @mbg.generated Fri Aug 24 09:40:15 CST 2018
   */
  BusinessTypeBean selectByPrimaryKey(Integer id);

  /**
   * @mbg.generated Fri Aug 24 09:40:15 CST 2018
   */
  int updateByPrimaryKeySelective(BusinessTypeBean record);

  /**
   * @mbg.generated Fri Aug 24 09:40:15 CST 2018
   */
  int updateByPrimaryKey(BusinessTypeBean record);

  /**
   * @param [paramMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 加添业务类别
   * @author quboka
   * @date 2018/8/24 11:19
   */
  int addBusinessClasses(Map<String, Object> paramMap);

  /**
   * @param [businessTypeBean]
   * @return int
   * @description: 校验类别是否存在
   * @author quboka
   * @date 2018/8/24 11:21
   */
  int checkoutClasses(BusinessTypeBean businessTypeBean);

  /**
   * @param [businessTypeBean]
   * @return int
   * @description: 校验父子中心业务类别是否有重名
   * @author ljfan
   * @date 2019/01/10 11:21
   */
  Integer checkDiddCenterClasses(BusinessTypeBean businessTypeBean);

  /**
   * @description 检验业务详情是否已存在
   * @param businessTypeBean
   * @return
   */
  Integer checkDiddCenterType(BusinessTypeBean businessTypeBean);
  /**
   * @param [businessTypeBean]
   * @return int
   * @description:修改业务类别
   * @author quboka
   * @date 2018/8/24 17:14
   */
  int updateBusinessClasses(BusinessTypeBean businessTypeBean);

  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除类别
   * @author quboka
   * @date 2018/8/24 17:28
   */
  int deleteBusinessClasses(Integer[] ids);

  /**
   * @param [businessTypeBean]
   * @return java.util.List<com.visionvera.remoteservice.bean.BusinessTypeBean>
   * @description: 业务类别查询
   * @author quboka
   * @date 2018/8/27 15:27
   */
  List<BusinessTypeBean> getBusinessClasses(BusinessTypeBean businessTypeBean);
  /**
   * @param [businessTypeBean]
   * @return java.util.List<com.visionvera.remoteservice.bean.BusinessTypeBean>
   * @description: 多中心取号机业务类别查询
   * @author ljfan
   * @date 2019/1/22 15:27
   */
  List<BusinessTypeBean> getBusinessClasseByServiceKeys(BusinessTypeBean businessTypeBean);

  /**
   * id查询业务详情
   *
   * @param businessDetailId
   * @return BusinessTypeBean
   */
  BusinessTypeBean getBusinessDetailById(@Param("businessDetailId") Integer businessDetailId);
  /**
   * id查询业务详情
   *
   * @param businessDetailId
   * @return BusinessTypeBean
   */
  BusinessTypeBean getBusinessTypeById(@Param("businessTypeId") Integer businessTypeId);

  /**
   * 根据业务类型信息查的业务信息
   *
   * @param paramMap
   * @return
   * @author ljfan
   * @date2018/11/22
   */
  List<BusinessTypeBean> getBusinessInfo(Map<String, Object> paramMap);

  /**
   * 根据业务ID查询业务材料
   */
  List<Map<String, Object>> getBusinessTypeInfoById(@Param("id") Integer id);

  /**
   * @param [businessTypeBean]
   * @return int
   * @description: 业务详情校验
   * @author quboka
   * @date 2018/8/27 15:27
   */
  int checkoutType(BusinessTypeBean businessTypeBean);

  /**
   * @param [businessTypeBean]
   * @return int
   * @description: 添加业务详情
   * @author quboka
   * @date 2018/8/27 15:28
   */
  int addBusiness(BusinessTypeBean businessTypeBean);

  /***
   * @description: 插入关联
   *
   * @author quboka
   * @date 2018/8/27 16:19
   * @param [typeId,materialsIdList]
   * @return void
   */
  void insertRel(@Param("typeId") Integer typeId,
      @Param("materialsIdList") String[] materialsIdList);

  /**
   * @param [typeId]
   * @return void
   * @description: 删除关联
   * @author quboka
   * @date 2018/8/27 17:21
   */
  void deleteRel(Integer typeId);

  /**
   * 根据参数查询所有业务类型
   *
   * @param businessTypeBean
   * @return
   */
  List<BusinessTypeBean> getBusinessType(BusinessTypeBean businessTypeBean);

  /**
   * @param [businessTypeBean]
   * @return int
   * @description: 修改状态
   * @author quboka
   * @date 2018/8/30 17:49
   */
  int updateState(BusinessTypeBean businessTypeBean);

  /**
   * @param [ids]
   * @return void
   * @description: 删除多个关联
   * @author quboka
   * @date 2018/8/31 11:01
   */
  void deleteRelByIds(Integer[] ids);

  /**
   * @param [ids]
   * @return int
   * @description: 删除多个业务详情
   * @author quboka
   * @date 2018/8/31 11:29
   */
  int deleteByIds(Integer[] ids);

  /**
   * @param [deptIds]
   * @return void
   * @description: 根据部门删除业务类别和业务详情
   * @author quboka
   * @date 2018/8/31 14:34
   */
  Integer deleteByDeptId(Integer[] deptIds);

  /***
   * @description: 根据业务类别id 获取部门id
   *
   * @author quboka
   * @date 2018/9/4 15:38
   * @param [id]
   * @return java.lang.Integer
   */
  Integer getDeptIdByid(Integer id);

  /**
   * @param [deptId]
   * @return com.visionvera.ssm.bean.BusinessTypeBean
   * @description: 根据部门id 获取 默认类别
   * @author quboka
   * @date 2018/9/4 15:43
   */
  List<BusinessTypeBean> getDefaultClassesByDeptId(Integer deptId);

  /**
   * @param [businessTypeBean]
   * @return com.visionvera.remoteservice.bean.BusinessTypeBean
   * @description: 根据部门id 获取 默认类别
   * @author ljfan
   * @date 2018/9/4 15:43
   */
  List<BusinessTypeBean> getBusinessTypeByDeptId(BusinessTypeBean businessTypeBean);

  /***
   * @description: 根据 父id 修改父id
   *
   * @author quboka
   * @date 2018/9/4 15:56
   * @param [newParentId, parentId]
   * @return void
   */
  void updateParentId(@Param("newParentId") Integer newParentId,
      @Param("parentId") Integer parentId);

  /**
   * @param [parentIds]
   * @return java.util.List<java.lang.Integer>
   * @description: 根据业务类型id 查询 业务详情id
   * @author quboka
   * @date 2018/9/4 16:08
   */
  List<Integer> getTypeIdsByClassesIds(Integer[] parentIds);

  /**
   * 　　* @Description: 启用/禁用服务类别 　　* @author: xueshiqi 　　* @date: 2018/10/29
   */
  int updateBusinessTypeState(BusinessTypeVo businessTypeVo);


  /**
   * @Ddescription:根据部门id及业务类型父级id查询相应业务
   * @author：ljfan
   * @date:2018/11/3
   */
  List<BusinessTypeBean> getBusinessInfoList(BusinessTypeBean businessTypeBean);

  /**
   　　* @Description: 根据服务中心查询业务类别
   　　* @author: xueshiqi
   　　* @date: 2018/11/28
   　　*/
  List<BusinessTypeBean> selectBusinessTypeByServiceKeys(@Param("serviceKeys") List<String> serviceKeys);

  int updateBusinessTypeFormIsForm(@Param("isform") Integer isform,@Param("ids") List<Integer> ids);
  /**
   * 根据村中心取得serviceKey获取取号业务中的业务详情
   */
  BusinessTypeBean getBusinessTypeByServiceKey(@Param("serviceKey") String serviceKey);

    //查询业务数量

    Integer getBusinessTypeDetailCountByCheck(@Param("handleServiceKey") String handleServiceKey, @Param("deptId") Integer deptId);

    Integer getBusinessTypeDetailCountByAdmin(@Param("serviceKeyList") List<String> serviceKeyList, @Param("deptId") Integer deptId);

    Integer getBusinessTypeDetailCount(@Param("deptId") Integer deptId);

    Integer getBusinessTypeInfoCount(@Param("deptId") Integer deptId);
}
