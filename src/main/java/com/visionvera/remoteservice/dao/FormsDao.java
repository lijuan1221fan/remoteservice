package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.Forms;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

public interface FormsDao {

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  int deleteByPrimaryKey(Integer id);

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  int insert(Forms record);

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  int insertSelective(Forms record);

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  Forms selectByPrimaryKey(Integer id);

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  List<Forms> selectByObject(Forms record);

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  int updateByPrimaryKeySelective(Forms record);

  /**
   * @mbg.generated Mon Jul 30 09:52:04 CST 2018
   */
  int updateByPrimaryKey(Forms record);
}