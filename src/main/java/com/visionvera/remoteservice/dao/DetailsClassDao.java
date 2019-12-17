package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.BusinessDetailsClass;
import java.util.List;
import java.util.Map;

/**
 * ClassName: DetailsClassDao
 *
 * @author quboka
 * @Description: 业务详细分类
 * @date 2018年5月8日
 */
public interface DetailsClassDao {

  /**
   * @param businessType 业务类型。  10011 ：婚姻变更
   * @return List<BusinessDetailsClass>
   * @Description: 业务获取详情类别列表
   * @author quboka
   * @date 2018年5月8日
   */
  List<BusinessDetailsClass> getClassificationList(
      Map<String, Object> paramsMap);

}
