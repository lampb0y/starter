package com.naswork.starter.config.sso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.config.SSOProperties;
import com.naswork.starter.config.TokenConvertProperties;
import com.naswork.starter.exception.ServerException;
import com.naswork.starter.service.ClientTokenRestService;
import com.naswork.starter.utils.JWTUtils;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.aam.UserRelation;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
import io.jsonwebtoken.Claims;

/**
 * jwt 认证过滤器
 * @author eyaomai
 *
 */
public class MultiSSOAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;
  private final SSOProperties multiSSOProperties;
  @Autowired
  private ClientTokenRestService clientTokenRestService;
  @Value("${aam.url:}")
  protected String aamUrl;
  public MultiSSOAuthenticationFilter(AuthenticationManager authenticationManager,
                                      SSOProperties multiSSOProperties) {
    this.authenticationManager = authenticationManager;
    this.multiSSOProperties = multiSSOProperties;
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
      System.out.println("tokenStr: " + tokenStr );
      if (authHeader != null && authHeader.startsWith(tokenHead)) {

        for (SSOProperties ssoServerProperties: multiSSOProperties.getServers()) {
//            try {
          //采用不同方式去解析token，直至解析一个成功或全部失败
          try {
            claims = JWTUtils.parseJwt(ssoServerProperties.getPublicKey(), tokenStr);
            if (null != claims) {
              JwtAuthenticationToken token =
                  new JwtAuthenticationToken(tokenStr, claims, AuthorityUtils.NO_AUTHORITIES);
              Authentication authentication = authenticationManager.authenticate(token);
              SecurityContextHolder.getContext().setAuthentication(authentication);
              break;
            }
          } catch (Exception e) {
            this.logger.warn("Fail to parse token by fn parseJwt ");
//            this.logger.warn("Fail to parse token by fn parseJwt");
          }


          try {
            claims = JWTUtils.parseJwtFromPublicKey(
                ssoServerProperties.getPublicKey(),
                tokenStr
            );
            if (null != claims) {
              JwtAuthenticationToken token =
                  new JwtAuthenticationToken(tokenStr, claims, AuthorityUtils.NO_AUTHORITIES);
              Authentication authentication = authenticationManager.authenticate(token);
              SecurityContextHolder.getContext().setAuthentication(authentication);
              break;
            }
          } catch (Exception e) {
            this.logger.warn("Fail to parse token by fn parseJwtFromPublicKey ");
//            this.logger.warn("Fail to parse token by fn parseJwt");
          }


          try {
            claims = JWTUtils.parseJwtRS256(ssoServerProperties.getPublicKey(), tokenStr);
            if (null != claims) {
              JwtAuthenticationToken token =
                  new JwtAuthenticationToken(tokenStr, claims, AuthorityUtils.NO_AUTHORITIES);
              Authentication authentication = authenticationManager.authenticate(token);
              SecurityContextHolder.getContext().setAuthentication(authentication);
              break;
            }
          } catch (Exception e) {
            this.logger.warn("Fail to parse token by fn parseJwtRS256 ");
//            this.logger.warn("Fail to parse token by fn parseJwt");
          }



//            } catch (Exception e) {
//              this.logger.error("Fail to parse token ", e);
//            }
        }

      }
    }
    if (claims == null) {
      filterChain.doFilter(request, response);
      return;
    }
//    Authentication authentication = authenticationManager.authenticate(
//        new JwtAuthenticationToken(tokenStr, claims, AuthorityUtils.NO_AUTHORITIES));
//    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }






}
