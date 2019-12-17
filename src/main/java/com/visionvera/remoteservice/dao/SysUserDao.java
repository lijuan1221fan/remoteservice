package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author xueshiqi
 * @Description: TODO
 * @date 2018/10/12
 */
public interface SysUserDao {

  /**
   * @Description: 获取用户列表
   * @author: xueshiqi
   * @date: 2018/10/12
   */
  List<SysUserBean> getSysUserList(Map paramsMap);

  /**
   * @Description: 删除用户
   * @author: xueshiqi
   * @date: 2018/10/15
   */
  int deleteSysUser(@Param("userIds") List<Integer> userIds);
  /**
   * @Description: 查询用户
   * @author: fanlijuan
   * @date: 2019/7/5
   */
  List<SysUserBean> getUserByIds(@Param("userIds") List<Integer> userIds);

  /**
   * @Description: 获取用户信息
   * @author: xueshiqi
   * @date: 2018/10/15
   */
  SysUserBean getSysUserInfo(Integer userId);

  /**
   * @Description: 根据用户名密码查询用户
   * @author: xueshiqi
   * @date: 2018/10/15
   */
  SysUserBean checkUserLogin(Map<String, Object> params);

  /**
   * @Description: 修改用户状态
   * @author: xueshiqi
   * @date: 2018/10/15
   */
  int updateState(Map<String, Integer> statusMap);

  /**
   * @Description: 修改登录时间
   * @author: xueshiqi
   * @date: 2018/10/15
   */
  int updateLastLoginTime(Integer userId);

  /**
   * @Description: 修改用户工作状态
   * @author: xueshiqi
   * @date: 2018/10/15
   */
  int updateWorkState(Map<String, Integer> workStatusMap);

  /**
   * 根据姓名查询用户
   *
   * @param loginName
   * @return
   */
  SysUserBean getByUserName(String loginName);

  /**
   * 添加用户
   *
   * @param sysUserBean
   * @return
   */
  int addUser(SysUserBean sysUserBean);

  /**
   * 编辑用户
   *
   * @param sysUserBean
   * @return
   */
  int editUser(SysUserBean sysUserBean);

  List<Long> getAllMenuId();

  List<Long> getMenuIdList(Integer userId);

  /**
   * @Description: 插入角色用户关联表
   * @author: xueshiqi
   * @date: 2018/11/13 0013
   */
  void addSysUserRoleRel(@Param("userId") Integer userId, @Param("roleId") int roleId);

  /**
   * @Description: 用户启用/禁用
   * @author: xueshiqi
   * @date: 2018/11/13
   */
  int updateUserState(SysUserBean sysUserBean);

  /**
   * 修改角色用户管理表
   *
   * @param userId
   * @param roleId
   */
  void updateSysUserRoleRel(@Param("userId") Integer userId, @Param("roleId") int roleId);

  /**
   * @Description: 重置密码
   * @author: xueshiqi
   * @date: 2018/11/14 0014
   */
  int resetPswd(SysUserBean sysUserBean);
  /**
   * @Description: 查询审批中心下得用户
   * @author: xueshiqi
   * @date: 2018/11/28
   */
  List<Integer> getUserIdsByServiceIds(@Param("serviceKeys") List<String> serviceKeys);

  /**
   * @param [ids]
   * @return java.util.List<java.lang.Integer>
   * @description: 根据部门id 查询用户id
   * @author quboka
   * @date 2018/8/31 15:21
   */
  List<Integer> selectUserIdByDeptId(Integer[] ids);

  /**
  　　* @Description: 查看用户详情，回显时候调用
  　　* @author: xueshiqi
  　　* @date: 2018/12/10
  　　*/
  SysUserBean getSysUserDetail(Integer userId);

  /**
　　* @Description: 获取登录用户信息
　　* @author: xueshiqi
　　* @date: 2018/12/12
　　*/
  SysUserBean getLoginUserInfo(Integer userId);

  /**
   * 　　* @Description: 编辑时验证用户是否存在 　　* @author: xueshiqi 　　* @date: 2018/12/21
   */
  SysUserBean getSysUserByLoginNameAndUserId(String loginName);
  /**
　　* @Description: 业务员在线办理人数限定
　　* @author: fanlijuan
　　* @date: 2019/02/14
　　*/
  List<SysUserBean> getUserListByServiceKey ();
  /**
   * 根据部门id 查询在线处理中业务人
   */
  List<SysUserBean> getUserByParam(SysUserBean bean);

  /**
   * 查询在线等待业务员，并且该业务员没有被推送业务
   * @param bean
   * @return
   */
  List<SysUserBean> getUserByParamAndNoBusinessInfo(SysUserBean bean);  /**
   * 查询在线等待业务员，并且该业务员没有被推送业务
   * @param bean
   * @return
   */
  List<SysUserBean> getUserByParamAndBusinessInfo(SysUserBean bean);
  /**
   * 将业务员状态更新为离线并空闲中
   */
  void updateSysUserState();

  /**
   * 修改角色用户管理表
   *
   * @param userId
   * @param roleId
   */
  void updateSysUserStateByWebSocket(@Param("userId") Integer userId,@Param("state") Integer state);


}
