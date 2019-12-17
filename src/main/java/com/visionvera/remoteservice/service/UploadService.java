package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.SynthesisPrint;
import com.visionvera.remoteservice.bean.UploadBean;
import com.visionvera.remoteservice.pojo.UploadVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 13624 on 2018/7/28.
 */
public interface UploadService {

  Map<String, Object> insertSelective(UploadBean uploadBean);

  /**
   * 根据业务key生成对应的图片
   *
   * @param businessId 业务id
   * @param sing 是否前面
   * @param stamp 是否签盖公章
   * @return
   */
  Map<String, Object> createImg(UploadVo uploadVo) throws IOException;

  /**
   * 远程打印表单
   *
   * @param businessId 业务id
   * @param sing 签名
   * @param stamp 签章
   * @return
   */
  Map<String, Object> dataPrint(Integer businessId, String imgUrl);

  /**
   * 　　* @Description: 综合业务打印 　　* @author: xueshiqi 　　* @date: 2018/12/20
   */
  Map<String, Object> commonPrint(SynthesisPrint synthesisPrint);

  /**
   * 根据业务key生成对应的图片
   *
   * @param businessId 业务id
   * @param pictureType 图片类型
   * @return
   */
  Map<String, Object> getPhoto(Integer businessId,Integer pictureType) throws IOException;

  /**
   * 根据后台配置合成图片
   * @param businessId
   * @param sing
   * @param stamp
   * @return
   */
  Map<String, Object>  createImgByAllocation(UploadVo uploadVo) throws IOException;

  /**
   * 上传表单图片
   * @param filedescription
   * @param businessId
   * @param file
   * @return
   */
  Map<String, Object> upload(String filedescription, Integer businessId, MultipartFile file);

  /**
   * 综合业务上传word
   * @param file
   * @return
   */
  Map<String, Object> uploadWord(MultipartFile file) throws IOException;
}
