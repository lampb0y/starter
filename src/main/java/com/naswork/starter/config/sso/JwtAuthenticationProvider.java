package com.naswork.starter.config.sso;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * jwt 认证 提供
 * @author eyaomai
 *
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private String publicKey;
  
  public JwtAuthenticationProvider(String publicKey) {
    this.publicKey = publicKey;
  }
  
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 可以进一步包装，获取更多信息
    return authentication;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }

}
