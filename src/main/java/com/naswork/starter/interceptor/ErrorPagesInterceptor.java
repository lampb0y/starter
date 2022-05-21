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
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * intercept error pages.
 */
public class ErrorPagesInterceptor extends HandlerInterceptorAdapter {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private String uriRegex = "^/api/[^//]+/v[0-9]{1,}/.*";
  private String swaggerReg = "^/v2/.*|^/swagger.*|/webjars/.*";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {
    String uri = request.getRequestURI();
    if (uri.matches(uriRegex) || uri.matches(swaggerReg)) {
      return true;
    } else {
      logger.error("URI {} not found", uri);
      response.setStatus(HttpStatus.NOT_FOUND.value());
      return false;
    }
  }

}
