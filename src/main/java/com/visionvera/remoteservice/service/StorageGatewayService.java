package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.pojo.StorageGatewayVo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author lijintao
 */
public interface StorageGatewayService {

  /**
   * 根据业务id获取身份证信息
   *
   * @param businessId 业务id
   * @return
   */
  Map<String, Object> getCardInfoAndModify(Integer businessId);

  /**
   * 扫描照
   *
   * @param businessId 业务key
   * @param pictureType 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照 （目前支持3和4、5）
   * @param materialsId 证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
   * @return
   */
  Map<String, Object> scanningPhotoAndModify(Integer businessId, Integer pictureType,
      Integer materialsId, String cardId, String cardName);

  /**
   * 删除图片
   *
   * @param ids id逗号分割
   * @param businessId 业务id
   * @param pictureType 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照（目前支持1、3和4、5）
   * @return
   */
  Map<String, Object> deletePhoto(String ids, Integer businessId, Integer pictureType);

  /**
   * 获取图片列表和录像地址
   *
   * @param businessId 业务id
   * @return
   */
  Map<String, Object> getPhotosAndVideoPath(Integer businessId);

  /**
   * 截图接口
   *
   * @param businessId 业务id
   * @return
   * @
   */
  Map<String, Object> printScreenAndModify(Integer businessId);

  /**
   * 获取图
   *
   * @param businessId 业务id
   * @param materialsId 证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
   * @return
   */
  Map<String, Object> getPrintScreenList(Integer businessId, Integer materialsId);

  /**
   * 转发存储网关图片
   *
   * @param id 图片id
   * @param field 要是有的图片字段
   * @return
   */
//  byte[] img(String id, String field);

  /**
   * 下载材料
   * @param cardName
   * @param cardId
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> downloadZip(String cardName, String cardId, Integer businessId, HttpServletRequest request, HttpServletResponse response) throws IOException;

  /**
   * 检查下载材料是否存在
   *
   * @param storageGatewayVo
   * @return
   */
  Map<String, Object> checkZip(StorageGatewayVo storageGatewayVo);
}
