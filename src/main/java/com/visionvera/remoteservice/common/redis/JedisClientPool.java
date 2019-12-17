// package com.visionvera.remoteservice.common.redis;
//
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import javax.annotation.Resource;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;
// import redis.clients.jedis.Jedis;
// import redis.clients.jedis.JedisPool;
//
// /**
//  * ClassName: JedisClientPool
//  *
//  * @author quboka
//  * @Description: redis链接
//  * @date 2018年3月30日
//  */
// @Component
// public class JedisClientPool implements JedisClient {
//
//   private final Logger logger = LoggerFactory.getLogger(JedisClientPool.class);
//
//   @Resource
//   private JedisPool jedisPool;
//
//   public JedisPool getJedisPool() {
//     return jedisPool;
//   }
//
//   public void setJedisPool(JedisPool jedisPool) {
//     this.jedisPool = jedisPool;
//   }
//
//   @Override
//   public Long incr(String key) {
//     Jedis jedis = jedisPool.getResource();
//     Long result = jedis.incr(key);
//     jedis.close();
//     return result;
//   }
//
//   @Override
//   public Long expire(String key, int seconds) {
//     Jedis jedis = jedisPool.getResource();
//     Long result = jedis.expire(key, seconds);
//     jedis.close();
//     return result;
//   }
//
//   @Override
//   public List<String> hvals(String key) {
//     Jedis jedis = jedisPool.getResource();
//     List<String> result = jedis.hvals(key);
//     jedis.close();
//     return result;
//   }
//
//   @Override
//   public String setString(String key, String value) {
//     Jedis jedis = null;
//     String result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.set(key, value);
//     } catch (Exception e) {
//       logger.error("存储数据失败", e);
//     } finally {
//       // 返还到连接池、
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public String getString(String key) {
//     Jedis jedis = null;
//     String result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.get(key);
//     } catch (Exception e) {
//       logger.error("获取数据失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Long del(String key) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.del(key);
//     } catch (Exception e) {
//       logger.error("删除数据失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Boolean exists(String key) {
//     Jedis jedis = null;
//     Boolean result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.exists(key);
//     } catch (Exception e) {
//       logger.error("查询key失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//
//   @Override
//   public String set(String key, Object object) {
//     Jedis jedis = null;
//     String result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.set(key.getBytes(), SerializerUtils.serialize(object));
//     } catch (Exception e) {
//       logger.error("存储object对象失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Object get(String key) {
//     Object object = null;
//     Jedis jedis = null;
//     try {
//       jedis = jedisPool.getResource();
//       byte[] in = jedis.get(key.getBytes());
//       object = (Object) SerializerUtils.deserialize(in);
//     } catch (Exception e) {
//       logger.error("获取redis中的object对象失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return object;
//   }
//
//   @Override
//   public Long ttl(String key) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.ttl(key);
//     } catch (Exception e) {
//       logger.error("查看剩余时间失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Long hset(String key, String field, String value) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.hset(key, field, value);
//     } catch (Exception e) {
//       logger.error("添加hash数据失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public String hget(String key, String field) {
//
//     Jedis jedis = null;
//     String result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.hget(key, field);
//     } catch (Exception e) {
//       logger.error("获取hash数据失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Map<String, String> hgetAll(String key) {
//     Jedis jedis = null;
//     Map<String, String> result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.hgetAll(key);
//     } catch (Exception e) {
//       logger.error("获取hash数据失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Long hdel(String key, String... field) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.hdel(key, field);
//     } catch (Exception e) {
//       logger.error("删除hash数据失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   //判断hash中是否存在字段
//   @Override
//   public Boolean hexists(String key, String field) {
//     Jedis jedis = null;
//     Boolean result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.hexists(key, field);
//     } catch (Exception e) {
//       logger.error("判断hash中是否存在字段失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   //取出hash中所有的key
//   @Override
//   public Set<String> gethkeys(String key) {
//     Jedis jedis = null;
//     Set<String> result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.hkeys(key);
//     } catch (Exception e) {
//       logger.error("取出hash中所有的key失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   @Override
//   public Set<String> getkeys(String key) {
//     Jedis jedis = null;
//     Set<String> result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.keys(key);
//     } catch (Exception e) {
//       logger.error("取出hash中所有的key失败", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   /**
//    * @param key
//    * @param strings 多个参数
//    * @return Long
//    * @Description: 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   @Override
//   public Long rpush(final String key, final String... strings) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.rpush(key, strings);
//     } catch (Exception e) {
//       logger.error("队列:" + key + ",添加错误", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   /**
//    * @param key
//    * @return String
//    * @Description: 移除并返回列表 key 的头元素。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   @Override
//   public String lpop(final String key) {
//     Jedis jedis = null;
//     String result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.lpop(key);
//     } catch (Exception e) {
//       logger.error("队列:" + key + ",获取错误", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//
//   /**
//    * @param timeout 超时参数 timeout 接受一个以秒为单位的数字作为值,超时参数设为 0 表示阻塞时间可以无限期延长(block indefinitely)
//    * @param key 如果列表为空，返回一个 nil ([])。否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值
//    * @return String
//    * @Description: 移除并返回列表 key 的头元素。如果所有给定 key 都不存在或包含空列表，那么 BLPOP 命令将阻塞连接，直到等待超时
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   @Override
//   public String blpop(int timeout, String key) {
//     Jedis jedis = null;
//     String result = null;
//     try {
//       jedis = jedisPool.getResource();
//       List<String> list = jedis.blpop(timeout, key);
//       if (list != null && list.size() > 1) {
//         result = list.get(1);
//       }
//     } catch (Exception e) {
//       logger.error("队列:" + key + ",获取错误", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//   /**
//    * @param key
//    * @return Long
//    * @Description: 返回列表 key 的长度  ,如果 key 不存在，则 key 被解释为一个空列表，返回 0 .如果 key 不是列表类型，返回一个错误。
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   @Override
//   public Long llen(final String key) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.llen(key);
//     } catch (Exception e) {
//       logger.error("队列:" + key + ",获取长度错误", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
//
//
//   /**
//    * @param key
//    * @return Long
//    * @Description: 查看列表 key 中所有元素
//    * @author quboka
//    * @date 2018年3月30日
//    */
//   @Override
//   public List<String> lrange(final String key) {
//     Jedis jedis = null;
//     List<String> result = null;
//     try {
//       jedis = jedisPool.getResource();
//       // lrange(final String key, final long start, final long end)返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
//       //下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
//       //也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
//       //***超出范围的下标值不会引起错误。
//       //如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
//       //如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
//       result = jedis.lrange(key, 0, jedis.llen(key) - 1);
//     } catch (Exception e) {
//       logger.error("查看列表 " + key + "错误", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
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
//   @Override
//   public Long lrem(final String key, final long count, final String value) {
//     Jedis jedis = null;
//     Long result = null;
//     try {
//       jedis = jedisPool.getResource();
//       result = jedis.lrem(key, count, value);
//     } catch (Exception e) {
//       logger.error("移除列表 " + key + "中的元素错误", e);
//     } finally {
//       // 返还到连接池
//       if (null != jedis) {
//         jedis.close();
//       }
//     }
//     return result;
//   }
// }
