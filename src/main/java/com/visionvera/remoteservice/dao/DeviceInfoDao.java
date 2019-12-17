package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.bean.UpdateDeviceInfo;
import com.visionvera.remoteservice.request.GetDeviceListRequest;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lijintao
 * @date 2018-08-22
 */
public interface DeviceInfoDao {

  int deleteByPrimaryKey(Integer id);

  int insert(DeviceInfo record);

  int insertSelective(DeviceInfo record);

  DeviceInfo selectByPrimaryKey(Integer id);

  DeviceInfo getAndroidByDeviceCode(String deviceCode);

  int addAndroid(DeviceInfo deviceInfo);

  List<DeviceInfo> selectExistDevice(GetDeviceListRequest getDeviceListRequest);

  /**
   * 根据服务中心和打印机类型查询打印机
   *
   * @param serviceKey
   * @param printerType
   * @return
   */
  DeviceInfo selectByServiceKeyAndPrinterType(@Param("serviceKey") String serviceKey,
      @Param("printerType") Integer printerType);

  int updateByPrimaryKeySelective(UpdateDeviceInfo record);

  int updateByPrimaryKey(DeviceInfo record);

  int deleteDeviceInfo(String[] deviceIdArray);

  /**
   * 根据servicekey删除设备
   *
   * @param serviceKeys
   * @return int
   * @author quboka
   * @date 2018年7月5日
   */
  int deleteDeviceInfoByServiceIds(List<String> serviceKeys);

  /**
  　　* @Description: 根据serviceKeys查询设备
  　　* @author: xueshiqi
  　　* @date: 2018/11/28
  　　*/
  List<DeviceInfo> selectByServiceKeys(@Param("serviceKeys") List<String> serviceKeys);
}
