package com.visionvera.remoteservice.ws;

import com.alibaba.fastjson.JSON;
import com.visionvera.common.enums.*;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UserSortVo;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.NumberService;
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
 * @author ljfan
 * @ClassName AcceptBusinessWebSocketHandler description 受理端推送业务
 * @date 2019/04/19 version
 */
@Component
public class AcceptBusinessWebSocketHandler implements WebSocketHandler {

  @Resource
  private BusinessInfoService businessInfoService;
  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private NumberService numberService;
  @Resource
  private CallDeviceWebSocketHandler callDeviceWebSocketHandler;
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private WindowDao windowDao;

  private static Logger logger = LoggerFactory.getLogger(AcceptBusinessWebSocketHandler.class);

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    if(session != null){
      logger.info(session.getUri().getPath());
      logger.info("Oprn a WebSocket. Current connection url: " + session.getUri());
    }

  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    if(session != null){
      logger.info("Close a webSocket. Current connection url: " + session.getUri());
    }
  }

  /**
   * web端发送消息并返回消息
   *
   * @param wsSession
   * @param message
   */
  @Override
  public void handleMessage(WebSocketSession wsSession, WebSocketMessage<?> message)
      throws Exception {
    wsSession.sendMessage(message);
    logger.info("Receive a message from client: " + message.toString() + "bean");
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    if(session != null){
      logger.info(
          "Close a webSocket.handleTransportError:前端异常关闭,关闭请求路径：" + session.getUri().toString());
    }

  }

  /**
   * 是否支持消息拆分发送：如果接收的数据量比较大，最好打开(true), 否则可能会导致接收失败。 如果出现WebSocket连接接收一次数据后就自动断开，应检查是否是这里的问题。
   */
  @Override
  public boolean supportsPartialMessages() {
    return true;
  }



  public void sendMessageForBusinessInfo(Integer userId,BusinessInfo info, UserSortVo usv) {
    //根据业务员ID取得业务员，判断业务员是否在等待业务中，是就推送业务
    SysUserBean userBean = sysUserDao.getSysUserInfo(userId);
    if (SysUserWorkStateEnum.Waiting.getValue().equals(Integer.valueOf(userBean.getWorkState()))) {
      ShowBusinessInfo showInfo = businessInfoService.getWebSocketTask(userBean,info);
      showInfo.setType(CommonConstant.BUSINESS_INFO);
      boolean flag = callDeviceWebSocketHandler
          .sendMessageForBusiness(new TextMessage(JSON.toJSONString(showInfo)), userId);
      //推送业务成功，同步在线业务员及叫号机待办号
      if (showInfo.getBusinessInfo() != null && flag) {
        redisUtils.listRemove(CommonConstant.WAITING_WORK_KEY+userBean.getServiceKey()+":"+userBean.getDeptId(),0,JSON.toJSONString(usv));
        //推送业务时，将窗口工作状态置为工作中
        WindowBean windowBean = windowDao.getUserWindowByUserId(userId);
        if (windowBean != null) {
          Integer num = windowDao.updateWindowUseStatus(windowBean.getId(), WindowUseStateEnum.InUse.getValue());
          logger.info("推送业务至id为" + userId + "的业务员，将窗口id为" + windowBean.getId() + "状态修改为工作中" + "-->" + num);
        }
        //等待号同步
        numberService.sendMessageAll(showInfo.getBusinessInfo().getServiceKey(),
            showInfo.getBusinessInfo().getDeptId());
      } else{
        //推送失败，进入等待队列
        businessInfoService.popBusinessInfo(userBean.getServiceKey());
      }
    }


  }

  public void sendMessageForBusinessCount(Integer userId) {
    businessInfoService.getWaitNumber(userId);
  }

  /**
   * 1。发送业务请求，但有恢复业务时，返回code及消息
   */
  public void getBusinessInfo(Integer userId) {
    //判断是否有恢复业务
    SysUserBean userBean = sysUserDao.getSysUserInfo(userId);
    BusinessInfo businessInfo = businessInfoDao.getBusinessByUserId(userId);
    if (businessInfo != null && BusinessInfoStateEnum.Processing.getValue()
        .equals(businessInfo.getState())) {
      ShowBusinessInfo info = new ShowBusinessInfo(WSWebReturnEnum.UNFINISHED_BUSINESS.getCode());
      info.setType(CommonConstant.BUSINESS_INFO);
      info.setMessage(WSWebReturnEnum.UNFINISHED_BUSINESS.getName());
      callDeviceWebSocketHandler
          .sendMessageForBusiness(new TextMessage(JSON.toJSONString(info)), userId);
      return;
    }
    if(userBean != null){
      businessInfoService.pushUserToqueue(userBean);
    }
  }


}
