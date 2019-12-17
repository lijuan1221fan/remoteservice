package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.api.handler.constant.StorageApi.FileType;
import com.visionvera.common.enums.CallDeviceEnum;
import com.visionvera.common.enums.UploadStateEnum;
import com.visionvera.common.utils.HttpContextUtils;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.constant.PictureConstant;
import com.visionvera.remoteservice.dao.*;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.pojo.UploadVo;
import com.visionvera.remoteservice.service.UploadService;
import com.visionvera.remoteservice.util.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

/**
 * @author 13624
 * @date 2018/7/28
 */
@Service
public class UploadServiceImpl implements UploadService {

  private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
  @Resource
  private UploadBeanDao uploadBeanDao;
  @Resource
  private PhotoConfigDao photoConfigDao;
  @Resource
  private PhotoBeanDao photoBeanDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private DeviceInfoDao deviceInfoDao;
  @Resource
  private StorageUploadHelper storageUploadHelper;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;
    @Resource
    private StorageResDao storageResDao;

  @Value("${temp.save.path}")
  private String tempPath;
  @Value("${temp.save.docpath}")
  private String docTempPath;

  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;

  @Override
  public Map<String, Object> insertSelective(UploadBean uploadBean) {
    uploadBeanDao.insertSelective(uploadBean);
    Map<String, String> map = new HashMap<>();
    map.put("imagePath", uploadBean.getFilePath());
    return ResultUtils.ok("上传成功", map);
  }

  /**
   * @param businessId 业务id
   * @param sing 是否前面
   * @param stamp 是否签盖公章
   * @return
   * @author jlm
   */
  @Override
  public Map<String, Object> createImg(UploadVo uploadVo)
      throws IOException {
    //判断上传的表单图片是否存在
    UploadBean uploadBean = JudgeConditionByUploadBean(uploadVo.getBusinessId());
    //判断业务是否存在
    BusinessInfo businessInfo = JudgeConditionByBusinessInfo(uploadVo.getBusinessId());
    Map<String, String> map = new HashMap<>();
    //上传表单的路径
    map.put("imagePath", uploadBean.getFilePath());
    String uniqueName = uploadBean.getUniqueName();
    String imagePath = UrlFileUtil.downloadFile(uploadBean.getFilePath(), tempPath);
    PhotoConfig record = new PhotoConfig();
    record.setBusinessType(businessInfo.getBusinessType());
    if (uploadVo.getSing() && !uploadVo.getStamp()) {
      record.setType(CommonConstant.SIGN_LOCATION);
    } else if (!uploadVo.getSing() && uploadVo.getStamp()) {
      record.setType(CommonConstant.STAMP_LOCATION);
    } else if (!uploadVo.getSing()) {
      File imageFile = new File(imagePath);
      imageFile.delete();
      return ResultUtils.ok("生成成功", map);
    }
    List<PhotoConfig> photoConfigs = photoConfigDao.selectByObject(record);
    PhotoConfig stampConfig = null;
    for (PhotoConfig photoConfig : photoConfigs) {
      if (photoConfig.getType() == CommonConstant.SIGN_LOCATION) {
        List<PhotoBean> printScreenList = photoBeanDao
            .selectByBusinessIdAndPictureType(uploadVo.getBusinessId(),
                PictureConstant.AUTOGRAPHED_PHOTO);
        if (!printScreenList.isEmpty()) {
          String signPath = "";
          for (PhotoBean photoBean : printScreenList) {
            if (photoBean.getIconPath() != null) {
              //签名url
              signPath = photoBean.getIconPath();
            }
          }
          if (StringUtil.isNotNull(signPath)) {
            //获取签名合成图片Url
              getSignUrl(uploadVo.getBusinessId(), uploadVo.getSingX(), uploadVo.getSingY(), map, uniqueName, imagePath, photoConfig, signPath);
          }
        }
      } else if (photoConfig.getType() == CommonConstant.STAMP_LOCATION) {
        //获取盖章合成图片Url
        stampConfig = getStampUrl(uploadVo.getBusinessId(), stampConfig, uploadVo.getStampX(), uploadVo.getStampY(), uploadBean, map, uniqueName, imagePath, photoConfig);
      }
    }
    if (photoConfigs.size() == 2 && stampConfig != null) {
      SysDeptBean deptBean = sysDeptBeanDao.getDeptStamp(businessInfo.getDeptId());
      if(deptBean == null || deptBean.getStampUrl().isEmpty()){
        return ResultUtils.error("该部门不存在或该部门无章");
      }
      String stampPath = UrlFileUtil.downloadFile(deptBean.getStampUrl(),tempPath);
      String targetPath = tempPath + "/" + uniqueName.replaceAll("\\.",
          "-" + CommonConstant.SIGN_LOCATION + "_" + CommonConstant.STAMP_LOCATION + ".");
      ImageUtil.markByIcon(stampPath,
          tempPath + "/" + uniqueName.replaceAll("\\.", "-" + CommonConstant.SIGN_LOCATION + "."),
          targetPath, uploadVo.getStampX(), uploadVo.getStampY(), stampConfig.getWidth(),
          stampConfig.getHeight());
        getStampAndSignUrl(map, uploadVo.getBusinessId(), targetPath);
      File file = new File(stampPath);
      if(file.exists()){
        file.delete();
      }
    }
    deleteImageFile(uniqueName, imagePath);
    return ResultUtils.ok("生成成功", map);
  }

  @Override
  public Map<String, Object> createImgByAllocation(UploadVo uploadVo) throws IOException {
      UploadBean uploadBean = JudgeConditionByUploadBean(uploadVo.getBusinessId());
      BusinessInfo businessInfo = JudgeConditionByBusinessInfo(uploadVo.getBusinessId());
      Map<String, String> map = new HashMap<>();
      map.put("imagePath", uploadBean.getFilePath());
      String uniqueName = uploadBean.getUniqueName();
      String imagePath = UrlFileUtil.downloadFile(uploadBean.getFilePath(), tempPath);
      PhotoConfig record = new PhotoConfig();
      record.setBusinessType(businessInfo.getBusinessType());
      if (uploadVo.getSing() && !uploadVo.getStamp()) {
        record.setType(CommonConstant.SIGN_LOCATION);
      } else if (!uploadVo.getSing() && uploadVo.getStamp()) {
        record.setType(CommonConstant.STAMP_LOCATION);
      } else if (!uploadVo.getSing()) {
        return ResultUtils.ok("生成成功", map);
      }
      List<PhotoConfig> photoConfigs = photoConfigDao.selectByObject(record);
      PhotoConfig stampConfig = null;
      for (PhotoConfig photoConfig : photoConfigs) {
        if (photoConfig.getType() == CommonConstant.SIGN_LOCATION) {
          List<PhotoBean> printScreenList = photoBeanDao
                  .selectByBusinessIdAndPictureType(uploadVo.getBusinessId(), PictureConstant.AUTOGRAPHED_PHOTO);
          if (!printScreenList.isEmpty()) {
            String signPath = "";
            for (PhotoBean photoBean : printScreenList) {
              if (photoBean.getIconPath() != null) {
                signPath = photoBean.getIconPath();
              }
            }
            if (StringUtil.isNotNull(signPath)) {
                getSignUrl(uploadVo.getBusinessId(), photoConfig.getX(), photoConfig.getY(), map, uniqueName, imagePath, photoConfig, signPath);
            }
          }
        } else if (photoConfig.getType() == CommonConstant.STAMP_LOCATION) {
          stampConfig = getStampUrl(uploadVo.getBusinessId(), stampConfig, photoConfig.getX(), photoConfig.getY(), uploadBean, map, uniqueName, imagePath, photoConfig);
        }
      }
      if (photoConfigs.size() == 2 && stampConfig != null) {
        SysDeptBean deptBean = sysDeptBeanDao.getDeptStamp(businessInfo.getDeptId());
        if(deptBean == null || deptBean.getStampUrl().isEmpty()){
          return ResultUtils.error("该部门不存在或该部门无章");
        }
        String stampPath = UrlFileUtil.downloadFile(deptBean.getStampUrl(),tempPath);
        String targetPath = tempPath + "/" + uniqueName.replaceAll("\\.",
                "-" + CommonConstant.SIGN_LOCATION + "_" + CommonConstant.STAMP_LOCATION + ".");
        ImageUtil.markByIcon(stampPath,
                tempPath + "/" + uniqueName.replaceAll("\\.", "-" + CommonConstant.SIGN_LOCATION + "."),
                targetPath, stampConfig.getX(), stampConfig.getY(), stampConfig.getWidth(),
                stampConfig.getHeight());
          getStampAndSignUrl(map, uploadVo.getBusinessId(), targetPath);
        File file = new File(stampPath);
        if(file.exists()){
          file.delete();
        }
      }
      deleteImageFile(uniqueName, imagePath);
      return ResultUtils.ok("生成成功", map);
  }

  @Override
  public Map<String, Object> upload(String filedescription, Integer businessId, MultipartFile file) {
    try {
      if (!file.isEmpty()) {
        SysUserBean user = ShiroUtils.getUserEntity();
        String originalFilename = file.getOriginalFilename();
        String suffix = FilenameUtils.getExtension(originalFilename);
        String path = FileUploadUtil.uploadFile(tempPath, file, suffix);
        File uploadFile = new File(path);
        StorageResVo storageResVo = storageUploadHelper
                .storageUpload(uploadFile, Integer.valueOf(FileType.IMG.getValue()));
        if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
          logger.info("存储网关返回文件路径为空");
          ResultUtils.error("存储网关返回文件路径为空");
        }
        if (uploadFile.exists()) {
          uploadFile.delete();
        }
        logger.info("文件url：" + storageResVo.getFileUrl());
        String fileName = FilenameUtils.getName(storageResVo.getFileUrl());
        UploadBean uploadBean = new UploadBean(fileName, filedescription, storageResVo.getFileUrl(),
                originalFilename,
                suffix, user.getUserId(), businessId, null);
        logger.info("上传成功");
        return insertSelective(uploadBean);
      } else {
        logger.info("上传失败，文件为空，请选择上传文件");
        return ResultUtils.error("上传失败，文件为空，请选择上传文件");
      }
    } catch (IOException e) {
      logger.info("上传失败：" + e);
      return ResultUtils.error("上传失败，请重试");
    }
  }


  @Override
  public Map<String, Object> uploadWord(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      logger.info("上传材料失败，请选择上传文件");
      return ResultUtils.error("材料为空，请选择正确上传文件");
    }
    String originalFilename = file.getOriginalFilename();
    String fileName = FilenameUtils.removeExtension(originalFilename);
    String suffix = FilenameUtils.getExtension(originalFilename);
    //判断后缀
    if (!CommonConstant.suffixList.contains(suffix)) {
      logger.info("上传材料失败，请选择正确的文件上传");
      return ResultUtils.error("请选择正确的文件上传");
    }
    String path = FileUploadUtil.uploadFile(docTempPath, file, suffix);
    File uploadFile = new File(path);
    StorageResVo storageResVo = storageUploadHelper
            .storageUpload(uploadFile, Integer.valueOf(FileType.WORD.getValue()));
    uploadFile.delete();
    if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
      logger.info("存储网关返回文件路径为空");
      ResultUtils.error("存储网关返回文件路径为空");
    }
    //文件绝对路径
    logger.info("文件绝对路径：" + storageResVo.getFileUrl());
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("path", storageResVo.getFileUrl());
    resultMap.put("name", fileName);
    return ResultUtils.ok("上传成功", resultMap);
  }

  @Override
  public Map<String, Object> dataPrint(Integer businessId, String imgUrl) {
    logger.info("开始远程打印" + "businessId:" + businessId);
    UploadBean uploadBean = uploadBeanDao.selectByBusinessId(businessId);
    if (uploadBean == null) {
      return ResultUtils.error("打印失败，业务材料不存在。");
    }
    logger.info("查询上传文件：" + JSON.toJSONString(uploadBean.toString()));
    BusinessInfo businessInfoBean = businessInfoDao.selectByPrimaryKey(businessId);
    String terminalNumber = businessInfoBean.getTerminalNumber();
    Boolean result;
    String imgPath = UrlFileUtil.downloadFile(imgUrl, tempPath);
    //通过websocket协议传输打印材料
    byte[] baseImage = FileUtil.imageTobyte(imgPath);
    String shaFile = null;
    File file = new File(imgPath);

    shaFile = FileUtil.getFileSha1(file);
    if (file.exists()) {
      file.delete();
    }
    PrintDataBean printDataBean = new PrintDataBean();
    printDataBean.setType(CallDeviceEnum.Printer.getValue());
    ArrayList<EmbeddedBean> list = new ArrayList<>();
    EmbeddedBean embeddedBean = new EmbeddedBean();
    //embeddedBean.setType(CallDeviceEnum.Printer.getValue());
    embeddedBean.setFileData(baseImage);
    embeddedBean.setFormat(imgUrl.substring(imgUrl.lastIndexOf('.') + 1));
    embeddedBean.setFileName(imgUrl.substring(imgUrl.lastIndexOf('/') + 1,
            imgUrl.lastIndexOf('.')));
    embeddedBean.setShaValue(shaFile);
    list.add(embeddedBean);
    printDataBean.setFiles(list);
    try {
        embeddedServerWebSocketHandler.sendMessageToEmbeddServer(
                new TextMessage(JSON.toJSONString(printDataBean)),
                terminalNumber, Boolean.TRUE);
        logger.info("发送打印材料给SCU");
    } catch (Exception e) {
      e.printStackTrace();
      return ResultUtils.error("调用打印机失败");
    }
    return ResultUtils.ok("调用打印机成功");
  }

  @Override
  public Map<String, Object> commonPrint(SynthesisPrint synthesisPrint) {
    BusinessInfo businessInfoBean = businessInfoDao
        .selectByPrimaryKey(synthesisPrint.getBusinessId());
    if (businessInfoBean == null) {
      return ResultUtils.error("业务不存在");
    }
    String terminalNumber = businessInfoBean.getTerminalNumber();

    List<String> urls = synthesisPrint.getUrls();
    PrintDataBean printDataBean = new PrintDataBean();
    printDataBean.setType(CallDeviceEnum.Printer.getValue());
    ArrayList<EmbeddedBean> list = new ArrayList<>();
    for (String url : urls) {
        String absolutePath = UrlFileUtil.downloadFile(url, docTempPath);
        //通过websocket协议传输打印材料
        byte[] baseImage = FileUtil.imageTobyte(absolutePath);
        String shaFile = null;
        File file = new File(absolutePath);
        shaFile = FileUtil.getFileSha1(file);
        EmbeddedBean embeddedBean = new EmbeddedBean();
        //embeddedBean.setType(CallDeviceEnum.Printer.getValue());
        embeddedBean.setFileData(baseImage);
        embeddedBean.setFormat(absolutePath.substring(absolutePath.lastIndexOf('.') + 1));
        embeddedBean.setFileName(absolutePath.substring(absolutePath.lastIndexOf('/') + 1,
            absolutePath.lastIndexOf('.')));
        embeddedBean.setShaValue(shaFile);
        list.add(embeddedBean);
        if (file.exists()) {
          file.delete();
        }
      }
    if(list.size()>0){
      printDataBean.setFiles(list);
      try {
        embeddedServerWebSocketHandler.sendMessageToEmbeddServer(
                new TextMessage(JSON.toJSONString(printDataBean)),
                terminalNumber, Boolean.TRUE);
      } catch (Exception e) {
        e.printStackTrace();
        return ResultUtils.error("调用打印机失败");
      }
      logger.info("发送打印材料信息给SCU");
      return ResultUtils.ok("调用打印机成功");
    }else{
      return ResultUtils.error("无打印材料");
    }
  }
  /**
   * @param businessId 业务id
   * @return 签名图片
   * @author fanlijuan
   */
  @Override
  public Map<String, Object> getPhoto(Integer businessId,Integer pictureType) {
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if (businessInfo == null) {
      logger.error("业务不存在");
      return ResultUtils.error("业务不存在");
    }
    List<PhotoBean> printScreenList = photoBeanDao
            .selectByBusinessIdAndPictureType(businessId, PictureConstant.AUTOGRAPHED_PHOTO);
    String signPath = "";
     if(printScreenList.size() > 0){
       for (PhotoBean photoBean : printScreenList) {
         if (photoBean.getIconPath() != null) {
           signPath = photoBean.getIconPath();
         }
       }
     }else{
       return ResultUtils.error("签名不存在！");
     }
    return ResultUtils.check("获取成功", signPath);
  }

  /**
   * 获取盖章URL
   * @param x
   * @param y
   * @param uploadBean
   * @param map
   * @param uniqueName
   * @param imagePath
   * @param photoConfig
   * @return
   * @throws IOException
   */
  private PhotoConfig getStampUrl(Integer businessId, PhotoConfig stampConfig, Integer x, Integer y, UploadBean uploadBean, Map<String, String> map, String uniqueName, String imagePath, PhotoConfig photoConfig) throws IOException {
    stampConfig = photoConfig;
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if(businessInfo == null){
      throw new MyException("业务不存在");
    }
    SysDeptBean deptBean = sysDeptBeanDao.getDeptStamp(businessInfo.getDeptId());
    if(deptBean == null || deptBean.getStampUrl().isEmpty()){
      throw new MyException("该部门不存在或该部门无章，请确认后重试");
    }
    if (UploadStateEnum.VeriSignPicture.getValue().equals(uploadBean.getState())) {
      map.put("stampPath",deptBean.getStampUrl());
    }
    //章路径
    String stampPath = UrlFileUtil.downloadFile(deptBean.getStampUrl(),tempPath);
    String targetPath = tempPath + "/" + uniqueName
            .replaceAll("\\.", "-" + CommonConstant.STAMP_LOCATION + ".");
    ImageUtil.markByIcon(stampPath, imagePath, targetPath, x, y, photoConfig.getWidth(), photoConfig.getHeight());
    //合成后带章图片路径
    File stampFile = new File(targetPath);
    StorageResVo storageResVo = storageUploadHelper.storageUpload(stampFile, Integer.valueOf(FileType.IMG.getValue()));
      if (storageResVo != null && storageResVo.getFileId() != null) {
          storageResDao.insertStroageRel(businessId, storageResVo.getFileId());
      }
    logger.info("盖章图片生成路径：" + storageResVo.getFileUrl());
    map.put("stampPath", storageResVo.getFileUrl());
    File file = new File(stampPath);
    if(file.exists()){
      file.delete();
    }
    return stampConfig;
  }


  /**
   * 获取签名图片URL
   * @param x
   * @param y
   * @param map
   * @param uniqueName
   * @param imagePath
   * @param photoConfig
   * @param signPath
   * @throws IOException
   */
  private void getSignUrl(Integer businessId, Integer x, Integer y, Map<String, String> map, String uniqueName, String imagePath, PhotoConfig photoConfig, String signPath) throws IOException {
    String targetPath = tempPath + "/" + uniqueName.replaceAll("\\.", "-" + CommonConstant.SIGN_LOCATION + ".");
    logger.info("签名图片生成路径" + targetPath);
      BufferedImage bufferedImage = ImageUtil.compressImage(signPath, photoConfig.getWidth(), photoConfig.getHeight());
      Image signImg = ImageUtil.sign(bufferedImage);
    if (signImg == null) {
      throw new MyException("图片生成失败,签名异常");
    }
      ImageUtil.markBySignIcon(signImg, imagePath, targetPath, x, y, photoConfig.getWidth(), photoConfig.getHeight());
    File signFile = new File(targetPath);
    StorageResVo storageResVo = storageUploadHelper.storageUpload(signFile, Integer.valueOf(FileType.IMG.getValue()));
      if (storageResVo != null && storageResVo.getFileId() != null) {
          storageResDao.insertStroageRel(businessId, storageResVo.getFileId());
      }
    logger.info("签名路径：" + storageResVo.getFileUrl());
    map.put("signPath", storageResVo.getFileUrl());
  }

  /**
   * 获取签名盖章图片URL
   * @param map
   * @param targetPath
   */
  private void getStampAndSignUrl(Map<String, String> map, Integer businessId, String targetPath) {
    File stampAndSignFile = new File(targetPath);
    StorageResVo storageResVo = storageUploadHelper.storageUpload(stampAndSignFile, Integer.valueOf(FileType.IMG.getValue()));
      if (storageResVo != null && storageResVo.getFileId() != null) {
          storageResDao.insertStroageRel(businessId, storageResVo.getFileId());
      }
    map.put("stampAndSignPath", storageResVo.getFileUrl());
    stampAndSignFile.delete();
  }

  /**
   * 判断上传文件是否存在
   * @param businessId
   * @return
   */
  private UploadBean JudgeConditionByUploadBean(Integer businessId){
    UploadBean uploadBean = uploadBeanDao.selectByBusinessId(businessId);
    if (uploadBean == null) {
      logger.error("上传文件不存在");
      throw new MyException("上传文件不存在");
    }
    return uploadBean;
  }

  /**
   * 判断业务是否存在
   * @param businessId
   * @return
   */
  private BusinessInfo JudgeConditionByBusinessInfo(Integer businessId){
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if (businessInfo == null) {
      logger.error("业务不存在");
      throw new MyException("业务不存在");
    }
    return businessInfo;
  }

  /**
   * 删除已上传的文件
   * @param uniqueName
   * @param imagePath
   */
  private void deleteImageFile(String uniqueName, String imagePath) {
    File imageFile = new File(imagePath);
    imageFile.delete();
    File signFile = new File(
            tempPath + "/" + uniqueName.replaceAll("\\.", "-" + CommonConstant.SIGN_LOCATION + "."));
    signFile.delete();
    File stampFile = new File(
            tempPath + "/" + uniqueName.replaceAll("\\.", "-" + CommonConstant.STAMP_LOCATION + "."));
    stampFile.delete();
  }
}

