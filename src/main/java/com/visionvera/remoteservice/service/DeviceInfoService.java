package com.visionvera.remoteservice.service;

import com.github.pagehelper.PageInfo;
import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.bean.UpdateDeviceInfo;
import com.visionvera.remoteservice.request.GetDeviceListRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhanghui
 * @since 2018-05-25
 */
public interface DeviceInfoService {

  /**
   * 批量删除接口
   *
   * @param deviceIds 设备id 逗号分割
   * @return
   */
  int deleteDeviceInfo(String deviceIds);

  /**
   * 根据服务中心和打印机类型查询打印机
   *
   * @param serviceKey 服务中心key
   * @param printerType 打印机类型
   * @return
   */
  DeviceInfo selectByServiceKeyAndPrinterType(String serviceKey, Integer printerType);

  /**
   * 根据id查询设备
   *
   * @param deviceId
   * @return
   */
  DeviceInfo selectById(Integer deviceId);

  /**
   * 保存设备
   *
   * @param deviceInfo
   * @return
   */
  int save(DeviceInfo deviceInfo);

  /**
   * 同步叫号机
   *
   * @param deviceInfo
   * @return
   */
  int syncDevice(DeviceInfo deviceInfo);

  /**
   * 更新设备
   *
   * @param deviceInfo
   * @return
   */
  int update(UpdateDeviceInfo deviceInfo);

  /**
   * 查询设备列表
   *
   * @param entity
   * @param pageNum
   * @param pageSize
   * @return
   */
  PageInfo<DeviceInfo> queryListPage(GetDeviceListRequest getDeviceListRequest);
}
