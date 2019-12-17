package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.RegionbBean;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: RegionbDao
 *
 * @author quboka
 * @Description: 行政区域
 * @date 2017年12月19日
 */
public interface RegionbDao {

  /**
   * @param pid
   * @return List<RegionbBean>
   * @Description: 行政区域列表
   * @author quboka
   * @date 2017年12月19日
   */
  List<RegionbBean> getList(String pid);

  /**
   * @param name
   * @return
   * @Description: 通过行政区域名称获取行政区域id
   * @author zhangkai
   * @date 2018年1月17日下午6:25:47
   */
  String getId(String name);
}
