package com.visionvera.app.service.impl;

import com.visionvera.app.dao.AppointmentInfoDao;
import com.visionvera.app.entity.AppointmentInfo;
import com.visionvera.app.service.AppRecordService;
import com.visionvera.common.enums.AndroidBusinessTypeEnum;
import com.visionvera.common.enums.QueueTypeEnum;
import com.visionvera.remoteservice.service.NumberService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2019/3/18
 */
@Service("appRecordService")
public class AppRecordServiceImpl implements AppRecordService {

  @Resource
  private AppointmentInfoDao appointmentInfoDao;
  @Resource
  private NumberService numberService;

  @Override
  public Map<String, Object> getNumber(String idCardNo, String serviceKey,String androidBusinessType) throws ParseException {
    //根据身份证查询当天是否有预约记录
    SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    String toDay = sdfDay.format(System.currentTimeMillis());
    List<AppointmentInfo> appointmentInfoList = appointmentInfoDao.selectByIdCardNoToDay(idCardNo, toDay);
    //size大于0  说明当天有预约记录
    if(appointmentInfoList.size()>0){
      SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String currentTime = sdfTime.format(System.currentTimeMillis());
      //判断当前时间段是否有预约记录
      AppointmentInfo appointmentInfo = appointmentInfoDao.selectByIdCardNo(idCardNo,currentTime);
      if(appointmentInfo == null){
        String dateTime = sdfDay.format(sdfTime.parse(appointmentInfoList.get(0).getStartTime()));
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
        String startHour = sdfHour.format(sdfTime.parse(appointmentInfoList.get(0).getStartTime()));
        String endHour = sdfHour.format(sdfTime.parse(appointmentInfoList.get(0).getEndTime()));
        return ResultUtils.error("您预约的办理时间为:" + dateTime + " " + startHour + "-" + endHour + ",请确认您的预约时间是否正确");
      }
      if (!serviceKey.equals(appointmentInfo.getServiceKey())) {
        return ResultUtils.error("您未在该中心进行预约操作，请确认预约中心地址");
      }
      return numberService.takeBusinessNumberAndModify(appointmentInfo.getServiceKey(),
              appointmentInfo.getBusinessType(), QueueTypeEnum.Booking.getValue(),appointmentInfo.getId(),androidBusinessType);

    }
    return ResultUtils.error("501");
  }

}
