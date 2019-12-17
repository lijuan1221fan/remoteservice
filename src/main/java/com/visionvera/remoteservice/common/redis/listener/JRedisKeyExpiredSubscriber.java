// package com.visionvera.remoteservice.common.redis.listener;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import redis.clients.jedis.Jedis;
// import redis.clients.jedis.JedisPool;
// import redis.clients.jedis.exceptions.JedisConnectionException;
//
// /**
//  * ClassName: JRedisKeyExpiredSubscriber
//  *
//  * @author quboka
//  * @Description: Redis key过期订阅者 使用该功能时，需要在redis.conf中配置，设置" notify-keyspace-events = EX "，
//  * 该功能在redis2.8版本之后开始支持
//  * @date 2018年4月8日
//  */
// public class JRedisKeyExpiredSubscriber implements Runnable {
//
//   /**
//    * redis的key过期事件通配符
//    */
//   private static final String KEY_EVENT_PATTENT = "__keyevent@*__:expired";
//   private static Logger logger = LoggerFactory.getLogger(JRedisKeyExpiredSubscriber.class);
//   /**
//    * Redis key失效监听是否处于工作中
//    */
//   public volatile boolean isJedisSubscriberStart = false;
//   /**
//    * @Fields jedisPoll : jedis链接池
//    */
//   private JedisPool jedisPool;
//   //构造方法注入
//   public JRedisKeyExpiredSubscriber(JedisPool jedisPool) {
//     this.jedisPool = jedisPool;
//   }
//
//   @Override
//   public void run() {
//     while (!isJedisSubscriberStart) {
//       try {
//         this.startJedisSubscriber();
//       } catch (JedisConnectionException e) {
//         isJedisSubscriberStart = false;
//         logger.error("Jedis 连接已异常断开！！！" + e.getMessage());
//       } catch (Exception e) {
//         isJedisSubscriberStart = false;
//         logger.error("Jedis Session失效订阅者监听异常！！！", e);
//       }
//       try {
//         Thread.sleep(5000);
//       } catch (InterruptedException e) {
//         e.printStackTrace();
//       }
//     }
//   }
//
//   /**
//    * @Description: 订阅
//    */
//   private void startJedisSubscriber() throws JedisConnectionException, Exception {
//     logger.info(Thread.currentThread().getName());
//     Jedis jedis = jedisPool.getResource();
//     //订阅一个或多个符合给定模式的频道
//     jedis.psubscribe(JRedisKeyExpiredPubSub.getInstance(), KEY_EVENT_PATTENT);
//     //设置Redis监听工作状态，true表示处于工作中
//     isJedisSubscriberStart = true;
//     logger.info("Jedis Session失效订阅者监听启动成功！！！");
//   }
//
// }
