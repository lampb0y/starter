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



import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.naswork.starter.contoller.BaseController;
import com.naswork.starter.interceptor.ErrorPagesInterceptor;
import com.naswork.starter.interceptor.KeycloakTokenErrorInterceptor;
import com.naswork.starter.interceptor.MdcInterceptor;

/**
 * set up interceptor.
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Value("${spring.application.name}")
  protected String appName;

  @Value("${rest.version}")
  protected String version;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new MdcInterceptor());
    registry.addInterceptor(new KeycloakTokenErrorInterceptor());
    registry.addInterceptor(new ErrorPagesInterceptor());
  }

  /**
   * Configure the path to limit the rest api path
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    Predicate<Class<?>> predicate =
        HandlerTypePredicate.forAnnotation(RestController.class)
                .and(HandlerTypePredicate.forBasePackage("com.naswork"));
    String prefix = "/api/" + appName + "/" + version;
    configurer.addPathPrefix(prefix, predicate);
  }
}
