package com.visionvera.remoteservice.controller;

import com.visionvera.common.utils.HttpContextUtils;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.common.validator.group.PrintGroup;
import com.visionvera.common.validator.group.SignPictureGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SynthesisPrint;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.pojo.UploadVo;
import com.visionvera.remoteservice.service.UploadService;
import com.visionvera.remoteservice.util.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author qbk
 * @date 2018/7/27
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

  private Logger logger = LogManager.getLogger(getClass());

  @Autowired
  private UploadService uploadService;
  @Resource
  private StorageUploadHelper storageUploadHelper;
  @Value("${temp.save.path}")
  private String tempPath;
  @Value("${temp.save.docpath}")
  private String docTempPath;

  /**
   * @param filedescription
   * @param businessId
   * @param file
   * @return
   * @author jlm
   */
  @RequestMapping(value = "/file", method = RequestMethod.POST)
  public Map<String, Object> upload(@RequestParam(value = "filedescription", defaultValue = "无描述") String filedescription,
      @RequestParam(value = "businessId") Integer businessId,
      @RequestParam("file") MultipartFile file) {
    return uploadService.upload(filedescription,businessId,file);
  }

  @RequestMapping(value = "/createImg", method = RequestMethod.POST)
  public Map<String, Object> createImg(@RequestBody UploadVo uploadVo) {
    try {
      ValidateUtil.validate(uploadVo);
      return uploadService.createImg(uploadVo);
    } catch (Exception e) {
        logger.error("图片获取失败,请检查是否配置正确", e);
        return ResultUtils.error("图片获取失败,请检查是否配置正确");
    }
  }

  @RequestMapping(value = "/createImgByAllocation",method = RequestMethod.POST)
  public Map<String, Object> createImgByAllocation(@RequestBody UploadVo uploadVo) {
    try {
      ValidateUtil.validate(uploadVo);
      return uploadService.createImgByAllocation(uploadVo);
    } catch (Exception e) {
        logger.error("图片获取失败,请检查是否配置正确", e);
        return ResultUtils.error("图片获取失败,请检查是否配置正确");
    }
  }

  @RequestMapping(value = "/dataPrint", method = RequestMethod.POST)
  public Map<String, Object> dataPrint(@RequestBody UploadVo uploadVo) {
    ValidateUtil.validate(uploadVo, PrintGroup.class);
    return uploadService.dataPrint(uploadVo.getBusinessId(), uploadVo.getImgUrl());
  }

  /**
   * 综合业务上传接口
   *
   * @param file
   * @return
   */
  @RequestMapping(value = "/uploadWord", method = RequestMethod.POST)
  public Map<String, Object> uploadWord(MultipartFile file) throws IOException {
    return uploadService.uploadWord(file);
  }

  /**
   * 　　* @Description: 综合业务打印 　　* @author: xueshiqi 　　* @date: 2018/12/20
   */
  @PostMapping("/commonPrint")
  public Map<String, Object> commonPrint(@RequestBody SynthesisPrint synthesisPrint) {
    ValidateUtil.validate(synthesisPrint);
    return uploadService.commonPrint(synthesisPrint);
  }

  /**
   * @param businessId 业务id
   * @return 签名图片
   * @author fanlijuan
   */
  @RequestMapping(value = "/getPhoto", method = RequestMethod.POST)
  public Map<String, Object> getPhoto(@RequestBody UploadVo uploadVo) {
    try {
      ValidateUtil.validate(uploadVo, SignPictureGroup.class);
      return uploadService
          .getPhoto(uploadVo.getBusinessId(),uploadVo.getPictureType());
    } catch (Exception e) {
      logger.error("图片获取失败", e);
      return ResultUtils.error("图片获取失败");
    }
  }

  /**
   * 新建业务获取x,y轴信息测试接口，非实际使用接口
   * @param url
   * @param type
   * @param stampX
   * @param stampY
   * @param signX
   * @param signY
   * @throws IOException
   */
  @RequestMapping("/testCreateImg")
  public String test(String url,Integer type,Integer stampX,Integer stampY,Integer signX,Integer signY) throws IOException {
    if(type.equals(1)){
      String stampPath = HttpContextUtils.getHttpServletRequest().getServletContext().getRealPath("")
              + "template/stamp.png";
      //盖章
      String tempath = UrlFileUtil.downloadFile(url, tempPath);
      String targetPath = tempPath + "/" + "11111.jpg";
      ImageUtil.markByIcon(stampPath,tempath,targetPath, stampX, stampY, 170, 170);
      File file = new File(targetPath);
      StorageResVo storageResVo = storageUploadHelper.storageUpload(file, 1);
      file.delete();
      return storageResVo.getFileUrl();
    }else if(type.equals(2)){
      //签名
      String signPath = HttpContextUtils.getHttpServletRequest().getServletContext().getRealPath("")
              + "template/sign.jpg";
      String tempath = UrlFileUtil.downloadFile(url, tempPath);
      String targetPath = tempPath + "/" + "22222.jpg";
      ImageUtil.markByIcon(signPath,tempath,targetPath, stampX, stampY, 200, 90);
      File file = new File(targetPath);
      StorageResVo storageResVo = storageUploadHelper.storageUpload(file, 1);
      file.delete();
      return storageResVo.getFileUrl();
    }
    return null;
  }

}
