package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.UnattendedCause;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: UnattendedCauseDao
 *
 * @author quboka
 * @Description: 未处理原因
 * @date 2018年4月10日
 */
public interface UnattendedCauseDao {

  /**
   * 插入业务和未处理原因的关联
   *
   * @param businessId 业务id
   * @param causeIdArray 未处理原因
   * @return
   */
  int insertBusinessUnattendedRel(@Param("businessId") Integer businessId,
      @Param("array") String[] causeIdArray);

  List<UnattendedCause> getCause(@Param("businessInfoId") Integer businessId);

}
