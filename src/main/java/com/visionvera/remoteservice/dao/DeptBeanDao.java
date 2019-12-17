package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.DeptBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptBeanDao {

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int deleteByPrimaryKey(Integer id);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int insert(DeptBean record);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int insertSelective(DeptBean record);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  DeptBean selectByPrimaryKey(Integer id);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int updateByPrimaryKeySelective(DeptBean record);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int updateByPrimaryKey(DeptBean record);

  /**
   * 校验名称
   *
   * @param id
   * @param serviceKey
   * @param deptName
   * @return
   */
  int checkTheName(@Param("id") Integer id, @Param("serviceKey") String serviceKey,
      @Param("deptName") String deptName);

  /**
   * 删除
   *
   * @param ids
   * @return
   */
  int deleteDeptByIds(Integer[] ids);

  /**
   * 查询接口
   *
   * @param paramsMap
   * @return
   */
  List<DeptBean> getDeptList(Map<String, Object> paramsMap);

  /**
   * 查询接口
   *
   * @param serviceKey 服务中心key
   * @return
   */
  int selectByServiceKey(String serviceKey);

  /**
   * @param [deptId]
   * @return java.util.List<java.lang.String>
   * @description: 获取标题
   * @author quboka
   * @date 2018/8/22 14:42
   */
  List<String> getDeptTitle(@Param("deptId") Integer deptId);

  /**
   * @param [deptTitle, deptId]
   * @return int
   * @description: 修改标题
   * @author quboka
   * @date 2018/8/22 16:01
   */
  int updateServiceTitle(@Param("deptTitle") String deptTitle,
      @Param("deptId") Integer deptId);

  /**
   * @param [paramsMap]
   * @return int
   * @description: 修改状态
   * @author quboka
   * @date 2018/8/30 17:40
   */
  int updateState(Map<String, Object> paramsMap);

  /**
   * @param [serviceKeys]
   * @return java.util.List<java.lang.Integer>
   * @description: 根据servicekey查询部门id
   * @author quboka
   * @date 2018/8/31 15:48
   */
  List<Integer> selectDepteIdByServiceKey(List<String> serviceKeys);
}
