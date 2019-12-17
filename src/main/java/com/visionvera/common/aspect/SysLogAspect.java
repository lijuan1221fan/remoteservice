package com.visionvera.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.dao.SysLogDao;
import com.visionvera.common.entity.SysLog;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


/**
 * @Description: 系统日志切面处理类
 * @Date: 2018.10.11
 * @Autho: JLM
 */
@Aspect
@Component
public class SysLogAspect {

  @Resource
  private SysLogDao sysLogDao;

  @Pointcut("@annotation(com.visionvera.common.annonation.SysLogAnno)")
  public void logPointCut() {

  }

  @Around("logPointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    long beginTime = System.currentTimeMillis();
    //执行方法
    Object result = point.proceed();
    //执行时长(毫秒)
    long time = System.currentTimeMillis() - beginTime;
    String message = JSONObject.toJSONString(result);
    JSONObject jsonObject = JSONObject.parseObject(message);
    if (jsonObject.getString("result").equals("true")) {
      //保存日志
      saveSysLog(point, time);
    }
    return result;
  }

  /**
   * @param joinPoint
   * @param time
   * @Description:保存日志
   * @Date: 2018/10/30
   * @return: void
   */
  private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    SysLog sysLog = new SysLog();
    SysLogAnno sysLogAnno = method.getAnnotation(SysLogAnno.class);
    if (sysLogAnno != null) {
      //注解上的描述
      sysLog.setOperation(sysLogAnno.value());
    }
    //请求的方法名
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = signature.getName();
    sysLog.setMethod(className + "." + methodName + "()");
    if (ShiroUtils.isLogin()) {
      //用户名
      SysUserBean user = ShiroUtils.getUserEntity();
      sysLog.setUserId(Long.valueOf(user.getUserId()));
      sysLog.setUsername(user.getLoginName());
      sysLog.setTime(time);
      sysLog.setcTime(new Timestamp(System.currentTimeMillis()));
      //保存系统日志
      sysLogDao.insertLog(sysLog);
    }
  }

  /**
   * @Description: 查询符合条件所有日志用于导出文件
   * @Date: 2018/10/11
   * @param: * @param null
   * @return:
   */
  public List<SysLog> queryAllSysLog(Map paramMap) {

    return sysLogDao.queryLog(paramMap);
  }


}
