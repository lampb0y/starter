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

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * WebAppConfigurer 类的 configurePathMatch 方法中设置的前缀不会作用到 swagger
 * 因此 swagger 的相关请求地址不需要以 "/api/" + appName + "/" + version 为前缀，
 * 直接访问 http://ip:port/swagger-ui.html 即可。
 * 但在 ErrorPagesInterceptor 类的 preHandle 方法中需允许 swagger 的相关请求地址通过拦截
 *
 * swagger 的相关请求地址大概有这些：/v2/**、/swagger-resources/**、/swagger-ui.html、/webjars/**
 *
 * 线上禁用 swagger 的几种方式（一般用其中一种就够了）：
 * 1. 开发环境的 nginx 暴露 swagger 相关请求地址，而线上的 nginx 屏蔽 swagger 的相关请求地址
 * 2. 在配置类(本类)上使用 @Profile 注解限制 spring.profiles.active = 非dev 时禁用
 * 3. 在配置类(本类)上使用 @ConditionalOnProperty 注解限制不存在 swagger2.enable = true 配置时禁用
 * 4. 在 ErrorPagesInterceptor 类判断是否存在 swagger2.enable = true 的配置，不存在则不允许 swagger 的相关请求地址通过拦截
 * ......
 *
 * 我们采用第 2 种
 */

@Profile({"dev", "test"})
@Configuration
@EnableSwagger2 // 注意该注解不要加在其他位置（如启动类）上
public class SwaggerConfig {

  @Value("${spring.application.name}")
  private String appName;

  @Value("${rest.version}")
  private String version;

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
      .useDefaultResponseMessages(false)
      .apiInfo(apiInfo())
      .select()
      .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
      .paths(PathSelectors.any())
      .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
      .title(appName) // 使用服务名作为文档标题
      .description(appName + " 服务的 API 文档")
      .version(version)
      .build();
  }

}
