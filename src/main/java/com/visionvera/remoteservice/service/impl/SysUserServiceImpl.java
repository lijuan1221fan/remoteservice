package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysUserStateEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.common.lock.Lock;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.*;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.MeetingService;
import com.visionvera.remoteservice.service.SysUserService;
import com.visionvera.remoteservice.util.EncryptionUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author xueshiqi
 * @Description:
 * @date 2018/10/12 0012
 */
@Service
public class SysUserServiceImpl implements SysUserService {

  private static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

  @Resource
  private SysUserDao sysUserDao;

  @Resource
  private SysRoleDao sysRoleDao;

  @Resource
  private VcDevDao vcDevDao;

  @Resource
  private WindowDao windowDao;

  @Resource
  private SysDeptBeanDao sysDeptBeanDao;

  @Resource
  private ServiceCenterDao serviceCenterDao;

  @Resource
  private BusinessInfoService businessInfoService;

  @Resource
  private MeetingService meetingService;

  @Resource
  private CommonConfigDao commonConfigDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
    @Resource
    private MeetingOperationDao meetingOperationDao;


  @Override
  public Map<String, Object> getSysUserList(SysUserBean sysUserBean) {
    // Integer pageNum = (Integer) params.get("pageNum");
    // Integer pageSize = (Integer) params.get("pageSize");
    // String userName = (String) params.get("userName");
    Integer pageNum = sysUserBean.getPageNum();
    Integer pageSize = sysUserBean.getPageSize();
    Map paramsMap = new HashMap();
    paramsMap.put("userName", sysUserBean.getUserName());
    paramsMap.put("userId", ShiroUtils.getUserId());
    ServiceCenterBean serviceCenterBean = new ServiceCenterBean();
    List<String> serviceCenterKeyList = new ArrayList<>();
    List<SysDeptBean> deptList = new ArrayList<>();
    List<SysUserBean> list = new ArrayList<>();
    SysDeptBean sysDeptBean = new SysDeptBean();
    sysDeptBean.setId(ShiroUtils.getUserEntity().getDeptId());
    serviceCenterBean.setState(StateEnum.Effective.getValue());
    //超管 看见所有中心用户
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SUPER_ADMIN)) {
      deptList = sysDeptBeanDao.getSysDeptListNoPage(null);
      deptList.add(sysDeptBean);
      paramsMap.put("deptList", deptList);
      serviceCenterBean.setType(1L);
      List<ServiceCenterBean> firstList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      for (ServiceCenterBean first : firstList) {
        serviceCenterKeyList.add(first.getServiceKey());
        serviceCenterBean.setType(2L);
        serviceCenterBean.setParentKey(first.getServiceKey());
        List<ServiceCenterBean> secondList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        for (ServiceCenterBean second : secondList) {
          serviceCenterKeyList.add(second.getServiceKey());
        }
      }
      paramsMap.put("serviceCenterKeyList", serviceCenterKeyList);
      PageHelper.startPage(pageNum, pageSize);
      list = sysUserDao.getSysUserList(paramsMap);
    }
    //统筹中心管理员 1所有部门 看见本中心及以下所有部门数据
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.FIRST_ADMIN)
        && ShiroUtils.getUserEntity().getDeptId() == 0) {
      deptList = sysDeptBeanDao.getSysDeptListNoPage(null);
      deptList.add(sysDeptBean);
//            serviceCenterKeyList.add(ShiroUtils.getUserEntity().getServiceKey());
      serviceCenterBean.setType(1L);
      List<ServiceCenterBean> firstList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      for (ServiceCenterBean first : firstList) {
        serviceCenterKeyList.add(first.getServiceKey());
        serviceCenterBean.setType(2L);
        serviceCenterBean.setParentKey(first.getServiceKey());
        List<ServiceCenterBean> secondList = serviceCenterDao
            .getServiceCenterByCondition(serviceCenterBean);
        for (ServiceCenterBean second : secondList) {
          serviceCenterKeyList.add(second.getServiceKey());
        }
      }
      paramsMap.put("serviceCenterKeyList", serviceCenterKeyList);
      paramsMap.put("deptList", deptList);
      PageHelper.startPage(pageNum, pageSize);
      list = sysUserDao.getSysUserList(paramsMap);
    }
    //统筹中心管理员 具体部门 看见本中心及以下所有具体部门数据
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.FIRST_ADMIN)
        && ShiroUtils.getUserEntity().getDeptId() != 0) {
//            serviceCenterKeyList.add(ShiroUtils.getUserEntity().getServiceKey());
      serviceCenterBean.setType(2L);
      serviceCenterBean.setParentKey(ShiroUtils.getUserEntity().getServiceKey());
      List<ServiceCenterBean> secondList = serviceCenterDao
          .getServiceCenterByCondition(serviceCenterBean);
      for (ServiceCenterBean second : secondList) {
        serviceCenterKeyList.add(second.getServiceKey());
      }
      deptList.add(sysDeptBean);
      paramsMap.put("serviceCenterKeyList", serviceCenterKeyList);
      paramsMap.put("deptList", deptList);
      PageHelper.startPage(pageNum, pageSize);
      list = sysUserDao.getSysUserList(paramsMap);
    }
    //审批中心管理员 1所有部门 看见本中心及以下所有部门数据
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SECOND_ADMIN)
        && ShiroUtils.getUserEntity().getDeptId() == 0) {
      deptList = sysDeptBeanDao.getSysDeptListNoPage(null);
      deptList.add(sysDeptBean);
      serviceCenterKeyList.add(ShiroUtils.getUserEntity().getServiceKey());
      paramsMap.put("serviceCenterKeyList", serviceCenterKeyList);
      paramsMap.put("deptList", deptList);
      PageHelper.startPage(pageNum, pageSize);
      list = sysUserDao.getSysUserList(paramsMap);
    }
    //审批中心管理员 具体部门 看见本中心及以下所有具体部门数据
    if (ShiroUtils.getUserEntity().getType().equals(CommonConstant.SECOND_ADMIN)
        && ShiroUtils.getUserEntity().getDeptId() != 0) {
      serviceCenterKeyList.add(ShiroUtils.getUserEntity().getServiceKey());
      deptList.add(sysDeptBean);
      paramsMap.put("serviceCenterKeyList", serviceCenterKeyList);
      paramsMap.put("deptList", deptList);
      paramsMap.put("type", SysUserTypeEnum.WholeCenterSalesman.getValue());
      PageHelper.startPage(pageNum, pageSize);
      list = sysUserDao.getSysUserList(paramsMap);
    }
    if (list.size() == 0) {
      logger.info("获取用户列表成功");
      return ResultUtils.ok("获取用户列表成功", new PageInfo<SysUserBean>(new ArrayList<SysUserBean>()));
    }
    logger.info("获取用户列表成功");
    PageInfo<SysUserBean> pageInfo = new PageInfo<SysUserBean>(list);
    return ResultUtils.ok("获取用户列表成功", pageInfo);
  }

  @Override
  public Map<String, Object> deleteSysUser(List<Integer> userIdList) {
    int i = 0;
    List<SysUserBean> list = sysUserDao.getUserByIds(userIdList);
    if(list.size() > 0){
      return ResultUtils.error("用户在线中，请禁止删除");
    }
    try {
      i = sysUserDao.deleteSysUser(userIdList);
    } catch (Exception e) {
      logger.error("删除用户错误", e);
      return ResultUtils.error("删除用户错误");
    }
    if (i < 0) {
      return ResultUtils.error("删除用户失败");
    }
    return ResultUtils.ok("删除用户成功");
  }


  @Override
  public Map<String, Object> logout(HttpServletRequest request) {

    // 结束任务
    List<BusinessInfo> businessInfoList = businessInfoService.getBusinessesByUserId(ShiroUtils.getUserId());

    // 1停会
    if(businessInfoList.size() > 0){
      Map<String, Object> stopMeetingAndModify = meetingService.stopMeetingAndModify(businessInfoList.get(0).getId());
      if (!(boolean) stopMeetingAndModify.get("result")) {
        logger.info("保存业务失败 " + stopMeetingAndModify.get("msg"));
        return ResultUtils.error( "保存业务失败。" + stopMeetingAndModify.get("msg"));
      }
      //退出时停会并删除meetingOperation记录以便可再次拉会
      MeetingOperation result = meetingOperationDao.selectByBusinessId(businessInfoList.get(0).getId());
      if (result != null) {
        logger.info("退出时执行删除会议记录操作");
        meetingOperationDao.deleteByPrimaryKey(result.getId());
      }
    }


    //2修改任务负载量
    if (CommonConstant.taskNumber.get() < CommonConstant.MAX_TASKS.get()) {
      CommonConstant.taskNumber.incrementAndGet();
    }

    // 修改用户工作状态
    Map<String, Integer> workStateMap = new HashMap<>();
    workStateMap.put("workState", CommonConstant.WORK_STATE_LEISURE);
    workStateMap.put("userId", ShiroUtils.getUserId());
    int workStatusResult = sysUserDao.updateWorkState(workStateMap);
    if (workStatusResult > 0) {
      logger.info("用户:" + ShiroUtils.getUserEntity().getLoginName() + "修改工作状态成功。");
    }

      //判断当前业务员是否存在未完成业务
      BusinessInfo businessInfo = businessInfoService.getBusinessByUserId(ShiroUtils.getUserId());
      if (businessInfo != null) {
          logger.info("用户：" + ShiroUtils.getUserEntity().getLoginName() + "退出成功,存在未完成业务");
          return ResultUtils.ok("用户退出成功");
      }

    //修改当前业务员对应窗口状态
    WindowBean windowBean = windowDao.getUserWindowByUserId(ShiroUtils.getUserId());
    if (windowBean != null) {
      //将窗口置为空闲状态
        logger.info("用户退出时，执行窗口空闲，并删除关联");
      windowDao.updateWindowUseStatus(windowBean.getId(), WindowUseStateEnum.Free.getValue());
      //删除关联关系
      windowDao.deleteWindowAndUserRelation(null, ShiroUtils.getUserId());
    }

    logger.info("用户：" + ShiroUtils.getUserEntity().getLoginName() + "退出成功");
    return ResultUtils.ok("用户退出成功");

  }

  @Override
  public Set<String> getUserPermSet(Integer userId) {
    List<String> permList;
    SysUserBean sysUserInfo = sysUserDao.getSysUserInfo(userId);
    //根用户,拥有所有权限
    if (sysUserInfo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
      List<SysPermissionBean> sysMenus = sysRoleDao.getAllPermission();
      permList = new ArrayList<>(sysMenus.size());
      for (SysPermissionBean sysMenu : sysMenus) {
        permList.add(sysMenu.getPerms());
      }
    } else {
      // 非根用户权限
      permList = sysRoleDao.getPermsListByUserId(userId);
    }

    //获取权限值set集合
    Set<String> permSet = new HashSet<>();
    for (String perm : permList) {
      if (StringUtils.isBlank(perm)) {
        continue;
      }
      permSet.addAll(Arrays.asList(perm.trim().split(",")));
    }

    return permSet;
  }

  @Override
  public SysUserBean getByUserName(String loginName) {
    return sysUserDao.getByUserName(loginName);
  }

  @Override
  public Set<String> getUserRoleSet(Integer userId) {
    List<String> roleList = sysRoleDao.getRoles(userId);
    Set<String> roleSet = new HashSet<>();
    for (String role : roleList) {
      if (StringUtils.isNotBlank(role)) {
        roleSet.addAll(Arrays.asList(role.trim().split(",")));
      }
    }
    return roleSet;
  }

  @Override
  public Map<String, Object> addUser(SysUserBean sysUserBean) {
    SysUserBean byUserName = sysUserDao.getByUserName(sysUserBean.getLoginName());
    if (byUserName != null) {
      return ResultUtils.error("添加用户失败，用户名已存在");
    }
    //校验中心是否可用
    ServiceCenterBean parentServiceCenter = serviceCenterDao
        .getServiceCenterByServiceKey(sysUserBean.getServiceKey());
    if(parentServiceCenter == null || StateEnum.Invalid.getValue().equals(parentServiceCenter.getState())){
      logger.info("添加失败，中心不存在");
      return ResultUtils.error("添加失败，中心不存在");
    }
    //校验部门是否可用，所有部门不需要校验
    if(!sysUserBean.getDeptId().equals(0)){
      SysDeptBean dept = sysDeptBeanDao.getDeptInfo(sysUserBean.getDeptId());
      if(dept == null || StateEnum.Invalid.getValue().equals(dept.getState())){
        logger.info("添加失败，部门不存在");
        return ResultUtils.error("添加失败，部门不存在");
      }
    }
    //校验角色是否可用，业务员不需要检验
    boolean b = !sysUserBean.getType().equals(SysUserTypeEnum.WholeCenterSalesman.getValue())
            && !sysUserBean.getType().equals(SysUserTypeEnum.AuditCenterSalesman.getValue());
    if(b){
      if(sysUserBean.getRoleId() == null){
        return ResultUtils.error("添加用户失败，角色不能为空");
      }
      SysRoleBean sysRoleInfo = sysRoleDao.getSysRoleInfo(sysUserBean.getRoleId());
      if(sysRoleInfo == null){
        logger.info("添加失败，所选角色不存在");
        return ResultUtils.error("添加失败，所选角色不存在");
      }
    }
    //盐
    String salt = UUID.randomUUID().toString().replaceAll("-", "");
    sysUserBean.setSalt(salt);
    //MD5  盐 + 密码
    sysUserBean.setPassword(EncryptionUtil.encrypt(sysUserBean.getPassword(), sysUserBean.getSalt()));
    //插如用户表
    int i = sysUserDao.addUser(sysUserBean);
    //插入角色用户关联表
    if (i > 0 && b) {
      sysUserDao.addSysUserRoleRel(sysUserBean.getUserId(), sysUserBean.getRoleId());
      return ResultUtils.ok("添加用户成功");
    }
    if(i < 1){
      return ResultUtils.error("添加用户失败");
    }
    return ResultUtils.ok("添加用户成功");
  }

  @Override
  public Map<String, Object> editUser(SysUserBean sysUserBean) {
    SysUserBean list = sysUserDao.getSysUserInfo(sysUserBean.getUserId());
    if(null != list&& SysUserStateEnum.OnLine.getValue().equals(Integer.valueOf(list.getState()))){
      return ResultUtils.error("用户在线中，请禁止编辑");
    }
    SysUserBean user = sysUserDao.getSysUserByLoginNameAndUserId(sysUserBean.getLoginName());
    if (user != null) {
      if (!user.getUserId().equals(sysUserBean.getUserId())) {
        return ResultUtils.error("更改用户信息失败,用户名已存在.");
      }
    }
    //校验中心是否可用
    ServiceCenterBean parentServiceCenter = serviceCenterDao
        .getServiceCenterByServiceKey(sysUserBean.getServiceKey());
    if(parentServiceCenter == null || StateEnum.Invalid.getValue().equals(parentServiceCenter.getState())){
      logger.info("编辑失败，中心不存在");
      return ResultUtils.error("编辑失败，中心不存在");
    }
    //校验部门是否可用
    if(!sysUserBean.getDeptId().equals(0)){
      SysDeptBean dept = sysDeptBeanDao.getDeptInfo(sysUserBean.getDeptId());
      if(dept == null || StateEnum.Invalid.getValue().equals(dept.getState())){
        logger.info("编辑失败，部门不存在");
        return ResultUtils.error("编辑失败，部门不存在");
      }
    }
    //校验角色是否可用
    boolean b = !sysUserBean.getType().equals(SysUserTypeEnum.WholeCenterSalesman.getValue())
            && !sysUserBean.getType().equals(SysUserTypeEnum.AuditCenterSalesman.getValue());
    if(b){
      if(sysUserBean.getRoleId() == null){
        return ResultUtils.error("编辑用户失败，角色不能为空");
      }
      SysRoleBean sysRoleInfo = sysRoleDao.getSysRoleInfo(sysUserBean.getRoleId());
      if(sysRoleInfo == null){
        logger.info("编辑失败，所选角色不存在");
        return ResultUtils.error("编辑失败，所选角色不存在");
      }
    }
    //修改用户表
    int i = sysUserDao.editUser(sysUserBean);
    //修改角色用户管理表
    if (i > 0 && b) {
      sysUserDao.updateSysUserRoleRel(sysUserBean.getUserId(), sysUserBean.getRoleId());
      return ResultUtils.ok("编辑用户成功");
    }
    if (i < 1) {
      return ResultUtils.error("编辑用户失败");
    }
    return ResultUtils.ok("编辑用户成功");
  }

  @Override
  public Map<String, Object> getRegionInfoByUser() {
    String serviceKey = ShiroUtils.getUserEntity().getServiceKey();
    ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenter(serviceKey);
    return ResultUtils.ok("查询当前登录用户所属行政机构", serviceCenter);
  }

  @Override
  public Map<String, Object> userEntryQueueAndModify(Integer userId, HttpServletRequest request) {
    SysUserBean user = sysUserDao.getSysUserInfo(userId);
    if (user == null) {
      logger.info("用户进入工作队列失败，无此用户");
      return ResultUtils.error("用户进入工作队列失败，无此用户");
    }
    //  判断是否在同步终端
    boolean isSyn = Lock.synDevice.get();
    if (isSyn) {
      logger.info("用户进入工作队列失败，终端正在同步中");
      return ResultUtils.error("用户进入工作队列失败，终端正在同步中");
    }
    //当有恢复业务时，窗口是使用中，不做最大业务数限制校验，如果是无业务处理中的业务员登录，校验在线办理数
    BusinessInfo info = businessInfoDao.getBusinessByUserId(userId);
    if(info == null){
      //判断最大业务办理数
      boolean isfull  = limitOnLine();
      if(!isfull){
        return ResultUtils.error("获取任务失败。当前办理人数已达系统上限，请稍后再试。");
      }
    }
    //todo -jlm
    //查询用户对应的窗口
    WindowBean window = windowDao.getUserWindowByUserId(userId);
    if (window == null) {
      return ResultUtils.error("请退出选择窗口后，开始办理业务");
    }
    //开始受理将用户对应窗口置为等待状态
    windowDao.updateWindowUseStatus(window.getId(), WindowUseStateEnum.Wait.getValue());
    //判断窗口是否绑定终端
    VcDevBean vcDev = vcDevDao.getDeviceByWindowId(window.getId());
    if (vcDev == null) {
      return ResultUtils.error("当前窗口未绑定终端,请绑定后再次尝试！");
    }
    // 修改用户工作状态
    Map<String, Integer> workStatusMap = new HashMap<String, Integer>();
    workStatusMap.put("workState", CommonConstant.WORK_STATE_WAIT);
    workStatusMap.put("userId", userId);
    int workStatusResult = sysUserDao.updateWorkState(workStatusMap);
    if (workStatusResult > 0) {
      logger.info("用户:" + userId + "修改工作状态成功");
    }
    logger.info("用户进入工作队列成功");
    return ResultUtils.ok("用户进入工作队列成功");
  }

  @Override
  public Map<String, Object> updateUserStatus(SysUserBean sysUserBean) {
    SysUserBean sysUser = sysUserDao.getSysUserInfo(sysUserBean.getUserId());
    if(sysUser == null || StateEnum.Invalid.getValue().equals(sysUser.getState())){
      return ResultUtils.error("该用户不存在");
    }
    int updateUserStatus = sysUserDao.updateUserState(sysUserBean);
    if (updateUserStatus > 0) {
      logger.info("用户启用/禁用成功");
      return ResultUtils.ok("用户启用/禁用成功");
    } else {
      logger.info("用户启用/禁用失败");
      return ResultUtils.error("用户启用/禁用失败");
    }
  }

  @Override
  public Map<String, Object> resetPswd(ResetPassWord resetPassWord) {
    SysUserBean sysUser = sysUserDao.getSysUserInfo(resetPassWord.getUserId());
    //MD5  盐 + 密码
    String newPswd = EncryptionUtil.encrypt(resetPassWord.getPwd(), sysUser.getSalt());
    SysUserBean sysUserBean = new SysUserBean();
    sysUserBean.setUserId(resetPassWord.getUserId());
    sysUserBean.setPassword(newPswd);
    int i = sysUserDao.resetPswd(sysUserBean);
    if (i > 0) {
      logger.info("重置密码成功");
      return ResultUtils.ok("重置密码成功");
    } else {
      logger.info("重置密码失败");
      return ResultUtils.error("重置密码失败");
    }
  }

  @Override
  public Map<String, Object> updatePswd(UpdatePswd updatePswd) {
    int i = 0;
    SysUserBean sysUser = sysUserDao.getSysUserInfo(ShiroUtils.getUserId());
    // 验证用户id与旧密码一致时更新密码
    String oldPwd = updatePswd.getOldPwd();
    String checkOldPwd = EncryptionUtil.encrypt(oldPwd, sysUser.getSalt());
    if (checkOldPwd.equals(sysUser.getPassword())) {
      String newPwd = updatePswd.getNewPwd();
      String toChangePwd = EncryptionUtil.encrypt(newPwd, sysUser.getSalt());
      SysUserBean sysUserBean = new SysUserBean();
      sysUserBean.setUserId(ShiroUtils.getUserId());
      sysUserBean.setPassword(toChangePwd);
      i = sysUserDao.resetPswd(sysUserBean);
    } else {
      return ResultUtils.error("原密码错误");
    }
    if (i < 0) {
      return ResultUtils.error("修改密码失败");
    }
    return ResultUtils.ok("修改密码成功");
  }

  @Override
  public Map<String, Object> getSysUserDetail(Integer userId) {
    SysUserBean sysUser = sysUserDao.getSysUserDetail(userId);
    logger.info("用户详情：" + sysUser);
    return ResultUtils.check(sysUser);
  }

  @Override
  public Map<String, Object> getLoginUserInfo(Integer userId) {
    SysUserBean sysUserInfo = sysUserDao.getLoginUserInfo(userId);
    return ResultUtils.ok("查询登录用户信息成功", sysUserInfo);
  }

  /**
   * @return 在线办理人数
   */
  @Override
  public boolean limitOnLine() {
    //todo -jlm
    //查询处于工作状态的窗口数量
    List<WindowBean> listWorkingWindows = windowDao.getListWorkingWindows();
    CommonConfigBean commonBean = commonConfigDao.getCommonConfigByName("max_tasks");
    Integer number = Integer.valueOf(commonBean.getCommonValue());
    if (listWorkingWindows.size() > number) {
      logger.info("获取任务失败。当前办理人数已达系统上限，请稍后再试。");
      return false;
    }
    return true;
  }
}
