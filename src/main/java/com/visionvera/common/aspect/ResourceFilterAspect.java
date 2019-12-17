package com.visionvera.common.aspect;

import static com.visionvera.remoteservice.constant.CommonConstant.SQL_FILTER;
import static com.visionvera.remoteservice.constant.CommonConstant.SUPER_ADMIN;

import com.visionvera.common.annonation.ResourceFilter;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 资源过滤，切面处理类
 *
 * @author EricShen
 * @date 2018-11-14
 */
@Aspect
@Component
public class ResourceFilterAspect {


  @Resource
  private ServiceCenterDao serviceCenterDao;

  @Pointcut("@annotation(com.visionvera.common.annonation.ResourceFilter)")
  public void ResourceFilterCut() {

  }

  @Before("ResourceFilterCut()")
  public void ResourceFilter(JoinPoint point) throws Throwable {
    Object params = point.getArgs()[0];
    if (params != null && params instanceof Map) {
      SysUserBean userEntity = ShiroUtils.getUserEntity();

      //如果不是超级管理员，则进行数据过滤
      if (!userEntity.getUserId().equals(SUPER_ADMIN)) {
        Map map = (Map) params;
        map.put(SQL_FILTER, getSQLFilter(userEntity, point));
      }

      return;
    }

    throw new MyException("资源权限接口，只能是Map类型参数，且不能为NULL");
  }

  /**
   * 获取资源过滤的SQL
   */
  private String getSQLFilter(SysUserBean userEntity, JoinPoint point) {
    MethodSignature signature = (MethodSignature) point.getSignature();
    ResourceFilter resourceFilter = signature.getMethod().getAnnotation(ResourceFilter.class);
    //获取表的别名
    String tableAlias = resourceFilter.tableAlias();
    if (StringUtils.isNotBlank(tableAlias)) {
      tableAlias += ".";
    }

    //部门ID列表
    Set<String> centerIdList = new HashSet<>();

    //用户角色对应的中心ID列表
    // todo userId查询角色list
    List<Long> roleIdList = null;
    if (!CollectionUtils.isEmpty(roleIdList)) {
      // todo 角色id查询所在中心list
      List<Long> roleCenterIdList = null;
      roleCenterIdList.addAll(roleCenterIdList);
    }

    //用户下级中心ID列表
    if (resourceFilter.subCenter()) {
      List<String> subCenterIdList = null;
      ServiceCenterBean condition = new ServiceCenterBean();
      condition.setParentKey(userEntity.getServiceKey());
      List<ServiceCenterBean> centerList = serviceCenterDao.getServiceCenterByCondition(condition);
      for (ServiceCenterBean serviceCenter : centerList) {
        subCenterIdList.add(serviceCenter.getServiceKey());
      }

      centerIdList.addAll(subCenterIdList);
    }

    StringBuilder sqlFilter = new StringBuilder();
    sqlFilter.append(" (");

    if (centerIdList.size() > 0) {
      sqlFilter.append(tableAlias).append(resourceFilter.centerId()).append(" in(")
          .append(StringUtils.join(centerIdList, ",")).append(")");
    }

    //没有本中心资源权限，也能查询本人数据
    if (resourceFilter.user()) {
      if (centerIdList.size() > 0) {
        sqlFilter.append(" or ");
      }
      sqlFilter.append(tableAlias).append(resourceFilter.userId()).append("=")
          .append(userEntity.getUserId());
    }

    sqlFilter.append(")");

    return sqlFilter.toString();
  }
}