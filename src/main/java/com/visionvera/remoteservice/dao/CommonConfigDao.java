package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.CommonConfigBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通用配置表数据层
 *
 * @author wangrz
 */
public interface CommonConfigDao {

  /***
   * 根据名称查询指定值
   * @param commonName
   * @return
   */
  CommonConfigBean getCommonConfigByName(String commonName);

  /**
   * @param paramsMap
   * @return Map<String   ,   Object>
   * @Description: 新增
   * @author quboka
   * @date 2017年12月26日
   */
  int add(Map<String, Object> paramsMap);

  /**
   * @param id
   * @return int
   * @Description: 删除
   * @author quboka
   * @date 2017年12月26日
   */
  int delete(Integer id);

  /**
   * @param paramsMap
   * @return int
   * @Description: 校验
   * @author quboka
   * @date 2017年12月26日
   */
  int checkout(Map<String, Object> paramsMap);

  /**
   * @param commonConfig
   * @return int
   * @Description: 修改
   * @author quboka
   * @date 2017年12月26日
   */
  int update(CommonConfigBean commonConfig);

  /**
   * @param para
   * @return int
   * @Description: 根据key 修改值
   * @author quboka
   * @date 2018年1月3日
   */
  int updateValueByKey(Map<String, Object> para);

  /**
   * @param para
   * @return int
   * @Description: 根据key 和version实现乐观锁修改值
   * @author lijintao
   * @date 2018年1月3日
   */
  int updateValueByKeyAndVersion(Map<String, Object> para);

  /**
   * @return List<CommonConfigBean>
   * @Description: 查询列表
   * @author quboka
   * @date 2018年1月3日
   */
  List<CommonConfigBean> getList();

  /***
   * 根据名称查询指定值
   * @param commonName
   * @return
   */
  List<CommonConfigBean> findCommonConfigByName(String commonName);
}
