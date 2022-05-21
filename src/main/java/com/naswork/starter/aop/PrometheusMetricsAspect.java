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


import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Sample;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
import org.springframework.stereotype.Component;

import com.naswork.starter.annotations.PrometheusMetrics;
import com.naswork.starter.exception.BaseException;
import com.naswork.starter.exception.UnauthorizedException;
import com.naswork.starter.utils.ThreadLocalUtil;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.MetricsEnum;

/**
 * Statistical information(access time,wrong times,success times) sent to Prometheus.
 *
 * @author ezborye
 */
@Aspect
@Component
@Order(2)
public class PrometheusMetricsAspect {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private HttpServletRequest request;

  public static final String AOP_TIMER = "timer";

  public static final String AOP_TIME_RECORD = "record";

  public static final String AOP_TAG_NAME = "tagName";

  public static final String NULL_REQUEST_MSG = "get request is null";

  public static final String DEFAULT_TAG_KEY = "services";

  /**
   * Pointcut Expression Method.
   */
  @Pointcut("@annotation(com.naswork.starter.annotations.PrometheusMetrics)")
  public void pcMethod() {
    // Do nothing because of aop method.
  }

  /**
   * prepare request name and timer for the other method in this PrometheusMetricsAspect.
   * 
   * @param point
   *          proxy object
   */
  @Before(value = "pcMethod()")
  public void before(JoinPoint point) {
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    PrometheusMetrics prometheusMetrics = methodSignature.getMethod()
        .getAnnotation(PrometheusMetrics.class);
    Map<String, Object> map = new HashMap<>();
    String name = getRequestName(prometheusMetrics);
    map.put(AOP_TAG_NAME, name);
    Timer timer = Metrics.timer(MetricsEnum.METHOD_COST_TIME.getName(),
        MetricsEnum.METHOD_COST_TIME.getTag(), name);
    Sample record = Timer.start();
    map.put(AOP_TIMER, timer);
    map.put(AOP_TIME_RECORD, record);
    ThreadLocalUtil.set(map);
  }

  /**
   * send success times and time spend in method to Prometheus.
   * 
   * @param point
   *          proxy object
   */
  @AfterReturning(value = "pcMethod()")
  public void afterReturning(JoinPoint point) {
    String name = ThreadLocalUtil.get().get(AOP_TAG_NAME).toString();
    Metrics.counter(MetricsEnum.USER_COUNTER_SUCCESS.getName(),
        MetricsEnum.USER_COUNTER_SUCCESS.getTag(), name).increment();
    stopTimeRecord();
  }

  /**
   * send wrong times and time spend in method to Prometheus.
   * 
   * @param point
   *          proxy object
   * @param ex
   *          exception from the monitored method.
   */
  @AfterThrowing(value = "pcMethod()", throwing = "ex")
  public void afterThrowing(JoinPoint point, Exception ex) {
    String name = ThreadLocalUtil.get().get(AOP_TAG_NAME).toString();
    Metrics.counter(MetricsEnum.USER_COUNTER_ERROR.getName(),
        MetricsEnum.USER_COUNTER_ERROR.getTag(), name).increment();
    counterErrorDetailed(ex);
    stopTimeRecord();
  }

  /**
   * Count every error message method.
   *
   * @param ex
   *          error object
   *          <li>Get error code.</li>
   * @author ezborye
   */
  private void counterErrorDetailed(Exception ex) {
    if (ex instanceof BaseException) {
      BaseException errorInfo = (BaseException) ex;
      Metrics.counter(MetricsEnum.INTERNAL_ERROR_CAPTURING.getName(),
          MetricsEnum.INTERNAL_ERROR_CAPTURING.getTag(), errorInfo.getCode()).increment();
    } else {
      Metrics
          .counter(MetricsEnum.INTERNAL_ERROR_CAPTURING.getName(),
              MetricsEnum.INTERNAL_ERROR_CAPTURING.getTag(), ErrorEnum.UNKNOWN_ERROR.code())
          .increment();
    }
  }

  /**
   * Get service name method.
   * <li>If parameter name is NULL,the request name is http request url</li>
   *
   * @param prometheusMetrics
   *          annotation Object
   * @return Prometheus tag name
   */
  private String getRequestName(PrometheusMetrics prometheusMetrics) {
    if (StringUtils.isNotEmpty(prometheusMetrics.value())) {
      return prometheusMetrics.value();
    } else {
      if (request != null) {
        return request.getRequestURI();
      } else {
        throw new UnauthorizedException(
            ErrorEnum.UNAUTH_REQUEST.append(NULL_REQUEST_MSG));
      }
    }
  }

  /**
   * record time spend in method and remove local thread.
   */
  private void stopTimeRecord() {
    Timer timer = (Timer) ThreadLocalUtil.get().get(AOP_TIMER);
    Sample record = (Sample) ThreadLocalUtil.get().get(AOP_TIME_RECORD);
    String name = ThreadLocalUtil.get().get(AOP_TAG_NAME).toString();
    record.stop(timer);
    logger.info(
        "take {} seconds to access request {}.", timer.totalTime(TimeUnit.SECONDS), name);
    ThreadLocalUtil.remove();
  }

}
