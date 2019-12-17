package com.visionvera.app.service;

import com.visionvera.app.entity.AppointmentInfo;
import com.visionvera.app.pojo.AppParaVo;
import com.visionvera.app.pojo.AppReservationVo;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassNameAppReservationService
 *description 预约取号
 * @author ljfan
 * @date 2019/03/19
 *version
 */
public interface AppReservationService {
  //取得所有的服务中心
 Map<String,Object> getServiceCenter(AppReservationVo appReservationVo);
  //取得所有的部门
 List<SysDeptBean> getDeptList();
  //根据部门、中心取得业务类别
 Map<String,Object> getBusinessesTypeList(@Param("deptId") Integer deptId,@Param("serviceKey") String serviceKey);
 //根据业务类别id取得业务详情列表
 List<BusinessTypeBean> getBusinessTypeInfoList(@Param("BusinessTypeId") Integer id);
 //根据业务详情id 取得业务详情明细
 Map<String,Object> getBusinessDetailInfoById(AppParaVo appParaVo);

 Map<String,Object> saveAppointmentInfo(AppointmentInfo appointmentInfo);

  //查询预约纪录
 Map<String,Object> reservationRecord(AppParaVo appParaVo);
 //取消预约
 Map<String,Object> cancellation(Integer appointmentId);
}
             
