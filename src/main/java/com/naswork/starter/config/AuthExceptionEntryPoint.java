/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.exception.UnauthorizedException;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.JsonResult;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * handle exception thrown by Oauth2Exception when validate token.
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws ServletException, IOException {

    JsonResult json = JsonResult.build(new UnauthorizedException(ErrorEnum.UNAUTH_REQUEST
        .append(authException.getMessage().replace("Cannot convert access token to JSON",
            "Failed to convert access token to JSON format"))));
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), json);
    logger.error("{}",
        mapper.writeValueAsString(json));
  }
}
