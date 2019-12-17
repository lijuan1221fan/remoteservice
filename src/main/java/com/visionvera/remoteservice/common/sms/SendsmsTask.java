package com.visionvera.remoteservice.common.sms;

import com.visionvera.api.handler.api.MeetingApiHandler;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.api.handler.utils.StringUtil;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.bean.SendSmsBean;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.Resource;

import com.visionvera.remoteservice.service.impl.InitializingBeanImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author ljfan
 * @ClassNameSendsmsTask 给业务员定时发送短信提醒
 * @date 2018/12/12 version
 */
@Component
public class SendsmsTask implements Runnable {
  private final Logger logger = LoggerFactory.getLogger(SendsmsTask.class);

  private String userName;
  private String phoneNumber;
  private String smsUrl;
  private String smsMessage;
  private Integer businessId;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    @Resource
  private RedisUtils redisUtils;

  @Override
  public void run() {
      sendMessage();
  }

  public void sendMessage() {
    try {
        if (StringUtil.isNotEmpty(smsUrl) && StringUtil.isNotEmpty(smsMessage)) {
          String ip = HttpUtil.getIpByUrl(smsUrl);
          Integer port = HttpUtil.getPortByUrl(smsUrl);
          logger.info("进入发送短信线程任务");
          //根据短信模版，替换业务员姓名
          String message = smsMessage.replace("xxx", userName);
          logger.info("message: " + message + phoneNumber);
          ResultEntity resultEntity = MeetingApiHandler.sendSms(ip, port, phoneNumber, message);
          InitializingBeanImpl.map.remove("smsThreadTask" + businessId);
          //redisUtils.remove("smsThreadTask" + businessId);
          logger.info("发送短信返回结果："+resultEntity.getResult()+"发送短信返回数据："+resultEntity.getData());
        } else {
          logger.info("请联系管理员配置短信模版，示例模版为:'【浙江省远程申办服务平台】尊敬的xxx，您当前有业务需要办理，请尽快处理！'");
        }
    } catch (Exception e) {
      logger.error("发送短信线程异常", e);
    }

  }

    /**
     * 取消给业务员发消息的线程任务
     * @param businessId
     */
  public void cancelSmsThreadTask(Integer businessId){
      ScheduledFuture future = (ScheduledFuture) InitializingBeanImpl.map.get("smsThreadTask" + businessId);
      //ScheduledFuture future = (ScheduledFuture) redisUtils.get("smsThreadTask" + businessId);
      if(future != null){
          InitializingBeanImpl.map.remove("smsThreadTask" + businessId);
          //redisUtils.remove("smsThreadTask" + businessId);
          boolean isDone = future.isDone();
          if(!isDone){
              future.cancel(Boolean.TRUE);
          }
      }
  }

}
