package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.dao.SysLogDao;
import com.visionvera.common.entity.SysLog;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.common.utils.HttpContextUtils;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.LockGroup;
import com.visionvera.common.validator.group.LoginGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.ResetPassWord;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UpdatePswd;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.service.SysUserService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueshiqi
 * @Description: 用户controller
 * @date 2018/10/12
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends AbstractController {

  private static Logger logger = LoggerFactory.getLogger(SysUserController.class);

  @Resource
  private SysUserService sysUserService;

  @Resource
  private SysLogDao sysLogDao;

  @Resource
  private SysUserDao sysUserDao;

    @Resource
    private WindowDao windowDao;

  /**
   * 用户列表
   *
   * @param params
   * @return
   */
  @RequiresPermissions("user:query")
  @PostMapping(value = "/getSysUserList")
  public Map<String, Object> getSysUserList(@RequestBody SysUserBean sysUserBean) {
    ValidateUtil.validate(sysUserBean,QueryGroup.class);
    return sysUserService.getSysUserList(sysUserBean);
  }

  /**
   * 　　* @Description: 删除用户 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  @SysLogAnno("删除用户信息")
  @RequiresPermissions("user:delete")
  @GetMapping(value = "/deleteSysUser")
  public Map<String, Object> deleteSysUser(@RequestParam List<Integer> userIds) {
    AssertUtil.isEmpty(userIds,"参数缺失");
    return sysUserService.deleteSysUser(userIds);
  }

  /**
   * 获取登录的用户信息
   */
  @PostMapping("/info")
  public Map<String, Object> info() {
    return sysUserService.getLoginUserInfo(ShiroUtils.getUserId());
  }

  /**
   * 　　* @Description: 登录 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  @SysLogAnno("用户登录操作")
  @PostMapping(value = "/login")
  public Map<String, Object> login(@RequestBody SysUserBean sysUserBean) {
    ValidateUtil.validate(sysUserBean,LoginGroup.class);
    try {
      Subject subject = ShiroUtils.getSubject();
      //sha256加密
      UsernamePasswordToken token = new UsernamePasswordToken(sysUserBean.getLoginName(), sysUserBean.getPassword());
      subject.login(token);
        //登录时判断当前用户是否关联窗口，并且关联的窗口处于等待状态，此时将窗口置为空闲状态
        WindowBean window = windowDao.getUserWindowByUserId(ShiroUtils.getUserId());
        if (window != null && WindowUseStateEnum.Wait.getValue().equals(window.getIsUse())) {
            logger.info("登录时，用户存在关联的窗口处于等待中，执行将窗口置为空闲");
            windowDao.updateWindowUseStatus(window.getId(), WindowUseStateEnum.Free.getValue());
        }
    } catch (UnknownAccountException | LockedAccountException e) {
      logger.info(e.getMessage());
      return ResultUtils.error(e.getMessage());
    } catch (IncorrectCredentialsException e) {
      logger.info(e.getMessage());
      return ResultUtils.error("账户名或密码不正确");
    } catch (AuthenticationException e) {
      logger.error(e.getMessage());
      return ResultUtils.error("账户验证失败");
    }
    return ResultUtils.ok("登陆成功");
  }

  /**
   * 退出
   */
  @PostMapping(value = "/logout")
  public Map<String, Object> logout() {
    SysLog sysLog = new SysLog();
    //获取request
    HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
    long begin = System.currentTimeMillis();
    if (ShiroUtils.getUserEntity().getType().equals(SysUserTypeEnum.WholeCenterSalesman.getValue())
            || ShiroUtils.getUserEntity().getType().equals(SysUserTypeEnum.AuditCenterSalesman.getValue())) {
      Map<String, Object> logout = sysUserService.logout(request);
      if (!(boolean) logout.get("result")) {
        return logout;
      }
    }
    // 退出时修改用户状态
    Map<String, Integer> stateMap = new HashMap<String, Integer>();
    stateMap.put("state", Integer.valueOf(CommonConstant.USER_OFF_LINE));
    stateMap.put("userId", ShiroUtils.getUserId());
    int stateResult = sysUserDao.updateState(stateMap);
    if (stateResult > 0) {
      logger.info("用户:" + ShiroUtils.getUserEntity().getLoginName() + "状态修改成功");
    }
    //用户名
    SysUserBean user = ShiroUtils.getUserEntity();
    sysLog.setUserId(Long.valueOf(user.getUserId()));
    sysLog.setUsername(user.getLoginName());
    sysLog.setcTime(new Timestamp(System.currentTimeMillis()));
    sysLog.setMethod("com.visionvera.remoteservice.controller.SysUserController.logout()");
    sysLog.setOperation("用户退出操作");
    ShiroUtils.logout();
    long end = System.currentTimeMillis();
    sysLog.setTime(end - begin);
    sysLogDao.insertLog(sysLog);
    return ResultUtils.ok("退出成功");
  }

  /**
   * 　　* @Description: 添加用户 　　* @author: xueshiqi 　　* @date: 2018/11/5
   */
  @RequiresPermissions("user:add")
  @PostMapping(value = "/addUser")
  public Map<String, Object> addUser(@RequestBody SysUserBean sysUserBean) {
    ValidateUtil.validate(sysUserBean,AddGroup.class);
    if(sysUserBean.getType().equals(CommonConstant.FIRST_ADMIN) || sysUserBean.getType().equals(CommonConstant.SECOND_ADMIN)){
      if(sysUserBean.getRoleId() == null){
        return ResultUtils.error("用户角色不能为空");
      }
    }
    return sysUserService.addUser(sysUserBean);
  }

  /**
   * 　　* @Description: 编辑用户 　　* @author: xueshiqi 　　* @date: 2018/11/5
   */
  @RequiresPermissions("user:edit")
  @PostMapping(value = "editUser")
  public Map<String, Object> editUser(@RequestBody SysUserBean sysUserBean) {
    ValidateUtil.validate(sysUserBean,UpdateGroup.class);
    return sysUserService.editUser(sysUserBean);
  }

  /**
   * 查询当前登录用户所属行政机构
   *
   * @return
   */
  @GetMapping("/getRegionInfoByUser")
  public Map<String, Object> getRegionInfoByUser() {
    return sysUserService.getRegionInfoByUser();
  }

  /**
   * @param userId 用户id
   * @return Map<String       ,       Object>
   * @Description: 空闲用户 进入队列 (开始受理业务)
   * @author quboka
   * @date 2018年3月23日
   */
  @RequestMapping(value = "/userEntryQueue", method = RequestMethod.GET)
  public Map<String, Object> userEntryQueue(@RequestParam Integer userId) {
    AssertUtil.isNull(userId,"参数缺失");
    HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
    return sysUserService.userEntryQueueAndModify(userId, request);
  }

  /**
   * 　　* @Description: 启用/禁用 　　* @author: xueshiqi 　　* @date: 2018/11/13
   */
  @PostMapping(value = "updateUserStatus")
  public Map<String, Object> updateUserStatus(@RequestBody SysUserBean sysUserBean) {
    ValidateUtil.validate(sysUserBean,LockGroup.class);
    return sysUserService.updateUserStatus(sysUserBean);
  }

  /**
   * 重置密码
   */
  @SysLogAnno("重置密码")
  @RequiresPermissions("user:reset")
  @RequestMapping("/reset")
  public Map<String, Object> resetPswd(@RequestBody ResetPassWord resetPassWord) {
    ValidateUtil.validate(resetPassWord);
    return sysUserService.resetPswd(resetPassWord);
  }

  /**
   * 修改当前用户密码
   */
  @SysLogAnno("修改当前用户密码")
  @RequestMapping("/updatePswd")
  public Map<String, Object> updatePswd(@RequestBody UpdatePswd updatePswd) {
    ValidateUtil.validate(updatePswd);
    return sysUserService.updatePswd(updatePswd);
  }

  /**
   * 　　* @Description: 查看用户详情，回显时候调用 　　* @author: xueshiqi 　　* @date: 2018/12/10
   */
  @GetMapping(value = "/getSysUserDetail")
  public Map<String, Object> getSysUserDetail(@RequestParam(value = "userId") Integer userId){
    AssertUtil.isNull(userId,"参数缺失");
    return sysUserService.getSysUserDetail(userId);
  }
}
