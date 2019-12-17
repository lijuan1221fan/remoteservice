package com.visionvera.remoteservice.common.timer;

import com.visionvera.app.dao.AppointmentInfoDao;
import com.visionvera.common.enums.MeetingStateEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.MeetingOperationDao;
import com.visionvera.remoteservice.dao.NumberDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.service.MeetingService;
import com.visionvera.remoteservice.service.NumberService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


/**
 * ClassName: DailyTimer
 * @author quboka
 * @Description: 每天的定时器
 * @date 2018年3月29日
 */
@Component
@Lazy(value = false)
public class DailyTimer {
  private Logger logger = LogManager.getLogger(getClass());
  @Resource
  private NumberDao numberDao;
  @Resource
  private VcDevDao vcDevDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private AppointmentInfoDao appointmentInfoDao;
  @Resource
  private MeetingOperationDao meetingOperationDao;
  @Resource
  private MeetingService meetingService;
  @Resource
  private RedisUtils redisUtils;
  @Value("${maintenance.time}")
  private boolean maintenanceTime;
  @Value("${maintenance.process}")
  private boolean maintenanceProcess;
  @Value("${temp.save.docpath}")
  private String tempDocPath;
  @Resource
  private WindowDao windowDao;
  @Resource
  private NumberService numberService;

  @Scheduled(cron = "${maintenance.cron}")
  public void task() {
    if (maintenanceTime) {
      logger.info("每天定时器执行：" + new Date());
      try {
        logger.info("调度开始");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历    中午
        //号码归零
        numberDao.updateNumberIsZero();
        //删除队列
        redisUtils.removePattern(CommonConstant.QUEUE_KEY + "*");
        //删除业务员等待队列
        redisUtils.removePattern(CommonConstant.WAITING_WORK_KEY + "*");
        //将所有未处理和处理中号码做过号处理
        List<BusinessInfo> businessInfoList = businessInfoDao.getAllWaitingAndDoingBusiness();
        businessInfoDao.passNumber();
        if(!CollectionUtils.isEmpty(businessInfoList)){
          for (BusinessInfo businessInfo : businessInfoList) {
            numberService.sendMessageAll(businessInfo.getServiceKey(), businessInfo.getDeptId());
          }
        }
        //将在线业务员更新为离线、空闲中
        sysUserDao.updateSysUserState();
        //将忙碌窗口改为空闲状态
        windowDao.setWindowFreeForAll();
        //重置状态
        vcDevDao.resetTheState();
        //删除冗余文件
        logger.info("删除冗余doc文件");
        UrlFileUtil.delAllFile(tempDocPath);
        //预约号过期处理
        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
        Date dBefore = calendar.getTime(); //得到前一天的时间
        Integer updateNum = appointmentInfoDao.passNumber(format.format(dBefore));
        logger.info("过去记录条数："+updateNum);
      } catch (Exception e) {
        logger.error("每天定时器错误", e);
      }
    }
  }
  //中午处理上午已过号数据
  @Scheduled(cron = "${maintenance.midday}")
  public void middayTask() {
    if (maintenanceProcess) {
      logger.info("每天中午定时器执行：" + new Date());
      try {
        logger.info("调度开始");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date processTime = new Date(); //得到前一天的时间
        Integer updateNum = appointmentInfoDao.processPassNumber(format.format(processTime));
        logger.info("过去记录条数："+updateNum);
      } catch (Exception e) {
        logger.error("每天定时器错误", e);
      }
    }
  }

  @Scheduled(cron = "${maintenance.cron}")
  public void stopMeetingTask() {
    if (maintenanceTime) {
      logger.info("断会定时器执行：" + new Date());
      try {
        //查找所有会议中的记录
        List<Integer> businessIdList = meetingOperationDao.selectByState(
                MeetingStateEnum.Meeting.getValue());
        if(!CollectionUtils.isEmpty(businessIdList)){
          for (Integer businessId : businessIdList) {
            Map<String, Object> map = meetingService.stopMeetingAndModify(businessId);
            if (!(boolean) map.get("result")) {
              logger.info("断会失败 " + businessId);
            }
          }
        }
      } catch (Exception e) {
        logger.error("断会定时器执行错误", e);
      }
    }
  }

}
