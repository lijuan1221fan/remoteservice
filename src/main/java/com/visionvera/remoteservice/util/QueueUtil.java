package com.visionvera.remoteservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ClassName: QueueUtil
 *
 * @author quboka
 * @Description: 存储消息的队列。
 * @date 2018年3月16日
 */
public class QueueUtil {

  private static Logger logger = LoggerFactory.getLogger(QueueUtil.class);

  /**
   * @Fields queue : 客户端发送消息的队列
   */
//	private static LinkedBlockingQueue<String> queue ;


  /**
   * @Description: 往队列里面添加消息
   */
  public static void put(LinkedBlockingQueue<String> queue, String value) {
    try {
      queue.put(value);
      logger.info("往队列里添加消息成功,队列长度：" + queue.size());
    } catch (InterruptedException e) {
      logger.error("往队列里添加消息失败.", e);
    }
  }

  /**
   * @Description: 从队列中获取消息
   */
  public static String take(LinkedBlockingQueue<String> queue) {
    String take = null;
    try {
      take = queue.take();
    } catch (InterruptedException e) {
      logger.error("从队列中获取消息失败.", e);
    }
    return take;
  }

  /**
   * @Description: 获取队列大小
   */
  public static int size(LinkedBlockingQueue<String> queue) {
    return queue.size();
  }

  /**
   * @Description: 拿出所有消息
   */
  public static List<String> getAllMessage(LinkedBlockingQueue<String> queue) {
    List<String> list = new ArrayList<String>();
    if (queue.size() > 0) {
      //drainTo():一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数），通过该方法，可以提升获取数据效率；不需要多次分批加锁或释放锁。
      queue.drainTo(list);
    }
    return list;
  }


}
