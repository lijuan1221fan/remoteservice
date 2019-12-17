package com.visionvera.common.annonation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源过滤
 *
 * @author EricShen
 * @date 2018-11-14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceFilter {

  /** 表的别名 */
  String tableAlias() default "";

  /** true：没有本中心权限，也能查询本人数据 */
  boolean user() default true;

  /** true：拥有下级中心权限 */
  boolean subCenter() default false;

  /** 中心ID */
  String centerId() default "centerId";

  /** 用户ID */
  String userId() default "userId";
}
