package com.visionvera.app.dao;

import com.visionvera.app.entity.AppointmentMaterials;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentMaterialsDao {

  /**
   * 上传材料
   */
  int insertSelective(AppointmentMaterials record);

  /**
   * 查询材料
   */
  List<AppointmentMaterials> selectByAppointmenId(Integer appointId);

  /**
   * 删除材料
   */
  int updateByPrimaryKey(@Param("id") Integer id);
  
}
