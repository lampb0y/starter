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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * a configuration to read SecurityProperties from application.yml.
 *
 */

@Component
@Configuration
@ConfigurationProperties(prefix = "sso")
@Validated
public class SSOProperties {

  private static final String TYPE_GENERAL = "general";
  private static final String TYPE_KEYCLOAK = "keycloak";
  @NotNull
  private boolean enabled;
  @NotNull
  private String issuerUri;

  private String publicKey;

  private String tokenUrl;

  private String resource;

  private String credential;

  private String type = TYPE_KEYCLOAK;

  private String scope;

  private List<SSOProperties> servers;

  public List<SSOProperties> getServers() {
    return servers;
  }

  public void setServers(List<SSOProperties> servers) {
    this.servers = servers;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getIssuerUri() {
    return issuerUri;
  }

  public void setIssuerUri(String issuerUri) {
    this.issuerUri = issuerUri;
  }


  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }


  public String getTokenUrl() {
    return tokenUrl;
  }

  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public String getCredential() {
    return credential;
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
