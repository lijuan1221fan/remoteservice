package com.visionvera.remoteservice.service.impl;

import com.visionvera.api.handler.api.MeetingApiHandler;
import com.visionvera.api.handler.bean.MeetingInfo;
import com.visionvera.api.handler.utils.HttpUtil;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.bean.MeetingUserVO;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.request.MeetingConfigRequest;
import com.visionvera.remoteservice.service.CommonConfigService;
import com.visionvera.remoteservice.service.TaskLoadService;
import com.visionvera.remoteservice.util.CustomUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/***
 * 通用配置业务层
 * @author wangrz
 * @date 2017年12月21日16:19:28
 *
 */
@Service
public class CommonConfigServiceImpl implements CommonConfigService {

  private Logger logger = LogManager.getLogger(getClass());
  @Resource
  private CommonConfigDao commonConfigDao;
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private VcDevDao vcDevDao;
  @Autowired
  private TaskLoadService taskLoadService;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;

  /***
   * 查询指定配置值-通用
   */
  @Override
  public List<CommonConfigBean> findCommonConfigByName(String commonName) {
    return commonConfigDao.findCommonConfigByName(commonName);
  }

  /***
   * 查询指定配置值-通用
   */
  @Override
  public CommonConfigBean getCommonConfigByName(String commonName) {
    return commonConfigDao.getCommonConfigByName(commonName);
  }

  /**
   * 新增
   */
  @Override
  public Map<String, Object> add(Map<String, Object> paramsMap) {
    //校验
    int checkoutResult = commonConfigDao.checkout(paramsMap);
    if (checkoutResult > 0) {
      logger.info("通用名称已存在。");
      return ResultUtils.error("通用名称已存在");
    }
    int addResult = commonConfigDao.add(paramsMap);
    if (addResult > 0) {
      logger.info("增加通用值成功。");
      return ResultUtils.ok("增加通用值成功");
    }

    return ResultUtils.error("增加通用值失败");
  }

  /**
   * 删除
   */
  @Override
  public Map<String, Object> delete(Integer id) {
    int deleteResult = commonConfigDao.delete(id);
    if (deleteResult > 0) {
      logger.info("删除通用值成功。");
      return ResultUtils.ok("删除通用值成功");
    }

    logger.info("删除通用值失败。");
    return ResultUtils.error("删除通用值失败");
  }

  /**
   * 修改
   */
  @Override
  public Map<String, Object> update(CommonConfigBean commonConfig) {
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    //校验
    paramsMap.put("id", commonConfig.getId());
    paramsMap.put("commonName", commonConfig.getCommonName());
    int checkoutResult = commonConfigDao.checkout(paramsMap);
    if (checkoutResult > 0) {
      logger.info("通用名称已存在。");
      return ResultUtils.error("通用名称已存在");
    }
    //修改
    int updateResult = commonConfigDao.update(commonConfig);
    if (updateResult > 0) {
      logger.info("修改成功。");
      return ResultUtils.ok("修改成功");
    }
    return ResultUtils.error("修改失败。");
  }

  /**
   * 批量修改
   */
  @Override
  public Map<String, Object> updateAll(Map<String, Object> paramsMap) {
    Map<String, Object> para = new HashMap<String, Object>(2);
    //遍历map
    Set<Entry<String, Object>> entries = paramsMap.entrySet();
    int n = 0;
    //如果二级标题部门不是全部部门时，不再保存到commom_config表中
    HashSet<Object> set = new HashSet<>();
    for (Entry<String, Object> entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue().toString();
      if("publicSubhead".equalsIgnoreCase(key) && !"0".equals(value)){
        set.add(entry);
      }
      if("socialSubhead".equalsIgnoreCase(key)){
        set.add(entry);
      }
    }
    //只有部门为全部部门时，才会保存到common_config表中
    if(set.size() == 2){
      entries.removeAll(set);
    }
    for (Entry<String, Object> entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue().toString();
      para.put("commonName", key);
      para.put("commonValue", value);
      boolean flag = value == null || "".equals(value);
      if (flag && "meeting_login_password".equalsIgnoreCase(key)) {
        String loginPwd = commonConfigDao.getCommonConfigByName("meeting_login_password")
            .getCommonValue();
        para.put("commonValue", loginPwd);
      }
      int updateResult = commonConfigDao.updateValueByKeyAndVersion(para);
      if (updateResult > 0) {
        if (key.equalsIgnoreCase(CommonConstant.CMS_IMAGE_URI_KEY)) {
          CommonConstant.CMS_IMAGE_URI = value;
        }
        logger.info(key + ":修改成功。");
      } else {
        logger.info(key + ":修改失败。");
        n++;
      }
    }
    if (paramsMap.containsKey(CommonConstant.MAX_TASKS_NAME)) {
      Object o = paramsMap.get(CommonConstant.MAX_TASKS_NAME);
      if (o != null) {
        taskLoadService.changeTaskNum(Integer.parseInt(o.toString()));
      }
    }
    if (n != 0) {
      logger.info("修改失败。");
      return ResultUtils.error("修改失败");
    }
    logger.info("修改成功。");
    return ResultUtils.ok("修改成功");
  }


  /**
   * 查询列表
   */
  @Override
  public Map<String, Object> getList() {
    List<CommonConfigBean> list = commonConfigDao.getList();

    if (list == null || list.size() == 0) {
        logger.info("查询成功，无数据");
        return ResultUtils.ok("查询成功，无数据", list);
    }
    SysUserBean user = ShiroUtils.getUserEntity();
    if(user.getDeptId() != 0){
      for (CommonConfigBean commonConfigBean : list) {
        if("socialSubhead".equals(commonConfigBean.getCommonName())){
          commonConfigBean.setCommonValue(sysDeptBeanDao.selectByPrimaryKey(user.getDeptId()).getDeptTitle());
        }
      }
    }
    logger.info("查询成功。");
    return ResultUtils.ok("查询成功", list);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> checkMeetingConfig(MeetingConfigRequest meetingConfigRequest) {
    ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler
        .doRegister(HttpUtil.getIpByUrl(meetingConfigRequest.getBaseUrl()),
            HttpUtil.getPortByUrl(meetingConfigRequest.getBaseUrl()),
            meetingConfigRequest.getLoginName(), meetingConfigRequest.getLoginPwd());
    logger.info("会管登陆返回：" + resultEntity);
    if (!resultEntity.getResult()) {
      return ResultUtils.error("验证失败");
    }
    MeetingInfo responseResult = resultEntity.getData();
    Map<String, Object> commonMap = new HashMap<>();
    //获取登陆token
    String accessToken = responseResult.getAccess_token();
    //获取用户信息
    List<MeetingUserVO> userList = CustomUtil
        .stringToJavaList(responseResult.getData(), "items", MeetingUserVO.class);
    if (CollectionUtils.isEmpty(userList)) {
      logger.error("验证失败,获取会管用户信息失败");
      return ResultUtils.error("验证失败");
    }
    String uuid = userList.get(0).getUuid();
    String groupId = userList.get(0).getGroupId();
    commonMap.put("commonName", "meeting_token");
    commonMap.put("commonValue", accessToken);
    commonConfigDao.updateValueByKey(commonMap);
    commonMap.put("commonName", "meeting_uuid");
    commonMap.put("commonValue", uuid);
    commonConfigDao.updateValueByKey(commonMap);
    commonMap.put("commonName", "meeting_groupId");
    commonMap.put("commonValue", groupId);
    commonConfigDao.updateValueByKey(commonMap);
    boolean checkResponseResult = checkResponseResult(responseResult);
    if (!checkResponseResult) {
      logger.error(responseResult.getErrmsg());
      return ResultUtils.error("验证失败");
    }
    logger.info("验证成功,登陆会管成功。");
    return ResultUtils.ok("验证成功");
  }

    @Override
    public Map<String, Object> changeVersion(Integer version) {
        //判断当前是否存在未完成业务
        List<BusinessInfo> businessInfoList = businessInfoDao.getBusinessInfoList();
        logger.info("BusinessInfo数据：" + businessInfoList.toString());
        if (businessInfoList.size() > 0) {
            return ResultUtils.error("当前存在业务正在办理中或排队中，请稍后再试");
        }
        Map<String, Object> commonMap = new HashMap<>(2);
        commonMap.put("commonName", "system_version");
        commonMap.put("commonValue", version);
        commonConfigDao.updateValueByKey(commonMap);
        commonMap.put("commonName", "meeting_url");
        commonMap.put("commonValue", "");
        commonConfigDao.updateValueByKey(commonMap);
        commonMap.put("commonName", "cms_url");
        commonMap.put("commonValue", "");
        commonConfigDao.updateValueByKey(commonMap);
        Integer num = vcDevDao.deleteAll();
        logger.info("删除终端数量:" + num);
        return ResultUtils.ok("设置成功");
    }

  private boolean checkResponseResult(MeetingInfo responseResult) {
    if (null == responseResult || responseResult.getErrcode() == 1
        || responseResult.getErrcode() == 2) {
      return false;
    }
    return responseResult.getErrcode() == 0;
  }
}
