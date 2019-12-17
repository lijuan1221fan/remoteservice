package com.visionvera.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.visionvera.api.handler.api.ContentManageApiHandler;
import com.visionvera.api.handler.api.StorageApiHandler;
import com.visionvera.api.handler.bean.ContentEntity;
import com.visionvera.api.handler.bean.ContentInfo;
import com.visionvera.api.handler.bean.StorageInfo;
import com.visionvera.api.handler.bean.res.ResRequest;
import com.visionvera.api.handler.constant.ContentApi;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.CommonConfigService;
import com.visionvera.remoteservice.service.StorageResService;
import java.io.File;
import javax.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jlm
 * @ClassName: 存储网关上传工具类
 * @Description:
 * @date 2019/1/24
 */
@Component
public class StorageUploadHelper {

  private static Logger logger = LoggerFactory.getLogger(StorageUploadHelper.class);

  @Resource
  private CommonConfigService commonConfigService;
  @Resource
  private StorageResService storageResService;

  /**
   * @param file 上传的文件
   * @param type 文件类型
   * @return
   */
  public StorageResVo storageUpload(File file, Integer type) {
    CommonConfigBean commonConfigBean = commonConfigService.getCommonConfigByName("cms_url");
    String cmsUrl = commonConfigBean.getCommonValue();
    if (cmsUrl.isEmpty()) {
      logger.info("内容管理平台路径为空");
      throw new MyException("内容管理平台路径为空");
    }
    ResultEntity<ContentInfo> storageIp = ContentManageApiHandler.getStorageIp(
        new ContentEntity(HttpUtil.getIpByUrl(cmsUrl), HttpUtil.getPortByUrl(cmsUrl)));
    logger.info("内容管理平台返回结果：" + storageIp);
    if (!storageIp.getResult()) {
      logger.info("内容管理平台获取存储网关路径失败: " + storageIp);
      throw new MyException("内容管理平台获取存储网关路径失败");
    }
    ContentEntity resContentEntity = JSONObject
        .parseObject(storageIp.getData().getData(), ContentEntity.class);
    //拼接存储网关路径
    String baseUrl = ContentApi.PROTOCOL + resContentEntity.getServerIp() + ":" + resContentEntity
        .getServerHost();
    ResRequest resRequest = new ResRequest(HttpUtil.getIpByUrl(baseUrl),
        HttpUtil.getPortByUrl(baseUrl), FilenameUtils.removeExtension(file.getName()),
        type.toString(), file);
    ResultEntity<StorageInfo> resultEntity = StorageApiHandler.uploadFile(resRequest);
    logger.info("存储网关返回结果： " + resultEntity);
    if (!resultEntity.getResult()) {
      throw new MyException("存储网关上传文件失败");
    }
    JSONObject dataMap = JSONObject.parseObject(resultEntity.getData().getData());
    String sIp = HttpUtil.getIpByUrl(dataMap.getString("fileUrl"));
    Integer sPort = HttpUtil.getPortByUrl(dataMap.getString("fileUrl"));
    String surl = ContentApi.PROTOCOL + sIp + ":" + sPort;
    StorageResVo storageResVo = new StorageResVo();
    storageResVo.setStorageUrl(surl);
    storageResVo.setResId(dataMap.getString("fileId"));
    storageResVo.setFileUrl(dataMap.getString("fileUrl"));
    storageResVo.setType(type);
    //插入存储网关上传资源表
    storageResService.addStorageRes(storageResVo);
    return storageResVo;
  }
}
