package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.CertificateBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: CertificateDao
 *
 * @author quboka
 * @Description: 证件
 * @date 2018年3月25日
 */
public interface CertificateDao {

  /**
   * @param certificateMap
   * @return int
   * @Description: 插入业务和证件关联
   * @author quboka
   * @date 2018年3月25日
   */
  int insertBusinessCertificateRel(Map<String, Object> certificateMap);

  /**
   * @param businessType 业务种类
   * @return Map<String   ,   Object>
   * @Description: 通过业务类型获取对应的证件类型
   * @author quboka
   * @date 2018年3月25日
   */
  List<CertificateBean> getCertificateByBusinessType(
      String businessType);

}
