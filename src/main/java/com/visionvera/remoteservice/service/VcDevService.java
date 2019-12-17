package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.pojo.VcDevVo;
import java.util.Map;

/**
 * ClassName: VcDevService
 *
 * @author quboka
 * @Description: 会管终端
 * @date 2018年5月17日
 */
public interface VcDevService {

  /**
   * @return Map<String       ,       Object>
   * @Description: 同步会管终端
   * @
   * @author quboka
   * @date 2018年5月17日
   */
  Map<String, Object> synDeviceAndModify();

  /**
   * @param serviceKey 服务中心key
   * @param userId 用户id
   * @param form 形态 1：村终端 2：镇终端
   * @param deviceId 终端id
   * @return Map<String       ,       Object>
   * @Description: 绑定终端
   * @
   * @author quboka
   * @date 2018年5月21日
   */
  Map<String, Object> bindingDeviceAndModify(VcDevVo vcDevVo);

  /**
   * @param deviceId
   * @return Map<String       ,       Object>
   * @Description: 解绑终端
   * @author quboka
   * @date 2018年5月21日
   */
  Map<String, Object> unbindDeviceAndModify(String deviceId);


  /**
   * @param pageNum
   * @param pageSize
   * @param id 终端id 终端号码
   * @param name 终端名称
   * @param serviceKey 服务中心名称
   * @return Map<String       ,       Object>
   * @Description: 管理端查询终端列表
   * @author quboka
   * @date 2018年5月21日
   */
  Map<String, Object> getDeviceList(Map<String, Object> paramsMap);

  /**
   * @Description: 超管获取终端
   * @author jlm
   * @date 2018年11月7日
   */
  Map<String, Object> superGetDevList(Map<String, Object> paramsMap);

  /**
   * 查询终端详情
   *
   * @param id
   * @return
   */
  VcDevBean getDeviceDetails(String id);

  /**
   * 修改终端信息
   *
   * @param deviceId
   * @param ip
   * @param type
   * @return
   */
  Map<String, Object> updateDeviceDetail(String deviceId, String ip, Integer type);

  /**
   * 根据ip查询终端
   *
   * @param ip
   * @return
   */
  VcDevBean getDeviceByIp(String ip);

  /**
   * 将终端置为空闲状态
   *
   * @param deviceId
   * @return
   */
  int setIdle(String deviceId);

  /**
   * scu设备重启
   * @param deviceId
   * @return
   */
  Map<String, Object> scuRestart(String deviceId);
  /**
   * scu设备状态获取
   * @param deviceId
   * @return
   */
  Map<String, Object> getScuAllState(String deviceId);
}
