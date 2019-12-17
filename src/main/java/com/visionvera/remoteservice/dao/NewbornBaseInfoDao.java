/*
 * NewbornBaseInfoMapper.java
 * ------------------*
 * 2018-06-20 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.NewbornBaseInfo;
import com.visionvera.remoteservice.bean.NewbornInfoPrintVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface NewbornBaseInfoDao {

  int deleteByPrimaryKey(Integer id);

  int insert(NewbornBaseInfo record);

  int insertSelective(NewbornBaseInfo record);

  NewbornBaseInfo selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(NewbornBaseInfo record);

  int updateByPrimaryKey(NewbornBaseInfo record);

  Integer selectByPrimaryBusinessKey(String businessKey);

  NewbornInfoPrintVo getNewbornPrint(String businessKey);
}