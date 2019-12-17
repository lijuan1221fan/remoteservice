package com.visionvera.remoteservice.ws;

import com.alibaba.fastjson.JSON;
import com.visionvera.common.enums.CallDeviceEnum;
import com.visionvera.common.enums.SysUserStateEnum;
import com.visionvera.common.enums.WSWebReturnEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.EmbeddedBean;
import com.visionvera.remoteservice.bean.EmbeddedServerInfo;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UserSortVo;
import com.visionvera.remoteservice.bean.WebSocketBean;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.SysUserService;
import com.visionvera.remoteservice.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by jlm on 2019-05-29 16:38
 */
@Component
public class CallDeviceWebSocketHandler implements WebSocketHandler {

  private static final CopyOnWriteArrayList<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(CallDeviceWebSocketHandler.class);
  @Resource
  private EmbeddedServerDao embeddedServerDao;
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
  @Resource
  private AcceptBusinessWebSocketHandler acceptBusinessWebSocketHandler;
  @Value("${temp.save.path}")
  private String tempPath;
  @Resource
  private BusinessInfoService businessInfoService;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private WindowDao windowDao;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    logger.info("前端：建立连接");
    sessionList.add(session);
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    String payload = (String) message.getPayload();
    WebSocketBean webSocketBean = JSON.parseObject(payload, WebSocketBean.class);
    logger.info("前端发送消息：" + webSocketBean.toString());
    EmbeddedBean embeddedBean = new EmbeddedBean();
    switch (webSocketBean.getType()) {
      case CommonConstant.BUSINESS_CONNECTION_RELATION:
        EmbeddedServerInfo callDeviceEmbeddedServer = embeddedServerDao
            .getCallDeviceEmbeddedServerByUserId(Integer.valueOf(webSocketBean.getUserId()));
        if (callDeviceEmbeddedServer == null) {
          embeddedServerDao
              .insertCallDeviceEmbeddedServerInfo(session.getId(), Integer.valueOf(webSocketBean.getUserId()));
          returnRrelation(Integer.valueOf(webSocketBean.getUserId()),
              WSWebReturnEnum.CONNECTION_RELATION
                  .getName(), WSWebReturnEnum.CONNECTION_RELATION.getCode());
          userOnLineState(Integer.valueOf(webSocketBean.getUserId()));
          logger.info("建立前端连接关系成功");
          return;
        }
        logger.info("更新前端连接关系成功");
        //webSocket找到userId之前的连接之后，关闭之前的连接，并发消息给前端
        //问题：同一浏览器同一用户打开两个受理窗口
        returnRrelation(Integer.valueOf(webSocketBean.getUserId()),
                "webSocket已重新建立", WSWebReturnEnum.CONNECTION_RELATION.getCode());
        embeddedServerDao.updateEmbeddedServerOfsessionIdByUserId(Integer.valueOf(webSocketBean.getUserId()), session.getId());
        userOnLineState(Integer.valueOf(webSocketBean.getUserId()));
        returnRrelation(Integer.valueOf(webSocketBean.getUserId()),
            WSWebReturnEnum.CONNECTION_RELATION
                .getName(), WSWebReturnEnum.CONNECTION_RELATION.getCode());
        return;
      case CommonConstant.HIGH_SHOTMETER:
        embeddedBean.setType(CallDeviceEnum.HighShotMeter.getValue());
        embeddedServerDao
            .updateEmbeddedServerByVcDevId(session.getId(),
                Integer.valueOf(webSocketBean.getUserId()));
        break;
      case CommonConstant.CARD_READER:
        embeddedBean.setType(CallDeviceEnum.CardReader.getValue());
        embeddedServerDao
            .updateEmbeddedServerByVcDevId(session.getId(),
                Integer.valueOf(webSocketBean.getUserId()));
        break;
      case CommonConstant.SIGN_ATURE_BOARD:
        embeddedBean.setType(CallDeviceEnum.SignatureBoard.getValue());
        embeddedServerDao
            .updateEmbeddedServerByVcDevId(session.getId(),
                Integer.valueOf(webSocketBean.getUserId()));
        break;
      case CommonConstant.FACE_ALIGNMENT:
        String path = UrlFileUtil.downloadFile(webSocketBean.getImgUrl(), tempPath);
        byte[] baseImage = FileUtil.imageTobyte(path);
        File file = new File(path);
        String shaFile = FileUtil.getFileSha1(file);
        embeddedBean.setType(CallDeviceEnum.FaceAlignment.getValue());
        embeddedBean.setPhoto(baseImage);
        embeddedBean.setShaValue(shaFile);
        file.delete();
        break;
      //与前端建立心跳机制
      case CommonConstant.HEART_BEAT:
        String userId = webSocketBean.getUserId();
        //问题：同一个websocket的userId发生变化，应告诉前端
        EmbeddedServerInfo embeddedServer = embeddedServerDao.getEmbeddedServerBySessionId(session.getId());
        if(embeddedServer != null && embeddedServer.getUserId() != Integer.parseInt(userId)){
          webSocketBean.setMessage("用户id发生变化");
        }
        sendMessageForHeartbeat(new TextMessage(JSON.toJSONString(webSocketBean)), session);
        return;
      case CommonConstant.BUSINESS_INFO:
        userId = webSocketBean.getUserId();
        acceptBusinessWebSocketHandler.getBusinessInfo(Integer.valueOf(userId));
        return;
      case CommonConstant.BUSINESS_COUNT:
        userId = webSocketBean.getUserId();
        acceptBusinessWebSocketHandler.sendMessageForBusinessCount(Integer.valueOf(userId));
        return;
    }
    embeddedServerWebSocketHandler.sendMessageToEmbeddServer(
        new TextMessage(JSON.toJSONString(embeddedBean)),
        webSocketBean.getVcDevId(), Boolean.TRUE);
  }
  private void userOnLineState(Integer userId){
    SysUserBean userBean = sysUserDao.getSysUserInfo(userId);
    if(userBean != null){
      sysUserDao.updateSysUserStateByWebSocket(userBean.getUserId(), SysUserStateEnum.OnLine.getValue());
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    logger.error("前端：Exception occurs on webSocket connection. disconnecting....");
    if (session.isOpen()) {
      session.close();
    }
    EmbeddedServerInfo bean = embeddedServerDao.getEmbeddedServerBySessionId(session.getId());
    if(bean != null){
      updateUser(bean.getUserId());
    }
    sessionList.remove(session);
    embeddedServerDao.deleteEmbeddedServer(session.getId());
  }
  //更新用户状态，移除redis中的用户
  private void updateUser(Integer userId){
    sysUserDao.updateSysUserStateByWebSocket(userId,SysUserStateEnum.OffLine.getValue());
    SysUserBean userBean = sysUserDao.getSysUserInfo(userId);
    if(userBean != null) {
        String waitQueueKey = CommonConstant.WAITING_WORK_KEY + userBean.getServiceKey() + ":" + userBean.getDeptId();
        UserSortVo vo = businessInfoService.getUserSortVo(userId);
        if (vo != null) {
            redisUtils.listRemove(waitQueueKey, 0, JSON.toJSONString(vo));
        }
    }
    //socket断开连接 判断当前用户是否存在未完成业务
    BusinessInfo businessInfo = businessInfoService.getBusinessByUserId(userId);
    if (businessInfo == null) {
      //不存在未完成业务 删除用户和窗口关联关系  并将窗口置为空闲状态
      WindowBean window = windowDao.getUserWindowByUserId(userId);
      if (window != null) {
        //将窗口置为空闲状态
        Integer num1 = windowDao.updateWindowUseStatus(window.getId(), WindowUseStateEnum.Free.getValue());
        logger.info("将窗口置为空闲状态-->" + num1);
        //删除当前用户与窗口关联关系
        Integer num2 = windowDao.deleteWindowAndUserRelation(null, userId);
        logger.info("删除当前id为" + userId + "的用户的窗口关联关系-->" + num2);
      }
    }
    if(userBean !=null){
      String waitQueueKey =CommonConstant.WAITING_WORK_KEY+userBean.getServiceKey()+":"+userBean.getDeptId();
      UserSortVo vo = businessInfoService.getUserSortVo(userId);
      if (vo != null) {
        redisUtils.listRemove(waitQueueKey,0,JSON.toJSONString(vo));
      }
    }
  }
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    logger.info("前端：失去连接");
    EmbeddedServerInfo bean = embeddedServerDao.getEmbeddedServerBySessionId(session.getId());
    if(bean != null){
      updateUser(bean.getUserId());
    }
    sessionList.remove(session);
    embeddedServerDao.deleteEmbeddedServer(session.getId());
  }

  @Override
  public boolean supportsPartialMessages() {
    return true;
  }

  //发送消息给前端
  public void sendMessage(String message, String vcDevId) throws IOException {
    if (sessionList.size() == 0) {
      logger.info("失去所有前端连接！");
    }
    EmbeddedServerInfo callDeviceEmbeddedServer = embeddedServerDao
        .getCallDeviceEmbeddedServer(vcDevId);
    if (callDeviceEmbeddedServer != null) {
      for (WebSocketSession session : sessionList) {
        if (session.getId().equals(callDeviceEmbeddedServer.getSessionId())) {
          logger.info("后台发送消息：" + message);
            session.sendMessage(new TextMessage(message));
          return;
        }
      }
      logger.info("sessionList中无与数据库sessionId匹配数据。数据库sessionId = " + callDeviceEmbeddedServer
          .getSessionId());
    } else {
      logger.info("与前端失去连接，数据库未查到关联关系");
    }

  }

  //发送业务及业务办理统计消息给前端
  public boolean sendMessageForBusiness(TextMessage message, Integer userId) {
    boolean flag = false;
    if (sessionList.size() == 0) {
      logger.info("失去所有前端连接！");
      return flag = false;
    }
    EmbeddedServerInfo callDeviceEmbeddedServer = embeddedServerDao
        .getCallDeviceEmbeddedServerRelationBusinessByUserId(userId);
    if (callDeviceEmbeddedServer != null) {
      for (WebSocketSession session : sessionList) {
        if (session.getId().equals(callDeviceEmbeddedServer.getSessionId())) {
          logger.info("后台发送消息：" + message);
            try {
              session.sendMessage(message);
              return flag = true;
            } catch (IOException e) {
              logger.info("向前端发送消息异常");
              e.printStackTrace();
              return flag = false;
            }

        }
      }
      logger.info("sessionList中无与数据库sessionId匹配数据。数据库sessionId = " + callDeviceEmbeddedServer
          .getSessionId());
      return flag = false;
    } else {
      logger.info("与前端失去连接，数据库未查到关联关系");
      return flag = false;
    }

  }

  /**
   * 开始业务关联关系建立成功
   *
   * @param userId
   */
  private void returnRrelation(Integer userId, String relationName, String type) {
    ShowBusinessInfo info = new ShowBusinessInfo(WSWebReturnEnum.RESPONSE_SUCCESS.getCode());
    logger.info("更新前端连接关系成功:typetype:"+type);
    info.setType(type);
    info.setMessage(relationName);
    sendMessageForBusiness(new TextMessage(JSON.toJSONString(info)), userId);
}

  //发送业务及业务办理统计消息给前端  心跳
  public void sendMessageForHeartbeat(TextMessage message, WebSocketSession session) {
    if (session == null) {
      logger.info("失去所有前端连接！");
      return;
    }
    try {
      session.sendMessage(message);
    } catch (IOException e) {
      logger.info("向前端发送消息异常");
      e.printStackTrace();
    }
  }


}
