/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/

package com.naswork.starter.aop;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import com.naswork.starter.config.PermissionAuthorization;

/**
 * override methods to do custom authorization.
 * 
 * @author elngjhx
 */
public class AuthPermissionEvaluator implements PermissionEvaluator {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private HttpServletRequest request;
  
  @Value("${sso.resource}")
  private String clientId;
  
  @Autowired(required = false)
  private PermissionAuthorization permissionAuthorization;
  
  public static final String CLAIM_GROUP = "group";
  public static final String CLAIM_CLIENT = "client";
  public static final String CLAIM_REALM = "realm";
  public static final String CLAIM_CLIENT_PERMISSION_ALL = "clientPermissionAll";
  public static final String CLAIM_CLIENT_PERMISSION_ANY = "clientPermissionAny";

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    Set<String> claimSet = this.extractContentByClaim(authentication, targetType, 
        (String) targetId);
    return this.hasPermission(targetType, claimSet, permission);
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object authenticationTarget,
      Object permissions) {
    Set<String> claimSet = this.extractContentByClaim(authentication, 
        (String) authenticationTarget, this.clientId);
    return this.hasPermission((String) authenticationTarget, claimSet, permissions);
  }
  
  private Set<String> extractContentByClaim(Authentication authentication, 
      String claimType, String resource) {
    
    SimpleKeycloakAccount account =
        (SimpleKeycloakAccount) ((KeycloakAuthenticationToken) authentication).getDetails();
    AccessToken token = account.getKeycloakSecurityContext().getToken();
    if (claimType.equals(CLAIM_REALM)) {
      if (token.getRealmAccess() != null) {
        return token.getRealmAccess().getRoles();
      }
    } else if (claimType.equals(CLAIM_CLIENT) || 
        claimType.equals(CLAIM_CLIENT_PERMISSION_ALL) ||
        claimType.equals(CLAIM_CLIENT_PERMISSION_ANY)) {
      if (resource != null) {
        Access access = token.getResourceAccess(resource);
        if (access != null) {
          return access.getRoles();
        }
      } else {
        Collection<Access> accessCollection = token.getResourceAccess().values();
        if (accessCollection != null && !accessCollection.isEmpty()) {
          Set<String> claimSet = new HashSet();
          for (Access access: accessCollection) {
            claimSet.addAll(access.getRoles());
          }
          return claimSet;
        }
      }
    } else if (claimType.equals(CLAIM_GROUP)) {
      if (token.getOtherClaims() != null && token.getOtherClaims().containsKey(CLAIM_GROUP)) {
        Set<String> claimSet = new HashSet();
        claimSet.addAll((List) token.getOtherClaims().get(CLAIM_GROUP));
        return claimSet;
      }
      
    }
    return null;
  }
  
  private boolean hasPermission(String targetType, Set<String> claimSet, Object target) {
    if (StringUtils.isEmpty(target)) {
      return false;
    }
    if (claimSet == null || claimSet.isEmpty()) {
      return false;
    }
    String[] itemList = ((String) target).split(",");
    if (targetType.equals(CLAIM_REALM) || 
        targetType.equals(CLAIM_GROUP) ||
        targetType.equals(CLAIM_CLIENT)) {
      for (String item: itemList) {
        if (claimSet.contains(item)) {
          return true;
        }
      }
      return false;
    } else if (targetType.equals(CLAIM_CLIENT_PERMISSION_ALL)) {
      if (this.permissionAuthorization == null) {
        return false;
      }
      return this.permissionAuthorization.hasAllPermission(
          new ArrayList<String>(claimSet), Arrays.asList(itemList));
    } else if (targetType.equals(CLAIM_CLIENT_PERMISSION_ANY)) {
      if (this.permissionAuthorization == null) {
        return false;
      }
      return this.permissionAuthorization.hasAnyPermission(
          new ArrayList<String>(claimSet), Arrays.asList(itemList));
    }
    return false;
  }
  
}
