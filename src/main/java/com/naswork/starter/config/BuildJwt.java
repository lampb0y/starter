package com.naswork.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: ChanningXie
 * @Date: 2021/2/26 10:53
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "build-jwt")
@Validated
public class BuildJwt {

  private String name;

  private String id;

  private String resource;

  private String issuerUri;

  private String privateKey;



}
