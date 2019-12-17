package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.UserBean;
import java.util.List;
import java.util.Map;

/***
 * 用户管理数据曾
 * @author wangruizhi
 *
 */
public interface UserDao {

  /***
   * 验证用户登陆
   * @param loginName 登陆名称
   * @param password 登陆密码
   * @return
   */
  UserBean checkUserLogin(Map<String, Object> paramsMap);

  /**
   * @param statusMap
   * @return int
   * @Description: 修改用户状态
   * @author quboka
   * @date 2018年3月22日
   */
  int updateState(Map<String, Integer> statusMap);

  /**
   * @param userId
   * @return int
   * @Description: 登陆时候修改 登陆时间
   * @author quboka
   * @date 2018年3月22日
   */
  int updateLastLoginTime(Integer userId);

  /**
   * @param workStatusMap
   * @return int
   * @Description: 修改用户工作状态
   * @author quboka
   * @date 2018年3月24日
   */
  int updateWorkState(Map<String, Integer> workStatusMap);

  /**
   * @param userId
   * @return UserBean
   * @Description: 根据id查询用户
   * @author quboka
   * @date 2018年3月26日
   */
  UserBean getUserById(Integer userId);

  /**
   * @param paramsMap
   * @return Integer
   * @Description: 按照名称校验用户
   * @author quboka
   * @date 2018年5月14日
   */
  Integer checkoutByName(Map<String, Object> paramsMap);

  /**
   * @param user
   * @return Integer
   * @Description: 添加用户
   * @author quboka
   * @date 2018年5月15日
   */
  Integer addUser(UserBean user);

  /**
   * @param user
   * @return Integer
   * @Description: 修改用户
   * @author quboka
   * @date 2018年5月15日
   */
  Integer updateUser(UserBean user);

  /**
   * @param ids
   * @return Integer
   * @Description: 删除用户
   * @author quboka
   * @date 2018年5月15日
   */
  Integer deleteUser(Integer[] ids);

  /**
   * @param paramsMap
   * @return List<UserBean>
   * @Description: 查询用户列表
   * @author quboka
   * @date 2018年5月15日
   */
  List<UserBean> getUserList(Map<String, Object> paramsMap);

  /**
   * @param serviceKeys
   * @return List<Integer>
   * @Description: 查询审批中心下得用户
   * @author quboka
   * @date 2018年7月4日
   */
  List<Integer> getUserIdsByServiceIds(List<String> serviceKeys);

  /**
   * @param [ids]
   * @return java.util.List<java.lang.Integer>
   * @description: 根据部门id 查询用户id
   * @author quboka
   * @date 2018/8/31 15:21
   */
  List<Integer> selectUserIdByDeptId(Integer[] ids);
}
