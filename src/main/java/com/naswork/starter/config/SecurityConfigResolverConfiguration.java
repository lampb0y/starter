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

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * In Keycloak version 7.0.0, define a bean of type {@link KeycloakSpringBootConfigResolver}
 * in {@link KactSecurityConfig} will cause an error "Requested bean is currently
 * in creation: Is there an unresolvable circular reference". To fix this issue,
 * define the bean in a separated class.
 */
@Configuration
public class SecurityConfigResolverConfiguration {

  SecurityConfigResolverConfiguration() {
  }

  @Bean
  public KeycloakSpringBootConfigResolver keycloakSpringBootConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }
}
