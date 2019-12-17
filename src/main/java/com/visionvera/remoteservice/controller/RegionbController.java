package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.bean.RegionbBean;
import com.visionvera.remoteservice.service.RegionbService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: RegionbController
 *
 * @author quboka
 * @Description: 行政区域
 * @date 2017年12月19日
 */
@RestController
@RequestMapping("/regionb")
public class RegionbController {

  private final Logger logger = LoggerFactory.getLogger(RegionbController.class);

  @Resource
  private RegionbService regionbService;

  /**
   * @param pid
   * @return Map<String   ,   Object>
   * @Description: 行政区域列表
   * @author quboka
   * @date 2017年12月19日
   */
  @RequestMapping("getList")
  public Map<String, Object> getList(
      @RequestParam(value = "pid", defaultValue = "000000000000") String pid) {
    List<RegionbBean> list = regionbService.getList(pid);
    return ResultUtils.ok("查询行政区域列表成功。", list);
  }

  @RequestMapping(value = "getId")
  public Map<String, Object> getId(String name) {
    return regionbService.getId(name);
  }


  /**
   * @param pid
   * @return Map<String   ,   Object>
   * @Description: 行政区域列表树
   * @author zhanghui
   * @date 2018年5月25日
   */
  @RequestMapping("getRegionTree")
  public Map<String, Object> getRegionTree(
      @RequestParam(value = "regionId", defaultValue = "000000000000") String regionId,
      @RequestParam(value = "serviceName", defaultValue = "") String serviceName,
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize
  ) {
    Map<String, Object> map = regionbService
        .getRegionTree(regionId, serviceName, pageNum, pageSize);
    return ResultUtils.ok("查询行政区域列表树成功", map);
  }
}
