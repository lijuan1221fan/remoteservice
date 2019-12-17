/*
 * BusinessLogDao.java
 * ------------------*
 * 2018-08-23 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.BusinessLog;
import java.util.List;

public interface BusinessLogDao {

  int deleteByPrimaryKey(Integer id);

  /**
   * 根据业务id删除操作记录
   * @param businessId
   * @return
   */
  int deleteByBusinessId(Integer businessId);

  int insert(BusinessLog record);

  int insertSelective(BusinessLog record);

  BusinessLog selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(BusinessLog record);

  int updateByPrimaryKey(BusinessLog record);

  List<BusinessLog> selectByBusinessId(Integer businessId);
}
