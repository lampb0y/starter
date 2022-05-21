/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/

package com.naswork.starter.aop;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.naswork.starter.annotations.AuditLog;
import com.naswork.starter.utils.LogBuilder;
import com.naswork.starter.utils.LogLabelUtils;
import com.naswork.starter.vo.PrivacyType;

/**
 * log audit log around method.
 * 
 * @author elngjhx
 *
 */
@Aspect
@Component
@Order(1)
public class AuditLogAspect {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private HttpServletRequest request;
  
  private String subjectType = null;

  /**
   * Pointcut Expression Method.
   */
  @Pointcut("@annotation(com.naswork.starter.annotations.AuditLog)")
  public void pcMethod() {
    // Do nothing because of aop method.
  }

  private void genSubjectType(JoinPoint point) {
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    RequestMapping rm = ((RequestMapping) methodSignature.getDeclaringType().getAnnotation(
        RequestMapping.class));
    if (rm != null && rm.value() != null && rm.value().length > 0) {
      String value = rm.value()[0];
      String[] items = value.split("/");
      for (String item: items) {
        if (!item.equals("/") && !item.trim().isEmpty()) {
          subjectType = item;
          break;
        }
      }
    } 
    if (subjectType == null) {
      AuditLog auditLog = methodSignature.getMethod()
          .getAnnotation(AuditLog.class);
      subjectType = auditLog.subjectType();
    }
  }
  /**
   * log before specified method run.
   * 
   * @param point
   *          proxy object
   */
  @Before(value = "pcMethod()")
  public void before(JoinPoint point) {
    // get preferred_username from token
    String userName = null;
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      userName = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    this.genSubjectType(point);
    // set user name, it will be used as actor id in audit log
    LogLabelUtils.setUserName(userName);

    logger.info(LogBuilder.buildBeginAuditMarker(request, PrivacyType.OPEN, 
        subjectType), "{}", "");
  }

  /**
   * log after specified method return.
   * 
   * @param point
   *          proxy object
   */
  @AfterReturning(value = "pcMethod()")
  public void afterReturning(JoinPoint point) {
    logger.info(LogBuilder.buildSuccessAuditMarker(request, PrivacyType.OPEN, 
        subjectType), "{}", "");
  }

  /**
   * log after specified method throw exception.
   * 
   * @param point
   *          proxy object
   */
  @AfterThrowing(value = "pcMethod()", throwing = "ex")
  public void afterThrowing(JoinPoint point, Exception ex) {
    logger.error(LogBuilder.buildFailureAuditMarker(request, PrivacyType.OPEN,
        subjectType), "{}", "");
  }
}
