package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.RegionbBean;
import java.util.List;
import java.util.Map;

/**
 * ClassName: RegionbService
 *
 * @author quboka
 * @Description: 行政区域
 * @date 2017年12月19日
 */
public interface RegionbService {

  /**
   * @param pid
   * @return List<RegionbBean>
   * @Description: 行政区域列表
   * @author quboka
   * @date 2017年12月19日
   */
  public List<RegionbBean> getList(String pid);

  /**
   * \
   *
   * @param name
   * @return
   * @Description: 通过行政区域名称获取行政区域id
   * @author zhangkai
   * @date 2018年1月17日下午6:26:52
   */
  Map<String, Object> getId(String name);

  /**
   * \
   *
   * @param regionId
   * @return
   * @Description: 行政区域树
   * @author zhanghui
   * @date 20185月24日
   */
  Map<String, Object> getRegionTree(String regionId, String serviceName, Integer pageNum,
      Integer pageSize);

}
