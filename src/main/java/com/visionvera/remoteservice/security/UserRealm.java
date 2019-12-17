package com.visionvera.remoteservice.security;

import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.SysRoleDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.service.SysUserService;
import com.visionvera.remoteservice.util.EncryptionUtil;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;


/**
 * 认证
 *
 * Created by EricShen on 2017/08/22
 */
public class UserRealm extends AuthorizingRealm {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  private SysUserService sysUserService;

  @Resource
  private SysUserDao sysUserDao;

  @Resource
  private SysRoleDao sysRoleDao;

  @Resource
  private SysDeptBeanDao sysDeptDao;

  @Resource
  private ServiceCenterDao serviceCenterDao;

  /**
   * 认证(登录时调用)
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(
      AuthenticationToken token) throws AuthenticationException {
    logger.info("Shiro开始登录认证");

    String loginName = (String) token.getPrincipal();//用户名
    String password = new String((char[]) token.getCredentials());//密码

    //查询用户信息
    SysUserBean user = sysUserService.getByUserName(loginName);

    //账号不存在
    if (user == null) {
      throw new UnknownAccountException("账号不存在");
    }

    //密码错误
    if (!EncryptionUtil.encrypt(password, user.getSalt()).equals(user.getPassword())) {
      throw new IncorrectCredentialsException();
    }

    //1可用 0 无效，-1不可用
    if (StateEnum.Disable.getValue().toString().equals(user.getUserState())) {
      throw new LockedAccountException("当前用户处于禁用状态，请联系管理员");
    }

    String serviceKey = user.getServiceKey();
    //非超管用户校验中心是否可用
    if(!user.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)){
      ServiceCenterBean serviceCenter = serviceCenterDao.getServiceCenterByServiceKey(serviceKey);
      if(StateEnum.Disable.getValue().toString().equals(serviceCenter.getState())){
        throw new LockedAccountException("当前用户所属中心处于禁用状态，请联系管理员");
      }
    }

    Integer deptId = user.getDeptId();
    SysDeptBean sysDeptBean = sysDeptDao.selectByPrimaryKey(deptId);
    //如果是所有部门，不进行此校验
    if(!deptId.equals(0)){
      if(StateEnum.Disable.getValue().toString().equals(sysDeptBean.getState())){
        throw new LockedAccountException("当前用户所属部门处于禁用状态，请联系管理员");
      }
    }

    if (!user.getType().equals(SysUserTypeEnum.WholeCenterSalesman.getValue()) && !(user.getUserId()
        .equals(CommonConstant.SUPER_ADMIN_ID)) && !user.getType().equals(SysUserTypeEnum.AuditCenterSalesman.getValue())) {
      Integer roleId = sysRoleDao.getUserRoleInfo(user.getUserId());
      if (ObjectUtils.isEmpty(roleId)) {
        throw new UnknownAccountException("当前用户无对应角色，请联系管理员分配角色");
      }
    }

    //登陆时候修改 登陆时间
    int loginTimeResult = sysUserDao.updateLastLoginTime(user.getUserId());
    if (loginTimeResult > 0) {
      logger.info("用户:" + user.getLoginName() + "修改登陆时间成功");
    }

    Map<String, Integer> workStatusMap = new HashMap<String, Integer>();
    //修改用户的在线/离线状态
    workStatusMap.put("state",Integer.valueOf(CommonConstant.USER_IN_LINE));
    workStatusMap.put("userId", user.getUserId());
    int updateState = sysUserDao.updateState(workStatusMap);
    if (updateState > 0) {
      logger.info("用户:" + user.getLoginName() + "修改用户状态成功");
    }

    workStatusMap.clear();
    //修改用户工作状态
    workStatusMap.put("workState", CommonConstant.WORK_STATE_LEISURE);
    workStatusMap.put("userId", user.getUserId());
    int workStatusResult = sysUserDao.updateWorkState(workStatusMap);
    if (workStatusResult > 0) {
      logger.info("用户:" + user.getLoginName() + "修改工作状态成功");
    }
    return new SimpleAuthenticationInfo(user, password, getName());
  }

  /**
   * 授权(验证权限时调用)
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    Integer userId = ShiroUtils.getUserId();
    //用户角色
//    Set<String> roleSet = sysUserService.getUserRoleSet(userId);
    //用户权限
    Set<String> permsSet = sysUserService.getUserPermSet(userId);

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//    info.setRoles(roleSet);
    info.setStringPermissions(permsSet);

    return info;
  }


}
