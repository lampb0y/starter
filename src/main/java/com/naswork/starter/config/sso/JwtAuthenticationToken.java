package com.naswork.starter.config.sso;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;

/**
 * jwt 认证token
 * @author eyaomai
 *
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  
  /**
   * 
   */
  private static final long serialVersionUID = -936340403304957266L;

  public JwtAuthenticationToken(String tokenStr, Claims jwtClaims,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.tokenStr = tokenStr;
    this.jwtClaims = jwtClaims;
    this.principal = new JwtPrincipal(this.jwtClaims.getSubject());
  }

  private String tokenStr;
  
  private Principal principal;
  
  private Claims jwtClaims;

  private String shadowId;

  public String getShadowId() {
    return shadowId;
  }

  public void setShadowId(String shadowId) {
    this.shadowId = shadowId;
  }

  @Override
  public Object getCredentials() {
    return this.jwtClaims;
  }
  
  public String getTokenStr() {
    return this.tokenStr;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  class JwtPrincipal implements Principal {

    private String name;
    public JwtPrincipal(String name) {
      this.name = name;
    }
    @Override
    public String getName() {
      return this.name;
    }
    
  }
  
}
