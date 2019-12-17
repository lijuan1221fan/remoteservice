// package com.visionvera.remoteservice.common.redis;
//
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
//
// /**
//  * ClassName: JedisClient
//  *
//  * @author quboka
//  * @Description: redis链接
//  * @date 2018年3月30日
//  */
// public interface JedisClient {
//
//   String set(String key, Object object);
//
//   Object get(String key);
//
//   String setString(String key, String value);
//
//   String getString(String key);
//
//   Boolean exists(String key);
//
//   Long expire(String key, int seconds);
//
//   Long ttl(String key);
//
//   Long incr(String key);
//
//   Long hset(String key, String field, String value);
//
//   String hget(String key, String field);
//
//   Long hdel(String key, String... field);
//
//   Boolean hexists(String key, String field);
//
//   List<String> hvals(String key);
//
//   Map<String, String> hgetAll(String key);
//
//   Long del(String key);
//
//   Set<String> gethkeys(String key);
//
//   Set<String> getkeys(String key);
//
//
//   /**
//    * @param key
//    * @param strings 多个参数
//    * @return Long
//    * @Description: 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   Long rpush(final String key, final String... strings);
//
//   /**
//    * @param key
//    * @return String
//    * @Description: 移除并返回列表 key 的头元素。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   String lpop(final String key);
//
//   /**
//    * @param timeout 超时参数 timeout 接受一个以秒为单位的数字作为值,超时参数设为 0 表示阻塞时间可以无限期延长(block indefinitely)
//    * @param key 如果列表为空，返回一个 nil ([])。否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值
//    * @return String
//    * @Description: 移除并返回列表 key 的头元素。如果所有给定 key 都不存在或包含空列表，那么 BLPOP 命令将阻塞连接，直到等待超时
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   String blpop(int timeout, String key);
//
//   /**
//    * @param key
//    * @return Long
//    * @Description: 返回列表 key 的长度  ,如果 key 不存在，则 key 被解释为一个空列表，返回 0 .如果 key 不是列表类型，返回一个错误。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   Long llen(final String key);
//
//   /**
//    * @param key
//    * @return Long
//    * @Description: 查看列表 key 中所有元素
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   List<String> lrange(final String key);
//
//   /**
//    * @param key
//    * @param count
//    * @param value
//    * @return Long
//    * @Description: 根据参数 count 的值，移除列表中与参数 value 相等的元素。 count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为
//    * count 。 count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。 count = 0 : 移除表中所有与 value 相等的值。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   Long lrem(final String key, final long count, final String value);
// }
