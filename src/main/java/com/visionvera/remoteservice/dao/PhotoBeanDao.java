/*
 * PhotoBeanDao.java
 * ------------------*
 * 2018-08-27 created
 */
package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.PhotoBean;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface PhotoBeanDao {

  int deleteByPrimaryKey(Integer id);

  int insert(PhotoBean record);

  int insertSelective(PhotoBean record);

  PhotoBean selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(PhotoBean record);

  int updateByPrimaryKey(PhotoBean record);

  List<PhotoBean> selectByPrimaryKeys(String[] idArray);

  int deletePhotoByIds(String[] idArray);

  /**
   * 查询不通业务材料类型图片
   *
   * @param businessId 业务id
   * @param materialsId 材料id
   * @return
   */
  List<PhotoBean> selectByBusinessIdAndMaterialsId(@Param("businessId") Integer businessId,
      @Param("materialsId") Integer materialsId);

  String selectById(@Param("id") String id, @Param("field") String field);

  /**
   * 根据业务id 和图片类型查询图片
   *
   * @param businessId 业务id
   * @param pictureType 图片类型
   * @return
   */
  List<PhotoBean> selectByBusinessIdAndPictureType(@Param("businessId") Integer businessId,
      @Param("pictureType") Integer pictureType);

    List<PhotoBean> selectByBusinessIdAndPictureTypeList(@Param("businessId") Integer businessId,
                                                         @Param("pictureTypeList") List<Integer> pictureTypeList);

  /**
   * 根据业务id查询业务图片
   * @param businessId
   * @return
   */
  List<PhotoBean> selectByBusinessId(@Param("businessId") Integer businessId);
}
