package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.api.handler.api.MeetingApiHandler;
import com.visionvera.api.handler.bean.MeetingEntity;
import com.visionvera.api.handler.bean.MeetingInfo;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.common.enums.*;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.common.lock.Lock;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.controller.VcDevController;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.dao.X86ConfigInformationDao;
import com.visionvera.remoteservice.pojo.VcDevVo;
import com.visionvera.remoteservice.service.VcDevService;
import com.visionvera.remoteservice.util.CustomUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.ws.EmbeddedServerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.TextMessage;

/**
 * ClassName: VcDevServiceImpl
 *
 * @author quboka
 * @Description: 会管终端
 * @date 2018年5月17日
 */
@Service
public class VcDevServiceImpl implements VcDevService {

  private static Logger logger = LoggerFactory.getLogger(VcDevController.class);

  @Resource
  private VcDevDao vcDevDao;
  @Resource
  private CommonConfigDao commonConfigDao;
  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private WindowDao windowDao;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;
  @Resource
  private X86ConfigInformationDao  x86ConfigInformationDao;
  @Resource
  private EmbeddedServerWebSocketHandler embeddedServerWebSocketHandler;
  @Resource
  private EmbeddedServerDao embeddedServerDao;
  /**
   * @return Map<String   ,   Object>
   * @Description: 同步会管终端
   * @
   * @author quboka
   * @date 2018年5月17日
   */
  @Override
  public Map<String, Object> synDeviceAndModify() {
    Lock.synDevice.set(false);
    // 判断是否有终端在工作中。
    Integer workResult = vcDevDao.isDeviceWorking();
    if (workResult > CommonConstant.zero) {
      logger.info("同步失败,当前还有终端在工作中。");
      return ResultUtils.error("同步失败,当前还有终端在工作中。");
    }
    // 修改同步锁：表示当前禁止业务
    boolean compareAndSet = Lock.synDevice.compareAndSet(false, true);
    if (!compareAndSet) {
      logger.info("同步失败,当前正在同步中。");
      return ResultUtils.error("同步失败,当前正在同步中。");
    }
    //获取会管baseUrl
    String baseUrl = commonConfigDao.getCommonConfigByName("meeting_url").getCommonValue();
    String access_token = commonConfigDao.getCommonConfigByName("meeting_token").getCommonValue();
    String loginName = commonConfigDao.getCommonConfigByName("meeting_login_name").getCommonValue();
    String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
        .getCommonValue();
    String groupId = commonConfigDao.getCommonConfigByName("meeting_groupId").getCommonValue();
    MeetingEntity synDevice = new MeetingEntity(HttpUtil.getIpByUrl(baseUrl),
        HttpUtil.getPortByUrl(baseUrl), loginName, loginPwd,
        access_token, groupId);
    ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler.synDevice(synDevice);
    logger.info("会管终端同步请求结果：" + resultEntity);
    if (!resultEntity.getResult()) {
      return ResultUtils.error("同步失败！");
    }
    //{"errcode":0,"errmsg":"获取设备列表成功","access_token":null,"data":{"items":[{"id":20188,"msgId":null,"monitorId":null,"channelId":null,"name":"邢台市清河县小里庄村","mac":"B8EA6A065642","ip":"4.26.88.112","type":"极光","typeId":4,"description":null,"groupId":"61fbfd08597711e89c5200f1f5115377","groupName":null,"alias":"虚拟终端","roleId":0,"pX":null,"pY":null,"pZ":null,"svrId":null,"updateTime":null,"regionId":null,"no":0,"streamType":null,"status":null,"address":null,"dataType":null,"devFunc":null,"systemTime":null,"pLayer":null},{"id":20189,"msgId":null,"monitorId":null,"channelId":null,"name":"邢台市清河县油坊村","mac":"B8EA6A065647","ip":"4.26.87.55","type":"极光","typeId":4,"description":null,"groupId":"61fbfd08597711e89c5200f1f5115377","groupName":null,"alias":"虚拟终端","roleId":0,"pX":null,"pY":null,"pZ":null,"svrId":null,"updateTime":null,"regionId":null,"no":0,"streamType":null,"status":null,"address":null,"dataType":null,"devFunc":null,"systemTime":null,"pLayer":null}],"extra":{"totalPage":1}}}
    MeetingInfo responseInfo = resultEntity.getData();
    //会管终端列表
    List<VcDevBean> vcDevList = CustomUtil
        .stringToJavaList(responseInfo.getData(), "items", VcDevBean.class);
    if (vcDevList.size() == CommonConstant.zero) {
      //删除本地终端
      vcDevDao.deleteDevice(null);
      // 修改同步锁：表示当前允许业务
      Lock.synDevice.set(false);
      logger.info("同步成功。");
      return ResultUtils.ok("同步成功。");
    }
    //本地终端列表
    List<VcDevBean> localDevList = vcDevDao.getDeviceList();
    if (localDevList.size() == CommonConstant.zero) {
      // 添加会管终端
      vcDevDao.addDeviceList(vcDevList);
      // 修改同步锁：表示当前允许业务
      Lock.synDevice.set(false);
      logger.info("同步成功。");
      return ResultUtils.ok("同步成功。");
    }

    //对比本地终端列表和同步过来的终端列表
    //性能参考：33037个 , 请求耗时：7564毫秒 比对终端耗时：61毫秒, 插入数据库 1分钟
    Map<String, Object> deviceContrast = deviceContrast(vcDevList, localDevList);

    //删除 的id集合
    List<Integer> deleteByIdList = (List<Integer>) deviceContrast.get("deleteByIdList");
    if (deleteByIdList != null && deleteByIdList.size() > CommonConstant.zero) {
      //删除本地多余终端，并解除 终端和村镇、用户关联
      vcDevDao.deleteDevice(deleteByIdList);
    }

    //添加和修改的终端集合
    List<VcDevBean> addList = (List<VcDevBean>) deviceContrast.get("addList");
    if (addList != null && addList.size() > CommonConstant.zero) {
      //新增同步过来多出的终端，并设置未被分配的形态
      //修改重合部分终端。保持当前形态和关联
      vcDevDao.addDeviceList(addList);
    }

    // 修改同步锁：表示当前允许业务
    Lock.synDevice.set(false);
    logger.info("同步成功。");
    return ResultUtils.ok("同步成功。");
  }


  /**
   * 验证服务器返回结果
   *
   * @param responseResult
   * @return
   */
  private boolean checkResponseResult(ResponseInfo responseResult) {
    if (null == responseResult || responseResult.getErrcode() == CommonConstant.one
        || responseResult.getErrcode() == CommonConstant.two) {
      return false;
    } else if (responseResult.getErrcode() == CommonConstant.zero) {
      return true;
    }
    return false;
  }


  /**
   * @param vcList 会管终端列表
   * @param localList 本地终端列表
   * @return addList 添加和修改的 终端集合
   * @Description: 终端集合对比
   * @author quboka
   * @date 2018年5月17日
   */
  private Map<String, Object> deviceContrast(List<VcDevBean> vcList, List<VcDevBean> localList) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    long st = System.currentTimeMillis();

    Map<VcDevBean, Integer> map = new HashMap<VcDevBean, Integer>(vcList.size() + localList.size());
    Map<VcDevBean, VcDevBean> copyMap = new HashMap<VcDevBean, VcDevBean>(
        vcList.size() + localList.size());

    //删除 的id集合
    List<String> deleteByIdList = new ArrayList<String>();

    //添加和修改的终端集合
    List<VcDevBean> addList = new ArrayList<VcDevBean>();

    //判定大小
    List<VcDevBean> maxList = vcList;
    List<VcDevBean> minList = localList;

    // 标记 会管终端为1
    // 遍历会管集合
    for (VcDevBean device : maxList) {  // a b c
      map.put(device, CommonConstant.one);
      copyMap.put(device, device);
    }
    // 标记 相同的终端为 2
    // 遍历本地终端集合
    for (VcDevBean device : minList) {  // b e f g
      Integer cc = map.get(device);
      if (cc != null) {
        //会管终端
        VcDevBean vcDevBean = copyMap.get(device);
        //保留本地独有属性
        vcDevBean.setDeptId(device.getDeptId());//部门id
        vcDevBean.setWindowId(device.getWindowId());//窗口id
        vcDevBean.setState(device.getState()); //状态 0：空闲 1：使用中 -1:不可用 -2：删除
        vcDevBean.setServiceKey(device.getServiceKey());// 服务中心key
        vcDevBean.setAssociated(device.getAssociated());//关联字段。用于扩展关联，当form为4时此字段为高拍仪扩展字段
        vcDevBean.setIp(device.getIp());//终端ip
        vcDevBean.setTypeId(device.getTypeId());//终端类型
        vcDevBean.setIsSingleCamera(device.getIsSingleCamera());//是否支持双usb
        map.put(vcDevBean, CommonConstant.two);
        continue;
      }
      // 标记 本地终端为3
      map.put(device, CommonConstant.three);
    }
    for (Map.Entry<VcDevBean, Integer> entry : map.entrySet()) {
      //1为添加  2为修改
      if (entry.getValue() == CommonConstant.one || entry.getValue() == CommonConstant.two) {
        addList.add(entry.getKey());
        resultMap.put("addList", addList);
      } else {
        //3为删除
        deleteByIdList.add(entry.getKey().getId());
        resultMap.put("deleteByIdList", deleteByIdList);
      }
    }
    logger.info(
        "比对终端耗时：" + (System.currentTimeMillis() - st) + "毫秒,应添加或修改：" + addList.size() + "个,应删除："
            + deleteByIdList.size() + "个.");
    return resultMap;

  }

  /**
   * @param serviceKey 服务中心key
   * @param windowId 窗口id
   * @param form 形态 1：服务中心 2：审批中心 3:默认 4：高拍仪 5：统筹中心
   * @param deviceId 终端id
   * @return Map<String   ,   Object>
   * @Description: 绑定终端
   * @
   * @author quboka
   * @date 2018年5月21日
   */
  @Override
  public Map<String, Object> bindingDeviceAndModify(VcDevVo vcDevVo) {

    boolean isSyn = Lock.synDevice.get();
    if (isSyn) {
      logger.info("绑定终端失败，终端正在同步中");
      return ResultUtils.error("绑定终端失败，终端正在同步中");
    }

    String deviceId = vcDevVo.getDeviceId();
    Integer deptId = vcDevVo.getDeptId();
    Integer windowId = vcDevVo.getWindowId();
    String serviceKey = vcDevVo.getServiceKey();
    Integer tcType = vcDevVo.getTcType();

    boolean exists1 = serviceKey == null || deviceId == null;
    if (exists1) {
      logger.info("绑定终端失败，缺少参数");
      return ResultUtils.error("绑定终端失败，缺少参数");
    }

    //  请先解绑
    VcDevBean device = vcDevDao.getDeviceById(deviceId);
    if (device == null) {
      logger.info("绑定终端失败，请选择正确的终端");
      return ResultUtils.error("绑定终端失败，请选择正确的终端");
    }
    if (device.getServiceKey() != null) {
      logger.info("绑定终端失败，当前终端存在绑定，请先解绑");
      return ResultUtils.error("绑定终端失败，当前终端存在绑定，请先解绑");
    }

    Map<String, Object> deviceMap = new HashMap<String, Object>();
    deviceMap.put("state", VcDevStateEnum.Effective.getValue());
    deviceMap.put("id", deviceId);
    //根据serviceKey查询中心
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenterByServiceKey(serviceKey);
      if (serviceCenter == null) {
          return ResultUtils.error("该中心不存在，请刷新后重试");
      }
    //村终端
    if (CommonConstant.CENTER_THREE == serviceCenter.getType()) {
      //判断要绑定的服务中心是否已经绑定终端
      List<VcDevBean> deviceByServiceKey = vcDevDao.getDeviceByServiceKey(serviceKey);
      if (deviceByServiceKey.size() > CommonConstant.zero) {
        return ResultUtils.error("当前中心已绑定终端，请勿重复绑定");
      }
        if (vcDevVo.getIsSingleCamera() == null) {
            return ResultUtils.error("请选择当前中心摄像头模式");
        }
      deviceMap.put("serviceKey", serviceKey);
      deviceMap.put("isSingleCamera", vcDevVo.getIsSingleCamera());
    } else if (serviceCenter.getType() == CommonConstant.CENTER_TWO) {
      //判断部门是否存在
      SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(deptId);
      if(deptId != 0){
        if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
          logger.info("绑定终端失败，部门不存在");
          return ResultUtils.error("绑定终端失败，部门不存在");
        }
      }
      //查看窗口存不存在
      WindowBean window = windowDao.getWindowById(windowId);
      if (window == null) {
        logger.info("绑定终端失败，窗口不存在");
        return ResultUtils.error("绑定终端失败，窗口不存在");
      }
      //判断窗口是否已经被绑定
      VcDevBean vcDev = vcDevDao.getDeviceByWindowId(windowId);
      if (vcDev != null) {
        logger.info("绑定终端失败，当前窗口已存在绑定的终端");
        return ResultUtils.error("绑定终端失败，当前窗口已存在绑定的终端");
      }
      deviceMap.put("serviceKey", serviceKey);
      deviceMap.put("windowId", windowId);
      deviceMap.put("deptId", deptId);
    } else if (serviceCenter.getType() == CommonConstant.CENTER_ONE) {
      //统筹中心
        if (tcType != null && TcTypeEnum.TC_ASSIST.getType().equals(tcType)) {
            //判断部门是否存在
            SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(deptId);
            if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
                logger.info("绑定终端失败，部门不存在");
                return ResultUtils.error("绑定终端失败，部门不存在");
            }

            Map<String, Object> paramMap = new HashMap();

            VcDevBean deviceByServiceKeyAndDeptId = vcDevDao.getDeviceByServiceKeyAndDeptId(paramMap);
            if (deviceByServiceKeyAndDeptId != null) {
                return ResultUtils.error("当前统筹中心的该部门已绑定终端，请勿重复绑定");
            }
      }
      if (tcType != null && TcTypeEnum.TC_ACCEPT.getType().equals(tcType)) {
        //查看窗口存不存在
        WindowBean window = windowDao.getWindowById(windowId);
        if (window == null) {
          logger.info("绑定终端失败，窗口不存在");
          return ResultUtils.error("绑定终端失败，窗口不存在");
        }
        //判断窗口是否已经被绑定
        VcDevBean vcDev = vcDevDao.getDeviceByWindowId(windowId);
        if (vcDev != null) {
          logger.info("绑定终端失败，当前窗口已存在绑定的终端");
          return ResultUtils.error("绑定终端失败，当前窗口已存在绑定的终端");
        }
        deviceMap.put("windowId", windowId);
      }
      deviceMap.put("serviceKey", serviceKey);
      deviceMap.put("deptId", deptId);
    }
    vcDevDao.updateVcDecvice(deviceMap);
    logger.info("绑定终端成功");
    return ResultUtils.ok("绑定终端成功");
  }

  /**
   * @param deviceId
   * @return Map<String   ,   Object>
   * @Description: 解绑终端
   * @author quboka
   * @date 2018年5月21日
   */
  @Override
  public Map<String, Object> unbindDeviceAndModify(String deviceId) {
    //查询终端
    VcDevBean device = vcDevDao.getDeviceById(deviceId);
    if (device == null) {
      logger.info("解绑终端失败，终端不存在");
      return ResultUtils.error("解绑终端失败，终端不存在");
    }
    if (device.getServiceKey() == null) {
      logger.info("解绑终端失败，当前终端没有绑定");
      return ResultUtils.error("解绑终端失败，当前终端没有绑定");
    }
    if (device.getWindowId() != null) {
      WindowBean window = windowDao.getWindowById(device.getWindowId());
      if (null != window && window.getIsUse().equals(CommonConstant.ISUSE)) {
        logger.info("解绑终端失败，当前终端所绑定窗口还在使用中");
        return ResultUtils.error("解绑终端失败，当前终端所绑定窗口还在使用中");
      }
    }
    if (device.getState() != null && device.getState().equals(VcDevStateEnum.InUse.getValue())) {
      logger.info("解绑终端失败，当前终端还在使用中");
      return ResultUtils.error("解绑终端失败，当前终端还在使用中");
    }
    Integer unbindResult = vcDevDao.unbindDeviceAndModify(deviceId);
    if (unbindResult == CommonConstant.zero) {
      logger.info("解绑终端失败");
      return ResultUtils.error("解绑终端失败");
    }

    logger.info("解绑终端成功");
    return ResultUtils.ok("解绑终端成功");
  }

  /**
   * @param pageNum
   * @param pageSize
   * @param id 终端id 终端号码
   * @param name 终端名称
   * @param serviceKey 服务中心名称
   * @param form 形态 0：未分配 1：村终端 2：镇终端 3:默认终端
   * @return Map<String   ,   Object>
   * @Description: 管理端查询终端列表
   * @author quboka
   * @date 2018年5月21日
   */
  @Override
  public Map<String, Object> getDeviceList(Map<String, Object> paramsMap) {
    Integer pageNum = (Integer) paramsMap.get("pageNum");
    Integer pageSize = (Integer) paramsMap.get("pageSize");
    deviceFilter(paramsMap);
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<VcDevBean> deviceListByMap = vcDevDao.getDeviceListByMap(paramsMap);
    for (VcDevBean vcDevBean : deviceListByMap) {
      EmbeddedServerInfo embeddedServerByVcDevId = embeddedServerDao.getEmbeddedServerByVcDevId(vcDevBean.getId());
      if (embeddedServerByVcDevId != null) {
        vcDevBean.setScuState(ScuOnlineStatusEnum.ONLINE.getValue());
        vcDevBean.setVersion(embeddedServerByVcDevId.getVersion());
      }
    }
      List<EmbeddedServerInfo> allEmbeddedServer = embeddedServerDao.getAllEmbeddedServer();
      if (ScuOnlineStatusEnum.ONLINE.getValue().equals((Integer) paramsMap.get("scuState")) && allEmbeddedServer.size() == 0) {
          deviceListByMap = new ArrayList<>();
      }
    PageInfo<VcDevBean> list = new PageInfo<VcDevBean>(deviceListByMap);
    return ResultUtils.ok("查询终端列表成功", list);
  }

  /**
   * @return Map<String   ,   Object>
   * @Description: 超管获取终端
   * @author jlm
   * @date 2018年11月7日
   */
  @Override
  public Map<String, Object> superGetDevList(Map<String, Object> paramsMap) {

    Integer pageNum = (Integer) paramsMap.get("pageNum");
    Integer pageSize = (Integer) paramsMap.get("pageSize");
    deviceFilter(paramsMap);
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<VcDevBean> deviceList = vcDevDao.superGetDevList(paramsMap);
    for (VcDevBean vcDevBean : deviceList) {
      EmbeddedServerInfo embeddedServerByVcDevId = embeddedServerDao.getEmbeddedServerByVcDevId(vcDevBean.getId());
      if (embeddedServerByVcDevId != null) {
        vcDevBean.setScuState(ScuOnlineStatusEnum.ONLINE.getValue());
        vcDevBean.setVersion(embeddedServerByVcDevId.getVersion());
      }
    }
      List<EmbeddedServerInfo> allEmbeddedServer = embeddedServerDao.getAllEmbeddedServer();
      if (ScuOnlineStatusEnum.ONLINE.getValue().equals((Integer) paramsMap.get("scuState")) && allEmbeddedServer.size() == 0) {
          deviceList = new ArrayList<>();
      }
    PageInfo<VcDevBean> list = new PageInfo<VcDevBean>(deviceList);
    return ResultUtils.ok("查询终端列表成功", list);
  }

  private void deviceFilter(Map<String, Object> paramsMap) {
    Integer scuState = (Integer) paramsMap.get("scuState");
    List<EmbeddedServerInfo> allEmbeddedServer = embeddedServerDao.getAllEmbeddedServer();
    if (ScuOnlineStatusEnum.ONLINE.getValue().equals(scuState) && allEmbeddedServer.size() > 0) {
      List<String> vcDevIdList = new ArrayList<>();
      for (EmbeddedServerInfo embeddedServerInfo : allEmbeddedServer) {
        vcDevIdList.add(embeddedServerInfo.getVcDevId());
      }
      paramsMap.put("vcDevIds", vcDevIdList);
    } else if (ScuOnlineStatusEnum.OFFLINE.getValue().equals(scuState) && allEmbeddedServer.size() > 0) {
      List<String> allDeviceId = vcDevDao.selectAllDeviceId();
      if (allDeviceId.size() > 0) {
        for (EmbeddedServerInfo embeddedServerInfo : allEmbeddedServer) {
          allDeviceId.remove(embeddedServerInfo.getVcDevId());
        }
        paramsMap.put("vcDevIds", allDeviceId);
      }
    }
  }

  /**
   * 查询终端详情
   *
   * @param id
   * @return
   */
  @Override
  public VcDevBean getDeviceDetails(String id) {
    VcDevBean device = vcDevDao.getDeviceById(id);
    //当服务中心是统筹中心时，有窗口的是受理统筹，没有窗口的是协助统筹
    if (device != null && device.getCenterType() != null && ServiceCenterTypeEnum.ADMIN.getType().intValue()
            == device.getCenterType().intValue()) {
      if (device.getWindowId() != null) {
        device.setTcType(TcTypeEnum.TC_ACCEPT.getType());
      } else {
        device.setTcType(TcTypeEnum.TC_ASSIST.getType());
      }
    }
    device.setDeptName(ObjectUtils.isEmpty(device.getDeptName())? "无" : device.getDeptName());
    EmbeddedServerInfo embeddedServerByVcDevId = embeddedServerDao.getEmbeddedServerByVcDevId(device.getId());
    if (embeddedServerByVcDevId != null) {
      device.setVersion(embeddedServerByVcDevId.getVersion());
      device.setScuState(ScuOnlineStatusEnum.ONLINE.getValue());
    }
    return device;
  }

  /**
   * 修改终端信息
   *
   * @param deviceId
   * @param ip
   * @param type
   * @return
   */
  @Override
  public Map<String, Object> updateDeviceDetail(String deviceId, String ip, Integer type) {
    VcDevBean deviceByIp = getDeviceByIp(ip);
    if (deviceByIp != null && (!deviceByIp.getId().equals(deviceId))) {
      logger.info("ip地址已存在");
      return ResultUtils.error("ip地址已存在");
    }
    VcDevBean deviceById = getDeviceDetails(deviceId);
    if (deviceById != null) {
      int i = vcDevDao.updateDeviceDetail(deviceId, ip, type);
      if (i > CommonConstant.zero) {
        logger.info("修改成功");
        return ResultUtils.ok("修改成功");
      }
    }
    logger.info("修改失败");
    return ResultUtils.error("修改失败");
  }

  @Override
  public VcDevBean getDeviceByIp(String ip) {
    return vcDevDao.getDeviceByIP(ip);
  }

  @Override
  public int setIdle(String deviceId) {
    return vcDevDao.setIdle(deviceId);
  }

  @Override
  public Map<String, Object> scuRestart(String deviceId) {
    try {
      EmbeddedBean embeddedBean=new EmbeddedBean();
      //请求scu重启模拟参数
      embeddedBean.setType(Integer.parseInt(CommonConstant.SCURESTART));
      embeddedServerWebSocketHandler
          .sendMessageToEmbeddServer(new TextMessage(JSON.toJSONString(embeddedBean)),
              deviceId, Boolean.FALSE);
      logger.info("SCU设备重启中");
      return ResultUtils.ok("SCU重启成功");
    } catch (Exception e) {
      e.printStackTrace();
      return ResultUtils.error("SCU重启失败");
    }
  }


  @Override
  public Map<String, Object> getScuAllState(String deviceId) {
    try {
      //获取设备状态，协议传输8
        EmbeddedBean embeddedBean = new EmbeddedBean();
        embeddedBean.setType(Integer.parseInt(CommonConstant.SCUALLSTATE));
        embeddedServerWebSocketHandler.sendMessageToEmbeddServer(new TextMessage(JSON.toJSONString(embeddedBean)), deviceId, Boolean.FALSE);
        logger.info("终端详情检测SCU设备状态");
      return ResultUtils.ok("SCU设备获状态获取成功");
    } catch (Exception e) {
      e.printStackTrace();
      return ResultUtils.error("SCU设备状态获取失败");
    }
  }


}










