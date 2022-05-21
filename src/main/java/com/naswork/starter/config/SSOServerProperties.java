//package com.naswork.starter.config;
//
//import javax.validation.constraints.NotNull;
//
///**
// * @Author: ChanningXie
// * @Date: 2020/12/26 19:34
// */
//public class SSOServerProperties {
//
//
//  private static final String TYPE_GENERAL = "general";
//  private static final String TYPE_KEYCLOAK = "keycloak";
//  @NotNull
//  private boolean enabled;
//  @NotNull
//  private String issuerUri;
//
//  private String publicKey;
//
//  private String tokenUrl;
//
//  private String resource;
//
//  private String credential;
//
//  private String type = TYPE_KEYCLOAK;
//
//  private String scope;
//
//
//
//  public boolean isEnabled() {
//    return enabled;
//  }
//
//  public void setEnabled(boolean enabled) {
//    this.enabled = enabled;
//  }
//
//  public String getIssuerUri() {
//    return issuerUri;
//  }
//
//  public void setIssuerUri(String issuerUri) {
//    this.issuerUri = issuerUri;
//  }
//
//
//  public String getPublicKey() {
//    return publicKey;
//  }
//
//  public void setPublicKey(String publicKey) {
//    this.publicKey = publicKey;
//  }
//
//
//  public String getTokenUrl() {
//    return tokenUrl;
//  }
//
//  public void setTokenUrl(String tokenUrl) {
//    this.tokenUrl = tokenUrl;
//  }
//
//  public String getResource() {
//    return resource;
//  }
//
//  public void setResource(String resource) {
//    this.resource = resource;
//  }
//
//  public String getCredential() {
//    return credential;
//  }
//
//  public void setCredential(String credential) {
//    this.credential = credential;
//  }
//
//  public String getType() {
//    return type;
//  }
//
//  public void setType(String type) {
//    this.type = type;
//  }
//
//
//  public String getScope() {
//    return scope;
//  }
//
//  public void setScope(String scope) {
//    this.scope = scope;
//  }
//}
