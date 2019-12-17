package com.visionvera.remoteservice.ws;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.WebSocketBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.DeviceInfoDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.pojo.BusinessTypeVo;
import com.visionvera.remoteservice.pojo.DeptListVo;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.request.GetDeviceListRequest;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.BusinessTypeService;
import com.visionvera.remoteservice.service.DeviceInfoService;
import com.visionvera.remoteservice.service.impl.NumberServiceImpl;
import com.visionvera.remoteservice.util.ResultUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Resource;

import com.visionvera.remoteservice.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author hakimjun
 * @Description: TODO
 * @date 2019/8/19
 */
@Component
public class H5WebSocketHandler implements WebSocketHandler {
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private BusinessInfoService businessInfoService;
  @Autowired
  private BusinessTypeService businessTypeService;
  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private NumberServiceImpl  numberService;
  @Resource
  private DeviceInfoService deviceInfoService;
  @Resource
  private DeviceInfoDao deviceInfoDao;

  private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(AndroidWebSocketHandler.class);

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.info("H5前端：建立连接");
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
      closeAndRemoveSession(session);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
      closeAndRemoveSession(session);
  }

  @Override
  public boolean supportsPartialMessages() {
    return true;
  }

  /**
   *收到消息，进行处理
   * @param session
   * @param message
   * @throws Exception
   */
  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
   {
    String payload = (String)message.getPayload();
    WebSocketBean webSocketBean = JSON.parseObject(payload, WebSocketBean.class);
    logger.info("前端发送消息：" + webSocketBean.toString());
    String queueKey = CommonConstant.WEBSOCKET_KEY + webSocketBean.getServiceKey();
     GetDeviceListRequest getDeviceListRequest =new GetDeviceListRequest();
     getDeviceListRequest.setServiceKey(webSocketBean.getServiceKey());
     List<DeviceInfo> deviceInfos = deviceInfoDao.selectExistDevice(getDeviceListRequest);
    if(deviceInfos.size()==0){
      //没有相关设备，设备已删除，进行推送
      businessInfoService.SendMessageForAndroidByJHJ(webSocketBean.getServiceKey());
    }
    setRedistValue(queueKey, webSocketBean, session);
    switch (webSocketBean.getType()){
      //与前端建立心跳
      case CommonConstant.HEART_BEAT:
        sendMessageForHeartbeat(new TextMessage(JSON.toJSONString(webSocketBean)), session);
        return;
      case CommonConstant.BUSINESS_TO_DO:
        sendMessageBusinessToDo(webSocketBean,session);
        return;
      case CommonConstant.BUSINESS_TYPE:
        sendMessageBusinessType(webSocketBean,session);
        return;
      case CommonConstant.H5_DETECTION:
        logger.info("开始执行将叫号机检测数据存入redis");
        saveDataToRedis(webSocketBean);
        logger.info("结束执行将叫号机检测数据存入redis");
        return;
      case CommonConstant.H5_32_DETECTION:
        logger.info("开始执行将32寸叫号机检测数据存入redis");
        saveDataToRedis(webSocketBean);
        logger.info("结束执行将32寸叫号机检测数据存入redis");
        return;
    }
   }

  private void saveDataToRedis(WebSocketBean webSocketBean) {
    boolean set = redisUtils.set(CommonConstant.ANDROID_DETECTION_KEY + webSocketBean.getServiceKey(), webSocketBean);
    if (!set) {
      logger.info("叫号机检测数据存入redis失败");
    }
  }

  private void sendMessageBusinessType(WebSocketBean webSocketBean,WebSocketSession session) {
    DeptListVo deptListVo=new DeptListVo();
    deptListVo.setServiceKey(webSocketBean.getServiceKey());
    deptListVo.setPageNum(webSocketBean.getPageNum());
    deptListVo.setPageSize(webSocketBean.getPageSize());
    deptListVo.setState(webSocketBean.getState());
    try {
      Map<String, Object> map=numberService.getDeptList(deptListVo);
      map.put("type",CommonConstant.BUSINESS_TYPE);
      session.sendMessage(new TextMessage(JSON.toJSONString(map)));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 发送业务等待数据
   * @param webSocketBean
   * @param session
   */
  private void sendMessageBusinessToDo(WebSocketBean webSocketBean,WebSocketSession session) {
    List<ShowBusinessInfo> showInfos = businessInfoService.getShowBusinessInfosForAndroid(CommonConstant.SUPER_ADMIN_DEPTID_TYPE, webSocketBean.getServiceKey());
    Map<String,Object> map=null;
    //服务中心信息不存在
    if(showInfos==null){
      map= ResultUtils.error("服务中心不存在");
    }
    //遍历在线安卓连接
    try {
      session.sendMessage(new TextMessage(JSON.toJSONString(map==null?showInfos:map)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 定向消息发送到android
   * @param message
   */
  public void sendMessageToH5(TextMessage message,String sessionId) {
    //遍历在线安卓连接
    for (WebSocketSession session : sessions) {
      logger.info("消息请求端：" + session.getUri().toString());
      logger.info("消息数据：" + message.toString());
      if(session.getId().equals(sessionId)){
        try {
          session.sendMessage(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
}


  //前端H5建立 心跳
  public void sendMessageForHeartbeat(TextMessage message, WebSocketSession session) {
    if (session == null) {
      logger.info("与H5端失去连接！");
      return;
    }
    try {
      session.sendMessage(message);
    } catch (IOException e) {
      logger.info("与H5端发生异常");
      e.printStackTrace();
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
          String value = (String) redisUtils.get(queueKey);
          if (!StringUtil.isEmpty(value)) {
              String[] valSplit = value.split(":");
              String sessionId = valSplit[1];
              if (sessionId.equals(session.getId())) {
                  logger.info("移除redis，Key：" + queueKey + "sessionId" + session.getId());
                  redisUtils.remove(queueKey);
          }
        }

//        List<String> queueVal = redisUtils.listRangeAllStr(queueKey);
//        if(CollectionUtils.isNotEmpty(queueVal)){
//          for(String val : queueVal){
//            String[] valSplit = val.split(":");
//            if(valSplit.length==2){
//              if(valSplit[1].equals(session.getId())){
//                String queueValue = valSplit[0]+":"+valSplit[1];
//                redisUtils.listRemove(queueKey,0,queueValue);
//                return;
//              }
//            }
//            if(valSplit[0].equals(deviceCode)){
//              //如果已存在连接，先移除
//              redisUtils.listRemove(queueKey,0,val);
//              return;
//            }
//          }
//        }
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
    String queueValue = bean.getDeviceCode()+":"+wsSession.getId();
    redisUtils.set(queueKey, queueValue);
  }

    private void closeAndRemoveSession(WebSocketSession session) {
        sessions.remove(session);
        removeRedistValue(session, "");
        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                logger.info("叫号机连接关闭异常" + e.getMessage());
            }
        }
    }

}
