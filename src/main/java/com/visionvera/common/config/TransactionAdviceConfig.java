package com.visionvera.common.config;

import javax.annotation.Resource;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 事务管理
 *
 * @author EricShen
 * @date 2018-12-03
 */
@Aspect
@Configuration
public class TransactionAdviceConfig {

  private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.visionvera.*.service.impl..*.*(..))";

  @Resource
  private PlatformTransactionManager transactionManager;

  @Bean
  public TransactionInterceptor txAdvice() {

    DefaultTransactionAttribute REQUIRED = new DefaultTransactionAttribute();
    REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

    DefaultTransactionAttribute REQUIRED_READONLY = new DefaultTransactionAttribute();
    REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    REQUIRED_READONLY.setReadOnly(true);

    NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

    // 对save,update,remove等开头的方法进行事务管理,只要有异常就回滚
    source.addTransactionalMethod("save*", REQUIRED);
    source.addTransactionalMethod("add*", REQUIRED);
    source.addTransactionalMethod("insert*", REQUIRED);
    source.addTransactionalMethod("remove*", REQUIRED);
    source.addTransactionalMethod("delete*", REQUIRED);
    source.addTransactionalMethod("update*", REQUIRED);
    source.addTransactionalMethod("edit*", REQUIRED);
    source.addTransactionalMethod("modify*", REQUIRED);
    source.addTransactionalMethod("change*", REQUIRED);
    source.addTransactionalMethod("set*", REQUIRED);
    source.addTransactionalMethod("batch*", REQUIRED);

    // select,find,count等开头的方法,开启只读,提高数据库访问性能
    // source.addTransactionalMethod("get*", REQUIRED_READONLY);
    // source.addTransactionalMethod("query*", REQUIRED_READONLY);
    // source.addTransactionalMethod("find*", REQUIRED_READONLY);
    // source.addTransactionalMethod("list*", REQUIRED_READONLY);
    // source.addTransactionalMethod("count*", REQUIRED_READONLY);
    // source.addTransactionalMethod("is*", REQUIRED_READONLY);

    return new TransactionInterceptor(transactionManager, source);
  }

  @Bean
  public Advisor txAdviceAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
    return new DefaultPointcutAdvisor(pointcut, txAdvice());
  }
}
