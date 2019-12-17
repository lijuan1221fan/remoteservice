package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.visionvera.common.enums.CallDeviceEnum;
import com.visionvera.common.utils.HttpContextUtils;
import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.DeviceInfo;
import com.visionvera.remoteservice.bean.EmbeddedBean;
import com.visionvera.remoteservice.bean.Materials;
import com.visionvera.remoteservice.bean.PhotoBean;
import com.visionvera.remoteservice.bean.Print;
import com.visionvera.remoteservice.bean.PrintDataBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.constant.PictureConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.DeviceInfoDao;
import com.visionvera.remoteservice.dao.MaterialsDao;
import com.visionvera.remoteservice.dao.PhotoBeanDao;
import com.visionvera.remoteservice.service.PrintService;
import com.visionvera.remoteservice.util.DateUtil;
import com.visionvera.remoteservice.util.ExcelExportUtils;
import com.visionvera.remoteservice.util.FileUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

/**
 * @author Administrator
 * @date 2018/8/16
 **/
@Service
public class PrintServiceImpl implements PrintService {

  private final static Logger logger = LoggerFactory.getLogger(PrintServiceImpl.class);
  @Resource
  private DeviceInfoDao deviceInfoDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private MaterialsDao materialsDao;
  @Resource
  @Qualifier("threadPoolTaskExecutor")
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;
  @Resource
  private PhotoBeanDao photoBeanDao;
  @Value("${temp.save.docpath}")
  private String tempPath;
  @Resource
  private BusinessTypeDao businessTypeDao;

  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;

  /**
   * 打印表单接口
   *
   * @param ids 打印文件的id 逗号分割
   * @param businessId 业务id
   * @return
   */
  @Override
  public Map<String, Object> print(String ids, Integer businessId) {
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    String outputUrl = null;
    if (businessInfo == null) {
      logger.info("打印失败，业务不存在");
      return ResultUtils.error("打印失败，业务不存在");
    }
    String terminalNumber = businessInfo.getTerminalNumber();
    String[] split = ids.split(",");
    List<Materials> list = materialsDao.selectByIds(split);
    PrintDataBean printDataBean = new PrintDataBean();
    ArrayList<EmbeddedBean> embeddedBeanList = new ArrayList<>();
    printDataBean.setType(CallDeviceEnum.Printer.getValue());
    for (Materials materials : list) {
      outputUrl = UrlFileUtil.downloadFile(materials.getFilePath(), tempPath);
      if (StringUtil.isEmpty(outputUrl)) {
        return ResultUtils.error("打印失败，材料不存在！");
      }
      if (StringUtil.isNotNull(outputUrl) && new File(outputUrl).exists()) {
        //通过websocket协议传输打印材料
        byte[] baseImage = FileUtil.imageTobyte(outputUrl);
        String shaFile = null;
        File file = new File(outputUrl);

        shaFile = FileUtil.getFileSha1(file);

        EmbeddedBean embeddedBean = new EmbeddedBean();
        //embeddedBean.setType(CallDeviceEnum.Printer.getValue());
        embeddedBean.setFileData(baseImage);
        embeddedBean.setFormat(materials.getFilePath().substring(materials.getFilePath().lastIndexOf('.') + 1));
        embeddedBean.setFileName(materials.getFilePath().substring(materials.getFilePath().lastIndexOf('/') + 1,
                materials.getFilePath().lastIndexOf('.')));
        embeddedBean.setShaValue(shaFile);
        embeddedBeanList.add(embeddedBean);
        if (file.exists()) {
          file.delete();
        }
      } else {
        return ResultUtils.error("打印失败，材料不存在！");
      }
    }
    printDataBean.setFiles(embeddedBeanList);
    try {
      embeddedServerWebSocketHandler.sendMessageToEmbeddServer(
              new TextMessage(JSON.toJSONString(printDataBean)),
              terminalNumber, Boolean.TRUE);
      logger.info("发送打印材料信息给SCU");
    } catch (Exception e) {
      e.printStackTrace();
      return ResultUtils.error("调用打印机失败");
    }
    return ResultUtils.ok("调用打印机成功");
  }

  /**
   * 根据业务id 套打签名
   *
   * @param businessId 业务id
   * @return
   */
  @Override
  public Map<String, Object> setPrint(Integer businessId) {
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if (businessInfo == null) {
      logger.info("打印失败，业务不存在");
      return ResultUtils.error("打印失败，业务不存在");
    }
    DeviceInfo deviceInfo = deviceInfoDao
        .selectByServiceKeyAndPrinterType(businessInfo.getServiceKey(),
            CommonConstant.COMMON_PRINTER);
    if (deviceInfo == null) {
      logger.info("打印失败，未绑定打印机");
      return ResultUtils.error("打印失败，未绑定打印机");
    }
    List<PhotoBean> printScreenList = photoBeanDao
        .selectByBusinessIdAndPictureType(businessId, PictureConstant.AUTOGRAPHED_PHOTO);
    if (!printScreenList.isEmpty()) {
      Map<String, Object> objectObjectHashMap = new HashMap<>();
      for (PhotoBean photoBean : printScreenList) {
        if (photoBean.getIconPath() != null) {
          objectObjectHashMap.put("image", photoBean);
        }
      }
      // String outPath = PrintServiceImpl.class.getClassLoader().getResource("").getPath().substring(1) + "1.xlsx";
      String outPath = HttpContextUtils.getHttpServletRequest().getServletContext().getRealPath("")
          + "template/1.xlsx";
      String tempPath =
          HttpContextUtils.getHttpServletRequest().getServletContext().getRealPath("")
              + "template/set_print.xlsx";
      objectObjectHashMap.put("F18","确认日期:"+ DateUtil.getNowDate());
      Boolean result = ExcelExportUtils
          .writeData(tempPath, outPath, objectObjectHashMap);
      if (!result) {
        try {
          CommonConstant.printLinkedQueue.put(new Print(deviceInfo.getDeviceName(), outPath));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return ResultUtils.ok("打印成功");
      }
    }
    return ResultUtils.error("打印失败");
  }

  @Override
  public Map<String, Object> formPrint(Integer businessId) {
    logger.info("开始申请单打印" + "businessId:" + businessId);
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if (businessInfo == null) {
      return ResultUtils.error("该业务不存在");
    }
    BusinessTypeBean businessDeatil = businessTypeDao.selectByPrimaryKey(businessInfo.getBusinessType());
    if (businessDeatil.getIsForm() == 1) {
      List<Materials> materialsList = materialsDao
              .selectMaterialsByTypeIdAndMaterialsType(businessDeatil.getId(), 1);
      if(materialsList.size()==0){
        return ResultUtils.error("打印材料不存在!");
      }
      //向SCU发送的打印的对象
      PrintDataBean printDataBean = new PrintDataBean();
      ArrayList<EmbeddedBean> list = new ArrayList<>();
      printDataBean.setType(CallDeviceEnum.Printer.getValue());
      for (Materials materials : materialsList) {
        String outputUrl = null;
        if (StringUtil.isNotNull(materials.getFilePath())) {
          outputUrl = UrlFileUtil.downloadFile(materials.getFilePath(), tempPath);
          if (!StringUtil.isEmpty(outputUrl) && new File(outputUrl).exists()) {
            //通过websocket协议传输打印材料
            byte[] baseImage = FileUtil.imageTobyte(outputUrl);
            String shaFile = null;
            File file = new File(outputUrl);
            shaFile = FileUtil.getFileSha1(file);
            EmbeddedBean embeddedBean = new EmbeddedBean();
            //devType为2时，代表是叫号机调用的打印机
            //embeddedBean.setDevType(DevTypeEnum.AllInOne.getValue());
            embeddedBean.setFileData(baseImage);
            //embeddedBean.setType(CallDeviceEnum.Printer.getValue());
            embeddedBean.setFormat(
                    materials.getFilePath().substring(materials.getFilePath().lastIndexOf('.') + 1));
            embeddedBean.setFileName(
                    materials.getFilePath().substring(materials.getFilePath().lastIndexOf('/') + 1,
                            materials.getFilePath().lastIndexOf('.')));
            embeddedBean.setShaValue(shaFile);
            list.add(embeddedBean);
            if (file.exists()) {
              file.delete();
            }
          }else{
            //否则提示材料不存在
            return ResultUtils.error("打印材料不存在！");
          }
        }
      }
      //当打印材料不为空时，向SCU发送打印材料
      printDataBean.setFiles(list);
      try {
        embeddedServerWebSocketHandler
                .sendMessageToEmbeddServer(new TextMessage(JSON.toJSONString(printDataBean)),
                        businessInfo.getTerminalNumber(), Boolean.TRUE);
        logger.info("打印业务申请单:"+JSON.toJSONString(printDataBean));
      } catch (Exception e) {
        e.printStackTrace();
        return ResultUtils.error("调用打印机失败");
      }
    }
    return ResultUtils.ok("调用打印机成功");
  }
}
