/*
 * NewbornInfoDao.java
 * ------------------*
 * 2018-06-20 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.NewbornInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewbornInfoDao {

  int deleteByPrimaryKey(Integer id);

  int deleteByPrimaryBaseId(Integer baseId);

  int insert(NewbornInfo record);

  int insertSelective(NewbornInfo record);

  NewbornInfo selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(NewbornInfo record);

  int updateByPrimaryKey(NewbornInfo record);

  void insertBatch(@Param("baseId") Integer baseId,
      @Param("newbornInfos") List<NewbornInfo> newbornInfos);
}