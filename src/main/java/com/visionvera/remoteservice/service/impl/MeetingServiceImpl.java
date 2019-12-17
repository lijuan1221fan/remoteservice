package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.visionvera.api.handler.api.ContentManageApiHandler;
import com.visionvera.api.handler.api.MeetingApiHandler;
import com.visionvera.api.handler.api.StorageApiHandler;
import com.visionvera.api.handler.bean.ContentEntity;
import com.visionvera.api.handler.bean.ContentInfo;
import com.visionvera.api.handler.bean.MeetingEntity;
import com.visionvera.api.handler.bean.MeetingInfo;
import com.visionvera.api.handler.bean.res.ResRequest;
import com.visionvera.api.handler.constant.ContentApi;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResponseCode;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.common.enums.AndroidBusinessTypeEnum;
import com.visionvera.common.enums.MeetingStateEnum;
import com.visionvera.common.enums.ShowScreenEnum;
import com.visionvera.common.enums.SystemVersionEnum;
import com.visionvera.common.enums.VcDevStateEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.MeetingOperation;
import com.visionvera.remoteservice.bean.ScheduleVO;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.common.sms.SendsmsTask;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.MeetingOperationDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.pojo.SecStreamVo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.MeetingService;
import com.visionvera.remoteservice.service.NumberService;
import com.visionvera.remoteservice.util.CustomUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author 13624
 * @date 2018/7/30
 */
@Service
public class MeetingServiceImpl implements MeetingService {

    private static Logger logger = LoggerFactory.getLogger(MeetingServiceImpl.class);
    @Resource
    private CommonConfigDao commonConfigDao;
    @Resource
    private MeetingOperationDao meetingOperationDao;
    @Resource
    private ServiceCenterDao serviceCenterDao;
    @Resource
    private VcDevDao vcDevDao;
    @Resource
    private SysDeptBeanDao sysDeptBeanDao;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private BusinessInfoService businessInfoService;
    private ResultEntity<MeetingInfo> resultEntity;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private NumberService numberService;
    /**
     * @author jlm
     */
    @Resource
    private WindowDao windowDao;
    @Resource
    private SendsmsTask sendsmsTask;

    private boolean checkResponseResult(MeetingInfo responseResult) {
        if (null == responseResult || responseResult.getErrcode() == 1
                || responseResult.getErrcode() == 2) {
            return false;
        } else if (responseResult.getErrcode() == 0) {
            return true;
        }
        return false;
    }

    private void updateCommonConfig(String key, String access_token) {
        Map<String, Object> commonMap = new HashMap<String, Object>(2);
        commonMap.put("commonName", key);
        commonMap.put("commonValue", access_token);
        commonConfigDao.updateValueByKey(commonMap);
    }

    private boolean checkMeetingStatus(String baseUrl, String uuid, String scheduleId,
                                       String access_token, String loginNamem, String loginPwd) {
        MeetingEntity checkMeetingStatus = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                HttpUtil.getPortByUrl(baseUrl),
                loginNamem, loginPwd, uuid, access_token, scheduleId);
        ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler
                .checkMeetingStatus(checkMeetingStatus);
        logger.info("请求返回：" + resultEntity);
        if (resultEntity.getResult()) {
            MeetingInfo responseInfo = resultEntity.getData();
            if (!access_token.equals(responseInfo.getAccess_token())) {
                Map<String, Object> map = new HashMap<String, Object>(2);
                //修改token
                updateCommonConfig("meeting_token", responseInfo.getAccess_token());
            }
            if (checkResponseResult(responseInfo) && responseInfo.getData() != null) {
                // todo for 16 64
                List<ScheduleVO> meetingData = CustomUtil.stringToJavaList(responseInfo.getData(), "items", ScheduleVO.class);
                if (meetingData != null && meetingData.size() > 0) {
                    ScheduleVO scheduleVO = meetingData.get(0);
                    if (scheduleVO.getStatus() != null && !"".equals(scheduleVO.getStatus())) {
                        //预约状态： 1预约中，2审批通过，3审批驳回，4执行中，5停止
                        if ("5".equals(scheduleVO.getStatus())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 开启会议
     *
     * @param businessId 业务key
     * @param number     终端号码
     * @return
     * @author jlm
     */
    @Override
    public Map<String, Object> startMeetingAndModify(Integer businessId, String number) {
        //根据用户查找窗口
        WindowBean window = windowDao.getUserWindowByUserId(ShiroUtils.getUserId());
        if (window == null) {
            logger.info("查无窗口");
            return ResultUtils.error("会议启动失败，查无窗口，请确认后重试");
        } else {
            Integer id = window.getId();
            VcDevBean vcDev = vcDevDao.getDeviceByWindowId(id);
            if (vcDev == null) {
                logger.info("会议启动失败，当前窗口未绑定终端！");
                return ResultUtils.error("会议启动失败，当前窗口未绑定终端！");
            }
            // 查询村终端
            VcDevBean slaveDev = vcDevDao.getDeviceById(number);
            if (slaveDev == null) {
                logger.info("会议启动失败，终端：" + number + "不存在！");
                return ResultUtils.error("会议启动失败，终端：" + number + "不存在！");
            }
            //1.从数据库查询用户id、token
            String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
            String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
            String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
            String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name")
                    .getCommonValue();
            String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
                    .getCommonValue();

            // system_version 1:16位  2:64位
            String systemVersion = commonConfigDao.getCommonConfigByName("system_version").getCommonValue();
            if (systemVersion == null) {
                return ResultUtils.error("系统版本未配置请配置后重试");
            }

            MeetingOperation result = meetingOperationDao.selectByBusinessId(businessId);
            if (result != null) {
                if (MeetingStateEnum.Stoped.getValue().equals(result.getState())) {
                    logger.info("已停止会议，不可重复开会");
                    return ResultUtils.error("会议已停止,不可重复开启");
                }
                // 检查会议
                if (checkMeetingStatus(baseUrl, uuid, result.getScheduleId(), access_token, loginName,
                        loginPwd)) {
                    logger.info("终端会议开启中，无需重复开会");
                    return ResultUtils.error("终端会议开启中，无需重复开会");
                }
            }
            //会议名称
            String meetingName = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String time = sdf.format(new Date()).replaceAll("-", "").replaceAll(":", "");
            StringBuffer sb = new StringBuffer();
            if (vcDev.getId().contains("-")) {
                String[] split = vcDev.getId().split("-");
                for (int i = 0; i < split.length; i++) {
                    if (i != 0 && i != split.length - 1) {
                        sb.append(split[i].substring(2));
                    }
                    if (i == split.length - 1) {
                        sb.append(split[i]);
                    }
                }
                meetingName = "申办" + "_" + sb.toString() + "_" + time;
            } else {
                meetingName = "申办" + "_" + vcDev.getId() + "_" + time;
            }
            logger.info("会议名称： " + meetingName);
            //虚拟终端号
            String dvrServerNumber = null;
            //存储网关地址
            String contentUrl = null;
            //内容管理平台获取存储网关IP地址
            String cmsUrl = commonConfigDao.getCommonConfigByName("cms_url").getCommonValue();
            ResultEntity<ContentInfo> storageIp = ContentManageApiHandler
                    .getStorageIp(
                            new ContentEntity(HttpUtil.getIpByUrl(cmsUrl), HttpUtil.getPortByUrl(cmsUrl)));
            logger.info("内容管理平台返回结果：" + storageIp);
            //如果返回结果成功
            if (storageIp.getResult() && storageIp.getData().getErrcode().equals(ContentApi.SUCCESS_CODE)) {
                ContentEntity resContentEntity = JSONObject.parseObject(storageIp.getData().getData(), ContentEntity.class);
                //存储网关路径
                contentUrl = ContentApi.PROTOCOL + resContentEntity.getServerIp() + ":" + resContentEntity
                        .getServerHost();
            } else {
                logger.info("内容管理平台返回信息" + storageIp.getData());
                return ResultUtils.error(storageIp.getData() + "");
            }
            if (StringUtil.isEmpty(contentUrl)) {
                return ResultUtils.error("存储网关路径为空");
            }
            //从存储网关获取虚拟终端号  获取1个虚拟终端
            ResultEntity<ContentInfo> dummyDeviceId = StorageApiHandler.getDummyDeviceId(
                    new ResRequest(HttpUtil.getIpByUrl(contentUrl), HttpUtil.getPortByUrl(contentUrl), null,
                            "1"));
            logger.info("存储网关返回信息：" + dummyDeviceId);
            if (dummyDeviceId.getResult() && dummyDeviceId.getData().getErrcode()
                    .equals(ContentApi.SUCCESS_CODE)) {
                String resdata = dummyDeviceId.getData().getData();
                List list = JSONArray.parseArray(resdata);
                Map resMap = (Map) list.get(0);
                dvrServerNumber = (String) resMap.get("v2vID");
                if (StringUtil.isEmpty(dvrServerNumber)) {
                    logger.info("虚拟终端号为空");
                    return ResultUtils.error("获取虚拟终端失败");
                }
            } else {
                logger.info("获取虚拟终端失败: " + dummyDeviceId.getData().toString());
                return ResultUtils.error("获取虚拟终端失败");
            }
            ResultEntity<MeetingInfo> resultEntity = null;
            if (systemVersion.equals(SystemVersionEnum.SixTeen.getValue())) {
                //16位
                resultEntity = MeetingApiHandler.telMedicineStartMeetingForSixteen(new MeetingEntity(HttpUtil.getIpByUrl(baseUrl), HttpUtil.getPortByUrl(baseUrl),
                        loginName, loginPwd, uuid, access_token, vcDev.getId(), vcDev.getIp(),
                        slaveDev.getId(), vcDev.getTypeId(), meetingName, 1, dvrServerNumber));
            } else {
                //64位
                MeetingEntity requestMeetingEntity = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl), HttpUtil.getPortByUrl(baseUrl),
                        loginName, loginPwd, uuid, access_token, vcDev.getId(), vcDev.getIp(),
                        slaveDev.getId(), vcDev.getTypeId(), meetingName, 1, dvrServerNumber);
                //判断村中心摄像头模式
                if (slaveDev.getIsSingleCamera().equals(2)) {
                    requestMeetingEntity.setUseDoubleUsb(2);
                } else {
                    requestMeetingEntity.setUseDoubleUsb(1);
                }
                resultEntity = MeetingApiHandler.telMedicineStartMeeting(requestMeetingEntity);
                logger.info("开启会议,请求返回：" + resultEntity);
            }

            if (resultEntity.getResult()) {
                MeetingInfo data = resultEntity.getData();
                if (!access_token.equals(data.getAccess_token())) {
                    //修改token
                    updateCommonConfig("meeting_token", data.getAccess_token());
                }
                String scheduleId = CustomUtil.getJSONObjectByJsonString(data.getData(), "extra")
                        .getString("scheduleId");
                String recVirNo = CustomUtil.getJSONObjectByJsonString(data.getData(), "extra")
                        .getString("recVirNo");
                Map<String, Object> resultMap = new HashMap<String, Object>(1);
                if (recVirNo == null || 0 == Integer.parseInt(recVirNo)) {
                    resultMap.put("isRecording", false);
                } else {
                    resultMap.put("isRecording", true);
                }
                MeetingOperation meetingOperation = new MeetingOperation();
                meetingOperation.setBusinessId(businessId);
                meetingOperation.setScheduleId(scheduleId);
                meetingOperation.setUserId(ShiroUtils.getUserId());
                String devices =
                        vcDev.getId() + "," + vcDev.getTypeId() + ",0;" + slaveDev.getId() + "," + slaveDev
                                .getTypeId() + ",0";
                meetingOperation.setTerminalList(devices);
                meetingOperation.setIp(vcDev.getIp());
                meetingOperation.setStorageAddress(contentUrl);
                meetingOperation.setMeetingName(meetingName);
                if (result == null) {
                    meetingOperationDao.insertSelective(meetingOperation);
                } else {
                    meetingOperation.setId(result.getId());
                    meetingOperationDao.updateByPrimaryKeySelective(meetingOperation);
                }
                logger.info("会议开启成功");
                SysUserBean userBean = ShiroUtils.getUserEntity();
                //业务员处理中
                updateUserState(userBean, CommonConstant.WORK_STATE_DISPOSE);
                updateVcDevState(vcDev.getId(), VcDevStateEnum.InUse.getValue());
                if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
                    MeetingEntity entity = new MeetingEntity();
                    entity.setIp(HttpUtil.getIpByUrl(baseUrl));
                    entity.setPort(HttpUtil.getPortByUrl(baseUrl));
                    entity.setLoginName(loginName);
                    entity.setLoginPwd(loginPwd);
                    entity.setUuid(uuid);
                    entity.setAccessToken(access_token);
                    entity.setMasterId(vcDev.getId());
                    entity.setSlaveId(slaveDev.getId());
                    entity.setScheduleId(scheduleId);
                    entity.setDvrServerNumber(dvrServerNumber);
                    entity.setMeetingName(meetingName);
                    ResultEntity<MeetingInfo> startRecordingresultEntity = MeetingApiHandler.startRecording(entity);
                    logger.info("录制返回结果：" + startRecordingresultEntity);
                    if (!startRecordingresultEntity.getResult()) {
                        logger.info("录制失败:" + startRecordingresultEntity.getData());
                    }
                }
                //开会成功后，将给业务员发短信的线程任务取消掉
                sendsmsTask.cancelSmsThreadTask(businessId);
                return ResultUtils.ok("会议开启成功！", resultMap);
            }
            String errorMsg = getMeetingInfoErrorMsg(resultEntity);
            logger.info("会管会议启动异常" + errorMsg);
            return ResultUtils.error("会管会议启动异常:" + errorMsg);
        }

    }

    @Override
    public Boolean checkMeetingStatus(Integer businessId) {
        //获取会议信息
        MeetingOperation result = meetingOperationDao.selectByBusinessId(businessId);
        if (result != null) {
            //1.从数据库查询用户id、token
            String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
            String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
            String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
            String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name")
                    .getCommonValue();
            String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
                    .getCommonValue();
            return checkMeetingStatus(baseUrl, uuid, result.getScheduleId(), access_token, loginName,
                    loginPwd);
        } else {
            return false;
        }

    }

    /***
     * 停止会议
     */
    @Override
    public Map<String, Object> stopMeetingAndModify(Integer businessId) {

        MeetingOperation meetingOperationInfo = meetingOperationDao.selectByBusinessId(businessId);
        if (null == meetingOperationInfo) {
            logger.info("未查到" + businessId + "对应的会议,即未开会时结束业务");
            return ResultUtils.ok("停止会议成功！");
        }
        String scheduleId = meetingOperationInfo.getScheduleId();
        String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
        String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
        String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
                .getCommonValue();

        //2.停止会议
        MeetingEntity stop = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                HttpUtil.getPortByUrl(baseUrl),
                loginName, loginPwd, uuid, access_token, scheduleId);
        ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler.stopMeeting(stop);
        logger.info("停止会议,请求返回：" + resultEntity);
        if (resultEntity.getResult()) {
            //会议操作记录
            meetingOperationInfo.setState(MeetingStateEnum.Stoped.getValue());
            meetingOperationDao.updateByPrimaryKeySelective(meetingOperationInfo);
            MeetingInfo data = resultEntity.getData();
            if (!access_token.equals(data.getAccess_token())) {
                //修改token
                updateCommonConfig("meeting_token", data.getAccess_token());
            }
            logger.info("停止会议成功");
            //更新主席状态
            String idList = meetingOperationInfo.getTerminalList();
            String[] ids = idList.split(",");
            String vcDevId = ids[0];
            updateVcDevState(vcDevId, VcDevStateEnum.Effective.getValue());
            return ResultUtils.ok("停止会议成功");
        } else {
            ResponseCode responseCode = resultEntity.getResponseCode();
            System.out.println(responseCode.getCode());
            if (ResponseCode.RESPONSE_ERROR == responseCode) {
                // 检查会议
                if (!checkMeetingStatus(baseUrl, uuid, scheduleId, access_token, loginName, loginPwd)) {
                    //会议操作记录
                    meetingOperationInfo.setState(MeetingStateEnum.Stoped.getValue());
                    meetingOperationDao.updateByPrimaryKeySelective(meetingOperationInfo);
                    //更新主席状态
                    String idList = meetingOperationInfo.getTerminalList();
                    String[] ids = idList.split(",");
                    String vcDevId = ids[0];
                    updateVcDevState(vcDevId, VcDevStateEnum.Effective.getValue());
                    logger.info("停止会议成功");
                    return ResultUtils.ok("停止会议成功");
                }
            }
            String errorMsg = getMeetingInfoErrorMsg(resultEntity);
            logger.info("会管停止会议失败" + errorMsg);
            return ResultUtils.error("会管停止会议失败:" + errorMsg);
        }
    }

    /***
     *
     * @Title: startSecStream
     * @Description: 开启/关闭辅流
     * @param @param businessKey 业务key
     * @param @param type 开启：1，关闭:2
     * @return Map<String, Object> 返回类型
     * @throws
     */
    @Override
    public Map<String, Object> setSecStreamAndModify(SecStreamVo secStreamVo) {
        if (secStreamVo.getType() != ShowScreenEnum.OPEN_THEN_SHARE_SCREEN.getValue()
                && secStreamVo.getType() != ShowScreenEnum.CLOSE_OPEN_THEN_SHARE_SCREEN.getValue()) {
            return ResultUtils.error("type参数类型错误！");
        }
        SysUserBean user = ShiroUtils.getUserEntity();
        if (null == user) {
            return ResultUtils.error("当前用户未登录,请登陆后再次尝试！");
        }
        //根据用户查询窗口
        WindowBean window = windowDao.getUserWindowByUserId(ShiroUtils.getUserId());
        VcDevBean vcDev = vcDevDao.getDeviceByWindowId(window.getId());
        if (vcDev == null) {
            logger.info("当前窗口未绑定终端,请绑定后再次尝试！");
            return ResultUtils.error("当前窗口未绑定终端,请绑定后再次尝试！");
        }
        //业务人员绑定终端号-----》窗口绑定终端号
        String userBindTerminalNumber = vcDev.getId();
        MeetingOperation meetingOperationInfo = meetingOperationDao
                .selectByBusinessId(secStreamVo.getBusinessId());
        if (null == meetingOperationInfo) {
            logger.info("未查到" + secStreamVo.getBusinessId() + "对应的会议。");
            return ResultUtils.error("业务" + secStreamVo.getBusinessId() + "不存在");
        }
        String scheduleId = meetingOperationInfo.getScheduleId();
        String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
        String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
        String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password").getCommonValue();
        String systemVersion = commonConfigDao.getCommonConfigByName("system_version").getCommonValue();
        if (systemVersion == null) {
            return ResultUtils.error("系统版本未配置请配置后重试");
        }
        ResultEntity<MeetingInfo> resultEntity = null;
        if (ShowScreenEnum.OPEN_THEN_SHARE_SCREEN.getValue().equals(secStreamVo.getType())) {
            //开启辅流
            MeetingEntity startStream = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                    HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
                    uuid, access_token, scheduleId, userBindTerminalNumber);
            if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
                resultEntity = MeetingApiHandler.startStream(startStream);
            } else {
                resultEntity = MeetingApiHandler.startStreamForSixteen(startStream);
            }
            logger.info("开启辅流,请求返回：" + resultEntity);
        } else {
            //关闭辅流
            MeetingEntity startStream = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                    HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
                    uuid, access_token, scheduleId, userBindTerminalNumber);
            if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
                resultEntity = MeetingApiHandler.stopStream(startStream);
            } else {
                resultEntity = MeetingApiHandler.stopStreamForSixteen(startStream);
            }
            logger.info("关闭辅流,请求返回：" + resultEntity);
        }
        if (resultEntity.getResult()) {
            MeetingInfo data = resultEntity.getData();
            if (!access_token.equals(data.getAccess_token())) {
                //修改token
                updateCommonConfig("meeting_token", data.getAccess_token());
            }
            return ResultUtils
                    .ok(ShowScreenEnum.OPEN_THEN_SHARE_SCREEN.getValue().equals(secStreamVo.getType())
                            ? "屏幕分享成功！"
                            : "屏幕分享关闭成功！");
        }
        String errorMsg = getMeetingInfoErrorMsg(resultEntity);
        logger.info("会管屏幕分享失败: " + errorMsg);
        return ResultUtils.error("会管屏幕分享失败: " + errorMsg);
    }

    /**
     * @param businessId 业务key
     * @return Map<String, Object>
     * @Description: 动态加入参会方
     * @author quboka
     * @date 2018年6月1日
     */
    @Override
    public Map<String, Object> dynAddDeviceAndModify(Integer businessId) {
        //查询当前用户对应窗口
        WindowBean window = windowDao.getUserWindowByUserId(ShiroUtils.getUserId());
        if (null == window) {
            logger.info("动态入会失败，当前用户未分配窗口！");
            return ResultUtils.error("动态入会失败，当前用户未分配窗口！");
        }
        VcDevBean vcDev = vcDevDao.getDeviceByWindowId(window.getId());
        if (vcDev == null) {
            logger.info("动态入会失败，当前窗口未绑定终端！");
            return ResultUtils.error("动态入会失败，当前窗口未绑定终端！");
        }

        // 查询业务信息
        BusinessInfo businessInfo = businessInfoService.selectByPrimaryKey(businessId);
        if (businessInfo == null || businessInfo.getTerminalNumber() == null) {
            logger.info("动态入会失败， 当前终端无记录信息");
            return ResultUtils.error("会议启动失败，当前终端无记录信息");
        }
        //统筹中心有审批中心数据
        Map serviceData = serviceCenterDao.getOverAllByServiceKey(businessInfo.getServiceKey());
        if (serviceData == null || serviceData.get("overServiceKey") == null) {
            logger.info("取得统筹中心失败， 当前记录不属于统筹与审批中心的业务");
            return ResultUtils.error("取得统筹中心失败， 当前记录不属于统筹与审批中心的业务");
        }
        Map map = new HashMap();
        map.put("deptId", businessInfo.getDeptId());
        map.put("serviceKey", serviceData.get("overServiceKey"));
        //获取空闲协助终端
        List<VcDevBean> vcDevs = vcDevDao.getDeviceListByServiceKeyAndDeptId(map);
        if (vcDevs.size() == CommonConstant.zero) {
            logger.info("统筹中心未绑定终端，请绑定..");
            return ResultUtils.error("统筹中心未绑定终端，请绑定..");
        }
        String dynDeviceNumber = vcDevs.get(0).getId();

        String masterDevNumber = vcDev.getId();

        String slaveDevNumber = businessInfo.getTerminalNumber();

        //1.查询会议信息
        MeetingOperation meetingOperationInfo = meetingOperationDao.selectByBusinessId(businessId);
        if (null == meetingOperationInfo) {
            logger.info("未查到" + businessId + "对应的会议。");
            return ResultUtils.error("业务" + businessId + "不存在");
        }

        String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
                .getCommonValue();
        String scheduleId = meetingOperationInfo.getScheduleId();
        String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
        String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
        String systemVersion = commonConfigDao.getCommonConfigByName("system_version").getCommonValue();
        if (systemVersion == null) {
            return ResultUtils.error("系统版本未配置请配置后重试");
        }
        ResultEntity<MeetingInfo> chaneResultEntity = null;
        if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
            MeetingEntity dynamicAdd = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                    HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
                    uuid, access_token, vcDev.getTypeId(), scheduleId, dynDeviceNumber, vcDevs.get(0).getName());
            ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler.dynamicAdd(dynamicAdd);
            logger.info("动态加入参会方,请求返回：" + resultEntity);
            //执行切换方法 todo
            if (!resultEntity.getResult()) {
                return ResultUtils.error("动态入会失败" + getMeetingInfoErrorMsg(resultEntity));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            chaneResultEntity = MeetingApiHandler.dynamicAddThreeMeeting(dynamicAdd);
            logger.info("动态切换画面，请求返回" + chaneResultEntity);
        } else {
            MeetingEntity dynamicAdd = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                    HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
                    uuid, access_token, masterDevNumber, slaveDevNumber, vcDev.getTypeId(), scheduleId,
                    dynDeviceNumber, vcDev.getRoleId());
            chaneResultEntity = MeetingApiHandler.dynamicAddForSixteen(dynamicAdd);
            logger.info("16位动态入会请求返回：" + chaneResultEntity);
        }
        if (chaneResultEntity.getResult()) {
            MeetingInfo data = chaneResultEntity.getData();
            if (!access_token.equals(data.getAccess_token())) {
                //修改token
                updateCommonConfig("meeting_token", data.getAccess_token());
            }

            //修改终端状态
            Map<String, Object> deviceMap = new HashMap<String, Object>();
            deviceMap.put("id", dynDeviceNumber);
            //2:使用中 1:空闲，有效  0:删除
            deviceMap.put("state", VcDevStateEnum.InUse.getValue());
            int updateDeviceResult = vcDevDao.updateVcDecvice(deviceMap);
            if (updateDeviceResult == 0) {
                logger.info("修改终端状态失败。");
            }
            Map dynMap = new HashMap();
            dynMap.put("dynDeviceNumber", dynDeviceNumber);
            //记录动态入会得终端
            CommonConstant.dynDelMap.put(businessId, dynDeviceNumber);

            meetingOperationInfo.setDynDeviceNumber(dynDeviceNumber);
            meetingOperationDao.updateByPrimaryKeySelective(meetingOperationInfo);

            logger.info("终端:" + dynDeviceNumber + "动态入会成功。");
            return ResultUtils.ok("动态加入参会方成功", dynMap);
        }
        String errorMsg = getMeetingInfoErrorMsg(chaneResultEntity);
        logger.info("终端:" + dynDeviceNumber + "动态入会失败。 " + errorMsg);
        return ResultUtils.error("协助方终端异常或忙碌中");
    }

    /**
     * @param businessId      业务id
     * @param dynDeviceNumber 动态终端号
     * @param session
     * @return Map<String, Object>
     * @Description: 动态移除参会方
     * @author quboka
     * @date 2018年6月1日
     */
    @Override
    public Map<String, Object> dynDelDeviceAndModify(Integer businessId,
                                                     String dynDeviceNumber, HttpSession session) {
        //1.查询会议信息
        MeetingOperation meetingOperationInfo = meetingOperationDao.selectByBusinessId(businessId);
        if (null == meetingOperationInfo) {
            logger.info("未查到" + businessId + "对应的会议。");
            return ResultUtils.error("业务" + businessId + "不存在");
        }

        //2.查询url
        String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();

        //3.查询终端信息
        VcDevBean deviceById = vcDevDao.getDeviceById(dynDeviceNumber);
        if (deviceById == null) {
            logger.info("未查到" + dynDeviceNumber + "对应的终端。");
            return ResultUtils.error("未查到业务" + dynDeviceNumber + "对应的终端");
        }
        //4.请求数据参数
        //会议id
        String scheduleId = meetingOperationInfo.getScheduleId();
        //会管登陆后用户id
        String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
        String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
                .getCommonValue();
        String systemVersion = commonConfigDao.getCommonConfigByName("system_version").getCommonValue();
        if (systemVersion == null) {
            return ResultUtils.error("系统版本未配置请配置后重试");
        }
        ResultEntity<MeetingInfo> resultEntity = null;
        MeetingEntity dynamicDel = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
                uuid, access_token, scheduleId, dynDeviceNumber);
        if (SystemVersionEnum.SixtyFour.getValue().equals(systemVersion)) {
            //执行切换方法 todo
            ResultEntity changeResultEntity = MeetingApiHandler.dynamicDelThreeMeeting(dynamicDel);
            logger.info("动态切换画面，请求返回：" + changeResultEntity);
            if (!changeResultEntity.getResult()) {
                return ResultUtils.error("动态切换画面失败：" + getMeetingInfoErrorMsg(changeResultEntity));
            }
            resultEntity = MeetingApiHandler.dynamicDel(dynamicDel);
            logger.info("动态移除参会方,请求返回：" + resultEntity);
        } else {
            resultEntity = MeetingApiHandler.dynamicDelForSixteen(dynamicDel);
            logger.info("动态移除参会方,请求返回：" + resultEntity);
        }
        if (resultEntity.getResult()) {
            MeetingInfo data = resultEntity.getData();
            if (!access_token.equals(data.getAccess_token())) {
                //修改token
                updateCommonConfig("meeting_token", data.getAccess_token());
            }

            //修改终端状态
            Map<String, Object> deviceMap = new HashMap<String, Object>();
            deviceMap.put("id", dynDeviceNumber);
            //2:使用中 1:空闲，有效  0:删除
            deviceMap.put("state", VcDevStateEnum.Effective.getValue());
            int updateDeviceResult = vcDevDao.updateVcDecvice(deviceMap);
            if (updateDeviceResult == 0) {
                logger.info("修改终端状态失败。");
            }
            meetingOperationInfo.setDynDeviceNumber(null);
            meetingOperationDao.updateByPrimaryKey(meetingOperationInfo);
            //移除记录得动态入会得终端
            CommonConstant.dynDelMap.remove(businessId);

            logger.info("终端:" + dynDeviceNumber + "动态移除成功。");
            return ResultUtils.ok("动态移除参会方成功");
        }
        String errorMsg = getMeetingInfoErrorMsg(resultEntity);
        logger.info("终端:" + dynDeviceNumber + "动态移除失败： " + errorMsg);
        return ResultUtils.error("会管动态移除参会方失败: " + errorMsg);
    }


    /**
     * 恢复业务
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> resumeSession(SysUserBean user) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //查询业务信息
        BusinessInfo businessInfo = businessInfoService.getBusinessByUserId(user.getUserId());
        if (businessInfo == null) {
            logger.info("恢复业务失败，当前用户无未完业务");
            return ResultUtils.error("恢复业务失败，当前用户无未完业务");
        }
        if (businessInfo.getTerminalNumber() != null) {
            VcDevBean device = vcDevDao.getDeviceById(businessInfo.getTerminalNumber());
            if (device != null) {
                businessInfo.setVcDevType(device.getTypeId());
            }
        }
        Integer deptId = user.getDeptId();
        SysDeptBean deptBean = new SysDeptBean();
        deptBean = sysDeptBeanDao.selectByPrimaryKey(businessInfo.getDeptId());
        //当前已办理人数
        Integer doneCount = businessInfoService.getDoneCount(user);
        businessInfo.setDoneCount(doneCount);
        businessInfo.setNumberPrefix(deptBean.getNumberPrefix());
        resultMap.put("businessInfo", businessInfo);
        //查询会议信息
        MeetingOperation meetingOperationInfo = meetingOperationDao
                .selectByBusinessId(businessInfo.getId());
        if (null == meetingOperationInfo) {
            logger.info("未查到" + businessInfo.getId() + "对应的会议。");
        }
        resultMap.put("meetingOperationInfo", meetingOperationInfo);
        String androidBusinessType = AndroidBusinessTypeEnum.DepartmentalProcess.getValue();
        if (user.getUserId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
            androidBusinessType = AndroidBusinessTypeEnum.AllProcess.getValue();
        }
        //恢复业务时 将该业务对应处理的窗口置为工作状态
        windowDao.updateWindowUseStatus(businessInfo.getHandleWindowId(), WindowUseStateEnum.InUse.getValue());
        numberService.sendMessageAll(businessInfo.getServiceKey(), businessInfo.getDeptId());
        return ResultUtils.ok("恢复业务成功", resultMap);
    }

    /***
     * 开启录制
     * @param businessId 业务id
     * @return
     */
//    @Override
//    public Map<String, Object> startRecording(Integer businessId) {
//        //查询会议信息
//        MeetingOperation meetingOperationInfo = meetingOperationDao.selectByBusinessId(businessId);
//        if (null == meetingOperationInfo) {
//            logger.info("未查到" + businessId + "对应的会议。");
//            return ResultUtils.error("开启录制失败，当前会议不存在");
//        }
//        String scheduleId = meetingOperationInfo.getScheduleId();
//
//        String terminal_list = meetingOperationInfo.getTerminalList();
//        if (StringUtils.isBlank(terminal_list)) {
//            logger.error("开启录制失败，获取终端信息失败！");
//            return ResultUtils.error("开启录制失败，获取终端信息失败！");
//
//        }
//        String chairmanNumber = (terminal_list.split(";")[0]).split(",")[0];
//        String articipantspNumber = (terminal_list.split(";")[1]).split(",")[0];
//        String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
//        String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
//        String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
//        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
//        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
//                .getCommonValue();
//
//        MeetingEntity startRecording = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
//                HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd, uuid, access_token, chairmanNumber, articipantspNumber, scheduleId);
//        ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler.startRecording(startRecording);
//        logger.info("开启录制,请求返回：" + resultEntity);
//        if (resultEntity.getResult()) {
//            MeetingInfo data = resultEntity.getData();
//            if (!access_token.equals(data.getAccess_token())) {
//                //修改token
//                updateCommonConfig("meeting_token", data.getAccess_token());
//            }
//            logger.info("开启录制成功：", startRecording);
//            return ResultUtils.ok("开启录制成功！");
//        } else {
//            return ResultUtils.error("开启录制失败！");
//        }
//
//    }


    /***
     * 停止录制
     * @param businessId
     * @return
     */
    @Override
    public Map<String, Object> stopRecording(Integer businessId) {
        //查询会议信息
        MeetingOperation meetingOperationInfo = meetingOperationDao.selectByBusinessId(businessId);
        String scheduleId = meetingOperationInfo.getScheduleId();
        String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
        baseUrl += CommonConstant.MEETING_BASE_URL;
        String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
        String uuid = commonConfigDao.getCommonConfigByName("meeting_uuid").getCommonValue();
        String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
                .getCommonValue();

        MeetingEntity stopRecording = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
                HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
                uuid, access_token, scheduleId);
        ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler.stopRecording(stopRecording);
        logger.info("停止录制,请求返回：" + resultEntity);

        if (resultEntity.getResult()) {
            MeetingInfo data = resultEntity.getData();
            if (!access_token.equals(data.getAccess_token())) {
                //修改token
                updateCommonConfig("meeting_token", data.getAccess_token());
            }
            return ResultUtils.ok("停止录制成功！");
        } else {
            return ResultUtils.error("停止录制失败！");
        }
    }

    private String getMeetingInfoErrorMsg(ResultEntity<MeetingInfo> resultEntity) {
        String errorMsg = "会管返回信息为空";
        if (resultEntity.getData() != null) {
            String dataToString = String.valueOf(resultEntity.getData());
            if (StringUtils.isNotBlank(dataToString)) {
                if (dataToString.contains("errmsg")) {
                    MeetingInfo meetingInfo = JSON.parseObject(dataToString, MeetingInfo.class);
                    errorMsg = meetingInfo.getErrmsg();
                    return errorMsg;
                }
                errorMsg = dataToString;
            }
        }
        return errorMsg;
    }

    /**
     * 更新用户状态
     *
     * @param user
     * @param workState
     */
    private void updateUserState(SysUserBean user, Integer workState) {
        Map<String, Integer> stateMap = new HashMap<String, Integer>();
        stateMap.put("workState", workState);
        stateMap.put("userId", user.getUserId());
        int workStateResult = sysUserDao.updateWorkState(stateMap);
        if (workStateResult == CommonConstant.zero) {
            logger.info("用户:" + user.getUserId() + "修改工作状态失败");
        }
    }

    /**
     * 更新终端状态
     *
     * @param vcDevId
     * @param state
     * @return
     */
    private int updateVcDevState(String vcDevId, Integer state) {
        Map<String, Object> deviceMap = new HashMap<>(CommonConstant.two);
        deviceMap.put("id", vcDevId);
        //0:空闲中 1使用中
        deviceMap.put("state", state);
        return vcDevDao.updateVcDecvice(deviceMap);
    }
}
