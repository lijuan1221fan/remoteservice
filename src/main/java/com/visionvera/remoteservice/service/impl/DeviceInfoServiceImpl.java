package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.enums.DeviceTypeEnum;
import com.visionvera.common.enums.ServiceCenterTypeEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UpdateDeviceInfo;
import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.DeviceInfoDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.request.GetDeviceListRequest;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.DeviceInfoService;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhanghui
 * @since 2018-05-25
 */
@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {

  private static Logger logger = LoggerFactory.getLogger(DeviceInfoServiceImpl.class);

  @Resource
  private DeviceInfoDao deviceInfoDao;

  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private BusinessInfoService businessInfoService;


  @Resource
  private VcDevDao vcDevDao;


  /**
   * 批量删除接口
   *
   * @param deviceIds 设备id 逗号分割
   * @return
   */
  @Override
  public int deleteDeviceInfo(String deviceIds) {
    String[] ids = deviceIds.split(",");
    GetDeviceListRequest getDeviceListRequest=new GetDeviceListRequest();
    getDeviceListRequest.setIds(Arrays.asList(ids));
    List<DeviceInfo> deviceInfos=  deviceInfoDao.selectExistDevice(getDeviceListRequest);
    Integer res=  deviceInfoDao.deleteDeviceInfo(ids);
    if(res>0){
      //调用推送
      for(DeviceInfo deviceInfo:deviceInfos) {
        businessInfoService.SendMessageForAndroidByJHJ(deviceInfo.getServiceKey());
      }
      return res;
    }
    return 0;
  }

  /**
   * 根据服务中心和打印机类型查询打印机
   *
   * @param serviceKey 服务中心key
   * @param printerType 打印机类型
   * @return
   */
  @Override
  public DeviceInfo selectByServiceKeyAndPrinterType(String serviceKey, Integer printerType) {
    return deviceInfoDao.selectByServiceKeyAndPrinterType(serviceKey, printerType);
  }

  /**
   * 根据id查询设备
   *
   * @param deviceId
   * @return
   */
  @Override
  public DeviceInfo selectById(Integer deviceId) {
    return deviceInfoDao.selectByPrimaryKey(deviceId);
  }

  /**
   * 保存设备
   *
   * @param deviceInfo
   * @return
   */
  @Override
  public int save(DeviceInfo deviceInfo) {
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(deviceInfo.getServiceKey());
    if (serviceCenter == null) {
      throw new MyException("中心不存在");
    }
    if (serviceCenter.getType().equals(1L) || serviceCenter.getType().equals(2L)) {
      throw new MyException("统筹中心或审批中心暂不支持添加打印机");
    }
    //校验是否绑定过
    //1常用打印机 2申请单打印机
    if (StringUtil.isNotNull(deviceInfo.getPrinterType())) {
      if (deviceInfo.getPrinterType().equals(CommonConstant.COMMON_PRINTER.toString())) {
        DeviceInfo commonDeviceInfo = deviceInfoDao
            .selectByServiceKeyAndPrinterType(deviceInfo.getServiceKey(),
                CommonConstant.COMMON_PRINTER);
        if (commonDeviceInfo != null) {
          throw new MyException("该中心已绑定过常用打印机，请勿重新添加");
        }
      }
      if (deviceInfo.getPrinterType().equals(CommonConstant.RECEIPT_PRINTER.toString())) {
        DeviceInfo remoteDeviceInfo = deviceInfoDao
            .selectByServiceKeyAndPrinterType(deviceInfo.getServiceKey(),
                CommonConstant.RECEIPT_PRINTER);
        if (remoteDeviceInfo != null) {
          throw new MyException("该中心已绑定过申请单打印机，请勿重新添加");
        }
      }
    }
    return deviceInfoDao.insertSelective(deviceInfo);
  }

  @Override
  public int syncDevice(DeviceInfo device) {
    // 校验设备类型
    if (!Arrays.asList(DeviceTypeEnum.AndroidDevice).contains(device.getType())) {
      throw new MyException("终端类型错误");
    }
    // 校验服务中心
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(device.getServiceKey());
    if (serviceCenter == null) {
      throw new MyException("服务中心不可用");
    }

    if (!serviceCenter.getType().equals(Long.valueOf(ServiceCenterTypeEnum.SERVER.getType()))) {
      throw new MyException("所选不是服务中心");
    }
    //校验所选服务中心是否绑定终端
    List<VcDevBean> deviceList = vcDevDao.getDeviceByServiceKey(device.getServiceKey());
    if(CollectionUtils.isEmpty(deviceList)){
      throw new MyException("所选中心未绑终端");
    }

    // 设备名，别名，地址
    device.setPrivateName(serviceCenter.getServiceName());
    device.setDeviceName(
        serviceCenter.getServiceName() + "-" + DeviceTypeEnum.typeToEnum(device.getType())
            .getName());
    device.setAddress(serviceCenter.getAddress());
    // 状态可用
    device.setState(StateEnum.Effective.getValue());
    //校验是否存在
    DeviceInfo byDeviceCode = deviceInfoDao.getAndroidByDeviceCode(device.getDeviceCode());
    if (byDeviceCode != null) {
      // 存在更新
      device.setId(byDeviceCode.getId());
      device.setCreateTime(byDeviceCode.getCreateTime());
      device.setUpdateTime(new Date());
      logger.info("更新一体机 设备编码: "+ device.getDeviceCode());
      return deviceInfoDao.updateByPrimaryKey(device);
    }
    // 不存在新增
    logger.info("新增一体机  设备编码: "+ device.getDeviceCode());
    return deviceInfoDao.addAndroid(device);
  }

  /**
   * 更新设备
   *
   * @param deviceInfo
   * @return
   */
  @Override
  public int update(UpdateDeviceInfo deviceInfo) {
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(deviceInfo.getServiceKey());
    if (serviceCenter == null) {
      throw new MyException("中心不存在");
    }
    if (serviceCenter.getType().equals(1L) || serviceCenter.getType().equals(2L)) {
      throw new MyException("统筹中心暂或审批中心暂不支持添加打印机");
    }
    //校验是否绑定过
    //1常用打印机 2申请单打印机
    if (deviceInfo.getPrinterType().equals(CommonConstant.COMMON_PRINTER.toString())) {
      DeviceInfo commonDeviceInfo = deviceInfoDao
          .selectByServiceKeyAndPrinterType(deviceInfo.getServiceKey(),
              CommonConstant.COMMON_PRINTER);
      if (commonDeviceInfo != null) {
        if (!deviceInfo.getId().equals(commonDeviceInfo.getId())) {
          throw new MyException("该中心已绑定过常用打印机，请勿重新添加");
        }
      }
    }
    if (deviceInfo.getPrinterType().equals(CommonConstant.RECEIPT_PRINTER.toString())) {
      DeviceInfo remoteDeviceInfo = deviceInfoDao
          .selectByServiceKeyAndPrinterType(deviceInfo.getServiceKey(),
              CommonConstant.RECEIPT_PRINTER);
      if (remoteDeviceInfo != null) {
        if (!deviceInfo.getId().equals(remoteDeviceInfo.getId())) {
          throw new MyException("该中心已绑定过申请单打印机，请勿重新添加");
        }
      }
    }
    return deviceInfoDao.updateByPrimaryKeySelective(deviceInfo);
  }

  /**
   * 查询设备列表
   *
   * @param getDeviceListRequest
   * @param pageNum
   * @param pageSize
   * @return
   */
  @Override
  public PageInfo<DeviceInfo> queryListPage(GetDeviceListRequest getDeviceListRequest) {
    Integer pageNum =getDeviceListRequest.pageNum;
    Integer pageSize =getDeviceListRequest.pageSize;
    List<String> serviceKeys = null;
    SysUserBean userBean = ShiroUtils.getUserEntity();
    //业务中心校验
    //1.当为admin账号时,拿所有数据
    //2.当为统筹管理员时，显示该账号及子中心的所有数据
    if(SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType())){
      //取得统筹中心及审批中心下的打印机
      String parentKey = userBean.getServiceKey();
      List<String> centerBeans  = serviceCenterDao.getServiceKeysByParentKey(parentKey);
      if(centerBeans.size()>0){
        serviceKeys = new ArrayList<>();
        serviceKeys.addAll(centerBeans);
      }
    }
    //3.当为审批管理员时，获取自己中心的打印机
    if(SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType())) {
      //查询列表：获取自己中心的打印机
      serviceKeys = new ArrayList<>();
      List<String> centerBeans = serviceCenterDao.getServiceKeysByParentServiceKey(userBean.getServiceKey());
      serviceKeys.addAll(centerBeans);
    }
    getDeviceListRequest.setServiceKey(null);
    getDeviceListRequest.setServiceKeys(serviceKeys);
    PageHelper.startPage(pageNum, pageSize);
    List<DeviceInfo> deviceInfos = deviceInfoDao.selectExistDevice(getDeviceListRequest);
    return new PageInfo<>(deviceInfos);
  }
}
