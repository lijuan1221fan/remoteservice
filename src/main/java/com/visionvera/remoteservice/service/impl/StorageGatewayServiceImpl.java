package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.visionvera.api.handler.api.StorageApiHandler;
import com.visionvera.api.handler.bean.EmbeddedEntity;
import com.visionvera.api.handler.bean.StorageEntity;
import com.visionvera.api.handler.bean.StorageInfo;
import com.visionvera.api.handler.bean.res.ResRequest;
import com.visionvera.api.handler.constant.StorageApi.FileType;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.app.service.AppMaterialsService;
import com.visionvera.common.enums.BusinessInfoStateEnum;
import com.visionvera.common.enums.CallDeviceEnum;
import com.visionvera.common.enums.StorageTypeEnum;
import com.visionvera.common.enums.SystemVersionEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.ClientInfo;
import com.visionvera.remoteservice.bean.MeetingOperation;
import com.visionvera.remoteservice.bean.PhotoBean;
import com.visionvera.remoteservice.bean.PhotoBeanVo;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.constant.LogEnums;
import com.visionvera.remoteservice.constant.PictureConstant;
import com.visionvera.remoteservice.constant.RegularExpression;
import com.visionvera.remoteservice.constant.WebConstant;
import com.visionvera.remoteservice.dao.*;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.pojo.StorageGatewayVo;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.BusinessLogService;
import com.visionvera.remoteservice.service.StorageGatewayService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.util.WebUtils;

import java.io.*;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * @author lijintao
 * @date
 */
@Service
public class StorageGatewayServiceImpl implements StorageGatewayService {

    private final Logger logger = LoggerFactory.getLogger(StorageGatewayServiceImpl.class);
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private CommonConfigDao commonConfigDao;
    @Resource
    private MeetingOperationDao meetingOperationDao;
    @Resource
    private ClientInfoDao clientInfoDao;
    @Autowired
    private BusinessLogService businessLogService;
    @Resource
    private PhotoBeanDao photoBeanDao;
    @Resource
    private StorageResDao storageResDao;
    @Resource
    private StorageUploadHelper storageUploadHelper;
    @Resource
    private ServiceCenterDao serviceCenterDao;
    @Resource
    private AppMaterialsService appMaterialsService;
    @Value("${temp.save.downloadpath}")
    private String picpath;
    @Value("${temp.save.path}")
    private String xpath;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 根据业务id获取身份证信息
     *
     * @param businessId 业务id
     * @return
     */
    @Override
    public Map<String, Object> getCardInfoAndModify(Integer businessId) {
        BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
        if (businessInfo == null) {
            logger.info("获取失败，业务不存在，" + businessId + "查询BussinessInfo为空");
            return ResultUtils.error("获取失败，业务不存在");
        }
        //获取服务中心key 根据key查询服务中心
        String serviceKey = businessInfo.getServiceKey();
        //获取终端号码，根据终端号码通过webSocket协议发送定向消息到X86
        String terminalNumber = businessInfo.getTerminalNumber();
        if (StringUtil.isEmpty(serviceKey)) {
            logger.info("当前业务无服务中心唯一标识，key值为空");
            return ResultUtils.error("当前业务无服务中心唯一标识");
        }
        if (terminalNumber == null) {
            logger.info("当前业务终端号码不存在");
            return ResultUtils.error("当前业务终端号码不存在");
        }
        ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
        if (serviceCenter == null) {
            logger.info("key为" + serviceKey + "的服务中心不存在");
            return ResultUtils.error("服务中心不存在");
        }
        try {

            //直接从redis中获取X86返回的消息
            String redisKey = CommonConstant.EMBEDDED_SERVER_KEY + "-" + terminalNumber + "-" + CallDeviceEnum.CardReader.getValue();
            if (redisUtils.get(redisKey) != null) {
                EmbeddedEntity embeddedEntity = (EmbeddedEntity) redisUtils.get(redisKey);
                redisUtils.remove(redisKey);
                StorageResVo storageResVo = (StorageResVo) embeddedEntity.getObject();
                ClientInfo clientInfo = ClientInfo.convert(embeddedEntity);
                clientInfo.setIconPath(storageResVo.getFileUrl());
                ClientInfo clientInfoExist = clientInfoDao.selectByIdcard(clientInfo.getIdcard());
                if (clientInfoExist == null) {
                    clientInfoDao.insertSelective(clientInfo);
                } else {
                    clientInfo.setEducationId(clientInfoExist.getEducationId());
                    clientInfo.setPhone(clientInfoExist.getPhone());
                    clientInfo.setId(clientInfoExist.getId());
                    clientInfo.setSource(clientInfoExist.getSource());
                    clientInfoDao.updateByPrimaryKeySelective(clientInfo);
                }
                // 客户信息与业务信息关联插入
                clientInfoDao.addClientInfoRelevance(businessId, clientInfo.getId(), null, null);
                PhotoBean photo = new PhotoBean();
                photo.setPictureId(storageResVo.getFileId());
                photo.setIconPath(storageResVo.getFileUrl());
                photo.setPictureType(PictureConstant.ID_CARD_HEAD);
                photo.setBusinessId(businessId);
                //插入图片
                photoBeanDao.insertSelective(photo);
                // 插入业务日志
                businessLogService.insertLog(businessId, LogEnums.BUSINESS_IDENTIFICATION_CARD.getCode(), LogEnums.BUSINESS_IDENTIFICATION_CARD.getValue(), photo.getId());
                Map<String, Object> dataMap = new HashMap<>();
                //查询预约材料图片路径
                List<PhotoBean> appointmentMaterialsList = appMaterialsService.getAppointmentMaterialsList(businessId);
                if (appointmentMaterialsList.size() > 0) {
                    dataMap.put("materials", appointmentMaterialsList);
                }
                dataMap.put("data", clientInfo);
                return ResultUtils.ok("获取身份证成功", dataMap);
            }
            logger.info("获取身份证信息失败，x86返回false");
            return ResultUtils.error("获取身份证信息失败");
        } catch (Exception e) {
            logger.info("获取身份证信息异常" + e);
            return ResultUtils.error("获取身份证信息失败");
        }
    }

    /**
     * 扫描照
     *
     * @param businessId  业务key
     * @param pictureType 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照 （目前支持3和4、5）
     * @param materialsId 证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
     * @return
     */
    @Override
    public Map<String, Object> scanningPhotoAndModify(Integer businessId, Integer pictureType,
                                                      Integer materialsId, String cardId, String cardName) {
        //获取服务中心key 根据key查询服务中心
        BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
        if (businessInfo == null) {
            logger.info("获取失败业务不存在，" + businessId + "查询BussinessInfo为空");
            return ResultUtils.error("获取失败，业务不存在");
        }
        //获取业务中的终端号
        String terminalNum = businessInfo.getTerminalNumber();
        if (StringUtil.isEmpty(terminalNum)) {
            logger.info("当前业务无终端号码");
            return ResultUtils.error("当前业务无终端号码");
        }
        String serviceKey = businessInfo.getServiceKey();
        if (StringUtil.isEmpty(serviceKey)) {
            logger.info("当前业务无服务中心唯一标识，key值为空");
            return ResultUtils.error("当前业务无服务中心唯一标识");
        }
        ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
        if (serviceCenter == null) {
            logger.info("key为" + serviceKey + "的服务中心不存在");
            return ResultUtils.error("服务中心不存在");
        }
        LogEnums log;
        switch (pictureType) {
            //签名照
            case PictureConstant.AUTOGRAPHED_PHOTO:
                log = LogEnums.BUSINESS_SIGNED_PHOTO;
                break;
            //指纹照
            case PictureConstant.FINGERPRINT_PHOTO:
                log = LogEnums.BUSINESS_SCAN_FINGERPRINT;
                break;
            //证件照
            case PictureConstant.CERTIFICATE_PHOTO:
                /*if (materialsId == null) {
                    logger.info("获取扫描照失败！缺少证件照类型。");
                    return ResultUtils.error("获取扫描照失败！缺少证件照类型。");
                }*/
                log = LogEnums.BUSINESS_HIGH_SPEED_PHOTOGRAPHIC;
                break;
            default:
                logger.info("获取扫描照失败！图片类型：" + pictureType + "未找到。");
                return ResultUtils.error("获取扫描照失败！图片类型：" + pictureType + "未找到。");
        }
        if (pictureType.equals(PictureConstant.AUTOGRAPHED_PHOTO)) {
            //获取签名照
            try {
                String redisKey = CommonConstant.EMBEDDED_SERVER_KEY + "-" + terminalNum + "-" + CallDeviceEnum.SignatureBoard.getValue();
                if (redisUtils.get(redisKey) != null) {
                    EmbeddedEntity embeddedEntity = (EmbeddedEntity) redisUtils.get(redisKey);
                    redisUtils.remove(redisKey);
                    StorageResVo storageResVo = (StorageResVo) embeddedEntity.getObject();
                    PhotoBean photo = insertPhotoAndBusinessLog(businessId, pictureType, materialsId, log, storageResVo);
                    //saveMaterialForUser(cardId, cardName, photo);
                    logger.info("业务：" + businessId + "，获取签名成功");
                    return ResultUtils.ok("获取签名成功", photo);
                }
                logger.info("获取签名失败，x86返回false");
                return ResultUtils.error("获取签名失败");
            } catch (Exception e) {
                logger.info("获取签名照失败：" + e);
                return ResultUtils.error("获取签名照失败");
            }
        } else if (pictureType.equals(PictureConstant.CERTIFICATE_PHOTO)) {
            //调用高拍仪
            try {
                String redisKey = CommonConstant.EMBEDDED_SERVER_KEY + "-" + terminalNum + "-" + CallDeviceEnum.HighShotMeter.getValue();
                // 直接从redis中获取x86返回的消息
                if (redisUtils.get(redisKey) != null) {
                    EmbeddedEntity embeddedEntity = (EmbeddedEntity) redisUtils.get(redisKey);
                    redisUtils.remove(redisKey);
                    StorageResVo storageResVo = (StorageResVo) embeddedEntity.getObject();
                    PhotoBean photo = insertPhotoAndBusinessLog(businessId, pictureType, materialsId, log, storageResVo);
                    //saveMaterialForUser(cardId, cardName, photo);
                    logger.info("业务：" + businessId + "，获取扫描照成功");
                    return ResultUtils.ok("获取扫描照成功", photo);
                }
                logger.info("获取扫描照失败，x86返回false");
                return ResultUtils.error("获取扫描照失败");
            } catch (Exception e) {
                logger.info("获取扫描照失败：" + e);
                return ResultUtils.error("获取扫描照失败");
            }
        }
        return ResultUtils.error("获取扫描照失败,图片类型未找到。");
    }

    /**
     * 删除图片
     *
     * @param ids         id逗号分割
     * @param businessId  业务id
     * @param pictureType 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照（目前支持1、3和4、5）
     * @return
     */
    @Override
    public Map<String, Object> deletePhoto(String ids, Integer businessId, Integer pictureType) {
        LogEnums log;
        switch (pictureType) {
            //截图
            case PictureConstant.VIDEO_SNAPSHOT:
                log = LogEnums.BUSINESS_DELPRINTSCREEN;
                break;
            //签名照
            case PictureConstant.AUTOGRAPHED_PHOTO:
                log = LogEnums.BUSINESS_DELETE_SIGNED_PHOTO;
                break;
            //指纹照
            case PictureConstant.FINGERPRINT_PHOTO:
                log = LogEnums.BUSINESS_DELETE_SCAN_FINGERPRINT;
                break;
            //证件照
            case PictureConstant.CERTIFICATE_PHOTO:
                log = LogEnums.BUSINESS_DELETE_HIGH_SPEED_PHOTOGRAPHIC;
                break;
            default:
                logger.info("删除扫描照失败！图片类型：" + pictureType + "未找到。");
                return ResultUtils.error("删除扫描照失败！图片类型：" + pictureType + "未找到。");
        }
        PhotoBean photoBean = photoBeanDao.selectByPrimaryKey(Integer.valueOf(ids));
        ResultEntity<StorageInfo> resultEntity = null;
        if (pictureType.equals(PictureConstant.VIDEO_SNAPSHOT)) {
            resultEntity = StorageApiHandler.deletePicture(
                    new StorageEntity(HttpUtil.getIpByUrl(photoBean.getIconPath()),
                            HttpUtil.getPortByUrl(photoBean.getIconPath()),
                            photoBean.getPictureId().toString(), pictureType));
            logger.info("删除描照请求结果：" + resultEntity);
        } else {
            StorageResVo storageResVo = storageResDao.selectStorageByFileId(photoBean.getPictureId());
            if (storageResVo == null) {
                return ResultUtils.ok("删除成功");
            }
            //删除方式修改
            String baseUrl = storageResVo.getStorageUrl();
            if (!StringUtil.isNotNull(baseUrl)) {
                logger.info("删除成功，存储网关路径为空" + storageResVo);
                return ResultUtils.ok("删除成功");
            }
            resultEntity = StorageApiHandler.delFileRes(
                    new ResRequest(HttpUtil.getIpByUrl(baseUrl), HttpUtil.getPortByUrl(baseUrl),
                            storageResVo.getResId()));
        }
        logger.info("删除描照请求结果：" + resultEntity);
        if (resultEntity.getResult() && resultEntity.getData().getResult()) {
            //删除签名照片
            int deleteResult = photoBeanDao.deleteByPrimaryKey(Integer.valueOf(ids));
            if (deleteResult > CommonConstant.zero) {
                //  插入业务日志
                businessLogService.insertLog(businessId, log.getCode(), log.getValue(), null);
            }
            logger.info("业务：" + businessId + "，删除成功");
            return ResultUtils.ok("删除成功");
        } else {
            logger.info("删除失败" + resultEntity);
            return ResultUtils.error("删除失败");
        }
    }

    /**
     * 获取图片列表和录像地址
     *
     * @param businessId 业务id
     * @return
     */
    @Override
    public Map<String, Object> getPhotosAndVideoPath(Integer businessId) {
        //查询业务状态
        BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
        if (businessInfo == null || BusinessInfoStateEnum.Untreated.getValue()
                .equals(businessInfo.getState())) {
            logger.info(businessId + "业务未开始或业务不存在");
            return ResultUtils.error("业务未开始或业务不存在");
        }
        //查询会议id
        MeetingOperation meetingOperation = meetingOperationDao.selectByBusinessId(businessId);
        if (meetingOperation == null || StringUtil.isNull(meetingOperation.getScheduleId())) {
            logger.info(businessId + "未找到对应的会议id");
            return ResultUtils.error("未找到录像信息！");
        }

        String systemVersion = commonConfigDao.getCommonConfigByName("system_version").getCommonValue();
        if (systemVersion == null) {
            return ResultUtils.error("系统版本未配置请配置后重试");
        }

        String scheduleId = meetingOperation.getScheduleId();
        String baseUrl = meetingOperation.getStorageAddress();
        String cmsUri = HttpUtil.getIpByUrl(meetingOperation.getStorageAddress());
        //存储网关type 视频类型，1正在录制的视频，默认值；2录制完毕的视频
        Integer type = 0;
        if (BusinessInfoStateEnum.Processing.getValue().equals(businessInfo.getState())) {
            type = StorageTypeEnum.Processing.getValue();
        } else if (BusinessInfoStateEnum.Processed.getValue().equals(businessInfo.getState())) {
            type = StorageTypeEnum.Processed.getValue();
        }

        ResultEntity<StorageInfo> resultEntity = null;
        if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
            resultEntity = StorageApiHandler.getPhotosAndVideoPath(
                    new StorageEntity(HttpUtil.getIpByUrl(baseUrl), HttpUtil.getPortByUrl(baseUrl),
                            meetingOperation.getMeetingName(), type, null, null));
        } else {
            resultEntity = StorageApiHandler.getPhotosAndVideoPath(new StorageEntity(HttpUtil.getIpByUrl(baseUrl), HttpUtil.getPortByUrl(baseUrl),
                    scheduleId, type, null, null));
        }

        logger.info("获取图片列表和录像地址请求结果：" + resultEntity);
        if (resultEntity.getResult() && resultEntity.getData().getResult()) {
            JSONObject resultData = JSONObject.parseObject(resultEntity.getData().getData());
            Map<String, Object> resultMap = ResultUtils.ok("获取图片列表和录像地址成功", resultData);
            if (StringUtil.isNotNull(cmsUri)) {
                StorageInfo storageInfo = resultEntity.getData();
                JSONObject data = JSONObject.parseObject(storageInfo.getData());
                String rtmppath = data.getString("rtmppath");
                //替换
                rtmppath = rtmppath.replaceAll(RegularExpression.IP_PORT, cmsUri);
                data.put("rtmppath", rtmppath);
                resultMap.put("data", data);
            }
            logger.info("业务：" + businessId + "，获取图片列表和录像地址成功");
            return resultMap;
        } else {
            logger.info("未找到录像信息！");
            return ResultUtils.error("未找到录像信息！");
        }
    }

    /**
     * 截图接口
     *
     * @param businessId 业务id
     * @return
     */
    @Override
    public Map<String, Object> printScreenAndModify(Integer businessId) {
        // 查询会议id
        MeetingOperation meetingOperation = meetingOperationDao.selectByBusinessId(businessId);
        if (meetingOperation == null) {
            logger.info("获取扫描照失败！" + businessId + "未找到对应的会议id");
            return ResultUtils.error("获取扫描照失败,未找到对应的会议id.");
        }

        String systemVersion = commonConfigDao.getCommonConfigByName("system_version").getCommonValue();
        if (systemVersion == null) {
            return ResultUtils.error("系统版本未配置请配置后重试");
        }

        String scheduleId = meetingOperation.getScheduleId();
        String baseUrl = meetingOperation.getStorageAddress();

        StorageEntity storageEntity = new StorageEntity();
        storageEntity.setIp(HttpUtil.getIpByUrl(baseUrl));
        storageEntity.setPort(HttpUtil.getPortByUrl(baseUrl));
        if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
            //64位
            storageEntity.setGuid(meetingOperation.getMeetingName());
            storageEntity.setType(PictureConstant.VIDEO_SNAPSHOT_64);
        } else {
            //16位
            storageEntity.setGuid(scheduleId);
            storageEntity.setType(PictureConstant.VIDEO_SNAPSHOT);
        }

        ResultEntity<StorageInfo> resultEntity = StorageApiHandler.getPicture(storageEntity);
        logger.info("获取截图请求结果：" + resultEntity);

        if (resultEntity.getResult() && resultEntity.getData().getResult()) {
            JSONObject resultData = JSONObject.parseObject(resultEntity.getData().getData());
            //解析数据构造图片对象
            PhotoBean photo = JSON.parseObject(resultEntity.getData().getData(), PhotoBean.class);
            photo.setPictureId(resultData.getInteger("id"));
            photo.setIconPath(resultData.getString("iconpath"));
            photo.setPictureType(PictureConstant.VIDEO_SNAPSHOT);
            photo.setBusinessId(businessId);
            //插入
            int addResult = photoBeanDao.insertSelective(photo);
            if (addResult > CommonConstant.zero) {
                // 插入业务日志
                businessLogService.insertLog(businessId, LogEnums.BUSINESS_PRINTSCREEN.getCode(),
                        LogEnums.BUSINESS_PRINTSCREEN.getValue(), photo.getId());
            }
            PhotoBeanVo photoBeanVo = new PhotoBeanVo();
            BeanUtils.copyProperties(photo, photoBeanVo);
            //此处修改
            resultData.put("data", photo);
            resultData.put("framePath", resultData.getString("framepath"));
            resultData.put("iconPath", resultData.getString("framepath"));
            resultData.put("businessId", businessId);
            logger.info("业务：" + businessId + "，截图成功");
            return ResultUtils.ok("获取截图成功", resultData);
        } else {
            logger.info("业务：" + businessId + "，截图失败");
            return ResultUtils.error("获取截图失败");
        }
    }

    /**
     * 获取图
     *
     * @param businessId  业务id
     * @param materialsId 证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
     * @return
     */
    @Override
    public Map<String, Object> getPrintScreenList(Integer businessId, Integer materialsId) {
        List<PhotoBean> photoList = photoBeanDao.selectByBusinessIdAndMaterialsId(businessId, materialsId);
        List<PhotoBeanVo> photoLists = new ArrayList<>();
        for (PhotoBean list : photoList) {
            PhotoBeanVo photoBeanVo = new PhotoBeanVo();
            BeanUtils.copyProperties(list, photoBeanVo);
            photoLists.add(photoBeanVo);
        }
        logger.info("查看截图列表成功");
        // return ResultUtils.ok("查看截图列表成功！", photoLists);
        return ResultUtils.ok("查看截图列表成功！", photoList);
    }

    /**
     * 转发存储网关图片
     *
     * @param id    图片id
     * @param field 要是有的图片字段
     * @return
     */
//  @Override
//  public byte[] img(String id, String field) {
//    String url = photoBeanDao.selectById(id, field);
//    return HttpUtils.sendGet(url);
//  }
    @Override
    public Map<String, Object> downloadZip(String cardName, String cardId, Integer businessId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //压缩文件名
        String zipname = cardName + cardId + ".zip";
        //用户材料文件夹名
        String dirNameForPeople = cardName + "-" + cardId;
        //判断当前用户材料文件夹是否存在
        File cusFile = new File(picpath + "/" + dirNameForPeople);
        //存在
        if (cusFile.exists()) {
            try {
                //进行压缩
                Map<String, Object> resultmap = UrlFileUtil.fileToZip(cusFile.getAbsolutePath(), cusFile.getAbsolutePath(), zipname);
                if (resultmap.get("exists") != null && resultmap.get("exists").equals("exists")) {
                    File file = new File(cusFile.getAbsolutePath() + "/", zipname);
                    //判断文件类型
                    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                    if (mimeType == null) {
                        mimeType = "application/octet-stream";
                    }
                    response.setContentType(mimeType);

                    //设置文件响应大小
                    response.setContentLengthLong(file.length());

                    //文件名编码，解决乱码问题
                    String encodedFileName = null;
                    String userAgentString = request.getHeader("User-Agent");
                    String browser = UserAgent.parseUserAgentString(userAgentString).getBrowser().getGroup()
                            .getName();
                    if (browser.equals("Chrome") || browser.equals("Internet Exploer") || browser
                            .equals("Safari")) {
                        encodedFileName = URLEncoder.encode(zipname, "utf-8").replaceAll("\\+", "%20");
                    } else {
                        encodedFileName = URLEncoder.encode(zipname, "utf-8");
                    }

                    //设置Content-Disposition响应头，一方面可以指定下载的文件名，另一方面可以引导浏览器弹出文件下载窗口
                    response
                            .setHeader("Content-Disposition", "attachment;fileName=\"" + encodedFileName + "\"");

                    //文件下载
                    InputStream in = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(in, response.getOutputStream());
                    return ResultUtils.ok("下载成功");
                } else if (resultmap.get(false) != null) {
                    return ResultUtils.error("下载失败，材料不存在");
                } else {
                    File file = new File(cusFile.getAbsolutePath() + "/", zipname);
                    //判断文件类型
                    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                    if (mimeType == null) {
                        mimeType = "application/octet-stream";
                    }
                    response.setContentType(mimeType);

                    //设置文件响应大小
                    response.setContentLengthLong(file.length());

                    //文件名编码，解决乱码问题
                    String encodedFileName = null;
                    String userAgentString = request.getHeader("User-Agent");
                    String browser = UserAgent.parseUserAgentString(userAgentString).getBrowser().getGroup()
                            .getName();
                    if (browser.equals("Chrome") || browser.equals("Internet Exploer") || browser
                            .equals("Safari")) {
                        encodedFileName = URLEncoder.encode(zipname, "utf-8").replaceAll("\\+", "%20");
                    } else {
                        encodedFileName = URLEncoder.encode(zipname, "utf-8");
                    }

                    //设置Content-Disposition响应头，一方面可以指定下载的文件名，另一方面可以引导浏览器弹出文件下载窗口
                    response
                            .setHeader("Content-Disposition", "attachment;fileName=\"" + encodedFileName + "\"");

                    //文件下载
                    InputStream in = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(in, response.getOutputStream());
                    return ResultUtils.ok("下载成功");
                }
            } finally {
                delMaterials(cardName, cardId);
            }
        } else {
            return ResultUtils.error("当前无可下载材料");
        }
    }


    @Override
    public Map<String, Object> checkZip(StorageGatewayVo storageGatewayVo) {
        //查询出当前业务相关业务图片
        List<PhotoBean> photoBeans = photoBeanDao.selectByBusinessId(storageGatewayVo.getBusinessId());
        if (photoBeans.size() > 0) {
            for (PhotoBean photoBean : photoBeans) {
                saveMaterialForUser(storageGatewayVo.getCardId(), storageGatewayVo.getCardName(), photoBean);
            }
            logger.info("下载材料准备完毕");
            return ResultUtils.ok("下载材料准备完毕");
        }
        logger.info("当前无可下载材料");
        return ResultUtils.error("当前无可下载材料");
    }

    //获取x86服务地址路径
    private String getXpath(Integer businessId) {
        BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
        //获取服务中心key 根据key查询服务中心
        String serviceKey = businessInfo.getServiceKey();
        if (StringUtil.isEmpty(serviceKey)) {
            logger.info("当前业务无服务中心唯一标识，key值为空");
            throw new MyException("当前业务无服务中心唯一标识");
        }
        ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
        if (serviceCenter == null) {
            logger.info("key为" + serviceKey + "的服务中心不存在");
            throw new MyException("服务中心不存在");
        }
        String xUrl = serviceCenter.getX86Url();
        if (StringUtil.isEmpty(serviceCenter.getX86Url())) {
            logger.info("X86服务连接失败，服务中心查询x86地址为空");
            throw new MyException("X86服务连接失败");
        }
        return xUrl;
    }

    /**
     * 获取存储网关返回结果
     *
     * @param xUrl
     * @param messageMap
     * @return
     */
    private StorageResVo getStorageResVo(String xUrl, JSONObject messageMap) {
        String downloadUrl = xUrl + messageMap.getString("static_file_route");
        String filePath = UrlFileUtil.downloadFile(downloadUrl, xpath);
        File file = new File(filePath);
        StorageResVo storageResVo = storageUploadHelper
                .storageUpload(file, Integer.valueOf(FileType.IMG.getValue()));
        if (file.exists()) {
            file.delete();
        }
        if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
            logger.info("存储网关返回文件路径为空");
            throw new MyException("存储网关返回文件路径为空");
        }
        return storageResVo;
    }

    /**
     * 获取x86返回信息
     *
     * @param webPath
     * @param callDevice
     * @return
     */
    private JSONObject getMessageMap(String webPath, Integer callDevice) throws Exception {
        Map<String, Object> webConstantMap = WebConstant
                .getWebConstant(callDevice);
        String webConstantMessage = WebUtils.httpPostWithJSON(webPath, webConstantMap);
        logger.info("X86返回信息：" + webConstantMessage);
        return JSONObject.parseObject(webConstantMessage);
    }

    /**
     * 将Photo和业务日志插入数据库
     *
     * @param businessId
     * @param pictureType
     * @param materialsId
     * @param log
     * @param storageResVo
     * @return
     */
    private PhotoBean insertPhotoAndBusinessLog(Integer businessId, Integer pictureType,
                                                Integer materialsId, LogEnums log, StorageResVo storageResVo) {
        PhotoBean photo = new PhotoBean();
        photo.setPictureId(storageResVo.getFileId());
        photo.setIconPath(storageResVo.getFileUrl());
        photo.setPictureType(pictureType);
        photo.setMaterialsId(materialsId);
        photo.setBusinessId(businessId);
        //插入
        int addResult = photoBeanDao.insertSelective(photo);
        if (addResult > CommonConstant.zero) {
            // 插入业务日志
            businessLogService
                    .insertLog(businessId, log.getCode(), log.getValue(), photo.getId());
        }
        return photo;
    }


    /**
     * @author jlm
     * @des 将材料图片保存至服务器
     * @date 2018.1.11
     */
    private void saveMaterialForUser(String cardId, String cardName, PhotoBean photo) {
        String dirNameForPeople = cardName + "-" + cardId;
        File file = new File(picpath);
        if (file.exists()) {
            //存在,创建人员文件夹
            File file1 = new File(file.getPath() + "/" + dirNameForPeople);
            //判断人员文件夹是否存在
            if (file1.exists()) {
                //人员文件夹存在,材料名,使用时间戳命名,避免重复
                UrlFileUtil.download(photo.getIconPath(), String.valueOf(System.currentTimeMillis()),
                        file1.getPath());
                logger.info("保存材料至本地成功");
            } else {
                //人员文件夹不存在
                file1.mkdir();
                UrlFileUtil.download(photo.getIconPath(), String.valueOf(System.currentTimeMillis()),
                        file1.getPath());
                logger.info("保存材料至本地成功");
            }
        } else {
            file.mkdir();
            //创建人员文件夹
            File file2 = new File(file.getPath() + "/" + dirNameForPeople);
            //材料保存
            UrlFileUtil.download(photo.getIconPath(), String.valueOf(System.currentTimeMillis()),
                    file2.getPath());
        }
    }

    private void delMaterials(String cardName, String cardId) {
        if (cardName != null && cardId != null) {
            String dirForPeoName = cardName + "-" + cardId;
            File file = new File(picpath + "/" + dirForPeoName);
            if (file.exists()) {
                UrlFileUtil.delAllFile(picpath);
                file.delete();
            }
        }
    }

}
