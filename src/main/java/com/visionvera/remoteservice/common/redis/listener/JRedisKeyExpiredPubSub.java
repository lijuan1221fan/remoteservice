// package com.visionvera.remoteservice.common.redis.listener;
//
// import com.visionvera.remoteservice.common.redis.RedisUserManage;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.ApplicationContext;
// import org.springframework.web.context.ContextLoader;
// import redis.clients.jedis.JedisPubSub;
//
// /**
//  * ClassName: JRedisKeyExpiredPubSub
//  *
//  * @author quboka
//  * @Description: Redis Key过期发布者 使用该功能时，需要在redis.conf中配置，设置" notify-keyspace-events = EX "，
//  * 该功能在redis2.8版本之后开始支持
//  * @date 2018年4月8日
//  */
// public class JRedisKeyExpiredPubSub extends JedisPubSub {
//
//   private static Logger logger = LoggerFactory.getLogger(JRedisKeyExpiredPubSub.class);
//
//   //饿汉式
//   private static JRedisKeyExpiredPubSub instance = new JRedisKeyExpiredPubSub();
//   //加载spring容器
//   private static volatile ApplicationContext context = null;
//
//   private JRedisKeyExpiredPubSub() {
//   }
//
//   public static JRedisKeyExpiredPubSub getInstance() {
//     return instance;
//   }
//
//   private static <T> T getBean(String beanName, Class<T> clazz) {
//     // 考虑懒汉式单例多线程不安全问题。用volatile 和 synchronized 双重锁
//     if (context == null) {
//       synchronized (JRedisKeyExpiredPubSub.class) {
//         // 获取上下文,而不再次加载配置文件
//         if (context == null) {
//           context = ContextLoader.getCurrentWebApplicationContext();
//         }
//       }
//     }
//     return context.getBean(beanName, clazz);
//   }
//
//   /**
//    * 初始化按表达式的方式订阅时候的处理
//    */
//   @Override
//   public void onPSubscribe(String pattern, int subscribedChannels) {
//     logger.info("onPSubscribe ---- pattern ----- " + pattern + " ---- subscribedChannels ----- "
//         + subscribedChannels);
//   }
//
//   /**
//    * 取得按表达式的方式订阅的消息后的处理
//    */
//   @Override
//   public void onPMessage(String pattern, String channel, String message) {
//     logger.info("onPMessage ---- pattern ---- " + pattern + " -----  channel ---- " + channel
//         + " ---- message ---- " + message);
//
//     //如果key即不是以规定开头的 ,那么，则不再进行下面的操作，因为没有意义
//     if (message == null || !message.startsWith(RedisUserManage.REMOTE_PREX)) {
//       return;
//     }
//
//     //注入redis用户管理对象
//     RedisUserManage redisUserManage = getBean("redisUserManage", RedisUserManage.class);
//     // 删除失效的用户
//     redisUserManage.removeUser(RedisUserManage.USER_HASH_KEY, message);
//   }
// }
