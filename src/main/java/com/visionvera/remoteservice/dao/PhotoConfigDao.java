package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.PhotoConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotoConfigDao {

  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  int deleteByPrimaryKey(Integer id);
  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  int insert(PhotoConfig record);

  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  int insertSelective(PhotoConfig record);

  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  PhotoConfig selectByPrimaryKey(Integer id);

  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  List<PhotoConfig> selectByObject(PhotoConfig record);

  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  int updateByPrimaryKeySelective(PhotoConfig record);

  /**
   * @mbg.generated Tue Jul 31 10:12:54 CST 2018
   */
  int updateByPrimaryKey(PhotoConfig record);

  PhotoConfig selectByBusinessType(@Param("businessType") Integer businessType,
                                   @Param("type") Integer type);
  /**
   * @param 根据业务详情ID取得签名盖章自定义位置
   */
  List<PhotoConfig> selectByBusinessDetailId(Integer businessDetailId);
}
