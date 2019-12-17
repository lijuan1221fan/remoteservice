package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.pojo.SecStreamVo;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpSession;

/****
 *
 * @ClassName: MeetingService
 * @Description: 会议调度业务接口层
 * @author wangruizhi
 * @date 2018年3月23日
 *
 */
public interface MeetingService {


  /***
   * 停止会议
   * @Title: stopMeeting
   * @Description: 停止会议
   * @param  businessId 业务id
   * @return Map<String , Object> 返回类型
   * @throws
   */
  Map<String, Object> stopMeetingAndModify(Integer businessId);

  /***
   * 开启/关闭辅流
   * @Title: startSecStream
   * @Description: 开启/关闭辅流
   * @param @param businessId 业务id
   * @param @param type 开启：1，关闭:2
   * @param session
   * @return Map<String , Object>    返回类型
   * @throws
   */
  Map<String, Object> setSecStreamAndModify(SecStreamVo secStreamVo);

  /**
   * 动态加入参会方
   *
   * @param businessId 业务id
   * @param dynDeviceNumber 动态终端号
   * @param session
   * @return Map<String   ,   Object>
   * @Description: 动态加入参会方
   * @author quboka
   * @date 2018年6月1日
   */
  Map<String, Object> dynAddDeviceAndModify(Integer businessId);

  /**
   * @param businessId 业务id
   * @param dynDeviceNumber 动态终端号
   * @param session
   * @return Map<String   ,   Object>
   * @Description: 动态移除参会方
   * @author quboka
   * @date 2018年6月1日
   */
  Map<String, Object> dynDelDeviceAndModify(Integer businessId,
      String dynDeviceNumber, HttpSession session);

  /**
   * 恢复业务
   *
   * @param user
   * @return
   */
  Map<String, Object> resumeSession(SysUserBean user);

  /****
   * 开启录制
   * @param businessId
   * @return
   */
//  Map<String, Object> startRecording(Integer businessId);

  /***
   * 停止录制
   * @param businessId
   * @return
   */
  Map<String, Object> stopRecording(Integer businessId);

  /**
   * 开启会议
   *
   * @param businessId 业务id
   * @param number 终端号码
   * @param userBean
   * @return
   */
  Map<String, Object> startMeetingAndModify(Integer businessId, String number)
      throws UnsupportedEncodingException;

  /**
   * 检查会议状态
   *
   * @param scheduleId
   * @return true 开会中  false停会中
   */
  Boolean checkMeetingStatus(Integer businessId);

}


