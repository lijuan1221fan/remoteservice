package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.ResetPassWord;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.UpdatePswd;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xueshiqi
 * @Description: 用户service
 * @date 2018/10/12
 */
public interface SysUserService {

  /**
   * 　　* @Description: 获取用户列表 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  Map<String, Object> getSysUserList(SysUserBean sysUserBean);

  /**
   * 　　* @Description: 删除用户 　　* @author: xueshiqi 　　* @date: 2018/10/15
   */
  Map<String, Object> deleteSysUser(List<Integer> userIdList);

  /**
   * 　　* @Description: 登出 　　* @author: xueshiqi 　　* @date: 2018/10/30
   */
  Map<String, Object> logout(HttpServletRequest request);


  /**
   * 当前登录用户权限
   *
   * @param userId
   * @return
   */
  Set<String> getUserPermSet(Integer userId);

  /**
   * 根据姓名查询用户
   *
   * @param loginName
   * @return
   */
  SysUserBean getByUserName(String loginName);

  /**
   * 获取用户角色
   *
   * @param userId
   * @return
   */
  Set<String> getUserRoleSet(Integer userId);

  /**
   * 添加用户
   *
   * @param sysUserBean
   * @return
   */
  Map<String, Object> addUser(SysUserBean sysUserBean);

  /**
   * 编辑用户
   *
   * @param sysUserBean
   * @return
   */
  Map<String, Object> editUser(SysUserBean sysUserBean);

  /**
   * 查询当前登录用户所属行政机构
   *
   * @return
   */
  Map<String, Object> getRegionInfoByUser();

  /**
   * 空闲用户 进入队列 (开始受理业务)
   *
   * @param userId
   * @return
   */
  Map<String, Object> userEntryQueueAndModify(Integer userId, HttpServletRequest request);

  /**
   * 　　* @Description: 启用/禁用 　　* @author: xueshiqi 　　* @date: 2018/11/13
   */
  Map<String, Object> updateUserStatus(SysUserBean sysUserBean);

  /**
   * 　　* @Description: 重置密码 　　* @author: xueshiqi 　　* @date: 2018/11/14
   */
  Map<String, Object> resetPswd(ResetPassWord resetPassWord);

  /**
   * 　　* @Description: 修改当前用户密码 　　* @author: xueshiqi 　　* @date: 2018/11/14
   */
  Map<String, Object> updatePswd(UpdatePswd updatePswd);

  /**
   * 　　* @Description: 查看用户详情，回显时候调用 　　* @author: xueshiqi 　　* @date: 2018/12/10
   */
  Map<String, Object> getSysUserDetail(Integer userId);

  /**
   * 　　* @Description: 获取登录用户信息 　　* @author: xueshiqi 　　* @date: 2018/12/12
   */
  Map<String, Object> getLoginUserInfo(Integer userId);

  /**
   * 业务员在线办理业务人数限定
   *
   * @return 在线办理人数
   */
  boolean limitOnLine();
}
