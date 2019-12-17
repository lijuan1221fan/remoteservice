package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.NumberIteration;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: NumberIterationDao
 *
 * @author quboka
 * @Description: 号码递增
 * @date 2018年3月22日
 */
public interface NumberIterationDao {

  /**
   * @param serviceKey服务中心唯一标识
   * @param type 类型  1000：公安远程服务 2000：社保远程服务
   * @param businessType
   * @return NumberIteration
   * @Description: 取号
   * @author quboka
   * @date 2018年3月22日
   */
  NumberIteration getNumber(@Param("serviceKey") String serviceKey,
      @Param("type") String type);

  /**
   * @param serviceKey服务中心唯一标识
   * @param type 类型  1000：公安远程服务 2000：社保远程服务
   * @param businessType
   * @return int
   * @Description: 号码迭代
   * @author quboka
   * @date 2018年3月22日
   */
/*  int numberIteration(@Param("serviceKey") String serviceKey, @Param("type") String type);*/

  /**
   * @return int
   * @Description: 号码归零
   * @author quboka
   * @date 2018年3月29日
   */
  int updateNumberIsZero();

  /**
   * @param numberIteration
   * @return int
   * @Description: 添加号码迭代
   * @author quboka
   * @date 2018年5月24日
   */
  int addNumberIteration(NumberIteration numberIteration);

  /**
   * 根据审批中心，部门查询迭代号码
   * @param numberIteration
   * @return
   */
  List<NumberIteration> getNumberParam(NumberIteration numberIteration);
  /**
   * 根据审批中心，部门迭代号码递增
   * @param numberIteration
   * @return
   */
  int numberIteration(NumberIteration numberIteration);

  /**
   * 根据部门更新审批中心的serviceKey
   * @param numberIteration
   * @return
   */
  int updateServiceKey(NumberIteration numberIteration);

}
