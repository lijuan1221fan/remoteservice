package com.visionvera.app.dao;

import com.visionvera.app.entity.AppointmentInfo;
import com.visionvera.app.entity.AppointmentMaterials;
import com.visionvera.app.pojo.AppParaVo;
import com.visionvera.app.pojo.AppUserVo;
import com.visionvera.app.pojo.ReservationBean;
import com.visionvera.remoteservice.bean.Materials;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author lijintao
 */
public interface AppointmentInfoDao {

  int deleteByPrimaryKey(Integer id);

  int insert(AppointmentInfo record);

  int insertSelective(AppointmentInfo record);

  AppointmentInfo selectByPrimaryKey(Integer id);
  int updateByPrimaryKeySelective(AppointmentInfo record);

  int updateByPrimaryKey(AppointmentInfo record);

  /**
   * jlm 根据身份证号查询预约记录
   */
  AppointmentInfo selectByIdCardNo(String idCardNo,String currentTime);

  /**
   * 根据身份证查询当天是否有预约记录
   * @param idCardNo
   * @param toDay
   * @return
   */
  List<AppointmentInfo> selectByIdCardNoToDay(String idCardNo,String toDay);

  /**
   * 根据业务详情id取得已预约的所有号
   * @param businessType
   * @return
   */
  List<AppointmentInfo> getAppointmentInfoListByBusinessDetailId(Integer businessType);

  /**
   * 校验预约号是不是已存在
   * @param appointmentInfo
   * @return
   */
  List<AppointmentInfo> checkNumber(AppointmentInfo appointmentInfo);

  /**
   * 校验预约号是否已满
   * @param appointmentInfo
   * @return
   */
  AppointmentInfo checkFull(AppointmentInfo appointmentInfo);
  /**
   * 获取预约纪录
   */
  List<ReservationBean> effectiveReservation(AppParaVo appParaVo);
  /**
   * 根据预约id 查询预约关联材料
   */
  List<AppointmentMaterials> reservationMaterail(Integer id);
  /**
   * 过期号处理
   */
  Integer passNumber(String date);
  /**
   *根据预约业务类别查询需要上传的业务材料
   */
  List<Materials> getMaterialsByappointmentId(Integer businessDetailId);

  /**
   * 根据材料id查询预约上传材料列表
   */
  List<AppointmentMaterials> getAppointmentMaterial(AppointmentMaterials appointmentMaterials);

  int updateState(@Param("id") Integer id);

  /**
   * 中午过号处理
   */
  Integer processPassNumber(String midday);

  Integer updateIdCardNoByAppUserId(AppUserVo appUserVo);
}

