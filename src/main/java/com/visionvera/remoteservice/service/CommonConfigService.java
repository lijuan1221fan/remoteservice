package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.request.MeetingConfigRequest;
import java.util.List;
import java.util.Map;

/***
 * 通用配置业务接口
 * @author wangrz
 * @date 2017年12月21日16:18:21
 *
 */
public interface CommonConfigService {

  /***
   * 根据名称查询指定值
   * @param commonName
   * @return
   */
  CommonConfigBean getCommonConfigByName(String commonName);

  /***
   * 根据名称查询指定值
   * @param commonName
   * @return
   */
  List<CommonConfigBean> findCommonConfigByName(String commonName);

  /**
   * @param paramsMap
   * @return Map<String   ,   Object>
   * @Description: 新增
   * @author quboka
   * @date 2017年12月26日
   */
  Map<String, Object> add(Map<String, Object> paramsMap);

  /**
   * @param id
   * @return Map<String   ,   Object>
   * @Description: 删除
   * @author quboka
   * @date 2017年12月26日
   */
  Map<String, Object> delete(Integer id);

  /**
   * @param commonConfigByName
   * @return Map<String   ,   Object>
   * @Description: 修改
   * @author quboka
   * @date 2017年12月26日
   */
  Map<String, Object> update(CommonConfigBean commonConfigByName);

  /**
   * @param paramsMap
   * @param version
   * @return Map<String   ,   Object>
   * @Description: 批量修改
   * @author quboka
   * @date 2018年1月3日
   */
  Map<String, Object> updateAll(Map<String, Object> paramsMap);

  /**
   * @return Map<String   ,   Object>
   * @Description: 批量查询
   * @author quboka
   * @date 2018年1月3日
   */
  Map<String, Object> getList();

  /**
   * @param params 参数
   * @return Map<String   ,   Object> 返回值
   * @Description: 会管信息验证
   * @author lijintao
   * @date 2018年7月5日
   */
  Map<String, Object> checkMeetingConfig(MeetingConfigRequest meetingConfigRequest);

    /**
     * 切换系统版本
     *
     * @param version
     * @return
     */
    Map<String, Object> changeVersion(Integer version);
}
