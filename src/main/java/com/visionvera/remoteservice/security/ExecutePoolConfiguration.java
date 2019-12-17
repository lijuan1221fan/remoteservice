package com.visionvera.remoteservice.security;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author ljfan
 * @ClassNameExecutePoolConfiguration 线程池配置
 * @date 2018/12/13 version
 */
@Configuration
public class ExecutePoolConfiguration {

  @Value("${sendSms.core-pool-size}")
  private int corePoolSize;
  @Value("${sendSms.max-pool-size}")
  private int maxPoolSize;
  @Value("${sendSms.queue-capacity}")
  private int queueCapacity;
  @Value("${sendSms.keep-alive-seconds}")
  private int keepAliveSeconds;
  @Bean(name = "taskExecutorPool")
  public ScheduledThreadPoolExecutor threadPoolTaskExecutor() {
    ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(corePoolSize,
            new BasicThreadFactory.Builder().namingPattern("schedule-pool-sms").daemon(true).build());
    return pool;
  }
}
