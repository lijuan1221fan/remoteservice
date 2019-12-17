package com.visionvera.remoteservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.visionvera.api.handler.api.ContentManageApiHandler;
import com.visionvera.api.handler.api.MeetingApiHandler;
import com.visionvera.api.handler.api.StorageApiHandler;
import com.visionvera.api.handler.bean.*;
import com.visionvera.api.handler.bean.res.ResRequest;
import com.visionvera.api.handler.constant.ContentApi;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.common.entity.ServerEntity;
import com.visionvera.common.enums.*;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import com.visionvera.remoteservice.ws.H5WebSocketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jlm on 2019-09-20 11:31
 */
@RestController
@RequestMapping("/detection")
public class PointDetectionController {

    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private CommonConfigDao commonConfigDao;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private H5WebSocketHandler h5WebSocketHandler;
    @Resource
    private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
    @Resource
    private VcDevDao vcDevDao;
    @Resource
    private EmbeddedServerDao embeddedServerDao;
    @Resource
    private ServiceCenterDao serviceCenterDao;

    // 会管 存储网关  内管检测
    @RequestMapping("/server")
    public Map<String, Object> serverDetection() {
        Map<String, Object> dataMap = new HashMap<>(3);
        ServerEntity meetingServerEntity = new ServerEntity();
        ServerEntity storageServerEntity = new ServerEntity();
        ServerEntity contentServerEntity = new ServerEntity();
        //1.会管检测
        boolean meetingFlag = true;

        String meetingUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password").getCommonValue();

        if (StringUtil.isEmpty(meetingUrl)) {
            meetingServerEntity.setResult(false);
            meetingServerEntity.setMsg("会管地址路径为空");
            dataMap.put("MeetingManage", meetingServerEntity);
            meetingFlag = false;
            logger.info("会管检测:" + meetingServerEntity);
        }
        // 1.会管检测
        getMeetingDetectionResult(dataMap, meetingServerEntity, meetingFlag, meetingUrl, loginName, loginPwd);
        // 2.内容管理平台检测
        boolean contentFlag = true;
        String contentUrl = commonConfigDao.getCommonConfigByName("cms_url").getCommonValue();
        String storageIp = "";
        contentFlag = isContentFlag(dataMap, contentServerEntity, contentFlag, contentUrl);
        Map contentDetectionResultMap = getContentDetectionResult(dataMap, contentServerEntity, contentFlag, contentUrl, storageIp);
        // 3.存储网关平台检测
        getStorageDetectionResult(dataMap, storageServerEntity, contentServerEntity, (Boolean) contentDetectionResultMap.get("contentFlag"), (String) contentDetectionResultMap.get("storageIp"));
        return ResultUtils.ok("获取服务端检测信息成功", dataMap);
    }

    // 叫号机检测
    @RequestMapping("/android")
    public Map<String, Object> androidDetection(@RequestParam("serviceKey") String serviceKey) {
        Map<String, Object> dataMap = new HashMap<>(3);
        ServerEntity androidRelationEntity = new ServerEntity();
        //根据serviceKey查找对应村的叫号机H5 session
        String queryKey = CommonConstant.WEBSOCKET_KEY + serviceKey;
        String value = (String) redisUtils.get(queryKey);
        if (StringUtil.isEmpty(value)) {
            LoseRelationForAndroidResult(dataMap, androidRelationEntity);
            return ResultUtils.ok("获取叫号机检测数据成功", getDisableCircularReferenceDetectMap(dataMap));
        }
        String clearKey = CommonConstant.ANDROID_DETECTION_KEY + serviceKey;
        redisUtils.remove(clearKey);
        logger.info("执行清除上一次叫号机检测数据");
        String[] queuesArray = value.split(":");
        String sessionId = queuesArray[1];
        WebSocketBean webSocketBean = new WebSocketBean();
        webSocketBean.setType(CommonConstant.H5_DETECTION);
        logger.info("开始发送叫号机检测消息至H5");
        h5WebSocketHandler.sendMessageToH5(new TextMessage(JSON.toJSONString(webSocketBean)), sessionId);
        logger.info("完成发送叫号机检测消息至H5");
        return ResultUtils.ok("叫号机检测消息");
    }

    //获取叫号机检测数据
    @RequestMapping("/getAndroidDetectionData")
    public Map<String, Object> getAndroidDetection(@RequestParam("serviceKey") String serviceKey) {
        String queryKey = CommonConstant.ANDROID_DETECTION_KEY + serviceKey;
        WebSocketBean androidData = (WebSocketBean) redisUtils.get(queryKey);
        Map<String, Object> dataMap = new HashMap<>(3);
        ServerEntity androidEntity = new ServerEntity();
        if (androidData != null) {
            getAndoroidDetectionResult(androidData, dataMap, androidEntity);
            redisUtils.remove(queryKey);
            return ResultUtils.ok("获取叫号机检测数据成功", dataMap);
        } else {
            logger.info("获取叫号机检测数据为空");
            return ResultUtils.ok("叫号机检测数据消息");
        }
    }

    // SCU检测
    @RequestMapping("/scu")
    public Map<String, Object> scuDetection(@RequestParam("serviceKey") String serviceKey) {
        if (sendDetectionMessageToScu(serviceKey, CallDeviceEnum.ScuDetection.getValue())) {
            return ResultUtils.ok("SCU检测消息");
        }
        return LoseScuRelation();
    }

    //获取SCU检测信息
    @RequestMapping("/getScuDetectionData")
    public Map<String, Object> getScuDetectionData(@RequestParam("serviceKey") String serviceKey) {
        Map<String, Object> dataMap = new HashMap<>();
        //根据serviceKey去查询对应的服务中心是否绑定终端
        List<VcDevBean> vcDevBeanList = vcDevDao.getDeviceByServiceKey(serviceKey);
        if (vcDevBeanList.size() > 0) {
            //获取村终端id
            String deviceId = vcDevBeanList.get(0).getId();
            //因为调用此接口时，说明已经成功发送消息至SCU,即SCU连接存在
            EmbeddedEntity embeddedEntity = (EmbeddedEntity) redisUtils.get(CommonConstant.SCU_DETECTION_KEY + deviceId);
            logger.info("SCU返回的检测结果：" + embeddedEntity);
            if (embeddedEntity != null) {
                if (CallDeviceEnum.ANOMALOUS.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
                    redisUtils.remove(CommonConstant.SCU_DETECTION_KEY + deviceId);
                    return getAnomalousData(dataMap);
                }
                //SCU连接关系
                ServerEntity scuRelationDetectionData = getScuRelationDetectionData();
                //高拍仪
                ServerEntity highDetectionData = getHighDetectionData(embeddedEntity);
                //签字版
                ServerEntity signNatureDetectionData = getSignNatureDetectionData(embeddedEntity);
                //身份证
                ServerEntity cardReaderDetectionData = getCardReaderDetectionData(embeddedEntity);
                //打印机
                ServerEntity printerDetectionData = getPrinterDetectionData(embeddedEntity);
                //存入dataMap，并清除redis缓存结果
                redisUtils.remove(CommonConstant.SCU_DETECTION_KEY + deviceId);
                return ResultUtils.ok("SCU消息", getScuDataMap(dataMap, scuRelationDetectionData, highDetectionData, signNatureDetectionData, cardReaderDetectionData, printerDetectionData));
            } else {
                logger.info("获取SCU检测结果为null");
                return ResultUtils.ok("SCU检测数据消息");
            }
        }
        return LoseScuRelation();
    }


    //发送检测终端消息
    @RequestMapping("/terminal")
    public Map<String, Object> terminalDetection(@RequestParam("serviceKey") String serviceKey) {
        if (sendDetectionMessageToScu(serviceKey, CallDeviceEnum.DeviceDetection.getValue())) {
            return ResultUtils.ok("终端检测消息");
        }
        return LoseScuRelation();
    }

    //获取终端检测信息
    @RequestMapping("getTerminalDetectionData")
    public Map<String, Object> getTerminalDetectionData(@RequestParam("serviceKey") String serviceKey) {
        Map<String, Object> dataMap = new HashMap<>();
        //根据serviceKey去查询对应的服务中心是否绑定终端
        List<VcDevBean> vcDevBeanList = vcDevDao.getDeviceByServiceKey(serviceKey);
        if (vcDevBeanList.size() > 0) {
            //获取村终端id
            String deviceId = vcDevBeanList.get(0).getId();
            //因为调用此接口时，说明已经成功发送消息至SCU,即SCU连接存在
            EmbeddedEntity embeddedEntity = (EmbeddedEntity) redisUtils.get(CommonConstant.DEVICE_DETECTION_KEY + deviceId);
            logger.info("SCU返回的检测结果：" + embeddedEntity);
            if (embeddedEntity != null) {
                if (CallDeviceEnum.ANOMALOUS.getValue().equals(Integer.parseInt(embeddedEntity.getType()))) {
                    redisUtils.remove(CommonConstant.DEVICE_DETECTION_KEY + deviceId);
                    return getAnomalousDataForTerminal(dataMap);
                }
                ServerEntity terminalStatusEntity = new ServerEntity();
                ServerEntity terminalVideoEntity = new ServerEntity();
                ServerEntity terminalAudioEntity = new ServerEntity();
                //清除redis缓存结果
                redisUtils.remove(CommonConstant.DEVICE_DETECTION_KEY + deviceId);
                if (TerminalStatusEnum.NORMAL.getValue().equals(embeddedEntity.getStatus())) {
                    // 终端状态正常
                    logger.info("检测终端状态正常");
                    terminalNormalHandle(embeddedEntity, terminalStatusEntity, terminalVideoEntity, terminalAudioEntity);
                    return getTerminalResultMap(dataMap, terminalStatusEntity, terminalVideoEntity, terminalAudioEntity);
                } else {
                    //终端状态不正常，则视频 音频直接异常
                    logger.info("检测终端状态异常");
                    terminalAbnormalHandle(embeddedEntity, terminalStatusEntity, terminalVideoEntity, terminalAudioEntity);
                    return getTerminalResultMap(dataMap, terminalStatusEntity, terminalVideoEntity, terminalAudioEntity);
                }
            } else {
                logger.info("获取终端检测数据结果为null");
                return ResultUtils.ok("终端检测数据消息");
            }
        }
        return LoseScuRelation();
    }

    @RequestMapping("/getServiceCenter")
    public Map<String, Object> getServiceCenter(@RequestParam("serviceKey") String serviceKey) {
        List<String> list = new ArrayList<>();
        list.add(serviceKey);
        List<ServiceCenterBean> serviceCenterListByParentKey = serviceCenterDao.getServiceCenterListByParentKey(list);
        if (serviceCenterListByParentKey.size() > 0) {
            return ResultUtils.ok("获取服务中心成功", serviceCenterListByParentKey);
        }
        return ResultUtils.error("该审批中心下无服务中心");
    }

    //获取内管检测结果
    private Map getContentDetectionResult(Map<String, Object> dataMap, ServerEntity contentServerEntity, boolean contentFlag, String contentUrl, String storageIp) {
        if (contentFlag) {
            ResultEntity contentEntity = ContentManageApiHandler.getStorageIp(new ContentEntity(HttpUtil.getIpByUrl(contentUrl), HttpUtil.getPortByUrl(contentUrl)));
            logger.info("内容管理平台检测返回信息：" + contentEntity);
            if (contentEntity.getResult()) {
                ContentInfo contentInfo = (ContentInfo) contentEntity.getData();
                ContentEntity resContentEntity = JSONObject.parseObject(contentInfo.getData(), ContentEntity.class);
                //拼接存储网关路径
                storageIp = ContentApi.PROTOCOL + resContentEntity.getServerIp() + ":" + resContentEntity.getServerHost();
                contentServerEntity.setResult(true);
                contentServerEntity.setMsg("内容管理平台正常");
                dataMap.put("ContentManage", contentServerEntity);
                logger.info("内管检测：" + contentServerEntity);
            } else {
                contentServerEntity.setResult(false);
                contentServerEntity.setMsg(contentEntity.getData().toString());
                dataMap.put("ContentManage", contentServerEntity);
                contentFlag = false;
                logger.info("内管检测：" + contentServerEntity);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("storageIp", storageIp);
        map.put("contentFlag", contentFlag);
        return map;
    }

    //处理内管flag 以判断是否检测存储网关
    private boolean isContentFlag(Map<String, Object> dataMap, ServerEntity contentServerEntity, boolean contentFlag, String contentUrl) {
        if (StringUtil.isEmpty(contentUrl)) {
            contentServerEntity.setResult(false);
            contentServerEntity.setMsg("内容管理服务地址路径为空");
            dataMap.put("ContentManage", contentServerEntity);
            contentFlag = false;
            logger.info("内管检测：" + contentServerEntity);
        }
        return contentFlag;
    }

    //获取存储网关检测结果
    private void getStorageDetectionResult(Map<String, Object> dataMap, ServerEntity storageServerEntity, ServerEntity contentServerEntity, boolean contentFlag, String storageIp) {
        if (contentFlag) {
            ResultEntity dummyDeviceId = StorageApiHandler.getDummyDeviceId(new ResRequest(HttpUtil.getIpByUrl(storageIp), HttpUtil.getPortByUrl(storageIp), null, "1"));
            logger.info("存储网关检测返回信息：" + dummyDeviceId);
            if (dummyDeviceId.getResult()) {
                storageServerEntity.setResult(true);
                storageServerEntity.setMsg("存储网关服务正常");
                dataMap.put("StorageManage", storageServerEntity);
                logger.info("存储网关检测：" + storageServerEntity);
            } else {
                String storageResponse = (String) dummyDeviceId.getData();
                if (storageResponse.contains("T")) {
                    storageServerEntity.setResult(false);
                    storageServerEntity.setMsg(storageResponse);
                    dataMap.put("StorageManage", storageServerEntity);
                    logger.info("存储网关检测：" + contentServerEntity);
                } else {
                    StorageInfo storageInfo = JSON.parseObject(storageResponse, StorageInfo.class);
                    storageServerEntity.setResult(false);
                    storageServerEntity.setMsg(storageInfo.getMsg());
                    dataMap.put("StorageManage", storageServerEntity);
                    logger.info("存储网关检测：" + contentServerEntity);
                }
            }
        } else {
            storageServerEntity.setResult(false);
            storageServerEntity.setMsg("存储网关路径获取失败");
            dataMap.put("StorageManage", storageServerEntity);
            logger.info("存储网关检测：" + storageServerEntity);
        }
    }

    //获取会管检测结果
    private void getMeetingDetectionResult(Map<String, Object> dataMap, ServerEntity meetingServerEntity, boolean meetingFlag, String meetingUrl, String loginName, String loginPwd) {
        if (StringUtil.isEmpty(loginName)) {
            meetingServerEntity.setResult(false);
            meetingServerEntity.setMsg("会管登录用户名为空");
            dataMap.put("MeetingManage", meetingServerEntity);
            meetingFlag = false;
            logger.info("会管检测:" + meetingServerEntity);
        }
        if (meetingFlag) {
            if (StringUtil.isEmpty(loginPwd)) {
                meetingServerEntity.setResult(false);
                meetingServerEntity.setMsg("会管登录密码为空");
                dataMap.put("MeetingManage", meetingServerEntity);
                meetingFlag = false;
                logger.info("会管检测:" + meetingServerEntity);
            }
        }
        if (meetingFlag) {
            ResultEntity resultEntity = MeetingApiHandler.doRegister(HttpUtil.getIpByUrl(meetingUrl), HttpUtil.getPortByUrl(meetingUrl), loginName, loginPwd);
            logger.info("会管检测返回信息：" + resultEntity);
            if (resultEntity.getResult()) {
                meetingServerEntity.setResult(true);
                meetingServerEntity.setMsg("会议管理平台正常");
                dataMap.put("MeetingManage", meetingServerEntity);
                logger.info("会管检测：" + meetingServerEntity);
            } else {
                String responseResult = (String) resultEntity.getData();
                if (responseResult.contains("T")) {
                    meetingServerEntity.setResult(false);
                    meetingServerEntity.setMsg(responseResult);
                    dataMap.put("MeetingManage", meetingServerEntity);
                    logger.info("会管检测：" + meetingServerEntity);
                } else {
                    MeetingInfo meetingInfo = JSON.parseObject(responseResult, MeetingInfo.class);
                    meetingServerEntity.setResult(false);
                    meetingServerEntity.setMsg(meetingInfo.getErrmsg());
                    dataMap.put("MeetingManage", meetingServerEntity);
                    logger.info("会管检测：" + meetingServerEntity);
                }
            }
        }
    }

    //获取安卓检测结果
    private void getAndoroidDetectionResult(WebSocketBean androidData, Map<String, Object> dataMap, ServerEntity androidEntity) {
        ServerEntity androidCardReaderEntity = new ServerEntity();
        ServerEntity androidPrintModeEntity = new ServerEntity();
        //判断身份证模块
        if (androidData.getCardReader().equals(ContentApi.SUCCESS_CODE)) {
            androidCardReaderEntity.setResult(true);
            androidCardReaderEntity.setMsg(AndroidDetectionCardReaderEnum.NORMAL.getName());
        } else {
            androidCardReaderEntity.setResult(false);
            androidCardReaderEntity.setMsg(AndroidDetectionCardReaderEnum.ABNORMAL.getName());
        }
        dataMap.put("AndroidCardReader", androidCardReaderEntity);
        logger.info("叫号机身份证模块：" + androidCardReaderEntity);
        if (androidData.getType() != null && androidData.getType().equals(CommonConstant.H5_DETECTION)) {
            //判断打印模块
            if (androidData.getPrintMode().equals(ContentApi.SUCCESS_CODE)) {
                androidPrintModeEntity.setResult(true);
                androidPrintModeEntity.setMsg(AndroidDetectionPrintModeEnum.NORMAL.getName());
            } else {
                androidPrintModeEntity.setResult(false);
                androidPrintModeEntity.setMsg(AndroidDetectionPrintModeEnum.valueToEnum(androidData.getPrintMode()).getName());
            }
            dataMap.put("AndroidPrintMode", androidPrintModeEntity);
            logger.info("叫号机打印模块：" + androidPrintModeEntity);
        }
        androidEntity.setResult(true);
        androidEntity.setMsg("与叫号机连接正常");
        dataMap.put("AndroidRelation", androidEntity);
    }

    //与安卓失去连接返回结果
    private void LoseRelationForAndroidResult(Map<String, Object> dataMap, ServerEntity androidRelationEntity) {
        androidRelationEntity.setResult(false);
        androidRelationEntity.setMsg("设备未连接");
        dataMap.put("AndroidRelation", androidRelationEntity);
        dataMap.put("AndroidCardReader", androidRelationEntity);
        dataMap.put("AndroidPrintMode", androidRelationEntity);
        logger.info("叫号机检测：失去连接");
    }

    //获取SCU连接检测结果
    private ServerEntity getScuRelationDetectionData() {
        ServerEntity scuRelationDetection = new ServerEntity();
        scuRelationDetection.setResult(true);
        scuRelationDetection.setMsg("scu连接正常");
        return scuRelationDetection;
    }

    //SCU返回CODE不为0时  按异常处理
    private Map<String, Object> getAnomalousData(Map dataMap) {
        ServerEntity anomalousEntity = new ServerEntity();
        anomalousEntity.setResult(false);
        anomalousEntity.setMsg("检测异常");
        logger.info("SCU返回SCU检测消息code非0");
        return ResultUtils.ok("SCU消息", getDisableCircularReferenceDetectMap(getScuDataMap(dataMap, anomalousEntity, anomalousEntity, anomalousEntity, anomalousEntity, anomalousEntity)));
    }

    private Map<String, Object> getAnomalousDataForTerminal(Map dataMap) {
        ServerEntity anomalousEntity = new ServerEntity();
        anomalousEntity.setResult(false);
        anomalousEntity.setMsg("检测异常");
        logger.info("SCU返回终端检测消息code非0");
        return ResultUtils.ok("SCU消息", getDisableCircularReferenceDetectMap(getTerminalDataMap(dataMap, anomalousEntity, anomalousEntity, anomalousEntity, anomalousEntity)));
    }



    //获取SCU打印模块检测结果
    private ServerEntity getPrinterDetectionData(EmbeddedEntity embeddedEntity) {
        ServerEntity printDetection = new ServerEntity();
        if (ContentApi.SUCCESS_CODE.equals(embeddedEntity.getPrintStatus())) {
            printDetection.setResult(true);
            printDetection.setMsg(PrintStatusEnum.NORMAL.getName());
        } else {
            printDetection.setResult(false);
            printDetection.setMsg(PrintStatusEnum.valueToEnum(embeddedEntity.getPrintStatus()).getName());
        }
        return printDetection;
    }

    //获取SCU身份证模块检测结果
    private ServerEntity getCardReaderDetectionData(EmbeddedEntity embeddedEntity) {
        ServerEntity cardDetection = new ServerEntity();
        if (ContentApi.SUCCESS_CODE.equals(embeddedEntity.getIdCardStatus()) || IdCardStatusEnum.NOCARD.getValue().equals(embeddedEntity.getIdCardStatus())) {
            cardDetection.setResult(true);
            cardDetection.setMsg(IdCardStatusEnum.valueToEnum(embeddedEntity.getIdCardStatus()).getName());
        } else {
            cardDetection.setResult(false);
            cardDetection.setMsg(IdCardStatusEnum.valueToEnum(embeddedEntity.getIdCardStatus()).getName());
        }
        return cardDetection;
    }

    //获取签字版模块检测结果
    private ServerEntity getSignNatureDetectionData(EmbeddedEntity embeddedEntity) {
        ServerEntity signNatureDetection = new ServerEntity();
        if (embeddedEntity.signatureStatus.equals(ContentApi.SUCCESS_CODE)) {
            signNatureDetection.setResult(true);
            signNatureDetection.setMsg(SignatureStatusEnum.NORMAL.getName());
        } else {
            signNatureDetection.setResult(false);
            signNatureDetection.setMsg(SignatureStatusEnum.valueToEnum(embeddedEntity.getSignatureStatus()).getName());
        }
        return signNatureDetection;
    }

    //获取高拍仪模块检测结果
    private ServerEntity getHighDetectionData(EmbeddedEntity embeddedEntity) {
        ServerEntity highPhotoDetection = new ServerEntity();
        if (embeddedEntity.getHighPhotoStatus().equals(ContentApi.SUCCESS_CODE)) {
            highPhotoDetection.setResult(true);
            highPhotoDetection.setMsg(HighPhotoStatusEnum.NORMAL.getName());
        } else {
            highPhotoDetection.setResult(false);
            highPhotoDetection.setMsg(HighPhotoStatusEnum.valueToEnum(embeddedEntity.getHighPhotoStatus()).getName());
        }
        return highPhotoDetection;
    }

    //发送检测消息至SCU 终端检测  SCU检测均走此方法
    private boolean sendDetectionMessageToScu(String serviceKey, Integer type) {
        //根据serviceKey去查询对应的服务中心是否绑定终端
        List<VcDevBean> vcDevBeanList = vcDevDao.getDeviceByServiceKey(serviceKey);
        if (vcDevBeanList != null && vcDevBeanList.size() > 0) {
            //获取村终端id
            String deviceId = vcDevBeanList.get(0).getId();
            logger.info("查询的服务中心已绑定终端：" + deviceId);
            //根据村终端id判断是否存在x86关联
            EmbeddedServerInfo embeddedServerByVcDevId = embeddedServerDao.getEmbeddedServerByVcDevId(deviceId);
            //如若存在，给x86发送检测消息
            if (embeddedServerByVcDevId != null) {
                TypeBean typeBean = new TypeBean();
                if (type.equals(CallDeviceEnum.ScuDetection.getValue())) {
                    logger.info("执行清除上一次SCU检测数据");
                    redisUtils.remove(CommonConstant.SCU_DETECTION_KEY + deviceId);
                    logger.info("开始发送发送检测SCU消息");
                    typeBean.setType(CallDeviceEnum.ScuDetection.getValue());
                } else {
                    logger.info("执行清除上一次终端检测数据");
                    redisUtils.remove(CommonConstant.DEVICE_DETECTION_KEY + deviceId);
                    logger.info("开始发送发送检测终端消息");
                    typeBean.setType(CallDeviceEnum.DeviceDetection.getValue());
                }
                embeddedServerWebSocketHandler.sendMessageToEmbeddServer(new TextMessage(JSON.toJSONString(typeBean)), deviceId, false);
                return true;
            }
        }
        return false;
    }

    //与SCU失去连接处理
    private Map<String, Object> LoseScuRelation() {
        Map<String, Object> dataMap = new HashMap<>();
        ServerEntity serverEntity = new ServerEntity();
        serverEntity.setResult(false);
        serverEntity.setMsg("设备未连接");
        logger.info("查询不到SCU关联");
        getScuDataMap(dataMap, serverEntity, serverEntity, serverEntity, serverEntity, serverEntity);
        return ResultUtils.ok("SCU返回信息", getDisableCircularReferenceDetectMap(dataMap));
    }

    //SCU检测最终返回数据处理
    private Map<String, Object> getScuDataMap(Map<String, Object> dataMap, ServerEntity scuRelationDetectionData, ServerEntity highDetectionData, ServerEntity signNatureDetectionData, ServerEntity cardReaderDetectionData, ServerEntity printerDetectionData) {
        dataMap.put("scuRelation", scuRelationDetectionData);
        dataMap.put("highPhotoStatus", highDetectionData);
        dataMap.put("idCardStatus", cardReaderDetectionData);
        dataMap.put("signatureStatus", signNatureDetectionData);
        dataMap.put("printStatus", printerDetectionData);
        return dataMap;
    }

    //终端检测返回数据处理
    private Map<String, Object> getTerminalDataMap(Map<String, Object> dataMap, ServerEntity scuRelationDetectionData, ServerEntity terminalStatusEntity, ServerEntity terminalVideoEntity, ServerEntity terminalAudioEntity) {
        scuRelationDetectionData.setResult(true);
        dataMap.put("scuRelation", scuRelationDetectionData);
        dataMap.put("terminalStatus", terminalStatusEntity);
        dataMap.put("terminalVideo", terminalVideoEntity);
        dataMap.put("terminalAudio", terminalAudioEntity);
        return dataMap;
    }

    //终端异常结果处理
    private void terminalAbnormalHandle(EmbeddedEntity embeddedEntity, ServerEntity terminalStatusEntity, ServerEntity terminalVideoEntity, ServerEntity terminalAudioEntity) {
        terminalStatusEntity.setResult(false);
        terminalStatusEntity.setMsg(TerminalStatusEnum.valueToEnum(embeddedEntity.getStatus()).getName());
        terminalVideoEntity.setResult(false);
        terminalVideoEntity.setMsg(TerminalVideoEnum.ABNORMAL.getName());
        terminalAudioEntity.setResult(false);
        terminalAudioEntity.setMsg(TerminalAudioEnum.ABNORMAL.getName());
    }

    //终端正常结果处理
    private void terminalNormalHandle(EmbeddedEntity embeddedEntity, ServerEntity terminalStatusEntity, ServerEntity terminalVideoEntity, ServerEntity terminalAudioEntity) {
        terminalStatusEntity.setResult(true);
        terminalStatusEntity.setMsg(TerminalStatusEnum.NORMAL.getName());
        //判断视频状态
        if (TerminalVideoEnum.NORMAL.getValue().equals(embeddedEntity.getVideo())) {
            terminalVideoEntity.setResult(true);
            terminalVideoEntity.setMsg(TerminalVideoEnum.NORMAL.getName());
        } else {
            terminalVideoEntity.setResult(false);
            terminalVideoEntity.setMsg(TerminalVideoEnum.ABNORMAL.getName());
        }
        //判断音频状态
        if (TerminalAudioEnum.NORMAL.getValue().equals(embeddedEntity.getAudio())) {
            terminalAudioEntity.setResult(true);
            terminalAudioEntity.setMsg(TerminalAudioEnum.NORMAL.getName());
        } else {
            terminalAudioEntity.setResult(false);
            terminalAudioEntity.setMsg(TerminalAudioEnum.ABNORMAL.getName());
        }
    }

    //终端检测最终返回结果处理
    private Map<String, Object> getTerminalResultMap(Map<String, Object> dataMap, ServerEntity terminalStatusEntity, ServerEntity terminalVideoEntity, ServerEntity terminalAudioEntity) {
        dataMap.put("terminalStatus", terminalStatusEntity);
        dataMap.put("terminalVideo", terminalVideoEntity);
        dataMap.put("terminalAudio", terminalAudioEntity);
        return ResultUtils.ok("终端消息", dataMap);
    }

    //处理对象重复引用
    private Map getDisableCircularReferenceDetectMap(Map<String, Object> dataMap) {
        String dataJson = JSON.toJSONString(dataMap, SerializerFeature.DisableCircularReferenceDetect);
        return JSON.parseObject(dataJson, Map.class);
    }

}
