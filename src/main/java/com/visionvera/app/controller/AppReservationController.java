package com.visionvera.app.controller;

import com.visionvera.app.entity.AppointmentInfo;
import com.visionvera.app.group.BusinessDetailInfoGroup;
import com.visionvera.app.group.ReservationGroup;
import com.visionvera.app.group.ReservationRecordGroup;
import com.visionvera.app.pojo.AppParaVo;
import com.visionvera.app.pojo.AppReservationVo;
import com.visionvera.app.service.AppReservationService;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.service.SysDeptService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 *
 * @ClassName: AppReservationController
 * @Author: ljfan
 * @Date: 2019-03-19 14:57
 * @Version: V1.0
 */
@RestController
@RequestMapping("/app")
public class AppReservationController {
  @Resource
  AppReservationService appReservationService;
  @Resource
  SysDeptService sysDeptService;

  /**
   *根据查询条件取得服务中心列表
   */
  @PostMapping("serviceCenterList")
  public Map<String, Object> serviceCenterList(@RequestBody AppReservationVo appReservationVo) {
    return appReservationService.getServiceCenter(appReservationVo);
  }
  /**
   * 取得部门列表
   */
  @PostMapping("departmentList")
  public Map<String, Object> departmentList() {
    return sysDeptService.getSysDeptListNotInDelete();
  }
  /**
   *根据村级服务中心serviceKey和部门id DeptId 取得业务类别列表
   */
  @PostMapping("getBusinessTypeList")
  public Map<String, Object> getBusinessTypeList(@RequestBody AppParaVo appParaVo) {
    ValidateUtil.validate(appParaVo, ReservationGroup.class);
   return  appReservationService.getBusinessesTypeList(appParaVo.getDeptId(),appParaVo.getServiceKey());
  }
  /**
   *
   */
  @GetMapping("businessDetailList")
  public Map<String, Object> businessDetailList(@RequestParam("businessTypeId")Integer businessTypeId) {
    AssertUtil.isNull(businessTypeId,"业务类型id不能为空");
    List<BusinessTypeBean> list= appReservationService.getBusinessTypeInfoList(businessTypeId);
    return ResultUtils.check("查询成功", list);
  }
  /**
   * 根据业务详情id 获取业务详细详细
   */
  @PostMapping("businessDetailInfo")
  public Map<String, Object> BusinessDetailInfo(@RequestBody AppParaVo appParaVo) {
    ValidateUtil.validate(appParaVo, BusinessDetailInfoGroup.class);
    return appReservationService.getBusinessDetailInfoById(appParaVo);
  }

  /**
   * 1。业务详情中设置上午与下午的限制数
   * 2。每次预约成功一个号，预约表纪录当前预约累积号，将当前累积号与限制号比较，如果小于限制号，则保存数据，若大于限制号，则提示"号已满，请选择其他时间"
   * 3。当天取号，先拿到预约最大号，在最大号的基础上依次递增
   * @param appointmentInfo
   * @return
   */
  @PostMapping("reservationNumber")
  public Map<String,Object> reservationNumber(@RequestBody AppointmentInfo appointmentInfo){
    ValidateUtil.validate(appointmentInfo,ReservationGroup.class);
    return appReservationService.saveAppointmentInfo(appointmentInfo);
  }

  /**
   * 1.预约纪录
   */
  @PostMapping("reservationRecord")
  public Map<String,Object> reservationRecord(@RequestBody AppParaVo appParaVo){
    ValidateUtil.validate(appParaVo, ReservationRecordGroup.class);
    if(StringUtil.isEmpty(appParaVo.getNowDay()) && StringUtil.isEmpty(appParaVo.getOldDay())){
        return ResultUtils.error("时间参数不能为空");
    }
    return appReservationService.reservationRecord(appParaVo);
  }
  /**
   * 取消预约
   */
  @GetMapping("cancleReservation")
  public Map<String,Object> cancleReservation(Integer appointMentId){
      AssertUtil.isNull(appointMentId,"预约主键不能为空");
      return appReservationService.cancellation(appointMentId);
  }
}
