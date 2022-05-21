package com.naswork.starter.config.sso;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.naswork.starter.utils.JWTUtils;

/**
import io.jsonwebtoken.Claims;

/**
 * jwt 认证过滤器
 * @author eyaomai
 *
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;
  private final String publicKey;
  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
      String publicKey) {
    this.authenticationManager = authenticationManager;
    this.publicKey = publicKey;
  }
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String authParam = request.getParameter("access_token");
    String tokenHead = "Bearer ";

    Claims claims = null;
    String tokenStr = null;
    if (authHeader == null && authParam != null) {
      authHeader = tokenHead + authParam;
    }
    if (authHeader != null) {
      tokenStr = authHeader.substring(tokenHead.length());
      if (authHeader != null && authHeader.startsWith(tokenHead)) {
        try {
          claims = JWTUtils.parseJwt(publicKey, tokenStr);
          //claims = JWTUtils.parseJwtFromPublicKey(publicKey, tokenStr);
        } catch (Exception e) {
          this.logger.error("Fail to parse token ", e);
        }
      }
    }
    if (claims == null) {
      filterChain.doFilter(request, response);
      return;
    }
    Authentication authentication = authenticationManager.authenticate(
        new JwtAuthenticationToken(tokenStr, claims, AuthorityUtils.NO_AUTHORITIES));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

}
