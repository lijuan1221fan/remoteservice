package com.visionvera.remoteservice.ws;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.visionvera.common.enums.AndroidShowTypeEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.WebSocketBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 功能描述:
 *
 * @ClassName: WebSocketController
 * @Author: ljfan
 * @Date: 2019-03-11 11:22
 * @Version: V1.0
 */
@Component
public class AndroidWebSocketHandler implements WebSocketHandler {
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private BusinessInfoService businessInfoService;
  private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(AndroidWebSocketHandler.class);

  @Override
  public void afterConnectionEstablished(WebSocketSession session){
    logger.info("叫号机：建立连接");
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
    //异常断开连接
    removeRedistValue(session,"");
    if(session.isOpen()){
      try{
        session.close();
      } catch (IOException e){
        logger.info("叫号机连接关闭异常"+e.getMessage());
      }
    }
  }

  /**
   * socket断开连接时，redis中的数据移除
   * @param session
   */
  private void removeRedistValue(WebSocketSession session,String deviceCode){
    List<ServiceCenterBean> centerBean = serviceCenterDao.getChirdServiceList();
    if(CollectionUtils.isNotEmpty(centerBean)){
      for(ServiceCenterBean bn: centerBean){
        String queueKey = CommonConstant.WEBSOCKET_KEY + bn.getServiceKey();
        List<String> queueVal = redisUtils.listRangeAllStr(queueKey);
        if(CollectionUtils.isNotEmpty(queueVal)){
           for(String val : queueVal){
               String[] valSplit = val.split(":");
               if(valSplit.length>2){
               if(valSplit[2].equals(session.getId())){
                 String queueValue = valSplit[0]+":"+valSplit[1]+":"+session.getId()+":"+valSplit[3];
                 redisUtils.listRemove(queueKey,0,queueValue);
                 return;
               }
               }
               if(valSplit[1].equals(deviceCode)){
                 //如果已存在连接，先移除
                 redisUtils.listRemove(queueKey,0,val);
                 return;
               }
           }
        }

      }
    }

  }

  /**
   * 将socket连接数据放入redis
   * @param queueKey
   * @param bean
   * @param wsSession
   */
  private void setRedistValue(String queueKey,WebSocketBean bean,WebSocketSession wsSession){
    removeRedistValue(wsSession,bean.getDeviceCode());
    String queueValue = bean.getBusinessType()+":"+bean.getDeviceCode()+":"+wsSession.getId()+":"+bean.getShowType();
    redisUtils.rightPush(queueKey,queueValue);
  }
  /**
   * android端发送消息并返回消息
   *
   * @param wsSession
   * @param message
   */
  @Override
  public void handleMessage(WebSocketSession wsSession, WebSocketMessage<?> message) throws IOException {
   String payload = (String) message.getPayload();
    WebSocketBean webSocketBean = JSON.parseObject(payload, WebSocketBean.class);
    logger.info("叫号机发送消息：" + webSocketBean.toString());
    switch (webSocketBean.getType()) {
      //与前端建立心跳机制
      case CommonConstant.HEART_BEAT:
        sendMessageForHeartbeat(new TextMessage(JSON.toJSONString(webSocketBean)), wsSession);
        return;
      case CommonConstant.NEW_CONNECTION_RELATION:
      String queueKey = CommonConstant.WEBSOCKET_KEY + webSocketBean.getServiceKey();
      setRedistValue(queueKey, webSocketBean, wsSession);
      if(!AndroidShowTypeEnum.OnlyTakeNumber.getValue().equals(webSocketBean.getShowType())){
        switch (webSocketBean.getBusinessType()){
          case CommonConstant.ANDROID_BUSINESS_TYPE_ALL:sendMessageToAndroidForAll(webSocketBean,wsSession);
            return;
          case CommonConstant.ANDROID_BUSINESS_TYPE_DEPARTMENTAL:sendMessageToAndroidForDepartment(webSocketBean,wsSession);
          default:

        }
      }
      default:

    }
  }

  /**
   * 定向消息发送到android
   * @param message
   */
  public void sendMessageToAndroid(TextMessage message,String sessionId) {
    //遍历在线安卓连接
    for (WebSocketSession session : sessions) {
      logger.info("消息请求端：" + session.getUri().toString());
      logger.info("消息数：" + message.toString());
      if(session.getId().equals(sessionId)){
        try {
            session.sendMessage(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }/**
   * 定向消息发送到android 根据叫号机设定及时推送消息分部门处理
   */
  public void sendMessageToAndroidForDepartment(WebSocketBean webSocketBean,WebSocketSession wsSession) {
    List<ShowBusinessInfo> showInfos=businessInfoService.getShowBusinessInfoList(webSocketBean.getServiceKey(), "");
    //遍历在线安卓连接
        try {
            wsSession.sendMessage(new TextMessage(JSON.toJSONString(showInfos)));
        } catch (IOException e) {
          e.printStackTrace();
        }
  }/**
   * 定向消息发送到android 根据叫号机设定及时推送消息一窗综办处理
   */
  public void sendMessageToAndroidForAll(WebSocketBean webSocketBean,WebSocketSession wsSession) {
    List<ShowBusinessInfo> showInfos = businessInfoService.getShowBusinessInfosForAndroid(CommonConstant.SUPER_ADMIN_DEPTID_TYPE, webSocketBean.getServiceKey());
    //遍历在线安卓连接
        try {
            wsSession.sendMessage(new TextMessage(JSON.toJSONString(showInfos)));
        } catch (IOException e) {
          e.printStackTrace();
        }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception){
    sessions.remove(session);
    //异常断开连接
    removeRedistValue(session,"");
    if(session.isOpen()){
      try{
        session.close();
      } catch (IOException e){
        logger.info("叫号机连接关闭异常"+e.getMessage());
      }
    }
  }

  //android端建立 心跳
  public void sendMessageForHeartbeat(TextMessage message, WebSocketSession session) {
    if (session == null) {
      logger.info("与android端失去连接！");
      return;
    }
    try {
      session.sendMessage(message);
    } catch (IOException e) {
      logger.info("与android端发生异常");
      e.printStackTrace();
    }
  }
  /**
   * 是否支持消息拆分发送：如果接收的数据量比较大，最好打开(true), 否则可能会导致接收失败。
   * 如果出现WebSocket连接接收一次数据后就自动断开，应检查是否是这里的问题。清除   diviceCode
   */
  @Override
  public boolean supportsPartialMessages() {
    return true;
  }
}
