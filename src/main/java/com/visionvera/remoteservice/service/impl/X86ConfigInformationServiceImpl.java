package com.visionvera.remoteservice.service.impl;/**
 * 功能描述:
 *
 * @ClassName: X86ConfigInformationServiceImpl
 * @Author: ljfan
 * @Date: 2019-06-27 11:19
 * @Version: V1.0
 */

import com.alibaba.fastjson.JSON;
import com.visionvera.common.enums.CallDeviceEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.remoteservice.bean.EmbeddedServerInfo;
import com.visionvera.remoteservice.bean.X86ConfigInformationBean;
import com.visionvera.remoteservice.controller.MaterialsController;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.X86ConfigInformationDao;
import com.visionvera.remoteservice.service.X86ConfigInformationService;
import com.visionvera.remoteservice.util.FileUploadUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

/**
 * @ClassNameX86ConfigInformationServiceImpl
 *description X86配置信息设定
 * @author ljfan
 * @date 2019/06/27
 *version
 */
@Service
public class X86ConfigInformationServiceImpl implements X86ConfigInformationService {
  private static Logger logger = LoggerFactory.getLogger(MaterialsController.class);
    @Resource
    X86ConfigInformationDao x86ConfigInformationDao;
    @Resource
    private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
    @Resource
    private EmbeddedServerDao embeddedServerDao;

  @Value("${temp.save.datapath}")
  private String X86path;
  @Value("${temp.save.downloadscupath}")
  private String downloadscupath;

  @Override
  public Map<String, Object> updateX86ConfigInformation(
      List<X86ConfigInformationBean> x86ConfigInformationBeanList) {
      String message = X86ConfigInformationBean.check(x86ConfigInformationBeanList);
      if(StringUtil.isNotEmpty(message)){
        return ResultUtils.ok(message+"值不符合规范");
      }
      Integer i= x86ConfigInformationDao.update(x86ConfigInformationBeanList);
      List<EmbeddedServerInfo> serverInfoList = embeddedServerDao.getAllEmbeddedServer();
    if (!CollectionUtils.isEmpty(serverInfoList)) {
      HashMap<String, Object> map = X86ConfigInformationBean.convert(x86ConfigInformationBeanList,
              CallDeviceEnum.X86Config.getValue());
      for (EmbeddedServerInfo embeddedServerInfo : serverInfoList) {
        embeddedServerWebSocketHandler.sendMessageToEmbeddServer(
                new TextMessage(JSON.toJSONString(map)),
                embeddedServerInfo.getVcDevId(), Boolean.FALSE);
        logger.info("发送SCU配置信息给SCU");
      }
    }
      if(i > 0){
      return ResultUtils.ok("成功");

      }
      return ResultUtils.error("失败");

  }
  @Override
  public Map<String, Object> getX86ConfigInformation() {
    X86ConfigInformationBean x86ConfigInformationBean = new X86ConfigInformationBean();
    x86ConfigInformationBean.setState(StateEnum.Effective.getValue());
    List<X86ConfigInformationBean> x86 = x86ConfigInformationDao.getList(x86ConfigInformationBean);
    if(x86.size() > 0){
      return ResultUtils.check(x86);
    }
    return ResultUtils.error("请初始化SCU配置信息");
  }
  @Override
  public Map<String, Object> uploadX86zip(MultipartFile file){
    if (file == null || file.isEmpty()) {
      logger.info("更新失败，请选择上传文件");
      return ResultUtils.error("更新失败，请选择上传文件");
    }
    String originalFilename = file.getOriginalFilename();
    String fileName = FilenameUtils.removeExtension(originalFilename);
    if(StringUtil.isNotEmpty(fileName)&&!fileName.contains("-")&&fileName.lastIndexOf("-")<0){
      logger.info("文件格式不正确，请重新上传:"+fileName);
      return ResultUtils.error("文件格式不正确，请重新上传");
    }
    //判断后缀
    if(!originalFilename.substring(originalFilename.lastIndexOf(".")+1,originalFilename.length()).equals("zip")){
      logger.info("文件格式不正确，请重新上传:"+fileName);
      return ResultUtils.error("文件格式不正确，请重新上传");
   }
   //修改上传方式
    String path = FileUploadUtil.uploadFile(X86path,downloadscupath, file);
    try{

      if(null != path){
        String vesion = fileName.substring(fileName.lastIndexOf("-")+1,fileName.length());
        List<X86ConfigInformationBean> list = new ArrayList<>();
        X86ConfigInformationBean bean = new X86ConfigInformationBean();
        bean.setColumnNameEn("zipUrl");
        bean.setColumnValue(path);
        list.add(bean);
        bean = new X86ConfigInformationBean();
        bean.setColumnNameEn("version");
        bean.setColumnValue(vesion);
        list.add(bean);
        int i= x86ConfigInformationDao.update(list);
        if(i > 0){
          InitializingBeanImpl.version = vesion;
          //x86ConfigInformationDao.updatescuState();
          return ResultUtils.ok("更新成功");
        }
      }
    } catch(Exception e){
      e.printStackTrace();
    }
    return ResultUtils.error("更新失败");
  }

}
