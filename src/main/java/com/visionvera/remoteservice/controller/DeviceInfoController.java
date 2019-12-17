package com.visionvera.remoteservice.controller;

import com.github.pagehelper.PageInfo;
import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.bean.UpdateDeviceInfo;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.request.GetDeviceListRequest;
import com.visionvera.remoteservice.service.DeviceInfoService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备
 *
 * @author lijintao
 * @since 2018-08-22
 */
@RestController
@RequestMapping("deviceInfo")
public class DeviceInfoController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Resource
  private DeviceInfoService deviceInfoService;


  /**
   * 获取设备列表
   *
   * @param pageNum 页码
   * @param pageSize 每页数量
   * @param entity 查询参数
   * @return 返回值
   */
  @RequiresPermissions("device:query")
  @PostMapping("getListPage")
  public Map<String, Object> getListPage(GetDeviceListRequest getDeviceListRequest) {
    PageInfo<DeviceInfo> pageInfo = deviceInfoService
        .queryListPage(getDeviceListRequest);
    return ResultUtils.ok("查询列表成功", pageInfo);

  }


  /**
   * 根据id查询设备
   *
   * @param deviceId 设备id
   * @return 返回值
   */
  @RequiresPermissions("device:info")
  @GetMapping("getDeviceDetail")
  public Map<String, Object> getDeviceDetail(@RequestParam(value = "deviceId") Integer deviceId) {
    try {
      DeviceInfo deviceInfo = deviceInfoService.selectById(deviceId);
      return ResultUtils.check(deviceInfo);
    } catch (Exception e) {
      logger.error("查询设备详情信息异常", e);
      return ResultUtils.error("查询设备详情信息异常");
    }
  }

  /**
   * 新增设备
   *
   * @param deviceInfo 设备对象
   * @return 返回值
   */
  @SysLogAnno("新增设备")
  @RequiresPermissions("device:add")
  @RequestMapping("addDeviceInfo")
  public Map<String, Object> addDeviceInfo(@RequestBody DeviceInfo deviceInfo) {

    try {
      int count = deviceInfoService.save(deviceInfo);
      if (count == 0) {
        return ResultUtils.error("保存失败");
      }
      return ResultUtils.ok("保存成功");
    } catch (MyException e) {
      logger.error("保存异常。", e.getMsg());
      return ResultUtils.error(e.getMsg());
    }

  }


  /**
   * 根据id批量删除设备
   *
   * @param deviceIds
   * @return 返回值
   */
  @SysLogAnno("删除设备")
  @RequiresPermissions("device:delete")
  @RequestMapping("delDeviceInfo")
  public Map<String, Object> delDeviceInfo(@RequestParam(value = "deviceIds") String deviceIds) {

    try {
      int count = deviceInfoService.deleteDeviceInfo(deviceIds);
      if (count == 0) {
        return ResultUtils.error("删除失败");
      }
      return ResultUtils.ok("删除成功");
    } catch (Exception e) {
      logger.error("删除异常", e);
      return ResultUtils.error("删除异常");
    }

  }

  /**
   * 更新设备
   *
   * @param deviceInfo 设备对象
   * @return 返回值
   */
  @SysLogAnno("修改设备")
  @RequiresPermissions("device:edit")
  @RequestMapping("updateDeviceInfo")
  public Map<String, Object> updateDeviceInfo(@RequestBody UpdateDeviceInfo deviceInfo) {

    try {
      int count = deviceInfoService.update(deviceInfo);
      if (count == 0) {
        return ResultUtils.error("修改失败");
      }
      return ResultUtils.ok("修改成功");
    } catch (MyException e) {
      logger.error("修改异常", e.getMsg());
      return ResultUtils.error(e.getMsg());
    }

  }

  /**
   * 获取当前机器所有打印机
   *
   * @return 返回值
   */
  @RequestMapping("getPrintServiceList")
  public Map<String, Object> getPrintServiceList() {

    try {
      // 构建打印请求属性集
      HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
      // 设置打印格式，因为未确定类型，所以选择autosense
      DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
      // 查找所有的可用的打印服务
      PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
      List<String> list = new ArrayList<>();
      for (PrintService printService : printServices) {
        list.add(printService.getName());
      }
      logger.info("获取当前机器所有打印机");
      return ResultUtils.ok("获取打印机服务名称列表成功", list);
    } catch (Exception e) {
      logger.error("获取打印机服务名称列表异常", e);
      return ResultUtils.error("获取打印机服务名称列表异常");
    }

  }
}

