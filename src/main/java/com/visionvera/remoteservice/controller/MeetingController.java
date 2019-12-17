package com.visionvera.remoteservice.controller;

import com.visionvera.common.validator.group.DynDelDeviceGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.group.StartGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.pojo.MeetingVo;
import com.visionvera.remoteservice.pojo.SecStreamVo;
import com.visionvera.remoteservice.service.MeetingService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***
 *
 * @ClassName: MeetingController
 * @Description: 会议调度接口
 * @author wangruizhi
 * @date 2018年3月23日
 *
 */
@RestController
@RequestMapping("meeting")
public class MeetingController {

  private static Logger logger = LoggerFactory.getLogger(MeetingController.class);

  @Resource
  private MeetingService meetingService;


  /**
   * @param businessId 业务key
   * @param number 终端号 (服务中心的终端号码)
   * @return Map<String , Object>
   * @Description: 开启会议
   * @date 2018年3月25日
   */
  @RequestMapping(value = "start", method = RequestMethod.POST)
  public Map<String, Object> startMeeting(@RequestBody MeetingVo meetingVo)
      throws UnsupportedEncodingException {
    ValidateUtil.validate(meetingVo, StartGroup.class);
    return meetingService
        .startMeetingAndModify(meetingVo.getBusinessId(), meetingVo.getNumber());
  }

  /***
   *
   * @Title: stopMeeting
   * @Description: 停止会议
   * @param  businessId 业务key
   * @return Map<String   ,   Object>    返回类型
   * @throws
   */
  @RequestMapping(value = "stop", method = RequestMethod.GET)
  public Map<String, Object> stopMeeting(@RequestParam(value = "businessId") Integer businessId) {
    return meetingService.stopMeetingAndModify(businessId);
  }

  /***
   *
   * @Title: startSecStream
   * @Description: 开启/关闭辅流
   * @param @param businessId 业务key
   * @param @param type 开启：1，关闭:2
   * @return Map<String   ,   Object>    返回类型
   * @throws
   */
  @PostMapping("setSecStreamType")
  public Map<String, Object> startSecStream(@RequestBody SecStreamVo secStreamVo) {
    ValidateUtil.validate(secStreamVo, QueryGroup.class);
    return meetingService.setSecStreamAndModify(secStreamVo);
  }

  /**
   * @param businessId 业务key
   * @param dynDeviceNumber 动态终端号
   * @param session
   * @return Map<String               ,               Object>
   * @Description: 动态加入参会方
   * @author quboka
   * @date 2018年6月1日
   */
  @GetMapping("dynAddDevice")
  public Map<String, Object> dynAddDevice(Integer businessId) {
    return meetingService.dynAddDeviceAndModify(businessId);
  }

  /**
   * @param businessId 业务key
   * @param dynDeviceNumber 动态终端号
   * @param session
   * @return Map<String , Object>
   * @Description: 动态移除参会方
   * @author quboka
   * @date 2018年6月1日
   */
  @RequestMapping(value = "dynDelDevice", method = RequestMethod.POST)
  public Map<String, Object> dynDelDevice(@RequestBody MeetingVo meetingVo, HttpSession session) {
    ValidateUtil.validate(meetingVo, DynDelDeviceGroup.class);
    return meetingService
        .dynDelDeviceAndModify(meetingVo.getBusinessId(), meetingVo.getDynDeviceNumber(), session);
  }

  /**
   * 恢复业务
   *
   * @param session
   * @return
   */
  @RequestMapping(value = "resumeSession", method = RequestMethod.GET)
  public Map<String, Object> resumeSession(HttpSession session) {
    SysUserBean user = ShiroUtils.getUserEntity();
    if (user == null) {
      logger.info("恢复业务失败，获取当前用户失败");
      return ResultUtils.error("恢复业务失败，获取当前用户失败");
    } else {
      return meetingService.resumeSession(user);
    }
  }

  /**
   * 开启录制
   *
   * @param businessId 业务key
   * @return
   */
//  @RequestMapping(value = "startRecording", method = RequestMethod.GET)
//  public Map<String, Object> startRecording(
//      @RequestParam(value = "businessId") Integer businessId) {
//    ValidateUtil.validate(businessId);
//    return meetingService.startRecording(businessId);
//  }


  /**
   * 停止录制
   *
   * @param businessId 业务key
   * @return
   */
  @RequestMapping(value = "stopRecording", method = RequestMethod.GET)
  public Map<String, Object> stopRecording(@RequestParam(value = "businessId") Integer businessId) {
    ValidateUtil.validate(businessId);
    return meetingService.stopRecording(businessId);
  }

  /**
   * 检查会议状态接口
   *
   * @param scheduleId
   * @return
   */
  /*@RequestMapping("checkMeetingStatus")
  public Boolean checkMeetingStatus(@RequestParam(value = "businessId") Integer businessId) {
    return meetingService.checkMeetingStatus(businessId);
  }*/
}
