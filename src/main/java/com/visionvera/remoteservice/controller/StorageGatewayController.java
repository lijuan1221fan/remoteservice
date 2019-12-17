package com.visionvera.remoteservice.controller;

import com.visionvera.common.utils.UrlFileUtil;
import com.visionvera.common.validator.group.*;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.ClientInfo;
import com.visionvera.remoteservice.bean.PhotoBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.pojo.StorageGatewayVo;
import com.visionvera.remoteservice.service.StorageGatewayService;
import com.visionvera.remoteservice.util.ResultUtils;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijintao
 * @date
 */
@RestController
@RequestMapping("storage")
public class StorageGatewayController {

    private final Logger logger = LoggerFactory.getLogger(StorageGatewayController.class);
    @Autowired
    private StorageGatewayService storageGatewayService;
    /**
     * 下载图片材料压缩包 jlm
     */
    @Value("${temp.save.downloadpath}")
    private String picpath;

    /**
     * 根据业务id获取身份证信息
     *
     * @param businessId 业务id
     * @return
     */
    @RequestMapping(value = "getCardInfo", method = RequestMethod.GET)
    public Map<String, Object> getCardInfo(@RequestParam(value = "businessId") Integer businessId) {
        ValidateUtil.validate(businessId);
        return storageGatewayService.getCardInfoAndModify(businessId);
    }

    /**
     * @param businessId   业务key
     * @param deviceNumber 获取扫描照的终端号码
     * @param pictureType  扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照 （目前支持3和4、5）
     * @param materialsId  证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
     * @param cardId       身份证号码
     * @param cardName     身份证姓名
     * @return Map<String, Object>
     * @Description: 扫描照
     * @author jlm
     * @date 2018年10月30日
     */
    @RequestMapping(value = "scanningPhoto", method = RequestMethod.POST)
    public Map<String, Object> scanningPhoto(@RequestBody StorageGatewayVo storageGatewayVo) {
        ValidateUtil.validate(storageGatewayVo, ScanningPhotoGroup.class);
        return storageGatewayService
                .scanningPhotoAndModify(storageGatewayVo.getBusinessId(), storageGatewayVo.getPictureType(),
                        storageGatewayVo.getMaterialsId(), storageGatewayVo.getCardId(),
                        storageGatewayVo.getCardName());
    }

    /**
     * 删除图片
     *
     * @param ids         id逗号分割
     * @param businessId  业务id
     * @param pictureType 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照（目前支持1、3和4、5）
     * @return
     */
    @RequestMapping(value = "deletePhoto", method = RequestMethod.POST)
    public Map<String, Object> deletePhoto(@RequestBody StorageGatewayVo storageGatewayVo) {
        ValidateUtil.validate(storageGatewayVo, DeleteGroup.class);
        return storageGatewayService
                .deletePhoto(storageGatewayVo.getIds(), storageGatewayVo.getBusinessId(),
                        storageGatewayVo.getPictureType());
    }

    /***
     *
     * @Title: getPhotosAndVideoPath
     * @Description: 获取图片列表和录像地址
     * @param businessId 业务key
     * @return Map<String, Object>
     * @throws
     */
    @RequestMapping(value = "getPhotosAndVideoPath", method = RequestMethod.GET)
    public Map<String, Object> getPhotosAndVideoPath(
            @RequestParam(value = "businessId") Integer businessId) {
        ValidateUtil.validate(businessId);
        return storageGatewayService.getPhotosAndVideoPath(businessId);
    }

    /***
     *
     *: 视频截图接口
     * @param businessId 业务ud
     * @return Map<String, Object>
     * @throws
     */
    @RequestMapping(value = "printScreen", method = RequestMethod.GET)
    public Map<String, Object> printScreen(
            @RequestParam(value = "businessId") Integer businessId) {
        ValidateUtil.validate(businessId);
        return storageGatewayService.printScreenAndModify(businessId);
    }

    /**
     * 获取图片列表
     *
     * @param businessId  业务id
     * @param materialsId 证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
     * @return
     */
    @RequestMapping(value = "getPrintScreenList", method = RequestMethod.POST)
    public Map<String, Object> getPrintScreenList(
            @RequestBody StorageGatewayVo storageGatewayVo) {
        ValidateUtil.validate(storageGatewayVo, GetGroup.class);
        return storageGatewayService.getPrintScreenList(storageGatewayVo.getBusinessId(),
                storageGatewayVo.getMaterialsId());
    }

    /**
     * 下载压缩包
     *
     * @param cardName
     * @param cardId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "zip", method = RequestMethod.POST)
    public void downloadZip(@RequestParam(value = "cardName") String cardName,
                            @RequestParam(value = "cardId") String cardId, @RequestParam(value = "businessId") Integer businessId,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        Map<String, Object> stringObjectMap = storageGatewayService.downloadZip(cardName, cardId, businessId, request, response);
        logger.info("下载结果：" + stringObjectMap.toString());
    }

    /**
     * 检查当前用户文件夹是否存在
     *
     * @param storageGatewayVo
     * @return
     */
    @RequestMapping(value = "checkZip", method = RequestMethod.POST)
    public Map<String, Object> checkZip(@RequestBody StorageGatewayVo storageGatewayVo) {
        ValidateUtil.validate(storageGatewayVo, CardGroup.class);
        return storageGatewayService.checkZip(storageGatewayVo);
    }
}
