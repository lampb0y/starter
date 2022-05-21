package com.naswork.starter.annotations;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
  /**
   * 组的别名 用于连表（例如：t1.）
   */
  public String groupAlias() default "";

  /**
   * 角色的别名
   */
  public String roleAlias() default "";
}
