// package com.visionvera.remoteservice.service;
//
// import com.visionvera.remoteservice.bean.UserBean;
// import java.util.Map;
// import javax.servlet.http.HttpServletRequest;
//
// /***
//  * 用户管理业务层接口
//  * @author wangruizhi
//  *
//  */
// public interface UserService {
//
//   /***
//    * 验证用户登陆
//    * @param loginName 登陆名称
//    * @param password 登陆密码
//    * @return
//    */
//   Map<String, Object> checkUserLogin(Map<String, Object> paramsMap);
//
//   /**
//    * @param businessKey 业务key ,可以为空
//    * @param remarks 备注
//    * @param certificateIds 证件类型
//    * @param session
//    * @return Map<String   ,   Object>
//    * @Description: 退出
//    * @author quboka
//    * @date 2018年3月31日
//    */
//   Map<String, Object> logout(Map<String, Object> paramsMap);
//
//   /**
//    * @param userId
//    * @return Map<String   ,   Object>
//    * @Description: 空闲用户 进入队列
//    * @author quboka
//    * @date 2018年3月23日
//    */
//   Map<String, Object> userEntryQueueAndModify(Integer userId, HttpServletRequest request);
//
//   /**
//    * @param paramsMap
//    * @return Map<String   ,   Object>
//    * @Description: 添加用户
//    * @
//    * @author quboka
//    * @date 2018年5月14日
//    */
//   Map<String, Object> addUser(Map<String, Object> paramsMap);
//
//   /**
//    * @param user
//    * @param roleId 角色id
//    * @return Map<String   ,   Object>
//    * @Description: 修改用户
//    * @author quboka
//    * @date 2018年5月15日
//    */
//   Map<String, Object> updateUser(UserBean user, Integer roleId);
//
//   /**
//    * @param ids
//    * @return Map<String   ,   Object>
//    * @Description: 删除用户
//    * @author quboka
//    * @date 2018年5月15日
//    */
//   Map<String, Object> deleteUser(Integer[] ids);
//
//   /**
//    * @param paramsMap
//    * @return Map<String   ,   Object>
//    * @Description: 查询用户列表
//    * @author quboka
//    * @date 2018年5月15日
//    */
//   Map<String, Object> getUserList(Map<String, Object> paramsMap);
//
// }
