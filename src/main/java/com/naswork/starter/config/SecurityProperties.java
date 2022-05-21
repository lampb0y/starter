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

import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;

/**
 * a configuration to read SecurityProperties from application.yml.
 *
 */

@Component
@Configuration
@ConfigurationProperties(prefix = "rest.security")
@Validated
public class SecurityProperties {
  @NotNull
  private boolean enabled;
  @NotNull
  private String apiMatcher;
  private Cors cors;
  
  private List<String> whiteList;
  
  /**
   * set a CorsConfiguration entity.
   * @return
   */
  public CorsConfiguration getCorsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
    corsConfiguration.setAllowedMethods(cors.getAllowedMethods());
    corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());
    corsConfiguration.setMaxAge(cors.getMaxAge());

    return corsConfiguration;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getApiMatcher() {
    return apiMatcher;
  }

  public void setApiMatcher(String apiMatcher) {
    this.apiMatcher = apiMatcher;
  }

  public Cors getCors() {
    return cors;
  }

  public void setCors(Cors cors) {
    this.cors = cors;
  }

  public List<String> getWhiteList() {
    return whiteList;
  }

  public void setWhiteList(List<String> whiteList) {
    this.whiteList = whiteList;
  }

  

  /**
   * Cross-origin resource sharing configs.
   */
  public static class Cors {
    @NotNull
    private List<String> allowedOrigins;
    @NotNull
    private List<String> allowedMethods;
    @NotNull
    private List<String> allowedHeaders;
    @NotNull
    private Long maxAge;

    public List<String> getAllowedOrigins() {
      return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
      this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
      return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
      this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
      return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
      this.allowedHeaders = allowedHeaders;
    }

    public Long getMaxAge() {
      return maxAge;
    }

    public void setMaxAge(Long maxAge) {
      this.maxAge = maxAge;
    }
  }
}
