/*
 * UploadBeanDao.java
 * ------------------*
 * 2018-08-29 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.UploadBean;

public interface UploadBeanDao {

  int deleteByPrimaryKey(Integer id);

  int insert(UploadBean record);

  int insertSelective(UploadBean record);

  UploadBean selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(UploadBean record);

  int updateByPrimaryKey(UploadBean record);

  UploadBean selectByBusinessId(Integer businessId);
}
