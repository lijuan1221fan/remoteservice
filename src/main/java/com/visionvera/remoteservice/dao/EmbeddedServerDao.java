package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.EmbeddedServerInfo;
import com.visionvera.remoteservice.bean.EmbededServerBackUpBean;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * Created by jlm on 2019-05-21 11:00
 */
public interface EmbeddedServerDao {

    Integer insertEmbeddedServer(@Param("vcDevId") String vcDevId, @Param("sessionId") String sessionId);

    Integer deleteEmbeddedServer(String sessionId);

    EmbeddedServerInfo getEmbeddedServerByVcDevId(@Param("vcDevId") String vcDevId);

    EmbeddedServerInfo getEmbeddedServer(String sessionId);
    /**
     * 受理端开始业务建立关联关系
     */
    Integer insertCallDeviceEmbeddedServerInfo(@Param("sessionId") String sessionId, @Param("userId") Integer userId);
    List<EmbeddedServerInfo> getAllEmbeddedServer();

    List<EmbeddedServerInfo> getValidEmbeddedServer();

    EmbeddedServerInfo getCallDeviceEmbeddedServer(@Param("vcDevId") String vcDevId);

    Integer updateCallDeviceEmbeddedServer(@Param("vcDevId") String vcDevId, @Param("sessionId") String sessionId);

    EmbeddedServerInfo getCallDeviceEmbeddedServerByUserId(@Param("userId") Integer userId);


    Integer updateEmbeddedServerByVcDevId(@Param("sessionId") String sessionId,@Param("userId") Integer userId);


    Integer updateEmbededeServerBySessionId(@Param("sessionId") String sessionId, @Param("createTime") String createTime);

    Integer deleteAllEmbeddedServer();

  /**
   * 业务办理关联关系建立成功
   * @param userId
   * @param sessionId
   * @return
   */
    Integer updateEmbeddedServerOfsessionIdByUserId(@Param("userId") Integer userId, @Param("sessionId") String sessionId);

  /**
   * 查询业务关联建立关系
   * @param userId
   * @return
   */
  EmbeddedServerInfo getCallDeviceEmbeddedServerRelationBusinessByUserId(@Param("userId") Integer userId);
  /**
   *  根据sessionID取得信息
   */
  EmbeddedServerInfo getEmbeddedServerBySessionId(@Param("sessionId") String sessionId);

  /**
   * 根据返回的设备状态更新
   * @param embeddedResponse
   * @param sessionId
   * @return
   */
  Integer updateEmbeddedServerByUserId(@Param("idCardStatus") Integer idCardStatus,@Param("highPhotoStatus")Integer highPhotoStatus ,@Param("signatureStatus")Integer signatureStatus,@Param("fingerPrintStatus")Integer fingerPrintStatus,@Param("printerStatus")Integer printerStatus,@Param("vcDevId") String vcDevId);


  /**
   * 根据返回的设备状态更新
   * @param
   * @param sessionId
   * @return
   */
  Integer updateVersionrByUSerId(@Param("version") String version,@Param("sessionId") String sessionId);


  void insertEmbeddedServerBackUp(EmbededServerBackUpBean embededServerBackUpBean);
  EmbededServerBackUpBean selectEmbeddedServerBackUp(@Param("vcDevId") String vcDevId);

  /**
   * 建立连接后删除之前备份表中的失去连接的数据
   * @param vcDevId
   */
  void deleteEmbeddedServerBackUp(String vcDevId);

  /**
   * 发送获取设备状态清空设备id对应的scu状态
   * @param vcDevId
   */
  Integer clearEmbeddedServerStatus(String vcDevId);

    EmbeddedServerInfo getOtherStatus(String vcDevId);
}
