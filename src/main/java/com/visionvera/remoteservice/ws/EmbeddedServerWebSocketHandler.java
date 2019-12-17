package com.visionvera.remoteservice.ws;


import com.alibaba.fastjson.JSON;
import com.visionvera.api.handler.bean.EmbeddedEntity;
import com.visionvera.api.handler.bean.EmbeddedInfo;
import com.visionvera.api.handler.constant.ContentApi;
import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.api.handler.utils.ImageUtil;
import com.visionvera.common.enums.CallDeviceEnum;
import com.visionvera.common.enums.ConfigTypeEnum;
import com.visionvera.common.enums.DevTypeEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.EmbeddedBean;
import com.visionvera.remoteservice.bean.EmbeddedServerInfo;
import com.visionvera.remoteservice.bean.EmbededServerBackUpBean;
import com.visionvera.remoteservice.bean.HeartBeatBean;
import com.visionvera.remoteservice.bean.WebSocketBean;
import com.visionvera.remoteservice.bean.X86ConfigInformationBean;
import com.visionvera.remoteservice.bean.versionUpdateBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.X86ConfigInformationDao;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.impl.InitializingBeanImpl;
import com.visionvera.remoteservice.util.FileUtil;
import com.visionvera.remoteservice.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by jlm on 2019-05-15 15:24   x86服务连接
 */
@Component
public class EmbeddedServerWebSocketHandler implements WebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(EmbeddedServerWebSocketHandler.class);
    private static final CopyOnWriteArrayList<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private ConcurrentHashMap<String, String> sessionDataMap = new ConcurrentHashMap<>();
    @Resource
    private EmbeddedServerDao embeddedServerDao;
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private StorageUploadHelper storageUploadHelper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private CallDeviceWebSocketHandler callDeviceWebSocketHandler;
    @Resource
    private X86ConfigInformationDao x86ConfigInformationDao;
    @Value("${temp.save.path}")
    private String xpath;
  @Value("${temp.save.datapath}")
  private String datapath;

    private static final String CONFIGKEY = "version";


    //建立连接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("SCU:建立连接");
        sessionList.add(session);
        String url = session.getUri().toString();
        int index = url.lastIndexOf("=");
        String vcDevId = url.substring(index + 1);
        EmbeddedServerInfo embeddedServerByVcDevId = embeddedServerDao
                .getEmbeddedServerByVcDevId(vcDevId);
        if (embeddedServerByVcDevId == null) {
            embeddedServerDao.insertEmbeddedServer(vcDevId, session.getId());
            embeddedServerDao.deleteEmbeddedServerBackUp(vcDevId);
            return;
        }
         embeddedServerDao.updateCallDeviceEmbeddedServer(vcDevId, session.getId());


    }

    //失去连接
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        logger.info("SCU:失去连接");
        logger.info("SCU返回状态：" + closeStatus.toString());
        sessionDataMap.remove(session.getId());
        sessionList.remove(session);
        //删除数据之前备份到对应库
      EmbeddedServerInfo embeddedServerInfo= embeddedServerDao.getEmbeddedServer(session.getId());
      if(embeddedServerInfo!=null){
        EmbededServerBackUpBean embededServerBackUpBean=new EmbededServerBackUpBean();
        embededServerBackUpBean.setVcDevId(embeddedServerInfo.getVcDevId());
       embededServerBackUpBean.setVersion(embeddedServerInfo.getVersion());
        embeddedServerDao.insertEmbeddedServerBackUp(embededServerBackUpBean);
      }
      embeddedServerDao.deleteEmbeddedServer(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("SCU:Exception occurs on webSocket connection. disconnecting....");
        logger.error("=========================");
        exception.printStackTrace();
        logger.error("=========================");
        sessionDataMap.remove(session.getId());
        if (session.isOpen()) {
            logger.info("=== 进入handleTransportError且session is open");
            try {
                logger.info("=== 准备关闭session");
                session.close();
                logger.info("=== 已经执行session close");
            } catch (IOException e) {
                logger.info("=== 进入handleTransportError的抓取异常");
                e.printStackTrace();
            }
        }
        sessionList.remove(session);
        logger.info("=== 已经执行清除sessionList");
        embeddedServerDao.deleteEmbeddedServer(session.getId());
        logger.info("=== 已经执行清除数据库关联关系");
    }

  //收到消息进行处理
  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws IOException {
    synchronized (this) {
      String sessionData = sessionDataMap.get(session.getId());
      if (!message.isLast()) {
        sessionDataMap.put(session.getId(), sessionData == null ?
            message.getPayload().toString() : sessionData + message.getPayload());
        return;
      }
      EmbeddedServerInfo embeddedServerInfo = embeddedServerDao.getEmbeddedServer(session.getId());
      EmbeddedEntity embeddedEntity = null;
      Integer userId = null;
      try {
        //心跳机制  包含dataTime的数据则是x86发送的心跳数据
        if (message.getPayload().toString().contains("dateTime")) {
          if (embeddedServerInfo != null) {
            HeartBeatBean heartBeatBean = JSON
                .parseObject(message.getPayload().toString(), HeartBeatBean.class);
            heartBeat(session, heartBeatBean);
          }
          return;
        }
        sessionDataMap.put(session.getId(), sessionData == null ?
            message.getPayload().toString() : sessionData + message.getPayload())      ;
        String data = sessionDataMap.get(session.getId());
        sessionDataMap.remove(session.getId());
        logger.info("SCU数据长度："+data.length());
        EmbeddedInfo embeddedInfo = JSON.parseObject(data, EmbeddedInfo.class);
       if(embeddedInfo.getData()!=null){embeddedEntity=JSON.parseObject(embeddedInfo.getData().toString(), EmbeddedEntity.class);}
        //更新配置文件  客户端获取配置
        if (CallDeviceEnum.X86Config.getValue().equals(embeddedInfo.getType())) {
          updateX86ConfigInfo(session);
          return;
        }
        //更新升级包 客户端获取升级包
        if (CallDeviceEnum.X86VersionUP.getValue().equals(embeddedInfo.getType())) {
          scuVersionUp(session);
          return;
        }
        if (embeddedEntity!=null) {
          if (CallDeviceEnum.X86Config.getValue().intValue() == Integer
              .parseInt(embeddedEntity.getType())
              || CallDeviceEnum.X86VersionUP.getValue().intValue() == Integer
              .parseInt(embeddedEntity.getType())) {
            return;
          }
        }
        if (embeddedServerInfo == null) {
          logger.info("未找到与SCU的关联关系");
          return;
        }
        userId = getUserIdByVcDevId(embeddedServerInfo.getVcDevId());
        //获取版本号
        if (CallDeviceEnum.scuVersion.getValue().equals(embeddedInfo.getType())) {
          versionUpdateBean versionUpdateBean = JSON.parseObject(data, versionUpdateBean.class);
          scuVersionUpdate(versionUpdateBean, embeddedServerInfo.getSessionId());
          logger.info("获取scu升级后的版本成功");
          if(embeddedEntity != null){
              embeddedEntity.setType(CommonConstant.SCUVERSION);
          }
          return;
          //sendSuccessMessageByUserId(embeddedServerInfo, embeddedEntity, userId);
        }
        //x86设备状态  信息返回
        if (embeddedEntity!=null&& CallDeviceEnum.EquipmentStatus.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
          Integer idCardStatus=embeddedEntity.getIdCardStatus();
          Integer highPhotoStatus=embeddedEntity.getHighPhotoStatus();
          Integer signatureStatus=embeddedEntity.getSignatureStatus();
          Integer fingerPrintStatus=embeddedEntity.getFingerPrintStatus();
          Integer printerStatus=embeddedEntity.getPrintStatus();
          EquipmentUpdate(idCardStatus,highPhotoStatus,signatureStatus,fingerPrintStatus,printerStatus,embeddedServerInfo.getVcDevId());
          return;
        }
        if (!data.contains("code")&&!data.contains("type")) {
          logger.info("data数据接收不完整：" + data);
          sendErrorMessage("当前网络不稳定，请重试", userId, null);
          return;
        }

        if (embeddedInfo.getCode() != null && embeddedInfo.getCode() != 0) {
          if (embeddedInfo.getData() != null) {
            //当返回的错误信息是打印机类型的，且是叫号机调用的打印机，则不发送失败消息给前端
            if (embeddedEntity != null && DevTypeEnum.CueingMachine.getValue()
                .equals(embeddedEntity.getDevType())) {
              logger.info("SCU返回失败信息：" + embeddedInfo.getMessage());
              return;
            }
            if (String.valueOf(CallDeviceEnum.Printer.getValue()).equals(embeddedEntity.getType()) ||
                    String.valueOf(CallDeviceEnum.PrintState.getValue()).equals(embeddedEntity.getType())) {
              sendErrorMessage(embeddedInfo.getMessage(), userId, embeddedEntity.getType());
              return;
            }
              if (CallDeviceEnum.ScuDetection.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
                  EmbeddedEntity detectionEntity = new EmbeddedEntity();
                  detectionEntity.setType("400");
                  redisUtils.set(CommonConstant.SCU_DETECTION_KEY + embeddedServerInfo.getVcDevId(), detectionEntity);
                  logger.info("SCU检测结果，code非0");
                  return;
              }
              if (CallDeviceEnum.DeviceDetection.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
                  EmbeddedEntity detectionEntity = new EmbeddedEntity();
                  detectionEntity.setType("400");
                  redisUtils.set(CommonConstant.DEVICE_DETECTION_KEY + embeddedServerInfo.getVcDevId(), detectionEntity);
                  logger.info("终端检测结果，code非0");
                  return;
              }
          }
          logger.info("SCU返回失败信息：" + embeddedInfo.getMessage());
          if(null != embeddedEntity.getType()){
            Integer sType = Integer.parseInt(embeddedEntity.getType());
            boolean flag = sType.equals(CallDeviceEnum.HighShotMeter.getValue()) || sType.equals(CallDeviceEnum.CardReader.getValue()) || sType.equals(CallDeviceEnum.SignatureBoard.getValue()) || sType.equals(CallDeviceEnum.Printer.getValue()) || sType.equals(CallDeviceEnum.FaceAlignment.getValue());
            if (embeddedInfo.getData() != null && flag && null != userId) {
              sendErrorMessage(embeddedInfo.getMessage(), userId, embeddedEntity.getType());
            }
            return;
          }

        }
        //打印
        if(embeddedEntity != null && null != embeddedEntity.getType()) {
          if (String.valueOf(CallDeviceEnum.Printer.getValue()).equals(embeddedEntity.getType()) ||
              String.valueOf(CallDeviceEnum.PrintState.getValue())
                  .equals(embeddedEntity.getType())) {
            sendSuccessMessageByUserId(embeddedServerInfo, embeddedEntity, userId);
            return;

          }
        }
      //todo -detection 对SCU检测数据进行处理
      if (CallDeviceEnum.ScuDetection.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
          logger.info("开始将SCU检测数据返回结果存入redis");
          redisUtils.set(CommonConstant.SCU_DETECTION_KEY + embeddedServerInfo.getVcDevId(), embeddedEntity);
          logger.info("结束将SCU检测数据返回结果存入redis");
          return;
      }

      //todo -detection 对终端检测数据进行处理
      if (CallDeviceEnum.DeviceDetection.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
          logger.info("开始将终端检测数据返回结果存入redis");
          redisUtils.set(CommonConstant.DEVICE_DETECTION_KEY + embeddedServerInfo.getVcDevId(), embeddedEntity);
          logger.info("结束将终端检测数据返回结果存入redis");
          return;
      }

        //对photo进行处理
        checkPhoto(embeddedEntity);
        //人证比对
        if (CommonConstant.FACE_ALIGNMENT.equals(embeddedEntity.getType())
            && embeddedEntity.getMatchValue() != null && embeddedEntity
            .getMatchValue().equals(ContentApi.SUCCESS_CODE)) {
          logger.info("人证比对成功");
          sendMessageForFaceAlignment(userId, embeddedEntity, "人证比对成功");
          return;
        } else if (CommonConstant.FACE_ALIGNMENT.equals(embeddedEntity.getType())
            && embeddedEntity.getMatchValue() != null && !embeddedEntity
            .getMatchValue().equals(ContentApi.SUCCESS_CODE)) {
          logger.info("人证比对失败");
          sendMessageForFaceAlignment(userId, embeddedEntity, "人证比对失败");
          return;
        } else if (CommonConstant.FACE_ALIGNMENT.equals(embeddedEntity.getType())
            && embeddedEntity.getMatchValue() == null) {
          logger.info("政务服务终端返回信息有误，请重试");
          sendErrorMessage("政务服务终端返回信息有误，请重试", userId, null);
          return;
        }
        //高拍仪 身份证 签名
        if (embeddedServerInfo.getVcDevId() != null) {
          //redis数据key 格式为 村终端号 + “-” + 业务类型 （1高拍仪 2身份证 3签名 4打印） 例：00001-1 为00001终端对应村的高拍仪返回的数据
          redisUtils.set(
              CommonConstant.EMBEDDED_SERVER_KEY + "-" + embeddedServerInfo.getVcDevId() + "-"
                  + embeddedEntity.getType(), embeddedEntity);
          //存储成功后发消息告诉前端 可以调用接口
          sendSuccessMessageByUserId(embeddedServerInfo, embeddedEntity, userId);
          logger.info("SCU获取消息成功");
          return;
        }

      } catch (MyException e) {
        e.printStackTrace();
        sendErrorMessage("内容管理平台获取存储网关路径失败", userId, null);
      } catch (Exception e) {
        e.printStackTrace();
        sendErrorMessage("连接异常，请稍后", userId, null);
      }
    }
  }
  /**
   * scu版本号更新
   *
   * @param versionUpdateBean
   * @param sessionId
   */
  private void scuVersionUpdate(versionUpdateBean versionUpdateBean, String sessionId ) {
    embeddedServerDao.updateVersionrByUSerId(versionUpdateBean.getVersion(), sessionId );
  }

  /**
   * x86设备状态更新
   *
   * @param
   */
  private void EquipmentUpdate(Integer idCardStatus,Integer highPhotoStatus ,Integer signatureStatus,Integer fingerPrintStatus,Integer printerStatus, String  vcDevId) {
    embeddedServerDao.updateEmbeddedServerByUserId(idCardStatus,highPhotoStatus,signatureStatus,fingerPrintStatus,printerStatus, vcDevId);
  }

  @Override
  public boolean supportsPartialMessages() {
    return true;
  }

    /**
     * 定向发送消息到x86
     *
     * @param message
     * @param vcDevId
     * @param flag    标识符 判断是否需要返回错误信息给前端
     */
    public void sendMessageToEmbeddServer(WebSocketMessage<?> message, String vcDevId, Boolean flag) {
      logger.info("发送消息至x86方法"+message);
        if (sessionList.size() == 0 && Boolean.TRUE.equals(flag)) {
            loseConnection(vcDevId);
            return;
        }
        if (sessionList.size() == 0 && Boolean.FALSE.equals(flag)) {
            return;
        }
        for (WebSocketSession session : sessionList) {

            EmbeddedServerInfo embeddedServer = embeddedServerDao.getEmbeddedServer(session.getId());
            if (embeddedServer != null && embeddedServer.getVcDevId().equals(vcDevId)) {
                try {
                    logger.info("=== 进入到发送消息至x86方法");
                    if (session.isOpen()) {
                        logger.info("=== session is open");
                        session.sendMessage(message);
                        logger.info("=== 发送消息完成");
                        return;
                    }
                } catch (IOException e) {
                    logger.info("=== 发送消息时进入异常");
                    e.printStackTrace();
                }
            }

        }
        if (Boolean.TRUE.equals(flag)) {
            loseConnection(vcDevId);
        }
    }

  /**
   * 失去连接消息发送至前端
   *
   * @param vcDevId
   */
  private void loseConnection(String vcDevId) {
    Integer userId = getUserIdByVcDevId(vcDevId);
    WebSocketBean webSocketBean = new WebSocketBean();
    // 1 标示x86错误
    webSocketBean.setCode("1");
    webSocketBean.setMessage("与SCU失去连接");
    embeddedServerDao.deleteEmbeddedServer(vcDevId);
    String errorMessage = JSON.toJSONString(webSocketBean);
    callDeviceWebSocketHandler.sendMessageForBusiness(new TextMessage(errorMessage), userId);
  }

  /**
   * 发送获取失败消息至前端
   *
   * @param message
   * @param type
   */
  private void sendErrorMessage(String message, Integer userId, String type) throws IOException {
    logger.info("SCU报错：" + message);
    WebSocketBean webSocketBean = new WebSocketBean();
    // 1 标示x86错误
    webSocketBean.setCode("1");
    webSocketBean.setType(type);
    webSocketBean.setMessage(message);
    String errorMessage = JSON.toJSONString(webSocketBean);
    callDeviceWebSocketHandler.sendMessageForBusiness(new TextMessage(errorMessage), userId);
  }

    /**
     * 发送成功消息至前端
     *
     * @param embeddedServerInfo
     * @param embeddedEntity
     * @throws IOException
     */
    private void sendSuccessMessage(EmbeddedServerInfo embeddedServerInfo, EmbeddedEntity embeddedEntity) throws IOException {
        WebSocketBean webSocketBean = new WebSocketBean();
        webSocketBean.setType(embeddedEntity.getType());
        webSocketBean.setCode(ContentApi.SUCCESS_CODE.toString());
        String successMessage = JSON.toJSONString(webSocketBean);
        callDeviceWebSocketHandler.sendMessage(successMessage, embeddedServerInfo.getVcDevId());
    }


  /**
   * 发送成功消息至前端
   *
   * @param embeddedServerInfo
   * @param embeddedEntity
   * @throws IOException
   */
  private void sendSuccessMessage(EmbeddedServerInfo embeddedServerInfo) throws IOException {
    String successMessage = JSON.toJSONString(embeddedServerInfo);
    callDeviceWebSocketHandler.sendMessage(successMessage, embeddedServerInfo.getVcDevId());
  }
    /**
     * 发送成功消息至前端
     *
     * @param embeddedServerInfo
     * @param userId
     * @throws IOException
     */
    private void sendSuccessMessageByUserId(EmbeddedServerInfo embeddedServerInfo,
                                            EmbeddedEntity embeddedEntity, Integer userId) throws IOException {
        WebSocketBean webSocketBean = new WebSocketBean();
        webSocketBean.setType(embeddedEntity.getType());
        webSocketBean.setCode(ContentApi.SUCCESS_CODE.toString());
        String successMessage = JSON.toJSONString(webSocketBean);
        callDeviceWebSocketHandler.sendMessageForBusiness(new TextMessage(successMessage), userId);
    }

    private String getFaceAligmentMessage(EmbeddedEntity embeddedEntity, WebSocketBean webSocketBean) {
        StorageResVo storageResVo = (StorageResVo) embeddedEntity.getObject();
        webSocketBean.setImgUrl(storageResVo.getFileUrl());
        return JSON.toJSONString(webSocketBean);
    }

    private void sendMessageForFaceAlignment(Integer userId, EmbeddedEntity embeddedEntity, String message) throws IOException {
        WebSocketBean webSocketBean = new WebSocketBean();
        webSocketBean.setType(embeddedEntity.getType());
        webSocketBean.setCode(ContentApi.SUCCESS_CODE.toString());
        webSocketBean.setMatchValue(embeddedEntity.getMatchValue());
        webSocketBean.setMessage(message);
        String failMessage = getFaceAligmentMessage(embeddedEntity, webSocketBean);
        callDeviceWebSocketHandler.sendMessageForBusiness(new TextMessage(failMessage), userId);    }

    /**
     * 心跳机制
     *
     * @param session
     */
    private void heartBeat(WebSocketSession session, HeartBeatBean heartBeatBean) throws IOException {
        heartBeatBean.setVersion(InitializingBeanImpl.version);
        session.sendMessage(new TextMessage(JSON.toJSONString(heartBeatBean)));
        //logger.info("版本号："+JSON.toJSONString(heartBeatBean));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        embeddedServerDao.updateEmbededeServerBySessionId(session.getId(), format.format(System.currentTimeMillis()));
        return;
    }


    /**
     * 发送心跳消息给x86
     * <p>
     * 此方法不用添加打印日志
     */
    private void sendHeartBeatToEmbeddServer(WebSocketMessage<?> message, String vcDevId, Boolean flag) {
        if (sessionList.size() == 0 && Boolean.TRUE.equals(flag)) {
            loseConnection(vcDevId);
            return;
        }
        if (sessionList.size() == 0 && Boolean.FALSE.equals(flag)) {
            return;
        }
        for (WebSocketSession session : sessionList) {
            EmbeddedServerInfo embeddedServer = embeddedServerDao.getEmbeddedServer(session.getId());
            if (embeddedServer != null && embeddedServer.getVcDevId().equals(vcDevId)) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Boolean.TRUE.equals(flag)) {
            loseConnection(vcDevId);
        }
    }

    /**
     * 处理photo
     *
     * @param entity
     * @return
     */
    private EmbeddedEntity checkPhoto(EmbeddedEntity entity) {
        if (entity !=null && entity.getPhoto() != null) {
            Map<String, Object> map = ImageUtil.generateImage(entity.getPhoto(), xpath);
            String filePath = (String) map.get("msg");
            File file = new File(filePath);
            String shaValue = FileUtil.getFileSha1(file);
            //校验文件传输的完整性
            if (shaValue == null || !shaValue.equals(entity.getShaValue())) {
                logger.info("文件接收不完整");
                return entity;
            }
            StorageResVo storageResVo = storageUploadHelper
                    .storageUpload(file, Integer.valueOf(StorageApi.FileType.IMG.getValue()));
            if (storageResVo.getFileUrl() == null) {
                logger.info("存储网关返回文件路径为空");
            }
            if (file.exists()) {
                boolean delete = file.delete();
                logger.info("是否成功刪除" + delete);
            }
            entity.setObject(storageResVo);
            entity.setPhoto(null);
        }
        return entity;
    }

    /**
     * 更新x86配置信息
     */
    private void updateX86ConfigInfo(WebSocketSession session) throws IOException {
        X86ConfigInformationBean configBean = new X86ConfigInformationBean();
        configBean.setConfigType(ConfigTypeEnum.X86ConfigFile.getValue());
        List<X86ConfigInformationBean> list = x86ConfigInformationDao.getList(configBean);
        if (!CollectionUtils.isEmpty(list)) {
            HashMap<String, Object> map = X86ConfigInformationBean.convert(list, CallDeviceEnum.X86Config.getValue());
            session.sendMessage(new TextMessage(JSON.toJSONString(map)));
            logger.info("发送x86配置信息给SCU");
        }
    }

    /**
     * x86升级包升级
     *
     * @param session
     */
    private void scuVersionUp(WebSocketSession session) throws IOException {
        X86ConfigInformationBean configBean = new X86ConfigInformationBean();
        configBean.setColumnNameEn("zipUrl");
        List<X86ConfigInformationBean> list = x86ConfigInformationDao.getList(configBean);
        if (!CollectionUtils.isEmpty(list)) {
            X86ConfigInformationBean x86ConfigInformationBean = list.get(0);
            String zipUrl = x86ConfigInformationBean.getColumnValue();
            EmbeddedBean embeddedBean = new EmbeddedBean();
            embeddedBean.setType(CallDeviceEnum.X86VersionUP.getValue());
            if (StringUtil.isNotEmpty(zipUrl)) {
                //byte[] updateData = FileUtil.imageTobyte(zipUrl);
              //下载路径固定，上传路径和服务器上传路径不一致所以需要分开变量赋值
                embeddedBean.setUrl(zipUrl);
                String fName = zipUrl.trim();
                String fileName = fName.substring(fName.lastIndexOf("/")+1);
                File file = new File(datapath+fileName);
                logger.info("哈希值转换文件路径"+datapath+fileName);
                String shaValue = FileUtil.getFileSha1(file);
                embeddedBean.setShaValue(shaValue);
                //file.delete();
            }
            session.sendMessage(new TextMessage(JSON.toJSONString(embeddedBean)));
            logger.info("发送SCU升级包给SCU");
        }
    }

    /**
     * 通过x86的vcDevId获取前端连接的userId
     *
     * @param vcDevId
     * @return
     */
    public Integer getUserIdByVcDevId(String vcDevId) {
        BusinessInfo businessInfo = businessInfoDao.getBusinessInfoOfTerminalNumber(vcDevId);
        if (businessInfo != null) {
            return businessInfo.getOperatorId();
        }
        return null;
    }

}
