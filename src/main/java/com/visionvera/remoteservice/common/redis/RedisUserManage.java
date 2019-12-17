// package com.visionvera.remoteservice.common.redis;
//
//
// import com.visionvera.remoteservice.bean.UserBean;
// import com.visionvera.remoteservice.util.StringUtil;
// import javax.annotation.Resource;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;
//
// /**
//  * ClassName: RedisUserManage
//  *
//  * @author quboka
//  * @Description: redis的用户管理类
//  * @date 2018年4月8日
//  */
// @Component
// public class RedisUserManage {
//
//   /**
//    * @Fields REDIS_MAXAGE : redis 存储 有效期
//    */
//   public static final Integer REDIS_MAXAGE = 60 * 60 * 3;
//   /**
//    * @Fields REMOTE_PREX : redis中存放用户key的前缀
//    */
//   public static final String REMOTE_PREX = "REMOTE_";
//   /**
//    * @Fields USER_HASH_KEY :  redis中用户的hash类型key
//    * @describe userKey = REMOTE_PREX + uuid
//    * @describe {userKey : userId}  {userId : userKey}
//    */
//   public static final String USER_HASH_KEY = "REMOTE_USER";
//   /**
//    * @Fields USER_COOKIENAME : 用户cookied的名称
//    */
//   public static final String USER_COOKIENAME = "REMOTE_USER_COOK";
//   private static Logger logger = LoggerFactory.getLogger(RedisUserManage.class);
//   @Resource
//   private JedisClientPool jedisClientPool;
//
//   /**
//    * @param userHashKey redis中存放用户的hash类型key
//    * @param userKey 用户唯一标识（前缀+uuid）
//    * @param user
//    * @param expiry
//    * @return boolean
//    * @Description: redis存储用户信息
//    * @author quboka
//    * @date 2018年4月8日
//    */
//   public boolean setUser(String userHashKey, String userKey, UserBean user, int expiry) {
//     boolean result = false;
//     try {
//
//       //清除旧的userKey，保证登录用户的唯一性
//       String oldUserKey = jedisClientPool.hget(userHashKey, String.valueOf(user.getUserId()));
//       removeUser(userHashKey, oldUserKey);
//
//       //清除旧数据
//       if (jedisClientPool.exists(userKey)) {
//         jedisClientPool.del(userKey);
//       }
//       //添加新数据   userKey ： user
//       jedisClientPool.set(userKey, user);
//       //设置时间
//       jedisClientPool.expire(userKey, expiry);
//
//       //userHashKey：{userKey ： userId}
//       if (jedisClientPool.hexists(userHashKey, userKey)) {
//         jedisClientPool.hdel(userHashKey, userKey);
//       }
//       jedisClientPool.hset(userHashKey, userKey, String.valueOf(user.getUserId()));
//
//       //userHashKey：{userId ：userKey }
//       if (jedisClientPool.hexists(userHashKey, String.valueOf(user.getUserId()))) {
//         jedisClientPool.hdel(userHashKey, String.valueOf(user.getUserId()));
//       }
//       jedisClientPool.hset(userHashKey, String.valueOf(user.getUserId()), userKey);
//
//       result = true;
//     } catch (Exception e) {
//       logger.error("添加用户到redis中失败", e);
//     }
//     return result;
//
//   }
//
//   /**
//    * @param userHashKey redis中存放用户的hash类型key
//    * @param userKey 用户唯一标识（前缀+uuid）
//    * @return boolean
//    * @Description: 删除redis中用户信息
//    * @author quboka
//    * @date 2018年4月8日
//    */
//   public boolean removeUser(String userHashKey, String userKey) {
//     boolean result = false;
//     if (StringUtil.isNull(userKey)) {
//       return result;
//     }
//     try {
//       String userId = null;
//       //删除
//       if (jedisClientPool.exists(userKey)) {
//         jedisClientPool.del(userKey);//清除数据
//       }
//       //删除
//       if (jedisClientPool.hexists(userHashKey, userKey)) {
//         userId = jedisClientPool.hget(userHashKey, userKey);
//         jedisClientPool.hdel(userHashKey, userKey);//清除数据
//       }
//       //删除
//       if (StringUtil.isNotNull(userId) && jedisClientPool.hexists(userHashKey, userId)) {
//         jedisClientPool.hdel(userHashKey, userId);
//       }
//       result = true;
//       logger.info("删除用户redis:[" + userKey + "]成功");
//     } catch (Exception e) {
//       logger.error("删除用户redis失败", e);
//     }
//     return result;
//   }
//
//
//   /**
//    * @param userKey 用户唯一标识（前缀+uuid）
//    * @return UserBean
//    * @Description: 根据userKey在redis中获取用户
//    * @author quboka
//    * @date 2018年4月8日
//    */
//   public UserBean getUserInfo(String userKey) {
//     UserBean result = null;
//     try {
//       result = (UserBean) jedisClientPool.get(userKey);
//     } catch (Exception e) {
//       logger.error("根据userKey获取user失败", e);
//     }
//     return result;
//   }
//
//
//   /**
//    * @param key
//    * @param expire
//    * @return boolean
//    * @Description: redis中的key重生时间
//    * @author quboka
//    * @date 2018年4月8日
//    */
//   public boolean setKeyExpiresTime(String key, int expire) {
//     boolean result = false;
//     try {
//       if (jedisClientPool.exists(key)) {
//         long count = jedisClientPool.expire(key, expire);
//         result = count > 0;
//         result = count > 0;
//       }
//     } catch (Exception e) {
//       logger.error("为redis：[" + key + "]续期失败", e);
//     }
//     return result;
//   }
//
//   public Object get(String key) {
//     if (key == null) {
//       return null;
//     }
//     Object obj = jedisClientPool.get(key);
//     return obj;
//   }
//
// //	public String getUserId(String sessionFlag, String sessionID) {
// //		return jedisClientPool.hget(sessionFlag, sessionID);
// //	}
//
//
// }
