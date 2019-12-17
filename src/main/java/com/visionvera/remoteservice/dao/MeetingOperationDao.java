/*
 * MeetingOperationDao.java
 * ------------------*
 * 2018-08-23 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.MeetingOperation;

import java.util.List;

/**
 * @author lijintao
 */
public interface MeetingOperationDao {

  int deleteByPrimaryKey(Integer id);

  int insert(MeetingOperation record);

  int insertSelective(MeetingOperation record);

  MeetingOperation selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(MeetingOperation record);

  int updateByPrimaryKey(MeetingOperation record);

  MeetingOperation selectByBusinessId(Integer businessId);

  List<Integer> selectByState(Integer state);
}