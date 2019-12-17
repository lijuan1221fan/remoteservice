package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.remoteservice.bean.RegionbBean;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.dao.RegionbDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.service.RegionbService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: RegionbServiceImol
 *
 * @author quboka
 * @Description: 行政区域
 * @date 2017年12月19日
 */
@Service
public class RegionbServiceImpl implements RegionbService {

  private final Logger logger = LoggerFactory.getLogger(RegionbServiceImpl.class);
  @Resource
  private RegionbDao regionbDao;
  @Resource
  private ServiceCenterDao serviceCenterDao;


  /**
   * 行政区域列表
   */
  @Override
  public List<RegionbBean> getList(String pid) {
    return regionbDao.getList(pid);
  }

  /**
   * 通过行政区域名称获取行政区域id
   */
  @Override
  public Map<String, Object> getId(String name) {
    String id = regionbDao.getId(name);
    if (id == null) {
      logger.info("查询行政区域id为空");
      return ResultUtils.ok("查询行政区域id为空");
    }
    logger.info("查询行政区域id成功", id);
    return ResultUtils.ok("查询行政区域id成功", id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> getRegionTree(String regionId, String serviceName, Integer pageNum,
      Integer pageSize) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    ServiceCenterBean bean = new ServiceCenterBean();
    bean.setGradeId(4);
    List<RegionbBean> regionList = regionbDao.getList(regionId);
    List<ServiceCenterBean> centerList = serviceCenterDao.getCenterListByParams(bean);
    //qbk 审批中心
    String[] regionIds = new String[]{};
    if (centerList != null && centerList.size() > 0) {
      ServiceCenterBean serviceCenterBean = centerList.get(0);
      String regionIdAll = serviceCenterBean.getRegionId();
      regionIds = regionIdAll.split("-");
    }
    List<Map<String, Object>> changeList = (List<Map<String, Object>>) JSON
        .parse(JSON.toJSONString(centerList));
//		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
    for (Map<String, Object> map : changeList) {
      map.put("name", map.get("serviceName"));
//	    	resultList.add(map);
    }
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    bean.setServiceName(serviceName);
    bean.setRegionKey(regionId);
//		bean.setGradeId(4);
    List<ServiceCenterBean> centerPageList = serviceCenterDao.getCenterListByParams(bean);
    PageInfo<ServiceCenterBean> pageInfo = new PageInfo<ServiceCenterBean>(centerPageList);
    resultMap.put("regionList", regionList);
    if (centerPageList.size() > 0) {
      resultMap.put("centerList", changeList);
    } else {
      resultMap.put("centerList", centerPageList);
    }
    resultMap.put("pageInfo", pageInfo);
    resultMap.put("regionIds", regionIds);
    return resultMap;
  }

}
