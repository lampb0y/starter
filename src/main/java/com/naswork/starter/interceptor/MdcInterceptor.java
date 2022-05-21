/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/

package com.naswork.starter.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.naswork.starter.utils.MdcUtils;

/**
 * set and remove trace id.
 */
/**
 * Mdc的拦截器，用于加入traceid
 * @author eyaomai
 *
 */
public class MdcInterceptor extends HandlerInterceptorAdapter {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    
    MdcUtils.generateTraceId();
    String traceId = MdcUtils.getTraceId();
    logger.debug("set trace ID {} for request {}",
        traceId, request.getRequestURI());

    return true;
  }

  /**
   * remove MDC trace id data .
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    String traceId = MdcUtils.getTraceId();
    MdcUtils.removeTraceId();
    
    logger.debug(
        "remove trace ID {} for request {}", traceId, request.getRequestURI());
  }
  
}
