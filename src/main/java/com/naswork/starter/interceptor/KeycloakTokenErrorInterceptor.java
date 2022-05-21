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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.exception.UnauthorizedException;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.JsonResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * intercept keycloak token error.
 */
public class KeycloakTokenErrorInterceptor extends HandlerInterceptorAdapter {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final String AUTH_HEADER = "WWW-Authenticate";
  private static final String AUTH_ERROR_PATTERN
      = "Bearer realm=\\\"(.+)\\\", error=\\\"(.+)\\\", error_description=\\\"(.+)\\\"";

  private static final int ERR_DESCRIPTION_INDEX = 3;

  private static final String AUTH_HEADER_MSG = "failed to verify keycloak token";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (request.getDispatcherType() == DispatcherType.ERROR) {
      String responseAuthHeader = response.getHeader(AUTH_HEADER);

      logger.debug(
          "response authentication header = {}", responseAuthHeader);

      if (responseAuthHeader != null) {
        Pattern pattern = Pattern.compile(AUTH_ERROR_PATTERN);
        Matcher matcher = pattern.matcher(responseAuthHeader);
        if (matcher.matches()) {
          String errorMsg = matcher.group(ERR_DESCRIPTION_INDEX);

          if (errorMsg.startsWith("Invalid token issuer.")) {
            // the error message also contains the expected issuer
            // only keep "Invalid token issuer."
            errorMsg = "Invalid token issuer.";
          }

          response.setHeader(AUTH_HEADER, AUTH_HEADER_MSG);
          response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
          response.setStatus(HttpStatus.UNAUTHORIZED.value());

          JsonResult json = JsonResult.build(
              new UnauthorizedException(ErrorEnum.UNAUTH_REQUEST.append(errorMsg)));

          ObjectMapper mapper = new ObjectMapper();
          mapper.writeValue(response.getOutputStream(), json);

          String responseMsg = mapper.writeValueAsString(json);
          logger.error(
              "keycloak token error: {}", responseMsg);
        }
      }

      return false;
    }

    return true;
  }

}
