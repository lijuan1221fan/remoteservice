package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.NumberBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface NumberDao {


  /**
   * @mbg.generated Wed Aug 29 16:50:20 CST 2018
   */
  int insertSelective(NumberBean record);

  /**
   * @mbg.generated Wed Aug 29 16:50:20 CST 2018
   */
  int updateByPrimaryKeySelective(NumberBean record);

  /**
   * @param serviceKey 审批中心key
   * @param deptId 部门id
   * @return int
   * @description: 号码迭代
   * @author quboka
   * @date 2018/8/29 16:59
   */
  /**
   * @author ljfan
   * @date 2018/11/26
   * 因部门不归属于中心，所有根据部门查询
   * @param deptId
   * @return
   */
  /**新需求变更
   * int numberIteration(@Param("serviceKey") String serviceKey, @Param("deptId") Integer deptId);*/
  int numberIteration(@Param("deptId") Integer deptId);

  /**
   * @param serviceKey 审批中心key
   * @param deptId 部门id
   * @return com.visionvera.remoteservice.bean.NumberBean
   * @description: 获取号码
   * @author quboka
   * @date 2018/8/29 17:19
   */
  NumberBean getNumber( @Param("deptId") Integer deptId);

  /**
   * @param [ids]
   * @return void
   * @description: 删除号码
   * @author quboka
   * @date 2018/8/31 15:04
   */
  int deleteByDeptId(Integer[] ids);

  /**
   * @param []
   * @return int
   * @description:号码归零
   * @author quboka
   * @date 2018/9/3 10:31
   */
  int updateNumberIsZero();

}