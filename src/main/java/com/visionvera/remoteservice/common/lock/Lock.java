package com.visionvera.remoteservice.common.lock;

import java.util.concurrent.atomic.AtomicBoolean;

public class Lock {

  /**
   * @Fields synDevice : 终端同步锁， true ： 同步中 false：未同步
   */
  public static AtomicBoolean synDevice = new AtomicBoolean(false);

}
