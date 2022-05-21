package com.naswork.starter.aop;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import com.naswork.starter.annotations.DataScope;
import com.naswork.starter.model.BaseModel;
import com.naswork.starter.model.QueryBaseModel;
import com.naswork.starter.service.AamRequestService;
import com.naswork.starter.service.BaseService;
import com.naswork.starter.utils.StrFormatterUtil;
import com.naswork.starter.vo.aam.GroupBasicInfoVO;
import com.naswork.starter.vo.aam.UserGroupsFullInfoVO;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 数据过滤处理
 * 在ServiceImpl方法上加@DataScope注解
 * @author yanggongming
 */
@Aspect
@Component
public class DataScopeAspect {
  /**
   * 全部数据权限
   */
  public static final String DATA_SCOPE_ALL = "1";

  /**
   * 自定数据权限
   */
  public static final String DATA_SCOPE_CUSTOM = "2";

  /**
   * 部门数据权限
   */
  public static final String DATA_SCOPE_DEPT = "3";

  /**
   * 部门及以下数据权限
   */
  public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

  /**
   * 仅本人数据权限
   */
  public static final String DATA_SCOPE_SELF = "5";

  /**
   * 监管数据权限
   */
  public static final String DATA_SCOPE_WATCH = "6";

  /**
   * 数据权限过滤关键字
   */
  public static final String DATA_SCOPE = "dataScope";


  @Autowired
  private AamRequestService aamRequestService;

  // 配置织入点
  @Pointcut("@annotation(com.naswork.starter.annotations.DataScope)")
  public void dataScopePointCut() {
  }

  @Before("dataScopePointCut()")
  public void doBefore(JoinPoint point) throws Throwable {
    handleDataScope(point);
  }

  protected void handleDataScope(final JoinPoint joinPoint) {
    // 获得注解
    DataScope controllerDataScope = getAnnotationLog(joinPoint);
    if (controllerDataScope == null) {
      return;
    }

    // 获取当前的用户
    UserGroupsFullInfoVO loginUser = aamRequestService.queryUserGroupsInfo(null);
    if (loginUser != null) {
      GroupBasicInfoVO currentGroup = loginUser.getCurrentGroup();

      // 如果没有父机构或者机构path只有1级，则数据不过滤
      if (currentGroup != null &&
          (currentGroup.getParentId() == null || currentGroup.getPath().lastIndexOf('/') == 0)){
        return;
      }
      dataScopeFilter(joinPoint, loginUser, controllerDataScope.groupAlias(),
          controllerDataScope.roleAlias());
    }
  }

  /**
   * 数据范围过滤
   *
   * @param joinPoint 切点
   * @param user      用户
   */
  public static void dataScopeFilter(
      JoinPoint joinPoint, UserGroupsFullInfoVO user, String groupAlias, String roleAlias) {
    StringBuilder sqlString = new StringBuilder();
    // 默认 部门及以下数据权限
    List<String> childGroupsIds = user.getChildGroupsIds();

    if (childGroupsIds != null && childGroupsIds.size() > 0){
//        String inSql = "'" + StringUtils.join(childGroupsIds, "','") + "'";
      sqlString.append(StrFormatterUtil.format(
          " OR {}group_id IN ( '{}' )",
          groupAlias,
          StringUtils.join(childGroupsIds, "','")));
    }

    if (StringUtils.isNotBlank(sqlString.toString())) {
      // 查询对象尽量为方法第一位
      Object[] params = joinPoint.getArgs();
      for (Object param : params) {
        if (ObjectUtils.isNotNull(param) && param instanceof QueryBaseModel) {
          QueryBaseModel baseModel = (QueryBaseModel) param;
          baseModel.setSql(" AND (" + sqlString.substring(4) + ")");
          break;
        }
      }
    }
  }

  /**
   * 是否存在注解，如果存在就获取
   */
  private DataScope getAnnotationLog(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();

    if (method != null) {
      return method.getAnnotation(DataScope.class);
    }
    return null;
  }
}
