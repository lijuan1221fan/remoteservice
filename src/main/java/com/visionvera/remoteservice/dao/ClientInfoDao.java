/*
 * ClientInfoDao.java
 * ------------------*
 * 2018-08-23 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.ClientInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author lijintao
 */
public interface ClientInfoDao {

  int deleteByPrimaryKey(Integer id);

  int insert(ClientInfo record);

  int insertSelective(ClientInfo record);

  ClientInfo selectByPrimaryKey(Integer id);

  ClientInfo selectByIdcard(String idcard);

  int updateByPrimaryKeySelective(ClientInfo record);

  int updateByPrimaryKey(ClientInfo record);

  void addClientInfoRelevance(@Param("businessId") Integer businessId, @Param("id") Integer id,
      @Param("educationId") Integer educationId, @Param("phone") String phone);

  String getNationName(String nation);

  ClientInfo selectByBusinessId(Integer businessId);
  //根据客户id删除学历关联关系
  int deleteByClientId(@Param("businessId") Integer businessId, @Param("clientId") Integer clientId);
}
